package net.barragem.view.backingbean.componentes;

public class TextoEFlagPrivadoDto {
	private String texto;
	private Boolean privada = Boolean.FALSE;

	public String getTexto() {
		return texto;
	}

	public void setTexto(String resposta) {
		this.texto = resposta;
	}

	public Boolean getPrivada() {
		return privada;
	}

	public void setPrivada(Boolean privada) {
		this.privada = privada;
	}
}
