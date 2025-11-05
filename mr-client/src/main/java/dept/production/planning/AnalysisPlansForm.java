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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class AnalysisPlansForm extends javax.swing.JFrame {
    boolean EDIT;
    boolean LOOK;
    PlanPDB ppdb;
    PlanDB pdb;
    private JButton buttClose;
    private JButton buttPrint;
    private JButton buttDetal;
    private JPanel osnova;
    private JPanel selectPanel;
    private JPanel buttPanel;
    private JPanel centerPanel;
    private JPanel filterRow;
    private JTable table;
    private DefaultTableModel tModel;
    private TableFilterHeader filterHeader;
    private Vector col;
    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;
    private JPanel tablePanel;
    private TableColumnModelListener tableColumnModelListener;
    private RowSorterListener tableSorterListener;
    private DefaultTableCellRenderer renderer;
    private ProgressBar pb;
    private JTextField projPlan;
    private JTextField prodPlan;
    private JTextField vypuskPlan;
    private JTextField projNumPlan;
    private JTextField prodNumPlan;
    private JTextField vypuskNumPlan;
    private JButton buttProj;
    private JButton buttPlan;
    private JButton buttVypusk;
    private JButton buttCreate;
    private Vector dataAnalysis;
    private Vector rowProj;
    private Vector rowPlan;
    private Vector rowVypusk;

    private MainController controller;

    public AnalysisPlansForm(MainController controller, boolean modal, int idProj, String nameProj, Vector dataAnalysis) {

        super("Анализ выполнения");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.controller = controller;

        try {

            init();

            projNumPlan.setText(String.valueOf(idProj));
            projPlan.setText(nameProj);

            createTable(dataAnalysis);

            filterHeader.getTable().getRowSorter().addRowSorterListener(tableSorterListener);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка формирования анализа выполнения! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(850, 550));
        setPreferredSize(new Dimension(1000, 750));

        col = new Vector();
        col.add("");
        col.add("Вид");
        col.add("Название");
        col.add("Модель");
        col.add("Проект(шт.)");
        col.add("План(шт.)");
        col.add("Факт(шт.)");
        col.add("Новинка");

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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
        table.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttDetal.doClick();
                }
            }
        });

        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        filterRow = new JPanel();
        filterRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tableColumnModelListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = table.getColumnModel();

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

        tableSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(table, filterRow, UtilPlan.COL_ANALYSIS);
            }
        };

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    if (table.getValueAt(row, table.getColumnCount() - 1).toString().equals(UtilPlan.NEW))
                        cell.setForeground(Color.RED);
                    else
                        cell.setForeground(table.getForeground());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }
        };

        projNumPlan = new JTextField();
        projNumPlan.setPreferredSize(new Dimension(50, 20));
        projNumPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttProj.doClick();
            }
        });

        projPlan = new JTextField();
        projPlan.setPreferredSize(new Dimension(150, 20));
        projPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttProj.doClick();
            }
        });

        prodNumPlan = new JTextField();
        prodNumPlan.setPreferredSize(new Dimension(50, 20));
        prodNumPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttPlan.doClick();
            }
        });

        prodPlan = new JTextField();
        prodPlan.setPreferredSize(new Dimension(150, 20));
        prodPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttPlan.doClick();
            }
        });

        vypuskNumPlan = new JTextField();
        vypuskNumPlan.setPreferredSize(new Dimension(50, 20));
        vypuskNumPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttVypusk.doClick();
            }
        });

        vypuskPlan = new JTextField();
        vypuskPlan.setPreferredSize(new Dimension(150, 20));
        vypuskPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttVypusk.doClick();
            }
        });

        buttProj = new JButton("..");
        buttProj.setPreferredSize(new Dimension(20, 20));
        buttProj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttProjActionPerformed(evt);
            }
        });

        buttPlan = new JButton("..");
        buttPlan.setPreferredSize(new Dimension(20, 20));
        buttPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttProdActionPerformed(evt);
            }
        });

        buttVypusk = new JButton("..");
        buttVypusk.setPreferredSize(new Dimension(20, 20));
        buttVypusk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttVypuskActionPerformed(evt);
            }
        });

        buttCreate = new JButton("Обновить");
        buttCreate.setPreferredSize(new Dimension(200, 25));
        buttCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCreateActionPerformed(evt);
            }
        });

        buttDetal = new JButton("Детали");
        buttDetal.setPreferredSize(new Dimension(200, 25));
        buttDetal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttDetalActionPerformed(evt);
            }
        });

        selectPanel = new JPanel();
        selectPanel.setLayout(new ParagraphLayout());
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(new JLabel("Проект: "));
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(projNumPlan);
        selectPanel.add(projPlan);
        selectPanel.add(buttProj);
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(new JLabel("План: "));
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(prodNumPlan);
        selectPanel.add(prodPlan);
        selectPanel.add(buttPlan);
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(new JLabel("Факт: "));
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(vypuskNumPlan);
        selectPanel.add(vypuskPlan);
        selectPanel.add(buttVypusk);
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(buttCreate);
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        selectPanel.add(buttDetal);

        buttPrint = new JButton("Печать");
        buttPrint.setPreferredSize(new Dimension(150, 20));
        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        buttClose = new JButton("Закрыть");
        buttClose.setPreferredSize(new Dimension(150, 20));
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(1, 1));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.add(filterRow, BorderLayout.SOUTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);

        osnova = new JPanel();
        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnova.add(selectPanel, BorderLayout.EAST);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            PlanOO oo = new PlanOO("Анализ выполнения.", tModel, table.getColumnModel());
            oo.createReport("DefaultTableBookFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        try {
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttProjActionPerformed(ActionEvent evt) {
        try {
            new ProjectPlanForm(controller, true, "", "");

            if (UtilPlan.ACTION_BUTT_PLAN_SELECT) {
                projNumPlan.setText(String.valueOf(UtilPlan.PLAN_SELECT_NUM));
                projPlan.setText(String.valueOf(UtilPlan.PLAN_SELECT_NAME));
            }

        } catch (Exception e) {
            projNumPlan.setText("");
            projPlan.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttProdActionPerformed(ActionEvent evt) {
        try {
            new PlanProductioForm(controller, true, UtilPlan.PLAN);

            if (UtilPlan.ACTION_BUTT_PLAN_SELECT) {
                prodNumPlan.setText(String.valueOf(UtilPlan.PLAN_SELECT_NUM));
                prodPlan.setText(String.valueOf(UtilPlan.PLAN_SELECT_NAME));
            }

        } catch (Exception e) {
            prodNumPlan.setText("");
            prodPlan.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttVypuskActionPerformed(ActionEvent evt) {
        try {
            new PlanProductioForm(controller, true, UtilPlan.VYPUSK);

            if (UtilPlan.ACTION_BUTT_PLAN_SELECT) {
                vypuskNumPlan.setText(String.valueOf(UtilPlan.PLAN_SELECT_NUM));
                vypuskPlan.setText(String.valueOf(UtilPlan.PLAN_SELECT_NAME));
            }

        } catch (Exception e) {
            vypuskNumPlan.setText("");
            vypuskPlan.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCreateActionPerformed(ActionEvent evt) {
        try {
            boolean flag = true;

            String str = "";

            if (projNumPlan.getText().trim().equals("")) {
                flag = false;
                str += "Вы не выбрали проект плана!\n";
            }

            if (prodNumPlan.getText().trim().equals("")) {
                flag = false;
                str += "Вы не выбрали план производства!\n";
            }

            if (vypuskNumPlan.getText().trim().equals("")) {
                flag = false;
                str += "Вы не выбрали факт выполнения!\n";
            }

            if (!flag) {
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            if (flag) {
                filterHeader.getTable().getRowSorter().removeRowSorterListener(tableSorterListener);

                createTable(getDataAnalysisPlans(Integer.valueOf(projNumPlan.getText()),
                        Integer.valueOf(prodNumPlan.getText()),
                        Integer.valueOf(vypuskNumPlan.getText())));

                filterHeader.getTable().getRowSorter().addRowSorterListener(tableSorterListener);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttDetalActionPerformed(ActionEvent evt) {
        try {

            if (table.getSelectedRow() != -1) {
                rowProj = new Vector();
                rowPlan = new Vector();
                rowVypusk = new Vector();

                pb = new ProgressBar(AnalysisPlansForm.this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            ppdb = new PlanPDB();

                            if (!projNumPlan.getText().trim().equals(""))
                                rowProj = ppdb.getDetalFasProject(Integer.valueOf(projNumPlan.getText().trim()),
                                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString()));
                            if (!prodNumPlan.getText().trim().equals(""))
                                rowPlan = ppdb.getDetalFasPlan(Integer.valueOf(prodNumPlan.getText().trim()),
                                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString()));
                            if (!vypuskNumPlan.getText().trim().equals(""))
                                rowVypusk = ppdb.getDetalFasVypusk(Integer.valueOf(vypuskNumPlan.getText().trim()),
                                        Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString()));

                        } catch (Exception e) {
                            rowProj = new Vector();
                            rowPlan = new Vector();
                            rowVypusk = new Vector();
                            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            ppdb.disConn();
                        }


                        pb.setMessage("Поиск наименований...");

                        Map tmpMap = new HashMap();
                        try {
                            pdb = new PlanDB();

                            for (int i = 0; i < rowPlan.size(); i++) {
                                Vector itemRow = (Vector) rowPlan.get(i);
                                Vector tmp = new Vector();

                                if (tmpMap.get(itemRow.get(0).toString().trim()) == null &&
                                        !itemRow.get(0).toString().trim().equals("")) {

                                    tmp = pdb.getDetalModel(Integer.valueOf(itemRow.get(0).toString()));

                                    if (tmp.size() > 0) {
                                        itemRow.set(1, tmp.get(0));
                                        itemRow.set(3, tmp.get(1));
                                        itemRow.set(4, tmp.get(2));

                                        tmpMap.put(itemRow.get(0).toString().trim(), tmp);
                                    }
                                } else {
                                    tmp = (Vector) tmpMap.get(itemRow.get(0).toString().trim());

                                    if (tmp != null) {
                                        if (tmp.size() > 0) {
                                            itemRow.set(1, tmp.get(0));
                                            itemRow.set(3, tmp.get(1));
                                            itemRow.set(4, tmp.get(2));
                                        }
                                    }
                                }

                                rowPlan.set(i, itemRow);

                            }

                            for (int i = 0; i < rowVypusk.size(); i++) {
                                Vector itemRow = (Vector) rowVypusk.get(i);
                                Vector tmp = new Vector();

                                if (tmpMap.get(itemRow.get(0).toString().trim()) == null &&
                                        !itemRow.get(0).toString().trim().equals("")) {

                                    tmp = pdb.getDetalModel(Integer.valueOf(itemRow.get(0).toString()));

                                    if (tmp.size() > 0) {
                                        itemRow.set(1, tmp.get(0));
                                        itemRow.set(3, tmp.get(1));
                                        itemRow.set(4, tmp.get(2));

                                        tmpMap.put(itemRow.get(0).toString().trim(), tmp);
                                    }
                                } else {
                                    tmp = (Vector) tmpMap.get(itemRow.get(0).toString().trim());

                                    if (tmp != null) {
                                        if (tmp.size() > 0) {
                                            itemRow.set(1, tmp.get(0));
                                            itemRow.set(3, tmp.get(1));
                                            itemRow.set(4, tmp.get(2));
                                        }
                                    }
                                }

                                rowVypusk.set(i, itemRow);

                            }

                        } catch (Exception e) {

                        } finally {
                            pdb.disConn();
                        }

                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                new AnalysisPlansDetalForm(
                        AnalysisPlansForm.this,
                        true,
                        rowProj,
                        rowPlan,
                        rowVypusk);
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private Vector getDataAnalysisPlans(final int idProj, final int idPlan, final int idVypusk) {
        dataAnalysis = new Vector();

        pb = new ProgressBar(AnalysisPlansForm.this, false, "Сбор данных ...");
        SwingWorker sw = new SwingWorker() {
            protected Object doInBackground() {
                try {
                    ppdb = new PlanPDB();

                    dataAnalysis = ppdb.getDataAnalysisPlans(idProj, idPlan, idVypusk);

                } catch (Exception e) {
                    dataAnalysis = new Vector();
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

        return dataAnalysis;
    }

    private void createTable(final Vector rowAll) {
        tModel = new DefaultTableModel(rowAll, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowAll.isEmpty())
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
                    tModel.setValueAt(Boolean.valueOf(value), table.convertRowIndexToModel(i), column);
                }

                minSelectedRow = -1;
                maxSelectedRow = -1;

                tableModelListenerIsChanging = false;
            }
        });

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);

        table.getColumnModel().getColumn(2).setCellRenderer(renderer);

        table.getColumnModel().addColumnModelListener(tableColumnModelListener);
        UtilPlan.setFooterTable(table, filterRow, UtilPlan.COL_ANALYSIS, UtilPlan.countSumm(table, UtilPlan.COL_ANALYSIS));

        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }

}
