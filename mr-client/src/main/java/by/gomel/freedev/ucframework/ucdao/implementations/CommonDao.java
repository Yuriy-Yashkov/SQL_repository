package by.gomel.freedev.ucframework.ucdao.implementations;

import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.march8.api.BaseEntity;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.comparators.ComporatorForTables;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 24.09.2014.
 */
public class CommonDao extends BaseDao implements ICommonDaoThread {


    @Override
    public void addEntity(final Object object) {
        EntityManager manager = getEntityManagerByEntity(object.getClass());
        EntityTransaction tr = manager.getTransaction();
        try {
            tr.begin();
            manager.merge(object);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            MainController.exception(e, "Ошибка при добавлении записи для " + object.getClass());
        } finally {
            manager.unwrap(Session.class).clear();
            manager.unwrap(Session.class).close();
            /// /manager.close();
        }
    }

    @Override
    public void deleteEntity(final Class<?> c, final int recordId) {
        final EntityManager manager = getEntityManagerByEntity(c);
        try {
            manager.getTransaction().begin();
            final Object tmpObject = manager.find(c, recordId);
            manager.remove(tmpObject);
            manager.getTransaction().commit();
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка удаления записи для " + c.getClass() + " с id=" + recordId);
        } finally {
            manager.unwrap(Session.class).clear();
            manager.unwrap(Session.class).close();
            // manager.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Object> getAllEntity(Class<?> c) {
        ArrayList<Object> result = null;
        EntityManager manager = getEntityManagerByEntity(c);
        try {
            final CriteriaBuilder crBuilder = manager.getCriteriaBuilder();
            final CriteriaQuery<?> crQuery = crBuilder
                    .createQuery(c);
            final Root<?> root = crQuery.from(c);
            crQuery.orderBy(crBuilder.asc(root.get("id")));

            final TypedQuery<?> query = manager
                    .createQuery(crQuery);
            result = (ArrayList<Object>) query.getResultList();

            Method methods[] = c.getMethods();
            int checkForOrderMethod = 0;
            for (Method method : methods) {
                if (method.getName().contains("getOrderByField")) checkForOrderMethod = 1;
            }
            if (checkForOrderMethod == 1) {
                try {
                    Method method = c.getDeclaredMethod("getOrderByField");
                    if (method != null) {
                        Collections.sort(result, new ComporatorForTables());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + c.getClass());
        } finally {
            // manager.unwrap(Session.class).clear();
            // manager.unwrap(Session.class).close();
            manager.close();
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Object> getAllEntity(Class<?> c, HashMap<String, ?> parameters) {
        ArrayList<Object> result = null;
        EntityManager manager = getEntityManagerByEntity(c);
        EntityTransaction transaction = manager.getTransaction();
        //final Session session = manager.unwrap(Session.class);
        try {
            //session.beginTransaction();
            transaction.begin();
            StoredProcedureQuery query = manager.createNamedStoredProcedureQuery("modelByNar");
            //Заполнение параметров
            for (Map.Entry<String, ?> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            query.execute();
            result = (ArrayList<Object>) query.getResultList();
            //Cортировка полученной коллекции из базы с проверкой
            // на наличие  существование метода getOrderByField в entity
            Method methods[] = c.getMethods();
            int checkForOrderMethod = 0;
            for (Method method : methods) {
                if (method.getName().contains("getOrderByField")) checkForOrderMethod = 1;
            }
            if (checkForOrderMethod == 1) {
                try {
                    Method method = c.getDeclaredMethod("getOrderByField");
                    if (method != null) {
                        Collections.sort(result, new ComporatorForTables());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Collections.sort(result, new ComporatorForTables());
            //transaction.commit();
            System.out.println("Common get all");
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + c.getClass());
        } finally {
            // manager.unwrap(Session.class).clear();
            // manager.unwrap(Session.class).close();
            manager.close();

        }

        return result;
    }

    @Override
    public Object getEntityById(final Class<?> c, final int recordId) {
        Object result = null;
        EntityManager manager = getEntityManagerByEntity(c);
        try {
            result = manager.find(c, recordId);
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения записи для " + c.getClass() + " с id=" + recordId);
        } finally {
            // manager.unwrap(Session.class).clear();
            // manager.unwrap(Session.class).close();
            manager.close();
        }
        return result;
    }

    @Override
    public void updateEntity(final Object object) {
        final EntityManager manager = getEntityManagerByEntity(object.getClass());
        final EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            manager.merge(object);
            transaction.commit();

            /*transaction.begin();
            manager.flush();
            transaction.commit();
            */
            System.out.println("Common update");
        } catch (final Exception e) {
            transaction.rollback();
            MainController.exception(e, "Ошибка обновления записи для " + object.getClass());
        } finally {
            manager.close();
        }
    }

    @Override
    public void updateCollection(final Object object) {
        List<Object> list = (List) object;
        final EntityManager manager = getEntityManagerByEntity(list.get(0).getClass());
        final EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            for (Object o : list) {
                if (((BaseEntity) o).getId() < 1) {
                    manager.persist(o);
                } else {
                    manager.merge(o);
                }
            }
            // manager.flush();
            transaction.commit();
            System.out.println("Common update");
        } catch (final Exception e) {
            transaction.rollback();
            MainController.exception(e, "Ошибка обновления записи для " + object.getClass());
        } finally {
            manager.close();
        }
    }

    @Override
    public Object saveEntity(final Object object) {
        EntityManager manager = getEntityManagerByEntity(object.getClass());
        EntityTransaction tr = manager.getTransaction();
        try {
            manager.getTransaction().begin();
            manager.persist(object);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
            //MainController.exception(e, "Ошибка сохранения записи для " + object.getClass());
        } finally {
            manager.close();
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<Object> getAllEntityByStringQuery(final Class<?> c, final String query_) {

        if (query_ != null) {
            System.out.println("SUBQUERY IS EXIST");
        } else {
            System.out.println("SUBQUERY IS NOT EXIST");
        }

        ArrayList<Object> result = null;
        final EntityManager manager = getEntityManagerByEntity(c);
        final Session session = manager.unwrap(Session.class);
        try {
            session.beginTransaction();
            final Query query = manager
                    .createQuery(query_);
            //query.setParameter("code", code);
            result = (ArrayList<Object>) query.getResultList();
            session.getTransaction().commit();
        } catch (final Exception e) {
            MainController.exception(e, "Получение данных по условию");
            session.getTransaction().rollback();
        } finally {
            session.close();
            if (session.isConnected()) {
                manager.close();
            }
        }

        return result;
    }

    @Override
    public ArrayList<Object> getAllEntityThread(final Class<?> c) {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected ArrayList<Object> doInBackground() throws Exception {
                result = getAllEntity(c);
                return result;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getResult();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + c.getClass());
        }
        return null;
    }

    @Override
    public Object saveEntityThread(final Object object) {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                resultObject = saveEntity(object);
                return resultObject;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getResultObject();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + object.getClass());
        }
        return null;
    }

    @Override
    public ArrayList<Object> getAllEntityThread(final Class<?> c, final HashMap<String, ?> parameters) {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected ArrayList<Object> doInBackground() throws Exception {
                result = getAllEntity(c, parameters);
                return result;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getResult();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + c.getClass());
        }
        return null;
    }

    @Override
    public ArrayList<Object> getAllEntityByStringQueryThread(final Class<?> c, final String query_) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected ArrayList<Object> doInBackground() throws Exception {
                result = getAllEntityByStringQuery(c, query_);
                return result;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getResult();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + c.getClass());
        }
        return null;
    }

    @Override
    public void closeEntityManager(final Class<?> classifierClass) {
        EntityManager manager = getEntityManagerByEntity(classifierClass);
        System.err.println("Close EM");
        manager.close();
    }

    @Override
    public Object getEntityByIdThread(final Class<?> c, final int recordId) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                resultObject = getEntityById(c, recordId);
                return resultObject;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        try {
            return task.getResultObject();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных для " + c.getClass());
        }
        return null;
    }

    @Override
    public void updateEntityThread(final Object object) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                updateEntity(object);
                return true;
            }
        }

        Task task = new Task("Сохранение данных...");

        try {
            task.executeTask();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка сохраниения данных для " + object.getClass());
        }
    }

    @Override
    public void updateCollectionThread(final Object object) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Object doInBackground() throws Exception {
                updateCollection(object);
                return true;
            }
        }

        Task task = new Task("Сохранение данных...");

        try {
            task.executeTask();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка сохраниения данных для " + object.getClass());
        }
    }
}
