package dept.production.zsh.zplata;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.User;
import common.UtilFunctions;
import dept.sprav.employe.EmployeForm;
import dept.sprav.employe.UtilEmploye;
import dept.sprav.model.ModelForm;
import dept.sprav.model.UtilModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class ZPlataDetalForm extends javax.swing.JDialog {
    JButton buttClose;
    JButton buttCloseNotSave;
    JButton buttSave;
    JButton buttPrint;
    JButton buttModel;
    JButton buttSpec;
    JButton buttPlus;
    JButton buttMinus;
    JButton buttCopyTech;
    JButton buttClear;
    JButton buttEmploye;
    JDateChooser sStdate;
    JComboBox dept;
    JComboBox brig;
    JCheckBox status;
    JCheckBox checkList;
    JTextField kolvo;
    JTextField marshrut;
    JTextField note;
    JLabel brigadir;
    JLabel sInsDate;
    JLabel title;
    JLabel modelNum;
    JLabel modelNaim;
    JLabel specNum;
    JLabel specNaim;
    JLabel sumSelectRow;
    JLabel sumVyrabatRow;
    JPanel osnova;
    JPanel titlePanel;
    JPanel buttPanel;
    JPanel buttEastPanel;
    JPanel upPanel;
    JPanel centerPanel;
    JTable table;

    DefaultTableModel tModel;
    DefaultTableCellRenderer renderer;
    Vector col;

    boolean EDIT = false;
    int idList;

    ZPlataPDB zpdb;

    User user = User.getInstance();
    private MainController controller;

    public ZPlataDetalForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        init();

        title.setText("Формирование листка запуска");

        buttSave.setText(UtilZPlata.BUTTON_ADD);
        try {
            dept.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.DEPT_MODEL, UtilZPlata.DEPT_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            brig.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIG_MODEL, UtilZPlata.BRIG_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        brigadir.setText(user.getFio());

        try {
            if (!UtilZPlata.DATE_VVOD.equals(""))
                if ((new SimpleDateFormat("MM.yyyy").format(sStdate.getDate())).equals(UtilZPlata.DATE_VVOD.substring(3)))
                    sStdate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilZPlata.DATE_VVOD));
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        if (((Item) dept.getSelectedItem()).getId() == 8)
            checkList.setSelected(true);

        createZPlataDetalTable(new Vector());

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public ZPlataDetalForm(MainController mainController, boolean modal, Vector dataList, Vector dataItem, int idList, boolean flag) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        init();

        title.setText("Листок запуска и передачи № " + idList);

        try {
            EDIT = flag;
            this.idList = idList;

            buttSave.setText(UtilZPlata.BUTTON_SAVE);

            dept.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.DEPT_MODEL, Integer.valueOf(dataList.get(0).toString())));
            brig.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIG_MODEL, Integer.valueOf(dataList.get(2).toString())));
            brigadir.setText(dataList.get(3).toString());
            modelNum.setText(dataList.get(4).toString());
            modelNaim.setText(dataList.get(5).toString());
            specNum.setText(dataList.get(6).toString());
            specNaim.setText(dataList.get(7).toString());
            marshrut.setText(dataList.get(8).toString());
            kolvo.setText(dataList.get(9).toString());
            sStdate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataList.get(10).toString()));
            sInsDate.setText(dataList.get(11).toString());
            note.setText(dataList.get(12).toString());
            status.setSelected(Integer.valueOf(dataList.get(14).toString()) == 0 ? false : true);

            if (((Item) dept.getSelectedItem()).getId() == 8)
                checkList.setSelected(true);

            createZPlataDetalTable(dataItem);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Данные листка запуска не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public ZPlataDetalForm(MainController mainController, boolean modal, Vector dataList, Vector dataItem, int idList) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        init();

        title.setText("Листок запуска и передачи № " + idList
                + " " + (Integer.valueOf(dataList.get(14).toString()) == -1 ? dataList.get(13).toString() : " "));

        try {
            this.idList = idList;

            sStdate.setEnabled(false);
            table.setEnabled(false);
            dept.setEnabled(false);
            brig.setEnabled(false);
            marshrut.setEnabled(false);
            kolvo.setEnabled(false);
            note.setEnabled(false);
            status.setEnabled(false);
            checkList.setEnabled(false);

            buttModel.setVisible(false);
            buttSpec.setVisible(false);
            buttEastPanel.setVisible(false);

            buttPanel.removeAll();
            buttPanel.add(buttCloseNotSave);
            buttPanel.add(buttPrint);
            buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            dept.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.DEPT_MODEL, Integer.valueOf(dataList.get(0).toString())));
            brig.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIG_MODEL, Integer.valueOf(dataList.get(2).toString())));
            brigadir.setText(dataList.get(3).toString());
            modelNum.setText(dataList.get(4).toString());
            modelNaim.setText(dataList.get(5).toString());
            specNum.setText(dataList.get(6).toString());
            specNaim.setText(dataList.get(7).toString());
            marshrut.setText(dataList.get(8).toString());
            kolvo.setText(dataList.get(9).toString());
            sStdate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataList.get(10).toString()));
            sInsDate.setText(dataList.get(11).toString());
            note.setText(dataList.get(12).toString());
            status.setSelected(Integer.valueOf(dataList.get(14).toString()) == 0 ? false : true);

            createZPlataDetalTable(dataItem);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Данные листка запуска не загружены! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

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
        setTitle("Учет выработки рабочих");

        setMinimumSize(new Dimension(850, 550));
        setPreferredSize(new Dimension(960, 740));

        osnova = new JPanel();
        titlePanel = new JPanel();
        upPanel = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();
        buttEastPanel = new JPanel();
        title = new JLabel();
        sStdate = new JDateChooser();
        buttClose = new JButton("Закрыть");
        buttCloseNotSave = new JButton("Закрыть");
        buttSave = new JButton();
        buttPrint = new JButton("Печать");
        buttModel = new JButton("Найти");
        buttSpec = new JButton("Найти");
        buttPlus = new JButton("+");
        buttMinus = new JButton("-");
        buttEmploye = new JButton("Р");
        buttCopyTech = new JButton("ОБ");
        buttClear = new JButton("ОЧ");
        col = new Vector();
        table = new JTable();
        sInsDate = new JLabel();
        modelNum = new JLabel();
        modelNaim = new JLabel();
        specNum = new JLabel();
        specNaim = new JLabel();
        brigadir = new JLabel();
        sumSelectRow = new JLabel("0");
        sumVyrabatRow = new JLabel("0");
        kolvo = new JTextField();
        marshrut = new JTextField();
        note = new JTextField();
        dept = new JComboBox(UtilZPlata.DEPT_MODEL);
        brig = new JComboBox(UtilZPlata.BRIG_MODEL);
        status = new JCheckBox("Листок сформирован;");
        checkList = new JCheckBox("Проверить кол-во;");

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.setLayout(new ParagraphLayout());
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttEastPanel.setLayout(new GridLayout(7, 0, 5, 5));
        buttEastPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dept.setPreferredSize(new Dimension(300, 20));
        brig.setPreferredSize(new Dimension(150, 20));
        modelNum.setPreferredSize(new Dimension(100, 20));
        modelNaim.setPreferredSize(new Dimension(350, 20));
        specNum.setPreferredSize(new Dimension(100, 20));
        specNaim.setPreferredSize(new Dimension(350, 20));
        sStdate.setPreferredSize(new Dimension(150, 20));
        sInsDate.setPreferredSize(new Dimension(150, 20));
        buttClose.setPreferredSize(new Dimension(50, 20));
        buttCloseNotSave.setPreferredSize(new Dimension(50, 20));
        note.setPreferredSize(new Dimension(600, 20));
        kolvo.setPreferredSize(new Dimension(100, 20));
        marshrut.setPreferredSize(new Dimension(150, 20));
        brigadir.setPreferredSize(new Dimension(300, 20));

        table.setPreferredScrollableViewportSize(new Dimension(0, 150));

        modelNum.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        modelNaim.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        specNum.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        specNaim.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        sInsDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        brigadir.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        sumSelectRow.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        sumVyrabatRow.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        sumSelectRow.setHorizontalAlignment(JLabel.CENTER);
        sumVyrabatRow.setHorizontalAlignment(JLabel.CENTER);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        table.setAutoCreateColumnsFromModel(true);

        sStdate.setDate((Calendar.getInstance()).getTime());
        sInsDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));

        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        table.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int colum = table.getSelectedColumn();
                if (colum == 11 || colum == 12) {
                    buttEmploye.doClick();
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

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                try {
                    if (!sumSelectRow.getText().trim().equals("0"))
                        sumSelectRow.setText("0");

                    int sum = 0;
                    if (table.getSelectedRowCount() > 1) {
                        for (int i = 0; i < table.getRowCount(); i++)
                            if (table.isRowSelected(i) && table.getValueAt(i, 13) != null)
                                sum += Integer.valueOf(table.getValueAt(i, 13).toString());

                        if (sum > 0)
                            sumSelectRow.setText(String.valueOf(sum));
                    }
                } catch (Exception ex) {
                    sumSelectRow.setText("0");
                    JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (Boolean.valueOf(table.getValueAt(row, 9).toString()))
                    cell.setBackground(Color.LIGHT_GRAY);
                else
                    cell.setBackground(table.getBackground());

                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }
        };

        buttModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttModelActionPerformed(evt);
            }
        });

        buttSpec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSpecActionPerformed(evt);
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

        buttEmploye.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEmployeActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

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

        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttClearActionPerformed(evt);
            }
        });

        buttCopyTech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCopyTechActionPerformed(evt);
            }
        });

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

        brig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brigActionPerformed(evt);
            }
        });

        col.add("");
        col.add("№ id_spec_item");
        col.add("№");
        col.add("Операция");
        col.add("ID ОБ");
        col.add("Оборудование");
        col.add("Р/Д");
        col.add("Норма ед. шт.");
        col.add("Норма времени");
        col.add("flagAdd");
        col.add("№ id_employees");
        col.add("Табел.");
        col.add("ФИО");
        col.add("Кол-во");
        col.add("Выработка");
        col.add("Р/Д employees");

        titlePanel.add(new JLabel("Цех: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(dept);
        titlePanel.add(new JLabel("Бригада: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(brig);
        titlePanel.add(new JLabel(" Бригадир: "));
        titlePanel.add(brigadir);
        titlePanel.add(new JLabel("Модель: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(modelNum);
        titlePanel.add(modelNaim);
        titlePanel.add(buttModel);
        titlePanel.add(new JLabel("Спецификация: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(specNum);
        titlePanel.add(specNaim);
        titlePanel.add(buttSpec);
        titlePanel.add(new JLabel("Маршрут №: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(marshrut);
        titlePanel.add(new JLabel(" Кол-во: "));
        titlePanel.add(kolvo);
        titlePanel.add(checkList);
        titlePanel.add(status);
        titlePanel.add(new JLabel(" Дата ввода: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(sStdate);
        titlePanel.add(new JLabel(" Дата корректировки: "));
        titlePanel.add(sInsDate);
        titlePanel.add(new JLabel("Примечание: "), ParagraphLayout.NEW_PARAGRAPH);
        titlePanel.add(note);

        upPanel.add(title, BorderLayout.NORTH);
        upPanel.add(titlePanel, BorderLayout.CENTER);

        buttEastPanel.add(buttPlus);
        buttEastPanel.add(buttMinus);
        buttEastPanel.add(buttEmploye);
        buttEastPanel.add(buttCopyTech);
        buttEastPanel.add(buttClear);
        buttEastPanel.add(sumSelectRow);
        buttEastPanel.add(sumVyrabatRow);

        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        centerPanel.add(buttEastPanel, BorderLayout.EAST);

        buttPanel.add(buttPrint);
        buttPanel.add(buttSave);
        buttPanel.add(buttClose);

        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) dept.getSelectedItem()).getId()), UtilZPlata.SETTING_DEPT_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void brigActionPerformed(ActionEvent evt) {
        try {
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) brig.getSelectedItem()).getId()), UtilZPlata.SETTING_BRIG_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttModelActionPerformed(ActionEvent evt) {
        new ModelForm(controller, true, true);
        modelNum.setText("");
        modelNaim.setText("");
        specNum.setText("");
        specNaim.setText("");
        createZPlataDetalTable(new Vector());
        modelNum.setText(UtilModel.MODEL);
        modelNaim.setText(UtilModel.NAIM);
    }

    private void buttSpecActionPerformed(ActionEvent evt) {
        if (!modelNum.getText().equals("")) {
            try {
                new SpecSearchForm(ZPlataDetalForm.this, true,
                        Integer.valueOf(modelNum.getText()),
                        ((Item) dept.getSelectedItem()).getId());
                specNum.setText("");
                specNaim.setText("");
                createZPlataDetalTable(new Vector());
                specNum.setText(SpecSearchForm.SPEC_NUM);
                specNaim.setText(SpecSearchForm.SPEC_NAIM);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            if (!specNum.getText().equals("")) {
                try {
                    zpdb = new ZPlataPDB();
                    createZPlataDetalTable(zpdb.getElementsSpec(Integer.valueOf(SpecSearchForm.SPEC_NUM)));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    zpdb.disConn();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Вы не выбрали модель!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buttPlusActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                if (table.getRowCount() > 0) {
                    Vector data = new Vector();

                    for (int i = 0; i < col.size(); i++)
                        data.add(table.getValueAt(table.getSelectedRow(), i));

                    data.setElementAt(true, 9);

                    tModel.insertRow(table.getSelectedRow() + 1, data);

                    updateSumVyrabatRows();
                }
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttMinusActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1)
                if (Boolean.valueOf(table.getValueAt(table.getSelectedRow(), 9).toString())) {
                    tModel.removeRow(table.getSelectedRow());

                    updateSumVyrabatRows();
                } else
                    JOptionPane.showMessageDialog(null, "Вы не можете удалить строку, которую вы не добавляли!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEmployeActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                new EmployeForm(
                        controller,
                        true,
                        UtilEmploye.TYPE_SEARCH_CONFIG,
                        ((Item) brig.getSelectedItem()).getId());

                table.setValueAt(UtilEmploye.NUM.equals("") ? null : UtilEmploye.NUM, table.getSelectedRow(), 10);
                table.setValueAt(UtilEmploye.TABEL.equals("") ? null : UtilEmploye.TABEL, table.getSelectedRow(), 11);
                table.setValueAt(UtilEmploye.NAIM.equals("") ? null : UtilEmploye.NAIM, table.getSelectedRow(), 12);
                table.setValueAt(UtilEmploye.CATEGORY.equals("") ? null : UtilEmploye.CATEGORY, table.getSelectedRow(), 15);

                for (int i = 0; i < UtilEmploye.VECTOR_EMPL.size(); i++) {
                    Vector row = (Vector) UtilEmploye.VECTOR_EMPL.get(i);
                    if (i != 0) {
                        buttPlus.doClick();
                    }
                    table.setValueAt(row.get(0).equals("") ? null : row.get(0), table.getSelectedRow(), 10);
                    table.setValueAt(row.get(1).equals("") ? null : row.get(1), table.getSelectedRow(), 11);
                    table.setValueAt(row.get(2).equals("") ? null : row.get(2), table.getSelectedRow(), 12);
                    table.setValueAt(row.get(3).equals("") ? null : row.get(3), table.getSelectedRow(), 15);
                }

            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        if (JOptionPane.showOptionDialog(null, "Сохранить листок запуска?", "Сохранение...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION)
            buttSave.doClick();
        else
            buttCloseNotSave.doClick();
    }

    private void buttCloseNotSaveActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            Vector tempData = new Vector();
            tempData.add(dept.getSelectedItem());
            tempData.add(brig.getSelectedItem());
            tempData.add(brigadir.getText());
            tempData.add("'" + new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate()));
            tempData.add(marshrut.getText().trim());
            tempData.add(kolvo.getText().trim());
            tempData.add(modelNum.getText());
            tempData.add(modelNaim.getText());
            tempData.add(specNum.getText());
            tempData.add(specNaim.getText());
            tempData.add(note.getText());

            ZPlataOO oo = new ZPlataOO((idList > 0 ? " №" + idList : ""), tempData, tModel.getDataVector());
            oo.createReport("ZPlata(listZapuska).ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            boolean saveFlag = true;
            String str = "";

            if (((Item) dept.getSelectedItem()).getId() == -1) {
                saveFlag = false;
                str += "Вы не выбрали цех!\n";
            }

            if (((Item) brig.getSelectedItem()).getId() == -1) {
                saveFlag = false;
                str += "Вы не выбрали бригаду!\n";
            }

            if (modelNum.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не выбрали модель!\n";
            }

            if (specNum.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не выбрали спецификацию!\n";
            }

            if (kolvo.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели кол-во!\n";
            } else {
                if (checkList.isSelected()) {
                    int idOperac;
                    int sumOperac = 0;
                    Vector operac = new Vector();

                    for (int i = 0; i < tModel.getRowCount(); i++) {
                        idOperac = Integer.valueOf(tModel.getValueAt(i, 1).toString());

                        if (!operac.contains(idOperac)) {
                            sumOperac = 0;
                            operac.add(idOperac);

                            for (Iterator it = tModel.getDataVector().iterator(); it.hasNext(); ) {
                                Vector vrow = (Vector) it.next();
                                if (Integer.valueOf(vrow.get(1).toString()) == idOperac && vrow.get(13) != null)
                                    sumOperac += Integer.valueOf(vrow.get(13).toString());
                            }

                            if (sumOperac != Integer.valueOf(kolvo.getText().trim())) {
                                saveFlag = false;
                                str += "Некорректный ввод: сумма по операции " + tModel.getValueAt(i, 3) + " не совпадает с кол-вом листка!\n";
                                break;
                            }
                        }
                    }
                }
            }

            for (Iterator it = tModel.getDataVector().iterator(); it.hasNext(); ) {
                Vector vrow = (Vector) it.next();
                if ((vrow.get(10) == null && vrow.get(13) != null) || (vrow.get(10) != null && vrow.get(13) == null)) {
                    saveFlag = false;
                    str += "Некорректный ввод: нет ФИО или нет кол-ва в операции " + vrow.get(3) + "!\n";
                    break;
                }
            }

            if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate())))
                saveFlag = false;

            if (!saveFlag)
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

            if (saveFlag) {
                try {
                    int sumlist = 0;

                    for (int i = 0; i < tModel.getRowCount(); i++) {
                        if (tModel.getValueAt(i, 13) != null)
                            sumlist += Integer.valueOf(tModel.getValueAt(i, 13).toString());
                    }

                    if (saveFlag && sumlist > 0) {
                        try {
                            zpdb = new ZPlataPDB();
                            if (evt.getActionCommand().equals(UtilZPlata.BUTTON_ADD)) {
                                zpdb.addList(((Item) dept.getSelectedItem()).getId(),
                                        ((Item) brig.getSelectedItem()).getId(),
                                        Integer.valueOf(modelNum.getText().trim()),
                                        Integer.valueOf(specNum.getText().trim()),
                                        Integer.valueOf(user.getIdEmployee()),
                                        Integer.valueOf(kolvo.getText().trim()),
                                        marshrut.getText().trim(),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate())),
                                        note.getText().trim(),
                                        (status.isSelected() ? 1 : 0),
                                        tModel.getDataVector());

                                JOptionPane.showMessageDialog(null, "Листок запуска успешно добавлен! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            } else if (evt.getActionCommand().equals(UtilZPlata.BUTTON_SAVE)) {
                                zpdb.updateList(idList,
                                        ((Item) dept.getSelectedItem()).getId(),
                                        ((Item) brig.getSelectedItem()).getId(),
                                        Integer.valueOf(modelNum.getText().trim()),
                                        Integer.valueOf(specNum.getText().trim()),
                                        Integer.valueOf(user.getIdEmployee()),
                                        Integer.valueOf(kolvo.getText().trim()),
                                        marshrut.getText().trim(),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate())),
                                        note.getText().trim(),
                                        (status.isSelected() ? 1 : 0),
                                        tModel.getDataVector());

                                JOptionPane.showMessageDialog(null, "Листок запуска успешно изменен! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            }

                            try {
                                if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate()))) {
                                    UtilZPlata.DATE_VVOD = new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate());
                                    UtilFunctions.setSettingPropFile(new SimpleDateFormat("dd.MM.yyyy").format(sStdate.getDate()), UtilZPlata.SETTING_DATE_VVOD);
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Ошибка сохранения в файл настроек даты ввода! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            }

                            dispose();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            zpdb.disConn();
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "Листок запуска пуст!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttClearActionPerformed(ActionEvent evt) {
        try {
            for (int i = 0; i < table.getRowCount(); i++)
                if (table.isRowSelected(i)) {
                    for (int j = 10; j < table.getColumnCount(); j++)
                        table.setValueAt(null, i, j);
                    updateSumVyrabatRows();
                }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCopyTechActionPerformed(ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                Object idTech = table.getValueAt(table.getSelectedRow(), 4);
                int[] tt = table.getSelectedRows();
                Vector t1 = new Vector();
                Vector t = new Vector();

                for (int i = 0; i < tt.length; i++) {
                    if (idTech != table.getValueAt(tt[i], 4))
                        throw new Exception("Вы выбрали разные виды оборудования!");
                    else
                        t.add(tt[i]);
                }

                for (int i = 0; i < table.getRowCount(); i++) {
                    if (idTech == table.getValueAt(i, 4) && !Boolean.valueOf(table.getValueAt(i, 9).toString()) && !t.contains(i) && !t1.contains(i)) {
                        for (int k = 0; k < t.size(); k++) {
                            if (k != 0) {
                                if (table.getRowCount() > 0) {
                                    Vector data = new Vector();

                                    for (int l = 0; l < col.size(); l++)
                                        data.add(table.getValueAt(i, l));

                                    data.setElementAt(true, 9);

                                    tModel.insertRow(i + 1, data);
                                    i++;

                                    for (int j = 0; j < t.size(); j++) {
                                        int temp_i = Integer.valueOf(t.get(j).toString());
                                        if (i <= temp_i) {
                                            t.remove(j);
                                            t.add(j, ++temp_i);
                                        }
                                    }

                                    for (int j = 0; j < t1.size(); j++) {
                                        int temp_i = Integer.valueOf(t1.get(j).toString());
                                        if (i <= temp_i) {
                                            t1.remove(j);
                                            t1.add(j, ++temp_i);
                                        }
                                    }
                                    t1.add(i);
                                }
                            }

                            for (int j = 10; j < table.getColumnCount(); j++) {
                                if (j != 14)
                                    table.setValueAt(table.getValueAt(Integer.valueOf(t.get(k).toString()), j), i, j);
                            }
                        }
                    }
                }
            } else
                JOptionPane.showMessageDialog(null, "Вы не выбрали строку!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createZPlataDetalTable(final Vector rowAll) {
        tModel = new DefaultTableModel(rowAll, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowAll.isEmpty())
                    return super.getClass();
                else
                    return (col == 0) ? getValueAt(0, col).getClass() : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0 || col == 13) ? true : false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                try {
                    Vector rowVector = (Vector) dataVector.elementAt(row);

                    if (value != null) {
                        if (col == 13) {
                            rowVector.setElementAt(Integer.valueOf(value.toString()).intValue(), col);
                            rowVector.setElementAt(UtilZPlata.formatNorm(Double.valueOf(Double.valueOf(value.toString()) * Double.valueOf(table.getValueAt(row, 8).toString())), 2), col + 1);
                            fireTableCellUpdated(row, col + 1);
                            updateSumVyrabatRows();
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

        table.setModel(tModel);

        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(1);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(1);
        table.getColumnModel().getColumn(7).setPreferredWidth(30);
        table.getColumnModel().getColumn(8).setPreferredWidth(45);
        table.getColumnModel().getColumn(9).setMinWidth(0);
        table.getColumnModel().getColumn(9).setMaxWidth(0);
        table.getColumnModel().getColumn(10).setMinWidth(0);
        table.getColumnModel().getColumn(10).setMaxWidth(0);
        table.getColumnModel().getColumn(11).setPreferredWidth(10);
        table.getColumnModel().getColumn(12).setPreferredWidth(180);
        table.getColumnModel().getColumn(13).setPreferredWidth(30);
        table.getColumnModel().getColumn(14).setPreferredWidth(30);
        table.getColumnModel().getColumn(15).setMinWidth(0);
        table.getColumnModel().getColumn(15).setMaxWidth(0);

        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

        for (int i = 1; i < col.size(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);

        updateSumVyrabatRows();
    }

    private void updateSumVyrabatRows() {
        try {
            if (!sumVyrabatRow.getText().trim().equals("0"))
                sumVyrabatRow.setText("0");
            double sum = 0;

            for (int i = 0; i < table.getRowCount(); i++)
                if (table.getValueAt(i, 2) != null && table.getValueAt(i, 14) != null)
                    if (Integer.valueOf(table.getValueAt(i, 2).toString()) >= 0)
                        sum += Double.valueOf(table.getValueAt(i, 14).toString());

            if (sum > 0)
                sumVyrabatRow.setText(UtilZPlata.formatNorm(sum, 2));
        } catch (Exception ex) {
            sumVyrabatRow.setText("0");
            JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
