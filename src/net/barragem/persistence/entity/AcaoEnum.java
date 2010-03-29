package net.barragem.persistence.entity;

public enum AcaoEnum {
	CriarBarragem("label_atualizacao_barragem_criou"), AtualizarBarragem("label_atualizacao_barragem_atualizou");

	private String acaoKey;

	private AcaoEnum(String acaoKey) {
		this.acaoKey = acaoKey;
	}

	public String getAcaoKey() {
		return acaoKey;
	}

	public void setAcaoKey(String acaoKey) {
		this.acaoKey = acaoKey;
	}
}
