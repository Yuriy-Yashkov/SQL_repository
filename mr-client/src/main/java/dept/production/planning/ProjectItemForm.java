package dept.production.planning;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import dept.production.zsh.spec.SpecForm;
import dept.production.zsh.spec.UtilSpec;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class ProjectItemForm extends javax.swing.JDialog {

    private static final Logger log = Logger.getLogger(ProjectItemForm.class.getName());

    User user = User.getInstance();
    private JButton buttClose;
    private JButton buttSaveItem;
    private JPanel osnova;
    private JPanel centerPanel;
    private JPanel buttPanel;
    private JTextField fasNum;
    private JTextField fasName;
    private JTextField sarText;
    private JTextField narText;
    private JTextField convText;
    private JTextField kolXText;
    private JTextPane noteText;
    private JButton buttFasSearch;
    private JLabel specNum;
    private JLabel specName;
    private JLabel specNorm;
    private JButton buttSpecSearch;
    private JLabel fasKolvo;
    private ButtonGroup buttonGroupFasVid;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JTable tableSostav;
    private JTable tableShkala;
    private JTable tableColor;
    private JCheckBox shPrint;
    private JCheckBox vyshivka;
    private JCheckBox fasNew;
    private JScrollPane scSostav;
    private JScrollPane scShkala;
    private JScrollPane scColor;
    private JButton buttShkalaPlus;
    private JButton buttShkalaMinus;
    private JButton buttSostavPlus;
    private JButton buttSostavMinus;
    private JButton buttColorPlus;
    private JButton buttColorMinus;
    private JPanel buttShkalaPanel;
    private JPanel buttSostavPanel;
    private JPanel buttColorPanel;
    private Vector colSstv;
    private Vector colClr;
    private Vector colShkl;
    private DefaultTableModel tModelShkala;
    private JPanel buttEditShkalaPanel;
    private JButton buttEditKolFas;
    private JButton buttEditKolDekad;
    private JTextField kolvoFasText;
    private JTextField kolvoDekadText;
    private DefaultTableModel tModelColor;
    private JPanel panelBoxTechLoadKol;
    private JCheckBox loadDek1;
    private JCheckBox loadDek2;
    private JCheckBox loadDek3;
    private ButtonGroup buttonGroupKol;
    private JRadioButton jRadioButton4;
    private JRadioButton jRadioButton5;
    private JPanel panelBox;
    private JButton buttSostavMod16;
    private JButton buttSostavRashod;
    private DefaultTableModel tModelSostav;
    private ProgressBar pb;
    private Vector dataDBF;
    private PlanPDB ppdb;
    private boolean flagEdit = false;
    private Vector updateItem;
    private JRadioButton jRadioButton6;
    private MainController controller;
    private ProjectDetalForm projForm;

    public ProjectItemForm(MainController mainController, ProjectDetalForm parent, boolean modal) {
        super(parent, modal);
        setTitle("Добавить новую запись");
        controller = mainController;

        flagEdit = false;
        projForm = (ProjectDetalForm) parent;
        ppdb = parent.ppdb;

        UtilPlan.EDIT_BUTT_ACTION = false;

        init();

        createTableShkala(new Vector());
        createTableSostav(new Vector());
        createTableColor(new Vector());

        updateSumVypusk();

        setLocationRelativeTo(parent);
        setVisible(true);
    }


    public ProjectItemForm(MainController mainController, ProjectDetalForm parent, boolean modal,
                           Vector data, Vector dataShkala, Vector dataSostav, Vector dataColor, Vector updateItem) {
        super(parent, modal);
        controller = mainController;
        setTitle("Редактирование записи");

        flagEdit = true;

        projForm = (ProjectDetalForm) parent;
        ppdb = parent.ppdb;

        UtilPlan.EDIT_BUTT_ACTION = false;

        init();

        this.updateItem = updateItem;

        fasNum.setText(data.get(0).toString());
        fasName.setText(data.get(1).toString());

        shPrint.setSelected(data.get(3).toString().indexOf('1') != -1 ? true : false);
        vyshivka.setSelected(data.get(3).toString().indexOf('2') != -1 ? true : false);

        fasNew.setSelected(Boolean.valueOf(data.get(4).toString()));

        kolXText.setText(data.get(5).toString());
        specNum.setText(data.get(6).toString());
        specName.setText(data.get(7).toString());
        specNorm.setText(data.get(8).toString());
        convText.setText(data.get(9).toString());
        noteText.setText(data.get(10).toString());
        sarText.setText(data.get(11).toString());
        narText.setText(data.get(12).toString());
        switch (Integer.valueOf(data.get(2).toString())) {
            case 0:
                jRadioButton6.setSelected(true);
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
                jRadioButton6.setSelected(true);
                break;
        }

        createTableShkala(dataShkala);
        createTableSostav(dataSostav);
        createTableColor(dataColor);

        updateSumVypusk();

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(850, 550));
        setPreferredSize(new Dimension(900, 750));

        cleanConstants();

        osnova = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();
        buttShkalaPanel = new JPanel();
        buttEditShkalaPanel = new JPanel();
        buttSostavPanel = new JPanel();
        buttColorPanel = new JPanel();
        panelBoxTechLoadKol = new JPanel();
        panelBox = new JPanel();

        buttClose = new JButton("Закрыть");
        buttSaveItem = new JButton("Сохранить");
        buttFasSearch = new JButton("Найти");
        buttSpecSearch = new JButton("Найти");
        buttShkalaPlus = new JButton("+");
        buttShkalaMinus = new JButton("-");
        buttSostavPlus = new JButton("+");
        buttSostavMinus = new JButton("-");
        buttSostavMod16 = new JButton("мод.16");
        buttSostavRashod = new JButton("расход");
        buttColorPlus = new JButton("+");
        buttColorMinus = new JButton("-");
        buttEditKolFas = new JButton("изменить");
        buttEditKolDekad = new JButton("изменить");
        fasNum = new JTextField();
        fasName = new JTextField();
        sarText = new JTextField();
        narText = new JTextField();
        convText = new JTextField();
        kolXText = new JTextField("1");
        fasKolvo = new JLabel();
        specNum = new JLabel(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_ID));
        specName = new JLabel(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NAME));
        specNorm = new JLabel(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NORM));
        noteText = new JTextPane();
        kolvoFasText = new JTextField();
        kolvoDekadText = new JTextField();
        buttonGroupFasVid = new ButtonGroup();
        buttonGroupKol = new ButtonGroup();
        jRadioButton6 = new JRadioButton("нет");
        jRadioButton1 = new JRadioButton("муж.");
        jRadioButton2 = new JRadioButton("жен.");
        jRadioButton3 = new JRadioButton("дет.");
        jRadioButton4 = new JRadioButton("Разделить");
        jRadioButton5 = new JRadioButton("Вставить");
        tableSostav = new JTable();
        tableShkala = new JTable();
        tableColor = new JTable();
        tModelShkala = new DefaultTableModel();
        scSostav = new JScrollPane(tableSostav);
        scShkala = new JScrollPane(tableShkala);
        scColor = new JScrollPane(tableColor);
        fasNew = new JCheckBox();
        shPrint = new JCheckBox("шт.печать");
        vyshivka = new JCheckBox("вышивка");
        colSstv = new Vector();
        colClr = new Vector();
        colShkl = new Vector();
        updateItem = new Vector();
        loadDek1 = new JCheckBox("1-я декада;");
        loadDek2 = new JCheckBox("2-я декада;");
        loadDek3 = new JCheckBox("3-я декада;");

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new ParagraphLayout());
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttShkalaPanel.setLayout(new GridLayout(2, 0, 5, 5));
        buttShkalaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttEditShkalaPanel.setLayout(new ParagraphLayout());
        buttSostavPanel.setLayout(new GridLayout(4, 0, 5, 5));
        buttSostavPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttColorPanel.setLayout(new GridLayout(2, 0, 5, 5));
        buttColorPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelBoxTechLoadKol.setLayout(new GridLayout(3, 1, 5, 5));
        panelBox.setLayout(new GridLayout(1, 2, 5, 5));

        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        fasNum.setPreferredSize(new Dimension(150, 20));
        fasName.setPreferredSize(new Dimension(250, 20));
        sarText.setPreferredSize(new Dimension(90, 20));
        narText.setPreferredSize(new Dimension(110, 20));
        convText.setPreferredSize(new Dimension(90, 20));
        kolXText.setPreferredSize(new Dimension(30, 20));
        fasKolvo.setPreferredSize(new Dimension(130, 20));
        specNum.setPreferredSize(new Dimension(50, 20));
        specName.setPreferredSize(new Dimension(270, 20));
        specNorm.setPreferredSize(new Dimension(100, 20));
        noteText.setPreferredSize(new Dimension(320, 150));
        scShkala.setPreferredSize(new Dimension(300, 150));
        scSostav.setPreferredSize(new Dimension(350, 120));
        scColor.setPreferredSize(new Dimension(320, 150));
        kolvoFasText.setPreferredSize(new Dimension(70, 20));
        kolvoDekadText.setPreferredSize(new Dimension(70, 20));

        fasKolvo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        specNum.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        specName.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        specNorm.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        noteText.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание:"));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        shPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        vyshivka.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadDek1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadDek2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loadDek3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton5.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton6.setFont(new java.awt.Font("Dialog", 0, 13));
        shPrint.setFont(new java.awt.Font("Dialog", 0, 13));
        vyshivka.setFont(new java.awt.Font("Dialog", 0, 13));
        buttEditKolFas.setFont(new java.awt.Font("Dialog", 0, 13));
        buttEditKolDekad.setFont(new java.awt.Font("Dialog", 0, 13));
        loadDek1.setFont(new java.awt.Font("Dialog", 0, 13));
        loadDek2.setFont(new java.awt.Font("Dialog", 0, 13));
        loadDek3.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setActionCommand("1");
        jRadioButton2.setActionCommand("2");
        jRadioButton3.setActionCommand("3");
        jRadioButton6.setActionCommand("0");

        jRadioButton6.setSelected(true);
        loadDek1.setSelected(true);
        jRadioButton4.setSelected(true);

        tableShkala.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        tableShkala.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                tableShkala.editCellAt(tableShkala.getSelectedRow(), tableShkala.getSelectedColumn());
                Component editor = tableShkala.getEditorComponent();
                if (editor != null) {
                    editor.requestFocus();
                    if (editor instanceof JTextField) {
                        ((JTextField) editor).selectAll();
                    }
                }
            }
        });

        tableSostav.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        tableSostav.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                tableSostav.editCellAt(tableSostav.getSelectedRow(), tableSostav.getSelectedColumn());
                Component editor = tableSostav.getEditorComponent();
                if (editor != null) {
                    editor.requestFocus();
                    if (editor instanceof JTextField) {
                        ((JTextField) editor).selectAll();
                    }
                }
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        buttSaveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveItemActionPerformed(evt);
            }
        });

        fasNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttFasSearch.doClick();
            }
        });

        buttFasSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttFasSearchActionPerformed(evt);
            }
        });

        buttSpecSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSpecSearchActionPerformed(evt);
            }
        });

        buttShkalaPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttShkalaPlusActionPerformed(evt);
            }
        });

        buttShkalaMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttShkalaMinusActionPerformed(evt);
            }
        });

        buttEditKolFas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEditKolFasActionPerformed(evt);
            }
        });

        buttEditKolDekad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEditKolDekadActionPerformed(evt);
            }
        });

        buttSostavPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSostavPlusActionPerformed(evt);
            }
        });

        buttSostavMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSostavMinusActionPerformed(evt);
            }
        });

        buttSostavMod16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSostavMod16ActionPerformed(evt);
            }
        });

        buttSostavRashod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSostavRashodActionPerformed(evt);
            }
        });

        buttColorPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttColorPlusActionPerformed(evt);
            }
        });

        buttColorMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttColorMinusActionPerformed(evt);
            }
        });

        colSstv.add("");
        colSstv.add("Код");
        colSstv.add("Артикул");
        colSstv.add("Состав");
        colSstv.add("Признак");
        colSstv.add("Расход");

        colClr.add("");
        colClr.add("Код");
        colClr.add("Цвет");

        colShkl.add("");
        colShkl.add("Рост");
        colShkl.add("Размер");
        colShkl.add("Кол-во");
        colShkl.add("Д1(%)");
        colShkl.add("Д2(%)");
        colShkl.add("Д3(%)");

        buttonGroupFasVid.add(jRadioButton1);
        buttonGroupFasVid.add(jRadioButton2);
        buttonGroupFasVid.add(jRadioButton3);
        buttonGroupFasVid.add(jRadioButton6);

        buttonGroupKol.add(jRadioButton4);
        buttonGroupKol.add(jRadioButton5);

        buttShkalaPanel.add(buttShkalaPlus);
        buttShkalaPanel.add(buttShkalaMinus);

        buttSostavPanel.add(buttSostavPlus);
        buttSostavPanel.add(buttSostavMinus);
        buttSostavPanel.add(buttSostavMod16);
        buttSostavPanel.add(buttSostavRashod);

        buttColorPanel.add(buttColorPlus);
        buttColorPanel.add(buttColorMinus);

        panelBoxTechLoadKol.add(loadDek1);
        panelBoxTechLoadKol.add(loadDek2);
        panelBoxTechLoadKol.add(loadDek3);

        panelBox.add(jRadioButton4);
        panelBox.add(jRadioButton5);

        centerPanel.add(new JLabel("Модель:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(fasNum);
        centerPanel.add(fasName, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerPanel.add(buttFasSearch);
        centerPanel.add(new JLabel("    Новинка:"));
        centerPanel.add(fasNew);
        centerPanel.add(new JLabel("Вид:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(jRadioButton6);
        centerPanel.add(jRadioButton1);
        centerPanel.add(jRadioButton2);
        centerPanel.add(jRadioButton3);
        centerPanel.add(new JLabel("    Шифр:"));
        centerPanel.add(sarText);
        centerPanel.add(new JLabel("    Артикул:"));
        centerPanel.add(narText);
        centerPanel.add(new JLabel("    Дополнение:"));
        centerPanel.add(convText);
        centerPanel.add(new JLabel("Шкала:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(scShkala, ParagraphLayout.NEW_LINE_STRETCH_HV);
        centerPanel.add(buttShkalaPanel);
        centerPanel.add(new JLabel("Выпуск:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(fasKolvo);
        centerPanel.add(new JLabel("    Кол-во:"));
        centerPanel.add(kolvoFasText);
        centerPanel.add(buttEditKolFas);
        centerPanel.add(new JLabel("    Комплект:"));
        centerPanel.add(kolXText);
        centerPanel.add(new JLabel("    По декадам(%):"));
        centerPanel.add(kolvoDekadText);
        centerPanel.add(buttEditKolDekad);
        centerPanel.add(new JLabel("Состав:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(scSostav, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerPanel.add(buttSostavPanel);
        centerPanel.add(new JLabel("Декор:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(shPrint);
        centerPanel.add(vyshivka);
        centerPanel.add(new JLabel("    Спец-я:"));
        centerPanel.add(specNum);
        centerPanel.add(specName);
        centerPanel.add(specNorm);
        centerPanel.add(buttSpecSearch);
        centerPanel.add(new JLabel("Цвет:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(scColor);
        centerPanel.add(buttColorPanel);
        centerPanel.add(noteText, ParagraphLayout.NEW_LINE_STRETCH_H);

        buttPanel.add(buttClose);
        buttPanel.add(buttSaveItem);

        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttSaveItemActionPerformed(ActionEvent evt) {
        try {
            boolean saveFlag = true;
            String str = "";

            if (fasNum.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели модель!\n";
            }

            if (fasName.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели название модели!\n";
            }

            if (tModelShkala.getDataVector().size() <= 0) {
                saveFlag = false;
                str += "Вы не ввели шкалу размеров!\n";
            }

            Vector data = tModelShkala.getDataVector();
            for (int i = 0; i < data.size(); i++) {
                if (Double.valueOf(((Vector) data.get(i)).elementAt(4).toString()) +
                        Double.valueOf(((Vector) data.get(i)).elementAt(5).toString()) +
                        Double.valueOf(((Vector) data.get(i)).elementAt(6).toString()) != 100) {
                    saveFlag = false;
                    str += "Сумма выпуска " + ((Vector) data.get(i)).elementAt(1).toString() + "-" + ((Vector) data.get(i)).elementAt(2).toString() + " по декадам не 100%!\n";
                    break;
                }
            }

            try {
                if (!convText.getText().trim().equals(""))
                    Integer.valueOf(convText.getText().trim());
            } catch (Exception e) {
                saveFlag = false;
                str += "Конвейер задан некорректно!\n";
            }

            try {
                if (!sarText.getText().trim().equals(""))
                    Integer.valueOf(sarText.getText().trim());
            } catch (Exception e) {
                saveFlag = false;
                str += "Шифр задан некорректно!\n";
            }

            if (!saveFlag) {
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            if (saveFlag) {
                try {
                    if (ppdb.searchItemTempProjectTable(Integer.valueOf(fasNum.getText().trim()),
                            (!sarText.getText().trim().equals("")) ? Integer.valueOf(sarText.getText().trim()) : 0,
                            narText.getText().trim().toUpperCase())) {

                        Vector updateItem_ = new Vector();
                        updateItem_.add(fasNum.getText().trim());
                        updateItem_.add((!sarText.getText().trim().equals("")) ? sarText.getText().trim() : 0);
                        updateItem_.add(narText.getText().trim().toUpperCase());

                        if ((flagEdit && !updateItem.equals(updateItem_)) || !flagEdit) {
                            if (JOptionPane.showOptionDialog(null,
                                    "Модель с такими параметрами уже существует.\nИзменить её?",
                                    "Внимание ",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    new Object[]{"Да", "Нет"}, "Да") == JOptionPane.YES_OPTION) {

                                if (!flagEdit) {
                                    updateItem = updateItem_;
                                }
                                flagEdit = true;
                            } else {
                                saveFlag = false;
                            }
                        }
                    }
                } catch (Exception e) {
                    saveFlag = false;
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            if (saveFlag) {
                pb = new ProgressBar(ProjectItemForm.this, false, "Сохранение ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            if (flagEdit) {
                                if (ppdb.updateItemTempProjectTable(
                                        updateItem,
                                        Integer.valueOf(user.getIdEmployee()),
                                        Integer.valueOf(fasNum.getText().trim()),
                                        fasName.getText().trim().replace("  ", " ").replace("  ", " ").toUpperCase(),
                                        tModelShkala.getDataVector(),
                                        Integer.valueOf(buttonGroupFasVid.getSelection().getActionCommand()),
                                        shPrint.isSelected(),
                                        vyshivka.isSelected(),
                                        Integer.valueOf(kolXText.getText().trim()),
                                        fasNew.isSelected(),
                                        Integer.valueOf(specNum.getText().trim()),
                                        (!convText.getText().trim().equals("")) ? Integer.valueOf(convText.getText().trim()) : 0,
                                        (!sarText.getText().trim().equals("")) ? Integer.valueOf(sarText.getText().trim()) : 0,
                                        narText.getText().trim().toUpperCase(),
                                        tModelSostav.getDataVector(),
                                        tModelColor.getDataVector(),
                                        noteText.getText().trim())) {

                                    JOptionPane.showMessageDialog(null,
                                            "Запись успешно изменена! ",
                                            "Завершено",
                                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                    UtilPlan.EDIT_BUTT_ACTION = true;
                                    dispose();
                                }

                            } else {
                                if (ppdb.addItemTempProjectTable(
                                        Integer.valueOf(user.getIdEmployee()),
                                        Integer.valueOf(fasNum.getText().trim()),
                                        fasName.getText().trim().replace("  ", " ").replace("  ", " ").toUpperCase(),
                                        tModelShkala.getDataVector(),
                                        Integer.valueOf(buttonGroupFasVid.getSelection().getActionCommand()),
                                        shPrint.isSelected(),
                                        vyshivka.isSelected(),
                                        Integer.valueOf(kolXText.getText().trim()),
                                        fasNew.isSelected(),
                                        Integer.valueOf(specNum.getText().trim()),
                                        (!convText.getText().trim().equals("")) ? Integer.valueOf(convText.getText().trim()) : 0,
                                        (!sarText.getText().trim().equals("")) ? Integer.valueOf(sarText.getText().trim()) : 0,
                                        narText.getText().trim().toUpperCase(),
                                        tModelSostav.getDataVector(),
                                        tModelColor.getDataVector(),
                                        noteText.getText().trim())) {

                                    JOptionPane.showMessageDialog(null,
                                            "Запись успешно добавлена! ",
                                            "Завершено",
                                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                    UtilPlan.EDIT_BUTT_ACTION = true;
                                    dispose();
                                }
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttFasSearchActionPerformed(ActionEvent evt) {
        try {
            new ProjectSearchForm(controller, true, fasNum.getText().trim(), UtilPlan.EDIT_PROJ_TYPE_FAS);

            if (UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION) {
                fasNum.setText(UtilPlan.EDIT_ADD_PROJ_FAS_NUM);
                fasName.setText(UtilPlan.EDIT_ADD_PROJ_FAS_NAME);

                if (!UtilPlan.EDIT_ADD_PROJ_FAS_RST_RZM.isEmpty()) {

                    Vector data = tModelShkala.getDataVector();

                    for (int i = 0; i < data.size(); i++) {
                        ((Vector) data.get(i)).setElementAt(true, 0);
                    }

                    for (Object obj : UtilPlan.EDIT_ADD_PROJ_FAS_RST_RZM) {
                        Vector tmp = (Vector) obj;
                        if (!data.contains(obj))
                            data.add(tmp);
                    }

                    createTableShkala(data);

                }
            }
        } catch (Exception e) {
            fasNum.setText("");
            fasName.setText("");
            createTableShkala(new Vector());
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSpecSearchActionPerformed(ActionEvent evt) {
        try {
            if (!fasNum.getText().trim().isEmpty()) {
                UtilSpec.SPEC_BUTT_SELECT_ACTION = false;

                new SpecForm(controller, true, Integer.valueOf(fasNum.getText().trim()));

                if (UtilSpec.SPEC_BUTT_SELECT_ACTION) {
                    specNum.setText(String.valueOf(UtilSpec.SPEC_ID));
                    specName.setText(UtilSpec.SPEC_NAME);
                    specNorm.setText(String.valueOf(UtilSpec.SPEC_NORM));
                }
            }
        } catch (Exception e) {
            UtilSpec.SPEC_BUTT_SELECT_ACTION = false;
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttShkalaPlusActionPerformed(ActionEvent evt) {
        try {
            Vector tmp = new Vector();
            tmp.add(false);
            tmp.add(Integer.valueOf("0"));
            tmp.add(Integer.valueOf("0"));
            tmp.add(new Double(0));
            tmp.add(new Double(33.3));
            tmp.add(new Double(33.3));
            tmp.add(new Double(33.4));
            tModelShkala.insertRow(0, tmp);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttShkalaMinusActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(null, "Удалить отмеченные строки?", "Удаление ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Удалить", "Отмена"}, "Удалить") == JOptionPane.YES_OPTION) {
                for (int i = 0; i < tModelShkala.getDataVector().size(); i++) {
                    if (Boolean.valueOf(((Vector) tModelShkala.getDataVector().get(i)).elementAt(0).toString())) {
                        tModelShkala.getDataVector().remove(i);
                        i--;
                    }
                }

                updateSumVypusk();
                createTableShkala(tModelShkala.getDataVector());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditKolFasActionPerformed(ActionEvent evt) {
        try {
            if (!kolvoFasText.getText().trim().isEmpty()) {
                double kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(kolvoFasText.getText().trim().replace(",", ".")), 3));

                if (JOptionPane.showOptionDialog(null, panelBox, "Выпуск (шт.)", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Изменить", "Отмена"}, "Изменить") == JOptionPane.YES_OPTION) {

                    Vector data = tModelShkala.getDataVector();
                    if (jRadioButton4.isSelected()) {
                        int select = 0;
                        Vector rowS = new Vector();
                        double kolS = new Double(0);

                        for (int i = 0; i < data.size(); i++) {
                            if (Boolean.valueOf(((Vector) data.get(i)).elementAt(0).toString())) {
                                select += 1;
                            }
                        }

                        if (select > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                if (Boolean.valueOf(((Vector) data.get(i)).elementAt(0).toString())) {
                                    rowS.add(i);
                                }
                            }

                            if (rowS.size() > 1) {
                                for (int i = 0; i < rowS.size() - 1; i++) {
                                    ((Vector) data.get(i)).setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(kol / select), 4)), 3);
                                    kolS += Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(kol / select), 4));
                                }

                                ((Vector) data.get(rowS.size() - 1)).setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(kol - kolS), 4)), 3);

                            } else {
                                jRadioButton5.setSelected(true);
                            }

                        }
                    }

                    if (jRadioButton5.isSelected()) {
                        for (int i = 0; i < data.size(); i++) {
                            if (Boolean.valueOf(((Vector) data.get(i)).elementAt(0).toString())) {
                                ((Vector) data.get(i)).setElementAt(kol, 3);
                            }
                        }
                    }
                    updateSumVypusk();
                    createTableShkala(data);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditKolDekadActionPerformed(ActionEvent evt) {
        try {
            if (!kolvoDekadText.getText().trim().isEmpty()) {
                double kol = Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(kolvoDekadText.getText().trim().replace(",", ".")), 3));

                if (JOptionPane.showOptionDialog(null, panelBoxTechLoadKol, "Выпуск по декадам ", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Изменить", "Отмена"}, "Изменить") == JOptionPane.YES_OPTION) {

                    Vector data = tModelShkala.getDataVector();

                    for (int i = 0; i < data.size(); i++) {
                        if (Boolean.valueOf(((Vector) data.get(i)).elementAt(0).toString())) {
                            if (loadDek1.isSelected()) {
                                ((Vector) data.get(i)).setElementAt(kol, 4);
                            }
                            if (loadDek2.isSelected()) {
                                ((Vector) data.get(i)).setElementAt(kol, 5);
                            }
                            if (loadDek3.isSelected()) {
                                ((Vector) data.get(i)).setElementAt(kol, 6);
                            }
                        }
                    }
                    createTableShkala(data);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSostavPlusActionPerformed(ActionEvent evt) {
        try {
            new ProjectSearchForm(controller, true, UtilPlan.EDIT_PROJ_TYPE_SOSTAV);

            if (UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION) {
                Vector data = tModelSostav.getDataVector();

                for (int i = 0; i < data.size(); i++) {
                    ((Vector) data.get(i)).setElementAt(false, 0);
                }

                for (Object obj : UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV) {
                    Vector tmp = (Vector) obj;
                    if (!data.contains(obj))
                        data.add(tmp);
                }

                createTableSostav(data);
            }
        } catch (Exception e) {
            createTableSostav(tModelSostav.getDataVector());
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSostavMinusActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(null, "Удалить отмеченные строки?", "Удаление ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Удалить", "Отмена"}, "Удалить") == JOptionPane.YES_OPTION) {
                for (int i = 0; i < tModelSostav.getDataVector().size(); i++) {
                    if (Boolean.valueOf(((Vector) tModelSostav.getDataVector().get(i)).elementAt(0).toString())) {
                        tModelSostav.getDataVector().remove(i);
                        i--;
                    }
                }

                createTableSostav(tModelSostav.getDataVector());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSostavMod16ActionPerformed(ActionEvent evt) {
        try {
            dataDBF = loadSpravDBF(new File(UtilPlan.DBF_MODELS16), 1);

            if (!dataDBF.isEmpty()) {
                new ProjectSearchForm(controller, true, dataDBF, fasNum.getText().trim(), UtilPlan.EDIT_PROJ_TYPE_MODEL16);

            } else {
                if (JOptionPane.showOptionDialog(null, "В данном справочнике ничего не найдено.\nИскать в другом справочнике?",
                        "Поиск ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION) {

                    final JFileChooser fc = new JFileChooser(new File(UtilPlan.DBF_MODELS16));
                    fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
                    fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            if (f != null) {
                                if (f.isDirectory()) {
                                    return true;
                                }
                            }
                            if (f.getName().endsWith(".DBF")) {
                                return true;
                            }
                            if (f.getName().endsWith(".dbf")) {
                                return true;
                            }
                            return false;
                        }

                        @Override
                        public String getDescription() {
                            return "";
                        }
                    });
                    fc.setAcceptAllFileFilterUsed(false);

                    if (fc.showDialog(ProjectItemForm.this, null) == JFileChooser.APPROVE_OPTION) {
                        if (fc.getSelectedFile().exists()) {
                            if (fc.getSelectedFile().getName().toLowerCase().endsWith(".dbf")) {
                                UtilPlan.DBF_MODELS16 = fc.getSelectedFile().getPath();
                                dataDBF = loadSpravDBF(new File(UtilPlan.DBF_MODELS16), 1);

                                if (!dataDBF.isEmpty())
                                    new ProjectSearchForm(controller, true, dataDBF, fasNum.getText().trim(), UtilPlan.EDIT_PROJ_TYPE_MODEL16);

                                try {
                                    UtilFunctions.setSettingPropFile(UtilPlan.DBF_MODELS16, UtilPlan.SETTING_DBF_MODELS16);
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                UtilPlan.DBF_MODELS16 = "";
                                JOptionPane.showMessageDialog(null, "Некорректный файл!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            UtilPlan.DBF_MODELS16 = "";
                            JOptionPane.showMessageDialog(null, "Файл не существует!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            if (UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION) {
                Vector data = tModelSostav.getDataVector();

                for (int i = 0; i < data.size(); i++) {
                    ((Vector) data.get(i)).setElementAt(false, 0);
                }

                for (Object obj : UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV) {
                    Vector tmp = (Vector) obj;
                    if (!data.contains(obj))
                        data.add(tmp);
                }

                createTableSostav(data);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSostavRashodActionPerformed(ActionEvent evt) {
        try {
            UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV = new Vector();

            log.info(UtilPlan.DBF_RASHOD);

            dataDBF = loadSpravDBF(new File(UtilPlan.DBF_RASHOD), 2);

            if (!dataDBF.isEmpty()) {
                new ProjectSearchForm(controller, true, dataDBF, fasNum.getText().trim(), UtilPlan.EDIT_PROJ_TYPE_RASHOD, UtilPlan.DBF_RASHOD_DETAL);

            } else {
                if (JOptionPane.showOptionDialog(null, "В данном справочнике ничего не найдено.\nИскать в другом справочнике?",
                        "Поиск ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION) {

                    final JFileChooser fc = new JFileChooser(new File(UtilPlan.DBF_RASHOD));
                    fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
                    fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            if (f != null) {
                                if (f.isDirectory()) {
                                    return true;
                                }
                            }
                            if (f.getName().endsWith(".DBF")) {
                                return true;
                            }
                            if (f.getName().endsWith(".dbf")) {
                                return true;
                            }
                            return false;
                        }

                        @Override
                        public String getDescription() {
                            return "";
                        }
                    });
                    fc.setAcceptAllFileFilterUsed(false);

                    if (fc.showDialog(ProjectItemForm.this, null) == JFileChooser.APPROVE_OPTION) {
                        if (fc.getSelectedFile().exists()) {
                            if (fc.getSelectedFile().getName().toLowerCase().endsWith(".dbf")) {
                                UtilPlan.DBF_RASHOD = fc.getSelectedFile().getPath();
                                dataDBF = loadSpravDBF(new File(UtilPlan.DBF_RASHOD), 2);

                                if (!dataDBF.isEmpty())
                                    new ProjectSearchForm(controller, true, dataDBF, fasNum.getText().trim(), UtilPlan.EDIT_PROJ_TYPE_RASHOD, UtilPlan.DBF_RASHOD_DETAL);

                                try {
                                    UtilFunctions.setSettingPropFile(UtilPlan.DBF_RASHOD, UtilPlan.SETTING_DBF_RASKLAD);
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                UtilPlan.DBF_RASHOD = "";
                                JOptionPane.showMessageDialog(null, "Некорректный файл!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            UtilPlan.DBF_RASHOD = "";
                            JOptionPane.showMessageDialog(null, "Файл не существует!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            if (UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION) {
                Vector data = tModelSostav.getDataVector();

                for (int i = 0; i < data.size(); i++) {
                    ((Vector) data.get(i)).setElementAt(false, 0);
                }

                for (Object obj : UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV) {
                    Vector tmp = (Vector) obj;
                    if (!data.contains(obj))
                        data.add(tmp);
                }

                createTableSostav(data);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttColorPlusActionPerformed(ActionEvent evt) {
        try {
            new ProjectSearchForm(controller, true, UtilPlan.EDIT_PROJ_TYPE_COLOR);

            if (UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION) {
                Vector data = tModelColor.getDataVector();

                for (int i = 0; i < data.size(); i++) {
                    ((Vector) data.get(i)).setElementAt(false, 0);
                }

                for (Object obj : UtilPlan.EDIT_ADD_PROJ_FAS_COLOR) {
                    Vector tmp = (Vector) obj;
                    if (!data.contains(obj))
                        data.add(tmp);
                }

                createTableColor(data);
            }
        } catch (Exception e) {
            createTableColor(tModelColor.getDataVector());
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttColorMinusActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(null, "Удалить отмеченные строки?", "Удаление ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Удалить", "Отмена"}, "Удалить") == JOptionPane.YES_OPTION) {
                for (int i = 0; i < tModelColor.getDataVector().size(); i++) {
                    if (Boolean.valueOf(((Vector) tModelColor.getDataVector().get(i)).elementAt(0).toString())) {
                        tModelColor.getDataVector().remove(i);
                        i--;
                    }
                }

                createTableColor(tModelColor.getDataVector());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTableSostav(final Vector row) {
        tModelSostav = new DefaultTableModel(row, colSstv) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();

            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0 || col == 3 || col == 4 || col == 5) return true;
                else return false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);

                    if (value != null) {
                        if (col == 4) {
                            rowVector.setElementAt(Integer.valueOf(value.toString().trim()), col);
                        } else if (col == 5) {
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(value.toString().trim().replace(",", ".")), 3)), col);
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
        tableSostav.setModel(tModelSostav);
        tableSostav.getColumnModel().getColumn(0).setPreferredWidth(1);
        tableSostav.getColumnModel().getColumn(1).setPreferredWidth(10);
        tableSostav.getColumnModel().getColumn(2).setPreferredWidth(50);
        tableSostav.getColumnModel().getColumn(3).setPreferredWidth(200);
        tableSostav.getColumnModel().getColumn(4).setPreferredWidth(50);
        tableSostav.getColumnModel().getColumn(5).setPreferredWidth(50);

        TableRowSorter sorter = new TableRowSorter<TableModel>(tModelSostav);
        tableSostav.setRowSorter(sorter);

        tableSostav.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableSostav.getTableHeader(), 0, ""));
    }

    private void createTableColor(final Vector row) {
        tModelColor = new DefaultTableModel(row, colClr) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0) return true;
                else return false;
            }
        };
        tableColor.setModel(tModelColor);
        tableColor.setAutoCreateColumnsFromModel(true);
        tableColor.getColumnModel().getColumn(0).setPreferredWidth(10);
        tableColor.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableColor.getColumnModel().getColumn(2).setPreferredWidth(200);

        TableRowSorter sorter = new TableRowSorter<TableModel>(tModelColor);
        tableColor.setRowSorter(sorter);

        tableColor.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableColor.getTableHeader(), 0, ""));

    }

    private void createTableShkala(final Vector row) {
        tModelShkala = new DefaultTableModel(row, colShkl) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0 || col == 1 || col == 2) return true;
                else return false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);

                    if (value != null) {
                        if (col == 1 || col == 2) {
                            rowVector.setElementAt(Integer.valueOf(value.toString()), col);
                        } else if (col == 3) {
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(value.toString().trim().replace(",", ".")), 3)), col);
                            updateSumVypusk();
                        } else if (col == 4 || col == 5 || col == 6) {
                            rowVector.setElementAt(Double.valueOf(UtilFunctions.formatNorm(Double.valueOf(value.toString().trim().replace(",", ".")), 2)), col);
                        } else {
                            rowVector.setElementAt(value, col);
                        }
                    }

                    fireTableCellUpdated(row, col);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + ee.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        tableShkala.setModel(tModelShkala);

        TableRowSorter sorter = new TableRowSorter<TableModel>(tModelShkala);
        tableShkala.setRowSorter(sorter);

        tableShkala.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableShkala.getTableHeader(), 0, ""));
    }

    private void updateSumVypusk() {
        try {
            double sum = 0;
            for (int i = 0; i < tModelShkala.getDataVector().size(); i++) {
                sum += Double.valueOf(((Vector) tModelShkala.getDataVector().get(i)).elementAt(3).toString().trim().replace(",", "."));
            }
            fasKolvo.setText(UtilFunctions.formatNorm(sum, 3));
        } catch (Exception e) {
            fasKolvo.setText("0");
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    private void cleanConstants() {
        UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_ID = 1;
        UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NAME = "Спецификация отсутствует";
        UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NORM = 0;
        UtilPlan.EDIT_ADD_PROJ_BUTT_ACTION = false;
        UtilPlan.EDIT_ADD_PROJ_FAS_NUM = "";
        UtilPlan.EDIT_ADD_PROJ_FAS_NAME = "";
        UtilPlan.EDIT_ADD_PROJ_FAS_RST_RZM = new Vector();
        UtilPlan.EDIT_ADD_PROJ_FAS_COLOR = new Vector();
        UtilPlan.EDIT_ADD_PROJ_FAS_SOSTAV = new Vector();
    }

    private Vector loadSpravDBF(final File fname, final int type) {
        dataDBF = new Vector();
        try {
            if (fname.exists()) {
                if (fname.getName().toLowerCase().endsWith(".dbf")) {
                    pb = new ProgressBar(ProjectItemForm.this, false, "Загрузка справочника ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            PlanDBF pdbf = new PlanDBF();
                            try {
                                if (type == 1) {
                                    dataDBF = pdbf.readDBFModels16(fname.getPath());
                                } else if (type == 2) {
                                    if (!fasNum.getText().trim().equals("")) {
                                        dataDBF = pdbf.readDBFRasklad3(fname.getPath(), Integer.valueOf(fasNum.getText().trim()));
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Введите модель!", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                                    }
                                }
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

                } else {
                    dataDBF = new Vector();
                    JOptionPane.showMessageDialog(null, "Некорректный файл!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } else {
                dataDBF = new Vector();
                JOptionPane.showMessageDialog(null, "Файл,по пути: " + fname.getPath() + " и имени: " + fname.getName().toLowerCase() + ", не существует!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            dataDBF = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return dataDBF;
    }
}
