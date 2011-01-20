package net.barragem.persistence.entity;

import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.barragem.scaffold.ReflectionHelper;

@Entity
@NamedQueries({
        @NamedQuery(name = "totalJogadoresDeUsuarioQuery", query = "select count(*) from Jogador where usuarioDono = :usuario"),
        @NamedQuery(name = "jogadoresPorUsuarioDonoQuery", query = "from Jogador j where j.usuarioDono.id=:idUsuarioDono and upper(j.nome) like upper(:nome) order by j.nome"),
        @NamedQuery(name = "pesquisaJogadorQuery", query = "select u.jogador from Usuario u join u.jogador j left outer join u.perfil p where upper(j.nome) like :p or upper(p.clubeMaisFrequentadoNome) like :p or upper(p.clubeMaisFrequentadoCidade) like :p or upper(p.professorNome) like :p or upper(p.raquete) like :p"),
        @NamedQuery(name = "pesquisaJogadorForaDaListaQuery", query = "select u.jogador from Usuario u left outer join u.perfil p where (upper(u.jogador.nome) like :p or upper(u.nome) like :p or upper(u.sobrenome) like :p) and u <> :u and u not in (select uc from Jogador j join j.usuarioCorrespondente uc where j.usuarioDono = :u)"),
        @NamedQuery(name = "pesquisaJogadorDeUsuarioQuery", query = "select j from Usuario u join u.jogadores j left outer join j.usuarioCorrespondente uc left outer join uc.perfil p where (upper(uc.nome) like :p1 or upper(uc.sobrenome) like :p1 or upper(p.clubeMaisFrequentadoNome) like :p1 or upper(p.clubeMaisFrequentadoCidade) like :p1 or upper(p.professorNome) like :p1 or upper(p.raquete) like :p1 or upper(j.nome) like :p1) and u = :p2 and u.jogador <> j"),
        @NamedQuery(name = "atualizaNomesJogadoresQuery", query = "UPDATE Jogador j SET j.nome = :nome WHERE j.usuarioCorrespondente.id = :id") })
@NamedNativeQueries({
        @NamedNativeQuery(name = "pontosPorJogadorIdQuery", query = "select je.jogador_id jogador_id, sum(jjb.pontuacaoObtida) sum_pontuacaoObtida, sum(b.valor) sum_pontuacaoBonus from jogadorjogobarragem jjb join jogadorevento je on jjb.id = je.id join ciclojogador cj on je.jogador_id = cj.jogador_id left outer join bonus b on cj.jogador_id = b.jogador_id and b.rodada_id in (select r.id from rodada r where r.ciclo_id = :cicloId) where cj.ciclo_id = :cicloId group by je.jogador_id order by sum(pontuacaoObtida)", resultSetMapping = "pontosPorJogadorIdResult"),
        @NamedNativeQuery(name = "pontosPorJogadorIdsEspecificosQuery", query = "select je.jogador_id jogador_id, sum(jjb.pontuacaoObtida) sum_pontuacaoObtida, sum(b.valor) sum_pontuacaoBonus from jogadorjogobarragem jjb join jogadorevento je on jjb.id = je.id join ciclojogador cj on je.jogador_id = cj.jogador_id left outer join bonus b on cj.jogador_id = b.jogador_id and b.rodada_id in (select r.id from rodada r where r.ciclo_id = :cicloId) where cj.ciclo_id = :cicloId and je.jogador_id in (:jogadorIds) group by je.jogador_id order by sum(pontuacaoObtida)", resultSetMapping = "pontosPorJogadorIdResult") })
@SqlResultSetMapping(name = "pontosPorJogadorIdResult", columns = { @ColumnResult(name = "jogador_id"),
        @ColumnResult(name = "sum_pontuacaoObtida"), @ColumnResult(name = "sum_pontuacaoBonus") })
@Table(name = "jogador")
public class Jogador extends BaseEntity implements Atualizavel {
    @TextoAtualizacao
    private String nome;

    @OneToOne
    private Usuario usuarioCorrespondente;

    @ManyToOne
    private Usuario usuarioDono;

    @Transient
    private Boolean jahAdicionado;

    @Transient
    private Integer pontuacaoGeral;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getUsuarioCorrespondente() {
        return usuarioCorrespondente;
    }

    public void setUsuarioCorrespondente(Usuario usuario) {
        this.usuarioCorrespondente = usuario;
    }

    public Usuario getUsuarioDono() {
        return usuarioDono;
    }

    public void setUsuarioDono(Usuario usuarioDono) {
        this.usuarioDono = usuarioDono;
    }

    public Boolean getJahAdicionado() {
        return jahAdicionado;
    }

    public void setJahAdicionado(Boolean jahAdicionado) {
        this.jahAdicionado = jahAdicionado;
    }

    @Override
    public String getTextoAtualizacao() {
        return ReflectionHelper.getTextoAtualizacao(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nome == null) ? 0 : nome.trim().toUpperCase().hashCode());
        result = prime
                * result
                + ((usuarioCorrespondente == null || usuarioCorrespondente.getId() == null) ? 0 : usuarioCorrespondente
                        .getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Jogador other = (Jogador) obj;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equalsIgnoreCase(other.nome))
            return false;
        if (usuarioCorrespondente != null && usuarioCorrespondente.getId() == null) {
            if (other.usuarioCorrespondente != null && other.usuarioCorrespondente.getId() != null)
                return false;
        } else if (usuarioCorrespondente != null
                && other.usuarioCorrespondente != null
                && !usuarioCorrespondente.getId().equals(
                        other.usuarioCorrespondente != null ? other.usuarioCorrespondente.getId() : null))
            return false;
        return true;
    }

    public void setPontuacaoGeral(Integer pontuacaoGeral) {
        this.pontuacaoGeral = pontuacaoGeral;
    }

    public Integer getPontuacaoGeral() {
        return pontuacaoGeral;
    }
}