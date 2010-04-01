CREATE TABLE barragem_net.atualizacao (
  id int(10) unsigned NOT NULL auto_increment,
  sujeitoClassName varchar(255) NOT NULL,
  sujeitoId int(10) unsigned NOT NULL,
  acao int(10) unsigned NOT NULL,
  objetoClassName varchar(255) default NULL,
  objetoId int(10) unsigned default NULL,
  predicado varchar(45) default NULL,
  isPredicadoAkey bit(1) default NULL,
  data datetime NOT NULL,
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE  barragem_net.predicado (
  id int(10) unsigned NOT NULL auto_increment,
  predicado varchar(45) NOT NULL,
  isPredicadoAkey bit(1) NOT NULL,
  predicadoValue varchar(45) default NULL,
  tipoPredicadoValue int(10) default NULL,
  predicadoValueId int(10) default NULL,
  atualizacao_id int(10) unsigned NOT NULL,
  PRIMARY KEY  (id),
  KEY FK_predicado_atualizacao (atualizacao_id),
  CONSTRAINT FK_predicado_atualizacao FOREIGN KEY (atualizacao_id) REFERENCES atualizacao (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;