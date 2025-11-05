package by.gomel.freedev.ucframework.uccore.modes.interfaces;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;

import javax.swing.*;

/**
 * @author Andy 05.11.2014.
 */
public interface IFormRegion {
    JPanel getTopContentPanel();

    JPanel getCenterContentPanel();

    JPanel getBottomContentPanel();

    JTable getViewPort();

    UCToolBar getToolBar();
}
