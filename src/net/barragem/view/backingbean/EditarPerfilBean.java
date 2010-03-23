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
import net.barragem.persistence.entity.Usuario;
import net.barragem.persistence.entity.Validatable;
import net.barragem.scaffold.PersistenceHelper;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class EditarPerfilBean extends BaseBean {

	private Perfil perfilEmFoco;
	private Usuario usuarioEmFoco;
	private Arquivo fotoEmFoco;
	private List<String> selectedItems;
	private String senhaAtual;
	private String novaSenha;
	private String confirmacaoNovaSenha;

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

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmacaoNovaSenha() {
		return confirmacaoNovaSenha;
	}

	public void setConfirmacaoNovaSenha(String confirmacaoNovaSenha) {
		this.confirmacaoNovaSenha = confirmacaoNovaSenha;
	}

	public void preparaPerfil(ActionEvent e) {
		EditarPerfilBean editarPerfilBean = new EditarPerfilBean();
		editarPerfilBean.setUsuarioEmFoco((Usuario) getUsuarioLogado().clone());
		if (editarPerfilBean.getUsuarioEmFoco().getPerfil() != null) {
			editarPerfilBean.setPerfilEmFoco((Perfil) editarPerfilBean.getUsuarioEmFoco().getPerfil().clone());
			if (editarPerfilBean.getPerfilEmFoco().getHash() != null) {
				if (!PersistenceHelper.isInitialized(getUsuarioLogado().getPerfil(), "foto")) {
					PersistenceHelper.initialize("foto", getUsuarioLogado().getPerfil());
				}
				editarPerfilBean.getPerfilEmFoco().setFoto(getUsuarioLogado().getPerfil().getFoto());
				editarPerfilBean.setFotoEmFoco(editarPerfilBean.getPerfilEmFoco().getFoto());
			}
		} else {
			editarPerfilBean.setPerfilEmFoco(new Perfil());
		}
		if (editarPerfilBean.getPerfilEmFoco().getCategorias() != null
				&& !editarPerfilBean.getPerfilEmFoco().getCategorias().isEmpty()) {
			editarPerfilBean.setSelectedItems(new ArrayList<String>());
			for (Categoria categoria : editarPerfilBean.getPerfilEmFoco().getCategorias()) {
				editarPerfilBean.getSelectedItems().add(categoria.getId().toString());
			}
		}

		setSessionAttribute("editarPerfilBean", editarPerfilBean);
	}

	public void salvaDadosBasicos(ActionEvent e) {
		if (valida(usuarioEmFoco)) {
			if (usuarioEmFoco.getPerfil() != null) {
				usuarioEmFoco.getPerfil().setUsuario(usuarioEmFoco);
			}
			usuarioEmFoco.getJogadores().remove(usuarioEmFoco.getJogador());
			usuarioEmFoco.getJogadores().add(usuarioEmFoco.getJogador());
			usuarioEmFoco.getJogador().setNome(usuarioEmFoco.getNomeCompletoCapital());
			PersistenceHelper.persiste(usuarioEmFoco);
			setUsuarioLogado(usuarioEmFoco);
			addMensagemAtualizacaoComSucesso();
			preparaPerfil(e);
		}
	}

	public void salvaPerfil(ActionEvent e) {
		if (selectedItems != null) {
			List<Categoria> categorias = new ArrayList<Categoria>();
			for (String selectedItem : selectedItems) {
				categorias.add(PersistenceHelper.findByPk(Categoria.class, Integer.valueOf(selectedItem)));
			}
			perfilEmFoco.setCategorias(categorias);
		}
		perfilEmFoco.setUsuario(getUsuarioLogado());
		perfilEmFoco.getUsuario().setPerfil(perfilEmFoco);
		PersistenceHelper.persiste(perfilEmFoco);
		addMensagemAtualizacaoComSucesso();
		preparaPerfil(e);
	}

	public void salvaFoto(ActionEvent e) {
		perfilEmFoco.setHash(encriptMd5(fotoEmFoco.getDado().toString()));
		perfilEmFoco.setFoto(fotoEmFoco);
		perfilEmFoco.setUsuario(getUsuarioLogado());
		perfilEmFoco.getUsuario().setPerfil(perfilEmFoco);
		PersistenceHelper.persiste(perfilEmFoco);
		addMensagemAtualizacaoComSucesso();
		preparaPerfil(e);
	}

	@Override
	protected boolean valida(Validatable validatable) {
		Usuario usuario = (Usuario) validatable;
		if (!PersistenceHelper.findByNamedQuery("alteracaoEmailExistenteQuery", usuario, usuario.getEmail()).isEmpty()) {
			messages.addErrorMessage("error_email_especificado_jah_existe", "error_email_especificado_jah_existe");
		}

		return super.valida(validatable);
	}

	public void alteraSenha(ActionEvent e) {
		if (validaAlteracaoSenha()) {
			getUsuarioLogado().setSenha(encriptMd5(novaSenha));
			senhaAtual = "";
			novaSenha = "";
			confirmacaoNovaSenha = "";
			PersistenceHelper.persiste(getUsuarioLogado());
			addMensagemAtualizacaoComSucesso();
		}
	}

	private boolean validaAlteracaoSenha() {
		if (senhaAtual.equals("") || novaSenha.equals("") || confirmacaoNovaSenha.equals("")) {
			messages.addErrorMessage(null, "label_preencha_os_campos_obrigatorios");
			return false;
		}
		if (!getUsuarioLogado().getSenha().equals(encriptMd5(senhaAtual))) {
			messages.addErrorMessage(null, "label_a_senha_atual_nao_confere");
			return false;
		}
		if (!novaSenha.equals(confirmacaoNovaSenha)) {
			messages.addErrorMessage(null, "label_as_novas_senhas_digitadas_nao_conferem");
			return false;
		}
		if (!validaSenha(null, novaSenha)) {
			return false;
		}
		return true;
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
			fotoEmFoco.setDono(usuarioEmFoco);
			fotoEmFoco.setNome(item.getFileName());
			fotoEmFoco.setTamanho(item.getFileSize());
			fotoEmFoco.setMime(item.getContentType());
		}

		event.getUploadItems().clear();
	}
}
