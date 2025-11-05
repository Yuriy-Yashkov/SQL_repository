package by.march8.ecs.application.modules.packing;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.label.LabelModule;
import by.march8.ecs.application.modules.packing.reception.ReceptionPackingModule;

/**
 * Набор модулей отдела УПАКОВКА
 * Created by Andy on 19.03.2015.
 */
public class PackingModuleSet implements ModuleSet {
    @Override
    public void initialModuleSet(final MainController controller) {

        final ReceptionPackingModule receptionPackingModule = new ReceptionPackingModule();
        receptionPackingModule.registerModule(controller);


        final LabelModule labelModule = new LabelModule();
        labelModule.registerModule(controller);

    }
}
