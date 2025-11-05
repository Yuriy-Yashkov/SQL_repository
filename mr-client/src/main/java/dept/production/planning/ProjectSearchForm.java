package dept.production.planning;

import by.march8.ecs.MainController;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.UtilFunctions;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
class ProjectSearchForm extends javax.swing.JDialog {
    String type = "";
    String path = "";
    private JPanel osnova;
    private JPanel titlePanel;
    private JPanel centerVerticalPanel;
    private JPanel centerHorizontPanel;
    private JPanel buttPanel;
    private JPanel userDataPanel;
    private JButton buttClose;
    private JButton buttAdd;
    private JButton buttSearch;
    private JTextField searchText;
    private JTable table1;
    private DefaultTableModel tModel1;
    private Vector col1;
    private Vector pdata;
    private PlanDB db;
    private JTable table2;
    private DefaultTableModel tModel2;
    private Vector col2;
    private int minSelectedRowTab2 = -1;
    private int maxSelectedRowTab2 = -1;
    private boolean table2ModelListenerIsChanging = false;
    private TableRowSorter<TableModel> sorterTab2;
    private TableRowSorter<TableModel> sorterTab1;
    private JButton buttDetal;
    private ProgressBar pb;
    private JPanel centerPanel;
    private JCheckBox userBox;
    private JLabel userKod;
    private JTextField userNar;
    private JTextField userSostav;
    private JTextField userRashod;
    private JButton buttUser;
    private JLabel textRashod;
    private JCheckBox checkBoxRashod;
    private MainController controller;

