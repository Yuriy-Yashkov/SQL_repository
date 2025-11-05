package dept.sprav.employe;

import common.CheckBoxHeader;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class BrigForm extends javax.swing.JDialog {
    Vector col;
    Vector row;

    DefaultTableModel tModel;

    EmployePDB epdb;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public BrigForm(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Параметры просмотра");

        init();

        createBrigTable();

        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Выберите бригады для просмотра");

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

        jButton1.setText("Закрыть");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Сохранить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            UtilEmploye.PARAM_BRIG = new Vector();

            for (int i = 0; i < jTable1.getRowCount(); i++) {
                if (Boolean.valueOf(jTable1.getValueAt(i, 0).toString()))
                    UtilEmploye.PARAM_BRIG.add(jTable1.getValueAt(i, 2).toString());
            }

        } catch (Exception e) {
            UtilEmploye.PARAM_BRIG = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            String rezaltPrint = "";
            for (Object object : UtilEmploye.PARAM_BRIG) {
                rezaltPrint += object.toString();
                rezaltPrint += ",";
            }

            UtilFunctions.setSettingPropFile(rezaltPrint, UtilEmploye.SETTING_PARAM_BRIG);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        dispose();

    }//GEN-LAST:event_jButton2ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void createBrigTable() {
        try {
            try {
                epdb = new EmployePDB();
                row = epdb.getAllBrig(UtilEmploye.PARAM_BRIG);
            } catch (Exception e) {
                row = new Vector();
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                epdb.disConn();
            }

            tModel = new DefaultTableModel(row, col) {
                @Override
                public Class<?> getColumnClass(int col) {
                    if (row.isEmpty())
                        return super.getClass();
                    else
                        return getValueAt(0, col).getClass();
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    return col == 0 ? true : false;
                }
            };

            jTable1.setModel(tModel);

            jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(90);
            jTable1.getColumnModel().getColumn(2).setMinWidth(0);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(0);

            jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(jTable1.getTableHeader(), 0, ""));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void init() {
        col = new Vector();

        col.add("");
        col.add("Бригада");
        col.add("idBrig");

        jTable1.setAutoCreateColumnsFromModel(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
    }
}
