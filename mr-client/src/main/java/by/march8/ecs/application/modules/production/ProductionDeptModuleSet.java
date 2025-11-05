package by.march8.ecs.application.modules.production;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.production.submodules.monitoring.MonitoringModule;
import by.march8.ecs.application.modules.production.submodules.packing.PackingModuleSet;
import by.march8.ecs.application.modules.production.submodules.planing.PlanningModule;
import by.march8.ecs.application.modules.production.submodules.cuttingAndSewing.CuttingAndSewingModule;

/**
 * Created by lidashka on 15.10.2018.
 */
public class ProductionDeptModuleSet implements ModuleSet {
    @Override
    public void initialModuleSet(MainController controller) {

        final PackingModuleSet packingModuleSet = new PackingModuleSet();
        packingModuleSet.initialModuleSet(controller);

        final  MonitoringModule monitoringModule = new MonitoringModule();
        monitoringModule.registerModule(controller);

        final CuttingAndSewingModule cuttingAndSewingModule = new CuttingAndSewingModule();
        cuttingAndSewingModule.registerModule(controller);

        final PlanningModule planningModule = new PlanningModule();
        planningModule.registerModule(controller);

        final ProductionDeptModule productionDeptModule = new ProductionDeptModule();
        productionDeptModule.registerModule(controller);
    }
}
