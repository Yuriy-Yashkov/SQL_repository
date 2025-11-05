package by.march8.ecs.application.modules.references.materals;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;

/**
 * Модуль справочников МАТЕРИАЛЫ
 *
 * @author andy-linux
 */
public class MaterialModule implements Module {

    public static JMenuItem miDecoration = new JMenuItem(
            MarchReferencesType.MATERIAL_CANVAS_WEAVE.getShortName());
    public static JMenuItem miModifier = new JMenuItem(
            MarchReferencesType.MATERIAL_CANVAS_MODIFIER.getShortName());
    public static JMenuItem miCanvas = new JMenuItem(
            MarchReferencesType.MATERIAL_CANVAS.getShortName());
    public static JMenuItem miYarnType = new JMenuItem(
            MarchReferencesType.MATERIAL_YARN_TYPE.getShortName());
    public static JMenuItem miYarn = new JMenuItem(
            MarchReferencesType.MATERIAL_YARN.getShortName());
    public static JMenuItem miYarnCategory = new JMenuItem(
            MarchReferencesType.MATERIAL_YARN_CATEGORY.getShortName());
    private final JMenu menu = new JMenu("Материалы");
    private final JMenu menuCanvas = new JMenu("Полотно");
    private final JMenu menuYarn = new JMenu("Пряжа");
    private MainController controller;

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_MATERIAL, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_MATERIAL_CANVAS, menuCanvas));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_MATERIAL_YARN, menuYarn));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_MATERIAL_CANVAS_MODIFIER, miModifier));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_MATERIAL_CANVAS_MANCANVAS, miCanvas));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_MATERIAL_CANVAS_DECORATION, miDecoration));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_MATERIAL_YARN_TYPE, miYarnType));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_MATERIAL_YARN_MANYARN, miYarn));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_MATERIAL_YARN_CATEGORY, miYarnCategory));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miModifier.addActionListener(e -> new Reference(controller,
                MarchReferencesType.MATERIAL_CANVAS_MODIFIER,
                MarchWindowType.INTERNALFRAME));

        miCanvas.addActionListener(e -> new Reference(controller, MarchReferencesType.MATERIAL_CANVAS,
                MarchWindowType.INTERNALFRAME));

        miDecoration.addActionListener(e -> new Reference(controller,
                MarchReferencesType.MATERIAL_CANVAS_WEAVE,
                MarchWindowType.INTERNALFRAME));

        miYarnType.addActionListener(e -> new Reference(controller,
                MarchReferencesType.MATERIAL_YARN_TYPE,
                MarchWindowType.INTERNALFRAME));

        miYarn.addActionListener(e -> new Reference(controller, MarchReferencesType.MATERIAL_YARN,
                MarchWindowType.INTERNALFRAME));


        miYarnCategory.addActionListener(e -> new Reference(controller, MarchReferencesType.MATERIAL_YARN_CATEGORY,
                MarchWindowType.INTERNALFRAME));
    }

}
