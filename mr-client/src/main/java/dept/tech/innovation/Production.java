/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.tech.innovation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author user
 */
public class Production extends JDialog {

    JTabbedPane jPane_1;
    JTabbedPane jPane_2;
    JPanel mainPanel;
    JPanel techPanel;
    JPanel tradePanel;
    Production th;
    private JMenuBar jMenuBarSettings;
    private JMenu jMenuSettings;
    private JMenuItem jMenuItemTechSettings;
    private JMenuItem jMenuItemTradeSettings;

    public Production(Frame owner, boolean modal) {
        super(owner, modal);
        th = this;


        initComponents();
        setComponents();
        setEvents();
        //  tc.setTechComponentsEventModel();
        // tc_1.setTradeComponentsEventModel();

        setSize(570, 570);
        setLocationRelativeTo(owner);
        setTitle("Продукция");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        setVisible(true);
    }

    private void initComponents() {

        jMenuBarSettings = new JMenuBar();
        jMenuItemTechSettings = new JMenuItem("Импортозамещение");
        jMenuItemTradeSettings = new JMenuItem("Торговые марки");
        jMenuSettings = new JMenu("Настройки");

        jPane_1 = new JTabbedPane();

        mainPanel = new JPanel();
        techPanel = new JPanel();
        tradePanel = new JPanel();

    }

    private void setComponents() {

        jMenuSettings.add(jMenuItemTechSettings);
        jMenuSettings.add(jMenuItemTradeSettings);
        jMenuBarSettings.add(jMenuSettings);

        setJMenuBar(jMenuBarSettings);

        mainPanel.setLayout(new BorderLayout(1, 1));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        techPanel.setLayout(new BorderLayout(1, 1));
        techPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tradePanel.setLayout(new BorderLayout(1, 1));
        tradePanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        TechTradeForm tc = new TechTradeForm(true);
        tc.setTechComponentsEventModel();

        techPanel.add(TechTradeForm.headPanel, BorderLayout.NORTH);
        techPanel.add(new JScrollPane(TechTradeForm.table), BorderLayout.CENTER);
        techPanel.add(TechTradeForm.buttPanel, BorderLayout.SOUTH);

        TechTradeForm tc_1 = new TechTradeForm(false);

        tc_1.setTradeComponentsEventModel();

        tradePanel.add(TechTradeForm.headPanel, BorderLayout.NORTH);
        tradePanel.add(new JScrollPane(TechTradeForm.table), BorderLayout.CENTER);
        tradePanel.add(TechTradeForm.buttPanel, BorderLayout.SOUTH);

        jPane_1.addTab("Импортозамещение", techPanel);
        jPane_1.addTab("Торговые марки", tradePanel);

        mainPanel.add(jPane_1);

        getContentPane().add(mainPanel);


    }

    private void setEvents() {
        jMenuItemTechSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TechSettingsForm(th, true).setVisible(true);
            }
        });

        jMenuItemTradeSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TechSettingsForm(th, true, true).setVisible(true);
            }
        });
    }


}
