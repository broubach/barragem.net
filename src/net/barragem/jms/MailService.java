package net.barragem.jms;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import net.barragem.util.BarragemMailSender;

public class MailService implements MessageListener {
	private static final Integer FROM_ID = new Integer(0);
	private static final Integer TO_ID = new Integer(1);
	private static final Integer SUBJECT_ID = new Integer(2);
	private static final Integer BODY_ID = new Integer(3);

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
			if (message instanceof TextMessage) {
				StringTokenizer strTkn = new StringTokenizer(((TextMessage) message).getText(), "&");

				Map<Integer, String> mailProperties = new HashMap<Integer, String>();
				for (Integer i = 0; strTkn.hasMoreElements(); i++) {
					mailProperties.put(i, strTkn.nextElement().toString());
				}

				mailSender.send(mailProperties.get(FROM_ID), mailProperties.get(TO_ID), mailProperties.get(SUBJECT_ID),
						mailProperties.get(BODY_ID));
			}
		} catch (JMSException ex) {
			throw new RuntimeException(ex);
		}
	}
}