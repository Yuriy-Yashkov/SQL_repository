/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.tech.innovation;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import com.jhlabs.awt.ParagraphLayout;
import common.CheckBoxHeader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


/**
 * @author user
 */
@SuppressWarnings("all")
public class TechTradeForm extends JDialog {

    public static JPanel headPanel;
    public static JPanel buttPanel;
    public static JButton btOk;
    public static JButton btAdd;
    public static JButton btDel;
    public static JButton btPrint;
    public static JTable table;
    Vector rows = new Vector();
    Vector cols = new Vector();
    DefaultTableModel tModel;
    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;
    boolean del_flag = false;
    boolean t_flag = false;
    private UCDatePicker stDate;
    private UCDatePicker enDate;


    public TechTradeForm(boolean tech) {
        t_flag = tech;
        if (tech) {
            loadTechItems();
        } else {
            loadTradeItems();
        }

        initComponents();
        setComponents();
        createTable();


    }

    private void loadTechItems() {
        try {
            ProductionDB db = new ProductionDB();
            rows = db.getTechIItemsList();
            db.disConn();
        } catch (Exception e) {
            System.err.println("Ошибка loadTechItems() " + e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTradeItems() {
        try {
            ProductionDB db = new ProductionDB();
            rows = db.getTradeIItemsList();
            db.disConn();
        } catch (Exception e) {
            System.err.println("Ошибка loadTradeItems() " + e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        headPanel = new JPanel();
        buttPanel = new JPanel();

        stDate = new UCDatePicker(DateUtils.getDateNow());
        stDate.setPreferredSize(new Dimension(110, 20));
        enDate = new UCDatePicker(DateUtils.getDateNow());
        enDate.setPreferredSize(new Dimension(110, 20));
        btOk = new JButton("OK");

        //stDate.setDate(new Date());
        //enDate.setDate(new Date());

        table = new JTable();

        btAdd = new JButton("Добавить");
        btDel = new JButton("Удалить");
        btPrint = new JButton("Печать");
    }

    private void setComponents() {
        headPanel.setLayout(new ParagraphLayout());
        headPanel.setBorder(BorderFactory.createTitledBorder("Продукция за период"));

        buttPanel.setLayout(new GridLayout(1, 4));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        headPanel.add(new JLabel(" с "));
        headPanel.add(stDate);
        headPanel.add(new JLabel(" по "));
        headPanel.add(enDate);

        buttPanel.add(btAdd);
        buttPanel.add(btDel);
        buttPanel.add(btPrint);
    }

    private void createTable() {

        cols.add("");
        cols.add("Наименование");
        cols.add("Модель");
        //   cols.add("Артикул");
        cols.add("Дата");
        //   cols.add("Код");
        cols.add("К.П.");

        tModel = new DefaultTableModel(rows, cols) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rows.isEmpty()) {
                    return super.getClass();
                } else {
                    return getValueAt(0, col).getClass();
                }
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                boolean flag = true;
                if (col != 4) {
                    if (col != 0) {
                        flag = true;
                    } else {
                        flag = true;
                    }
                }
                return flag;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(55);
        table.getColumnModel().getColumn(3).setPreferredWidth(75);
        //  table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(10);
        //  table.getColumnModel().getColumn(5).setWidth(0);
        //  table.getColumnModel().getColumn(5).setMinWidth(0);
        //  table.getColumnModel().getColumn(5).setMaxWidth(0);
        //    table.getColumnModel().getColumn(6).setPreferredWidth(20);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);

//        TableColumn gradeColumn = table.getColumnModel().getColumn(4);
//        final JComboBox comboBox = new JComboBox();
//        gradeColumn.setCellEditor(new DefaultCellEditor(comboBox));
//        
//        setValueAtComboBoxInTable(comboBox);
//        
//        for (int i = 0; i < rows.size(); i++){
//           int val = Integer.parseInt(((Vector)rows.elementAt(i)).get(4).toString());
//           for(int j = 0; j < tModel.getRowCount(); j++){
//               if (i == j) {
//                 tModel.setValueAt(val, j, 4);
//               }
//           }    
//        }  

    }

//        public void setValueAtComboBoxInTable(JComboBox comboBox) {
//         try {
//             ProductionDB db = new ProductionDB();
//             Vector tmp = new Vector();
//             if(t_flag) {
//                 tmp = db.getSettingKod();
//             }
//             else {
//                 tmp = db.getTSettingKod();
//             }
//             for (int i = 0; i < tmp.size(); i++) {    
//                  comboBox.addItem(Integer.parseInt(((Vector)tmp.elementAt(i)).get(0).toString()));
//             }
//             db.disConn();
//            } catch(Exception ex) {
//                    System.err.println("Ошибка createTable() "+ ex);
//                    JOptionPane.showMessageDialog(null, ex.getMessage(),  "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
//            } 
//    }

    public void setTechComponentsEventModel() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableValueChanged(e);
            }
        });

        tModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                tModelTableChanged(e);
            }
        });

