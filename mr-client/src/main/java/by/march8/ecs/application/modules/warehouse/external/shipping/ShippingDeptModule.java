package by.march8.ecs.application.modules.warehouse.external.shipping;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.sales.mode.SalesMonitorMode;
import by.march8.ecs.application.modules.warehouse.external.shipping.mode.FinishedGoodsMode;
import by.march8.ecs.application.modules.warehouse.external.shipping.mode.SaleDocumentReportMode;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;
import java.awt.*;

/**
 * Модуль отгрузки готовой продукции покупателю (Модуль продажи)
 */
public class ShippingDeptModule implements Module {

    private JMenuItem miShipping = new JMenuItem("Накладные на отгрузку готовой продукции");
    private JMenuItem miShippingReport = new JMenuItem("Обчет об отгрузке готовой продукции");


    private JMenuItem miSaleMonitor = new JMenuItem("Монитор отгрузки");
    private JMenu menu = new JMenu("Отгрузка продукции");
    private MainController controller;

    @Override
    public void registerModule(final MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        miShippingReport.setForeground(Color.BLUE);

        menu.add(miShipping);
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_FINISHEDGOODS, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_FINISHEDGOODS_SHIPPING
                , miShipping));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_FINISHEDGOODS_SALEMONITOR
                , miSaleMonitor));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_FINISHEDGOODS_REPORT
                , miShippingReport));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miShipping.addActionListener(e -> new FinishedGoodsMode(controller));
        miSaleMonitor.addActionListener(a -> new SalesMonitorMode(controller));
        miShippingReport.addActionListener(a -> new SaleDocumentReportMode(controller));
    }
}
