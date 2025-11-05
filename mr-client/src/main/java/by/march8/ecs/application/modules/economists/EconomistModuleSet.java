package by.march8.ecs.application.modules.economists;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.economists.modules.EconomistCalculationModule;
import by.march8.ecs.application.modules.economists.modules.EconomistModule;
import by.march8.ecs.application.modules.economists.modules.EconomistRemainModule;
import by.march8.ecs.application.modules.economists.modules.EconomistReportModule;

public class EconomistModuleSet implements ModuleSet {
    @Override
    public void initialModuleSet(MainController controller) {

        EconomistReportModule economistReportModule = new EconomistReportModule();
        economistReportModule.registerModule(controller);

        EconomistCalculationModule economistCalculationModule = new EconomistCalculationModule();
        economistCalculationModule.registerModule(controller);

        EconomistRemainModule planningModule = new EconomistRemainModule();
        planningModule.registerModule(controller);

        EconomistModule productionDeptModule = new EconomistModule();
        productionDeptModule.registerModule(controller);
    }
}
