package net.barragem.view.backingbean;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;

import net.barragem.business.bo.BarragemBo;
import net.barragem.persistence.entity.Barragem;
import net.barragem.scaffold.Paginavel;
import net.barragem.scaffold.PaginavelSampleImpl;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;
import org.hibernate.exception.ConstraintViolationException;

@KeepAlive
public class GerirBarragemBean extends BaseBean {
	private String pesquisa;
	private String pesquisaSalva;
	private Integer tipoPesquisa = new Integer(1);
	private Barragem barragemEmFoco;
	private List<Barragem> barragensQueAdministro;
	private ListDataModel barragensQueParticipo;
	private Paginavel<Barragem> paginacaoBarragens;

	public GerirBarragemBean() {
		lista();
		paginacaoBarragens = new PaginavelSampleImpl<Barragem>((List<Barragem>) barragensQueParticipo.getWrappedData());
		barragensQueParticipo = new ListDataModel(paginacaoBarragens.getPagina());
	}

	public List<Barragem> getBarragensQueAdministro() {
		return barragensQueAdministro;
	}

	public ListDataModel getBarragensQueParticipo() {
		return barragensQueParticipo;
	}

	public Paginavel<Barragem> getPaginacaoBarragens() {
		return paginacaoBarragens;
	}

	public void setPaginacaoBarragens(Paginavel<Barragem> paginacaoBarragens) {
		this.paginacaoBarragens = paginacaoBarragens;
	}

	public Barragem getBarragemEmFoco() {
		return barragemEmFoco;
	}

	public void setBarragemEmFoco(Barragem barragemEmFoco) {
		this.barragemEmFoco = barragemEmFoco;
	}

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public String getPesquisaSalva() {
		return pesquisaSalva;
	}

	public void setPesquisaSalva(String pesquisaSalva) {
		this.pesquisaSalva = pesquisaSalva;
	}

	public Integer getTipoPesquisa() {
		return tipoPesquisa;
	}

	public void setTipoPesquisa(Integer tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
	}

	public void removeBarragem(ActionEvent e) {
		Barragem barragemARemover = barragensQueAdministro.get(getIndex());
		PersistenceHelper.remove(barragemARemover);
		barragensQueAdministro.remove(barragemARemover);
	}

	public void novaBarragem(ActionEvent e) {
		barragemEmFoco = new Barragem();
		barragemEmFoco.setAdministrador(getUsuarioLogado());
	}

	public void salvaBarragem(ActionEvent e) {
		if (valida(barragemEmFoco)) {
			try {
				getBo(BarragemBo.class).salva(barragemEmFoco);
				lista();
				addMensagemAtualizacaoComSucesso();
				editaCiclo(e);
			} catch (ConstraintViolationException e1) {
				messages.addErrorMessage("label_barragem_jah_existente", "label_barragem_jah_existente");
			}
		}
	}

	private void lista() {
		barragensQueAdministro = PersistenceHelper.findByNamedQuery("barragensQueAdministroQuery", getUsuarioLogado());
		barragensQueParticipo = new ListDataModel(PersistenceHelper.findByNamedQuery("barragensQueParticipoQuery",
				getUsuarioLogado()));
	}

	public void detalhaBarragem(ActionEvent e) {
		GerirCicloBean gerirCiclo = (GerirCicloBean) getRequestAttribute("gerirCicloBean");
		barragemEmFoco = (Barragem) gerirCiclo.getBarragemEmFoco().clone();
	}

	public void editaCiclo(ActionEvent e) {
		if (getIndex() != -1) {
			barragemEmFoco = (Barragem) PersistenceHelper.findByPk(Barragem.class, barragensQueAdministro.get(
					getIndex()).getId(), "ciclos");
		} else {
			GerirBarragemBean bean = (GerirBarragemBean) getRequestAttribute("gerirBarragemBean");
			barragemEmFoco = bean.getBarragemEmFoco();
		}
		GerirCicloBean cicloBean = new GerirCicloBean();
		cicloBean.carregaUltimoCiclo(barragemEmFoco);

		setRequestAttribute("gerirCicloBean", cicloBean);
	}

	public void exibePainelBarragem(ActionEvent e) {
		Barragem barragem = (Barragem) PersistenceHelper.findByPk(Barragem.class, barragensQueAdministro
				.get(getIndex()).getId(), "ciclos");

		ExibirPainelBarragemBean painelBarragemBean = new ExibirPainelBarragemBean();
		painelBarragemBean.setCicloEmFoco(barragem.getCiclos().get(0));
		setRequestAttribute("exibirPainelBarragemBean", painelBarragemBean);
	}

	public String pesquisaBarragem() {
		if (tipoPesquisa.equals(new Integer(1))) {
			PesquisarBean pesquisarBean = new PesquisarBean();
			setRequestAttribute("pesquisarBean", pesquisarBean);
			pesquisaSalva = null;
			return pesquisarBean.pesquisaBarragem(pesquisa);
		}
		if (pesquisa == null || pesquisa.length() == 0) {
			messages.addErrorMessage(null, "label_insira_um_texto_para_pesquisa");
			return "";
		}
		List<Barragem> resultado = PersistenceHelper.findByNamedQuery("pesquisaBarragemDeUsuarioQuery",
				getUsuarioLogado(), new StringBuilder().append("%").append(pesquisa).append("%").toString()
						.toUpperCase());
		if (resultado.isEmpty()) {
			messages.addInfoMessage("label_nenhum_resultado_encontrado", "label_nenhum_resultado_encontrado");
		} else {
			paginacaoBarragens = new PaginavelSampleImpl<Barragem>(resultado);
			barragensQueParticipo = new ListDataModel(paginacaoBarragens.getPagina());
			pesquisaSalva = pesquisa;
		}
		return "";
	}

	public void limpaFiltro(ActionEvent e) {
		pesquisaSalva = null;
		paginacaoBarragens = new PaginavelSampleImpl<Barragem>(paginacaoBarragens.getSourceList());
		barragensQueParticipo = new ListDataModel(paginacaoBarragens.getPagina());
	}
}