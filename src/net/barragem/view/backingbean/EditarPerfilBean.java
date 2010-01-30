package net.barragem.view.backingbean;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Categoria;
import net.barragem.persistence.entity.Perfil;
import net.barragem.util.PersistenceHelper;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class EditarPerfilBean extends BaseBean {

	private static final char FILL_LEFT_CHAR = '0';
	private static final int MAX_HASH_SIZE = 9;
	private Perfil perfilEmFoco = new Perfil();
	private Arquivo fotoEmFoco;
	private List<String> selectedItems;

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

	public List<String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public void editaPerfil(ActionEvent e) {
		perfilEmFoco = getUsuarioLogado().getPerfil();
		if (perfilEmFoco == null) {
			perfilEmFoco = new Perfil();
		} else if (perfilEmFoco.getFoto() != null) {
			fotoEmFoco = perfilEmFoco.getFoto();
		}
		if (perfilEmFoco.getCategorias() != null && !perfilEmFoco.getCategorias().isEmpty()) {
			selectedItems = new ArrayList<String>();
			for (Categoria categoria : perfilEmFoco.getCategorias()) {
				selectedItems.add(categoria.getId().toString());
			}
		}
	}

	public void salvaPerfil(ActionEvent e) {
		perfilEmFoco.setUsuario(getUsuarioLogado());
		if (selectedItems != null) {
			List<Categoria> categorias = new ArrayList<Categoria>();
			for (String selectedItem : selectedItems) {
				categorias.add(PersistenceHelper.findByPk(Categoria.class, Integer.valueOf(selectedItem)));
			}
			perfilEmFoco.setCategorias(categorias);
		}
		getUsuarioLogado().setPerfil(perfilEmFoco);
		if (fotoEmFoco != null) {
			perfilEmFoco
					.setHash(encriptMd5(fillLeft(perfilEmFoco.getId().toString(), MAX_HASH_SIZE, FILL_LEFT_CHAR)));
			perfilEmFoco.setFoto(fotoEmFoco);
		}
		PersistenceHelper.persiste(perfilEmFoco);
		addMensagemAtualizacaoComSucesso();
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
