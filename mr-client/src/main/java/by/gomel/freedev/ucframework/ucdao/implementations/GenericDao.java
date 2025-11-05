package by.gomel.freedev.ucframework.ucdao.implementations;

import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.swing.*;
import java.util.List;

/**
 * @author Andy 03.11.15
 */
public class GenericDao<T> extends BaseDao implements IGenericDao<T>, IGenericDaoGUI<T> {

    @SuppressWarnings("all")
    public void loadComboBox(JComboBox comboBox, Class<T> c) {
        List<T> array = null;
        array = getAllEntity(c);
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
    }

    @Override
    public List<T> getAllEntity(Class<T> className) {
        List<T> result = null;
        final EntityManager manager = getEntityManagerByEntity(className);
        // final Session session = manager.unwrap(Session.class);
        try {
            //session.beginTransaction();
            TypedQuery<T> q = manager.createQuery("SELECT e FROM " + className.getSimpleName() + " e", className);
            result = q.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка получения всех записей для " + className.getName() + ":");
        } finally {
            manager.close();
        }

        return result;
    }

    @Override
    public T getEntityById(Class<T> className, int id) {
        T result = null;
        EntityManager manager = getEntityManagerByEntity(className);
        //EntityTransaction transaction = manager.getTransaction();
        final Session session = manager.unwrap(Session.class);
        try {
            session.getTransaction().begin();
            result = manager.find(className, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            MainController.exception(e, "Ошибка получения записи для " + className.getName() + " с id=" + id + ":");
        } finally {
            session.close();
            //manager.close();
        }
        return result;
    }

    @Override
    public T saveEntity(T object) {
        System.out.println("HERE___>>>>>   " + object.getClass());
        EntityManager manager = getEntityManagerByEntity(object.getClass());
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            manager.persist(object);
            manager.flush();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            MainController.exception(e, "Ошибка сохранения записи для " + object.getClass());
        } finally {
            manager.close();
        }
        return object;
    }

