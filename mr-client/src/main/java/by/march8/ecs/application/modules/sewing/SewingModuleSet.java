package by.march8.ecs.application.modules.sewing;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;

public class SewingModuleSet implements ModuleSet {
    @Override
    public void initialModuleSet(MainController controller) {
        SewingModule sewingModule = new SewingModule();
        sewingModule.registerModule(controller);
    }
}
