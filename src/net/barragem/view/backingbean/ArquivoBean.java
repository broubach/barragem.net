package net.barragem.view.backingbean;

import java.io.IOException;
import java.io.OutputStream;

import net.barragem.persistence.entity.Arquivo;
import net.barragem.persistence.entity.Perfil;
import net.barragem.util.PersistenceHelper;

public class ArquivoBean extends BaseBean {

	public void paintFoto(OutputStream stream, Object object) throws IOException {
		if (object instanceof Perfil) {
			PersistenceHelper.initialize("foto", object);
			stream.write(((Perfil) object).getFoto().getDado());
		} else if (object instanceof Arquivo) {
			stream.write(((Arquivo) object).getDado());
		}
	}
}
