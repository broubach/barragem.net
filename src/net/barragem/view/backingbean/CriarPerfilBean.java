package net.barragem.view.backingbean;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Perfil;
import net.barragem.util.PersistenceHelper;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class CriarPerfilBean extends BaseBean {

	private Perfil perfilEmFoco = new Perfil();
	private Arquivo fotoEmFoco;

	public Perfil getPerfilEmFoco() {
		return perfilEmFoco;
	}

	public void setPerfilEmFoco(Perfil perfilEmFoco) {
		this.perfilEmFoco = perfilEmFoco;
	}

	public Arquivo getFotoEmFoco() {
		return fotoEmFoco;
	}

	public void setFotoEmFoco(Arquivo fotoEmFoco) {
		this.fotoEmFoco = fotoEmFoco;
	}

	public void salva(ActionEvent e) {
		perfilEmFoco.setUsuario(getUsuarioLogado());
		getUsuarioLogado().setPerfil(perfilEmFoco);
		if (fotoEmFoco != null) {
			perfilEmFoco.setFoto(fotoEmFoco);
		}
		PersistenceHelper.persiste(perfilEmFoco);
	}

	public void paint(OutputStream stream, Object object) throws IOException {
		if (fotoEmFoco == null) {
			String path = getServletContext().getRealPath("img/foto-jogador.png");
			java.io.File fsFile = new java.io.File(path);

			fotoEmFoco = new Arquivo();
			fotoEmFoco.setDado(getBytesFromFile(fsFile));
		}
		stream.write(fotoEmFoco.getDado());
	}

	private static byte[] getBytesFromFile(java.io.File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset = numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	public void listener(UploadEvent event) throws Exception {
		UploadItem item = event.getUploadItem();
		fotoEmFoco.setDado(item.getData());
		fotoEmFoco.setData(new Date());
		fotoEmFoco.setDono(getUsuarioLogado());
		fotoEmFoco.setNome(item.getFileName());
		fotoEmFoco.setTamanho(item.getFileSize());
		fotoEmFoco.setMime(item.getContentType());

		event.getUploadItems().clear();
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}
}
