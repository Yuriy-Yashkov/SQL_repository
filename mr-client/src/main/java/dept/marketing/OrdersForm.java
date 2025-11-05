package dept.marketing;

import common.User;
import workDB.PDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Vector;

public class OrdersForm extends javax.swing.JDialog {
    private final String formSent = "Заявки отправленные на склад";
    private final String formOpen = "Открыть заявку";
    public PDB pdb;
    private int idMadeorder;
    private String madeorder;
    private User user = User.getInstance();
    private Vector col = new Vector();
    private DefaultTableModel tm = null;
    private CartForm cartform;
    private int idcl;
    private String title;
    private String format;
    private TableRowSorter<TableModel> sorter;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    public OrdersForm(javax.swing.JDialog parent, boolean modal, int idcl) {
        super(parent, modal);
        initComponents();
        this.setTitle(formOpen);
        this.idcl = idcl;
        this.title = formOpen;

        cartform = (CartForm) parent;
        pdb = cartform.pdb;

        idMadeorder = Integer.parseInt(user.getIdEmployee());

        createOrderSaveForm();

        jTextField1.setVisible(false);
        jFormattedTextField1.setVisible(false);
        jButton4.setVisible(false);
        jButton5.setVisible(false);
        jMenuBar1.setVisible(false);
        jLabel1.setVisible(false);

        jTable1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    jButton3ActionPerformed(null);
                }
            }
        });

        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }
    public OrdersForm(javax.swing.JFrame parent, boolean modal) {
        super(parent, modal);

        madeorder = user.getFio();
        if (madeorder != null) {
            initComponents();
            this.setTitle(formSent);
            this.title = formSent;

            CartForm.maskFormatterDate().install(jFormattedTextField1);
            Calendar c = Calendar.getInstance();
            int i = c.get(Calendar.MONTH) + 1;
            String month = new String();
            if (i < 10) month = "0" + i;
            else month = Integer.toString(i);
            jFormattedTextField1.setText(new String("01." + month + "." + c.get(Calendar.YEAR)));

            pdb = new PDB();
            pdb.updateListOrdersPDB();

            createOrderSkladForm();

            jLabel1.setText(formSent + " c ");
            jButton1.setText("Смотреть выполнение");
            jButton3.setText("Удалить");
            jButton2.setText("Редактировать");

            jTable1.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        jButton3ActionPerformed(null);
                    }

                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        jButton1ActionPerformed(null);
                    }
                }
            });

            addWindowListener(new WindowListener() {
                public void windowClosed(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    pdb.disConn();
                }

                public void windowOpened(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowActivated(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }
            });

            setLocationRelativeTo(null);
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору. ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

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
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jButton1.setText("Открыть");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Отмена");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Удалить");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton4.setText("Поиск");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton5.setText("Показать");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jFormattedTextField1.setFont(new java.awt.Font("Dialog", 0, 14));

        jMenu1.setText("Переход");
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenuItem1.setText("Мои заявки");
        jMenuItem1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Справка");
        jMenu2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenuItem2.setText("Статус заявки");
        jMenuItem2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Горячие клавиши");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 949, Short.MAX_VALUE)
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(67, 67, 67)
                                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                                                .addGap(28, 28, 28)
                                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                                .addGap(60, 60, 60))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(134, 134, 134)
                                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                                .addGap(12, 12, 12)
                                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                                .addGap(12, 12, 12)
                                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                                .addGap(128, 128, 128))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(144, 144, 144)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                                .addGap(241, 241, 241))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButton5)
                                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton4)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton1)
                                        .addComponent(jButton3)
                                        .addComponent(jButton2))
                                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (title.equals(formOpen)) {
            dispose();
        } else if (title.equals(formSent)) {
            if (jTable1.getSelectedRowCount() > 0) {
                try {
                    if (pdb.downloadSavedOrder(Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()), 3))
                        new MarketingForm(this, true, Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()),
                                Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()), jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString(),
                                new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString())),
                                Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 11).toString()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } else
                JOptionPane.showMessageDialog(null, "Ничего не выбрано!", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            if (jTable1.getSelectedRowCount() > 0) {
                if (title.equals(formOpen)) {
                    if (pdb.downloadSavedOrder(Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()), 1)) {
                        MarketingForm.noteorder = jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString();
                        dispose();
                        cartform.cartFormUpdate();
                    }
                } else if (title.equals(formSent)) {
                    new CartForm(this, true, Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()),
                            Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()), jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString(), new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(
                            jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString())), Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 11).toString()), jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString(),
                            jTable1.getValueAt(jTable1.getSelectedRow(), 9).toString(), jTable1.getValueAt(jTable1.getSelectedRow(), 7).toString(), jTable1.getValueAt(jTable1.getSelectedRow(), 6).toString(), jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString());
                }
            } else
                JOptionPane.showMessageDialog(null, "Ничего не выбрано!", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            if (title.equals(formOpen)) cartform.cartFormUpdate();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            if (jTable1.getSelectedRowCount() > 0) {
                if (JOptionPane.showOptionDialog(null, "Вы уверены, что хотите удалить выделенную заявку?", "Удаление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да") == JOptionPane.YES_OPTION) {
                    pdb.deleteOrder(Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()), Integer.parseInt(user.getIdEmployee()));
                    if (title.equals(formOpen))
                        createOrderSaveForm();
                    else if (title.equals(formSent))
                        createOrderSkladForm();
                }
            } else
                JOptionPane.showMessageDialog(null, "Ничего не выбрано!", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String text = jTextField1.getText();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter(text));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JOptionPane.showMessageDialog(null, " 'не открыта' - заявка отправлена на склад, но ещё не открыта;\n 'формируется' - заявка открыта и принята к исполнению;\n 'выполнена' - заявка выполнена и готова к отгрузке;\n 'срок вышел' - срок выполнения заявки вышел;\n", "Статус заявки", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        jTextField1.setText(madeorder);
        jButton4ActionPerformed(null);
        jTextField1.setText(null);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        createOrderSkladForm();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        JOptionPane.showMessageDialog(null, "DELETE - удалить заявку;\nSPACE - посмотреть выполнение заявки;\n"/*F5 - заявка выполнена/заявка не выполнена;"*/, "Горячие клавиши", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void createOrderSaveForm() {
        col = new Vector();
        col.add("№ заявки");
        col.add("На дату");
        col.add("Составил");
        col.add("Отдел");
        col.add("Дата");
        col.add("Примечание");

        tm = new DefaultTableModel(pdb.getSavedOrders(idcl, idMadeorder), col) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        jTable1.setModel(tm);
        jTable1.setAutoCreateColumnsFromModel(true);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(120);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(40);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(80);

        sorter = new TableRowSorter<TableModel>(tm) {
            @Override
            public Comparator<?> getComparator(int column) {
                if (column == 0) {
                    return new Comparator<String>() {
                        public int compare(String s1, String s2) {
                            return Integer.parseInt(s1) - Integer.parseInt(s2);
                        }
                    };
                }
                if (column == 1 || column == 4) {
                    if (column == 1) format = "dd-MM-yyyy";
                    if (column == 4) format = "dd-MM-yyyy HH:mm:ss";
                    return new Comparator<String>() {
                        public int compare(String s1, String s2) {
                            try {
                                return new SimpleDateFormat(format).parse(s2).compareTo(new SimpleDateFormat(format).parse(s1));
                            } catch (Exception e) {
                                return s1.compareTo(s2);
                            }
                        }
                    };
                }
                return super.getComparator(column);
            }
        };
        jTable1.setRowSorter(sorter);
    }

    public void createOrderSkladForm() {
        if (CartForm.checkDate(jFormattedTextField1.getText().trim())) {
            col = new Vector();
            col.add("№ заявки");
            col.add("Заказчик код");
            col.add("Заказчик");
            col.add("На дату");
            col.add("Составил");
            col.add("Отдел");
            col.add("Дата");
            col.add("Изменил");
            col.add("Дата изм.");
            col.add("Выполнил");
            col.add("Статус");
            col.add("Статус код");

            tm = new DefaultTableModel(pdb.getSkladOrders(jFormattedTextField1.getText().trim()), col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            jTable1.setModel(tm);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(1).setMinWidth(0);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(130);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(60);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(60);
            jTable1.getColumnModel().getColumn(8).setMinWidth(0);
            jTable1.getColumnModel().getColumn(8).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(9).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(10).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(11).setMinWidth(0);
            jTable1.getColumnModel().getColumn(11).setMaxWidth(0);

            sorter = new TableRowSorter<TableModel>(tm) {
                @Override
                public Comparator<?> getComparator(int column) {
                    if (column == 0) {
                        return new Comparator<String>() {
                            public int compare(String s1, String s2) {
                                return Integer.parseInt(s1) - Integer.parseInt(s2);
                            }
                        };
                    }
                    if (column == 3 || column == 6) {
                        if (column == 3) format = "dd-MM-yyyy";
                        if (column == 6) format = "dd-MM-yyyy HH:mm:ss";
                        return new Comparator<String>() {
                            public int compare(String s1, String s2) {
                                try {
                                    return new SimpleDateFormat(format).parse(s2).compareTo(new SimpleDateFormat(format).parse(s1));
                                } catch (Exception e) {
                                    return s1.compareTo(s2);
                                }
                            }
                        };
                    }
                    return super.getComparator(column);
                }
            };
            jTable1.setRowSorter(sorter);
        }
    }
}
