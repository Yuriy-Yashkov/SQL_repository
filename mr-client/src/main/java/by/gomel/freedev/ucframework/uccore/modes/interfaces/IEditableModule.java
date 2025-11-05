package by.gomel.freedev.ucframework.uccore.modes.interfaces;

/**
 * Интерфейс описывает методы для работы редактируемым модулем-объектом
 * <p/>Перечислены основные операции с данными
 *
 * @author andy-linux
 */
public interface IEditableModule {
    /**
     * Новая запись
     */
    void addRecord();

    /**
     * Редактирование записи по строке в таблице
     */
    @Deprecated
    void editRecord(int selectedRow);

    /**
     * Редактирование записи по строке в таблице
     */
    void editRecord();

    /**
     * Удаление записи по строке в таблице
     */
    @Deprecated
    void deleteRecord(int selectedRow);

    /**
     * Удаление записи по строке в таблице
     */
    void deleteRecord();

    /**
     * Просмотр записи ReadOnly
     */
    @Deprecated
    void viewRecord(int selectedRow);

    /**
     * Просмотр записи ReadOnly
     */
    void viewRecord();

    /**
     * Обновить содержимое
     */
    void updateContent();

    /**
     * Метод выгрузки данных из справочника в документ OpenOffice
     */
    void referenceToReport();
}
