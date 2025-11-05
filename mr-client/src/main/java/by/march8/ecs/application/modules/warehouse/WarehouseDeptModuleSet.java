package by.march8.ecs.application.modules.warehouse;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.ShippingDeptModule;
import by.march8.ecs.application.modules.warehouse.internal.acceptiondept.yarn.YarnAcceptionModule;
import by.march8.ecs.application.modules.warehouse.internal.displacement.DisplacementDeptModule;

/**
 * @author Andy 16.12.2014.
 */
public class WarehouseDeptModuleSet implements ModuleSet {
    @Override
    public void initialModuleSet(final MainController controller) {
        YarnAcceptionModule yarnAcceptionModule = new YarnAcceptionModule();
        yarnAcceptionModule.registerModule(controller);

        DisplacementDeptModule displacementDeptModule = new DisplacementDeptModule();
        displacementDeptModule.registerModule(controller);

        ShippingDeptModule shippingDeptModule = new ShippingDeptModule();
        shippingDeptModule.registerModule(controller);

        WarehouseModule warehouseModule = new WarehouseModule();
        warehouseModule.registerModule(controller);
    }
}
