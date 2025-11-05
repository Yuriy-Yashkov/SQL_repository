package by.march8.ecs.application.modules.packing.reception;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

/**
 * Модуль ПРИЕМКА НА УПАКОВКУ
 * Created by Andy on 19.03.2015.
 */
public class ReceptionPackingModule implements Module {
    private JMenu menu = new JMenu("Упаковка");
    private JMenuItem miReception = new JMenuItem("Прием продукции на упаковку");

    private MainController controller;

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_PACKING, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_PACKING_RECEPTION, miReception));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miReception.addActionListener(e -> new ReceptionPackingFunctionalMode(controller));
    }
}
