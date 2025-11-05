package dept.marketing.report;

import common.AutoComplete;
import common.ProgressBar;
import common.UtilFunctions;
import workDB.DB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Vector;

public class SaleForm extends javax.swing.JDialog {
    String sDate, eDate;
    ProgressBar pb;
    DB db;
    JComboBox cb = new JComboBox();
    SaleForm thisForm = this;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    public SaleForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("Продажи");

        jPanel1.setLayout(new GridLayout());

        db = new DB();
        cb = new AutoComplete(db.getAllNameIzdelie());

        cb.setEditable(true);
        jPanel1.add(cb);

        UtilFunctions.maskFormatterDate().install(jFormattedTextField1);
        UtilFunctions.maskFormatterDate().install(jFormattedTextField2);

        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.MONTH) + 1;
        String month = new String();
        if (i < 10) month = "0" + i;
        else month = Integer.toString(i);
        sDate = new String("01." + month + "." + c.get(Calendar.YEAR));
        eDate = new String(new SimpleDateFormat("dd.MM.yyyy").format(c.getTime()));
        jFormattedTextField1.setText(sDate);
        jFormattedTextField2.setText(eDate);

        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 13));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Задайте параметры для формирования отчёта");

        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 335, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 24, Short.MAX_VALUE)
        );

        jLabel24.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel24.setText("Наименование группы изделия:");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel4.setText("Период с");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel5.setText("по");

        jButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton1.setText("Показать");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton2.setText("Отмена");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox1.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jCheckBox1.setText("Показать текущие остатки на складе;");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(33, 33, 33))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(17, 17, 17))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap())
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap()))))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(93, 93, 93)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            if (UtilFunctions.checkDate(jFormattedTextField1.getText()) && UtilFunctions.checkDate(jFormattedTextField2.getText())) {
                sDate = jFormattedTextField1.getText().trim();
                eDate = jFormattedTextField2.getText().trim();
                pb = new ProgressBar(this, false, "Формирование отчёта...");
                SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        new TableForm(thisForm, true);
                        return 0;
                    }

                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    // End of variables declaration//GEN-END:variables

    private class TableForm extends JDialog {
        final TableRowSorter<TableModel> sorter;

        public TableForm(SaleForm parent, boolean modal) {
            super(parent, modal);
            this.setTitle("Продажи");

            JPanel selectPanel = new JPanel();
            selectPanel.setLayout(new BorderLayout(1, 1));
            selectPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            final JLabel uplabel = new JLabel("период c " + sDate + " по " + eDate);
            uplabel.setHorizontalAlignment(JLabel.CENTER);
            uplabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 10, 1));
            selectPanel.add(uplabel, BorderLayout.NORTH);

            Vector col = new Vector();
            Vector row = new Vector();

            col.add("Название");
            col.add("Модель");
            col.add("Продано");
            if (jCheckBox1.isSelected()) col.add("Остатки");

            db = new DB();
            row = db.getSaleZaPeriod(cb.getSelectedItem().toString().trim(), sDate, eDate, jCheckBox1.isSelected());

            final JTable list = new JTable();
            final DefaultTableModel list_tm = new DefaultTableModel(row, col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            list.setModel(list_tm);
            sorter = new TableRowSorter<TableModel>(list_tm) {
                @Override
                public Comparator<?> getComparator(int column) {
                    if (column != 0) {
                        return new Comparator<String>() {
                            public int compare(String s1, String s2) {
                                return Integer.parseInt(s1) - Integer.parseInt(s2);
                            }
                        };
                    }
                    return super.getComparator(column);
                }
            };
            list.setRowSorter(sorter);
            selectPanel.add(new JScrollPane(list), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2, 5, 5));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 1, 1, 1));
            selectPanel.add(buttonPanel, BorderLayout.SOUTH);

            JButton sbn = new JButton("Печать");
            sbn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    OpenOffice oo = new OpenOffice("Продажи за " + uplabel.getText(), list_tm);
                    oo.createReport("Sales.ots");
                }
            });

            JButton cbn = new JButton("Отмена");
            cbn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispose();
                }
            });

            buttonPanel.add(sbn);
            buttonPanel.add(cbn);

            getContentPane().add(selectPanel);
            setPreferredSize(new Dimension(450, 650));
            pack();
            setLocationRelativeTo(parent);
            setResizable(false);
            setVisible(true);
        }
    }
}
