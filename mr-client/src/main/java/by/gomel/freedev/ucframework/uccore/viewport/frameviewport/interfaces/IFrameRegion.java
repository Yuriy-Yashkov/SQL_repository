package by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;

import javax.swing.*;

/**
 * Интерфейс старый и никому не нужной, будет выпилен по-случаю
 * <br/>Даже комментировать ничего не буду
 * Created by Andy on 05.11.2014.
 */
@Deprecated
@SuppressWarnings("unused")
public interface IFrameRegion {
    JPanel getTopContentPanel();

    JPanel getCenterContentPanel();

    JPanel getBottomContentPanel();

    UCToolBar getToolBar();
}
