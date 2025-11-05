package dept.tech.innovation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/*
 *
 * @author norg
 */
public class TechSettingsForm extends JDialog {

    JPanel mainPanel;
    JPanel buttonPanel;
    JPanel tablePanel;

    JTable table;
    DefaultTableModel tModel;
    JScrollPane scrollTable;

    Vector columns = new Vector();
    Vector rows = new Vector();

    //  final private JPanel pnContent =  new JPanel(new GridBagLayout());
    JButton btnAdd;
    JButton btnDel;
    JButton btnSave;
    boolean trFlag = false;

    public TechSettingsForm(JDialog parent, boolean modal) {
        super(parent, modal);
        try {
            ProductionDB db = new ProductionDB();
            rows = db.getItemsSettings();
            db.disConn();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        columns.add("№");
        columns.add("Наименование проекта");

        init();

        table.setRowHeight(30);


        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(550);


        add(mainPanel);
        setTitle("Настройки");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(parent);
    }

    public TechSettingsForm(JDialog parent, boolean modal, boolean trade) {
        super(parent, modal);
        trFlag = trade;
        try {
            ProductionDB db = new ProductionDB();
            rows = db.getIItemsSettings();
            db.disConn();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        columns.add("№");
        columns.add("Наименование проекта");

        init();

        table.setRowHeight(30);


        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(550);


        add(mainPanel);
        setTitle("Настройки");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(parent);
    }

    public void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        tablePanel = new JPanel();

        btnAdd = new JButton("Добавить");
        btnAdd.setPreferredSize(new Dimension(120, 20));
        btnAdd.setMinimumSize(btnAdd.getPreferredSize());
        btnDel = new JButton("Удалить");
        btnDel.setPreferredSize(new Dimension(120, 20));
        btnDel.setMinimumSize(btnDel.getPreferredSize());
        btnSave = new JButton("Сохранить");
        btnSave.setPreferredSize(new Dimension(120, 20));
        btnSave.setMinimumSize(btnSave.getPreferredSize());

        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Vector tmp = new Vector();
                tmp.add(numRowTable());
                tmp.add("");
                tModel.insertRow(table.getRowCount(), tmp);

            }
        });

        btnDel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (table.getSelectedRow() != -1) {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (Integer.valueOf(table.getValueAt(i, 0).toString()) == Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString())) {
                                tModel.removeRow(i);
                                break;
                            }
                        }
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (i > 0) {
                                if ((Integer.valueOf(table.getValueAt(i, 0).toString()) - Integer.valueOf(table.getValueAt(i - 1, 0).toString())) > 1) {
                                    int num = i;
                                    for (int j = num; j < table.getRowCount(); j++) {
                                        table.setValueAt(Integer.valueOf(table.getValueAt(j, 0).toString()) - 1, j, 0);
                                    }
                                    break;
                                }
                            }
                            if (i == 0) {
                                if (!Integer.valueOf(table.getValueAt(i, 0).toString()).equals(1)) {
                                    for (int j = i; j < table.getRowCount(); j++) {
                                        table.setValueAt(Integer.valueOf(table.getValueAt(j, 0).toString()) - 1, j, 0);
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Вы не выбрали строку для удаления!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    ProductionDB db = new ProductionDB();
                    Vector tmp = new Vector();
                    for (int i = 0; i < table.getRowCount(); i++) {
                        Vector temp = new Vector();
                        temp.add(Integer.valueOf(table.getValueAt(i, 0).toString()));
                        temp.add(String.valueOf(table.getValueAt(i, 1).toString().trim()));
                        tmp.add(temp);
                    }
                    if (!trFlag) {
                        db.checkItemsSettings(tmp);
                    } else {
                        db.checkIItemsSettings(tmp);
                    }
                    db.disConn();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //создаём таблицу

        tModel = new DefaultTableModel(rows, columns) {
            @Override
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };

        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);


        scrollTable = new JScrollPane(table);


        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDel);
        buttonPanel.add(btnSave);

        mainPanel.add(scrollTable, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private int numRowTable() {
        try {
            if (table.getSelectedRow() == -1) {
                int num = (table.getRowCount() == 1) ? Integer.valueOf(table.getValueAt(0, 0).toString()) : 0;
                for (int i = 1; i < table.getRowCount(); i++) {
                    num = Integer.valueOf(table.getValueAt(i - 1, 0).toString()) + 1;
                    table.setValueAt(num, i, 0);
                }
                return num + 1;
            } else {
                return table.getRowCount() + 1;
            }
        } catch (Exception e) {
            return 0;
        }
    }


}
