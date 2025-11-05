package by.march8.ecs.application.shell.general.events;

import by.march8.ecs.MainController;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Слушатель всплывающих подсказок приложения
 * Created by Andy on 03.11.2014.
 */
public class HintActivateMouseListener implements MouseListener {
    private MainController controller;

    public HintActivateMouseListener(final MainController controller) {
        this.controller = controller;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        controller.showHint(((JComponent) e.getSource()).getToolTipText());
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        controller.showHint("");
    }
}
