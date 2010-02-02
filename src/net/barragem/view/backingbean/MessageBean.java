package net.barragem.view.backingbean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import net.barragem.util.MessageBundleUtils;

public class MessageBean {

	public FacesMessage getFirstInfoMessage() {
		FacesMessage message = null;
		for (Iterator<FacesMessage> it = FacesContext.getCurrentInstance().getMessages(null); it.hasNext();) {
			message = it.next();
			if (message.getSeverity() == FacesMessage.SEVERITY_INFO) {
				return message;
			}
		}
		return null;
	}

	public FacesMessage getFirstErrorMessage() {
		FacesMessage message = null;
		for (Iterator<FacesMessage> it = FacesContext.getCurrentInstance().getMessages(); it.hasNext();) {
			message = it.next();
			if (message.getSeverity() == FacesMessage.SEVERITY_ERROR) {
				return message;
			}
		}
		return null;
	}

	public Map<String, FacesMessage> getErrorMessages() {
		Map<String, FacesMessage> errors = new HashMap<String, FacesMessage>();
		FacesMessage message = null;
		String clientId = null;
		for (Iterator<String> it = FacesContext.getCurrentInstance().getClientIdsWithMessages(); it.hasNext();) {
			clientId = it.next();
			for (Iterator<FacesMessage> itMessages = FacesContext.getCurrentInstance().getMessages(clientId); itMessages
					.hasNext();) {
				message = itMessages.next();
				if (message.getSeverity() == FacesMessage.SEVERITY_ERROR) {
					errors.put(clientId, message);
				}
			}
		}
		return errors;
	}

	public Map<String, FacesMessage> getInfoMessages() {
		Map<String, FacesMessage> errors = new HashMap<String, FacesMessage>();
		FacesMessage message = null;
		String clientId = null;
		for (Iterator<String> it = FacesContext.getCurrentInstance().getClientIdsWithMessages(); it.hasNext();) {
			clientId = it.next();
			for (Iterator<FacesMessage> itMessages = FacesContext.getCurrentInstance().getMessages(clientId); itMessages
					.hasNext();) {
				message = itMessages.next();
				if (message.getSeverity() == FacesMessage.SEVERITY_INFO) {
					errors.put(clientId, message);
				}
			}
		}
		return errors;
	}

	public void addErrorMessage(String clientId, String msgKey, String... parameters) {
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSummary(MessageBundleUtils.getInstance().get(msgKey, parameters));
		facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage(clientId, facesMessage);
	}

	public void addInfoMessage(String clientId, String msgKey, String... parameters) {
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSummary(MessageBundleUtils.getInstance().get(msgKey, parameters));
		facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
		FacesContext.getCurrentInstance().addMessage(clientId, facesMessage);
	}
}