package by.march8.ecs.application.modules.production.submodules.packing.unsort;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.upack.ns.DataForm;
import dept.upack.ns.PereuchZSH;

import javax.swing.*;

public class UsortModule implements Module {

    private final JMenu miUnsortMenu = new JMenu("Не сортная");
    private final JMenuItem miReCounting = new JMenuItem("Переучёт ЗШ цеха Н/С продукции");
    private final JMenuItem miCounting = new JMenuItem("Учёт н/с");

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
        miUnsortMenu.add(miReCounting);
        miUnsortMenu.add(miCounting);

        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_UNSORT, miUnsortMenu));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_UNSORT_RECOUNTING, miReCounting));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION_PACKING_UNSORT_COUNTING, miCounting));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miReCounting.addActionListener(a -> new PereuchZSH(ownerFrame, true));
        miCounting.addActionListener( a -> new DataForm(ownerFrame, true));
    }
}
