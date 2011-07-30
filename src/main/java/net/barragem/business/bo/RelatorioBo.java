package net.barragem.business.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Rodada;
import net.barragem.scaffold.PersistenceHelper;
import net.barragem.view.backingbean.componentes.EventoMaisRecenteComparator;
import net.barragem.view.backingbean.componentes.JogadorEventoComparatorVencedorPrimeiroBarragem;

public class RelatorioBo extends BaseBo {

    public RelatorioBo(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void inicializaRodadas(Integer startIndex, Integer endIndex, List<Rodada> rodadas) {
        if (startIndex == null || endIndex == null) {
            startIndex = 0;
            endIndex = rodadas.size() - 1;
        }
        for (int i = startIndex; i <= endIndex; i++) {
            if (i < rodadas.size()) {
                PersistenceHelper.initialize("bonuses", rodadas.get(i));
                PersistenceHelper.initialize("jogos", rodadas.get(i));
                getBo(BarragemBo.class).atribuiPontuacaoGeralAJogadoresDeJogosBarragem(rodadas.get(i).getJogos());
                for (Jogo jogoBarragem : rodadas.get(i).getJogos()) {
                    getBo(EventoBo.class).inicializaParcial(jogoBarragem,
                            new JogadorEventoComparatorVencedorPrimeiroBarragem());
                }
                rodadas.get(i).setJogosOrdenados(new ArrayList<JogoBarragem>(rodadas.get(i).getJogos()));
                Collections.sort(rodadas.get(i).getJogosOrdenados(), new EventoMaisRecenteComparator());
            }
        }
    }
}
