package net.barragem.view.backingbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.barragem.business.bo.RelatorioBo;
import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;
import net.barragem.persistence.entity.Rodada;
import net.barragem.scaffold.PersistenceHelper;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class ExibirPainelBarragemBean extends BaseBean {

    private Ciclo cicloEmFoco;
    private Integer startIndex = new Integer(0);
    private Integer endIndex = new Integer(1);
    private Integer numeroRodada;

    private List<Rodada> rodadas;
    private List<CicloJogador> ranking;
    private List<Ciclo> ciclos;

    public ExibirPainelBarragemBean() {
        ciclos = PersistenceHelper.findByNamedQuery("ciclosDeBarragemQuery", getId());

        cicloEmFoco = (Ciclo) PersistenceHelper.findByNamedQuery("ultimoCicloPorBarragemQuery", getId()).get(0);

        PersistenceHelper.initialize("rodadas", cicloEmFoco);
        rodadas = new ArrayList<Rodada>(cicloEmFoco.getRodadas());
        Collections.reverse(rodadas);
        getBo(RelatorioBo.class).inicializaRodadas(startIndex, endIndex, getRodadas());

        PersistenceHelper.initialize("ranking", cicloEmFoco);
        ranking = new ArrayList<CicloJogador>(cicloEmFoco.getRanking());
        for (Iterator<CicloJogador> it = ranking.iterator(); it.hasNext();) {
            if (it.next().isCongelado()) {
                it.remove();
            }
        }
    }

    public Ciclo getCicloEmFoco() {
        return cicloEmFoco;
    }

    public void setCicloEmFoco(Ciclo cicloEmFoco) {
        this.cicloEmFoco = cicloEmFoco;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer beginIndex) {
        this.startIndex = beginIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Integer getNumeroRodada() {
        return numeroRodada;
    }

    public void setNumeroRodada(Integer numeroRodada) {
        this.numeroRodada = numeroRodada;
    }

    public List<Rodada> getRodadas() {
        return rodadas;
    }

    public void setRodadas(List<Rodada> rodadas) {
        this.rodadas = rodadas;
    }

    public List<CicloJogador> getRanking() {
        return ranking;
    }

    public void setRanking(List<CicloJogador> ranking) {
        this.ranking = ranking;
    }

    public List<Ciclo> getCiclos() {
        return ciclos;
    }

    public void setCiclos(List<Ciclo> ciclos) {
        this.ciclos = ciclos;
    }

    public void visualizaCiclo(ActionEvent e) {
        if (getIndex() != -1) {
            Ciclo ciclo = cicloEmFoco.getBarragem().getCiclos().get(getIndex());
            cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, ciclo.getId(), "ranking", "rodadas");
        } else {
            cicloEmFoco = (Ciclo) PersistenceHelper.findByPk(Ciclo.class, cicloEmFoco.getId(), "ranking", "rodadas");
        }
        PersistenceHelper.initialize("ciclos", cicloEmFoco.getBarragem());
    }

    public String visualizaRodada() {
        if (numeroRodada != null && numeroRodada > 0 && numeroRodada <= cicloEmFoco.getRodadas().size()) {
            startIndex = cicloEmFoco.getRodadas().size() - numeroRodada;
            endIndex = startIndex + 1;
        } else {
            numeroRodada = cicloEmFoco.getRodadas().size();
            startIndex = 0;
            endIndex = 1;
        }
        getBo(RelatorioBo.class).inicializaRodadas(startIndex, endIndex, getRodadas());
        refreshView();

        return null;
    }
}