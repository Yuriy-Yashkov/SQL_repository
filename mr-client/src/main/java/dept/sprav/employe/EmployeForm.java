package dept.sprav.employe;

import by.march8.ecs.MainController;
import common.FormMenu;
import common.User;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import workDB.PDB;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Vector;

@SuppressWarnings("all")
public class EmployeForm extends javax.swing.JDialog implements FormMenu {
    private static TreeMap menu = new TreeMap();
    EmployePDB epdb = null;
    PDB pdb = null;
    Vector col;
    Vector row;
    DefaultTableModel tModel;
    TableRowSorter<TableModel> sorter;
    TableFilterHeader filterHeader;
    private boolean flagDialog = false;
    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;
    private User user = User.getInstance();
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    public EmployeForm() {
        initMenu();
/*        initComponents();
        init();*/
    }
    public EmployeForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Справочник");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        initComponents();
        init();

        jButton5.setVisible(false);
        jMenu2.setVisible(false);
        jMenuItem2.setVisible(false);

        createEmployeTable();

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setPreferredSize(new Dimension(800, 450));
        pack();
        setVisible(true);
    }
    public EmployeForm(MainController mainController, boolean modal, String typeSearch, int idBrig) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Справочник");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        initComponents();

        flagDialog = true;

        init();

        jMenu1.setVisible(false);
        jMenuItem1.setVisible(false);
        jMenu2.setVisible(true);
        jMenuItem2.setVisible(true);

        if (typeSearch.equals(UtilEmploye.TYPE_SEARCH_BRIGNUM)) {
            try {
                if (idBrig != -1)
                    UtilEmploye.PARAM_BRIG.add(String.valueOf(idBrig));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка! " + e.getMessage(),
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                if (!UtilFunctions.readPropFileString(UtilEmploye.SETTING_PARAM_BRIG).trim().equals(""))
                    UtilEmploye.PARAM_BRIG = new Vector(Arrays.asList(UtilFunctions.readPropFileStringArray(UtilEmploye.SETTING_PARAM_BRIG)));
                else if (idBrig != -1)
                    UtilEmploye.PARAM_BRIG.add(String.valueOf(idBrig));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }

        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        createEmployeTable();

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }
    public EmployeForm(MainController mainController, boolean modal, int idDept, String str) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Справочник");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        initComponents();
        init();

        jButton3.setVisible(false);
        jMenu2.setVisible(false);
        jMenuItem2.setVisible(false);

        createEmployeTable();

        jButton4.doClick();

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        initMenu();
        controller.menuFormInitialisation(getClass().getName(), menu);

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();


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

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Сотрудники предприятия");

        jButton1.setText("Добавить");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Редактировать");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("История");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Поиск");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton5.setText("Выбрать");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Ключевое слово:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton4)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton5)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new AddEmployeForm(this, true, false, flagDialog);
        createEmployeTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jTable1.getSelectedRowCount() > 0) {
            new AddEmployeForm(this, true, true, flagDialog);
            createEmployeTable();
            jButton4.doClick();

        } else
            JOptionPane.showMessageDialog(null,
                    "Вы не выбрали ни одного сотрудника!",
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jTable1.getSelectedRowCount() > 0) {
            if (flagDialog) {
                new HistoryEmployeForm(this, true,
                        jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString(),
                        Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()));
            } else {
                new HistoryEmployeForm(this, true,
                        jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString(),
                        Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()));
            }

        } else
            JOptionPane.showMessageDialog(null,
                    "Вы не выбрали ни одного сотрудника!",
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (jTable1.getSelectedRowCount() > 0) {
            if (JOptionPane.showOptionDialog(null, "Вы уверены, что хотите удалить выбранного сотрудника из базы?", "Удаление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да") == JOptionPane.YES_OPTION) {
                try {
                    epdb = new EmployePDB();
                    epdb.deleteEmploye(Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    epdb.disConn();
                }

                createEmployeTable();
            }
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали ни одного сотрудника!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {

            java.util.List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(3);
            filters.add(RowFilter.regexFilter("(?iu)" + jTextField1.getText().trim()));

            RowFilter<Object, Object> serviceFilter = RowFilter.andFilter(filters);
            sorter.setRowFilter(serviceFilter);

            if (jTable1.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "По заданному запросу ничего не найдено!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                sorter.setRowFilter(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) jButton4.doClick();
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            if (jTable1.getSelectedRow() == -1)
                JOptionPane.showMessageDialog(null, "Вы не выбрали ни одного сотрудника!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            else {
                UtilEmploye.NUM = "";
                UtilEmploye.NAIM = "";
                UtilEmploye.TABEL = "";
                UtilEmploye.CATEGORY = "";
                UtilEmploye.VECTOR_EMPL = new Vector();

                if (flagDialog) {
                    for (Object rows : tModel.getDataVector()) {
                        if (((Vector) rows).get(0).toString().equals("true")) {
                            Vector tmp = new Vector();
                            tmp.add(((Vector) rows).get(1).toString());
                            tmp.add(((Vector) rows).get(5).toString());
                            tmp.add(((Vector) rows).get(2).toString());
                            tmp.add(((Vector) rows).get(6).toString());
                            UtilEmploye.VECTOR_EMPL.add(tmp);
                        }
                    }

                    if (UtilEmploye.VECTOR_EMPL.isEmpty()) {
                        Vector tmp = new Vector();
                        tmp.add(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
                        tmp.add(jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString());
                        tmp.add(jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString());
                        tmp.add(jTable1.getValueAt(jTable1.getSelectedRow(), 6).toString());
                        UtilEmploye.VECTOR_EMPL.add(tmp);

                        UtilEmploye.NUM = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
                        UtilEmploye.NAIM = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();
                        UtilEmploye.TABEL = jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString();
                        UtilEmploye.CATEGORY = jTable1.getValueAt(jTable1.getSelectedRow(), 6).toString();
                    }

                } else {
                    UtilEmploye.NUM = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
                    UtilEmploye.NAIM = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
                    UtilEmploye.TABEL = jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString();
                    UtilEmploye.CATEGORY = jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString();
                }

                dispose();
            }
        } catch (Exception e) {
            UtilEmploye.NUM = "";
            UtilEmploye.NAIM = "";
            UtilEmploye.TABEL = "";
            UtilEmploye.CATEGORY = "";
            UtilEmploye.VECTOR_EMPL = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            new BrigForm(this, true);
            createEmployeTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void createEmployeTable() {
        if (flagDialog)
            createEmployeBrigTable();
        else
            createEmployeDefaultTable();
    }

    private void createEmployeDefaultTable() {
        try {
            epdb = new EmployePDB();
            row = epdb.getAllEmployees();
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            epdb.disConn();
        }

        tModel = new DefaultTableModel(row, col) {
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        jTable1.setModel(tModel);
        jTable1.setAutoCreateColumnsFromModel(true);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(3);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(5);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(3);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(30);

        sorter = new TableRowSorter<TableModel>(tModel);
        jTable1.setRowSorter(sorter);
    }

    private void createEmployeBrigTable() {
        try {
            epdb = new EmployePDB();
            row = epdb.getAllEmployees(UtilEmploye.PARAM_BRIG);
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            epdb.disConn();
        }

        tModel = new DefaultTableModel(row, col) {
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0) ? true : false;
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
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(3);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(5);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(7).setMinWidth(0);
        jTable1.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(8).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(9).setPreferredWidth(30);

        sorter = new TableRowSorter<TableModel>(tModel);
        jTable1.setRowSorter(sorter);
    }

    public int getSelectEmploye() {
        return Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), flagDialog ? 1 : 0).toString());
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        jMenu1.setText("Правка");

        jMenuItem1.setText("Удалить");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Сервис");

        jMenuItem2.setText("Параметры просмотра");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);


        jMenu1.setVisible(false);
        jMenu2.setVisible(false);
        jMenuItem1.setVisible(false);
        jMenuItem2.setVisible(false);

        menu.put("1", jMenu1);              //Правка
        menu.put("1-1", jMenuItem1);            //Удалить
        menu.put("2", jMenu2);              //Сервис
        menu.put("2-1", jMenuItem2);            //Параметры просморта
    }

    private void init() {
        col = new Vector();
        row = new Vector();
        tModel = null;

        filterHeader = new TableFilterHeader(jTable1, AutoChoices.ENABLED);

        UtilEmploye.NUM = "";
        UtilEmploye.NAIM = "";
        UtilEmploye.TABEL = "";
        UtilEmploye.CATEGORY = "";
        UtilEmploye.VECTOR_EMPL = new Vector();
        UtilEmploye.PARAM_BRIG = new Vector();

        if (flagDialog)
            col.add("");
        col.add("Код");
        col.add("ФИО");
        col.add("Подразделение");
        col.add("Бригада");
        col.add("Таб. номер");
        col.add("Разряд");
        col.add("Логин/Пароль");
        col.add("Профессия");
        col.add("Примечание");

        jTable1.getTableHeader().setReorderingAllowed(false);

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1 && jButton5.isVisible()) {
                    try {
                        jButton5.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public TreeMap getMenuMap() {
        return menu;
    }

    public void setFormVisible() {
        this.setVisible(false);
    }

    public void disposeForm() {
        dispose();
    }
}
