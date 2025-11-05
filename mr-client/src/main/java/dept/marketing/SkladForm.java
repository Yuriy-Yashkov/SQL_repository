package dept.marketing;

import common.PanelWihtFone;
import workDB.DB;
import workDB.PDB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

public class SkladForm extends javax.swing.JDialog {
    public PDB pdb = new PDB();
    public DB db;
    int idvaluta = 1;
    int kurs, nds;
    private SkladForm parentSkladForm = this;
    private TableRowSorter<TableModel> sorter;
    private DefaultTableModel tm;
    private JTable table = new JTable();
    private String eDate, sDate;
    private String format;
    private JComboBox cb = new JComboBox();
    private Vector model = new Vector();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextField jTextField1;
    public SkladForm(JFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("Склад");
        try {
            pdb.updateListOrdersPDB();

            CartForm.maskFormatterDate().install(jFormattedTextField1);
            CartForm.maskFormatterDate().install(jFormattedTextField2);

            Calendar c = Calendar.getInstance();
            int i = c.get(Calendar.MONTH) + 1;
            String month = new String();
            if (i < 10) month = "0" + i;
            else month = Integer.toString(i);
            sDate = new String("01." + month + "." + c.get(Calendar.YEAR));
            eDate = new String(new SimpleDateFormat("dd.MM.yyyy").format(c.getTime()));
            jFormattedTextField1.setText(sDate);
            jFormattedTextField2.setText(eDate);

            createOrdersSklad(1);

            jTabbedPane1.getModel().addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    if (jTabbedPane1.getModel().getSelectedIndex() == 0) {
                        jButton3.setText("Заявка выполнена");
                        createOrdersSklad(1);
                    } else if (jTabbedPane1.getModel().getSelectedIndex() == 1) {
                        jButton3.setText("Заявка не выполнена");
                        createOrdersSklad(3);
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

            table.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        if (table.getSelectedRowCount() > 0) {
                            new OrderDialog(parentSkladForm, true, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()),
                                    Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString()), table.getValueAt(table.getSelectedRow(), 3).toString());

                            String vNdoc = "";
                            Vector ndocs = pdb.getNDocs(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()));
                            for (int i = 0; i < ndocs.size(); i++)
                                vNdoc = vNdoc + ndocs.get(i) + "; ";
                            table.setValueAt(vNdoc, table.getSelectedRow(), 8);

                            if (Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()) == 1 || table.getValueAt(table.getSelectedRow(), 1).toString().equals("true")) {
                                pdb.updateListOrdersPDB();
                                updateTabbedPane(jTabbedPane1.getModel().getSelectedIndex());
                            }
                        } else
                            JOptionPane.showMessageDialog(null, "Ничего не выделено!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        jButton2ActionPerformed(null);
                    }

                    if (e.getKeyCode() == KeyEvent.VK_F5) {
                        jButton3ActionPerformed(null);
                    }
                }
            });

            table.addMouseListener(new java.awt.event.MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        try {
                            Robot r = new Robot();
                            r.keyPress(KeyEvent.VK_SPACE);
                            r.keyRelease(KeyEvent.VK_SPACE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createOrdersSklad(final int param) {
        Vector col = new Vector();
        try {
            col.add("статус");
            col.add("edit");
            col.add("№ заявки");
            col.add("Заказчик");
            col.add("Код заказчика");
            col.add("Выполнить на дату");
            col.add("Составил");
            col.add("Дата");
            col.add("№ накладной");
            col.add("Выполнил");

            tm = new DefaultTableModel(pdb.getOrders(param, sDate, eDate), col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            table.setModel(tm);
            table.setAutoCreateColumnsFromModel(true);
            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(1).setMinWidth(0);
            table.getColumnModel().getColumn(1).setMaxWidth(0);
            table.getColumnModel().getColumn(2).setPreferredWidth(3);
            table.getColumnModel().getColumn(3).setPreferredWidth(200);
            table.getColumnModel().getColumn(4).setPreferredWidth(30);
            table.getColumnModel().getColumn(5).setPreferredWidth(80);
            table.getColumnModel().getColumn(6).setPreferredWidth(80);
            table.getColumnModel().getColumn(7).setPreferredWidth(80);
            table.getColumnModel().getColumn(8).setPreferredWidth(80);
            table.getColumnModel().getColumn(9).setPreferredWidth(80);

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (Integer.parseInt(table.getValueAt(row, 0).toString()) == 1)
                        cell.setBackground(Color.LIGHT_GRAY);
                    else if (Integer.parseInt(table.getValueAt(row, 0).toString()) == 4)
                        cell.setBackground(Color.PINK);
                    else if (table.getValueAt(row, 1).toString().equals("true"))
                        cell.setBackground(Color.YELLOW);
                    else
                        cell.setBackground(table.getBackground());
                    cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    return cell;
                }
            };
            for (int i = 0; i < col.size(); i++)
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);

            sorter = new TableRowSorter<TableModel>(tm) {
                @Override
                public Comparator<?> getComparator(int column) {
                    if (column == 2 || column == 4) {
                        return new Comparator<String>() {
                            public int compare(String s1, String s2) {
                                return Integer.parseInt(s1) - Integer.parseInt(s2);
                            }
                        };
                    }
                    if (column == 5 || column == 7) {
                        if (column == 5) format = "dd-MM-yyyy";
                        if (column == 7) format = "dd-MM-yyyy HH:mm:ss";
                        return new Comparator<String>() {
                            public int compare(String s1, String s2) {
                                try {
                                    return new SimpleDateFormat(format).parse(s2).compareTo(new SimpleDateFormat(format).parse(s1));
                                } catch (Exception e) {
                                    return s2.compareTo(s1);
                                }
                            }
                        };
                    }
                    return super.getComparator(column);
                }

                public void run() {
                    tm.fireTableStructureChanged();
                    tm.fireTableDataChanged();
                }
            };
            table.setRowSorter(sorter);

            jTabbedPane1.setComponentAt(jTabbedPane1.getModel().getSelectedIndex(), new JScrollPane(table));
        } catch (Exception e) {
            System.out.println("Ошибка! OrdersSklad() " + e);
        }
    }

    private void updateTabbedPane(int tmpselect) {
        jTabbedPane1.getModel().clearSelection();
        jTabbedPane1.getModel().setSelectedIndex(tmpselect);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton2.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jButton2.setText("Форма для печати");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton3.setText("Заявка выполнена");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabbedPane1.addTab("Невыполненные заявки", jTabbedPane2);
        jTabbedPane1.addTab("Выполненные заявки", jTabbedPane3);

        jLabel1.setText("Заявку составили в период:   с");

        jLabel2.setText("по");

        jButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton4.setText("Показать");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("Поиск");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jMenu1.setText("Справка");
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenuItem1.setText("Статус заявки");
        jMenuItem1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Горячие клавиши");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(209, 209, 209)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jFormattedTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                                .addGap(39, 39, 39)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(214, 214, 214))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(219, 219, 219)
                                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton4)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButton2)
                                                .addComponent(jButton3))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButton1)))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            if (table.getSelectedRowCount() > 0) {
                if (Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()) != 1) {
                    if (jTabbedPane1.getModel().getSelectedIndex() == 0) {
                        pdb.setOrderProcessSklad(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()), 3);
                    } else if (jTabbedPane1.getModel().getSelectedIndex() == 1) {
                        pdb.setOrderProcessSklad(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()), 2);
                        pdb.updateListOrdersPDB();
                    }
                    updateTabbedPane(jTabbedPane1.getModel().getSelectedIndex());
                } else
                    JOptionPane.showMessageDialog(null, "Заявка ещё не открыта!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Ничего не выделено!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (CartForm.checkDate(jFormattedTextField1.getText()) && CartForm.checkDate(jFormattedTextField2.getText())) {
            sDate = jFormattedTextField1.getText();
            eDate = jFormattedTextField2.getText();
            updateTabbedPane(jTabbedPane1.getModel().getSelectedIndex());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new Spravka(parentSkladForm, true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JOptionPane.showMessageDialog(null, "SPACE(пробел) - открыть окно \"Заявка №\" / \"Накладная №\" ;\nCTRL - открыть печатную форму;\nF5 - заявка выполнена/заявка не выполнена;", "Горячие клавиши", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            if (table.getSelectedRowCount() > 0) {
                if (pdb.downloadSavedOrder(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()), 2)) {
                    OpenOffice oo = new OpenOffice(this, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()), table.getValueAt(table.getSelectedRow(), 3).toString(), new SimpleDateFormat("yyyy.MM.dd").format(
                            new SimpleDateFormat("dd-MM-yyyy").parse(table.getValueAt(table.getSelectedRow(), 5).toString())), 2, idvaluta, kurs, nds, pdb.downloadNoteSavedOrder(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString())));
                    updateTabbedPane(jTabbedPane1.getModel().getSelectedIndex());
                    oo.createReport("ЗаявкаСклад.ots");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String text = jTextField1.getText().trim();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                if (table.getRowCount() == 0) {
                    sorter.setRowFilter(null);
                    JOptionPane.showMessageDialog(null, "По заданному запросу ничего не найдено", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        jTextField1.setText("");
        jTextField1.requestFocusInWindow();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        jButton1.doClick();
    }//GEN-LAST:event_jTextField1ActionPerformed
    // End of variables declaration//GEN-END:variables

    private class OrderDialog extends JDialog {
        private OrderDialog parentOrderDialog = this;

        public OrderDialog(SkladForm parent, boolean modal, final int idOrder, final int idClient, String nameClient) {
            super(parent, modal);
            this.setTitle("Заявка №" + idOrder + " для (" + idClient + ")" + nameClient);
            JPanel selectPanel = new JPanel();
            selectPanel.setLayout(new BorderLayout(1, 1));
            selectPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JPanel endPanel = new JPanel();
            endPanel.setLayout(new BorderLayout(1, 1));
            endPanel.setBorder(BorderFactory.createEmptyBorder(5, 1, 1, 1));
            selectPanel.add(endPanel, BorderLayout.SOUTH);

            JLabel temp_ = new JLabel(pdb.downloadNoteSavedOrder(idOrder));
            temp_.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            selectPanel.add(temp_, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 3, 5, 5));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

            JButton sbn = new JButton("Прикрепить накладную");
            sbn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    new NDocDialog(parentOrderDialog, true, idOrder, idClient);
                }
            });

            JButton cbn = new JButton("Закрыть");
            cbn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispose();
                }
            });

            buttonPanel.add(sbn);
            buttonPanel.add(cbn);
            endPanel.add(buttonPanel, BorderLayout.SOUTH);

            final JTable listTable = new JTable();

            Vector col = new Vector();
            col.add("Модель");
            col.add("Артикул");
            col.add("Наименование");
            col.add("Рост");
            col.add("Размер");
            col.add("Сорт");
            col.add("Цвет");
            col.add("Кол-во");

            Vector row = pdb.getOrder(idOrder, 2);

            DefaultTableModel list_tm = new DefaultTableModel(row, col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            listTable.setModel(list_tm);
            listTable.setAutoCreateColumnsFromModel(true);
            selectPanel.add(new JScrollPane(listTable), BorderLayout.CENTER);
            getContentPane().add(selectPanel);

            int sum = 0;
            for (int i = 0; i < row.size(); i++) sum += Integer.parseInt(listTable.getValueAt(i, 7).toString());
            JLabel label = new JLabel("ИТОГО:     " + new DecimalFormat("###,###.###").format(sum) + " шт.");
            label.setBorder(BorderFactory.createEmptyBorder(1, 1, 3, 5));
            label.setHorizontalAlignment(JLabel.RIGHT);
            buttonPanel.add(label, BorderLayout.EAST);

            setPreferredSize(new Dimension(850, 550));
            pack();
            setLocationRelativeTo(parent);
            setResizable(false);
            setVisible(true);
        }
    }

    private class NDocDialog extends JDialog {
        final TableRowSorter<TableModel> sorterNDoc;
        final DefaultListModel listModel;
        private NDocDialog parentNDocDialog = this;

        public NDocDialog(OrderDialog parent, boolean modal, final int idOrder, final int idClient) {
            super(parent, modal);
            this.setTitle("Накладные");

            listEmployeSklad();

            JPanel selectPanel = new JPanel();
            selectPanel.setLayout(new BorderLayout(1, 1));
            selectPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout(1, 1));
            mainPanel.setPreferredSize(new Dimension(200, 0));
            selectPanel.add(mainPanel, BorderLayout.EAST);

            JPanel endPanel = new JPanel();
            endPanel.setLayout(new BorderLayout(1, 1));
            endPanel.setBorder(BorderFactory.createEmptyBorder(10, 1, 1, 1));
            selectPanel.add(endPanel, BorderLayout.SOUTH);

            JPanel editPanel = new JPanel();
            editPanel.setLayout(new GridLayout(3, 3, 5, 1));
            mainPanel.add(editPanel, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2, 5, 5));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 1, 1, 1));

            endPanel.add(new JLabel("Принял к исполнению..."), BorderLayout.NORTH);
            cb.setSelectedIndex(0);
            endPanel.add(cb);
            endPanel.add(buttonPanel, BorderLayout.SOUTH);

            listModel = new DefaultListModel();
            Vector ndocs = pdb.getNDocs(idOrder);
            for (int i = 0; i < ndocs.size(); i++) {
                listModel.addElement(ndocs.get(i));
            }
            final JList listNdoc = new JList(listModel);
            mainPanel.add(new JScrollPane(listNdoc), BorderLayout.CENTER);

            JButton sbn = new JButton("Сохранить");
            sbn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Vector ndocsListModel = new Vector();
                    for (int i = 0; i < listModel.size(); i++) {
                        ndocsListModel.add(listModel.getElementAt(i));
                    }

                    Item itv = (Item) model.get(cb.getSelectedIndex());
                    Object gatherorder = itv.getId();
                    if (Integer.parseInt(gatherorder.toString()) == 0) gatherorder = null;

                    if (pdb.editNDocOrder(ndocsListModel, idOrder, gatherorder))
                        dispose();
                }
            });

            JButton cbn = new JButton("Отмена");
            cbn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispose();
                }
            });

            buttonPanel.add(sbn);
            buttonPanel.add(cbn);

            final JTextField text = new JTextField();
            editPanel.add(text);

            JButton addButton = new JButton("Добавить");
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String ndoc = text.getText().trim();
                    text.setText("");
                    if (!ndoc.isEmpty()) {
                        db = new DB();
                        if (db.testNDoc(ndoc, idClient)) {
                            int idOrderUsed = pdb.getNDocOtgruz(ndoc);
                            if (idOrderUsed == 0) {
                                if (!listModel.contains(ndoc)) {
                                    listModel.addElement(ndoc);
                                    int index = listModel.size() - 1;
                                    listNdoc.setSelectedIndex(index);
                                    listNdoc.ensureIndexIsVisible(index);
                                    sorterNDoc.setRowFilter(createRowFilter());
                                }
                            } else
                                JOptionPane.showMessageDialog(null, "Накладная уже привязана к заявке №" + idOrderUsed, "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } else
                            JOptionPane.showMessageDialog(null, "Накладная с таким номером для выбранного клиента не существует или помечена на удаление!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } else
                        JOptionPane.showMessageDialog(null, "В поле для ввода накладной пусто!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            });
            editPanel.add(addButton);

            final JButton removeButton = new JButton("Удалить");
            removeButton.setEnabled(false);
            removeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pdb.deleteNDoc(listNdoc.getSelectedValue());
                    listModel.remove(listNdoc.getSelectedIndex());
                    sorterNDoc.setRowFilter(createRowFilter());
                }
            });
            editPanel.add(removeButton);

            listNdoc.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (listNdoc.getSelectedIndex() >= 0)
                        removeButton.setEnabled(true);
                    else
                        removeButton.setEnabled(false);
                }
            });

            final JTable listTable = new JTable();

            Vector col = new Vector();
            col.add("накладная");
            col.add("дата");

            db = new DB();
            Vector row = db.getNDocs(idClient);

            DefaultTableModel list_tm = new DefaultTableModel(row, col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            listTable.setModel(list_tm);
            listTable.setAutoCreateColumnsFromModel(true);

            listTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() > 1) {
                        try {
                            Robot r = new Robot();
                            r.keyPress(KeyEvent.VK_SPACE);
                            r.keyRelease(KeyEvent.VK_SPACE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    } else text.setText(listTable.getValueAt(listTable.getSelectedRow(), 0).toString());
                }
            });

            listTable.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        if (listTable.getSelectedRowCount() > 0) {
                            new DetalNDocDialog(parentNDocDialog, true, listTable.getValueAt(listTable.getSelectedRow(), 0).toString());
                        } else
                            JOptionPane.showMessageDialog(null, "Ничего не выделено!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            sorterNDoc = new TableRowSorter<TableModel>(list_tm) {
                public Comparator<?> getComparator(int column) {
                    if (column == 1) {
                        format = "dd-MM-yyyy";
                        return new Comparator<String>() {
                            public int compare(String s1, String s2) {
                                try {
                                    return new SimpleDateFormat(format).parse(s2).compareTo(new SimpleDateFormat(format).parse(s1));
                                } catch (Exception e) {
                                    return s2.compareTo(s1);
                                }
                            }
                        };
                    }
                    return super.getComparator(column);
                }
            };
            sorterNDoc.setRowFilter(createRowFilter());
            listTable.setRowSorter(sorterNDoc);

            selectPanel.add(new JScrollPane(listTable), BorderLayout.CENTER);
            getContentPane().add(selectPanel);

            setPreferredSize(new Dimension(450, 450));
            pack();
            setLocationRelativeTo(parent);
            setResizable(false);
            setVisible(true);
        }

        private RowFilter createRowFilter() {
            RowFilter<TableModel, Object> rf = null;
            java.util.List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(listModel.size());

            for (int i = 0; i < listModel.size(); i++) {
                filters.add(RowFilter.regexFilter(listModel.getElementAt(i).toString()));
            }

            RowFilter<TableModel, Object> fooBarFilter = RowFilter.orFilter(filters);
            rf = RowFilter.notFilter(fooBarFilter);

            return rf;
        }

        private void listEmployeSklad() {
            Vector tmpemployee = pdb.getEmployeSklad();
            model.removeAllElements();
            cb.removeAllItems();

            model.add(new Item(0, "-"));
            for (int i = 0; i < tmpemployee.size(); i++) {
                Vector temp = (Vector) tmpemployee.elementAt(i);
                model.add(new Item(Integer.parseInt(temp.get(0).toString()), temp.get(1).toString()));
            }

            for (Iterator itr = model.iterator(); itr.hasNext(); ) {
                Item it = (Item) itr.next();
                cb.addItem(it.getDescription());
            }
        }
    }

    private class DetalNDocDialog extends JDialog {
        public DetalNDocDialog(NDocDialog parent, boolean modal, String ndoc) {
            super(parent, modal);
            this.setTitle("Накладная №" + ndoc);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(1, 1));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            final JTable tableDetal = new JTable();

            Vector col = new Vector();
            col.add("Модель");
            col.add("Артикул");
            col.add("Наименование");
            col.add("Размеры");
            col.add("Сорт");
            col.add("Цвет");
            col.add("Кол-во");
            col.add("Цена ед.");

            db = new DB();
            Vector row = db.getNaklAllDescr(ndoc, 0);

            DefaultTableModel tm = new DefaultTableModel(row, col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            tableDetal.setModel(tm);
            tableDetal.setAutoCreateColumnsFromModel(true);

            panel.add(new JScrollPane(tableDetal), BorderLayout.CENTER);
            getContentPane().add(panel);

            setPreferredSize(new Dimension(850, 300));
            pack();
            setLocationRelativeTo(parent);
            setResizable(false);
            setVisible(true);
        }
    }

    private class Spravka extends JDialog {
        int x = 10;
        int y = 10;

        public Spravka(SkladForm parent, boolean modal) {
            super(parent, modal);
            this.setTitle("Статус заявки");

            PanelWihtFone mainPanel = new PanelWihtFone();

            JLabel colorG = new JLabel();
            colorG.setOpaque(true);
            colorG.setBackground(Color.LIGHT_GRAY);
            colorG.setBounds(x, y, 320, 30);
            colorG.setText("Новая заявка");
            colorG.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(colorG);

            JLabel colorW = new JLabel();
            colorW.setOpaque(true);
            colorW.setBackground(Color.WHITE);
            colorW.setBounds(x, y + 50, 320, 30);
            colorW.setText("Заявка открыта");
            colorW.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(colorW);

            JLabel colorY = new JLabel();
            colorY.setOpaque(true);
            colorY.setBackground(Color.YELLOW);
            colorY.setBounds(x, y + 100, 320, 30);
            colorY.setText("Заявка изменена");
            colorY.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(colorY);

            JLabel colorR = new JLabel();
            colorR.setOpaque(true);
            colorR.setBackground(Color.PINK);
            colorR.setBounds(x, y + 150, 320, 30);
            colorR.setText("Заявка просрочена");
            colorR.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(colorR);

            getContentPane().add(mainPanel);
            setPreferredSize(new Dimension(350, 250));
            pack();
            setLocationRelativeTo(parent);
            setResizable(false);
            setVisible(true);
        }
    }

    private class Item {
        private int id;
        private String description;

        public Item(int id, String description) {
            this.id = id;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }
    }
}
