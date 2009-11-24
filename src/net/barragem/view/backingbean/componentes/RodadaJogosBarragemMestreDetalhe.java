package net.barragem.view.backingbean.componentes;

import java.util.Collections;
import java.util.Iterator;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.JogadorJogo;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Parcial;
import net.barragem.persistence.entity.Placar;
import net.barragem.persistence.entity.Rodada;
import net.barragem.persistence.entity.SimplesDuplasEnum;
import net.barragem.util.PersistenceHelper;

public class RodadaJogosBarragemMestreDetalhe extends MestreDetalheImpl<Rodada, JogoBarragem> {

	public Jogador jogadorVencedorWo;

	public Jogador getJogadorVencedorWo() {
		return jogadorVencedorWo;
	}

	public void setJogadorVencedorWo(Jogador jogadorVencedorWo) {
		this.jogadorVencedorWo = jogadorVencedorWo;
	}

	public void preparaJogoParaEdicao(int index) {
		preparaJogoParaEdicao(getDetalhes().get(index));
	}

	public void preparaJogoParaEdicao() {
		preparaJogoParaEdicao(getDetalheEmFoco());
	}

	public void preparaJogoParaEdicao(JogoBarragem jogoBarragem) {
		setDetalheEmFoco(jogoBarragem);
		setMestre(getDetalheEmFoco().getRodada());
		setDetalhes(getMestre().getJogos());

		ordena();
		completaSetsSeNecessario();
	}

	private void ordena() {
		for (Jogo jogo : getDetalhes()) {
			Collections.sort(jogo.getJogadoresEventos(), new JogadorEventoComparatorVencedorPrimeiro());
		}
		Collections.sort(getDetalhes(), new JogoBarragemComparator());
	}

	private void completaSetsSeNecessario() {
		for (int i = 0; i < getMestre().getCiclo().getParametros().getModalidadeDeSets().getNumeroDeSets(); i++) {
			if (i >= getDetalheEmFoco().getPlacar().getParciais().size()) {
				Parcial parcial = new Parcial();
				parcial.setPlacar(getDetalheEmFoco().getPlacar());
				getDetalheEmFoco().getPlacar().getParciais().add(parcial);
			}
		}
	}

	public void editaProximoJogo() {
		Integer i = getIndiceNosDetalhes();
		if (i != null && i.intValue() + 1 < getDetalhes().size()) {
			preparaJogoParaEdicao(i.intValue() + 1);
		}
	}

	public void editaJogoAnterior() {
		Integer i = getIndiceNosDetalhes();
		if (i != null && i.intValue() - 1 >= 0) {
			preparaJogoParaEdicao(i.intValue() - 1);
		}
	}

	public void preparaJogoParaAtualizacao() {
		Jogador vencedor = getDetalheEmFoco().obtemVencedor(getJogadorVencedorWo());
		if (vencedor != null) {
			getDetalheEmFoco().marcaVencedor(vencedor);
			inverteParciaisVencedorasEPerdadorasSeNecessario();
		} else {
			getDetalheEmFoco().desmarcaVencedor();
		}
		removeSetsIncompletos(getDetalheEmFoco().getPlacar());
		adicionaDetalheNaLista();
	}

	private void removeSetsIncompletos(Placar r) {
		Parcial parcial = null;
		for (Iterator<Parcial> it = r.getParciais().iterator(); it.hasNext();) {
			parcial = it.next();
			if (parcial.getParcialVencedor() == null || parcial.getParcialPerdedor() == null || r.getWo()) {
				if (parcial.getId() != null) {
					parcial.setPlacar(null);
					PersistenceHelper.remove(parcial);
				}
				it.remove();
			}
		}
	}

	private void inverteParciaisVencedorasEPerdadorasSeNecessario() {
		if (getDetalheEmFoco().getTipo().equals(SimplesDuplasEnum.Simples) && !getDetalheEmFoco().getPlacar().getWo()) {
			if (((JogadorJogo) getDetalheEmFoco().getJogadoresEventos().get(1)).getVencedor()) {
				getDetalheEmFoco().inverteParciaisVencedorasEPerdedoras();
			}
		}
	}

	private void adicionaDetalheNaLista() {
		if (getDetalhes().contains(getDetalheEmFoco())) {
			getDetalhes().remove(getDetalheEmFoco());
		}
		getDetalhes().add(getDetalheEmFoco());
	}
}