package by.march8.ecs.application.modules.warehouse.internal.displacement;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.internal.displacement.mode.DisplacementMode;
import by.march8.ecs.application.modules.warehouse.internal.displacement.mode.DisplacementReportMode;
import by.march8.ecs.application.modules.warehouse.internal.storage.mode.StorageLocationsMode;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Модуль внутреннего перемещения
 */
public class DisplacementDeptModule implements Module {
    private MainController controller;

    private JMenu menu = new JMenu("Внутреннее перемещение");
    private JMenuItem miDisplacementInvoice = new JMenuItem("Журнал накладных внутреннего перемещения");
    private JMenuItem miStorageLocations = new JMenuItem("Данные по местам хранения");
    private JMenuItem miDisplacementReport = new JMenuItem("Отчет о сдаче готовой продукции");


    @Override
    public void registerModule(final MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        miDisplacementReport.setForeground(Color.BLUE);
        menu.add(miDisplacementInvoice);
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_DISPLACEMENT, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_DISPLACEMENT_INVOICE, miDisplacementInvoice));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_DISPLACEMENT_REPORT, miDisplacementReport));

        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING_DISPLACEMENT_STORAGELOCATIONS, miStorageLocations));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miDisplacementInvoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new DisplacementMode(controller);
            }
        });

        miStorageLocations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new StorageLocationsMode(controller);
            }
        });

        miDisplacementReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new DisplacementReportMode(controller);
            }
        });
    }


}
