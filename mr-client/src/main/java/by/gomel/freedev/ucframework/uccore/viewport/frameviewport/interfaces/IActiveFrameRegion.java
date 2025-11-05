package by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;

import javax.swing.*;

/**
 * Интерфейс доступа к регионам формы
 * Created by Andy on 21.12.14.
 */
public interface IActiveFrameRegion {
    /**
     * Возвращает контейнер верхней части формы
     */
    @SuppressWarnings("unused")
    JPanel getTopContentPanel();

    /**
     * Возвращает контейнер центральной части формы
     */
    JPanel getCenterContentPanel();

    /**
     * Возвращает контейнер нижней части формы
     */
    @SuppressWarnings("unused")
    JPanel getBottomContentPanel();

    /**
     * Возвращает панель инструментов формы
     */
    UCToolBar getToolBar();

}
