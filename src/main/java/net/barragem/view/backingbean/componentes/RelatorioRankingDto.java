package net.barragem.view.backingbean.componentes;

public class RelatorioRankingDto {
	private Integer ranking;
	private Integer pontuacao;
	private String jogadorHash;
	private String jogadorNome;

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}

	public String getJogadorHash() {
		return jogadorHash;
	}

	public void setJogadorHash(String jogadorHash) {
		this.jogadorHash = jogadorHash;
	}

	public String getJogadorNome() {
		return jogadorNome;
	}

	public void setJogadorNome(String jogadorNome) {
		this.jogadorNome = jogadorNome;
	}

}
