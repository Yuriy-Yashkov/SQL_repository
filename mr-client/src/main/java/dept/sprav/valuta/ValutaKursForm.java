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
@SuppressWarnings("serial")
public class ValutaKursForm extends JDialog {
    @SuppressWarnings("rawtypes")
    Vector columns = null;
    @SuppressWarnings("rawtypes")
    Vector rows;
    int x = 10;
    int y = 10;
    JDialog self;
    private PanelWihtFone mainPanel;
    private JLabel lHead;
    private JButton bAdd;
    private JButton bDel;
    private JComboBox<String> cbDate;
    private JTable table;
    private DefaultTableModel tModel;
    private JScrollPane scrollTable;

    public ValutaKursForm(JFrame parent, boolean f) {
        super(parent, f);
        self = this;
        setTitle("Курсы валют");
        initComponents();
        add(mainPanel);
        setSize(400, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initComponents() {
        mainPanel = new PanelWihtFone();

        columns = new Vector();

        columns.add("Валюта(осн)");
        columns.add("Валюта(доп)");
        columns.add("Курс");
        columns.add("Дата действия");
        columns.add("Код курса");

        ValutaPDB db = new ValutaPDB();
        rows = db.getKursList(0);
        db.disConn();

        lHead = new JLabel("Курсы валют:");
        lHead.setBounds(x, y - 5, 150, 20);
        mainPanel.add(lHead);

        // создаём таблицу
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
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
                tModel);
        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(35);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 20, 380, 280);
        mainPanel.add(scrollTable);

        String[] cbVar = {"Текущие", "Прошлые", "Будущие"};
        cbDate = new JComboBox<String>(cbVar);
        cbDate.setSelectedIndex(0);
        cbDate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getKursValut();
            }
        });
        cbDate.setBounds(x + 105, y - 5, 120, 20);
        mainPanel.add(cbDate);

        bAdd = new JButton("Добавить курс");
        bAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new EditValutaKursForm(self, true, 0);
                cbDate.setSelectedItem(cbDate.getSelectedItem().toString());
            }
        });
        bAdd.setBounds(x, y + 310, 150, 30);
        mainPanel.add(bAdd);

        bDel = new JButton("Удалить курс");
        bDel.setEnabled(false);
        bDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    int result = JOptionPane.showConfirmDialog(null,
                            "Удалеление курса валюты! Продолжить ?",
                            "Внимание", javax.swing.JOptionPane.YES_NO_OPTION);
                    if (result == 0) {
                        ValutaPDB db = new ValutaPDB();
                        db.delKursValuta((Long) table.getValueAt(
                                table.getSelectedRow(), 4));
                        getKursValut();
                    }
                }
            }
        });
        bDel.setBounds(x + 230, y + 310, 150, 30);
        mainPanel.add(bDel);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void getKursValut() {
        ValutaPDB db = new ValutaPDB();
        bDel.setEnabled(false);
        if (cbDate.getSelectedItem().toString().equals("Текущие")) {
            rows = db.getKursList(0);
        } else if (cbDate.getSelectedItem().toString()
                .equals("Прошлые")) {
            rows = db.getKursList(1);
            bDel.setEnabled(true);
        } else {
            rows = db.getKursList(2);
        }
        db.disConn();
        if (rows.size() == 0) {
            Vector tmp = new Vector();
            tmp.add("нет");
            tmp.add("данных");
            tmp.add("о");
            tmp.add("курсах");
            tmp.add(-1);
            rows.add(tmp);
        }
        setTableModel();
    }

    private void setTableModel() {
        tModel.setDataVector(rows, columns);
        tModel.fireTableDataChanged();
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(35);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);
    }
}
