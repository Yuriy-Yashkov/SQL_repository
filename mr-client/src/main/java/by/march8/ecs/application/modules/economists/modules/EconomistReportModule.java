package by.march8.ecs.application.modules.economists.modules;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.econom.InDateForm;
import dept.econom.PoshivDateForm;

import javax.swing.*;

public class EconomistReportModule implements Module {

    private final JMenu reportsMenu = new JMenu("Отчеты");
    private final JMenuItem miDeliveredWarehouse = new JMenuItem("Выпущенная продукция по ЗШ цеху");
    private final JMenuItem miAcceptedForPackaging = new JMenuItem("Пошив запуск");
    private final JMenuItem miRemains = new JMenuItem("Пошив выпуск");
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
        reportsMenu.add(miDeliveredWarehouse);
        reportsMenu.add(miAcceptedForPackaging);
        reportsMenu.add(miRemains);

        miDeliveredWarehouse.setIcon(new ImageIcon(progPath + "/Img/doc_excel_table.png"));
        miAcceptedForPackaging.setIcon(new ImageIcon(progPath + "/Img/doc_page.png"));
        miRemains.setIcon(new ImageIcon(progPath + "/Img/doc_page_previous.png"));

        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST_REPORT, reportsMenu));
        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST_REPORT_PRODUCTION, miDeliveredWarehouse));
        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST_REPORT_SEWINGINPUT, miAcceptedForPackaging));
        controller.addModuleMenu(new SectionMenu(MarchSection.ECONOMIST_REPORT_SEWINGOUTPUT, miRemains));
    }

    @Override
    public void registerMenuEvents() {
        miDeliveredWarehouse.addActionListener(e -> new InDateForm(ownerFrame, true,
            "InfProdZakrSh.ots", miDeliveredWarehouse.getText()));

        miAcceptedForPackaging.addActionListener(e -> new PoshivDateForm(ownerFrame, true, "Запуск"));

        miRemains.addActionListener(e -> new PoshivDateForm(ownerFrame, true, "Выпуск"));
    }
}
