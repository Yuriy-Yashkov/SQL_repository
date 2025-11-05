package dept.sprav.valuta;

import common.PanelWihtFone;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 *
 * @author vova
 */
public class ValutaForm extends JDialog {

    Vector columns = null;
    Vector rows;
    int x = 10;
    int y = 10;
    JDialog self;
    private PanelWihtFone mainPanel;
    private JLabel lHead;
    private JButton bAdd;
    private JButton bDel;
    private JButton bEdit;
    private JTable table;
    private DefaultTableModel tModel;
    private JScrollPane scrollTable;

    public ValutaForm(JFrame parent, boolean f) {
        super(parent, f);
        self = this;
        setTitle("Валюты");

        initComponents();

        add(mainPanel);
        setSize(630, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        columns = new Vector();

        columns.add("№");
        columns.add("Имя");
        columns.add("Наименование");
        columns.add("Обозначение");
        columns.add("Описание");
        columns.add("Код валюты");

        ValutaPDB db = new ValutaPDB();
        rows = db.getValutaList();
        db.disConn();

        lHead = new JLabel("Список валют:");
        lHead.setBounds(x, y, 150, 20);
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
        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(0).setPreferredWidth(15);
        table.getColumnModel().getColumn(1).setPreferredWidth(35);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 20, 280 + columns.size() * 50, 280);
        mainPanel.add(scrollTable);

        bDel = new JButton("Удалить");
        bDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Удалить? Все курсы с этой валютой будут удалены", "Подтверждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (table.getSelectedRow() >= 0) {
                        ValutaPDB db = new ValutaPDB();
                        db.delValuta(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 5).toString()));
                        db.disConn();
                    } else
                        JOptionPane.showMessageDialog(null, "Выбирете валюту", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    setTableModel();
                }
            }
        });
        bDel.setBounds(x, y + 310, 120, 25);
        mainPanel.add(bDel);

        bEdit = new JButton("Изменить");
        bEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() >= 0) {
                    new EditValutaForm(self, true, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 5).toString()));
                } else JOptionPane.showMessageDialog(null, "Выбирете валюту", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                setTableModel();
            }
        });
        bEdit.setBounds(x + 140, y + 310, 120, 25);
        mainPanel.add(bEdit);

        bAdd = new JButton("Добавить");
        bAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new EditValutaForm(self, true, 0);
                setTableModel();
            }
        });
        bAdd.setBounds(x + 280, y + 310, 120, 25);
        mainPanel.add(bAdd);
    }

    private void setTableModel() {
        ValutaPDB db = new ValutaPDB();
        rows = db.getValutaList();
        db.disConn();
        tModel.setDataVector(rows, columns);
        tModel.fireTableDataChanged();
        table.getColumnModel().getColumn(0).setPreferredWidth(15);
        table.getColumnModel().getColumn(1).setPreferredWidth(35);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
    }
}