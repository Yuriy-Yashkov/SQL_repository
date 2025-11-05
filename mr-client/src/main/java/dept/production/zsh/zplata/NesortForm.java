package dept.production.zsh.zplata;

import by.march8.ecs.MainController;
import common.CheckBoxHeader;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class NesortForm extends javax.swing.JDialog {
    Vector col;
    Vector row;

    DefaultTableModel tModel;

    ZPlataPDB zpdb;

    String sDate;
    String eDate;
    int idDept;
    int idBrig;

    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public NesortForm(MainController mainController, boolean modal, int idDept, int idBrig, String sDate, String eDate, String title) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        initComponents();
        setTitle("Несортные изделия");

        this.sDate = sDate;
        this.eDate = eDate;
        this.idDept = idDept;
        this.idBrig = idBrig;

        jLabel1.setText(title);

        init();

        //if(UtilZPlata.BRIGADIR_EDIT){
        jButton2.setVisible(true);
        jButton3.setVisible(true);
        jMenu1.setVisible(true);
        jMenuItem1.setVisible(true);
        //}

        createNesortTable();

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
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
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" ");

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

        jButton2.setText("Изменить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Добавить");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(" ");

        jButton4.setText("Печать");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(" ");
        jLabel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jMenu1.setText("Правка");

        jMenuItem1.setText("Удалить");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                        .addComponent(jButton1)
                                        .addComponent(jButton4)
                                        .addComponent(jButton3)
                                        .addComponent(jButton2)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3))
                                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new AddNesortForm(controller, true, new Vector(), false);
        createNesortTable();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jTable1.getSelectedRow() == -1)
            JOptionPane.showMessageDialog(null, "Вы не выбрали строку!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        else {
            Vector tempData = (Vector) (tModel.getDataVector()).get(jTable1.getSelectedRow());
            new AddNesortForm(controller, true, tempData, true);
        }
        createNesortTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            zpdb = new ZPlataPDB();
            for (Object rows : tModel.getDataVector()) {
                if (((Vector) rows).get(0).toString().equals("true"))
                    zpdb.deleteNesort(Integer.valueOf(((Vector) rows).get(1).toString()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка удаления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }
        createNesortTable();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            Vector vec = new Vector();

            vec.add(3);
            vec.add(5);
            vec.add(7);
            vec.add(8);
            vec.add(9);
            vec.add(10);
            vec.add(11);
            vec.add(12);

            ZPlataOO oo = new ZPlataOO(getTitle(), tModel, vec);
            oo.createReport("ZPlataDefaultTable.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        row = new Vector();
        col = new Vector();

        col.add("");
        col.add("idNesort");
        col.add("idDept");
        col.add("Цех");
        col.add("idBrig");
        col.add("Бригада");
        col.add("idEmployer");
        col.add("ФИО");
        col.add("Р/д");
        col.add("Модель");
        col.add("Кол-во");
        col.add("Выработка");
        col.add("Дата ввода");
        col.add("Дата корр.");

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
                    if (!jLabel3.getText().trim().equals("0"))
                        jLabel3.setText("0");

                    int nCol = 0;
                    double sum = 0;
                    if (jTable1.getSelectedRowCount() > 1) {
                        for (int i = 0; i < jTable1.getRowCount(); i++)
                            if (jTable1.isRowSelected(i)) {
                                if (jTable1.getSelectedColumn() == 11 || jTable1.getSelectedColumn() == 10)
                                    nCol = jTable1.getSelectedColumn();
                                if (nCol > 0)
                                    if (jTable1.getValueAt(i, nCol) != null)
                                        sum += Double.valueOf(jTable1.getValueAt(i, nCol).toString());

                            }

                        if (sum != 0)
                            jLabel3.setText(UtilZPlata.formatNorm(sum, 2));
                    }
                } catch (Exception ex) {
                    jLabel3.setText("0");
                    JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        jLabel2.setText("k=" + UtilZPlata.NESORT_KOF);

        jButton2.setVisible(false);
        jButton3.setVisible(false);
        jMenu1.setVisible(false);
        jMenuItem1.setVisible(false);
    }

    private void createNesortTable() {
        try {
            zpdb = new ZPlataPDB();

            row = zpdb.getNesort(idDept, idBrig, UtilFunctions.convertDateStrToLong(sDate), UtilFunctions.convertDateStrToLong(eDate));
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
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

        jTable1.getColumnModel().getColumn(0).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(1).setMinWidth(0);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(2).setMinWidth(0);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(4).setMinWidth(0);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(6).setMinWidth(0);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(8).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(9).setPreferredWidth(25);
        jTable1.getColumnModel().getColumn(10).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(11).setPreferredWidth(25);
        jTable1.getColumnModel().getColumn(12).setPreferredWidth(40);
        jTable1.getColumnModel().getColumn(13).setPreferredWidth(40);

        jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(jTable1.getTableHeader(), 0, ""));
    }
}
