package dept.upack.ns;

import common.PanelWihtFone;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author vova
 * @date 16.11.2011
 */
public class ItemListForm extends JDialog {

    JLabel lHead;
    JLabel lFoot;
    JTable table;
    JLabel lTTN;
    JTextField tfTTN;
    JButton bClose;
    JButton bPrint;
    JPanel mainPanel;
    JComboBox cbSkidka;
    DefaultTableModel tModel;
    JScrollPane scrollTable;
    int x = 10;
    int y = 10;
    Vector columns = new Vector();
    Vector rows = new Vector();
    String sd;
    String ed;
    JDialog temp;
    ArrayList<String> skidkaType = new ArrayList<String>();
    private TableColumn tcol;

    public ItemListForm(JDialog parent, boolean f, String sd, String ed) {
        super(parent, f);
        temp = this;
        this.sd = sd;
        this.ed = ed;

        try {
            UpackNSDB db = new UpackNSDB();
            rows = db.getItemList(sd, ed);
            skidkaType = db.getSkidkaList();
            db.disConn();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        columns.add("Наименование");
        columns.add("Модель");
        columns.add("Артикул");
        columns.add("Сорт");
        columns.add("Рост");
        columns.add("Размер");
        columns.add("№ МЛиста");
        columns.add("Код МЛ");
        columns.add("Кол-во");
        columns.add("Бригада");
        columns.add("Скидка");

        initComponents();

        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setPreferredWidth(40);
        table.getColumnModel().getColumn(6).setPreferredWidth(55);
        table.getColumnModel().getColumn(7).setPreferredWidth(0);
        table.getColumnModel().getColumn(7).setWidth(0);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(8).setPreferredWidth(30);
        table.getColumnModel().getColumn(9).setPreferredWidth(50);
        table.getColumnModel().getColumn(10).setPreferredWidth(40);

        add(mainPanel);

        setSize(20 + columns.size() * 70, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Принятые изделия");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        lHead = new JLabel("Принятые изделия от " + sd + " по " + ed);
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
        for (int i = 1; i < table.getColumnCount(); i++) {
            tcol = table.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new CustomTableCellRenderer2());
        }

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 25, columns.size() * 70, 380);
        mainPanel.add(scrollTable);

        bClose = new JButton("Закрыть");
        bClose.setBounds((20 + columns.size() * 70) / 2 - 60, 450, 120, 20);
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mainPanel.add(bClose);


        bPrint = new JButton("Отчёт");
        bPrint.setBounds((20 + columns.size() * 70) - 200, 425, 120, 20);
        bPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Vector<Vector> filterVector = new Vector<Vector>();
                    for (int i = 0; i < table.getRowCount(); i++) {
                        filterVector.add((Vector) rows.get(table.convertRowIndexToModel(i)));
                    }
                    UpackNSOO oo = new UpackNSOO("Учёт н/с продукции с " + sd + " по " + ed + " (" + cbSkidka.getSelectedItem().toString().trim() + ")", filterVector, null);
                    oo.createReport("УчётНС.ots");
                    JOptionPane.showMessageDialog(null, "Отчёт сформирован", "Отчёт сформирован", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(bPrint);

        cbSkidka = new JComboBox(skidkaType.toArray());
        cbSkidka.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String val = cbSkidka.getSelectedItem().toString().trim();
                if ("Все".equals(val)) {
                    sorter.setRowFilter(null);
                } else sorter.setRowFilter(RowFilter.regexFilter(val, 10));
            }
        });
        cbSkidka.setBounds((20 + columns.size() * 70) - 70, 425, 60, 20);
        mainPanel.add(cbSkidka);

        lTTN = new JLabel("Артикул");
        lTTN.setBounds(50, 425, 80, 20);
        mainPanel.add(lTTN);

        tfTTN = new JTextField(9);
        tfTTN.setBounds(135, 425, 80, 20);
        tfTTN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sorter.setRowFilter(RowFilter.regexFilter(tfTTN.getText(), 2));
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tfTTN.setText("");
                    sorter.setRowFilter(null);
                }
            }
        });
        mainPanel.add(tfTTN);
    }
}
