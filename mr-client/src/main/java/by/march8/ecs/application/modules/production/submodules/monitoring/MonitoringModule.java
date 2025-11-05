package by.march8.ecs.application.modules.production.submodules.monitoring;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.production.monitoring.DataForm;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MonitoringModule implements Module {

    private final JMenu miMonitoringMenu = new JMenu("Мониторинг");
    private final JMenuItem miMadeToOrder = new JMenuItem("Производство под заказ");

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
        miMonitoringMenu.add(miMadeToOrder);
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_MONITORING, miMonitoringMenu));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_MONITORING_MADE_TO_ORDER, miMadeToOrder));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miMadeToOrder.addActionListener(a -> new DataForm(ownerFrame, true));
    }

}
