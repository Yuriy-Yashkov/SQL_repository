package dept.upack;

import lombok.SneakyThrows;
import workDB.DB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class TableForm extends JDialog {

    Vector users = new Vector();
    Vector col = new Vector();
    String sd;
    String ed;
    DefaultTableModel tm;
    String kol;
    String sum;
    String kolOfComplete = "";
    String sumOfComplete = "";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    @SneakyThrows
    public TableForm(JDialog parent, boolean modal, String sd, String ed, int ot4ot, int n) {
        super(parent, modal);
        this.sd = new String(sd);
        this.ed = new String(ed);

        initComponents();
        this.setTitle("Сдача");
        jLabel1.setText("Сдано на склад в период с " + sd + " по " + ed);
        jLabel2.setText("");
        col.add("№");
        col.add("Модель");
        col.add("Шифр артикула");
        col.add("Наименование");
        col.add("Сорт");
        col.add("Кол-во");
        col.add("Кол-во с комплектностью");
        col.add("Цена");
        col.add("Сумма");
        //col.add("Сумма с компл");
        jButton2.setVisible(false);
        jLabel4.setVisible(false);
        DB db = new DB();
        if (ot4ot == 1) {
            users = db.sdachaNaSklad(sd, ed, n);
            this.setTitle("Сдача");
            jLabel1.setText("Сдано на склад в период с " + sd + " по " + ed);
        } else if (ot4ot == 2) {
            users = db.sdachaNaUpack(sd, ed);
            this.setTitle("Приём");
            jLabel1.setText("Принято на упаковку в период с " + sd + " по " + ed);
            jLabel4.setVisible(true);
            jButton2.setVisible(true);
        } else if (ot4ot == 3) {
            users = db.ostUpack();
            this.setTitle("Остатки");
            jLabel1.setText("Текущие остатки");
        }
        kol = parsStr(Long.toString(db.kol), 3);
        sum = parsStr(Long.toString(db.sum), 3);
        kolOfComplete = parsStr(Long.toString(db.kolForSdachaNaUpack), 3);
        sumOfComplete = parsStr(Long.toString(db.sumForSdachaNaUpack), 3);
        jLabel2.setText("Итого: " + kol + "      " + sum);
        jLabel4.setText("Кол-во с учётом комплектности: " + kolOfComplete);
        jLabel3.setText("Неверные " + parsStr(Long.toString(db.mkol), 3) + " на сумму " + parsStr(Long.toString(db.msum), 3));
        tm = new DefaultTableModel(users, col);
        jTable1.setModel(tm);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(20);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(150);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(50);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public String parsStr(String str, int razr) {
        StringBuffer s = new StringBuffer(str);
        int i = s.length() - razr;
        for (; i > 0; i = i - razr) {
            s.insert(i, '.');
        }
        return s.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(788, 637));

        jLabel1.setText("jLabel1");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("jLabel2");

        jLabel3.setText("jLabel3");

        jButton1.setText("Закрыть");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Размеры");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Печать");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setText("jLabel3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel1)
                                .addContainerGap(476, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 346, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 187, Short.MAX_VALUE)
                                                .addComponent(jButton1)
                                                .addGap(91, 91, 91)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2))
                                .addGap(105, 105, 105))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButton1)
                                                .addComponent(jButton3))
                                        .addComponent(jLabel4))
                                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int row = -1;
        row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Не выбрана ни одна строка", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        new RazmerForm(this, true, sd, ed, (String) jTable1.getValueAt(row, 1), (String) jTable1.getValueAt(row, 2), (String) jTable1.getValueAt(row, 4));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            tm.addRow(new Object[]{"", "", "", "", "", kol.toString(), kolOfComplete.toString(), "", sum});
            OpenOffice oo = new OpenOffice(jLabel1.getText(), tm);
            oo.createReport("ПринятоСданоУпаковка.ots");
           /* Font font = new Font(null, Font.BOLD, 14);
            MessageFormat headerFormat = new MessageFormat(jLabel1.getText());
            MessageFormat footerFormat = new MessageFormat("- {0} -");
            //this.print(this.getGraphics());
            Book b = new Book();
            //b.append("ew"., null);
            b.append(jTable1.getPrintable(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat), null);
            jTable1.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);*/

        } catch (Exception pe) {
            System.err.println("Error printing: " + pe.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    // End of variables declaration//GEN-END:variables

}
