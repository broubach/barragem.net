package net.barragem.persistence.entity;

public enum TipoBackhandEnum {
	UmaMao("label_uma_mao"), DuasMaos("label_duas_maos");

	private String labelKey;

	TipoBackhandEnum(String labelKey) {
		this.labelKey = labelKey;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}
}
