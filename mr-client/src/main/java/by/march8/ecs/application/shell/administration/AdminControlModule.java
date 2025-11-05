package by.march8.ecs.application.shell.administration;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.administration.controller.AdminController;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;

/**
 * Модуль администрирования
 *
 * @see Module
 * Created by Andy on 10.09.2014.
 */
public class AdminControlModule implements Module {
    final JMenu adminReferences = new JMenu("Справочник");
    private final JMenuItem miAdminControlPanel = new JMenuItem(
            "Панель управления");
    private final JMenuItem miUserRoles = new JMenuItem(
            MarchReferencesType.ADM_ROLE.getShortName());
    private final JMenuItem miUserRights = new JMenuItem(
            MarchReferencesType.ADM_RIGHT.getShortName());
    private final JMenuItem miUserFunctionMode = new JMenuItem(
            MarchReferencesType.ADM_FUNCTION_MODE.getShortName());
    private final JMenu adminMenu = new JMenu("Администрирование");
    private MainController controller;

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_ADMINISTRATOR, adminMenu));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_ADMINISTRATOR_CONTROLPANEL, miAdminControlPanel));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_ADMINISTRATOR_REFERENCES, adminReferences));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_ADMINISTRATOR_REFERENCES_USERROLE, miUserRoles));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_ADMINISTRATOR_REFERENCES_USERRIGHT, miUserRights));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_ADMINISTRATOR_REFERENCES_USERMODE, miUserFunctionMode));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miAdminControlPanel.addActionListener(e -> new AdminController(controller));

        miUserRoles.addActionListener(e -> new Reference(controller, MarchReferencesType.ADM_ROLE,
                MarchWindowType.INTERNALFRAME));

        miUserRights.addActionListener(e -> new Reference(controller, MarchReferencesType.ADM_RIGHT,
                MarchWindowType.INTERNALFRAME));

        miUserFunctionMode.addActionListener(e -> new Reference(controller,
                MarchReferencesType.ADM_FUNCTION_MODE,
                MarchWindowType.INTERNALFRAME));

    }
}
