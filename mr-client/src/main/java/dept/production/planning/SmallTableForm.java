package dept.production.planning;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import dept.production.zsh.spec.NormForm;
import dept.production.zsh.spec.SpecForm;
import dept.production.zsh.spec.SpecPDB;
import dept.production.zsh.spec.UtilSpec;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SmallTableForm extends javax.swing.JDialog {

    public PlanPDB ppdb;
    User user = User.getInstance();
    private JPanel osnova;
    private JPanel titlePanel;
    private JPanel centerPanel;
    private JPanel buttPanel;
    private JPanel buttEastPanel;
    private JPanel filterRow;
    private JPanel panelCheckBox;
    private JLabel title;
    private JButton buttClose;
    private JButton buttPrint;
    private JButton buttOpenSpec;
    private JButton buttSaveEditPlan;
    private JButton buttPlus;
    private JButton buttMinus;
    private JButton buttSpec;
    private JButton buttNew;
    private JButton buttClear;
    private JButton buttCopy;
    private JButton buttPaste;
    private JButton buttDuplicate;
    private Vector colVec;
    private JTable table;
    private DefaultTableModel tModel;
    private TableColumnModelListener tableColumnModelListener;
    private TableFilterHeader filterHeader;
    private JPanel panelBox;
    private JCheckBox box1;
    private JCheckBox box2;
    private JCheckBox box3;
    private JCheckBox box4;
    private JCheckBox box5;
    private JCheckBox box6;
    private JCheckBox box7;
    private SpecPDB spdb;
    private int dayWorkPlan;
    private DefaultTableCellRenderer rendererEditTable;
    private ProgressBar pb;

    private HashMap<String, String> mapSetting = new HashMap<String, String>();
    private JMenuBar jMenuBar1;
    private JMenu jMenu1;
    private JMenuItem jMenuItem1;
    private TableRowSorter<TableModel> sorter;
    private JButton buttSaveEditSostav;

    private MainController controller;

    public SmallTableForm(MainController mainController, PlanProductioForm parent, boolean modal, Vector dataTable, String text) {
        super(parent, modal);
        controller = mainController;
        setTitle("Результат сравнения");

        setPreferredSize(new Dimension(1000, 600));

        init();

        title.setText(text);

        colVec.add("");
        colVec.add("Модель");
        colVec.add("№ плана");
        colVec.add("Спецификация");
        colVec.add("Кол-во");
        colVec.add("№ плана");
        colVec.add("Спецификация");
        colVec.add("Кол-во");

        createDefaultТableSmallTable(dataTable, colVec);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(MainController mainController, ProjectSearchForm parent, boolean modal, Vector dataTable, String text) {
        super(parent, modal);
        controller = mainController;
        setTitle("Раскладка подробно");

        setPreferredSize(new Dimension(700, 400));

        init();

        title.setText(text);

        colVec.add("");
        colVec.add("Модель");
        colVec.add("Шифр");
        colVec.add("Артикул");
        colVec.add("Наименование");
        colVec.add("Признак");
        colVec.add("Шифр полотна");
        colVec.add("Артикул полотна");
        colVec.add("Рост");
        colVec.add("Размер");
        colVec.add("Расход сырья");

        createТableSmallTableSort(dataTable, colVec);

        sorter.toggleSortOrder(8);

        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setMinWidth(0);
        table.getColumnModel().getColumn(6).setMaxWidth(0);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(MainController mainController, TableMaterialForm parent, boolean modal, Vector dataTable, String text) {
        super(parent, modal);
        controller = mainController;
        setTitle("Расход сырья");

        setPreferredSize(new Dimension(800, 400));

        init();

        title.setText(text);

        colVec.add("");
        colVec.add("Модель");
        colVec.add("Конвейер");
        colVec.add("Шифр");
        colVec.add("Артикул");
        colVec.add("Кол-во(шт.)");
        colVec.add("Расход сырья");
        colVec.add("Расход всего");

        createТableSmallTable(dataTable, colVec);

        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(150);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(MainController mainController, ProjectUpdateForm parent, boolean modal, Vector dataTable, String text) {
        super(parent, modal);
        controller = mainController;
        setTitle("Артикула не обновлены");

        setPreferredSize(new Dimension(1000, 400));

        init();

        title.setText(text);

        colVec.add("");
        colVec.add("Вид");
        colVec.add("Название");
        colVec.add("Модель");
        colVec.add("Арт. ДО");
        colVec.add("Арт. ПОСЛЕ");
        colVec.add("Рост");
        colVec.add("Размер");
        colVec.add("Состав");
        colVec.add("Декор");
        colVec.add("Кол-во(шт.)");
        colVec.add("x2");
        colVec.add("Примечание");
        colVec.add("Расцветка");
        colVec.add("Шифр");
        colVec.add("Конвейер");
        colVec.add("Новинка");

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        createТableSmallTable(dataTable, colVec);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(MainController mainController, TableTechForm parent, boolean modal, Vector dataTable, String tech) {
        super(parent, modal);
        controller = mainController;
        setTitle("Просмотр");
        setPreferredSize(new Dimension(800, 400));

        init();

        centerPanel.add(filterRow, BorderLayout.SOUTH);
        buttPanel.add(buttOpenSpec);

        title.setText(tech);

        colVec.add("");
        colVec.add("Модель");
        colVec.add("№");
        colVec.add("Спецификация");
        colVec.add("Трудоемкость на ед.");
        colVec.add("Кол-во операций");
        colVec.add("Загрузка");

        createТableSmallTable(dataTable, colVec);

        table.getColumnModel().addColumnModelListener(tableColumnModelListener);
        filterHeader.getTable().getRowSorter().addRowSorterListener(new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.setFooterTable(table, filterRow, UtilPlan.COL_TECH_SMALL, UtilPlan.countSumm(table, UtilPlan.COL_TECH_SMALL));
            }
        });

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(MainController mainController, ProjectDetalForm parent, boolean modal, Vector dataTable, String text, boolean type) {
        super(parent, modal);
        controller = mainController;
        setTitle("Расход сырья по моделям");

        setPreferredSize(new Dimension(900, 500));

        init();

        title.setText(text + ". Расход сырья по моделям.");

        colVec.add("");
        colVec.add("id");
        colVec.add("Конвейер");
        colVec.add("Шифр");
        colVec.add("Артикул");
        colVec.add("Модель");
        colVec.add("Код полотна");
        colVec.add("Вид");
        colVec.add("Артикул полотна");
        colVec.add("Состав");
        colVec.add("Расход");

        if (type) {
            ppdb = parent.ppdb;

            buttPanel.add(buttSaveEditSostav);

            createEditТableSmallSostavTable(dataTable, colVec);
        } else
            createDefaultТableSmallTable(dataTable, colVec);

        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(25);
        table.getColumnModel().getColumn(3).setPreferredWidth(25);
        table.getColumnModel().getColumn(4).setPreferredWidth(25);
        table.getColumnModel().getColumn(9).setPreferredWidth(125);
        table.getColumnModel().getColumn(10).setPreferredWidth(90);

        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        table.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
                Component editor = table.getEditorComponent();
                if (editor != null) {
                    editor.requestFocus();
                    if (editor instanceof JTextField) {
                        ((JTextField) editor).selectAll();
                    }
                }
            }
        });

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(MainController mainController, PlanDetalForm parent, boolean modal, Vector dataEdit, String nameTitle, int dayWorkPlan) {
        super(parent, modal);
        controller = mainController;
        setTitle("Редактирование");
        setPreferredSize(new Dimension(950, 500));

        ppdb = parent.ppdb;
        this.dayWorkPlan = dayWorkPlan;

        UtilPlan.EDIT_BUTT_ACTION = false;

        init();

        initMenu();

        colVec.add("");
        colVec.add("flag");
        colVec.add("idPlan");
        colVec.add("Шифр.арт.");
        colVec.add("Модель");
        colVec.add("Рост");
        colVec.add("Размер");
        colVec.add("кол. М.");
        colVec.add("№ Спец.");
        colVec.add("Спецификация");
        colVec.add("Трудоемкость на ед.");
        colVec.add("Трудоемкость на выпуск");
        colVec.add("Конвейер");
        colVec.add("1 декада(%)");
        colVec.add("2 декада(%)");
        colVec.add("3 декада(%)");
        colVec.add("Примечание");
        colVec.add("Новинка");

        mapSetting.put("", "true");
        mapSetting.put("Модель", "true");
        mapSetting.put("Рост", "true");
        mapSetting.put("Размер", "true");
        mapSetting.put("кол. М.", "true");
        mapSetting.put("Спецификация", "true");
        mapSetting.put("Трудоемкость на ед.", "true");
        mapSetting.put("Трудоемкость на выпуск", "true");

        try {
            String[][] arr = UtilFunctions.readPropFileStringsArray(UtilPlan.SETTING_MAP_EDIT);

            for (int i = 0; i < arr.length; i++)
                mapSetting.put(arr[i][0], arr[i][1]);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        title.setText(nameTitle);

        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        table.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int colum = table.getSelectedColumn();
                if (colum == 8 || colum == 9) {
                    buttSpec.doClick();
                } else {
                    table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
                    Component editor = table.getEditorComponent();
                    if (editor != null) {
                        editor.requestFocus();
                        if (editor instanceof JTextField) {
                            ((JTextField) editor).selectAll();
                        }
                    }
                }
            }
        });

        rendererEditTable = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    if (Integer.valueOf(table.getValueAt(row, 1).toString()) == -1) {
                        cell.setBackground(Color.PINK);
                        /* public Class<?> getColumnClass(int col){ 
                            if(row.isEmpty())
                                return super.getClass();      
                            else
                                return getValueAt(0, col).getClass(); 
                        } */

                    } else {
                        cell.setBackground(table.getBackground());
                    }
                    cell.getClass();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }
        };

        centerPanel.add(buttEastPanel, BorderLayout.EAST);
        buttPanel.add(buttSaveEditPlan);

        createEditSmallTable(dataEdit, colVec);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SmallTableForm(MainController mainController, PlanProductioForm parent, boolean modal, Vector data) {
        super(parent, modal);
        controller = mainController;
        setTitle("Просмотр");
        setPreferredSize(new Dimension(800, 600));

        init();

        colVec.add("");
        colVec.add("Шифр");
        colVec.add("Артикул");
        colVec.add("Модель");
        colVec.add("Кол-во");
        colVec.add("№ плана");
        colVec.add("Название");
        colVec.add("Статус");
        colVec.add("idStatus");

        createТableSmallTable(data, colVec);

        table.getColumnModel().getColumn(8).setMinWidth(0);
        table.getColumnModel().getColumn(8).setMaxWidth(0);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Параметры");

        jMenuItem1.setText("Столбцы таблицы");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    final HashMap<String, String> tempMap = new HashMap<String, String>();

                    panelCheckBox.removeAll();

                    for (int i = 3; i < table.getColumnCount(); i++) {
                        final String name = table.getColumnName(i);
                        final JCheckBox tempBox = new JCheckBox(name);

                        if (Boolean.valueOf(mapSetting.get(name)))
                            tempBox.setSelected(true);

                        tempBox.addItemListener(new ItemListener() {
                            public void itemStateChanged(ItemEvent event) {
                                if (tempBox.isSelected() == true) tempMap.put(name, "true");
                                else tempMap.put(name, "false");
                            }
                        });

                        panelCheckBox.add(tempBox);
                    }

                    if (JOptionPane.showOptionDialog(null, panelCheckBox, "Столбцы таблицы", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Сохранить", "Отмена"}, "Сохранить") == JOptionPane.YES_OPTION) {
                        for (int i = 0; i < table.getColumnCount(); i++) {
                            if (tempMap.get(table.getColumnName(i)) != null)
                                mapSetting.put(table.getColumnName(i), tempMap.get(table.getColumnName(i)));
                        }

                        Object[] keys = mapSetting.keySet().toArray();
                        Object[] values = mapSetting.values().toArray();
                        String rezaltPrint = "";

                        for (int row = 0; row < mapSetting.size(); row++) {
                            rezaltPrint += keys[row];
                            rezaltPrint += ",";
                            rezaltPrint += values[row];
                            rezaltPrint += ";";
                        }

                        try {
                            UtilFunctions.setSettingPropFile(rezaltPrint, UtilPlan.SETTING_MAP_EDIT);

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }

                        createEditSmallTable(tModel.getDataVector(), colVec);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);
    }

    private void init() {
        setMinimumSize(new Dimension(350, 350));

        clearConstantEditPlan();

        osnova = new JPanel();
        titlePanel = new JPanel();
        centerPanel = new JPanel();
        buttEastPanel = new JPanel();
        buttPanel = new JPanel();
        panelBox = new JPanel();
        panelCheckBox = new JPanel();

        title = new JLabel();
        buttClose = new JButton("Закрыть");
        buttPrint = new JButton("Печать");
        buttOpenSpec = new JButton("Открыть");
        buttSaveEditPlan = new JButton("Сохранить");
        buttSaveEditSostav = new JButton("Сохранить");
        buttDuplicate = new JButton("КОПИЯ");
        buttPlus = new JButton("ДОБАВИТЬ");
        buttMinus = new JButton("УДАЛИТЬ");
        buttSpec = new JButton("СПЕЦИФИКАЦИЯ");
        buttNew = new JButton("НОВИНКИ");
        buttCopy = new JButton("КОПИРОВАТЬ");
        buttPaste = new JButton("ВСТАВИТЬ");
        buttClear = new JButton("ОЧИСТИТЬ");
        colVec = new Vector();
        table = new JTable();
        tModel = new DefaultTableModel();
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);
        filterRow = new JPanel();
        box1 = new JCheckBox("Кол-во;");
        box2 = new JCheckBox("Спецификация;");
        box3 = new JCheckBox("Конвейер;");
        box4 = new JCheckBox("1-я декада;");
        box5 = new JCheckBox("2-я декада;");
        box6 = new JCheckBox("3-я декада;");
        box7 = new JCheckBox("Примечание");

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.setLayout(new ParagraphLayout());
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttEastPanel.setLayout(new GridLayout(8, 0, 5, 5));
        buttEastPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        filterRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBox.setLayout(new GridLayout(0, 3, 5, 5));
        panelCheckBox.setLayout(new GridLayout(0, 2, 5, 5));

        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 18));

        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);

        tableColumnModelListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = table.getColumnModel();
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

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        buttOpenSpec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOpenSpecActionPerformed(evt);
            }
        });

        buttSaveEditPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveEditPlanActionPerformed(evt);
            }
        });

        buttSaveEditSostav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveEditSostavActionPerformed(evt);
            }
        });

        buttDuplicate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttDuplicateActionPerformed(evt);
            }
        });

        buttPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPlusActionPerformed(evt);
            }
        });

        buttMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttMinusActionPerformed(evt);
            }
        });

        buttSpec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSpecActionPerformed(evt);
            }
        });

        buttNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttNewActionPerformed(evt);
            }
        });

        buttClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttClearActionPerformed(evt);
            }
        });

        buttCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCopyActionPerformed(evt);
            }
        });

        buttPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPasteActionPerformed(evt);
            }
        });

        panelBox.add(box1);
        panelBox.add(box2);
        panelBox.add(box3);
        panelBox.add(box4);
        panelBox.add(box5);
        panelBox.add(box6);
        panelBox.add(box7);

        buttEastPanel.add(buttDuplicate);
        buttEastPanel.add(buttPlus);
        buttEastPanel.add(buttMinus);
        buttEastPanel.add(buttCopy);
        buttEastPanel.add(buttPaste);
        buttEastPanel.add(buttClear);
        buttEastPanel.add(buttSpec);
        buttEastPanel.add(buttNew);

        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

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
            PlanOO oo = new PlanOO(title.getText(), tModel, table.getColumnModel());
            oo.createReport("DefaultTableAlbumFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOpenSpecActionPerformed(ActionEvent evt) {
        if (table.getSelectedRow() != -1) {
            try {
                spdb = new SpecPDB();

                Vector dataSpec = spdb.getDataSpec(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
                Vector dataItem = spdb.getDataSpecItem(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
                Vector dataTech = spdb.getDataSpecTech(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 2).toString()));
                //UtilSpec.DEPT_MODEL = UtilPlan.DEPT_MODEL;
                new NormForm(controller, true, dataSpec, dataItem, dataTech);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Данные спецификаций не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                spdb.disConn();
            }
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали строку!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    private void buttSaveEditPlanActionPerformed(ActionEvent evt) {
        try {
            if (ppdb.saveDataEditPlanTemp(tModel.getDataVector(), dayWorkPlan, Integer.valueOf(user.getIdEmployee()))) {
                JOptionPane.showMessageDialog(null, "Обновление завершено успешно!", "Внимание", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                UtilPlan.EDIT_BUTT_ACTION = true;
                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSaveEditSostavActionPerformed(ActionEvent evt) {
        try {
            if (ppdb.saveDataEditSostavProjectTemp(tModel.getDataVector(), Integer.valueOf(user.getIdEmployee()))) {
                JOptionPane.showMessageDialog(null, "Обновление завершено успешно!", "Внимание", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttDuplicateActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                Vector data = new Vector();
                data.add(false);
                data.add(1);
                data.add(0);
                data.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString()));
                data.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 4).toString()));
                data.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString()));
                data.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 6).toString()));
                data.add(Double.valueOf(table.getValueAt(table.getSelectedRow(), 7).toString()));
                data.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 8).toString()));
                data.add(table.getValueAt(table.getSelectedRow(), 9).toString());
                data.add(table.getValueAt(table.getSelectedRow(), 10).toString());
                data.add(Double.valueOf(table.getValueAt(table.getSelectedRow(), 11).toString()));
                data.add(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 12).toString()));
                data.add(Double.valueOf(table.getValueAt(table.getSelectedRow(), 13).toString()));
                data.add(Double.valueOf(table.getValueAt(table.getSelectedRow(), 14).toString()));
                data.add(Double.valueOf(table.getValueAt(table.getSelectedRow(), 15).toString()));
                data.add(table.getValueAt(table.getSelectedRow(), 16).toString());
                data.add(table.getValueAt(table.getSelectedRow(), 17).toString());

                tModel.insertRow(table.getSelectedRow(), data);
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPlusActionPerformed(ActionEvent evt) {
        try {
            new PlanAddItemForm(this, true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttMinusActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                int[] select = table.getSelectedRows();

                for (int i : select) {
                    table.setValueAt(-1, i, 1);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Вы не выделили ни одной строки!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            clearConstantEditPlan();
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSpecActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                clearConstantEditPlan();

                new SpecForm(controller, true, Integer.valueOf(table.getValueAt(table.getSelectedRow(), 4).toString()));

                if (UtilSpec.SPEC_BUTT_SELECT_ACTION) {
                    pb = new ProgressBar(SmallTableForm.this, false, "Обновление ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                int[] select = table.getSelectedRows();
                                table.clearSelection();

                                for (int i : select) {
                                    table.setValueAt(UtilSpec.SPEC_ID, i, 8);
                                    table.setValueAt(UtilSpec.SPEC_NAME, i, 9);
                                    table.setValueAt(UtilFunctions.formatNorm(UtilSpec.SPEC_NORM, UtilPlan.ROUNDING_NORM), i, 10);
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Сбой обновления!" + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                            return null;
                        }

                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выделили ни одной строки!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            clearConstantEditPlan();
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttNewActionPerformed(ActionEvent evt) {
        try {
            new NewModelsPlanForm(this, true);

            if (UtilPlan.EDIT_NEW_MODELS_BUTT_ACTION) {
                UtilPlan.EDIT_BUTT_ACTION = true;

                pb = new ProgressBar(SmallTableForm.this, false, "Обновление ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            Vector newFas = ppdb.getAllNewModelsPlanTemp();
                            Vector data = tModel.getDataVector();
                            for (int i = 0; i < data.size(); i++) {
                                if (newFas.contains(Integer.valueOf(((Vector) data.get(i)).get(4).toString()))) {
                                    tModel.setValueAt(UtilPlan.NEW, i, 17);
                                } else {
                                    tModel.setValueAt("", i, 17);
                                }
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Сбой обновления!" + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);
            }

        } catch (Exception e) {
            clearConstantEditPlan();
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttClearActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                if (JOptionPane.showOptionDialog(null, panelBox, "Очистить ", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Очистить", "Отмена"}, box1) == JOptionPane.YES_OPTION) {

                    pb = new ProgressBar(SmallTableForm.this, false, "Обновление ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                int[] select = table.getSelectedRows();
                                table.clearSelection();

                                for (int i : select) {
                                    if (box1.isSelected()) {
                                        table.setValueAt(new Double(0), i, 7);
                                    }

                                    if (box2.isSelected()) {
                                        table.setValueAt(UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_ID, i, 8);
                                        table.setValueAt(UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NAME, i, 9);
                                        table.setValueAt(UtilFunctions.formatNorm(UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NORM, UtilPlan.ROUNDING_NORM), i, 10);
                                    }

                                    if (box3.isSelected()) {
                                        table.setValueAt(0, i, 12);
                                    }

                                    if (box4.isSelected()) {
                                        table.setValueAt(new Double(0), i, 13);
                                    }

                                    if (box5.isSelected()) {
                                        table.setValueAt(new Double(0), i, 14);
                                    }

                                    if (box6.isSelected()) {
                                        table.setValueAt(new Double(0), i, 15);
                                    }

                                    if (box7.isSelected()) {
                                        table.setValueAt("", i, 16);
                                    }
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Сбой обновления!" + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                            return null;
                        }

                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выделили ни одной строки!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCopyActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                if (JOptionPane.showOptionDialog(null, panelBox, "Копировать ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Копировать", "Отмена"}, box1) == JOptionPane.YES_OPTION) {
                    clearConstantEditPlan();

                    if (box1.isSelected()) {
                        UtilPlan.EDIT_PLAN_KOL = Double.valueOf(table.getValueAt(table.getSelectedRow(), 7).toString());

                    }

                    if (box2.isSelected()) {
                        UtilPlan.EDIT_PLAN_SPEC_ID = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 8).toString());
                        UtilPlan.EDIT_PLAN_SPEC_NAME = table.getValueAt(table.getSelectedRow(), 9).toString();
                        UtilPlan.EDIT_PLAN_SPEC_NORM = Double.valueOf(table.getValueAt(table.getSelectedRow(), 10).toString());
                    }

                    if (box3.isSelected()) {
                        UtilPlan.EDIT_PLAN_CONV = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 12).toString());
                    }

                    if (box4.isSelected()) {
                        UtilPlan.EDIT_PLAN_DEKAD1 = Double.valueOf(table.getValueAt(table.getSelectedRow(), 13).toString());
                    }

                    if (box5.isSelected()) {
                        UtilPlan.EDIT_PLAN_DEKAD2 = Double.valueOf(table.getValueAt(table.getSelectedRow(), 14).toString());
                    }

                    if (box6.isSelected()) {
                        UtilPlan.EDIT_PLAN_DEKAD3 = Double.valueOf(table.getValueAt(table.getSelectedRow(), 15).toString());
                    }

                    if (box7.isSelected()) {
                        UtilPlan.EDIT_PLAN_NOTE = table.getValueAt(table.getSelectedRow(), 16).toString();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выделили ни одной строки!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            clearConstantEditPlan();
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPasteActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                if (JOptionPane.showOptionDialog(null, panelBox, "Вставить ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Вставить", "Отмена"}, box1) == JOptionPane.YES_OPTION) {
                    pb = new ProgressBar(SmallTableForm.this, false, "Обновление ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                int[] select = table.getSelectedRows();
                                table.clearSelection();

                                for (int i : select) {
                                    if (box1.isSelected()) {
                                        table.setValueAt(UtilPlan.EDIT_PLAN_KOL, i, 7);
                                    }

                                    if (box2.isSelected()) {
                                        table.setValueAt(UtilPlan.EDIT_PLAN_SPEC_ID, i, 8);
                                        table.setValueAt(UtilPlan.EDIT_PLAN_SPEC_NAME, i, 9);
                                        table.setValueAt(UtilPlan.EDIT_PLAN_SPEC_NORM, i, 10);
                                    }

                                    if (box3.isSelected()) {
                                        table.setValueAt(UtilPlan.EDIT_PLAN_CONV, i, 12);
                                    }

                                    if (box4.isSelected()) {
                                        table.setValueAt(UtilPlan.EDIT_PLAN_DEKAD1, i, 13);
                                    }

                                    if (box5.isSelected()) {
                                        table.setValueAt(UtilPlan.EDIT_PLAN_DEKAD2, i, 14);
                                    }

                                    if (box6.isSelected()) {
                                        table.setValueAt(UtilPlan.EDIT_PLAN_DEKAD3, i, 15);
                                    }

                                    if (box7.isSelected()) {
                                        table.setValueAt(UtilPlan.EDIT_PLAN_NOTE, i, 16);
                                    }
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Сбой обновления!" + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                            return null;
                        }

                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Вы не выделили ни одной строки!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createТableSmallTable(final Vector row, Vector col) {
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
                return col == 0 ? true : false;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);
    }

    private void createТableSmallTableSort(final Vector row, Vector col) {
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
                return col == 0 ? true : false;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
    }

    private void createDefaultТableSmallTable(final Vector row, Vector col) {
        tModel = new DefaultTableModel(row, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return (col == 0) ? getValueAt(0, col).getClass() : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);
    }

    private void createEditТableSmallSostavTable(final Vector row, Vector col) {
        tModel = new DefaultTableModel(row, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return (col == 0) ? getValueAt(0, col).getClass() : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0 || col == 10) ? true : false;
            }

            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);

                    if (value != null) {
                        if (col == 10) {
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(value.toString().trim().replace(",", ".")), 2)), col);
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

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);
    }

    private void createEditSmallTable(final Vector row, Vector col) {
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
                return (col == 0 || col == 3 || col == 4 || col == 5 || col == 6 ||
                        col == 7 || col == 12 || col == 13 || col == 14 || col == 15 || col == 16) ? true : false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);

                    if (value != null) {
                        if (col == 7) {
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(value.toString()), 3)), col);
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(value.toString()) * Double.valueOf(table.getValueAt(row, 10).toString())), 3)), 11);
                            fireTableCellUpdated(row, 11);
                        } else if (col == 10) {
                            rowVector.setElementAt(UtilFunctions.formatNorm(Double.valueOf(value.toString()), UtilPlan.ROUNDING_NORM), col);
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(Double.valueOf(value.toString()) * Double.valueOf(table.getValueAt(row, 7).toString())), 3)), 11);
                            fireTableCellUpdated(row, 11);
                        } else if (col == 13 || col == 14 || col == 15) {
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(value.toString().trim().replace(",", ".")), 2)), col);
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

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        UtilPlan.initColumTableMap(table, mapSetting);

        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rendererEditTable);
        }
    }

    private void clearConstantEditPlan() {
        UtilPlan.EDIT_BUTT_ACTION = false;
        UtilPlan.EDIT_PLAN_KOL = 0;
        UtilPlan.EDIT_PLAN_SPEC_ID = 1;
        UtilPlan.EDIT_PLAN_SPEC_NAME = "Спецификация отсутствует";
        UtilPlan.EDIT_PLAN_SPEC_NORM = 0;
        UtilPlan.EDIT_PLAN_DEKAD1 = 0;
        UtilPlan.EDIT_PLAN_DEKAD2 = 0;
        UtilPlan.EDIT_PLAN_DEKAD3 = 0;
        UtilPlan.EDIT_PLAN_NOTE = "";
    }

    public void addItemEditSmallTable() {
        try {
            Vector data = new Vector();
            data.add(false);
            data.add(1);
            data.add(0);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_SAR);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_FAS);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_RST);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_RZM);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_KOL);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_ID);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NAME);
            data.add(UtilFunctions.formatNorm(UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NORM, UtilPlan.ROUNDING_NORM));
            data.add(new Double(0));
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_CONV);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD1);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD2);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD3);
            data.add(UtilPlan.EDIT_ADD_ITEM_PLAN_NOTE);
            data.add("");

            tModel.insertRow(table.getRowCount(), data);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка: " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