    public ProjectSearchForm(MainController mainController, boolean modal, String search, String type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        setTitle("Модель");

        init();

        searchText.setText(search);

        this.type = UtilPlan.EDIT_PROJ_TYPE_FAS;

        UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION = false;
        UtilPlan.EDIT_ADD_PROJ_FAS_NUM = "";
        UtilPlan.EDIT_ADD_PROJ_FAS_NAME = "";
        UtilPlan.EDIT_ADD_PROJ_FAS_RST_RZM = new Vector();

        col1.add("Модель");
        col1.add("Наименование");

        col2.add("");
        col2.add("Рост");
        col2.add("Размер");

        osnova.add(centerVerticalPanel, BorderLayout.CENTER);

        createTab1Search(new Vector(), col1);
        createTab2Search(new Vector(), col2);

        if (!searchText.getText().trim().equals(""))
            buttSearch.doClick();

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public ProjectSearchForm(MainController mainController, boolean modal, String type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        init();
        TableFilterHeader filterHeader = new TableFilterHeader(table2, AutoChoices.ENABLED);

        this.type = type;

        if (type.equals(UtilPlan.EDIT_PROJ_TYPE_COLOR)) {
            setTitle("Цвет");

            UtilPlan.EDIT_ADD_PROJ_FAS_COLOR = new Vector();

            col2.add("");
            col2.add("Код");
            col2.add("Цвет");

            osnova.add(new JScrollPane(table2), BorderLayout.CENTER);

            createTab2Search(getColor(), col2);

        } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_SOSTAV)) {
            setTitle("Полотно");

            UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV = new Vector();

            col2.add("");
            col2.add("Код");
            col2.add("Артикул");
            col2.add("Состав");

            centerPanel.add(new JScrollPane(table2), BorderLayout.CENTER);
            centerPanel.add(userDataPanel, BorderLayout.SOUTH);

            osnova.add(centerPanel, BorderLayout.CENTER);

            createTab2Search(getPolotno(), col2);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public ProjectSearchForm(MainController mainController, boolean modal, Vector data, String fas, String type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        init();

        this.type = type;

        if (type.equals(UtilPlan.EDIT_PROJ_TYPE_MODEL16)) {
            setTitle("Справочник Модели16");

            UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV = new Vector();

            col1.add("Код");
            col1.add("Модель");
            col1.add("Полотно");
            col1.add("idVid");
            col1.add("Вид");

            col2.add("");
            col2.add("Код");
            col2.add("Артикул");
            col2.add("Состав");

            centerHorizontPanel.add(new JScrollPane(table1));
            centerHorizontPanel.add(new JScrollPane(table2));

            osnova.add(centerHorizontPanel, BorderLayout.CENTER);

            createTab1Search(data, col1);
            createTab2Search(new Vector(), col2);

            searchText.setText(fas);

            buttSearch.doClick();

        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public ProjectSearchForm(MainController mainController, boolean modal, Vector data, String fas, String type, String path) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        init();

        this.type = type;
        this.path = path;

        if (type.equals(UtilPlan.EDIT_PROJ_TYPE_RASHOD)) {
            setTitle("Справочник раскладок");

            UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV = new Vector();

            col1.add("");
            col1.add("Модель");
            col1.add("Шифр");
            col1.add("Артикул");
            col1.add("Наименование");
            col1.add("Признак");
            col1.add("Шифр полотна");
            col1.add("Артикул полотна");
            col1.add("Расход сырья");

            col2.add("");
            col2.add("Код");
            col2.add("Артикул");
            col2.add("Состав");

            table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    try {
                        if (!textRashod.getText().trim().equals("0"))
                            textRashod.setText("0");

                        int sum = 0;
                        if (table1.getSelectedRowCount() > 1) {
                            for (int i = 0; i < table1.getRowCount(); i++)
                                if (table1.isRowSelected(i) && table1.getValueAt(i, 8) != null)
                                    sum += Double.valueOf(table1.getValueAt(i, 8).toString());

                            if (sum > 0)
                                textRashod.setText(String.valueOf(UtilFunctions.formatNorm(sum, 2)));

                        } else if (table1.getSelectedRowCount() == 1) {
                            textRashod.setText(UtilFunctions.formatNorm(Double.valueOf(table1.getValueAt(table1.getSelectedRow(), 8).toString()), 2));
                        }

                    } catch (Exception ex) {
                        textRashod.setText("0");
                        JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            buttPanel.setLayout(new GridLayout(0, 4, 5, 5));

            JPanel tempPanel = new JPanel();
            tempPanel.setLayout(new BorderLayout());

            tempPanel.add(textRashod, BorderLayout.CENTER);
            tempPanel.add(checkBoxRashod, BorderLayout.WEST);

            buttPanel.add(buttDetal);
            buttPanel.add(tempPanel);

            centerHorizontPanel.add(new JScrollPane(table1));
            centerHorizontPanel.add(new JScrollPane(table2));

            osnova.add(centerHorizontPanel, BorderLayout.CENTER);

            createTab1Search(data, col1);
            createTab2Search(new Vector(), col2);

            searchText.setText(fas);

            searchText.setEnabled(false);
            buttSearch.setVisible(false);

            buttSearch.doClick();
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(550, 400));
        setPreferredSize(new Dimension(700, 450));

        type = "";

        osnova = new JPanel();
        titlePanel = new JPanel();
        centerPanel = new JPanel();
        centerVerticalPanel = new JPanel();
        centerHorizontPanel = new JPanel();
        buttPanel = new JPanel();
        userDataPanel = new JPanel();

        buttClose = new JButton("Закрыть");
        buttAdd = new JButton("Выбрать");
        buttDetal = new JButton("Подробно");
        buttSearch = new JButton("Найти");
        searchText = new JTextField();
        table1 = new JTable();
        table2 = new JTable();
        tModel1 = new DefaultTableModel();
        tModel2 = new DefaultTableModel();
        col1 = new Vector();
        col2 = new Vector();
        userBox = new JCheckBox();
        userKod = new JLabel("-1");
        userNar = new JTextField();
        userSostav = new JTextField("примерно");
        userRashod = new JTextField();
        buttUser = new JButton("+");
        textRashod = new JLabel("0");
        checkBoxRashod = new JCheckBox();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.setLayout(new BorderLayout(1, 1));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerVerticalPanel.setLayout(new GridLayout(1, 2, 5, 5));
        centerHorizontPanel.setLayout(new GridLayout(2, 1, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        userDataPanel.setLayout(new FlowLayout());

        userKod.setPreferredSize(new Dimension(50, 20));
        userNar.setPreferredSize(new Dimension(150, 20));
        userSostav.setPreferredSize(new Dimension(300, 20));
        userRashod.setPreferredSize(new Dimension(100, 20));

        userKod.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        textRashod.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        buttAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAddActionPerformed(evt);
            }
        });

        buttDetal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttDetalActionPerformed(evt);
            }
        });

        buttSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSearchActionPerformed(evt);
            }
        });

        buttUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttUserActionPerformed(evt);
            }
        });

        searchText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttSearch.doClick();
            }
        });

        table1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    if (type.equals(UtilPlan.EDIT_PROJ_TYPE_FAS)) {
                        openRstRzmTable();
                    } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_MODEL16)) {
                        openPolotnoTable(table1.getValueAt(table1.getSelectedRow(), 2).toString().trim());
                    } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_RASHOD)) {
                        openPolotnoTable(table1.getValueAt(table1.getSelectedRow(), 7).toString().trim());
                    }
                }
            }
        });

        table1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (type.equals(UtilPlan.EDIT_PROJ_TYPE_FAS)) {
                        openRstRzmTable();
                    } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_MODEL16)) {
                        openPolotnoTable(table1.getValueAt(table1.getSelectedRow(), 2).toString().trim());
                    } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_RASHOD)) {
                        openPolotnoTable(table1.getValueAt(table1.getSelectedRow(), 7).toString().trim());
                    }
                }
            }
        });

        table2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowTab2 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowTab2 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        titlePanel.add(new JLabel("Поиск"), BorderLayout.NORTH);
        titlePanel.add(searchText, BorderLayout.CENTER);
        titlePanel.add(buttSearch, BorderLayout.EAST);

        buttPanel.add(buttClose);
        buttPanel.add(buttAdd);

        //userDataPanel.add(userBox);
        userDataPanel.add(userKod);
        userDataPanel.add(userNar);
        userDataPanel.add(userSostav);
        //userDataPanel.add(userRashod);
        userDataPanel.add(buttUser);

        centerVerticalPanel.add(new JScrollPane(table1));
        centerVerticalPanel.add(new JScrollPane(table2));

        osnova.add(titlePanel, BorderLayout.NORTH);
        //  osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttDetalActionPerformed(ActionEvent evt) {
        try {
            if (table1.getSelectedRow() != -1) {
                pdata = new Vector();
                pb = new ProgressBar(ProjectSearchForm.this, false, "Загрузка справочника ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        PlanDBF pdbf = new PlanDBF();
                        try {
                            Vector row = new Vector();
                            for (int i = 0; i < table1.getColumnCount() - 1; i++) {
                                row.add(table1.getValueAt(table1.getSelectedRow(), i));
                            }
                            pdata = pdbf.readDBFRaskladDetal(path, row);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф отгрузка: " + ex.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                new SmallTableForm(controller, this, true, pdata, "");
            } else
                JOptionPane.showMessageDialog(null, "Вы ничего не выбрали!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        try {
            if (type.equals(UtilPlan.EDIT_PROJ_TYPE_FAS)) {
                UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION = true;
                UtilPlan.EDIT_ADD_PROJ_FAS_NUM = Integer.valueOf(table1.getValueAt(table1.getSelectedRow(), 0).toString()).toString();
                UtilPlan.EDIT_ADD_PROJ_FAS_NAME = table1.getValueAt(table1.getSelectedRow(), 1).toString();

                for (int i = 0; i < table2.getRowCount(); i++) {
                    if (Boolean.valueOf(table2.getValueAt(i, 0).toString())) {
                        Vector tmp = new Vector();
                        tmp.add(true);
                        tmp.add(Integer.valueOf(table2.getValueAt(i, 1).toString()));
                        tmp.add(Integer.valueOf(table2.getValueAt(i, 2).toString()));
                        tmp.add(new Double(0));
                        tmp.add(new Double(33.3));
                        tmp.add(new Double(33.3));
                        tmp.add(new Double(33.4));
                        UtilPlan.EDIT_ADD_PROJ_FAS_RST_RZM.add(tmp);
                    }
                }
            } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_COLOR)) {
                UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION = true;

                for (int i = 0; i < table2.getRowCount(); i++) {
                    if (Boolean.valueOf(table2.getValueAt(i, 0).toString())) {
                        Vector tmp = new Vector();
                        tmp.add(false);
                        tmp.add(Integer.valueOf(table2.getValueAt(i, 1).toString()));
                        tmp.add(table2.getValueAt(i, 2).toString());
                        UtilPlan.EDIT_ADD_PROJ_FAS_COLOR.add(tmp);
                    }
                }
            } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_SOSTAV) || type.equals(UtilPlan.EDIT_PROJ_TYPE_MODEL16)) {
                UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION = true;

                for (int i = 0; i < table2.getRowCount(); i++) {
                    if (Boolean.valueOf(table2.getValueAt(i, 0).toString())) {
                        Vector tmp = new Vector();
                        tmp.add(false);
                        tmp.add(Integer.valueOf(table2.getValueAt(i, 1).toString()));
                        tmp.add(table2.getValueAt(i, 2).toString());
                        tmp.add(table2.getValueAt(i, 3).toString());
                        tmp.add(0);
                        tmp.add(new Double(0));
                        UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV.add(tmp);
                    }
                }
                try {
                    if (userBox.isSelected()) {
                        Vector tmp = new Vector();
                        tmp.add(false);
                        tmp.add(Integer.valueOf(userKod.getText().trim()));
                        tmp.add(userNar.getText().trim().toUpperCase().replace(" ", "").replace(" ", "").replace(" ", ""));
                        tmp.add(userSostav.getText().trim());
                        tmp.add(0);
                        tmp.add(Double.valueOf(userRashod.getText().trim()));
                        UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV.add(tmp);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

            } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_RASHOD)) {
                UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION = true;

                for (int i = 0; i < table1.getRowCount(); i++) {
                    if (Boolean.valueOf(table1.getValueAt(i, 0).toString())) {
                        Vector tmp = new Vector();
                        tmp.add(false);
                        tmp.add(-1);
                        tmp.add(table1.getValueAt(i, 7).toString());
                        tmp.add("");
                        tmp.add(0);
                        tmp.add(checkBoxRashod.isSelected() ?
                                Double.valueOf(textRashod.getText().trim()) :
                                Double.valueOf(table1.getValueAt(i, 8).toString()));
                        UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV.add(tmp);
                    }
                }

                for (int i = 0; i < table2.getRowCount(); i++) {
                    if (Boolean.valueOf(table2.getValueAt(i, 0).toString())) {
                        Vector tmp = new Vector();
                        tmp.add(false);
                        tmp.add(Integer.valueOf(table2.getValueAt(i, 1).toString()));
                        tmp.add(table2.getValueAt(i, 2).toString());
                        tmp.add(table2.getValueAt(i, 3).toString());
                        tmp.add(0);
                        tmp.add(checkBoxRashod.isSelected() ?
                                Double.valueOf(textRashod.getText().trim()) :
                                Double.valueOf(table1.getValueAt(table1.getSelectedRow(), 8).toString()));
                        UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV.add(tmp);
                    }
                }

            }
            dispose();

        } catch (Exception e) {
            UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION = false;
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        try {
            if (type.equals(UtilPlan.EDIT_PROJ_TYPE_FAS)) {
                Vector row = new Vector();
                try {
                    db = new PlanDB();
                    row = db.getAllModels(searchText.getText().trim());
                } catch (Exception e) {
                    row = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    db.disConn();
                }

                createTab1Search(row, col1);
            } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_COLOR) || type.equals(UtilPlan.EDIT_PROJ_TYPE_SOSTAV)) {
                String text = searchText.getText().trim();
                if (text.length() == 0) {
                    sorterTab2.setRowFilter(null);
                } else {
                    try {
                        sorterTab2.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                        if (table2.getRowCount() == 0) {
                            JOptionPane.showMessageDialog(null, "По заданному запросу ничего не найдено!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                            sorterTab2.setRowFilter(null);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (type.equals(UtilPlan.EDIT_PROJ_TYPE_MODEL16) || type.equals(UtilPlan.EDIT_PROJ_TYPE_RASHOD)) {
                String text = searchText.getText().trim();
                if (text.length() == 0) {
                    sorterTab1.setRowFilter(null);
                } else {
                    try {
                        sorterTab1.setRowFilter(RowFilter.regexFilter("(?iu)" + text, 1));
                        if (table1.getRowCount() == 0) {
                            JOptionPane.showMessageDialog(null, "По заданному запросу ничего не найдено!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                            sorterTab1.setRowFilter(null);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttUserActionPerformed(ActionEvent evt) {
        try {
            if (type.equals(UtilPlan.EDIT_PROJ_TYPE_SOSTAV)) {
                Vector tmp = new Vector();
                tmp.add(true);
                tmp.add(Integer.valueOf(userKod.getText().trim()));
                tmp.add(userNar.getText().trim().toUpperCase().replace(" ", "").replace(" ", "").replace(" ", ""));
                tmp.add(userSostav.getText().trim());

                tModel2.insertRow(0, tmp);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRstRzmTable() {
        try {
            if (table1.getSelectedRow() != -1) {
                Vector row = new Vector();
                try {
                    db = new PlanDB();
                    row = db.openRstRzm(Integer.valueOf(table1.getValueAt(table1.getSelectedRow(), 0).toString()));
                } catch (Exception e) {
                    row = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    db.disConn();
                }

                createTab2Search(row, col2);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPolotnoTable(String polotno) {
        try {
            if (table1.getSelectedRow() != -1) {
                Vector row = new Vector();
                try {
                    db = new PlanDB();
                    row = db.openPolotno(polotno);
                } catch (Exception e) {
                    row = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    db.disConn();
                }

                createTab2Search(row, col2);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTab1Search(final Vector rowTab1, Vector col) {
        tModel1 = new DefaultTableModel(rowTab1, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowTab1.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        table1.setModel(tModel1);
        table1.setAutoCreateColumnsFromModel(true);

        sorterTab1 = new TableRowSorter<TableModel>(tModel1);
        table1.setRowSorter(sorterTab1);

        table1.getColumnModel().getColumn(0).setPreferredWidth(50);
        table1.getColumnModel().getColumn(1).setPreferredWidth(150);

        if (type.equals(UtilPlan.EDIT_PROJ_TYPE_MODEL16)) {
            table1.getColumnModel().getColumn(2).setPreferredWidth(150);
            table1.getColumnModel().getColumn(3).setMinWidth(0);
            table1.getColumnModel().getColumn(3).setMaxWidth(0);
            table1.getColumnModel().getColumn(4).setPreferredWidth(150);
        }

        if (type.equals(UtilPlan.EDIT_PROJ_TYPE_RASHOD)) {
            sorterTab1.toggleSortOrder(5);
            sorterTab1.toggleSortOrder(4);
            sorterTab1.toggleSortOrder(3);

            table1.getColumnModel().getColumn(0).setPreferredWidth(10);
            table1.getColumnModel().getColumn(1).setPreferredWidth(50);
            table1.getColumnModel().getColumn(2).setMinWidth(0);
            table1.getColumnModel().getColumn(4).setPreferredWidth(150);
            table1.getColumnModel().getColumn(2).setMaxWidth(0);
            table1.getColumnModel().getColumn(6).setMinWidth(0);
            table1.getColumnModel().getColumn(6).setMaxWidth(0);
        }
    }

    private void createTab2Search(final Vector rowTab2, Vector col) {
        tModel2 = new DefaultTableModel(rowTab2, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowTab2.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModel2.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (table2ModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowTab2 == -1 || minSelectedRowTab2 == -1) {
                    return;
                }
                table2ModelListenerIsChanging = true;
                boolean value = ((Boolean) tModel2.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowTab2; i <= maxSelectedRowTab2; i++) {
                    tModel2.setValueAt(Boolean.valueOf(value), table2.convertRowIndexToModel(i), column);
                }

                minSelectedRowTab2 = -1;
                maxSelectedRowTab2 = -1;

                table2ModelListenerIsChanging = false;
            }
        });

        table2.setModel(tModel2);
        table2.setAutoCreateColumnsFromModel(true);

        table2.getColumnModel().getColumn(0).setPreferredWidth(30);
        table2.getColumnModel().getColumn(1).setPreferredWidth(150);
        table2.getColumnModel().getColumn(2).setPreferredWidth(150);
        if (type.equals(UtilPlan.EDIT_PROJ_TYPE_SOSTAV) ||
                type.equals(UtilPlan.EDIT_PROJ_TYPE_MODEL16) ||
                type.equals(UtilPlan.EDIT_PROJ_TYPE_RASHOD)) {
            table2.getColumnModel().getColumn(3).setPreferredWidth(350);
        }

        sorterTab2 = new TableRowSorter<TableModel>(tModel2);
        table2.setRowSorter(sorterTab2);

        table2.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table2.getTableHeader(), 0, ""));
    }

    private Vector getColor() {
        Vector row = new Vector();
        try {
            db = new PlanDB();
            row = db.getAllColors();
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            db.disConn();
        }
        return row;
    }

    private Vector getPolotno() {
        Vector row = new Vector();
        try {
            db = new PlanDB();
            row = db.getAllPolotno();
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            db.disConn();
        }
        return row;
    }

}
