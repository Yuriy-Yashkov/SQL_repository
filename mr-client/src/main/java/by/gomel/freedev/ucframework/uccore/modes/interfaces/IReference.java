package by.gomel.freedev.ucframework.uccore.modes.interfaces;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;

import java.util.ArrayList;

/**
 * Интерфейс, описывающий методы для работы с редактируемым справочником
 *
 * @author andy-linux
 * @see IEditableModule
 */
public interface IReference extends IEditableModule {
    /**
     * Возвращает коллекцию данных
     *
     * @return коллекция данных
     */
    ArrayList<Object> getDataCollection();

    /**
     * ВОзвращает панель управления закрепленную за данным справочником
     *
     * @return панель управления справочником
     */
    ControlPane getControlPane();

    /**
     * Возвращает панель редактирования закрепленную за данным справочником
     *
     * @return панель редактирования
     */
    EditingPane getEditingPane();

    /**
     * Возвращает ссылку на главный контроллер приложения
     *
     * @return контроллер приложения
     */
    MainController getMainController();

    /**
     * Возвращает тип справочника приложения
     *
     * @return тип справочника приложения
     */
    MarchReferencesType getReferences();

    /**
     * Возвращает интерфейс доступа к операциям с записями
     *
     * @return интерфей доступа к операциям с записями
     */
    IEditableModule getEditableModule();

    /**
     * Возвращает уровень доступа для справочника
     *
     * @return права доступа на справочник
     */
    RightEnum getRight();
}