package net.barragem.view.backingbean;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import javax.faces.event.ActionEvent;

import net.barragem.persistence.entity.Usuario;
import net.barragem.util.PersistenceHelper;
import nl.captcha.Captcha;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class RegistrarNovoUsuarioBean extends BaseBean {

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

	public void registraNovoUsuario(ActionEvent e) {
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

				PersistenceHelper.persiste(usuarioEmFoco);
				setUsuarioLogado(usuarioEmFoco);

			}
		} catch (UnsupportedEncodingException e1) {
			throw new RuntimeException(e1);
		}
	}
}