package dept.production.zsh.zplata;

import com.jhlabs.awt.ParagraphLayout;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.UtilFunctions;
import dept.production.planning.UtilPlan;
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
import java.util.Vector;

/**
 *
 * @author lidashka
 */
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
    private JPanel filterRow;
    private TableColumnModelListener tableColumnModelListener;
    private TableFilterHeader filterHeader;

    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;

    private ProgressBar pb;

    public TableTechForm(java.awt.Dialog parent, boolean modal, Vector data, String titleText) {
        super(parent, modal);

        init();

        title.setText(titleText);

        createTechTable(data);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setTitle("Просмотр загрузки оборудования");

        setMinimumSize(new Dimension(350, 350));
        setPreferredSize(new Dimension(850, 650));

        osnova = new JPanel();
        titlePanel = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();

        title = new JLabel();
        buttClose = new JButton("Закрыть");
        buttPrint = new JButton("Печать");
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
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
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
        col.add("Стоимость");
        col.add("Трудоемкость (ч)");
        col.add("Пл. раб. время");
        col.add("Кол-во маш. на выпуск");
        col.add("Кол-во рем. ед.");

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

        buttPrint.addActionListener(evt -> buttPrintActionPerformed(evt));

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        centerPanel.add(new JScrollPane(tableTech), BorderLayout.CENTER);
        centerPanel.add(filterRow, BorderLayout.SOUTH);

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);

        osnova.add(title, BorderLayout.NORTH);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            ZPlataOO oo = new ZPlataOO(title.getText(), tModelTech, tableTech.getColumnModel());
            oo.createReport("DefaultTableBookFormatCheck.ots");
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

        tableTech.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableTech.getTableHeader(), 0, ""));

        tableTech.getColumnModel().addColumnModelListener(tableColumnModelListener);

        filterHeader.getTable().getRowSorter().addRowSorterListener(new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.setFooterTable(tableTech, filterRow, UtilZPlata.COL_TECH, UtilPlan.countSumm(tableTech, UtilZPlata.COL_TECH));
            }
        });
    }

}
