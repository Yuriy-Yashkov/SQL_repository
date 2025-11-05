package dept.sklad.ho;

import by.march8.ecs.framework.common.LogCrutch;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.Item;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class VedomostForm extends javax.swing.JDialog {
    //private static final Logger log = new Log().getLoger(VedomostForm.class);
    private static final LogCrutch log = new LogCrutch();
    private User user = User.getInstance();
    private JButton buttOtchet;
    private JButton buttClose;
    private JButton buttExecute;
    private JButton buttInventory;
    private JDateChooser sStDate;
    private JDateChooser sDate;
    private JDateChooser eDate;
    private JPanel osnova;
    private JPanel searchPanel;
    private JPanel buttPanel;
    private JLabel title;
    private JComboBox dept;
    private JComboBox empl;
    private JCheckBox daySat;

    private ButtonGroup buttonGroup;
    //  private ButtonGroup buttonGroup2;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;

    private String titleOtchet;

    private Vector model;

    private SkladHOPDB spdb;
    private String TYPE;
    private ProgressBar pb;
    private Vector data;

    public VedomostForm(SkladHOForm parent, boolean modal) {
        super(parent, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setTitle("Закрыть остатки?");
        setPreferredSize(new Dimension(430, 180));

        init();

        //jRadioButton2.setEnabled(false);

        //jRadioButton1.setText(" в кладовой;");
        //jRadioButton2.setText(" в др. подразделениях;");

        //searchPanel.add(new JLabel("Остаток:"), ParagraphLayout.NEW_PARAGRAPH);
        //searchPanel.add(jRadioButton1);
        //searchPanel.add(jRadioButton2);  

        searchPanel.add(new JLabel("Нач. периода:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(sStDate);

        buttPanel.add(buttExecute);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        try {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, -1);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));

            sDate.setDate(c.getTime());
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            eDate.setDate(c.getTime());

            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);

        } catch (Exception e) {
            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);
            sDate.setDate(d);
            eDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public VedomostForm(java.awt.Dialog parent, boolean modal, Date sDate, Date eDate) {
        super(parent, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setTitle("Общая карта раскроя");
        setPreferredSize(new Dimension(460, 200));

        init();

        TYPE = UtilSkladHO.REPORT_KRO;

        searchPanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(dept);
        searchPanel.add(new JLabel("Работник:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(empl);

        buttPanel.add(buttOtchet);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate)) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate))) {

                this.sDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(sDate)));
                this.eDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(eDate)));

            } else {
                this.sDate.setDate(d);
                this.eDate.setDate((Calendar.getInstance()).getTime());
            }
        } catch (Exception e) {
            this.sDate.setDate(d);
            this.eDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public VedomostForm(java.awt.Dialog parent, boolean modal, String typeReport, Date sStDate, Date eStDate) {
        super(parent, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        if (typeReport.equals(UtilSkladHO.REPORT_POLOTNO_F)) {
            setPreferredSize(new Dimension(430, 150));
            setTitle("Движение фабричного полотна");
        } else
            setPreferredSize(new Dimension(430, 180));

        init();

        TYPE = typeReport;

        if (typeReport.equals(UtilSkladHO.REPORT_POLOTNO_I_PO)) {
            setTitle("ПО по движению импортного полотна");

            jRadioButton1.setActionCommand("");
            jRadioButton2.setActionCommand("м");
            jRadioButton3.setActionCommand("кг");

            jRadioButton1.setText(" все;");
            jRadioButton2.setText(" м;");
            jRadioButton3.setText(" кг;");

            buttonGroup.add(jRadioButton1);
            buttonGroup.add(jRadioButton2);
            buttonGroup.add(jRadioButton3);

            searchPanel.add(new JLabel("Ед. изм.:"), ParagraphLayout.NEW_PARAGRAPH);
            searchPanel.add(jRadioButton1);
            searchPanel.add(jRadioButton2);
            searchPanel.add(jRadioButton3);

        } else if (typeReport.equals(UtilSkladHO.REPORT_POLOTNO_I)) {
            setTitle("Движение импорного полотна");

            jRadioButton1.setText(" по наименованию;");
            //jRadioButton2.setText(" по группам;");

            searchPanel.add(new JLabel("Движение:"), ParagraphLayout.NEW_PARAGRAPH);
            searchPanel.add(jRadioButton1);
            //searchPanel.add(jRadioButton2);

        } else if (typeReport.equals(UtilSkladHO.REPORT_VMATERIAL)) {
            setTitle("Движение всп. материалов");

            jRadioButton1.setText(" по наименованию;");
            //jRadioButton2.setText(" по группам;");

            searchPanel.add(new JLabel("Движение:"), ParagraphLayout.NEW_PARAGRAPH);
            searchPanel.add(jRadioButton1);
            //searchPanel.add(jRadioButton2);
        }

        buttPanel.add(buttOtchet);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate)) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eStDate))) {

                this.sDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(sStDate)));
                this.eDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(new SimpleDateFormat("dd.MM.yyyy").format(eStDate)));

            } else {
                this.sDate.setDate(d);
                this.eDate.setDate((Calendar.getInstance()).getTime());
            }
        } catch (Exception e) {
            this.sDate.setDate(d);
            this.eDate.setDate((Calendar.getInstance()).getTime());
        }

        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public VedomostForm(SkladHOForm parent, boolean modal, Vector data) {
        super(parent, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setTitle("Накладная на выдачу");
        setPreferredSize(new Dimension(460, 200));

        init();

        TYPE = UtilSkladHO.REPORT_NAK;

        this.data = data;

        searchPanel.removeAll();
        searchPanel.add(new JLabel("Дата:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(sDate);
        searchPanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(dept);
        searchPanel.add(new JLabel("Работник:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(empl);

        buttPanel.add(buttOtchet);
        buttPanel.add(buttClose);

        deptActionPerformed(null);

        try {
            sDate.setDate((Calendar.getInstance()).getTime());
            eDate.setDate((Calendar.getInstance()).getTime());
        } catch (Exception e) {
        }

        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public VedomostForm(java.awt.Dialog parent, boolean modal, String report, Date date) {
        super(parent, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setTitle("Инвентаризационная опись");
        setPreferredSize(new Dimension(430, 130));

        init();

        searchPanel.removeAll();
        searchPanel.add(new JLabel("Нач. периода:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(sStDate);

        buttPanel.add(buttInventory);
        buttPanel.add(buttClose);

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);

        try {
            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(date))) {
                this.sStDate.setDate(new SimpleDateFormat("dd.MM.yyyy")
                        .parse(new SimpleDateFormat("dd.MM.yyyy").format(date)));
            } else {
                this.sStDate.setDate(d);
            }
        } catch (Exception e) {
            this.sStDate.setDate(d);
        }

        deptActionPerformed(null);

        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        TYPE = "";

        osnova = new JPanel();
        searchPanel = new JPanel();
        buttPanel = new JPanel();
        dept = new JComboBox(UtilSkladHO.DEPT_MODEL);
        empl = new JComboBox();
        buttOtchet = new JButton("Сформировать отчёт");
        buttClose = new JButton("Закрыть");
        buttExecute = new JButton("Выполнить");
        buttInventory = new JButton("Сформировать");
        sStDate = new JDateChooser();
        sDate = new JDateChooser();
        eDate = new JDateChooser();
        buttonGroup = new ButtonGroup();
        //buttonGroup2 = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();

        osnova.setLayout(new BorderLayout(1, 1));
        searchPanel.setLayout(new ParagraphLayout());
        buttPanel.setLayout(new GridLayout(0, 2, 5, 5));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dept.setPreferredSize(new Dimension(300, 20));
        empl.setPreferredSize(new Dimension(300, 20));
        sStDate.setPreferredSize(new Dimension(200, 20));
        sDate.setPreferredSize(new Dimension(200, 20));
        eDate.setPreferredSize(new Dimension(200, 20));

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setActionCommand("0");
        jRadioButton2.setActionCommand("1");
        jRadioButton3.setActionCommand("2");
        jRadioButton4.setActionCommand("3");

        jRadioButton1.setSelected(true);

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, UtilSkladHO.DEPT_SELECT_ITEM);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

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

        buttExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttExecuteActionPerformed(evt);
            }
        });

        buttInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttInventoryActionPerformed(evt);
            }
        });

        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        /*
        buttonGroup2.add(jRadioButton3);
        buttonGroup2.add(jRadioButton4);
        */

        searchPanel.add(new JLabel("Остатки:"), ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel("    c   "), ParagraphLayout.NEW_LINE);
        searchPanel.add(sDate);
        searchPanel.add(new JLabel("    по "), ParagraphLayout.NEW_LINE);
        searchPanel.add(eDate);

        osnova.add(searchPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttExecuteActionPerformed(ActionEvent evt) {
        try {
            if (!new SimpleDateFormat("dd").format(sStDate.getDate()).equals("01"))
                JOptionPane.showMessageDialog(null,
                        "Нач. периода не 1-е число месяца!\n "
                                + "При выполнении исправится автоматически!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

            if (JOptionPane.showOptionDialog(null, "Закрыть период?", "Остатки", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, "Да") == JOptionPane.YES_OPTION) {

                try {
                    pb = new ProgressBar(this, false, "Закрытие периода...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                spdb = new SkladHOPDB();

                                java.util.Date sNDate = sDate.getDate();
                                sNDate.setDate(1);

                                java.util.Date d = sStDate.getDate();
                                d.setDate(1);

                                sStDate.setDate(d);

                                if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())) &&
                                        UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())) &&
                                        UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()))) {

                                    if (spdb.executeOstSkladHO(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sNDate)),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())),
                                            Integer.valueOf(user.getIdEmployee()))) {

                                        JOptionPane.showMessageDialog(null, "Период успешно закрыт! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                        dispose();
                                    } else
                                        JOptionPane.showMessageDialog(null, "Сбой! Период не закрыт! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);

                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            } finally {
                                spdb.disConn();
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
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    private void buttInventoryActionPerformed(ActionEvent evt) {
        try {
            boolean flagContinue = true;
            if (!new SimpleDateFormat("dd").format(sStDate.getDate()).equals("01")) {
                flagContinue = false;

                if (JOptionPane.showOptionDialog(null,
                        "Нач. периода не 1-е число месяца!\n "
                                + "При выполнении исправится автоматически!\n "
                                + "Продолжить?",
                        "Внимание",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new Object[]{"Да", "Отмена"},
                        "Отмена") == JOptionPane.YES_OPTION)
                    flagContinue = true;
            }

            if (flagContinue) {
                try {
                    pb = new ProgressBar(this, false, "Сбор данных...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                java.util.Date d = sStDate.getDate();
                                d.setDate(1);
                                sStDate.setDate(d);

                                spdb = new SkladHOPDB();

                                if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()))) {
                                    Vector items = spdb.getOst1DateStartPrice(UtilFunctions
                                            .convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())), false);

                                    SkladHOOO oo = new SkladHOOO("", "'" + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()), items);
                                    oo.createReport(UtilSkladHO.OTCHET_INVENTORY);

                                    dispose();
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null,
                                        "Ошибка! " + e.getMessage(),
                                        "Ошибка",
                                        javax.swing.JOptionPane.ERROR_MESSAGE);
                            } finally {
                                spdb.disConn();
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
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    private void buttOtchetActionPerformed(ActionEvent evt) {
        try {
            Vector tempOtchet = new Vector();

            if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())) &&
                    UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate()))) {

                tempOtchet.add(" период с " + new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate()) +
                        " по " + new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate()));

                if (TYPE.equals(UtilSkladHO.REPORT_NAK)) {
                    tempOtchet = new Vector();

                    tempOtchet.add(((Item) dept.getSelectedItem()).getDescription());
                    tempOtchet.add(((Item) empl.getSelectedItem()).getDescription());
                    tempOtchet.add(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate()));

                    SkladHOOO oo = new SkladHOOO("", tempOtchet, data);
                    oo.createReport(UtilSkladHO.OTCHET_NAK);

                } else if (TYPE.equals(UtilSkladHO.REPORT_KRO)) {
                    Vector tempFasOtchet = new Vector();
                    Vector tempTMCOtchet = new Vector();
                    Vector tempReturnOtchet = new Vector();
                    String str = "";

                    tempOtchet.add(((Item) dept.getSelectedItem()).getDescription());
                    tempOtchet.add(empl.getSelectedItem().toString());
                    tempOtchet.add("");

                    SkladHOOO oo = new SkladHOOO("",
                            tempOtchet,
                            tempFasOtchet,
                            tempTMCOtchet,
                            tempReturnOtchet,
                            0);
                    oo.createReport(UtilSkladHO.OTCHET_MAP);

                } else if (TYPE.equals(UtilSkladHO.REPORT_POLOTNO_F)) {
                    SkladHOOO oo = new SkladHOOO("", tempOtchet, getData());
                    oo.createReport(UtilSkladHO.OTCHET_MOVE_NP);

                } else if (TYPE.equals(UtilSkladHO.REPORT_POLOTNO_I)) {
                    SkladHOOO oo = new SkladHOOO("", tempOtchet, getData());
                    if (jRadioButton1.isSelected())
                        oo.createReport(UtilSkladHO.OTCHET_MOVE_IP_P);
                    else if (jRadioButton2.isSelected())
                        oo.createReport(UtilSkladHO.OTCHET_MOVE_IP_G);

                } else if (TYPE.equals(UtilSkladHO.REPORT_VMATERIAL)) {
                    SkladHOOO oo = new SkladHOOO("", tempOtchet, getData());
                    if (jRadioButton1.isSelected())
                        oo.createReport(UtilSkladHO.OTCHET_MOVE_VM_P);
                    else if (jRadioButton2.isSelected())
                        oo.createReport(UtilSkladHO.OTCHET_MOVE_VM_G);
                } else if (TYPE.equals(UtilSkladHO.REPORT_POLOTNO_I_PO)) {
                    SkladHOOO oo = new SkladHOOO("", tempOtchet, getData());
                    oo.createReport(UtilSkladHO.OTCHET_MOVE_PO_IP);
                }

                //dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            empl.removeAllItems();

            spdb = new SkladHOPDB();
            Vector tmp = spdb.getEmplList(((Item) dept.getSelectedItem()).getId());

            model = new Vector();

            model.add(new Item(-1, "Все...", ""));
            for (int j = 0; j < tmp.size(); j++)
                model.add(new Item(Integer.parseInt(((Vector) tmp.elementAt(j)).get(0).toString()), ((Vector) tmp.elementAt(j)).get(1).toString(), ""));

            for (Iterator it = model.iterator(); it.hasNext(); )
                empl.addItem(it.next());

        } catch (Exception e) {
            empl = new JComboBox();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }

        empl.setSelectedIndex(0);

    }

    private Vector getData() {
        data = new Vector();

        try {
            pb = new ProgressBar(this, false, "Сбор данных ...");
            SwingWorker sw = new SwingWorker() {
                private SkladHOPDB spdb;

                protected Object doInBackground() {
                    try {
                        spdb = new SkladHOPDB();

                        java.util.Date d = sDate.getDate();
                        d.setDate(1);

                        if (TYPE.equals(UtilSkladHO.REPORT_KRO)) {
                            data = spdb.getDataReportAllMap(
                                    ((Item) empl.getSelectedItem()).getId(),
                                    ((Item) dept.getSelectedItem()).getId(),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())));

                        } else if (TYPE.equals(UtilSkladHO.REPORT_POLOTNO_F)) {
                            data = spdb.getDataReportMoveNP(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())));

                        } else if (TYPE.equals(UtilSkladHO.REPORT_POLOTNO_I_PO)) {
                            data = spdb.getDataReportPOIP(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())),
                                    buttonGroup.getSelection().getActionCommand());

                        } else if (TYPE.equals(UtilSkladHO.REPORT_POLOTNO_I)) {
                            if (jRadioButton1.isSelected()) {
                                data = spdb.getDataReportMove(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())),
                                        1);
                            } /*else if (jRadioButton2.isSelected()){
                                data = spdb.getDataReportMoveIP(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())));
                            } */
                        } else if (TYPE.equals(UtilSkladHO.REPORT_VMATERIAL)) {
                            if (jRadioButton1.isSelected()) {
                                data = spdb.getDataReportMove(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())),
                                        2);
                            } /*else if (jRadioButton2.isSelected()){
                                data = spdb.getDataReportMoveVM(UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(d)),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sDate.getDate())),
                                    UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eDate.getDate())));
                            } */

                        }

                    } catch (Exception e) {
                        data = new Vector();
                        JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
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
            data = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return data;
    }
}
