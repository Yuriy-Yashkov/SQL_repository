package by.gomel.freedev.ucframework.uccore.modes.interfaces;

/**
 * @author Andy 20.01.2016.
 */
public interface ISelectableFunctionalMode {

    /**
     * Метод показывает форму с возможностью выбора из таблицы
     * @param presetObject объект для предустановки данных
     * @return выбранный из таблицы объект
     */
    Object showSelectModal(Object presetObject);
}
