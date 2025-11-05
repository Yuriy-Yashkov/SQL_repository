package by.gomel.freedev.ucframework.ucdao.interfaces;

import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Andy 03.11.15
 */
@SuppressWarnings("unused")
public interface IGenericDaoGUI<T> extends IGenericDao<T> {
    List<T> getAllEntityGUI(final Class<T> className) throws SQLException;

    T getEntityByIdGUI(final Class<T> c, final int id) throws SQLException;

    List<T> getEntityListByQueryGUI(final Class<T> className, String query) throws SQLException;

    T getEntityByQueryGUI(final Class<T> className, String query) throws SQLException;

    List<T> getEntityListByNamedQueryGUI(final Class<T> className, String namedQuery, List<QueryProperty> criteria) throws SQLException;

    T getEntityByNamedQueryGUI(final Class<T> className, String namedQuery, List<QueryProperty> criteria) throws SQLException;
}
