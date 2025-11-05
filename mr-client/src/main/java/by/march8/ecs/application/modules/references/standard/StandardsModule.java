package by.march8.ecs.application.modules.references.standard;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;

/**
 * Модуль справочников СТАНДАРТЫ
 *
 * @author andy-linux
 */
public class StandardsModule implements Module {

    public final static JMenuItem miInvoices = new JMenuItem(
            MarchReferencesType.INVOICE_TYPE.getShortName());
    private final static JMenuItem miUnit = new JMenuItem(
            MarchReferencesType.UNIT.getShortName());

    private final static JMenuItem miCodeType = new JMenuItem(
            MarchReferencesType.CODETYPE.getShortName());
    private final static JMenuItem miCodeList = new JMenuItem(
            MarchReferencesType.CODELIST.getShortName());
    private final JMenu classification = new JMenu("Классификационные стандарты");
    private final JMenu menu = new JMenu("Стандарты");
    private final JMenu documents = new JMenu("Документы");
    private MainController controller;

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_STANDARD, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_STANDARD_UNIT, miUnit));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_STANDARD_DOCUMENTS, documents));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_STANDARD_DOCUMENTS_INVOICESTYPE, miInvoices));

        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_STANDARD_CLASSIFICATION, classification));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_STANDARD_CLASSIFICATION_CODELIST, miCodeList));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_STANDARD_CLASSIFICATION_CODETYPE, miCodeType));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miCodeType.addActionListener(e -> new Reference(controller, MarchReferencesType.CODETYPE,
                MarchWindowType.INTERNALFRAME));
        miCodeList.addActionListener(e -> new Reference(controller, MarchReferencesType.CODELIST,
                MarchWindowType.INTERNALFRAME));
        miUnit.addActionListener(e -> new Reference(controller, MarchReferencesType.UNIT,
                MarchWindowType.INTERNALFRAME));

        miInvoices.addActionListener(e -> new Reference(controller, MarchReferencesType.INVOICE_TYPE,
                MarchWindowType.INTERNALFRAME));

    }

}
