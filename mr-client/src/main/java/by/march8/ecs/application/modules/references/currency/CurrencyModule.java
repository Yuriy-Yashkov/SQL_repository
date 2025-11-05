package by.march8.ecs.application.modules.references.currency;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.currency.mode.CurrencyRateMonitorMode;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

/**
 * @author Andy 05.08.2016.
 */
public class CurrencyModule implements Module {

    private final JMenuItem miCurrency = new JMenuItem("Монитор курсов валюты");
    private final JMenu menu = new JMenu("Валюта");
    private MainController controller;

    @Override
    public void registerModule(final MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        menu.add(miCurrency);
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_CURRENCY, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_CURRENCY_MONITOR
                , miCurrency));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miCurrency.addActionListener(e -> new CurrencyRateMonitorMode(controller));
    }
}
