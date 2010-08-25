package net.barragem.persistence.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import net.barragem.scaffold.MessageBundleUtils;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "jogo")
public class Jogo extends Evento {

	@OneToOne(cascade = CascadeType.ALL)
	private Placar placar;
	private SimplesDuplasEnum tipo = SimplesDuplasEnum.Simples;

	public Placar getPlacar() {
		return placar;
	}

	public void setPlacar(Placar placar) {
		this.placar = placar;
	}

	public SimplesDuplasEnum getTipo() {
		return tipo;
	}

	public void setTipo(SimplesDuplasEnum tipo) {
		this.tipo = tipo;
	}

	public Jogador getJogadorVencedorSimples() {
		return getJogadorBarragemVencedorSimples() != null ? getJogadorBarragemVencedorSimples().getJogador() : null;
	}

	public Jogador getJogadorPerdedorSimples() {
		return getJogadorBarragemPerdedorSimples() != null ? getJogadorBarragemPerdedorSimples().getJogador() : null;
	}

	public void inverteParciaisVencedorasEPerdedoras() {
		Parcial parcial = null;
		int aux = 0;
		for (Iterator<Parcial> it = getPlacar().getParciais().iterator(); it.hasNext();) {
			parcial = it.next();
			aux = parcial.getParcialPerdedor();
			parcial.setParcialPerdedor(parcial.getParcialVencedor());
			parcial.setParcialVencedor(aux);
		}
	}

	public JogadorJogo getJogadorJogoVencedorSimples() {
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (((JogadorJogo) jogadorEvento).getVencedor()) {
				return (JogadorJogo) jogadorEvento;
			}
		}
		return null;
	}

	public JogadorJogo getJogadorJogoPerdedorSimples() {
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (!((JogadorJogo) jogadorEvento).getVencedor()) {
				return (JogadorJogo) jogadorEvento;
			}
		}
		return null;
	}

	public JogadorJogoBarragem getJogadorBarragemVencedorSimples() {
		return (JogadorJogoBarragem) getJogadorJogoVencedorSimples();
	}

	public JogadorJogoBarragem getJogadorBarragemPerdedorSimples() {
		return (JogadorJogoBarragem) getJogadorJogoPerdedorSimples();
	}

	@Override
	public void cloneTo(Object e) {
		super.cloneTo(e);
		((Jogo) e).setPlacar(placar);
		((Jogo) e).setTipo(tipo);
	}

	@Override
	public String getTipoStr() {
		return MessageBundleUtils.getInstance().get("label_jogo_avulso");
	}

	@Override
	public String getJogadoresStr() {
		StringBuilder vencedores = new StringBuilder();
		StringBuilder perdedores = new StringBuilder();
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (((JogadorJogo) jogadorEvento).getVencedor() != null && ((JogadorJogo) jogadorEvento).getVencedor()) {
				vencedores.append(jogadorEvento.getJogador().getNome().trim()).append(", ");
			} else {
				perdedores.append(jogadorEvento.getJogador().getNome().trim()).append(", ");
			}
		}
		// TODO: se for duplas, nao serah possivel agrupar jogadores do mesmo
		// time
		if (vencedores.length() <= 0) {
			vencedores = new StringBuilder();
			perdedores = new StringBuilder();
			for (int i = 0; i < getJogadoresEventos().size() / 2; i++) {
				vencedores.append(getJogadoresEventos().get(i).getJogador().getNome().trim()).append(", ");
			}
			for (int i = getJogadoresEventos().size() / 2; i < getJogadoresEventos().size(); i++) {
				perdedores.append(getJogadoresEventos().get(i).getJogador().getNome().trim()).append(", ");
			}
		}
		vencedores.delete(vencedores.length() - ", ".length(), vencedores.length());
		perdedores.delete(perdedores.length() - ", ".length(), perdedores.length());
		return vencedores.append(" X ").append(perdedores).toString();
	}

	@Override
	public String getResultadoStr() {
		if (getUsuarioLogado() == null) {
			throw new IllegalStateException();
		}
		return isUsuarioLogadoVencedor() ? MessageBundleUtils.getInstance().get("label_vitoria")
				: isUsuarioLogadoPerdedor() ? MessageBundleUtils.getInstance().get("label_derrota")
						: MessageBundleUtils.getInstance().get("label_indefinido");
	}

	public void calculaVencedorEInverteParciaisSeNecessario(Jogador jogadorVencedorWo) {
		Jogador vencedor = calculaVencedor(jogadorVencedorWo);
		if (vencedor != null) {
			marcaVencedor(vencedor);
			inverteParciaisVencedorasEPerdadorasSeNecessario();
		} else {
			desmarcaVencedor();
		}
	}

	private void inverteParciaisVencedorasEPerdadorasSeNecessario() {
		if (tipo.equals(SimplesDuplasEnum.Simples) && !placar.getWo()) {
			if (((JogadorJogo) getJogadoresEventos().get(1)).getVencedor()) {
				Collections.reverse(getJogadoresEventos());
				inverteParciaisVencedorasEPerdedoras();
			}
		}
	}

	public Jogador calculaVencedor(Jogador jogadorVencedorWo) {
		if (getPlacar().getWo() && jogadorVencedorWo == null) {
			return null;
		} else if (getPlacar().getWo() && jogadorVencedorWo != null) {
			return jogadorVencedorWo;
		}
		Map<Jogador, Integer> totalSetsVencidos = new HashMap<Jogador, Integer>();
		Jogador teoricoVencedor = getJogadoresEventos().get(0).getJogador();
		Jogador teoricoPerdedor = getJogadoresEventos().get(1).getJogador();
		totalSetsVencidos.put(teoricoVencedor, new Integer(0));
		totalSetsVencidos.put(teoricoPerdedor, new Integer(0));
		Parcial parcial = null;
		for (int i = 0; i < getPlacar().getParciais().size(); i++) {
			parcial = getPlacar().getParciais().get(i);
			if (parcial.getParcialVencedor() == null || parcial.getParcialPerdedor() == null) {
				continue;
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

		return null;
	}

	public void marcaVencedor(Jogador vencedor) {
		JogadorJogo jogadorJogoBarragem = null;
		for (int i = 0; i < getJogadoresEventos().size(); i++) {
			jogadorJogoBarragem = (JogadorJogo) getJogadoresEventos().get(i);
			if (jogadorJogoBarragem.getJogador() == vencedor) {
				jogadorJogoBarragem.setVencedor(true);
			} else {
				jogadorJogoBarragem.setVencedor(false);
			}
		}
	}

	public void desmarcaVencedor() {
		JogadorJogo jogadorJogo = null;
		for (int i = 0; i < getJogadoresEventos().size(); i++) {
			jogadorJogo = (JogadorJogo) getJogadoresEventos().get(i);
			jogadorJogo.setVencedor(false);
		}
	}

	public boolean possuiVencedores() {
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (((JogadorJogo) jogadorEvento).getVencedor()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isUsuarioLogadoVencedor() {
		JogadorEvento jogadorEvento = getJogadorEventoUsuarioLogado();
		return ((JogadorJogo) jogadorEvento).getVencedor();
	}

	@Override
	public boolean isUsuarioLogadoPerdedor() {
		JogadorEvento jogadorEvento = getJogadorEventoUsuarioLogado();
		if (!((JogadorJogo) jogadorEvento).getVencedor() && ((Jogo) this).possuiVencedores()) {
			return true;
		}
		return false;
	}

}