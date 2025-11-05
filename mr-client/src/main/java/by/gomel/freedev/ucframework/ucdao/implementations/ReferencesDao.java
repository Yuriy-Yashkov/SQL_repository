package by.gomel.freedev.ucframework.ucdao.implementations;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.ucdao.interfaces.IReferencesDao;
import by.march8.ecs.MainController;
import by.march8.entities.admin.UserRole;
import by.march8.entities.materials.YarnItem;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author andy-linux
 */
@SuppressWarnings("all")
public class ReferencesDao extends BaseDao implements IReferencesDao {
    //Session session=null;
    //EntityTransaction transaction;
    EntityManager manager = null;

    @SuppressWarnings("unchecked")
    @Override
    public boolean isExistYarnByCode(final int code) throws SQLException {
        List<Object> result = null;

        final EntityManager manager = getEntityManagerByEntity(YarnItem.class);
        try {
            final Query query = manager
                    .createQuery("SELECT y FROM YarnItem y where code = :code");
            query.setParameter("code", code);
            result = query.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка проверки наличия пряжи в БД для sar=" + code);
        } finally {
            manager.close();
        }
        assert result != null;
        return !result.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<Object> loadComboBoxData(final MarchReferencesType handbook)
            throws SQLException {
        //  final EntityManager manager = getEntityManagerByEntity(handbook
        //      .getClassifierClass());


        /*
         * String orderBy = handbook.getOrder(); if (orderBy.equals("")) {
         * orderBy = "id"; }
         */

        //final Session session = manager.unwrap(Session.class);
       /*if (session==null)
            session= manager.unwrap(Session.class);
        else {session.clear();session.close();
            session=manager.unwrap(Session.class);}*/
        manager = getEntityManagerByEntity(handbook
                .getClassifierClass());
        List<Object> result = null;
        EntityTransaction transaction = manager.getTransaction();
        try {
            //session.beginTransaction();
            transaction.begin();
            final CriteriaBuilder crBuilder = manager.getCriteriaBuilder();
            final CriteriaQuery<Object> crQuery = (CriteriaQuery<Object>) crBuilder
                    .createQuery(handbook.getClassifierClass());
            final Root<Object> root = (Root<Object>) crQuery.from(handbook
                    .getClassifierClass());
            // crQuery.orderBy(crBuilder.asc(root.get(orderBy)));
            crQuery.select(root);
            final TypedQuery<Object> query = manager.createQuery(crQuery);
            result = query.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка загрузки combobox для справочника \"" + handbook.getShortName() + "\"");
        } finally {
            transaction = null;
            manager.unwrap(Session.class).clear();
            manager.unwrap(Session.class).close();
            //manager.close();
        }
        return (ArrayList<Object>) result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Object> loadComboBoxData(String query_) throws SQLException {

        ArrayList<Object> result = null;
        manager = getEntityManagerByEntity(UserRole.class);
       /* if (session==null)
            session= manager.unwrap(Session.class);
        else {session.clear();session.close();
            session=manager.unwrap(Session.class);}*/
        //final Session session = manager.unwrap(Session.class);
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            final Query query = manager
                    .createQuery(query_);
            result = (ArrayList<Object>) query.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Получение данных по условию");
        } finally {
            transaction = null;
            manager.unwrap(Session.class).clear();
            manager.unwrap(Session.class).close();
            // manager.close();
        }
        return result;
    }

    public ArrayList<Object> loadComboBoxDataByProcedure(final MarchReferencesType handbook, String procedureName,
                                                         HashMap<String, ?> parameters) {
        ArrayList<Object> result = null;
        final EntityManager manager = getEntityManagerByEntity(handbook
                .getClassifierClass());
        /*if (session==null)
            session= manager.unwrap(Session.class);
        else {session.clear();session.close();
            session=manager.unwrap(Session.class);}*/
        final Session session = manager.unwrap(Session.class);
        try {
            session.beginTransaction();
            StoredProcedureQuery query = manager.createNamedStoredProcedureQuery(procedureName);
            //Заполнение параметров
            for (Map.Entry<String, ?> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            query.execute();
            result = (ArrayList<Object>) query.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Получение данных по условию");
        } finally {
            manager.close();
        }
        return result;

    }

    @Override
    public void loadComboBoxData(JComboBox combobox, Class<?> clas) {
        ArrayList<Object> array = null;
        try {
            array = loadComboBoxData(MarchReferencesType.getTypeByClassName(clas));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        combobox.setModel(new DefaultComboBoxModel(array.toArray()));
    }

    @SuppressWarnings("unchecked")
    /**
     * Метод по названию класса и запросу, возвращает соотв. коллекцию объектов,
     * если запрос ничего не вернул, возвращает null
     */
    public ArrayList<Object> loadDataForFile(Class<?> c, String query_) throws SQLException {

        ArrayList<Object> result = null;
        final EntityManager manager = getEntityManagerByEntity(c);
        final Session session = manager.unwrap(Session.class);
        try {
            session.beginTransaction();
            final Query query = manager
                    .createQuery(query_);
            result = (ArrayList<Object>) query.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Получение данных по условию");
        } finally {
            session.clear();
            session.close();
            // manager.close();
        }
        if (result.isEmpty()) return null;
        else return result;
    }

    @SuppressWarnings("unchecked")
    /**
     * Метод по названию класса и запросу, возвращает соотв. коллекцию,
     * если запрос ничего не вернул, возвращает null
     */
    public ArrayList<?> loadDataFromBase(Class<?> c, String someQuery) throws SQLException {

        ArrayList<?> result = null;
        final EntityManager manager = getEntityManagerByEntity(c);
        final Session session = manager.unwrap(Session.class);
        try {
            session.beginTransaction();
            final Query query = manager
                    .createQuery(someQuery);
            result = (ArrayList<?>) query.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Получение данных по условию");
        } finally {
            session.clear();
            session.close();
            // manager.close();
        }
        if (result.isEmpty()) return null;
        else return result;
    }


}