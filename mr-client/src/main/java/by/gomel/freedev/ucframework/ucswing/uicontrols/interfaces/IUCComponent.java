package by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces;


import by.gomel.freedev.ucframework.ucswing.uicontrols.UCController;

import javax.swing.*;

/**
 * Основной интерфейс-маркер UC компонентов
 * Created by andy-linux on 3/21/15.
 */
public interface IUCComponent extends IUCTabOrder, IUCVerification {
    /**
     * Если состояния/содержимое компонента изменялось с момента setText/setSelectIndex
     * возвращает true
     *
     * @return компонент изменен
     */
    boolean isModified();

    /**
     * Метод устанавливает фокус на компонент
     */
    void setFocus();

    /**
     * Метод возвращает ссылку на компонент
     *
     * @return ссылка на компонент
     */
    JComponent getComponent();

    /**
     * Возвращает ссылку на контроллер
     * @return ссылка на контролллер
     */
    UCController getUCController();

    /**
     * Устанавливает компоненту ссылку на контроллер
     * @param ucc ссылка на контроллер
     */
    void setUCController(UCController ucc);

}
