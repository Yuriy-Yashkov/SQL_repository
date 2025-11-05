package by.march8.ecs.application.shell.servicemonitor.model;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ServicePanel extends JPanel {
    private JLabel lblServiceName;
    private JButton btnServiceReload;

    public ServicePanel() {
        super(new MigLayout());
        setPreferredSize(new Dimension(0, 80));
        lblServiceName = new JLabel("Наименование службы");
        btnServiceReload = new JButton("Перезапуск");
        add(new JLabel(""), "wrap");
        add(lblServiceName, "wrap");
        add(btnServiceReload, "wrap");
        setBorder(BorderFactory.createLineBorder(Color.gray));
        //setBorder(new EmptyBorder(10,10,10,10));
        setBackground(Color.WHITE);
    }
}
