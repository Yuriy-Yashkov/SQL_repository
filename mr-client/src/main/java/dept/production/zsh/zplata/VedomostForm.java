package dept.production.zsh.zplata;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.LogCrutch;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.ProgressBar;
import common.UtilFunctions;
import dept.sprav.employe.EmployeForm;
import dept.sprav.employe.UtilEmploye;
import dept.sprav.model.ModelForm;
import dept.sprav.model.UtilModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
//import org.apache.log4j.Logger;

/**
 *
 * @author lidashka
 */
public class VedomostForm extends javax.swing.JDialog {
    private static final LogCrutch log = new LogCrutch();//Log().getLoger(VedomostForm.class);
    JButton buttProduction;
    JButton buttKolvo;
    JButton buttNesort;
    JButton buttWork;
    JButton buttTech;
    JButton buttonTech;
    JButton buttOtchet;
    JButton buttClose;
    JDateChooser sStDate;
    JDateChooser eStDate;
    JPanel osnova;
    JPanel searchPanel;
    JPanel buttPanel;
    JLabel title;
    JComboBox dept;
    JComboBox brig;
    JCheckBox daySaturday;
    JCheckBox daySunday;
    JCheckBox jobEvaluation;
    JLabel searchNum;
    JLabel searchNaim;
    JButton buttSearch;
    ButtonGroup buttonGroup;
    JRadioButton jRadioButton1;
    JRadioButton jRadioButton2;
    JRadioButton jRadioButton3;
    JRadioButton jRadioButton4;
    String titleOtchet;
    Vector model;
    ZPlataPDB zpdb;
    private ProgressBar pb;
    private Vector dataTech;
    private MainController controller;

    public VedomostForm(MainController mainController, boolean modal, String title, Date sDate, Date eDate) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        this.titleOtchet = title;

        setPreferredSize(new Dimension(480, 230));

