package by.march8.ecs.application.modules.warehouse.internal.acceptiondept.yarn;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

/**
 * @author Andy 25.02.2015.
 */
public class YarnAcceptionModule implements Module {
    private JMenu menu = new JMenu("Приемка");
    private JMenuItem miYarn = new JMenuItem("Приемка сырья (пряжа/нить)");

    private MainController controller;


    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        menu.add(miYarn);
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_WAREHOUSE, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_WAREHOUSE_TEST, miYarn));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        // new TestFunctionalMode(controller);
        miYarn.addActionListener(e -> {

        });
    }
}
