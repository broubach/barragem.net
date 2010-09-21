package net.barragem.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import net.barragem.scaffold.MessageBundleUtils;

import org.apache.commons.lang.StringEscapeUtils;

@Entity
@NamedQuery(name = "lastTwitterUpdateQuery", query = "from AtualizacaoTwitter")
@Table(name = "atualizacaotwitter")
public class AtualizacaoTwitter extends BaseEntity {

	private String texto;
	private Date dataGravacao;
	private Date data;

	public String getTexto() {
		return texto;
	}

	public String getTextoEncoded() {
		return StringEscapeUtils.escapeXml(texto);
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getDataGravacao() {
		return dataGravacao;
	}

	public void setDataGravacao(Date dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getData() {
		return data;
	}

	public String getDataDescr() {
		long delta = (System.currentTimeMillis() - getData().getTime()) / 1000;

		if (delta < 60) {
			return MessageBundleUtils.getInstance().get("label_twitter_menos_de_um_minuto_atras");
		} else if (delta < 120) {
			return MessageBundleUtils.getInstance().get("label_twitter_um_minuto_atras");
		} else if (delta < (60 * 60)) {
			return new StringBuilder().append(delta / 60).append(" ").append(
					MessageBundleUtils.getInstance().get("label_twitter_minutos_atras")).toString();
		} else if (delta < (120 * 60)) {
			return MessageBundleUtils.getInstance().get("label_twitter_uma_hora_atras");
		} else if (delta < (24 * 60 * 60)) {
			return new StringBuilder().append(delta / 3600).append(" ").append(
					MessageBundleUtils.getInstance().get("label_twitter_horas_atras")).toString();
		} else if (delta < (48 * 60 * 60)) {
			return MessageBundleUtils.getInstance().get("label_twitter_um_dia_atras");
		} else {
			return new StringBuilder().append(delta / 86400).append(" ").append(
					MessageBundleUtils.getInstance().get("label_twitter_dias_atras")).toString();
		}
	}

	public String getTextoResumido() {
		return texto.substring(0, texto.length() >= 20 ? 20 : texto.length());
	}

	public String getTextoResumidoEncoded() {
		return StringEscapeUtils.escapeXml(getTextoResumido());
	}
}