        if (titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW)) {
            setTitle("Ведомость (форма Т4)");
            setPreferredSize(new Dimension(480, 260));
        } else if (titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW_BY_PERSONAL_NUMBER)) {
            setTitle("Ведомость (форма Т4 по таб.№)");
            setPreferredSize(new Dimension(480, 260));
        } else if (titleOtchet.equals(UtilZPlata.OTCHET_NV)) {
            setTitle("Накопительная ведомость");
            setPreferredSize(new Dimension(480, 280));
        } else if (titleOtchet.equals(UtilZPlata.OTCHET_NV_EMPL)) {
            setTitle("Накопительная ведомость по рабочим");
            setPreferredSize(new Dimension(480, 270));
        } else if (titleOtchet.equals(UtilZPlata.OTCHET_NV_MODEL)) {
            setTitle("Накопительная ведомость по моделям");
        }

        init();

        if (titleOtchet.equals(UtilZPlata.OTCHET_NV)) {
            jRadioButton1.setText(" Чистые ед. + брак;");
            jRadioButton2.setText(" Кол-во по маршруту;");
            jRadioButton3.setText(" Краткий вид;");

            searchPanel.add(new JLabel(" Выработка:"), ParagraphLayout.NEW_PARAGRAPH);
            searchPanel.add(jRadioButton1);
            searchPanel.add(jRadioButton2, ParagraphLayout.NEW_LINE);
            searchPanel.add(jRadioButton3, ParagraphLayout.NEW_LINE);
        } else if (titleOtchet.equals(UtilZPlata.OTCHET_NV_EMPL)) {
            jRadioButton1.setText(" Краткий;");
            jRadioButton2.setText(" Развёрнутый;");

            searchPanel.add(new JLabel(" Вид:"), ParagraphLayout.NEW_PARAGRAPH);
            searchPanel.add(jRadioButton1);
            searchPanel.add(jRadioButton2);

            searchPanel.add(new JLabel(" Рабочий:"), ParagraphLayout.NEW_PARAGRAPH);
            searchPanel.add(searchNum);
            searchPanel.add(searchNaim);
            searchPanel.add(buttSearch);

        } else if (titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW)
                || titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW_BY_PERSONAL_NUMBER)) {
            jRadioButton1.setText(" 1 смена;");
            jRadioButton2.setText(" 2 смены;");
            jRadioButton3.setText(" 3 смены;");

            searchPanel.add(new JLabel(" Смены:"), ParagraphLayout.NEW_PARAGRAPH);
            searchPanel.add(jRadioButton1);
            searchPanel.add(jRadioButton2);
            searchPanel.add(jRadioButton3);

            searchPanel.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
            searchPanel.add(daySaturday);
            searchPanel.add(daySunday);
        } else if (titleOtchet.equals(UtilZPlata.OTCHET_NV_MODEL)) {
            searchPanel.add(new JLabel(" Модель:"), ParagraphLayout.NEW_PARAGRAPH);
            searchPanel.add(searchNum);
            searchPanel.add(searchNaim);
            searchPanel.add(buttSearch);
        }

        buttPanel.add(buttOtchet);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate)) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate))) {

                sStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(sDate)));
                eStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(eDate)));

            } else {
                sStDate.setDate(d);
                eStDate.setDate((Calendar.getInstance()).getTime());
            }
        } catch (Exception e) {
            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public VedomostForm(MainController mainController, boolean modal, Date sDate, Date eDate) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Выработка");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setPreferredSize(new Dimension(500, 230));

        init();

        jRadioButton1.setText(" Рабочий;");
        jRadioButton2.setText(" Бригада;");
        jRadioButton3.setText(" Модель;");
        jRadioButton4.setText(" Маршрут;");

        searchPanel.add(new JLabel(" Выработка:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(jRadioButton3);
        searchPanel.add(jRadioButton4);

        buttPanel.add(buttProduction);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate)) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate))) {

                sStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(sDate)));
                eStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(eDate)));

            } else {
                sStDate.setDate(d);
                eStDate.setDate((Calendar.getInstance()).getTime());
            }
        } catch (Exception e) {
            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public VedomostForm(MainController mainController, boolean modal, Date sDate, Date eDate, String t) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Количество");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setPreferredSize(new Dimension(480, 230));

        init();

        jRadioButton1.setText(" Маршрут;");
        jRadioButton2.setText(" Модель;");
        jRadioButton3.setText(" Ошибка в листке;");

        searchPanel.add(new JLabel(" Кол-во по:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(jRadioButton3);

        buttPanel.add(buttKolvo);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate)) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate))) {

                sStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(sDate)));
                eStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(eDate)));

            } else {
                sStDate.setDate(d);
                eStDate.setDate((Calendar.getInstance()).getTime());
            }
        } catch (Exception e) {
            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public VedomostForm(MainController mainController, boolean modal, boolean flag, Date sDate, Date eDate) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Выполнение");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setPreferredSize(new Dimension(480, 310));

        init();

        jRadioButton1.setText(" Учитывать;");
        jRadioButton2.setText(" Не учитывать;");

        searchPanel.add(new JLabel(" Разряд:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(daySaturday);
        searchPanel.add(daySunday);
        searchPanel.add(new JLabel(), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(jobEvaluation);

        buttPanel.add(buttWork);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate)) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate))) {

                sStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(sDate)));
                eStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(eDate)));

            } else {
                sStDate.setDate(d);
                eStDate.setDate((Calendar.getInstance()).getTime());
            }
        } catch (Exception e) {
            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public VedomostForm(MainController mainController, Date sDate, Date eDate) {
        super(mainController.getMainForm(), true);
        controller = mainController;
        setTitle("Отчет по бригадам");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setPreferredSize(new Dimension(480, 200));

        init();

        buttPanel.add(buttonTech);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate)) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate))) {

                sStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(sDate)));
                eStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(eDate)));

            } else {
                sStDate.setDate(d);
                eStDate.setDate((Calendar.getInstance()).getTime());
            }
        } catch (Exception e) {
            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public VedomostForm(MainController mainController, boolean modal, boolean flag, Date sDate, Date eDate, String text) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Загрузка оборудования");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setPreferredSize(new Dimension(480, 200));

        init();

        buttPanel.add(buttTech);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate)) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate))) {

                sStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(sDate)));
                eStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(eDate)));

            } else {
                sStDate.setDate(d);
                eStDate.setDate((Calendar.getInstance()).getTime());
            }
        } catch (Exception e) {
            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public VedomostForm(MainController mainController, boolean modal, Date sDate, Date eDate, boolean flag) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Несортные изделия");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setPreferredSize(new Dimension(480, 200));

        init();

        buttPanel.add(buttNesort);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate)) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate))) {

                sStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(sDate)));
                eStDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(eDate)));

            } else {
                sStDate.setDate(d);
                eStDate.setDate((Calendar.getInstance()).getTime());
            }
        } catch (Exception e) {
            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    private void init() {
        osnova = new JPanel();
        searchPanel = new JPanel();
        buttPanel = new JPanel();
        dept = new JComboBox(UtilZPlata.DEPT_MODEL);
        brig = new JComboBox();
        daySaturday = new JCheckBox(" суббота;");
        daySunday = new JCheckBox(" воскресенье;");
        jobEvaluation = new JCheckBox("учитывать только табельный;");
        buttProduction = new JButton("Выработка");
        buttKolvo = new JButton("Количество");
        buttNesort = new JButton("Несортные изд.");
        buttWork = new JButton("Выполнение");
        buttTech = new JButton("Оборудование");
        buttonTech = new JButton("Сформировать отчёт");
        buttOtchet = new JButton("Сформировать отчёт");
        buttClose = new JButton("Закрыть");
        buttSearch = new JButton("Найти");
        sStDate = new JDateChooser();
        eStDate = new JDateChooser();
        buttonGroup = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        searchPanel.setLayout(new ParagraphLayout());
        buttPanel.setLayout(new GridLayout(0, 2, 5, 5));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dept.setPreferredSize(new Dimension(300, 20));
        brig.setPreferredSize(new Dimension(300, 20));
        sStDate.setPreferredSize(new Dimension(200, 20));
        eStDate.setPreferredSize(new Dimension(200, 20));

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        daySaturday.setFont(new java.awt.Font("Dialog", 0, 13));
        daySunday.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        daySaturday.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        daySunday.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setActionCommand("1");
        jRadioButton2.setActionCommand("2");
        jRadioButton3.setActionCommand("3");
        jRadioButton4.setActionCommand("4");

        jRadioButton1.setSelected(true);
        daySaturday.setSelected(true);

        searchNum = new JLabel();
        searchNaim = new JLabel();
        searchNum.setPreferredSize(new Dimension(70, 20));
        searchNaim.setPreferredSize(new Dimension(210, 20));
        searchNum.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        searchNaim.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        try {
            dept.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.DEPT_MODEL, UtilZPlata.DEPT_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

        buttProduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttProductionActionPerformed(evt);
            }
        });

        buttKolvo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttKolvoActionPerformed(evt);
            }
        });

        buttNesort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttNesortActionPerformed(evt);
            }
        });

        buttWork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttWorkActionPerformed(evt);
            }
        });

        buttTech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTechActionPerformed(evt);
            }
        });

        buttonTech.addActionListener(evt -> buttonTechActionPerformed(evt));

        buttOtchet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOtchetActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        buttSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (titleOtchet.equals(UtilZPlata.OTCHET_NV_EMPL)) {
                    buttEmployeActionPerformed(evt);
                } else if (titleOtchet.equals(UtilZPlata.OTCHET_NV_MODEL)) {
                    buttModelActionPerformed(evt);
                }
            }
        });

        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton4);

        searchPanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(dept);
        searchPanel.add(new JLabel("Бригада:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(brig);
        searchPanel.add(new JLabel("Период:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel("    c   "), ParagraphLayout.NEW_LINE);
        searchPanel.add(sStDate);
        searchPanel.add(new JLabel("    по "), ParagraphLayout.NEW_LINE);
        searchPanel.add(eStDate);

        osnova.add(searchPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttModelActionPerformed(ActionEvent evt) {
        try {
            new ModelForm(controller, true, true);

            searchNum.setText("");
            searchNaim.setText("");

            searchNum.setText(UtilModel.MODEL);
            searchNaim.setText(UtilModel.NAIM);

        } catch (Exception e) {
            searchNum.setText("");
            searchNaim.setText("");

            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEmployeActionPerformed(ActionEvent evt) {
        try {
            new EmployeForm(
                    controller,
                    true,
                    UtilEmploye.TYPE_SEARCH_BRIGNUM,
                    ((Item) brig.getSelectedItem()).getId());

            searchNum.setText("");
            searchNaim.setText("");

            searchNum.setText(UtilEmploye.NUM);
            searchNaim.setText(UtilEmploye.TABEL + " " + UtilEmploye.NAIM);

        } catch (Exception e) {
            searchNum.setText("");
            searchNaim.setText("");
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttProductionActionPerformed(ActionEvent evt) {
        try {
            Vector dataProduction = new Vector();
            String tempSrt = " ";
            zpdb = new ZPlataPDB();

            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))) {

                tempSrt += "Цех: ";
                tempSrt += (((Item) dept.getSelectedItem()).getDescription());
                tempSrt += ", бригада: ";
                tempSrt += (brig.getSelectedItem().toString());
                tempSrt += ", за";
                tempSrt += (" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                        " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()));

                if (buttonGroup.getSelection().getActionCommand().equals("1"))
                    dataProduction = zpdb.getProductionEmployeesTable(
                            ((Item) dept.getSelectedItem()).getId(),
                            ((Item) brig.getSelectedItem()).getId(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));
                else if (buttonGroup.getSelection().getActionCommand().equals("2"))
                    dataProduction = zpdb.getProductionBrigTable(
                            ((Item) dept.getSelectedItem()).getId(),
                            ((Item) brig.getSelectedItem()).getId(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));
                else if (buttonGroup.getSelection().getActionCommand().equals("3"))
                    dataProduction = zpdb.getProductionModelTable(
                            ((Item) dept.getSelectedItem()).getId(),
                            ((Item) brig.getSelectedItem()).getId(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));
                else if (buttonGroup.getSelection().getActionCommand().equals("4"))
                    dataProduction = zpdb.getProductionMarshrutTable(
                            ((Item) dept.getSelectedItem()).getId(),
                            ((Item) brig.getSelectedItem()).getId(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                new ZPlataTableForm(controller, true,
                        dataProduction,
                        tempSrt,
                        buttonGroup.getSelection().getActionCommand(),
                        new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()),
                        new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()),
                        ((Item) dept.getSelectedItem()).getId(),
                        ((Item) brig.getSelectedItem()).getId());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }
    }

    private void buttKolvoActionPerformed(ActionEvent evt) {
        try {
            Vector dataKolvo = new Vector();
            String tempSrt = " ";
            String type = "";
            zpdb = new ZPlataPDB();

            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))) {

                tempSrt += "Цех: ";
                tempSrt += (((Item) dept.getSelectedItem()).getDescription());
                tempSrt += ", бригада: ";
                tempSrt += (brig.getSelectedItem().toString());
                tempSrt += ", за";
                tempSrt += (" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                        " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()));

                if (jRadioButton1.isSelected()) type = UtilZPlata.KOLVO_MARSHRUT;
                else if (jRadioButton2.isSelected()) type = UtilZPlata.KOLVO_MODEL;
                else if (jRadioButton3.isSelected()) type = UtilZPlata.KOLVO_ERROR;

                if (type.equals(UtilZPlata.KOLVO_MARSHRUT) || type.equals(UtilZPlata.KOLVO_MODEL)) {
                    if (type.equals(UtilZPlata.KOLVO_MODEL))
                        dataKolvo = zpdb.getKolvoModelTable(
                                ((Item) dept.getSelectedItem()).getId(),
                                ((Item) brig.getSelectedItem()).getId(),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                    else if (type.equals(UtilZPlata.KOLVO_MARSHRUT))
                        dataKolvo = zpdb.getKolvoMarshrutTable(
                                ((Item) dept.getSelectedItem()).getId(),
                                ((Item) brig.getSelectedItem()).getId(),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                    new ZPlataTableForm(controller, true,
                            dataKolvo,
                            tempSrt,
                            type,
                            new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()),
                            new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()),
                            ((Item) dept.getSelectedItem()).getId(),
                            ((Item) brig.getSelectedItem()).getId());

                } else if (type.equals(UtilZPlata.KOLVO_ERROR)) {
                    Vector rowData = new Vector();
                    Vector data = new Vector();
                    String titleData = "Ошибки в листках запуска!";

                    rowData = zpdb.getDetalKolvoErrorList(
                            ((Item) dept.getSelectedItem()).getId(),
                            ((Item) brig.getSelectedItem()).getId(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                    new ZPlataSmallTableForm(controller, true, titleData, rowData, data, type);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }
    }

    private void buttNesortActionPerformed(ActionEvent evt) {
        try {
            String tempSrt = " ";
            zpdb = new ZPlataPDB();

            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))) {

                tempSrt += "Цех: ";
                tempSrt += (((Item) dept.getSelectedItem()).getDescription());
                tempSrt += ", бригада: ";
                tempSrt += (brig.getSelectedItem().toString());
                tempSrt += ", за";
                tempSrt += (" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                        " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()));

                new NesortForm(controller, true,
                        ((Item) dept.getSelectedItem()).getId(),
                        ((Item) brig.getSelectedItem()).getId(),
                        new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()),
                        new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()),
                        tempSrt);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }
    }

    private void buttWorkActionPerformed(ActionEvent evt) {
        try {
            Vector dataWork = new Vector();
            String tempSrt = " ";
            zpdb = new ZPlataPDB();

            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))) {

                if (new SimpleDateFormat("MM.yyyy").format(sStDate.getDate()).equals(new SimpleDateFormat("MM.yyyy").format(eStDate.getDate()))) {
                    if (((Item) dept.getSelectedItem()).getId() != -1 && ((Item) brig.getSelectedItem()).getId() != -1) {
                        tempSrt += "Цех: ";
                        tempSrt += (((Item) dept.getSelectedItem()).getDescription());
                        tempSrt += ", бригада: ";
                        tempSrt += (brig.getSelectedItem().toString());
                        tempSrt += ", за";
                        tempSrt += (" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                                " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()));

                        if (jobEvaluation.isSelected()) {
                            dataWork = zpdb.getWorkEmployeTableByOnlyJobEvaluation(((Item) dept.getSelectedItem()).getId(),
                                    ((Item) brig.getSelectedItem()).getId(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                                    jRadioButton1.isSelected() ? true : false,
                                    UtilFunctions.getWorkingDays(
                                            Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()).substring(0, 2)),
                                            Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()).substring(0, 2)),
                                            Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()).substring(3, 5)),
                                            Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()).substring(6, 10)),
                                            5,
                                            daySaturday.isSelected(),
                                            daySunday.isSelected()).size() * 8);
                        } else {
                            dataWork = zpdb.getWorkEmployeTable(((Item) dept.getSelectedItem()).getId(),
                                    ((Item) brig.getSelectedItem()).getId(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                                    jRadioButton1.isSelected() ? true : false,
                                    UtilFunctions.getWorkingDays(
                                            Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()).substring(0, 2)),
                                            Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()).substring(0, 2)),
                                            Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()).substring(3, 5)),
                                            Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()).substring(6, 10)),
                                            5,
                                            daySaturday.isSelected(),
                                            daySunday.isSelected()).size() * 8);
                        }

                        new WorkForm(this, true, dataWork, tempSrt, jRadioButton1.isSelected() ? true : false);
                    } else
                        JOptionPane.showMessageDialog(null, "Цех и бригаду нужно выбрать обязательно!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(null, "Отчёт можно формировать только за один месяц!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }
    }

    private void buttTechActionPerformed(ActionEvent evt) {
        try {
            dataTech = new Vector();
            String tempSrt = " ";

            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))) {

                if (new SimpleDateFormat("MM.yyyy").format(sStDate.getDate())
                        .equals(new SimpleDateFormat("MM.yyyy").format(eStDate.getDate()))) {

                    if (((Item) dept.getSelectedItem()).getId() != -1) {

                        tempSrt += "Цех: ";
                        tempSrt += (((Item) dept.getSelectedItem()).getDescription());
                        tempSrt += ", бригада(ы): ";
                        if(((Item) brig.getSelectedItem()).getId() != -1)
                            tempSrt += (brig.getSelectedItem().toString());
                        else
                            tempSrt += "Все";
                        tempSrt += ", за";
                        tempSrt += (" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                                " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()));
                        if (((Item) brig.getSelectedItem()).getId() != -1) {
                            pb = new ProgressBar(this, false, "Обработка данных ...");
                            SwingWorker sw = new SwingWorker() {
                                protected Object doInBackground() {
                                    try {
                                        zpdb = new ZPlataPDB();
                                        dataTech = zpdb.getLoadTechBrig(
                                                ((Item) dept.getSelectedItem()).getId(),
                                                ((Item) brig.getSelectedItem()).getId(),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                                    } catch (Exception e) {
                                        dataTech = new Vector();

                                        JOptionPane.showMessageDialog(null,
                                                "Ошибка. " + e.getMessage(),
                                                "Ошибка",
                                                javax.swing.JOptionPane.ERROR_MESSAGE);
                                    } finally {
                                        zpdb.disConn();
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
                            pb = new ProgressBar(this, false, "Обработка данных ...");
                            SwingWorker sw = new SwingWorker() {
                                protected Object doInBackground() {
                                    try {
                                        zpdb = new ZPlataPDB();
                                        dataTech = zpdb.getLoadTechAllBrig(
                                                ((Item) dept.getSelectedItem()).getId(),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                                    } catch (Exception e) {
                                        dataTech = new Vector();

                                        JOptionPane.showMessageDialog(null,
                                                "Ошибка. " + e.getMessage(),
                                                "Ошибка",
                                                javax.swing.JOptionPane.ERROR_MESSAGE);
                                    } finally {
                                        zpdb.disConn();
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

                        new TableTechForm(this, true, dataTech, tempSrt);

                    } else
                        JOptionPane.showMessageDialog(null,
                                "Цех и бригаду нужно выбрать обязательно!",
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(null,
                            "Отчёт можно формировать только за один месяц!",
                            "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttonTechActionPerformed(ActionEvent evt) {

        ZPlataOO oo = new ZPlataOO();
        int brigadeId = ((Item) brig.getSelectedItem()).getId();
        int departmentId = ((Item) dept.getSelectedItem()).getId();
        ZPlataPDB zpdb = new ZPlataPDB();

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))) {

                java.sql.Date sDate = new java.sql.Date(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())));
                java.sql.Date eDate = new java.sql.Date(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                if (departmentId != -1 && brigadeId != -1) {
                    oo.getReport(zpdb, sDate, eDate, departmentId, brigadeId);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOtchetActionPerformed(ActionEvent evt) {
        try {
            Vector tempOtchet = new Vector();
            Vector brigOtchet = new Vector();
            Vector daysOtchet = new Vector();
            Vector vtoOtchet = new Vector();
            Vector kolOtchet = new Vector();
            Vector defectOtchet = new Vector();
            double brakOtchet = 0;
            int idEmployer = -1;

            zpdb = new ZPlataPDB();

            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))) {
                if (titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW)
                        || titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW_BY_PERSONAL_NUMBER)) {
                    if (new SimpleDateFormat("MM.yyyy").format(sStDate.getDate()).equals(new SimpleDateFormat("MM.yyyy").format(eStDate.getDate()))) {
                        if (((Item) dept.getSelectedItem()).getId() != -1 && ((Item) brig.getSelectedItem()).getId() != -1) {

                            tempOtchet.add(((Item) dept.getSelectedItem()).getDescription());
                            tempOtchet.add(brig.getSelectedItem().toString());
                            tempOtchet.add(" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                                    " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()));
                            daysOtchet = UtilFunctions.getWorkingDays(Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()).substring(0, 2)),
                                    Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()).substring(0, 2)),
                                    Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()).substring(3, 5)),
                                    Integer.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()).substring(6, 10)),
                                    5,
                                    daySaturday.isSelected(),
                                    daySunday.isSelected());

                            brakOtchet = zpdb.getProductionBrakBrigOtchet(
                                    ((Item) brig.getSelectedItem()).getId(),
                                    ((Item) dept.getSelectedItem()).getId(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                            brigOtchet = zpdb.getProductionBrigOtchetNEW(
                                    ((Item) brig.getSelectedItem()).getId(),
                                    ((Item) dept.getSelectedItem()).getId(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                            if (((Item) dept.getSelectedItem()).getId() == 8)
                                vtoOtchet = zpdb.getProductionVtoOtchet(
                                        ((Item) brig.getSelectedItem()).getId(),
                                        ((Item) dept.getSelectedItem()).getId(),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                            ZPlataOO oo = new ZPlataOO(tempOtchet,
                                    brigOtchet,
                                    daysOtchet,
                                    Integer.parseInt(buttonGroup.getSelection().getActionCommand()),
                                    brakOtchet,
                                    vtoOtchet,
                                    Integer.parseInt(((Item) brig.getSelectedItem()).getDescription().trim()));
                            if (!((Item) dept.getSelectedItem()).getDescription().equals("Цех швейный")) {
                                if (titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW))
                                    oo.createReport((Integer.parseInt(buttonGroup.getSelection().getActionCommand()) == 1)
                                            ? UtilZPlata.OTCHET_T4_NEW
                                            : UtilZPlata.OTCHET_T4_PL_NEW);

                                else if (titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW_BY_PERSONAL_NUMBER))
                                    oo.createReport((Integer.parseInt(buttonGroup.getSelection().getActionCommand()) == 1)
                                            ? UtilZPlata.OTCHET_T4_NEW_BY_PERSONAL_NUMBER
                                            : UtilZPlata.OTCHET_T4_PL_NEW_BY_PERSONAL_NUMBER);
                            } else {
                                if (titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW))
                                    oo.createReport((Integer.parseInt(buttonGroup.getSelection().getActionCommand()) == 1)
                                            ? UtilZPlata.OTCHET_T4_NEW_SEWING_WORKSHOP
                                            : UtilZPlata.OTCHET_T4_PL_NEW_SEWING_WORKSHOP);

                                else if (titleOtchet.equals(UtilZPlata.OTCHET_T4_NEW_BY_PERSONAL_NUMBER))
                                    oo.createReport((Integer.parseInt(buttonGroup.getSelection().getActionCommand()) == 1)
                                            ? UtilZPlata.OTCHET_T4_NEW_BY_PERSONAL_NUMBER_SEWING_WORKSHOP
                                            : UtilZPlata.OTCHET_T4_PL_NEW_BY_PERSONAL_NUMBER_SEWING_WORKSHOP);
                            }
                        } else
                            JOptionPane.showMessageDialog(null, "Цех и бригаду нужно выбрать обязательно!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } else
                        JOptionPane.showMessageDialog(null, "Ведомость выработки в нормо-часах и расчёт заработной\n"
                                + "платы рабочим можно формировать только за один месяц!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);

                } else if (titleOtchet.equals(UtilZPlata.OTCHET_NV)) {
                    tempOtchet.add(((Item) dept.getSelectedItem()).getDescription());
                    tempOtchet.add(brig.getSelectedItem().toString());
                    tempOtchet.add(" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                            " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()));
                    tempOtchet.add(zpdb.getAllKolModel(
                            ((Item) brig.getSelectedItem()).getId(),
                            ((Item) dept.getSelectedItem()).getId(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))));

                    if (buttonGroup.getSelection().getActionCommand().equals("1") || buttonGroup.getSelection().getActionCommand().equals("2"))
                        brigOtchet = zpdb.getProductionOtchetNV(
                                ((Item) brig.getSelectedItem()).getId(),
                                ((Item) dept.getSelectedItem()).getId(),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                                buttonGroup.getSelection().getActionCommand());

                    else if (buttonGroup.getSelection().getActionCommand().equals("3"))
                        brigOtchet = zpdb.getProductionOtchetNVShvei(
                                ((Item) brig.getSelectedItem()).getId(),
                                ((Item) dept.getSelectedItem()).getId(),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                    ZPlataOO oo = new ZPlataOO(buttonGroup.getSelection().getActionCommand(), tempOtchet, brigOtchet,
                            buttonGroup.getSelection().getActionCommand().equals("1")
                                    ? zpdb.getProductionOtchetNVBrak(
                                    ((Item) brig.getSelectedItem()).getId(),
                                    ((Item) dept.getSelectedItem()).getId(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())))
                                    : new Vector());

                    oo.createReport(UtilZPlata.OTCHET_NV);

                } else if (titleOtchet.equals(UtilZPlata.OTCHET_NV_EMPL)) {
                    try {
                        idEmployer = Integer.valueOf(searchNum.getText().trim());
                    } catch (NumberFormatException e) {
                        idEmployer = -1;
                    }

                    tempOtchet.add(((Item) dept.getSelectedItem()).getDescription());
                    tempOtchet.add(brig.getSelectedItem().toString());
                    tempOtchet.add(" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                            " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()));
                    tempOtchet.add(zpdb.getAllKolModel(
                            ((Item) brig.getSelectedItem()).getId(),
                            ((Item) dept.getSelectedItem()).getId(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))));

                    if (buttonGroup.getSelection().getActionCommand().equals("1")) {
                        brigOtchet = zpdb.getProductionOtchetNVEmplKr(
                                ((Item) brig.getSelectedItem()).getId(),
                                ((Item) dept.getSelectedItem()).getId(),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                                idEmployer);

                        ZPlataOO oo = new ZPlataOO("", tempOtchet, brigOtchet, new Vector());
                        oo.createReport(UtilZPlata.OTCHET_NV_EMPL_KR);
                    } else if (buttonGroup.getSelection().getActionCommand().equals("2")) {
                        brigOtchet = zpdb.getProductionOtchetNVEmplRaz(
                                ((Item) brig.getSelectedItem()).getId(),
                                ((Item) dept.getSelectedItem()).getId(),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                                idEmployer);

                        ZPlataOO oo = new ZPlataOO("", tempOtchet, brigOtchet, new Vector());
                        oo.createReport(UtilZPlata.OTCHET_NV_EMPL);
                    }

                } else if (titleOtchet.equals(UtilZPlata.OTCHET_NV_MODEL)) {
                    tempOtchet.add(((Item) dept.getSelectedItem()).getDescription());
                    tempOtchet.add(brig.getSelectedItem().toString());
                    tempOtchet.add(" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                            " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()));

                    tempOtchet.add("");

                    brigOtchet = zpdb.getProductionOtchetNVModel(
                            ((Item) brig.getSelectedItem()).getId(),
                            ((Item) dept.getSelectedItem()).getId(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                            Integer.valueOf(searchNum.getText().trim()));

                    kolOtchet = zpdb.getSumTotalByModel(
                            ((Item) brig.getSelectedItem()).getId(),
                            ((Item) dept.getSelectedItem()).getId(),
                            Integer.valueOf(searchNum.getText().trim()),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                    defectOtchet = zpdb.getSumDefectByModel(
                            ((Item) brig.getSelectedItem()).getId(),
                            ((Item) dept.getSelectedItem()).getId(),
                            Integer.valueOf(searchNum.getText().trim()),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())));

                    ZPlataOO oo = new ZPlataOO("", tempOtchet, brigOtchet, kolOtchet, defectOtchet);
                    oo.createReport(UtilZPlata.OTCHET_NV_MODEL);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            brig.removeAllItems();

            zpdb = new ZPlataPDB();
            Vector tmp = zpdb.getBrigList(((Item) dept.getSelectedItem()).getId());

            model = new Vector();

            model.add(new Item(-1, "Все...", ""));
            for (int j = 0; j < tmp.size(); j++)
                model.add(new Item(Integer.parseInt(((Vector) tmp.elementAt(j)).get(0).toString()), ((Vector) tmp.elementAt(j)).get(1).toString(), ""));

            for (Iterator it = model.iterator(); it.hasNext(); )
                brig.addItem(it.next());

        } catch (Exception e) {
            brig = new JComboBox();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }

        try {
            brig.setSelectedItem(UtilZPlata.getIndexModel(model, UtilZPlata.BRIG_SELECT_ITEM));

            if (brig.getSelectedItem() == null)
                brig.setSelectedIndex(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
