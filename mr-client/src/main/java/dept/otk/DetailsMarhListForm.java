package dept.otk;

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
public class DetailsMarhListForm extends JDialog {
    Vector columns = null;
    Vector rows;
    Vector t2columns = null;
    Vector t2rows;
    int x = 10;
    int y = 10;
    int sum = 0;
    int sumP = 0;
    private JPanel mainPanel;
    private JTable table;
    private JTable tablePlan;
    private JLabel lHead;
    private JLabel lPlan;
    private JLabel lsum;
    private JLabel lsumP;
    private DefaultTableModel tModel;
    private DefaultTableModel tModel2;
    private JScrollPane scrollTable;
    private JScrollPane scrollTable2;
    private JButton bClose;


    public DetailsMarhListForm(JDialog parent, boolean f, int kod_marh, String nomer_marh, String sd, String ed) {
        super(parent, f);
        OtkDB db = new OtkDB();
        rows = db.getMarhListDetails(kod_marh, sd, ed);
        t2rows = db.getMarhListDetailsPlan(kod_marh);
        initComponents();
        //lHead.setText(str);
        setTitle("Маршрут №" + nomer_marh);
        add(mainPanel);
        setSize(310 + columns.size() * 50, 500);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        columns = new Vector();

        columns.add("№");
        columns.add("Модель");
        columns.add("Артикул");
        columns.add("Название");
        columns.add("Цвет");
        columns.add("Размер");
        columns.add("Сорт");
        columns.add("Кол-во");

        t2columns = new Vector();
        t2columns.add("№");
        t2columns.add("Модель");
        t2columns.add("Название");
        t2columns.add("Размер");
        t2columns.add("Кол-во");

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
        for (int i = 0; i < tModel.getRowCount(); i++) sum += Integer.parseInt(tModel.getValueAt(i, 7).toString());

        tModel2 = new DefaultTableModel(t2rows, t2columns) {
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
        for (int i = 0; i < tModel2.getRowCount(); i++) sumP += Integer.parseInt(tModel2.getValueAt(i, 4).toString());


        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(0).setPreferredWidth(15);
        table.getColumnModel().getColumn(1).setPreferredWidth(35);
        table.getColumnModel().getColumn(2).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(30);

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 20, 280 + columns.size() * 50, 180);
        mainPanel.add(scrollTable);


        tablePlan = new JTable(tModel2);
        tablePlan.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablePlan.getColumnModel().getColumn(1).setPreferredWidth(50);
        tablePlan.getColumnModel().getColumn(2).setPreferredWidth(160);
        scrollTable2 = new JScrollPane(tablePlan);
        scrollTable2.setBounds(x, y + 220, 280 + columns.size() * 50, 180);
        mainPanel.add(scrollTable2);

        lHead = new JLabel("Пошив:");
        lHead.setBounds(x, y, 200, 20);
        mainPanel.add(lHead);
        lsum = new JLabel("" + sum);
        lsum.setBounds(x + 630, y, 80, 20);
        mainPanel.add(lsum);

        lPlan = new JLabel("План:");
        lPlan.setBounds(x, y + 200, 200, 20);
        mainPanel.add(lPlan);
        lsumP = new JLabel("" + sumP);
        lsumP.setBounds(x + 630, y + 200, 80, 20);
        mainPanel.add(lsumP);

        bClose = new JButton("Закрыть");
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bClose.setBounds((310 + columns.size() * 50) / 2 - 60, y + 430, 120, 20);
        mainPanel.add(bClose);
    }
}
