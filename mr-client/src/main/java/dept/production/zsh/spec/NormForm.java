package dept.production.zsh.spec;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.User;
import common.UtilFunctions;
import dept.sprav.model.ModelForm;
import dept.sprav.model.UtilModel;
import dept.sprav.tech.TechForm;
import dept.sprav.tech.UtilTech;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class NormForm extends JDialog {
    JButton buttClose;
    JButton buttSave;
    JButton buttPrint;
    JButton buttModel;
    JButton buttPlus;
    JButton buttMinus;
    JButton buttTech;
    JButton buttFormula;
    JButton buttOper;
    JDateChooser sStdate;
    JComboBox dept;
    JLabel userEdit;
    JLabel brigNorm;
    JLabel sInsDate;
    JLabel title;
    JLabel modelNum;
    JLabel modelNaim;
    JPanel osnova;
    JPanel titlePanel;
    JPanel buttPanel;
    JPanel buttEastPanel;
    JPanel upPanel;
    JPanel centerPanel;
    JTable tableAll;
    JTable tableTech;
    JTextField spec;
    int specNum;
    JCheckBox chBoxOtk;
    JCheckBox chbBasic;

    boolean COPY = false;
    DefaultTableModel tModelAll;
    DefaultTableModel tModelTech;
    Vector colAll;
    Vector colTech;

    Vector updateTable;
    Vector updateNorm;

    SpecPDB spdb;

    User user = User.getInstance();
    private MainController controller;

    public NormForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Нормы выработки и времени");
        COPY = false;
        title = new JLabel("Формирование спецификации модели");

        init();

        buttSave.setText(UtilSpec.BUTTON_ADD);
        chBoxOtk.setVisible(true);

        dept.setSelectedItem(UtilSpec.getDept(UtilSpec.DEPT_MODEL, UtilSpec.DEPT_SELECT_ITEM));
        userEdit.setText(user.getFio());

        createTableNorm(new Vector(), new Vector());
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public NormForm(MainController mainController, boolean modal, Vector dataSpec, Vector dataItem, Vector dataTech, boolean flagCopy) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Нормы выработки и времени");
        COPY = flagCopy;
        title = new JLabel(COPY ? "Формирование спецификации" : "Корректировка спецификации № " + dataSpec.get(0));
        init();

        buttSave.setText(COPY ? UtilSpec.BUTTON_ADD : UtilSpec.BUTTON_SAVE);
        if (buttSave.getText().equals(UtilSpec.BUTTON_SAVE)) {
            colAll.add("ID item");

            for (Iterator it = dataItem.iterator(); it.hasNext(); ) {
                Vector obj = (Vector) it.next();

                updateTable.add(obj);
                updateNorm.add(obj.get(7));
            }
        }

        chBoxOtk.setVisible(true);

        try {
            specNum = Integer.valueOf(dataSpec.get(0).toString());
            modelNum.setText(dataSpec.get(1).toString());
            modelNaim.setText(dataSpec.get(2).toString());
            dept.setSelectedItem(UtilSpec.getDept(UtilSpec.DEPT_MODEL, Integer.valueOf(dataSpec.get(3).toString())));
            spec.setText(dataSpec.get(4).toString());
            if (!COPY) {
                userEdit.setText(dataSpec.get(6).toString());
                sStdate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataSpec.get(5).toString()));
                if (dataSpec.get(8) != null) {
                    chbBasic.setSelected((Boolean) dataSpec.get(8));
                } else {
                    chbBasic.setSelected(false);
                }
                sInsDate.setText(dataSpec.get(7).toString());
            } else
                userEdit.setText(user.getFio());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Данные спецификаций не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createTableNorm(dataItem, dataTech);
        setNormBrig();

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public NormForm(MainController mainController, boolean modal, Vector dataSpec, Vector dataItem, Vector dataTech) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Нормы выработки и времени");
        title = new JLabel("Cпецификация № " + dataSpec.get(0));
        init();

        try {
            sStdate.setEnabled(false);
            spec.setEditable(false);
            tableAll.setEnabled(false);
            tableTech.setEnabled(false);
            dept.setEnabled(false);

            buttModel.setVisible(false);
            buttEastPanel.setVisible(false);

            buttPanel.removeAll();
            buttPanel.add(buttClose);
            buttPanel.add(buttPrint);
            buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            dept.setSelectedItem(UtilSpec.getDept(UtilSpec.DEPT_MODEL, Integer.valueOf(dataSpec.get(3).toString())));
            specNum = Integer.valueOf(dataSpec.get(0).toString());
            modelNum.setText(dataSpec.get(1).toString());
            modelNaim.setText(dataSpec.get(2).toString());
            spec.setText(dataSpec.get(4).toString());
            userEdit.setText(dataSpec.get(6).toString());
            sStdate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataSpec.get(5).toString()));
            sInsDate.setText(dataSpec.get(7).toString());

            if (dataSpec.get(8) != null) {
                chbBasic.setSelected((Boolean) dataSpec.get(8));
            } else {
                chbBasic.setSelected(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Данные спецификаций не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createTableNorm(dataItem, dataTech);
        setNormBrig();

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(700, 600));
        setPreferredSize(new Dimension(980, 700));

        osnova = new JPanel();
        titlePanel = new JPanel();
        upPanel = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();
        buttEastPanel = new JPanel();

        sStdate = new JDateChooser();
        buttClose = new JButton("Закрыть");
        buttSave = new JButton();
        buttPrint = new JButton("Печать");
        buttModel = new JButton("Найти");
        buttPlus = new JButton("+");
        buttMinus = new JButton("-");
        buttOper = new JButton("OП");
        buttTech = new JButton("OБ");
        buttFormula = new JButton("Ф");
        colAll = new Vector();
        colTech = new Vector();
        updateTable = new Vector();
        updateNorm = new Vector();
        tableAll = new JTable();
        tableTech = new JTable();
        sInsDate = new JLabel();
        modelNum = new JLabel();
        modelNaim = new JLabel();
        brigNorm = new JLabel(UtilSpec.formatNorm(new Double(0)));
        userEdit = new JLabel();
        dept = new JComboBox(UtilSpec.DEPT_ITEMS);
        spec = new JTextField();
        chBoxOtk = new JCheckBox(" Сформировать нормы для ОТК");
        chBoxOtk.setVisible(false);
        chbBasic = new JCheckBox(" Пометить спецификацию, как основную для модели");
        chBoxOtk.setVisible(false);
        chbBasic.setForeground(Color.BLUE);

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.setLayout(new ParagraphLayout());
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttEastPanel.setLayout(new GridLayout(5, 0, 5, 5));
        buttEastPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dept.setPreferredSize(new Dimension(300, 20));
        modelNum.setPreferredSize(new Dimension(100, 20));
        modelNaim.setPreferredSize(new Dimension(400, 20));
        sStdate.setPreferredSize(new Dimension(120, 20));
        sInsDate.setPreferredSize(new Dimension(150, 20));
        buttClose.setPreferredSize(new Dimension(50, 20));
        spec.setPreferredSize(new Dimension(580, 20));
        brigNorm.setPreferredSize(new Dimension(150, 20));
        userEdit.setPreferredSize(new Dimension(350, 20));

        tableAll.setPreferredScrollableViewportSize(new Dimension(0, 150));
        tableTech.setPreferredScrollableViewportSize(new Dimension(0, 100));

        modelNum.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        modelNaim.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        sInsDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        brigNorm.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        userEdit.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        tableAll.setAutoCreateColumnsFromModel(true);
        tableTech.setAutoCreateColumnsFromModel(true);

        sStdate.setDate((Calendar.getInstance()).getTime());
        sInsDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));

        brigNorm.setHorizontalAlignment(JLabel.RIGHT);

        tableAll.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        tableAll.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int colum = tableAll.getSelectedColumn();
                if (colum == 2 || colum == 3) {
                    buttTech.doClick();
                } else {
                    tableAll.editCellAt(tableAll.getSelectedRow(), tableAll.getSelectedColumn());
                    Component editor = tableAll.getEditorComponent();
                    if (editor != null) {
                        editor.requestFocus();
                        if (editor instanceof JTextField) {
                            ((JTextField) editor).selectAll();
                        }
                    }
                }
            }
        });

        buttModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ModelForm(controller, true, true);
                modelNum.setText(UtilModel.MODEL);
                modelNaim.setText(UtilModel.NAIM);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
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

        buttTech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTechActionPerformed(evt);
            }
        });

        buttFormula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttFormulaActionPerformed(evt);
            }
        });

        buttOper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOperActionPerformed(evt);
            }
        });

        colAll.add("№");
        colAll.add("Операция");
        colAll.add("ID ОБ");
        colAll.add("Оборудование");
        colAll.add("Р/Д");
        colAll.add("Норма ед. шт.");
        colAll.add("Норма времени");

        colTech.add("ID ОБ");
        colTech.add("Oборудование");
        colTech.add("Норма времени");

        titlePanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(dept);
        titlePanel.add(new JLabel("      "));
        titlePanel.add(chBoxOtk);
        titlePanel.add(new JLabel("Модель:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(modelNum);
        titlePanel.add(modelNaim);
        titlePanel.add(buttModel);
        titlePanel.add(new JLabel("Спецификация:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(spec);
        titlePanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(sStdate);
        titlePanel.add(new JLabel("     Пользователь: "));
        titlePanel.add(userEdit);
        //titlePanel.add(new JLabel(" Дата корректировки:"));
        //titlePanel.add(sInsDate);
        titlePanel.add(new JLabel("Бриг. норма:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(brigNorm);
        titlePanel.add(chbBasic);

        upPanel.add(title, BorderLayout.NORTH);
        upPanel.add(titlePanel, BorderLayout.CENTER);

        buttEastPanel.add(buttPlus);
        buttEastPanel.add(buttMinus);
        buttEastPanel.add(buttOper);
        buttEastPanel.add(buttTech);
        buttEastPanel.add(buttFormula);

        centerPanel.add(new JScrollPane(tableAll), BorderLayout.CENTER);
        centerPanel.add(new JScrollPane(tableTech), BorderLayout.SOUTH);
        centerPanel.add(buttEastPanel, BorderLayout.EAST);

        buttPanel.add(buttClose);
        buttPanel.add(buttSave);
        buttPanel.add(buttPrint);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            SpecOO oo = new SpecOO(spec.getText(),
                    new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate()),
                    new BigDecimal(Double.valueOf(brigNorm.getText().toString())).setScale(UtilSpec.ROUNDING_NORM, RoundingMode.HALF_UP).doubleValue(),
                    tModelAll.getDataVector(),
                    tModelTech.getDataVector(),
                    ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId());
            oo.createReport("SpecТр-2.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPlusActionPerformed(ActionEvent evt) {
        Vector data = new Vector();
        data.add(numRowTableAll());
        data.add("");
        data.add(new Integer(-1));
        data.add("-");
        data.add(new Integer(1));
        data.add(new Integer(0));
        data.add(UtilSpec.formatNorm(new Double(0)));
        tModelAll.insertRow(tableAll.getSelectedRow() != -1 ? tableAll.getSelectedRow() : tableAll.getRowCount(), data);
    }

    private void buttMinusActionPerformed(ActionEvent evt) {
        try {
            if (tableAll.getSelectedRow() != -1) {
                if (Integer.valueOf(tableAll.getValueAt(tableAll.getSelectedRow(), 2).toString()) != -1)
                    tModelTechRemoveRow();

                tModelAll.removeRow(tableAll.getSelectedRow());
                setNormTech();
                setNormBrig();
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку для удаления!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTechActionPerformed(ActionEvent evt) {
        try {
            if (tableAll.getSelectedRow() != -1) {
                // System.out.println("Конструктор справлочника");
                new TechForm(controller, true);

                if (UtilTech.ID != -1) {
                    tModelTechRemoveRow();

                    tableAll.setValueAt(UtilTech.ID, tableAll.getSelectedRow(), 2);
                    tableAll.setValueAt(UtilTech.TECH, tableAll.getSelectedRow(), 3);

                    boolean copyItem = true;
                    for (int i = 0; i < tableTech.getRowCount(); i++) {
                        if (Integer.valueOf(tableTech.getValueAt(i, 0).toString()) == UtilTech.ID) {
                            copyItem = false;
                            break;
                        }
                    }

                    if (UtilTech.ID == -1)
                        copyItem = false;

                    if (copyItem) {
                        tModelTech.insertRow(tableTech.getRowCount(), new Vector());
                        tableTech.setValueAt(UtilTech.ID, tableTech.getRowCount() - 1, 0);
                        tableTech.setValueAt(UtilTech.TECH, tableTech.getRowCount() - 1, 1);
                    }
                }
                if (UtilTech.TECH.equals("-")) {
                    tModelTechRemoveRow();

                    tableAll.setValueAt(UtilTech.ID, tableAll.getSelectedRow(), 2);
                    tableAll.setValueAt(UtilTech.TECH, tableAll.getSelectedRow(), 3);
                }

                setNormTech();
                setNormBrig();
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку для оборудования!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttFormulaActionPerformed(ActionEvent evt) {
        try {
            if (tableAll.getSelectedRow() != -1) {
                UtilSpec.FORMULA_NORM = UtilSpec.formatNorm(Double.valueOf(tableAll.getValueAt(tableAll.getSelectedRow(), 6).toString()));
                new FormulaForm(this, true);
                tableAll.setValueAt(UtilSpec.FORMULA_NORM, tableAll.getSelectedRow(), 6);

                setNormTech();
                setNormBrig();
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку для формулы!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOperActionPerformed(ActionEvent evt) {
        if (tableAll.getSelectedRow() != -1) {
            new OperacForm(this, true, ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId());
            tableAll.setValueAt(UtilSpec.OPERAC, tableAll.getSelectedRow(), 1);
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали строку для операции!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            UtilSpec.SPEC_BUTT_SAVE_ACTION = false; // Кнопка сохранения не нажата

            boolean saveFlag = true;
            String str = "";
            Vector deleteItem = new Vector();

            if (((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId() == -1) {
                saveFlag = false;
                str += "Вы не выбрали цех!\n";
            }

            if (modelNum.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не выбрали модель!\n";
            }

            if (spec.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели наименование спецификации!\n";
            }

            if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate())))
                saveFlag = false;

            if (tModelAll.getRowCount() < 1) {
                saveFlag = false;
                str += "Вы не заполнили таблицу спецификации!\n";
            }

            int kolTechNull = 0;
            for (Iterator it = tModelAll.getDataVector().iterator(); it.hasNext(); ) {
                Vector vrow = (Vector) it.next();
                if (Integer.valueOf(vrow.get(2).toString()) == -1 && !vrow.get(3).toString().equals("-")) {
                    saveFlag = false;
                    str += "В таблице не заполнен столбец оборудование!\n";
                    break;
                }
                if (Integer.valueOf(vrow.get(2).toString()) == -1 && vrow.get(3).toString().equals("-")) {
                    kolTechNull++;
                }
            }

            if (kolTechNull == tModelAll.getRowCount()) {
                //  saveFlag = false;
                str += "Бригадная норма не рассчитана!\n";
            }

            if (!saveFlag)
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

            if (saveFlag) {
                //Делаем спецификацию для ОТК
                if (chBoxOtk.isVisible() && chBoxOtk.isSelected()) {
                    Vector tmp = new Vector();
                    tmp.add(Integer.valueOf(modelNum.getText().trim()));
                    tmp.add(spec.getText().trim());

                    Vector item = new Vector();
                    item.add(tmp);

                    new NormAddMoreForm(controller, true, UtilSpec.ID_DEPT_OTK, item);

                    //помечаем DEPT_SELECT_ITEM выранным цехом из активного окна
                    UtilSpec.DEPT_SELECT_ITEM = ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId();

                    //помечаем что кнопка сохранить для активного окна не нажата
                    UtilSpec.SPEC_BUTT_SAVE_ACTION = false;
                }

                //Сохранение основной спецификации
                UtilSpec.SPEC_BUTT_SAVE_ACTION = true; // Кнопка сохранения нажата

                try {
                    spdb = new SpecPDB();

                    //Сохранеие спецификации после коррект.
                    if (evt.getActionCommand().equals(UtilSpec.BUTTON_SAVE)) {
                        int tempKolList = 0;
                        String tempOperac = "";
                        Vector editNorm = new Vector();

                        for (Iterator it = tModelAll.getDataVector().iterator(); it.hasNext(); ) {
                            Object obj = ((Vector) it.next()).get(7);
                            if (obj != null)
                                editNorm.add(obj);
                        }

                        for (Iterator it = updateNorm.iterator(); it.hasNext(); ) {
                            Object objUpdateNorm = it.next();

                            if (!editNorm.contains(objUpdateNorm)) {
                                tempKolList = spdb.searchListItem(Integer.valueOf(objUpdateNorm.toString()));

                                if (tempKolList > 0) {
                                    tempOperac = "";
                                    for (Iterator it1 = updateTable.iterator(); it1.hasNext(); ) {
                                        Vector objUpdateTable = (Vector) it1.next();
                                        if (objUpdateTable.get(7).equals(objUpdateNorm)) {
                                            tempOperac = objUpdateTable.get(0) + ", " + objUpdateTable.get(1) + ", " +
                                                    objUpdateTable.get(5) + ", " + objUpdateTable.get(6);
                                            break;
                                        }
                                    }
                                    saveFlag = false;
                                    str += !tempOperac.equals("") ?
                                            "Удаление операции " + tempOperac + " не возможно!\n" +
                                                    "Она используется в " + tempKolList + " листках запуска!\n" +
                                                    "Начните редактирование спецификации сначала!" :
                                            "Ошибка подсчета!";
                                    break;
                                } else
                                    deleteItem.add(objUpdateNorm);
                            }
                        }

                        if (!saveFlag)
                            JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                        else {
                            if (spdb.updateSpec(specNum, ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId(),
                                    Integer.valueOf(modelNum.getText().trim()),
                                    Integer.valueOf(user.getIdEmployee()),
                                    spec.getText().trim(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate())),
                                    tModelAll.getDataVector(),
                                    deleteItem, chbBasic.isSelected())) {

                                JOptionPane.showMessageDialog(null,
                                        "Спецификация успешно сохранена под №" + specNum + "!",
                                        "Завершено",
                                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                //записываем выбранный цех в настройки
                                UtilFunctions.setSettingPropFile(
                                        String.valueOf(((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId()),
                                        UtilSpec.SETTING_DEPT_SELECT_ITEM);
                                dispose();
                            } else
                                JOptionPane.showMessageDialog(null,
                                        "Произошла ошибка.\nСпецификация под №" + specNum + " не сохранена!",
                                        "Завершено",
                                        javax.swing.JOptionPane.WARNING_MESSAGE);

                        }
                    } else
                        //Сохранеие новой спецификации
                        if (evt.getActionCommand().equals(UtilSpec.BUTTON_ADD)) {
                            if (spdb.addSpec(((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId(),
                                    Integer.valueOf(modelNum.getText().trim()),
                                    Integer.valueOf(user.getIdEmployee()),
                                    spec.getText().toString().trim(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate())),
                                    tModelAll.getDataVector(), chbBasic.isSelected())) {

                                JOptionPane.showMessageDialog(null, "Спецификация успешно добавлена! ",
                                        "Завершено",
                                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                //записываем выбранный цех в настройки
                                UtilFunctions.setSettingPropFile(
                                        String.valueOf(((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId()),
                                        UtilSpec.SETTING_DEPT_SELECT_ITEM);

                                dispose();
                            } else
                                JOptionPane.showMessageDialog(null,
                                        "Произошла ошибка.\nСпецификация не добавлена!",
                                        "Завершено",
                                        javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка. " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    spdb.disConn();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTableNorm(Vector rowAll, Vector rowTech) {
        tModelAll = new DefaultTableModel(rowAll, colAll) {

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 2 || col == 3) return false;
                return true;
            }

            public void setValueAt(Object value, int row, int col) {
                try {
                    if (!value.equals("") && value != null) {
                        Vector rowVector = (Vector) dataVector.elementAt(row);
                        switch (col) {
                            case 0:
                                rowVector.setElementAt(Integer.valueOf(value.toString()).intValue(), col);
                                break;
                            case 4:
                                rowVector.setElementAt(Integer.valueOf(value.toString()).intValue(), col);
                                break;
                            case 5:
                                rowVector.setElementAt(Integer.valueOf(value.toString()).intValue(), col);
                                rowVector.setElementAt(UtilSpec.formatNorm(Double.valueOf(value.toString()) != 0 ? 8 / Double.valueOf(value.toString()) : Double.valueOf(value.toString())), col + 1);
                                fireTableCellUpdated(row, col + 1);
                                setNormTech();
                                setNormBrig();
                                break;
                            case 6:
                                value = value.toString().replace(",", ".");
                                rowVector.setElementAt(UtilSpec.formatNorm(Double.valueOf(value.toString()).doubleValue()), col);
                                rowVector.setElementAt(new BigDecimal(Double.valueOf(value.toString()) != 0 ? 8 / Double.valueOf(value.toString()) : Double.valueOf(value.toString())).setScale(0, RoundingMode.HALF_UP).intValue(), col - 1);
                                fireTableCellUpdated(row, col - 1);
                                setNormTech();
                                setNormBrig();
                                break;
                            default:
                                rowVector.setElementAt(value, col);
                                break;

                        }
                    }
                    fireTableCellUpdated(row, col);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + ee.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        tableAll.setModel(tModelAll);
        tableAll.getColumnModel().getColumn(0).setPreferredWidth(1);
        tableAll.getColumnModel().getColumn(1).setPreferredWidth(350);
        tableAll.getColumnModel().getColumn(2).setMinWidth(0);
        tableAll.getColumnModel().getColumn(2).setMaxWidth(0);
        tableAll.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableAll.getColumnModel().getColumn(4).setPreferredWidth(1);
        tableAll.getColumnModel().getColumn(5).setPreferredWidth(50);
        tableAll.getColumnModel().getColumn(6).setPreferredWidth(80);
        if (buttSave.getText().equals(UtilSpec.BUTTON_SAVE)) {
            tableAll.getColumnModel().getColumn(7).setMinWidth(0);
            tableAll.getColumnModel().getColumn(7).setMaxWidth(0);
        }

        tModelTech = new DefaultTableModel(rowTech, colTech) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            public void setValueAt(Object value, int row, int col) {
                try {
                    if (!value.equals("") && value != null) {
                        Vector rowVector = (Vector) dataVector.elementAt(row);
                        if (col == 2)
                            rowVector.setElementAt(UtilSpec.formatNorm(Double.valueOf(value.toString())), col);
                        else
                            rowVector.setElementAt(value, col);
                    }
                    fireTableCellUpdated(row, col);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + ee.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        tableTech.setModel(tModelTech);
        tableTech.getColumnModel().getColumn(0).setMinWidth(0);
        tableTech.getColumnModel().getColumn(0).setMaxWidth(0);
        tableTech.getColumnModel().getColumn(1).setPreferredWidth(260);
        tableTech.getColumnModel().getColumn(2).setPreferredWidth(60);
    }

    private void setNormTech() {
        try {
            for (int i = 0; i < tableTech.getRowCount(); i++) {
                double tNorm = new Double(0);

                for (int j = 0; j < tableAll.getRowCount(); j++) {
                    if (Integer.valueOf(tableAll.getValueAt(j, 2).toString()) != -1) {
                        if (Integer.valueOf(tableTech.getValueAt(i, 0).toString()) == Integer.valueOf(tableAll.getValueAt(j, 2).toString()) && !tableAll.getValueAt(j, 6).equals("")) {
                            tNorm += Double.valueOf(tableAll.getValueAt(j, 6).toString()).doubleValue();
                        }
                    }
                }
                tableTech.setValueAt(tNorm, i, 2);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setNormBrig() {
        try {
            double bNorm = new Double(0);
            for (int j = 0; j < tableAll.getRowCount(); j++) {
                if (Integer.valueOf(tableAll.getValueAt(j, 0).toString()) >= 0 && Integer.valueOf(tableAll.getValueAt(j, 2).toString()) >= 0)
                    bNorm += Double.valueOf(tableAll.getValueAt(j, 6).toString()).doubleValue();
            }
            brigNorm.setText(UtilSpec.formatNorm(bNorm));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tModelTechRemoveRow() {
        boolean deleteItem = true;
        for (int i = 0; i < tableAll.getRowCount(); i++) {
            if (Integer.valueOf(tableAll.getValueAt(i, 2).toString()) == Integer.valueOf(tableAll.getValueAt(tableAll.getSelectedRow(), 2).toString()) && tableAll.getSelectedRow() != i) {
                deleteItem = false;
                break;
            }
        }
        if (deleteItem) {
            for (int i = 0; i < tableTech.getRowCount(); i++) {
                if (Integer.valueOf(tableTech.getValueAt(i, 0).toString()) == Integer.valueOf(tableAll.getValueAt(tableAll.getSelectedRow(), 2).toString())) {
                    tModelTech.removeRow(i);
                    break;
                }
            }
        }
    }

    private int numRowTableAll() {
        try {
            if (tableAll.getSelectedRow() == -1) {
                int num = (tableAll.getRowCount() == 1) ? Integer.valueOf(tableAll.getValueAt(0, 0).toString()) : 0;
                for (int i = 1; i < tableAll.getRowCount(); i++) {
                    // num = i+1;
                    num = Integer.valueOf(tableAll.getValueAt(i - 1, 0).toString()) + 1;
                    tableAll.setValueAt(num, i, 0);
                }
                return num + 1;
            } else return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
