package by.march8.ecs.application.modules.otk;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;

public class OtkModuleSet implements ModuleSet {
    @Override
    public void initialModuleSet(MainController controller) {
        OtkModule otkModule = new OtkModule();
        otkModule.registerModule(controller);
    }
}
