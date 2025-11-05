package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces;

import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings("unused")
public interface IViewPortActions<T> {

    /**
     * Метод возвращает объект из модели данных, соответствующий позиции курсора таблицы
     */
    T getSelectedItem();

    /**Метод возвращает индекс активной строки грида*/
    int getSelectedRowIndex();

    /**
     * Метод возвращает коллекцию объектов, с пометкой в чекбоксах таблицы
     * <br/>Используется для режима множественного выбора из справочника
     */
    Set<T> getSelectedItems();

    /**
     * Метод предустановки курсора в позицию соответствующую объекту аргумента
     * <br/>Используется для режима выбора из справочника
     */
    void preset(T object);

    /**
     * Метод предустановки чекбоксов таблицы соответствующих объектам коллекции аргумента
     * <br/>Используется для режима множественного выбора из справочника
     */
    void preset(ArrayList<T> collection);

    /**
     * Обновление модели таблицы и восстановление курсора согласно методов setDeletedObject(Object object)
     * или setUpdatedObject(Object object)
     */
    void updateViewPort();
}
