package by.march8.ecs.application.modules.marking;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.markerovka.PrintLabelForm;
import dept.markerovka.TransItemForm;

import javax.swing.*;

public class MarkingModule implements Module {


    private final JMenuItem miPrintUnit = new JMenuItem("Печать этикеток (ед)");
    private final JMenuItem miPrintInvoice = new JMenuItem("Печать этикеток (ттн)");
    private final JMenuItem miTransferProduct = new JMenuItem("Перевод изделия");

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

        controller.addModuleMenu(new SectionMenu(MarchSection.MARKING_PRINTUNIT, miPrintUnit));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKING_PRINTINVOICE, miPrintInvoice));
        controller.addModuleMenu(new SectionMenu(MarchSection.MARKING_TRANSFERPRODUCT, miTransferProduct));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miPrintUnit.addActionListener(a -> new PrintLabelForm(ownerFrame, true));
        miPrintInvoice.addActionListener(a -> new dept.markerovka.NakladnieForm(ownerFrame, true));
        miTransferProduct.addActionListener(a -> new TransItemForm(ownerFrame, true));
    }
}
