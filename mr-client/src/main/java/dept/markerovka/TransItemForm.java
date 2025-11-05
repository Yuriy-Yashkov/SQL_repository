/*
 * TransItemForm.java
 *
 * Created on 02.04.2012, 12:45:03
 */
package dept.markerovka;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vova
 */
public class TransItemForm extends javax.swing.JDialog {

    public String bufer = "";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bFind;
    private javax.swing.JButton bTrans;
    private javax.swing.JComboBox cbLng;
    private javax.swing.JComboBox cbName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField tfNar;
    private javax.swing.JTextField tfTName;
    public TransItemForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        MarkerovkaDB db = new MarkerovkaDB();
        List<Language> lngs = db.getLaguages();

        cbName.removeAllItems();
        cbName.setEnabled(false);

        cbLng.setEnabled(false);
        cbLng.removeAllItems();
        for (Language lng : lngs) {
            cbLng.addItem(lng);
        }
        cbLng.setSelectedIndex(0);

        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Перевод изделия");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfNar = new javax.swing.JTextField();
        bFind = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cbName = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfTName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbLng = new javax.swing.JComboBox();
        bCancel = new javax.swing.JButton();
        bTrans = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Артикул");

        bFind.setText("Найти");
        bFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFindActionPerformed(evt);
            }
        });

        jLabel2.setText("Наименование");

        cbName.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        cbName.setEnabled(false);
        cbName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNameActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Перевод"));
        jPanel1.setToolTipText("");

        jLabel3.setText("Наименование");

        tfTName.setEnabled(false);

        jLabel4.setText("Язык перевода");

        cbLng.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        cbLng.setEnabled(false);
        cbLng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLngActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbLng, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfTName, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(cbLng, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(tfTName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(159, Short.MAX_VALUE))
        );

        bCancel.setText("Отмена");
        bCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });

        bTrans.setText("Перевести");
        bTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTransActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(jLabel1)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(tfNar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(bFind))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cbName, 0, 312, Short.MAX_VALUE))
                                                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(117, 117, 117)
                                                .addComponent(bTrans)
                                                .addGap(38, 38, 38)
                                                .addComponent(bCancel)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(tfNar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(bFind))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(cbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(bTrans)
                                        .addComponent(bCancel))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFindActionPerformed
        String nar;
        MarkerovkaDB db = new MarkerovkaDB();
        try {
            nar = tfNar.getText().trim().toUpperCase();
            cbName.removeAllItems();
            List<String> names = db.getNames(nar);
            for (String lng : names) {
                cbName.addItem(lng);
            }
            cbName.setSelectedIndex(0);
            tfTName.setText(db.getTransName(nar, ((Language) cbLng.getSelectedItem()).getId(), cbName.getSelectedItem().toString()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
        cbLng.setEnabled(true);
        cbName.setEnabled(true);
        tfTName.setEnabled(true);
        bufer = tfTName.getText().trim();
    }//GEN-LAST:event_bFindActionPerformed

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
        dispose();
    }//GEN-LAST:event_bCancelActionPerformed

    private void bTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTransActionPerformed
        String nar;
        MarkerovkaDB db = new MarkerovkaDB();
        try {
            nar = tfNar.getText().trim().toUpperCase();
            if ("".equals(tfTName.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Значение не может быть пустым", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                tfTName.setText(bufer);
                tfTName.selectAll();
            } else {
                if (db.chekTransName(nar, ((Language) cbLng.getSelectedItem()).getId(), cbName.getSelectedItem().toString())) {
                    if (JOptionPane.showConfirmDialog(null, "Заменить уже существующий перевод?", "Вопрос", JOptionPane.OK_CANCEL_OPTION) == 0) {
                        db.apdTransName(nar, ((Language) cbLng.getSelectedItem()).getId(), cbName.getSelectedItem().toString(), tfTName.getText().trim());
                        JOptionPane.showMessageDialog(null, "Перевод усешно завершён", "оК", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    db.setTransName(nar, ((Language) cbLng.getSelectedItem()).getId(), cbName.getSelectedItem().toString(), tfTName.getText().trim());
                    JOptionPane.showMessageDialog(null, "Перевод усешно завершён", "оК", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bTransActionPerformed

    private void cbLngActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLngActionPerformed
        if (cbName.getItemCount() > 0) {
            String nar;
            MarkerovkaDB db = new MarkerovkaDB();
            try {
                nar = tfNar.getText().trim().toUpperCase();
                tfTName.setText(db.getTransName(nar, ((Language) cbLng.getSelectedItem()).getId(), cbName.getSelectedItem().toString()));
                bufer = tfTName.getText().trim();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_cbLngActionPerformed

    private void cbNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNameActionPerformed
        if (cbName.getItemCount() > 0) {
            String nar;
            MarkerovkaDB db = new MarkerovkaDB();
            try {
                nar = tfNar.getText().trim().toUpperCase();
                tfTName.setText(db.getTransName(nar, ((Language) cbLng.getSelectedItem()).getId(), cbName.getSelectedItem().toString()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_cbNameActionPerformed
    // End of variables declaration//GEN-END:variables
}
