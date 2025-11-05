package dept;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.mode.ImageManagerMode;
import by.march8.ecs.application.modules.marketing.mode.ERPRemainsMode;
import by.march8.ecs.application.modules.marketing.mode.ProductionCatalogMode;
import by.march8.ecs.application.modules.sales.mode.PreOrderSaleDocumentMode;
import by.march8.ecs.application.modules.sales.mode.SalesMonitorMode;
import by.march8.ecs.application.modules.sales.model.PreOrderControlType;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.application.shell.servicemonitor.ServiceMonitorMode;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.ConsoleMode;
import common.ProgressBar;
import dept.econom.InDateForm;
import dept.econom.PoshivDateForm;
import dept.markerovka.PrintLabelForm;
import dept.markerovka.TransItemForm;
import dept.marketing.MarketingForm;
import dept.marketing.cena.CenaForm;
import dept.mylog.LoginDataForm;
import dept.nsi.ChekcNDS;
import dept.nsi.ClientKT;
import dept.nsi.Otgruz;
import dept.nsi.PlanSstoimost;
import dept.nsi.TrudoZat;
import dept.production.planning.PlanProductioForm;
import dept.production.planning.ProjectPlanForm;
import dept.production.zsh.spec.SpecForm;
import dept.production.zsh.zplata.BuhVedomostForm;
import dept.production.zsh.zplata.UtilZPlata;
import dept.production.zsh.zplata.ZPlataForm;
import dept.sbit.SendMailForm;
import dept.sbit.client.form.ClientForm;
import dept.sklad.SvodnayaVedomostPoFirmMag;
import dept.sklad.ho.CenaTMCSkHOForm;
import dept.sklad.ho.SkladHOForm;
import dept.sklad.ho.SpravTMCSkHOForm;
import dept.sprav.employe.EmployeForm;
import dept.sprav.model.ModelForm;
import dept.sprav.tech.TechForm;
import dept.sprav.valuta.ValutaForm;
import dept.sprav.valuta.ValutaKursForm;
import dept.tech.EanList;
import dept.tech.innovation.Production;
import dept.tools.ToolsForm;
import dept.tools.imgmanager.ImageManagerForm;
import lombok.Getter;
import workOO.OpenOffice;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Andy on 18.09.2015.
 */

public class MyReportsMenuBar extends JMenuBar {
    private JMenu jMenu1;
    private JMenu jMenu10;
    private JMenu jMenu11;
    private JMenu jMenu12;
    private JMenu jMenu13;
    private JMenu jMenu14;
    private JMenu jMenu15;
    private JMenu jMenu16;
    private JMenu jMenu17;
    private JMenu jMenu18;
    private JMenu jMenu19;
    private JMenu jMenu2;
    private JMenu jMenu20;
    private JMenu jMenu21;
    private JMenu jMenu22;
    private JMenu jMenu23;
    private JMenu jMenu24;
    private JMenu jMenu25;
    private JMenu jMenu26;
    private JMenu jMenu27;
    private JMenu jMenu28;
    private JMenu jMenu29;
    private JMenu jMenu3;
    private JMenu jMenu30;
    private JMenu jMenu31;
    private JMenu jMenu32;
    private JMenu jMenu4;
    public static JMenu MENU_SETTINGS  = new JMenu("Настройки");
    private JMenu jMenu6;
    private JMenu jMenu7;
    private JMenu jMenu8;
    private JMenu jMenu9;
    private JMenu jMenuCalculationPrice;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem10;
    private JMenuItem jMenuItem12;
    private JMenuItem jMenuItem13;
    private JMenuItem jMenuItem14;
    private JMenuItem jMenuItem15;
    private JMenuItem jMenuItem16;
    private JMenuItem jMenuItem17;
    private JMenuItem jMenuItem18;
    private JMenuItem jMenuItem19;
    private JMenuItem jMenuItem2;
    private JMenuItem jMenuItem20;
    private JMenuItem jMenuItem21;
    private JMenuItem jMenuItem22;
    private JMenuItem jMenuItem23;
    private JMenuItem jMenuItem24;
    private JMenuItem jMenuItem25;
    private JMenuItem jMenuItem26;
    private JMenuItem jMenuItem27;
    private JMenuItem jMenuItem28;
    private JMenuItem jMenuItem29;
    private JMenuItem jMenuItem3;
    private JMenuItem jMenuItem30;
    private JMenuItem jMenuItem31;
    private JMenuItem jMenuItem32;
    private JMenuItem jMenuItem33;
    private JMenuItem jMenuItem34;
    private JMenuItem jMenuItem35;
    private JMenuItem jMenuItem36;
    private JMenuItem jMenuItem37;
    private JMenuItem jMenuItem38;
    private JMenuItem jMenuItem39;
    private JMenuItem jMenuItem4;
    private JMenuItem jMenuItem40;
    private JMenuItem jMenuItem41;
    private JMenuItem jMenuItem42;
    private JMenuItem jMenuItem43;
    private JMenuItem jMenuItem44;
    private JMenuItem miCurrencyRateUpdater;
    private JMenuItem jMenuItem45;
    private JMenuItem jMenuItem46;
    private JMenuItem jMenuItem47;
    private JMenuItem jMenuItem48;
    private JMenuItem jMenuItem49;
    private JMenuItem jMenuItem5;
    private JMenuItem jMenuItem50;
    private JMenuItem jMenuItem51;
    private JMenuItem jMenuItem52;
    private JMenuItem jMenuItem53;
    private JMenuItem jMenuItem54;
    private JMenuItem jMenuItem55;
    private JMenuItem jMenuItem56;
    private JMenuItem jMenuItem57;
    private JMenuItem jMenuItem58;
    private JMenuItem jMenuItem59;
    private JMenuItem jMenuItem6;
    private JMenuItem jMenuItem60;
    private JMenuItem jMenuItem61;
    private JMenuItem jMenuItem62;
    private JMenuItem jMenuItem63;
    private JMenuItem jMenuItem64;
    private JMenuItem jMenuItem65;
    private JMenuItem jMenuItem66;
    private JMenuItem jMenuItem67;
    private JMenuItem jMenuItem68;
    private JMenuItem jMenuItem69;
    private JMenuItem jMenuItem7;
    private JMenuItem jMenuItem70;
    private JMenuItem jMenuItem71;
    private JMenuItem jMenuItem72;
    private JMenuItem jMenuItem73;
    private JMenuItem jMenuItem74;
    private JMenuItem jMenuItem75;
    private JMenuItem jMenuItem76;
    private JMenuItem jMenuItem77;
    private JMenuItem jMenuItem78;
    private JMenuItem jMenuItem79;
    private JMenuItem jMenuItem8;
    private JMenuItem jMenuItem80;
    private JMenuItem jMenuItem81;
    private JMenuItem jMenuItem82;
    private JMenuItem jMenuItem83;
    private JMenuItem jMenuItem84;
    private JMenuItem jMenuItem85;
    private JMenuItem jMenuItem9;
    private JMenuItem jMenuItem90;
    private JMenuItem miERPRemains = new JMenuItem();
    private JMenuItem miServices = new JMenuItem();
    private JMenuItem miConsole = new JMenuItem();
    private JMenuItem miSaleDept = new JMenuItem();
    private JMenuItem miRemains = new JMenuItem();
    private JMenuItem jMenuItemCalculationPrice;
    private JMenuItem miSkladTekOst;
    private JMenuItem miRemainsPriceList ;
    private JMenuItem miRemainsPriceListJournal ;
    private JMenuItem miRemainsPriceListView ;
    private JMenuItem miMarketingPriceList ;
    private JMenuItem miEMailHistory ;
    private JMenuItem miEurotorg ;

    private JMenu mUnloading ;
    public static final JMenu MENU_FILE = new JMenu("Файл");
    public static final JMenu MENU_REFERENCE = new JMenu("Справочники");
    public static final JMenu MENU_OTK = new JMenu("ОТК");
    public static final JMenu MENU_NSI = new JMenu("НСИ");
    public static final JMenu MENU_PRODUCTION = new JMenu("Производство");
    public static final JMenu MENU_WAREHOUSE = new JMenu("Склад");
    public static final JMenu MENU_SEWING = new JMenu("Пошив");
    public static final JMenu MENU_PLAN = new JMenu("План");
    public static final JMenu MENU_SALES = new JMenu("Сбыт");
    public static final JMenu MENU_WES = new JMenu("ВЭС");
    public static final JMenu MENU_MARKING = new JMenu("Маркировка");
    public static final JMenu MENU_MARKETING = new JMenu("Маркетинг");
    public static final JMenu MENU_ECONOMIST = new JMenu("Экономисты");
    public static final JMenu MENU_ACCOUNTING = new JMenu("Учет продукции");
    public static final JMenu MENU_ART = new JMenu("ХЭО");
    public static final JMenu MENU_INNOVATIONS = new JMenu("Нововведения");
    private static final JMenu MENU_TECH = new JMenu("Тех отдел");

