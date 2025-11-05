package by.gomel.freedev.ucframework.ucdao.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Интерфейс добавляет методы получения данных в отдельном потоке в сопровождении
 * всплывающего диалогового окна с индикатором прогресса
 * Created by Andy on 07.11.14.
 */
public interface ICommonDaoThread extends ICommonDao {

    ArrayList<Object> getAllEntityThread(final Class<?> c) throws SQLException;

    ArrayList<Object> getAllEntityThread(final Class<?> c, final HashMap<String, ?> parameters) throws SQLException;
    //ArrayList<Object> getAllByStoredProcedureThread(final Class<?> c, String procedureName) throws SQLException;

    ArrayList<Object> getAllEntityByStringQueryThread(final Class<?> c, String query_) throws SQLException;

    void closeEntityManager(final Class<?> classifierClass);

    Object getEntityByIdThread(final Class<?> c, final int recordId)
            throws SQLException;

    Object saveEntityThread(final Object object) throws SQLException;

    void updateEntityThread(final Object object) throws SQLException;

    void updateCollectionThread(final Object object) throws SQLException;
}
