package net.barragem.scaffold;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class BarragemMailSender extends JavaMailSenderImpl {

	public void send(String from, String to, String subject, String body) {
		send(from, null, to, subject, body);
	}

	public void send(String from, String fromName, String to, String subject, String body) {
		try {
			MimeMessage message = createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			if (fromName != null) {
				helper.setFrom(new InternetAddress(from, fromName));
			} else {
				helper.setFrom(from);
			}
			helper.setSubject(subject);
			helper.setText(body, true);

			send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
