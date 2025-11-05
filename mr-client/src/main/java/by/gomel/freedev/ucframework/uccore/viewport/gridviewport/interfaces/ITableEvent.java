package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces;

/**
 * Интерфейс описывает собития в таблицы для последующей обработки клиентом
 * Created by Andy on 19.12.2014.
 */
public interface ITableEvent<T> {
    /**
     * Метод срабатывает при одиночном клике в строке таблицы
     *
     * @param rowIndex индекс строки в таблице
     * @param columnIndex индекс строки в таблице
     * @param item   объект из модели данных
     */
    void onClick(int rowIndex, int columnIndex, T item);

    /**
     * Метод срабатывает при двойном клике в строке таблицы
     *
     * @param rowIndex индекс строки в таблице
     * @param item   объект из модели данных
     */
    void onDoubleClick(int rowIndex, int columnIndex, T item);

    /**
     * Метод срабатывает при смене позиции курсора в гриде
     *
     * @param rowIndex индекс строки в таблице
     * @param item   объект из модели данных
     */
    void onSelectChanged(int rowIndex, T item);

    /**
     * Метод срабатывает при обновлении GridViewPort
     */
    void onUpdate();
}
