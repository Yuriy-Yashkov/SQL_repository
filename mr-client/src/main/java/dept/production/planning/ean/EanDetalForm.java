package dept.production.planning.ean;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

/**
 * Класс формирования и отображения заявки на получение EAN-кодов
 * @author lidashka
 */
public class EanDetalForm extends JDialog {
    private static EanItem eanItemCurrent;
    boolean EDIT;
    boolean LOOK;
    private JButton buttClose;
    private JButton buttPrint;
    private JButton buttSave;
    private JButton buttEditPlus;
    private JButton buttEditMinus;
    private JButton buttEditIzm;
    private JButton buttEditSrt;
    private JButton buttSearchbyMarshList;
    private JButton buttGpcSeg;
    private JButton buttGpcSem;
    private JButton buttGpcKl;
    private JButton buttGpcBr;
    private JButton buttOKRB;
    private JButton buttTHB;
    private JButton buttGOST;
    private JDateChooser eanDate;
    private JTextPane eanNote;
    private JTextField eanNum;
    private JTextField eanName;
    private JLabel insDate;
    private JLabel title;
    private JLabel vvodDate;
    private JLabel vvodAvtor;
    private JLabel insAvtor;
    private JPanel osnovaPanel;
    private JPanel infoPanel;
    private JPanel titleInfoPanel;
    private JPanel buttPanel;
    private JPanel factoryPanel;
    private JPanel tableSmallDetalPanel;
    private JPanel tableBigDetalPanel;
    private JPanel tableClassDetalPanel;
    private JPanel buttEastSmallDetalPanel;
    private JPanel buttFootClassDetalPanel;
    private JTabbedPane tableTabbedPane;
    private ButtonGroup buttonGroupStatus;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private JRadioButton jRadioButton5;
    private JRadioButton jRadioButton6;
    private Object[] colSmall;
    private Object[] colBig;
    private Object[] colClass;
    private JTable tableSmall;
    private JTable tableBig;
    private JTable tableClass;
    private DefaultTableModel tModelSmall;
    private DefaultTableModel tModelBig;
    private DefaultTableModel tModelClass;
    private TableColumnModelListener tableSmallColumnModelListener;
    private DefaultTableCellRenderer renderer;
    private int minSelectedRowSmall;
    private int maxSelectedRowSmall;
    private boolean tableSmallListenerIsChanging;
    private int minSelectedRowBig;
    private int maxSelectedRowBig;
    private boolean tableBigListenerIsChanging;
    private int minSelectedRowClass;
    private int maxSelectedRowClass;
    private boolean tableClassListenerIsChanging;
    private User user;
    private EanPDB epdb;
    private ProgressBar pb;

    private int idEanList;
    private String typeEanList;
    private JButton buttEditCopy;
    private JPanel boxPanel;
    private JCheckBox box1;
    private JCheckBox box2;
    private JCheckBox box3;
    private JCheckBox box4;

    private MainController controller;

    public EanDetalForm() {
    }

    /**
     * Конструктор формы в режиме add
     * @param mainController
     * @param modal
     */
    public EanDetalForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        typeEanList = UtilEan.ADD;

        initMenu();
        initPropSetting();
        init();
        initData(typeEanList, null);

