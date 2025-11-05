package by.march8.ecs.application.shell.administration;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.tools.ToolsForm;
import dept.tools.imgmanager.ImageManagerForm;

import javax.swing.*;

public class SettingsCommonModule implements Module {

    private final JMenuItem miConfig = new JMenuItem("Конфигурация");
    private final JMenuItem miPhoto = new JMenuItem("Управление фотографиями");

    private MainController controller;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_CONFIGURE, miConfig));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_PHOTO, miPhoto));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miConfig.addActionListener(a -> new ToolsForm(controller, true));
        miPhoto.addActionListener(a-> controller.openInternalFrame(
                new ImageManagerForm(controller)));
    }

}
