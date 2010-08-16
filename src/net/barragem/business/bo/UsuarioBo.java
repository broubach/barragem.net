package net.barragem.business.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.barragem.persistence.entity.Perfil;
import net.barragem.persistence.entity.Usuario;
import net.barragem.scaffold.PersistenceHelper;

public class UsuarioBo extends BaseBo {

	public UsuarioBo(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void carregaFotos(List<Usuario> usuarios) {
		Map<Integer, Usuario> usuarioPorIdPerfil = new HashMap<Integer, Usuario>();

		for (Usuario usuario : usuarios) {
			if (usuario.getPerfil() != null && usuario.getPerfil().getHash() != null) {
				usuarioPorIdPerfil.put(usuario.getPerfil().getId(), usuario);
			}
		}

		if (!usuarioPorIdPerfil.isEmpty()) {
			List<Perfil> loadedPerfis = PersistenceHelper.findByNamedQuery("perfilFetchFotoQuery", usuarioPorIdPerfil
					.keySet());

			Usuario usuario = null;
			for (Perfil loadedPerfil : loadedPerfis) {
				usuario = usuarioPorIdPerfil.get(loadedPerfil.getId());
				usuario.setPerfil(loadedPerfil);
			}
		}
	}
}
