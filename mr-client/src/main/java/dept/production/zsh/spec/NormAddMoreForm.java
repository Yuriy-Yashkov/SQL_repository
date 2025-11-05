package dept.production.zsh.spec;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import dept.sprav.tech.TechForm;
import dept.sprav.tech.UtilTech;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
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
 * Created by lidashka
 */

@SuppressWarnings("all")
public class NormAddMoreForm extends javax.swing.JDialog {
    JLabel userEdit;
    JComboBox dept;
    SpecPDB spdb;

    User user = User.getInstance();
    private JPanel osnova;
    private JPanel buttPanel;
    private JPanel centerPanel;
    private JPanel buttEastPanel;
    private JScrollPane checkPanel;
    private JTable tableModels;
    private JTable tableNorm;
    private JPanel titlePanel;
    private JPanel upPanel;
    private JDateChooser sStdate;
    private JButton buttClose;
    private JButton buttSave;
    private JButton buttPlus;
    private JButton buttMinus;
    private JButton buttOper;
    private JButton buttTech;
    private JTextField textOper;
    private JLabel techNum;
    private JLabel techText;
    private JTextField textvnorm;
    private JLabel sInsDate;
    private JButton buttSpec;
    private Vector colModels;
    private Vector colNorm;
    private JTextField textenorm;
    private int idTech;
    private DefaultTableModel tModelModels;
    private DefaultTableModel tModelNorm;
    private ProgressBar pb;
    private Vector check;

    private MainController controller;

    public NormAddMoreForm(MainController mainController, boolean modal, int deptSelectItem, Vector dataModel) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        setTitle("Нормы выработки и времени");

        init();

        UtilSpec.DEPT_SELECT_ITEM = deptSelectItem;

        dept.setSelectedItem(UtilSpec.getDept(UtilSpec.DEPT_MODEL, deptSelectItem));
        userEdit.setText(user.getFio());

        createTableModels(dataModel != null ? dataModel : new Vector());
        createTableNorm(new Vector());

