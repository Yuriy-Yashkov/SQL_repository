package by.march8.ecs.application.modules.references.common;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

public class CommonModule implements Module {

    private final JMenuItem miEanColors = new JMenuItem("Справочник цветов");
    private MainController controller;

    @Override
    public void registerModule(final MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_COLOR, miEanColors));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {

        miEanColors.addActionListener(a -> new ColorReferenceMode(controller));
    }
}
