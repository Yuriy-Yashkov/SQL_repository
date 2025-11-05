package dept.marketing;

import by.march8.ecs.framework.common.Settings;
import common.PanelWihtFone;
import lombok.SneakyThrows;
import workDB.DB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author vova
 */
@SuppressWarnings("all")
class CustomTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY);
        } else if (((table.getValueAt(row, 8)).toString()).equals("Формируется")) {
            if ((((table.getValueAt(row, 10)).toString()).equals("true"))) {
                c.setBackground(Color.YELLOW);
            } else {
                c.setBackground(Settings.COLOR_RECALCULATE);
            }
        } else if ((table.getValueAt(row, 8)).equals("Удалён")) {
            c.setBackground(Color.PINK);
        } else if ((table.getValueAt(row, 8).toString().trim()).equals("Непонятно =)")) {
            c.setBackground(Color.BLUE);
        } else if (((table.getValueAt(row, 8)).toString()).equals("Закрыт")) {
            c.setBackground(Color.white);
        }
        return c;
    }
}

@SuppressWarnings("all")
public class Nakladnie extends JDialog {

    JLabel lHead;
    JLabel lFoot = new JLabel();
    JTable table;
    JButton bClose;
    JButton bShow;
    JButton bDescr;
    JPanel mainPanel;
    DefaultTableModel tModel;
    JScrollPane scrollTable;
    int x = 10;
    int y = 10;
    Vector columns = new Vector();
    Vector rows = new Vector();
    MaskFormatter formatter;
    JFormattedTextField ftDate;
    String date;
    JDialog temp;
    private TableColumn tcol;
    private JLabel lKod;
    private JTextField tfKod;

    @SneakyThrows
    public Nakladnie(JFrame parent, boolean f) {
        super(parent, f);
        temp = this;

        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.MONTH) + 1;
        String month = new String();
        if (i < 10) {
            month = "0" + i;
        } else {
            month = Integer.toString(i);
        }
        date = new String("01." + month + "." + c.get(Calendar.YEAR));
        DB db = new DB();
        rows = db.getNakl(date);
        columns.add("Дата");
        columns.add("Номер");
        columns.add("Операция");
        columns.add("Код пол.");
        columns.add("Получатель");
        columns.add("Сумма без НДС");
        columns.add("НДС");
        columns.add("Сумма с НДС");
        columns.add("Статус");
        columns.add("№ Заявки");
        columns.add("Расчет");

        initComponents();

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        //table.getColumnModel().getColumn(5).setPreferredWidth(40);
        //table.getColumnModel().getColumn(6).setPreferredWidth(40);
        //table.getColumnModel().getColumn(7).setPreferredWidth(40);
        //table.getColumnModel().getColumn(8).setPreferredWidth(40);
        table.getColumnModel().getColumn(10).setPreferredWidth(0);
        table.getColumnModel().getColumn(10).setMinWidth(0);
        table.getColumnModel().getColumn(10).setMaxWidth(0);

        add(mainPanel);

        setSize(25 + 280 + columns.size() * 70, 540);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Накладные на отгрузку");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        lHead = new JLabel("Накладные на отгрузку c ");
        lHead.setBounds(x + 100, y, 200, 20);
        mainPanel.add(lHead);

        ftDate = new javax.swing.JFormattedTextField(formatter);
        ftDate.setText(date);
        ftDate.setBounds(x + 300, y, 80, 20);
        mainPanel.add(ftDate);

        bShow = new JButton("Показать");
        bShow.setBounds(x + 385, y, 120, 20);
        bShow.addActionListener(new ActionListener() {
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                DB db = new DB();
                rows = db.getNakl(ftDate.getValue().toString());
                while (tModel.getRowCount() > 0) {
                    tModel.removeRow(0);
                }
                for (int i = 0; i < rows.size(); i++) {
                    tModel.addRow((Vector) rows.get(i));
                }
            }
        });
        mainPanel.add(bShow);

        lFoot.setBounds(x, y + 430, 500, 20);
        mainPanel.add(lFoot);

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
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() != -1) {
                    DB db = new DB();
                    Integer count[] = db.getNaklDescr((String) table.getValueAt(table.getSelectedRow(), 1));
                    lFoot.setText("Всего едениц: " + count[0] + "   В упаковках: " + (count[0] - count[2]) + "   Россыпью: " + count[2] + "   Упаковок:" + count[1]);
                }
            }
        });

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 25, 280 + columns.size() * 70, 400);
        mainPanel.add(scrollTable);


        bDescr = new JButton("Детали");
        bDescr.setBounds(x + 600, y + 430, 120, 20);
        bDescr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    //new NakladnieDescr(temp, true, lFoot.getText(), (String)table.getValueAt(table.getSelectedRow(), 1));
                    DB db = new DB();
                    OpenOffice oo = new OpenOffice("Накладная на отгрузку №" + (String) table.getValueAt(table.getSelectedRow(), 1), db.getNaklAllDescr((String) table.getValueAt(table.getSelectedRow(), 1), 0));
                    oo.createReport("NakladnieDescr.ots");
                }
            }
        });
        mainPanel.add(bDescr);

        bClose = new JButton("Закрыть");
        bClose.setBounds((25 + 280 + columns.size() * 70) / 2 - 60, 470, 120, 20);
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mainPanel.add(bClose);

        lKod = new JLabel("Код:");
        lKod.setBounds(45, 470, 40, 20);
        mainPanel.add(lKod);

        tfKod = new JTextField(9);
        tfKod.setBounds(80, 470, 100, 20);
        tfKod.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER) {
                    sorter.setRowFilter(RowFilter.regexFilter(tfKod.getText(), 3));
                } else if (evt.getKeyCode() == evt.VK_ESCAPE) {
                    tfKod.setText("");
                    sorter.setRowFilter(null);
                }
            }
        });
        mainPanel.add(tfKod);
    }
}
