package by.gomel.freedev.ucframework.uccore.modes.interfaces;


import by.gomel.freedev.ucframework.ucswing.uicontrols.UCController;

/**
 * Интерфейс описывает методы для панели редактирования сущности
 *
 * @author andy-linux
 */
public interface IEditingPane {
    /**
     * Метод возвращает измененную/созданную в панели редактирования сущность-источник
     * для последующего сохранения ее в БД.
     * <p/>В данном методе происходит заполение сущности данными из компонентов редактирования:
     * (напр. <code>entity.setName(tfName.getText())</code>)
     * <p/>Важно, что перед вызовом данного метода, срабатывает метод verificationData()и пока он не вернет true,
     * данный метод не будет вызван
     */
    Object getSourceEntity();

    /**
     * Метод устанавливает сущность - источник для панели редактирования.
     * Если аргументом передан null необходима первичная инициализация сущности внутри метода.
     * <p/>В данном методе заполняются все компоненты редактирования полей сущности:
     * (напр.\<code> tfName.setText(entity.getName())</code>)
     */
    void setSourceEntity(Object object);

    /**
     * Метод проверяет компоненты редактирования на корректность введенных значений,
     * и возвращает true если данные соответствуют условию
     * <code>
     * <p/>
     * if (tfName.getText().equals("")) {
     * JOptionPane.showMessageDialog(null, "Необходимо указать наименование",
     * "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
     * <p/>
     * tfName.requestFocusInWindow();
     * return false;
     * <p/>
     * } else {
     * <p/>tfNameEng.setText(tfNameEng.getText().toUpperCase());
     * }
     * <p/>...
     * <p/>...
     * <p/>return true ;
     * </code>
     * <p/>
     * Далее вызывается метод getSourceEntity()
     */
    boolean verificationData();

    /**
     * Заполнение данными панели редактирования.
     * На всякий случай вынесено в отделльный метод, но автоматически пока нигде не вызывается.
     * Задел на будущее
     */
    void defaultFillingData();

    UCController getUCController();

}
