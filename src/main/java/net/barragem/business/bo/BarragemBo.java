package net.barragem.business.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.AcaoEnum;
import net.barragem.persistence.entity.Atualizacao;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.Evento;
import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.scaffold.PersistenceHelper;

public class BarragemBo extends BaseBo {

    public BarragemBo(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void salva(Barragem barragem) {
        AcaoEnum acaoEnum = null;
        if (barragem.getId() == null) {
            acaoEnum = AcaoEnum.CriarBarragem;
        }
        PersistenceHelper.persiste(barragem);
        if (acaoEnum != null) {
            PersistenceHelper.persiste(barragem.criaCicloERodada());
            PersistenceHelper.persiste(Atualizacao.criaCriarBarragem(getUsuarioLogado(), barragem));
        }
    }

    public void atribuiPontuacaoGeralAJogadoresDoCiclo(Ciclo ciclo) {
        Map<Integer, Integer> pontuacaoPorJogador = obtemPontuacaoPorJogador((List<Object[]>) PersistenceHelper
                .findByNamedQuery("pontosPorJogadorIdQuery", ciclo.getId()));

        for (CicloJogador cicloJogador : ciclo.getRanking()) {
            cicloJogador.getJogador().setPontuacaoGeral(pontuacaoPorJogador.get(cicloJogador.getJogador().getId()));
        }
    }

    private Map<Integer, Integer> obtemPontuacaoPorJogador(List<Object[]> pontuacaoPorJogador) {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (Object[] obj : pontuacaoPorJogador) {
            result.put((Integer) obj[0], (obj[1] == null ? 0 : ((BigDecimal) obj[1]).intValue())
                    + (obj[2] == null ? 0 : ((BigDecimal) obj[2]).intValue()));
        }
        return result;
    }

    public void atribuiPontuacaoGeralAJogadoresDeJogosBarragem(List<? extends Evento> eventos) {
        Set<JogoBarragem> jogosBarragem = new HashSet<JogoBarragem>();
        for (Evento evento : eventos) {
            if (evento instanceof JogoBarragem) {
                jogosBarragem.add((JogoBarragem) evento);
            }
        }

        Map<Integer, List<JogoBarragem>> jogosBarragemPorCicloId = new HashMap<Integer, List<JogoBarragem>>();
        for (JogoBarragem evento : jogosBarragem) {
            if (jogosBarragemPorCicloId.get(evento.getRodada().getCiclo().getId()) == null) {
                jogosBarragemPorCicloId.put(evento.getRodada().getCiclo().getId(), new ArrayList<JogoBarragem>());
            }
            jogosBarragemPorCicloId.get(evento.getRodada().getCiclo().getId()).add(evento);
        }

        Set<Integer> jogadorIds = null;
        Map<Integer, Integer> pontuacaoPorJogador = null;
        for (Integer cicloId : jogosBarragemPorCicloId.keySet()) {
            jogadorIds = new HashSet<Integer>();
            for (JogoBarragem evento : jogosBarragemPorCicloId.get(cicloId)) {
                for (JogadorEvento jogadorEvento : evento.getJogadoresEventos()) {
                    jogadorIds.add(jogadorEvento.getJogador().getId());
                }
            }

            pontuacaoPorJogador = obtemPontuacaoPorJogador((List<Object[]>) PersistenceHelper.findByNamedQuery(
                    "pontosPorJogadorIdsEspecificosQuery", cicloId, jogadorIds));
            for (JogoBarragem evento : jogosBarragemPorCicloId.get(cicloId)) {
                for (JogadorEvento jogadorEvento : evento.getJogadoresEventos()) {
                    jogadorEvento.getJogador().setPontuacaoGeral(
                            pontuacaoPorJogador.get(jogadorEvento.getJogador().getId()));
                }
            }
        }
    }
}
