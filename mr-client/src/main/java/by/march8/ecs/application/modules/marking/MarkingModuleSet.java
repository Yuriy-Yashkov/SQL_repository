package by.march8.ecs.application.modules.marking;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;

public class MarkingModuleSet implements ModuleSet {
    @Override
    public void initialModuleSet(MainController controller) {
        MarkingModule markingModule = new MarkingModule();
        markingModule.registerModule(controller);
    }
}
