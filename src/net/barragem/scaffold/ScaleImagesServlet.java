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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Perfil;

public class ScaleImagesServlet extends HttpServlet {

	public static byte[] scale(byte[] original, String mime, int width, int height, ServletOutputStream os)
			throws IOException {
		BufferedImage bsrc = ImageIO.read(new ByteArrayInputStream(original));

		AffineTransform at = AffineTransform.getScaleInstance(width, height);
		BufferedImage bdest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bdest.createGraphics();
		g.drawRenderedImage(bsrc, at);

		// TODO: terminar servlet

		//		if (mime.contains("jpeg")) {
		//			BufferedImage image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//			image2.getGraphics().drawImage(bdest, 0, 0, null);
		//
		//			bdest = image2;
		//		}
		if (os != null) {
			ImageIO.write(bdest, "JPG", os);
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(bdest, "JPG", out);
		return out.toByteArray();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		byte[] fotoAntiga;
		String fotoAntigaMime;
		Arquivo fotoPequena;
		List<Perfil> perfis = PersistenceHelper.findAll(Perfil.class);
		for (Perfil perfil : perfis) {
			if (perfil.getHash() != null) {
				PersistenceHelper.initialize("foto", perfil);
				fotoAntiga = perfil.getFoto().getDado();
				fotoAntigaMime = perfil.getFoto().getMime();
				perfil.getFoto().setDado(scale(fotoAntiga, fotoAntigaMime, 123, 162, resp.getOutputStream()));
				perfil.getFoto().setMime("image/jpeg");
				perfil.getFoto().setTamanho(perfil.getFoto().getDado().length);
				fotoPequena = new Arquivo();
				fotoPequena.setMime("image/jpeg");
				fotoPequena.setDado(scale(fotoAntiga, fotoAntigaMime, 37, 49, null));
				fotoPequena.setData(new Date());
				fotoPequena.setDono(perfil.getFoto().getDono());
				fotoPequena.setNome(perfil.getFoto().getNome());
				fotoPequena.setTamanho(fotoPequena.getDado().length);
				//perfil.setFotoPequena(fotoPequena);
				PersistenceHelper.persiste(perfil);
			}
		}
	}

	public static void main(String args[]) {
		ImageIO.getReaderMIMETypes();
	}
}