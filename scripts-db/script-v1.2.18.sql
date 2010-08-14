update usuario set nome = trim(nome), sobrenome = trim(sobrenome);

update jogador set nome = (select concat(concat(u.nome, ' '), u.sobrenome) from usuario u where u.id = usuariocorrespondente_id) where usuariocorrespondente_id is not null;