package net.barragem.persistence.entity;

public enum AcaoEnum {
	CriarBarragem("label_acao_barragem_criou"), SortearJogosBarragem("label_acao_barragem_sorteou"), CriarJogoBarragem(
			"label_acao_jogo_barragem_criou"), AtualizarJogoBarragem("label_acao_jogo_barragem_atualizou"), AdicionarUsuario(
			"label_acao_lista_adicionou");

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
