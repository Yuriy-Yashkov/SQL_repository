package by.march8.ecs.application.modules.nsi;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;

public class NsiModuleSet implements ModuleSet {

    @Override
    public void initialModuleSet(MainController controller) {

        NsiModule nsiModule = new NsiModule();
        nsiModule.registerModule(controller);

        ReferencesModule referencesModule = new ReferencesModule();
        referencesModule.registerModule(controller);
    }
}
