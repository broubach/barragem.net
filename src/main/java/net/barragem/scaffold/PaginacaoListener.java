package net.barragem.scaffold;

public interface PaginacaoListener<T> {

	public void afterPageChange(Paginavel<T> paginavel);
}
