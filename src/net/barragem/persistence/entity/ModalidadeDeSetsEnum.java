package net.barragem.persistence.entity;

public enum ModalidadeDeSetsEnum {
	MelhorDeTresSets(3), MelhorDeTresSetsComSuperTiebreakNoUltimoSet(3), SetProfissionalUnico(1);

	private Integer numeroDeSets;

	ModalidadeDeSetsEnum(Integer numeroDeSets) {
		this.numeroDeSets = numeroDeSets;
	}

	public Integer getNumeroDeSets() {
		return numeroDeSets;
	}

	public void setNumeroDeSets(Integer numeroDeSets) {
		this.numeroDeSets = numeroDeSets;
	}
}
