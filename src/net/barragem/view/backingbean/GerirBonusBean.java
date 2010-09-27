package net.barragem.view.backingbean;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.barragem.persistence.entity.Bonus;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Rodada;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class GerirBonusBean extends BaseBean {

	private Bonus bonusEmFoco;
	private Rodada rodadaEmFoco;

	public Bonus getBonusEmFoco() {
		return bonusEmFoco;
	}

	public void setBonusEmFoco(Bonus bonusEmFoco) {
		this.bonusEmFoco = bonusEmFoco;
	}

	public Rodada getRodadaEmFoco() {
		return rodadaEmFoco;
	}

	public void setRodadaEmFoco(Rodada rodadaEmFoco) {
		this.rodadaEmFoco = rodadaEmFoco;
	}

	public void preparaNovo(ActionEvent e) {
		rodadaEmFoco = PersistenceHelper.findByPk(Rodada.class, getId(), "bonuses");
		PersistenceHelper.initialize("ranking", rodadaEmFoco.getCiclo());
		bonusEmFoco = new Bonus();
		bonusEmFoco.setRodada(rodadaEmFoco);
	}

	public void preparaEdicao(ActionEvent e) {
		bonusEmFoco = PersistenceHelper.findByPk(Bonus.class, getId());
		rodadaEmFoco = bonusEmFoco.getRodada();
		PersistenceHelper.initialize("ranking", rodadaEmFoco.getCiclo());
		PersistenceHelper.initialize("bonuses", rodadaEmFoco);
	}

	public void removeBonus(ActionEvent e) {
		bonusEmFoco = PersistenceHelper.findByPk(Bonus.class, getId());
		rodadaEmFoco = bonusEmFoco.getRodada();
		PersistenceHelper.remove(bonusEmFoco);
		addMensagemAtualizacaoComSucesso();
		preparaAntecessor();
		bonusEmFoco = null;
		rodadaEmFoco = null;
	}

	public List<SelectItem> getListaJogadores() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		if (rodadaEmFoco != null) {
			SelectItem item = null;
			for (Jogador jogador : rodadaEmFoco.getCiclo().getJogadoresDoRanking()) {
				item = new SelectItem(jogador, jogador.getNome());
				result.add(item);
			}
		}
		return result;
	}

	public void salva(ActionEvent e) {
		if (valida(bonusEmFoco)) {
			PersistenceHelper.persiste(bonusEmFoco);
			if (bonusEmFoco.getJogador().getUsuarioCorrespondente() != null) {
				sendMail("no-reply@barragem.net", getUsuarioLogado().getNomeCompletoCapital(), bonusEmFoco.getJogador()
						.getUsuarioCorrespondente().getEmail(), new StringBuilder().append(
						"barragem.net - você ganhou bonus para a rodada ").append(bonusEmFoco.getRodada().getNumero())
						.append(" da barragem ").append(bonusEmFoco.getRodada().getCiclo().getBarragem().getLocal())
						.toString(), MessageFormat.format(emailTemplateNovoBonus, bonusEmFoco.getRodada().getNumero(),
						new StringBuilder().append(bonusEmFoco.getRodada().getCiclo().getBarragem().getLocal()).append(
								" - ").append(bonusEmFoco.getRodada().getCiclo().getBarragem().getCategoria())
								.toString()));
			}
			addMensagemAtualizacaoComSucesso();
			preparaAntecessor();
		}
	}

	protected boolean valida(Bonus bonus) {
		super.valida(bonus, true);
		for (Bonus item : rodadaEmFoco.getBonuses()) {
			if (item.getJogador().equals(bonus.getJogador()) && bonus.getId() == null) {
				messages.addErrorMessage(null, "label_jogador_jah_possui_bonus_cadastrado");
			}
		}
		return messages.getErrorMessages().isEmpty();
	}

	private void preparaAntecessor() {
		GerirRodadaBean gerirRodadaBean = (GerirRodadaBean) getRequestAttribute("gerirRodadaBean");
		PersistenceHelper.initialize("rodadas", rodadaEmFoco.getCiclo());
		gerirRodadaBean.carregaRodada(rodadaEmFoco.getCiclo(), rodadaEmFoco.getNumero() - 1);
	}
}