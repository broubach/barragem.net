package net.barragem.view.backingbean.componentes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.JogadorJogo;
import net.barragem.persistence.entity.JogadorJogoBarragem;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Parcial;
import net.barragem.persistence.entity.Rodada;
import net.barragem.persistence.entity.SimplesDuplasEnum;
import net.barragem.util.PersistenceHelper;
import net.barragem.view.exception.BusinessException;

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

	public void ordena() {
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
		marcaVencedor();
		removeSetsIncompletos();
		inverteParciaisVencedorasEPerdadorasSeNecessario();
		adicionaDetalheNaLista();
	}

	private void marcaVencedor() {
		Jogador vencedor;
		try {
			vencedor = obtemVencedor();
		} catch (BusinessException e) {
			// excessao nao pode ocorrer, pois verificacao jah ocorreu. Se
			// ocorrer, que comprometa todo o sistema para avisar que existe
			// algo de errado
			throw new RuntimeException(e);
		}
		JogadorJogoBarragem jogadorJogoBarragem = null;
		for (int i = 0; i < getDetalheEmFoco().getJogadoresEventos().size(); i++) {
			jogadorJogoBarragem = (JogadorJogoBarragem) getDetalheEmFoco().getJogadoresEventos().get(i);
			if (jogadorJogoBarragem.getJogador() == vencedor) {
				jogadorJogoBarragem.setVencedor(true);
			} else {
				jogadorJogoBarragem.setVencedor(false);
			}
		}
	}

	public Jogador obtemVencedor() throws BusinessException {
		if (getDetalheEmFoco().getPlacar().getWo() && jogadorVencedorWo == null) {
			throw new BusinessException("wo", "label_true");
		} else if (getDetalheEmFoco().getPlacar().getWo() && jogadorVencedorWo != null) {
			return jogadorVencedorWo;
		}
		Map<Jogador, Integer> totalSetsVencidos = new HashMap<Jogador, Integer>();
		Jogador teoricoVencedor = getDetalheEmFoco().getJogadoresEventos().get(0).getJogador();
		Jogador teoricoPerdedor = getDetalheEmFoco().getJogadoresEventos().get(1).getJogador();
		totalSetsVencidos.put(teoricoVencedor, new Integer(0));
		totalSetsVencidos.put(teoricoPerdedor, new Integer(0));
		Parcial parcial = null;
		for (int i = 0; i < getDetalheEmFoco().getPlacar().getParciais().size(); i++) {
			parcial = getDetalheEmFoco().getPlacar().getParciais().get(i);
			if ((parcial.getParcialVencedor() == null || parcial.getParcialPerdedor() == null) && i > 0) {
				continue;
			} else if (parcial.getParcialVencedor() == null || parcial.getParcialPerdedor() == null) {
				throw new BusinessException("primeiro_set", "label_true");
			}
			if (parcial.getParcialVencedor() > parcial.getParcialPerdedor()) {
				totalSetsVencidos.put(teoricoVencedor, totalSetsVencidos.get(teoricoVencedor) + 1);
			} else if (parcial.getParcialPerdedor() > parcial.getParcialVencedor()) {
				totalSetsVencidos.put(teoricoPerdedor, totalSetsVencidos.get(teoricoPerdedor) + 1);
			}
		}
		if (totalSetsVencidos.get(teoricoVencedor) > totalSetsVencidos.get(teoricoPerdedor)) {
			return teoricoVencedor;
		}
		if (totalSetsVencidos.get(teoricoPerdedor) > totalSetsVencidos.get(teoricoVencedor)) {
			return teoricoPerdedor;
		}

		throw new BusinessException(null, "error_jogo_nao_possui_vencedor");
	}

	private void removeSetsIncompletos() {
		Parcial parcial = null;
		for (Iterator<Parcial> it = getDetalheEmFoco().getPlacar().getParciais().iterator(); it.hasNext();) {
			parcial = it.next();
			if (parcial.getParcialVencedor() == null || parcial.getParcialPerdedor() == null
					|| getDetalheEmFoco().getPlacar().getWo()) {
				if (parcial.getId() != null) {
					parcial.setPlacar(null);
					PersistenceHelper.remove(parcial);
				}
				it.remove();
			}
		}
	}

	private void inverteParciaisVencedorasEPerdadorasSeNecessario() {
		if (getDetalheEmFoco().getTipo().equals(SimplesDuplasEnum.Simples)) {
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