package by.march8.ecs.application.modules.label;


import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.label.mode.MarkingMode;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;

/**
 * Created by suvarov on 19.12.14.
 */
public class LabelModule implements Module {
    private final static JMenuItem miLabelOne = new JMenuItem(
            MarchReferencesType.ACCOUNTING_LABEL_ONE.getShortName());
    private final static JMenuItem miVNSICD = new JMenuItem(
            MarchReferencesType.ACCOUNTING_VNSICD.getShortName());
    private final static JMenuItem miVNSISTANDART = new JMenuItem(
            MarchReferencesType.ACCOUNTING_VNSISTANDART.getShortName());
    private final static JMenuItem miVNSITextil = new JMenuItem(
            MarchReferencesType.ACCOUNTING_VNSITEXTIL.getShortName());
    private final static JMenuItem miVNSIPolotno = new JMenuItem(
            MarchReferencesType.ACCOUNTING_VNSPOLOTNO.getShortName());
    private final JMenu classification = new JMenu("Маркировка");
    private MainController controller;

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        //GeneralMenuBar.MENU_ACCOUNTING.add(classification) ;
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_LABEL, classification));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_LABEL_LABELONE, miLabelOne));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_LABEL_VWCOLOR, miVNSICD));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_LABEL_VWCANVAS, miVNSIPolotno));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_LABEL_VWGOST, miVNSISTANDART));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_LABEL_VWTEXTIL, miVNSITextil));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {

        miLabelOne.addActionListener(e -> new MarkingMode(controller));

        miVNSICD.addActionListener(e -> {
                new Reference(controller, MarchReferencesType.ACCOUNTING_VNSICD,
                    MarchWindowType.INTERNALFRAME);
        });
        miVNSIPolotno.addActionListener(e -> {
                new Reference(controller, MarchReferencesType.ACCOUNTING_VNSPOLOTNO,
                    MarchWindowType.INTERNALFRAME);
        });
        miVNSISTANDART.addActionListener(e -> {
                new Reference(controller, MarchReferencesType.ACCOUNTING_VNSISTANDART,
                    MarchWindowType.INTERNALFRAME);
        });
        miVNSITextil.addActionListener(e -> {
                new Reference(controller, MarchReferencesType.ACCOUNTING_VNSITEXTIL,
                    MarchWindowType.INTERNALFRAME);
        });

    }
}