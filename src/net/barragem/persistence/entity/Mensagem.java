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
}
