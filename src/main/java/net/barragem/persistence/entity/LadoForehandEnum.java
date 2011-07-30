package net.barragem.persistence.entity;

public enum LadoForehandEnum {
	Esquerdo("label_esquerda"), Direito("label_direita");

	private String labelKey;

	LadoForehandEnum(String labelKey) {
		this.labelKey = labelKey;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public void setLabelKey(String label) {
		this.labelKey = label;
	}
}
