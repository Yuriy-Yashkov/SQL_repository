package dept.marketing.report;

import common.ColumnGroup;
import common.GroupableTableHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class AnalizRezalt extends javax.swing.JDialog {
    DefaultTableModel tModel;

    JTable table;

    Vector col;
    Vector row;

    int type;

    String[] gcol;
    String strReport;
    AnalizRR formAnalizRR;
    AnalizVolumeOtgruz formAnalizVolumeOtgruz;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public AnalizRezalt(java.awt.Dialog parent, boolean modal, Vector dataTable, String strReport, int type) {
        super(parent, modal);
        initComponents();

        row = dataTable;
        this.type = type;
        this.strReport = strReport;

        init();

        if (type == 1) {
            formAnalizRR = (AnalizRR) parent;
            setTitle("Анализ региональных рынков");
            initAnalizRR();
            createAnalizRR();
        } else if (type == 2) {
            formAnalizVolumeOtgruz = (AnalizVolumeOtgruz) parent;
            setTitle("Анализ объема отгрузок");
            initVolumeOtgruz();
            createVolumeOtgruz();
        }

        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1069, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 442, Short.MAX_VALUE)
        );

        jButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton1.setText("Печать");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("  ");

        jButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton2.setText("Закрыть");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton3.setText("Развернуть группу");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1069, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12, 12, 12))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1069, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3)
                                        .addComponent(jButton1))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            ReportOO oo = new ReportOO(strReport, tModel, gcol, type == 1 ? formAnalizRR.getSumItogo() : formAnalizVolumeOtgruz.getSumItogo());
            oo.createReport(type == 1 ? "AnalizRR.ots" : "AnalizVolumeOtgruz.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            if (table.getSelectedRow() != -1) {
                if (Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString()) > 0) {
                    new AnalizDetal(
                            this,
                            true,
                            type == 1 ? formAnalizRR.getDataGroup(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString())) :
                                    formAnalizVolumeOtgruz.getDataGroup(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString())),
                            type,
                            gcol);
                }
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали ни одной группы изделий!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void initAnalizRR() {
        gcol = new String[]{formAnalizRR.list1.getText(),
                formAnalizRR.list2.getText(),
                formAnalizRR.list3.getText(),
                formAnalizRR.list4.getText(),
                formAnalizRR.list5.getText(),
                formAnalizRR.list6.getText()};

        col.add("idPtk");
        col.add("Изделия");
        col.add("т.шт.");
        col.add("руб.");
        col.add("т.шт.");
        col.add("руб.");
        col.add("т.шт.");
        col.add("руб.");
        col.add("т.шт.");
        col.add("руб.");
        col.add("т.шт.");
        col.add("руб.");
        col.add("т.шт.");
        col.add("руб.");
        col.add("т.шт.");
        col.add("руб.");

        jLabel1.setText("Анализ региональных рынков за период " + strReport);
    }

    private void createAnalizRR() {
        try {
            tModel = new DefaultTableModel(row, col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            table.setModel(tModel);
            table.setAutoCreateColumnsFromModel(true);
            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(40);
            table.getColumnModel().getColumn(3).setPreferredWidth(40);
            table.getColumnModel().getColumn(4).setPreferredWidth(40);
            table.getColumnModel().getColumn(5).setPreferredWidth(40);
            table.getColumnModel().getColumn(6).setPreferredWidth(40);
            table.getColumnModel().getColumn(7).setPreferredWidth(40);
            table.getColumnModel().getColumn(8).setPreferredWidth(40);
            table.getColumnModel().getColumn(9).setPreferredWidth(40);
            table.getColumnModel().getColumn(10).setPreferredWidth(40);
            table.getColumnModel().getColumn(11).setPreferredWidth(40);
            table.getColumnModel().getColumn(12).setPreferredWidth(40);
            table.getColumnModel().getColumn(13).setPreferredWidth(40);
            table.getColumnModel().getColumn(14).setPreferredWidth(40);
            table.getColumnModel().getColumn(15).setPreferredWidth(40);

            TableColumnModel cm = table.getColumnModel();
            ColumnGroup g_name = new ColumnGroup("");
            GroupableTableHeader header = (GroupableTableHeader) table.getTableHeader();
            int j = 2;
            for (int i = 0; i < gcol.length; i++) {
                g_name = new ColumnGroup(gcol[i]);
                g_name.add(cm.getColumn(j++));
                g_name.add(cm.getColumn(j++));
                header.addColumnGroup(g_name);
            }
            g_name = new ColumnGroup("Итого");
            g_name.add(cm.getColumn(j++));
            g_name.add(cm.getColumn(j++));
            header.addColumnGroup(g_name);

            jPanel3.add(new JScrollPane(table));

            jTable1.setModel(new DefaultTableModel(formAnalizRR.getSumItogo(), col));
            jTable1.setAutoCreateColumnsFromModel(true);
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(9).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(10).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(11).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(12).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(13).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(14).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(15).setPreferredWidth(40);

            jTable1.getTableHeader().setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initVolumeOtgruz() {
        col.add("idPtk");
        col.add("Изделия");
        col.add("РБ");
        col.add("ЭКСПОРТ");
        col.add("Всего");
        col.add("% ЭКСПОРТ");
        col.add("РБ");
        col.add("ЭКСПОРТ");
        col.add("Всего");
        col.add("% ЭКСПОРТ");

        jLabel1.setText("Анализ объема отгрузок за период " + strReport);
    }

    private void createVolumeOtgruz() {
        try {
            tModel = new DefaultTableModel(row, col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            table.setModel(tModel);
            table.setAutoCreateColumnsFromModel(true);
            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(40);
            table.getColumnModel().getColumn(3).setPreferredWidth(40);
            table.getColumnModel().getColumn(4).setPreferredWidth(40);
            table.getColumnModel().getColumn(5).setPreferredWidth(40);
            table.getColumnModel().getColumn(6).setPreferredWidth(40);
            table.getColumnModel().getColumn(7).setPreferredWidth(40);
            table.getColumnModel().getColumn(8).setPreferredWidth(40);
            table.getColumnModel().getColumn(9).setPreferredWidth(40);

            TableColumnModel cm = table.getColumnModel();
            ColumnGroup g_name = new ColumnGroup("");
            GroupableTableHeader header = (GroupableTableHeader) table.getTableHeader();

            g_name = new ColumnGroup("Объём (шт.)");
            g_name.add(cm.getColumn(2));
            g_name.add(cm.getColumn(3));
            g_name.add(cm.getColumn(4));
            g_name.add(cm.getColumn(5));
            header.addColumnGroup(g_name);

            g_name = new ColumnGroup("Сумма (руб.)");
            g_name.add(cm.getColumn(6));
            g_name.add(cm.getColumn(7));
            g_name.add(cm.getColumn(8));
            g_name.add(cm.getColumn(9));
            header.addColumnGroup(g_name);

            jPanel3.add(new JScrollPane(table));

            jTable1.setModel(new DefaultTableModel(formAnalizVolumeOtgruz.getSumItogo(), col));
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(9).setPreferredWidth(40);

            jTable1.getTableHeader().setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void init() {
        col = new Vector();

        table = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        jButton3.setVisible(false);

        jPanel3.setLayout(new GridLayout());
        jPanel3.setPreferredSize(new Dimension(1032, 324));

        try {
            for (Iterator it = row.iterator(); it.hasNext(); ) {
                if (Integer.valueOf(((Vector) it.next()).get(0).toString()) > 0) {
                    jButton3.setVisible(true);
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
