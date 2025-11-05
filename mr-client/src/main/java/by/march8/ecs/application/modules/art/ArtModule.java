package by.march8.ecs.application.modules.art;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.art.mode.ProductionReportMode;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;

/**
 * @author by lidashka.
 */
public class ArtModule implements Module {

    private final JMenuItem miModelConfectionMap = new JMenuItem(
            MarchReferencesType.MODEL_CONFECTION_MAP.getShortName());

    private final JMenuItem miModelSample = new JMenuItem(
            MarchReferencesType.MODEL_SAMPLE.getShortName());

    private final JMenuItem miProtocol = new JMenuItem(
            MarchReferencesType.MODEL_PROTOCOL.getShortName());

    private final JMenuItem miProductionReport = new JMenuItem("Производственный отчет");

    private MainController controller = null;

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.ART_PRODUCTIONREPORT, miProductionReport));
        controller.addModuleMenu(new SectionMenu(MarchSection.ART_SAMPLE, miModelSample));
        controller.addModuleMenu(new SectionMenu(MarchSection.ART_CONFECTION, miModelConfectionMap));
        controller.addModuleMenu(new SectionMenu(MarchSection.ART_PROTOCOL, miProtocol));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miModelConfectionMap.addActionListener(e -> new Reference(controller, MarchReferencesType.MODEL_CONFECTION_MAP,
                MarchWindowType.INTERNALFRAME));

        miModelSample.addActionListener(e -> new Reference(controller, MarchReferencesType.MODEL_SAMPLE,
                MarchWindowType.INTERNALFRAME));

        miProtocol.addActionListener(e -> new Reference(controller, MarchReferencesType.MODEL_PROTOCOL,
                MarchWindowType.INTERNALFRAME));

        miProductionReport.addActionListener(a -> {
            new ProductionReportMode(controller);
        });
    }

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }
}
