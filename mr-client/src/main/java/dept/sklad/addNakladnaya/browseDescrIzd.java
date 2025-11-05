/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad.addNakladnaya;

import dept.sklad.SkladDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * @author user
 */
public class browseDescrIzd extends JDialog {

    JTable jtTable;
    DefaultTableModel dtmTableModel;
    JScrollPane jspScrollPane;
    JButton btnSelect;
    JButton btnClose;
    JPanel jpContent;
    JPanel jpButton;
    HashMap hm;
    String sstr;
    addPryazhaNakl apn;

    public browseDescrIzd(addPryazhaNakl parent, String str) {
        super(parent);
        sstr = str;
        apn = parent;
        initComp();
        this.setFocusable(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public void initComp() {
        if (sstr.equals("1")) {
            jpButton = new JPanel(new FlowLayout());
            jpContent = new JPanel(new BorderLayout());
            btnClose = new JButton("Закрыть");
            btnClose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            btnSelect = new JButton("Выбрать");
            btnSelect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hm = new HashMap();
                    apn.tfShifrArt.setText(jtTable.getValueAt(jtTable.getSelectedRow(), 0).toString());
                    dispose();
                }
            });
            jtTable = new JTable();
            dtmTableModel = new DefaultTableModel();
            SkladDB sdb = new SkladDB();
            HashMap newHash = new HashMap();
            newHash = sdb.getAllPryazha();
            updateTable1(newHash);
            jspScrollPane = new JScrollPane(jtTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jpContent.add(jspScrollPane, BorderLayout.CENTER);
            jpButton.add(btnSelect);
            jpButton.add(btnClose);
            jpContent.add(jpButton, BorderLayout.PAGE_END);
            this.add(jpContent);
            pack();
            this.setSize(800, 350);
            this.setTitle("Выбор пряжи");
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        } else {
            jpButton = new JPanel(new FlowLayout());
            jpContent = new JPanel(new BorderLayout());
            btnClose = new JButton("Закрыть");
            btnClose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            btnSelect = new JButton("Выбрать");
            btnSelect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hm = new HashMap();
                    apn.tfNomDoc.setText(jtTable.getValueAt(jtTable.getSelectedRow(), 0).toString());
                    dispose();
                }
            });
            jtTable = new JTable();
            dtmTableModel = new DefaultTableModel();
            SkladDB sdb = new SkladDB();
            HashMap newHash = new HashMap();
            newHash = sdb.getAllNakl();
            updateTable2(newHash);
            jspScrollPane = new JScrollPane(jtTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jpContent.add(jspScrollPane, BorderLayout.CENTER);
            jpButton.add(btnSelect);
            jpButton.add(btnClose);
            jpContent.add(jpButton, BorderLayout.PAGE_END);
            this.add(jpContent);
            pack();
            this.setSize(800, 350);
            this.setTitle("Выбор пряжи");
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }

    public void updateTable1(HashMap data) {
        String[][] dataTable = new String[data.size()][2];
        String[] columnName = {"SAR", "NGPR"};
        for (int i = 0; i < data.size(); i++) {
            HashMap hash = new HashMap();
            hash = (HashMap) data.get(i);
            dataTable[i][0] = hash.get("sar").toString();
            dataTable[i][1] = hash.get("ngpr").toString();
        }
        dtmTableModel.setDataVector(dataTable, columnName);
        jtTable.setModel(dtmTableModel);
    }

    public void updateTable2(HashMap data) {
        String[][] dataTable = new String[data.size()][2];
        String[] columnName = {"NDOC", "DATE"};
        for (int i = 0; i < data.size(); i++) {
            HashMap hash = new HashMap();
            hash = (HashMap) data.get(i);
            dataTable[i][0] = hash.get("ndoc").toString();
            dataTable[i][1] = hash.get("date").toString();
        }
        dtmTableModel.setDataVector(dataTable, columnName);
        jtTable.setModel(dtmTableModel);
    }
}
