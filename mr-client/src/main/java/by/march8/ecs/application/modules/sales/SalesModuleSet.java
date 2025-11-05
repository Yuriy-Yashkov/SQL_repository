package by.march8.ecs.application.modules.sales;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;

public class SalesModuleSet implements ModuleSet {

    @Override
    public void initialModuleSet(MainController controller) {
        SalesModule salesModule = new SalesModule();
        salesModule.registerModule(controller);
    }
}
