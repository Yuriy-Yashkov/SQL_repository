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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
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
public class PlanDetalForm extends javax.swing.JDialog {
    boolean EDIT;
    boolean LOOK;
    int idPlan;
    String typePlan;
    User user = User.getInstance();
    PlanPDB ppdb;
    private JButton buttCloseNotSave;
    private JButton buttTech;
    private JButton buttPrint;
    private JButton buttEditTable;
    private JButton buttSave;
    private JDateChooser planStDate;
    private JComboBox dept;
    private JLabel kolvoIzdPlan;
    private JTextField workDayPlan;
    private JTextField noteText;
    private JTextPane infoText;
    private JLabel insDate;
    private JLabel title;
    private JTextField planName;
    private JLabel vvodDate;
    private JLabel vvodAvtor;
    private JLabel insAvtor;
    private JPanel osnova;
    private JPanel titlePanel;
    private JPanel buttPanel;
    private JPanel upPanel;
    private JPanel centerPanel;
    private JPanel editPanel;
    private JPanel filterRowPlan;
    private JPanel filterRowDetal;
    private JPanel filterRowСonv;
    private JPanel filterRowDekad;
    private JTabbedPane tableTabbedPane;
    private JTable tablePlan;
    private JTable tableDetal;
    private JTable tableСonv;
    private JTable tableDekad;
    private DefaultTableModel tModelPlan;
    private DefaultTableModel tModelDetal;
    private DefaultTableModel tModelConv;
    private DefaultTableModel tModelDekad;
    private TableRowSorter sorterPlan;
    private TableFilterHeader filterHeaderPlan;
    private TableFilterHeader filterHeaderDetal;
    private TableFilterHeader filterHeaderConv;
    private TableFilterHeader filterHeaderDekad;
    private Vector colPlan;
    private Vector colDetal;
    private Vector colConv;
    private Vector colDekad;
    private int minSelectedRowPlan = -1;
    private int maxSelectedRowPlan = -1;
    private boolean tablePlanModelListenerIsChanging = false;
    private int minSelectedRowDetal = -1;
    private int maxSelectedRowDetal = -1;
    private boolean tableDetalModelListenerIsChanging = false;
    private int minSelectedRowConv = -1;
    private int maxSelectedRowConv = -1;
    private boolean tableConvModelListenerIsChanging = false;
    private int minSelectedRowDekad = -1;
    private int maxSelectedRowDekad = -1;
    private boolean tableDekadModelListenerIsChanging = false;
    private JPanel tablePlanPanel;
    private JPanel tableDetalPanel;
    private JPanel tableСonvPanel;
    private JPanel tableDekadPanel;
    private TableColumnModelListener tableColumnModelPlanListener;
    private TableColumnModelListener tableColumnModelDetalListener;
    private TableColumnModelListener tableColumnModelConvListener;
    private TableColumnModelListener tableColumnModelDekadListener;
    private RowSorterListener tablePlanSorterListener;
    private RowSorterListener tableDetalSorterListener;
    private RowSorterListener tableConvSorterListener;
    private RowSorterListener tableDekadSorterListener;
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
    private JRadioButton jRadioButton4;
    private JRadioButton loadTech1;
    private JRadioButton loadTech2;
    private JRadioButton loadTechMonth;
    private JRadioButton loadTechDek1;
    private JRadioButton loadTechDek2;
    private JRadioButton loadTechDek3;
    private JPanel panelBox;
    private JPanel panelBoxTechLoad;
    private JPanel panelBoxTechLoadKol;
    private Vector data;
    private JMenu jMenu2;
    private JMenuItem jMenuItem3;
    private JMenuItem jMenuItem4;
    private MainController controller;

    public PlanDetalForm(MainController mainController, boolean modal,
                         Vector dataPlan, Vector dataItem, Vector dataDetalItem, Vector dataConvItem, Vector dataDekadItem, int idPlan) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Просмотр плана производства");

