package by.march8.ecs.application.modules.economists.modules;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.calculationprice.CalculationPriceForm;

import javax.swing.*;

public class EconomistCalculationModule implements Module {

    private final JMenu calculationMenu = new JMenu("Калькуляция");
    private final JMenuItem miCalculationPrice = new JMenuItem("Расчет цен");

    private MainController controller;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        calculationMenu.add(miCalculationPrice);

        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST_CALCULATION, calculationMenu));
        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST_CALCULATION_PRICES, miCalculationPrice));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miCalculationPrice.addActionListener(e ->
                new CalculationPriceForm(controller, false));
    }
}
