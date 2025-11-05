package by.gomel.freedev.ucframework.ucdao.interfaces;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Интерфей описывает основные методы слоя DAO для работы с БД Created by Andy on
 * 24.09.2014.
 */
public interface ICommonDao {

    void updateEntity(final Object object) throws SQLException;

    void updateCollection(final Object object) throws SQLException;

    Object saveEntity(final Object object) throws SQLException;

    void addEntity(final Object object) throws SQLException;

    void deleteEntity(final Class<?> c, final int recordId) throws SQLException;

    ArrayList<Object> getAllEntity(final Class<?> c) throws SQLException;

    ArrayList<Object> getAllEntity(final Class<?> c, final HashMap<String, ?> parameters) throws SQLException;

    Object getEntityById(final Class<?> c, final int recordId)
            throws SQLException;


    /*    Object saveEntityMerge(final Object object) throws SQLException;*/

    List<Object> getAllEntityByStringQuery(final Class<?> c, String query_) throws SQLException;
/*
    void commitTransaction();*/

}