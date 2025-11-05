package by.march8.ecs.application.modules.tech;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;

public class TechModuleSet implements ModuleSet {
    @Override
    public void initialModuleSet(MainController controller) {
        final TechModule techModule = new TechModule();
        techModule.registerModule(controller);
    }
}
