package by.gomel.freedev.ucframework.uccore.viewport.gridviewport;


import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.AbstractNiceGrid;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IGridToolTipEvent;
import by.march8.api.BaseEntity;

import java.util.ArrayList;
import java.util.Set;

/**
 * Класс области просмотра ТАБЛИЦА
 * <br/>Содержит методы  получения как единичных так и множественных наборов данных
 * <br/>Содержит методы обработки событий: одиночный клик мышки, двойной клик мышки, изменение позиции курсора, обновление данных
 * <br/>Будет изменяться и дополняться
 * <p>
 * <br/>Created by Andy on 19.12.2014.
 */
public class GridViewPort<T> extends AbstractNiceGrid<T> {

    /**
     * Конструктор создает новый объект.
     *
     * @param c           имя класса, по которому будет генерироваться TableModel
     * @param multiSelect устанавливает режим множественного выбора по чекбоксу
     */
    public GridViewPort(Class<?> c, boolean multiSelect) {
        super(c, multiSelect);
    }

    /**
     * Конструктор создает новый объект.
     *
     * @param c имя класса, по которому будет генерироваться TableModel
     */
    public GridViewPort(Class<?> c) {
        super(c);
    }

    /**
     * Конструктор создает новый объект.
     *
     * @param c           имя класса, по которому будет генерироваться TableModel
     * @param multiSelect устанавливает режим множественного выбора по чекбоксу
     * @param columnLimit ограничения на видимость колонок с таблице
     */
    public GridViewPort(Class<?> c, boolean multiSelect, boolean columnLimit) {
        super(c, multiSelect, columnLimit);
    }

    @Override
    public T getSelectedItem() {
        if (isEmpty()) {
            return null;
        } else {
            return data.get(tView.getSelectedRowIndex());
        }
    }

    @Override
    public Set<T> getSelectedItems() {
        if (isEmpty()) {
            return null;
        } else {
            return getTableModel().getSelectedItems();
        }
    }

    @Override
    public void preset(final T object) {
        setUpdatedObject(object);
        updateViewPort();
    }

    @Override
    public void preset(final ArrayList<T> collection) {
        getTableModel().preset(collection);
    }

    @Override
    public void updateViewPort() {
        tModel.fireTableDataChanged();
        if (tFooterModel != null) {
            tFooterModel.fireTableDataChanged();
        }

        if (deletedObject != null) {
            afterDeleteUpdate();
        }

        if (updatedObject != null) {
            setActiveRowByObject(updatedObject);
        }

        deletedObject = null;
        updatedObject = null;

        if (tableEventHandler != null) {
            tableEventHandler.onUpdate();
        }
    }

    /**
     * Метод помечает объект на удаление. При последующем вызове UpdateViewPort, и при условии
     * что из модели данных этот объект удален, произойдет переход курсора к последующей или
     * предыдущей строке таблицы
     */
    public void setDeletedObject(T object) {
        deletedObject = object;
        beforeDeleteUpdate(deletedObject);
    }

    /**
     * Метод помечает объект в таблице как обновляемый. При последующем вызове UpdateViewPort,
     * курсор таблицы установится в строке, соответствующей данному объекту
     */
    public void setUpdatedObject(Object object) {
        updatedObject = object;
    }

    /**
     * Метод первичной инициализации курсора таблицы.
     * Устанавливает курсор на первую строку (индекс 0)
     */
    public void primaryInitialization() {
        if (tView.getRowCount() > 0) {
            setActiveRowByTableIndex(0);
        }
    }

    public void addToolTipHandler(IGridToolTipEvent event) {
        if (event != null) {
            tView.setToolTipHandler(event);
        }
    }

    public T getItemByObjectId(int id) {
        for (T o : data) {
            BaseEntity item = (BaseEntity) o;
            if (item.getId() == id) {
                return o;
            }
        }
        return null;
    }

    public void resetFilter() {
        filterHeader.resetFilter();
    }

    public void presetFilter(int index, String value) {
        filterHeader.getFilterEditor(index).setContent(value);
    }

    private boolean isEmpty() {
        return data.isEmpty();
    }

    public void initializeFooterBar(T item) {
        initializeFooterTable(item);
    }

    public void selectAll(boolean clear) {
        tModel.selectAll(clear);
        updateViewPort();
    }

    public void setCheck(int id, boolean b) {
        tModel.selectCheck(id, b);
        updateViewPort();
    }

    public void setRowHeight(T item, int height) {
        int id = getObjectIndexInDataModel(item);
        // Сопоставляем индекс с номером строки в гриде
        int id_ = tView.convertRowIndexToView(id);
        getTable().setRowHeight(id_, height);
    }
}
