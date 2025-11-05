package dept.production.zsh.zplata;

import by.march8.ecs.MainController;
import common.CheckBoxHeader;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ZPlataTableForm extends javax.swing.JDialog {
    DefaultTableModel tModel;
    Vector col;
    TableRowSorter sorter;
    DefaultTableCellRenderer renderer;

    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;

    ZPlataPDB zpdb;

    String flag;
    String sDate;
    String eDate;
    int idDept;
    int idBrig;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public ZPlataTableForm(MainController mainController, boolean modal, Vector row, String title, String flag, String sDate, String eDate, int idDept, int idBrig) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle(title);
        initComponents();

        this.flag = flag;
        this.sDate = sDate;
        this.eDate = eDate;
        this.idDept = idDept;
        this.idBrig = idBrig;

        if (flag.equals(UtilZPlata.KOLVO_MARSHRUT) || flag.equals(UtilZPlata.KOLVO_MODEL))
            jLabel1.setVisible(false);

        init();

        createTableZPlataForm(row);

        setLocationRelativeTo(controller.getMainForm());
        setPreferredSize(new Dimension(850, 550));
        pack();
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
        jLabel1 = new javax.swing.JLabel();

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

        jButton2.setText("Печать");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Развернуть");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("0");
        jLabel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 158, Short.MAX_VALUE)
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            Vector vec = new Vector();

            vec.add(2);
            vec.add(4);

            if (flag.equals("1")) {
                vec.add(6);
                vec.add(7);
                vec.add(8);
                vec.add(9);
            } else if (flag.equals("2")) {
                vec.add(5);
            } else if (flag.equals("3")) {
                vec.add(5);
                vec.add(6);
            } else if (flag.equals("4")) {
                vec.add(5);
                vec.add(6);
                vec.add(7);
            } else if (flag.equals(UtilZPlata.KOLVO_MARSHRUT) || flag.equals(UtilZPlata.KOLVO_MODEL)) {
                vec.add(5);
                vec.add(6);
                vec.add(7);
                vec.add(8);
            }

            ZPlataOO oo = new ZPlataOO(getTitle(), tModel, vec);
            oo.createReport("ZPlataDefaultTable.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            if (jTable1.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } else {
                Vector dataUpdate = new Vector();
                Vector rowData = new Vector();
                Vector data = new Vector();
                String titleData = "";

                try {
                    zpdb = new ZPlataPDB();
                    if (flag.equals("1")) {
                        rowData = zpdb.getDetalProductionEmployees(
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString()),
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));

                        data.add(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString()));
                        data.add(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString()));

                        titleData = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString() + ", " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString() + ", " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 7).toString() + ", разряд " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString();

                    } else if (flag.equals("2")) {
                        rowData = zpdb.getDetalProductionBrig(
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString()),
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));

                        titleData = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString() + ", " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString();

                    } else if (flag.equals("3")) {
                        rowData = zpdb.getDetalProductionModel(
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString()),
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));

                        titleData = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString() + ", " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString() + ", " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString();

                    } else if (flag.equals("4")) {
                        rowData = zpdb.getDetalProductionMarshrut(
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString()),
                                jTable1.getValueAt(jTable1.getSelectedRow(), 6).toString(),
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));

                        titleData = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString() + ", " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString() + ", " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString();

                    } else if (flag.equals(UtilZPlata.KOLVO_MODEL)) {
                        rowData = zpdb.getDetalKolvoModel(
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString()),
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));

                        titleData = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString() + ", " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString() + ", модель " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString();

                    } else if (flag.equals(UtilZPlata.KOLVO_MARSHRUT)) {
                        rowData = zpdb.getDetalKolvoMarshrut(
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()),
                                Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString()),
                                jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString(),
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));

                        titleData = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString() + ", " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString() + ", модель " +
                                jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString();

                    }
                } catch (Exception e) {
                    titleData = "";
                    rowData = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка" + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    zpdb.disConn();
                }

                new ZPlataSmallTableForm(controller, true, titleData, rowData, data, flag);

                try {
                    zpdb = new ZPlataPDB();
                    if (flag.equals("1"))
                        dataUpdate = zpdb.getProductionEmployeesTable(
                                idDept,
                                idBrig,
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));
                    else if (flag.equals("2"))
                        dataUpdate = zpdb.getProductionBrigTable(
                                idDept,
                                idBrig,
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));
                    else if (flag.equals("3"))
                        dataUpdate = zpdb.getProductionModelTable(
                                idDept,
                                idBrig,
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));
                    else if (flag.equals("4"))
                        dataUpdate = zpdb.getProductionMarshrutTable(
                                idDept,
                                idBrig,
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));
                    else if (flag.equals(UtilZPlata.KOLVO_MODEL))
                        dataUpdate = zpdb.getKolvoModelTable(
                                idDept,
                                idBrig,
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));
                    else if (flag.equals(UtilZPlata.KOLVO_MARSHRUT))
                        dataUpdate = zpdb.getKolvoMarshrutTable(
                                idDept,
                                idBrig,
                                UtilFunctions.convertDateStrToLong(sDate),
                                UtilFunctions.convertDateStrToLong(eDate));
                } catch (Exception e) {
                    dataUpdate = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    zpdb.disConn();
                }

                createTableZPlataForm(dataUpdate);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        col = new Vector();

        col.add("");
        col.add("idDept");
        col.add("Цех");
        col.add("idBrig");
        col.add("Бригада");
        if (flag.equals("1")) {
            col.add("idEmploye");
            col.add("Табел.");
            col.add("ФИО");
            col.add("Разряд");
        }
        if (flag.equals("3"))
            col.add("Модель");
        if (flag.equals("4")) {
            col.add("Модель");
            col.add("Маршрут");
        }
        if (flag.equals(UtilZPlata.KOLVO_MODEL)) {
            col.add("Модель");
            col.add("Кол-во маршрут");
            col.add("Кол-во чистое");
            col.add("Кол-во брак");
        } else if (flag.equals(UtilZPlata.KOLVO_MARSHRUT)) {
            col.add("Маршрут");
            col.add("Кол-во маршрут");
            col.add("Кол-во чистое");
            col.add("Кол-во брак");
        } else
            col.add("Выработка");

        jTable1.setAutoCreateColumnsFromModel(true);
        jTable1.getTableHeader().setReorderingAllowed(false);

        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
                try {
                    if (!jLabel1.getText().trim().equals("0"))
                        jLabel1.setText("0");

                    int nCol = 0;
                    double sum = 0;
                    if (jTable1.getSelectedRowCount() > 1) {
                        for (int i = 0; i < jTable1.getRowCount(); i++)
                            if (jTable1.isRowSelected(i)) {
                                if (flag.equals("1")) nCol = 9;
                                else if (flag.equals("2")) nCol = 5;
                                else if (flag.equals("3")) nCol = 6;
                                else if (flag.equals("4")) nCol = 7;

                                if (nCol > 0)
                                    if (jTable1.getValueAt(i, nCol) != null)
                                        sum += Double.valueOf(jTable1.getValueAt(i, nCol).toString());

                            }

                        if (sum > 0)
                            jLabel1.setText(UtilZPlata.formatNorm(sum, 2));
                    }
                } catch (Exception ex) {
                    jLabel1.setText("0");
                    JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setBackground(table.getBackground());
                try {
                    if (Integer.valueOf(table.getValueAt(row, 7).toString()) == 0 &&
                            Integer.valueOf(table.getValueAt(row, 8).toString()) == 0)
                        cell.setBackground(Color.YELLOW);
                    else {
                        if (Integer.valueOf(table.getValueAt(row, 6).toString()) !=
                                Integer.valueOf(table.getValueAt(row, 7).toString()) +
                                        Integer.valueOf(table.getValueAt(row, 8).toString()))
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

    private void createTableZPlataForm(final Vector row) {
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

        tModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRow == -1 || minSelectedRow == -1) {
                    return;
                }
                tableModelListenerIsChanging = true;
                boolean value = ((Boolean) tModel.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                    tModel.setValueAt(Boolean.valueOf(value), jTable1.convertRowIndexToModel(i), column);
                }

                minSelectedRow = -1;
                maxSelectedRow = -1;

                tableModelListenerIsChanging = false;
            }
        });

        jTable1.setModel(tModel);
        jTable1.setAutoCreateColumnsFromModel(true);

        jTable1.getColumnModel().getColumn(0).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(1).setMinWidth(0);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(3).setMinWidth(0);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(10);
        if (flag.equals("1")) {
            jTable1.getColumnModel().getColumn(5).setMinWidth(0);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(10);
        } else if (flag.equals("3"))
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(10);

        sorter = new TableRowSorter<TableModel>(tModel);
        jTable1.setRowSorter(sorter);
        jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(jTable1.getTableHeader(), 0, ""));

        if (flag.equals(UtilZPlata.KOLVO_MARSHRUT) || flag.equals(UtilZPlata.KOLVO_MODEL))
            jTable1.getColumnModel().getColumn(6).setCellRenderer(renderer);
    }
}
