package by.march8.ecs.application.modules.settings;


import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.general.uicontrol.GeneralMenuBar;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;

/**
 * Created by suvarov on 04.12.14.
 */
public class DocumentModule implements Module {
    private final static JMenuItem miDocument = new JMenuItem(
            MarchReferencesType.SETTINGS_DOCUMENT.getShortName());
    private final static JMenuItem miDocumentType = new JMenuItem(
            MarchReferencesType.SETTINGS_DOCUMENT_TYPE.getShortName());
    private final static JMenuItem miDocumentRelation = new JMenuItem(
            MarchReferencesType.SETTINGS_DOCUMENT_RELATION.getShortName());
    private final JMenu classification = new JMenu("Документы");
    private MainController controller;

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        GeneralMenuBar.MENU_SETTINGS.add(classification);
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_DOCUMENTS, classification));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_DOCUMENTS_DOCUMENT, miDocument));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_DOCUMENTS_DOCUMENTRELATION, miDocumentRelation));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS_DOCUMENTS_DOCUMENTTYPE, miDocumentType));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miDocument.addActionListener(e -> new Reference(controller, MarchReferencesType.SETTINGS_DOCUMENT,
                MarchWindowType.INTERNALFRAME));
        miDocumentRelation.addActionListener(e -> new Reference(controller, MarchReferencesType.SETTINGS_DOCUMENT_RELATION,
                MarchWindowType.INTERNALFRAME));
        miDocumentType.addActionListener(e -> new Reference(controller, MarchReferencesType.SETTINGS_DOCUMENT_TYPE,
                MarchWindowType.INTERNALFRAME));
    }
}
