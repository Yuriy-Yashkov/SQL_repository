package by.march8.api;

/**
 * Интерфейс описывает методы справочника имеющего определенную структуру:
 * <p/> id, Name, Note
 * <p/>
 * Таким образом можно использовать одну панель редактирования для схожих справочников простой структуры полей.
 */
public interface ISimpleReference {
    /**
     * Возвращает значение поля Name
     * @return значение
     */
    String getName();

    /**
     * Устанавливает значение полю NAME
     * @param strName значение
     */
    void setName(String strName);
    /**
     * Возвращает значение поля Note
     * @return значение
     */
    String getNote();

    /**
     * Устанавливает значение поля Note
     * @param strNote значение
     */
    void setNote(String strNote);

    /**
     * Возвращает значение поля ID
     * @return значение
     */
    int getId();

    /**
     * Устанавливае значение полю ID
     * @param id значение
     */
    void setId(int id);
}
