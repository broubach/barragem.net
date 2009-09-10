package net.barragem.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.PersistenceException;

import net.barragem.persistence.entity.BaseEntity;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

	public static <T> T findByPk(Class<T> clazz, Integer id, String... collectionsToLoad) {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Object entity = session.get(clazz, id);
			if (collectionsToLoad != null) {
				for (String collection : collectionsToLoad) {
					Hibernate.initialize(BeanUtils.getProperty(entity, collection));
				}
			}

			return (T) entity;
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

	public static void initialize(String collectionName, Object... proxys) {
		if (proxys == null) {
			return;
		}
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			for (Object proxy : proxys) {
				session.lock(proxy, LockMode.NONE);
				Hibernate.initialize(BeanUtils.getProperty(proxy, collectionName));
			}
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
