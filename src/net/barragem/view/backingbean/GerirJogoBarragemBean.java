package net.barragem.view.backingbean;

import java.util.Collections;
import java.util.Iterator;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.JogadorJogo;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Parcial;
import net.barragem.persistence.entity.Rodada;
import net.barragem.persistence.entity.SimplesDuplasEnum;
import net.barragem.util.PersistenceHelper;
import net.barragem.view.backingbean.componentes.MestreDetalheImpl;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GerirJogoBarragemBean extends BaseBean {

	private MestreDetalheImpl<Rodada, JogoBarragem> mestreDetalhe = new MestreDetalheImpl<Rodada, JogoBarragem>();

	public MestreDetalheImpl<Rodada, JogoBarragem> getMestreDetalhe() {
		return mestreDetalhe;
	}

	public void setMestreDetalhe(MestreDetalheImpl<Rodada, JogoBarragem> mestreDetalhe) {
		this.mestreDetalhe = mestreDetalhe;
	}

	public void carregaJogo(Rodada rodada, int index) {
		preparaJogoParaEdicao(rodada.getJogos().get(index));
	}

	public void adicionaJogo(Rodada rodada) {
		preparaJogoParaEdicao(rodada.criaJogoBarragem(null, null));
	}

	public void editaProximoJogo(ActionEvent e) {
		Integer i = mestreDetalhe.getIndiceNosDetalhes();
		if (i != null && i.intValue() + 1 < mestreDetalhe.getDetalhes().size()) {
			preparaJogoParaEdicao(mestreDetalhe.getDetalhes().get(i.intValue() + 1));
		}
	}

	public void editaJogoAnterior(ActionEvent e) {
		Integer i = mestreDetalhe.getIndiceNosDetalhes();
		if (i != null && i.intValue() - 1 >= 0) {
			preparaJogoParaEdicao(mestreDetalhe.getDetalhes().get(i.intValue() - 1));
		}
	}

	private void preparaJogoParaEdicao(JogoBarragem jogo) {
		mestreDetalhe.setDetalheEmFoco((JogoBarragem) jogo.clone());
		mestreDetalhe.setMestre(mestreDetalhe.getDetalheEmFoco().getRodada());
		mestreDetalhe.setDetalhes(mestreDetalhe.getMestre().getJogos());

		Collections.sort(mestreDetalhe.getDetalheEmFoco().getJogadoresEventos(),
				new JogadorEventoComparatorVencedorPrimeiro());

		completaSetsSeNecessario();
	}

	private void completaSetsSeNecessario() {
		for (int i = 0; i < mestreDetalhe.getMestre().getCiclo().getParametros().getModalidadeDeSets()
				.getNumeroDeSets(); i++) {
			if (i >= mestreDetalhe.getDetalheEmFoco().getPlacar().getParciais().size()) {
				Parcial parcial = new Parcial();
				parcial.setPlacar(mestreDetalhe.getDetalheEmFoco().getPlacar());
				mestreDetalhe.getDetalheEmFoco().getPlacar().getParciais().add(parcial);
			}
		}
	}

	public void adicionaParcial(ActionEvent e) {
		Parcial parcial = new Parcial();
		parcial.setPlacar(mestreDetalhe.getDetalheEmFoco().getPlacar());
		mestreDetalhe.getDetalheEmFoco().getPlacar().getParciais().add(parcial);
	}

	public void marcaVencedorAEsquerda(ActionEvent e) {
		((JogadorJogo) mestreDetalhe.getDetalheEmFoco().getJogadoresEventos().get(1)).setVencedor(Boolean.FALSE);
	}

	public void marcaVencedorADireita(ActionEvent e) {
		((JogadorJogo) mestreDetalhe.getDetalheEmFoco().getJogadoresEventos().get(0)).setVencedor(Boolean.FALSE);
	}

	public void salvaJogo(ActionEvent e) {
		for (JogadorEvento jogadorEvento : mestreDetalhe.getDetalheEmFoco().getJogadoresEventos()) {
			if (mestreDetalhe.getMestre().getJogadorJogoBarragem(jogadorEvento.getJogador()) != null
					&& mestreDetalhe.getMestre().getJogadorJogoBarragem(jogadorEvento.getJogador()).getEvento().getId() != null
					&& !mestreDetalhe.getMestre().getJogadorJogoBarragem(jogadorEvento.getJogador()).getEvento()
							.getId().equals(mestreDetalhe.getDetalheEmFoco().getId())) {
				messages.addErrorMessage(null, "error_jogador_ja_jogou_rodada", jogadorEvento.getJogador().getNome());
			}
		}
		if (!messages.getErrorMessages().isEmpty()) {
			return;
		}

		preparaJogoParaAtualizacao();

		PersistenceHelper.persiste(mestreDetalhe.getDetalheEmFoco());

		preparaJogoParaEdicao(mestreDetalhe.getDetalheEmFoco());

		for (Jogo jogo : mestreDetalhe.getDetalhes()) {
			Collections.sort(jogo.getJogadoresEventos(), new JogadorEventoComparatorVencedorPrimeiro());
		}
		Collections.sort(mestreDetalhe.getDetalhes(), new JogoBarragemComparator());
	}

	private void preparaJogoParaAtualizacao() {
		Parcial parcial = null;
		for (Iterator<Parcial> it = mestreDetalhe.getDetalheEmFoco().getPlacar().getParciais().iterator(); it.hasNext();) {
			parcial = it.next();
			if (parcial.getParcialVencedor() == null || parcial.getParcialPerdedor() == null
					|| mestreDetalhe.getDetalheEmFoco().getPlacar().getWo()) {
				if (parcial.getId() != null) {
					parcial.setPlacar(null);
					PersistenceHelper.remove(parcial);
				}
				it.remove();
			}
		}

		if (mestreDetalhe.getDetalheEmFoco().getTipo().equals(SimplesDuplasEnum.Simples)) {
			if (((JogadorJogo) mestreDetalhe.getDetalheEmFoco().getJogadoresEventos().get(1)).getVencedor()) {
				mestreDetalhe.getDetalheEmFoco().inverteParciaisVencedorasEPerdedoras();
			}
		}

		if (mestreDetalhe.getDetalhes().contains(mestreDetalhe.getDetalheEmFoco())) {
			mestreDetalhe.getDetalhes().remove(mestreDetalhe.getDetalheEmFoco());
		}
		mestreDetalhe.getDetalhes().add(mestreDetalhe.getDetalheEmFoco());
	}
}