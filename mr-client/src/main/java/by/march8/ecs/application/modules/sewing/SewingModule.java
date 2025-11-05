package by.march8.ecs.application.modules.sewing;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

public class SewingModule implements Module {

    private final JMenu miReport = new JMenu("Отчёты");
    private final JMenuItem miReportJournalEanCode = new JMenuItem("Трудозатраты");
    private final JMenuItem miCutCardMode = new JMenuItem("Выполнение плана в н/ч");
    private final JMenuItem miSeamstressProductionMap = new JMenuItem("Выполнение плана в млн. руб.");
    private final JMenuItem miCurrentRemnants = new JMenuItem("Накопительная ведомость");

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
        miReport.add(miReportJournalEanCode);
        miReport.add(miCutCardMode);
        miReport.add(miSeamstressProductionMap);
        miReport.add(miCurrentRemnants);

        controller.addModuleMenu(new SectionMenu(MarchSection.SEWING_REPORT, miReport));
        controller.addModuleMenu(new SectionMenu(MarchSection.SEWING_REPORT_JOURNAL, miReportJournalEanCode));
        controller.addModuleMenu(new SectionMenu(MarchSection.SEWING_REPORT_CUT, miCutCardMode));
        controller.addModuleMenu(new SectionMenu(MarchSection.SEWING_REPORT_PRODUCTIONMAP, miSeamstressProductionMap));
        controller.addModuleMenu(new SectionMenu(MarchSection.SEWING_REPORT_REMNANTS, miCurrentRemnants));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miReportJournalEanCode.addActionListener(e ->
                new dept.poshiv.DataForm(ownerFrame, true, "ТрудозатПошив.ots"));
        miCutCardMode.addActionListener(a ->
                new dept.poshiv.DataForm(ownerFrame, true, "планПошивНЧ.ots"));
        miSeamstressProductionMap.addActionListener(a ->
                new dept.poshiv.DataForm(ownerFrame, true, "планПошивМР.ots"));
        miCurrentRemnants.addActionListener( a ->
                new dept.poshiv.DataForm(ownerFrame, true, "пошивПодекадныйВыпуск.ots"));
    }
}