        this.setTitle("Новая заявка");
        this.setLocationRelativeTo(mainController.getMainForm());
        this.setVisible(true);
    }

    /**
     * Конструктор формы в режиме open or copy or edit
     * @param mainController
     * @param modal
     * @param data
     * @param type
     */
    public EanDetalForm(MainController mainController, boolean modal, EanList data, String type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        typeEanList = type;

        initMenu();
        initPropSetting();
        init();
        initData(typeEanList, data);

        this.setTitle("Заявка на получение ean-кодов");
        this.setLocationRelativeTo(mainController.getMainForm());
        this.setVisible(true);
    }

    /**
     * Метод задает текущий элемент EanItem
     * @param eanItem
     * @return
     */
    public static boolean setCurrentEanItem(EanItem eanItem) {
        eanItemCurrent = eanItem;

        return true;
    }

    private void initMenu() {

    }

    private void initPropSetting() {

    }

    private void init() {
        this.setMinimumSize(new Dimension(850, 450));
        this.setPreferredSize(new Dimension(1000, 600));

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
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

        title = new JLabel("Заявки на получение EAN-кодов");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        factoryPanel = new JPanel();
        factoryPanel.setLayout(new ParagraphLayout());

        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout(1, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        titleInfoPanel = new JPanel();
        titleInfoPanel.setLayout(new ParagraphLayout());

        tableSmallDetalPanel = new JPanel();
        tableSmallDetalPanel.setLayout(new BorderLayout(1, 1));

        buttEastSmallDetalPanel = new JPanel();
        buttEastSmallDetalPanel.setLayout(new GridLayout(8, 0, 5, 5));
        buttEastSmallDetalPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        tableBigDetalPanel = new JPanel();
        tableBigDetalPanel.setLayout(new BorderLayout(1, 1));

        tableClassDetalPanel = new JPanel();
        tableClassDetalPanel.setLayout(new BorderLayout(1, 1));

        buttFootClassDetalPanel = new JPanel();
        buttFootClassDetalPanel.setLayout(new GridLayout(2, 5, 5, 5));
        buttFootClassDetalPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        tableTabbedPane = new javax.swing.JTabbedPane();
        tableTabbedPane.addTab("Заявка", infoPanel);
        tableTabbedPane.addTab("Модели", tableSmallDetalPanel);
        tableTabbedPane.addTab("Размеры", tableBigDetalPanel);
        tableTabbedPane.addTab("Классификация", tableClassDetalPanel);

        box1 = new JCheckBox();
        box1.setFont(new java.awt.Font("Dialog", 0, 13));
        box1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box1.setText("1;");

        box2 = new JCheckBox();
        box2.setFont(new java.awt.Font("Dialog", 0, 13));
        box2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box2.setText("2;");
        box2.setSelected(true);

        box3 = new JCheckBox();
        box3.setFont(new java.awt.Font("Dialog", 0, 13));
        box3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box3.setText("н/с;");

        box4 = new JCheckBox();
        box4.setFont(new java.awt.Font("Dialog", 0, 13));
        box4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box4.setText("У;");

        boxPanel = new JPanel();
        boxPanel.setLayout(new ParagraphLayout());
        boxPanel.add(box1, ParagraphLayout.NEW_LINE_STRETCH_V);
        boxPanel.add(box2, ParagraphLayout.NEW_LINE_STRETCH_V);
        boxPanel.add(box3, ParagraphLayout.NEW_LINE_STRETCH_V);
        boxPanel.add(box4, ParagraphLayout.NEW_LINE_STRETCH_V);

        buttSave = new JButton("Сохранить");
        buttSave.addActionListener(evt -> buttSaveActionPerformed(evt));

        buttPrint = new JButton("Печать");
        buttPrint.addActionListener(evt -> buttPrintActionPerformed(evt));

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(evt -> buttCloseActionPerformed(evt));

        buttEditPlus = new JButton("+");
        buttEditPlus.addActionListener(evt -> buttPlusActionPerformed(evt));

        buttEditMinus = new JButton("-");
        buttEditMinus.addActionListener(evt -> buttEditMinusActionPerformed(evt));

        buttEditIzm = new JButton("изм");
        buttEditIzm.addActionListener(evt -> buttEditActionPerformed(evt));

        buttEditCopy = new JButton("коп");
        buttEditCopy.addActionListener(evt -> buttCopyActionPerformed(evt));

        buttEditSrt = new JButton("сорт");
        buttEditSrt.addActionListener(evt -> buttEditSrtActionPerformed(evt));

        buttSearchbyMarshList = new JButton("м/л");
        buttSearchbyMarshList.addActionListener(evt -> buttSearchbyMarshListActionPerformed(evt));

        buttGpcSeg = new JButton("сегмент");
        buttGpcSeg.addActionListener(evt -> buttGpcSegActionPerformed(evt));

        buttGpcSem = new JButton("семейство");
        buttGpcSem.addActionListener(evt -> buttGpcSemActionPerformed(evt));

        buttGpcKl = new JButton("класс");
        buttGpcKl.addActionListener(evt -> buttGpcKlActionPerformed(evt));

        buttGpcBr = new JButton("брик");
        buttGpcBr.addActionListener(evt -> buttGpcBrActionPerformed(evt));

        buttOKRB = new JButton("ОКРБ-007");
        buttOKRB.addActionListener(evt -> buttOkrbActionPerformed(evt));

        buttTHB = new JButton("ТНВЭДТ");
        buttTHB.addActionListener(evt -> buttThbActionPerformed(evt));

        buttGOST = new JButton("ГОСТ");
        buttGOST.addActionListener(evt -> buttGostActionPerformed(evt));

        eanName = new JTextField();
        eanName.setPreferredSize(new Dimension(650, 20));

        eanNum = new JTextField();
        eanNum.setPreferredSize(new Dimension(150, 20));

        eanDate = new JDateChooser();
        eanDate.setPreferredSize(new Dimension(150, 20));

        eanNote = new JTextPane();
        eanNote.setPreferredSize(new Dimension(700, 100));

        vvodDate = new JLabel();
        vvodDate.setPreferredSize(new Dimension(150, 20));
        vvodDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        vvodAvtor = new JLabel();
        vvodAvtor.setPreferredSize(new Dimension(400, 20));
        vvodAvtor.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        insDate = new JLabel();
        insDate.setPreferredSize(new Dimension(150, 20));
        insDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        insAvtor = new JLabel();
        insAvtor.setPreferredSize(new Dimension(400, 20));
        insAvtor.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jRadioButton1 = new JRadioButton();
        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton1.setText("Отправлен;");
        jRadioButton1.setActionCommand("0");

        jRadioButton2 = new JRadioButton();
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setText("Формируется;");
        jRadioButton2.setActionCommand("1");
        jRadioButton2.setSelected(true);

        jRadioButton4 = new JRadioButton();
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setText("Ответ;");
        jRadioButton4.setActionCommand("2");

        jRadioButton5 = new JRadioButton();
        jRadioButton5.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton5.setText("Импорт;");
        jRadioButton5.setActionCommand("3");

        jRadioButton6 = new JRadioButton();
        jRadioButton6.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton6.setText("Закрыт;");
        jRadioButton6.setActionCommand("4");

        jRadioButton3 = new JRadioButton();
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setText("Удалён;");
        jRadioButton3.setActionCommand("-1");

        buttonGroupStatus = new ButtonGroup();
        buttonGroupStatus.add(jRadioButton1);
        buttonGroupStatus.add(jRadioButton2);
        buttonGroupStatus.add(jRadioButton4);
        buttonGroupStatus.add(jRadioButton5);
        buttonGroupStatus.add(jRadioButton6);
        buttonGroupStatus.add(jRadioButton3);

        minSelectedRowSmall = -1;
        maxSelectedRowSmall = -1;
        tableSmallListenerIsChanging = false;

        minSelectedRowBig = -1;
        maxSelectedRowBig = -1;
        tableBigListenerIsChanging = false;

        minSelectedRowClass = -1;
        maxSelectedRowClass = -1;
        tableClassListenerIsChanging = false;

        colSmall = new Object[13];
        colSmall[0] = "";
        colSmall[1] = "flag";
        colSmall[2] = "idItem";
        colSmall[3] = "Вид";
        colSmall[4] = "Название";
        colSmall[5] = "Модель";
        colSmall[6] = "Артикул";
        colSmall[7] = "id Цвет";
        colSmall[8] = "Цвет";
        colSmall[9] = "Сорт";
        colSmall[10] = "x2";
        colSmall[11] = "Упаковка";
        colSmall[12] = "Примечание";

        UtilEan.COLUM_FAS = 5;
        UtilEan.COLUM_NAR = 6;
        UtilEan.COLUM_COLOR = 7;
        UtilEan.COLUM_SRT = 9;

        colBig = new Object[14];
        colBig[0] = "";
        colBig[1] = "flag";
        colBig[2] = "idItem";
        colBig[3] = "Вид";
        colBig[4] = "Название";
        colBig[5] = "Модель";
        colBig[6] = "Артикул";
        colBig[7] = "id Цвет";
        colBig[8] = "Цвет";
        colBig[9] = "Сорт";
        colBig[10] = "Рост";
        colBig[11] = "Размер";
        colBig[12] = "Этикетка";
        colBig[13] = "EAN13";

        colClass = new Object[14];
        colClass[0] = "";
        colClass[1] = "flag";
        colClass[2] = "idItem";
        colClass[3] = "Вид";
        colClass[4] = "Название";
        colClass[5] = "Модель";
        colClass[6] = "Артикул";
        colClass[7] = "GPC сегмент";
        colClass[8] = "GPC семейство";
        colClass[9] = "GPC класс";
        colClass[10] = "GPC брик";
        colClass[11] = "ОКРБ-007";
        colClass[12] = "ТНВЭДТ";
        colClass[13] = "ГОСТ";

        tableSmall = new JTable();
        tableSmall.setAutoCreateColumnsFromModel(true);
        tableSmall.getTableHeader().setReorderingAllowed(false);
        tableSmall.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            minSelectedRowSmall = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                    ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
            maxSelectedRowSmall = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                    ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
        });
        new TableFilterHeader(tableSmall, AutoChoices.ENABLED);

        tModelSmall = new DefaultTableModel();

        tableBig = new JTable();
        tableBig.setAutoCreateColumnsFromModel(true);
        tableBig.getTableHeader().setReorderingAllowed(false);
        tableBig.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            minSelectedRowBig = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                    ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
            maxSelectedRowBig = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                    ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
        });
        new TableFilterHeader(tableBig, AutoChoices.ENABLED);

        tModelBig = new DefaultTableModel();

        tableClass = new JTable();
        tableClass.setAutoCreateColumnsFromModel(true);
        tableClass.getTableHeader().setReorderingAllowed(false);
        tableClass.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            minSelectedRowClass = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                    ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
            maxSelectedRowClass = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                    ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
        });
        new TableFilterHeader(tableClass, AutoChoices.ENABLED);

        tModelClass = new DefaultTableModel();

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    if (table.getValueAt(row, 1).toString().equals("-1"))
                        cell.setBackground(Color.PINK);
                    else
                        cell.setBackground(table.getBackground());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }

            ;
        };

        factoryPanel.add(new JLabel("Заказчик:"), ParagraphLayout.NEW_PARAGRAPH);
        factoryPanel.add(new JLabel(UtilEan.FACTORY_NAME));
        factoryPanel.add(new JLabel("   номер ГС1:"));
        factoryPanel.add(new JLabel(UtilEan.FACTORY_GS1));
        factoryPanel.add(new JLabel("Подготовил(а):"), ParagraphLayout.NEW_PARAGRAPH);
        factoryPanel.add(new JLabel(UtilEan.FACTORY_EMPL));
        factoryPanel.add(new JLabel("   тел:"));
        factoryPanel.add(new JLabel(UtilEan.FACTORY_TEL));


        titleInfoPanel.add(new JLabel(" Статус:"), ParagraphLayout.NEW_PARAGRAPH);
        titleInfoPanel.add(jRadioButton1);
        titleInfoPanel.add(jRadioButton2);
        titleInfoPanel.add(jRadioButton4);
        titleInfoPanel.add(jRadioButton5);
        titleInfoPanel.add(jRadioButton6);
        titleInfoPanel.add(jRadioButton3);
        titleInfoPanel.add(new JLabel("Уч. номер: "), ParagraphLayout.NEW_PARAGRAPH);
        titleInfoPanel.add(eanNum);
        titleInfoPanel.add(new JLabel("Название: "), ParagraphLayout.NEW_PARAGRAPH);
        titleInfoPanel.add(eanName);
        titleInfoPanel.add(new JLabel("Дата: "), ParagraphLayout.NEW_PARAGRAPH);
        titleInfoPanel.add(eanDate);
        titleInfoPanel.add(new JLabel("Дата ввода: "), ParagraphLayout.NEW_PARAGRAPH);
        titleInfoPanel.add(vvodDate);
        titleInfoPanel.add(new JLabel(" Автор ввода:"));
        titleInfoPanel.add(vvodAvtor);
        titleInfoPanel.add(new JLabel("Дата корр.: "), ParagraphLayout.NEW_PARAGRAPH);
        titleInfoPanel.add(insDate);
        titleInfoPanel.add(new JLabel(" Автор корр. :"));
        titleInfoPanel.add(insAvtor);
        titleInfoPanel.add(new JLabel("Примечание: "), ParagraphLayout.NEW_PARAGRAPH);
        titleInfoPanel.add(eanNote);
        titleInfoPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        titleInfoPanel.add(factoryPanel);

        buttEastSmallDetalPanel.add(buttEditPlus);
        buttEastSmallDetalPanel.add(buttEditMinus);
        buttEastSmallDetalPanel.add(buttEditIzm);
        buttEastSmallDetalPanel.add(buttEditCopy);
        buttEastSmallDetalPanel.add(buttEditSrt);
        buttEastSmallDetalPanel.add(buttSearchbyMarshList);

        buttFootClassDetalPanel.add(new JLabel("        Код по GPC:"));
        buttFootClassDetalPanel.add(buttGpcSeg);
        buttFootClassDetalPanel.add(buttGpcSem);
        buttFootClassDetalPanel.add(buttGpcKl);
        buttFootClassDetalPanel.add(buttGpcBr);
        buttFootClassDetalPanel.add(new JLabel("        Код по:"));
        buttFootClassDetalPanel.add(buttOKRB);
        buttFootClassDetalPanel.add(buttTHB);
        buttFootClassDetalPanel.add(buttGOST);

        tableSmallDetalPanel.add(new JScrollPane(tableSmall), BorderLayout.CENTER);
        tableSmallDetalPanel.add(buttEastSmallDetalPanel, BorderLayout.EAST);

        tableBigDetalPanel.add(new JScrollPane(tableBig), BorderLayout.CENTER);

        tableClassDetalPanel.add(new JScrollPane(tableClass), BorderLayout.CENTER);
        tableClassDetalPanel.add(buttFootClassDetalPanel, BorderLayout.SOUTH);

        infoPanel.add(title, BorderLayout.NORTH);
        infoPanel.add(titleInfoPanel, BorderLayout.CENTER);

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        buttPanel.add(buttSave);

        osnovaPanel.add(tableTabbedPane, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void initData(String type, EanList eanlist) {
        user = User.getInstance();

        idEanList = -1;

        eanDate.setDate((Calendar.getInstance()).getTime());

        vvodDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));
        insDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));
        vvodAvtor.setText(user.getFio());
        insAvtor.setText(user.getFio());

        createSmallTable(new Object[][]{});
        createBigTable(new Object[][]{});
        createClassTable(new Object[][]{});

        if (type.equals(UtilEan.ADD))
            title.setText("Новая заявка на получение EAN-кодов");

        else if (type.equals(UtilEan.EDIT) ||
                type.equals(UtilEan.COPY) ||
                type.equals(UtilEan.OPEN)) {

            title.setText("Заявка на получение EAN-кодов №" + eanlist.getIdEanlist());

            idEanList = eanlist.getIdEanlist();

            eanNum.setText(String.valueOf(eanlist.getEanNum()));
            eanName.setText(String.valueOf(eanlist.getEanName()));

            try {
                eanDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(eanlist.getEanDate()));
            } catch (ParseException e) {
                eanDate.setDate((Calendar.getInstance()).getTime());
            }

            if (type.equals(UtilEan.COPY)) {
                jRadioButton2.setSelected(true);
            } else {
                switch (eanlist.getIdStatus()) {
                    case 0:
                        jRadioButton1.setSelected(true);
                        break;
                    case 1:
                        jRadioButton2.setSelected(true);
                        break;
                    case 2:
                        jRadioButton4.setSelected(true);
                        break;
                    case 3:
                        jRadioButton5.setSelected(true);
                        break;
                    case 4:
                        jRadioButton6.setSelected(true);
                        break;
                    case -1:
                        jRadioButton3.setSelected(true);
                        break;
                    default:
                        jRadioButton2.setSelected(true);
                        break;
                }
            }

            vvodDate.setText(String.valueOf(eanlist.getDateVvod()));
            vvodAvtor.setText(String.valueOf(eanlist.getVvodFio()));
            insDate.setText(String.valueOf(eanlist.getInsDate()));
            insAvtor.setText(String.valueOf(eanlist.getInsFio()));

            eanNote.setText(String.valueOf(eanlist.getNote()));

            try {
                setDataInTables(eanlist.getEanItems());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Данные не загружены" + ex.getMessage(),
                        "Ошибка ",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }

        if (type.equals(UtilEan.COPY)) {
            title.setText("Новая заявка на получение EAN-кодов");

            idEanList = -1;
        }

        if (type.equals(UtilEan.OPEN)) {
            buttEastSmallDetalPanel.setVisible(false);
            buttFootClassDetalPanel.setVisible(false);
            buttSave.setVisible(false);
        }
    }

    private void buttPlusActionPerformed(ActionEvent evt) {
        buttEdit(UtilEan.ADD);
    }

    private void buttEditActionPerformed(ActionEvent evt) {
        buttEdit(UtilEan.EDIT);
    }

    private void buttCopyActionPerformed(ActionEvent evt) {
        buttEdit(UtilEan.COPY);
    }

    private void buttReturnEdit() {
        buttEdit(UtilEan.RETURN);
    }

    private void buttEditSrtActionPerformed(ActionEvent evt) {
        boolean selectRowFlag = false;
        for (int i = 0; i < tableSmall.getRowCount(); i++) {
            if (Boolean.valueOf(tableSmall.getValueAt(i, 0).toString())) {
                selectRowFlag = true;
                break;
            }
        }

        if (JOptionPane.showOptionDialog(null,
                boxPanel,
                "Добавить сорт?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Да", "Отмена"},
                "Нет") == JOptionPane.YES_OPTION) {

            if (box1.isSelected() || box2.isSelected() || box3.isSelected() || box4.isSelected()) {
                try {
                    if (selectRowFlag) {
                        for (int i = 0; i < tableSmall.getRowCount(); i++) {
                            if (Boolean.valueOf(tableSmall.getValueAt(i, 0).toString())) {
                                eanItemCurrent = getEanItem(tableSmall.convertRowIndexToModel(i));
                                eanItemCurrent.setIdFlag(0);

                                if (box1.isSelected()) {
                                    eanItemCurrent.setFasSrt(1);

                                    if (checkEanItemInTable(eanItemCurrent))
                                        insertEanItemInTable();
                                }

                                if (box2.isSelected()) {
                                    eanItemCurrent.setFasSrt(2);

                                    if (checkEanItemInTable(eanItemCurrent))
                                        insertEanItemInTable();
                                }

                                if (box3.isSelected()) {
                                    eanItemCurrent.setFasSrt(3);

                                    if (checkEanItemInTable(eanItemCurrent))
                                        insertEanItemInTable();
                                }

                                if (box4.isSelected()) {
                                    eanItemCurrent.setFasSrt(4);

                                    if (checkEanItemInTable(eanItemCurrent))
                                        insertEanItemInTable();
                                }

                                tableSmall.setValueAt(false, i, 0);
                            }
                        }
                    } else {
                        eanItemCurrent = getEanItem(tableSmall.convertRowIndexToModel(tableSmall.getSelectedRow()));
                        eanItemCurrent.setIdFlag(0);

                        if (box1.isSelected()) {
                            eanItemCurrent.setFasSrt(1);

                            if (checkEanItemInTable(eanItemCurrent))
                                insertEanItemInTable();
                        }

                        if (box2.isSelected()) {
                            eanItemCurrent.setFasSrt(2);

                            if (checkEanItemInTable(eanItemCurrent))
                                insertEanItemInTable();
                        }

                        if (box3.isSelected()) {
                            eanItemCurrent.setFasSrt(3);

                            if (checkEanItemInTable(eanItemCurrent))
                                insertEanItemInTable();
                        }

                        if (box4.isSelected()) {
                            eanItemCurrent.setFasSrt(4);

                            if (checkEanItemInTable(eanItemCurrent))
                                insertEanItemInTable();
                        }
                    }
                    JOptionPane.showMessageDialog(null,
                            "Сорт добавлен успешно!",
                            "Завершено",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Сбой!" + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void buttSearchbyMarshListActionPerformed(ActionEvent evt) {
        try {
            new SearchEANByMarshListForm(controller, true, "'01.09.2018'");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEdit(String type) {
        try {
            if (type.equals(UtilEan.ADD)) {
                new EanItemForm(controller, true);

            } else if (type.equals(UtilEan.EDIT)) {
                if (tableSmall.getSelectedRow() != -1) {
                    new EanItemForm(controller, true, getEanItem(tableSmall.convertRowIndexToModel(tableSmall.getSelectedRow())));

                    if (UtilEan.ACTION_BUTT_ITEM_ADD)
                        deleteEanItem(tableSmall.convertRowIndexToModel(tableSmall.getSelectedRow()));
                } else
                    JOptionPane.showMessageDialog(null,
                            "Вы ничего не выбрали!",
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);

            } else if (type.equals(UtilEan.COPY)) {
                if (tableSmall.getSelectedRow() != -1) {
                    new EanItemForm(controller, true, getEanItem(tableSmall.convertRowIndexToModel(tableSmall.getSelectedRow())), "");
                } else
                    JOptionPane.showMessageDialog(null,
                            "Вы ничего не выбрали!",
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);

            } else if (type.equals(UtilEan.RETURN)) {
                JOptionPane.showMessageDialog(null,
                        "Модель с таким артикулом, сортом, цветом уже есть в таблице",
                        "Внимание ",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

                type = UtilEan.EDIT;

                new EanItemForm(controller, true, type, eanItemCurrent);
            }

            if (UtilEan.ACTION_BUTT_ITEM_ADD) {
                if (checkEanItemInTable(eanItemCurrent))
                    insertEanItemInTable();
                else
                    buttReturnEdit();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditMinusActionPerformed(ActionEvent evt) {
        boolean select = false;
        for (int i = 0; i < tableSmall.getRowCount(); i++) {
            if (Boolean.valueOf(tableSmall.getValueAt(i, 0).toString())) {
                select = true;
                break;
            }
        }

        if (select) {
            if (JOptionPane.showOptionDialog(null,
                    "Удалить строки?",
                    "Удаление...",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Нет"},
                    "Нет") == JOptionPane.YES_OPTION) {

                pb = new ProgressBar(this, false, "Обновление ...");
                SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() {
                        try {
                            for (int i = 0; i < tableSmall.getRowCount(); i++) {
                                if (Boolean.valueOf(tableSmall.getValueAt(i, 0).toString())) {
                                    removeEanItem(tableSmall.convertRowIndexToModel(i));
                                    tableSmall.setValueAt(false, i, 0);
                                }
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,
                                    "Сбой обновления!" + e.getMessage(),
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
                pb.setVisible(true);
            }
        } else
            JOptionPane.showMessageDialog(null,
                    "Вы ничего не выбрали!",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);

        tableSmall.revalidate();
        tableSmall.repaint();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        try {
            if (typeEanList.equals(UtilEan.OPEN)) {
                dispose();
            } else {
                if (JOptionPane.showOptionDialog(null,
                        "Сохранить изменения?",
                        "Сохранение...",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Да", "Нет"},
                        "Нет") == JOptionPane.YES_OPTION)

                    buttSave.doClick();

                else {
                    dispose();
                }
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            boolean saveFlag = true;
            String str = "";

            try {
                if (!eanNum.getText().trim().equals("")) {
                    Integer.valueOf(eanNum.getText().trim());
                } else {
                    saveFlag = false;
                    str += "Вы не ввели номер заявки!\n";
                }
            } catch (NumberFormatException e) {
                saveFlag = false;
                str += "Номер заявки задан некорректно!\n";
            }

            if (eanName.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели название заявки!\n";
            }

            if (tModelSmall.getDataVector().size() <= 0) {
                saveFlag = false;
                str += "В заявке нет записей!\n";
            }

            if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eanDate.getDate()))) {
                saveFlag = false;
            }

            if (!saveFlag) {
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            if (saveFlag) {
                pb = new ProgressBar(this, false, "Сохранение заявки ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            epdb = new EanPDB();

                            if (typeEanList.equals(UtilEan.ADD) || typeEanList.equals(UtilEan.COPY)) {
                                if (epdb.addEanlist(
                                        Integer.valueOf(eanNum.getText().trim()),
                                        eanName.getText().trim(),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eanDate.getDate())),
                                        Integer.valueOf(buttonGroupStatus.getSelection().getActionCommand()),
                                        eanNote.getText().trim(),
                                        Integer.valueOf(user.getIdEmployee()),
                                        getDataInTables())) {

                                    JOptionPane.showMessageDialog(null,
                                            "Заявка на получение ean-кодов успешно добавлена! ",
                                            "Завершено",
                                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                    dispose();
                                }

                            } else if (typeEanList.equals(UtilEan.EDIT)) {
                                if (epdb.editEanlist(
                                        idEanList,
                                        Integer.valueOf(eanNum.getText().trim()),
                                        eanName.getText().trim(),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eanDate.getDate())),
                                        Integer.valueOf(buttonGroupStatus.getSelection().getActionCommand()),
                                        eanNote.getText().trim(),
                                        Integer.valueOf(user.getIdEmployee()),
                                        getDataInTables())) {

                                    JOptionPane.showMessageDialog(null,
                                            "Заявка на получение ean-кодов успешно сохранена! ",
                                            "Завершено",
                                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                    dispose();
                                }
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,
                                    "Ошибка. " + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);

                        } finally {
                            epdb.disConn();
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
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            switch (tableTabbedPane.getModel().getSelectedIndex()) {
                case 0:
                    break;
                case 1: {
                    EanOO oo = new EanOO(
                            title.getText().trim() + " , уч. номер " + eanNum.getText().trim(),
                            tModelSmall,
                            tableSmall.getColumnModel());
                    oo.createReport("DefaultTableAlbumFormatCheck.ots", false);
                    break;
                }
                case 2: {
                    EanOO oo = new EanOO(
                            title.getText().trim() + " , уч. номер " + eanNum.getText().trim(),
                            tModelBig,
                            tableBig.getColumnModel());
                    oo.createReport("DefaultTableBookFormatCheck.ots", false);
                    break;
                }
                case 3: {
                    EanOO oo = new EanOO(
                            title.getText().trim() + " , уч. номер " + eanNum.getText().trim(),
                            tModelClass,
                            tableClass.getColumnModel());
                    oo.createReport("DefaultTableAlbumFormatCheck.ots", false);
                    break;
                }
                default:
                    break;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttGpcSegActionPerformed(ActionEvent evt) {
        setKodClassInTable(7, UtilEan.DATA_KOD_GPC_SEG);
    }

    private void buttGpcSemActionPerformed(ActionEvent evt) {
        setKodClassInTable(8, UtilEan.DATA_KOD_GPC_SEM);
    }

    private void buttGpcKlActionPerformed(ActionEvent evt) {
        setKodClassInTable(9, UtilEan.DATA_KOD_GPC_KL);
    }

    private void buttGpcBrActionPerformed(ActionEvent evt) {
        setKodClassInTable(10, UtilEan.DATA_KOD_GPC_BR);
    }

    private void buttOkrbActionPerformed(ActionEvent evt) {
        setKodClassInTable(11, UtilEan.DATA_KOD_OKRB);
    }

    private void buttThbActionPerformed(ActionEvent evt) {
        setKodClassInTable(12, UtilEan.DATA_KOD_THB);
    }

    private void buttGostActionPerformed(ActionEvent evt) {
        setKodClassInTable(13, UtilEan.DATA_KOD_GOST);
    }

    private void setKodClassInTable(final int colum, String type) {
        boolean select = false;
        for (int i = 0; i < tableClass.getRowCount(); i++) {
            if (Boolean.valueOf(tableClass.getValueAt(i, 0).toString())) {
                select = true;
                break;
            }
        }

        if (select) {
            new SearchForm(this, true, type, "");

            if (UtilEan.ACTION_BUTT_SELECT) {
                pb = new ProgressBar(this, false, "Обновление ...");
                SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() {
                        try {
                            for (int i = 0; i < tableClass.getRowCount(); i++) {
                                if (Boolean.valueOf(tableClass.getValueAt(i, 0).toString())) {
                                    tModelClass.setValueAt(UtilEan.ITEM_ADD_SELECT_ITEM,
                                            i,
                                            colum);
                                }
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,
                                    "Сбой обновления!" + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
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
        } else
            JOptionPane.showMessageDialog(null,
                    "Вы ничего не выбрали!",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    private void createSmallTable(final Object[][] row) {
        tModelSmall = new DefaultTableModel(row, colSmall) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : super.getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelSmall.addTableModelListener(e -> {
            if (tableSmallListenerIsChanging) {
                return;
            }
            int firstRow = e.getFirstRow();
            int column = e.getColumn();

            if (column != 0 || maxSelectedRowSmall == -1 || minSelectedRowSmall == -1) {
                return;
            }
            tableSmallListenerIsChanging = true;
            boolean value = ((Boolean) tModelSmall.getValueAt(firstRow, column)).booleanValue();
            for (int i = minSelectedRowSmall; i <= maxSelectedRowSmall; i++) {
                tModelSmall.setValueAt(Boolean.valueOf(value), tableSmall.convertRowIndexToModel(i), column);
            }

            minSelectedRowSmall = -1;
            maxSelectedRowSmall = -1;

            tableSmallListenerIsChanging = false;
        });

        tableSmall.setModel(tModelSmall);
        tableSmall.setAutoCreateColumnsFromModel(true);

        tableSmall.getColumnModel().getColumn(1).setMinWidth(0);
        tableSmall.getColumnModel().getColumn(1).setMaxWidth(0);
        tableSmall.getColumnModel().getColumn(2).setMinWidth(0);
        tableSmall.getColumnModel().getColumn(2).setMaxWidth(0);
        tableSmall.getColumnModel().getColumn(3).setMinWidth(0);
        tableSmall.getColumnModel().getColumn(3).setMaxWidth(0);
        tableSmall.getColumnModel().getColumn(7).setMinWidth(0);
        tableSmall.getColumnModel().getColumn(7).setMaxWidth(0);

        for (int i = 1; i < tableSmall.getColumnModel().getColumnCount(); i++) {
            tableSmall.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tableSmall.getColumnModel().addColumnModelListener(tableSmallColumnModelListener);

        tableSmall.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableSmall.getTableHeader(), 0, ""));
    }

    private void createBigTable(final Object[][] row) {
        tModelBig = new DefaultTableModel(row, colBig) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : super.getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0 || col == 11) ? true : false;
            }
        };

        tModelBig.addTableModelListener(e -> {
            if (tableBigListenerIsChanging) {
                return;
            }
            int firstRow = e.getFirstRow();
            int column = e.getColumn();

            if (column != 0 || maxSelectedRowBig == -1 || minSelectedRowBig == -1) {
                return;
            }
            tableBigListenerIsChanging = true;
            boolean value = ((Boolean) tModelBig.getValueAt(firstRow, column)).booleanValue();
            for (int i = minSelectedRowBig; i <= maxSelectedRowBig; i++) {
                tModelBig.setValueAt(Boolean.valueOf(value), tableBig.convertRowIndexToModel(i), column);
            }

            minSelectedRowBig = -1;
            maxSelectedRowBig = -1;

            tableBigListenerIsChanging = false;
        });

        tableBig.setModel(tModelBig);
        tableBig.setAutoCreateColumnsFromModel(true);

        tableBig.getColumnModel().getColumn(1).setMinWidth(0);
        tableBig.getColumnModel().getColumn(1).setMaxWidth(0);
        tableBig.getColumnModel().getColumn(2).setMinWidth(0);
        tableBig.getColumnModel().getColumn(2).setMaxWidth(0);
        tableBig.getColumnModel().getColumn(3).setMinWidth(0);
        tableBig.getColumnModel().getColumn(3).setMaxWidth(0);

        for (int i = 1; i < tableBig.getColumnModel().getColumnCount(); i++) {
            tableBig.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tableBig.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableBig.getTableHeader(), 0, ""));
    }

    private void createClassTable(final Object[][] row) {
        tModelClass = new DefaultTableModel(row, colClass) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : super.getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelClass.addTableModelListener(e -> {
            if (tableClassListenerIsChanging) {
                return;
            }
            int firstRow = e.getFirstRow();
            int column = e.getColumn();

            if (column != 0 || maxSelectedRowClass == -1 || minSelectedRowClass == -1) {
                return;
            }
            tableClassListenerIsChanging = true;
            boolean value = ((Boolean) tModelClass.getValueAt(firstRow, column)).booleanValue();
            for (int i = minSelectedRowClass; i <= maxSelectedRowClass; i++) {
                tModelClass.setValueAt(Boolean.valueOf(value), tableClass.convertRowIndexToModel(i), column);
            }

            minSelectedRowClass = -1;
            maxSelectedRowClass = -1;

            tableClassListenerIsChanging = false;
        });

        tableClass.setModel(tModelClass);
        tableClass.setAutoCreateColumnsFromModel(true);

        tableClass.getColumnModel().getColumn(1).setMinWidth(0);
        tableClass.getColumnModel().getColumn(1).setMaxWidth(0);
        tableClass.getColumnModel().getColumn(2).setMinWidth(0);
        tableClass.getColumnModel().getColumn(2).setMaxWidth(0);
        tableClass.getColumnModel().getColumn(3).setMinWidth(0);
        tableClass.getColumnModel().getColumn(3).setMaxWidth(0);

        for (int i = 1; i < tableClass.getColumnModel().getColumnCount(); i++) {
            tableClass.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tableClass.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableClass.getTableHeader(), 0, ""));
    }

    /**
     * Метод добавляет элемент EanItem в таблицы формы просмотра
     */
    private void insertEanItemInTable() {
        try {
            pb = new ProgressBar(this, false, "Сбор данных...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        pb.setMessage("Обновление таблицы модель ...");

                        tModelSmall.insertRow(
                                tModelSmall.getRowCount(),
                                new Object[]{
                                        false,
                                        eanItemCurrent.getFlag(),
                                        eanItemCurrent.getId(),
                                        eanItemCurrent.getFasVid(),
                                        eanItemCurrent.getFasName(),
                                        eanItemCurrent.getFasNum(),
                                        eanItemCurrent.getFasNar(),
                                        eanItemCurrent.getColorNum(),
                                        eanItemCurrent.getColorName(),
                                        eanItemCurrent.getFasSrt(),
                                        eanItemCurrent.getKolX(),
                                        eanItemCurrent.getUpacText(),
                                        eanItemCurrent.getNoteText()}
                        );

                        pb.setMessage("Обновление таблицы размеры...");

                        for (EanItemListSize eanItemListSize : eanItemCurrent.getDataSize()) {

                            tModelBig.insertRow(
                                    tModelBig.getRowCount(),
                                    new Object[]{
                                            false,
                                            eanItemCurrent.getFlag(),
                                            eanItemCurrent.getId(),
                                            eanItemCurrent.getFasVid(),
                                            eanItemCurrent.getFasName(),
                                            eanItemCurrent.getFasNum(),
                                            eanItemCurrent.getFasNar(),
                                            eanItemCurrent.getColorNum(),
                                            eanItemCurrent.getColorName(),
                                            eanItemCurrent.getFasSrt(),
                                            eanItemListSize.getRst(),
                                            eanItemListSize.getRzm(),
                                            eanItemListSize.getSizePrint(),
                                            eanItemListSize.getEan13()});
                        }

                        pb.setMessage("Обновление таблицы классификация...");
                        tModelClass.insertRow(
                                tModelClass.getRowCount(),
                                new Object[]{
                                        false,
                                        eanItemCurrent.getFlag(),
                                        eanItemCurrent.getId(),
                                        eanItemCurrent.getFasVid(),
                                        eanItemCurrent.getFasName(),
                                        eanItemCurrent.getFasNum(),
                                        eanItemCurrent.getFasNar(),
                                        eanItemCurrent.getTextGpcSeg(),
                                        eanItemCurrent.getTextGpcSem(),
                                        eanItemCurrent.getTextGpcKl(),
                                        eanItemCurrent.getTextGpcBr(),
                                        eanItemCurrent.getTextOKRB(),
                                        eanItemCurrent.getTextTHB(),
                                        eanItemCurrent.getTextGOST()});

                    } catch (Exception e) {
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
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Метод возвращает все данные элементов EanItem все таблицы формы просмотра.
     * @return
     * @throws Exception
     */
    private ArrayList<EanItem> getDataInTables() throws Exception {
        ArrayList<EanItem> items = new ArrayList<>();

        try {
            for (int i = 0; i < tModelSmall.getDataVector().size(); i++)
                items.add(getEanItem(i));
        } catch (Exception e) {
            items = new ArrayList<EanItem>();
            System.out.println(e.getMessage());
            throw new Exception("Ошибка getTablesItem() " + e.getMessage(), e);
        }
        return items;
    }

    /**
     * Метод заполняет данными все таблицы формы просмотра.
     * @param items
     * @throws Exception
     */
    private void setDataInTables(ArrayList<EanItem> items) throws Exception {
        try {
            for (EanItem eanItem : items) {
                tModelSmall
                        .addRow(new Object[]{
                                false,
                                eanItem.getFlag(),
                                eanItem.getId(),
                                eanItem.getFasVid(),
                                eanItem.getFasName(),
                                eanItem.getFasNum(),
                                eanItem.getFasNar(),
                                eanItem.getColorNum(),
                                eanItem.getColorName(),
                                eanItem.getFasSrt(),
                                eanItem.getKolX(),
                                eanItem.getUpacText(),
                                eanItem.getNoteText()});
            }

            for (EanItem eanItem : items) {
                for (EanItemListSize eanItemListSize : eanItem.getDataSize()) {
                    tModelBig
                            .addRow(new Object[]{
                                    false,
                                    eanItem.getFlag(),
                                    eanItem.getId(),
                                    eanItem.getFasVid(),
                                    eanItem.getFasName(),
                                    eanItem.getFasNum(),
                                    eanItem.getFasNar(),
                                    eanItem.getColorNum(),
                                    eanItem.getColorName(),
                                    eanItem.getFasSrt(),
                                    eanItemListSize.getRst(),
                                    eanItemListSize.getRzm(),
                                    eanItemListSize.getSizePrint(),
                                    eanItemListSize.getEan13()});
                }
            }

            for (EanItem eanItem : items) {
                tModelClass
                        .addRow(new Object[]{
                                false,
                                eanItem.getFlag(),
                                eanItem.getId(),
                                eanItem.getFasVid(),
                                eanItem.getFasName(),
                                eanItem.getFasNum(),
                                eanItem.getFasNar(),
                                eanItem.getTextGpcSeg(),
                                eanItem.getTextGpcSem(),
                                eanItem.getTextGpcKl(),
                                eanItem.getTextGpcBr(),
                                eanItem.getTextOKRB(),
                                eanItem.getTextTHB(),
                                eanItem.getTextGOST()});
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            throw new Exception("Ошибка setDataInTables() " + e.getMessage(), e);
        }
    }

    /**
     * Метод возвращает данные элемента EanItem из всех таблиц формы просмотра.
     * @param nRow
     * @return
     * @throws Exception
     */
    private EanItem getEanItem(int nRow) throws Exception {
        EanItem eanItem = new EanItem();
        try {
            Vector rowSmall = (Vector) tModelSmall.getDataVector().elementAt(nRow);

            eanItem.setIdFlag(Integer.valueOf(rowSmall.get(1).toString()));
            eanItem.setId(Integer.valueOf(rowSmall.get(2).toString()));
            eanItem.setFasVid(Integer.valueOf(rowSmall.get(3).toString()));
            eanItem.setFasName(rowSmall.get(4).toString());
            eanItem.setFasNum(Integer.valueOf(rowSmall.get(5).toString()));
            ;
            eanItem.setFasNar(rowSmall.get(6).toString());
            eanItem.setColorNum(Integer.valueOf(rowSmall.get(7).toString()));
            eanItem.setColorName(rowSmall.get(8).toString());
            eanItem.setFasSrt(Integer.valueOf(rowSmall.get(9).toString()));
            eanItem.setKolX(Integer.valueOf(rowSmall.get(10).toString()));
            eanItem.setUpacText(rowSmall.get(11).toString());
            eanItem.setNoteText(rowSmall.get(12).toString());

            Vector rowClass;
            for (Object objectClass : tModelClass.getDataVector()) {
                rowClass = (Vector) objectClass;

                if (eanItem.getFasNum() == Integer.valueOf(rowClass.get(5).toString()) &&
                        eanItem.getFasNar().equals(rowClass.get(6).toString())) {
                    eanItem.setTextGpcSeg(Integer.valueOf(rowClass.get(7).toString()));
                    eanItem.setTextGpcSem(Integer.valueOf(rowClass.get(8).toString()));
                    eanItem.setTextGpcKl(Integer.valueOf(rowClass.get(9).toString()));
                    eanItem.setTextGpcBr(Integer.valueOf(rowClass.get(10).toString()));
                    eanItem.setTextOKRB(rowClass.get(11).toString());
                    eanItem.setTextTHB(rowClass.get(12).toString());
                    eanItem.setTextGOST(rowClass.get(13).toString());
                }
            }

            ArrayList<EanItemListSize> listSizes = new ArrayList<>();
            EanItemListSize size;
            Vector rowBig;
            for (Object objectBig : tModelBig.getDataVector()) {
                rowBig = (Vector) objectBig;

                if (eanItem.getFasNum() == Integer.valueOf(rowBig.get(5).toString()) &&
                        eanItem.getFasNar().equals(rowBig.get(6).toString()) &&
                        eanItem.getFasSrt() == Integer.valueOf(rowBig.get(9).toString()) &&
                        eanItem.getColorName().equals(rowBig.get(8).toString())) {
                    size = new EanItemListSize();
                    size.setRst(Integer.valueOf(rowBig.get(10).toString()));
                    size.setRzm(Integer.valueOf(rowBig.get(11).toString()));
                    size.setSizePrint(rowBig.get(12).toString());
                    listSizes.add(size);
                }
            }
            eanItem.setDataSize(listSizes);

        } catch (NumberFormatException e) {
            eanItem = new EanItem();
            System.out.println(e.getMessage());
            throw new Exception("Ошибка getEanItem() " + e.getMessage(), e);
        }
        return eanItem;
    }

    /**
     * Метод удаляет данные элемента EanItem из всех таблиц формы просмотра
     * @param nRow
     * @return
     * @throws Exception
     */
    private boolean deleteEanItem(int nRow) throws Exception {
        boolean rez = false;
        try {
            EanItem eanItem = getEanItem(nRow);

            Vector rowClass;
            for (int i = 0; i < tModelClass.getDataVector().size(); i++) {
                rowClass = (Vector) tModelClass.getDataVector().elementAt(i);
                if (eanItem.getFasNum() == Integer.valueOf(rowClass.get(5).toString()) &&
                        eanItem.getFasNar().equals(rowClass.get(6).toString())) {
                    tModelClass.removeRow(i);
                    i--;
                }
            }

            Vector rowBig;
            for (int i = 0; i < tModelBig.getDataVector().size(); i++) {
                rowBig = (Vector) tModelBig.getDataVector().elementAt(i);

                if (eanItem.getFasNum() == Integer.valueOf(rowBig.get(5).toString()) &&
                        eanItem.getFasNar().equals(rowBig.get(6).toString()) &&
                        eanItem.getFasSrt() == Integer.valueOf(rowBig.get(9).toString()) &&
                        eanItem.getColorName().equals(rowBig.get(8).toString())) {
                    tModelBig.removeRow(i);
                    i--;
                }
            }

            tModelSmall.removeRow(nRow);

            rez = true;

        } catch (Exception e) {
            rez = false;
            System.out.println(e.getMessage());
            throw new Exception("Ошибка deleteEanItem() " + e.getMessage(), e);
        }
        return rez;
    }

    /**
     * Метод помечает на удаление данные элемента EanItem из всех таблиц формы просмотра
     * @param nRow
     * @return
     * @throws Exception
     */
    private boolean removeEanItem(int nRow) throws Exception {
        boolean rez = false;
        try {
            EanItem eanItem = getEanItem(nRow);

            Vector rowClass;
            for (int i = 0; i < tModelClass.getDataVector().size(); i++) {
                rowClass = (Vector) tModelClass.getDataVector().elementAt(i);
                if (eanItem.getFasNum() == Integer.valueOf(rowClass.get(5).toString()) &&
                        eanItem.getFasNar().equals(rowClass.get(6).toString())) {
                    tModelClass.setValueAt(-1, i, 1);
                }
            }

            Vector rowBig;
            for (int i = 0; i < tModelBig.getDataVector().size(); i++) {
                rowBig = (Vector) tModelBig.getDataVector().elementAt(i);

                if (eanItem.getFasNum() == Integer.valueOf(rowBig.get(5).toString()) &&
                        eanItem.getFasNar().equals(rowBig.get(6).toString()) &&
                        eanItem.getFasSrt() == Integer.valueOf(rowBig.get(9).toString()) &&
                        eanItem.getColorName().equals(rowBig.get(8).toString())) {
                    tModelBig.setValueAt(-1, i, 1);
                }
            }

            tModelSmall.setValueAt(-1, nRow, 1);

            rez = true;

        } catch (Exception e) {
            rez = false;
            System.out.println(e.getMessage());
            throw new Exception("Ошибка removeEanItem() " + e.getMessage(), e);
        }
        return rez;
    }

    /**
     * Проверка на существование в таблице повторов записи моделей(модель, артикул, цвет, сорт)
     * @param item
     * @return
     * @throws Exception
     */
    public boolean checkEanItemInTable(EanItem item) throws Exception {
        boolean rez = true;
        try {

            for (int i = 0; i < tModelSmall.getDataVector().size(); i++) {
                if (
                    // проверка модели
                        item.getFasNum() == Integer.valueOf(((Vector) tModelSmall.getDataVector().elementAt(tableSmall.convertRowIndexToModel(i))).get(UtilEan.COLUM_FAS).toString()) &&
                                // проверка артикула
                                item.getFasNar().equals(((Vector) tModelSmall.getDataVector().elementAt(tableSmall.convertRowIndexToModel(i))).get(UtilEan.COLUM_NAR).toString()) &&
                                // проверка цвета
                                item.getColorNum() == Integer.valueOf(((Vector) tModelSmall.getDataVector().elementAt(tableSmall.convertRowIndexToModel(i))).get(UtilEan.COLUM_COLOR).toString()) &&
                                // проверка сорта
                                item.getFasSrt() == Integer.valueOf(((Vector) tModelSmall.getDataVector().elementAt(tableSmall.convertRowIndexToModel(i))).get(UtilEan.COLUM_SRT).toString()) &&
                                // проверка что запись не помечена на удаление
                                -1 != Integer.valueOf(((Vector) tModelSmall.getDataVector().elementAt(tableSmall.convertRowIndexToModel(i))).get(1).toString())
                ) {
                    rez = false;
                    break;
                }
            }

        } catch (NumberFormatException e) {
            rez = false;
            System.out.println(e.getMessage());
            throw new Exception("Ошибка checkItem() " + e.getMessage(), e);
        }
        return rez;
    }
}
