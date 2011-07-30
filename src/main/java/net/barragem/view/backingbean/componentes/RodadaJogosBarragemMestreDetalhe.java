package net.barragem.view.backingbean.componentes;

import java.util.Collections;

import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Jogo;
import net.barragem.persistence.entity.JogoBarragem;
import net.barragem.persistence.entity.Rodada;

public class RodadaJogosBarragemMestreDetalhe extends MestreDetalheImpl<Rodada, JogoBarragem> {

    public Jogador jogadorVencedorWo;

    public Jogador getJogadorVencedorWo() {
        return jogadorVencedorWo;
    }

    public void setJogadorVencedorWo(Jogador jogadorVencedorWo) {
        this.jogadorVencedorWo = jogadorVencedorWo;
    }

    public void preparaJogoParaEdicao(int index) {
        preparaJogoParaEdicao(getDetalhes().get(index));
    }

    public void preparaJogoParaEdicao() {
        preparaJogoParaEdicao(getDetalheEmFoco());
    }

    public void preparaJogoParaEdicao(JogoBarragem jogoBarragem) {
        setDetalheEmFoco(jogoBarragem);
        setMestre(getDetalheEmFoco().getRodada());
        setDetalhes(getMestre().getJogos());

        ordena();
        getDetalheEmFoco().getPlacar().completaSetsSeNecessario(
                getMestre().getCiclo().getParametros().getModalidadeDeSets().getNumeroDeSets());
    }

    private void ordena() {
        for (Jogo jogo : getDetalhes()) {
            Collections.sort(jogo.getJogadoresEventos(), new JogadorEventoComparatorVencedorPrimeiro());
        }
        Collections.sort(getDetalhes(), new JogoBarragemComparator());
    }

    public void editaProximoJogo() {
        Integer i = getIndiceNosDetalhes();
        if (i != null && i.intValue() + 1 < getDetalhes().size()) {
            preparaJogoParaEdicao(i.intValue() + 1);
        }
    }

    public void editaJogoAnterior() {
        Integer i = getIndiceNosDetalhes();
        if (i != null && i.intValue() - 1 >= 0) {
            preparaJogoParaEdicao(i.intValue() - 1);
        }
    }

    public void adicionaDetalheNaLista() {
        if (getDetalhes().contains(getDetalheEmFoco())) {
            getDetalhes().remove(getDetalheEmFoco());
        }
        getDetalhes().add(getDetalheEmFoco());
    }
}