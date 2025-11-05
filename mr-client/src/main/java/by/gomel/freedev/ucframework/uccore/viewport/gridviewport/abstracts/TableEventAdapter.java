package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ITableEvent;

/**
 * Адаптер описывает события типа ITableEvent в таблице
 * Created by Andy on 19.12.2014.
 */
public abstract class TableEventAdapter<T> implements ITableEvent<T> {

    @Override
    public void onClick(final int rowIndex, int columnIndex, final T item) {

    }

    @Override
    public void onDoubleClick(final int rowIndex, int columnIndex, final T item) {

    }

    @Override
    public void onSelectChanged(final int rowIndex, final T item) {

    }

    @Override
    public void onUpdate() {

    }
}
