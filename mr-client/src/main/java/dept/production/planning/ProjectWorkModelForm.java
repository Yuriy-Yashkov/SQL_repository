package dept.production.planning;

import common.ProgressBar;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ProjectWorkModelForm extends javax.swing.JDialog {
    PlanPDB ppdb;
    private JPanel osnovaPanel;
    private JPanel buttPanel;
    private JButton buttClose;
    private JButton buttSave;
    private JButton buttTab1Cancel;
    private JButton buttTab1Minus;
    private JButton buttTab1Edit;
    private JPanel buttEastTab1Panel;
    private TableFilterHeader filterHeaderDetalTab1;
    private JTable tableDetalTab1;
    private int minSelectedRowDetalTab1 = -1;
    private int maxSelectedRowDetalTab1 = -1;
    private boolean tableDetalTab1ModelListenerIsChanging = false;
    private Vector colDetalTab1;
    private DefaultTableModel tModelDetalTab1;
    private JButton buttPrint;
    private ProgressBar pb;
    private Vector dataTable;

    public ProjectWorkModelForm(javax.swing.JDialog parent, boolean modal) {
        super(parent, modal);
        setTitle("Модели закрепленные за экономистами");

        init();

        createPlanDetalTab1Table(getData());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(550, 350));
        setPreferredSize(new Dimension(550, 550));

        colDetalTab1 = new Vector();
        colDetalTab1.add("");
        colDetalTab1.add("ID");
        colDetalTab1.add("Модель");
        colDetalTab1.add("Название");
        colDetalTab1.add("Примечание");

        tableDetalTab1 = new JTable();
        tableDetalTab1.setAutoCreateColumnsFromModel(true);
        tableDetalTab1.getTableHeader().setReorderingAllowed(false);
        tableDetalTab1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    buttTab1Edit.doClick();
                }
            }
        });
        tableDetalTab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowDetalTab1 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowDetalTab1 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableDetalTab1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttTab1Edit.doClick();
                }
            }
        });

        filterHeaderDetalTab1 = new TableFilterHeader(tableDetalTab1, AutoChoices.ENABLED);

        buttTab1Cancel = new JButton("отменить");
        buttTab1Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttTab1CancelActionPerformed(evt);
            }
        });

        buttTab1Minus = new JButton("-");
        buttTab1Minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttTab1MinusActionPerformed(evt);
            }
        });

        buttTab1Edit = new JButton("изменить");
        buttTab1Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1EditActionPerformed(evt);
            }
        });

        buttEastTab1Panel = new JPanel();
        buttEastTab1Panel.setLayout(new GridLayout(7, 0, 5, 5));
        buttEastTab1Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttEastTab1Panel.add(buttTab1Edit);
        //buttEastTab1Panel.add(buttTab1Cancel); 
        //buttEastTab2Panel.add(buttTab2Edit);  

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSave = new JButton("Сохранить");
        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttPrint = new JButton("Печать");
        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttSave);
        buttPanel.add(buttPrint);

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnovaPanel.add(buttEastTab1Panel, BorderLayout.EAST);
        osnovaPanel.add(new JScrollPane(tableDetalTab1), BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        try {

            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            boolean flagSelect = false;

            for (int i = 0; i < tableDetalTab1.getRowCount(); i++) {
                Vector vec = (Vector) tModelDetalTab1.getDataVector()
                        .get(tableDetalTab1.convertRowIndexToModel(i));

                if (vec.get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            if (flagSelect) {

                if (JOptionPane.showOptionDialog(null,
                        "Сохранить изменения?",
                        "Сохранение...",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION) {

                    pb = new ProgressBar(ProjectWorkModelForm.this, false, "Сохранение ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                ppdb = new PlanPDB();

                                Vector tmp = new Vector();

                                for (int i = 0; i < tableDetalTab1.getRowCount(); i++) {
                                    Vector vec = (Vector) tModelDetalTab1.getDataVector()
                                            .get(tableDetalTab1.convertRowIndexToModel(i));

                                    if (vec.get(0).toString().equals("true")) {
                                        tmp.add(vec);
                                    }
                                }


                                if (!tmp.isEmpty()) {

                                    ppdb.saveDataWorkModelProject(tmp);

                                    JOptionPane.showMessageDialog(null,
                                            "Сохранение завершено! ",
                                            "Внимание",
                                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                }

                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null,
                                        "Данные не сохранены! " + e.getMessage(),
                                        "Ошибка",
                                        javax.swing.JOptionPane.ERROR_MESSAGE);
                            } finally {
                                ppdb.disConn();
                            }
                            return null;
                        }

                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setVisible(true);

                    createPlanDetalTab1Table(getData());
                }
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали в таблице для сохранения!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            PlanOO oo = new PlanOO("", tModelDetalTab1, tableDetalTab1.getColumnModel());
            oo.createReport("DefaultTableBookFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1EditActionPerformed(ActionEvent evt) {
        try {
            boolean flagSelect = false;

            for (int i = 0; i < tableDetalTab1.getRowCount(); i++) {
                Vector vec = (Vector) tModelDetalTab1.getDataVector()
                        .get(tableDetalTab1.convertRowIndexToModel(i));

                if (vec.get(0).toString().equals("true")) {
                    flagSelect = true;
                    break;
                }
            }

            if (flagSelect) {
                JTextField text = new JTextField();

                if (JOptionPane.showOptionDialog(null,
                        text,
                        "Примечание ",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Добавить", "Отмена"}, "Добавить") == JOptionPane.YES_OPTION) {

                    for (int i = 0; i < tableDetalTab1.getRowCount(); i++) {
                        Vector vec = (Vector) tModelDetalTab1.getDataVector()
                                .get(tableDetalTab1.convertRowIndexToModel(i));

                        if (vec.get(0).toString().equals("true")) {
                            ((Vector) tModelDetalTab1.getDataVector()
                                    .get(tableDetalTab1.convertRowIndexToModel(i)))
                                    .setElementAt(text.getText().trim(), 4);
                        }
                    }
                    tModelDetalTab1.fireTableDataChanged();
                }

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали в таблице моделей!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createPlanDetalTab1Table(final Vector row) {
        tModelDetalTab1 = new DefaultTableModel(row, colDetalTab1) {
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

        tModelDetalTab1.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableDetalTab1ModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowDetalTab1 == -1 || minSelectedRowDetalTab1 == -1) {
                    return;
                }
                tableDetalTab1ModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelDetalTab1.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowDetalTab1; i <= maxSelectedRowDetalTab1; i++) {
                    tModelDetalTab1.setValueAt(Boolean.valueOf(value), tableDetalTab1.convertRowIndexToModel(i), column);
                }

                minSelectedRowDetalTab1 = -1;
                maxSelectedRowDetalTab1 = -1;

                tableDetalTab1ModelListenerIsChanging = false;
            }
        });

        tableDetalTab1.setModel(tModelDetalTab1);
        tableDetalTab1.setAutoCreateColumnsFromModel(true);

        tableDetalTab1.getColumnModel().getColumn(0).setPreferredWidth(20);
        tableDetalTab1.getColumnModel().getColumn(1).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(1).setMaxWidth(0);
        tableDetalTab1.getColumnModel().getColumn(2).setPreferredWidth(70);
        tableDetalTab1.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableDetalTab1.getColumnModel().getColumn(4).setPreferredWidth(150);

        //tableDetalTab1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableDetalTab1.getTableHeader(), 0, ""));                      
    }

    private Vector getData() {
        dataTable = new Vector();
        try {

            pb = new ProgressBar(ProjectWorkModelForm.this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        ppdb = new PlanPDB();

                        dataTable = ppdb.getDataWorkModelProject();

                    } catch (Exception e) {
                        dataTable = new Vector();
                        JOptionPane.showMessageDialog(null,
                                "Данные не загружены! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        ppdb.disConn();
                    }
                    return null;
                }

                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setVisible(true);

        } catch (Exception e) {
        }
        return dataTable;
    }

}
