package net.barragem.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "atualizacao")
public class Atualizacao extends BaseEntity {

	private String sujeitoClassName;
	private Integer sujeitoId;
	private AcaoEnum acao;
	private String objetoClassName;
	private Integer objetoId;
	private Boolean isPredicadoAKey;
	private String predicado;
	private Date data;

	public Atualizacao() {
	}

	public Atualizacao(String sujeitoClassName, Integer sujeitoId, AcaoEnum acao, String objetoClassName,
			Integer objetoId, Date data) {
		this.sujeitoClassName = sujeitoClassName;
		this.sujeitoId = sujeitoId;
		this.acao = acao;
		this.objetoClassName = objetoClassName;
		this.objetoId = objetoId;
		this.data = data;
	}

	public String getSujeitoClassName() {
		return sujeitoClassName;
	}

	public void setSujeitoClassName(String sujeitoClassName) {
		this.sujeitoClassName = sujeitoClassName;
	}

	public Integer getSujeitoId() {
		return sujeitoId;
	}

	public void setSujeitoId(Integer sujeitoId) {
		this.sujeitoId = sujeitoId;
	}

	public AcaoEnum getAcao() {
		return acao;
	}

	public void setAcao(AcaoEnum acao) {
		this.acao = acao;
	}

	public String getObjetoClassName() {
		return objetoClassName;
	}

	public void setObjetoClassName(String objetoClassName) {
		this.objetoClassName = objetoClassName;
	}

	public Integer getObjetoId() {
		return objetoId;
	}

	public void setObjetoId(Integer objetoId) {
		this.objetoId = objetoId;
	}

	public Boolean getIsPredicadoAKey() {
		return isPredicadoAKey;
	}

	public void setIsPredicadoAKey(Boolean isPredicadoAKey) {
		this.isPredicadoAKey = isPredicadoAKey;
	}

	public String getPredicado() {
		return predicado;
	}

	public void setPredicado(String predicadoKey) {
		this.predicado = predicadoKey;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}
