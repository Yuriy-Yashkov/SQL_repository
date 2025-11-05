package by.march8.ecs.application.modules.marketing;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;

public class MarketingModuleSet implements ModuleSet {

    @Override
    public void initialModuleSet(MainController controller) {
        MarketingModule marketingModule = new MarketingModule();
        marketingModule.registerModule(controller);

        AssortmentModule assortmentModule = new AssortmentModule();
        assortmentModule.registerModule(controller);

        ReportModule reportModule = new ReportModule();
        reportModule.registerModule(controller);
    }
}
