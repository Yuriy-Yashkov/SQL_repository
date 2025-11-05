package dept.production.planning;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class TableTechForm extends javax.swing.JDialog {
    private Vector col;
    private Vector row;
    private JPanel osnova;
    private JPanel titlePanel;
    private JPanel buttPanel;
    private JPanel centerPanel;
    private JLabel title;
    private JButton buttClose;
    private JButton buttPrint;
    private JTextPane infoText;
    private JTable tableTech;
    private DefaultTableModel tModelTech;
    private JButton buttDetalTech;
    private JPanel filterRow;
    private TableColumnModelListener tableColumnModelListener;
    private TableFilterHeader filterHeader;

    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;

    private PlanPDB ppdb;
    private ProgressBar pb;
    private JMenuBar jMenuBar1;
    private JMenu jMenu1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;

    private int workDay;
    private boolean flagLoad = false;
    private MainController controller;

    public TableTechForm(MainController mainController, boolean modal,
                         Vector data, int idPlan, int workDay, String titleTextSelect, String titleTextKol, int flagKol) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        this.workDay = workDay;

        init();

        title.setText("Загрузка оборудования " + titleTextSelect + " плана №" + idPlan + " " + titleTextKol);

        createTechTable(getLoadTech(data, flagKol, UtilPlan.PLAN));

        addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                ppdb.disConn();
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

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public TableTechForm(ProjectDetalForm parent, boolean modal,
                         Vector data, int idProj, int workDay, String titleTextSelect, String titleTextKol, int flagKol) {
        super(parent, modal);
        controller = parent.getController();
        this.workDay = workDay;

        init();

        title.setText("Загрузка оборудования " + titleTextSelect + " " + titleTextKol);

        createTechTable(getLoadTech(data, flagKol, UtilPlan.PROJECT));

        addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                ppdb.disConn();
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

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setTitle("Просмотр загрузки оборудования");

        setMinimumSize(new Dimension(350, 350));
        setPreferredSize(new Dimension(650, 650));

        initMenu();

        osnova = new JPanel();
        titlePanel = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();

        title = new JLabel();
        buttClose = new JButton("Закрыть");
        buttPrint = new JButton("Печать");
        buttDetalTech = new JButton("Подробно");
        col = new Vector();
        tableTech = new JTable();
        tModelTech = new DefaultTableModel();
        filterHeader = new TableFilterHeader(tableTech, AutoChoices.ENABLED);
        filterRow = new JPanel();
        infoText = new JTextPane();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.setLayout(new ParagraphLayout());
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        filterRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        infoText.setPreferredSize(new Dimension(330, 80));

        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 16));

        tableTech.setAutoCreateColumnsFromModel(true);
        tableTech.getTableHeader().setReorderingAllowed(false);

        col.add("");
        col.add("idTech");
        col.add("Оборудование");
        col.add("Трудоемкость");

        tableTech.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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

        tableColumnModelListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableTech.getColumnModel();
                if (filterRow.getComponents().length > 0) {
                    for (int i = 0; i < tcm.getColumnCount(); i++) {
                        JTextField textField = (JTextField) filterRow.getComponent(i);
                        Dimension d = textField.getPreferredSize();
                        d.width = tcm.getColumn(i).getWidth();
                        textField.setPreferredSize(d);
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            filterRow.revalidate();
                        }
                    });
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterRow.getComponent(e.getFromIndex());
                filterRow.remove(e.getFromIndex());
                filterRow.add(moved, e.getToIndex());
                filterRow.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        buttDetalTech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttDetalTechActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        centerPanel.add(new JScrollPane(tableTech), BorderLayout.CENTER);
        centerPanel.add(filterRow, BorderLayout.SOUTH);

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        buttPanel.add(buttDetalTech);

        osnova.add(title, BorderLayout.NORTH);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Сервис");

        jMenuItem1.setText("Модели");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Загрузка оборудования");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);
    }

    private void jMenuItem1ActionPerformed(ActionEvent evt) {
        try {
            infoText.setText("");

            Vector models = ppdb.getAllModelsLoadTechInPlanProduction();

            infoText.setText(infoText.getText() + models.toString() + "\n");

            JOptionPane.showOptionDialog(null, new JScrollPane(infoText), "Модели ", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Закрыть"}, "Закрыть");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem2ActionPerformed(ActionEvent evt) {
        try {
            if (!flagLoad) {

                flagLoad = true;

                tableTech.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
                tableTech.getActionMap().put("key-enter", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        //int colum = tableTech.getSelectedColumn();
                        tableTech.editCellAt(tableTech.getSelectedRow(), tableTech.getSelectedColumn());
                        Component editor = tableTech.getEditorComponent();
                        if (editor != null) {
                            editor.requestFocus();
                            if (editor instanceof JTextField) {
                                ((JTextField) editor).selectAll();
                            }
                        }
                    }
                });

                for (int i = 0; i < tModelTech.getDataVector().size(); i++) {
                    Vector vv = (Vector) tModelTech.getDataVector().elementAt(i);
                    vv.addElement(0);
                }

                tModelTech.addColumn("Кол-во швей");

                for (int i = 0; i < tModelTech.getDataVector().size(); i++) {
                    Vector vv = (Vector) tModelTech.getDataVector().elementAt(i);
                    vv.addElement(workDay * 8);
                }
                tModelTech.addColumn("Раб. часы");

                for (int i = 0; i < tModelTech.getDataVector().size(); i++) {
                    Vector vv = (Vector) tModelTech.getDataVector().elementAt(i);
                    vv.addElement(new Double(0));
                }
                tModelTech.addColumn("Загрузка(%)");

                tModelTech.fireTableStructureChanged();

                tableTech.getColumnModel().getColumn(1).setMinWidth(0);
                tableTech.getColumnModel().getColumn(1).setMaxWidth(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            PlanOO oo = new PlanOO(title.getText(), tModelTech, tableTech.getColumnModel());
            oo.createReport("DefaultTableBookFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttDetalTechActionPerformed(ActionEvent evt) {
        try {
            if (tableTech.getSelectedRow() != -1) {
                new SmallTableForm(controller, this, true,
                        ppdb.getTechDetalInPlanProduction(Integer.valueOf(tableTech.getValueAt(tableTech.getSelectedRow(), 1).toString())),
                        tableTech.getValueAt(tableTech.getSelectedRow(), 2).toString());
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали оборудование!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void createTechTable(final Vector data) {
        tModelTech = new DefaultTableModel(data, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (data.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 1 || col == 2 || col == 3) ? false : true;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);

                    if (value != null) {
                        if (col == 4) {
                            rowVector.setElementAt(Integer.valueOf(value.toString()), col);
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm((Double.valueOf(tableTech.getValueAt(row, 3).toString()) * 100)
                                    / (Integer.valueOf(value.toString()) * Integer.valueOf(tableTech.getValueAt(row, 5).toString())), 1)) * 100, 6);
                            fireTableCellUpdated(row, 6);
                        } else if (col == 5) {
                            rowVector.setElementAt(Integer.valueOf(value.toString()), col);
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm((Double.valueOf(tableTech.getValueAt(row, 3).toString()) * 100)
                                    / (Integer.valueOf(value.toString()) * Integer.valueOf(tableTech.getValueAt(row, 4).toString())), 1)) * 100, 6);
                            fireTableCellUpdated(row, 6);
                        } else {
                            rowVector.setElementAt(value, col);
                        }
                    } else {
                        rowVector.setElementAt(value, col);
                    }

                    fireTableCellUpdated(row, col);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + ee.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        tModelTech.addTableModelListener(new TableModelListener() {
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
                boolean value = ((Boolean) tModelTech.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                    tModelTech.setValueAt(Boolean.valueOf(value), tableTech.convertRowIndexToModel(i), column);
                }

                minSelectedRow = -1;
                maxSelectedRow = -1;

                tableModelListenerIsChanging = false;
            }
        });

        tableTech.setModel(tModelTech);
        tableTech.setAutoCreateColumnsFromModel(true);

        tableTech.getColumnModel().getColumn(0).setPreferredWidth(30);
        tableTech.getColumnModel().getColumn(1).setMinWidth(0);
        tableTech.getColumnModel().getColumn(1).setMaxWidth(0);
        tableTech.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableTech.getColumnModel().getColumn(3).setPreferredWidth(150);

        tableTech.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableTech.getTableHeader(), 0, ""));

        tableTech.getColumnModel().addColumnModelListener(tableColumnModelListener);

        filterHeader.getTable().getRowSorter().addRowSorterListener(new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.setFooterTable(tableTech, filterRow, UtilPlan.COL_TECH, UtilPlan.countSumm(tableTech, UtilPlan.COL_TECH));
            }
        });
    }

    private Vector getLoadTech(final Vector data, final int flag, final String type) {
        row = new Vector();
        try {
            pb = new ProgressBar(this, false, "Обработка данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        ppdb = new PlanPDB();
                        if (type.equals(UtilPlan.PLAN)) {
                            if (ppdb.createTableLoadTechInPlanProduction(data, flag)) {
                                row = ppdb.getLoadTechInPlanProduction();
                            }
                        }
                        if (type.equals(UtilPlan.PROJECT)) {
                            if (ppdb.createTableLoadTechInProjectPlan(data, flag)) {
                                row = ppdb.getLoadTechInPlanProduction();
                            }
                        }
                        dispose();
                    } catch (Exception e) {
                        row = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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
            row = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return row;
    }
}
