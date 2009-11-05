package net.barragem.persistence.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.Embeddable;

import net.barragem.util.ValidatableSampleImpl;

import org.apache.commons.beanutils.BeanUtils;

@Embeddable
public class ParametroCiclo extends BaseEntity implements Validatable, Cloneable {

	private Integer duracaoEmMeses = new Integer(2);

	private ModalidadeDeSetsEnum modalidadeDeSets = ModalidadeDeSetsEnum.MelhorDeTresSets;

	@ValidateRequired
	private Integer raioParaSorteioDeJogosNoRanking = new Integer(4);

	@ValidateRequired
	private Integer pontuacaoVencedorAbaixoDoAdversarioSemPerderParciais = new Integer(16);
	@ValidateRequired
	private Integer pontuacaoVencedorAbaixoDoAdversarioPerdendoParciais = new Integer(13);
	@ValidateRequired
	private Integer pontuacaoVencedorAcimaDoAdversarioSemPerderParciais = new Integer(14);
	@ValidateRequired
	private Integer pontuacaoVencedorAcimaDoAdversarioPerdendoParciais = new Integer(11);

	@ValidateRequired
	private Integer pontuacaoPerdedorAbaixoDoAdversarioSemMarcarParciais = new Integer(3);
	@ValidateRequired
	private Integer pontuacaoPerdedorAbaixoDoAdversarioMarcandoParciais = new Integer(6);
	@ValidateRequired
	private Integer pontuacaoPerdedorAcimaDoAdversarioSemMarcarParciais = new Integer(1);
	@ValidateRequired
	private Integer pontuacaoPerdedorAcimaDoAdversarioMarcandoParciais = new Integer(4);

	@ValidateRequired
	private Integer pontuacaoVitoriaPorWo = new Integer(14);
	@ValidateRequired
	private Integer pontuacaoVencedorPrimeiraRodada = new Integer(16);
	@ValidateRequired
	private Integer pontuacaoPerdedorPrimeiraRodada = new Integer(6);

	@ValidateRequired
	private Integer rodadasDeHistoricoMantidasParaCalculoDoRanking = new Integer(5);

	public Integer getDuracaoEmMeses() {
		return duracaoEmMeses;
	}

	public void setDuracaoEmMeses(Integer duracaoEmMeses) {
		this.duracaoEmMeses = duracaoEmMeses;
	}

	public ModalidadeDeSetsEnum getModalidadeDeSets() {
		return modalidadeDeSets;
	}

	public void setModalidadeDeSets(ModalidadeDeSetsEnum modalidadeDeSets) {
		this.modalidadeDeSets = modalidadeDeSets;
	}

	public Integer getPontuacaoVencedorAbaixoDoAdversarioSemPerderParciais() {
		return pontuacaoVencedorAbaixoDoAdversarioSemPerderParciais;
	}

	public void setPontuacaoVencedorAbaixoDoAdversarioSemPerderParciais(
			Integer pontuacaoVencedorABaixoDoAdversarioSemPerderParciais) {
		this.pontuacaoVencedorAbaixoDoAdversarioSemPerderParciais = pontuacaoVencedorABaixoDoAdversarioSemPerderParciais;
	}

	public Integer getPontuacaoVencedorAbaixoDoAdversarioPerdendoParciais() {
		return pontuacaoVencedorAbaixoDoAdversarioPerdendoParciais;
	}

	public void setPontuacaoVencedorAbaixoDoAdversarioPerdendoParciais(
			Integer pontuacaoVencedorABaixoDoAdversarioPerdendoParciais) {
		this.pontuacaoVencedorAbaixoDoAdversarioPerdendoParciais = pontuacaoVencedorABaixoDoAdversarioPerdendoParciais;
	}

	public Integer getPontuacaoVencedorAcimaDoAdversarioSemPerderParciais() {
		return pontuacaoVencedorAcimaDoAdversarioSemPerderParciais;
	}

	public void setPontuacaoVencedorAcimaDoAdversarioSemPerderParciais(
			Integer pontuacaoVencedorACimaDoAdversarioSemPerderParciais) {
		this.pontuacaoVencedorAcimaDoAdversarioSemPerderParciais = pontuacaoVencedorACimaDoAdversarioSemPerderParciais;
	}

	public Integer getPontuacaoVencedorAcimaDoAdversarioPerdendoParciais() {
		return pontuacaoVencedorAcimaDoAdversarioPerdendoParciais;
	}

