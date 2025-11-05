package by.march8.ecs.application.modules.production.submodules.packing.remains;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class RemainsModule implements Module {

    private final JMenu menu = new JMenu("Остатки");
    private final JMenuItem miCurrentBalance = new JMenuItem("Текущие остатки");
    private final JMenuItem miMonthBalance = new JMenuItem("Остатки на начало месяца");
    private final JMenuItem miDateBalance = new JMenuItem("Остатки на дату");

    private MainController controller;
    private JFrame ownerFrame;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = mainController.getMainForm();
        registerMenu();
    }

    @Override
    public void registerMenu() {
        menu.add(miCurrentBalance);
        menu.add(miMonthBalance);
        menu.add(miDateBalance);
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_BALANCE, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_BALANCE_CURRENT, miCurrentBalance));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_BALANCE_MONTH, miMonthBalance));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_BALANCE_DATE, miDateBalance));
    }

    @Override
    public void registerMenuEvents() {
        miCurrentBalance.addActionListener(e ->
                new dept.upack.ostatki.OstDataForm(ownerFrame, true, 0));
        miMonthBalance.addActionListener(a ->
                new dept.upack.ostatki.OstDataForm(ownerFrame, true, 1));
        miDateBalance.addActionListener(a ->
                new dept.upack.ostatki.OstDataForm(ownerFrame, true, 2));
    }
}
