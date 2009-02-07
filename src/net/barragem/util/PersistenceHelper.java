package net.barragem.util;

import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class PersistenceHelper {

	public static void persiste(Object entidade) {
		Session session = null;
		Transaction t = null;
		try {
			session = HibernateUtil.getSession();
			t = session.beginTransaction();
			session.save(entidade);
			t.commit();
		} catch (PersistenceException pe) {
			t.rollback();
			throw pe;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public static List find(String query) {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			return session.createQuery(query).list();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
