package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "predicado")
public class Predicado extends BaseEntity {

	@ManyToOne
	private Atualizacao atualizacao;
	private String predicado;
	private Boolean isPredicadoAKey;
	private String predicadoValue;
	private TipoPredicadoValueEnum tipoPredicadoValue;
	private Integer predicadoValueId;

	public Predicado() {
	}

	public Predicado(String predicado, Boolean isPredicadoAKey) {
		this.predicado = predicado;
		this.isPredicadoAKey = isPredicadoAKey;
	}

	public Predicado(String predicado, Boolean isPredicadoAKey, String predicadoValue,
			TipoPredicadoValueEnum tipoPredicadoValue, Integer predicadoValueId) {
		this(predicado, isPredicadoAKey);
		this.predicadoValue = predicadoValue;
		this.tipoPredicadoValue = tipoPredicadoValue;
		this.predicadoValueId = predicadoValueId;
	}

	public Atualizacao getAtualizacao() {
		return atualizacao;
	}

	public void setAtualizacao(Atualizacao atualizacao) {
		this.atualizacao = atualizacao;
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

	public void setPredicado(String predicado) {
		this.predicado = predicado;
	}

	public String getPredicadoValue() {
		return predicadoValue;
	}

	public void setPredicadoValue(String predicadoValue) {
		this.predicadoValue = predicadoValue;
	}

	public TipoPredicadoValueEnum getTipoPredicadoValue() {
		return tipoPredicadoValue;
	}

	public void setTipoPredicadoValue(TipoPredicadoValueEnum tipoPredicadoValue) {
		this.tipoPredicadoValue = tipoPredicadoValue;
	}

	public Integer getPredicadoValueId() {
		return predicadoValueId;
	}

	public void setPredicadoValueId(Integer predicadoValueId) {
		this.predicadoValueId = predicadoValueId;
	}
}
