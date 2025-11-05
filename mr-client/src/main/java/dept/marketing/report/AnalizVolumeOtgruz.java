package dept.marketing.report;

import com.toedter.calendar.JDateChooser;
import common.ProgressBar;
import common.UtilFunctions;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class AnalizVolumeOtgruz extends javax.swing.JDialog {
    JDateChooser sDate;
    JDateChooser eDate;

    Vector kt;
    Vector rezaltData;

    ProgressBar pb;
    ReportPDB pdb;
    ReportDB db;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    public AnalizVolumeOtgruz(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Анализ объема отгрузок");
        initComponents();
        init();

        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Задайте параметры для отчёта");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Период c");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 122, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 13, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel3.setText("по");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 133, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 14, Short.MAX_VALUE)
        );

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setText("По моделям;");

        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setText("По группам изделий;");

        jCheckBox1.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jCheckBox1.setText("только чулочно-носочные изделия;");

        jButton1.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jButton1.setText("Сформировать отчёт ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jButton2.setText("Закрыть ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Dialog", 2, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(19, 19, 19))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(12, 12, 12)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jRadioButton1)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(jRadioButton2))
                                                                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(10, 10, 10))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jRadioButton1)
                                        .addComponent(jRadioButton2))
                                .addGap(3, 3, 3)
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            pdb = new ReportPDB();
            db = new ReportDB();
            rezaltData = new Vector();

            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate()))) {
                pb = new ProgressBar(this, false, "Поиск данных для отчёта...");

                final SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        rezaltData = pdb.getVolumeOtgruz(
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())),
                                kt,
                                jCheckBox1.isSelected());

                        if (buttonGroup1.getSelection().getActionCommand().equals("2")) {
                            pb.setMessage("Группировка по группам изделий...");
                            rezaltData = db.getGroupVolumeOtgruz(pdb.getTempAnalizVolumeOtgruzTable(), jCheckBox1.isSelected());
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();

                pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                pb.setVisible(true);

                new AnalizRezalt(this, true, rezaltData,
                        " с " + new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate()) +
                                " по " + new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate()),
                        2);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        kt = new Vector();
        sDate = new JDateChooser();
        eDate = new JDateChooser();

        jPanel1.setLayout(new GridLayout());
        jPanel2.setLayout(new GridLayout());

        sDate.setPreferredSize(new Dimension(130, 20));
        eDate.setPreferredSize(new Dimension(130, 20));

        jRadioButton1.setActionCommand("1");
        jRadioButton2.setActionCommand("2");

        jRadioButton2.setSelected(true);

        try {
            pdb = new ReportPDB();
            String[] period = pdb.getPeriodOtgruz();
            jLabel4.setText("Доспупный период отгрузки c " + period[0] + " по " + period[1]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }

        kt.addAll(Arrays.asList("1505,", "1508,", "1515,1516,", "1521,", "1530,1533,", "1538,"));

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        java.util.Date d = c.getTime();
        d.setDate(1);
        sDate.setDate(d);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        eDate.setDate(c.getTime());

        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);

        jPanel1.add(sDate);
        jPanel2.add(eDate);
    }

    public Vector getDataGroup(int idPtk) {
        Vector rezalt = new Vector();
        try {
            rezalt = db.getModelsGroupVolumeOtgruz(idPtk, jCheckBox1.isSelected());
        } catch (Exception e) {
            rezalt = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rezalt;
    }

    public Vector getSumItogo() {
        Vector rezalt = new Vector();
        try {
            rezalt = pdb.getSumItogoVolumeOtgruz(jCheckBox1.isSelected());
        } catch (Exception e) {
            rezalt = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rezalt;
    }
}
