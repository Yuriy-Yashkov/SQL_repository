package dept.upack.ns;

import common.PanelWihtFone;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author vova
 * @date 16.11.2011
 */

class CustomTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(SwingConstants.CENTER);
        if (isSelected) c.setBackground(Color.LIGHT_GRAY);
        else if ("Новый".equals((table.getValueAt(row, 5)).toString())) {
            c.setBackground(Color.YELLOW);
        } else if ("Отказ".equals(table.getValueAt(row, 5))) {
            c.setBackground(Color.PINK);
        } else if ("--//--".equals(table.getValueAt(row, 5).toString().trim())) {
            c.setBackground(Color.BLUE);
        } else if ("Принят".equals(((table.getValueAt(row, 5)).toString()))) {
            c.setBackground(Color.white);
        }
        return c;
    }
}

public class MarhListForm extends JDialog {

    JLabel lHead;
    JLabel lFoot;
    JTable table;
    JLabel lTTN;
    JTextField tfTTN;
    JButton bClose;
    JButton bDescr;
    JButton bComf;
    JButton bRef;
    JPanel mainPanel;
    DefaultTableModel tModel;
    JScrollPane scrollTable;
    int x = 10;
    int y = 10;
    Vector columns = new Vector();
    Vector rows = new Vector();
    String sd;
    String ed;
    JDialog temp;
    private TableColumn tcol;


    public MarhListForm(JDialog parent, boolean f, String sd, String ed) {
        super(parent, f);
        temp = this;
        this.sd = sd;
        this.ed = ed;

        try {
            UpackNSDB db = new UpackNSDB();
            rows = db.getMarhList(sd, ed);
            db.disConn();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        columns.add("№ МЛиста");
        columns.add("Код МЛ");
        columns.add("Кол-во");
        columns.add("Бригада");
        columns.add("Дата");
        columns.add("Статус");

        initComponents();

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);

        add(mainPanel);

        setSize(20 + columns.size() * 70, 530);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Маршрутные листы н/с");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        lHead = new JLabel("Маршрутные листы от " + sd + " по " + ed);
        lHead.setBounds(x + 20, y, 350, 20);
        mainPanel.add(lHead);

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
        tModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {

            }
        });

        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        for (int i = 0; i < table.getColumnCount(); i++) {
            tcol = table.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new CustomTableCellRenderer());
        }

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 25, columns.size() * 70, 380);
        mainPanel.add(scrollTable);

        bDescr = new JButton("?");
        bDescr.setBounds(265, 425, 50, 20);
        bDescr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    new MarhListDescrForm(temp, true, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 1).toString()));
                }
            }
        });
        mainPanel.add(bDescr);

        bComf = new JButton("+");
        bComf.setBounds(155, 425, 50, 20);
        bComf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    try {
                        UpackNSDB db = new UpackNSDB();
                        if (db.confirmML(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 1).toString()))) {
                            JOptionPane.showMessageDialog(null, "Маршрут принят", "Успешно", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }
                        rows = db.getMarhList(sd, ed);
                        db.disConn();

                        while (tModel.getRowCount() > 1) tModel.removeRow(1);
                        for (int i = 0; i < rows.size(); i++) {
                            tModel.addRow(new Vector((Vector) rows.get(i)));
                        }
                    } catch (Exception ex) {
                        System.err.println("Ошибка: " + ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        mainPanel.add(bComf);

        bRef = new JButton("-");
        bRef.setBounds(210, 425, 50, 20);
        bRef.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    try {
                        UpackNSDB db = new UpackNSDB();
                        if (db.refusalML(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 1).toString()))) {
                            JOptionPane.showMessageDialog(null, "Отказ от маршрута", "Успешно", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }
                        rows = db.getMarhList(sd, ed);
                        db.disConn();

                        while (tModel.getRowCount() > 1) tModel.removeRow(1);
                        for (int i = 0; i < rows.size(); i++) {
                            tModel.addRow(new Vector((Vector) rows.get(i)));
                        }
                    } catch (Exception ex) {
                        System.err.println("Ошибка: " + e);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        mainPanel.add(bRef);

        bClose = new JButton("Закрыть");
        bClose.setBounds((20 + columns.size() * 70) / 2 - 60, 460, 120, 20);
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mainPanel.add(bClose);

        lTTN = new JLabel("Код");
        lTTN.setBounds(50, 425, 30, 20);
        mainPanel.add(lTTN);

        tfTTN = new JTextField(9);
        tfTTN.setBounds(80, 425, 60, 20);
        tfTTN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sorter.setRowFilter(RowFilter.regexFilter(tfTTN.getText(), 1));
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tfTTN.setText("");
                    sorter.setRowFilter(null);
                }
            }
        });
        mainPanel.add(tfTTN);
    }
}
