package by.march8.ecs.application.modules.production.submodules.packing;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.production.submodules.packing.remains.RemainsModule;
import by.march8.ecs.application.modules.production.submodules.packing.reports.ReportsModule;
import by.march8.ecs.application.modules.production.submodules.packing.unsort.UsortModule;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

public class PackingModuleSet implements ModuleSet {
    private final JMenu miPacking = new JMenu("Упаковка");

    @Override
    public void initialModuleSet(MainController controller) {
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING, miPacking));

        ReportsModule reportsModule = new ReportsModule();
        reportsModule.registerModule(controller);

        RemainsModule remainsModule = new RemainsModule();
        remainsModule.registerModule(controller);

        UsortModule usortModule = new UsortModule();
        usortModule.registerModule(controller);
    }
}
