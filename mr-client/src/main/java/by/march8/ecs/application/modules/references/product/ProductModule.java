package by.march8.ecs.application.modules.references.product;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;


/**
 * Модуль справочников ИЗДЕЛИЯ
 *
 * */
public class ProductModule implements Module {
    private final JMenuItem miModelProduct = new JMenuItem(
            MarchReferencesType.MODEL_PRODUCT.getShortName());
    private final JMenuItem miModelSize = new JMenuItem(
            MarchReferencesType.MODEL_SIZE.getShortName());
    private final JMenuItem miProductType = new JMenuItem(
            MarchReferencesType.PRODUCT_TYPE.getShortName());
    private final JMenuItem miProductKind = new JMenuItem(
            MarchReferencesType.PRODUCT_KIND.getShortName());
    private final JMenuItem miProductBrand = new JMenuItem(
            MarchReferencesType.PRODUCT_BRAND.getShortName());
    private final JMenuItem miProductCollection = new JMenuItem(
            MarchReferencesType.PRODUCT_COLLECTION.getShortName());
    private final JMenuItem miProductGroup = new JMenuItem(
            MarchReferencesType.PRODUCT_GROUP.getShortName());
    private final JMenuItem miProductRange = new JMenuItem(
            MarchReferencesType.PRODUCT_RANGE.getShortName());
    private final JMenuItem miModelMaterial = new JMenuItem(
            MarchReferencesType.MODEL_MATERIAL.getShortName());
    private final JMenuItem miMeasurementType = new JMenuItem(
            MarchReferencesType.MEASUREMENT_TYPE.getShortName());
    private final JMenu classifier = new JMenu("Классификация");
    private final JMenu measurement = new JMenu("Обмеры");
    private final JMenu product = new JMenu("Изделия");

    private MainController controller = null;

    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT, product));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_CLASSIFICATION, classifier));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_MODEL, miModelProduct));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_MEASUREMENT, measurement));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_MATERIAL, miModelMaterial));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_SIZE, miModelSize));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_MEASUREMENT_TYPE, miMeasurementType));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_CLASSIFICATION_RANGE, miProductRange));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_CLASSIFICATION_BRAND, miProductBrand));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_CLASSIFICATION_KIND, miProductKind));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_CLASSIFICATION_GROUP, miProductGroup));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_PRODUCT_CLASSIFICATION_COLLECTION, miProductCollection));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miModelProduct.addActionListener(e -> new Reference(controller, MarchReferencesType.MODEL_PRODUCT,
                MarchWindowType.INTERNALFRAME));
        miModelMaterial.addActionListener(e -> new Reference(controller, MarchReferencesType.MODEL_MATERIAL,
                MarchWindowType.INTERNALFRAME));
        miModelSize.addActionListener(e -> new Reference(controller, MarchReferencesType.MODEL_SIZE,
                MarchWindowType.INTERNALFRAME));
        miProductType.addActionListener(e -> new Reference(controller, MarchReferencesType.PRODUCT_TYPE,
                MarchWindowType.INTERNALFRAME));
        miProductKind.addActionListener(e -> new Reference(controller, MarchReferencesType.PRODUCT_KIND,
                MarchWindowType.INTERNALFRAME));
        miProductBrand.addActionListener(e -> new Reference(controller, MarchReferencesType.PRODUCT_BRAND,
                MarchWindowType.INTERNALFRAME));
        miProductCollection.addActionListener(e -> new Reference(controller, MarchReferencesType.PRODUCT_COLLECTION,
                MarchWindowType.INTERNALFRAME));
        miProductGroup.addActionListener(e -> new Reference(controller, MarchReferencesType.PRODUCT_GROUP,
                MarchWindowType.INTERNALFRAME));
        miProductRange.addActionListener(e -> new Reference(controller, MarchReferencesType.PRODUCT_RANGE,
                MarchWindowType.INTERNALFRAME));
        miMeasurementType.addActionListener(e -> new Reference(controller, MarchReferencesType.MEASUREMENT_TYPE,
                MarchWindowType.INTERNALFRAME));
    }

    @Override
    public void registerModule(final MainController controller) {
        this.controller = controller;
        registerMenu();
    }

}
