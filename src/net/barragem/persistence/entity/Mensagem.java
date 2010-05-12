package net.barragem.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( {
		@NamedQuery(name = "mensagemQuery", query = "from Mensagem m where m.destinatario = :usuario"),
		@NamedQuery(name = "novasMensagens", query = "select count(*) from Mensagem m where m.destinatario = :usuario and m.data > :dataUltimoAcesso") })
@Table(name = "mensagem")
public class Mensagem extends BaseEntity {

	@ManyToOne
	private Usuario destinatario;
	@ManyToOne
	private Usuario remetente;
	private String mensagem;
	private Date data;

	public Usuario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}

	public Usuario getRemetente() {
		return remetente;
	}

	public void setRemetente(Usuario remetente) {
		this.remetente = remetente;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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
		Mensagem other = (Mensagem) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}
