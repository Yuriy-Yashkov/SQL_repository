package by.march8.ecs.application.shell.administration;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.administration.controller.AdminLogModule;

/**
 * Набор модулей администратора
 * @see ModuleSet
 * Created by Andy on 10.09.2014.
 */
public class SettingsModuleSet implements ModuleSet {

    @Override
    public void initialModuleSet(final MainController controller) {
        new AdminControlModule().registerModule(controller);
        new AdminLogModule().registerModule(controller);
        new SettingsCommonModule().registerModule(controller);
    }
}
