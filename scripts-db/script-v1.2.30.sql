alter table perfil add usuarioTwitter varchar(100);

alter table evento add column usuarioResponsavel_id int unsigned;

alter table evento add foreign key (usuarioResponsavel_id) references usuario(id);

update evento set usuarioResponsavel_id = 115;

update evento set usuarioResponsavel_id = 201 where id = 111;

update evento set usuarioResponsavel_id = 113 where id in (9, 113);

update evento set usuarioResponsavel_id = 155 where id in (112, 114, 115, 116, 117);

alter table evento CHANGE usuarioResponsavel_id usuarioResponsavel_id int unsigned not null;

alter table mensagem add column lida bit(1);

update mensagem set lida = 1;

update mensagem set lida = 0 where exists (select 1 from usuario u where mensagem.destinatario_id = u.id and mensagem.data > u.dataUltimoAcesso);

alter table mensagem CHANGE lida lida bit(1) not null;

alter table mensagem add column privada bit(1);

update mensagem set privada = 0;

alter table mensagem CHANGE privada privada bit(1) not null;