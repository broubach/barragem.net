package net.barragem.view.backingbean;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Perfil;
import net.barragem.util.PersistenceHelper;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class EditarPerfilBean extends BaseBean {

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

	public void editaPerfil(ActionEvent e) {
		perfilEmFoco = getUsuarioLogado().getPerfil();
		if (perfilEmFoco == null) {
			perfilEmFoco = new Perfil();
		} else if (perfilEmFoco.getFoto() != null) {
			fotoEmFoco = perfilEmFoco.getFoto();
		}
	}

	public void salvaPerfil(ActionEvent e) {
		perfilEmFoco.setUsuario(getUsuarioLogado());
		getUsuarioLogado().setPerfil(perfilEmFoco);
		if (fotoEmFoco != null) {
			perfilEmFoco.setFoto(fotoEmFoco);
		}
		PersistenceHelper.persiste(perfilEmFoco);
	}

	public void paintFotoUpload(OutputStream stream, Object object) throws IOException {
		if (fotoEmFoco == null) {
			fotoEmFoco = getFotoDefaultJogador();
		}
		stream.write(fotoEmFoco.getDado());
	}

	public void atualizaFotoUpload(UploadEvent event) {
		UploadItem item = event.getUploadItem();

		if (item.getFileName().equals(getFotoDefaultJogador().getNome())
				&& item.getFileSize() == getFotoDefaultJogador().getTamanho().intValue()
				&& item.getContentType().equals(getFotoDefaultJogador().getMime())) {
			fotoEmFoco = getFotoDefaultJogador();
		} else {
			fotoEmFoco = new Arquivo();
			fotoEmFoco.setDado(item.getData());
			fotoEmFoco.setData(new Date());
			fotoEmFoco.setDono(getUsuarioLogado());
			fotoEmFoco.setNome(item.getFileName());
			fotoEmFoco.setTamanho(item.getFileSize());
			fotoEmFoco.setMime(item.getContentType());
		}

		event.getUploadItems().clear();
	}
}
