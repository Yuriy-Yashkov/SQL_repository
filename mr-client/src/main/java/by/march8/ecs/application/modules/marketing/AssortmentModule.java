package by.march8.ecs.application.modules.marketing;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.mode.ImageManagerMode;
import by.march8.ecs.application.modules.marketing.mode.EanCodeJournalMode;
import by.march8.ecs.application.modules.marketing.mode.ProductionCatalogMode;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.common.BackgroundTask;

import javax.swing.*;

public class AssortmentModule implements Module {

    private final JMenu mAssortment = new JMenu("Ассортимент");

    private final JMenuItem miImageBase = new JMenuItem("База изображений");
    private final JMenuItem miProductionCatalog = new JMenuItem("Каталог продукции");
    private final JMenuItem miEanJournal = new JMenuItem("Справочник EAN кодов");

    private MainController controller;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_ASSORTMENT, mAssortment));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_ASSORTMENT_IMAGEBASE, miImageBase));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_ASSORTMENT_PRODUCTIONCATALOG, miProductionCatalog));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_ASSORTMENT_EANJOURNAL, miEanJournal));
    }

    @Override
    public void registerMenuEvents() {
        miImageBase.addActionListener(a -> {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }
                @Override
                protected Boolean doInBackground() throws Exception {
                    new ImageManagerMode(controller);
                    return true;
                }
            }

            Task task = new Task("Запуск базы изображений ...");
            task.executeTask();
        });
        miProductionCatalog.addActionListener(a -> {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }
                @Override
                protected Boolean doInBackground() throws Exception {
                    new ProductionCatalogMode(controller);
                    return true;
                }
            }

            Task task = new Task("Запуск журнала каталогов...");
            task.executeTask();
        });
        miEanJournal.addActionListener(a -> new EanCodeJournalMode(controller));
    }
}
