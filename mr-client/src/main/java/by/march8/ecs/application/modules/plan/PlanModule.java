package by.march8.ecs.application.modules.plan;


import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.plan.mode.PlanMode;
import by.march8.ecs.application.modules.planning.mode.ProductionPlanningMode;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

/**
 * Created by dpliushchai on 19.11.2014.
 */
public class PlanModule implements Module {

    public static JMenuItem miPlanMode = new JMenuItem(
            "Выполнение плана");

    private JMenuItem miProductionPlanning = new JMenuItem("Планирование производства");

    private MainController controller;

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.PLAN_PLANMODE, miPlanMode));
        controller.addModuleMenu(new SectionMenu(MarchSection.PLAN_PRODUCTIONPLANNING, miProductionPlanning));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miPlanMode.addActionListener(e ->
                new PlanMode(controller)
        );

        miProductionPlanning.addActionListener(a -> {
            new ProductionPlanningMode(controller);
        });
    }
}