	public void setPontuacaoVencedorAcimaDoAdversarioPerdendoParciais(
			Integer pontuacaoVencedorACimaDoAdversarioPerdendoParciais) {
		this.pontuacaoVencedorAcimaDoAdversarioPerdendoParciais = pontuacaoVencedorACimaDoAdversarioPerdendoParciais;
	}

	public Integer getPontuacaoPerdedorAbaixoDoAdversarioSemMarcarParciais() {
		return pontuacaoPerdedorAbaixoDoAdversarioSemMarcarParciais;
	}

	public void setPontuacaoPerdedorAbaixoDoAdversarioSemMarcarParciais(
			Integer pontuacaoPerdedorABaixoDoAdversarioSemMarcarParciais) {
		this.pontuacaoPerdedorAbaixoDoAdversarioSemMarcarParciais = pontuacaoPerdedorABaixoDoAdversarioSemMarcarParciais;
	}

	public Integer getPontuacaoPerdedorAbaixoDoAdversarioMarcandoParciais() {
		return pontuacaoPerdedorAbaixoDoAdversarioMarcandoParciais;
	}

	public void setPontuacaoPerdedorAbaixoDoAdversarioMarcandoParciais(
			Integer pontuacaoPerdedorABaixoDoAdversarioMarcandoParciais) {
		this.pontuacaoPerdedorAbaixoDoAdversarioMarcandoParciais = pontuacaoPerdedorABaixoDoAdversarioMarcandoParciais;
	}

	public Integer getPontuacaoPerdedorAcimaDoAdversarioSemMarcarParciais() {
		return pontuacaoPerdedorAcimaDoAdversarioSemMarcarParciais;
	}

	public void setPontuacaoPerdedorAcimaDoAdversarioSemMarcarParciais(
			Integer pontuacaoPerdedorACimaDoAdversarioSemMarcarParciais) {
		this.pontuacaoPerdedorAcimaDoAdversarioSemMarcarParciais = pontuacaoPerdedorACimaDoAdversarioSemMarcarParciais;
	}

	public Integer getPontuacaoPerdedorAcimaDoAdversarioMarcandoParciais() {
		return pontuacaoPerdedorAcimaDoAdversarioMarcandoParciais;
	}

	public void setPontuacaoPerdedorAcimaDoAdversarioMarcandoParciais(
			Integer pontuacaoPerdedorACimaDoAdversarioMarcandoParciais) {
		this.pontuacaoPerdedorAcimaDoAdversarioMarcandoParciais = pontuacaoPerdedorACimaDoAdversarioMarcandoParciais;
	}

	public Integer getRaioParaSorteioDeJogosNoRanking() {
		return raioParaSorteioDeJogosNoRanking;
	}

	public void setRaioParaSorteioDeJogosNoRanking(Integer raioParaSorteioDeJogosNoRanking) {
		this.raioParaSorteioDeJogosNoRanking = raioParaSorteioDeJogosNoRanking;
	}

	public Integer getPontuacaoVitoriaPorWo() {
		return pontuacaoVitoriaPorWo;
	}

	public void setPontuacaoVitoriaPorWo(Integer pontuacaoVitoriaPorWo) {
		this.pontuacaoVitoriaPorWo = pontuacaoVitoriaPorWo;
	}

	public Integer getPontuacaoVencedorPrimeiraRodada() {
		return pontuacaoVencedorPrimeiraRodada;
	}

	public void setPontuacaoVencedorPrimeiraRodada(Integer pontuacaoVencedorPrimeiraRodada) {
		this.pontuacaoVencedorPrimeiraRodada = pontuacaoVencedorPrimeiraRodada;
	}

	public Integer getPontuacaoPerdedorPrimeiraRodada() {
		return pontuacaoPerdedorPrimeiraRodada;
	}

	public void setPontuacaoPerdedorPrimeiraRodada(Integer pontuacaoPerdedorPrimeiraRodada) {
		this.pontuacaoPerdedorPrimeiraRodada = pontuacaoPerdedorPrimeiraRodada;
	}

	public Integer getRodadasDeHistoricoMantidasParaCalculoDoRanking() {
		return rodadasDeHistoricoMantidasParaCalculoDoRanking;
	}

	public void setRodadasDeHistoricoMantidasParaCalculoDoRanking(Integer rodadasDeHistoricoMantidasParaCalculoDoRanking) {
		this.rodadasDeHistoricoMantidasParaCalculoDoRanking = rodadasDeHistoricoMantidasParaCalculoDoRanking;
	}

	public List<String> validate() {
		return new ValidatableSampleImpl(this).validate();
	}

	public Object clone() {
		try {
			return BeanUtils.cloneBean(this);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}