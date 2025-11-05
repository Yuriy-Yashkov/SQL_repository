package by.march8.ecs.application.shell.general.events;

import by.march8.ecs.MainController;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * The Class EventInternalFrame.
 *
 * @author andy-linux
 */
public class EventInternalFrame implements InternalFrameListener {

    /** The controller. */
    private MainController controller;

    /**
     * Instantiates a new event internal frame.
     *
     * @param controller the controller
     */
    public EventInternalFrame(MainController controller) {
        this.controller = controller;
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameOpened(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameClosing(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameClosing(InternalFrameEvent e) {

    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameClosed(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        controller.closeInternalFrame(e.getInternalFrame());
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameIconified(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }


    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameDeiconified(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameActivated(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        controller.setActiveInternalFrame(e.getInternalFrame());
    }


    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameDeactivated(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

}
