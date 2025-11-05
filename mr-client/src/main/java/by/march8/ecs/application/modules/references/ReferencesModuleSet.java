package by.march8.ecs.application.modules.references;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.art.ArtModule;
import by.march8.ecs.application.modules.references.common.CommonModule;
import by.march8.ecs.application.modules.references.currency.CurrencyModule;
import by.march8.ecs.application.modules.references.classifier.ClassifierModule;
import by.march8.ecs.application.modules.references.client.ClientModule;
import by.march8.ecs.application.modules.references.company.EnterpriseModule;
import by.march8.ecs.application.modules.settings.DocumentModule;
import by.march8.ecs.application.modules.references.materals.MaterialModule;
import by.march8.ecs.application.modules.references.product.ProductModule;
import by.march8.ecs.application.modules.references.readonly.ReadOnlyModule;
import by.march8.ecs.application.modules.references.standard.StandardsModule;

/**
 * Набор модулей СПРАВОЧНИК.
 *
 * @author andy-linux
 *
 */
public class ReferencesModuleSet implements ModuleSet {

    @Override
    public void initialModuleSet(final MainController controller) {

        final StandardsModule standardsModule = new StandardsModule();
        standardsModule.registerModule(controller);

        final EnterpriseModule enterpriseModule = new EnterpriseModule();
        enterpriseModule.registerModule(controller);

        final MaterialModule materialModule = new MaterialModule();
        materialModule.registerModule(controller);

        final ProductModule productModule = new ProductModule();
        productModule.registerModule(controller);

        final ArtModule artModule = new ArtModule();
        artModule.registerModule(controller);

        final ReadOnlyModule readOnlyModule = new ReadOnlyModule();
        readOnlyModule.registerModule(controller);

        final ClassifierModule classifierModule = new ClassifierModule();
        classifierModule.registerModule(controller);

        final ClientModule clientModule = new ClientModule();
        clientModule.registerModule(controller);

        final CurrencyModule currencyModule = new CurrencyModule();
        currencyModule.registerModule(controller);

        final DocumentModule documentModule = new DocumentModule();
        documentModule.registerModule(controller);

        final CommonModule commonModule = new CommonModule();
        commonModule.registerModule(controller);

    }
}
