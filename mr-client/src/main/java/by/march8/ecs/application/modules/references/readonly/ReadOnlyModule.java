package by.march8.ecs.application.modules.references.readonly;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;


/**
 * МОдуль справочников программы March8
 * Created by Andy on 13.08.2015.
 */
public class ReadOnlyModule implements Module {
    private MainController controller;
    private JMenu menu = new JMenu("Справочники March8 (только чтение)");

    private JMenuItem miContractor = new JMenuItem("Контрагенты");
    private JMenuItem miCurrency = new JMenuItem("Валюта");


    @Override
    public void registerModule(final MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_READONLY, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_READONLY_CONTRACTOROLD, miContractor));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_READONLY_CURRENCYOLD, miCurrency));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miContractor.addActionListener(e -> new Reference(controller, MarchReferencesType.CONTRACTOR_OLD,
                MarchWindowType.INTERNALFRAME));
        miCurrency.addActionListener(e -> new Reference(controller, MarchReferencesType.CURRENCY_OLD,
                MarchWindowType.INTERNALFRAME));
    }

}
