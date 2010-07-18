CREATE TABLE  `barragem_net`.`treino` (
  `id` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `FK_treino_evento` FOREIGN KEY (`id`) REFERENCES `evento` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

alter table barragem_net.evento add column hora datetime default NULL;

ALTER TABLE `barragem_net`.`jogadorevento` MODIFY COLUMN `comentario` VARCHAR(255) DEFAULT NULL;