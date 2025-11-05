package dept.production.planning;

import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class AnalysisPlansDetalForm extends JDialog {
    private JPanel osnova;
    private JPanel centerPanel;
    private Vector colProj;
    private Vector colPlan;
    private Vector colVypusk;
    private JPanel buttPanel;
    private JTable tableProj;
    private TableFilterHeader filterHeaderProj;
    private JTable tablePlan;
    private TableFilterHeader filterHeaderPlan;
    private JTable tableVypusk;
    private TableFilterHeader filterHeaderVypusk;
    private JButton buttClose;
    private DefaultTableModel tModelProj;
    private DefaultTableModel tModelPlan;
    private DefaultTableModel tModelVypusk;
    private JPanel tableProjPanel;
    private JPanel tablePlanPanel;
    private JPanel tableVypuskPanel;
    private JPanel filterProjRow;
    private JPanel filterPlanRow;
    private JPanel filterVypuskRow;
    private TableColumnModelListener tableProjColumnModelListener;
    private TableColumnModelListener tablePlanColumnModelListener;
    private TableColumnModelListener tableVypuskColumnModelListener;
    private RowSorterListener tableProjSorterListener;
    private RowSorterListener tablePlanSorterListener;
    private RowSorterListener tableVypuskSorterListener;

    public AnalysisPlansDetalForm(JFrame parent, boolean modal,
                                  Vector dataProj,
                                  Vector dataPlan,
                                  Vector dataVypusk) {

        super(parent, modal);
        setTitle("Детали...");

        init();

        createTableProj(dataProj);
        createTablePlan(dataPlan);
        createTableVypusk(dataVypusk);

        filterHeaderProj.getTable().getRowSorter().addRowSorterListener(tableProjSorterListener);
        filterHeaderPlan.getTable().getRowSorter().addRowSorterListener(tablePlanSorterListener);
        filterHeaderVypusk.getTable().getRowSorter().addRowSorterListener(tableVypuskSorterListener);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(850, 550));
        setPreferredSize(new Dimension(900, 650));

        colProj = new Vector();
        colProj.add("Вид");
        colProj.add("Название");
        colProj.add("Модель");
        colProj.add("Полотно");
        colProj.add("К-т");
        colProj.add("Цвет");
        colProj.add("Примечание");
        colProj.add("Новинка");
        colProj.add("Размер");
        colProj.add("Кол-во");

        colPlan = new Vector();
        colPlan.add("Шифр");
        colPlan.add("Название");
        colPlan.add("Модель");
        colPlan.add("Артикул");
        colPlan.add("Состав");
        colPlan.add("Рост");
        colPlan.add("Размер");
        colPlan.add("Кол-во");

        colVypusk = new Vector();
        colVypusk.add("Шифр");
        colVypusk.add("Название");
        colVypusk.add("Модель");
        colVypusk.add("Артикул");
        colVypusk.add("Состав");
        colVypusk.add("Рост");
        colVypusk.add("Размер");
        colVypusk.add("Кол-во");

        tableProj = new JTable();
        tableProj.setAutoCreateColumnsFromModel(true);
        tableProj.getTableHeader().setReorderingAllowed(false);
        filterHeaderProj = new TableFilterHeader(tableProj, AutoChoices.ENABLED);

        filterProjRow = new JPanel();
        filterProjRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tableProjColumnModelListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableProj.getColumnModel();

                for (int i = 0; i < tcm.getColumnCount(); i++) {
                    JTextField textField = (JTextField) filterProjRow.getComponent(i);
                    Dimension d = textField.getPreferredSize();
                    d.width = tcm.getColumn(i).getWidth();
                    textField.setPreferredSize(d);
                }

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        filterProjRow.revalidate();
                    }
                });
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterProjRow.getComponent(e.getFromIndex());
                filterProjRow.remove(e.getFromIndex());
                filterProjRow.add(moved, e.getToIndex());
                filterProjRow.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };

        tableProjSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableProj, filterProjRow, UtilPlan.COL_ANALYSIS_PROJ);
            }
        };

        tablePlan = new JTable();
        tablePlan.setAutoCreateColumnsFromModel(true);
        tablePlan.getTableHeader().setReorderingAllowed(false);
        filterHeaderPlan = new TableFilterHeader(tablePlan, AutoChoices.ENABLED);

        filterPlanRow = new JPanel();
        filterPlanRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tablePlanColumnModelListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tablePlan.getColumnModel();

                for (int i = 0; i < tcm.getColumnCount(); i++) {
                    JTextField textField = (JTextField) filterPlanRow.getComponent(i);
                    Dimension d = textField.getPreferredSize();
                    d.width = tcm.getColumn(i).getWidth();
                    textField.setPreferredSize(d);
                }

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        filterPlanRow.revalidate();
                    }
                });
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterPlanRow.getComponent(e.getFromIndex());
                filterPlanRow.remove(e.getFromIndex());
                filterPlanRow.add(moved, e.getToIndex());
                filterPlanRow.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };

        tablePlanSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tablePlan, filterPlanRow, UtilPlan.COL_ANALYSIS_PLAN);
            }
        };

        tableVypusk = new JTable();
        tableVypusk.setAutoCreateColumnsFromModel(true);
        tableVypusk.getTableHeader().setReorderingAllowed(false);
        filterHeaderVypusk = new TableFilterHeader(tableVypusk, AutoChoices.ENABLED);

        filterVypuskRow = new JPanel();
        filterVypuskRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tableVypuskColumnModelListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableVypusk.getColumnModel();

                for (int i = 0; i < tcm.getColumnCount(); i++) {
                    JTextField textField = (JTextField) filterVypuskRow.getComponent(i);
                    Dimension d = textField.getPreferredSize();
                    d.width = tcm.getColumn(i).getWidth();
                    textField.setPreferredSize(d);
                }

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        filterVypuskRow.revalidate();
                    }
                });
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterVypuskRow.getComponent(e.getFromIndex());
                filterVypuskRow.remove(e.getFromIndex());
                filterVypuskRow.add(moved, e.getToIndex());
                filterVypuskRow.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };

        tableVypuskSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableVypusk, filterVypuskRow, UtilPlan.COL_ANALYSIS_VYPUSK);
            }
        };

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttPanel.add(buttClose);

        tableProjPanel = new JPanel();
        tableProjPanel.setLayout(new BorderLayout(1, 1));
        tableProjPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Проект плана"));
        tableProjPanel.add(new JScrollPane(tableProj), BorderLayout.CENTER);
        tableProjPanel.add(filterProjRow, BorderLayout.SOUTH);

        tablePlanPanel = new JPanel();
        tablePlanPanel.setLayout(new BorderLayout(1, 1));
        tablePlanPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("План производства"));
        tablePlanPanel.add(new JScrollPane(tablePlan), BorderLayout.CENTER);
        tablePlanPanel.add(filterPlanRow, BorderLayout.SOUTH);

        tableVypuskPanel = new JPanel();
        tableVypuskPanel.setLayout(new BorderLayout(1, 1));
        tableVypuskPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Факт (сдано на склад)"));
        tableVypuskPanel.add(new JScrollPane(tableVypusk), BorderLayout.CENTER);
        tableVypuskPanel.add(filterVypuskRow, BorderLayout.SOUTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 0, 5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        centerPanel.add(tableProjPanel);
        centerPanel.add(tablePlanPanel);
        centerPanel.add(tableVypuskPanel);

        osnova = new JPanel();
        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void createTableProj(final Vector rowAll) {
        tModelProj = new DefaultTableModel(rowAll, colProj) {
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

        tableProj.setModel(tModelProj);
        tableProj.setAutoCreateColumnsFromModel(true);

        // tableProj.getColumnModel().getColumn(0).setPreferredWidth(10);
        //   tableProj.getColumnModel().getColumn(1).setPreferredWidth(30);
        //  tableProj.getColumnModel().getColumn(2).setPreferredWidth(150);

        UtilPlan.setFooterTable(tableProj, filterProjRow, UtilPlan.COL_ANALYSIS_PROJ, UtilPlan.countSumm(tableProj, UtilPlan.COL_ANALYSIS_PROJ));

    }

    private void createTablePlan(final Vector rowAll) {
        tModelPlan = new DefaultTableModel(rowAll, colPlan) {
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

        tablePlan.setModel(tModelPlan);
        tablePlan.setAutoCreateColumnsFromModel(true);

        // tablePlan.getColumnModel().getColumn(0).setPreferredWidth(10);
        //   tablePlan.getColumnModel().getColumn(1).setPreferredWidth(30);
        //  tablePlan.getColumnModel().getColumn(2).setPreferredWidth(150);

        UtilPlan.setFooterTable(tablePlan, filterPlanRow, UtilPlan.COL_ANALYSIS_PLAN, UtilPlan.countSumm(tablePlan, UtilPlan.COL_ANALYSIS_PLAN));

    }

    private void createTableVypusk(final Vector rowAll) {
        tModelVypusk = new DefaultTableModel(rowAll, colVypusk) {
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

        tableVypusk.setModel(tModelVypusk);
        tableVypusk.setAutoCreateColumnsFromModel(true);

        // tableVypusk.getColumnModel().getColumn(0).setPreferredWidth(10);
        //   tableVypusk.getColumnModel().getColumn(1).setPreferredWidth(30);
        //  tableVypusk.getColumnModel().getColumn(2).setPreferredWidth(150);

        UtilPlan.setFooterTable(tableVypusk, filterVypuskRow, UtilPlan.COL_ANALYSIS_VYPUSK, UtilPlan.countSumm(tableVypusk, UtilPlan.COL_ANALYSIS_VYPUSK));

    }

}
