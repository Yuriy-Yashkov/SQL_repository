package by.march8.ecs.application.modules.marketing;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

public class ReportModule implements Module {

    private final JMenu jMenu23 = new JMenu("Отчёты");

    private final JMenuItem jMenuItem37 = new JMenuItem("Анализ отгрузок");
    private final JMenuItem jMenuItem38 = new JMenuItem("Продажи");
    private final JMenuItem jMenuItem56 = new JMenuItem("Анализ регион. рынков");
    private final JMenuItem jMenuItem58 = new JMenuItem("Анализ объема отгрузок");

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
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_REPORTS, jMenu23));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_REPORTS_SHIPMENTANALYSIS, jMenuItem37));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_REPORTS_SALES, jMenuItem38));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_REPORTS_REGIONALANALYSIS, jMenuItem56));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKETING_REPORTS_VOLUMEANALYSIS, jMenuItem58));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        jMenuItem37.addActionListener(a -> new dept.marketing.report.OtgruzForm(ownerFrame, true));
        jMenuItem38.addActionListener(a -> new dept.marketing.report.SaleForm(ownerFrame, true));
        jMenuItem56.addActionListener(a -> new dept.marketing.report.AnalizRR(ownerFrame, true));
        jMenuItem58.addActionListener(a -> new dept.marketing.report.AnalizVolumeOtgruz(ownerFrame, true));
    }
}
