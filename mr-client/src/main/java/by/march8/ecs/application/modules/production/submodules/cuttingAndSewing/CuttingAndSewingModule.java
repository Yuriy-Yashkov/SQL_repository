package by.march8.ecs.application.modules.production.submodules.cuttingAndSewing;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.production.zsh.spec.SpecForm;
import dept.production.zsh.zplata.BuhVedomostForm;
import dept.production.zsh.zplata.UtilZPlata;
import dept.production.zsh.zplata.ZPlataForm;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class CuttingAndSewingModule implements Module {

    private final JMenu menu = new JMenu("Закройно-швейный цех");
    private final JMenuItem miModels = new JMenuItem("Спецификации моделей");
    private final JMenuItem miCalculationWages = new JMenuItem("Расчет ЗП рабочим");
    private final JMenuItem miSalaryTransferred = new JMenuItem("ЗП переданная в бухгалтерию");

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
        menu.add(miSalaryTransferred);
        menu.add(miCalculationWages);
        menu.add(miModels);
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_WORKHOUSE, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_WORKHOUSE_MODELS, miModels));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_WORKHOUSE_CALCULATION_WAGES, miCalculationWages));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_WORKHOUSE_SALARY_TRANFERED, miSalaryTransferred));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miModels.addActionListener(a -> new SpecForm(controller, true));
        miCalculationWages.addActionListener(a -> new ZPlataForm(controller, true));
        miSalaryTransferred.addActionListener(a -> new BuhVedomostForm(ownerFrame, true, UtilZPlata.OPEN));
    }

}
