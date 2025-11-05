package by.march8.ecs.application.modules.marketing;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.mode.MarketingPriceListJournalMode;
import by.march8.ecs.application.modules.marketing.mode.WarehouseRemainsMode;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.marketing.MarketingForm;
import dept.marketing.cena.CenaForm;

import javax.swing.*;

public class MarketingModule implements Module {
    private final JMenuItem jMenuItem21 = new JMenuItem("Оформление заявок");
    private final JMenuItem miWarehouse = new JMenuItem("Склад");
    private final JMenuItem jMenuItem31 = new JMenuItem("Просмотр заявок");
    private final JMenuItem jMenuItem32 = new JMenuItem("Накладные на отгрузку");
    private final JMenuItem jMenuItem41 = new JMenuItem("Цены");
    private final JMenuItem miRemains = new JMenuItem("Оперативные остатки продукции");
    private final JMenuItem miMarketingPriceList = new JMenuItem("Журнал документов уценки");

    private MainController controller;
    private JFrame ownerFrame;
    private String progPath;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = controller.getMainForm();
        progPath = controller.getBootstrap().getRunPath();
        registerMenu();
    }

    @Override
    public void registerMenu() {
        miRemains.setIcon(new ImageIcon(progPath + "/Img/database_refresh_24.png"));
        miMarketingPriceList.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/document_24.png"));

        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_REQUESTSCREATE, jMenuItem21));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_WAREHOUSE, miWarehouse));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_REQUESTSVIEW, jMenuItem31));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_SHIPMENTINVOICES, jMenuItem32));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_PRICES, jMenuItem41));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_REMAINS, miRemains));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_PRICELIST, miMarketingPriceList));


        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        jMenuItem21.addActionListener(a -> new MarketingForm(ownerFrame, true));
        miWarehouse.addActionListener(a -> new dept.marketing.SkladForm(ownerFrame, true));
        jMenuItem31.addActionListener(a -> new dept.marketing.OrdersForm(ownerFrame, true));
        jMenuItem32.addActionListener(a -> new dept.marketing.Nakladnie(ownerFrame, true));
        jMenuItem41.addActionListener(a -> new CenaForm(controller, true));

        miRemains.addActionListener(a -> new WarehouseRemainsMode(controller, RightEnum.WRITE));
        miMarketingPriceList.addActionListener(a -> new MarketingPriceListJournalMode(controller, RightEnum.WRITE));

    }
}
