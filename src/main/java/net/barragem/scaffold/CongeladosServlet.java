package net.barragem.scaffold;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.business.bo.JogadorBo;
import net.barragem.persistence.entity.Barragem;
import net.barragem.persistence.entity.Ciclo;
import net.barragem.persistence.entity.CicloJogador;

public class CongeladosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JogadorBo jogadorBo = new JogadorBo(req, resp);
        for (Barragem barragem : PersistenceHelper.findAll(Barragem.class)) {
            PersistenceHelper.initialize("ciclos", barragem);
            for (Ciclo ciclo : barragem.getCiclos()) {
                PersistenceHelper.initialize("ranking", ciclo);
                PersistenceHelper.initialize("rodadas", ciclo);
                for (CicloJogador cicloJogador : ciclo.getCicloJogadoresCongelados()) {
                    jogadorBo.congelaJogador(cicloJogador, false);
                }
                PersistenceHelper.persiste(ciclo);
            }
        }
    }
}
