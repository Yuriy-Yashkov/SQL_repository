package by.march8.ecs.application.modules.otk;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

public class OtkModule implements Module {

    private final JMenuItem miPack = new JMenuItem("Упаковка");

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
        controller.addModuleMenu(new SectionMenu(MarchSection.OTK_PACK, miPack));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miPack.addActionListener(a -> new dept.otk.DataForm(ownerFrame, true));
    }
}
