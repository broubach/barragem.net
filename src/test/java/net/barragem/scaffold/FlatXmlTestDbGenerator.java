package net.barragem.scaffold;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class FlatXmlTestDbGenerator {

	public static void main(String[] args) throws Exception {
		Connection jdbcConnection = null;
		try {
			Class driverClass = Class.forName("com.mysql.jdbc.Driver");
			jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/barragem_net", "barra_gem",
					"Ba2Nc8");
			criaJogadoresReais(jdbcConnection);
			criaGestorBarragem(jdbcConnection);
		} finally {
			if (jdbcConnection != null) {
				jdbcConnection.close();
			}
		}
	}

	private static void criaJogadoresReais(Connection jdbcConnection) throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

		QueryDataSet partialDataSet = new QueryDataSet(connection);
		partialDataSet
				.addTable(
						"USUARIO",
						"select u.* from USUARIO u where u.id in (select j.usuariocorrespondente_id from JOGADOR j where j.usuariodono_id = 115 and j.usuariocorrespondente_id is not null and j.usuariocorrespondente_id <> 115)");
		partialDataSet
				.addTable(
						"PERFIL",
						"select p.* from PERFIL p where p.usuario_id in (select j.usuariocorrespondente_id from JOGADOR j where j.usuariodono_id = 115 and j.usuariocorrespondente_id is not null and j.usuariocorrespondente_id <> 115)");
		partialDataSet
				.addTable(
						"JOGADOR",
						"select j.* from JOGADOR j where j.ID in (select u.JOGADOR_ID from USUARIO u join JOGADOR j2 on u.id = j2.usuariocorrespondente_id where j2.usuariodono_id = 115 and j2.usuariocorrespondente_id is not null and j2.usuariocorrespondente_id <> 115)");
		partialDataSet
				.addTable(
						"ARQUIVO",
						"select a.* from ARQUIVO a where a.DONO_ID in (select j.usuariocorrespondente_id from JOGADOR j where j.usuariodono_id = 115 and j.usuariocorrespondente_id is not null and j.usuariocorrespondente_id <> 115)");

		FlatXmlDataSet.write(partialDataSet, new FileOutputStream("test/jogadoresBarragens.xml"));
	}

	private static void criaGestorBarragem(Connection jdbcConnection) throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

		QueryDataSet partialDataSet = new QueryDataSet(connection);
		partialDataSet.addTable("USUARIO", "select u.* from USUARIO u where u.id = 115");
		partialDataSet.addTable("PERFIL", "select p.* from PERFIL p where p.usuario_id = 115");
		partialDataSet.addTable("JOGADOR",
				"select j.* from JOGADOR j where j.USUARIODONO_ID = 115 and j.USUARIOCORRESPONDENTE_ID is null");
		partialDataSet.addTable("ARQUIVO", "select a.* from ARQUIVO a where a.DONO_ID = 115");

		FlatXmlDataSet.write(partialDataSet, new FileOutputStream("test/gestorBarragem.xml"));
	}

	private static void criaBarragens(Connection jdbcConnection) throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

		QueryDataSet partialDataSet = new QueryDataSet(connection);
		partialDataSet.addTable("CICLO", "select u.* from USUARIO u where u.id = 115");
		partialDataSet.addTable("CATEGORIA", "select p.* from PERFIL p where p.usuario_id = 115");
		partialDataSet.addTable("BARRAGEM",
				"select j.* from JOGADOR j where j.USUARIODONO_ID = 115 and j.USUARIOCORRESPONDENTE_ID is null");
		partialDataSet.addTable("RODADA", "select p.* from PERFIL p where p.usuario_id = 115");
		partialDataSet.addTable("PLACAR", "select p.* from PERFIL p where p.usuario_id = 115");
		partialDataSet.addTable("PARCIAL", "select p.* from PERFIL p where p.usuario_id = 115");

		partialDataSet.addTable("CICLOJOGADOR", "select p.* from PERFIL p where p.usuario_id = 115");

		partialDataSet.addTable("EVENTO", "select p.* from PERFIL p where p.usuario_id = 115");
		partialDataSet.addTable("JOGO", "select p.* from PERFIL p where p.usuario_id = 115");
		partialDataSet.addTable("JOGOBARRAGEM", "select p.* from PERFIL p where p.usuario_id = 115");

		partialDataSet.addTable("JOGADOREVENTO", "select p.* from PERFIL p where p.usuario_id = 115");
		partialDataSet.addTable("JOGADORJOGO", "select p.* from PERFIL p where p.usuario_id = 115");
		partialDataSet.addTable("JOGADORJOGOBARRAGEM", "select p.* from PERFIL p where p.usuario_id = 115");

		FlatXmlDataSet.write(partialDataSet, new FileOutputStream("test/barragens.xml"));
	}
}
