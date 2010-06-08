CREATE TABLE barragem_net.atualizacaotwitter (
  id int(10) unsigned NOT NULL auto_increment,
  texto varchar(140) NOT NULL,
  data datetime NOT NULL,
  dataGravacao datetime NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into barragem_net.atualizacaotwitter (texto, `data`, dataGravacao) values ('Versãozinha da semana liberada..', now(), now());