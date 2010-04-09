package net.barragem.business.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.BaseEntity;
import net.barragem.persistence.entity.Conta;
import net.barragem.persistence.entity.Predicado;
import net.barragem.persistence.entity.TipoPredicadoValueEnum;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

public class LoginBo extends BaseBo {

	public LoginBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void login(Usuario usuario) {
		// atualiza hora do ultimo acesso
		usuario.setDataPenultimoAcesso(usuario.getDataUltimoAcesso());
		usuario.setDataUltimoAcesso(new Date());
		PersistenceHelper.persiste(usuario);
		PersistenceHelper.initialize("jogadores", usuario);
		// coloca usuario na sessao
		setUsuarioLogado(usuario);
		// coloca conta do usuario na sessao
		setContaUsuario((Conta) PersistenceHelper.findByNamedQuery("findContaPorUsuarioQuery", usuario).get(0));

		// carrega atualizacoes
		List<Atualizacao> atualizacoes = PersistenceHelper.findByNamedQueryWithLimits("atualizacaoPaginaInicialQuery",
				0, 10, getUsuarioLogado().getId());
		if (!atualizacoes.isEmpty()) {
			Map<Integer, Atualizacao> mapAtualizacoes = new HashMap<Integer, Atualizacao>();
			for (Atualizacao atualizacao : atualizacoes) {
				mapAtualizacoes.put(atualizacao.getId(), atualizacao);
				atualizacao.setPredicados(new ArrayList<Predicado>());
			}
			List<Object[]> predicados = PersistenceHelper.findByNamedQuery("predicadoPaginaInicialQuery", atualizacoes);
			for (Object[] o : predicados) {
				mapAtualizacoes.get(o[0]).getPredicados().add((Predicado) o[1]);
			}
			for (Atualizacao atualizacao : atualizacoes) {
				Collections.sort(atualizacao.getPredicados(), new Comparator<Predicado>() {
					@Override
					public int compare(Predicado o1, Predicado o2) {
						return o1.getPredicado().compareTo(o2.getPredicado()) * -1;
					}
				});
			}
		}
		carregaAtualizacoes(atualizacoes);
		setAtualizacoes(atualizacoes);
	}

	public void carregaAtualizacoes(List<Atualizacao> atualizacoes) {
		try {
			for (Atualizacao atualizacao : atualizacoes) {
				atualizacao.setLoadedSujeito((BaseEntity) PersistenceHelper.findByPk(Class.forName(atualizacao
						.getSujeitoClassName()), atualizacao.getSujeitoId()));
				if (atualizacao.getObjetoId() != null) {
					atualizacao.setLoadedObjeto((BaseEntity) PersistenceHelper.findByPk(Class.forName(atualizacao
							.getObjetoClassName()), atualizacao.getObjetoId()));
				}
				for (Predicado predicado : atualizacao.getPredicados()) {
					if (predicado.getTipoPredicadoValue() != null
							&& predicado.getTipoPredicadoValue().equals(TipoPredicadoValueEnum.Clazz)) {
						predicado.setLoadedPredicado((BaseEntity) PersistenceHelper.findByPk(Class.forName(predicado
								.getPredicadoValue()), predicado.getPredicadoValueId()));
					}
				}
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}