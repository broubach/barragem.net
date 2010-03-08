package net.barragem.scaffold;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class BarragemMailSender extends JavaMailSenderImpl {

	public void send(String from, String to, String subject, String body) {
		try {
			MimeMessage message = createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setFrom(from);
			helper.setSubject(subject);
			helper.setText(body, true);

			send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
