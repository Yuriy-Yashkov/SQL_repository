package by.march8.ecs.application.modules.economists.modules;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.economists.mode.WarehousePriceListJournalMode;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

public class EconomistRemainModule implements Module {

    private final JMenu miRemainsPriceList = new JMenu("Прейскурант остатков");
    private final JMenuItem miRemainsPriceListJournal = new JMenuItem("Журнал прейскурантов");
    private final JMenuItem miRemainsPriceListView = new JMenuItem("Просмотр прейскурантов");

    private MainController controller;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        miRemainsPriceList.add(miRemainsPriceListJournal);
        miRemainsPriceList.add(miRemainsPriceListView);

        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST_REMAINS, miRemainsPriceList));
        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST_REMAINS_VIEW, miRemainsPriceListView));
        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST_REMAINS_JOURNAL, miRemainsPriceListJournal));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miRemainsPriceListJournal.addActionListener(a ->
                new WarehousePriceListJournalMode(controller, RightEnum.WRITE));
        miRemainsPriceListView.addActionListener(a ->
                new WarehousePriceListJournalMode(controller, RightEnum.READ));
    }
}
