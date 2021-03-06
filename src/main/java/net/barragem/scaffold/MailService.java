package net.barragem.scaffold;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class MailService implements MessageListener {

	private BarragemMailSender mailSender;

	public BarragemMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(BarragemMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof MapMessage) {
				MapMessage mapMessage = (MapMessage) message;
				mailSender.send(mapMessage.getString("from"), mapMessage.getString("fromName"), mapMessage
						.getString("to"), mapMessage.getString("subject"), mapMessage.getString("body"));
			}
		} catch (JMSException ex) {
			throw new RuntimeException(ex);
		}
	}
}