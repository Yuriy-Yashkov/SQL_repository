package by.march8.ecs.application.shell.model;

import javax.swing.*;


/**
 * Модель описывает внутреннее окно как объект и пункт меню, вызывающий его
 *
 * @author andy-linux
 */
public class FrameLink {

    /** The i frame. */
    private JInternalFrame iFrame = null;

    /** The i menu. */
    private JMenuItem iMenu = null;

    /**
     * Instantiates a new frame link.
     *
     * @param iFrame the i frame
     * @param iMenu the i menu
     */
    public FrameLink(JInternalFrame iFrame, JMenuItem iMenu) {
        this.iFrame = iFrame;
        this.iMenu = iMenu;
    }

    /**
     * Gets the menu.
     *
     * @return the menu
     */
    public JMenuItem getMenu() {
        return iMenu;
    }

    /**
     * Gets the frame.
     *
     * @return the frame
     */
    public JInternalFrame getFrame() {
        return iFrame;
    }

}
