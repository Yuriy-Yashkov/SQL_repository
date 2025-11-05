package dept.marketing.report;

import common.ColumnGroup;
import common.GroupableTableHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class AnalizDetal extends javax.swing.JDialog {
    DefaultTableModel tModel;

    JTable table;

    String[] gcol;

    Vector col;
    Vector row;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;

    public AnalizDetal(java.awt.Dialog parent, boolean modal, Vector data, int type, String[] gcol) {
        super(parent, modal);
        initComponents();

        row = data;
        this.gcol = gcol;

        init();

        if (type == 1) {
            setTitle("Анализ региональных рынков");
            initAnalizRR();
            createDetalAnalizRR();
        } else if (type == 2) {
            setTitle("Анализ объема отгрузок");
            initVolumeOtgruz();
            createDetalVolumeOtgruz();
        }

        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton1.setText("Закрыть");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1003, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 298, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void initAnalizRR() {
        col.add("Модель");
        col.add("Шифр");
        col.add("Артикул");
        col.add("Наименование");
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
    }

    private void createDetalAnalizRR() {
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
            table.getColumnModel().getColumn(1).setPreferredWidth(10);
            table.getColumnModel().getColumn(2).setPreferredWidth(10);
            table.getColumnModel().getColumn(3).setPreferredWidth(10);
            table.getColumnModel().getColumn(4).setPreferredWidth(10);
            table.getColumnModel().getColumn(5).setPreferredWidth(10);
            table.getColumnModel().getColumn(6).setPreferredWidth(10);
            table.getColumnModel().getColumn(7).setPreferredWidth(10);
            table.getColumnModel().getColumn(8).setPreferredWidth(10);
            table.getColumnModel().getColumn(9).setPreferredWidth(10);
            table.getColumnModel().getColumn(10).setPreferredWidth(10);
            table.getColumnModel().getColumn(11).setPreferredWidth(10);
            table.getColumnModel().getColumn(12).setPreferredWidth(10);
            table.getColumnModel().getColumn(13).setPreferredWidth(10);
            table.getColumnModel().getColumn(14).setPreferredWidth(10);
            table.getColumnModel().getColumn(15).setPreferredWidth(10);
            table.getColumnModel().getColumn(16).setPreferredWidth(10);
            table.getColumnModel().getColumn(17).setPreferredWidth(10);

            TableColumnModel cm = table.getColumnModel();
            ColumnGroup g_name = new ColumnGroup("");
            GroupableTableHeader header = (GroupableTableHeader) table.getTableHeader();
            int j = 4;
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

            jPanel1.add(new JScrollPane(table));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    private void initVolumeOtgruz() {
        col.add("Модель");
        col.add("Шифр");
        col.add("Артикул");
        col.add("Наименование");
        col.add("РБ");
        col.add("ЭКСПОРТ");
        col.add("Всего");
        col.add("% ЭКСПОРТ");
        col.add("РБ");
        col.add("ЭКСПОРТ");
        col.add("Всего");
        col.add("% ЭКСПОРТ");
    }

    private void createDetalVolumeOtgruz() {
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
            table.getColumnModel().getColumn(1).setPreferredWidth(10);
            table.getColumnModel().getColumn(2).setPreferredWidth(10);
            table.getColumnModel().getColumn(3).setPreferredWidth(10);
            table.getColumnModel().getColumn(4).setPreferredWidth(10);
            table.getColumnModel().getColumn(5).setPreferredWidth(10);
            table.getColumnModel().getColumn(6).setPreferredWidth(10);
            table.getColumnModel().getColumn(7).setPreferredWidth(10);
            table.getColumnModel().getColumn(8).setPreferredWidth(10);
            table.getColumnModel().getColumn(9).setPreferredWidth(10);
            table.getColumnModel().getColumn(10).setPreferredWidth(10);
            table.getColumnModel().getColumn(11).setPreferredWidth(10);

            TableColumnModel cm = table.getColumnModel();
            ColumnGroup g_name = new ColumnGroup("");
            GroupableTableHeader header = (GroupableTableHeader) table.getTableHeader();

            g_name = new ColumnGroup("Объём (тыс.шт)");
            g_name.add(cm.getColumn(4));
            g_name.add(cm.getColumn(5));
            g_name.add(cm.getColumn(6));
            g_name.add(cm.getColumn(7));
            header.addColumnGroup(g_name);

            g_name = new ColumnGroup("Сумма (руб)");
            g_name.add(cm.getColumn(8));
            g_name.add(cm.getColumn(9));
            g_name.add(cm.getColumn(10));
            g_name.add(cm.getColumn(11));
            header.addColumnGroup(g_name);

            jPanel1.add(new JScrollPane(table));
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

        jPanel1.setLayout(new GridLayout());
        jPanel1.setPreferredSize(new Dimension(1032, 324));
    }
}