        setNorm();
        setTech(UtilSpec.ID_TECH_OTK);

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(800, 550));
        setPreferredSize(new Dimension(600, 650));

        osnova = new JPanel();
        titlePanel = new JPanel();
        upPanel = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();
        buttEastPanel = new JPanel();
        checkPanel = new JScrollPane();

        sStdate = new JDateChooser();
        buttClose = new JButton("Закрыть");
        buttSave = new JButton("Добавить");
        buttPlus = new JButton("+");
        buttMinus = new JButton("-");
        buttSpec = new JButton("Найти");
        buttOper = new JButton("Найти");
        buttTech = new JButton("Найти");
        idTech = -1;
        textOper = new JTextField("отк");
        techNum = new JLabel();
        techText = new JLabel();
        textenorm = new JTextField("0");
        textvnorm = new JTextField(UtilSpec.formatNorm(0));
        sInsDate = new JLabel();
        userEdit = new JLabel();
        tableModels = new JTable();
        tableNorm = new JTable();
        colModels = new Vector();
        colNorm = new Vector();
        dept = new JComboBox(UtilSpec.DEPT_ITEMS);

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.setLayout(new ParagraphLayout());
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttEastPanel.setLayout(new GridLayout(6, 0, 5, 5));
        buttEastPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dept.setPreferredSize(new Dimension(300, 20));
        sStdate.setPreferredSize(new Dimension(120, 20));
        sInsDate.setPreferredSize(new Dimension(150, 20));
        buttClose.setPreferredSize(new Dimension(50, 20));
        textOper.setPreferredSize(new Dimension(400, 20));
        techNum.setPreferredSize(new Dimension(70, 20));
        techText.setPreferredSize(new Dimension(330, 20));
        textenorm.setPreferredSize(new Dimension(110, 20));
        textvnorm.setPreferredSize(new Dimension(110, 20));
        userEdit.setPreferredSize(new Dimension(300, 20));
        checkPanel.setPreferredSize(new Dimension(400, 200));

        tableModels.setPreferredScrollableViewportSize(new Dimension(0, 150));
        tableNorm.setPreferredScrollableViewportSize(new Dimension(450, 30));
        tableNorm.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        tableNorm.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                tableNorm.editCellAt(tableNorm.getSelectedRow(), tableNorm.getSelectedColumn());
                Component editor = tableNorm.getEditorComponent();
                if (editor != null) {
                    editor.requestFocus();
                    if (editor instanceof JTextField) {
                        ((JTextField) editor).selectAll();
                    }
                }
            }
        });

        sInsDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        textOper.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        techNum.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        techText.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        textenorm.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        textvnorm.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        userEdit.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        textenorm.setHorizontalAlignment(JLabel.RIGHT);
        textvnorm.setHorizontalAlignment(JLabel.RIGHT);

        sStdate.setDate((Calendar.getInstance()).getTime());
        sInsDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));

        colModels.add("Модель");
        colModels.add("Спецификация");

        colNorm.add("ед.шт.");
        colNorm.add("временя");

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        buttSave.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttOper.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOpenActionPerformed(evt);
            }
        });

        buttTech.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTechActionPerformed(evt);
            }
        });

        buttPlus.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPlusActionPerformed(evt);
            }
        });

        buttMinus.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttMinusActionPerformed(evt);
            }
        });

        buttSpec.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSpecActionPerformed(evt);
            }
        });


        titlePanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(dept);
        titlePanel.add(new JLabel("Операция:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(textOper);
        titlePanel.add(buttOper);
        titlePanel.add(new JLabel("Оборудование:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(techNum);
        titlePanel.add(techText);
        titlePanel.add(buttTech);
        titlePanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(sStdate);
        titlePanel.add(new JLabel("         Автор:"));
        titlePanel.add(userEdit);
        titlePanel.add(new JLabel("Норма:"), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(new JScrollPane(tableNorm));

        upPanel.add(titlePanel, BorderLayout.CENTER);

        buttEastPanel.add(buttPlus);
        buttEastPanel.add(buttMinus);
        buttEastPanel.add(buttSpec);

        centerPanel.add(new JScrollPane(tableModels), BorderLayout.CENTER);
        centerPanel.add(buttEastPanel, BorderLayout.EAST);

        buttPanel.add(new JLabel());
        buttPanel.add(buttSave);
        buttPanel.add(buttClose);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            UtilSpec.SPEC_BUTT_SAVE_ACTION = false; // Кнопка сохранения не нажата
            boolean saveFlag = true;
            String str = "";
            Vector dataItem = new Vector();

            if (((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId() == -1) {
                saveFlag = false;
                str += "Вы не выбрали цех!\n";
            }

            if (textOper.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели наименование операции!\n";
            }

            if (techText.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели наименование оборудования!\n";
            }

            if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate())))
                saveFlag = false;


            if (tModelNorm.getRowCount() < 1) {
                saveFlag = false;
                str += "Ошибка нормы!\n";
            } else {
                if (Double.valueOf(tModelNorm.getValueAt(0, 0).toString()) <= 0 ||
                        Double.valueOf(tModelNorm.getValueAt(0, 1).toString()) <= 0) {
                    saveFlag = false;
                    str += "Норма задана некорректно!\n";
                }
            }

            if (tModelModels.getRowCount() < 1) {
                saveFlag = false;
                str += "Вы не заполнили таблицу моделей!\n";
            }

            for (Object dataItem1 : tModelModels.getDataVector()) {

                if (((Vector) dataItem1).get(0).toString().trim().equals("") ||
                        ((Vector) dataItem1).get(1).toString().trim().equals("")) {
                    saveFlag = false;
                    str += "В таблице есть пусты строки!\n";
                    break;

                } else {
                    try {
                        Integer.valueOf(((Vector) dataItem1).get(0).toString());
                    } catch (Exception e) {
                        saveFlag = false;
                        str += "В таблице столбец 'Модель' заполнен с ошибкой!\n";
                        break;
                    }


                    if (((Vector) dataItem1).get(1).toString().trim().equals("")) {
                        saveFlag = false;
                        str += "В таблице не заполнен столбец 'Спецификация'!\n";
                        break;
                    }
                }
            }

            if (!saveFlag)
                JOptionPane.showMessageDialog(null,
                        str,
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

            if (saveFlag) {
                try {
                    spdb = new SpecPDB();

                    check = new Vector();

                    pb = new ProgressBar(this, false, "Проверка...");
                    final SwingWorker sw = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {

                            for (Object vec : tModelModels.getDataVector()) {
                                Vector tmp = spdb.checkSpecModels(Integer.valueOf(((Vector) vec).elementAt(0).toString()),
                                        ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId());

                                for (Iterator iterator = tmp.iterator(); iterator.hasNext(); ) {
                                    check.add((Vector) iterator.next());
                                }
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

                    if (!check.isEmpty()) {

                        checkPanel.setViewportView(new JTextArea("Для этих моделей уже существуют спецификации\n"
                                + check.toString().replace("],", ",\n").replace("[", "").replace("]", "")));

                        if (JOptionPane.showOptionDialog(null,
                                checkPanel,
                                "Продолжить?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.NO_OPTION) {
                            saveFlag = false;
                        }
                    }

                    if (saveFlag) {
                        UtilSpec.SPEC_BUTT_SAVE_ACTION = true; // Кнопка сохранения нажата

                        if (spdb.addSpecMore(((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId(),
                                textOper.getText().trim(),
                                Integer.valueOf(techNum.getText().trim()),
                                Integer.valueOf(user.getIdEmployee()),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate())),
                                Integer.valueOf(tModelNorm.getValueAt(0, 0).toString().trim()),
                                Double.valueOf(tModelNorm.getValueAt(0, 1).toString().trim()),
                                tModelModels.getDataVector())) {

                            JOptionPane.showMessageDialog(null,
                                    "Добавлено успешно!",
                                    "Завершено",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            //записываем выбранный цех в настройки
                            UtilFunctions.setSettingPropFile(
                                    String.valueOf(((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId()),
                                    UtilSpec.SETTING_DEPT_SELECT_ITEM);

                            dispose();
                        } else
                            JOptionPane.showMessageDialog(null,
                                    "Произошла ошибка.\nНе добавлено!",
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

    private void buttOpenActionPerformed(ActionEvent evt) {
        try {
            new OperacForm(this, true, ((Item) UtilSpec.DEPT_MODEL.get(dept.getSelectedIndex())).getId());
            textOper.setText(UtilSpec.OPERAC);
        } catch (Exception e) {
            textOper.setText("");
        }
    }

    private void buttTechActionPerformed(ActionEvent evt) {
        try {
            new TechForm(controller, true);

            idTech = UtilTech.ID;
            techNum.setText(String.valueOf(UtilTech.ID));
            techText.setText(UtilTech.TECH);

        } catch (Exception e) {
            idTech = -1;
            techNum.setText("");
            techText.setText("");
        }
    }

    private void buttPlusActionPerformed(ActionEvent evt) {
        try {
            Vector data = new Vector();
            data.add("");
            data.add("");
            tModelModels.insertRow(tableModels.getSelectedRow() != -1 ? tableModels.convertRowIndexToModel(tableModels.getSelectedRow()) : tableModels.getRowCount(), data);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttMinusActionPerformed(ActionEvent evt) {
        try {
            if (tableModels.getSelectedRow() != -1) {
                tModelModels.removeRow(tableModels.convertRowIndexToModel(tableModels.getSelectedRow()));

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы не выбрали строку для удаления!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSpecActionPerformed(ActionEvent evt) {
        try {

            UtilSpec.SPEC_BUTT_SELECT_ACTION = false;

            new SpecForm(controller, true, "");

            if (UtilSpec.SPEC_BUTT_SELECT_ACTION) {

                pb = new ProgressBar(this, false, "Обработка...");
                final SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        for (Object colModel : UtilSpec.SPEC_SELECTED_MODEL) {
                            tModelModels.insertRow(tableModels.getRowCount(), (Vector) colModel);
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
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTableModels(final Vector data) {
        tModelModels = new DefaultTableModel(data, colModels);

        tableModels.setModel(tModelModels);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModelModels);
        tableModels.setRowSorter(sorter);


        tableModels.getColumnModel().getColumn(0).setPreferredWidth(20);
        tableModels.getColumnModel().getColumn(1).setPreferredWidth(300);
    }

    private void createTableNorm(final Vector data) {
        tModelNorm = new DefaultTableModel(data, colNorm) {

            @Override
            public void setValueAt(Object value, int row, int col) {
                try {
                    if (!value.equals("") && value != null) {
                        Vector rowVector = (Vector) dataVector.elementAt(row);
                        switch (col) {
                            case 0:
                                rowVector.setElementAt(Integer.valueOf(value.toString()).intValue(), col);
                                rowVector
                                        .setElementAt(UtilSpec
                                                .formatNorm(Double.valueOf(value.toString()) != 0 ? 8 / Double.valueOf(value.toString()) : Double.valueOf(value.toString())), col + 1);
                                fireTableCellUpdated(row, col + 1);
                                break;
                            case 1:
                                value = value.toString().replace(",", ".");
                                rowVector
                                        .setElementAt(UtilSpec
                                                .formatNorm(Double.valueOf(value.toString()).doubleValue()), col);
                                rowVector
                                        .setElementAt(new BigDecimal(Double.valueOf(value.toString()) != 0 ? 8 / Double.valueOf(value.toString()) : Double.valueOf(value.toString())).setScale(0, RoundingMode.HALF_UP).intValue(), col - 1);
                                fireTableCellUpdated(row, col - 1);
                                break;
                            default:
                                rowVector.setElementAt(value, col);
                                break;

                        }
                    }
                    fireTableCellUpdated(row, col);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null,
                            "Введено некорректное значение: " + ee.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        tableNorm.setModel(tModelNorm);

    }

    private void setNorm() {
        Vector norm = new Vector();
        norm.add(0);
        norm.add(UtilSpec.formatNorm(new Double(0)));
        tModelNorm.insertRow(0, norm);
    }

    private void setTech(int id) {
        try {
            spdb = new SpecPDB();

            String tech = spdb.searchTech(id).trim();

            if (!tech.equals("")) {
                idTech = id;
                techNum.setText(String.valueOf(id));
                techText.setText(tech);
            }

        } catch (Exception e) {
            idTech = -1;
            techNum.setText("");
            techText.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. Оборудование не установлено." + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

        } finally {
            spdb.disConn();
        }
    }
}
