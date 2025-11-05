package by.march8.ecs.application.modules.references.company;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;

/**
 * Модуль справочников ПРЕДПРИЯТИЕ
 *
 * @author andy-linux
 */
public class EnterpriseModule implements Module {

    public static JMenuItem miDepartment = new JMenuItem(
            MarchReferencesType.COMPANY_DEPARTMENTS.getShortName());
    public static JMenuItem miPosition = new JMenuItem(
            MarchReferencesType.COMPANY_POSITION.getShortName());
    public static JMenuItem miEmployees = new JMenuItem(
            MarchReferencesType.COMPANY_EMPLOYEES.getShortName());
    public static JMenuItem miEquipment = new JMenuItem(
            MarchReferencesType.EQUIPMENT.getShortName());

    public final JMenu menu = new JMenu("Предприятие");

    private MainController controller;

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_COMPANY, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_COMPANY_DEPARTMENT, miDepartment));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_COMPANY_POSITION, miPosition));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_COMPANY_EMPLOYEES, miEmployees));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_COMPANY_EQUIPMENT, miEquipment));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miDepartment.addActionListener(e -> new Reference(controller, MarchReferencesType.COMPANY_DEPARTMENTS,
                MarchWindowType.INTERNALFRAME));

        miPosition.addActionListener(e -> new Reference(controller, MarchReferencesType.COMPANY_POSITION,
                MarchWindowType.INTERNALFRAME));

        miEmployees.addActionListener(e -> new Reference(controller, MarchReferencesType.COMPANY_EMPLOYEES,
                MarchWindowType.INTERNALFRAME));

        miEquipment.addActionListener(e -> new Reference(controller, MarchReferencesType.EQUIPMENT,
                MarchWindowType.INTERNALFRAME));

    }
}
