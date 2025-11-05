package by.march8.ecs.application.modules.wes;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;

public class WesModuleSet implements ModuleSet {
    @Override
    public void initialModuleSet(MainController controller) {
        WesModule wesModule = new WesModule();
        wesModule.registerModule(controller);
    }
}
