package dept.production.planning;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.Item;
import common.ProgressBar;
import common.User;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class ProjectDetalForm extends javax.swing.JDialog {
    boolean EDIT;
    boolean LOOK;
    PlanPDB ppdb;
    PlanDB pdb;
    private JButton buttClose;
    private JButton buttTech;
    private JButton buttPrint;
    private JButton buttEditTable;
    private JButton buttSave;
    private JButton buttTab1Shkala;
    private JButton buttTab1Plus;
    private JButton buttTab1Minus;
    private JButton buttTab1Edit;
    private JButton buttTab2Plus;
    private JButton buttTab2Minus;
    private JButton buttTab2Edit;
    private JDateChooser projStDate;
    private JComboBox dept;
    private JLabel kolvoIzdPlan;
    private JTextField workDayProj;
    private JTextPane noteText;
    private JLabel insDate;
    private JLabel title;
    private JTextField projName;
    private JLabel vvodDate;
    private JLabel vvodAvtor;
    private JLabel insAvtor;
    private JPanel osnova;
    private JPanel infoPanel;
    private JPanel titlePanel;
    private JPanel buttPanel;
    private JPanel centerPanel;
    private JPanel filterRowStok;
    private JPanel filterRowVnorm;
    private JPanel filterRowDekad;
    //  private JPanel filterRowDetalTab1;
    private JPanel filterRowDetalTab2;
    private JPanel buttEastTab1Panel;
    private JPanel buttEastTab2Panel;
    private JTabbedPane tableTabbedPane;
    private JTable tableStok;
    private JTable tableVnorm;
    private JTable tableDekad;
    private JTable tableDetalTab1;
    private JTable tableDetalTab2;
    private DefaultTableModel tModelStok;
    private DefaultTableModel tModelVnorm;
    private DefaultTableModel tModelDekad;
    private DefaultTableModel tModelDetalTab1;
    private DefaultTableModel tModelDetalTab2;
    //private TableRowSorter sorterPlan;
    private TableFilterHeader filterHeaderStok;
    private TableFilterHeader filterHeaderVnorm;
    private TableFilterHeader filterHeaderDekad;
    private TableFilterHeader filterHeaderDetalTab1;
    private TableFilterHeader filterHeaderDetalTab2;
    private Vector colStok;
    private Vector colVnorm;
    private Vector colDekad;
    private Vector colDetalTab1;
    private Vector colDetalTab2;
    private int minSelectedRowStok = -1;
    private int maxSelectedRowStok = -1;
    private boolean tablePlanModelListenerIsChanging = false;
    private int minSelectedRowVnorm = -1;
    private int maxSelectedRowVnorm = -1;
    private boolean tableVnormModelListenerIsChanging = false;
    private int minSelectedRowDekad = -1;
    private int maxSelectedRowDekad = -1;
    private boolean tableDekadModelListenerIsChanging = false;
    private int minSelectedRowDetalTab1 = -1;
    private int maxSelectedRowDetalTab1 = -1;
    private boolean tableDetalTab1ModelListenerIsChanging = false;
    private int minSelectedRowDetalTab2 = -1;
    private int maxSelectedRowDetalTab2 = -1;
    private boolean tableDetalTab2ModelListenerIsChanging = false;
    private int idProj;
    private String typeProj;
    private User user = User.getInstance();
    private JPanel tableStockPanel;
    private JPanel tableDetalPanel;
    private JPanel tableDetalTab1Panel;
    private JPanel tableDetalTab2Panel;
    private JPanel tableVnormPanel;
    private JPanel tableDekadPanel;
    private TableColumnModelListener tableColumnModelStokListener;
    private TableColumnModelListener tableColumnModelVnormListener;
    private TableColumnModelListener tableColumnModelDekadListener;
    private TableColumnModelListener tableColumnModelDetalTab1Listener;
    private TableColumnModelListener tableColumnModelDetalTab2Listener;
    private RowSorterListener tableStokSorterListener;
    private RowSorterListener tableVnormSorterListener;
    private RowSorterListener tableDekadSorterListener;
    private RowSorterListener tableDetalTab1SorterListener;
    private RowSorterListener tableDetalTab2SorterListener;
    private DefaultTableCellRenderer renderer;
    private JMenuBar jMenuBar1;
    private JMenu jMenu1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private Vector dataTable;
    private ProgressBar pb;
    private ButtonGroup buttonGroupStatus;
    private ButtonGroup buttonGroupTech;
    private ButtonGroup buttonGroupTechKol;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton loadTech1;
    private JRadioButton loadTech2;
    private JRadioButton loadTechMonth;
    private JRadioButton loadTechDek1;
    private JRadioButton loadTechDek2;
    private JRadioButton loadTechDek3;
    private JPanel panelBox;
    private Vector data;
    private JMenu jMenu2;
    private JMenuItem jMenuItem3;
    private JMenuItem jMenuItem4;
    private JPanel panelBoxTechLoad;
    private JPanel panelBoxTechLoadKol;
    private JMenuItem jMenuItem5;
    private JMenuItem jMenuItem6;
    private Vector dataItem;
    private Vector dataShkala;
    private Vector dataSostav;
    private Vector dataColor;
    private JMenu jMenu3;
    private JMenuItem jMenuItem7;
    private JMenuItem jMenuItem8;
    private JMenuItem jMenuItem9;
    private JMenuItem jMenuItem10;
    private JMenuItem jMenuItem11;
    private JMenuItem jMenuItem12;
    private JMenuItem jMenuItem13;
    private Vector dataStokCenaReport;
    private JMenuItem jMenuItem14;

    private MainController controller;

    public ProjectDetalForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Новый проект плана производства");

        typeProj = UtilPlan.PROJECT_ADD;

        init();

        try {
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilPlan.DEPT_MODEL, UtilPlan.DEPT_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        EDIT = true;

        title.setText("Новый проект плана производства");
        //  dept.setSelectedItem();
        projStDate.setDate((Calendar.getInstance()).getTime());
        vvodDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));
        insDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));
        vvodAvtor.setText(user.getFio());
        insAvtor.setText(user.getFio());

        try {
            pb = new ProgressBar(ProjectDetalForm.this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        ppdb = new PlanPDB();
                        LOOK = (ppdb.createTempPlanItemTables(idProj) &&
                                ppdb.createTempPlanFasNewTables(idProj) &&
                                ppdb.createTempPlanFasColorTables(idProj) &&
                                ppdb.createTempPlanFasSostavTables(idProj) &&
                                ppdb.createTempPlanFasDekorTables(idProj)) ? true : false;

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Данные проекта плана не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }

                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setVisible(true);

            if (LOOK) {
                createStokTable(new Vector());
                createPlanVnormTable(new Vector());
                createPlanDekadTable(new Vector());
                createPlanDetalTab1Table(new Vector());
                createPlanDetalTab2Table(new Vector());

                filterHeaderStok.getTable().getRowSorter().addRowSorterListener(tableStokSorterListener);
                filterHeaderVnorm.getTable().getRowSorter().addRowSorterListener(tableVnormSorterListener);
                filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
                // filterHeaderDetalTab1.getTable().getRowSorter().addRowSorterListener(tableDetalTab1SorterListener);
                filterHeaderDetalTab2.getTable().getRowSorter().addRowSorterListener(tableDetalTab2SorterListener);
            } else {
                throw new Exception("Ошибка формирования временных таблиц! ");

            }

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowListener() {
                public void windowClosed(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    buttClose.doClick();
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка формирования проекта плана производства! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public ProjectDetalForm(MainController mainController, boolean modal,
                            Vector dataProj, Vector dataItem, Vector dataDetalItem, Vector dataVnormItem, Vector dataDekadItem, int idProj) {

        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Просмотр проекта плана производства");

        try {
            this.idProj = idProj;

            typeProj = UtilPlan.PROJECT_OPEN;

            init();

            buttSave.setVisible(true);
            buttTab1Plus.setVisible(true);
            buttTab1Minus.setVisible(true);
            buttTab1Edit.setVisible(true);
            buttEastTab2Panel.setVisible(true);

            title.setText("Проект плана производства № " + this.idProj);

            projName.setText(dataProj.get(4).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilPlan.DEPT_MODEL, Integer.valueOf(dataProj.get(0).toString())));
            projStDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataProj.get(3).toString()));
            kolvoIzdPlan.setText(new BigDecimal(dataProj.get(14).toString()).setScale(0, RoundingMode.HALF_UP).toString());
            workDayProj.setText(dataProj.get(16).toString());
            noteText.setText(dataProj.get(11).toString());
            vvodDate.setText(dataProj.get(7).toString());
            vvodAvtor.setText(dataProj.get(6).toString());
            insDate.setText(dataProj.get(10).toString());
            insAvtor.setText(dataProj.get(9).toString());

            switch (Integer.valueOf(dataProj.get(13).toString())) {
                case 1:
                    jRadioButton1.setSelected(true);
                    break;
                case 3:
                    jRadioButton2.setSelected(true);
                    break;
                case -2:
                    jRadioButton3.setSelected(true);
                    break;
                default:
                    jRadioButton2.setSelected(true);
                    break;
            }

            createStokTable(dataItem);
            createPlanVnormTable(dataVnormItem);
            createPlanDekadTable(dataDekadItem);
            createPlanDetalTab1Table(dataDetalItem);
            createPlanDetalTab2Table(new Vector());

            filterHeaderStok.getTable().getRowSorter().addRowSorterListener(tableStokSorterListener);
            filterHeaderVnorm.getTable().getRowSorter().addRowSorterListener(tableVnormSorterListener);
            filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
            //    filterHeaderDetalTab1.getTable().getRowSorter().addRowSorterListener(tableDetalTab1SorterListener);
            filterHeaderDetalTab2.getTable().getRowSorter().addRowSorterListener(tableDetalTab2SorterListener);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка формирования проекта плана производства! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public ProjectDetalForm(MainController mainController, boolean modal, Vector dataProj, final int idProj, String typeProj) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Редактирование проекта плана производства");

        try {

            this.idProj = idProj;
            this.typeProj = typeProj;

            init();

            this.EDIT = true;

            title.setText(typeProj.equals(UtilPlan.PROJECT_COPY) ?
                    "Копирование проекта плана производства" :
                    "Редактирование проекта плана № " + idProj);

            projName.setText(dataProj.get(4).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilPlan.DEPT_MODEL, Integer.valueOf(dataProj.get(0).toString())));
            projStDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataProj.get(3).toString()));
            kolvoIzdPlan.setText(new BigDecimal(dataProj.get(14).toString()).setScale(0, RoundingMode.HALF_UP).toString());
            workDayProj.setText(dataProj.get(16).toString());
            noteText.setText(dataProj.get(11).toString());
            vvodDate.setText(dataProj.get(7).toString());
            vvodAvtor.setText(dataProj.get(6).toString());
            insDate.setText(dataProj.get(10).toString());
            insAvtor.setText(dataProj.get(9).toString());

            switch (Integer.valueOf(dataProj.get(13).toString())) {
                case 1:
                    jRadioButton1.setSelected(true);
                    break;
                case 3:
                    jRadioButton2.setSelected(true);
                    break;
                case -2:
                    jRadioButton3.setSelected(true);
                    break;
                default:
                    jRadioButton2.setSelected(true);
                    break;
            }

            try {

                pb = new ProgressBar(ProjectDetalForm.this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            ppdb = new PlanPDB();
                            LOOK = (ppdb.createTempPlanItemTables(idProj) &&
                                    ppdb.createTempPlanFasNewTables(idProj) &&
                                    ppdb.createTempPlanFasColorTables(idProj) &&
                                    ppdb.createTempPlanFasSostavTables(idProj) &&
                                    ppdb.createTempPlanFasDekorTables(idProj)) ? true : false;

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Данные проекта плана не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                if (LOOK) {
                    createStokTable(updateDataTables(UtilPlan.DATA_STOK));
                    createPlanVnormTable(updateDataTables(UtilPlan.DATA_VNORM));
                    createPlanDekadTable(updateDataTables(UtilPlan.DATA_DEKAD));
                    createPlanDetalTab1Table(updateDataTables(UtilPlan.DATA_DETAL));
                    createPlanDetalTab2Table(new Vector());

                    filterHeaderStok.getTable().getRowSorter().addRowSorterListener(tableStokSorterListener);
                    filterHeaderVnorm.getTable().getRowSorter().addRowSorterListener(tableVnormSorterListener);
                    filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
                    // filterHeaderDetalTab1.getTable().getRowSorter().addRowSorterListener(tableDetalTab1SorterListener);
                    filterHeaderDetalTab2.getTable().getRowSorter().addRowSorterListener(tableDetalTab2SorterListener);
                } else {
                    throw new Exception("Ошибка формирования временных таблиц! ");

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка формирования проекта плана производства! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                dispose();
            }

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowListener() {
                public void windowClosed(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    buttClose.doClick();
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

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Данные не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(850, 550));
        setPreferredSize(new Dimension(1000, 750));

        initMenu();

        EDIT = false;
        LOOK = false;

        osnova = new JPanel();
        infoPanel = new JPanel();
        titlePanel = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();
        tableStockPanel = new JPanel();
        tableDetalPanel = new JPanel();
        tableDetalTab1Panel = new JPanel();
        tableDetalTab2Panel = new JPanel();
        buttEastTab1Panel = new JPanel();
        buttEastTab2Panel = new JPanel();
        tableVnormPanel = new JPanel();
        tableDekadPanel = new JPanel();
        panelBox = new JPanel();
        panelBoxTechLoad = new JPanel();
        panelBoxTechLoadKol = new JPanel();

        title = new JLabel();
        projStDate = new JDateChooser();
        vvodDate = new JLabel();
        insDate = new JLabel();
        projName = new JTextField();
        vvodAvtor = new JLabel();
        insAvtor = new JLabel();
        noteText = new JTextPane();
        kolvoIzdPlan = new JLabel();
        workDayProj = new JTextField();
        dept = new JComboBox(UtilPlan.DEPT_MODEL);
        tableTabbedPane = new javax.swing.JTabbedPane();
        tableStok = new JTable();
        tableDetalTab1 = new JTable();
        tableDetalTab2 = new JTable();
        tableVnorm = new JTable();
        tableDekad = new JTable();
        colStok = new Vector();
        colDetalTab1 = new Vector();
        colDetalTab2 = new Vector();
        colVnorm = new Vector();
        colDekad = new Vector();
        dataTable = new Vector();
        filterRowStok = new JPanel();
        //    filterRowDetalTab1 = new JPanel();
        filterRowDetalTab2 = new JPanel();
        filterRowVnorm = new JPanel();
        filterRowDekad = new JPanel();
        filterHeaderStok = new TableFilterHeader(tableStok, AutoChoices.ENABLED);
        filterHeaderVnorm = new TableFilterHeader(tableVnorm, AutoChoices.ENABLED);
        filterHeaderDekad = new TableFilterHeader(tableDekad, AutoChoices.ENABLED);
        filterHeaderDetalTab1 = new TableFilterHeader(tableDetalTab1, AutoChoices.ENABLED);
        filterHeaderDetalTab2 = new TableFilterHeader(tableDetalTab2, AutoChoices.ENABLED);
        tModelStok = new DefaultTableModel();
        tModelDetalTab1 = new DefaultTableModel();
        tModelDetalTab2 = new DefaultTableModel();
        tModelVnorm = new DefaultTableModel();
        tModelDekad = new DefaultTableModel();
        buttEditTable = new JButton("Редактировать");
        buttSave = new JButton("Сохранить");
        buttTech = new JButton("Оборудование");
        buttPrint = new JButton("Печать");
        buttClose = new JButton("Закрыть");
        buttTab1Plus = new JButton("+");
        buttTab1Minus = new JButton("-");
        buttTab1Edit = new JButton("изм");
        buttTab1Shkala = new JButton("шк");
        buttTab2Plus = new JButton("+");
        buttTab2Minus = new JButton("-");
        buttTab2Edit = new JButton("изм");
        buttonGroupStatus = new ButtonGroup();
        buttonGroupTech = new ButtonGroup();
        buttonGroupTechKol = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        loadTech1 = new JRadioButton();
        loadTech2 = new JRadioButton();
        loadTechMonth = new JRadioButton();
        loadTechDek1 = new JRadioButton();
        loadTechDek2 = new JRadioButton();
        loadTechDek3 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.setLayout(new BorderLayout(1, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.setLayout(new ParagraphLayout());
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tableStockPanel.setLayout(new BorderLayout(1, 1));
        tableDetalPanel.setLayout(new GridLayout(2, 0, 5, 5));
        tableDetalTab1Panel.setLayout(new BorderLayout(1, 1));
        tableDetalTab2Panel.setLayout(new BorderLayout(1, 1));
        buttEastTab1Panel.setLayout(new GridLayout(4, 0, 5, 5));
        buttEastTab1Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttEastTab2Panel.setLayout(new GridLayout(4, 0, 5, 5));
        buttEastTab2Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tableVnormPanel.setLayout(new BorderLayout(1, 1));
        tableDekadPanel.setLayout(new BorderLayout(1, 1));
        filterRowStok.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        //   filterRowDetalTab1.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterRowDetalTab2.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterRowVnorm.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterRowDekad.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelBoxTechLoad.setLayout(new GridLayout(2, 1, 5, 5));
        panelBoxTechLoadKol.setLayout(new GridLayout(2, 2, 5, 5));
        panelBoxTechLoadKol.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Кол-во", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12))); // NOI18N
        GridLayout gridLayout = new GridLayout();
        gridLayout.setHgap(5);
        panelBox.setLayout(gridLayout);

        projName.setPreferredSize(new Dimension(650, 20));
        projStDate.setPreferredSize(new Dimension(150, 20));
        dept.setPreferredSize(new Dimension(400, 20));
        vvodDate.setPreferredSize(new Dimension(150, 20));
        vvodAvtor.setPreferredSize(new Dimension(400, 20));
        insDate.setPreferredSize(new Dimension(150, 20));
        insAvtor.setPreferredSize(new Dimension(400, 20));
        noteText.setPreferredSize(new Dimension(700, 100));
        kolvoIzdPlan.setPreferredSize(new Dimension(150, 20));
        workDayProj.setPreferredSize(new Dimension(150, 20));
        buttClose.setPreferredSize(new Dimension(150, 20));

        workDayProj.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodAvtor.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insAvtor.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        kolvoIzdPlan.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        kolvoIzdPlan.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        buttonGroupStatus.add(jRadioButton1);
        buttonGroupStatus.add(jRadioButton2);
        buttonGroupStatus.add(jRadioButton3);

        jRadioButton2.setSelected(true);

        buttonGroupTech.add(loadTech1);
        buttonGroupTech.add(loadTech2);

        buttonGroupTechKol.add(loadTechMonth);
        buttonGroupTechKol.add(loadTechDek1);
        buttonGroupTechKol.add(loadTechDek2);
        buttonGroupTechKol.add(loadTechDek3);

        loadTech1.setSelected(true);
        loadTechMonth.setSelected(true);

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTech1.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTech2.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTechMonth.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTechDek1.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTechDek2.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTechDek3.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTech1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTech2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTechMonth.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTechDek1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTechDek2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTechDek3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setText("Проект;");
        jRadioButton2.setText("Формируется;");
        jRadioButton3.setText("Удалён;");
        loadTech1.setText("по всем моделям");
        loadTech2.setText("только по выделенным моделям");
        loadTechMonth.setText("за месяц;");
        loadTechDek1.setText("1-я декада;");
        loadTechDek2.setText("2-я декада;");
        loadTechDek3.setText("3-я декада;");

        jRadioButton1.setActionCommand("1");
        jRadioButton2.setActionCommand("3");
        jRadioButton3.setActionCommand("-2");
        loadTech1.setActionCommand(loadTech1.getText());
        loadTech2.setActionCommand(loadTech2.getText());
        loadTechMonth.setActionCommand(loadTechMonth.getText());
        loadTechDek1.setActionCommand(loadTechDek1.getText());
        loadTechDek2.setActionCommand(loadTechDek2.getText());
        loadTechDek3.setActionCommand(loadTechDek3.getText());

        loadTechMonth.setMnemonic(0);
        loadTechDek1.setMnemonic(1);
        loadTechDek2.setMnemonic(2);
        loadTechDek3.setMnemonic(3);

        tableStok.setAutoCreateColumnsFromModel(true);
        tableDetalTab1.setAutoCreateColumnsFromModel(true);
        tableDetalTab2.setAutoCreateColumnsFromModel(true);
        tableVnorm.setAutoCreateColumnsFromModel(true);
        tableDekad.setAutoCreateColumnsFromModel(true);

        tableStok.getTableHeader().setReorderingAllowed(false);
        tableDetalTab1.getTableHeader().setReorderingAllowed(false);
        tableDetalTab2.getTableHeader().setReorderingAllowed(false);
        tableVnorm.getTableHeader().setReorderingAllowed(false);
        tableDekad.getTableHeader().setReorderingAllowed(false);

        colStok.add("");
        colStok.add("Вид");
        colStok.add("Название");
        colStok.add("Модель");
        colStok.add("Состав");
        colStok.add("Кол-во(шт.)");
        colStok.add("x2");
        colStok.add("Артикул");
        colStok.add("Рост");
        colStok.add("Размер");
        colStok.add("Расцветка");
        colStok.add("Декор");
        colStok.add("Примечание");
        colStok.add("Новинка");

        colVnorm.add("");
        colVnorm.add("Вид");
        colVnorm.add("Название");
        colVnorm.add("Модель");
        colVnorm.add("Состав");
        colVnorm.add("Кол-во(шт.)");
        colVnorm.add("x2");
        colVnorm.add("idSpec");
        colVnorm.add("fasSpec");
        colVnorm.add("Спецификация");
        colVnorm.add("Трудоемкость на ед.");
        colVnorm.add("Трудоемкость на выпуск");
        colVnorm.add("Новинка");

        colDekad.add("");
        colDekad.add("Вид");
        colDekad.add("Название");
        colDekad.add("Модель");
        colDekad.add("Состав");
        colDekad.add("Кол-во(шт.)");
        colDekad.add("x2");
        colDekad.add("(%) 1декада");
        colDekad.add("(шт.) 1декада");
        colDekad.add("(%) 2декада");
        colDekad.add("(шт.) 2декада");
        colDekad.add("(%) 3декада");
        colDekad.add("(шт.) 3декада");
        colDekad.add("Новинка");

        colDetalTab1.add("");
        colDetalTab1.add("Вид");
        colDetalTab1.add("Название");
        colDetalTab1.add("Модель");
        colDetalTab1.add("Состав");
        colDetalTab1.add("Кол-во(шт.)");
        colDetalTab1.add("x2");
        colDetalTab1.add("Спец.№");
        colDetalTab1.add("Спецификация");
        colDetalTab1.add("Трудоемкость на ед.");
        colDetalTab1.add("Трудоемкость на выпуск");
        colDetalTab1.add("Расцветка");
        colDetalTab1.add("Декор");
        colDetalTab1.add("Дополнение");
        colDetalTab1.add("Примечание");
        colDetalTab1.add("Шифр");
        colDetalTab1.add("Артикул");
        colDetalTab1.add("Новинка");

        colDetalTab2.add("");
        colDetalTab2.add("idPlanItem");
        colDetalTab2.add("Рост");
        colDetalTab2.add("Размер");
        colDetalTab2.add("Кол-во(шт.)");
        colDetalTab2.add("(%) 1декада");
        colDetalTab2.add("(шт.) 1декада");
        colDetalTab2.add("(%) 2декада");
        colDetalTab2.add("(шт.) 2декада");
        colDetalTab2.add("(%) 3декада");
        colDetalTab2.add("(шт.) 3декада");
        colDetalTab2.add("Трудоемкость на выпуск");    
        
        /*
        colDetalTab.add("");
        colDetalTab.add("idPlanItem");
        colDetalTab.add("Вид");  
        colDetalTab.add("Название");
        colDetalTab.add("Модель");           
        colDetalTab.add("Состав"); 
        colDetalTab.add("Рост");   
        colDetalTab.add("Размер");
        colDetalTab.add("Кол-во(шт.)");  
        colDetalTab.add("x2"); 
        colDetalTab.add("(%) 1декада"); 
        colDetalTab.add("(шт.) 1декада");  
        colDetalTab.add("(%) 2декада"); 
        colDetalTab.add("(шт.) 2декада");  
        colDetalTab.add("(%) 3декада"); 
        colDetalTab.add("(шт.) 3декада");  
        colDetalTab.add("Спец.№");  
        colDetalTab.add("Спецификация");        
        colDetalTab.add("Трудоемкость на ед.");
        colDetalTab.add("Трудоемкость на выпуск");  
        colDetalTab.add("Расцветка");
        colDetalTab.add("Примечание");          
        colDetalTab.add("Конвейер");  
        colDetalTab.add("idEmpl");
        colDetalTab.add("ФИО");
        colDetalTab.add("Дата");
        colDetalTab.add("Новинка"); 
         */

        tableColumnModelStokListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableStok.getColumnModel();

                for (int i = 0; i < tcm.getColumnCount(); i++) {
                    JTextField textField = (JTextField) filterRowStok.getComponent(i);
                    Dimension d = textField.getPreferredSize();
                    d.width = tcm.getColumn(i).getWidth();
                    textField.setPreferredSize(d);
                }

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        filterRowStok.revalidate();
                    }
                });
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterRowStok.getComponent(e.getFromIndex());
                filterRowStok.remove(e.getFromIndex());
                filterRowStok.add(moved, e.getToIndex());
                filterRowStok.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };

        tableColumnModelVnormListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableVnorm.getColumnModel();
                if (filterRowVnorm.getComponents().length > 0) {
                    for (int i = 0; i < tcm.getColumnCount(); i++) {
                        JTextField textField = (JTextField) filterRowVnorm.getComponent(i);
                        Dimension d = textField.getPreferredSize();
                        d.width = tcm.getColumn(i).getWidth();
                        textField.setPreferredSize(d);
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            filterRowVnorm.revalidate();
                        }
                    });
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterRowVnorm.getComponent(e.getFromIndex());
                filterRowVnorm.remove(e.getFromIndex());
                filterRowVnorm.add(moved, e.getToIndex());
                filterRowVnorm.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };

        tableColumnModelDekadListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableDekad.getColumnModel();
                if (filterRowDekad.getComponents().length > 0) {
                    for (int i = 0; i < tcm.getColumnCount(); i++) {
                        JTextField textField = (JTextField) filterRowDekad.getComponent(i);
                        Dimension d = textField.getPreferredSize();
                        d.width = tcm.getColumn(i).getWidth();
                        textField.setPreferredSize(d);
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            filterRowDekad.revalidate();
                        }
                    });
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterRowDekad.getComponent(e.getFromIndex());
                filterRowDekad.remove(e.getFromIndex());
                filterRowDekad.add(moved, e.getToIndex());
                filterRowDekad.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        }; 
        /*
        tableColumnModelDetalTab1Listener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableDetalTab1.getColumnModel();
                if(filterRowDetalTab1.getComponents().length>0){
                    for (int i = 0; i < tcm.getColumnCount(); i++) {
                        JTextField textField = (JTextField) filterRowDetalTab1.getComponent(i);
                        Dimension d = textField.getPreferredSize();
                        d.width = tcm.getColumn(i).getWidth();
                        textField.setPreferredSize(d);
                    }
                
                   SwingUtilities.invokeLater(new Runnable() {
                       @Override
                       public void run() {
                           filterRowDetalTab1.revalidate();
                       }
                   });
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterRowDetalTab1.getComponent(e.getFromIndex());
                filterRowDetalTab1.remove(e.getFromIndex());
                filterRowDetalTab1.add(moved, e.getToIndex());
                filterRowDetalTab1.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {     
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };  
        */
        tableColumnModelDetalTab2Listener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableDetalTab2.getColumnModel();
                if (filterRowDetalTab2.getComponents().length > 0) {
                    for (int i = 0; i < tcm.getColumnCount(); i++) {
                        JTextField textField = (JTextField) filterRowDetalTab2.getComponent(i);
                        Dimension d = textField.getPreferredSize();
                        d.width = tcm.getColumn(i).getWidth();
                        textField.setPreferredSize(d);
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            filterRowDetalTab2.revalidate();
                        }
                    });
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterRowDetalTab2.getComponent(e.getFromIndex());
                filterRowDetalTab2.remove(e.getFromIndex());
                filterRowDetalTab2.add(moved, e.getToIndex());
                filterRowDetalTab2.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };

        tableStokSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableStok, filterRowStok, UtilPlan.COL_STOK);
            }
        };

        tableVnormSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableVnorm, filterRowVnorm, UtilPlan.COL_VNORM);
            }
        };

        tableDekadSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableDekad, filterRowDekad, UtilPlan.COL_PJDEKAD);
            }
        };
        /*
        tableDetalTab1SorterListener = new RowSorterListener(){
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableDetalTab1, filterRowDetalTab1, UtilPlan.COL_PJDETAL1);  
            }
        };
        */

        tableDetalTab2SorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableDetalTab2, filterRowDetalTab2, UtilPlan.COL_PJDETAL2);
            }
        };

        tableDetalTab1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    buttTab1Shkala.doClick();
                }
            }
        });

        tableDetalTab1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttTab1Shkala.doClick();
                }
            }
        });

        tableStok.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowStok = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowStok = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableVnorm.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowVnorm = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowVnorm = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableDekad.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowDekad = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowDekad = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableDetalTab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowDetalTab1 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowDetalTab1 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableDetalTab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowDetalTab2 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowDetalTab2 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

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

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
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

        buttTab1Shkala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1SelectActionPerformed(evt);
            }
        });

        buttTab1Plus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1PlusActionPerformed(evt);
            }
        });

        buttTab1Minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1MinusActionPerformed(evt);
            }
        });

        buttTab1Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1EditActionPerformed(evt);
            }
        });

        buttTab2Plus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2PlusActionPerformed(evt);
            }
        });

        buttTab2Minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2MinusActionPerformed(evt);
            }
        });

        buttTab2Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2EditActionPerformed(evt);
            }
        });

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

        panelBoxTechLoad.add(loadTech1);
        panelBoxTechLoad.add(loadTech2);

        panelBoxTechLoadKol.add(loadTechMonth);
        panelBoxTechLoadKol.add(loadTechDek1);
        panelBoxTechLoadKol.add(loadTechDek2);
        panelBoxTechLoadKol.add(loadTechDek3);

        titlePanel.add(new JLabel("Цех: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(dept);
        titlePanel.add(new JLabel("Название: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(projName);
        titlePanel.add(new JLabel("Дата: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(projStDate);
        titlePanel.add(new JLabel("Рабочие дни: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(workDayProj);
        titlePanel.add(new JLabel("Статус:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(jRadioButton1);
        titlePanel.add(jRadioButton2);
        titlePanel.add(jRadioButton3);
        titlePanel.add(new JLabel("Дата ввода: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(vvodDate);
        titlePanel.add(new JLabel(" Автор ввода:"));
        titlePanel.add(vvodAvtor);
        titlePanel.add(new JLabel("Дата корр.: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(insDate);
        titlePanel.add(new JLabel(" Автор корр. :"));
        titlePanel.add(insAvtor);
        titlePanel.add(new JLabel("Примечание: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(noteText);

        infoPanel.add(title, BorderLayout.NORTH);
        infoPanel.add(titlePanel, BorderLayout.CENTER);

        buttEastTab1Panel.add(buttTab1Shkala);
        buttEastTab1Panel.add(buttTab1Plus);
        buttEastTab1Panel.add(buttTab1Minus);
        buttEastTab1Panel.add(buttTab1Edit);

        //buttEastTab2Panel.add(buttTab2Plus); 
        buttEastTab2Panel.add(buttTab2Minus);
        //buttEastTab2Panel.add(buttTab2Edit);        

        tableStockPanel.add(new JScrollPane(tableStok), BorderLayout.CENTER);
        tableStockPanel.add(filterRowStok, BorderLayout.SOUTH);

        tableVnormPanel.add(new JScrollPane(tableVnorm), BorderLayout.CENTER);
        tableVnormPanel.add(filterRowVnorm, BorderLayout.SOUTH);

        tableDekadPanel.add(new JScrollPane(tableDekad), BorderLayout.CENTER);
        tableDekadPanel.add(filterRowDekad, BorderLayout.SOUTH);

        tableDetalTab1Panel.add(new JScrollPane(tableDetalTab1), BorderLayout.CENTER);
        tableDetalTab1Panel.add(buttEastTab1Panel, BorderLayout.EAST);
        //tableDetalTab1Panel.add(filterRowDetalTab1, BorderLayout.SOUTH);

        tableDetalTab2Panel.add(new JScrollPane(tableDetalTab2), BorderLayout.CENTER);
        tableDetalTab2Panel.add(buttEastTab2Panel, BorderLayout.EAST);
        tableDetalTab2Panel.add(filterRowDetalTab2, BorderLayout.SOUTH);

        tableDetalPanel.add(tableDetalTab1Panel);
        tableDetalPanel.add(tableDetalTab2Panel);

        tableTabbedPane.addTab("информация", infoPanel);
        tableTabbedPane.addTab("ассортимент", tableStockPanel);
        tableTabbedPane.addTab("трудоемкость", tableVnormPanel);
        tableTabbedPane.addTab("декады", tableDekadPanel);
        tableTabbedPane.addTab("проект", tableDetalPanel);
        //tableTabbedPane.addTab("подробный проект", tableDetalPanel);

        centerPanel.add(tableTabbedPane, BorderLayout.CENTER);

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        buttPanel.add(buttSave);

        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu2.setText("Сервис");

        jMenuItem4.setText("Проверить спецификации");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Просмотр");

        jMenuItem1.setText("Загрузка цеха");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem5.setText("Загрузка оборудования");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setText("Потребность сырья на выпуск");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem7.setText("Расход сырья по моделям");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuBar1.add(jMenu3);

        jMenu1.setText("Документы");

        jMenuItem3.setText("Проект плана (ассортимент по наименованию)");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem10.setText("Проект плана (ассортимент по модели)");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);

        jMenuItem2.setText("Проект плана (трудоемкость)");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem8.setText("Новинки");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuItem9.setText("Штучная печать");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem9);

        jMenuItem11.setText("Потребность сырья на выпуск");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem11);

        jMenuItem12.setText("Расход полотна по моделям");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        jMenuItem13.setText("Проект плана (с артикулом)");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem13);

        jMenuItem14.setText("Проект плана (с артикулом и фото)");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem14);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);
    }

    private void jMenuItem1ActionPerformed(ActionEvent evt) {
        try {
            double[] temp = new double[4];

            for (int i = 0; i < tModelVnorm.getDataVector().size(); i++) {
                temp[0] += Double.valueOf(((Vector) tModelVnorm.getDataVector().get(i)).elementAt(5).toString().trim());
                temp[1] += 0;
                temp[2] += Double.valueOf(((Vector) tModelVnorm.getDataVector().get(i)).elementAt(11).toString().trim());
                temp[3] += 0;
            }

            new CapacityForm(this, true, Integer.valueOf(workDayProj.getText()), temp);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem2ActionPerformed(ActionEvent evt) {
        try {
            Vector dataReport = new Vector();

            if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                try {
                    ppdb = new PlanPDB();
                    dataReport = ppdb.getDataReportProjectPlanVnorm(idProj);
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }
            } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                    typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                try {
                    dataReport = ppdb.getDataReportProjectPlanVnormTemp();
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            PlanOO oo = new PlanOO(new SimpleDateFormat("MM.yyyy").format(projStDate.getDate()), dataReport);
            oo.createReport("ProjectPlanVnorm.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem3ActionPerformed(ActionEvent evt) {
        try {
            Vector dataReport = new Vector();

            if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                try {
                    ppdb = new PlanPDB();
                    dataReport = ppdb.getDataReportProjectPlanStok(idProj, 1);
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }
            } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                    typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                try {
                    dataReport = ppdb.getDataReportProjectPlanStokTemp(1);
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            PlanOO oo = new PlanOO(new SimpleDateFormat("MM.yyyy").format(projStDate.getDate()), dataReport);
            oo.createReport("ProjectPlanStok.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem4ActionPerformed(ActionEvent evt) {
        try {
            JTextPane infoText = new JTextPane();
            infoText.setPreferredSize(new Dimension(330, 80));
            infoText.setText("");

            Vector checkSpec = new Vector();
            Vector checkModel = new Vector();

            for (int i = 0; i < tModelVnorm.getRowCount(); i++) {
                if (Integer.valueOf(tModelVnorm.getValueAt(i, 8).toString()) < 0) {
                    checkSpec.add(tModelVnorm.getValueAt(i, 3).toString());
                } else {
                    if (!tModelVnorm.getValueAt(i, 8).toString().equals(tModelVnorm.getValueAt(i, 3).toString())) {
                        checkModel.add(tModelVnorm.getValueAt(i, 3).toString());
                    }
                }
            }

            if (checkSpec.isEmpty() && checkModel.isEmpty()) {
                infoText.setText("Все спецификации заданы корректно!");
            } else {
                if (!checkSpec.isEmpty()) {
                    infoText.setText(infoText.getText() + "Отсутствует спецификация в модели: " + checkSpec.toString() + "\n");
                }

                if (!checkModel.isEmpty()) {
                    infoText.setText(infoText.getText() + "Спецификация не соответствует модели: " + checkModel.toString() + "\n");
                }
            }

            JOptionPane.showOptionDialog(null, new JScrollPane(infoText), "Проверка проекта ", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Закрыть"}, "Закрыть");


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem5ActionPerformed(ActionEvent evt) {
        data = new Vector();
        try {
            panelBox.removeAll();
            panelBox.revalidate();

            panelBox.add(panelBoxTechLoad);

            panelBoxTechLoad.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Модели", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12))); // NOI18N

            loadTech2.setEnabled(true);
            loadTechMonth.setSelected(true);

            if (tableTabbedPane.getModel().getSelectedIndex() == 0 ||
                    tableTabbedPane.getModel().getSelectedIndex() == 1 ||
                    tableTabbedPane.getModel().getSelectedIndex() == 3 ||
                    tableTabbedPane.getModel().getSelectedIndex() == 4) {
                loadTech2.setEnabled(false);
                loadTech1.setSelected(true);
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 2) {
                panelBox.add(panelBoxTechLoad);
                loadTech2.setEnabled(true);
                loadTech1.setSelected(true);
            }

            if (JOptionPane.showOptionDialog(null, panelBox, "Загрузка оборудования ", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Сформировать", "Отмена"}, "Сформировать") == JOptionPane.YES_OPTION) {

                if (loadTech1.isSelected()) {
                    data = tModelVnorm.getDataVector();
                }

                if (loadTech2.isSelected()) {
                    pb = new ProgressBar(this, false, "Сбор данных ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                for (Object row : tModelVnorm.getDataVector()) {
                                    if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                                        data.add((Vector) row);
                                    }
                                }
                            } catch (Exception e) {
                                data = new Vector();
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
                }
                new TableTechForm(this, true,
                        data,
                        idProj,
                        Integer.valueOf(workDayProj.getText()),
                        buttonGroupTech.getSelection().getActionCommand(),
                        buttonGroupTechKol.getSelection().getActionCommand(),
                        buttonGroupTechKol.getSelection().getMnemonic());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem6ActionPerformed(ActionEvent evt) {
        data = new Vector();
        try {
            panelBox.removeAll();
            panelBox.revalidate();

            panelBoxTechLoad.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Модели", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12))); // NOI18N

            if (tableTabbedPane.getModel().getSelectedIndex() == 0) {
                panelBox.add(panelBoxTechLoad);

                loadTech2.setEnabled(false);
                loadTech1.setSelected(true);
                loadTechMonth.setSelected(true);
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 1 ||
                    tableTabbedPane.getModel().getSelectedIndex() == 2) {
                panelBox.add(panelBoxTechLoad);

                loadTech2.setEnabled(false);
                loadTech1.setSelected(true);
                loadTechMonth.setSelected(true);
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 3) {
                panelBox.add(panelBoxTechLoad);
                panelBox.add(panelBoxTechLoadKol);

                loadTech2.setEnabled(false);
                loadTech1.setSelected(true);
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 4) {
                panelBox.add(panelBoxTechLoad);

                loadTech2.setEnabled(true);
                loadTech1.setSelected(true);
                loadTechMonth.setSelected(true);
            }

            if (JOptionPane.showOptionDialog(null, panelBox, "Расход сырья", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Сформировать", "Отмена"}, "Сформировать") == JOptionPane.YES_OPTION) {

                if (loadTech1.isSelected()) {
                    try {
                        if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                            try {
                                ppdb = new PlanPDB();
                                data = ppdb.getDataMaterialTable(idProj);
                            } catch (Exception e) {
                                data = new Vector();
                                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            } finally {
                                ppdb.disConn();
                            }

                        } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                                typeProj.equals(UtilPlan.PROJECT_COPY) ||
                                typeProj.equals(UtilPlan.PROJECT_EDIT)) {

                            data = ppdb.getDataMaterialTableTemp();
                        }
                    } catch (Exception e) {
                        data = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (loadTech2.isSelected()) {
                    pb = new ProgressBar(this, false, "Сбор данных ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                if (tableTabbedPane.getModel().getSelectedIndex() == 4) {
                                    for (Object row : tModelDetalTab1.getDataVector()) {
                                        if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                                            Vector tmp = new Vector();
                                            tmp.add(((Vector) row).get(3));
                                            tmp.add(((Vector) row).get(5));
                                            tmp.add(new Double(0));
                                            tmp.add(new Double(0));
                                            tmp.add(new Double(0));
                                            tmp.add(((Vector) row).get(13));
                                            tmp.add(((Vector) row).get(15));
                                            tmp.add(((Vector) row).get(16));
                                            data.add(tmp);
                                        }
                                    }

                                    loadTechMonth.setSelected(true);
                                } else
                                    data = new Vector();

                            } catch (Exception e) {
                                data = new Vector();
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
                }
                new TableMaterialForm(ProjectDetalForm.this, true,
                        data,
                        idProj,
                        buttonGroupTech.getSelection().getActionCommand(),
                        buttonGroupTechKol.getSelection().getActionCommand(),
                        buttonGroupTechKol.getSelection().getMnemonic(),
                        typeProj);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem7ActionPerformed(ActionEvent evt) {
        data = new Vector();
        try {
            pb = new ProgressBar(this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                            try {
                                ppdb = new PlanPDB();
                                data = ppdb.getRashodMaterialModels(idProj);
                            } catch (Exception e) {
                                data = new Vector();
                                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            } finally {
                                ppdb.disConn();
                            }

                        } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                                typeProj.equals(UtilPlan.PROJECT_COPY) ||
                                typeProj.equals(UtilPlan.PROJECT_EDIT)) {

                            data = ppdb.getRashodMaterialModelsTemp();
                        }
                    } catch (Exception e) {
                        data = new Vector();
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

            new SmallTableForm(controller, ProjectDetalForm.this,
                    true,
                    data,
                    title.getText(),
                    (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                            typeProj.equals(UtilPlan.PROJECT_COPY) ||
                            typeProj.equals(UtilPlan.PROJECT_EDIT)) ? true : false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem8ActionPerformed(ActionEvent evt) {
        try {
            Vector dataReport = new Vector();

            if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                try {
                    ppdb = new PlanPDB();
                    dataReport = ppdb.getDataReportProjectPlanFasNew(idProj);
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }
            } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                    typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                try {
                    dataReport = ppdb.getDataReportProjectPlanFasNewTemp();
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            PlanOO oo = new PlanOO(new SimpleDateFormat("MM.yyyy").format(projStDate.getDate()), dataReport);
            oo.createReport("ProjectPlanStok.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem9ActionPerformed(ActionEvent evt) {
        try {
            Vector dataReport = new Vector();

            if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                try {
                    ppdb = new PlanPDB();
                    dataReport = ppdb.getDataReportProjectPlanDekorPrint(idProj);
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }
            } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                    typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                try {
                    dataReport = ppdb.getDataReportProjectPlanDekorPrintTemp();
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            PlanOO oo = new PlanOO(new SimpleDateFormat("MM.yyyy").format(projStDate.getDate()), dataReport);
            oo.createReport("ProjectPlanStok.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem10ActionPerformed(ActionEvent evt) {
        try {
            Vector dataReport = new Vector();

            if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                try {
                    ppdb = new PlanPDB();
                    dataReport = ppdb.getDataReportProjectPlanStok(idProj, 2);
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }
            } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                    typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                try {
                    dataReport = ppdb.getDataReportProjectPlanStokTemp(2);
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            PlanOO oo = new PlanOO(new SimpleDateFormat("MM.yyyy").format(projStDate.getDate()), dataReport);
            oo.createReport("ProjectPlanStok.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem11ActionPerformed(ActionEvent evt) {
        try {
            Vector dataReport = new Vector();

            if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                try {
                    ppdb = new PlanPDB();
                    dataReport = ppdb.getReportRashodMaterialProject(idProj);
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }
            } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                    typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                try {
                    dataReport = ppdb.getReportRashodMaterialProjectTemp();
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            PlanOO oo = new PlanOO(new SimpleDateFormat("MM.yyyy").format(projStDate.getDate()), dataReport);
            oo.createReport("ProjectPlanMaterials.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem12ActionPerformed(ActionEvent evt) {
        try {
            Vector dataReport = new Vector();

            if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                try {
                    ppdb = new PlanPDB();
                    dataReport = ppdb.getReportRashodMaterialModelsProject(idProj);
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    ppdb.disConn();
                }
            } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                    typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                try {
                    dataReport = ppdb.getReportRashodMaterialModelsProjectTemp();
                } catch (Exception e) {
                    dataReport = new Vector();
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            PlanOO oo = new PlanOO(new SimpleDateFormat("MM.yyyy").format(projStDate.getDate()), dataReport);
            oo.createReport("ProjectPlanMaterialsModel.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem13ActionPerformed(ActionEvent evt) {
        try {

            PlanOO oo = new PlanOO(projName.getText().trim(), getDataStokCenaReport());
            oo.createReport("ProjectPlanStokCena.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem14ActionPerformed(ActionEvent evt) {
        try {

            PlanOO oo = new PlanOO(projName.getText().trim(), getDataStokCenaReport());
            oo.createReport("ProjectPlanStokCenaImage.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        try {
            if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                dispose();
            } else {
                if (JOptionPane.showOptionDialog(null, "Сохранить изменения?", "Сохранение...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION)
                    buttSave.doClick();
                else {
                    ppdb.disConn();
                    dispose();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) dept.getSelectedItem()).getId()), UtilPlan.SETTING_DEPT_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            if (tableTabbedPane.getModel().getSelectedIndex() == 1) {
                PlanOO oo = new PlanOO(title.getText(), tModelStok, tableStok.getColumnModel());
                oo.createReport("DefaultTableAlbumFormatCheck.ots");
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 2) {
                PlanOO oo = new PlanOO(title.getText(), tModelVnorm, tableVnorm.getColumnModel());
                oo.createReport("DefaultTableAlbumFormatCheck.ots");
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 3) {
                PlanOO oo = new PlanOO(title.getText(), tModelDekad, tableDekad.getColumnModel());
                oo.createReport("DefaultTableAlbumFormatCheck.ots");
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 4) {
                boolean flag = false;
                for (Object row : tModelDetalTab2.getDataVector()) {
                    if (((Vector) row).get(0).toString().equals("true")) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    PlanOO oo = new PlanOO(title.getText(), tModelDetalTab2, tableDetalTab2.getColumnModel());
                    oo.createReport("DefaultTableAlbumFormatCheck.ots");
                } else {
                    PlanOO oo = new PlanOO(title.getText(), tModelDetalTab1, tableDetalTab1.getColumnModel());
                    oo.createReport("DefaultTableAlbumFormatCheck.ots");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            if (EDIT) {
                boolean saveFlag = true;
                String str = "";

                if (((Item) dept.getSelectedItem()).getId() == -1) {
                    saveFlag = false;
                    str += "Вы не выбрали цех!\n";
                }

                if (projName.getText().trim().equals("")) {
                    saveFlag = false;
                    str += "Вы не ввели название проекта плана!\n";
                }

                try {
                    if (workDayProj.getText().trim().equals("")) {
                        saveFlag = false;
                        str += "Вы не ввели кол-во рабочих дней!\n";
                    } else
                        Integer.valueOf(workDayProj.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Кол-во рабочих дней задано некорректно!\n";
                }

                if (ppdb.checkKolvoPlanTableTemp() <= 0) {
                    saveFlag = false;
                    str += "В проекте нет записей!\n";
                }

                if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(projStDate.getDate()))) {
                    saveFlag = false;
                }

                if (!saveFlag) {
                    JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

                if (saveFlag) {
                    pb = new ProgressBar(ProjectDetalForm.this, false, "Сохранение проекта плана ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                if (typeProj.equals(UtilPlan.PROJECT_ADD) || typeProj.equals(UtilPlan.PROJECT_COPY)) {
                                    ppdb.addTempProjectTable(projName.getText().trim(),
                                            ((Item) dept.getSelectedItem()).getId(),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(projStDate.getDate())),
                                            Integer.valueOf(buttonGroupStatus.getSelection().getActionCommand()),
                                            noteText.getText().trim(),
                                            Integer.valueOf(user.getIdEmployee()),
                                            Integer.valueOf(workDayProj.getText()));

                                    JOptionPane.showMessageDialog(null, "Проект плана производства успешно добавлен! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                    ppdb.disConn();
                                    dispose();
                                } else if (typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                                    ppdb.updateTempProjectTable(idProj,
                                            projName.getText().trim(),
                                            ((Item) dept.getSelectedItem()).getId(),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(projStDate.getDate())),
                                            Integer.valueOf(buttonGroupStatus.getSelection().getActionCommand()),
                                            noteText.getText().trim(),
                                            Integer.valueOf(user.getIdEmployee()),
                                            Integer.valueOf(workDayProj.getText()));

                                    JOptionPane.showMessageDialog(null, "Проект плана производства успешно сохранен! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                    ppdb.disConn();
                                    dispose();
                                }
                            } catch (Exception e) {
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
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1SelectActionPerformed(ActionEvent evt) {
        try {
            if (tableDetalTab1.getSelectedRow() != -1) {
                Vector row = new Vector();

                if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                    try {
                        ppdb = new PlanPDB();
                        row = ppdb.getShkalaFasProjectDetalTable(idProj,
                                Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 3).toString()),
                                Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 13).toString()),
                                Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 15).toString()),
                                tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 16).toString());
                    } catch (Exception e) {
                        row = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        ppdb.disConn();
                    }
                } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                        typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                    try {
                        row = ppdb.getShkalaFasProjectDetalTemp(Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 3).toString()),
                                Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 13).toString()),
                                Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 15).toString()),
                                tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 16).toString());
                    } catch (Exception e) {
                        row = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }

                createPlanDetalTab2Table(row);
            } else
                JOptionPane.showMessageDialog(null, "Вы ничего не выбрали!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1PlusActionPerformed(ActionEvent evt) {
        try {
            new ProjectItemForm(controller, this, true);

            if (UtilPlan.EDIT_BUTT_ACTION) {

                filterHeaderStok.getTable().getRowSorter().removeRowSorterListener(tableStokSorterListener);
                filterHeaderVnorm.getTable().getRowSorter().removeRowSorterListener(tableVnormSorterListener);
                filterHeaderDekad.getTable().getRowSorter().removeRowSorterListener(tableDekadSorterListener);
                filterHeaderDetalTab1.getTable().getRowSorter().removeRowSorterListener(tableDetalTab1SorterListener);
                filterHeaderDetalTab2.getTable().getRowSorter().removeRowSorterListener(tableDetalTab2SorterListener);

                createStokTable(updateDataTables(UtilPlan.DATA_STOK));
                createPlanVnormTable(updateDataTables(UtilPlan.DATA_VNORM));
                createPlanDekadTable(updateDataTables(UtilPlan.DATA_DEKAD));
                createPlanDetalTab1Table(updateDataTables(UtilPlan.DATA_DETAL));
                createPlanDetalTab2Table(new Vector());

                filterHeaderStok.getTable().getRowSorter().addRowSorterListener(tableStokSorterListener);
                filterHeaderVnorm.getTable().getRowSorter().addRowSorterListener(tableVnormSorterListener);
                filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
                filterHeaderDetalTab1.getTable().getRowSorter().addRowSorterListener(tableDetalTab1SorterListener);
                filterHeaderDetalTab2.getTable().getRowSorter().addRowSorterListener(tableDetalTab2SorterListener);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1MinusActionPerformed(ActionEvent evt) {
        try {
            Vector delete = new Vector();
            for (Object row : tModelDetalTab1.getDataVector()) {
                if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                    delete.add(row);
                }
            }

            if (delete.size() > 0) {
                if (JOptionPane.showOptionDialog(null, "Удалить отмеченные строки?", "Удаление ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Удалить", "Отмена"}, "Удалить") == JOptionPane.YES_OPTION) {

                    if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                            typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                        try {
                            ppdb.deleteItemsProjectPlanTemp(delete);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    filterHeaderStok.getTable().getRowSorter().removeRowSorterListener(tableStokSorterListener);
                    filterHeaderVnorm.getTable().getRowSorter().removeRowSorterListener(tableVnormSorterListener);
                    filterHeaderDekad.getTable().getRowSorter().removeRowSorterListener(tableDekadSorterListener);
                    filterHeaderDetalTab1.getTable().getRowSorter().removeRowSorterListener(tableDetalTab1SorterListener);
                    filterHeaderDetalTab2.getTable().getRowSorter().removeRowSorterListener(tableDetalTab2SorterListener);

                    createStokTable(updateDataTables(UtilPlan.DATA_STOK));
                    createPlanVnormTable(updateDataTables(UtilPlan.DATA_VNORM));
                    createPlanDekadTable(updateDataTables(UtilPlan.DATA_DEKAD));
                    createPlanDetalTab1Table(updateDataTables(UtilPlan.DATA_DETAL));
                    createPlanDetalTab2Table(new Vector());

                    filterHeaderStok.getTable().getRowSorter().addRowSorterListener(tableStokSorterListener);
                    filterHeaderVnorm.getTable().getRowSorter().addRowSorterListener(tableVnormSorterListener);
                    filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
                    filterHeaderDetalTab1.getTable().getRowSorter().addRowSorterListener(tableDetalTab1SorterListener);
                    filterHeaderDetalTab2.getTable().getRowSorter().addRowSorterListener(tableDetalTab2SorterListener);
                }
            } else
                JOptionPane.showMessageDialog(null, "Вы ничего не выбрали!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1EditActionPerformed(ActionEvent evt) {
        try {
            if (tableDetalTab1.getSelectedRow() != -1) {
                dataItem = new Vector();
                dataShkala = new Vector();
                dataSostav = new Vector();
                dataColor = new Vector();

                final Vector update = new Vector();
                update.add(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 3).toString());
                update.add(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 15).toString());
                update.add(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 16).toString());

                pb = new ProgressBar(ProjectDetalForm.this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            dataItem = ppdb.getDataItemProject(Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 3).toString()),
                                    Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 13).toString()),
                                    Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 15).toString()),
                                    tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 16).toString().trim().toUpperCase());
                            dataShkala = ppdb.getDataShkalaProject(Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 3).toString()),
                                    Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 13).toString()),
                                    Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 15).toString()),
                                    tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 16).toString().trim().toUpperCase());
                            dataSostav = ppdb.getDataSostavProject(Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 3).toString()),
                                    Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 13).toString()),
                                    Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 15).toString()),
                                    tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 16).toString().trim().toUpperCase());
                            dataColor = ppdb.getDataColorProject(Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 3).toString()),
                                    Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 13).toString()),
                                    Integer.valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 15).toString()),
                                    tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 16).toString().trim().toUpperCase());

                        } catch (Exception e) {
                            dataItem = new Vector();
                            dataShkala = new Vector();
                            dataSostav = new Vector();
                            dataColor = new Vector();
                            JOptionPane.showMessageDialog(null, "Данные проекта плана производства не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                new ProjectItemForm(controller, this, true, dataItem, dataShkala, dataSostav, dataColor, update);

                if (UtilPlan.EDIT_BUTT_ACTION) {

                    filterHeaderStok.getTable().getRowSorter().removeRowSorterListener(tableStokSorterListener);
                    filterHeaderVnorm.getTable().getRowSorter().removeRowSorterListener(tableVnormSorterListener);
                    filterHeaderDekad.getTable().getRowSorter().removeRowSorterListener(tableDekadSorterListener);
                    filterHeaderDetalTab1.getTable().getRowSorter().removeRowSorterListener(tableDetalTab1SorterListener);
                    filterHeaderDetalTab2.getTable().getRowSorter().removeRowSorterListener(tableDetalTab2SorterListener);

                    createStokTable(updateDataTables(UtilPlan.DATA_STOK));
                    createPlanVnormTable(updateDataTables(UtilPlan.DATA_VNORM));
                    createPlanDekadTable(updateDataTables(UtilPlan.DATA_DEKAD));
                    createPlanDetalTab1Table(updateDataTables(UtilPlan.DATA_DETAL));
                    createPlanDetalTab2Table(new Vector());

                    filterHeaderStok.getTable().getRowSorter().addRowSorterListener(tableStokSorterListener);
                    filterHeaderVnorm.getTable().getRowSorter().addRowSorterListener(tableVnormSorterListener);
                    filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
                    filterHeaderDetalTab1.getTable().getRowSorter().addRowSorterListener(tableDetalTab1SorterListener);
                    filterHeaderDetalTab2.getTable().getRowSorter().addRowSorterListener(tableDetalTab2SorterListener);
                }
            } else
                JOptionPane.showMessageDialog(null, "Вы ничего не выбрали!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2PlusActionPerformed(ActionEvent evt) {
        try {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2MinusActionPerformed(ActionEvent evt) {
        try {
            Vector delete = new Vector();
            for (Object row : tModelDetalTab2.getDataVector()) {
                if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                    delete.add(Integer.valueOf(((Vector) row).get(1).toString()));
                }
            }

            if (delete.size() > 0) {
                if (JOptionPane.showOptionDialog(null, "Удалить отмеченные строки?", "Удаление ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Удалить", "Отмена"}, "Удалить") == JOptionPane.YES_OPTION) {

                    if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                            typeProj.equals(UtilPlan.PROJECT_COPY) || typeProj.equals(UtilPlan.PROJECT_EDIT)) {
                        try {
                            ppdb.deleteItemProjectPlanTemp(delete);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    filterHeaderStok.getTable().getRowSorter().removeRowSorterListener(tableStokSorterListener);
                    filterHeaderVnorm.getTable().getRowSorter().removeRowSorterListener(tableVnormSorterListener);
                    filterHeaderDekad.getTable().getRowSorter().removeRowSorterListener(tableDekadSorterListener);
                    filterHeaderDetalTab1.getTable().getRowSorter().removeRowSorterListener(tableDetalTab1SorterListener);
                    filterHeaderDetalTab2.getTable().getRowSorter().removeRowSorterListener(tableDetalTab2SorterListener);

                    createStokTable(updateDataTables(UtilPlan.DATA_STOK));
                    createPlanVnormTable(updateDataTables(UtilPlan.DATA_VNORM));
                    createPlanDekadTable(updateDataTables(UtilPlan.DATA_DEKAD));
                    createPlanDetalTab1Table(updateDataTables(UtilPlan.DATA_DETAL));
                    createPlanDetalTab2Table(new Vector());

                    filterHeaderStok.getTable().getRowSorter().addRowSorterListener(tableStokSorterListener);
                    filterHeaderVnorm.getTable().getRowSorter().addRowSorterListener(tableVnormSorterListener);
                    filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
                    filterHeaderDetalTab1.getTable().getRowSorter().addRowSorterListener(tableDetalTab1SorterListener);
                    filterHeaderDetalTab2.getTable().getRowSorter().addRowSorterListener(tableDetalTab2SorterListener);
                }
            } else
                JOptionPane.showMessageDialog(null, "Вы ничего не выбрали!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2EditActionPerformed(ActionEvent evt) {
        try {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createStokTable(final Vector rowAll) {
        tModelStok = new DefaultTableModel(rowAll, colStok) {
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

        tModelStok.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tablePlanModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowStok == -1 || minSelectedRowStok == -1) {
                    return;
                }
                tablePlanModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelStok.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowStok; i <= maxSelectedRowStok; i++) {
                    tModelStok.setValueAt(Boolean.valueOf(value), tableStok.convertRowIndexToModel(i), column);
                }

                minSelectedRowStok = -1;
                maxSelectedRowStok = -1;

                tablePlanModelListenerIsChanging = false;
            }
        });

        tableStok.setModel(tModelStok);
        tableStok.setAutoCreateColumnsFromModel(true);

        tableStok.getColumnModel().getColumn(0).setPreferredWidth(1);
        tableStok.getColumnModel().getColumn(1).setPreferredWidth(5);
        tableStok.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableStok.getColumnModel().getColumn(3).setPreferredWidth(50);
        tableStok.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableStok.getColumnModel().getColumn(5).setPreferredWidth(70);
        tableStok.getColumnModel().getColumn(6).setPreferredWidth(1);
        tableStok.getColumnModel().getColumn(7).setPreferredWidth(80);
        tableStok.getColumnModel().getColumn(8).setPreferredWidth(50);
        tableStok.getColumnModel().getColumn(9).setPreferredWidth(50);
        tableStok.getColumnModel().getColumn(10).setPreferredWidth(50);
        tableStok.getColumnModel().getColumn(11).setPreferredWidth(50);
        tableStok.getColumnModel().getColumn(12).setMinWidth(0);
        tableStok.getColumnModel().getColumn(12).setMaxWidth(0);

        tableStok.getColumnModel().getColumn(3).setCellRenderer(renderer);

        tableStok.getColumnModel().addColumnModelListener(tableColumnModelStokListener);
        UtilPlan.setFooterTable(tableStok, filterRowStok, UtilPlan.COL_STOK, UtilPlan.countSumm(tableStok, UtilPlan.COL_STOK));

        tableStok.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableStok.getTableHeader(), 0, ""));
    }

    private void createPlanVnormTable(final Vector rowAll) {
        tModelVnorm = new DefaultTableModel(rowAll, colVnorm) {
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

        tModelVnorm.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableVnormModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowVnorm == -1 || minSelectedRowVnorm == -1) {
                    return;
                }
                tableVnormModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelVnorm.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowVnorm; i <= maxSelectedRowVnorm; i++) {
                    tModelVnorm.setValueAt(Boolean.valueOf(value), tableVnorm.convertRowIndexToModel(i), column);
                }

                minSelectedRowVnorm = -1;
                maxSelectedRowVnorm = -1;

                tableVnormModelListenerIsChanging = false;
            }
        });

        tableVnorm.setModel(tModelVnorm);
        tableVnorm.setAutoCreateColumnsFromModel(true);

        tableVnorm.getColumnModel().getColumn(0).setPreferredWidth(1);
        tableVnorm.getColumnModel().getColumn(1).setPreferredWidth(10);
        tableVnorm.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableVnorm.getColumnModel().getColumn(3).setPreferredWidth(50);
        tableVnorm.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableVnorm.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableVnorm.getColumnModel().getColumn(6).setPreferredWidth(5);
        tableVnorm.getColumnModel().getColumn(7).setMinWidth(0);
        tableVnorm.getColumnModel().getColumn(7).setMaxWidth(0);
        tableVnorm.getColumnModel().getColumn(8).setMinWidth(0);
        tableVnorm.getColumnModel().getColumn(8).setMaxWidth(0);
        tableVnorm.getColumnModel().getColumn(9).setPreferredWidth(100);
        tableVnorm.getColumnModel().getColumn(10).setPreferredWidth(100);
        tableVnorm.getColumnModel().getColumn(11).setPreferredWidth(100);
        tableVnorm.getColumnModel().getColumn(12).setMinWidth(0);
        tableVnorm.getColumnModel().getColumn(12).setMaxWidth(0);

        tableVnorm.getColumnModel().getColumn(3).setCellRenderer(renderer);

        tableVnorm.getColumnModel().addColumnModelListener(tableColumnModelVnormListener);
        UtilPlan.setFooterTable(tableVnorm, filterRowVnorm, UtilPlan.COL_VNORM, UtilPlan.countSumm(tableVnorm, UtilPlan.COL_VNORM));

        tableVnorm.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableVnorm.getTableHeader(), 0, ""));
    }

    private void createPlanDekadTable(final Vector rowAll) {
        tModelDekad = new DefaultTableModel(rowAll, colDekad) {
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

        tModelDekad.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableDekadModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowDekad == -1 || minSelectedRowDekad == -1) {
                    return;
                }
                tableDekadModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelDekad.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowDekad; i <= maxSelectedRowDekad; i++) {
                    tModelDekad.setValueAt(Boolean.valueOf(value), tableDekad.convertRowIndexToModel(i), column);
                }

                minSelectedRowDekad = -1;
                maxSelectedRowDekad = -1;

                tableDekadModelListenerIsChanging = false;
            }
        });

        tableDekad.setModel(tModelDekad);
        tableDekad.setAutoCreateColumnsFromModel(true);

        tableDekad.getColumnModel().getColumn(0).setPreferredWidth(1);
        tableDekad.getColumnModel().getColumn(1).setPreferredWidth(10);
        tableDekad.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableDekad.getColumnModel().getColumn(3).setPreferredWidth(50);
        tableDekad.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableDekad.getColumnModel().getColumn(5).setPreferredWidth(70);
        tableDekad.getColumnModel().getColumn(6).setPreferredWidth(5);
        tableDekad.getColumnModel().getColumn(7).setPreferredWidth(30);
        tableDekad.getColumnModel().getColumn(8).setPreferredWidth(70);
        tableDekad.getColumnModel().getColumn(9).setPreferredWidth(30);
        tableDekad.getColumnModel().getColumn(10).setPreferredWidth(70);
        tableDekad.getColumnModel().getColumn(11).setPreferredWidth(30);
        tableDekad.getColumnModel().getColumn(12).setPreferredWidth(70);
        tableDekad.getColumnModel().getColumn(13).setMinWidth(0);
        tableDekad.getColumnModel().getColumn(13).setMaxWidth(0);

        tableDekad.getColumnModel().getColumn(3).setCellRenderer(renderer);

        tableDekad.getColumnModel().addColumnModelListener(tableColumnModelDekadListener);
        UtilPlan.setFooterTable(tableDekad, filterRowDekad, UtilPlan.COL_PJDEKAD, UtilPlan.countSumm(tableDekad, UtilPlan.COL_PJDEKAD));

        tableDekad.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableDekad.getTableHeader(), 0, ""));
    }

    private void createPlanDetalTab1Table(final Vector rowAll) {
        tModelDetalTab1 = new DefaultTableModel(rowAll, colDetalTab1) {
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

        tModelDetalTab1.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableDetalTab1ModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowDetalTab1 == -1 || minSelectedRowDetalTab1 == -1) {
                    return;
                }
                tableDetalTab1ModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelDetalTab1.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowDetalTab1; i <= maxSelectedRowDetalTab1; i++) {
                    tModelDetalTab1.setValueAt(Boolean.valueOf(value), tableDetalTab1.convertRowIndexToModel(i), column);
                }

                minSelectedRowDetalTab1 = -1;
                maxSelectedRowDetalTab1 = -1;

                tableDetalTab1ModelListenerIsChanging = false;
            }
        });

        tableDetalTab1.setModel(tModelDetalTab1);
        tableDetalTab1.setAutoCreateColumnsFromModel(true);

        tableDetalTab1.getColumnModel().getColumn(0).setPreferredWidth(10);
        tableDetalTab1.getColumnModel().getColumn(1).setPreferredWidth(30);
        tableDetalTab1.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableDetalTab1.getColumnModel().getColumn(3).setPreferredWidth(70);
        tableDetalTab1.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableDetalTab1.getColumnModel().getColumn(5).setPreferredWidth(70);
        tableDetalTab1.getColumnModel().getColumn(6).setPreferredWidth(30);

        tableDetalTab1.getColumnModel().getColumn(8).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(8).setMaxWidth(0);
        tableDetalTab1.getColumnModel().getColumn(10).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(10).setMaxWidth(0);
        /*
        tableDetalTab1.getColumnModel().getColumn(0).setPreferredWidth(30);               
        tableDetalTab1.getColumnModel().getColumn(1).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(1).setMaxWidth(0);
        tableDetalTab1.getColumnModel().getColumn(2).setPreferredWidth(50); 
        tableDetalTab1.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableDetalTab1.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableDetalTab1.getColumnModel().getColumn(5).setPreferredWidth(150);                         
        tableDetalTab1.getColumnModel().getColumn(23).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(23).setMaxWidth(0);
        
        tableDetal.getColumnModel().addColumnModelListener(tableColumnModelDetalListener);
        
        
        */
        tableDetalTab1.getColumnModel().getColumn(3).setCellRenderer(renderer);

        //  tableDetalTab1.getColumnModel().addColumnModelListener(tableColumnModelDetalTab1Listener);
        //   UtilPlan.setFooterTable(tableDetalTab1, filterRowDetalTab1, UtilPlan.COL_PJDETAL1, UtilPlan.countSumm(tableDetalTab1, UtilPlan.COL_PJDETAL1));

        tableDetalTab1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableDetalTab1.getTableHeader(), 0, ""));
    }

    private void createPlanDetalTab2Table(final Vector rowAll) {
        tModelDetalTab2 = new DefaultTableModel(rowAll, colDetalTab2) {
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

        tModelDetalTab2.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableDetalTab2ModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowDetalTab2 == -1 || minSelectedRowDetalTab2 == -1) {
                    return;
                }
                tableDetalTab2ModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelDetalTab2.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowDetalTab2; i <= maxSelectedRowDetalTab2; i++) {
                    tModelDetalTab2.setValueAt(Boolean.valueOf(value), tableDetalTab2.convertRowIndexToModel(i), column);
                }

                minSelectedRowDetalTab2 = -1;
                maxSelectedRowDetalTab2 = -1;

                tableDetalTab2ModelListenerIsChanging = false;
            }
        });

        tableDetalTab2.setModel(tModelDetalTab2);
        tableDetalTab2.setAutoCreateColumnsFromModel(true);

        tableDetalTab2.getColumnModel().getColumn(1).setMinWidth(0);
        tableDetalTab2.getColumnModel().getColumn(1).setMaxWidth(0);
         /*
        tableDetalTab2.getColumnModel().getColumn(0).setPreferredWidth(30);               
        tableDetalTab2.getColumnModel().getColumn(1).setMinWidth(0);
        tableDetalTab2.getColumnModel().getColumn(1).setMaxWidth(0);
        tableDetalTab2.getColumnModel().getColumn(2).setPreferredWidth(50); 
        tableDetalTab2.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableDetalTab2.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableDetalTab2.getColumnModel().getColumn(5).setPreferredWidth(150);                         
        tableDetalTab2.getColumnModel().getColumn(23).setMinWidth(0);
        tableDetalTab2.getColumnModel().getColumn(23).setMaxWidth(0);
       
        tableDetal.getColumnModel().addColumnModelListener(tableColumnModelDetalListener);
        */

        tableDetalTab2.getColumnModel().addColumnModelListener(tableColumnModelDetalTab2Listener);
        UtilPlan.setFooterTable(tableDetalTab2, filterRowDetalTab2, UtilPlan.COL_PJDETAL2, UtilPlan.countSumm(tableDetalTab2, UtilPlan.COL_PJDETAL2));

        tableDetalTab2.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableDetalTab2.getTableHeader(), 0, ""));
    }

    private Vector updateDataTables(final String type) {
        dataTable = new Vector();

        pb = new ProgressBar(ProjectDetalForm.this, false, "Сбор данных ...");
        SwingWorker sw = new SwingWorker() {
            protected Object doInBackground() {
                try {
                    if (type.equals(UtilPlan.DATA_STOK)) {
                        pb.setMessage("Обновление ассортимента ...");
                        dataTable = ppdb.getDataItemProjectStokTemp();
                    } else if (type.equals(UtilPlan.DATA_VNORM)) {
                        pb.setMessage("Обновление трудоемкости ...");
                        dataTable = ppdb.getDataItemProjectVnormTemp();
                    } else if (type.equals(UtilPlan.DATA_DEKAD)) {
                        pb.setMessage("Обновление декад...");
                        dataTable = ppdb.getDataItemProjectDekadTemp();
                    } else if (type.equals(UtilPlan.DATA_DETAL)) {
                        pb.setMessage("Обновление проекта...");
                        dataTable = ppdb.getDataItemProjectDetalTemp();
                    }
                } catch (Exception e) {
                    dataTable = new Vector();
                    JOptionPane.showMessageDialog(null, "Сбой обновления! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }

            protected void done() {
                pb.dispose();
            }
        };
        sw.execute();
        pb.setVisible(true);

        return dataTable;
    }

    private Vector getDataStokCenaReport() {
        try {
            dataStokCenaReport = new Vector();

            pb = new ProgressBar(this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {

                    try {
                        if (typeProj.equals(UtilPlan.PROJECT_OPEN)) {
                            try {
                                ppdb = new PlanPDB();
                                dataStokCenaReport = ppdb.getDataItemProjectStok(idProj);
                            } catch (Exception e) {
                                dataStokCenaReport = new Vector();
                                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            } finally {
                                ppdb.disConn();
                            }

                        } else if (typeProj.equals(UtilPlan.PROJECT_ADD) ||
                                typeProj.equals(UtilPlan.PROJECT_COPY) ||
                                typeProj.equals(UtilPlan.PROJECT_EDIT)) {

                            dataStokCenaReport = ppdb.getDataItemProjectStokTemp();
                        }
                    } catch (Exception e) {
                        dataStokCenaReport = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    try {
                        pdb = new PlanDB();
                        dataStokCenaReport = pdb.getDataItemProjectStokCena(dataStokCenaReport);
                    } catch (Exception e) {
                        dataStokCenaReport = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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

        } catch (Exception e) {
            dataStokCenaReport = new Vector();

            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return dataStokCenaReport;
    }

    public MainController getController() {
        return controller;
    }

}
