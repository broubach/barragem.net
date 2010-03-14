package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Rodada;
import net.barragem.persistence.entity.SimplesDuplasEnum;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.MestreDetalheImpl;
import net.barragem.view.backingbean.componentes.RodadaJogosBarragemMestreDetalhe;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GerirJogoBarragemBean extends BaseBean {

	public RodadaJogosBarragemMestreDetalhe mestreDetalhe = new RodadaJogosBarragemMestreDetalhe();

	public RodadaJogosBarragemMestreDetalhe getMestreDetalhe() {
		return mestreDetalhe;
	}

	public void setMestreDetalhe(RodadaJogosBarragemMestreDetalhe mestreDetalhe) {
		this.mestreDetalhe = mestreDetalhe;
	}

	public void carregaJogo(Rodada rodada, int index) {
		mestreDetalhe.preparaJogoParaEdicao(rodada.getJogos().get(index));
	}

	public void adicionaJogo(Rodada rodada) {
		mestreDetalhe.preparaJogoParaEdicao(rodada.criaJogoBarragem(null, null));
	}

	public void editaProximoJogo(ActionEvent e) {
		mestreDetalhe.editaProximoJogo();
	}

	public void editaJogoAnterior(ActionEvent e) {
		mestreDetalhe.editaJogoAnterior();
	}

	public void salvaJogo(ActionEvent e) {
		if (valida(mestreDetalhe)) {
			mestreDetalhe.preparaJogoParaAtualizacao();

			PersistenceHelper.persiste(mestreDetalhe.getDetalheEmFoco());

			PersistenceHelper.persiste(getContaUsuario().criaOperacaoDebitoJogoBarragem(1));
			PersistenceHelper.persiste(getContaUsuario());

			mestreDetalhe.preparaJogoParaEdicao();

			addMensagemAtualizacaoComSucesso();
		}
	}

	private boolean valida(RodadaJogosBarragemMestreDetalhe mestreDetalhe) {
		validaCamposObrigatorios(mestreDetalhe.getDetalheEmFoco());
		validaJogadorJahJogouNaRodada(mestreDetalhe);
		if (!messages.getErrorMessages().isEmpty()) {
			return false;
		}
		return true;
	}

	private void validaCamposObrigatorios(JogoBarragem jogoBarragem) {
		super.valida(jogoBarragem);
		if (jogoBarragem.getTipo().equals(SimplesDuplasEnum.Simples)) {
			Jogador jogador1 = jogoBarragem.getJogadoresEventos().get(0).getJogador();
			Jogador jogador2 = jogoBarragem.getJogadoresEventos().get(1).getJogador();
			if (jogador1 != null && jogador2 != null && jogador1.equals(jogador2)) {
				messages.addErrorMessage(null, "error_jogadores_selecionados_devem_ser_diferentes");
			}
			if (jogador1 == null || jogador2 == null) {
				messages.addErrorMessage("jogador", "label_true");
			}
		}
	}

	private void validaJogadorJahJogouNaRodada(MestreDetalheImpl<Rodada, JogoBarragem> mestreDetalhe) {
		for (JogadorEvento jogadorEvento : mestreDetalhe.getDetalheEmFoco().getJogadoresEventos()) {
			if (mestreDetalhe.getMestre().getJogadorJogoBarragem(jogadorEvento.getJogador()) != null
					&& mestreDetalhe.getMestre().getJogadorJogoBarragem(jogadorEvento.getJogador()).getEvento().getId() != null
					&& !mestreDetalhe.getMestre().getJogadorJogoBarragem(jogadorEvento.getJogador()).getEvento()
							.getId().equals(mestreDetalhe.getDetalheEmFoco().getId())) {
				messages.addErrorMessage(null, "error_jogador_ja_jogou_rodada", jogadorEvento.getJogador().getNome());
			}
		}
	}

	public List<SelectItem> getListaJogadoresSelecionados() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		JogadorEvento jogadorEvento = null;
		if (mestreDetalhe.getDetalheEmFoco() == null) {
			return items;
		}
		for (Iterator<JogadorEvento> it = mestreDetalhe.getDetalheEmFoco().getJogadoresEventos().iterator(); it
				.hasNext();) {
			jogadorEvento = it.next();
			if (jogadorEvento.getJogador() != null) {
				SelectItem selectItem = new SelectItem(jogadorEvento.getJogador(), jogadorEvento.getJogador().getNome());
				items.add(selectItem);
			}
		}
		return items;
	}
}