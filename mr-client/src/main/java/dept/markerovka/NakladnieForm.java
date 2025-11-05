package dept.markerovka;

import com.toedter.calendar.JDateChooser;
import common.PanelWihtFone;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.util.Calendar;

/**
 *
 * @author vova
 * @date 18.04.2012
 */
public class NakladnieForm extends JDialog {

    final DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
    JDialog self;
    JLabel lHead;
    JLabel lFoot;
    JTable table;
    JLabel lTTN;
    JTextField tfTTN;
    JButton bClose;
    JButton bShow;
    JButton bDescr;
    JButton bPrilojenie;
    JButton bPutList;
    JPanel mainPanel;
    DefaultTableModel tModel;
    JScrollPane scrollTable;
    int x = 10;
    int y = 10;
    Object[] columns = new Object[5];
    Object[][] rows = null;
    String date;
    private JDateChooser dateChooser;
    //  private TableColumn tcol;

    public NakladnieForm(JFrame parent, boolean f) {
        super(parent, f);
        self = this;

        Calendar c = Calendar.getInstance();
        java.util.Date d = c.getTime();
        d.setDate(1);
        dateChooser = new JDateChooser(d);
        dateChooser.setBounds(x + 200, y, 100, 20);
        //UtilFunctions.checkDate(df.format(dateChooser.getDate()))

        MarkerovkaDB db = new MarkerovkaDB();
        rows = db.getNakl(df.format(dateChooser.getDate()));
        columns[0] = "Дата";
        columns[1] = "Номер";
        columns[2] = "Операция";
        columns[3] = "Получатель";
        columns[4] = "Код";

        initComponents();


        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(0);
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(3).setPreferredWidth(240);
        table.getColumnModel().getColumn(4).setPreferredWidth(0);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);

        add(mainPanel);

        setSize(25 + 280 + 4 * 70, 510);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Накладные на отгрузку");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();
        mainPanel.add(dateChooser);

        lHead = new JLabel("Накладные на отгрузку от ");
        lHead.setBounds(x, y, 200, 20);
        mainPanel.add(lHead);

        bShow = new JButton("Показать");
        bShow.setMargin(new Insets(0, 0, 0, 0));
        bShow.setBounds(x + 315, y, 80, 20);
        bShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MarkerovkaDB db = new MarkerovkaDB();
                rows = db.getNakl(df.format(dateChooser.getDate()));
                while (tModel.getRowCount() > 0) tModel.removeRow(0);
                for (int i = 0; i < rows.length; i++) tModel.addRow(rows[i]);
            }
        });
        mainPanel.add(bShow);

        tModel = new DefaultTableModel(rows, columns);

        table = new JTable(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, y + 25, 280 + 4 * 70, 400);
        mainPanel.add(scrollTable);

        bDescr = new JButton("Детали");
        bDescr.setBounds(x + 220, y + 440, 100, 20);
        bDescr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    new NakladnieDescrForm(self, true, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString().trim()));
                    bShow.doClick();
                }
            }
        });
        mainPanel.add(bDescr);

        lTTN = new JLabel("ТТН");
        lTTN.setBounds(50, 440, 40, 20);
        mainPanel.add(lTTN);

        tfTTN = new JTextField(9);
        tfTTN.setBounds(80, 440, 100, 20);
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
