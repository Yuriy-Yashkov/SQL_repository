package dept.production.zsh.zplata;

import by.march8.ecs.MainController;
import com.toedter.calendar.JDateChooser;
import common.User;
import common.UtilFunctions;
import dept.sprav.employe.EmployeForm;
import dept.sprav.employe.UtilEmploye;
import dept.sprav.model.ModelForm;
import dept.sprav.model.UtilModel;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class AddNesortForm extends javax.swing.JDialog {
    boolean flagEdit = false;
    JDateChooser sDate;

    ZPlataPDB zpdb;
    int idNesort;
    private User user = User.getInstance();
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    public AddNesortForm(MainController mainController, boolean modal, Vector dataNesort, boolean type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        setTitle(type ? "Изменить" : "Добавить");
        initComponents();

        this.flagEdit = type;

        init();

        if (flagEdit) {
            try {
                this.idNesort = Integer.valueOf(dataNesort.get(1).toString());

                try {
                    jComboBox1.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.DEPT_MODEL, Integer.valueOf(dataNesort.get(2).toString())));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    jComboBox2.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIG_MODEL, Integer.valueOf(dataNesort.get(4).toString())));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                jLabel11.setText(Integer.valueOf(dataNesort.get(6).toString()).toString());
                jLabel5.setText(dataNesort.get(7).toString());
                jTextField1.setText(Integer.valueOf(dataNesort.get(8).toString()).toString());
                jLabel13.setText(Integer.valueOf(dataNesort.get(9).toString()).toString());
                jTextField2.setText(Integer.valueOf(dataNesort.get(10).toString()).toString());
                jLabel10.setText(UtilZPlata.formatNorm(Double.valueOf(dataNesort.get(11).toString()), 2));
                sDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataNesort.get(12).toString()));

            } catch (Exception e) {
                flagEdit = false;
                setTitle("Добавить");
                idNesort = 0;
                jLabel11.setText(" ");
                jLabel5.setText(" ");
                jTextField1.setText(" ");
                jLabel13.setText(" ");
                jTextField2.setText(" ");
                jLabel10.setText(" ");
                sDate.setDate(Calendar.getInstance().getTime());
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                jComboBox1.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.DEPT_MODEL, UtilZPlata.DEPT_SELECT_ITEM));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            try {
                jComboBox2.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIG_MODEL, UtilZPlata.BRIG_SELECT_ITEM));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" ");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Цех:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Бригада:");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Работник:");

        jButton1.setText("Найти");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText(" ");
        jLabel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Разряд:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Кол-во:");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Дата:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 207, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 27, Short.MAX_VALUE)
        );

        jLabel9.setText("Выработка:");

        jLabel10.setText(" ");
        jLabel10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton2.setText(" ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Закрыть");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText(" ");

        jButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton4.setText("Рассчитать");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Модель:");

        jButton5.setText("Найти");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel13.setText(" ");
        jLabel13.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                                                        .addComponent(jLabel9)
                                                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                                                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                                                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addGap(12, 12, 12)))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGap(22, 22, 22))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                                                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(jButton5)
                                        .addComponent(jLabel13))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            boolean saveFlag = true;
            String str = "";

            if (((Item) jComboBox1.getSelectedItem()).getId() == -1) {
                saveFlag = false;
                str += "Вы не выбрали цех!\n";
            }

            if (((Item) jComboBox2.getSelectedItem()).getId() == -1) {
                saveFlag = false;
                str += "Вы не выбрали бригаду!\n";
            }

            if (jLabel13.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не выбрали модель!\n";
            }

            if (jLabel11.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не выбрали работника!\n";
            }

            if (jTextField1.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели разряд!\n";
            }

            if (jTextField2.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели кол-во!\n";
            }

            if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate()))) {
                saveFlag = false;
                str += "Ошибка ввода даты!\n";
            }

            if (!saveFlag)
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

            if (saveFlag) {
                try {
                    zpdb = new ZPlataPDB();

                    if (evt.getActionCommand().equals(UtilZPlata.BUTTON_ADD)) {
                        zpdb.addNesort(((Item) jComboBox1.getSelectedItem()).getId(),
                                ((Item) jComboBox2.getSelectedItem()).getId(),
                                Integer.valueOf(user.getIdEmployee()),
                                Integer.valueOf(jLabel11.getText()),
                                Integer.valueOf(jTextField1.getText().trim()),
                                Integer.valueOf(jLabel13.getText().trim()),
                                Integer.valueOf(jTextField2.getText().trim()),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())));
                    } else if (evt.getActionCommand().equals(UtilZPlata.BUTTON_SAVE)) {
                        zpdb.updateNesort(idNesort,
                                ((Item) jComboBox1.getSelectedItem()).getId(),
                                ((Item) jComboBox2.getSelectedItem()).getId(),
                                Integer.valueOf(user.getIdEmployee()),
                                Integer.valueOf(jLabel11.getText()),
                                Integer.valueOf(jTextField1.getText().trim()),
                                Integer.valueOf(jLabel13.getText().trim()),
                                Integer.valueOf(jTextField2.getText().trim()),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())));
                    }
                    dispose();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    zpdb.disConn();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            new EmployeForm(
                    controller,
                    true,
                    UtilEmploye.TYPE_SEARCH_CONFIG,
                    ((Item) jComboBox2.getSelectedItem()).getId());

            jLabel11.setText(UtilEmploye.NUM);
            jLabel5.setText(UtilEmploye.TABEL + " " + UtilEmploye.NAIM);

        } catch (Exception e) {
            jLabel5.setText("");
            jLabel11.setText("");
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            jLabel10.setText(" ");

            if (!jTextField2.getText().trim().equals(""))
                jLabel10.setText(UtilZPlata.formatNorm(new Double(Integer.valueOf(jTextField2.getText()) * UtilZPlata.NESORT_KOF), 2));
        } catch (Exception e) {
            jTextField2.setText("");
            jLabel10.setText(" ");
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            new ModelForm(controller, true, true);

            jLabel13.setText(UtilModel.MODEL);
        } catch (Exception e) {
            jLabel13.setText("");
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        sDate = new JDateChooser();

        sDate.setPreferredSize(new Dimension(130, 20));

        sDate.setDate(Calendar.getInstance().getTime());

        jPanel1.setLayout(new GridLayout());
        jPanel1.add(sDate);

        jLabel11.setVisible(false);

        jLabel1.setText("Несортные изделия");
        jButton2.setText(flagEdit ? UtilZPlata.BUTTON_SAVE : UtilZPlata.BUTTON_ADD);

        jComboBox1.removeAllItems();
        jComboBox2.removeAllItems();

        for (Iterator it = UtilZPlata.DEPT_MODEL.iterator(); it.hasNext(); )
            jComboBox1.addItem(it.next());

        for (Iterator it = UtilZPlata.BRIG_MODEL.iterator(); it.hasNext(); )
            jComboBox2.addItem(it.next());
    }
}
