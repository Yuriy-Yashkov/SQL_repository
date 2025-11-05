package dept.production.zsh.zplata;

import common.CheckBoxHeader;
import common.ProgressBar;
import common.User;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

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
import java.util.Vector;

/**
 * Форма выбора/поиска
 *
 * @author lidashka
 */
public class SelectForm extends JDialog {
    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;
    private JPanel osnovaPanel;
    private JPanel centrPanel;
    private JPanel buttPanel;
    private JButton buttClose;
    private JButton buttSelect;
    private Vector col;
    private Vector row;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private TableFilterHeader filterHeader;
    private JTextField searchText;
    private ProgressBar pb;
    private ZPlataPDB pdb;
    private JMenuBar jMenuBar;
    private JMenu jMenu2;
    private JMenu jMenu1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private JMenuItem jMenuItem3;
    private JMenuItem jMenuItem4;
    private User user;

    public SelectForm() {
    }

    /**
     * Конструктор 
     *
     * @param parent
     * @param modal
     * @param type
     */
    public SelectForm(java.awt.Dialog parent,
                      boolean modal) {

        super(parent, modal);

        cleanConstants();
        initMenu();
        init();
        initData();

        this.setTitle("Поиск");
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
     * Очищает константы
     */
    private void cleanConstants() {
        UtilZPlata.ACTION_BUTT_SELECT_SEARCH = false;
        UtilZPlata.ITEM_SELECT_SEARCH_ID = "";
        UtilZPlata.ITEM_SELECT_SEARCH_DATE = "";
        UtilZPlata.ITEM_SELECT_SEARCH_DEPT = "";
        UtilZPlata.ITEM_SELECT_SEARCH_BRIG = "";
        UtilZPlata.ITEM_SELECT_SEARCH_PERIOD = "";
    }

    /**
     * Инициализирует меню формы
     */
    private void initMenu() {
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu2.setText("Файл");

        jMenuItem4.setText("Печать");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar.add(jMenu2);

        jMenu1.setText("Правка");

        jMenuItem1.setText("Отправить повторно");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setText("Удалить запись");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar.add(jMenu1);

        if (UtilZPlata.BRIGADIR_EDIT) {
            setJMenuBar(jMenuBar);
        }
    }

    /**
     * Первоначальные параметры
     */
    private void initData() {
        user = User.getInstance();

        Vector data = new Vector();

        try {
            data = getData();

        } catch (Exception e) {
            data = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createTable(data);
    }

    /**
     * Инициализирует параметры и компоненты формы 
     */
    private void init() {
        setMinimumSize(new Dimension(450, 500));
        setPreferredSize(new Dimension(550, 500));

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSelect = new JButton("Выбрать");
        buttSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSelectActionPerformed(evt);
            }
        });

        col = new Vector();
        col.add("");
        col.add("Код");
        col.add("Дата");
        col.add("Цех");
        col.add("Бригада");
        col.add("Период");

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    try {
                        buttSelect.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Ошибка! " + ex.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        tModel = new DefaultTableModel();

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

        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        centrPanel = new JPanel();
        centrPanel.setLayout(new BorderLayout(1, 1));
        centrPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttSelect);

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnovaPanel.add(centrPanel, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void jMenuItem1ActionPerformed(ActionEvent evt) {
        boolean flag = false;

        try {
            for (int i = 0; i < table.getRowCount(); i++) {
                if ((Boolean) (table.getValueAt(i, 0))) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        if (flag) {
            if (JOptionPane.showOptionDialog(
                    null,
                    "Отправить данные повторно?",
                    "Отправка данных...",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Нет"},
                    "Нет") == JOptionPane.YES_OPTION) {
                try {
                    pdb = new ZPlataPDB();

                    pb = new ProgressBar(this, false, "Сбор данных...");
                    final SwingWorker sw = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            try {

                                Vector tmpData = new Vector();

                                for (int i = 0; i < table.getRowCount(); i++) {
                                    if ((Boolean) (table.getValueAt(i, 0)))
                                        tmpData.add(Integer.valueOf(table.getValueAt(i, 1).toString()));
                                }

                                if (pdb.replayImportReportVedtT4(tmpData)) {
                                    JOptionPane.showMessageDialog(null,
                                            "Данные успешно отправлены! ",
                                            "Завершено",
                                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                } else
                                    JOptionPane.showMessageDialog(null,
                                            "Ошибка отправки данных! ",
                                            "Завершено",
                                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null,
                                        "Ошибка отправки данных! " + e.getMessage(),
                                        "Ошибка",
                                        javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    pb.setVisible(true);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    pdb.disConn();
                }

            }
        } else
            JOptionPane.showMessageDialog(null,
                    "Вы не выбрали данные для повторной отправки! ",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);

    }

    private void jMenuItem3ActionPerformed(ActionEvent evt) {
        boolean flag = false;

        try {
            for (int i = 0; i < table.getRowCount(); i++) {
                if ((Boolean) (table.getValueAt(i, 0))) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        if (flag) {
            if (JOptionPane.showOptionDialog(
                    null,
                    "Удалить данные?",
                    "Удаление...",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Нет"},
                    "Нет") == JOptionPane.YES_OPTION) {
                try {
                    pdb = new ZPlataPDB();

                    pb = new ProgressBar(this, false, "Удаление...");
                    final SwingWorker sw = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            try {
                                for (int i = 0; i < table.getRowCount(); i++) {
                                    if ((Boolean) (table.getValueAt(i, 0)))
                                        pdb.deleteReportList(Integer.valueOf(table.getValueAt(i, 1).toString()));
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null,
                                        "Ошибка удаления! " + e.getMessage(),
                                        "Ошибка",
                                        javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    pb.setVisible(true);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    pdb.disConn();
                }

                createTable(getData());
            }
        } else
            JOptionPane.showMessageDialog(null,
                    "Вы не выбрали данные для удаления! ",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);

    }

    private void jMenuItem4ActionPerformed(ActionEvent evt) {
        try {
            ZPlataOO oo = new ZPlataOO("", tModel, table.getColumnModel());
            oo.createReport("DefaultTableBookFormatCheck.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            cleanConstants();

            if (table.getSelectedRow() != -1) {

                UtilZPlata.ACTION_BUTT_SELECT_SEARCH = true;

                UtilZPlata.ITEM_SELECT_SEARCH_ID = table.getValueAt(table.getSelectedRow(), 1).toString();
                UtilZPlata.ITEM_SELECT_SEARCH_DATE = table.getValueAt(table.getSelectedRow(), 2).toString();
                UtilZPlata.ITEM_SELECT_SEARCH_DEPT = table.getValueAt(table.getSelectedRow(), 3).toString();
                UtilZPlata.ITEM_SELECT_SEARCH_BRIG = table.getValueAt(table.getSelectedRow(), 4).toString();
                UtilZPlata.ITEM_SELECT_SEARCH_PERIOD = table.getValueAt(table.getSelectedRow(), 5).toString();

                dispose();

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            cleanConstants();

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        try {
            String text = searchText.getText().trim();
            if (text.length() == 0) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                    if (table.getRowCount() == 0)
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTable(Vector data) {
        tModel = new DefaultTableModel(data, col) {
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
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(20);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }

    private Vector getData() {
        row = new Vector();
        try {
            pdb = new ZPlataPDB();
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        pb.setMessage("Сбор данных ...");

                        row = pdb.getAllImportReports();
                    } catch (Exception e) {
                        row = new Vector();
                        JOptionPane.showMessageDialog(null,
                                "Сбой обновления! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            pb.setVisible(true);
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
        return row;
    }

}
