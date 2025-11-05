package dept.production.planning.ean;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Класс для создания и корректировки ассортимента 
 *
 * @author lidashka
 */

public class EanItemForm extends JDialog {
    MainController controller;
    private Integer fasSar;
    private JButton buttClose;
    private JButton buttSave;
    private JButton buttGpcSeg;
    private JButton buttGpcSem;
    private JButton buttGpcKl;
    private JButton buttGpcBr;
    private JButton buttOKRB;
    private JButton buttTHB;
    private JButton buttGOST;
    private JButton buttDefault;
    private JButton buttFasSearch;
    private JButton buttColorSearch;
    private JButton buttShkalaPlus;
    private JButton buttShkalaMinus;
    private JButton buttShkalaEdit;
    private JButton buttNarCheck;
    private JTextField fasNum;
    private JTextField fasName;
    private JTextField colorNum;
    private JTextField colorName;
    private JTextField sarText;
    private JTextField narText;
    private JTextField kolXText;
    private JTextField upacText;
    private JTextField textGpcSeg;
    private JTextField textGpcSem;
    private JTextField textGpcKl;
    private JTextField textGpcBr;
    private JTextField textOKRB;
    private JTextField textTHB;
    private JTextField textGOST;
    private JTextPane noteText;
    private JPanel boxPanel;
    private ButtonGroup buttonGroupFasVid;
    private ButtonGroup buttonGroupFasSrt;
    private ButtonGroup buttonGroupBox;
    private ButtonGroup buttonGroupBoxRzm2;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private JRadioButton fasSrt1;
    private JRadioButton fasSrt2;
    private JRadioButton fasSrt3;
    private JRadioButton fasSrt4;
    private JRadioButton box1;
    private JRadioButton box2;
    private JRadioButton box3;
    private JRadioButton box4;
    private JCheckBox box5;
    private JRadioButton box6;
    private JRadioButton box7;
    private JRadioButton box8;
    private Object[] col;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter sorter;
    private int minSelectedRow;
    private int maxSelectedRow;
    private boolean tableModelListenerIsChanging;
    private User user;
    private ProgressBar pb;
    private EanPDB epdb;
    private String typeEanListItem;
    private boolean flagEdit;
    private int idItemEdit;
    private int flagItemEdit;

    /**
     * Конструктор формы в режиме add 
     * @param controller
     * @param modal
     */
    public EanItemForm(MainController controller, boolean modal) {
        super(controller.getMainForm(), modal);
        this.controller = controller;


        setConstants(UtilEan.ADD, null);
        cleanConstants();
        initMenu();
        initPropSetting();
        init();
        initData(typeEanListItem, null);

        this.setTitle("Добавить новую запись");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    /**
     * Конструктор формы в режиме edit 
     * @param controller
     * @param modal
     * @param item
     */
    public EanItemForm(MainController controller, boolean modal, EanItem item) {
        super(controller.getMainForm(), modal);
        this.controller = controller;
        setConstants(UtilEan.EDIT, item);
        cleanConstants();
        initMenu();
        initPropSetting();
        init();
        initData(typeEanListItem, item);

        this.setTitle("Редактировать запись");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    /**
     * Конструктор формы в режиме copy 
     * @param controller
     * @param modal
     * @param eanItem
     * @param type
     */
    public EanItemForm(MainController controller, boolean modal, EanItem eanItem, String type) {
        super(controller.getMainForm(), modal);
        this.controller = controller;

        setConstants(UtilEan.COPY, eanItem);
        cleanConstants();
        initMenu();
        initPropSetting();
        init();
        initData(typeEanListItem, eanItem);

        this.setTitle("Редактировать запись");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    /**
     * Конструктор формы в режиме return 
     * @param controller
     * @param modal
     * @param type
     * @param item
     */
    public EanItemForm(MainController controller, boolean modal, String type, EanItem item) {
        super(controller.getMainForm(), modal);
        this.controller = controller;

        setConstants(type, item);
        cleanConstants();
        initMenu();
        initPropSetting();
        init();
        initData(typeEanListItem, item);

        this.setTitle("Редактировать запись");
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(850, 680));
        setPreferredSize(new Dimension(850, 680));

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSave = new JButton("Сохранить");
        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttFasSearch = new JButton("Найти");
        buttFasSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttFasSearchActionPerformed(evt);
            }
        });

        buttColorSearch = new JButton("Найти");
        buttColorSearch.addActionListener(evt -> buttColorSearchActionPerformed(evt));

        buttShkalaPlus = new JButton("+");
        buttShkalaPlus.addActionListener(evt -> buttShkalaPlusActionPerformed(evt));

        buttShkalaMinus = new JButton("-");
        buttShkalaMinus.addActionListener(evt -> buttShkalaMinusActionPerformed(evt));

