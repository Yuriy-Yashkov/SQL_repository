package dept.sklad.ho;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.Item;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import dept.sprav.employe.EmployeForm;
import dept.sprav.employe.UtilEmploye;
import dept.sprav.model.ModelForm;
import dept.sprav.model.UtilModel;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SkladStorageItemForm extends javax.swing.JDialog {
    public SkladHOPDB spdb;
    int minSelectedModelRow = -1;
    int maxSelectedModelRow = -1;
    boolean tableModelModelListenerIsChanging = false;
    private User user = User.getInstance();
    private JPanel osnova;
    private JPanel buttPanel;
    private JPanel buttEastTab1Panel;
    private JPanel modelPanel;
    private JPanel emplPanel;
    private JPanel filterModelRow;
    private JLabel vvodEmpl;
    private JLabel vvodDate;
    private JLabel insEmpl;
    private JLabel insDate;
    private JLabel numEmpl;
    private JLabel emplName;
    private JTextField doc;
    private JComboBox dept;
    private JButton buttSave;
    private JButton buttClose;
    private JButton buttPlus;
    private JButton buttMinus;
    private JButton buttDetal;
    private JButton buttModel;
    private JButton buttEmpl;
    private JDateChooser stDate;
    private JTextPane note;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private JTabbedPane tableTabPane;
    private JTable tableModel;
    private Vector colModel;
    private DefaultTableModel tModelModel;
    private TableRowSorter<TableModel> sorterModel;
    private TableFilterHeader filterHeaderModel;
    private DefaultTableCellRenderer renderer;
    private boolean EDIT;
    private boolean LOOK;
    private String TYPE;
    private ProgressBar pb;
    private int idStorage;
    private Vector colVector;
    private Vector dataTable;
    private JButton buttPrint;
    private MainController controller;

    public SkladStorageItemForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Сдача на склад");

        TYPE = UtilSkladHO.TYPE_ADD;

        init();

        EDIT = true;

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, UtilSkladHO.DEPT_SELECT_ITEM);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createModelTable(new Vector());

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public SkladStorageItemForm(MainController mainController, boolean modal, Vector dataEdit, String type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Сдача на склад №" + dataEdit.get(1).toString());
        setPreferredSize(new Dimension(800, 450));

        TYPE = type;

        init();

        idStorage = Integer.valueOf(dataEdit.get(1).toString());
        EDIT = false;

        emplPanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        emplPanel.add(vvodDate);
        emplPanel.add(new JLabel("    Автор:"));
        emplPanel.add(vvodEmpl);
        emplPanel.add(new JLabel("Дата корр.:"), ParagraphLayout.NEW_PARAGRAPH);
        emplPanel.add(insDate);
        emplPanel.add(new JLabel("    Автор:"));
        emplPanel.add(insEmpl);

        buttEastTab1Panel.setVisible(false);
        buttSave.setVisible(false);

        buttEmpl.setEnabled(false);
        dept.setEnabled(false);
        stDate.setEnabled(false);
        note.setEnabled(false);
        jRadioButton2.setEnabled(false);
        jRadioButton3.setEnabled(false);
        jRadioButton4.setEnabled(false);

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                if ((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                    stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, UtilSkladHO.DEPT_SELECT_ITEM);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {

            stDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataEdit.get(2).toString()));
            doc.setText(dataEdit.get(3).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, Integer.valueOf(dataEdit.get(4).toString())));
            numEmpl.setText(dataEdit.get(6).toString());
            emplName.setText(dataEdit.get(7).toString());
            note.setText(dataEdit.get(16).toString());
            vvodDate.setText(dataEdit.get(17).toString());
            vvodEmpl.setText(dataEdit.get(18).toString());
            insDate.setText(dataEdit.get(19).toString());
            insEmpl.setText(dataEdit.get(20).toString());
            switch (Integer.valueOf(dataEdit.get(22).toString())) {
                case 0:
                    jRadioButton2.setSelected(true);
                    break;
                case 1:
                    jRadioButton3.setSelected(true);
                    break;
                case -1:
                    jRadioButton4.setSelected(true);
                    break;
                default:
                    jRadioButton3.setSelected(true);
                    break;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        createModelTable(getDataTable());

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public SkladStorageItemForm(MainController mainController, boolean modal, Vector dataEdit, boolean flagAccess, String type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Сдача на склад №" + dataEdit.get(1).toString());
        setPreferredSize(new Dimension(900, 450));

        TYPE = type;

        init();

        idStorage = Integer.valueOf(dataEdit.get(1).toString());
        EDIT = true;

        emplPanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        emplPanel.add(vvodDate);
        emplPanel.add(new JLabel("    Автор:"));
        emplPanel.add(vvodEmpl);
        emplPanel.add(new JLabel("Дата корр.:"), ParagraphLayout.NEW_PARAGRAPH);
        emplPanel.add(insDate);
        emplPanel.add(new JLabel("    Автор:"));
        emplPanel.add(insEmpl);

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                if ((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                    stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, UtilSkladHO.DEPT_SELECT_ITEM);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            stDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataEdit.get(2).toString()));
            doc.setText(dataEdit.get(3).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, Integer.valueOf(dataEdit.get(4).toString())));
            numEmpl.setText(dataEdit.get(6).toString());
            emplName.setText(dataEdit.get(7).toString());
            note.setText(dataEdit.get(16).toString());
            vvodDate.setText(dataEdit.get(17).toString());
            vvodEmpl.setText(dataEdit.get(18).toString());
            insDate.setText(dataEdit.get(19).toString());
            insEmpl.setText(dataEdit.get(20).toString());
            switch (Integer.valueOf(dataEdit.get(22).toString())) {
                case 0:
                    jRadioButton2.setSelected(true);
                    break;
                case 1:
                    jRadioButton3.setSelected(true);
                    break;
                case -1:
                    jRadioButton4.setSelected(true);
                    break;
                default:
                    jRadioButton3.setSelected(true);
                    break;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        createModelTable(getDataTable());

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void init() {
        UtilSkladHO.BUTT_ACTION_EDIT = false;

        idStorage = 0;

        setMinimumSize(new Dimension(900, 430));
        setPreferredSize(new Dimension(900, 550));

        osnova = new JPanel();
        buttPanel = new JPanel();
        modelPanel = new JPanel();
        emplPanel = new JPanel();
        buttEastTab1Panel = new JPanel();

        tableTabPane = new JTabbedPane();
        numEmpl = new JLabel();
        emplName = new JLabel();
        doc = new JTextField();
        vvodEmpl = new JLabel();
        vvodDate = new JLabel();
        insEmpl = new JLabel();
        insDate = new JLabel();
        tableModel = new JTable();
        filterHeaderModel = new TableFilterHeader(tableModel, AutoChoices.ENABLED);
        colModel = new Vector();
        colVector = new Vector();
        note = new JTextPane();
        dept = new JComboBox(UtilSkladHO.DEPT_MODEL);
        stDate = new JDateChooser();
        buttPrint = new JButton("Печать");
        buttEmpl = new JButton("Работник");
        buttSave = new JButton("Сохранить");
        buttClose = new JButton("Закрыть");
        buttPlus = new JButton("+");
        buttMinus = new JButton("-");
        buttDetal = new JButton("Детали");
        buttModel = new JButton("Модели");
        buttonGroup = new ButtonGroup();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        emplPanel.setLayout(new ParagraphLayout());
        modelPanel.setLayout(new BorderLayout(1, 1));
        modelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        dept.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        buttEastTab1Panel.setLayout(new GridLayout(6, 0, 5, 5));
        buttEastTab1Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dept.setPreferredSize(new Dimension(350, 20));
        emplName.setPreferredSize(new Dimension(300, 20));
        numEmpl.setPreferredSize(new Dimension(100, 20));
        doc.setPreferredSize(new Dimension(350, 20));
        stDate.setPreferredSize(new Dimension(120, 20));
        note.setPreferredSize(new Dimension(250, 60));
        vvodEmpl.setPreferredSize(new Dimension(240, 20));
        vvodDate.setPreferredSize(new Dimension(120, 20));
        insEmpl.setPreferredSize(new Dimension(240, 20));
        insDate.setPreferredSize(new Dimension(120, 20));

        numEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        emplName.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        note.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton2.setText("формируется;");
        jRadioButton3.setText("закрыт;");
        jRadioButton4.setText("удалён;");

        jRadioButton2.setActionCommand("0");
        jRadioButton3.setActionCommand("1");
        jRadioButton4.setActionCommand("-1");

        jRadioButton2.setSelected(true);

        tableModel.setAutoCreateColumnsFromModel(true);
        tableModel.getTableHeader().setReorderingAllowed(false);

        tableModel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        tableModel.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int colum = tableModel.getSelectedColumn();
                if (colum >= 21 && colum <= 22) {
                    buttModel.doClick();
                } else {
                    tableModel.editCellAt(tableModel.getSelectedRow(), tableModel.getSelectedColumn());
                    Component editor = tableModel.getEditorComponent();
                    if (editor != null) {
                        editor.requestFocus();
                        if (editor instanceof JTextField) {
                            ((JTextField) editor).selectAll();
                        }
                    }
                }
            }
        });

        tableModel.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedModelRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedModelRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //   buttPrintActionPerformed(evt);
            }
        });

        buttEmpl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEmplActionPerformed(evt);
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

        buttDetal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttDetalActionPerformed(evt);
            }
        });

        buttModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttModelActionPerformed(evt);
            }
        });

        colModel.add("");
        colModel.add("№ К/р");
        colModel.add("Дата");
        colModel.add("Модель");
        colModel.add("Название");
        colModel.add("Артикул");
        colModel.add("idВид");
        colModel.add("Вид");
        colModel.add("Кол-во");
        colModel.add("Шкала");
        colModel.add("Примечание");
        colModel.add("idЦех");
        colModel.add("Цех");
        colModel.add("idАвтор");
        colModel.add("Автор");
        colModel.add("Дата ввода");
        colModel.add("Автор ввода");
        colModel.add("Дата корр.");
        colModel.add("Автор корр.");
        colModel.add("Статус");
        colModel.add("idStatus");
        colModel.add("Модель пр.");
        colModel.add("Название пр.");

        colVector.add(0);
        colVector.add(1);
        colVector.add(3);
        colVector.add(4);
        colVector.add(8);
        colVector.add(9);
        colVector.add(21);
        colVector.add(22);

        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton4);

        emplPanel.add(new JLabel("Дата :"), ParagraphLayout.NEW_PARAGRAPH);
        emplPanel.add(stDate);
        emplPanel.add(new JLabel("    Статус:"));
        emplPanel.add(jRadioButton2);
        emplPanel.add(jRadioButton3);
        emplPanel.add(jRadioButton4);
        emplPanel.add(new JLabel("Документ:"), ParagraphLayout.NEW_PARAGRAPH);
        emplPanel.add(doc);
        emplPanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        emplPanel.add(dept);
        emplPanel.add(buttEmpl, ParagraphLayout.NEW_PARAGRAPH);
        emplPanel.add(numEmpl);
        emplPanel.add(emplName);
        emplPanel.add(new JLabel("Примечание :"), ParagraphLayout.NEW_PARAGRAPH);
        emplPanel.add(note, ParagraphLayout.NEW_LINE_STRETCH_H);

        buttEastTab1Panel.add(buttPlus);
        buttEastTab1Panel.add(buttMinus);
        buttEastTab1Panel.add(buttDetal);
        buttEastTab1Panel.add(buttModel);

        modelPanel.add(new JScrollPane(tableModel), BorderLayout.CENTER);
        modelPanel.add(buttEastTab1Panel, BorderLayout.EAST);

        buttPanel.add(buttClose);
        buttPanel.add(buttSave);
        buttPanel.add(new JLabel());
        buttPanel.add(buttPrint);

        tableTabPane.addTab("Информация", emplPanel);
        tableTabPane.addTab("Модели", modelPanel);

        osnova.add(tableTabPane, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            UtilSkladHO.DEPT_SELECT_ITEM = ((Item) dept.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) dept.getSelectedItem()).getId()), UtilSkladHO.SETTING_DEPT_SELECT_ITEM);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        try {
            if (TYPE.equals(UtilSkladHO.TYPE_OPEN) ||
                    TYPE.equals(UtilSkladHO.TYPE_ADD_RETURN) ||
                    TYPE.equals(UtilSkladHO.TYPE_EDIT_RETURN)) {
                dispose();
            } else {
                if (JOptionPane.showOptionDialog(null, "Сохранить изменения?", "Сохранение...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION)
                    buttSave.doClick();
                else {
                    dispose();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            if (EDIT) {
                boolean saveFlag = true;
                String str = "";

                try {
                    if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()))) {
                        UtilSkladHO.DATE_VVOD = new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate());
                        UtilFunctions.setSettingPropFile(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()), UtilSkladHO.SETTING_DATE_VVOD);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка сохранения в файл настроек даты ввода! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()))) {
                        saveFlag = false;
                        str += "Дата задана некорректно!\n";
                    }
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Дата задана некорректно!\n";
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    Integer.valueOf(((Item) dept.getSelectedItem()).getId());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Цех задан некорректно!\n";
                }

                try {
                    if (!numEmpl.getText().trim().equals(""))
                        Integer.valueOf(numEmpl.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Код работника задан некорректно!\n";
                }

                if (tModelModel.getRowCount() < 1) {
                    saveFlag = false;
                    str += "Вы не заполнили таблицу моделей!\n";
                }

                if (!saveFlag) {
                    JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

                if (saveFlag) {
                    try {
                        spdb = new SkladHOPDB();

                        if (TYPE.equals(UtilSkladHO.TYPE_ADD)) {
                            if (spdb.addSkladStorage(
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                    doc.getText().trim(),
                                    note.getText().trim(),
                                    Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                                    numEmpl.getText().trim().equals("") ? null : Integer.valueOf(numEmpl.getText().trim()),
                                    Integer.valueOf(user.getIdEmployee()),
                                    Integer.valueOf(buttonGroup.getSelection().getActionCommand()),
                                    tModelModel.getDataVector())) {
                                JOptionPane.showMessageDialog(null, "Запись успешно добавлена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                UtilSkladHO.BUTT_ACTION_EDIT = true;

                                dispose();
                            }
                        } else if (TYPE.equals(UtilSkladHO.TYPE_EDIT)) {
                            if (spdb.editSkladStorage(
                                    idStorage,
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                    doc.getText().trim(),
                                    note.getText().trim(),
                                    Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                                    numEmpl.getText().trim().equals("") ? null : Integer.valueOf(numEmpl.getText().trim()),
                                    Integer.valueOf(user.getIdEmployee()),
                                    Integer.valueOf(buttonGroup.getSelection().getActionCommand()),
                                    tModelModel.getDataVector())) {
                                JOptionPane.showMessageDialog(null, "Запись успешно изменена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                UtilSkladHO.BUTT_ACTION_EDIT = true;

                                dispose();
                            }
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEmplActionPerformed(ActionEvent evt) {
        try {
            new EmployeForm(controller, true, ((Item) dept.getSelectedItem()).getId(), "");

            numEmpl.setText(UtilEmploye.NUM);
            emplName.setText(UtilEmploye.NAIM);

        } catch (Exception e) {
            numEmpl.setText("");
            emplName.setText("");

            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttPlusActionPerformed(ActionEvent evt) {
        try {
            Vector temp = new Vector();

            new SearchTableForm(controller, true,
                    new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()),
                    idStorage);

            if (UtilSkladHO.BUTT_ACTION_SELECT_PRODUCT)
                if (UtilSkladHO.PRODUCT_DATA_VECTOR != null) {

                    boolean flagAdd = true;

                    for (int i = 0; i < tModelModel.getDataVector().size(); i++) {
                        if (UtilSkladHO.PRODUCT_DATA_VECTOR.get(1).toString().equals(
                                ((Vector) tModelModel.getDataVector().elementAt(i)).get(1).toString())) {
                            flagAdd = false;
                            break;
                        }
                    }

                    if (flagAdd) {
                        temp = UtilSkladHO.PRODUCT_DATA_VECTOR;
                        temp.add("");
                        temp.add("");

                        tModelModel.insertRow(tableModel.getSelectedRow() != -1 ?
                                        tableModel.getSelectedRow() : tableModel.getRowCount(),
                                temp);

                    } else
                        JOptionPane.showMessageDialog(null, "Нельзя дублировать карты раскроя! ", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

                }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }


    private void buttMinusActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(null, "Удалить отмеченные строки?", "Удаление ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Удалить", "Отмена"}, "Удалить") == JOptionPane.YES_OPTION) {
                for (int i = 0; i < tModelModel.getDataVector().size(); i++) {
                    if (Boolean.valueOf(((Vector) tModelModel.getDataVector().get(i)).elementAt(0).toString())) {
                        tModelModel.getDataVector().remove(i);
                        i--;
                    }
                }

                createModelTable(tModelModel.getDataVector());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttDetalActionPerformed(ActionEvent evt) {
        try {
            if (tableModel.getSelectedRow() != -1) {
                new SkladProductItemForm(controller, true,
                        UtilSkladHO.getItemTable(tableModel),
                        UtilSkladHO.TYPE_OPEN);

            } else {
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
        }

    }

    private void buttModelActionPerformed(ActionEvent evt) {
        try {
            if (tableModel.getSelectedRow() != -1) {
                try {
                    new ModelForm(controller, true, true);

                    tableModel.setValueAt(UtilModel.MODEL, tableModel.getSelectedRow(), 21);
                    tableModel.setValueAt(UtilModel.NAIM, tableModel.getSelectedRow(), 22);

                } catch (Exception e) {
                    tableModel.setValueAt("", tableModel.getSelectedRow(), 21);
                    tableModel.setValueAt("", tableModel.getSelectedRow(), 22);

                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Вы не выбрали запись!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    private void createModelTable(final Vector data) {
        tModelModel = new DefaultTableModel(data, colModel) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (data.isEmpty())
                    return super.getClass();
                else
                    return (col == 0) ? getValueAt(0, col).getClass() : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0 || col == 21 || col == 22) ? true : false;
            }

            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);

                    if (value != null) {
                        if (col == 21) {
                            if (!value.equals(""))
                                rowVector.setElementAt(Integer.valueOf(value.toString()).intValue(), col);
                            else
                                rowVector.setElementAt(value, col);
                        } else if (col == 22) {
                            rowVector.setElementAt(String.valueOf(value.toString().trim().toUpperCase()), col);
                        } else
                            rowVector.setElementAt(value, col);
                    } else
                        rowVector.setElementAt(value, col);

                    fireTableCellUpdated(row, col);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + ee.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        tModelModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedModelRow == -1 || minSelectedModelRow == -1) {
                    return;
                }
                tableModelModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelModel.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedModelRow; i <= maxSelectedModelRow; i++) {
                    tModelModel.setValueAt(Boolean.valueOf(value), tableModel.convertRowIndexToModel(i), column);
                }

                minSelectedModelRow = -1;
                maxSelectedModelRow = -1;

                tableModelModelListenerIsChanging = false;
            }
        });

        tableModel.setModel(tModelModel);
        tableModel.setAutoCreateColumnsFromModel(true);

        initColum();

        sorterModel = new TableRowSorter<TableModel>(tModelModel);
        tableModel.setRowSorter(sorterModel);
        tableModel.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableModel.getTableHeader(), 0, ""));

    }

    private void initColum() {
        try {
            tableModel.getColumnModel().getColumn(0).setPreferredWidth(5);

            for (int i = 1; i < tModelModel.getColumnCount(); i++) {
                if (!colVector.contains(i)) {
                    tableModel.getColumnModel().getColumn(i).setMinWidth(0);
                    tableModel.getColumnModel().getColumn(i).setMaxWidth(0);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    private Vector getDataTable() {
        dataTable = new Vector();

        try {
            pb = new ProgressBar(this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {

                        pb.setMessage("Обновление таблицы моделей ...");

                        try {
                            spdb = new SkladHOPDB();
                            dataTable = spdb.getDataStorageModelTable(idStorage);

                        } catch (Exception e) {
                            dataTable = new Vector();
                            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            spdb.disConn();
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

        } catch (Exception e) {
            dataTable = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return dataTable;
    }
}
