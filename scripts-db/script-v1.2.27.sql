CREATE TABLE barragem_net.bonus (
  id int(10) unsigned NOT NULL auto_increment,
  justificativa varchar(45) NOT NULL,
  valor int not null,
  rodada_id int(10) unsigned NOT NULL,
  jogador_id int(10) unsigned NOT NULL,
  PRIMARY KEY (id),
  KEY FK_bonus_rodada (rodada_id),
  KEY FK_bonus_jogador (jogador_id),
  CONSTRAINT FK_bonus_rodada FOREIGN KEY (rodada_id) REFERENCES rodada (id),
  CONSTRAINT FK_bonus_jogador FOREIGN KEY (jogador_id) REFERENCES jogador (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
