package by.gomel.freedev.ucframework.uccore.modes.interfaces;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;

/**
 * Интерфейс для доступа к событиям на добавление и редактирование записи с внешней обработкой
 * Created by Andy on 09.01.2015.
 */
public interface IExternalEditEvents {
    /**
     * Событие на добавление объекта
     *
     * @param object добавляемый объект
     */
    void addRecord(Object object);

    /**
     * Событие на редактирование объекта
     *
     * @param object редактируемыый объект
     */
    void editRecord(Object object);

    /**
     * Возвращает тип справочника
     *
     * @return тип справочника
     */
    MarchReferencesType getReferenceType();

    /**
     * Возвращает активный объект
     *
     * @return объект
     */
    Object getActiveObject();

}
