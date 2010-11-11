package net.barragem.view.backingbean.componentes;

import java.util.Date;

public class RelatorioRodadasDto {
	private String rodada;
	private Date data;
	private Date hora;
	private Integer pontuacaoVencedor;
	private String jogadorVencedorNome;
	private String jogadorVencedorHash;
	private Integer pontuacaoPerdedor;
	private String jogadorPerdedorNome;
	private String jogadorPerdedorHash;
	private String placar;

	public RelatorioRodadasDto() {

	}

	public RelatorioRodadasDto(String rodada, Date data, Date hora, Integer pontuacaoVencedor,
			String jogadorVencedorNome, String jogadorVencedorHash, Integer pontuacaoPerdedor,
			String jogadorPerdedorNome, String jogadorPerdedorHash, String placar) {
		this.rodada = rodada;
		this.data = data;
		this.hora = hora;
		this.pontuacaoVencedor = pontuacaoVencedor;
		this.jogadorVencedorNome = jogadorVencedorNome;
		this.jogadorVencedorHash = jogadorVencedorHash;
		this.pontuacaoPerdedor = pontuacaoPerdedor;
		this.jogadorPerdedorNome = jogadorPerdedorNome;
		this.jogadorPerdedorHash = jogadorPerdedorHash;
		this.placar = placar;
	}

	public String getRodada() {
		return rodada;
	}

	public void setRodada(String rodada) {
		this.rodada = rodada;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public String getJogadorVencedorNome() {
		return jogadorVencedorNome;
	}

	public void setJogadorVencedorNome(String jogadorVencedorNome) {
		this.jogadorVencedorNome = jogadorVencedorNome;
	}

	public String getJogadorVencedorHash() {
		return jogadorVencedorHash;
	}

	public void setJogadorVencedorHash(String jogadorVencedorHash) {
		this.jogadorVencedorHash = jogadorVencedorHash;
	}

	public String getJogadorPerdedorNome() {
		return jogadorPerdedorNome;
	}

	public void setJogadorPerdedorNome(String jogadorPerdedorNome) {
		this.jogadorPerdedorNome = jogadorPerdedorNome;
	}

	public String getJogadorPerdedorHash() {
		return jogadorPerdedorHash;
	}

	public void setJogadorPerdedorHash(String jogadorPerdedorHash) {
		this.jogadorPerdedorHash = jogadorPerdedorHash;
	}

	public String getPlacar() {
		return placar;
	}

	public void setPlacar(String placar) {
		this.placar = placar;
	}

	public Integer getPontuacaoVencedor() {
		return pontuacaoVencedor;
	}

	public void setPontuacaoVencedor(Integer pontuacaoVencedor) {
		this.pontuacaoVencedor = pontuacaoVencedor;
	}

	public Integer getPontuacaoPerdedor() {
		return pontuacaoPerdedor;
	}

	public void setPontuacaoPerdedor(Integer pontuacaoPerdedor) {
		this.pontuacaoPerdedor = pontuacaoPerdedor;
	}
}