        buttShkalaEdit = new JButton("изм");
        buttShkalaEdit.addActionListener(evt -> buttShkalaEditActionPerformed(evt));

        buttNarCheck = new JButton("Проверить");
        buttNarCheck.addActionListener(evt -> buttNarCheckActionPerformed(evt));

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

        buttDefault = new JButton("Шаблон");
        buttDefault.addActionListener(evt -> buttDefaultActionPerformed(evt));

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex()
                    == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
            maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex()
                    == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
        });
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
        tModel = new DefaultTableModel();

        textGpcSeg = new JTextField();
        textGpcSeg.setPreferredSize(new Dimension(120, 20));

        textGpcSem = new JTextField();
        textGpcSem.setPreferredSize(new Dimension(120, 20));

        textGpcKl = new JTextField();
        textGpcKl.setPreferredSize(new Dimension(120, 20));

        textGpcBr = new JTextField();
        textGpcBr.setPreferredSize(new Dimension(120, 20));

        textOKRB = new JTextField();
        textOKRB.setPreferredSize(new Dimension(120, 20));

        textTHB = new JTextField();
        textTHB.setPreferredSize(new Dimension(120, 20));

        textGOST = new JTextField();
        textGOST.setPreferredSize(new Dimension(120, 20));

        colorNum = new JTextField();
        colorNum.setPreferredSize(new Dimension(30, 20));
        colorNum.setEditable(false);
        //colorNum.setVisible(false); 

        colorName = new JTextField();
        colorName.setPreferredSize(new Dimension(250, 20));
        colorName.setEditable(false);

        sarText = new JTextField();
        sarText.setPreferredSize(new Dimension(120, 20));

        narText = new JTextField();
        narText.setPreferredSize(new Dimension(250, 20));

        upacText = new JTextField();
        upacText.setPreferredSize(new Dimension(250, 20));

        kolXText = new JTextField();
        kolXText.setPreferredSize(new Dimension(120, 20));

        fasSrt1 = new JRadioButton();
        fasSrt1.setFont(new java.awt.Font("Dialog", 0, 13));
        fasSrt1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fasSrt1.setText("1;");
        fasSrt1.setActionCommand("1");
        fasSrt1.setSelected(true);

        fasSrt2 = new JRadioButton();
        fasSrt2.setFont(new java.awt.Font("Dialog", 0, 13));
        fasSrt2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fasSrt2.setText("2;");
        fasSrt2.setActionCommand("2");

        fasSrt3 = new JRadioButton();
        fasSrt3.setFont(new java.awt.Font("Dialog", 0, 13));
        fasSrt3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fasSrt3.setText("н/c;");
        fasSrt3.setActionCommand("3");

        fasSrt4 = new JRadioButton();
        fasSrt4.setFont(new java.awt.Font("Dialog", 0, 13));
        fasSrt4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fasSrt4.setText("У;");
        fasSrt4.setActionCommand("4");

        jRadioButton1 = new JRadioButton();
        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton1.setText("муж;");
        jRadioButton1.setActionCommand("1");

        jRadioButton2 = new JRadioButton();
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setText("жен;");
        jRadioButton2.setActionCommand("2");

        jRadioButton3 = new JRadioButton();
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setText("дет;");
        jRadioButton3.setActionCommand("3");

        jRadioButton4 = new JRadioButton();
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setText("нет;");
        jRadioButton4.setActionCommand("0");
        jRadioButton4.setSelected(true);

        box1 = new JRadioButton();
        box1.setFont(new java.awt.Font("Dialog", 0, 13));
        box1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box1.setText("рост - размер;");
        box1.setActionCommand("0");
        box1.setSelected(true);

        box2 = new JRadioButton();
        box2.setFont(new java.awt.Font("Dialog", 0, 13));
        box2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box2.setText("диапазон роста - размер;");
        box2.setActionCommand("1");

        box8 = new JRadioButton();
        box8.setFont(new java.awt.Font("Dialog", 0, 13));
        box8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box8.setText("размер - диапазон роста;");
        box8.setActionCommand("2");

        box3 = new JRadioButton();
        box3.setFont(new java.awt.Font("Dialog", 0, 13));
        box3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box3.setText("без роста - диапазон размера;");
        box3.setActionCommand("3");

        box4 = new JRadioButton();
        box4.setFont(new java.awt.Font("Dialog", 0, 13));
        box4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box4.setText("без роста - размер;");
        box4.setActionCommand("4");

        box5 = new JCheckBox("+ обхват бедер");
        box5.addActionListener(evt -> checkbox5ActionPerformed(evt));

        box6 = new JRadioButton();
        box6.setFont(new java.awt.Font("Dialog", 0, 13));
        box6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box6.setText("+6;");
        box6.setActionCommand("+6");
        box6.setSelected(true);
        box6.setEnabled(false);

        box7 = new JRadioButton();
        box7.setFont(new java.awt.Font("Dialog", 0, 13));
        box7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box7.setText("-10;");
        box7.setActionCommand("-10");
        box7.setEnabled(false);

        JPanel tmp = new JPanel();
        tmp.add(box6);
        tmp.add(box7);

        boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(0, 1, 5, 5));
        boxPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        boxPanel.add(box1);
        boxPanel.add(box2);
        boxPanel.add(box8);
        boxPanel.add(box3);
        boxPanel.add(box4);
        boxPanel.add(box5);
        boxPanel.add(tmp);

        buttonGroupFasVid = new ButtonGroup();
        buttonGroupFasVid.add(jRadioButton4);
        buttonGroupFasVid.add(jRadioButton1);
        buttonGroupFasVid.add(jRadioButton2);
        buttonGroupFasVid.add(jRadioButton3);

        buttonGroupFasSrt = new ButtonGroup();
        buttonGroupFasSrt.add(fasSrt1);
        buttonGroupFasSrt.add(fasSrt2);
        buttonGroupFasSrt.add(fasSrt3);
        buttonGroupFasSrt.add(fasSrt4);

        buttonGroupBox = new ButtonGroup();
        buttonGroupBox.add(box1);
        buttonGroupBox.add(box8);
        buttonGroupBox.add(box2);
        buttonGroupBox.add(box3);
        buttonGroupBox.add(box4);

        buttonGroupBoxRzm2 = new ButtonGroup();
        buttonGroupBoxRzm2.add(box6);
        buttonGroupBoxRzm2.add(box7);

        col = new Object[4];
        col[0] = "";
        col[1] = "Рост";
        col[2] = "Размер";
        col[3] = "Этикетка";

        // Модель
        fasNum = new JTextField();
        fasNum.setPreferredSize(new Dimension(120, 20));
        fasNum.addActionListener(e -> buttFasSearch.doClick());

        fasName = new JTextField();
        fasName.setPreferredSize(new Dimension(380, 20));

        // Вид
        JPanel vidPanel = new JPanel();
        vidPanel.setLayout(new ParagraphLayout());
        vidPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вид"));
        vidPanel.add(jRadioButton4);
        vidPanel.add(jRadioButton1);
        vidPanel.add(jRadioButton2);
        vidPanel.add(jRadioButton3);

        // Сорт
        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new ParagraphLayout());
        sortPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Сорт"));
        sortPanel.add(fasSrt1);
        sortPanel.add(fasSrt2);
        sortPanel.add(fasSrt3);
        sortPanel.add(fasSrt4);

        // Комплект
        JPanel kolXPanel = new JPanel();
        kolXPanel.setLayout(new ParagraphLayout());
        kolXPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Комплект"));
        kolXPanel.add(kolXText);

        // Артикул
        JPanel narPanel = new JPanel();
        narPanel.setLayout(new ParagraphLayout());
        narPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Артикул"));
        narPanel.add(narText);
        narPanel.add(buttNarCheck);

        // Цвет 
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new ParagraphLayout());
        colorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Цвет"));
        colorPanel.add(colorNum);
        colorPanel.add(colorName);
        colorPanel.add(buttColorSearch);

        // Размерная сетка
        JScrollPane scShkala = new JScrollPane(table);
        scShkala.setPreferredSize(new Dimension(280, 280));

        JPanel buttShkalaPanel = new JPanel();
        buttShkalaPanel.setLayout(new GridLayout(7, 0, 5, 5));
        buttShkalaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttShkalaPanel.add(buttShkalaPlus);
        buttShkalaPanel.add(buttShkalaMinus);
        buttShkalaPanel.add(buttShkalaEdit);

        JPanel rzmPanel = new JPanel();
        rzmPanel.setLayout(new BorderLayout(1, 1));
        rzmPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Размерная сетка"));
        rzmPanel.add(scShkala, BorderLayout.CENTER);
        rzmPanel.add(buttShkalaPanel, BorderLayout.EAST);

        // Коды
        JPanel kodPanel = new JPanel();
        kodPanel.setLayout(new GridLayout(8, 2, 5, 5));
        kodPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Коды"));
        kodPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        kodPanel.add(buttGpcSeg);
        kodPanel.add(textGpcSeg);
        kodPanel.add(buttGpcSem);
        kodPanel.add(textGpcSem);
        kodPanel.add(buttGpcKl);
        kodPanel.add(textGpcKl);
        kodPanel.add(buttGpcBr);
        kodPanel.add(textGpcBr);
        kodPanel.add(buttOKRB);
        kodPanel.add(textOKRB);
        kodPanel.add(buttTHB);
        kodPanel.add(textTHB);
        kodPanel.add(buttGOST);
        kodPanel.add(textGOST);
        kodPanel.add(buttDefault);

        // Упаковка
        JPanel upacPanel = new JPanel();
        upacPanel.setLayout(new BorderLayout(1, 1));
        upacPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Упаковка"));
        upacPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        upacPanel.add(upacText);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(1, 1));
        rightPanel.add(kodPanel, BorderLayout.CENTER);
        rightPanel.add(upacPanel, BorderLayout.SOUTH);

        // Примечание        
        noteText = new JTextPane();

        JScrollPane notePane = new JScrollPane(noteText);
        notePane.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание"));
        notePane.setPreferredSize(new Dimension(300, 60));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new ParagraphLayout());
        centerPanel.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(new JLabel("Модель:"));
        centerPanel.add(fasNum);
        centerPanel.add(fasName);
        centerPanel.add(buttFasSearch);
        centerPanel.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(vidPanel);
        centerPanel.add(sortPanel);
        centerPanel.add(kolXPanel);
        centerPanel.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(narPanel);
        centerPanel.add(colorPanel);
        centerPanel.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(rzmPanel, ParagraphLayout.NEW_LINE_STRETCH_HV);
        centerPanel.add(new JLabel());
        centerPanel.add(rightPanel);
        centerPanel.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(notePane, ParagraphLayout.NEW_LINE_STRETCH_H);

        JPanel buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttSave);

        JPanel osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnovaPanel.add(centerPanel, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void checkbox5ActionPerformed(ActionEvent evt) {
        if (box5.isSelected()) {
            box6.setEnabled(true);
            box7.setEnabled(true);
        } else {
            box6.setEnabled(false);
            box7.setEnabled(false);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            UtilEan.ACTION_BUTT_ITEM_ADD = false;

            boolean saveFlag = true;
            String str = "";

            // проверка Модели
            if (fasNum.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели модель!\n";
            }

            if (fasName.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели название модели!\n";
            }

            // проверка Комплект 
            try {
                if (!kolXText.getText().trim().equals(""))
                    Integer.valueOf(kolXText.getText().trim());
            } catch (NumberFormatException e) {
                saveFlag = false;
                str += "Комплект задан некорректно!\n";
            }

            // проверка Цвет
            if (colorNum.getText().trim().equals("")) {
                saveFlag = false;
                str += "Цвет задан некорректно!\n";
            } else {
                try {
                    if (!colorNum.getText().trim().equals(""))
                        Integer.valueOf(colorNum.getText().trim());
                } catch (NumberFormatException e) {
                    saveFlag = false;
                    str += "Цвет задан некорректно!\n";
                }
            }

            if (colorName.getText().trim().equals("")) {
                if (JOptionPane.showOptionDialog(null,
                        "Цвет пустая строка!\n"
                                + "Продолжить сохранение?\n",
                        "Внимание",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Да", "Нет"},
                        "Нет") == JOptionPane.NO_OPTION) {
                    saveFlag = false;
                    str += "Цвет задан некорректно!\n";
                }
            }
            /*
            if(colorName.getText().trim().equals("")){
                saveFlag = false;
                str += "Вы не ввели название цвета!\n";
            } 
            */

            // проверка Код GPC сегмент
            try {
                if (!textGpcSeg.getText().trim().equals(""))
                    Integer.valueOf(textGpcSeg.getText().trim());
                else
                    textGpcSeg.setText("0");
            } catch (Exception e) {
                saveFlag = false;
                str += "GPC сегмент задан некорректно!\n";
            }

            // проверка Код GPC семейство
            try {
                if (!textGpcSem.getText().trim().equals(""))
                    Integer.valueOf(textGpcSem.getText().trim());
                else
                    textGpcSem.setText("0");
            } catch (Exception e) {
                saveFlag = false;
                str += "GPC семейство задан некорректно!\n";
            }

            // проверка Код GPC класс
            try {
                if (!textGpcKl.getText().trim().equals(""))
                    Integer.valueOf(textGpcKl.getText().trim());
                else
                    textGpcKl.setText("0");
            } catch (Exception e) {
                saveFlag = false;
                str += "GPC класс задан некорректно!\n";
            }

            // проверка Код GPC брик
            try {
                if (!textGpcBr.getText().trim().equals(""))
                    Integer.valueOf(textGpcBr.getText().trim());
                else
                    textGpcBr.setText("0");
            } catch (Exception e) {
                saveFlag = false;
                str += "GPC брик задан некорректно!\n";
            }

            // проверка ОКРБ

            try {
                if (textOKRB.getText().trim().equals("")) {
                    saveFlag = false;
                    str += "Вы не ввели ОКРБ!\n";
                }
            } catch (Exception e) {
                saveFlag = false;
                str += "ОКРБ задан некорректно!\n";
            }

            // проверка ТНВЭДТ
            try {
                if (textTHB.getText().trim().equals("")) {
                    saveFlag = false;
                    str += "Вы не ввели ТНВЭДТ!\n";
                }
            } catch (Exception e) {
                saveFlag = false;
                str += "ГОСТ задан некорректно!\n";
            }

            // проверка ГОСТ
            try {
                if (textOKRB.getText().trim().equals("")) {
                    saveFlag = false;
                    str += "Вы не ввели ГОСТ!\n";
                }
            } catch (Exception e) {
                saveFlag = false;
                str += "ГОСТ задан некорректно!\n";
            }

            // проверка Размерная сетка
            if (tModel.getDataVector().size() <= 0) {
                saveFlag = false;
                str += "Вы не ввели шкалу размеров!\n";
            }

            if (!saveFlag) {
                JOptionPane.showMessageDialog(
                        null,
                        str,
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            if (saveFlag) {
                pb = new ProgressBar(this, false, "Сохранение ...");
                SwingWorker sw = new SwingWorker() {
                    private EanItem eanItemEdit;

                    protected Object doInBackground() {
                        try {
                            if (typeEanListItem.equals(UtilEan.ADD) || typeEanListItem.equals(UtilEan.COPY)) {
                                idItemEdit = -1;
                                flagItemEdit = 0;
                            }
                            eanItemEdit = new EanItem();
                            eanItemEdit.setIdFlag(flagItemEdit);
                            eanItemEdit.setId(idItemEdit);
                            eanItemEdit.setFasNum(Integer.valueOf(fasNum.getText().trim()));
                            eanItemEdit.setFasName(fasName.getText().trim().replace("  ", " ").replace("  ", " ").toUpperCase());
                            eanItemEdit.setFasVid(Integer.valueOf(buttonGroupFasVid.getSelection().getActionCommand()));
                            eanItemEdit.setFasSrt(Integer.valueOf(buttonGroupFasSrt.getSelection().getActionCommand()));
                            eanItemEdit.setKolX(Integer.valueOf(kolXText.getText().trim()));
                            eanItemEdit.setFasNar(narText.getText().trim().toUpperCase());
                            eanItemEdit.setColorNum(Integer.valueOf(colorNum.getText().trim()));
                            eanItemEdit.setColorName(colorName.getText().trim().replace("  ", " ").replace("  ", " ").toUpperCase());
                            eanItemEdit.setNoteText(noteText.getText().trim());
                            eanItemEdit.setUpacText(upacText.getText().trim());
                            eanItemEdit.setTextGpcSeg(Integer.valueOf(textGpcSeg.getText().trim()));
                            eanItemEdit.setTextGpcSem(Integer.valueOf(textGpcSem.getText().trim()));
                            eanItemEdit.setTextGpcKl(Integer.valueOf(textGpcKl.getText().trim()));
                            eanItemEdit.setTextGpcBr(Integer.valueOf(textGpcBr.getText().trim()));
                            eanItemEdit.setTextOKRB(textOKRB.getText().trim());
                            eanItemEdit.setTextTHB(textTHB.getText().trim());
                            eanItemEdit.setTextGOST(textGOST.getText().trim());

                            ArrayList<EanItemListSize> dataSize = new ArrayList<EanItemListSize>();
                            EanItemListSize listSize = null;

                            for (Object object : tModel.getDataVector()) {
                                listSize = new EanItemListSize();

                                listSize.setRst(Integer.valueOf(((Vector) object).get(1).toString()));
                                listSize.setRzm(Integer.valueOf(((Vector) object).get(2).toString()));
                                listSize.setSizePrint(((Vector) object).get(3).toString());

                                dataSize.add(listSize);
                            }

                            eanItemEdit.setDataSize(dataSize);

                            UtilEan.ACTION_BUTT_ITEM_ADD = true;

                            EanDetalForm.setCurrentEanItem(eanItemEdit);

                            dispose();
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null,
                                    "Ошибка. " + e.getMessage(),
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttFasSearchActionPerformed(ActionEvent evt) {
        try {
            ArrayList<String> search = new ArrayList<>();
            search.add(fasNum.getText().trim().toLowerCase());
            search.add(buttonGroupFasSrt.getSelection().getActionCommand());
            EanDB eanDB = new EanDB();
            Couple<String, Integer> couple = eanDB.getTnvedByItemModel(fasNum.getText());
            this.textTHB.setText(couple.getV1());
            this.fasSar = couple.getV2();
            new SelectFasForm(controller, true, search);

            if (UtilEan.ACTION_BUTT_SELECT_FAS) {
                fasNum.setText(UtilEan.ITEM_ADD_FAS_NUM);
                fasName.setText(UtilEan.ITEM_ADD_FAS_NAME);
                narText.setText(UtilEan.ITEM_ADD_FAS_NAR);
                kolXText.setText(UtilEan.ITEM_ADD_FAS_KOLX);

                setVidFas(UtilEan.ITEM_ADD_FAS_VID);
                setSrtFas(UtilEan.ITEM_ADD_SRT);

                setDataTable(UtilEan.ITEM_ADD_RST_RZM);

                if (!UtilEan.ITEM_ADD_COLOR_NUM.equals("-1") || !UtilEan.ITEM_ADD_COLOR_NUM.equals("2")) {
                    colorNum.setText(UtilEan.ITEM_ADD_COLOR_NUM);
                    colorName.setText(UtilEan.ITEM_ADD_COLOR_NAME);
                }
            }


        } catch (Exception e) {
            fasNum.setText("");
            fasName.setText("");
            setDataTable(null);
            colorNum.setText("2");
            colorName.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTnvedtCode() {
        return "";
    }

    private void buttColorSearchActionPerformed(ActionEvent evt) {
        try {
            new SearchForm(this, true, UtilEan.SELECT_COLOR, "");

            if (UtilEan.ACTION_BUTT_SELECT) {
                colorNum.setText(UtilEan.ITEM_ADD_SELECT_ITEM_ID);
                colorName.setText(UtilEan.ITEM_ADD_SELECT_ITEM);
            }

        } catch (Exception e) {
            colorNum.setText("2");
            colorName.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttShkalaPlusActionPerformed(ActionEvent evt) {
        try {
            Vector tmp = new Vector();
            tmp.add(false);
            tmp.add(Integer.valueOf("0"));
            tmp.add(Integer.valueOf("0"));
            tmp.add("0-0");
            tModel.insertRow(0, tmp);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttShkalaMinusActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(null,
                    "Удалить отмеченные строки?",
                    "Удаление ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Удалить", "Отмена"},
                    "Удалить") == JOptionPane.YES_OPTION) {

                for (int i = 0; i < tModel.getDataVector().size(); i++) {
                    if (Boolean.valueOf(((Vector) tModel.getDataVector().get(i)).elementAt(0).toString())) {
                        tModel.getDataVector().remove(i);
                        i--;
                    }
                }

                tModel.fireTableDataChanged();

                //setDataTable(tModel.getDataVector());
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttShkalaEditActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(
                    null,
                    boxPanel,
                    "Редактировать ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"},
                    "Сохранить") == JOptionPane.YES_OPTION) {

                for (int i = 0; i < tModel.getDataVector().size(); i++) {
                    Vector tmp = ((Vector) tModel.getDataVector().get(i));

                    if (Boolean.valueOf(tmp.elementAt(0).toString())) {

                        tModel.setValueAt(
                                getValue(tmp.elementAt(1).toString(),
                                        tmp.elementAt(2).toString(),
                                        buttonGroupBox.getSelection().getActionCommand(),
                                        box5.isSelected(),
                                        Integer.valueOf(buttonGroupBoxRzm2.getSelection().getActionCommand())),
                                i,
                                3);

                    }
                }
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttGpcSegActionPerformed(ActionEvent evt) {
        new SearchForm(this, true, UtilEan.DATA_KOD_GPC_SEG, "");

        if (UtilEan.ACTION_BUTT_SELECT) {
            textGpcSeg.setText(UtilEan.ITEM_ADD_SELECT_ITEM);
        }
    }

    private void buttGpcSemActionPerformed(ActionEvent evt) {
        new SearchForm(this, true, UtilEan.DATA_KOD_GPC_SEM, "");

        if (UtilEan.ACTION_BUTT_SELECT) {
            textGpcSem.setText(UtilEan.ITEM_ADD_SELECT_ITEM);
        }
    }

    private void buttGpcKlActionPerformed(ActionEvent evt) {
        new SearchForm(this, true, UtilEan.DATA_KOD_GPC_KL, "");

        if (UtilEan.ACTION_BUTT_SELECT) {
            textGpcKl.setText(UtilEan.ITEM_ADD_SELECT_ITEM);
        }
    }

    private void buttGpcBrActionPerformed(ActionEvent evt) {
        new SearchForm(this, true, UtilEan.DATA_KOD_GPC_BR, "");

        if (UtilEan.ACTION_BUTT_SELECT) {
            textGpcBr.setText(UtilEan.ITEM_ADD_SELECT_ITEM);
        }
    }

    private void buttOkrbActionPerformed(ActionEvent evt) {
        new SearchForm(this, true, UtilEan.DATA_KOD_OKRB, "");

        if (UtilEan.ACTION_BUTT_SELECT) {
            textOKRB.setText(UtilEan.ITEM_ADD_SELECT_ITEM);
        }
    }

    private void buttThbActionPerformed(ActionEvent evt) {
        new SearchForm(this, true, UtilEan.DATA_KOD_THB, "");

        if (UtilEan.ACTION_BUTT_SELECT) {
            textTHB.setText(UtilEan.ITEM_ADD_SELECT_ITEM);
        }
    }

    private void buttGostActionPerformed(ActionEvent evt) {
        new SearchForm(this, true, UtilEan.DATA_KOD_GOST, "");

        if (UtilEan.ACTION_BUTT_SELECT) {
            textGOST.setText(UtilEan.ITEM_ADD_SELECT_ITEM);
        }
    }

    private void buttNarCheckActionPerformed(ActionEvent evt) {
        if (!narText.getText().trim().toUpperCase().equals("")) {
            new SearchForm(this, true, UtilEan.CHECK_FAS_NAR, narText.getText().trim().toUpperCase());

        } else
            JOptionPane.showMessageDialog(null,
                    "Вы не ввели артикул!",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    private void buttDefaultActionPerformed(ActionEvent evt) {
        new SearchTemplateForm(this, true, UtilEan.DATA_KOD_DEFAULT, fasNum.getText().trim());

        if (UtilEan.ACTION_BUTT_SELECT) {
            textGpcSeg.setText(UtilEan.ITEM_ADD_FAS_KOD_DEFAULT.elementAt(0).toString());
            textGpcSem.setText(UtilEan.ITEM_ADD_FAS_KOD_DEFAULT.elementAt(1).toString());
            textGpcKl.setText(UtilEan.ITEM_ADD_FAS_KOD_DEFAULT.elementAt(2).toString());
            textGpcBr.setText(UtilEan.ITEM_ADD_FAS_KOD_DEFAULT.elementAt(3).toString());
            textOKRB.setText(UtilEan.ITEM_ADD_FAS_KOD_DEFAULT.elementAt(4).toString());
            textTHB.setText(UtilEan.ITEM_ADD_FAS_KOD_DEFAULT.elementAt(5).toString());
            textGOST.setText(UtilEan.ITEM_ADD_FAS_KOD_DEFAULT.elementAt(6).toString());
        }
    }

    private void cleanConstants() {
        UtilEan.ACTION_BUTT_ITEM_ADD = false;
    }

    private void initMenu() {

    }

    private void initPropSetting() {

    }

    private void initData(String type, EanItem item) {
        user = User.getInstance();

        fasSrt1.setSelected(true);

        kolXText.setText("1");
        upacText.setText("Пакет");

        textGpcSeg.setText("67000000");
        textGpcSem.setText("67010000");
        textGpcKl.setText("0");
        textGpcBr.setText("0");

        colorNum.setText("2");

        createTableShkala(new Object[][]{});

        if (type.equals(UtilEan.EDIT) || type.equals(UtilEan.COPY)) {
            fasNum.setText(String.valueOf(item.getFasNum()));
            fasName.setText(item.getFasName());
            narText.setText(item.getFasNar());
            kolXText.setText(String.valueOf(item.getKolX()));

            setVidFas(String.valueOf(item.getFasVid()));
            setSrtFas(String.valueOf(item.getFasSrt()));

            colorNum.setText(String.valueOf(item.getColorNum()));
            colorName.setText(item.getColorName());

            upacText.setText(item.getUpacText());
            noteText.setText(item.getNoteText());

            textGpcSeg.setText(String.valueOf(item.getTextGpcSeg()));
            textGpcSem.setText(String.valueOf(item.getTextGpcSem()));
            textGpcKl.setText(String.valueOf(item.getTextGpcKl()));
            textGpcBr.setText(String.valueOf(item.getTextGpcBr()));
            textOKRB.setText(item.getTextOKRB());
            textTHB.setText(item.getTextTHB());
            textGOST.setText(item.getTextGOST());

            setDataTable(item.getDataSize());
        }
    }

    private void createTableShkala(final Object[][] row) {
        tModel = new DefaultTableModel(row, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : super.getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0 || col == 1 || col == 2 || col == 3) return true;
                else return false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);

                    if (value != null) {
                        if (col == 1 || col == 2) {
                            rowVector.setElementAt(Integer.valueOf(value.toString()), col);

                            rowVector.setElementAt(
                                    getValue(String.valueOf(rowVector.get(1)),
                                            String.valueOf(rowVector.get(2)),
                                            buttonGroupBox.getSelection().getActionCommand(),
                                            box5.isSelected(),
                                            Integer.valueOf(buttonGroupBoxRzm2.getSelection().getActionCommand())),
                                    3);

                            fireTableCellUpdated(row, 3);
                        } else {
                            rowVector.setElementAt(value, col);
                        }
                    }

                    fireTableCellUpdated(row, col);
                } catch (NumberFormatException ee) {
                    JOptionPane.showMessageDialog(null,
                            "Введено некорректное значение: " + ee.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        tModel.addTableModelListener(e -> {
            if (tableModelListenerIsChanging) {
                return;
            }
            int firstRow = e.getFirstRow();
            int column = e.getColumn();

            if (column != 0 || maxSelectedRow == -1 || minSelectedRow == -1) {
                return;
            }
            tableModelListenerIsChanging = true;
            boolean value = ((Boolean) tModel.getValueAt(firstRow, column));
            for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                tModel.setValueAt(value, table.convertRowIndexToModel(i), column);
            }

            minSelectedRow = -1;
            maxSelectedRow = -1;

            tableModelListenerIsChanging = false;
        });

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(20);
        table.getColumnModel().getColumn(2).setPreferredWidth(20);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }

    private void setDataTable(ArrayList<EanItemListSize> listSizes) {
        try {
            tModel.getDataVector().removeAllElements();
            revalidate();

            for (EanItemListSize size : listSizes) {
                tModel
                        .addRow(new Object[]{
                                false,
                                size.getRst(),
                                size.getRzm(),
                                size.getSizePrint()});
            }
        } catch (Exception e) {

        }
    }

    private void setVidFas(String vid) {
        try {
            switch (Integer.valueOf(vid)) {
                case 0:
                    jRadioButton4.setSelected(true);
                    break;
                case 1:
                    jRadioButton1.setSelected(true);
                    break;
                case 2:
                    jRadioButton2.setSelected(true);
                    break;
                case 3:
                    jRadioButton3.setSelected(true);
                    break;
                default:
                    jRadioButton4.setSelected(true);
                    break;
            }
        } catch (NumberFormatException e) {
            jRadioButton4.setSelected(true);

            JOptionPane.showMessageDialog(null,
                    "Введено некорректное значение: " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setSrtFas(String srt) {
        try {
            switch (Integer.valueOf(srt)) {
                case 1:
                    fasSrt1.setSelected(true);
                    break;
                case 2:
                    fasSrt2.setSelected(true);
                    break;
                case 3:
                    fasSrt3.setSelected(true);
                    break;
                case 4:
                    fasSrt4.setSelected(true);
                    break;
                default:
                    fasSrt1.setSelected(true);
                    break;
            }
        } catch (NumberFormatException e) {
            fasSrt1.setSelected(true);

            JOptionPane.showMessageDialog(null,
                    "Введено некорректное значение: " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Корректировка записи размера на этикетке.
     * @param rst рост;
     * @param rzm размер;
     * @param type тип;
     * @param raz2 2-е значение размера;
     * @param value параметр(+6; -10) 2-го значения размера;
     * @return запись размера на этикетке
     */
    private String getValue(String rst, String rzm, String type, boolean raz2, int value) {
        String str = "";

        try {
            switch (Integer.valueOf(type)) {
                case 0: // рост - размер
                    str = rst + "-" + rzm;
                    break;
                case 1: // диапазон роста - размер
                    str = rst + "," + (Integer.valueOf(rst) + 6) + "-" + rzm;
                    break;
                case 2: // размер - диапазон роста
                    str = rzm + "-" + rst + "," + (Integer.valueOf(rst) + 6);
                    break;
                case 3: // без роста - диапазон размера
                    str = rzm + "-" + (Integer.valueOf(rzm) + 1);
                    break;
                case 4:  // без роста - размер
                    str = rzm;
                    break;
                default:
                    str = rst + "-" + rzm;
                    break;
            }

            if (raz2) {
                if (Integer.valueOf(type) == 2)
                    // размер - (обхват бедер) - диапазон роста
                    str = rzm + "-" + (Integer.valueOf(rzm) + value) + "-" + rst + "," + (Integer.valueOf(rst) + 6);
                else
                    str = str + "-" + (Integer.valueOf(rzm) + value);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Введено некорректное значение: " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return str;
    }

    private void setConstants(String type, EanItem item) {
        if (type.equals(UtilEan.ADD)) {
            typeEanListItem = UtilEan.ADD;
            flagEdit = false;
            idItemEdit = -1;
            flagItemEdit = 0;

        } else if (type.equals(UtilEan.EDIT)) {
            typeEanListItem = UtilEan.EDIT;
            flagEdit = true;
            idItemEdit = item.getId();
            flagItemEdit = item.getFlag();

        } else if (type.equals(UtilEan.COPY)) {
            typeEanListItem = UtilEan.COPY;
            flagEdit = false;
            idItemEdit = -1;
            flagItemEdit = 0;
        }
    }
}