        try {
            this.idPlan = idPlan;

            init();

            title.setText("План производства № " + idPlan);

            planName.setEnabled(false);
            planStDate.setEnabled(false);
            dept.setEnabled(false);
            workDayPlan.setEnabled(false);
            noteText.setEnabled(false);
            jRadioButton1.setEnabled(false);
            jRadioButton2.setEnabled(false);
            jRadioButton3.setEnabled(false);
            jRadioButton4.setEnabled(false);
            jMenu1.setVisible(false);

            planName.setText(dataPlan.get(4).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilPlan.DEPT_MODEL, Integer.valueOf(dataPlan.get(0).toString())));
            planStDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataPlan.get(3).toString()));
            kolvoIzdPlan.setText(new BigDecimal(dataPlan.get(14).toString()).setScale(0, RoundingMode.HALF_UP).toString());
            workDayPlan.setText(new BigDecimal(Double.valueOf(dataPlan.get(14).toString()) / Double.valueOf(dataPlan.get(15).toString())).setScale(0, RoundingMode.HALF_UP).toString());
            noteText.setText(dataPlan.get(11).toString());
            vvodDate.setText(dataPlan.get(7).toString());
            vvodAvtor.setText(dataPlan.get(6).toString());
            insDate.setText(dataPlan.get(10).toString());
            insAvtor.setText(dataPlan.get(9).toString());

            switch (Integer.valueOf(dataPlan.get(13).toString())) {
                case 0:
                    jRadioButton1.setSelected(true);
                    break;
                case 1:
                    jRadioButton2.setSelected(true);
                    break;
                case 2:
                    jRadioButton3.setSelected(true);
                    break;
                case -1:
                    jRadioButton4.setSelected(true);
                    break;
                default:
                    jRadioButton3.setSelected(true);
                    break;
            }

            createPlanTable(dataItem);
            createPlanDetalTable(dataDetalItem);
            createPlanConvTable(dataConvItem);
            createPlanDekadTable(dataDekadItem);

            filterHeaderPlan.getTable().getRowSorter().addRowSorterListener(tablePlanSorterListener);
            filterHeaderDetal.getTable().getRowSorter().addRowSorterListener(tableDetalSorterListener);
            filterHeaderConv.getTable().getRowSorter().addRowSorterListener(tableConvSorterListener);
            filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Данные плана производства не загружены! " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public PlanDetalForm(MainController mainController, boolean modal, Vector dataPlan, final int idPlan, String typePlan) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Редактирование плана производства");

        try {
            this.idPlan = idPlan;
            this.typePlan = typePlan;

            init();

            this.EDIT = true;

            title.setText(typePlan.equals(UtilPlan.PLAN_COPY) ?
                    "Редактирование плана производства" :
                    "Редактирование плана производства № " + idPlan);

            buttPanel.add(buttSave);
            buttPanel.add(buttEditTable);

            workDayPlan.setEnabled(false);
            jRadioButton1.setEnabled(false);
            jRadioButton2.setEnabled(false);

            planName.setText(dataPlan.get(4).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilPlan.DEPT_MODEL, Integer.valueOf(dataPlan.get(0).toString())));
            planStDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataPlan.get(3).toString()));
            kolvoIzdPlan.setText(new BigDecimal(dataPlan.get(14).toString()).setScale(0, RoundingMode.HALF_UP).toString());
            workDayPlan.setText(new BigDecimal(Double.valueOf(dataPlan.get(14).toString()) / Double.valueOf(dataPlan.get(15).toString())).setScale(0, RoundingMode.HALF_UP).toString());
            noteText.setText(dataPlan.get(11).toString());
            vvodDate.setText(dataPlan.get(7).toString());
            vvodAvtor.setText(dataPlan.get(6).toString());
            insDate.setText(dataPlan.get(10).toString());
            insAvtor.setText(dataPlan.get(9).toString());

            if (typePlan.equals(UtilPlan.PLAN_EDIT)) {
                switch (Integer.valueOf(dataPlan.get(13).toString())) {
                    case 0:
                        jRadioButton1.setSelected(true);
                        break;
                    case 1:
                        jRadioButton2.setSelected(true);
                        break;
                    case 2:
                        jRadioButton3.setSelected(true);
                        break;
                    case -1:
                        jRadioButton4.setSelected(true);
                        break;
                    default:
                        jRadioButton3.setSelected(true);
                        break;
                }

                if (jRadioButton1.isSelected() || jRadioButton2.isSelected()) {
                    jRadioButton3.setEnabled(false);
                    jRadioButton4.setEnabled(false);

                    buttSave.setVisible(false);
                }

            } else if (typePlan.equals(UtilPlan.PLAN_COPY)) {
                jRadioButton3.setSelected(true);
            }

            try {
                pb = new ProgressBar(PlanDetalForm.this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            ppdb = new PlanPDB();
                            LOOK = (ppdb.createTempPlanItemTables(idPlan) &&
                                    ppdb.createTempPlanFasNewTables(idPlan)) ? true : false;

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Данные плана производства не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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
                    createPlanTable(updateDataTables(UtilPlan.DATA_KR));
                    createPlanDetalTable(updateDataTables(UtilPlan.DATA_DETAL));
                    createPlanConvTable(updateDataTables(UtilPlan.DATA_CONV));
                    createPlanDekadTable(updateDataTables(UtilPlan.DATA_DEKAD));

                    filterHeaderPlan.getTable().getRowSorter().addRowSorterListener(tablePlanSorterListener);
                    filterHeaderDetal.getTable().getRowSorter().addRowSorterListener(tableDetalSorterListener);
                    filterHeaderConv.getTable().getRowSorter().addRowSorterListener(tableConvSorterListener);
                    filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
                } else {
                    throw new Exception("Ошибка при создании временных таблиц! ");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка редактирования плана производства! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            addWindowListener(new WindowListener() {
                public void windowClosed(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
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

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Данные не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(850, 550));
        setPreferredSize(new Dimension(1000, 720));

        initMenu();

        EDIT = false;
        LOOK = false;

        osnova = new JPanel();
        titlePanel = new JPanel();
        upPanel = new JPanel();
        editPanel = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();
        tablePlanPanel = new JPanel();
        tableDetalPanel = new JPanel();
        tableСonvPanel = new JPanel();
        tableDekadPanel = new JPanel();
        panelBox = new JPanel();
        panelBoxTechLoad = new JPanel();
        panelBoxTechLoadKol = new JPanel();

        title = new JLabel();
        planStDate = new JDateChooser();
        vvodDate = new JLabel();
        insDate = new JLabel();
        planName = new JTextField();
        vvodAvtor = new JLabel();
        insAvtor = new JLabel();
        noteText = new JTextField();
        infoText = new JTextPane();
        kolvoIzdPlan = new JLabel();
        workDayPlan = new JTextField();
        dept = new JComboBox(UtilPlan.DEPT_MODEL);
        tableTabbedPane = new javax.swing.JTabbedPane();
        tablePlan = new JTable();
        tableDetal = new JTable();
        tableСonv = new JTable();
        tableDekad = new JTable();
        colPlan = new Vector();
        colDetal = new Vector();
        colConv = new Vector();
        colDekad = new Vector();
        dataTable = new Vector();
        filterRowPlan = new JPanel();
        filterRowDetal = new JPanel();
        filterRowСonv = new JPanel();
        filterRowDekad = new JPanel();
        filterHeaderPlan = new TableFilterHeader(tablePlan, AutoChoices.ENABLED);
        filterHeaderDetal = new TableFilterHeader(tableDetal, AutoChoices.ENABLED);
        filterHeaderConv = new TableFilterHeader(tableСonv, AutoChoices.ENABLED);
        filterHeaderDekad = new TableFilterHeader(tableDekad, AutoChoices.ENABLED);
        tModelPlan = new DefaultTableModel();
        tModelDetal = new DefaultTableModel();
        tModelConv = new DefaultTableModel();
        tModelDekad = new DefaultTableModel();
        buttEditTable = new JButton("Редактировать");
        buttSave = new JButton("Сохранить");
        buttTech = new JButton("Оборудование");
        buttPrint = new JButton("Печать");
        buttCloseNotSave = new JButton("Закрыть");
        buttonGroupStatus = new ButtonGroup();
        buttonGroupTech = new ButtonGroup();
        buttonGroupTechKol = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();
        loadTech1 = new JRadioButton();
        loadTech2 = new JRadioButton();
        loadTechMonth = new JRadioButton();
        loadTechDek1 = new JRadioButton();
        loadTechDek2 = new JRadioButton();
        loadTechDek3 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.setLayout(new ParagraphLayout());
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        editPanel.setLayout(new ParagraphLayout());
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tablePlanPanel.setLayout(new BorderLayout(1, 1));
        tableDetalPanel.setLayout(new BorderLayout(1, 1));
        tableСonvPanel.setLayout(new BorderLayout(1, 1));
        tableDekadPanel.setLayout(new BorderLayout(1, 1));
        filterRowPlan.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterRowDetal.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterRowСonv.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterRowDekad.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelBoxTechLoad.setLayout(new GridLayout(2, 1, 5, 5));
        panelBoxTechLoad.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Модели", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12))); // NOI18N
        panelBoxTechLoadKol.setLayout(new GridLayout(2, 2, 5, 5));
        panelBoxTechLoadKol.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Кол-во", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12))); // NOI18N
        GridLayout gridLayout = new GridLayout();
        gridLayout.setHgap(5);
        panelBox.setLayout(gridLayout);

        planName.setPreferredSize(new Dimension(400, 20));
        planStDate.setPreferredSize(new Dimension(100, 20));
        dept.setPreferredSize(new Dimension(300, 20));
        vvodDate.setPreferredSize(new Dimension(150, 20));
        vvodAvtor.setPreferredSize(new Dimension(350, 20));
        insDate.setPreferredSize(new Dimension(150, 20));
        insAvtor.setPreferredSize(new Dimension(350, 20));
        noteText.setPreferredSize(new Dimension(800, 20));
        infoText.setPreferredSize(new Dimension(330, 80));
        kolvoIzdPlan.setPreferredSize(new Dimension(150, 20));
        workDayPlan.setPreferredSize(new Dimension(50, 20));
        buttCloseNotSave.setPreferredSize(new Dimension(50, 20));

        workDayPlan.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodAvtor.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insAvtor.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        kolvoIzdPlan.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        kolvoIzdPlan.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        planStDate.setDate((Calendar.getInstance()).getTime());
        insDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));

        buttonGroupStatus.add(jRadioButton1);
        buttonGroupStatus.add(jRadioButton2);
        buttonGroupStatus.add(jRadioButton3);
        buttonGroupStatus.add(jRadioButton4);

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
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTech1.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTech2.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTechMonth.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTechDek1.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTechDek2.setFont(new java.awt.Font("Dialog", 0, 13));
        loadTechDek3.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTech1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTech2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTechMonth.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTechDek1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTechDek2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadTechDek3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setText("Главный;");
        jRadioButton2.setText("Проект;");
        jRadioButton3.setText("Копия;");
        jRadioButton4.setText("Удалён;");
        loadTech1.setText("по всем моделям");
        loadTech2.setText("только по выделенным моделям");
        loadTechMonth.setText("за месяц;");
        loadTechDek1.setText("1-я декада;");
        loadTechDek2.setText("2-я декада;");
        loadTechDek3.setText("3-я декада;");

        jRadioButton1.setActionCommand("0");
        jRadioButton2.setActionCommand("1");
        jRadioButton3.setActionCommand("2");
        jRadioButton4.setActionCommand("-1");
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

        tablePlan.setAutoCreateColumnsFromModel(true);
        tableDetal.setAutoCreateColumnsFromModel(true);
        tableСonv.setAutoCreateColumnsFromModel(true);
        tableDekad.setAutoCreateColumnsFromModel(true);

        tablePlan.getTableHeader().setReorderingAllowed(false);
        tableDetal.getTableHeader().setReorderingAllowed(false);
        tableСonv.getTableHeader().setReorderingAllowed(false);
        tableDekad.getTableHeader().setReorderingAllowed(false);

        //tableDetal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        colPlan.add("");
        colPlan.add("idSpec");
        colPlan.add("fasSpec");
        colPlan.add("Спецификация");
        colPlan.add("Модель");
        colPlan.add("кол. М.");
        colPlan.add("Трудоемкость на ед.");
        colPlan.add("Трудоемкость на выпуск");

        colDetal.add("");
        colDetal.add("idPlanItem");
        colDetal.add("Шифр.арт.");
        colDetal.add("Модель");
        colDetal.add("Рост");
        colDetal.add("Размер");
        colDetal.add("кол. М.");
        colDetal.add("кол. Д.");
        colDetal.add("Спец.№");
        colDetal.add("Спецификация");
        colDetal.add("Трудоемкость на ед.");
        colDetal.add("Трудоемкость на выпуск");
        colDetal.add("idEmpl");
        colDetal.add("ФИО");
        colDetal.add("Дата");
        colDetal.add("Конвейер");
        colDetal.add("1 декада(%)");
        colDetal.add("2 декада(%)");
        colDetal.add("3 декада(%)");
        colDetal.add("Примечание");
        colDetal.add("Новинка");

        colConv.add("");
        colConv.add("Конвейер");
        colConv.add("idSpec");
        colConv.add("fasSpec");
        colConv.add("Спецификация");
        colConv.add("Модель");
        colConv.add("кол. М.");
        colConv.add("Трудоемкость на ед.");
        colConv.add("Трудоемкость на выпуск");
        colConv.add("Новинка");

        colDekad.add("");
        colDekad.add("idSpec");
        colDekad.add("Спецификация");
        colDekad.add("Название");
        colDekad.add("Модель");
        colDekad.add("кол. М");
        colDekad.add("(%) 1декада");
        colDekad.add("(шт.) 1декада");
        colDekad.add("(%) 2декада");
        colDekad.add("(шт.) 2декада");
        colDekad.add("(%) 3декада");
        colDekad.add("(шт.) 3декада");
        colDekad.add("Конвейер");
        colDekad.add("Новинка");

        tableColumnModelPlanListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tablePlan.getColumnModel();
                if (filterRowPlan.getComponents().length > 0) {
                    for (int i = 0; i < tcm.getColumnCount(); i++) {
                        JTextField textField = (JTextField) filterRowPlan.getComponent(i);
                        Dimension d = textField.getPreferredSize();
                        d.width = tcm.getColumn(i).getWidth();
                        textField.setPreferredSize(d);
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            filterRowPlan.revalidate();
                        }
                    });
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterRowPlan.getComponent(e.getFromIndex());
                filterRowPlan.remove(e.getFromIndex());
                filterRowPlan.add(moved, e.getToIndex());
                filterRowPlan.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };

        tableColumnModelDetalListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableDetal.getColumnModel();
                if (filterRowDetal.getComponents().length > 0) {
                    for (int i = 0; i < tcm.getColumnCount(); i++) {
                        JTextField textField = (JTextField) filterRowDetal.getComponent(i);
                        Dimension d = textField.getPreferredSize();
                        d.width = tcm.getColumn(i).getWidth();
                        textField.setPreferredSize(d);
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            filterRowDetal.revalidate();
                        }
                    });
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterRowDetal.getComponent(e.getFromIndex());
                filterRowDetal.remove(e.getFromIndex());
                filterRowDetal.add(moved, e.getToIndex());
                filterRowDetal.validate();
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        };

        tableColumnModelConvListener = new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
            }

            public void columnMarginChanged(ChangeEvent e) {
                TableColumnModel tcm = tableСonv.getColumnModel();
                if (filterRowСonv.getComponents().length > 0) {
                    for (int i = 0; i < tcm.getColumnCount(); i++) {
                        JTextField textField = (JTextField) filterRowСonv.getComponent(i);
                        Dimension d = textField.getPreferredSize();
                        d.width = tcm.getColumn(i).getWidth();
                        textField.setPreferredSize(d);
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            filterRowСonv.revalidate();
                        }
                    });
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
                Component moved = filterRowСonv.getComponent(e.getFromIndex());
                filterRowСonv.remove(e.getFromIndex());
                filterRowСonv.add(moved, e.getToIndex());
                filterRowСonv.validate();
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

        tablePlanSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tablePlan, filterRowPlan, UtilPlan.COL_PLAN);
            }
        };

        tableDetalSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableDetal, filterRowDetal, UtilPlan.COL_DETAL);
            }
        };

        tableConvSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableСonv, filterRowСonv, UtilPlan.COL_CONV);
            }
        };

        tableDekadSorterListener = new RowSorterListener() {
            public void sorterChanged(RowSorterEvent e) {
                UtilPlan.addRowSorterListener(tableDekad, filterRowDekad, UtilPlan.COL_DEKAD);
            }
        };

        tablePlan.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowPlan = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowPlan = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableDetal.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowDetal = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowDetal = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableСonv.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowConv = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowConv = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
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

        buttCloseNotSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseNotSaveActionPerformed(evt);
            }
        });

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        buttTech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTechActionPerformed(evt);
            }
        });

        buttEditTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEditTableActionPerformed(evt);
            }
        });

        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
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

        titlePanel.add(new JLabel("Название: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(planName);
        titlePanel.add(new JLabel("     Цех: "));
        titlePanel.add(dept);
        titlePanel.add(new JLabel("Дата: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(planStDate);
        //titlePanel.add(new JLabel("     Кол-во раб. дней: "));   
        //titlePanel.add(workDayPlan);
        titlePanel.add(new JLabel("     Статус:"));
        titlePanel.add(jRadioButton1);
        titlePanel.add(jRadioButton2);
        titlePanel.add(jRadioButton3);
        titlePanel.add(jRadioButton4);
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

        upPanel.add(title, BorderLayout.NORTH);
        upPanel.add(titlePanel, BorderLayout.CENTER);

        tablePlanPanel.add(new JScrollPane(tablePlan), BorderLayout.CENTER);
        tableDetalPanel.add(new JScrollPane(tableDetal), BorderLayout.CENTER);
        tableСonvPanel.add(new JScrollPane(tableСonv), BorderLayout.CENTER);
        tableDekadPanel.add(new JScrollPane(tableDekad), BorderLayout.CENTER);
        tablePlanPanel.add(filterRowPlan, BorderLayout.SOUTH);
        tableDetalPanel.add(filterRowDetal, BorderLayout.SOUTH);
        tableСonvPanel.add(filterRowСonv, BorderLayout.SOUTH);
        tableDekadPanel.add(filterRowDekad, BorderLayout.SOUTH);

        tableTabbedPane.addTab("краткий", tablePlanPanel);
        tableTabbedPane.addTab("подробный", tableDetalPanel);
        tableTabbedPane.addTab("конвейера", tableСonvPanel);
        tableTabbedPane.addTab("декады", tableDekadPanel);

        centerPanel.add(tableTabbedPane, BorderLayout.CENTER);

        buttPanel.add(buttCloseNotSave);
        buttPanel.add(buttPrint);
        buttPanel.add(buttTech);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Правка");

        jMenuItem2.setText("Очистить");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Сервис");

        jMenuItem4.setText("Проверить спецификации в плане");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenuItem1.setText("Загрузка цеха");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);
        
        /*
        jMenuItem3.setText("Открыть спецификации моделей");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);*/


        setJMenuBar(jMenuBar1);
    }

    private void jMenuItem1ActionPerformed(ActionEvent evt) {
        try {
            double[] temp = null;
            if (tableTabbedPane.getModel().getSelectedIndex() == 0) {
                temp = UtilPlan.countSumm(tablePlan, UtilPlan.COL_PLAN);
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 1) {
                temp = UtilPlan.countSumm(tableDetal, UtilPlan.COL_DETAL);
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 2) {
                temp = UtilPlan.countSumm(tableСonv, UtilPlan.COL_CONV);
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 3) {
                temp = UtilPlan.countSumm(tableDekad, UtilPlan.COL_DEKAD);
            }

            if (temp != null) {
                new CapacityForm(this, true, Integer.valueOf(workDayPlan.getText()), temp);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem2ActionPerformed(ActionEvent evt) {
        try {
            JPanel panelBox = new JPanel();
            JCheckBox box1 = new JCheckBox("Шифр артикула;");
            JCheckBox box2 = new JCheckBox("Рост;");
            JCheckBox box3 = new JCheckBox("Размер;");
            JCheckBox box4 = new JCheckBox("Конвейер;");

            panelBox.setLayout(new GridLayout(0, 1, 5, 5));

            panelBox.add(box1);
            panelBox.add(box2);
            panelBox.add(box3);
            panelBox.add(box4);

            if (JOptionPane.showOptionDialog(null, panelBox, "Очистить ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Очистить", "Отмена"}, "Очистить") == JOptionPane.YES_OPTION) {
                if (box1.isSelected() || box2.isSelected() || box3.isSelected() || box4.isSelected()) {
                    if (ppdb.removeSarRstRzmTempPlanTable(box1.isSelected(), box2.isSelected(),
                            box3.isSelected(), box4.isSelected(), Integer.valueOf(user.getIdEmployee()))) {

                        filterHeaderPlan.getTable().getRowSorter().removeRowSorterListener(tablePlanSorterListener);
                        filterHeaderDetal.getTable().getRowSorter().removeRowSorterListener(tableDetalSorterListener);
                        filterHeaderConv.getTable().getRowSorter().removeRowSorterListener(tableConvSorterListener);
                        filterHeaderDekad.getTable().getRowSorter().removeRowSorterListener(tableDekadSorterListener);

                        createPlanTable(updateDataTables(UtilPlan.DATA_KR));
                        createPlanDetalTable(updateDataTables(UtilPlan.DATA_DETAL));
                        createPlanConvTable(updateDataTables(UtilPlan.DATA_CONV));
                        createPlanDekadTable(updateDataTables(UtilPlan.DATA_DEKAD));

                        filterHeaderPlan.getTable().getRowSorter().addRowSorterListener(tablePlanSorterListener);
                        filterHeaderDetal.getTable().getRowSorter().addRowSorterListener(tableDetalSorterListener);
                        filterHeaderConv.getTable().getRowSorter().addRowSorterListener(tableConvSorterListener);
                        filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
                    } else {
                        JOptionPane.showMessageDialog(null, "Сбой обновления!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem3ActionPerformed(ActionEvent evt) {
        try {


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem4ActionPerformed(ActionEvent evt) {
        try {
            infoText.setText("");

            Vector checkSpec = new Vector();
            Vector checkModel = new Vector();

            for (int i = 0; i < tModelPlan.getRowCount(); i++) {
                if (Integer.valueOf(tModelPlan.getValueAt(i, 2).toString()) < 0) {
                    checkSpec.add(tModelPlan.getValueAt(i, 4).toString());
                } else {
                    if (!tModelPlan.getValueAt(i, 2).toString().equals(tModelPlan.getValueAt(i, 4).toString())) {
                        checkModel.add(tModelPlan.getValueAt(i, 4).toString());
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

            JOptionPane.showOptionDialog(null, new JScrollPane(infoText), "Проверка плана ", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Закрыть"}, "Закрыть");

        } catch (Exception e) {
            infoText.setText("");
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseNotSaveActionPerformed(ActionEvent evt) {
        dispose();
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
            if (tableTabbedPane.getModel().getSelectedIndex() == 0 || tableTabbedPane.getModel().getSelectedIndex() == 2) {
                PlanOO oo = new PlanOO(title.getText(),
                        tableTabbedPane.getModel().getSelectedIndex() == 0 ? tModelPlan : tModelConv,
                        tableTabbedPane.getModel().getSelectedIndex() == 0 ? tablePlan.getColumnModel() : tableСonv.getColumnModel());
                oo.createReport("DefaultTableBookFormatCheck.ots");

            } else if (tableTabbedPane.getModel().getSelectedIndex() == 1 || tableTabbedPane.getModel().getSelectedIndex() == 3) {
                PlanOO oo = new PlanOO(title.getText(),
                        tableTabbedPane.getModel().getSelectedIndex() == 1 ? tModelDetal : tModelDekad,
                        tableTabbedPane.getModel().getSelectedIndex() == 1 ? tableDetal.getColumnModel() : tableDekad.getColumnModel());
                oo.createReport("DefaultTableAlbumFormatCheck.ots");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTechActionPerformed(ActionEvent evt) {
        data = new Vector();
        try {
            panelBox.removeAll();
            panelBox.revalidate();

            if (tableTabbedPane.getModel().getSelectedIndex() == 0 ||
                    tableTabbedPane.getModel().getSelectedIndex() == 2) {
                panelBox.add(panelBoxTechLoad);
                loadTechMonth.setSelected(true);
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 1 ||
                    tableTabbedPane.getModel().getSelectedIndex() == 3) {
                panelBox.add(panelBoxTechLoad);
                panelBox.add(panelBoxTechLoadKol);
            }

            if (JOptionPane.showOptionDialog(null,
                    panelBox,
                    "Загрузка оборудования ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Сформировать", "Отмена"},
                    "Сформировать") == JOptionPane.YES_OPTION) {

                if (loadTech1.isSelected()) {
                    data = tModelDetal.getDataVector();
                }

                if (loadTech2.isSelected()) {
                    pb = new ProgressBar(this, false, "Сбор данных ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                if (tableTabbedPane.getModel().getSelectedIndex() == 0) {
                                    Vector tmpData = new Vector();

                                    for (Object row : tModelPlan.getDataVector()) {
                                        if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                                            Vector tmp = new Vector();
                                            tmp.add(((Vector) row).get(1));
                                            tmp.add(((Vector) row).get(4));
                                            tmpData.add(tmp);
                                        }
                                    }

                                    for (Object row : tModelDetal.getDataVector()) {
                                        Vector tmp = new Vector();
                                        tmp.add(((Vector) row).get(8));
                                        tmp.add(((Vector) row).get(3));
                                        if (tmpData.contains(tmp)) {
                                            data.add((Vector) row);
                                        }
                                    }

                                    loadTechMonth.setSelected(true);

                                } else if (tableTabbedPane.getModel().getSelectedIndex() == 1) {
                                    for (Object row : tModelDetal.getDataVector()) {
                                        if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                                            data.add((Vector) row);
                                        }
                                    }
                                } else if (tableTabbedPane.getModel().getSelectedIndex() == 2) {
                                    Vector tmpData = new Vector();

                                    for (Object row : tModelConv.getDataVector()) {
                                        if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                                            Vector tmp = new Vector();
                                            tmp.add(((Vector) row).get(2));
                                            tmp.add(((Vector) row).get(5));
                                            tmp.add(((Vector) row).get(1));
                                            tmpData.add(tmp);
                                        }
                                    }

                                    for (Object row : tModelDetal.getDataVector()) {
                                        Vector tmp = new Vector();
                                        tmp.add(((Vector) row).get(8));
                                        tmp.add(((Vector) row).get(3));
                                        tmp.add(((Vector) row).get(15));
                                        if (tmpData.contains(tmp)) {
                                            data.add((Vector) row);
                                        }
                                    }

                                    loadTechMonth.setSelected(true);

                                } else if (tableTabbedPane.getModel().getSelectedIndex() == 3) {
                                    Vector tmpData = new Vector();

                                    for (Object row : tModelDekad.getDataVector()) {
                                        if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                                            Vector tmp = new Vector();
                                            tmp.add(((Vector) row).get(1));
                                            tmp.add(((Vector) row).get(4));
                                            tmp.add(((Vector) row).get(12));
                                            tmpData.add(tmp);
                                        }
                                    }

                                    for (Object row : tModelDetal.getDataVector()) {
                                        Vector tmp = new Vector();
                                        tmp.add(((Vector) row).get(8));
                                        tmp.add(((Vector) row).get(3));
                                        tmp.add(((Vector) row).get(15));
                                        if (tmpData.contains(tmp)) {
                                            data.add((Vector) row);
                                        }
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
                new TableTechForm(controller, true,
                        data,
                        idPlan,
                        Integer.valueOf(workDayPlan.getText()),
                        buttonGroupTech.getSelection().getActionCommand(),
                        buttonGroupTechKol.getSelection().getActionCommand(),
                        buttonGroupTechKol.getSelection().getMnemonic());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditTableActionPerformed(ActionEvent evt) {
        try {
            Vector vecEdit = new Vector();
            Vector dataEdit = new Vector();

            if (tableTabbedPane.getModel().getSelectedIndex() == 0) {
                for (int i = 0; i < tablePlan.getRowCount(); i++) {
                    if (Boolean.valueOf(tablePlan.getValueAt(i, 0).toString())) {
                        Vector tmp = new Vector();
                        tmp.add(Integer.valueOf(tablePlan.getValueAt(i, 1).toString()));
                        tmp.add(Integer.valueOf(tablePlan.getValueAt(i, 4).toString()));
                        vecEdit.add(tmp);
                    }
                }

                dataEdit = ppdb.getDataEditPlanTemp(vecEdit);

            } else if (tableTabbedPane.getModel().getSelectedIndex() == 1) {
                for (int i = 0; i < tableDetal.getRowCount(); i++) {
                    if (Boolean.valueOf(tableDetal.getValueAt(i, 0).toString())) {
                        vecEdit.add(Integer.valueOf(tableDetal.getValueAt(i, 1).toString()));
                    }
                }

                dataEdit = ppdb.getDataEditPlanDetalTemp(vecEdit);
            } else if (tableTabbedPane.getModel().getSelectedIndex() == 2) {
                for (int i = 0; i < tableСonv.getRowCount(); i++) {
                    if (Boolean.valueOf(tableСonv.getValueAt(i, 0).toString())) {
                        Vector tmp = new Vector();
                        tmp.add(Integer.valueOf(tableСonv.getValueAt(i, 2).toString()));
                        tmp.add(Integer.valueOf(tableСonv.getValueAt(i, 5).toString()));
                        tmp.add(Integer.valueOf(tableСonv.getValueAt(i, 1).toString()));
                        vecEdit.add(tmp);
                    }
                }

                dataEdit = ppdb.getDataEditPlanConvTemp(vecEdit);

            } else if (tableTabbedPane.getModel().getSelectedIndex() == 3) {
                for (int i = 0; i < tableDekad.getRowCount(); i++) {
                    if (Boolean.valueOf(tableDekad.getValueAt(i, 0).toString())) {
                        Vector tmp = new Vector();
                        tmp.add(Integer.valueOf(tableDekad.getValueAt(i, 1).toString()));
                        tmp.add(Integer.valueOf(tableDekad.getValueAt(i, 4).toString()));
                        tmp.add(Integer.valueOf(tableDekad.getValueAt(i, 12).toString()));
                        vecEdit.add(tmp);
                    }
                }

                dataEdit = ppdb.getDataEditPlanDekadTemp(vecEdit);
            }

            new SmallTableForm(controller, this, true, dataEdit, "", Integer.valueOf(workDayPlan.getText()));

            if (UtilPlan.EDIT_BUTT_ACTION) {

                filterHeaderPlan.getTable().getRowSorter().removeRowSorterListener(tablePlanSorterListener);
                filterHeaderDetal.getTable().getRowSorter().removeRowSorterListener(tableDetalSorterListener);
                filterHeaderConv.getTable().getRowSorter().removeRowSorterListener(tableConvSorterListener);
                filterHeaderDekad.getTable().getRowSorter().removeRowSorterListener(tableDekadSorterListener);

                createPlanTable(updateDataTables(UtilPlan.DATA_KR));
                createPlanDetalTable(updateDataTables(UtilPlan.DATA_DETAL));
                createPlanConvTable(updateDataTables(UtilPlan.DATA_CONV));
                createPlanDekadTable(updateDataTables(UtilPlan.DATA_DEKAD));

                filterHeaderPlan.getTable().getRowSorter().addRowSorterListener(tablePlanSorterListener);
                filterHeaderDetal.getTable().getRowSorter().addRowSorterListener(tableDetalSorterListener);
                filterHeaderConv.getTable().getRowSorter().addRowSorterListener(tableConvSorterListener);
                filterHeaderDekad.getTable().getRowSorter().addRowSorterListener(tableDekadSorterListener);
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

                if (planName.getText().trim().equals("")) {
                    saveFlag = false;
                    str += "Вы не ввели название плана!\n";
                }

                if (ppdb.checkKolvoPlanTableTemp() <= 0) {
                    saveFlag = false;
                    str += "В плане нет записей!\n";
                }

                if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(planStDate.getDate()))) {
                    saveFlag = false;
                }

                if (!saveFlag) {
                    JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

                if (saveFlag) {
                    pb = new ProgressBar(PlanDetalForm.this, false, "Сохранение плана производства ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                if (typePlan.equals(UtilPlan.PLAN_EDIT)) {
                                    ppdb.updateTempPlanTable(idPlan,
                                            planName.getText().trim(),
                                            ((Item) dept.getSelectedItem()).getId(),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(planStDate.getDate())),
                                            Integer.valueOf(buttonGroupStatus.getSelection().getActionCommand()),
                                            noteText.getText().trim(),
                                            Integer.valueOf(user.getIdEmployee()));

                                    ppdb.updateNewModelsTempPlanTable(idPlan);

                                    JOptionPane.showMessageDialog(null, "План производства успешно сохранен! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                } else if (typePlan.equals(UtilPlan.PLAN_COPY)) {
                                    int id = ppdb.addTempPlanTable(planName.getText().trim(),
                                            ((Item) dept.getSelectedItem()).getId(),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(planStDate.getDate())),
                                            Integer.valueOf(buttonGroupStatus.getSelection().getActionCommand()),
                                            noteText.getText().trim(),
                                            Integer.valueOf(user.getIdEmployee()));

                                    if (id > 0)
                                        ppdb.updateNewModelsTempPlanTable(id);

                                    JOptionPane.showMessageDialog(null, "План производства успешно добавлен! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                }
                                dispose();
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

    private void createPlanTable(final Vector rowAll) {
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

        tModelPlan.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tablePlanModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowPlan == -1 || minSelectedRowPlan == -1) {
                    return;
                }
                tablePlanModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelPlan.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowPlan; i <= maxSelectedRowPlan; i++) {
                    tModelPlan.setValueAt(Boolean.valueOf(value), tablePlan.convertRowIndexToModel(i), column);
                }

                minSelectedRowPlan = -1;
                maxSelectedRowPlan = -1;

                tablePlanModelListenerIsChanging = false;
            }
        });

        tablePlan.setModel(tModelPlan);
        tablePlan.setAutoCreateColumnsFromModel(true);

        tablePlan.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablePlan.getColumnModel().getColumn(1).setMinWidth(0);
        tablePlan.getColumnModel().getColumn(1).setMaxWidth(0);
        tablePlan.getColumnModel().getColumn(2).setMinWidth(0);
        tablePlan.getColumnModel().getColumn(2).setMaxWidth(0);
        tablePlan.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablePlan.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablePlan.getColumnModel().getColumn(5).setPreferredWidth(150);
        tablePlan.getColumnModel().getColumn(6).setPreferredWidth(150);
        tablePlan.getColumnModel().getColumn(7).setPreferredWidth(150);

        sorterPlan = new TableRowSorter<TableModel>(tModelPlan);
        tablePlan.setRowSorter(sorterPlan);

        tablePlan.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tablePlan.getTableHeader(), 0, ""));

        tablePlan.getColumnModel().addColumnModelListener(tableColumnModelPlanListener);
        UtilPlan.setFooterTable(tablePlan, filterRowPlan, UtilPlan.COL_PLAN, UtilPlan.countSumm(tablePlan, UtilPlan.COL_PLAN));
    }

    private void createPlanDetalTable(final Vector rowAll) {
        tModelDetal = new DefaultTableModel(rowAll, colDetal) {
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

        tModelDetal.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableDetalModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowDetal == -1 || minSelectedRowDetal == -1) {
                    return;
                }
                tableDetalModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelDetal.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowDetal; i <= maxSelectedRowDetal; i++) {
                    tModelDetal.setValueAt(Boolean.valueOf(value), tableDetal.convertRowIndexToModel(i), column);
                }

                minSelectedRowDetal = -1;
                maxSelectedRowDetal = -1;

                tableDetalModelListenerIsChanging = false;
            }
        });

        tableDetal.setModel(tModelDetal);
        tableDetal.setAutoCreateColumnsFromModel(true);
        tableDetal.getColumnModel().getColumn(0).setPreferredWidth(30);
        tableDetal.getColumnModel().getColumn(1).setMinWidth(0);
        tableDetal.getColumnModel().getColumn(1).setMaxWidth(0);
        tableDetal.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableDetal.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableDetal.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableDetal.getColumnModel().getColumn(5).setPreferredWidth(50);
        tableDetal.getColumnModel().getColumn(6).setPreferredWidth(100);
        tableDetal.getColumnModel().getColumn(7).setMinWidth(0);
        tableDetal.getColumnModel().getColumn(7).setMaxWidth(0);
        tableDetal.getColumnModel().getColumn(8).setPreferredWidth(90);
        tableDetal.getColumnModel().getColumn(9).setPreferredWidth(300);
        tableDetal.getColumnModel().getColumn(10).setPreferredWidth(150);
        tableDetal.getColumnModel().getColumn(11).setPreferredWidth(150);
        tableDetal.getColumnModel().getColumn(12).setMinWidth(0);
        tableDetal.getColumnModel().getColumn(12).setMaxWidth(0);
        tableDetal.getColumnModel().getColumn(13).setPreferredWidth(150);
        tableDetal.getColumnModel().getColumn(14).setPreferredWidth(70);
        tableDetal.getColumnModel().getColumn(15).setPreferredWidth(80);

        tableDetal.getColumnModel().getColumn(3).setCellRenderer(renderer);

        //   tableDetal.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableDetal.getTableHeader(), 0, ""));

        tableDetal.getColumnModel().addColumnModelListener(tableColumnModelDetalListener);
        UtilPlan.setFooterTable(tableDetal, filterRowDetal, UtilPlan.COL_DETAL, UtilPlan.countSumm(tableDetal, UtilPlan.COL_DETAL));

    }

    private void createPlanConvTable(final Vector rowAll) {
        tModelConv = new DefaultTableModel(rowAll, colConv) {
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

        tModelConv.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableConvModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowConv == -1 || minSelectedRowConv == -1) {
                    return;
                }
                tableConvModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelConv.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowConv; i <= maxSelectedRowConv; i++) {
                    tModelConv.setValueAt(Boolean.valueOf(value), tableСonv.convertRowIndexToModel(i), column);
                }

                minSelectedRowConv = -1;
                maxSelectedRowConv = -1;

                tableConvModelListenerIsChanging = false;
            }
        });

        tableСonv.setModel(tModelConv);
        tableСonv.setAutoCreateColumnsFromModel(true);
        tableСonv.getColumnModel().getColumn(0).setPreferredWidth(30);
        tableСonv.getColumnModel().getColumn(1).setPreferredWidth(30);
        tableСonv.getColumnModel().getColumn(2).setMinWidth(0);
        tableСonv.getColumnModel().getColumn(2).setMaxWidth(0);
        tableСonv.getColumnModel().getColumn(3).setMinWidth(0);
        tableСonv.getColumnModel().getColumn(3).setMaxWidth(0);
        tableСonv.getColumnModel().getColumn(4).setPreferredWidth(150);
        tableСonv.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableСonv.getColumnModel().getColumn(6).setPreferredWidth(150);
        tableСonv.getColumnModel().getColumn(7).setPreferredWidth(150);
        tableСonv.getColumnModel().getColumn(8).setPreferredWidth(150);
        tableСonv.getColumnModel().getColumn(9).setPreferredWidth(50);

        tableСonv.getColumnModel().getColumn(5).setCellRenderer(renderer);

        //  tableConv.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableConv.getTableHeader(), 0, ""));           

        tableСonv.getColumnModel().addColumnModelListener(tableColumnModelConvListener);
        UtilPlan.setFooterTable(tableСonv, filterRowСonv, UtilPlan.COL_CONV, UtilPlan.countSumm(tableСonv, UtilPlan.COL_CONV));
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

        tableDekad.getColumnModel().getColumn(0).setPreferredWidth(2);
        tableDekad.getColumnModel().getColumn(1).setMinWidth(0);
        tableDekad.getColumnModel().getColumn(1).setMaxWidth(0);
        tableDekad.getColumnModel().getColumn(2).setPreferredWidth(70);
        tableDekad.getColumnModel().getColumn(3).setPreferredWidth(70);
        tableDekad.getColumnModel().getColumn(4).setPreferredWidth(40);
        tableDekad.getColumnModel().getColumn(5).setPreferredWidth(40);
        tableDekad.getColumnModel().getColumn(6).setPreferredWidth(20);
        tableDekad.getColumnModel().getColumn(7).setPreferredWidth(40);
        tableDekad.getColumnModel().getColumn(8).setPreferredWidth(20);
        tableDekad.getColumnModel().getColumn(9).setPreferredWidth(40);
        tableDekad.getColumnModel().getColumn(10).setPreferredWidth(20);
        tableDekad.getColumnModel().getColumn(11).setPreferredWidth(40);
        tableDekad.getColumnModel().getColumn(12).setPreferredWidth(2);
        tableDekad.getColumnModel().getColumn(13).setPreferredWidth(20);

        tableDekad.getColumnModel().getColumn(4).setCellRenderer(renderer);

        //tableDekad.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableConv.getTableHeader(), 0, ""));           

        tableDekad.getColumnModel().addColumnModelListener(tableColumnModelDekadListener);
        UtilPlan.setFooterTable(tableDekad, filterRowDekad, UtilPlan.COL_DEKAD, UtilPlan.countSumm(tableDekad, UtilPlan.COL_DEKAD));

    }

    private Vector updateDataTables(final String type) {
        dataTable = new Vector();

        pb = new ProgressBar(PlanDetalForm.this, false, "Сбор данных ...");
        SwingWorker sw = new SwingWorker() {
            protected Object doInBackground() {
                try {
                    if (type.equals(UtilPlan.DATA_KR)) {
                        pb.setMessage("Обновление плана ...");
                        dataTable = ppdb.getDataItemPlanTemp();
                    } else if (type.equals(UtilPlan.DATA_DETAL)) {
                        pb.setMessage("Обновление подробного плана ...");
                        dataTable = ppdb.getDataItemPlanDetalTemp();
                    } else if (type.equals(UtilPlan.DATA_CONV)) {
                        pb.setMessage("Обновление плана с конвейерами...");
                        dataTable = ppdb.getDataItemPlanConvTemp();
                    } else if (type.equals(UtilPlan.DATA_DEKAD)) {
                        pb.setMessage("Обновление плана по декадам...");
                        dataTable = ppdb.getDataItemPlanDekadTemp();
                    }
                } catch (Exception e) {
                    dataTable = new Vector();
                    JOptionPane.showMessageDialog(null, "Сбой обновления плана! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
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
}