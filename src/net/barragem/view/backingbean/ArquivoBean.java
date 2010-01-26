package net.barragem.view.backingbean;

import java.io.IOException;
import java.io.OutputStream;

import net.barragem.persistence.entity.Arquivo;

public class ArquivoBean extends BaseBean {

	public void paintFotoPerfil(OutputStream stream, Object object) throws IOException {
		Arquivo arquivo = null;
		if (getUsuarioLogado().getPerfil() == null || getUsuarioLogado().getPerfil().getFoto() == null) {
			arquivo = getFotoDefaultJogador();
		} else {
			arquivo = getUsuarioLogado().getPerfil().getFoto();
		}
		arquivo.paintFoto(stream, object);
	}
}
