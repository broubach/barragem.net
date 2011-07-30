package net.barragem.business.bo;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Perfil;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.ArquivoBean;

public class PerfilBo extends BaseBo {

	public PerfilBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void salvaFoto(Usuario usuarioEmFoco, Perfil perfilEmFoco, Arquivo fotoEmFoco) {
		try {
			Arquivo fotoASerRemovida = null;
			if (fotoEmFoco.getId() == null && perfilEmFoco.getFoto() != null && perfilEmFoco.getFoto().getId() != null
					&& !perfilEmFoco.getFoto().getId().equals(ArquivoBean.FOTO_DEFAULT_JOGADOR_ID)) {
				fotoASerRemovida = perfilEmFoco.getFoto();
			}

			int x1 = getRequestParameter("x1") != null && !"".equals(getRequestParameter("x1")) ? Integer
					.parseInt(getRequestParameter("x1")) : 0;
			int y1 = getRequestParameter("y1") != null && !"".equals(getRequestParameter("y1")) ? Integer
					.parseInt(getRequestParameter("y1")) : 0;
			int x2 = getRequestParameter("x2") != null && !"".equals(getRequestParameter("x2")) ? Integer
					.parseInt(getRequestParameter("x2")) : 0;
			int y2 = getRequestParameter("y2") != null && !"".equals(getRequestParameter("y2")) ? Integer
					.parseInt(getRequestParameter("y2")) : 0;
			Arquivo fotoPequena = null;
			if (fotoEmFoco.getId() == null || !fotoEmFoco.getId().equals(ArquivoBean.FOTO_DEFAULT_JOGADOR_ID)
					|| x2 - x1 > 0 && y2 - y1 > 0) {
				if (fotoEmFoco.getId() != null && fotoEmFoco.getId().equals(ArquivoBean.FOTO_DEFAULT_JOGADOR_ID)) {
					String nome = fotoEmFoco.getNome();
					byte[] dado = fotoEmFoco.getDado();
					fotoEmFoco = new Arquivo();
					fotoEmFoco.setData(new Date());
					fotoEmFoco.setDono(usuarioEmFoco);
					fotoEmFoco.setNome(nome);
					fotoEmFoco.setDado(dado);
				}
				fotoPequena = scaleFotosDefaultEPequena(usuarioEmFoco, fotoEmFoco, x1, y1, x2, y2);
			} else {
				fotoPequena = getFotoPequenaJogador();
			}

			perfilEmFoco.setHash(encriptMd5(fotoEmFoco.getDado().toString()));
			perfilEmFoco.setFoto(fotoEmFoco);
			perfilEmFoco.setFotoPequena(fotoPequena);
			perfilEmFoco.setUsuario(getUsuarioLogado());
			perfilEmFoco.getUsuario().setPerfil(perfilEmFoco);
			PersistenceHelper.persiste(perfilEmFoco);
			if (fotoASerRemovida != null) {
				PersistenceHelper.remove(fotoASerRemovida);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Arquivo scaleFotosDefaultEPequena(Usuario usuarioEmFoco, Arquivo fotoEmFoco, int x1, int y1, int x2, int y2)
			throws IOException {
		byte[] dadoOriginal = fotoEmFoco.getDado();
		fotoEmFoco.setDado(cropAndScaleImage(dadoOriginal, x1, y1, x2, y2, FOTO_DEFAULT_WIDTH, FOTO_DEFAULT_HEIGHT));
		fotoEmFoco.setTamanho(fotoEmFoco.getDado().length);
		fotoEmFoco.setMime("image/png");

		Arquivo fotoPequena = new Arquivo();
		fotoPequena.setData(new Date());
		fotoPequena.setDono(usuarioEmFoco);
		fotoPequena.setNome(fotoEmFoco.getNome());
		fotoPequena.setDado(cropAndScaleImage(dadoOriginal, x1, y1, x2, y2, FOTO_PEQUENA_WIDTH, FOTO_PEQUENA_HEIGHT));
		fotoPequena.setTamanho(fotoPequena.getDado().length);
		fotoPequena.setMime("image/png");
		return fotoPequena;
	}
}
