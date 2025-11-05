package by.gomel.freedev.ucframework.uccore.modes.interfaces;

/**
 * Интерфейс описывает методы редактируемого функционального режима
 */
public interface IEditableFunctionalMode {


    /**
     * Обновить содержимое
     */
    void updateContent();

    /**
     * Новая запись
     */
    void addRecord();

    /**
     * Редактирование записи по строке в таблице
     */
    void editRecord();

    /**
     * Удаление записи по строке в таблице
     */
    void deleteRecord();

    /**
     * Просмотр записи ReadOnly
     */
    void viewRecord();

}