//        tModel.addTableModelListener(new TableModelListener() {
//            public void tableChanged(TableModelEvent e) {
//                tModelComboChanged(e);
//            }
//        });

        btAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btAddactionPerformed(e);
            }
        });

        btDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btDelactionPerformed(e);
            }
        });

        btPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btPrintactionPerformed(e);
            }
        });
    }

    public void setTradeComponentsEventModel() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableValueChanged(e);
            }
        });

        tModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                tModelTableChanged(e);
            }
        });

//        tModel.addTableModelListener(new TableModelListener() {
//            public void tableChanged(TableModelEvent e) {
//                tTModelComboChanged(e);
//            }
//        });

        btAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btTAddactionPerformed(e);
            }
        });

        btDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btTDelactionPerformed(e);
            }
        });

        btPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btTPrintactionPerformed(e);
            }
        });
    }

    public void tableValueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
        maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
    }

    public void tModelTableChanged(TableModelEvent e) {
        if (tableModelListenerIsChanging) {
            return;
        }
        int firstRow = e.getFirstRow();
        int column = e.getColumn();
        if (column != 0 || maxSelectedRow == -1 || minSelectedRow == -1) {
            return;
        }
        tableModelListenerIsChanging = true;
        boolean value = ((Boolean) tModel.getValueAt(firstRow, column)).booleanValue();
        for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
            tModel.setValueAt(Boolean.valueOf(value), table.convertRowIndexToModel(i), column);
        }
        minSelectedRow = -1;
        maxSelectedRow = -1;
        tableModelListenerIsChanging = false;
    }

