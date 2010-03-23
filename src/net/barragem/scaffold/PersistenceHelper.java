package net.barragem.scaffold;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.PersistenceException;

import net.barragem.persistence.entity.BaseEntity;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
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

	public static List findByNamedQueryWithLimits(String namedQuery, int firstResult, int maxResults,
			Object... paramValues) {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.getNamedQuery(namedQuery);
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
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

	public static void initialize(String proxyName, Object... entities) {
		if (entities == null) {
			return;
		}
		Session session = null;
		Object proxy = null;
		try {
			session = HibernateUtil.getSession();
			for (Object entity : entities) {
				session.lock(entity, LockMode.NONE);
				proxy = ReflectionHelper.get(entity, proxyName);
				if (proxy instanceof BaseEntity) {
					List result = find("select o from " + entity.getClass().getName() + " o join fetch o." + proxyName
							+ " where o.id=" + ((BaseEntity) entity).getId());
					if (!result.isEmpty()) {
						ReflectionHelper.set(entity, proxyName, ReflectionHelper.get(result.get(0), proxyName));
					}
				} else {
					Hibernate.initialize(proxy);
				}
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

	public static <T> List<T> findAll(Class<T> c) {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			return session.createCriteria(c).list();

		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public static boolean isInitialized(Object obj, String proxy) {
		try {
			if (BeanUtils.getProperty(obj, proxy).equals("com.sun.jdi.InvocationException occurred invoking method.")) {
				return false;
			}
			return true;
		} catch (LazyInitializationException e) {
			return false;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
