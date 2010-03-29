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