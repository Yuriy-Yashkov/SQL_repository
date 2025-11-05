package by.march8.ecs.application.modules.production.submodules.packing.reports;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

public class ReportsModule implements Module {

    private final JMenu reportsMenu = new JMenu("Отчёты");
    private final JMenuItem miDeliveredWarehouse = new JMenuItem("Сдано на склад");
    private final JMenuItem miAcceptedForPackaging = new JMenuItem("Принято на упаковку");
    private final JMenuItem miRemains = new JMenuItem("Остатки");
    private final JMenuItem miLostML = new JMenuItem("потерянные МЛ");
    private final JMenuItem miDeliveryByModels = new JMenuItem("Сдача по моделям");
    private final JMenuItem miStandardsOfTime = new JMenuItem("Нормы времени н.в.");
    private final JMenuItem miInternalDisplacement = new JMenuItem("Внутреннее перемещение");
    private String progPath;

    private MainController controller;
    private JFrame ownerFrame;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = controller.getMainForm();
        progPath = controller.getBootstrap().getRunPath();
        registerMenu();
    }

    @Override
    public void registerMenu() {
        reportsMenu.setIcon(new ImageIcon(progPath + "/Img/books.png"));
        miDeliveredWarehouse.setIcon(new ImageIcon(progPath + "/Img/doc_page_previous.png"));
        miAcceptedForPackaging.setIcon(new ImageIcon(progPath + "/Img/doc_page.png"));
        miRemains.setIcon(new ImageIcon(progPath + "/Img/report.png"));

        reportsMenu.add(miDeliveredWarehouse);
        reportsMenu.add(miAcceptedForPackaging);
        reportsMenu.add(miRemains);
        reportsMenu.add(miLostML);
        reportsMenu.add(miDeliveryByModels);
        reportsMenu.add(miStandardsOfTime);
        reportsMenu.add(miInternalDisplacement);
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_REPORT, reportsMenu));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_REPORT_DELIVERED_WAREHOUSE, miDeliveredWarehouse));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_REPORT_ACCEPTED_FOR_PACKING, miAcceptedForPackaging));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_REPORT_REMAINS, miRemains));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_REPORT_LOST_ML, miLostML));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_REPORT_DELIVERY_BY_MODELS, miDeliveryByModels));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_REPORT_STANDARS_OF_TIME, miStandardsOfTime));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_REPORT_INTERNAL_DISPLACEMENT, miInternalDisplacement));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miDeliveredWarehouse.addActionListener( a -> new dept.upack.DataForm2(ownerFrame, true, 1));
        miAcceptedForPackaging.addActionListener( a -> new dept.upack.DataForm(ownerFrame, true, 2));
        miRemains.addActionListener( a -> new dept.upack.DataForm(ownerFrame, true, 3));
        miLostML.addActionListener( a -> new dept.upack.MarshListFrom(ownerFrame, true));
        miDeliveryByModels.addActionListener( a -> new dept.upack.DataForm(ownerFrame, true, 4));
        miStandardsOfTime.addActionListener( a -> new dept.upack.norm.DateForm(ownerFrame, true));
        miInternalDisplacement.addActionListener( a -> new dept.upack.DateFormMove(ownerFrame, true));
    }
}