    private JMenuItem miRelogin = new JMenuItem("Сменить пользователя") ;
    private JMenuItem miExit = new JMenuItem("Выход") ;

    @Getter
    private Map<String, JMenuItem> treeMenu = new TreeMap<>();

    private MainController controller;
    private JFrame ownerFrame;
    private String progPath;
    private JMenu mAssortment;
    private JMenuItem miProductionCatalog;
    private JMenuItem miImageBase;
    private JMenuItem miPreOrderExport;
    private JMenuItem miPreOrderInternal;
    private JMenuItem miSetClient;
    private JMenuItem miEanJournal;

    public MyReportsMenuBar(MainController mainController) {
        controller = mainController;
        progPath = controller.getBootstrap().getRunPath();
        ownerFrame = controller.getMainForm();
        initComponents();
        initEvents();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new JMenuItem();
        jMenuItem19 = new JMenuItem();
        jMenuItem77 = new JMenuItem();
        jMenuItem48 = new JMenuItem();
        jMenuItem53 = new JMenuItem();
        jMenuItem50 = new JMenuItem();
        jMenuItem21 = new JMenuItem();
        jMenuItem31 = new JMenuItem();
        jMenuItem25 = new JMenuItem();
        jMenuItem32 = new JMenuItem();
        jMenuItem41 = new JMenuItem();
        jMenuItem37 = new JMenuItem();
        jMenuItem38 = new JMenuItem();
        jMenuItem56 = new JMenuItem();
        jMenuItem58 = new JMenuItem();
        jMenuItem16 = new JMenuItem();
        jMenuItem33 = new JMenuItem();
        jMenuItem36 = new JMenuItem();
        jMenuItem40 = new JMenuItem();
        jMenuItem43 = new JMenuItem();
        jMenuItem60 = new JMenuItem();
        jMenuItem65 = new JMenuItem();
        jMenuItem64 = new JMenuItem();
        jMenuItem14 = new JMenuItem();
        jMenuItem15 = new JMenuItem();
        jMenuItem17 = new JMenuItem();
        jMenuItem18 = new JMenuItem();
        jMenuItem20 = new JMenuItem();
        jMenuItem49 = new JMenuItem();
        jMenuItem55 = new JMenuItem();
        jMenuItem85 = new JMenuItem();
        jMenuItem8 = new JMenuItem();
        jMenuItem9 = new JMenuItem();
        jMenuItem12 = new JMenuItem();
        jMenuItem29 = new JMenuItem();
        jMenuItem35 = new JMenuItem();
        jMenuItem54 = new JMenuItem();
        jMenuItem62 = new JMenuItem();
        jMenuItem26 = new JMenuItem();
        jMenuItem27 = new JMenuItem();
        jMenuItem28 = new JMenuItem();
        jMenuItem13 = new JMenuItem();
        jMenuItem39 = new JMenuItem();
        jMenuItem46 = new JMenuItem();
        jMenuItem57 = new JMenuItem();
        jMenuItem61 = new JMenuItem();
        jMenuItem69 = new JMenuItem();
        jMenuItem81 = new JMenuItem();
        jMenuItem83 = new JMenuItem();
        jMenuItem70 = new JMenuItem();
        jMenuItem6 = new JMenuItem();
        jMenuItem63 = new JMenuItem();
        jMenuItem84 = new JMenuItem();
        jMenuItem23 = new JMenuItem();
        jMenuItem24 = new JMenuItem();
        miSkladTekOst = new JMenuItem();
        jMenuItem34 = new JMenuItem();
        jMenuItem42 = new JMenuItem();
        jMenuItem59 = new JMenuItem();
        jMenuItem71 = new JMenuItem();
        jMenuItem72 = new JMenuItem();
        jMenuItem73 = new JMenuItem();
        jMenuItem76 = new JMenuItem();
        jMenuItem67 = new JMenuItem();
        jMenuItem66 = new JMenuItem();
        jMenuItem79 = new JMenuItem();
        jMenuItem78 = new JMenuItem();
        jMenuItem68 = new JMenuItem();
        jMenuItem74 = new JMenuItem();
        jMenuItem75 = new JMenuItem();
        jMenuItem80 = new JMenuItem();
        jMenuItem30 = new JMenuItem();
        jMenuItem44 = new JMenuItem();
        miCurrencyRateUpdater = new JMenuItem();
        jMenuItem45 = new JMenuItem();
        jMenuItem51 = new JMenuItem();
        jMenuItem52 = new JMenuItem();
        jMenuItem82 = new JMenuItem();
        jMenuItem10 = new JMenuItem();
        jMenuItem47 = new JMenuItem();
        jMenuItem3 = new JMenuItem();
        jMenuItem4 = new JMenuItem();
        jMenuItem7 = new JMenuItem();
        jMenuItemCalculationPrice = new JMenuItem();
        miRemainsPriceListJournal = new JMenuItem();
        miRemainsPriceListView = new JMenuItem();
        miMarketingPriceList = new JMenuItem();
        miEMailHistory = new JMenuItem();
        miEurotorg = new JMenuItem("Евроторг");
        jMenuItem22 = new JMenuItem();
        jMenuItem2 = new JMenuItem();
        jMenuItem5 = new JMenuItem();
        // jMenuItem11 = new JMenuItem();
        jMenuItem90 = new JMenuItem();

        jMenu1 = new JMenu();
        jMenu16 = new JMenu();
        jMenu26 = new JMenu();
        jMenu17 = new JMenu();
        jMenu23 = new JMenu();
        jMenu14 = new JMenu();
        jMenu15 = new JMenu();
        jMenu11 = new JMenu();
        jMenu12 = new JMenu();
        jMenu13 = new JMenu();
        jMenu27 = new JMenu();
        jMenu28 = new JMenu();
        jMenu7 = new JMenu();
        jMenu9 = new JMenu();
        jMenu21 = new JMenu();
        jMenu10 = new JMenu();
        jMenu24 = new JMenu();
        jMenu29 = new JMenu();
        jMenu6 = new JMenu();
        jMenu30 = new JMenu();
        jMenu19 = new JMenu();
        jMenu20 = new JMenu();
        jMenu31 = new JMenu();
        jMenu32 = new JMenu();
        jMenu22 = new JMenu();
        jMenu25 = new JMenu();
        jMenu8 = new JMenu();
        jMenu4 = new JMenu();
        jMenu2 = new JMenu();
        jMenuCalculationPrice = new JMenu();
        miRemainsPriceList = new JMenu();
        mUnloading = new JMenu("Выгрузки");
        jMenu3 = new JMenu();
        jMenu18 = new JMenu();
        mAssortment = new JMenu();
        miProductionCatalog = new JMenuItem();
        miImageBase = new JMenuItem();
        miEanJournal = new JMenuItem();
        miPreOrderExport = new JMenuItem();

        //  mFinishedProducts = new JMenu(MENU_FINISHEDPRODUCTS) ;

        // ----------создаём иконки к пунктам меню---------------
        try {
            miExit.setIcon(new ImageIcon(progPath + "/Img/cross.png"));
            jMenuItem6.setIcon(new ImageIcon(progPath + "/Img/email.png"));
            jMenu10.setIcon(new ImageIcon(progPath + "/Img/application.png"));
            jMenuItem5.setIcon(new ImageIcon(progPath + "/Img/cog.png"));
            jMenuItem2.setIcon(new ImageIcon(progPath + "/Img/help.png"));
            jMenu18.setIcon(new ImageIcon(progPath + "/Img/myLog.png"));
            jMenuItem21.setIcon(new ImageIcon(progPath + "/Img/users_group.png"));
            jMenuItem31.setIcon(new ImageIcon(progPath + "/Img/filefind.png"));
            jMenuItem25.setIcon(new ImageIcon(progPath + "/Img/bankId.png"));
            jMenuItem41.setIcon(new ImageIcon(progPath + "/Img/price.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //logger.error("Ошибка при создании иконок для меню", e);
            JOptionPane.showMessageDialog(null,
                    "Ошибка при создании иконок для меню", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
        }

        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE, MENU_REFERENCE));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING, MENU_ACCOUNTING));
        controller.addModuleMenu(new SectionMenu(MarchSection.ART, MENU_ART));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION, MENU_PRODUCTION));
        controller.addModuleMenu(new SectionMenu(MarchSection.SALES, MENU_SALES));
        controller.addModuleMenu(new SectionMenu(MarchSection.SEWING, MENU_SEWING));
        controller.addModuleMenu(new SectionMenu(MarchSection.PLAN, MENU_PLAN));
        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST, MENU_ECONOMIST));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS, MENU_SETTINGS));
        controller.addModuleMenu(new SectionMenu(MarchSection.TECH, MENU_TECH));
        controller.addModuleMenu(new SectionMenu(MarchSection.WES, MENU_WES));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKING, MENU_MARKING));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING, MENU_MARKETING));
        controller.addModuleMenu(new SectionMenu(MarchSection.OTK, MENU_OTK));
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI, MENU_NSI));
        controller.addModuleMenu(new SectionMenu(MarchSection.WAREHOUSE, MENU_WAREHOUSE));

        add(MENU_FILE);
        createFileMenu();
        add(MENU_WES);
        add(MENU_MARKING);
        add(MENU_MARKETING);
        add(MENU_NSI);
        add(MENU_OTK);
        add(MENU_ACCOUNTING);
        add(MENU_ART);
        add(MENU_PLAN);
        add(MENU_PRODUCTION);
        add(MENU_SALES);
        add(MENU_SEWING);
        add(MENU_WAREHOUSE);
        add(MENU_REFERENCE);
        add(MENU_TECH);
        add(MENU_ECONOMIST);
        add(MENU_SETTINGS);
