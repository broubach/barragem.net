package net.barragem.scaffold;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Perfil;

public class ScaleImagesServlet extends HttpServlet {
	public static byte[] scale(byte[] original, int width, int height) throws IOException {
		BufferedImage bsrc = ImageIO.read(new ByteArrayInputStream(original));
		BufferedImage bdest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		AffineTransform at = AffineTransform.getScaleInstance((double) width / bsrc.getWidth(), (double) height
				/ bsrc.getHeight());
		Graphics2D g = bdest.createGraphics();
		g.drawRenderedImage(bsrc, at);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(bdest, "JPG", out);
		return out.toByteArray();
	}

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
					perfil.getFoto().setDado(scale(fotoAntiga, 123, 162));
					perfil.getFoto().setMime("image/jpeg");
					perfil.getFoto().setTamanho(perfil.getFoto().getDado().length);
					fotoPequena = new Arquivo();
					fotoPequena.setMime("image/jpeg");
					fotoPequena.setDado(scale(fotoAntiga, 37, 49));
					fotoPequena.setData(new Date());
					fotoPequena.setDono(perfil.getFoto().getDono());
					fotoPequena.setNome(perfil.getFoto().getNome());
					fotoPequena.setTamanho(fotoPequena.getDado().length);
					//perfil.setFotoPequena(fotoPequena);
				} else {
					fotoPequena = PersistenceHelper.findByPk(Arquivo.class, 1000);
				}
				PersistenceHelper.persiste(perfil);
			}
		}
	}
}