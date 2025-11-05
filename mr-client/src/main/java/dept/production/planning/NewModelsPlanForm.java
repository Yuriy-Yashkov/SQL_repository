package dept.production.planning;

import common.ProgressBar;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class NewModelsPlanForm extends javax.swing.JDialog {
    private Vector col;
    private Vector row;
    private JPanel osnova;
    private JPanel buttPanel;
    private JPanel titlePanel;
    private JLabel title;
    private JButton buttClose;
    private JButton buttPrint;
    private JButton buttSave;
    private JButton butSearch;
    private JTextField textSearch;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter sorter;

    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;

    private PlanPDB ppdb;
    private ProgressBar pb;

    public NewModelsPlanForm(SmallTableForm parent, boolean modal) {
        super(parent, modal);

        init();

        title.setText("Новые модели плана");

        ppdb = parent.ppdb;

        createTechTable();

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        UtilPlan.EDIT_NEW_MODELS_BUTT_ACTION = false;

        setTitle("Новые модели плана производства");

        setMinimumSize(new Dimension(350, 350));
        setPreferredSize(new Dimension(400, 450));

        titlePanel = new JPanel();
        osnova = new JPanel();
        buttPanel = new JPanel();

        title = new JLabel();
        buttClose = new JButton("Закрыть");
        buttPrint = new JButton("Печать");
        buttSave = new JButton("Сохранить");
        butSearch = new JButton("Поиск");
        textSearch = new JTextField();
        col = new Vector();
        table = new JTable();
        tModel = new DefaultTableModel();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.setLayout(new BorderLayout(1, 1));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 16));

        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);

        col.add("Новинка");
        col.add("Модель");

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

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        butSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butSearchActionPerformed(evt);
            }
        });

        textSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textSearchKeyPressed(evt);
            }
        });

        titlePanel.add(butSearch, BorderLayout.EAST);
        titlePanel.add(textSearch, BorderLayout.CENTER);

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        buttPanel.add(buttSave);

        osnova.add(titlePanel, BorderLayout.NORTH);
        osnova.add(new JScrollPane(table), BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void butSearchActionPerformed(ActionEvent evt) {
        String text = textSearch.getText().trim();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                if (table.getRowCount() == 0)
                    JOptionPane.showMessageDialog(null, "По заданному запросу ничего не найдено!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void textSearchKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) butSearch.doClick();
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            PlanOO oo = new PlanOO(title.getText(), tModel, table.getColumnModel());
            oo.createReport("DefaultTableBookFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            Vector data = new Vector();
            for (int i = 0; i < tModel.getDataVector().size(); i++) {
                if (((Vector) tModel.getDataVector().elementAt(i)).get(0).toString().equals("true")) {
                    data.add(Integer.valueOf(tModel.getValueAt(i, 1).toString()));
                }
            }
            if (ppdb.saveNewModelsEditPlanTemp(data))
                JOptionPane.showMessageDialog(null, "Новые модели успешно обновлены!", "Внимание ", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            UtilPlan.EDIT_NEW_MODELS_BUTT_ACTION = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void createTechTable() {
        try {
            row = ppdb.getDataEditPlanNewModelsTemp();
            dispose();
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        tModel = new DefaultTableModel(row, col) {
            @Override
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
                    tModel.setValueAt(Boolean.valueOf(value), table.convertRowIndexToModel(i), column);
                }

                minSelectedRow = -1;
                maxSelectedRow = -1;

                tableModelListenerIsChanging = false;
            }
        });

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
    }
}
