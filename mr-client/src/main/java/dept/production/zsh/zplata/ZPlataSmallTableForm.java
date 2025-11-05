package dept.production.zsh.zplata;

import by.march8.ecs.MainController;
import common.User;
import workDB.PDB;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ZPlataSmallTableForm extends javax.swing.JDialog {
    Vector col;
    DefaultTableModel tModel;
    DefaultTableCellRenderer renderer;

    String flag;
    Vector data;

    PDB pdb = null;
    ZPlataPDB zpdb = null;

    User user = User.getInstance();
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;

    public ZPlataSmallTableForm(MainController mainController, boolean modal, String title, Vector row, Vector data, String flag) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        initComponents();
        setTitle(title);

        this.flag = flag;
        this.data = data;

        init();

        // if(UtilZPlata.BRIGADIR_EDIT)
        jButton3.setVisible(true);
        createSmallTableZPlataForm(row);

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        jButton2.setText("Открыть");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Изменить");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            Vector dataList = new Vector();
            Vector dataItem = new Vector();

            try {
                zpdb = new ZPlataPDB();
                dataList = zpdb.getDataList(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()));
                dataItem = zpdb.getDataListItem(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()));

                new ZPlataDetalForm(controller, true,
                        dataList,
                        dataItem,
                        Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Данные листка запуска не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                zpdb.disConn();
            }
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали листок запуска!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            Vector dataList = new Vector();
            Vector dataItem = new Vector();
            Vector dataUpdate = new Vector();

            try {
                zpdb = new ZPlataPDB();
                dataList = zpdb.getDataList(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()));
                dataItem = zpdb.getDataListItem(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()));

                new ZPlataDetalForm(controller, true,
                        dataList,
                        dataItem,
                        Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()),
                        true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Данные листка запуска не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                zpdb.disConn();
            }

            try {
                zpdb = new ZPlataPDB();

                if (flag.equals("1"))
                    dataUpdate = zpdb.getDetalProductionEmployees(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()),
                            Integer.valueOf(data.get(0).toString()),
                            Integer.valueOf(data.get(1).toString()));
                else if (flag.equals(UtilZPlata.KOLVO_MODEL) || flag.equals(UtilZPlata.KOLVO_MARSHRUT))
                    dataUpdate = zpdb.getDetalKolvo(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()));
                else if (flag.equals(UtilZPlata.KOLVO_ERROR))
                    dataUpdate = zpdb.getDetalKolvoErrorList(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()));
                else
                    dataUpdate = zpdb.getDetalProduction(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString()));

                if (!dataUpdate.isEmpty()) {
                    jTable1.setValueAt(dataUpdate.get(0), jTable1.getSelectedRow(), 0);
                    jTable1.setValueAt(dataUpdate.get(1), jTable1.getSelectedRow(), 1);
                    jTable1.setValueAt(dataUpdate.get(2), jTable1.getSelectedRow(), 2);
                    jTable1.setValueAt(dataUpdate.get(3), jTable1.getSelectedRow(), 3);
                    jTable1.setValueAt(dataUpdate.get(4), jTable1.getSelectedRow(), 4);
                    jTable1.setValueAt(dataUpdate.get(5), jTable1.getSelectedRow(), 5);

                    if (flag.equals(UtilZPlata.KOLVO_MARSHRUT) || flag.equals(UtilZPlata.KOLVO_MODEL)) {
                        jTable1.setValueAt(dataUpdate.get(6), jTable1.getSelectedRow(), 6);
                        jTable1.setValueAt(dataUpdate.get(7), jTable1.getSelectedRow(), 7);
                        jTable1.setValueAt(dataUpdate.get(8), jTable1.getSelectedRow(), 8);
                    } else {
                        jTable1.setValueAt(dataUpdate.get(6), jTable1.getSelectedRow(), 6);
                        jTable1.setValueAt(dataUpdate.get(7), jTable1.getSelectedRow(), 7);
                        jTable1.setValueAt(dataUpdate.get(8), jTable1.getSelectedRow(), 8);
                        jTable1.setValueAt(dataUpdate.get(9), jTable1.getSelectedRow(), 9);
                    }
                } else
                    JOptionPane.showMessageDialog(null, "Данные в таблице изменились!\n Чтобы увидеть изменения, заново сформируйте таблицу! ", "Внимание!", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                zpdb.disConn();
            }

        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали листок запуска!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_jButton3ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        jButton3.setVisible(false);

        col = new Vector();

        col.add("idDept");
        col.add("Цех");
        col.add("idBrig");
        col.add("Бригада");
        col.add("Листок №");
        col.add("Модель");
        col.add("Маршрут");
        if (flag.equals(UtilZPlata.KOLVO_MARSHRUT) || flag.equals(UtilZPlata.KOLVO_MODEL)) {
            col.add("Кол-во маршрут");
            col.add("Кол-во чистое");
            col.add("Кол-во брак");
        } else if (flag.equals(UtilZPlata.KOLVO_ERROR)) {
            col.add("Дата ввода");
            col.add("Выработка");
            col.add("Выработка*");
        } else {
            col.add("Дата ввода");
            col.add("Дата коррект.");
            col.add("Выработка");
        }

        jTable1.setAutoCreateColumnsFromModel(true);
        jTable1.getTableHeader().setReorderingAllowed(false);

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setBackground(table.getBackground());
                try {
                    if (Integer.valueOf(table.getValueAt(row, 8).toString()) == 0 &&
                            Integer.valueOf(table.getValueAt(row, 9).toString()) == 0)
                        cell.setBackground(Color.YELLOW);
                    else {
                        if (Integer.valueOf(table.getValueAt(row, 7).toString()) !=
                                Integer.valueOf(table.getValueAt(row, 8).toString()) +
                                        Integer.valueOf(table.getValueAt(row, 9).toString()))
                            cell.setBackground(Color.PINK);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }
        };
    }

    private void createSmallTableZPlataForm(Vector row) {
        tModel = new DefaultTableModel(row, col);

        jTable1.setModel(tModel);

        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(2).setMinWidth(0);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(8).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(9).setPreferredWidth(10);

        if (flag.equals(UtilZPlata.KOLVO_MARSHRUT) || flag.equals(UtilZPlata.KOLVO_MODEL))
            jTable1.getColumnModel().getColumn(7).setCellRenderer(renderer);
    }
}
