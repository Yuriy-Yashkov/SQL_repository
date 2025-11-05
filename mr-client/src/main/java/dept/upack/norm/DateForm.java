package dept.upack.norm;

/**
 *
 * @author vova
 * @date 11.05.2012
 */

import com.toedter.calendar.JDateChooser;
import common.PanelWihtFone;
import common.ProgressBar;
import common.UtilFunctions;
import dept.MyReportsModule;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DateForm extends JDialog {
    JDialog th;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private PanelWihtFone mainPanel;
    private JDateChooser dateChooser;
    private JDateChooser dateChooser2;
    private String format;
    private ProgressBar pb;

    public DateForm(JFrame parent, boolean modal) {
        super(parent, modal);
        th = this;

        initComponents();
        this.setTitle("Период просмотра");

        add(mainPanel);
        setSize(320, 190);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        Calendar c = Calendar.getInstance();
        format = "dd.MM.yyyy HH:mm:ss";
        final DateFormat df = new java.text.SimpleDateFormat(format);
        final DateFormat df2 = new java.text.SimpleDateFormat("dd.MM HH:mm");

        java.util.Date d = c.getTime();
        d.setHours(0);
        d.setMinutes(0);
        d.setSeconds(0);
        dateChooser = new JDateChooser(d, format);
        dateChooser.setBounds(80, 50, 180, 20);
        mainPanel.add(dateChooser);

        d = c.getTime();
        d.setSeconds(0);
        dateChooser2 = new JDateChooser(d, format);
        dateChooser2.setBounds(80, 80, 180, 20);
        mainPanel.add(dateChooser2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Накопительная ведомость");
        jLabel1.setBounds(20, 20, 300, 20);
        mainPanel.add(jLabel1);

        jLabel2.setText("с");
        jLabel2.setBounds(60, 50, 20, 20);
        mainPanel.add(jLabel2);

        jLabel3.setText("по");
        jLabel3.setBounds(60, 80, 20, 20);
        mainPanel.add(jLabel3);

        jButton1.setText("Печать");
        jButton1.addActionListener(evt -> {
            try {
                if (UtilFunctions.checkDate(df.format(dateChooser.getDate())) && UtilFunctions.checkDate(df.format(dateChooser2.getDate()))) {

                    pb = new ProgressBar(th, false, "Сбор данных");
                    SwingWorker sw = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            NVDB db = new NVDB();
                            try {
                                HashMap params = new HashMap();
                                params.put("pDateStart", df2.format(dateChooser.getDate()));
                                params.put("pDateEnd", df2.format(dateChooser2.getDate()));
                                JasperReport jasperReport = JasperCompileManager.compileReport(MyReportsModule.progPath + "/Templates/JasperReports/" + "normVremNV.jrxml");
                                JasperPrint jasperPrint = JasperFillManager.fillReport(
                                        jasperReport,
                                        params,
                                        new JRResultSetDataSource(db.getItemNV(df.format(dateChooser.getDate()), df.format(dateChooser2.getDate()))));
                                JasperViewer.viewReport(jasperPrint, false);
                            } catch (Exception e) {
                                System.err.println(e);
                            }
                            return 0;
                        }

                        @Override
                        protected void done() {
                            try {
                                pb.dispose();
                            } catch (Exception ex) {
                                System.err.println("Ошибка при получении результатов из фонового потока " + ex);
                            }
                        }
                    };
                    sw.execute();
                    pb.setVisible(true);
                    dispose();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка " + e, "Ошибка", JOptionPane.ERROR_MESSAGE);

            }
        });
        jButton1.setBounds(100, 120, 120, 20);
        mainPanel.add(jButton1);
    }
} 
