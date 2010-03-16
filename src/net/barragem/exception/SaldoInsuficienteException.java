package net.barragem.exception;

public class SaldoInsuficienteException extends BusinessException {

	public SaldoInsuficienteException(String clientId, String messageKey) {
		super(clientId, messageKey);
	}

	public SaldoInsuficienteException() {
		this(null, null);
	}
}
