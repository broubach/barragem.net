alter table jogadorevento CHANGE comentario comentario varchar(1000) null;

insert into operacao (conta_id, quantidade, data)
select c.id, 150, now() from conta c join usuario o on c.proprietario_id = o.id where c.proprietario_id not in(113, 115);

insert into operacaocarga (id, valorMonetario)
select o.id, 600 from operacao o join conta c on o.conta_id = c.id where o.id not in(88, 101, 319) and c.proprietario_id not in(113, 115);
