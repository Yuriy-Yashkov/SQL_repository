package by.gomel.freedev.ucframework.ucdao.interfaces;

import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public interface IGenericDao<T> {

    void loadComboBox(JComboBox comboBox, Class<T> className) throws SQLException;


    List<T> getAllEntity(final Class<T> className) throws SQLException;

    T getEntityById(final Class<T> className, final int id) throws SQLException;

    T saveEntity(final T object) throws SQLException;

    void updateEntity(final T object) throws SQLException;

    void deleteEntity(final Class<T> className, final int id) throws SQLException;


    List<T> getEntityListByQuery(final Class<T> className, String query) throws SQLException;

    T getEntityByQuery(final Class<T> className, String query) throws SQLException;

    int executeUpdateByQuery(final Class<T> className, String query) throws SQLException;

    List<T> getEntityListByNamedQuery(final Class<T> className, String namedQuery, List<QueryProperty> criteria) throws SQLException;

    T getEntityByNamedQuery(final Class<T> className, String namedQuery, List<QueryProperty> criteria) throws SQLException;

    int executeUpdateByNamedQuery(final Class<T> className, String namedQuery, List<QueryProperty> criteria) throws SQLException;

}
