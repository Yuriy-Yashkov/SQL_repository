package by.march8.ecs.application.modules.production;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.production.mode.CutCardMode;
import by.march8.ecs.application.modules.production.mode.JournalEanCodeViewMode;
import by.march8.ecs.application.modules.production.mode.RouteSheetViewMode;
import by.march8.ecs.application.modules.production.mode.SeamstressProductionMode;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.production.remnantsofcut.RemnantsOfCutMain;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

/**
 * Модуль отчеты производства
 */
public class ProductionDeptModule implements Module {

    private final JMenuItem miReportRouteSheet = new JMenuItem("Маршрутные листы");
    private final JMenuItem miReportJournalEanCode = new JMenuItem("Журнал EAN-кодов");
    private final JMenuItem miCutCardMode = new JMenuItem("Карты кроя");
    private final JMenuItem miSeamstressProductionMap = new JMenuItem("Карта выработки швеи");
    private final JMenuItem miCurrentRemnants = new JMenuItem("Текущие остатки кроя");

    private MainController controller;
    private JFrame ownerFrame;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = controller.getMainForm();
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_ROUTESHEET, miReportRouteSheet));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_JOURNALEANCODE, miReportJournalEanCode));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_CUTCARD, miCutCardMode));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_SEAMSTRESSMAP, miSeamstressProductionMap));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_REMAINS, miCurrentRemnants));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miReportRouteSheet.addActionListener(e ->
                new RouteSheetViewMode(controller));
        miReportJournalEanCode.addActionListener(e ->
                new JournalEanCodeViewMode(controller));
        miCutCardMode.addActionListener(e ->
                new CutCardMode(controller));
        miSeamstressProductionMap.addActionListener(a ->
                new SeamstressProductionMode(controller));
        miCurrentRemnants.addActionListener(a ->
                new RemnantsOfCutMain(ownerFrame, true));
    }

}
