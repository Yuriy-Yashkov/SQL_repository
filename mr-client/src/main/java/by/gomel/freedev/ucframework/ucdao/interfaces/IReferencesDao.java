package by.gomel.freedev.ucframework.ucdao.interfaces;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Интерфейс описывает спец. методы DAO для работы со справочником
 *
 * @author andy-linux
 */
public interface IReferencesDao {

    /**
     * Возвращает коллекцию сущностей для справочника
     *
     * @throws SQLException
     *
     *             Collection<? extends Object> getEntityArray(Class<?>
     *             classifierClass, int type, String myQuery) throws
     *             SQLException;
     */

    /**
     * Проверяет, есть ли данный шифр артикула пряжи в БД
     */
    boolean isExistYarnByCode(int code) throws SQLException;

    /**
     * Возвращает коллекцию сущностей для последующей загрузки в JComboBox
     */
    ArrayList<Object> loadComboBoxData(final MarchReferencesType handbook)
            throws SQLException;

    ArrayList<Object> loadComboBoxData(String query) throws SQLException;

    ArrayList<Object> loadComboBoxDataByProcedure(final MarchReferencesType handbook, String procedureName,
                                                  HashMap<String, ?> parameters) throws SQLException;

    void loadComboBoxData(JComboBox combobox, Class<?> clas);
}