//    public void tModelComboChanged(TableModelEvent e) {
//        if (del_flag) {
//            return;
//        } 
//        table.getSelectedRow();
//        
//        if(table.getSelectedRow() != -1) {
//                   try {
//                        ProductionDB db = new ProductionDB();
//                        String ngpr = table.getValueAt(table.getSelectedRow(), 1).toString().trim();
//                        int fas = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString());
//                        int kod_naim = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString());
//                        db.updateTech(fas, kod_naim, ngpr);
//                        db.disConn();
//                    } catch (Exception ex) {
//                         System.err.println("Ошибка tModelComboChanged(TableModelEvent e) "+ ex);
//                         JOptionPane.showMessageDialog(null, ex.getMessage(),  "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
//                    }
//             }
//        del_flag = false;
//    }  
//    
//    public void tTModelComboChanged(TableModelEvent e) {
//        if (del_flag) {
//            return;
//        } 
//        if(table.getSelectedRow() != -1) {
//                   try {
//                        ProductionDB db = new ProductionDB();
//                        String ngpr = table.getValueAt(table.getSelectedRow(), 1).toString().trim();
//                        int fas = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString());
//                        int kod_naim = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString());
//                        db.updateTrade(fas, kod_naim, ngpr);
//                        db.disConn();
//                    } catch (Exception ex) {
//                         System.err.println("Ошибка tModelComboChanged(TableModelEvent e) "+ ex);
//                         JOptionPane.showMessageDialog(null, ex.getMessage(),  "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
//                    }
//             }
//        del_flag = false;
//    }

    public void btDelactionPerformed(ActionEvent e) {
        int marked_for_del = 0;
        for (int i = 0; i < tModel.getRowCount(); i++) {
            boolean checkBoxVal = ((Boolean) tModel.getValueAt(i, 0)).booleanValue();
            if (checkBoxVal) {
                marked_for_del += 1;
                int fas = Integer.parseInt((tModel.getValueAt(i, 2)).toString().trim());
                String ngpr = tModel.getValueAt(i, 1).toString().trim();
                try {
                    ProductionDB db = new ProductionDB();
                    db.delItems(fas, ngpr);
                    rows = db.getTechIItemsList();
                    db.disConn();
                } catch (Exception ex) {
                    System.err.println("Ошибка btDelActionPerformed(ActionEvent e) " + ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (marked_for_del > 0) {
            del_flag = true;
            while (tModel.getRowCount() > 0) {
                tModel.removeRow(0);
            }
            for (int j = 0; j < rows.size(); j++) {
                tModel.addRow(new Vector((Vector) rows.get(j)));
            }
        }

    }

    public void btTDelactionPerformed(ActionEvent e) {
        int marked_for_del = 0;
        for (int i = 0; i < tModel.getRowCount(); i++) {
            boolean checkBoxVal = ((Boolean) tModel.getValueAt(i, 0)).booleanValue();
            if (checkBoxVal) {
                marked_for_del += 1;
                int fas = Integer.parseInt((tModel.getValueAt(i, 2)).toString().trim());
                String ngpr = tModel.getValueAt(i, 1).toString().trim();
                try {
                    ProductionDB db = new ProductionDB();
                    db.delTItems(fas, ngpr);
                    rows = db.getTradeIItemsList();
                    db.disConn();
                } catch (Exception ex) {
                    System.err.println("Ошибка btDelActionPerformed(ActionEvent e) " + ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (marked_for_del > 0) {
            del_flag = true;
            while (tModel.getRowCount() > 0) {
                tModel.removeRow(0);
            }
            for (int j = 0; j < rows.size(); j++) {
                tModel.addRow(new Vector((Vector) rows.get(j)));
            }
        }

    }

    public void btAddactionPerformed(ActionEvent e) {
        new AddProduction(this, true);
        ProductionDB db = new ProductionDB();
        try {
            rows = db.getTechIItemsList();
        } catch (Exception ex) {
            System.err.println("Ошибка bAddActionPerformed(ActionEvent e) " + ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        db.disConn();
        while (tModel.getRowCount() > 0) {
            tModel.removeRow(0);
        }
        for (int i = 0; i < rows.size(); i++) {
            tModel.addRow(new Vector((Vector) rows.get(i)));
        }
    }

    public void btTAddactionPerformed(ActionEvent e) {
        new AddProduction(this, true, true);
        ProductionDB db = new ProductionDB();
        try {
            rows = db.getTradeIItemsList();
        } catch (Exception ex) {
            System.err.println("Ошибка bAddActionPerformed(ActionEvent e) " + ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        db.disConn();
        while (tModel.getRowCount() > 0) {
            tModel.removeRow(0);
        }
        for (int i = 0; i < rows.size(); i++) {
            tModel.addRow(new Vector((Vector) rows.get(i)));
        }
    }

    public void btPrintactionPerformed(ActionEvent e) {
        DatePeriod period = new DatePeriod();
        System.err.println(DateUtils.getNormalDateFormat(stDate.getDate()) + " - " + DateUtils.getNormalDateFormat(enDate.getDate()));
        period.setBegin(stDate.getDate());
        period.setEnd(enDate.getDate());
        new DateForm(this, true, period);
        ProductionDB db = new ProductionDB();
        try {
            rows = db.getTechIItemsList();
        } catch (Exception ex) {
            System.err.println("Ошибка bPrintActionPerformed(ActionEvent e) " + ex);
            // log.error("Ошибка bPrintActionPerformed(ActionEvent e)", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        db.disConn();
        while (tModel.getRowCount() > 0) {
            tModel.removeRow(0);
        }
        for (int i = 0; i < rows.size(); i++) {
            tModel.addRow(new Vector((Vector) rows.get(i)));
        }
    }

    public void btTPrintactionPerformed(ActionEvent e) {
        new DateForm(this, true, true);
        ProductionDB db = new ProductionDB();
        try {
            rows = db.getTradeIItemsList();
        } catch (Exception ex) {
            System.err.println("Ошибка bPrintActionPerformed(ActionEvent e) " + ex);
            // log.error("Ошибка bPrintActionPerformed(ActionEvent e)", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        db.disConn();
        while (tModel.getRowCount() > 0) {
            tModel.removeRow(0);
        }
        for (int i = 0; i < rows.size(); i++) {
            tModel.addRow(new Vector((Vector) rows.get(i)));
        }
    }

}
