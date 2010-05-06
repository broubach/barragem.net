CREATE TABLE barragem_net.mensagem (
  id int(10) unsigned NOT NULL auto_increment,
  destinatario_id int(10) unsigned NOT NULL,
  remetente_id int(10) unsigned NOT NULL,
  mensagem varchar(255) NOT NULL,
  data datetime NOT NULL,
  PRIMARY KEY  (id),
  KEY FK_mensagem_usuario1 (destinatario_id),
  KEY FK_mensagem_usuario2 (remetente_id),
  CONSTRAINT FK_mensagem_usuario1 FOREIGN KEY (destinatario_id) REFERENCES usuario (id),
  CONSTRAINT FK_mensagem_usuario2 FOREIGN KEY (remetente_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;