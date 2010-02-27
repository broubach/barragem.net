package net.barragem.view.backingbean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.barragem.persistence.entity.Conta;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;
import nl.captcha.Captcha;

public class RegistrarNovoUsuarioBean extends BaseBean {

	private static final String REGISTRAR_NOVO_USUARIO = "registrarNovoUsuario";
	private Usuario usuarioEmFoco = new Usuario();
	private Integer aniversarioDia;
	private Integer aniversarioMes;
	private Integer aniversarioAno;
	private String captchaAnswer;

	public Usuario getUsuarioEmFoco() {
		return usuarioEmFoco;
	}

	public void setUsuarioEmFoco(Usuario usuarioEmFoco) {
		this.usuarioEmFoco = usuarioEmFoco;
	}

	public Integer getAniversarioDia() {
		return aniversarioDia;
	}

	public void setAniversarioDia(Integer aniversarioDia) {
		this.aniversarioDia = aniversarioDia;
	}

	public Integer getAniversarioMes() {
		return aniversarioMes;
	}

	public void setAniversarioMes(Integer aniversarioMes) {
		this.aniversarioMes = aniversarioMes;
	}

	public Integer getAniversarioAno() {
		return aniversarioAno;
	}

	public void setAniversarioAno(Integer aniversarioAno) {
		this.aniversarioAno = aniversarioAno;
	}

	public String getCaptchaAnswer() {
		return captchaAnswer;
	}

	public void setCaptchaAnswer(String captcha) {
		this.captchaAnswer = captcha;
	}

	public String valida() {
		boolean possuiCampoVazio = false;

		// nome
		if (usuarioEmFoco.getNome() == null || usuarioEmFoco.getNome().trim().equals("")) {
			possuiCampoVazio = true;
		}

		// sobrenome
		if (usuarioEmFoco.getSobrenome() == null || usuarioEmFoco.getSobrenome().trim().equals("")) {
			possuiCampoVazio = true;
		}

		// email
		if (usuarioEmFoco.getEmail() == null || usuarioEmFoco.getEmail().trim().equals("")) {
			possuiCampoVazio = true;
		}

		// senha
		if (usuarioEmFoco.getSenha() == null || usuarioEmFoco.getSenha().trim().equals("")) {
			possuiCampoVazio = true;
		}

		// sexo
		if (usuarioEmFoco.getSexo() == null) {
			possuiCampoVazio = true;
		}

		// aniversario
		if (aniversarioDia == null || new Integer(0).equals(aniversarioDia)) {
			possuiCampoVazio = true;
		}
		if (aniversarioMes == null || new Integer(0).equals(aniversarioMes)) {
			possuiCampoVazio = true;
		}
		if (aniversarioAno == null || new Integer(0).equals(aniversarioAno)) {
			possuiCampoVazio = true;
		}

		if (possuiCampoVazio) {
			messages.addErrorMessage(REGISTRAR_NOVO_USUARIO, "error_favor_preencher_todos_os_campos");
		} else {
			if (usuarioEmFoco.getEmail().indexOf('@') == -1 || usuarioEmFoco.getEmail().indexOf('.') == -1) {
				messages.addErrorMessage(REGISTRAR_NOVO_USUARIO, "error_digite_um_email_valido");
			} else if (!PersistenceHelper.findByNamedQuery("emailExistenteQuery", usuarioEmFoco.getEmail()).isEmpty()) {
				messages.addErrorMessage(REGISTRAR_NOVO_USUARIO, "error_email_especificado_jah_existe");
			} else if (usuarioEmFoco.getSenha().length() < 6) {
				messages.addErrorMessage(REGISTRAR_NOVO_USUARIO, "error_a_senha_deve_conter_no_minimo_6_caracteres");
			}
		}

		if (messages.getErrorMessages().isEmpty()) {
			return "passo1";
		}
		return null;
	}

	public String registraNovoUsuario() {
		try {
			Captcha captcha = (Captcha) getSessionAttribute(Captcha.NAME);
			getRequest().setCharacterEncoding("UTF-8");
			if (captcha.isCorrect(captchaAnswer)) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DATE, aniversarioDia);
				calendar.set(Calendar.MONTH, aniversarioMes);
				calendar.set(Calendar.YEAR, aniversarioAno);

				usuarioEmFoco.setAniversario(calendar.getTime());
				usuarioEmFoco.setSenha(encriptMd5(usuarioEmFoco.getSenha()));
				usuarioEmFoco.setDataUltimoAcesso(new Date());

				Jogador jogador = new Jogador();
				jogador.setNome(usuarioEmFoco.getNomeCompletoCapital());
				jogador.setUsuarioCorrespondente(usuarioEmFoco);
				jogador.setUsuarioDono(usuarioEmFoco);
				usuarioEmFoco.setJogador(jogador);

				List<Jogador> jogadores = new ArrayList<Jogador>();
				jogadores.add(jogador);
				usuarioEmFoco.setJogadores(jogadores);

				PersistenceHelper.persiste(usuarioEmFoco);
				setUsuarioLogado(usuarioEmFoco);

				Conta conta = new Conta();
				conta.setProprietario(usuarioEmFoco);
				PersistenceHelper.persiste(conta);
				setContaUsuario(conta);

				// TODO: redigir conteudo de email
				sendMail("no-reply@barragem.net", usuarioEmFoco.getEmail(), "Bem-vindo ao Barragem.net",
						"Você se registrou no Barragem.net. O Email registrado é...");
				return "passo2";

			} else {
				messages.addErrorMessage(REGISTRAR_NOVO_USUARIO,
						"error_o_texto_inserido_nao_corresponde_ao_controle_de_seguranca");
				return null;
			}
		} catch (UnsupportedEncodingException e1) {
			throw new RuntimeException(e1);
		}
	}
}