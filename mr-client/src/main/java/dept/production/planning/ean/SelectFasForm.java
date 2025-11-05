package dept.production.planning.ean;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.ProgressBar;
import dept.production.planning.PlanProductioForm;
import dept.production.planning.ProjectPlanForm;
import dept.production.planning.UtilPlan;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

/**
 * Форма для поиска/создания модели с размерным рядом. 
 *
 * @author lidashka
 */
public class SelectFasForm extends JDialog {
    private JPanel osnovaPanel;
    private JPanel buttPanel;
    private JPanel titleTablePrpPanel;
    private JPanel titleTablePlpPanel;
    private JPanel titleTableMssqlPanel;
    private JPanel titleTableSizePanel;
    private JPanel tablePrpPanel;
    private JPanel tablePlpPanel;
    private JPanel tableMssqlPanel;
    private JPanel tableTehPanel;
    private JPanel tableSizePanel;
    private JPanel buttTablePrpPanel;
    private JPanel buttTablePlpPanel;
    private JPanel buttTableMssqlPanel;
    private JButton buttClose;
    private JButton buttSelect;
    private JButton buttSearchTablePrp;
    private JButton buttSearchTablePlp;
    private JButton buttSearchTableMssql;
    private JButton buttSizeTablePrp;
    private JButton buttSizeTablePlp;
    private JButton buttSizeTableMssql;
    private JButton buttSearchPlan;
    private JButton buttSearchProject;
    private JTextField fasNumPrp;
    private JTextField fasNumPlp;
    private JTextField fasNumMssql;
    private JTextField projectNum;
    private JTextField projectName;
    private JTextField planNum;
    private JTextField planName;
    private JTextField planNumSizes;
    private JTextField planNameSizes;
    private JTabbedPane fasTabbedPane;
    private JTabbedPane sizeTabbedPane;
    private JCheckBox fasNew;
    private JCheckBox eanNullMssql;
    private JCheckBox eanNull;
    private ButtonGroup buttonGroupFasSrt;
    private JRadioButton fasSrt1;
    private JRadioButton fasSrt2;
    private JRadioButton fasSrt3;
    private JRadioButton fasSrt4;
    private Vector rowSizes;
    private Vector colPrp;
    private Vector colPlp;
    private Vector colMssql;
    private Vector colSize;
    private JTable tablePrp;
    private JTable tablePlp;
    private JTable tableMssql;
    private JTable tableSize;
    private DefaultTableModel tModelPrp;
    private DefaultTableModel tModelPlp;
    private DefaultTableModel tModelMssql;
    private DefaultTableModel tModelSize;
    private JScrollPane scTableSize;
    private JSplitPane splitPane;

    private int minSelectedRowTabSize = -1;
    private int maxSelectedRowTabSize = -1;
    private boolean table3ModelListenerIsChanging = false;

    private EanPDB epdb;
    private EanDB edb;
    private ProgressBar pb;
    private JTextPane sizeNote;
    private JScrollPane scNoteSize;
    private JPanel titleTableMarshListPanel;
    private JTextField fasNumMarshList;
    private JButton buttSearchTableMarshList;
    private Vector colMarshList;
    private JPanel tableMarshListPanel;
    private JPanel buttTableMarshListPanel;
    private JTable tableMarshList;
    private DefaultTableModel tModelMarshList;
    private JDateChooser dateMarshListSearch;
    private DefaultTableCellRenderer renderer;
    private JButton buttSizeTableMarshList;
    private JButton buttSearchPlanSizes;
    private Vector rowTable;
    private int selectTablePlpRow;
    private int selectTtablePrpRow;
    private int selectTableMssqlRow;
    private int selectTableMarshListRow;

    private MainController controller;

