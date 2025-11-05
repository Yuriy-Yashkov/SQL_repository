package dept.sbit;

import workDB.PDB;

import javax.swing.*;
import java.awt.*;

public class EditEmailForm extends javax.swing.JDialog {
    int id_Client = -10;
    String eMail = null;
    boolean add = false;
    SendMailForm jd = null;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;

    public EditEmailForm(Dialog parent, boolean modal, int id_Client, String mail) {
        super(parent, modal);
        jd = (SendMailForm) parent;

        setTitle("Редактор электронных адресов");
        this.id_Client = id_Client;
        this.eMail = new String(mail);
        initComponents();

        if (eMail.equals("Добавить...".toString()) || eMail.equals("Адрес отсутствует".toString()) || eMail.equals("".toString())) {
            jButton1.setText("Добавить");
            jButton2.setVisible(false);
            add = true;
        } else {
            add = false;
            jButton1.setText("Изменить");
            jButton2.setVisible(true);
            jTextField1.setText(eMail);
        }

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Изменить");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 16));
        jLabel1.setText("Редактор электронных адресов");

        jButton2.setText("Удалить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(48, 48, 48)
                                                .addComponent(jLabel1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton2)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean checkEmail() {
        String str = new String(jTextField1.getText().trim());
        if (str.length() == 0) {
            JOptionPane.showMessageDialog(null, "Вы не ввели электронный адрес", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (checkEmail()) {
            PDB pdb = new PDB();
            if (add) {
                if (pdb.setEmail(jd.getIdClient(), "", jTextField1.getText().trim().toLowerCase()))
                    JOptionPane.showMessageDialog(null, "Электронный адрес добавлен успешно", "Адрес добавлен", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else if (pdb.setEmail(jd.getIdClient(), eMail.trim().toLowerCase(), jTextField1.getText().trim().toLowerCase()))
                JOptionPane.showMessageDialog(null, "Электронный адрес изменён успешно", "Адрес изменён", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            pdb.disConn();
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (checkEmail() && !add) {
            PDB pdb = new PDB();
            if (pdb.delEmail(jd.getIdClient(), jTextField1.getText().trim().toLowerCase()))
                JOptionPane.showMessageDialog(null, "Электронный адрес удалён", "Адрес удалён", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Ошибка удаления электронного адреса", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            pdb.disConn();
            dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    // End of variables declaration//GEN-END:variables

}
