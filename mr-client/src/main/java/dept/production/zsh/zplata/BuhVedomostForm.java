package dept.production.zsh.zplata;

import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.ProgressBar;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class BuhVedomostForm extends javax.swing.JDialog {
    ButtonGroup buttonGroup;
    JRadioButton jRadioButton1;
    JRadioButton jRadioButton2;
    private JPanel osnovaPanel;
    private JPanel buttPanel;
    private JPanel headPanel;
    private JButton buttOpen;
    private JButton buttSave;
    private JButton buttClose;
    private JButton buttPrint;
    private JButton buttSelect;
    private JButton buttSelectDB;
    private JDateChooser vedDate;
    private JTextField idReport;
    private JTextField date;
    private JTextField dept;
    private JTextField brig;
    private JTextField period;
    private JTextField vedName;
    private ItemT4 dataT4;
    private Vector col;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter sorter;
    private TableFilterHeader filterHeader;
    private ZPlataPDB pdb;
    private ProgressBar pb;
    private String typeSearch;
    private JPanel headCentrPanel;
    private JPanel headTitlePanel;
    private String typeForm;
    private Vector tempRow;

    public BuhVedomostForm(java.awt.Dialog parent, boolean modal, String type) {
        super(parent, modal);

        typeForm = type;

        initPropSetting();
        init();
        cleanConstants();
        initData();

        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public BuhVedomostForm(java.awt.Frame parent, boolean modal, String type) {
        super(parent, modal);

        typeForm = type;

        initPropSetting();
        init();
        cleanConstants();
        initData();

        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void initPropSetting() {
        try {
            UtilZPlata.FOLDER_SELECT = UtilFunctions.readPropFileString(UtilZPlata.SETTING_FOLDER_SELECT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void init() {
        setMinimumSize(new Dimension(450, 350));
        setPreferredSize(new Dimension(850, 650));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        headPanel = new JPanel();
        headPanel.setLayout(new BorderLayout(1, 1));
        headPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        headTitlePanel = new JPanel();
        headTitlePanel.setLayout(new ParagraphLayout());

        headCentrPanel = new JPanel();
        headCentrPanel.setLayout(new ParagraphLayout());
        if (typeForm.equals(UtilZPlata.ADD))
            headCentrPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Данные из ведомости T4"));

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttClose = new JButton("Отмена");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttPrint = new JButton("Печать");
        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttPrintActionPerformed(evt);
            }
        });

        buttSelect = new JButton("Выбрать");
        buttSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSelectActionPerformed(evt);
            }
        });

        buttSelectDB = new JButton("Выбрать");
        buttSelectDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSelectDBActionPerformed(evt);
            }
        });

        buttSave = new JButton("Отправить");
        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttOpen = new JButton("Показать");
        buttOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOpenActionPerformed(evt);
            }
        });

        vedDate = new JDateChooser();
        vedDate.setPreferredSize(new Dimension(150, 20));

        vedName = new JTextField();
        vedName.setPreferredSize(new Dimension(350, 20));
        vedName.setEnabled(false);

        idReport = new JTextField();
        idReport.setPreferredSize(new Dimension(200, 20));
        idReport.setEnabled(false);

        date = new JTextField();
        date.setPreferredSize(new Dimension(200, 20));
        date.setEnabled(false);

        dept = new JTextField();
        dept.setPreferredSize(new Dimension(200, 20));
        dept.setEnabled(false);

        brig = new JTextField();
        brig.setPreferredSize(new Dimension(150, 20));
        brig.setEnabled(false);

        period = new JTextField();
        period.setPreferredSize(new Dimension(250, 20));
        period.setEnabled(false);

        dataT4 = new ItemT4();

        jRadioButton1 = new JRadioButton();
        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton1.setText("обычная;");
        jRadioButton1.setActionCommand("1");
        jRadioButton1.setSelected(true);

        jRadioButton2 = new JRadioButton();
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setText("большая(3 смены);");
        jRadioButton2.setActionCommand("3");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);

        col = new Vector();
        col.add("Табель №");
        col.add("ФИО");
        col.add("Профессия");
        col.add("Разряд");
        col.add("dataTabel");
        col.add("Бр.выр.");
        col.add("Итого выр.");
        col.add("Процент");
        col.add("Стоимость");
        col.add("Веч.ч.");
        col.add("Ноч.д.");//10  
        col.add("Ноч.ч.");
        col.add("Отр.д.");
        col.add("Отр.ч.");
        col.add("Дв.выр.");
        col.add("Вн.д.");
        col.add("Вн.ч.");
        col.add("Тар.д.");
        col.add("Тар.ч.");
        col.add("Ср.д.");
        col.add("Ср.ч.");  //10
        col.add("Вредность");
        col.add("+Стоимость");
        col.add("-Стоимость");
        col.add("Коф.уч.");
        col.add("ДК.выр.");
        col.add("Несор.");
        col.add("2/3T.д.");
        col.add("2/3T.ч.");
        col.add("Отпуск.д.");
        col.add("Б/лист.д.");  //10       
        col.add("Переход.д.");
        col.add("Соц.отп.д.");
        col.add("ДМ д.");
        col.add("Мед.ком. д.");
        col.add("Освоение проф.");
        col.add("Размер премии(%)");
        col.add("Уд. из премии(%)");

        table = new JTable();
        //  table.setAutoCreateColumnsFromModel(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        tModel = new DefaultTableModel();

        headTitlePanel.add(new JLabel("Отчетный месяц:"));
        if (typeForm.equals(UtilZPlata.ADD)) {
            headTitlePanel.add(vedDate);

            headTitlePanel.add(new JLabel("             Ведомость:"));
            headTitlePanel.add(jRadioButton1);
            headTitlePanel.add(jRadioButton2);
        } else if (typeForm.equals(UtilZPlata.OPEN)) {
            headTitlePanel.add(date);

        }

        if (typeForm.equals(UtilZPlata.ADD)) {
            headCentrPanel.add(new JLabel("Т4:"), ParagraphLayout.NEW_PARAGRAPH);
            headCentrPanel.add(vedName, ParagraphLayout.NEW_LINE_STRETCH_H);
            headCentrPanel.add(buttSelect);
        }

        headCentrPanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        headCentrPanel.add(dept);
        headCentrPanel.add(new JLabel("Бригада:"));
        headCentrPanel.add(brig);
        headCentrPanel.add(new JLabel("Период:"));
        headCentrPanel.add(period, ParagraphLayout.NEW_LINE_STRETCH_H);

        if (typeForm.equals(UtilZPlata.OPEN)) {
            headCentrPanel.add(buttSelectDB);
        }

        headPanel.add(headTitlePanel, BorderLayout.NORTH);
        headPanel.add(headCentrPanel, BorderLayout.CENTER);

        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        if (typeForm.equals(UtilZPlata.ADD)) {
            buttPanel.add(buttSave);
        } else if (typeForm.equals(UtilZPlata.OPEN)) {
            buttPanel.add(buttOpen);
        }

        osnovaPanel.add(headPanel, BorderLayout.NORTH);
        osnovaPanel.add(new JScrollPane(table,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttSelectDBActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true);

            if (UtilZPlata.ACTION_BUTT_SELECT_SEARCH) {
                idReport.setText(UtilZPlata.ITEM_SELECT_SEARCH_ID);
                date.setText(UtilZPlata.ITEM_SELECT_SEARCH_DATE);
                dept.setText(UtilZPlata.ITEM_SELECT_SEARCH_DEPT);
                brig.setText(UtilZPlata.ITEM_SELECT_SEARCH_BRIG);
                period.setText(UtilZPlata.ITEM_SELECT_SEARCH_PERIOD);
            } else {
                idReport.setText("");
                date.setText("");
                dept.setText("");
                brig.setText("");
                period.setText("");
            }

        } catch (Exception e) {
            idReport.setText("");
            date.setText("");
            dept.setText("");
            brig.setText("");
            period.setText("");
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        tempRow = new Vector();
        try {
            pb = new ProgressBar(this, false, "Обработка данных ...");
            SwingWorker sw = new SwingWorker() {

                protected Object doInBackground() {
                    try {
                        pdb = new ZPlataPDB();

                        tempRow = pdb.getDataReportList(Integer.valueOf(idReport.getText().trim()));

                    } catch (Exception e) {
                        tempRow = new Vector();
                        JOptionPane.showMessageDialog(null,
                                "Ошибка. " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);

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

            createTable(tempRow);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        final JFileChooser fc = new JFileChooser(UtilZPlata.FOLDER_SELECT);
        fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null) {
                    if (f.isDirectory()) {
                        return true;
                    }
                }
                if (f.getName().toLowerCase().endsWith(".ods")) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".xlsx")) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".xls")) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".csv")) {
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

        if (fc.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {

            if (fc.getSelectedFile().exists()) {
                pb = new ProgressBar(this, false, "Проверка и загрузка файла ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            ZPlataOO oo = new ZPlataOO(fc.getSelectedFile().getPath(), 1);
                            dataT4 = oo.openReport(
                                    BuhVedomostForm.this,
                                    (Integer.valueOf(buttonGroup.getSelection().getActionCommand()) == 1) ?
                                            UtilZPlata.OTCHET_T4_NEW :
                                            UtilZPlata.OTCHET_T4_PL_NEW,
                                    Integer.valueOf(buttonGroup.getSelection().getActionCommand()));
                        } catch (Exception ex) {
                            dataT4 = new ItemT4();

                            JOptionPane.showMessageDialog(
                                    null,
                                    "Ошибка чтения ведомости: " + ex.getMessage(),
                                    "Ошибка!",
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

                vedName.setText(fc.getSelectedFile().getPath());
                dept.setText(dataT4.getDept());
                brig.setText(dataT4.getBrig());
                period.setText(dataT4.getPeriod());
                createTable(getData(dataT4.getDataPeople()));

            } else {
                vedName.setText("не выбран");
                dept.setText("");
                brig.setText("");
                period.setText("");
                createTable(new Vector());

                JOptionPane.showMessageDialog(
                        null,
                        "Файл не существует!",
                        "Ошибка!",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            BuhVedomostForm.this.toFront(); // на самый верх
        }
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            boolean saveFlag = true;
            String str = "";


            if (vedName.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не выбрали ведомость!\n";
            }

            if (tModel.getDataVector().size() <= 0) {
                saveFlag = false;
                str += "Нет данных для сохранения!\n";
            }

            if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(vedDate.getDate()))) {

                java.util.Date d = (vedDate.getDate());
                d.setDate(1);
                vedDate.setDate(d);

                saveFlag = false;
            }

            if (!saveFlag) {
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            if (saveFlag) {
                dataT4.setDate(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(vedDate.getDate())));

                pb = new ProgressBar(this, false, "Отправка данных ...");
                SwingWorker sw = new SwingWorker() {

                    protected Object doInBackground() {
                        try {
                            pdb = new ZPlataPDB();

                            if (pdb.addImportReportVedtT4(dataT4)) {

                                JOptionPane.showMessageDialog(null,
                                        "Данные успешно отправлены! ",
                                        "Завершено",
                                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                dispose();
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,
                                    "Ошибка. " + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);

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

                try {
                    UtilZPlata.FOLDER_SELECT = vedName.getText();
                    UtilFunctions.setSettingPropFile(vedName.getText(), UtilZPlata.SETTING_FOLDER_SELECT);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttPrintActionPerformed(ActionEvent evt) {
        try {
            ZPlataOO oo = new ZPlataOO("цех - " + dept.getText().trim() +
                    ", бригада - " + brig.getText().trim() +
                    ", период - " + period.getText().trim(),
                    tModel,
                    table.getColumnModel());
            oo.createReport("DefaultTableAlbumFormatCheck.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initData() {
        if (typeForm.equals(UtilZPlata.ADD))
            this.setTitle("Отправка ведомости Т4");

        else if (typeForm.equals(UtilZPlata.OPEN))
            this.setTitle("Просмотр переданных данных ведомости Т4");

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);
        d.setMonth(d.getMonth() - 1);

        vedDate.setDate(d);

        createTable(new Vector());
    }

    private void cleanConstants() {
        dataT4 = new ItemT4();
    }

    private void createTable(Vector row) {
        tModel = new DefaultTableModel(row, col) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);
    }


    private Vector getData(Vector data) {
        Vector rez = new Vector();

        try {

            for (int i = 0; i < data.size(); i++) {
                ItemBuh tmpBuh = (ItemBuh) data.get(i);
                Vector tmp = new Vector();
                tmp.add(tmpBuh.getTabelNum());
                tmp.add(tmpBuh.getFio());
                tmp.add(tmpBuh.getProf());
                tmp.add(tmpBuh.getRazryad());
                tmp.add(tmpBuh.getDataTabel());
                tmp.add(tmpBuh.getVyrBrig());
                tmp.add(tmpBuh.getVyrItog());
                tmp.add(tmpBuh.getProcent());
                tmp.add(tmpBuh.getStoim());
                tmp.add(tmpBuh.getClVech());
                tmp.add(tmpBuh.getdNoch());
                tmp.add(tmpBuh.getClNoch());
                tmp.add(tmpBuh.getdOtrab());
                tmp.add(tmpBuh.getClOtrab());
                tmp.add(tmpBuh.getVyrX2());
                tmp.add(tmpBuh.getdVnedr());
                tmp.add(tmpBuh.getClVnedr());
                tmp.add(tmpBuh.getdTarif());
                tmp.add(tmpBuh.getClTarif());
                tmp.add(tmpBuh.getdSredn());
                tmp.add(tmpBuh.getClSredn());
                tmp.add(tmpBuh.getVrednost());
                tmp.add(tmpBuh.getStoimPlus());
                tmp.add(tmpBuh.getStoimMinus());
                tmp.add(tmpBuh.getKofUch());
                tmp.add(tmpBuh.getVyrDK());
                tmp.add(tmpBuh.getNesort());
                tmp.add(tmpBuh.getdTarif23());
                tmp.add(tmpBuh.getClTarif23());
                tmp.add(tmpBuh.getdOtpusk());
                tmp.add(tmpBuh.getdBList());
                tmp.add(tmpBuh.getdPerevod());
                tmp.add(tmpBuh.getdSOtpusk());
                tmp.add(tmpBuh.getdDMateri());
                tmp.add(tmpBuh.getdMk());
                tmp.add(tmpBuh.getVyrOsvoen());
                tmp.add(tmpBuh.getPercent());
                tmp.add(tmpBuh.getMinusPercent());

                rez.add(tmp);
            }

        } catch (Exception e) {
            rez = new Vector();
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return rez;
    }

}
