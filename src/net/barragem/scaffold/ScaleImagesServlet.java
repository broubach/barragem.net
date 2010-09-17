package net.barragem.scaffold;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Perfil;
import net.barragem.view.backingbean.BaseBean;

public class ScaleImagesServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		byte[] fotoAntiga;
		Arquivo fotoPequena;
		List<Perfil> perfis = PersistenceHelper.findAll(Perfil.class);
		for (Perfil perfil : perfis) {
			if (perfil.getHash() != null) {
				PersistenceHelper.initialize("foto", perfil);
				if (!perfil.getFoto().getId().equals(new Integer(2))) {
					fotoAntiga = perfil.getFoto().getDado();
					perfil.getFoto().setDado(
							BaseBean.scaleImage(fotoAntiga, BaseBean.FOTO_DEFAULT_WIDTH, BaseBean.FOTO_DEFAULT_HEIGHT));
					perfil.getFoto().setMime("image/png");
					perfil.getFoto().setTamanho(perfil.getFoto().getDado().length);
					fotoPequena = new Arquivo();
					fotoPequena.setMime("image/png");
					fotoPequena.setDado(BaseBean.scaleImage(fotoAntiga, BaseBean.FOTO_PEQUENA_WIDTH,
							BaseBean.FOTO_PEQUENA_HEIGHT));
					fotoPequena.setData(new Date());
					fotoPequena.setDono(perfil.getFoto().getDono());
					fotoPequena.setNome(perfil.getFoto().getNome());
					fotoPequena.setTamanho(fotoPequena.getDado().length);
				} else {
					fotoPequena = PersistenceHelper.findByPk(Arquivo.class, 64);
				}
				perfil.setFotoPequena(fotoPequena);
				PersistenceHelper.persiste(perfil);
			}
		}
	}
}