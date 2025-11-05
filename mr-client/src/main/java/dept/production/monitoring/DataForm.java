package dept.production.monitoring;

import com.toedter.calendar.JDateChooser;
import common.PanelWihtFone;
import common.ProgressBar;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DateFormat;
import java.util.Calendar;

/**
 *
 * @author vova
 * @date 31.05.2012
 */
public class DataForm extends JDialog {

    JDialog th;
    JTable table;
    DefaultTableModel tModel;
    Object[][] rows;
    Object[] cols;
    JScrollPane scrollTable;
    int x = 10, y = 10;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private PanelWihtFone mainPanel;

    public DataForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        th = this;

        cols = new Object[2];
        cols[0] = "Код";
        cols[1] = "Название";
        initComponents();
        this.setTitle("Период просмотра");

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        add(mainPanel);
        setSize(340, 350);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();


        final ProgressBar pb = new ProgressBar(th, false, "Получение списка клиентов...");
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                Calendar now = Calendar.getInstance();
                MonitoringDB db = new MonitoringDB();
                rows = db.getClient(now.getTimeInMillis());
                return null;
            }

            @Override
            protected void done() {
                pb.dispose();
            }
        };
        sw.execute();
        pb.setVisible(true);


        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        Calendar c = Calendar.getInstance();
        final DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");


        java.util.Date d = c.getTime();
        d.setDate(1);
        final JDateChooser dateChooser = new JDateChooser(d);
        dateChooser.setBounds(120, 50, 100, 20);
        mainPanel.add(dateChooser);

        final JDateChooser dateChooser2 = new JDateChooser(c.getTime());
        dateChooser2.setBounds(120, 80, 100, 20);
        mainPanel.add(dateChooser2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Данные по пошиву за период:");
        jLabel1.setBounds(60, 20, 300, 20);
        mainPanel.add(jLabel1);

        jLabel2.setText("с");
        jLabel2.setBounds(100, 50, 20, 20);
        mainPanel.add(jLabel2);

        jLabel3.setText("по");
        jLabel3.setBounds(100, 80, 20, 20);
        mainPanel.add(jLabel3);

        tModel = new DefaultTableModel(rows, cols);
        table = new JTable(tModel);

        scrollTable = new JScrollPane(table);
        scrollTable.setBounds(x, 110, 180 + cols.length * 70, 150);
        mainPanel.add(scrollTable);

        jButton1.setText("Показать");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (table.getSelectedRow() > -1)
                        if (UtilFunctions.checkDate(df.format(dateChooser.getDate())) && UtilFunctions.checkDate(df.format(dateChooser2.getDate()))) {
                            new ModelForm(th, false, table.getValueAt(table.getSelectedRow(), 1).toString(), Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()), df.format(dateChooser.getDate()), df.format(dateChooser2.getDate()));
                        }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
        });
        jButton1.setBounds(110, 280, 120, 20);
        mainPanel.add(jButton1);

    }
} 