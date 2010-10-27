package net.barragem.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( {
		@NamedQuery(name = "todasMensagensQuery", query = "from Mensagem m where m.destinatario = :usuario order by m.data desc"),
		@NamedQuery(name = "mensagensVisiveisQuery", query = "from Mensagem m where m.destinatario = :usuario and (m.privada = 1 and m.remetente = :remetente or m.privada = 0) order by m.data desc"),
		@NamedQuery(name = "novasMensagensQuery", query = "select count(*) from Mensagem m where m.destinatario = :usuario and m.lida = 0"),
		@NamedQuery(name = "marcaMensagensComoLidasQuery", query = "update Mensagem m set m.lida = 1 where m.lida = 0 and m.destinatario.id = :usuarioId")})
@Table(name = "mensagem")
public class Mensagem extends BaseEntity {

	@ManyToOne
	private Usuario destinatario;
	@ManyToOne
	private Usuario remetente;
	private String mensagem;
	private Date data;
	private boolean lida;
	private boolean privada;

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

	public boolean isLida() {
    	return lida;
    }

	public void setLida(boolean lida) {
    	this.lida = lida;
    }

	public boolean isPrivada() {
    	return privada;
    }

	public void setPrivada(boolean privada) {
    	this.privada = privada;
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
