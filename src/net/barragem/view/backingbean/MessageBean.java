package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageBean extends BaseBean {

	public FacesMessage getFirstInfoMessage() {
		FacesMessage message = null;
		for (Iterator<FacesMessage> it = FacesContext.getCurrentInstance().getMessages(); it.hasNext();) {
			message = it.next();
			if (message.getSeverity() == FacesMessage.SEVERITY_INFO) {
				return message;
			}
		}
		return null;
	}

	public List<FacesMessage> getErrorMessages(String id) {
		List<FacesMessage> messages = new ArrayList<FacesMessage>();
		FacesMessage message = null;
		for (Iterator<FacesMessage> it = FacesContext.getCurrentInstance().getMessages(); it.hasNext();) {
			message = it.next();
			if (message.getSeverity() == FacesMessage.SEVERITY_ERROR) {
				messages.add(message);
			}
		}
		return messages;
	}
}
