/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.tech.innovation;

import common.PanelWihtFone;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 * @author user
 */


public class AddProduction extends JDialog {

    JPanel mainPanel;
    JTable table;
    DefaultTableModel tModel;

    JButton bAdd;
    JButton bSearch;

    JScrollPane scrollTable;

    Vector columns = new Vector();
    Vector rows = new Vector();

    JTextField tfFas;
    JLabel lFas;
    boolean flag = false;
//    int nFas = 0;

    public AddProduction(JDialog owner, boolean modal) {
        super(owner, modal);

        columns.add("Наименование");
        columns.add("Модель");
        columns.add("Код пр.");

        initComponents();

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(35);
        table.getColumnModel().getColumn(2).setPreferredWidth(20);

        add(mainPanel);

        setSize(550, 340);
        setLocationRelativeTo(owner);
        setResizable(false);
        setTitle("Добавление инновационных изделий");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public AddProduction(JDialog owner, boolean modal, boolean trade) {
        super(owner, modal);

        flag = trade;

        columns.add("Наименование");
        columns.add("Модель");
        columns.add("Код пр.");


        initComponents();

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(35);
        table.getColumnModel().getColumn(2).setPreferredWidth(20);

        add(mainPanel);

        setSize(550, 340);
        setLocationRelativeTo(owner);
        setResizable(false);
        setTitle("Добавление инновационных изделий");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        bAdd = new JButton("Добавить");
        bAdd.setBounds(420, 280, 110, 20);
        bAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {


                    ProductionDB db = new ProductionDB();

                    if (tModel.getValueAt(table.getSelectedRow(), 2) == null) {
                        JOptionPane.showMessageDialog(null, "Выберите категорию проекта для добавляемого изделия!");
                        return;
                    }

                    String name = tModel.getValueAt(table.getSelectedRow(), 0).toString().trim();
                    int fas = Integer.parseInt(tModel.getValueAt(table.getSelectedRow(), 1).toString());
                    int kod_naim = Integer.parseInt(tModel.getValueAt(table.getSelectedRow(), 2).toString());

                    if (!flag) {
                        if (db.addTechItems(fas, name, kod_naim)) {
                            JOptionPane.showMessageDialog(null, "Изделие добавлено", "Сообщение", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            tfFas.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Изделие не было добавлено", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        if (db.addTradeItems(fas, name, kod_naim)) {
                            JOptionPane.showMessageDialog(null, "Изделие добавлено", "Сообщение", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            tfFas.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Изделие не было добавлено", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    db.disConn();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });
        mainPanel.add(bAdd);
        bAdd.setEnabled(false);

        //кнопка поиска
        bSearch = new JButton("Найти");
        bSearch.setBounds(130, 280, 100, 20);
        bSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ProductionDB db = new ProductionDB();
                    int fas = Integer.parseInt(tfFas.getText().toString());
                    rows = db.searchItems(fas);

                    db.disConn();

                    while (tModel.getRowCount() > 0) {
                        tModel.removeRow(0);
                    }

                    for (int i = 0; i < rows.size(); i++) {
                        tModel.addRow(new Vector((Vector) rows.get(i)));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(bSearch);

        //создаём таблицу
        tModel = new DefaultTableModel(rows, columns);
        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);

        ListSelectionModel selModel = table.getSelectionModel();

        selModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

                if (table.getSelectedRow() >= 0) {
                    bAdd.setEnabled(true);
                } else {
                    bAdd.setEnabled(false);
                }

            }
        });

        addComboToTable(new JComboBox());

//        TableColumn gradeColumn = table.getColumnModel().getColumn(0);
//        final JComboBox comboBox = new JComboBox();
//        gradeColumn.setCellEditor(new DefaultCellEditor(comboBox));
//        
//        setValueAtComboBox(comboBox);
//        for (int i = 0; i < rows.size(); i++){
//           String val = ((Vector)rows.elementAt(i)).get(0).toString().trim();
//           for(int j = 0; j < tModel.getRowCount(); j++){
//               if (i == j) {
//                 tModel.setValueAt(val, j, 0);
//               }
//           }    
//          
//        } 


        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(5, 5, 533, 270);
        mainPanel.add(scrollTable);

        lFas = new JLabel("Модель:");
        lFas.setBounds(5, 280, 80, 20);
        mainPanel.add(lFas);

        tfFas = new JTextField(9);
        tfFas.setBounds(75, 280, 50, 20);
        tfFas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        ProductionDB db = new ProductionDB();
                        int fas = Integer.parseInt(tfFas.getText());
                        rows = db.searchItems(fas);

                        db.disConn();

                        while (tModel.getRowCount() > 0) {
                            tModel.removeRow(0);
                        }

                        for (Object row : rows) {
                            tModel.addRow(new Vector((Vector) row));
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tfFas.setText("");

                }
            }
        });
        mainPanel.add(tfFas);

    }

    private void addComboToTable(JComboBox combo) {
        TableColumn gradeColumn = table.getColumnModel().getColumn(2);
        JComboBox comboBox = combo;
        comboBox.removeAllItems();
        try {
            addItemToCombo(comboBox);
        } catch (Exception e) {

        }
        gradeColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    private void addItemToCombo(JComboBox combo) {
        try {
            ProductionDB db = new ProductionDB();
            Vector tmp = new Vector();
            if (!flag) {
                tmp = db.getSettingKod();
            } else {
                tmp = db.getTSettingKod();
            }


            for (int i = 0; i < tmp.size(); i++) {
                combo.addItem(Integer.parseInt(((Vector) tmp.elementAt(i)).get(0).toString()));
            }
            db.disConn();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

//     public void  setValueAtComboBox(JComboBox cb) {
//         InnovDB db = new InnovDB();
//         try {
//             if (tfFas != null) {              
//                 
//                 int fas = Integer.parseInt(tfFas.getText().toString().trim());
//                 nFas = fas;
//                 Vector tmp = db.getComboVal(fas);
//                 for (int i = 0; i < tmp.size(); i++) {    
//                       cb.addItem(((Vector)tmp.elementAt(i)).get(0).toString().trim());
//                 }   
//             }
//             
//             db.disConn();
//            } catch(Exception ex) {
//                    System.err.println("Ошибка createTable() "+ ex);
//                    JOptionPane.showMessageDialog(null, ex.getMessage(),  "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
//            } 
//    }


}
