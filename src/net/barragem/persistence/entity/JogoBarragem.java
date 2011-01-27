package net.barragem.persistence.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import net.barragem.scaffold.MessageBundleUtils;
import net.barragem.scaffold.ValidatableSampleImpl;

@Entity
@Table(name = "jogobarragem")
@PrimaryKeyJoinColumn(name = "id")
public class JogoBarragem extends Jogo implements Validatable {

	@ManyToOne
	private Rodada rodada;

	public Rodada getRodada() {
		return rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
	}

	public JogadorJogoBarragem getJogadorEvento(Jogador jogador) {
		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			if (jogadorEvento.getJogador().equals(jogador)) {
				return (JogadorJogoBarragem) jogadorEvento;
			}
		}
		return null;
	}

	public List<String> validate() {
		return new ValidatableSampleImpl(this).validate();
	}

	@Override
	public void cloneTo(Object e) {
		super.cloneTo(e);
		((JogoBarragem) e).setRodada(getRodada());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jogo other = (Jogo) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String getTipoLabel() {
		return MessageBundleUtils.getInstance().get("label_jogo_barragem");
	}

	public String getTipoValue() {
		return "jb";
	}

	@Override
	public String getDescricaoCalculada() {
		StringBuilder result = new StringBuilder();
		result.append(getRodada().getCiclo().getBarragem().getLocal());
		result.append(" - ");
		result.append(getRodada().getCiclo().getNome());
		result.append(", ");
		result.append(getRodada().getCiclo().getBarragem().getCategoria());
		result.append(", ");
		result.append(getRodada().getNumero());
		result.append(MessageBundleUtils.getInstance().get("label_rodada_posfixo"));
		if (getData() != null || getHora() != null) {
			result.append(", ");
			result.append(getDataHoraFormatada());
		}
		return result.toString();
	}
}