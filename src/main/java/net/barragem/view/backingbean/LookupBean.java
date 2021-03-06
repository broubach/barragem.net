package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import net.barragem.persistence.entity.Categoria;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.LadoForehandEnum;
import net.barragem.persistence.entity.ModalidadeDeSetsEnum;
import net.barragem.persistence.entity.SexoEnum;
import net.barragem.persistence.entity.TipoBackhandEnum;
import net.barragem.scaffold.MessageBundleUtils;
import net.barragem.scaffold.PersistenceHelper;

public class LookupBean extends BaseBean {

	public List<SelectItem> getListaJogadores() {
		List<Jogador> jogadores = null;
		GerirJogoBarragemBean jogoBarragemBean = null;
		if ((jogoBarragemBean = (GerirJogoBarragemBean) getRequestAttribute("gerirJogoBarragemBean")) != null
				&& jogoBarragemBean.getMestreDetalhe().getDetalheEmFoco() != null) {
			jogoBarragemBean = (GerirJogoBarragemBean) getRequestAttribute("gerirJogoBarragemBean");
			jogadores = jogoBarragemBean.getMestreDetalhe().getMestre().getCiclo().getJogadoresDoRanking();

		} else {
			jogadores = PersistenceHelper.findByNamedQuery("jogadoresPorUsuarioDonoQuery", "%", getUsuarioLogado().getId());
		}

		List<SelectItem> items = new ArrayList<SelectItem>();
		Jogador jogador = null;
		for (Iterator<Jogador> it = jogadores.iterator(); it.hasNext();) {
			jogador = it.next();
			SelectItem selectItem = new SelectItem(jogador, jogador.getNome());
			items.add(selectItem);
		}
		return items;
	}

	public List<SelectItem> getListaCbCategorias() {
		List<Categoria> categorias = PersistenceHelper.findAll(Categoria.class);

		List<SelectItem> items = new ArrayList<SelectItem>();
		Categoria categoria = null;
		for (Iterator<Categoria> it = categorias.iterator(); it.hasNext();) {
			categoria = it.next();
			SelectItem selectItem = new SelectItem(categoria.getId(), MessageBundleUtils.getInstance().get(
					categoria.getNome()));
			items.add(selectItem);
		}
		return items;
	}

	public List<SelectItem> getListaCategorias() {
		List<Categoria> categorias = PersistenceHelper.findAll(Categoria.class);

		List<SelectItem> items = new ArrayList<SelectItem>();
		Categoria categoria = null;
		for (Iterator<Categoria> it = categorias.iterator(); it.hasNext();) {
			categoria = it.next();
			SelectItem selectItem = new SelectItem(categoria, MessageBundleUtils.getInstance().get(categoria.getNome()));
			items.add(selectItem);
		}
		return items;
	}

	public List<SelectItem> getModalidades() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(ModalidadeDeSetsEnum.MelhorDeTresSets, MessageBundleUtils.getInstance().get(
				"label_melhor_de_tres_sets")));
		items.add(new SelectItem(ModalidadeDeSetsEnum.MelhorDeTresSetsComSuperTiebreakNoUltimoSet, MessageBundleUtils
				.getInstance().get("label_melhor_de_tres_sets_com_super_tiebreak_no_ultimo_set")));
		items.add(new SelectItem(ModalidadeDeSetsEnum.SetProfissionalUnico, MessageBundleUtils.getInstance().get(
				"label_set_profissional_unico")));
		return items;
	}

	public List<SelectItem> getGeneros() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(SexoEnum.Homem, MessageBundleUtils.getInstance().get("label_homem")));
		items.add(new SelectItem(SexoEnum.Mulher, MessageBundleUtils.getInstance().get("label_mulher")));
		return items;
	}

	public List<SelectItem> getSexos() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(SexoEnum.Homem, MessageBundleUtils.getInstance().get("label_masculino")));
		items.add(new SelectItem(SexoEnum.Mulher, MessageBundleUtils.getInstance().get("label_feminino")));
		return items;
	}

	public List<SelectItem> getListaAnosDesde1900Desc() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = anoAtual; i >= 1900; i--) {
			items.add(new SelectItem(i, String.valueOf(i)));
		}
		return items;
	}

	public List<SelectItem> getListaLadosForehand() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(LadoForehandEnum.Direito, MessageBundleUtils.getInstance().get("label_direito")));
		items.add(new SelectItem(LadoForehandEnum.Esquerdo, MessageBundleUtils.getInstance().get("label_esquerdo")));
		return items;
	}

	public List<SelectItem> getListaTiposBackhand() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(TipoBackhandEnum.UmaMao, MessageBundleUtils.getInstance().get("label_uma_mao")));
		items.add(new SelectItem(TipoBackhandEnum.DuasMaos, MessageBundleUtils.getInstance().get("label_duas_maos")));
		return items;
	}
}
