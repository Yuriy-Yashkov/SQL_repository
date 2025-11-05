package dept.production.planning;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import common.CheckBoxHeader;
import common.ProgressBar;
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
public class TableMaterialForm extends javax.swing.JDialog {
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
    private JTable tableMaterial;
    private DefaultTableModel tModelMaterial;
    private JButton buttDetalMaterial;
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
    private String type;
    private int idProj;
    private MainController controller;

    public TableMaterialForm(ProjectDetalForm parent, boolean modal,
                             Vector data, int idProj, String titleTextSelect, String titleTextKol, int flagKol, final String type) {
        super(parent, modal);
        controller = parent.getController();


        this.idProj = idProj;
        this.type = type;

        if (type.equals(UtilPlan.PROJECT_ADD) ||
                type.equals(UtilPlan.PROJECT_COPY) ||
                type.equals(UtilPlan.PROJECT_EDIT)) {
            ppdb = parent.ppdb;
        }

        init();

        title.setText("Расход сырья " + titleTextSelect +
                ((type.equals(UtilPlan.PROJECT_ADD) || type.equals(UtilPlan.PROJECT_COPY)) ? "" : " плана №" + idProj) +
                " " + titleTextKol);

        createMaterialTable(getRashodMaterial(data, flagKol));

        addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                if (type.equals(UtilPlan.PROJECT_OPEN))
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
        setTitle("Просмотр расхода сырья");

        setMinimumSize(new Dimension(350, 350));
        setPreferredSize(new Dimension(750, 550));

        initMenu();

        osnova = new JPanel();
        titlePanel = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();

        title = new JLabel();
        buttClose = new JButton("Закрыть");
        buttPrint = new JButton("Печать");
        buttDetalMaterial = new JButton("Подробно");
        col = new Vector();
        tableMaterial = new JTable();
        tModelMaterial = new DefaultTableModel();
        filterHeader = new TableFilterHeader(tableMaterial, AutoChoices.ENABLED);
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

        tableMaterial.setAutoCreateColumnsFromModel(true);
        tableMaterial.getTableHeader().setReorderingAllowed(false);

        col.add("");
        col.add("Код");
        col.add("Артикул");
        col.add("Расход сырья");

        tableMaterial.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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
                TableColumnModel tcm = tableMaterial.getColumnModel();
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

        buttDetalMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttDetalMaterialActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        centerPanel.add(new JScrollPane(tableMaterial), BorderLayout.CENTER);
        centerPanel.add(filterRow, BorderLayout.SOUTH);

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        buttPanel.add(buttDetalMaterial);

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

                tableMaterial.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
                tableMaterial.getActionMap().put("key-enter", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        //int colum = tableTech.getSelectedColumn();
                        tableMaterial.editCellAt(tableMaterial.getSelectedRow(), tableMaterial.getSelectedColumn());
                        Component editor = tableMaterial.getEditorComponent();
                        if (editor != null) {
                            editor.requestFocus();
                            if (editor instanceof JTextField) {
                                ((JTextField) editor).selectAll();
                            }
                        }
                    }
                });

                for (int i = 0; i < tModelMaterial.getDataVector().size(); i++) {
                    Vector vv = (Vector) tModelMaterial.getDataVector().elementAt(i);
                    vv.addElement(0);
                }

                tModelMaterial.addColumn("Кол-во швей");

                for (int i = 0; i < tModelMaterial.getDataVector().size(); i++) {
                    Vector vv = (Vector) tModelMaterial.getDataVector().elementAt(i);
                    vv.addElement(workDay * 8);
                }
                tModelMaterial.addColumn("Раб. часы");

                for (int i = 0; i < tModelMaterial.getDataVector().size(); i++) {
                    Vector vv = (Vector) tModelMaterial.getDataVector().elementAt(i);
                    vv.addElement(new Double(0));
                }
                tModelMaterial.addColumn("Загрузка(%)");

                tModelMaterial.fireTableStructureChanged();

                tableMaterial.getColumnModel().getColumn(1).setMinWidth(0);
                tableMaterial.getColumnModel().getColumn(1).setMaxWidth(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            PlanOO oo = new PlanOO(title.getText(), tModelMaterial, tableMaterial.getColumnModel());
            oo.createReport("DefaultTableBookFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttDetalMaterialActionPerformed(ActionEvent evt) {
        try {
            if (tableMaterial.getSelectedRow() != -1) {
                Vector data = new Vector();

                if (type.equals(UtilPlan.PROJECT_ADD) ||
                        type.equals(UtilPlan.PROJECT_COPY) ||
                        type.equals(UtilPlan.PROJECT_EDIT)) {
                    try {
                        data = ppdb.getRashodMaterialDetalInProjectTemp(
                                Integer.valueOf(tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 1).toString()),
                                tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 2).toString());
                    } catch (Exception e) {
                        data = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                } else if (type.equals(UtilPlan.PROJECT_OPEN)) {
                    try {
                        data = ppdb.getRashodMaterialDetalInProject(
                                Integer.valueOf(tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 1).toString()),
                                tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 2).toString(),
                                idProj);
                    } catch (Exception e) {
                        data = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }

                new SmallTableForm(controller, TableMaterialForm.this, true, data, tableMaterial.getValueAt(tableMaterial.getSelectedRow(), 2).toString());
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали оборудование!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void createMaterialTable(final Vector data) {
        tModelMaterial = new DefaultTableModel(data, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (data.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col != 0) ? false : true;
            }
        };

        tModelMaterial.addTableModelListener(new TableModelListener() {
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
                boolean value = ((Boolean) tModelMaterial.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                    tModelMaterial.setValueAt(Boolean.valueOf(value), tableMaterial.convertRowIndexToModel(i), column);
                }

                minSelectedRow = -1;
                maxSelectedRow = -1;

                tableModelListenerIsChanging = false;
            }
        });

        tableMaterial.setModel(tModelMaterial);
        tableMaterial.setAutoCreateColumnsFromModel(true);

        tableMaterial.getColumnModel().getColumn(0).setPreferredWidth(30);
        tableMaterial.getColumnModel().getColumn(1).setPreferredWidth(50);
        tableMaterial.getColumnModel().getColumn(2).setPreferredWidth(60);
        tableMaterial.getColumnModel().getColumn(3).setPreferredWidth(150);

        tableMaterial.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableMaterial.getTableHeader(), 0, ""));

        tableMaterial.getColumnModel().addColumnModelListener(tableColumnModelListener);

        filterHeader.getTable().getRowSorter().addRowSorterListener(new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.setFooterTable(tableMaterial, filterRow, UtilPlan.COL_MATERIAL, UtilPlan.countSumm(tableMaterial, UtilPlan.COL_MATERIAL));
            }
        });
    }

    private Vector getRashodMaterial(final Vector data, final int flag) {
        row = new Vector();
        try {
            pb = new ProgressBar(this, false, "Обработка данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        if (type.equals(UtilPlan.PROJECT_ADD) ||
                                type.equals(UtilPlan.PROJECT_COPY) ||
                                type.equals(UtilPlan.PROJECT_EDIT)) {

                            if (ppdb.createTableRashodMaterialInProjectPlan(data, flag)) {
                                row = ppdb.getRashodMaterialProjectTemp();
                            }

                        } else if (type.equals(UtilPlan.PROJECT_OPEN)) {
                            try {
                                ppdb = new PlanPDB();
                                if (ppdb.createTableRashodMaterialInProjectPlan(data, flag)) {
                                    row = ppdb.getRashodMaterialProject(idProj);
                                }
                            } catch (Exception e) {
                                row = new Vector();
                                JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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
