package dept.production.zsh.zplata;

import common.CheckBoxHeader;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class WorkForm extends javax.swing.JDialog {
    Vector col;
    Vector data;

    DefaultTableModel tModel;
    DefaultTableCellRenderer renderer;
    TableRowSorter sorter;

    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;

    boolean flag;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;

    public WorkForm(java.awt.Dialog parent, boolean modal, Vector row, String title, boolean flag) {
        super(parent, modal);
        initComponents();

        setTitle("Выполнение. " + title);
        this.flag = flag;

        init();

        createWorkTableZPlataForm(row);

        setLocationRelativeTo(parent);
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
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        jButton2.setText("Печать");
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2))
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

            for (int i = 3; i < col.size(); i++) {
                if (i != 4)
                    vec.add(i);
                else if (flag)
                    vec.add(i);
            }

            ZPlataOO oo = new ZPlataOO(getTitle(), tModel, vec);
            oo.createReport("ZPlataDefaultTable.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        col = new Vector();

        col.add("");
        col.add("idEmploye");
        col.add("Таб.");
        col.add("ФИО");
        col.add("Разряд");
        col.add("Выработка");
        col.add("Несортные");
        col.add("Др. конв.");
        col.add("Итого");
        col.add("Отр. время");
        col.add("Выполнение");

        jTable1.setAutoCreateColumnsFromModel(true);
        jTable1.getTableHeader().setReorderingAllowed(false);

        jTable1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        jTable1.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                jTable1.editCellAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                Component editor = jTable1.getEditorComponent();
                if (editor != null) {
                    editor.requestFocus();
                    if (editor instanceof JTextField) {
                        ((JTextField) editor).selectAll();
                    }
                }
            }
        });

        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
            maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
        });

        new TableFilterHeader(jTable1, AutoChoices.ENABLED);
    }

    private void createWorkTableZPlataForm(final Vector row) {
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
                return col == 0 || col == 5 || col == 6 || col == 7 || col == 9 ? true : false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);
                    double itogo = 0;

                    if (value != null) {
                        switch (col) {
                            case 5:
                            case 6:
                            case 7:
                                rowVector.setElementAt(new Double(UtilZPlata.formatNorm(Double.valueOf(value.toString()), 2)), col);
                                itogo = new Double(UtilZPlata.formatNorm(new Double(UtilZPlata.formatNorm(Double.valueOf(rowVector.elementAt(5).toString()), 2)) +
                                        new Double(UtilZPlata.formatNorm(Double.valueOf(rowVector.elementAt(6).toString()), 2)) +
                                        new Double(UtilZPlata.formatNorm(Double.valueOf(rowVector.elementAt(7).toString()), 2)), 2));

                                rowVector.setElementAt(new Double(UtilZPlata.formatNorm(itogo, 2)), 8);
                                rowVector.setElementAt(new Double(UtilZPlata.formatNorm((itogo * 100) / Integer.valueOf(rowVector.elementAt(9).toString()), 2)), 10);
                                fireTableCellUpdated(row, 8);
                                fireTableCellUpdated(row, 10);
                                break;
                            case 9:
                                rowVector.setElementAt(Integer.valueOf(value.toString()), col);
                                rowVector.setElementAt(new Double(UtilZPlata.formatNorm((Double.valueOf(rowVector.elementAt(8).toString()) * 100) / Integer.valueOf(rowVector.elementAt(9).toString()), 2)), 10);
                                fireTableCellUpdated(row, 10);
                                break;
                            default:
                                rowVector.setElementAt(value, col);
                                break;
                        }
                    }

                    fireTableCellUpdated(row, col);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + ee.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
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

        jTable1.getColumnModel().getColumn(0).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(1).setMinWidth(0);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(130);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(8).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(9).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(10).setPreferredWidth(30);

        sorter = new TableRowSorter<TableModel>(tModel);
        jTable1.setRowSorter(sorter);
        jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(jTable1.getTableHeader(), 0, ""));
    }
}
