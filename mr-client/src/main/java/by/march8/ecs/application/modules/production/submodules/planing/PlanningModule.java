package by.march8.ecs.application.modules.production.submodules.planing;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.production.planning.PlanProductioForm;
import dept.production.planning.ProjectPlanForm;

import javax.swing.*;

public class PlanningModule implements Module {
    private final JMenu planningMenu = new JMenu("Планирование");
    private final JMenuItem miProductionPlan = new JMenuItem("План производства");
    private final JMenuItem miCalculationWages = new JMenuItem("Проект плана");
    private final JMenuItem miEAN = new JMenuItem("EAN-коды");
    private final JMenuItem miAnalysisOfImplementation = new JMenuItem("Анализ выполнения");

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
        planningMenu.add(miProductionPlan);
        planningMenu.add(miCalculationWages);
        planningMenu.add(miEAN);
        planningMenu.add(miAnalysisOfImplementation);
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PLANNING, planningMenu));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PLANNING_PRODUCTION_PLAN, miProductionPlan));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PLANNING_CALCULATION_WAGES, miCalculationWages));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PLANNING_EAN, miEAN));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PLANNING_ANALYSIS_OF_IMPLEMENTATION, miAnalysisOfImplementation));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miProductionPlan.addActionListener(a -> new PlanProductioForm(controller, true, false));
        miCalculationWages.addActionListener(a -> new ProjectPlanForm(controller, true, false));
        miEAN.addActionListener(a -> new dept.production.planning.ean.EanForm(controller, true));
        miAnalysisOfImplementation.addActionListener(a -> new ProjectPlanForm(controller, true, ""));
    }
}
