package net.barragem.view.backingbean.componentes;

import net.barragem.persistence.entity.JogadorEvento;
import net.barragem.persistence.entity.JogoBarragem;

public class JogadorEventoComparatorVencedorPrimeiroBarragem extends JogadorEventoComparatorVencedorPrimeiro {
    public int compare(JogadorEvento o1, JogadorEvento o2) {
        if (o1.getEvento() instanceof JogoBarragem && !((JogoBarragem) o1.getEvento()).possuiVencedores()) {
            return o2.getJogador().getPontuacaoGeral().compareTo(o1.getJogador().getPontuacaoGeral());
        }
        return super.compare(o1, o2);
    }
}
