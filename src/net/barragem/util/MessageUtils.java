package net.barragem.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageUtils {

	static private MessageUtils instance;
	private final ResourceBundle rb;

	private MessageUtils(ClassLoader classLoader) {
		rb = ResourceBundle.getBundle("message-bundle", Locale.getDefault(), classLoader);
	}

	public static synchronized MessageUtils getInstance() {
		if (null == instance) {
			instance = new MessageUtils(MessageUtils.class.getClassLoader());
		}

		return instance;
	}

	public synchronized String get(String key) {
		return rb.getString(key);
	}

	public synchronized String get(String key, Object... parametros) {
		return new MessageFormat(rb.getString(key)).format(parametros);
	}
}
