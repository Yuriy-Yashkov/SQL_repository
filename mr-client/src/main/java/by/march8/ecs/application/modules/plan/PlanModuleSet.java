package by.march8.ecs.application.modules.plan;


import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;

/**
 * Created by dpliushchai on 19.11.2014.
 */
public class PlanModuleSet implements ModuleSet {

    @Override
    public void initialModuleSet(final MainController controller) {
        // ***************************************************
        // Инициализация План
        // ***************************************************
        final PlanModule planModule = new PlanModule();
        planModule.registerModule(controller);
    }
}
