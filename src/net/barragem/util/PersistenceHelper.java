package net.barragem.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.PersistenceException;

import net.barragem.persistence.entity.BaseEntity;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sun.org.apache.commons.beanutils.BeanUtils;

public class PersistenceHelper {

	public static void persiste(BaseEntity entidade) {
		Session session = null;
		Transaction t = null;
		try {
			session = HibernateUtil.getSession();
			t = session.beginTransaction();
			if (entidade.getId() != null) {
				session.update(entidade);
			} else {
				session.save(entidade);
			}

			session.merge(entidade);
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

	public static List findByNamedQuery(String namedQuery, Object... paramValues) {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.getNamedQuery(namedQuery);
			if (paramValues != null) {
				for (int i = 0; i < query.getNamedParameters().length; i++) {
					query.setParameter(query.getNamedParameters()[i], paramValues[i]);
				}
			}
			return query.list();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public static void remove(Object entity) {
		Session session = null;
		Transaction t = null;
		try {
			session = HibernateUtil.getSession();
			t = session.beginTransaction();
			session.delete(entity);
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

	public static Object findByPk(Class clazz, Integer id, String... collectionsToLoad) {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Object entity = session.get(clazz, id);
			if (collectionsToLoad != null) {
				for (String collection : collectionsToLoad) {
					Hibernate.initialize(BeanUtils.getProperty(entity, collection));
				}
			}

			return entity;
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
