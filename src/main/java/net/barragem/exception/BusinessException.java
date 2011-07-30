package net.barragem.exception;

public class BusinessException extends Exception {
	private String clientId;
	private String messageKey;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public BusinessException(String clientId, String messageKey) {
		this.clientId = clientId;
		this.messageKey = messageKey;
	}
}