    @Override
    public void updateEntity(T object) {
        final EntityManager manager = getEntityManagerByEntity(object.getClass());
        final EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            manager.merge(object);
            manager.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            MainController.exception(e, "Ошибка обновления записи для " + object.getClass());
        } finally {
            manager.close();
        }
    }

    @Override
    public void deleteEntity(Class<T> className, int id) {
        final EntityManager manager = getEntityManagerByEntity(className);
        final EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            final T tmpObject = manager.find(className, id);
            manager.remove(tmpObject);
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            MainController.exception(e, "Ошибка удаления записи для " + className.getName() + " с id=" + id + ":");
        } finally {
            manager.close();
        }
    }

    @Override
    public List<T> getEntityListByQuery(Class<T> className, String query) {
        List<T> result = null;
        final EntityManager manager = getEntityManagerByEntity(className);
        //final EntityTransaction transaction = manager.getTransaction();
        try {
            TypedQuery<T> q = manager.createQuery(query, className);
            result = q.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка получения спизка записей по запросу [" + query + "]");
        } finally {
            manager.close();
        }

        return result;
    }

    @Override
    public T getEntityByQuery(Class<T> className, String query) {
        T result = null;
        final EntityManager manager = getEntityManagerByEntity(className);
        //final EntityTransaction transaction = manager.getTransaction();

        try {
            TypedQuery<T> q = manager.createQuery(query, className);
            List<T> list = q.getResultList();
            if (list.size() > 0) {
                result = list.get(0);
            }
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка получения записи по запросу [" + query + "]");
        } finally {
            manager.close();
        }

        return result;
    }

    @Override
    public int executeUpdateByQuery(Class<T> className, String query) {
        int count = 0;
        final EntityManager manager = getEntityManagerByEntity(className);
        final EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            final Query q = manager.createQuery(query);
            count = q.executeUpdate();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            MainController.exception(e, "Ошибка получения спизка записей по запросу [" + query + "]");
        } finally {
            manager.close();
        }
        return count;
    }

    @Override
    public List<T> getEntityListByNamedQuery(Class<T> className, String namedQuery, List<QueryProperty> criteria) {
        EntityManager manager = getEntityManagerByEntity(className);
        List<T> result = null;
        final Session session = manager.unwrap(Session.class);

        try {
            session.getTransaction().begin();
            //session.nam
            TypedQuery<T> q = manager.createNamedQuery(namedQuery, className);
            if (criteria != null) {
                for (QueryProperty prop : criteria) {
                    q.setParameter(prop.getKey(), prop.getValue());
                }
            }
            result = q.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            //transaction.rollback();
            session.getTransaction().rollback();
            MainController.exception(e, "Ошибка получения данных для именованного запроса [ " + namedQuery + "]");
        } finally {
            session.close();
            if (session.isConnected()) {
                manager.close();
            }
        }

        return result;
    }

    @Override
    public T getEntityByNamedQuery(Class<T> className, String namedQuery, List<QueryProperty> criteria) {
        EntityManager manager = getEntityManagerByEntity(className);
        T result = null;
        try {
            TypedQuery<T> q = manager.createNamedQuery(namedQuery, className);
            if (criteria != null) {
                for (QueryProperty prop : criteria) {
                    q.setParameter(prop.getKey(), prop.getValue());
                }
            }
            List<T> list = q.getResultList();
            if (list.size() > 0) {
                result = list.get(0);
            }
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения записи для именованного запроса [ " + namedQuery + "]");
        } finally {
            manager.close();
        }
        return result;
    }

    @Override
    public int executeUpdateByNamedQuery(Class<T> className, String namedQuery, List<QueryProperty> criteria) {
        EntityManager manager = getEntityManagerByEntity(className);
        final EntityTransaction transaction = manager.getTransaction();
        int count = 0;
        try {
            transaction.begin();
            TypedQuery<T> q = manager.createNamedQuery(namedQuery, className);
            if (criteria != null) {
                for (QueryProperty prop : criteria) {
                    q.setParameter(prop.getKey(), prop.getValue());
                }
            }
            count = q.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для именованного запроса [ " + namedQuery + "]");
            transaction.rollback();
        } finally {
            manager.close();
        }
        return count;
    }

    @Override
    public List<T> getAllEntityGUI(Class<T> className) {
        class Task extends BackgroundTask {
            private List<T> result = null;

            public Task(final String messageText) {
                super(messageText);
            }

            public List<T> getList() {
                return result;
            }

            @Override
            protected List<T> doInBackground() throws Exception {
                result = getAllEntity(className);
                return result;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getList();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + className.getName());
        }
        return null;
    }

    @Override
    public T getEntityByIdGUI(Class<T> className, int id) {
        class Task extends BackgroundTask {
            private T result = null;

            public Task(final String messageText) {
                super(messageText);
            }

            public T getObject() {
                return result;
            }

            @Override
            protected T doInBackground() throws Exception {
                result = getEntityById(className, id);
                return result;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getObject();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + className.getName());
        }
        return null;
    }

    @Override
    public List<T> getEntityListByQueryGUI(Class<T> className, String query) {
        class Task extends BackgroundTask {
            private List<T> result = null;

            public Task(final String messageText) {
                super(messageText);
            }

            public List<T> getList() {
                return result;
            }

            @Override
            protected List<T> doInBackground() throws Exception {
                result = getEntityListByQuery(className, query);
                return result;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getList();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + className.getName());
        }
        return null;
    }


    @Override
    public T getEntityByQueryGUI(Class<T> className, String query) {
        class Task extends BackgroundTask {
            private T result = null;

            public Task(final String messageText) {
                super(messageText);
            }

            public T getObject() {
                return result;
            }

            @Override
            protected T doInBackground() throws Exception {
                result = getEntityByQuery(className, query);
                return result;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getObject();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + className.getName());
        }
        return null;
    }

    @Override
    public List<T> getEntityListByNamedQueryGUI(Class<T> className, String namedQuery, List<QueryProperty> criteria) {
        class Task extends BackgroundTask {
            private List<T> result = null;

            public Task(final String messageText) {
                super(messageText);
            }

            public List<T> getList() {
                return result;
            }

            @Override
            protected List<T> doInBackground() throws Exception {
                result = getEntityListByNamedQuery(className, namedQuery, criteria);
                return result;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getList();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + className.getName());
        }
        return null;
    }

    @Override
    public T getEntityByNamedQueryGUI(Class<T> className, String namedQuery, List<QueryProperty> criteria) {
        class Task extends BackgroundTask {
            private T result = null;

            public Task(final String messageText) {
                super(messageText);
            }

            public T getObject() {
                return result;
            }

            @Override
            protected T doInBackground() throws Exception {
                result = getEntityByNamedQuery(className, namedQuery, criteria);
                return result;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getObject();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + className.getName());
        }
        return null;
    }

    public T unproxy(T proxied) {
        T entity = proxied;
        if (entity != null && entity instanceof HibernateProxy) {
            Hibernate.initialize(entity);
            entity = (T) ((HibernateProxy) entity)
                    .getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }


}
