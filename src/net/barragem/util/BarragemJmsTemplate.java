package net.barragem.util;

import org.springframework.jms.core.JmsTemplate;

public class BarragemJmsTemplate extends JmsTemplate {

	private Boolean enabled = true;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
