drop index `Unique-Nome-Local-Usuario_id` on barragem_net.barragem;

alter table barragem_net.barragem drop column nome;

alter table barragem_net.barragem add column categoria_id int unsigned not null;

update barragem_net.barragem set categoria_id = 3 where id = 1;

CREATE UNIQUE INDEX `Unique-Categoria_id-Local-Usuario_id` using btree ON barragem_net.barragem (categoria_id, local, usuario_id);

alter table barragem_net.barragem add foreign key (categoria_id) references categoria(id);