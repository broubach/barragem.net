package net.barragem.persistence.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "requisicaorecuperarsenha")
@NamedQueries( {
		@NamedQuery(name = "findRequisicaoValidaByHashQuery", query = "from RequisicaoRecuperarSenha r where r.hash = :hash and r.dataConclusao = null and r.data > :hojeMenosDoisDias"),
		@NamedQuery(name = "findRequisicaoByHashQuery", query = "from RequisicaoRecuperarSenha r where r.hash = :hash") })
public class RequisicaoRecuperarSenha extends BaseEntity {
	@ManyToOne(cascade = { CascadeType.ALL })
	private Usuario usuario;
	private Date data;
	private String hash;
	private Date dataConclusao;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}
}
