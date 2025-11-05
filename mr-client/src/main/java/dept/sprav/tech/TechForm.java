package dept.sprav.tech;

import by.march8.ecs.MainController;
import common.FormMenu;
import common.User;
import common.UtilFunctions;
import workDB.PDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class TechForm extends javax.swing.JDialog implements FormMenu {
    private static TreeMap menu = new TreeMap();
    DefaultTableModel tModel;
    TableRowSorter sorter;
    Vector col;
    Vector row;
    TechPDB mpdb;
    PDB pdb;
    private User user = User.getInstance();
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
    private javax.swing.JTextField jTextField1;
    public TechForm() {
        //initComponents();
        //init();
        initMenu();
    }
    public TechForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        initMenu();

        initComponents();
        init();

        jButton3.setVisible(true);
        controller.menuFormInitialisation(getClass().getName(), menu);

        if (!modal) {
            jButton4.setVisible(true);
            jButton2.setVisible(true);
            jMenu1.setVisible(true);
            jButton3.setVisible(false);
        } else {
            jButton4.setVisible(false);
            jButton2.setVisible(false);
            jMenu1.setVisible(false);
            jButton3.setVisible(true);
        }

        createModelTable();

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

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

        jMenu1.setVisible(false);
        jMenuItem1.setVisible(false);

        menu.put("1", jMenu1);              //Правка
        menu.put("1-1", jMenuItem1);            //Удалить
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        initMenu();
        //controller.menu

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();


        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Оборудование");

        jLabel2.setText("Ключевые слова для поиска");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jButton1.setText("Поиск");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        jButton2.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jButton2.setText("Добавить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jButton3.setText("Выбрать");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jButton4.setText("Изменить");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(jTextField1)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(8, 8, 8)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(31, 31, 31)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                                .addComponent(jLabel2))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3)
                                        .addComponent(jButton4))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            if (jTable1.getSelectedRow() == -1)
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали оборудование!",
                        "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            else {
                UtilTech.ID = Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
                UtilTech.TECH = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String text = jTextField1.getText().trim();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                if (jTable1.getRowCount() == 0)
                    JOptionPane.showMessageDialog(null,
                            "По заданному запросу ничего не найдено!",
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка " + e.getMessage(),
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new AddTechForm(this, true);
        createModelTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) jButton1.doClick();
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            new AddTechForm(this, true,
                    jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString(),
                    jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString(),
                    Double.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString().trim().replace(",", ".")));
        } else
            JOptionPane.showMessageDialog(null,
                    "Вы не выбрали оборудование для корректировки!",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);

        createModelTable();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            if (JOptionPane.showOptionDialog(null,
                    "Вы действительно хотите удалить запись?",
                    "Удаление записи",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Отмена"}, "Отмена") == JOptionPane.YES_OPTION) {

                try {
                    mpdb = new TechPDB();

                    mpdb.deleteTech(Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()));

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Не удалось удалить оборудование!\n" + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    mpdb.disConn();
                }
            }
        } else
            JOptionPane.showMessageDialog(null,
                    "Вы не выбрали оборудование для корректировки!",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);

        createModelTable();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        setTitle("Справочник оборудования");

        col = new Vector();
        row = new Vector();

        col.add("Код");
        col.add("Оборудование");
        col.add("Стоимость");

        jLabel3.setIcon(UtilFunctions.createIcon("/Img/tech.png"));

        UtilTech.ID = -1;
        UtilTech.TECH = "";

        jTable1.getTableHeader().setReorderingAllowed(false);

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1 && jButton3.isVisible()) {
                    try {
                        jButton3.doClick();

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Ошибка! " + ex.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);

                    }
                }
            }
        });
    }

    private void createModelTable() {
        try {
            mpdb = new TechPDB();
            row = mpdb.getAllTech();
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            mpdb.disConn();
        }

        tModel = new DefaultTableModel(row, col) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        jTable1.setModel(tModel);
        jTable1.setAutoCreateColumnsFromModel(true);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(130);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(70);

        sorter = new TableRowSorter<TableModel>(tModel) {
            public Comparator<?> getComparator(int column) {
                if (column == 0) {
                    return new Comparator<String>() {
                        public int compare(String s1, String s2) {
                            return Integer.parseInt(s1) - Integer.parseInt(s2);
                        }
                    };
                }
                return super.getComparator(column);
            }
        };
        jTable1.setRowSorter(sorter);
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
