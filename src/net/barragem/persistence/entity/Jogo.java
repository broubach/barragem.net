package net.barragem.persistence.entity;

import java.util.Iterator;

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
			if (((JogadorJogo) jogadorEvento).getVencedor() == null || ((JogadorJogo) jogadorEvento).getVencedor()) {
				vencedores.append(jogadorEvento.getJogador().getNome().trim()).append(", ");
			} else {
				perdedores.append(jogadorEvento.getJogador().getNome().trim()).append(", ");
			}
		}

		vencedores.delete(vencedores.length() - 2, vencedores.length());
		if (perdedores.length() >= 2) {
			perdedores.delete(perdedores.length() - 2, perdedores.length());
			return vencedores.append(" X ").append(perdedores).toString();
		}
		return vencedores.toString();
	}
}