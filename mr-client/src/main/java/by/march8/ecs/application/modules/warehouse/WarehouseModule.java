package by.march8.ecs.application.modules.warehouse;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.sklad.SvodnayaVedomostPoFirmMag;
import dept.sklad.ho.CenaTMCSkHOForm;
import dept.sklad.ho.SkladHOForm;
import dept.sklad.ho.SpravTMCSkHOForm;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class WarehouseModule implements Module {

    private final JMenu jMenu31 = new JMenu("ХЭО");
    private final JMenu jMenu32 = new JMenu("Остатки");

    private final JMenuItem jMenuItem34 = new JMenuItem("Накладные на отгрузку");
    private final JMenuItem jMenuItem42 = new JMenuItem("Возвраты");
    private final JMenuItem jMenuItem59 = new JMenuItem("Спец. цены");
    private final JMenuItem jMenuItem76 = new JMenuItem("Разделение накладных");
    private final JMenuItem jMenuItem67 = new JMenuItem("Перенос спецификации");
    private final JMenuItem jMenuItem66 = new JMenuItem("Информация по накладной");
    private final JMenuItem jMenuItem79 = new JMenuItem("Отчет по отгрузке");
    private final JMenuItem jMenuItem78 = new JMenuItem("Сводная ведомость(7-я сводка)");
    private final JMenuItem jMenuItem68 = new JMenuItem("Экспорт отгрузки в DBF");
    private final JMenuItem jMenuItem80 = new JMenuItem("Отчет по инвентаризации (для сверки)");

    private final JMenuItem jMenuItem71 = new JMenuItem("Учет ТМЦ");
    private final JMenuItem jMenuItem72 = new JMenuItem("Справочник ТМЦ");
    private final JMenuItem jMenuItem73 = new JMenuItem("Цены ТМЦ");

    private final JMenuItem jMenuItem74 = new JMenuItem("Текущие остатки");
    private final JMenuItem jMenuItem75 = new JMenuItem("Оборотка");

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
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_SHIPMENTINVOICES, jMenuItem34));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_RETURNS, jMenuItem42));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_SPECIALPRICES, jMenuItem59));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_HEO, jMenu31));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_HEO_ACCOUNTING, jMenuItem71));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_HEO_CATALOG, jMenuItem72));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_HEO_PRICES, jMenuItem73));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_INVOICESPLIT, jMenuItem76));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_SPECTRANSFER, jMenuItem67));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_INVOICEINFO, jMenuItem66));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_SHIPMENTREPORT, jMenuItem79));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_SUMMARYSTATEMENT, jMenuItem78));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_EXPORTDBF, jMenuItem68));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_BALANCES, jMenu32));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_BALANCES_CURRENT, jMenuItem74));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_BALANCES_TURNOVER, jMenuItem75));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE_INVENTORYREPORT, jMenuItem80));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        jMenuItem34.addActionListener(a ->
                controller.openInternalFrame(new dept.sklad.Nakladnie(controller, true)));
        jMenuItem42.addActionListener(a ->
                new dept.sklad.Import.ImportNakladnie(controller, ownerFrame, true));
        jMenuItem59.addActionListener(a -> {
            Map hm = new HashMap();
            JDialog jd = new JDialog();
            jd.setTitle("mainForm");
            new dept.sklad.specCena.SpecCena(ownerFrame, hm);
        });
        jMenuItem76.addActionListener(a ->
                new dept.sklad.ShareNakl(ownerFrame));
        jMenuItem67.addActionListener(a ->
                new dept.sklad.TransferNakl(ownerFrame));
        jMenuItem66.addActionListener(a ->
                new dept.sklad.NaklInfo(ownerFrame));
        jMenuItem79.addActionListener(a ->
                new dept.sklad.ReportsOtgruz(ownerFrame));
        jMenuItem78.addActionListener(a ->
                new SvodnayaVedomostPoFirmMag(ownerFrame));
        jMenuItem68.addActionListener(a ->
                new dept.sklad.EksportOtgruzDBF(ownerFrame));
        jMenuItem80.addActionListener(a ->
                new dept.sklad.ForRevise(ownerFrame));

        jMenuItem71.addActionListener(a ->
                new SkladHOForm(controller, true));
        jMenuItem72.addActionListener(a ->
                new SpravTMCSkHOForm(controller, true));
        jMenuItem73.addActionListener(a ->
                new CenaTMCSkHOForm(controller, true));

        jMenuItem74.addActionListener(a ->
                new dept.sklad.ostatki.NowRemains(ownerFrame));
        jMenuItem75.addActionListener(a ->
                new dept.sklad.ostatki.RevOfCompany(ownerFrame));
    }
}
