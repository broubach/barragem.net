package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import net.barragem.scaffold.MessageBundleUtils;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "treino")
public class Treino extends Evento {

	@Override
	public String getTipoLabel() {
		return MessageBundleUtils.getInstance().get("label_treino");
	}

	@Override
	public String getJogadoresLabel() {
		StringBuilder jogadores = new StringBuilder();

		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			jogadores.append(jogadorEvento.getJogador().getNome().trim()).append(", ");
		}
		jogadores.delete(jogadores.length() - 2, jogadores.length());

		return jogadores.toString();
	}

	@Override
	public String getResultadoLabel() {
		return "";
	}

	@Override
	public boolean isUsuarioLogadoVencedor() {
		return false;
	}

	@Override
	public boolean isUsuarioLogadoPerdedor() {
		return false;
	}

	@Override
	public String getTipoValue() {
		return "t";
	}

	@Override
	public String getResultadoValue() {
		return RESULTADO_INDEFINIDO;
	}
}