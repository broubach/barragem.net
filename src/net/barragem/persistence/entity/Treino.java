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
	public String getTipoStr() {
		return MessageBundleUtils.getInstance().get("label_treino");
	}

	@Override
	public String getJogadoresStr() {
		StringBuilder jogadores = new StringBuilder();

		for (JogadorEvento jogadorEvento : getJogadoresEventos()) {
			jogadores.append(jogadorEvento.getJogador().getNome().trim()).append(", ");
		}
		jogadores.delete(jogadores.length() - 2, jogadores.length());

		return jogadores.toString();
	}
}