    public SelectFasForm(MainController controller, boolean modal, ArrayList<String> search) {
        super(controller.getMainForm(), modal);
        this.controller = controller;

        cleanConstants();
        init();
        initData();
        setData(search);

        fasTabbedPane.setSelectedIndex(3);
        if (fasNumMssql.getText().trim().replace(" ", "").replace(" ", "").length() > 2)
            buttSearchTableMarshList.doClick();

        this.setTitle("Модель");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    public SelectFasForm(MainController controller, boolean modal) {
        super(controller.getMainForm(), modal);
        this.controller = controller;

        cleanConstants();
        init();
        initData();

        buttSelect.setVisible(false);

        fasTabbedPane.setSelectedIndex(3);
        buttSearchTableMarshList.doClick();

        this.setTitle("Модель");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    private void cleanConstants() {
        UtilEan.ACTION_BUTT_SELECT_FAS = false;
        UtilEan.ITEM_ADD_FAS_NUM = "";
        UtilEan.ITEM_ADD_FAS_NAME = "";
        UtilEan.ITEM_ADD_FAS_NAR = "";
        UtilEan.ITEM_ADD_FAS_VID = "0";
        UtilEan.ITEM_ADD_FAS_KOLX = "1";
        UtilEan.ITEM_ADD_RST_RZM = new ArrayList<>();
        UtilEan.ITEM_ADD_SRT = "1";
        UtilEan.ITEM_SEARCH_COLOR_NAME = "";
        UtilEan.ITEM_ADD_COLOR_NUM = "2";
        UtilEan.ITEM_ADD_COLOR_NAME = "";
    }

    private void init() {
        setMinimumSize(new Dimension(550, 500));
        setPreferredSize(new Dimension(1000, 700));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        titleTablePrpPanel = new JPanel();
        titleTablePrpPanel.setLayout(new ParagraphLayout());

        titleTablePlpPanel = new JPanel();
        titleTablePlpPanel.setLayout(new ParagraphLayout());

        titleTableMssqlPanel = new JPanel();
        titleTableMssqlPanel.setLayout(new ParagraphLayout());

        titleTableMarshListPanel = new JPanel();
        titleTableMarshListPanel.setLayout(new ParagraphLayout());

        titleTableSizePanel = new JPanel();
        titleTableSizePanel.setLayout(new ParagraphLayout());
        titleTableSizePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttTablePrpPanel = new JPanel();
        buttTablePrpPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttTablePrpPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttTablePlpPanel = new JPanel();
        buttTablePlpPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttTablePlpPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttTableMssqlPanel = new JPanel();
        buttTableMssqlPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttTableMssqlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttTableMarshListPanel = new JPanel();
        buttTableMarshListPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttTableMarshListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        tablePrpPanel = new JPanel();
        tablePrpPanel.setLayout(new BorderLayout(1, 1));

        tablePlpPanel = new JPanel();
        tablePlpPanel.setLayout(new BorderLayout(1, 1));

        tableMssqlPanel = new JPanel();
        tableMssqlPanel.setLayout(new BorderLayout(1, 1));

        tableMarshListPanel = new JPanel();
        tableMarshListPanel.setLayout(new BorderLayout(1, 1));

        tableTehPanel = new JPanel();
        tableTehPanel.setLayout(new BorderLayout(1, 1));

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JMenu menu = new JMenu("Файл");
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Печать");
        menu.add(menuItem);
        menuItem.addActionListener(evt -> menuItemActionPerformed(evt));

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(evt -> buttCloseActionPerformed(evt));

        buttSelect = new JButton("Выбрать");
        buttSelect.addActionListener(evt -> buttSelectActionPerformed(evt));

        buttSearchTablePrp = new JButton("Найти");
        buttSearchTablePrp.setPreferredSize(new Dimension(100, 25));
        buttSearchTablePrp.addActionListener(evt -> buttSearchTablePrpActionPerformed(evt));

        buttSearchTablePlp = new JButton("Найти");
        buttSearchTablePlp.setPreferredSize(new Dimension(100, 25));
        buttSearchTablePlp.addActionListener(evt -> buttSearchTablePlpActionPerformed(evt));

        buttSearchTableMssql = new JButton("Найти");
        buttSearchTableMssql.setPreferredSize(new Dimension(100, 25));
        buttSearchTableMssql.addActionListener(evt -> buttSearchTableMssqlActionPerformed(evt));

        buttSearchTableMarshList = new JButton("Найти");
        buttSearchTableMarshList.setPreferredSize(new Dimension(100, 25));
        buttSearchTableMarshList.addActionListener(evt -> buttSearchTableMarshListActionPerformed(evt));

        buttSizeTablePrp = new JButton("Размеры");
        buttSizeTablePrp.addActionListener(evt -> openSize());

        buttSizeTablePlp = new JButton("Размеры");
        buttSizeTablePlp.addActionListener(evt -> openSize());

        buttSizeTableMssql = new JButton("Размеры");
        buttSizeTableMssql.addActionListener(evt -> openSize());

        buttSizeTableMarshList = new JButton("Размеры");
        buttSizeTableMarshList.addActionListener(evt -> openSize());

        buttSearchPlan = new JButton("План");
        buttSearchPlan.addActionListener(evt -> buttSearchPlanActionPerformed(evt));

        buttSearchPlanSizes = new JButton("План");
        buttSearchPlanSizes.addActionListener(evt -> buttSearchPlanActionPerformed(evt));

        buttSearchProject = new JButton("Проект");
        buttSearchProject.addActionListener(evt -> buttSearchProjectActionPerformed(evt));

        fasNumPrp = new JTextField();
        fasNumPrp.setPreferredSize(new Dimension(100, 20));
        fasNumPrp.addActionListener(e -> buttSearchTablePrp.doClick());

        fasNumPlp = new JTextField();
        fasNumPlp.setPreferredSize(new Dimension(100, 20));
        fasNumPlp.addActionListener(e -> buttSearchTablePlp.doClick());

        fasNumMssql = new JTextField();
        fasNumMssql.setPreferredSize(new Dimension(100, 20));
        fasNumMssql.addActionListener(e -> buttSearchTableMssql.doClick());

        fasNumMarshList = new JTextField();
        fasNumMarshList.setPreferredSize(new Dimension(100, 20));
        fasNumMarshList.addActionListener(e -> buttSearchTableMarshList.doClick());

        projectNum = new JTextField();
        projectNum.setPreferredSize(new Dimension(100, 20));
        projectNum.addActionListener(e -> buttSearchTableMssql.doClick());
        projectNum.setEnabled(false);

        projectName = new JTextField();
        projectName.setPreferredSize(new Dimension(100, 20));
        projectName.addActionListener(e -> buttSearchTableMssql.doClick());
        projectName.setEnabled(false);

        planNum = new JTextField();
        planNum.setPreferredSize(new Dimension(100, 20));
        planNum.addActionListener(e -> buttSearchTablePlp.doClick());
        planNum.setEnabled(false);

        planName = new JTextField();
        planName.setPreferredSize(new Dimension(100, 20));
        planName.addActionListener(e -> buttSearchTablePlp.doClick());
        planName.setEnabled(false);

        planNumSizes = new JTextField();
        planNumSizes.setPreferredSize(new Dimension(80, 20));
        planNumSizes.setEnabled(false);

        planNameSizes = new JTextField();
        planNameSizes.setPreferredSize(new Dimension(120, 20));
        planNameSizes.setEnabled(false);

        dateMarshListSearch = new JDateChooser();
        dateMarshListSearch.setPreferredSize(new Dimension(150, 20));

        sizeNote = new JTextPane();
        sizeNote.setPreferredSize(new Dimension(700, 50));

        fasNew = new JCheckBox();
        fasNew.setFont(new java.awt.Font("Dialog", 0, 13));
        fasNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        eanNullMssql = new JCheckBox();
        eanNullMssql.setFont(new java.awt.Font("Dialog", 0, 13));
        eanNullMssql.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        eanNull = new JCheckBox();
        eanNull.setFont(new java.awt.Font("Dialog", 0, 13));
        eanNull.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        colPrp = new Vector();
        colPrp.add("Проект №");
        colPrp.add("Модель");
        colPrp.add("Название");
        colPrp.add("Вид");
        colPrp.add("Комплект");
        colPrp.add("Новинка");

        colPlp = new Vector();
        colPlp.add("Модель");
        colPlp.add("Шифр");
        colPlp.add("Артикул");
        colPlp.add("Название");
        colPlp.add("Состав сырья");

        colMssql = new Vector();
        colMssql.add("Модель");
        colMssql.add("Шифр");
        colMssql.add("Артикул");
        colMssql.add("Название");
        colMssql.add("Состав сырья");

        colMarshList = new Vector();
        colMarshList.add("Модель");
        colMarshList.add("Шифр");
        colMarshList.add("Артикул");
        colMarshList.add("Название");
        colMarshList.add("ID 1");
        colMarshList.add("Ц/маршрут");
        colMarshList.add("ID 2");
        colMarshList.add("Ц/ean");
        colMarshList.add("Кол-во");
        colMarshList.add("Заявка");
        colMarshList.add("Совпадение");
        colMarshList.add("ID заявок");

        colSize = new Vector();
        colSize.add("");
        colSize.add("Рост");
        colSize.add("Размер");
        colSize.add("Этикетка");
        colSize.add("EAN13");
        colSize.add("План");
        colSize.add("Закрой");
        colSize.add("ГС1");

        tablePrp = new JTable();
        tablePrp.setAutoCreateColumnsFromModel(true);
        tablePrp.getTableHeader().setReorderingAllowed(false);
        tablePrp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    openSize();
                }
            }
        });
        new TableFilterHeader(tablePrp, AutoChoices.ENABLED);

        tablePrp.getSelectionModel().addListSelectionListener(e -> {
            if (selectTtablePrpRow != tablePrp.getSelectedRow()) {
                selectTtablePrpRow = tablePrp.getSelectedRow();
                createTableSize(new Vector(), colSize);
            }
        });

        tablePlp = new JTable();
        tablePlp.setAutoCreateColumnsFromModel(true);
        tablePlp.getTableHeader().setReorderingAllowed(false);
        tablePlp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    openSize();
                }
            }
        });
        new TableFilterHeader(tablePlp, AutoChoices.ENABLED);

        tablePlp.getSelectionModel().addListSelectionListener(e -> {
            if (selectTablePlpRow != tablePlp.getSelectedRow()) {
                selectTablePlpRow = tablePlp.getSelectedRow();
                createTableSize(new Vector(), colSize);
            }
        });

        tableMssql = new JTable();
        tableMssql.setAutoCreateColumnsFromModel(true);
        tableMssql.getTableHeader().setReorderingAllowed(false);
        tableMssql.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    openSize();
                }
            }
        });
        new TableFilterHeader(tableMssql, AutoChoices.ENABLED);

        tableMssql.getSelectionModel().addListSelectionListener(e -> {
            if (selectTableMssqlRow != tableMssql.getSelectedRow()) {
                selectTableMssqlRow = tableMssql.getSelectedRow();
                createTableSize(new Vector(), colSize);
            }
        });

        tableMarshList = new JTable();
        tableMarshList.setAutoCreateColumnsFromModel(true);
        tableMarshList.getTableHeader().setReorderingAllowed(false);
        tableMarshList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    openSize();
                }
            }
        });
        new TableFilterHeader(tableMarshList, AutoChoices.ENABLED);

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    switch (Integer.valueOf(table.getValueAt(row, 10).toString())) {
                        case 1:  // EAN-код отсутствует
                            cell.setBackground(Color.RED);
                            break;
                        case 2: // EAN-код отсутствует для цвета марш. листа
                            cell.setBackground(Color.PINK);
                            break;
                        case 3: // EAN-код отсутствует для некоторых закроенных размерах
                            cell.setBackground(Color.YELLOW);
                            break;
                        default:
                            cell.setBackground(table.getBackground());
                            break;
                    }
                } catch (NumberFormatException e) {
                    cell.setBackground(table.getBackground());
                }

                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }

            ;
        };

        JMenuItem detalItem = new JMenuItem("Маршрут подробно");
        detalItem.addActionListener(evt -> detalPopupMenuTableMarshListActionPerformed(evt));
        JMenuItem showItem = new JMenuItem("Показать заявки");
        showItem.addActionListener(evt -> showPopupMenuTableMarshListActionPerformed(evt));
        JMenuItem editItem = new JMenuItem("Изменить цвет");
        editItem.addActionListener(evt -> editPopupMenuTableMarshListActionPerformed(evt));

        final JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(showItem);
        popupMenu.add(detalItem);
        popupMenu.add(editItem);

        tableMarshList.setComponentPopupMenu(popupMenu);

        tableMarshList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tableMarshList.setRowSelectionInterval(
                        tableMarshList.rowAtPoint(e.getPoint()),
                        tableMarshList.rowAtPoint(e.getPoint()));
            }
        });

        tableMarshList.getSelectionModel().addListSelectionListener(e -> {
            if (selectTableMarshListRow != tableMarshList.getSelectedRow()) {
                selectTableMarshListRow = tableMarshList.getSelectedRow();
                createTableSize(new Vector(), colSize);
            }
        });

        tableSize = new JTable();
        tableSize.setAutoCreateColumnsFromModel(true);
        tableSize.getTableHeader().setReorderingAllowed(false);
        tableSize.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            minSelectedRowTabSize = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                    ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
            maxSelectedRowTabSize = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                    ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
        });
        new TableFilterHeader(tableSize, AutoChoices.ENABLED);

        tModelPrp = new DefaultTableModel();
        tModelPlp = new DefaultTableModel();
        tModelMssql = new DefaultTableModel();
        tModelSize = new DefaultTableModel();

        scTableSize = new JScrollPane(tableSize);
        scTableSize.setPreferredSize(new Dimension(380, 100));

        scNoteSize = new JScrollPane(sizeNote);
        scNoteSize.setPreferredSize(new Dimension(350, 50));

        fasSrt1 = new JRadioButton();
        fasSrt1.setFont(new java.awt.Font("Dialog", 0, 13));
        fasSrt1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fasSrt1.setText("1;");
        fasSrt1.setActionCommand("1");
        fasSrt1.setSelected(true);
        fasSrt1.addActionListener(evt -> {
            createTableSize(new Vector(), colSize);
            openSize();
        });

        fasSrt2 = new JRadioButton();
        fasSrt2.setFont(new java.awt.Font("Dialog", 0, 13));
        fasSrt2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fasSrt2.setText("2;");
        fasSrt2.setActionCommand("2");
        fasSrt2.addActionListener(evt -> {
            createTableSize(new Vector(), colSize);
            openSize();
        });

        fasSrt3 = new JRadioButton();
        fasSrt3.setFont(new java.awt.Font("Dialog", 0, 13));
        fasSrt3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fasSrt3.setText("3;");
        fasSrt3.setActionCommand("3");
        fasSrt3.addActionListener(evt -> {
            createTableSize(new Vector(), colSize);
            openSize();
        });

        fasSrt4 = new JRadioButton();
        fasSrt4.setFont(new java.awt.Font("Dialog", 0, 13));
        fasSrt4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fasSrt4.setText("4;");
        fasSrt4.setActionCommand("4");
        fasSrt4.addActionListener(evt -> {
            createTableSize(new Vector(), colSize);
            openSize();
        });

        buttonGroupFasSrt = new ButtonGroup();
        buttonGroupFasSrt.add(fasSrt1);
        buttonGroupFasSrt.add(fasSrt2);
        buttonGroupFasSrt.add(fasSrt3);
        buttonGroupFasSrt.add(fasSrt4);

        fasTabbedPane = new javax.swing.JTabbedPane();

        fasTabbedPane.addTab("План", tablePlpPanel);
        fasTabbedPane.addTab("Проект плана", tablePrpPanel);
        fasTabbedPane.addTab("Классификатор", tableMssqlPanel);
        fasTabbedPane.addTab("Маршрутные листы", tableMarshListPanel);

        tableSizePanel = new JPanel();
        tableSizePanel.setLayout(new BorderLayout(1, 1));

        sizeTabbedPane = new javax.swing.JTabbedPane();
        sizeTabbedPane.addTab("Размерная сетка", tableSizePanel);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setResizeWeight(1);
        splitPane.setLeftComponent(fasTabbedPane);
        splitPane.setRightComponent(sizeTabbedPane);

        titleTablePrpPanel.add(buttSearchProject, ParagraphLayout.NEW_PARAGRAPH);
        titleTablePrpPanel.add(projectNum);
        titleTablePrpPanel.add(projectName, ParagraphLayout.NEW_LINE_STRETCH_H);
        titleTablePrpPanel.add(new JLabel("Модель:"), ParagraphLayout.NEW_PARAGRAPH);
        titleTablePrpPanel.add(fasNumPrp, ParagraphLayout.NEW_LINE_STRETCH_H);
        titleTablePrpPanel.add(new JLabel("     Новинка"));
        titleTablePrpPanel.add(fasNew);
        titleTablePrpPanel.add(buttSearchTablePrp);

        titleTablePlpPanel.add(buttSearchPlan, ParagraphLayout.NEW_PARAGRAPH);
        titleTablePlpPanel.add(planNum);
        titleTablePlpPanel.add(planName, ParagraphLayout.NEW_LINE_STRETCH_H);
        titleTablePlpPanel.add(new JLabel("Модель:"), ParagraphLayout.NEW_PARAGRAPH);
        titleTablePlpPanel.add(fasNumPlp, ParagraphLayout.NEW_LINE_STRETCH_H);
        titleTablePlpPanel.add(buttSearchTablePlp);

        titleTableMssqlPanel.add(new JLabel("Модель:"), ParagraphLayout.NEW_PARAGRAPH);
        titleTableMssqlPanel.add(fasNumMssql, ParagraphLayout.NEW_LINE_STRETCH_H);
        titleTableMssqlPanel.add(buttSearchTableMssql);

        titleTableMarshListPanel.add(new JLabel("Дата:"), ParagraphLayout.NEW_PARAGRAPH);
        titleTableMarshListPanel.add(dateMarshListSearch);
        titleTableMarshListPanel.add(new JLabel("   Модель:"));
        titleTableMarshListPanel.add(fasNumMarshList, ParagraphLayout.NEW_LINE_STRETCH_H);
        titleTableMarshListPanel.add(buttSearchTableMarshList);

        titleTableSizePanel.add(planNumSizes, ParagraphLayout.NEW_PARAGRAPH);
        titleTableSizePanel.add(planNameSizes, ParagraphLayout.NEW_LINE_STRETCH_H);
        titleTableSizePanel.add(buttSearchPlanSizes);
        titleTableSizePanel.add(new JLabel("Сорт:"), ParagraphLayout.NEW_PARAGRAPH);
        titleTableSizePanel.add(fasSrt1);
        titleTableSizePanel.add(fasSrt2);
        titleTableSizePanel.add(fasSrt3);
        titleTableSizePanel.add(fasSrt4);

        buttTablePrpPanel.add(new JLabel());
        buttTablePrpPanel.add(buttSizeTablePrp);

        buttTablePlpPanel.add(new JLabel());
        buttTablePlpPanel.add(buttSizeTablePlp);

        buttTableMssqlPanel.add(new JLabel());
        buttTableMssqlPanel.add(buttSizeTableMssql);

        buttTableMarshListPanel.add(new JLabel());
        buttTableMarshListPanel.add(buttSizeTableMarshList);

        tablePrpPanel.add(titleTablePrpPanel, BorderLayout.NORTH);
        tablePrpPanel.add(new JScrollPane(tablePrp), BorderLayout.CENTER);
        tablePrpPanel.add(buttTablePrpPanel, BorderLayout.SOUTH);

        tablePlpPanel.add(titleTablePlpPanel, BorderLayout.NORTH);
        tablePlpPanel.add(new JScrollPane(tablePlp), BorderLayout.CENTER);
        tablePlpPanel.add(buttTablePlpPanel, BorderLayout.SOUTH);

        tableMssqlPanel.add(titleTableMssqlPanel, BorderLayout.NORTH);
        tableMssqlPanel.add(new JScrollPane(tableMssql), BorderLayout.CENTER);
        tableMssqlPanel.add(buttTableMssqlPanel, BorderLayout.SOUTH);

        tableMarshListPanel.add(titleTableMarshListPanel, BorderLayout.NORTH);
        tableMarshListPanel.add(new JScrollPane(tableMarshList), BorderLayout.CENTER);
        tableMarshListPanel.add(buttTableMarshListPanel, BorderLayout.SOUTH);

        tableSizePanel.add(titleTableSizePanel, BorderLayout.NORTH);
        tableSizePanel.add(scTableSize, BorderLayout.CENTER);

        buttPanel.add(buttClose);
        buttPanel.add(buttSelect);

        osnovaPanel.add(menuBar, BorderLayout.NORTH);
        osnovaPanel.add(splitPane, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void menuItemActionPerformed(ActionEvent evt) {
        try {
            TableColumnModel columnModel = null;
            //TableModel model = null;
            JTable table = null;

            switch (fasTabbedPane.getModel().getSelectedIndex()) {
                case 0:
                    table = tablePlp;
                    columnModel = tablePlp.getColumnModel();
                    break;
                case 1:
                    table = tablePrp;
                    columnModel = tablePrp.getColumnModel();
                    break;
                case 2:
                    table = tableMssql;
                    columnModel = tableMssql.getColumnModel();
                    break;
                case 3:
                    table = tableMarshList;
                    columnModel = tableMarshList.getColumnModel();
                    break;
                default:
                    break;
            }

            if (table != null && columnModel != null) {
                EanOO oo = new EanOO(
                        "Маршрутные листы c: " + new SimpleDateFormat("dd.MM.yyyy").format(dateMarshListSearch.getDate()),
                        (DefaultTableModel) table.getModel(),
                        columnModel);
                oo.createReport("DefaultTableAlbumFormatCheck.ots", false);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
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

            // План произв. 
            UtilEan.ITEM_ADD_PLAN_NUM = planNum.getText().trim();
            UtilEan.ITEM_ADD_PLAN_NAME = planName.getText().trim();

            // Проект плана произв.
            UtilEan.ITEM_ADD_PROJECT_NUM = projectNum.getText().trim();
            UtilEan.ITEM_ADD_PROJECT_NAME = projectName.getText().trim();

            // номер модели, название модели, артикул, вид, комплект
            switch (fasTabbedPane.getModel().getSelectedIndex()) {
                case 0:
                    if (tablePlp.getSelectedRow() != -1) {
                        UtilEan.ITEM_ADD_FAS_NUM = Integer.valueOf(tablePlp.getValueAt(tablePlp.getSelectedRow(), 0).toString()).toString();
                        UtilEan.ITEM_ADD_FAS_NAME = tablePlp.getValueAt(tablePlp.getSelectedRow(), 3).toString();
                        UtilEan.ITEM_ADD_FAS_NAR = tablePlp.getValueAt(tablePlp.getSelectedRow(), 2).toString().toUpperCase();
                        UtilEan.ITEM_ADD_FAS_KOLX = "1";
                    }
                    break;
                case 1:
                    if (tablePrp.getSelectedRow() != -1) {
                        UtilEan.ITEM_ADD_FAS_NUM = Integer.valueOf(tablePrp.getValueAt(tablePrp.getSelectedRow(), 1).toString()).toString();
                        UtilEan.ITEM_ADD_FAS_NAME = tablePrp.getValueAt(tablePrp.getSelectedRow(), 2).toString().toLowerCase();
                        UtilEan.ITEM_ADD_FAS_VID = tablePrp.getValueAt(tablePrp.getSelectedRow(), 3).toString();
                        UtilEan.ITEM_ADD_FAS_KOLX = tablePrp.getValueAt(tablePrp.getSelectedRow(), 4).toString();
                    }
                    break;
                case 2:
                    if (tableMssql.getSelectedRow() != -1) {
                        UtilEan.ITEM_ADD_FAS_NUM = Integer.valueOf(tableMssql.getValueAt(tableMssql.getSelectedRow(), 0).toString()).toString();
                        UtilEan.ITEM_ADD_FAS_NAME = tableMssql.getValueAt(tableMssql.getSelectedRow(), 3).toString();
                        UtilEan.ITEM_ADD_FAS_NAR = tableMssql.getValueAt(tableMssql.getSelectedRow(), 2).toString().toUpperCase();
                    }
                    break;
                case 3:
                    if (tableMarshList.getSelectedRow() != -1) {
                        UtilEan.ITEM_ADD_FAS_NUM = Integer.valueOf(tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 0).toString()).toString();
                        UtilEan.ITEM_ADD_FAS_NAME = tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 3).toString();
                        UtilEan.ITEM_ADD_FAS_NAR = tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 2).toString().toUpperCase();
                        UtilEan.ITEM_SEARCH_COLOR_NAME = tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 7).toString();
                    }
                    break;
                default:
                    break;
            }

            // сорт
            UtilEan.ITEM_ADD_SRT = buttonGroupFasSrt.getSelection().getActionCommand();

            // размеры
            EanItemListSize listSize;
            for (int i = 0; i < tableSize.getRowCount(); i++) {
                if (Boolean.valueOf(tableSize.getValueAt(i, 0).toString())) {
                    listSize = new EanItemListSize();
                    listSize.setRst(Integer.valueOf(tableSize.getValueAt(i, 1).toString()));
                    listSize.setRzm(Integer.valueOf(tableSize.getValueAt(i, 2).toString()));
                    if (tableSize.getValueAt(i, 2).toString().trim().equals(""))
                        listSize.setSizePrint(tableSize.getValueAt(i, 1).toString() + "-" + tableSize.getValueAt(i, 2).toString());
                    else
                        listSize.setSizePrint(tableSize.getValueAt(i, 3).toString().trim().replace(" ", "").replace(" ", ""));

                    UtilEan.ITEM_ADD_RST_RZM.add(listSize);
                }
            }

            // цвет
            if (!UtilEan.ITEM_SEARCH_COLOR_NAME.equals("")) {
                try {
                    int idColor = -1;

                    edb = new EanDB();

                    idColor = edb.getColorId(UtilEan.ITEM_SEARCH_COLOR_NAME);

                    if (idColor != -1) {
                        UtilEan.ITEM_ADD_COLOR_NUM = String.valueOf(idColor);
                        UtilEan.ITEM_ADD_COLOR_NAME = UtilEan.ITEM_SEARCH_COLOR_NAME;
                    }

                } catch (Exception e) {
                    UtilEan.ITEM_ADD_COLOR_NUM = "2";
                    UtilEan.ITEM_ADD_COLOR_NAME = "";
                } finally {
                    edb.disConn();
                }
            }

            // кнопка "Выбрать" была нажата

            UtilEan.ITEM_SEARCH_DATE_MARSH_LIST = new SimpleDateFormat("dd.MM.yyyy").format(dateMarshListSearch.getDate());
            UtilEan.ACTION_BUTT_SELECT_FAS = true;

            dispose();
        } catch (NumberFormatException e) {
            cleanConstants();
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchTablePrpActionPerformed(ActionEvent evt) {
        try {
            if (!projectNum.getText().trim().equals("")) {
                createTableSize(new Vector(), colSize);

                createTablePrp(
                        getModels(UtilEan.DATA_FAS_PROJECT),
                        colPrp);
            } else
                JOptionPane.showMessageDialog(null,
                        "Номер проекта плана не выбран!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchTablePlpActionPerformed(ActionEvent evt) {
        try {
            if (!planNum.getText().trim().equals("")) {
                createTableSize(new Vector(), colSize);

                createTablePlp(
                        getModels(UtilEan.DATA_FAS_PLAN),
                        colPlp);
            } else
                JOptionPane.showMessageDialog(null,
                        "Номер плана производства не выбран!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchTableMssqlActionPerformed(ActionEvent evt) {
        try {
            edb = new EanDB();

            createTableSize(new Vector(), colSize);

            createTableMssql(
                    getModels(UtilEan.DATA_FAS_MSSQL),
                    colMssql);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            edb.disConn();
        }
    }

    private void buttSearchTableMarshListActionPerformed(ActionEvent evt) {
        try {
            edb = new EanDB();

            createTableSize(new Vector(), colSize);

            createTableMarshList(getModels(UtilEan.DATA_FAS_MARSH_LIST), colMarshList);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            edb.disConn();
        }
    }

    private void detalPopupMenuTableMarshListActionPerformed(ActionEvent evt) {
        try {
            edb = new EanDB();

            new SmallTableForm(
                    controller,
                    true,
                    edb.getDetalMarshList(
                            Integer.valueOf(tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 0).toString()),
                            Integer.valueOf(tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 1).toString()),
                            (tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 2)).toString(),
                            Integer.valueOf(tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 4).toString()),
                            new SimpleDateFormat("dd.MM.yyyy").format(dateMarshListSearch.getDate())),
                    new SimpleDateFormat("dd.MM.yyyy").format(dateMarshListSearch.getDate()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            edb.disConn();
        }
    }

    private void showPopupMenuTableMarshListActionPerformed(ActionEvent evt) {
        try {
            epdb = new EanPDB();

            new SmallTableForm(
                    controller,
                    true,
                    epdb.getDetalModelAndSize(
                            (Vector) tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 11),
                            Integer.valueOf(tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 6).toString()))
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            epdb.disConn();
        }
    }

    private void editPopupMenuTableMarshListActionPerformed(ActionEvent evt) {
        try {
            if (tableMarshList.getSelectedRow() != -1) {

                UtilEan.ACTION_BUTT_EDIT_COLOR = false;

                new SmallEditDetalForm(
                        this,
                        true,
                        Integer.valueOf(tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 4).toString()),
                        tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 5).toString(),
                        Integer.valueOf(tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 6).toString()),
                        tableMarshList.getValueAt(tableMarshList.getSelectedRow(), 7).toString()
                );

                if (UtilEan.ACTION_BUTT_EDIT_COLOR)
                    buttSearchTableMarshList.doClick();

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchProjectActionPerformed(ActionEvent evt) {
        try {
            new ProjectPlanForm(controller, true);

            if (UtilPlan.ACTION_BUTT_PLAN_SELECT) {
                projectNum.setText(String.valueOf(UtilPlan.PLAN_SELECT_NUM));
                projectName.setText(String.valueOf(UtilPlan.PLAN_SELECT_NAME));
            }

        } catch (Exception e) {
            projectNum.setText("");
            projectName.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchPlanActionPerformed(ActionEvent evt) {
        try {
            createTableSize(new Vector(), colSize);

            new PlanProductioForm(controller, true, true);

            if (UtilPlan.ACTION_BUTT_PLAN_SELECT) {
                planNum.setText(String.valueOf(UtilPlan.PLAN_SELECT_NUM));
                planName.setText(String.valueOf(UtilPlan.PLAN_SELECT_NAME));

                planNumSizes.setText(String.valueOf(UtilPlan.PLAN_SELECT_NUM));
                planNameSizes.setText(String.valueOf(UtilPlan.PLAN_SELECT_NAME));

                UtilPlan.PLAN_SELECT_NUM_SIZES = UtilPlan.PLAN_SELECT_NUM;
                UtilPlan.PLAN_SELECT_NAME_SIZES = UtilPlan.PLAN_SELECT_NAME;
            }

        } catch (Exception e) {
            planNum.setText("");
            planName.setText("");

            planNumSizes.setText("");
            planNameSizes.setText("");

            UtilPlan.PLAN_SELECT_NUM_SIZES = -1;
            UtilPlan.PLAN_SELECT_NAME_SIZES = "";

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private Vector getModels(final String type) {
        rowTable = new Vector();
        try {
            epdb = new EanPDB();
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        pb.setMessage("Обновление таблицы моделей ...");

                        if (type.equals(UtilEan.DATA_FAS_PLAN)) {
                            rowTable = epdb.getAllModelsPlan(fasNumPlp.getText().trim(),
                                    Integer.valueOf(planNum.getText().trim()));

                        } else if (type.equals(UtilEan.DATA_FAS_PROJECT)) {
                            rowTable = epdb.getAllModelsProject(
                                    fasNumPrp.getText().trim(),
                                    Integer.valueOf(projectNum.getText().trim()),
                                    fasNew.isSelected());

                        } else if (type.equals(UtilEan.DATA_FAS_MSSQL)) {
                            rowTable = edb.getAllModels(fasNumMssql.getText().trim());

                        } else if (type.equals(UtilEan.DATA_FAS_MARSH_LIST)) {
                            rowTable = edb.getAllModelsInMarshList(
                                    fasNumMarshList.getText().trim(),
                                    new SimpleDateFormat("dd.MM.yyyy").format(dateMarshListSearch.getDate()));

                            rowTable = epdb.getOrderEANAndColor(rowTable);
                        }
                    } catch (Exception e) {
                        rowTable = new Vector();
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
            rowTable = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            epdb.disConn();
        }
        return rowTable;
    }

    /**
     * Возвращает размеры удовлетворяющие критериям поиска.
     * @param type тип
     * @param fas
     * @param sar
     * @param nar
     * @param srt
     * @param idPlan
     * @return
     */
    private Vector getSizeModel(final String type,
                                final int fas,
                                final int sar,
                                final String nar,
                                final int srt,
                                final int idColor,
                                final int idColorNew,
                                final int idPlan) {
        rowSizes = new Vector();
        try {
            edb = new EanDB();
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        Vector tmp;
                        Vector rowSize;
                        Vector rowSizesPlan;
                        Vector rowSizesCut;
                        Vector rowSizesOrder;

                        if (type.equals(UtilEan.DATA_FAS_SIZE_PLAN)) {
                            // поиск в ПП
                            pb.setMessage("Обновление размерной сетки ...");

                            rowSizesPlan = new Vector();
                            try {
                                epdb = new EanPDB();

                                // РС по номеру и шифру из заданного ПП                                
                                rowSizesPlan = epdb.openRstRzm(
                                        Integer.valueOf(planNum.getText().trim()),
                                        fas,
                                        sar);

                            } catch (Exception e) {
                                rowSizesPlan = new Vector();
                            } finally {
                                epdb.disConn();
                            }

                            for (Object obj : rowSizesPlan) {
                                rowSize = (Vector) obj;

                                // к РС добавляем EAN 
                                tmp = edb.getEanFas(
                                        fas,
                                        sar,
                                        nar,
                                        srt,
                                        Integer.valueOf(rowSize.elementAt(1).toString()),
                                        Integer.valueOf(rowSize.elementAt(2).toString()));

                                if (!tmp.isEmpty()) {
                                    rowSize.add(tmp.get(0));
                                    rowSize.add(tmp.get(1));
                                } else {
                                    rowSize.add("");
                                    rowSize.add("");
                                }

                                rowSize.add("+");

                                rowSizes.add(rowSize);
                            }

                        } else if (type.equals(UtilEan.DATA_FAS_SIZE_MSSQL)) {
                            // Поиск по всему классификатору
                            pb.setMessage("Обновление размерной сетки ...");

                            // РС модели. В вектор включена пара рост - 0, размер - всевозможные размеры данной модели.
                            rowSizes = edb.openRstRzm(fas);

                        } else if (type.equals(UtilEan.DATA_FAS_SIZE_EAN_MSSQL)) {
                            // Поиск модели в классификаторе
                            pb.setMessage("Обновление размерной сетки ...");

                            rowSizesPlan = new Vector();
                            rowSizesCut = new Vector();
                            rowSizesOrder = new Vector();

                            // РС модели 
                            rowSizes = edb.openRstRzm(
                                    fas,
                                    sar,
                                    nar,
                                    srt,
                                    idColorNew);

                            try {
                                epdb = new EanPDB();

                                // Если ПП выбран
                                if (idPlan != -1) {
                                    try {
                                        // РС модели по номеру и шифру из заданного плана производства
                                        rowSizesPlan = epdb.openRstRzmInPlan(
                                                idPlan,
                                                fas,
                                                sar);
                                    } catch (Exception e) {
                                        rowSizesPlan = new Vector();
                                    }
                                }

                                try {
                                    // РС модели по номеру и шифру
                                    rowSizesOrder = epdb.openRstRzmInOrder(
                                            idColorNew,
                                            nar,
                                            fas,
                                            srt);
                                } catch (Exception e) {
                                    rowSizesOrder = new Vector();
                                }

                            } catch (Exception e) {

                            } finally {
                                epdb.disConn();
                            }

                            // Если модель была закроена.
                            try {
                                // РС модели, которая была закроена
                                rowSizesCut = edb.getRstRzmModelsInMarshList(
                                        fas,
                                        sar,
                                        nar,
                                        idColor,
                                        new SimpleDateFormat("dd-MM-yyyy").format(dateMarshListSearch.getDate()));
                            } catch (Exception e) {
                                rowSizesCut = new Vector();
                            }

                            if (!rowSizesPlan.isEmpty()
                                    || !rowSizesCut.isEmpty()
                                    || !rowSizesOrder.isEmpty()) {

                                for (int i = 0; i < rowSizes.size(); i++) {
                                    rowSize = (Vector) rowSizes.elementAt(i);

                                    tmp = new Vector();
                                    tmp.add(rowSize.get(1));
                                    tmp.add(rowSize.get(2));

                                    // Если в ПП размер найден, помечаем '+'
                                    if (rowSizesPlan.contains(tmp)) {
                                        rowSize.setElementAt("+", 5);
                                        rowSizes.setElementAt(rowSize, i);
                                    }

                                    // Если в маршрутных листах размер найден, помечаем '+'
                                    if (rowSizesCut.contains(tmp)) {
                                        rowSize.setElementAt("+", 6);
                                        rowSizes.setElementAt(rowSize, i);
                                    }

                                    // Если размер ранее уже был заказан, помечаем '+'
                                    if (rowSizesOrder.contains(tmp)) {
                                        rowSize.setElementAt("+", 7);
                                        rowSizes.setElementAt(rowSize, i);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        rowSizes = new Vector();
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
            rowSizes = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            edb.disConn();
        }
        return rowSizes;
    }

    private void openSize() {
        try {
            boolean flag = false;
            JTable table = null;
            String type = "";
            String nar = "";
            int colFas = -1;
            int colSar = -1;
            int colNar = -1;
            int fas = -1;
            int sar = -1;
            int srt = -1;
            int idPlan = -1;
            int idColor = -1;
            int idColorNew = -1;

            switch (fasTabbedPane.getModel().getSelectedIndex()) {
                case 0:
                    if (tablePlp.getSelectedRow() != -1) {
                        flag = true;
                        table = tablePlp;
                        type = UtilEan.DATA_FAS_SIZE_PLAN;
                        colFas = 0;
                        colSar = 1;
                        colNar = 2;
                    }
                    break;
                case 1:
                    if (tablePrp.getSelectedRow() != -1) {
                        flag = true;
                        table = tablePrp;
                        type = UtilEan.DATA_FAS_SIZE_MSSQL;
                        colFas = 1;
                    }
                    break;
                case 2:
                    if (tableMssql.getSelectedRow() != -1) {
                        flag = true;
                        table = tableMssql;
                        type = UtilEan.DATA_FAS_SIZE_EAN_MSSQL;
                        colFas = 0;
                        colSar = 1;
                        colNar = 2;
                    }
                    break;
                case 3:
                    if (tableMarshList.getSelectedRow() != -1) {
                        flag = true;
                        table = tableMarshList;
                        type = UtilEan.DATA_FAS_SIZE_EAN_MSSQL;
                        colFas = 0;
                        colSar = 1;
                        colNar = 2;

                        try {
                            idColor = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 4).toString());
                        } catch (NumberFormatException e) {
                            idColor = -1;
                        }

                        try {
                            idColorNew = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 6).toString());
                        } catch (NumberFormatException e) {
                            idColorNew = -1;
                        }

                    }
                    break;
                default:
                    break;
            }

            if (flag) {

                if (table != null) {
                    if (colFas != -1) {
                        try {
                            fas = Integer.valueOf(table.getValueAt(table.getSelectedRow(), colFas).toString());

                        } catch (NumberFormatException e) {
                            flag = false;

                            fas = -1;
                            JOptionPane.showMessageDialog(null,
                                    "Модель задана некорректно.\n" + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    if (colSar != -1) {
                        try {
                            sar = Integer.valueOf(table.getValueAt(table.getSelectedRow(), colSar).toString());

                        } catch (NumberFormatException e) {
                            flag = false;

                            sar = -1;
                            JOptionPane.showMessageDialog(null,
                                    "Шифр задан некорректно.\n" + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    if (colNar != -1) {
                        try {
                            nar = table.getValueAt(table.getSelectedRow(), colNar).toString();

                        } catch (Exception e) {
                            flag = false;

                            nar = "";
                            JOptionPane.showMessageDialog(null,
                                    "Артикул задан некорректно.\n" + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    idPlan = -1;
                    if (!planNumSizes.getText().trim().equals("")) {
                        try {
                            idPlan = Integer.valueOf(planNumSizes.getText().trim());

                        } catch (NumberFormatException e) {
                            flag = false;

                            idPlan = -1;
                            JOptionPane.showMessageDialog(null,
                                    "План задан некорректно.\n" + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    try {
                        srt = Integer.valueOf(buttonGroupFasSrt.getSelection().getActionCommand());

                    } catch (NumberFormatException e) {
                        flag = false;

                        srt = -1;
                        JOptionPane.showMessageDialog(null,
                                "Сорт задан некорректно.\n" + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    if (flag)
                        createTableSize(getSizeModel(type, fas, sar, nar, srt, idColor, idColorNew, idPlan), colSize);

                }
            } else
                JOptionPane.showMessageDialog(null,
                        "Критерии поиска заданы некорректно!\n"
                                + "Вы не выбрали модель!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTablePrp(final Vector rowTab, Vector col) {
        tModelPrp = new DefaultTableModel(rowTab, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowTab.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablePrp.setModel(tModelPrp);
        tablePrp.setAutoCreateColumnsFromModel(true);

    }

    private void createTablePlp(final Vector rowTab, Vector col) {
        tModelPlp = new DefaultTableModel(rowTab, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowTab.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablePlp.setModel(tModelPlp);
        tablePlp.setAutoCreateColumnsFromModel(true);

    }

    private void createTableMssql(final Vector rowTab2, Vector col) {
        tModelMssql = new DefaultTableModel(rowTab2, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowTab2.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableMssql.setModel(tModelMssql);
        tableMssql.setAutoCreateColumnsFromModel(true);
    }

    private void createTableMarshList(final Vector rows, Vector col) {
        tModelMarshList = new DefaultTableModel(rows, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rows.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableMarshList.setModel(tModelMarshList);
        tableMarshList.setAutoCreateColumnsFromModel(true);

        tableMarshList.getColumnModel().getColumn(0).setPreferredWidth(1);
        tableMarshList.getColumnModel().getColumn(1).setMinWidth(0);
        tableMarshList.getColumnModel().getColumn(1).setMaxWidth(0);
        tableMarshList.getColumnModel().getColumn(2).setPreferredWidth(35);
        tableMarshList.getColumnModel().getColumn(3).setPreferredWidth(45);
        tableMarshList.getColumnModel().getColumn(4).setMinWidth(0);
        tableMarshList.getColumnModel().getColumn(4).setMaxWidth(0);
        tableMarshList.getColumnModel().getColumn(5).setPreferredWidth(60);
        tableMarshList.getColumnModel().getColumn(6).setMinWidth(0);
        tableMarshList.getColumnModel().getColumn(6).setMaxWidth(0);
        tableMarshList.getColumnModel().getColumn(7).setPreferredWidth(35);
        tableMarshList.getColumnModel().getColumn(8).setPreferredWidth(1);
        tableMarshList.getColumnModel().getColumn(9).setPreferredWidth(80);
        tableMarshList.getColumnModel().getColumn(10).setMinWidth(0);
        tableMarshList.getColumnModel().getColumn(10).setMaxWidth(0);
        tableMarshList.getColumnModel().getColumn(11).setMinWidth(0);
        tableMarshList.getColumnModel().getColumn(11).setMaxWidth(0);

        // изменение высоты строки в таблице по столбцу 
        for (int row = 0; row < tableMarshList.getRowCount(); row++) {
            int rowHeight = tableMarshList.getRowHeight();

            Component comp = tableMarshList.prepareRenderer(tableMarshList.getCellRenderer(row, 9), row, 9);
            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);

            tableMarshList.setRowHeight(row, rowHeight);
        }

        for (int i = 0; i < tableMarshList.getColumnModel().getColumnCount(); i++) {
            tableMarshList.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void createTableSize(final Vector rowTab, Vector col) {
        tModelSize = new DefaultTableModel(rowTab, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowTab.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelSize.addTableModelListener(e -> {
            if (table3ModelListenerIsChanging) {
                return;
            }
            int firstRow = e.getFirstRow();
            int column = e.getColumn();

            if (column != 0 || maxSelectedRowTabSize == -1 || minSelectedRowTabSize == -1) {
                return;
            }
            table3ModelListenerIsChanging = true;
            boolean value = ((Boolean) tModelSize.getValueAt(firstRow, column));
            for (int i = minSelectedRowTabSize; i <= maxSelectedRowTabSize; i++) {
                tModelSize.setValueAt(value, tableSize.convertRowIndexToModel(i), column);
            }

            minSelectedRowTabSize = -1;
            maxSelectedRowTabSize = -1;

            table3ModelListenerIsChanging = false;
        });

        tableSize.setModel(tModelSize);
        tableSize.setAutoCreateColumnsFromModel(true);
        tableSize.getColumnModel().getColumn(0).setPreferredWidth(1);
        tableSize.getColumnModel().getColumn(1).setPreferredWidth(10);
        tableSize.getColumnModel().getColumn(2).setPreferredWidth(10);
        tableSize.getColumnModel().getColumn(3).setMinWidth(0);
        tableSize.getColumnModel().getColumn(3).setMaxWidth(0);
        tableSize.getColumnModel().getColumn(4).setPreferredWidth(170);
        tableSize.getColumnModel().getColumn(5).setPreferredWidth(1);
        tableSize.getColumnModel().getColumn(6).setPreferredWidth(1);
        tableSize.getColumnModel().getColumn(7).setPreferredWidth(1);

        tableSize.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableSize.getTableHeader(), 0, ""));

        // изменение высоты строки в таблице по столбцу 
        for (int row = 0; row < tableSize.getRowCount(); row++) {
            int rowHeight = tableSize.getRowHeight();

            Component comp = tableSize.prepareRenderer(tableSize.getCellRenderer(row, 4), row, 4);
            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);

            tableSize.setRowHeight(row, rowHeight);
        }
    }

    private void initData() {
        try {
            createTablePrp(new Vector(), colPrp);
            createTablePlp(new Vector(), colPlp);
            createTableMssql(new Vector(), colMssql);
            createTableMarshList(new Vector(), colMarshList);
            createTableSize(new Vector(), colSize);

            planNum.setText(UtilEan.ITEM_ADD_PLAN_NUM);
            planName.setText(UtilEan.ITEM_ADD_PLAN_NAME);
            projectNum.setText(UtilEan.ITEM_ADD_PROJECT_NUM);
            projectName.setText(UtilEan.ITEM_ADD_PROJECT_NAME);

            if (UtilPlan.PLAN_SELECT_NUM_SIZES != -1) {
                planNumSizes.setText(String.valueOf(UtilPlan.PLAN_SELECT_NUM_SIZES));
                planNameSizes.setText(String.valueOf(UtilPlan.PLAN_SELECT_NAME_SIZES));
            }

            // Дата (поиск марш. лист)
            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);
            dateMarshListSearch.setDate(d);
            try {
                if (!UtilEan.ITEM_SEARCH_DATE_MARSH_LIST.equals(""))
                    dateMarshListSearch.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilEan.ITEM_SEARCH_DATE_MARSH_LIST));

            } catch (ParseException e) {
                dateMarshListSearch.setDate(d);
            }

            selectTableMarshListRow = -1;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setData(ArrayList<String> search) {
        try {
            fasNumPrp.setText(search.get(0));
            fasNumPlp.setText(search.get(0));
            fasNumMssql.setText(search.get(0));
            fasNumMarshList.setText(search.get(0));

            switch (Integer.valueOf(search.get(1))) {
                case 1:
                    fasSrt1.setSelected(true);
                    break;
                case 2:
                    fasSrt2.setSelected(true);
                    break;
                case 3:
                    fasSrt3.setSelected(true);
                    break;
                default:
                    fasSrt1.setSelected(true);
                    break;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