//        createWESMenu();
//        createLabelMenu();
//        createMarketingMenu();
//        createNSIMenu();
//        createOTKMenu();
//        createProductionMenu();
//        createSalesMenu();
//        createSewingMenu();
//        createWarehouseMenu();
//        createTechDepartmentMenu();
//        createEconomistsMenu();
//        createSettingsMenu();
        createHelpMenu();
    }

    private void createFileMenu() {
        MENU_FILE.add(miRelogin);
        MENU_FILE.add(miExit);
        miExit.addActionListener(e -> controller.applicationTerminate());
        miRelogin.addActionListener(e -> controller.changeUser());
    }
//
//    private void createWESMenu() {
//        jMenu16.setText("ВЭС");
//        jMenuItem19.setText("Отправка накладных");
//        jMenuItem19.addActionListener(this::jMenuItem19ActionPerformed);
//        jMenu16.add(jMenuItem19);
//
//        jMenuItem77.setText("Накладные на отгрузку/Возвраты");
//        jMenuItem77.addActionListener(this::jMenuItem77ActionPerformed);
//        jMenu16.add(jMenuItem77);
//
//        miPreOrderExport.setText("Предварительный заказ");
//        miPreOrderExport.addActionListener(a -> miPreOrderExportActionListener());
//        jMenu16.add(miPreOrderExport);
//        add(jMenu16);
//
//        treeMenu.put("12", jMenu16);
//        treeMenu.put("12-1", jMenuItem19);
//        treeMenu.put("12-2", jMenuItem77);
//        treeMenu.put("12-3", miPreOrderExport);
//    }
//
//    private void createLabelMenu() {
//        jMenu26.setText("Маркировка");
//
//        jMenuItem48.setText("Печать этикеток (ед)");
//        jMenuItem48.addActionListener(this::jMenuItem48ActionPerformed);
//        jMenu26.add(jMenuItem48);
//
//        jMenuItem53.setText("Печать этикеток (ттн)");
//        jMenuItem53.addActionListener(this::jMenuItem53ActionPerformed);
//        jMenu26.add(jMenuItem53);
//
//        jMenuItem50.setText("Перевод изделия");
//        jMenuItem50.addActionListener(this::jMenuItem50ActionPerformed);
//        jMenu26.add(jMenuItem50);
//        add(jMenu26);
//
//        treeMenu.put("16", jMenu26);
//        treeMenu.put("16-1", jMenuItem48);
//        treeMenu.put("16-2", jMenuItem50);
//        treeMenu.put("16-3", jMenuItem53);
//    }
//
//    private void createMarketingMenu() {
//        jMenu17.setText("Маркетинг");
//
//        jMenuItem21.setText("Оформление заявок");
//        jMenuItem21.addActionListener(this::jMenuItem21ActionPerformed);
//        jMenu17.add(jMenuItem21);
//
//        jMenuItem25.setText("Склад");
//        jMenuItem25.addActionListener(this::jMenuItem25ActionPerformed);
//        jMenu17.add(jMenuItem25);
//
//        jMenuItem31.setText("Просмотр заявок");
//        jMenuItem31.addActionListener(this::jMenuItem31ActionPerformed);
//        jMenu17.add(jMenuItem31);
//
//        jMenuItem32.setText("Накладные на отгрузку");
//        jMenuItem32.addActionListener(this::jMenuItem32ActionPerformed);
//        jMenu17.add(jMenuItem32);
//
//        jMenuItem41.setText("Цены");
//        jMenuItem41.addActionListener(this::jMenuItem41ActionPerformed);
//        jMenu17.add(jMenuItem41);
//
//        miRemains.setText("Оперативные остатки продукции");
//        miRemains.setIcon(new ImageIcon(progPath + "/Img/database_refresh_24.png"));
//        miRemains.addActionListener(a -> new WarehouseRemainsMode(controller, RightEnum.WRITE));
//
//        jMenu17.add(miRemains);
//
//        marketingReports();
//        jMenu17.add(jMenu23);
//
//        miMarketingPriceList.setText("Журнал документов уценки");
//        miMarketingPriceList.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/document_24.png"));
//        miMarketingPriceList.addActionListener(a-> new MarketingPriceListJournalMode(controller, RightEnum.WRITE));
//        jMenu17.add(miMarketingPriceList);
//
//        marketingPriceList();
//        jMenu17.add(mAssortment);
//
//        add(jMenu17);
//
//        treeMenu.put("13", jMenu17);
//        treeMenu.put("13-1", jMenuItem21);
//        treeMenu.put("13-2", jMenuItem25);
//        treeMenu.put("13-3", jMenuItem31);
//        treeMenu.put("13-4", jMenuItem32);
//        treeMenu.put("13-5", jMenuItem41);
//        treeMenu.put("13-6", jMenu23);
//        treeMenu.put("13-6-1", jMenuItem37);
//        treeMenu.put("13-6-2", jMenuItem38);
//        treeMenu.put("13-6-3", jMenuItem56);
//        treeMenu.put("13-6-4", jMenuItem58);
//        treeMenu.put("13-7", miMarketingPriceList);
//        treeMenu.put("13-8", mAssortment);
//        treeMenu.put("13-8-1", miProductionCatalog);
//        treeMenu.put("13-8-2", miImageBase);
//        treeMenu.put("13-8-3", miEanJournal);
//    }
//
//    private void marketingReports() {
//        jMenu23.setText("Отчёты");
//
//        jMenuItem37.setText("Анализ отгрузок");
//        jMenuItem37.addActionListener(this::jMenuItem37ActionPerformed);
//        jMenu23.add(jMenuItem37);
//
//        jMenuItem38.setText("Продажи");
//        jMenuItem38.addActionListener(this::jMenuItem38ActionPerformed);
//        jMenu23.add(jMenuItem38);
//
//        jMenuItem56.setText("Анализ регион. рынков ");
//        jMenuItem56.addActionListener(this::jMenuItem56ActionPerformed);
//        jMenu23.add(jMenuItem56);
//
//        jMenuItem58.setText("Анализ объема отгрузок");
//        jMenuItem58.addActionListener(this::jMenuItem58ActionPerformed);
//        jMenu23.add(jMenuItem58);
//    }
//
//    private void marketingPriceList() {
//        mAssortment.setText("Ассортимент");
//
//        miImageBase.setText("База изображений");
//        miImageBase.addActionListener(a -> miImageBaseActionListener());
//        mAssortment.add(miImageBase);
//
//        miProductionCatalog.setText("Каталог продукции");
//        miProductionCatalog.addActionListener(a -> miProductionCatalogActionListener());
//        mAssortment.add(miProductionCatalog);
//
//        miEanJournal.setText("Справочник EAN кодов");
//        miEanJournal.addActionListener(a ->
//                new EanCodeJournalMode(controller));
//        mAssortment.add(miEanJournal);
//    }
//
//    private void createNSIMenu() {
//        jMenu14.setText("НСИ");
//
//        jMenu15.setText("Справочники");
//
//        jMenuItem16.setText("Обновить справочник трудозатрат");
//        jMenuItem16.addActionListener(this::jMenuItem16ActionPerformed);
//        jMenu15.add(jMenuItem16);
//
//        jMenuItem33.setText("Обновить плановую себестоимость");
//        jMenuItem33.addActionListener(this::jMenuItem33ActionPerformed);
//        jMenu15.add(jMenuItem33);
//
//        jMenuItem36.setText("Обновить справочник клиентов (КТ)");
//        jMenuItem36.addActionListener(this::jMenuItem36ActionPerformed);
//        jMenu15.add(jMenuItem36);
//
//        jMenuItem40.setText("Обновить справочник отгрузки");
//        jMenuItem40.addActionListener(evt -> jMenuItem40ActionPerformed());
//        jMenu15.add(jMenuItem40);
//
//        jMenuItem43.setText("Очистка справочника NSI_KLD");
//        jMenuItem43.addActionListener(this::jMenuItem43ActionPerformed);
//        jMenu15.add(jMenuItem43);
//
//        jMenuItem60.setText("Справочник ВЭК");
//        jMenuItem60.addActionListener(this::jMenuItem60ActionPerformed);
//        jMenu15.add(jMenuItem60);
//
//        jMenuItem65.setText("Перемещение без массы");
//        jMenuItem65.addActionListener(this::jMenuItem65ActionPerformed);
//        jMenu15.add(jMenuItem65);
//
//        jMenu14.add(jMenu15);
//
//        jMenuItem64.setText("Проверка ставок НДС");
//        jMenuItem64.addActionListener(this::jMenuItem64ActionPerformed);
//        jMenu14.add(jMenuItem64);
//
//        add(jMenu14);
//
//        treeMenu.put("11", jMenu14);
//        treeMenu.put("11-1", jMenu15);
//        treeMenu.put("11-1-1", jMenuItem16);
//        treeMenu.put("11-1-2", jMenuItem33);
//        treeMenu.put("11-1-3", jMenuItem36);
//        treeMenu.put("11-1-4", jMenuItem40);
//        treeMenu.put("11-1-5", jMenuItem43);
//        treeMenu.put("11-1-6", jMenuItem60);
//        treeMenu.put("11-1-7", jMenuItem65);
//        treeMenu.put("11-2", jMenuItem64);
//    }
//    private void createOTKMenu() {
//        jMenu11.setText("ОТК");
//        jMenuItem14.setText("Упаковка");
//        jMenuItem14.addActionListener(this::jMenuItem14ActionPerformed);
//        jMenu11.add(jMenuItem14);
//        add(jMenu11);
//
//        treeMenu.put("9", jMenu11);
//        treeMenu.put("9-1", jMenuItem14);
//    }
//
//    private void createProductionMenu() {
//        jMenu27.setText("Производство");
//
//        jMenu28.setText("Закройно-швейный цех");
//
//        jMenuItem49.setText("Спецификации моделей");
//        jMenuItem49.addActionListener(this::jMenuItem49ActionPerformed);
//        jMenu28.add(jMenuItem49);
//
//        jMenuItem55.setText("Расчет ЗП рабочим");
//        jMenuItem55.addActionListener(this::jMenuItem55ActionPerformed);
//        jMenu28.add(jMenuItem55);
//
//        jMenuItem85.setText("ЗП переданная в бухгалтерию");
//        jMenuItem85.addActionListener(this::jMenuItem85ActionPerformed);
//        jMenu28.add(jMenuItem85);
//
//        jMenu27.add(jMenu28);
//
//        jMenu7.setText("Упаковка");
//
//        jMenu9.setText("Отчёты");
//
//        jMenuItem8.setText("Сдано на склад");
//        jMenuItem8.addActionListener(this::jMenuItem8ActionPerformed);
//        jMenu9.add(jMenuItem8);
//
//        jMenuItem9.setText("Принято на упаковку");
//        jMenuItem9.addActionListener(this::jMenuItem9ActionPerformed);
//        jMenu9.add(jMenuItem9);
//
//        jMenuItem12.setText("Остатки");
//        jMenuItem12.addActionListener(this::jMenuItem12ActionPerformed);
//        jMenu9.add(jMenuItem12);
//
//        jMenuItem29.setText("потерянные МЛ");
//        jMenuItem29.addActionListener(this::jMenuItem29ActionPerformed);
//        jMenu9.add(jMenuItem29);
//
//        jMenuItem35.setText("Сдача по моделям");
//        jMenuItem35.addActionListener(this::jMenuItem35ActionPerformed);
//        jMenu9.add(jMenuItem35);
//
//        jMenuItem54.setText("Нормы времени н.в.");
//        jMenuItem54.addActionListener(this::jMenuItem54ActionPerformed);
//        jMenu9.add(jMenuItem54);
//
//        jMenuItem62.setText("Внутреннее перемещение");
//        jMenuItem62.addActionListener(this::jMenuItem62ActionPerformed);
//        jMenu9.add(jMenuItem62);
//
//        jMenu7.add(jMenu9);
//
//        jMenu21.setText("Остатки");
//
//        jMenuItem26.setText("Остатки на начало месяца");
//        jMenuItem26.addActionListener(this::jMenuItem26ActionPerformed);
//        jMenu21.add(jMenuItem26);
//
//        jMenuItem27.setText("Остатки на дату");
//        jMenuItem27.addActionListener(this::jMenuItem27ActionPerformed);
//        jMenu21.add(jMenuItem27);
//
//        jMenuItem28.setText("Текущие остатки");
//        jMenuItem28.addActionListener(this::jMenuItem28ActionPerformed);
//        jMenu21.add(jMenuItem28);
//
//        jMenu7.add(jMenu21);
//
//        jMenu10.setText("Программы");
//
//        jMenuItem13.setText("Калькулятор");
//        jMenuItem13.addActionListener(this::jMenuItem13ActionPerformed);
//        jMenu10.add(jMenuItem13);
//
//        jMenu7.add(jMenu10);
//
//        jMenu24.setText("Несортная");
//
//        jMenuItem39.setText("Переучёт ЗШ цеха Н/С продукции");
//        jMenuItem39.addActionListener(this::jMenuItem39ActionPerformed);
//        jMenu24.add(jMenuItem39);
//
//        jMenuItem46.setText("Учёт н/с");
//        jMenuItem46.addActionListener(this::jMenuItem46ActionPerformed);
//        jMenu24.add(jMenuItem46);
//
//        jMenu7.add(jMenu24);
//
//        jMenu27.add(jMenu7);
//
//        jMenu29.setText("Мониторинг");
//
//        jMenuItem57.setText("Производство под заказ");
//        jMenuItem57.addActionListener(this::jMenuItem57ActionPerformed);
//        jMenu29.add(jMenuItem57);
//
//        jMenu27.add(jMenu29);
//
//        jMenu30.setText("Планирование");
//
//        jMenuItem61.setText("План производства");
//        jMenuItem61.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jMenuItem61ActionPerformed(evt);
//            }
//        });
//        jMenu30.add(jMenuItem61);
//
//        jMenuItem69.setText("Проект плана");
//        jMenuItem69.addActionListener(this::jMenuItem69ActionPerformed);
//        jMenu30.add(jMenuItem69);
//
//        jMenuItem81.setText("EAN-коды ");
//        jMenuItem81.addActionListener(this::jMenuItem81ActionPerformed);
//        jMenu30.add(jMenuItem81);
//
//        jMenuItem83.setText("Анализ выполнения");
//        jMenuItem83.addActionListener(this::jMenuItem83ActionPerformed);
//        jMenu30.add(jMenuItem83);
//
//        jMenu27.add(jMenu30);
//
//        jMenuItem70.setText("Текущие остатки кроя");
//        jMenuItem70.addActionListener(this::jMenuItem70ActionPerformed);
//        jMenu27.add(jMenuItem70);
//
//        JMenuItem miReportRouteSheet = new JMenuItem("Маршрутные листы");
//        JMenuItem miReportJournalEanCode = new JMenuItem("Журнал EAN-кодов");
//        JMenuItem miCutCardMode = new JMenuItem("Карты кроя");
//        JMenuItem miSeamstressProductionMap = new JMenuItem("Карта выработки швеи");
//        miReportRouteSheet.addActionListener(e -> new RouteSheetViewMode(controller));
//        miReportJournalEanCode.addActionListener(e -> new JournalEanCodeViewMode(controller));
//        miCutCardMode.addActionListener(e -> new CutCardMode(controller));
//        miSeamstressProductionMap.addActionListener(a -> new SeamstressProductionMode(controller));
//
//        jMenu27.add(miReportRouteSheet);
//        jMenu27.add(miReportJournalEanCode);
//        jMenu27.add(miCutCardMode);
//        jMenu27.add(miSeamstressProductionMap);
//
//        add(jMenu27);
//
//        treeMenu.put("17", jMenu27);
//        treeMenu.put("17-1", jMenu28);
//        treeMenu.put("17-1-1", jMenuItem49);
//        treeMenu.put("17-1-2", jMenuItem55);
//        treeMenu.put("17-1-3", jMenuItem85);
//        treeMenu.put("17-2", jMenu29);
//        treeMenu.put("17-2-1", jMenuItem57);
//        treeMenu.put("17-3", jMenu30);
//        treeMenu.put("17-3-1", jMenuItem61);
//        treeMenu.put("17-3-2", jMenuItem69);
//        treeMenu.put("17-3-3", jMenuItem81);
//        treeMenu.put("17-3-4", jMenuItem83);
//        treeMenu.put("17-4", jMenuItem70);
//        treeMenu.put("17-5", jMenu7);
//        treeMenu.put("17-5-1", jMenu9);
//        treeMenu.put("17-5-1-1", jMenuItem8);
//        treeMenu.put("17-5-1-2", jMenuItem9);
//        treeMenu.put("17-5-1-3", jMenuItem12);
//        treeMenu.put("17-5-1-4", jMenuItem29);
//        treeMenu.put("17-5-1-5", jMenuItem35);
//        treeMenu.put("17-5-1-6", jMenuItem54);
//        treeMenu.put("17-5-1-7", jMenuItem62);
//        treeMenu.put("17-5-2", jMenu21);
//        treeMenu.put("17-5-2-1", jMenuItem26);
//        treeMenu.put("17-5-2-2", jMenuItem27);
//        treeMenu.put("17-5-2-3", jMenuItem28);
//        treeMenu.put("17-5-3", jMenu24);
//        treeMenu.put("17-5-3-1", jMenuItem39);
//        treeMenu.put("17-5-3-2", jMenuItem46);
//        treeMenu.put("17-5-4", jMenu10);
//        treeMenu.put("17-5-4-1", jMenuItem13);
//    }
//
//    private void createSalesMenu() {
//        jMenu6.setText("Сбыт");
//
//        jMenuItem6.setText("Отправка накладных");
//        jMenuItem6.addActionListener(this::jMenuItem6ActionPerformed);
//        jMenu6.add(jMenuItem6);
//
//        jMenuItem63.setText("Изменить ТН");
//        jMenuItem63.addActionListener(this::jMenuItem63ActionPerformed);
//        jMenu6.add(jMenuItem63);
//
//        jMenuItem84.setText("Протокол по заявке ");
//        jMenuItem84.addActionListener(this::jMenuItem84ActionPerformed);
//        jMenu6.add(jMenuItem84);
//
//        miEMailHistory.setText("История отправки документов");
//        miEMailHistory.addActionListener(a -> new EMailHistory(controller));
//        jMenu6.add(miEMailHistory);
//
//        miEurotorg.addActionListener(a -> new EurotorgUnloading(controller));
//        mUnloading.add(miEurotorg);
//        //jMenu6.add(mUnloading);
//
//        miSetClient = new JMenuItem("Управление клиентами");
//        miSetClient.addActionListener(this::jMenuItemClientActionPerformed);
//        jMenu6.add(miSetClient);
//
//        miPreOrderInternal = new JMenuItem("Предварительный заказ");
//        miPreOrderInternal.addActionListener(a -> miPreOrderInternal());
//        jMenu6.add(miPreOrderInternal);
//
//        add(jMenu6);
//
//        treeMenu.put("6", jMenu6);
//        treeMenu.put("6-1", jMenuItem6);
//        treeMenu.put("6-2", jMenuItem84);
//        treeMenu.put("6-3", mUnloading);
//        treeMenu.put("6-3-1", miEurotorg);
//        treeMenu.put("6-4", miEMailHistory);
//        treeMenu.put("6-5", miPreOrderInternal);
//        treeMenu.put("6-6", miSetClient);
//    }
//
//    private void createSewingMenu() {
//        jMenu12.setText("Пошив");
//
//        jMenu13.setText("Отчёты");
//
//        jMenuItem15.setText("Трудозатраты");
//        jMenuItem15.addActionListener(this::jMenuItem15ActionPerformed);
//        jMenu13.add(jMenuItem15);
//
//        jMenuItem17.setText("Выполнение плана в н/ч");
//        jMenuItem17.addActionListener(this::jMenuItem17ActionPerformed);
//        jMenu13.add(jMenuItem17);
//
//        jMenuItem18.setText("Выполнение плана в млн. руб.");
//        jMenuItem18.addActionListener(this::jMenuItem18ActionPerformed);
//        jMenu13.add(jMenuItem18);
//
//        jMenuItem20.setText("Накопительная ведомость");
//        jMenuItem20.addActionListener(this::jMenuItem20ActionPerformed);
//        jMenu13.add(jMenuItem20);
//
//        jMenu12.add(jMenu13);
//
//        add(jMenu12);
//
//        treeMenu.put("10", jMenu12);            //Пошив
//        treeMenu.put("10-1", jMenu13);              //Отчёты
//        treeMenu.put("10-1-1", jMenuItem15);            //Трудозатраты
//        treeMenu.put("10-1-2", jMenuItem17);            //Выполнение плана в нормочасах
//        treeMenu.put("10-1-3", jMenuItem18);            //Выполнение плана в млн. руб.
//        treeMenu.put("10-1-4", jMenuItem20);            //Накопительная ведомость
//    }
//    private void createWarehouseMenu() {
//        jMenu19.setText("Склад");
//
//        jMenuItem34.setText("Накладные на отгрузку");
//        jMenuItem34.addActionListener(this::jMenuItem34ActionPerformed);
//        jMenu19.add(jMenuItem34);
//
//        jMenuItem42.setText("Возвраты ");
//        jMenuItem42.addActionListener(this::jMenuItem42ActionPerformed);
//        jMenu19.add(jMenuItem42);
//
//        jMenuItem59.setText("Спец. цены");
//        jMenuItem59.addActionListener(this::jMenuItem59ActionPerformed);
//        jMenu19.add(jMenuItem59);
//
//        jMenu31.setText("ХЭО");
//
//        jMenuItem71.setText("Учет ТМЦ");
//        jMenuItem71.addActionListener(this::jMenuItem71ActionPerformed);
//        jMenu31.add(jMenuItem71);
//
//        jMenuItem72.setText("Справочник ТМЦ");
//        jMenuItem72.addActionListener(this::jMenuItem72ActionPerformed);
//        jMenu31.add(jMenuItem72);
//
//        jMenuItem73.setText("Цены ТМЦ");
//        jMenuItem73.addActionListener(this::jMenuItem73ActionPerformed);
//        jMenu31.add(jMenuItem73);
//
//        jMenu19.add(jMenu31);
//
//        jMenuItem76.setText("Разделение накладных");
//        jMenuItem76.addActionListener(this::jMenuItem76ActionPerformed);
//        jMenu19.add(jMenuItem76);
//
//        jMenuItem67.setText("Перенос спецификации");
//        jMenuItem67.addActionListener(this::jMenuItem67ActionPerformed);
//        jMenu19.add(jMenuItem67);
//
//        jMenuItem66.setText("Информация по накладной");
//        jMenuItem66.addActionListener(this::jMenuItem66ActionPerformed);
//        jMenu19.add(jMenuItem66);
//
//        jMenuItem79.setText("Отчет по отгрузке");
//        jMenuItem79.addActionListener(this::jMenuItem79ActionPerformed);
//        jMenu19.add(jMenuItem79);
//
//        jMenuItem78.setText("Сводная ведомость(7-я сводка)");
//        jMenuItem78.addActionListener(this::jMenuItem78ActionPerformed);
//        jMenu19.add(jMenuItem78);
//
//        jMenuItem68.setText("Экспорт отгрузки в DBF");
//        jMenuItem68.addActionListener(this::jMenuItem68ActionPerformed);
//        jMenu19.add(jMenuItem68);
//
//        jMenu32.setText("Остатки");
//        jMenu32.addActionListener(this::jMenu32ActionPerformed);
//
//        jMenuItem74.setText("Текущие остатки");
//        jMenuItem74.addActionListener(this::jMenuItem74ActionPerformed);
//        jMenu32.add(jMenuItem74);
//
//        jMenuItem75.setText("Оборотка");
//        jMenuItem75.addActionListener(this::jMenuItem75ActionPerformed);
//        jMenu32.add(jMenuItem75);
//
//        jMenu19.add(jMenu32);
//
//        jMenuItem80.setText("Отчет по инвентаризации (для сверки)");
//        jMenuItem80.addActionListener(this::jMenuItem80ActionPerformed);
//        jMenu19.add(jMenuItem80);
//
//        add(jMenu19);
//
//        treeMenu.put("15", jMenu19);
//        treeMenu.put("15-1", jMenu20);
//        treeMenu.put("15-1-1", jMenuItem23);
//        treeMenu.put("15-1-2", jMenuItem24);
//        treeMenu.put("15-1-3", miSkladTekOst);
//        treeMenu.put("15-2", jMenuItem34);
//        treeMenu.put("15-3", jMenuItem42);
//        treeMenu.put("15-4", jMenuItem59);
//        treeMenu.put("15-5", jMenuItem63);
//        treeMenu.put("15-6", jMenuItem66);
//        treeMenu.put("15-7", jMenuItem67);
//        treeMenu.put("15-8", jMenuItem68);
//        treeMenu.put("15-9", jMenu31);
//        treeMenu.put("15-9-1", jMenuItem71);
//        treeMenu.put("15-9-2", jMenuItem72);
//        treeMenu.put("15-9-3", jMenuItem73);
//        treeMenu.put("15-10", jMenu32);
//        treeMenu.put("15-10-1", jMenuItem74);
//        treeMenu.put("15-10-2", jMenuItem75);
//        treeMenu.put("15-11", jMenuItem76);
//        treeMenu.put("15-12", jMenuItem78);
//        treeMenu.put("15-13", jMenuItem79);
//        treeMenu.put("15-14", jMenuItem80);
//    }
//
//    private void createTechDepartmentMenu() {
//        jMenu8.setText("Тех отдел");
//
//        jMenuItem10.setText("Новые изделия в плане");
//        jMenuItem10.addActionListener(this::jMenuItem10ActionPerformed);
//        jMenu8.add(jMenuItem10);
//
//        jMenuItem47.setText("Продукция");
//        jMenuItem47.addActionListener(this::jMenuItem47ActionPerformed);
//        jMenu8.add(jMenuItem47);
//
//        add(jMenu8);
//
//        treeMenu.put("8", jMenu8);
//        treeMenu.put("8-1", jMenuItem10);
//        treeMenu.put("8-2", jMenuItem47);
//    }
//
//    private void createEconomistsMenu() {
//        jMenu4.setText("Экономисты");
//
//        jMenu2.setText("Отчёты");
//
//        jMenuItem3.setText("Выпущенная продукция по ЗШ цеху");
//        jMenuItem3.addActionListener(this::jMenuItem3ActionPerformed);
//        jMenu2.add(jMenuItem3);
//
//        jMenuItem4.setText("Пошив запуск");
//        jMenuItem4.addActionListener(this::jMenuItem4ActionPerformed);
//        jMenu2.add(jMenuItem4);
//
//        jMenuItem7.setText("Пошив выпуск");
//        jMenuItem7.addActionListener(this::jMenuItem7ActionPerformed);
//        jMenu2.add(jMenuItem7);
//
//        jMenu4.add(jMenu2);
//
//        jMenuCalculationPrice.setText("Калькуляция");
//
//        jMenuItemCalculationPrice.setText("Расчет цен");
//        jMenuItemCalculationPrice.addActionListener(this::jMenuItemCalculationPriceActionPerformed);
//        jMenuCalculationPrice.add(jMenuItemCalculationPrice);
//        jMenuCalculationPrice.getAccessibleContext().setAccessibleName("Расчет цен1");
//        jMenu4.add(jMenuCalculationPrice);
//
//        miRemainsPriceList.setText("Прейскурант остатков");
//
//        miRemainsPriceListJournal.setText("Журнал прейскурантов");
//        miRemainsPriceListJournal.addActionListener(a -> new WarehousePriceListJournalMode(controller, RightEnum.WRITE));
//        miRemainsPriceList.add(miRemainsPriceListJournal);
//
//        miRemainsPriceListView.setText("Просмотр прейскурантов");
//        miRemainsPriceListView.addActionListener(a -> new WarehousePriceListJournalMode(controller, RightEnum.READ));
//        miRemainsPriceList.add(miRemainsPriceListView);
//
//        jMenu4.add(miRemainsPriceList);
//
//        add(jMenu4);
//
//        treeMenu.put("4", jMenu4);
//        treeMenu.put("4-1", jMenu2);
//        treeMenu.put("4-1-1", jMenuItem3);
//        treeMenu.put("4-1-2", jMenuItem4);
//        treeMenu.put("4-1-3", jMenuItem7);
//        treeMenu.put("4-2", jMenuCalculationPrice);
//        treeMenu.put("4-2-1", jMenuItemCalculationPrice);
//        treeMenu.put("4-3", miRemainsPriceList);
//        treeMenu.put("4-3-1", miRemainsPriceListJournal);
//        treeMenu.put("4-3-2", miRemainsPriceListView);
//
//    }
//
//    private void createSettingsMenu() {
//        MENU_SETTINGS.setText("Настройка");
//
//        jMenuItem5.setText("Конфигурация");
//        jMenuItem5.addActionListener(this::jMenuItem5ActionPerformed);
//        MENU_SETTINGS.add(jMenuItem5);
//
//        jMenuItem90.setText("Управление фотографиями");
//        jMenuItem90.addActionListener(this::jMenuItem90ActionPerformed);
//        MENU_SETTINGS.add(jMenuItem90);
//
//        add(MENU_SETTINGS);
//    }
//
    private void createHelpMenu() {
        jMenu3.setText("Помощь");

        jMenuItem2.setText("О программе");
        jMenuItem2.addActionListener(this::jMenuItem2ActionPerformed);
        jMenu3.add(jMenuItem2);

        miERPRemains.setText("ERPRemains");
        miERPRemains.addActionListener(a -> new ERPRemainsMode(controller));
        jMenu3.add(miERPRemains);

        miServices.setText("ServiceMonitor");
        miServices.addActionListener(a -> new ServiceMonitorMode(controller));
        jMenu3.add(miServices);

        miConsole.setText("Консоль");
        miConsole.addActionListener(a -> new ConsoleMode(controller));
        jMenu3.add(miConsole);

        jMenuItem22.setText("Куда звонить?");
        jMenuItem22.addActionListener(this::jMenuItem22ActionPerformed);

        jMenu3.add(jMenuItem22);

        miSaleDept.setText("Журнал заявок");
        miSaleDept.addActionListener(a->{
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }
                @Override
                protected Boolean doInBackground() throws Exception {
                    new SalesMonitorMode(controller);
                    return true;
                }
            }
            Task task = new Task("Запуск журнала заявок...");
            task.executeTask();

        });
        jMenu3.add(miSaleDept);

        add(jMenu3);

        treeMenu.put("3", jMenu3);
        treeMenu.put("3-1", jMenuItem2);
        treeMenu.put("3-2", miERPRemains);
        treeMenu.put("3-3", miServices);
        treeMenu.put("3-4", miConsole);
        treeMenu.put("3-5", jMenu18);
        treeMenu.put("3-5-1", jMenuItem22);
        treeMenu.put("3-6", miSaleDept);
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new AboutForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new ToolsForm(controller, true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        new SendMailForm(controller, ownerFrame, true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        new PoshivDateForm(ownerFrame, true, "Запуск");
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        new PoshivDateForm(ownerFrame, true, "Выпуск");
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        EanList el;
        JFileChooser fc = new JFileChooser(MyReportsModule.dbfPlanPath);
        fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null) {
                    return f.isDirectory()
                            || f.getName().endsWith(".DBF") && f.getName().startsWith("PLAN")
                            || f.getName().endsWith(".dbf") && f.getName().startsWith("plan");
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "PLAN";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = new String(fc.getSelectedFile().getPath());
            el = new EanList();
            el.setPath(path);
            //el.createList(path);
            OpenOffice oo = new OpenOffice();
            oo.createReport("EanPlan.ots");
        }

    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        new dept.upack.DataForm2(ownerFrame, true, 1);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        new dept.upack.DataForm(ownerFrame, true, 2);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        new dept.upack.DataForm(ownerFrame, true, 3);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        Runtime r = Runtime.getRuntime();
        try {
            r.exec("calc");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ошибка при запуске калькулятора " + e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new InDateForm(ownerFrame, true, "InfProdZakrSh.ots", jMenuItem3.getText());
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        new dept.otk.DataForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        new dept.poshiv.DataForm(ownerFrame, true, "ТрудозатПошив.ots");
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        new TrudoZat().update();
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        new dept.poshiv.DataForm(ownerFrame, true, "планПошивНЧ.ots");
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        new dept.poshiv.DataForm(ownerFrame, true, "планПошивМР.ots");
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        new SendMailForm(controller, ownerFrame, true, false);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        new dept.poshiv.DataForm(ownerFrame, true, "пошивПодекадныйВыпуск.ots");
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        new MarketingForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        new LoginDataForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        new dept.sklad.ostatki.OstDataForm(ownerFrame, true, 1);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        new dept.sklad.ostatki.OstDataForm(ownerFrame, true, 2);
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void miSkladTekOstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSkladTekOstActionPerformed
        new dept.sklad.ostatki.OstDataForm(ownerFrame, true, 0);
    }//GEN-LAST:event_miSkladTekOstActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        new dept.marketing.SkladForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        new dept.upack.ostatki.OstDataForm(ownerFrame, true, 1);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        new dept.upack.ostatki.OstDataForm(ownerFrame, true, 2);
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        new dept.upack.ostatki.OstDataForm(ownerFrame, true, 0);
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        new dept.upack.MarshListFrom(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        new EmployeForm(controller, true);
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        new dept.marketing.OrdersForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        new dept.marketing.Nakladnie(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed

        JFileChooser fc = new JFileChooser("/nfs/ksl01_D/VED_S16/_DBF/_KR_PLAN/");
        fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null) {
                    if (f.isDirectory()) {
                        return true;
                    }
                }
                return f.getName().endsWith(".DBF") && f.getName().startsWith("KR")
                        || f.getName().endsWith(".dbf") && f.getName().startsWith("kr");
            }

            @Override
            public String getDescription() {
                return "PLAN";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            final String path = new String(fc.getSelectedFile().getPath());
            final ProgressBar pb = new ProgressBar(ownerFrame, false, "Обновление справочника...");
            SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    PlanSstoimost pss = new PlanSstoimost(path);
                    pss.update();
                    return 0;
                }

                @Override
                protected void done() {
                    pb.dispose();
                }
            };

            sw.execute();
            pb.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        controller.openInternalFrame(new dept.sklad.Nakladnie(controller, true));
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        new dept.upack.DataForm(ownerFrame, true, 4);
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        JFileChooser fc = new JFileChooser("/nfs/ser01_D/BUD/SV/nsi_pd.dbf");
        fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null && f.isDirectory()) return true;

                return f.getName().endsWith(".dbf") && f.getName().startsWith("nsi")
                        || f.getName().endsWith(".DBF") && f.getName().startsWith("NSI");
            }

            @Override
            public String getDescription() {
                return "PLAN";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            final String path = new String(fc.getSelectedFile().getPath());
            final ProgressBar pb = new ProgressBar(ownerFrame, false, "Обновление справочника...");
            SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    ClientKT c = new ClientKT(path);
                    c.update();
                    return 0;
                }

                @Override
                protected void done() {
                    pb.dispose();
                }
            };

            sw.execute();
            pb.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        new dept.marketing.report.OtgruzForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
        new dept.marketing.report.SaleForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
        new dept.upack.ns.PereuchZSH(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jMenuItem41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem41ActionPerformed
        new CenaForm(controller, true);
    }//GEN-LAST:event_jMenuItem41ActionPerformed

    private void jMenuItem40ActionPerformed() {//GEN-FIRST:event_jMenuItem40ActionPerformed
        final JFileChooser fc = new JFileChooser("/nfs/ser01/bud/");
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null) {
                    if (f.isDirectory()) {
                        return true;
                    }
                }

                if (f.getName().toLowerCase().endsWith(".dbf")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "*.dbf";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showDialog(ownerFrame, null) == JFileChooser.APPROVE_OPTION) {
            final ProgressBar pb = new ProgressBar(ownerFrame, false, "Обновление справочника...");
            SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    Otgruz c = new Otgruz(fc.getSelectedFile().getPath());
                    c.update();
                    return null;
                }

                @Override
                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setVisible(true);
        }
    }

    private void jMenuItem42ActionPerformed(java.awt.event.ActionEvent evt) {
        new dept.sklad.Import.ImportNakladnie(controller, ownerFrame, true);
    }

    private void jMenuItem43ActionPerformed(java.awt.event.ActionEvent evt) {
        new dept.nsi.SpravIzdelie(ownerFrame, true);
    }

    private void jMenuItem45ActionPerformed(java.awt.event.ActionEvent evt) {
        new ValutaForm(ownerFrame, true);
    }

    private void jMenuItem44ActionPerformed(java.awt.event.ActionEvent evt) {
        new ValutaKursForm(ownerFrame, true);
    }

    private void jMenuItem46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem46ActionPerformed
        new dept.upack.ns.DataForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem46ActionPerformed

    private void jMenuItem47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem47ActionPerformed
        new Production(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem47ActionPerformed

    private void jMenuItem48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem48ActionPerformed
        new PrintLabelForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem48ActionPerformed

    private void jMenuItem50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem50ActionPerformed
        new TransItemForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem50ActionPerformed

    private void jMenuItem49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem49ActionPerformed
        new SpecForm(controller, true);
    }//GEN-LAST:event_jMenuItem49ActionPerformed

    private void jMenuItem51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem51ActionPerformed
        new ModelForm(controller, true, false);
    }//GEN-LAST:event_jMenuItem51ActionPerformed

    private void jMenuItem52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem52ActionPerformed
        new TechForm(controller, false);
    }//GEN-LAST:event_jMenuItem52ActionPerformed

    private void jMenuItem53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem53ActionPerformed
        new dept.markerovka.NakladnieForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem53ActionPerformed

    private void jMenuItem54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem54ActionPerformed
        new dept.upack.norm.DateForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem54ActionPerformed

    private void jMenuItem55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem55ActionPerformed
        new ZPlataForm(controller, true);
    }//GEN-LAST:event_jMenuItem55ActionPerformed

    private void jMenuItem56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem56ActionPerformed
        new dept.marketing.report.AnalizRR(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem56ActionPerformed

    private void jMenuItem57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem57ActionPerformed
        new dept.production.monitoring.DataForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem57ActionPerformed

    private void jMenuItem58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem58ActionPerformed
        new dept.marketing.report.AnalizVolumeOtgruz(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem58ActionPerformed

    private void jMenuItem59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem59ActionPerformed
        HashMap hm = new HashMap();
        JDialog jd = new JDialog();
        jd.setTitle("mainForm");
        new dept.sklad.specCena.SpecCena(ownerFrame, hm);
    }//GEN-LAST:event_jMenuItem59ActionPerformed

    private void jMenuItem60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem60ActionPerformed
        new dept.nsi.DirectoryCodeForeignEconomicRelations(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem60ActionPerformed

    private void jMenuItem61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem61ActionPerformed
        new PlanProductioForm(controller, true, false);
    }//GEN-LAST:event_jMenuItem61ActionPerformed

    private void jMenuItem62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem62ActionPerformed
        new dept.upack.DateFormMove(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem62ActionPerformed

    private void jMenuItem63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem63ActionPerformed
        new dept.sbit.EditTradeAllowance(ownerFrame);
    }//GEN-LAST:event_jMenuItem63ActionPerformed

    private void jMenuItem64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem64ActionPerformed
        new ChekcNDS(ownerFrame);
    }//GEN-LAST:event_jMenuItem64ActionPerformed

    private void jMenuItem65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem65ActionPerformed
        new dept.nsi.DateMoveNoMassa(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem65ActionPerformed

    private void jMenuItem66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem66ActionPerformed
        new dept.sklad.NaklInfo(ownerFrame);
    }//GEN-LAST:event_jMenuItem66ActionPerformed

    private void jMenuItem67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem67ActionPerformed
        new dept.sklad.TransferNakl(ownerFrame);
    }//GEN-LAST:event_jMenuItem67ActionPerformed

    private void jMenuItem68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem68ActionPerformed
        new dept.sklad.EksportOtgruzDBF(ownerFrame);
    }//GEN-LAST:event_jMenuItem68ActionPerformed

    private void jMenuItem69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem69ActionPerformed
        new ProjectPlanForm(controller, true, false);
    }//GEN-LAST:event_jMenuItem69ActionPerformed

    private void jMenuItem70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem70ActionPerformed
        new dept.production.remnantsofcut.RemnantsOfCutMain(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem70ActionPerformed

    private void jMenuItem71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem71ActionPerformed
        new SkladHOForm(controller, true);
    }//GEN-LAST:event_jMenuItem71ActionPerformed

    private void jMenuItem72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem72ActionPerformed
        new SpravTMCSkHOForm(controller, true);
    }//GEN-LAST:event_jMenuItem72ActionPerformed

    private void jMenuItem73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem73ActionPerformed
        new CenaTMCSkHOForm(controller, true);
    }//GEN-LAST:event_jMenuItem73ActionPerformed

    private void jMenuItem74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem74ActionPerformed
        new dept.sklad.ostatki.NowRemains(ownerFrame);
    }//GEN-LAST:event_jMenuItem74ActionPerformed

    private void jMenu32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu32ActionPerformed

    }//GEN-LAST:event_jMenu32ActionPerformed

    private void jMenuItem75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem75ActionPerformed
        new dept.sklad.ostatki.RevOfCompany(ownerFrame);
    }//GEN-LAST:event_jMenuItem75ActionPerformed

    private void jMenuItem76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem76ActionPerformed
        new dept.sklad.ShareNakl(ownerFrame);
    }//GEN-LAST:event_jMenuItem76ActionPerformed

    private void jMenuItem77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem77ActionPerformed
        new dept.ves.Nakladnie(controller, true);
    }//GEN-LAST:event_jMenuItem77ActionPerformed

    private void jMenuItem78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem78ActionPerformed
        new SvodnayaVedomostPoFirmMag(ownerFrame);
    }//GEN-LAST:event_jMenuItem78ActionPerformed

    private void jMenuItem79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem79ActionPerformed
        new dept.sklad.ReportsOtgruz(ownerFrame);
    }//GEN-LAST:event_jMenuItem79ActionPerformed

    private void jMenuItem80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem80ActionPerformed
        new dept.sklad.ForRevise(ownerFrame);
    }//GEN-LAST:event_jMenuItem80ActionPerformed

    private void jMenuItemCalculationPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCalculationPriceActionPerformed
        new dept.calculationprice.CalculationPriceForm(controller, false);
    }//GEN-LAST:event_jMenuItemCalculationPriceActionPerformed

    private void jMenuItem81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem81ActionPerformed
        new dept.production.planning.ean.EanForm(controller, true);
    }//GEN-LAST:event_jMenuItem81ActionPerformed

    private void jMenuItem82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem82ActionPerformed
        new dept.sprav.product.ProductForm(ownerFrame, true);
    }//GEN-LAST:event_jMenuItem82ActionPerformed

    private void jMenuItem90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem90ActionPerformed
        controller.openInternalFrame(new ImageManagerForm(controller));
    }//GEN-LAST:event_jMenuItem90ActionPerformed

    private void jMenuItem83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem83ActionPerformed
        new ProjectPlanForm(controller, true, "");
    }//GEN-LAST:event_jMenuItem83ActionPerformed

    private void jMenuItem84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem84ActionPerformed
        new dept.sbit.protocol.ProtocolForm(controller, true);
    }//GEN-LAST:event_jMenuItem84ActionPerformed

    private void jMenuItemClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem84ActionPerformed
        new ClientForm(ownerFrame, true);
    }

    private void jMenuItem85ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem85ActionPerformed
        new BuhVedomostForm(ownerFrame, true, UtilZPlata.OPEN);
    }//GEN-LAST:event_jMenuItem85ActionPerformed

    private void miPreOrderExportActionListener() {
        BackgroundTask<String> task = new BackgroundTask("Запуск журнала заказов...") {
            @Override
            protected Boolean doInBackground() throws Exception {
                new PreOrderSaleDocumentMode(controller, PreOrderControlType.EXPORT);
                return true;
            }
        };
        task.executeTask();
    }

    private void miProductionCatalogActionListener() {
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
    }

    private void miImageBaseActionListener() {
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
    }

    private void miPreOrderInternal() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }
            @Override
            protected Boolean doInBackground() throws Exception {
                new PreOrderSaleDocumentMode(controller, PreOrderControlType.INTERNAL );
                return true;
            }
        }

        Task task = new Task("Запуск журнала заказов...");
        task.executeTask();
    }

    public void hideMenu() {
        //jMenu1.setVisible(false);
        jMenu2.setVisible(false);
        //jMenu3.setVisible(false);
        jMenu4.setVisible(false);
        MENU_SETTINGS.setVisible(false);
        jMenu6.setVisible(false);
        jMenu7.setVisible(false);
        jMenu8.setVisible(false);
        jMenu9.setVisible(false);
        jMenu10.setVisible(false);
        jMenu11.setVisible(false);
        jMenu12.setVisible(false);
        jMenu13.setVisible(false);
        jMenu14.setVisible(false);
        jMenu15.setVisible(false);
        jMenu16.setVisible(false);
        jMenu17.setVisible(false);
        jMenu18.setVisible(false);
        jMenu19.setVisible(false);
        jMenu20.setVisible(false);
        jMenu21.setVisible(false);
        jMenu22.setVisible(false);
        jMenu23.setVisible(false);
        jMenu24.setVisible(false);
        jMenu25.setVisible(false);
        jMenu26.setVisible(false);
        jMenu27.setVisible(false);
        jMenu28.setVisible(false);
        jMenu29.setVisible(false);
        jMenu30.setVisible(false);
        jMenu31.setVisible(false);
        jMenu32.setVisible(false);

        jMenuItem1.setVisible(false);
        //jMenuItem2.setVisible(false);
        jMenuItem3.setVisible(false);
        jMenuItem4.setVisible(false);
        jMenuItem5.setVisible(false);
        jMenuItem6.setVisible(false);
        jMenuItem7.setVisible(false);
        jMenuItem8.setVisible(false);
        jMenuItem9.setVisible(false);
        jMenuItem10.setVisible(false);
        //jMenuItem11.setVisible(false);
        jMenuItem12.setVisible(false);
        jMenuItem13.setVisible(false);
        jMenuItem14.setVisible(false);
        jMenuItem15.setVisible(false);
        jMenuItem16.setVisible(false);
        jMenuItem17.setVisible(false);
        jMenuItem18.setVisible(false);
        jMenuItem19.setVisible(false);
        jMenuItem20.setVisible(false);
        jMenuItem21.setVisible(false);
        jMenuItem22.setVisible(false);
        jMenuItem23.setVisible(false);
        jMenuItem24.setVisible(false);
        miSkladTekOst.setVisible(false);
        jMenuItem25.setVisible(false);
        jMenuItem26.setVisible(false);
        jMenuItem27.setVisible(false);
        jMenuItem28.setVisible(false);
        jMenuItem29.setVisible(false);
        jMenuItem30.setVisible(false);
        jMenuItem31.setVisible(false);
        jMenuItem32.setVisible(false);
        jMenuItem33.setVisible(false);
        jMenuItem34.setVisible(false);
        jMenuItem35.setVisible(false);
        jMenuItem36.setVisible(false);
        jMenuItem37.setVisible(false);
        jMenuItem38.setVisible(false);
        jMenuItem39.setVisible(false);
        jMenuItem40.setVisible(false);
        jMenuItem41.setVisible(false);
        jMenuItem42.setVisible(false);
        jMenuItem43.setVisible(false);
        jMenuItem44.setVisible(false);
        miCurrencyRateUpdater.setVisible(false);
        jMenuItem45.setVisible(false);
        jMenuItem46.setVisible(false);
        jMenuItem47.setVisible(false);
        jMenuItem48.setVisible(false);
        jMenuItem49.setVisible(false);
        jMenuItem50.setVisible(false);
        jMenuItem51.setVisible(false);
        jMenuItem52.setVisible(false);
        jMenuItem53.setVisible(false);
        jMenuItem54.setVisible(false);
        jMenuItem55.setVisible(false);
        jMenuItem56.setVisible(false);
        jMenuItem57.setVisible(false);
        jMenuItem58.setVisible(false);
        jMenuItem59.setVisible(false);
        jMenuItem60.setVisible(false);
        jMenuItem61.setVisible(false);
        jMenuItem62.setVisible(false);
        jMenuItem63.setVisible(false);
        jMenuItem64.setVisible(false);
        jMenuItem65.setVisible(false);
        jMenuItem66.setVisible(false);
        jMenuItem67.setVisible(false);
        jMenuItem68.setVisible(false);
        jMenuItem69.setVisible(false);
        jMenuItem70.setVisible(false);
        jMenuItem71.setVisible(false);
        jMenuItem72.setVisible(false);
        jMenuItem73.setVisible(false);
        jMenuItem74.setVisible(false);
        jMenuItem75.setVisible(false);
        jMenuItem76.setVisible(false);
        jMenuItem77.setVisible(false);
        jMenuItem78.setVisible(false);
        jMenuItem79.setVisible(false);
        jMenuItem80.setVisible(false);
        jMenuCalculationPrice.setVisible(false);
        jMenuItem81.setVisible(false);
        jMenuItem82.setVisible(false);
        jMenuItem83.setVisible(false);
        jMenuItem84.setVisible(false);
        jMenuItem85.setVisible(false);
        jMenuItem90.setVisible(false);

        miRemainsPriceList.setVisible(false);
        miRemainsPriceListJournal.setVisible(false);
        miRemainsPriceListView.setVisible(false);

        miMarketingPriceList.setVisible(false);
        miEMailHistory.setVisible(false);

        mAssortment.setVisible(false);
        miProductionCatalog.setVisible(false);
        miImageBase.setVisible(false);
        miEanJournal.setVisible(false);

        miPreOrderExport.setVisible(false);
//        miPreOrderInternal.setVisible(false);
    }

    private void initEvents() {
        miExit.addActionListener(e -> {
            if (!SystemUtils.applicationIsOutdated()) {
                controller.applicationTerminate();
            } else {
                System.exit(0);
            }
        });
        miRelogin.addActionListener(e -> controller.changeUser());
    }

}
