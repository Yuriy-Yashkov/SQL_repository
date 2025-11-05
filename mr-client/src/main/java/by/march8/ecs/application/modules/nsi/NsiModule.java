package by.march8.ecs.application.modules.nsi;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.nsi.ChekcNDS;

import javax.swing.*;

public class NsiModule implements Module {

    private final JMenuItem miCheckNDS = new JMenuItem("Проверка ставок НДС");

    private MainController controller;
    private JFrame ownerFrame;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = controller.getMainForm();
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_CHECKNDS, miCheckNDS));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miCheckNDS.addActionListener(e -> new ChekcNDS(ownerFrame));
    }
}
