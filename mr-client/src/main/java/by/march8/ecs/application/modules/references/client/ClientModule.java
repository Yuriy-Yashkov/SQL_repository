package by.march8.ecs.application.modules.references.client;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.client.mode.ClientMode;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Andy 11.12.2015.
 */
public class ClientModule implements Module {
    private JMenuItem miContractor = new JMenuItem("Контрагенты");
    private JMenu menu = new JMenu("Контрагенты March8");
    private MainController controller;

    @Override
    public void registerModule(final MainController mainController) {
        controller = mainController;
        registerMenu();
    }

    @Override
    public void registerMenu() {
        menu.add(miContractor);
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_CLIENT, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE_CLIENT_CLIENTMARCH8
                , miContractor));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miContractor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new ClientMode(controller);
            }
        });
    }
}
