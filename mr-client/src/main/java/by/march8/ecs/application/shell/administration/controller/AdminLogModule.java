package by.march8.ecs.application.shell.administration.controller;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

/**
 * @author Andy 29.10.2014.
 */
public class AdminLogModule implements Module {

    private JMenuItem miAdminLog = new JMenuItem("Монитор событий");
    private MainController controller;

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_ADMINISTRATOR_LOGMONITOR, miAdminLog));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miAdminLog.addActionListener(e -> new AdminLogController(controller));
    }
}
