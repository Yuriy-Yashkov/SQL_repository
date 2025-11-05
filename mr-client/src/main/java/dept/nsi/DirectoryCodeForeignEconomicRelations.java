/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.nsi;

import common.PanelWihtFone;
import common.ProgressBar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author user
 */
public class DirectoryCodeForeignEconomicRelations extends JDialog {
    public DefaultTableModel tModel;
    JTable table;
    Vector columns = new Vector();
    Vector rows = new Vector();
    JScrollPane scrollTable;
    JPanel mainPanel;
    JButton bDelete;
    JButton bAdd;
    JButton bUpdate;
    JButton bClose;
    JLabel lModel;
    JTextField tfModel;
    JDialog thisForm;
    ProgressBar pb;
    private TableColumn tcol;


    public DirectoryCodeForeignEconomicRelations(JFrame parent, boolean f) {
        super(parent, f);
        thisForm = this;

        columns.add("");
        columns.add("Код");
        columns.add("Состав");
        columns.add("Название");

        initComponents();

        //table.getColumnModel().getColumn(4).setPreferredWidth(180);
        add(mainPanel);
        setTitle("NSI_KLD");

        this.setSize(950, 560);
        this.setMinimumSize(new Dimension(950, 560));
        this.setMaximumSize(new Dimension(1000, 700));
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();
        bClose = new JButton("Закрыть");
        bDelete = new JButton("Удалить");
        bUpdate = new JButton("Редактировать");
        bAdd = new JButton("Добавить");
        bClose.addActionListener(new DirectoryCodeForeignEconomicRelations.ActionClose());
        bAdd.addActionListener(new DirectoryCodeForeignEconomicRelations.ActionAdd());
        bUpdate.addActionListener(new DirectoryCodeForeignEconomicRelations.ActionUpdate());


        tModel = new DefaultTableModel();
        table = new JTable();


        JScrollPane spData = new JScrollPane(table);

        JTableHeader header = table.getTableHeader();
        header.setResizingAllowed(false);


        //*************************************************************************************
        // Init northPanel of mainPanel
        //*************************************************************************************


        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(new JLabel("Список внешне экономичиских кодов"));
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(spData);

        //*************************************************************************************
        // Init southPanel of mainPanel
        //*************************************************************************************             
        JPanel southPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JPanel southLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel southLeftGridPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        southLeftGridPanel.add(bAdd);
        southLeftGridPanel.add(bUpdate);
        southLeftGridPanel.add(bDelete);
        southLeftPanel.add(southLeftGridPanel);
        southPanel.add(southLeftPanel);
        JPanel southRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southRightPanel.add(bClose);
        southPanel.add(southRightPanel);

        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel);

        updateForm();

    }

    private void ActionAddButton(java.awt.event.ActionEvent evt) {
        new EditDirectoryCodeForeignEconomicRelations(this, true);
    }

    private void ActionUpdateButton(java.awt.event.ActionEvent evt) {
        boolean flagError = true;
        ArrayList elem = new ArrayList();
        for (int i = 0; i < tModel.getRowCount(); i++) {
            if ((Boolean) tModel.getValueAt(i, 0)) {
                ArrayList v = new ArrayList();
                for (int j = 1; j < 4; j++) {
                    v.add(tModel.getValueAt(i, j));
                }
                elem.add(v);
            }
        }
        if (elem.size() > 1) {
            JOptionPane.showMessageDialog(null, "Вы пометили несколько записей", "Ошибка", JOptionPane.ERROR_MESSAGE);
            flagError = false;
        }
        if (elem.size() == 0) {
            JOptionPane.showMessageDialog(null, "Не выбрано не одной записи", "Ошибка", JOptionPane.ERROR_MESSAGE);
            flagError = false;
        }
        /*
        long kol = Long.parseLong(((ArrayList)elem.get(0)).get(0).toString());
        String str1 = ((ArrayList)elem.get(0)).get(1).toString();
        String str2 = ((ArrayList)elem.get(0)).get(2).toString();
        * */

        if (flagError) {
            new EditDirectoryCodeForeignEconomicRelations(this, true, Long.parseLong(((ArrayList) elem.get(0)).get(0).toString()), ((ArrayList) elem.get(0)).get(1).toString(), ((ArrayList) elem.get(0)).get(2).toString());
        }

    }

    void updateForm() {
        NsiDB db = new NsiDB();
        rows = new Vector(db.getNSI_WKD());
        tModel.setDataVector(rows, columns);
        //tModel.fireTableDataChanged();
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setModel(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(165);
        table.getColumnModel().getColumn(3).setPreferredWidth(450);
        for (int i = 1; i < table.getColumnCount(); i++) {
            TableColumn tc = table.getColumnModel().getColumn(0);
            tc.setCellEditor(table.getDefaultEditor(Boolean.class));
            tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
            tcol = table.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new CustomTableCellRenderer());
        }
    }

    class ActionClose implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    class ActionAdd implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ActionAddButton(e);
        }
    }

    class ActionUpdate implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ActionUpdateButton(e);
        }
    }


}