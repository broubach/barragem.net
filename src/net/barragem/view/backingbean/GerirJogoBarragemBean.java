package net.barragem.view.backingbean;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Parcial;
import net.barragem.persistence.entity.Rodada;
import net.barragem.util.PersistenceHelper;
import net.barragem.view.backingbean.componentes.MestreDetalheImpl;
import net.barragem.view.backingbean.componentes.RodadaJogosBarragemMestreDetalhe;
import net.barragem.view.exception.BusinessException;

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

	public void adicionaParcial(ActionEvent e) {
		Parcial parcial = new Parcial();
		parcial.setPlacar(mestreDetalhe.getDetalheEmFoco().getPlacar());
		mestreDetalhe.getDetalheEmFoco().getPlacar().getParciais().add(parcial);
	}

	public void salvaJogo(ActionEvent e) {
		if (valida(mestreDetalhe)) {
			mestreDetalhe.preparaJogoParaAtualizacao();

			PersistenceHelper.persiste(mestreDetalhe.getDetalheEmFoco());

			mestreDetalhe.preparaJogoParaEdicao();

			messages.addInfoMessage(null, "label_informacao_atualizada_com_sucesso");
		}
	}

	private boolean valida(RodadaJogosBarragemMestreDetalhe mestreDetalhe) {
		validaJogadorJahJogouNaRodada(mestreDetalhe);
		validaExisteVencedor(mestreDetalhe);
		if (!messages.getErrorMessages().isEmpty()) {
			return false;
		}
		return true;
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

	private void validaExisteVencedor(RodadaJogosBarragemMestreDetalhe mestreDetalhe) {
		try {
			mestreDetalhe.obtemVencedor();
		} catch (BusinessException e) {
			messages.addErrorMessage(e.getClientId(), e.getMessageKey());
		}
	}
}