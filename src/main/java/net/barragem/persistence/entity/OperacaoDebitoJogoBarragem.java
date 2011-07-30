package net.barragem.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "operacaodebitojogobarragem")
public class OperacaoDebitoJogoBarragem extends Operacao {

}