alter table ciclojogador add pontuacaoCongelada int null;

alter table ciclojogador add pontuacaoCongeladaExcedente int null;

alter table ciclojogador CHANGE habilitado congelado bit not null;

update ciclojogador set congelado = !congelado;