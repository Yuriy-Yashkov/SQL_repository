package by.march8.ecs.application.modules.references.classifier;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierMode;
import by.march8.ecs.application.modules.references.common.ColorReferenceMode;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Andy 16.10.2015.
 */
public class ClassifierModule implements Module {

    private JMenuItem miClassifier = new JMenuItem("Классификатор продукции");

    private JMenuItem miEanColors = new JMenuItem("Справочник цветов");
    private JMenu menu = new JMenu("Классификатор");
    private MainController controller;

    @Override
    public void registerModule(final MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        menu.add(miClassifier);
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_CLASSIFIER, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_CLASSIFIER_CLASSIFIERITEM, miClassifier));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_COLOR, miEanColors));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miClassifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new ClassifierMode(controller);
            }
        });

        miEanColors.addActionListener(a -> {
            new ColorReferenceMode(controller);
        });
    }
}
