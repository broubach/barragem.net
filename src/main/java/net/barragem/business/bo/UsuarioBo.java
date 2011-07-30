package net.barragem.business.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Jogador;
import net.barragem.persistence.entity.Perfil;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

public class UsuarioBo extends BaseBo {

	public UsuarioBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void carregaFotosUsuarios(List<Usuario> usuarios) {
		Map<Integer, Usuario> usuarioPorIdPerfil = new HashMap<Integer, Usuario>();

		for (Usuario usuario : usuarios) {
			if (usuario.getPerfil() != null && usuario.getPerfil().getHash() != null
					&& !PersistenceHelper.isInitialized(usuario.getPerfil(), "foto")) {
				usuarioPorIdPerfil.put(usuario.getPerfil().getId(), usuario);
			}
		}

		if (!usuarioPorIdPerfil.isEmpty()) {
			List<Object[]> objs = PersistenceHelper.findByNamedQuery("perfilFetchFotoQuery", usuarioPorIdPerfil
					.keySet());

			Usuario usuario = null;
			Perfil perfil = null;
			for (Object[] obj : objs) {
				perfil = (Perfil) obj[0];
				perfil.setFoto(new Arquivo());
				perfil.getFoto().setId((Integer) obj[1]);
				perfil.getFoto().setTamanho((Integer) obj[2]);
				usuario = usuarioPorIdPerfil.get(perfil.getId());
				usuario.setPerfil(perfil);
			}
		}
	}

	public void carregaFotosJogadores(List<Jogador> jogadores) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		for (Jogador jogador : jogadores) {
			if (jogador.getUsuarioCorrespondente() != null) {
				usuarios.add(jogador.getUsuarioCorrespondente());
			}
		}
		carregaFotosUsuarios(usuarios);
	}
}
