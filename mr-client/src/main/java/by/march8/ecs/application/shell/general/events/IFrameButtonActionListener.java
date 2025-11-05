package by.march8.ecs.application.shell.general.events;

import by.gomel.freedev.ucframework.ucswing.IFrameButton;
import by.march8.ecs.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Слушатель событий для кнопок панели задач приложения (список открытых окон)
 * @author Andy on 02.11.14.
 */
public class IFrameButtonActionListener implements ActionListener {
    private MainController controller;

    public IFrameButtonActionListener(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IFrameButton button = (IFrameButton) e.getSource();
        controller.setActiveInternalFrame(button.getFrame());
    }
}
