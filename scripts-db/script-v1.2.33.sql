alter table ciclojogador add pontuacaoCongelada int null;

alter table ciclojogador add rodadaDescongelamento_id int(10) unsigned null;

ALTER TABLE ciclojogador ADD CONSTRAINT FK_ciclojogador_rodada FOREIGN KEY FK_ciclojogador_rodada (rodadaDescongelamento_id)
    REFERENCES rodada (id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;

alter table ciclojogador CHANGE habilitado congelado bit not null;

update ciclojogador set congelado = !congelado;