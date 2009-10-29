package net.barragem.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageBundleUtils {

	static private MessageBundleUtils instance;
	private final ResourceBundle rb;

	private MessageBundleUtils(ClassLoader classLoader) {
		rb = ResourceBundle.getBundle("message-bundle", Locale.getDefault(), classLoader);
	}

	public static synchronized MessageBundleUtils getInstance() {
		if (null == instance) {
			instance = new MessageBundleUtils(MessageBundleUtils.class.getClassLoader());
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
