package dept.marketing.report;

import com.jhlabs.awt.ParagraphLayout;
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
public class AnalizRR extends javax.swing.JDialog {
    public JTextPane list1;
    public JTextPane list2;
    public JTextPane list3;
    public JTextPane list4;
    public JTextPane list5;
    public JTextPane list6;
    JButton but1p;
    JButton but1m;
    JButton but2p;
    JButton but2m;
    JButton but3p;
    JButton but3m;
    JButton but4p;
    JButton but4m;
    JButton but5p;
    JButton but5m;
    JButton but6p;
    JButton but6m;
    JDateChooser sDate;
    JDateChooser eDate;
    JPanel pan1;
    JPanel pan2;
    JPanel pan3;
    JPanel pan4;
    JPanel pan5;
    JPanel pan6;
    Vector kt;
    Vector rezaltData;

    ProgressBar pb;
    ReportDB db;
    ReportPDB pdb;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    public AnalizRR(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Анализ региональных рынков");
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
        final JLabel jLabel1 = new JLabel();
        final JLabel jLabel2 = new JLabel();
        final JButton jButton2 = new JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        final JLabel jLabel3 = new JLabel();
        final JButton jButton3 = new JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Задайте параметры для отчёта");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Период c");

        jButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton2.setText("Закрыть");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(AnalizRR.this::jButton2ActionPerformed);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 99, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 13, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 122, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 14, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel3.setText("по");

        jButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton3.setText("Сформировать отчёт");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(AnalizRR.this::jButton3ActionPerformed);

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setText("По моделям;");
        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setText("По группам изделий;");
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 444, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 398, Short.MAX_VALUE)
        );

        jCheckBox1.setFont(new java.awt.Font("Dialog", 0, 13));
        jCheckBox1.setText("только чулочно-носочные изделия;");
        jCheckBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel4.setFont(new java.awt.Font("Dialog", 2, 13));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel3)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(36, 36, 36)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jRadioButton1)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jRadioButton2))
                                                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jRadioButton1)
                                        .addComponent(jRadioButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox1)
                                .addGap(8, 8, 8)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton3)
                                        .addComponent(jButton2))
                                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (kt.size() < 6 || kt.contains("")) {
            JOptionPane.showMessageDialog(null, "Есть пустое поле в региональных рынках.", "Внимание!", javax.swing.JOptionPane.WARNING_MESSAGE);
        } else {
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
                            rezaltData = pdb.getOtgruzRR(
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())),
                                    kt,
                                    jCheckBox1.isSelected());

                            if (buttonGroup1.getSelection().getActionCommand().equals("2")) {
                                pb.setMessage("Группировка по группам изделий...");
                                rezaltData = db.getGroupOtgruzRR(pdb.getTempAnalizRRTable(), jCheckBox1.isSelected());
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

                    new AnalizRezalt(AnalizRR.this, true, rezaltData,
                            " с " + new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate()) +
                                    " по " + new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate()),
                            1);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                pdb.disConn();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        sDate = new JDateChooser();
        eDate = new JDateChooser();
        pan1 = new JPanel();
        pan2 = new JPanel();
        pan3 = new JPanel();
        pan4 = new JPanel();
        pan5 = new JPanel();
        pan6 = new JPanel();
        but1p = new JButton("+");
        but1m = new JButton("-");
        but2p = new JButton("+");
        but2m = new JButton("-");
        but3p = new JButton("+");
        but3m = new JButton("-");
        but4p = new JButton("+");
        but4m = new JButton("-");
        but5p = new JButton("+");
        but5m = new JButton("-");
        but6p = new JButton("+");
        but6m = new JButton("-");
        list1 = new JTextPane();
        list2 = new JTextPane();
        list3 = new JTextPane();
        list4 = new JTextPane();
        list5 = new JTextPane();
        list6 = new JTextPane();
        kt = new Vector();

        jPanel1.setLayout(new GridLayout());
        jPanel2.setLayout(new GridLayout());
        jPanel3.setLayout(new ParagraphLayout());
        pan1.setLayout(new GridLayout(2, 0, 5, 5));
        pan2.setLayout(new GridLayout(2, 0, 5, 5));
        pan3.setLayout(new GridLayout(2, 0, 5, 5));
        pan4.setLayout(new GridLayout(2, 0, 5, 5));
        pan5.setLayout(new GridLayout(2, 0, 5, 5));
        pan6.setLayout(new GridLayout(2, 0, 5, 5));

        sDate.setPreferredSize(new Dimension(130, 20));
        eDate.setPreferredSize(new Dimension(130, 20));
        but1p.setPreferredSize(new Dimension(45, 20));
        but1m.setPreferredSize(new Dimension(45, 20));
        but2p.setPreferredSize(new Dimension(45, 20));
        but2m.setPreferredSize(new Dimension(45, 20));
        but3p.setPreferredSize(new Dimension(45, 20));
        but3m.setPreferredSize(new Dimension(45, 20));
        but4p.setPreferredSize(new Dimension(45, 20));
        but4m.setPreferredSize(new Dimension(45, 20));
        but5p.setPreferredSize(new Dimension(45, 20));
        but5m.setPreferredSize(new Dimension(45, 20));
        but6p.setPreferredSize(new Dimension(45, 20));
        but6m.setPreferredSize(new Dimension(45, 20));
        list1.setPreferredSize(new Dimension(250, 50));
        list2.setPreferredSize(new Dimension(250, 50));
        list3.setPreferredSize(new Dimension(250, 50));
        list4.setPreferredSize(new Dimension(250, 50));
        list5.setPreferredSize(new Dimension(250, 50));
        list6.setPreferredSize(new Dimension(250, 50));

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        java.util.Date d = c.getTime();
        d.setDate(1);
        sDate.setDate(d);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        eDate.setDate(c.getTime());

        jRadioButton1.setActionCommand("1");
        jRadioButton2.setActionCommand("2");

        jRadioButton2.setSelected(true);

        but1p.addActionListener(evt -> new KTForm(AnalizRR.this, true, UtilReport.DOCUMENT_LIST1));

        but1m.addActionListener(evt -> {
            cleanList(UtilReport.DOCUMENT_LIST1);
            list1.setText("");
        });

        but2p.addActionListener(evt -> new KTForm(AnalizRR.this, true, UtilReport.DOCUMENT_LIST2));

        but2m.addActionListener(evt -> {
            cleanList(UtilReport.DOCUMENT_LIST2);
            list2.setText("");
        });

        but3p.addActionListener(evt -> new KTForm(AnalizRR.this, true, UtilReport.DOCUMENT_LIST3));

        but3m.addActionListener(evt -> {
            cleanList(UtilReport.DOCUMENT_LIST3);
            list3.setText("");
        });

        but4p.addActionListener(evt -> new KTForm(AnalizRR.this, true, UtilReport.DOCUMENT_LIST4));

        but4m.addActionListener(evt -> {
            cleanList(UtilReport.DOCUMENT_LIST4);
            list4.setText("");
        });

        but5p.addActionListener(evt -> new KTForm(AnalizRR.this, true, UtilReport.DOCUMENT_LIST5));

        but5m.addActionListener(evt -> {
            cleanList(UtilReport.DOCUMENT_LIST5);
            list5.setText("");
        });

        but6p.addActionListener(evt -> new KTForm(AnalizRR.this, true, UtilReport.DOCUMENT_LIST6));

        but6m.addActionListener(evt -> {
            cleanList(UtilReport.DOCUMENT_LIST6);
            list6.setText("");
        });

        kt.addAll(Arrays.asList("1505,", "1508,", "1515,1516,", "1521,", "1530,1533,", "1538,"));

        int num;
        try {
            pdb = new ReportPDB();
            for (int i = 0; i < kt.size(); i++) {
                String element = (String) kt.get(i);
                String[] elements = element.split(",");
                for (final String element1 : elements) {
                    num = i + 1;
                    setNameKT(Integer.valueOf(element1), pdb.getNameKT(Integer.valueOf(element1)), "list" + num);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }

        try {
            pdb = new ReportPDB();
            String[] period = pdb.getPeriodOtgruz();
            jLabel4.setText("Доспупный период отгрузки c " + period[0] + " по " + period[1]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }

        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);

        pan1.add(but1p);
        pan1.add(but1m);
        pan2.add(but2p);
        pan2.add(but2m);
        pan3.add(but3p);
        pan3.add(but3m);
        pan4.add(but4p);
        pan4.add(but4m);
        pan5.add(but5p);
        pan5.add(but5m);
        pan6.add(but6p);
        pan6.add(but6m);

        jPanel3.add(new JLabel("Регион. рынок:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        jPanel3.add(new JScrollPane(list1));
        jPanel3.add(pan1);
        jPanel3.add(new JLabel("Регион. рынок:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        jPanel3.add(new JScrollPane(list2));
        jPanel3.add(pan2);
        jPanel3.add(new JLabel("Регион. рынок:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        jPanel3.add(new JScrollPane(list3));
        jPanel3.add(pan3);
        jPanel3.add(new JLabel("Регион. рынок:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        jPanel3.add(new JScrollPane(list4));
        jPanel3.add(pan4);
        jPanel3.add(new JLabel("Регион. рынок:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        jPanel3.add(new JScrollPane(list5));
        jPanel3.add(pan5);
        jPanel3.add(new JLabel("Регион. рынок:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        jPanel3.add(new JScrollPane(list6));
        jPanel3.add(pan6);

        jPanel1.add(sDate);
        jPanel2.add(eDate);
    }

    public void setNameKT(int idKt, String nameKt, String type) {
        try {
            if (type != null && nameKt.length() > 0) {
                if (type.equals(UtilReport.DOCUMENT_LIST1)) {
                    list1.setText(list1.getText().trim() + " " + nameKt + ";\n");
                } else if (type.equals(UtilReport.DOCUMENT_LIST2)) {
                    list2.setText(list2.getText().trim() + " " + nameKt + ";\n");
                } else if (type.equals(UtilReport.DOCUMENT_LIST3)) {
                    list3.setText(list3.getText().trim() + " " + nameKt + ";\n");
                } else if (type.equals(UtilReport.DOCUMENT_LIST4)) {
                    list4.setText(list4.getText().trim() + " " + nameKt + ";\n");
                } else if (type.equals(UtilReport.DOCUMENT_LIST5)) {
                    list5.setText(list5.getText().trim() + " " + nameKt + ";\n");
                } else if (type.equals(UtilReport.DOCUMENT_LIST6)) {
                    list6.setText(list6.getText().trim() + " " + nameKt + ";\n");
                }
                int num = Integer.valueOf(type.replace("list", "")) - 1;
                String tmp = kt.get(num).toString();
                if (tmp.indexOf(String.valueOf(idKt)) == -1) {
                    tmp = tmp + String.valueOf(idKt) + ",";
                    kt.remove(num);
                    kt.add(num, tmp);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cleanList(String type) {
        try {
            int num = Integer.valueOf(type.replace("list", "")) - 1;
            kt.remove(num);
            kt.add(num, "");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public Vector getDataGroup(Integer idPtk) {
        Vector rezalt = new Vector();
        try {
            rezalt = db.getModelsGroupAnalizRR(idPtk, jCheckBox1.isSelected());
        } catch (Exception e) {
            rezalt = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rezalt;
    }

    public Vector getSumItogo() {
        Vector rezalt = new Vector();
        try {
            rezalt = pdb.getSumItogoAnalizRR(jCheckBox1.isSelected());
        } catch (Exception e) {
            rezalt = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rezalt;
    }
}
