package dept.sklad;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import common.DateUtils;
import common.MoneyToStr;
import common.PanelWihtFone;
import common.ProgressBar;
import common.Valuta;
import common.WeightToStr;
import dept.sklad.model.ClientCombo;
import dept.sklad.model.ComboContractor;
import dept.sklad.model.Dogovor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author vova
 */
@SuppressWarnings("all")
public class PrintTTN extends JDialog {

    final DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
    public SkladDB db = new SkladDB();
    public SkladDB sdb = new SkladDB();
    public SwingWorker sw;
    JPanel mainPanel, mainPan1, mainPan2, mainPan3, jpTablePolotno;
    JPanel jpFooter = new JPanel(new FlowLayout());
    JPanel jpHeader = new JPanel(new FlowLayout());
    JTabbedPane jtpMyPan;
    PrintTTN paren;
    String[] val = {"USD", "EUR", "UAN", "RUB", "BYR"};
    JLabel lNDoc;
    JLabel lDDoc;
    JLabel lnds;
    JLabel lTorgNad;
    JLabel lValuta;
    JLabel lblSaleRate;
    JLabel lcFactor;
    JLabel lcDiscount;
    JLabel lblFixedRate;
    JLabel lgPrinial;
    JLabel lClose;
    JLabel lCMR;
    JLabel lCMRDate;
    int qwert = 0;
    JButton bPrint;
    JButton bClose;
    JButton bWriteButton;
    JButton bCloseTTN;
    JButton butTemp;
    JTextField tfNDoc = new JTextField();
    MaskFormatter formatter;
    JFormattedTextField ftDate, tfgDoverDate;
    int x = 10;
    int y = 0;
    int izm = 0;
    int[] radioButt = new int[7];
    String date;
    String ttn;
    HashMap param = new HashMap();
    HashMap money = new HashMap();
    JTextField tfCMR = new JTextField();
    JFormattedTextField tfCMRDate = new JFormattedTextField();
    JTextField tfaAvto = new JTextField();
    JTextField tfaPrits = new JTextField();
    JTextField tfaVodatel = new JTextField();
    JTextField tfaVladelets = new JTextField();
    JTextField tfaZakazchik = new JTextField();
    JTextField tfaPlatelschik = new JTextField();
    JTextField tfTorgNad = new JTextField();
    JTextField tfgOtpravitel = new JTextField();
    JTextField tfgPoluchatel = new JTextField();
    // JTextField tfgOsnovanie = new JTextField();
    ClientCombo tfgOsnovanie;
    JTextField tfgPunktpogruz = new JTextField();
    JTextField tfgPunktotgruz = new JTextField();
    JTextField tfgPereadresovka = new JTextField();
    JTextField tfgOtpustil = new JTextField();
    JTextField tfgSdal = new JTextField();
    JTextField tfgPlomba = new JTextField();
    JTextField tfgPrinial = new JTextField();
    JTextField tfgIspolPogr = new JTextField();
    JTextField tfgSposobPogr = new JTextField();
    JTextField tfgPrimechanie = new JTextField();
    JTextField tfgDoc = new JTextField();
    JTextField tfnds = new JTextField();
    JTextField tfgDoverennost = new JTextField();
    JTextField tfgDoverVidannoi = new JTextField();
    JTextField tfgDoverPrinial = new JTextField();
    JTextField tfgDoverNomerPlombi = new JTextField();
    JComboBox tfgPunktotgruzCB;
    JComboBox tfgPoluchatelCB;
    JTextField tfcFactor = new JTextField();
    JTextField tfcDiscount = new JTextField();
    UCTextField tfSaleRate = new UCTextField();
    JTextField tfFixedRate = new JTextField();
    Color lightRed = new Color(255, 200, 200);
    Color lightGreen = new Color(200, 255, 200);
    Color col;
    int ButtonClick = 0;
    JTable jtTablePolotno = new JTable();
    DefaultTableModel dtmTablePolotno = new DefaultTableModel();
    Object[] vColumn = {"Код_Строки", "Шифр артикула", "Артикул", "Цвет", "Наименование", "Рулоны/Пачки", "Вес/Размер"};
    Object[] vColumn1 = {"Код_Строки", "Шифр артикула", "Артикул", "Цвет", "Наименование", "Рулоны/Пачки", "Вес (конд.)", "Вес (суровый)"};
    Object[][] vRows;
    JScrollPane jspTablePolotno = new JScrollPane();
    JButton jbSavePolotno = new JButton("Сохранить");
    JButton jbExitPolotno = new JButton("Закрыть/Отмена");
    JLabel lbNumberNakl;
    JCheckBox editCloseDateChek;
    JComboBox cbValuta;
    JRadioButton skidkaSum;
    JRadioButton skidkaCen;
    JRadioButton ttn1;
    JRadioButton ttn2;
    JRadioButton tn2;
    JRadioButton ndsY;
    JRadioButton ndsN;
    JRadioButton bel;
    JRadioButton eks;
    JRadioButton pricingY;
    JRadioButton pricingN;
    JRadioButton torgY;
    JRadioButton torgN;
    ProgressBar pb;
    JDialog thisForm;
    String[] adresRazgruz;
    String[] adresPoluchatel;
    String[] adrOtprav;
    String dfstr = "";
    float torgNadbavka = -1;
    boolean isRetund = false;
    private String keyDate = "01.07.2016";
    private ArrayList<Valuta> valuta = new ArrayList();
    private JCheckBox cbUnits;
    private JCheckBox cbPolotno;
    private JCheckBox cbSebestoim;
    private JComboBox<String> cbProtocol = new JComboBox<String>();
    private JCheckBox cbPrePayment = new JCheckBox("Предоплата");
    private JCheckBox cbNoSort = new JCheckBox("Несортные изделия для РФ");
    private ArrayList<Dogovor> dogovors = new ArrayList<Dogovor>();
    private JCheckBox cbSimpleAnnex = new JCheckBox("Упрощенное прил.");
    private JPanel pRound = new JPanel(null);
    private JLabel lblRound = new JLabel("Округление: ");
    private JComboBox<String> cbRound = new JComboBox<String>();

    public PrintTTN(JFrame parent, boolean f, String ttn) {
        super(parent, f);
        thisForm = this;
        this.ttn = ttn;
        pb = new ProgressBar(parent, false, "Получение деталей...");
        class SWorker extends SwingWorker<HashMap, Object> {

            String tn = new String();

            public SWorker(String tn) {
                this.tn = tn;
            }

            @Override
            protected HashMap doInBackground() throws Exception {
                SkladDB db = new SkladDB();
                HashMap hm = db.getPutListInfo(tn);
                dogovors = db.getClientDogovorList(tn);
                cbNoSort.setSelected(db.getNoSortFlag(tn));
                db.disConn();
                return hm;
            }

            @Override
            protected void done() {
                try {
                    param = get();
                    pb.dispose();
                } catch (Exception ex) {
                    System.err.println("Ошибка при получении результатов из фонового потока " + ex);
                }
            }
        }
        ;
        SWorker sw = new SWorker(this.ttn);
        sw.execute();
        pb.setVisible(true);
        SkladPDB pdb = new SkladPDB();
        valuta = pdb.getValutaList();
        pdb.disConn();
        // Адрес рузгрузки
        // Адрес получателя

        // Накладная определена как возвратная
        if (param.get("operac").toString().trim().equals("Возврат от покупателя")) {
            isRetund = true;
            cbPrePayment.setEnabled(false);
        } else {
            cbPrePayment.setEnabled(true);
        }

        if (param.get("razgruz").toString().trim().equals("")) {
            adresRazgruz = db.getAdresRazgruz(this.ttn);
        } else {
            adresRazgruz = new String[1];
            adresRazgruz[0] = param.get("razgruz").toString().trim();
        }

        if (param.get("gruzopoluchatel") == null) {
            adresPoluchatel = db.getAdresPoluchatel(this.ttn);
        } else {
            adresPoluchatel = new String[1];
            //gruzopoluchatel
            adresPoluchatel[0] = param.get("gruzopoluchatel").toString().trim();
        }
        adrOtprav = db.getAdresOtprav();

        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');


        init();
        add(jtpMyPan);
        setSize(965, 738);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Печать ТТН для накладной № " + ttn);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void init() {
        String buf;
        jtpMyPan = new JTabbedPane();
        jtpMyPan.setTabPlacement(JTabbedPane.TOP);
        jpTablePolotno = new JPanel(new BorderLayout());

        mainPanel = new PanelWihtFone();
        mainPan1 = new JPanel();
        mainPan1.setLayout(null);
        mainPan1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPan1.setBorder(BorderFactory.createTitledBorder("Груз"));
        mainPan1.setBounds(x, y + 30, 945, 350);
        mainPan2 = new JPanel();
        mainPan2.setLayout(null);
        mainPan2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPan2.setBorder(BorderFactory.createTitledBorder("Транспорт"));
        mainPan2.setBounds(x, y + 380, 945, 110);
        mainPan3 = new PanelWihtFone();
        mainPan3 = new JPanel();
        mainPan3.setLayout(null);
        mainPan3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPan3.setBorder(BorderFactory.createTitledBorder("Скидки"));
        mainPan3.setBounds(x, y + 490, 945, 110);
        if (param.get("date").toString().trim().equals("")) {
            Calendar cc = Calendar.getInstance();
            dfstr = "";
            int i = cc.get(Calendar.DAY_OF_MONTH);
            if (i < 10) {
                dfstr += "0" + Integer.toString(i) + ".";
            } else {
                dfstr += "" + Integer.toString(i) + ".";
            }
            i = cc.get(Calendar.MONTH) + 1;
            if (i < 10) {
                dfstr += "0" + Integer.toString(i) + ".";
            } else {
                dfstr += "" + Integer.toString(i) + ".";
            }
            dfstr += "" + cc.get(Calendar.YEAR);
        } else {
            dfstr = param.get("date").toString();
        }


        jbExitPolotno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        jbSavePolotno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object[][] valuesFromTab = null;
                valuesFromTab = getValuesFromTable();
                Object[] temp = valuesFromTab[0];
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i] != null) {

                    }
                }
                SkladDB sdb = new SkladDB();
                if (jtTablePolotno.getColumnCount() == 7) {
                    sdb.saveValuesInTabPolotno(valuesFromTab);
                } else {
                    sdb.saveValuesInTabPryazha(valuesFromTab);
                }
                sdb.disConn();
            }
        });

        lCMR = new JLabel("CMR №");
        lCMRDate = new JLabel("от");
        tfCMR = new JTextField();
        tfCMR.setText("б/н");
        tfCMRDate = new JFormattedTextField();

        tfCMR.addFocusListener(new FocusListener() {
            Color col = tfCMR.getBackground();

            public void focusGained(FocusEvent e) {
                if (tfCMR.isEditable()) {
                    tfCMR.setBackground(lightGreen);
                } else {
                    tfCMR.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfCMR.setBackground(col);
            }
        });
        Object strqw = null;
        try {
            strqw = formatter.stringToValue(dfstr);
        } catch (Exception e) {
        }
        tfCMRDate = new JFormattedTextField(formatter);
        tfCMRDate.setValue(strqw);
        tfCMRDate.addFocusListener(new FocusListener() {
            Color col = tfCMRDate.getBackground();

            public void focusGained(FocusEvent e) {
                if (tfCMRDate.isEditable()) {
                    tfCMRDate.setBackground(lightGreen);
                } else {
                    tfCMRDate.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfCMRDate.setBackground(col);
            }
        });
        lCMR.setBounds(x + 325, y + 10, 50, 20);
        mainPanel.add(lCMR);
        lCMRDate.setBounds(x + 425, y + 10, 30, 20);
        mainPanel.add(lCMRDate);
        tfCMR.setBounds(x + 375, y + 10, 50, 20);
        mainPanel.add(tfCMR);
        tfCMRDate.setBounds(x + 455, y + 10, 80, 20);
        mainPanel.add(tfCMRDate);
        lCMR.setVisible(false);
        lCMRDate.setVisible(false);
        tfCMR.setVisible(false);
        tfCMRDate.setVisible(false);


        lNDoc = new JLabel("Путевой лист № ");
        lDDoc = new JLabel(" от ");
        tfNDoc = new JTextField(param.get("nomer").toString());
        tfNDoc.addFocusListener(new FocusListener() {
            Color col = tfNDoc.getBackground();

            public void focusGained(FocusEvent e) {
                if (tfNDoc.isEditable()) {
                    tfNDoc.setBackground(lightGreen);
                } else {
                    tfNDoc.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfNDoc.setBackground(col);
            }
        });

        bPrint = new JButton("Сформировать");
        y = 0;
        x = 0;

        lNDoc.setBounds(x + 10, y + 10, 120, 20);
        mainPanel.add(lNDoc);
        tfNDoc.setBounds(x + 135, y + 10, 60, 20);
        mainPanel.add(tfNDoc);
        lDDoc.setBounds(x + 200, y + 10, 30, 20);
        mainPanel.add(lDDoc);
        Object ob = null;
        try {
            ob = formatter.stringToValue(dfstr);
        } catch (Exception e) {
        }
        //dfstr = convetDate(param.get("date").toString());
        ftDate = new JFormattedTextField(formatter);
        //ftDate.setText(ob.toString());
        ftDate.setValue(ob);
        //formatter.
        ftDate.addFocusListener(new FocusListener() {
            Color col = ftDate.getBackground();

            public void focusGained(FocusEvent e) {
                if (ftDate.isEditable()) {
                    ftDate.setBackground(lightGreen);
                } else {
                    ftDate.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                ftDate.setBackground(col);
                Valuta valuta = (Valuta) cbValuta.getSelectedItem();
                if (!valuta.getName().toLowerCase().equals("byr")) {
                    updateCurrencyRateValue(valuta.getName());
                }
            }
        });

        ftDate.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {

            }

            @Override
            public void keyPressed(final KeyEvent e) {
                Valuta valuta = (Valuta) cbValuta.getSelectedItem();
                if (!valuta.getName().toLowerCase().equals("byr")) {
                    updateCurrencyRateValue(valuta.getName());
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }
        });

        ftDate.setBounds(x + 230, y + 10, 100, 20);
        mainPanel.add(ftDate);

        cbSebestoim = new JCheckBox("Считать  по себестоимости");
        cbSebestoim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        cbSebestoim.setBounds(x + 550, y + 10, 225, 20);
        mainPanel.add(cbSebestoim);

        cbPolotno = new JCheckBox("Полотно/Пряжа");
        cbPolotno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cbPolotno.isSelected()) {
                    jtpMyPan.setEnabledAt(1, true);
                    pb = new ProgressBar(thisForm, false, "Получение спецификации полотна");
                    sw = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            lbNumberNakl = new JLabel("Номер накладной " + ttn);
                            vRows = sdb.getRowsFromPolotno(ttn);
                            if (vRows[0][1].toString().trim().startsWith("470")) {
                                dtmTablePolotno.setDataVector(vRows, vColumn1);
                            }
                            if (!vRows[0][1].toString().trim().startsWith("470")) {
                                dtmTablePolotno.setDataVector(vRows, vColumn);
                            }
                            jtTablePolotno.setModel(dtmTablePolotno);
                            jspTablePolotno.setViewportView(jtTablePolotno);
                            jpTablePolotno.add(jspTablePolotno, BorderLayout.CENTER);
                            jpFooter.add(jbSavePolotno);
                            jpFooter.add(jbExitPolotno);
                            jpHeader.add(lbNumberNakl);
                            /*cbUnits = new JCheckBox("Отметить если полотно в метрах");
                             jpHeader.add(cbUnits);*/
                            jpTablePolotno.add(jpFooter, BorderLayout.PAGE_END);
                            jpTablePolotno.add(jpHeader, BorderLayout.PAGE_START);
                            return 0;
                        }

                        @Override
                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setVisible(true);
                    thisForm.toFront();
                } else {
                    jtpMyPan.setEnabledAt(1, false);
                }
            }
        });

        cbPolotno.setBounds(x + 775, y + 10, 150, 20);
        mainPanel.add(cbPolotno);

        JLabel lgOtpravitel = new JLabel("Грузоотправитель");
        JLabel lgPoluchatel = new JLabel("Грузополучатель");
        JLabel lgOsnovanie = new JLabel("Основание отпуска");
        JLabel lgPunktpogruz = new JLabel("Пункт погрузки");
        JLabel lgPunktotgruz = new JLabel("Пункт разгрузки");
        JLabel lgPereadresovka = new JLabel("Переадресовка");
        JLabel lgOtpustil = new JLabel("Отпуск разрешил");
        JLabel lgSdal = new JLabel("Сдал грузоотправитель");
        JLabel lgPlomba = new JLabel("Номер пломбы");
        lgPrinial = new JLabel("Товар к перевозке принял");
        JLabel lgIspolPogr = new JLabel("Исполнитель погрузки");
        JLabel lgSposobPogr = new JLabel("Способ погрузки");
        JLabel lgPrimechanie = new JLabel("Примечание");
        JLabel lgDoc = new JLabel("С товаром переданы документы");
        JLabel lgDoverennost = new JLabel("По доверенности №");
        JLabel lgDoverDate = new JLabel("от");
        JLabel lgDoverVidannoi = new JLabel("Выданной:");
        JLabel lgDoverPrinial = new JLabel("Принял грузополучатель");
        JLabel lgDoverNomerPlombi = new JLabel("Номер пломбы");

        if (param.get("operac").toString().trim().startsWith("Возв")) {
            adrOtprav = new String[1];
            adrOtprav = db.getAdresPoluchatel(this.ttn);
        }
        tfgOtpravitel = new JTextField(adrOtprav[0]);
        tfgOtpravitel.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgOtpravitel.getBackground();
                if (tfgOtpravitel.isEditable()) {
                    tfgOtpravitel.setBackground(lightGreen);
                } else {
                    tfgOtpravitel.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgOtpravitel.setBackground(col);
            }
        });


        /*if(!param.get("adres_poluch").equals("")){
         adresPoluchatel = new String[1];
         adresPoluchatel[0] = param.get("adres_poluch").toString();
         }*/
        if (param.get("operac").toString().trim().startsWith("Возв")) {
            adresPoluchatel = new String[1];
            adresPoluchatel = db.getAdresOtprav();
        }
        tfgPoluchatelCB = new JComboBox(adresPoluchatel);

        /*if (!param.get("adres_poluch").toString().trim().equals("")&&!param.get("adres_poluch").toString().trim().equals(null))
         tfgPoluchatelCB.addItem(param.get("adres_poluch").toString());*/

        tfgPoluchatelCB.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgPoluchatel.getBackground();
                if (tfgPoluchatel.isEditable()) {
                    tfgPoluchatel.setBackground(lightGreen);
                } else {
                    tfgPoluchatel.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgPoluchatel.setBackground(col);
            }
        });

        tfgOsnovanie = new ClientCombo(dogovors);
        tfgOsnovanie.setDogovor(param.get("osnovanie").toString());

        tfgOsnovanie.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgOsnovanie.getBackground();
                if (tfgOsnovanie.isEditable()) {
                    tfgOsnovanie.setBackground(lightGreen);
                } else {
                    tfgOsnovanie.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgOsnovanie.setBackground(col);
            }
        });

        tfgOsnovanie.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                param.put("osnovanie", tfgOsnovanie.getItemAt(tfgOsnovanie.getSelectedIndex()));
            }
        });


        if (param.get("operac").toString().trim().startsWith("Возв")) {
            adrOtprav = new String[1];
            adrOtprav = db.getAdresRazgruz(this.ttn);
        }
        tfgPunktpogruz = new JTextField(adrOtprav[0]);
        tfgPunktpogruz.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgPunktpogruz.getBackground();
                if (tfgPunktpogruz.isEditable()) {
                    tfgPunktpogruz.setBackground(lightGreen);
                } else {
                    tfgPunktpogruz.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgPunktpogruz.setBackground(col);
            }
        });

        if (!param.get("razgruz").equals("")) {
            adresRazgruz = new String[1];
            adresRazgruz[0] = param.get("razgruz").toString();
        }
        if (param.get("operac").toString().trim().startsWith("Возв")) {
            adresRazgruz = new String[1];
            adresRazgruz[0] = "246708, г. Гомель, ул. Советская, д.41";
        }

        tfgPunktotgruzCB = new JComboBox(adresRazgruz);
        tfgPunktotgruz.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgPunktotgruz.getBackground();
                if (tfgPunktotgruz.isEditable()) {
                    tfgPunktotgruz.setBackground(lightGreen);
                } else {
                    tfgPunktotgruz.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgPunktotgruz.setBackground(col);
            }
        });

        tfgPereadresovka = new JTextField(param.get("pereadres").toString());
        tfgPereadresovka.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgPereadresovka.getBackground();
                if (tfgPereadresovka.isEditable()) {
                    tfgPereadresovka.setBackground(lightGreen);
                } else {
                    tfgPereadresovka.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgPereadresovka.setBackground(col);
            }
        });

        tfgDoverennost = new JTextField(param.get("dover").toString());
        tfgDoverennost.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgDoverennost.getBackground();
                if (tfgDoverennost.isEditable()) {
                    tfgDoverennost.setBackground(lightGreen);
                } else {
                    tfgDoverennost.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgDoverennost.setBackground(col);
            }
        });

        try {
            if (!param.get("dover").toString().trim().equals("")) {
                ob = formatter.stringToValue(param.get("doverDate").toString());
            } else {
                ob = formatter.stringToValue("00.00.0000");
            }
        } catch (Exception e) {
        }

        tfgDoverDate = new JFormattedTextField(formatter);
        tfgDoverDate.setValue(ob);
        tfgDoverDate.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgDoverDate.getBackground();
                if (tfgDoverDate.isEditable()) {
                    tfgDoverDate.setBackground(lightGreen);
                } else {
                    tfgDoverDate.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgDoverDate.setBackground(col);
            }
        });
        tfgDoverDate.setBounds(x + 835, y + 230, 100, 20);
        mainPan1.add(tfgDoverDate);


        tfgDoverVidannoi = new JTextField(param.get("doverVidan").toString());
        tfgDoverVidannoi.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgDoverVidannoi.getBackground();
                if (tfgDoverVidannoi.isEditable()) {
                    tfgDoverVidannoi.setBackground(lightGreen);
                } else {
                    tfgDoverVidannoi.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgDoverVidannoi.setBackground(col);
            }
        });

        tfgDoverPrinial = new JTextField(param.get("doverPrinial").toString());
        tfgDoverPrinial.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgDoverPrinial.getBackground();
                if (tfgDoverPrinial.isEditable()) {
                    tfgDoverPrinial.setBackground(lightGreen);
                } else {
                    tfgDoverPrinial.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgDoverPrinial.setBackground(col);
            }
        });

        tfgDoverNomerPlombi = new JTextField(param.get("doverPlomba").toString());
        tfgDoverNomerPlombi.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgDoverNomerPlombi.getBackground();
                if (tfgDoverNomerPlombi.isEditable()) {
                    tfgDoverNomerPlombi.setBackground(lightGreen);
                } else {
                    tfgDoverNomerPlombi.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgDoverNomerPlombi.setBackground(col);
            }
        });

        if (param.get("otpustil").toString().equals("")) {
            tfgOtpustil = new JTextField("спец. по продажам ОС ");
        } else {
            tfgOtpustil = new JTextField(param.get("otpustil").toString());
        }


        tfgOtpustil.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgOtpustil.getBackground();
                if (tfgOtpustil.isEditable()) {
                    tfgOtpustil.setBackground(lightGreen);
                } else {
                    tfgOtpustil.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgOtpustil.setBackground(col);
            }
        });

        if (param.get("sdal").toString().equals("")) {
            tfgSdal = new JTextField("кладовщик ");
        } else {
            tfgSdal = new JTextField(param.get("sdal").toString());
        }
        tfgSdal.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgSdal.getBackground();
                if (tfgSdal.isEditable()) {
                    tfgSdal.setBackground(lightGreen);
                } else {
                    tfgSdal.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgSdal.setBackground(col);
            }
        });

        if (param.get("nplombi").toString().equals("")) {
            tfgPlomba = new JTextField("\"8 Марта\" Склад № 6");
        } else {
            tfgPlomba = new JTextField(param.get("nplombi").toString());
        }
        tfgPlomba.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgPlomba.getBackground();
                if (tfgPlomba.isEditable()) {
                    tfgPlomba.setBackground(lightGreen);
                } else {
                    tfgPlomba.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgPlomba.setBackground(col);
            }
        });


        if (param.get("prinial").toString().equals("")) {
            tfgPrinial = new JTextField("водитель ");
        } else {
            tfgPrinial = new JTextField(param.get("prinial").toString());
        }
        tfgPrinial.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgPrinial.getBackground();
                if (tfgPrinial.isEditable()) {
                    tfgPrinial.setBackground(lightGreen);
                } else {
                    tfgPrinial.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgPrinial.setBackground(col);
            }
        });

        if (param.get("ispolnitel").toString().equals("")) {
            tfgIspolPogr = new JTextField("ОАО \"8 Марта\"");
        } else {
            tfgIspolPogr = new JTextField(param.get("ispolnitel").toString());
        }
        tfgIspolPogr.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgIspolPogr.getBackground();
                if (tfgIspolPogr.isEditable()) {
                    tfgIspolPogr.setBackground(lightGreen);
                } else {
                    tfgIspolPogr.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgIspolPogr.setBackground(col);
            }
        });

        tfgSposobPogr = new JTextField(param.get("sposob").toString());
        tfgSposobPogr.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgSposobPogr.getBackground();
                if (tfgSposobPogr.isEditable()) {
                    tfgSposobPogr.setBackground(lightGreen);
                } else {
                    tfgSposobPogr.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgSposobPogr.setBackground(col);
            }
        });


        if (param.get("primechanie").toString().equals("")) {
            tfgPrimechanie = new JTextField("Франко-станция назначения");
        } else {
            tfgPrimechanie = new JTextField(param.get("primechanie").toString());
        }
        tfgPrimechanie.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgPrimechanie.getBackground();
                if (tfgPrimechanie.isEditable()) {
                    tfgPrimechanie.setBackground(lightGreen);
                } else {
                    tfgPrimechanie.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgPrimechanie.setBackground(col);
            }
        });

        if (param.get("doc").toString().equals("")) {
            tfgDoc = new JTextField("ТТН № " + ttn);
        } else {
            tfgDoc = new JTextField(param.get("doc").toString());
        }

        tfgDoc.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfgDoc.getBackground();
                if (tfgDoc.isEditable()) {
                    tfgDoc.setBackground(lightGreen);
                } else {
                    tfgDoc.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfgDoc.setBackground(col);
            }
        });

        lgOtpravitel.setBounds(x + 10, y + 20, 140, 20);
        mainPan1.add(lgOtpravitel);
        tfgOtpravitel.setBounds(x + 155, y + 20, 780, 20);
        tfgOtpravitel.setEnabled(false);
        mainPan1.add(tfgOtpravitel);

        lgPoluchatel.setBounds(x + 10, y + 50, 140, 20);
        mainPan1.add(lgPoluchatel);

        tfgPoluchatelCB.setBounds(x + 155, y + 50, 780, 20);
        tfgPoluchatelCB.setEnabled(true);
        tfgPoluchatelCB.setEditable(true);
        mainPan1.add(tfgPoluchatelCB);

        lgOsnovanie.setBounds(x + 10, y + 80, 150, 20);
        mainPan1.add(lgOsnovanie);
        tfgOsnovanie.setBounds(x + 155, y + 80, 780, 20);
        tfgOsnovanie.setEnabled(false);
        tfgOsnovanie.setEditable(false);
        mainPan1.add(tfgOsnovanie);

        lgPunktpogruz.setBounds(x + 10, y + 110, 150, 20);
        mainPan1.add(lgPunktpogruz);
        tfgPunktpogruz.setBounds(x + 155, y + 110, 780, 20);
        tfgPunktpogruz.setEnabled(false);
        mainPan1.add(tfgPunktpogruz);

        lgPunktotgruz.setBounds(x + 10, y + 140, 150, 20);
        mainPan1.add(lgPunktotgruz);

        tfgPunktotgruzCB.setBounds(x + 155, y + 140, 780, 20);
        tfgPunktotgruzCB.setEnabled(true);
        tfgPunktotgruzCB.setEditable(true);
        mainPan1.add(tfgPunktotgruzCB);

        lgPereadresovka.setBounds(x + 10, y + 170, 150, 20);
        mainPan1.add(lgPereadresovka);
        tfgPereadresovka.setBounds(x + 155, y + 170, 780, 20);
        mainPan1.add(tfgPereadresovka);

        lgOtpustil.setBounds(x + 10, y + 200, 150, 20);
        mainPan1.add(lgOtpustil);
        tfgOtpustil.setBounds(x + 225, y + 200, 260, 20);
        mainPan1.add(tfgOtpustil);
        lgSdal.setBounds(x + 500, y + 200, 180, 20);
        mainPan1.add(lgSdal);
        tfgSdal.setBounds(x + 695, y + 200, 240, 20);
        mainPan1.add(tfgSdal);

        lgPrinial.setBounds(x + 10, y + 230, 210, 20);
        mainPan1.add(lgPrinial);
        tfgPrinial.setBounds(x + 225, y + 230, 260, 20);
        mainPan1.add(tfgPrinial);

        lgDoverennost.setBounds(x + 500, y + 230, 150, 20);
        mainPan1.add(lgDoverennost);
        tfgDoverennost.setBounds(x + 650, y + 230, 150, 20);
        mainPan1.add(tfgDoverennost);

        lgDoverDate.setBounds(x + 805, y + 230, 30, 20);
        mainPan1.add(lgDoverDate);

        lgDoverVidannoi.setBounds(x + 10, y + 260, 210, 20);
        mainPan1.add(lgDoverVidannoi);
        tfgDoverVidannoi.setBounds(x + 155, y + 260, 150, 20);
        mainPan1.add(tfgDoverVidannoi);

        lgDoverPrinial.setBounds(x + 310, y + 260, 180, 20);
        mainPan1.add(lgDoverPrinial);
        tfgDoverPrinial.setBounds(x + 490, y + 260, 150, 20);
        mainPan1.add(tfgDoverPrinial);

        lgDoverNomerPlombi.setBounds(x + 650, y + 260, 110, 20);
        mainPan1.add(lgDoverNomerPlombi);
        tfgDoverNomerPlombi.setBounds(x + 785, y + 260, 150, 20);
        mainPan1.add(tfgDoverNomerPlombi);

        lgPlomba.setBounds(x + 10, y + 290, 120, 20);
        mainPan1.add(lgPlomba);
        tfgPlomba.setBounds(x + 155, y + 290, 150, 20);
        mainPan1.add(tfgPlomba);

        lgIspolPogr.setBounds(x + 310, y + 290, 170, 20);
        mainPan1.add(lgIspolPogr);
        tfgIspolPogr.setBounds(x + 490, y + 290, 150, 20);
        mainPan1.add(tfgIspolPogr);
        lgSposobPogr.setBounds(x + 650, y + 290, 120, 20);
        mainPan1.add(lgSposobPogr);
        tfgSposobPogr.setBounds(x + 785, y + 290, 50, 20);
        tfgSposobPogr.setEnabled(false);
        mainPan1.add(tfgSposobPogr);

        lgPrimechanie.setBounds(x + 10, y + 320, 100, 20);
        mainPan1.add(lgPrimechanie);
        tfgPrimechanie.setBounds(x + 155, y + 320, 335, 20);
        mainPan1.add(tfgPrimechanie);

        lgDoc.setBounds(x + 530, y + 320, 250, 20);
        mainPan1.add(lgDoc);
        tfgDoc.setBounds(x + 785, y + 320, 150, 20);
        mainPan1.add(tfgDoc);

        ButtonGroup bg = new ButtonGroup();
        ttn1 = new JRadioButton("ТТН-1");
        ttn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (ttn1.isSelected()) {
                        setPropElements();
                    }
                    radioButt[0] = 0;
                } catch (Exception ex) {
                }
            }
        });
        ttn2 = new JRadioButton("ТТН-1(Для фирменных)");
        ttn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (ttn2.isSelected()) {
                        setPropElements();
                        pRound.setVisible(true);
                    }
                    radioButt[0] = 1;
                } catch (Exception ex) {
                }
            }
        });
        tn2 = new JRadioButton("ТН-2");
        tn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (tn2.isSelected()) {

                        qwert = cbValuta.getSelectedIndex();
                        setPropElements();
                    }
                    radioButt[0] = 2;
                } catch (Exception ex) {
                }
            }
        });

        ttn1.setSelected(true);
        bg.add(ttn1);
        bg.add(ttn2);
        bg.add(tn2);
        x = x + 20;
        y = y + 100;
        ttn1.setBounds(x, y + 500, 100, 20);
        mainPanel.add(ttn1);
        ttn2.setBounds(x, y + 525, 200, 20);
        mainPanel.add(ttn2);
        tn2.setBounds(x + 100, y + 500, 100, 20);
        mainPanel.add(tn2);

        JLabel lblProtocol = new JLabel("Протокол вида:");
        ComboContractor.filling(cbProtocol);

        lblProtocol.setBounds(x + 330, y + 500, 150, 24);
        cbProtocol.setBounds(x + 450, y + 505, 250, 20);
        cbPrePayment.setBounds(x + 682, y + 526, 120, 24);
        cbSimpleAnnex.setBounds(x + 530, y + 526, 145, 24);


        lblRound.setBounds(0, 0, 100, 20);
        cbRound.setBounds(120, 0, 50, 20);
        cbRound.addItem("50");
        cbRound.addItem("100");
        cbRound.setSelectedIndex(0);
        pRound.add(lblRound);
        pRound.add(cbRound);
        pRound.setBounds(x + 330, y + 526, 170, 20);
        cbRound.setSelectedIndex(1);
        mainPanel.add(pRound);


        mainPanel.add(lblProtocol);
        mainPanel.add(cbProtocol);
        mainPanel.add(cbPrePayment);
        mainPanel.add(cbSimpleAnnex);

        cbPrePayment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Valuta valuta = (Valuta) cbValuta.getSelectedItem();
                if (!valuta.getName().toLowerCase().equals("byr")) {
                    updateCurrencyRateValue(valuta.getName());
                }
            }
        });

        ButtonGroup bg2 = new ButtonGroup();
        bel = new JRadioButton("Беларусь");
        bel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (bel.isSelected()) {
                        setPropElements();
                    }
                    radioButt[1] = 0;
                } catch (Exception ex) {
                }
            }
        });
        eks = new JRadioButton("Экспорт");
        eks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (eks.isSelected()) {
                        if (param.get("primechanie").toString().equals("")) {
                            tfgPrimechanie = new JTextField("Условия поставки ФСА-Гомель");
                        } else {
                            tfgPrimechanie = new JTextField(param.get("primechanie").toString());
                        }

                        if (param.get("otpustil").toString().equals("")) {
                            tfgOtpustil = new JTextField("Cпец. ОВЭС ");
                        } else {
                            tfgOtpustil = new JTextField(param.get("otpustil").toString());
                        }
                        if (param.get("sdal").toString().equals("")) {
                            tfgSdal = new JTextField("Зав. скл. гот. прод. ");
                        } else {
                            tfgSdal = new JTextField(param.get("sdal").toString());
                        }
                        lCMR.setVisible(true);
                        lCMRDate.setVisible(true);
                        tfCMR.setVisible(true);
                        tfCMRDate.setVisible(true);

                        ttn2.setEnabled(false);
                        tn2.setEnabled(true);
                        ttn1.setSelected(true);
                        setPropElements();
                    }
                    radioButt[1] = 1;
                } catch (Exception ex) {
                }
            }
        });
        bel.setSelected(true);
        bg2.add(bel);
        bg2.add(eks);
        bel.setBounds(x + 200, y + 500, 100, 20);
        mainPanel.add(bel);
        eks.setBounds(x + 200, y + 525, 100, 20);
        mainPanel.add(eks);
        JLabel lPricing = new JLabel("Расчёт цены");
        lPricing.setBounds(x + 730, y + 500, 100, 20);
        mainPanel.add(lPricing);
        ButtonGroup bg4 = new ButtonGroup();
        pricingN = new JRadioButton("по коэф");
        pricingY = new JRadioButton("пересчёт");
        pricingY.setEnabled(true);
        pricingN.setSelected(true);
        pricingN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (pricingN.isSelected()) {
                        radioButt[2] = 0;
                        setPropElements();
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        pricingY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (pricingY.isSelected()) {
                        setPropElements();
                        radioButt[2] = 1;
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        bg4.add(pricingY);
        bg4.add(pricingN);
        pricingY.setBounds(x + 830, y + 500, 100, 20);
        mainPanel.add(pricingY);
        pricingN.setBounds(x + 830, y + 525, 100, 20);
        mainPanel.add(pricingN);
        x = x - 20;
        y = y - 100;
//форимрование закладки "Транспорт"
        JLabel laAvto = new JLabel("Автомобиль");
        JLabel laPrits = new JLabel("Прицеп");
        JLabel laVoditel = new JLabel("Водитель");
        JLabel laVladelets = new JLabel("Владелец автомобиля");
        JLabel laZakazchik = new JLabel("Заказчик автомобиля");
        JLabel laPlatelschik = new JLabel("Плательщик");

        tfaAvto = new JTextField(param.get("avto").toString());
        tfaAvto.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfaAvto.getBackground();
                if (tfaAvto.isEditable()) {
                    tfaAvto.setBackground(lightGreen);
                } else {
                    tfaAvto.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfaAvto.setBackground(col);
            }
        });

        tfaPrits = new JTextField(param.get("trailer").toString());
        tfaPrits.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfaPrits.getBackground();
                if (tfaPrits.isEditable()) {
                    tfaPrits.setBackground(lightGreen);
                } else {
                    tfaPrits.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfaPrits.setBackground(col);
            }
        });

        tfaVodatel = new JTextField(param.get("voditel").toString());
        tfaVodatel.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfaVodatel.getBackground();
                if (tfaVodatel.isEditable()) {
                    tfaVodatel.setBackground(lightGreen);
                } else {
                    tfaVodatel.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfaVodatel.setBackground(col);
            }
        });

        if (param.get("vladelets_avto").toString().equals("")) {
            tfaVladelets = new JTextField("грузоотправитель ");
        } else {
            tfaVladelets = new JTextField(param.get("vladelets_avto").toString());
        }
        tfaVladelets.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfaVladelets.getBackground();
                if (tfaVladelets.isEditable()) {
                    tfaVladelets.setBackground(lightGreen);
                } else {
                    tfaVladelets.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfaVladelets.setBackground(col);
            }
        });

        tfaZakazchik = new JTextField(param.get("zakazchik_avto").toString());
        tfaZakazchik.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfaZakazchik.getBackground();
                if (tfaZakazchik.isEditable()) {
                    tfaZakazchik.setBackground(lightGreen);
                } else {
                    tfaZakazchik.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfaZakazchik.setBackground(col);
            }
        });

        tfaPlatelschik = new JTextField(param.get("schot_platelschik").toString());
        tfaPlatelschik.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfaPlatelschik.getBackground();
                if (tfaPlatelschik.isEditable()) {
                    tfaPlatelschik.setBackground(lightGreen);
                } else {
                    tfaPlatelschik.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfaPlatelschik.setBackground(col);
            }
        });

        x = 0;
        y = 20;

        laAvto.setBounds(x + 10, y, 100, 20);
        mainPan2.add(laAvto);
        tfaAvto.setBounds(x + 150, y, 150, 20);
        mainPan2.add(tfaAvto);

        laPrits.setBounds(x + 305, y, 60, 20);
        mainPan2.add(laPrits);
        tfaPrits.setBounds(x + 485, y, 150, 20);
        mainPan2.add(tfaPrits);

        laVoditel.setBounds(x + 10, y + 30, 100, 20);
        mainPan2.add(laVoditel);
        tfaVodatel.setBounds(x + 150, y + 30, 150, 20);
        mainPan2.add(tfaVodatel);

        laVladelets.setBounds(x + 305, y + 30, 180, 20);
        mainPan2.add(laVladelets);
        tfaVladelets.setBounds(x + 485, y + 30, 150, 20);
        mainPan2.add(tfaVladelets);

        laZakazchik.setBounds(x + 305, y + 60, 180, 20);
        mainPan2.add(laZakazchik);
        tfaZakazchik.setBounds(x + 485, y + 60, 150, 20);
        mainPan2.add(tfaZakazchik);

        laPlatelschik.setBounds(x + 10, y + 60, 180, 20);
        mainPan2.add(laPlatelschik);
        tfaPlatelschik.setBounds(x + 150, y + 60, 150, 20);
        mainPan2.add(tfaPlatelschik);

//форимрование закладки "Расчёт цен"

        lcFactor = new JLabel("Коэффициент");
        lcDiscount = new JLabel("Скидка клиенту");
        lblFixedRate = new JLabel("Курс НБРБ ");

        tfcFactor = new JTextField("1");
        tfcDiscount = new JTextField("0");
        tfSaleRate = new UCTextField("1");
        //tfSaleRate.setComponentParams();


        tfcFactor = new JTextField(param.get("skidka_client").toString());
        tfcFactor.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfcFactor.getBackground();
                if (tfcFactor.isEditable()) {
                    tfcFactor.setBackground(lightGreen);
                } else {
                    tfcFactor.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfcFactor.setBackground(col);
            }
        });

        tfcDiscount = new JTextField(param.get("skidka_opt").toString());
        tfcDiscount.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfcDiscount.getBackground();
                if (tfcDiscount.isEditable()) {
                    tfcDiscount.setBackground(lightGreen);
                } else {
                    tfcDiscount.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfcDiscount.setBackground(col);
            }
        });

        tfSaleRate = new UCTextField(param.get("kurs_nbrb").toString());
        tfSaleRate.setComponentParams(lblSaleRate, Float.class, 8);

        tfSaleRate.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfSaleRate.getBackground();
                if (tfSaleRate.isEditable()) {
                    tfSaleRate.setBackground(lightGreen);
                } else {
                    tfSaleRate.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfSaleRate.setBackground(col);
            }
        });

        tfFixedRate = new JTextField(param.get("kkrr").toString());
        tfFixedRate.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfFixedRate.getBackground();
                if (tfFixedRate.isEditable()) {
                    tfFixedRate.setBackground(lightGreen);
                } else {
                    tfFixedRate.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfFixedRate.setBackground(col);
            }
        });

        cbValuta = new JComboBox(valuta.toArray());
        setSelectedCB(param.get("valuta").toString());

//                Valuta valuta = (Valuta)cbValuta.getSelectedItem();
//                SkladPDB pdb = new SkladPDB();
//                float kurs = pdb.getValutaKurs(valuta.getId());
//                pdb.disConn();
//                tfFixedRate.setText(String.valueOf(kurs));

        lcFactor.setBounds(x + 10, y + 60, 160, 20);
        mainPan3.add(lcFactor);
        tfcFactor.setBounds(x + 150, y + 60, 65, 20);
        mainPan3.add(tfcFactor);

        lcDiscount.setBounds(x + 10, y, 120, 20);
        mainPan3.add(lcDiscount);
        tfcDiscount.setBounds(x + 150, y, 65, 20);
        mainPan3.add(tfcDiscount);

        lblFixedRate.setBounds(x + 220, y, 140, 20);
        mainPan3.add(lblFixedRate);
        tfSaleRate.setBounds(x + 350, y + 30, 60, 20);
        tfFixedRate.setBounds(x + 350, y, 60, 20);
        mainPan3.add(tfSaleRate);

        lValuta = new JLabel("Валюта отгрузки");
        lValuta.setBounds(x + 10, y + 30, 140, 20);
        mainPan3.add(lValuta);

        cbValuta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cbValuta = (JComboBox) e.getSource();
                Valuta valuta = (Valuta) cbValuta.getSelectedItem();
                SkladPDB pdb = new SkladPDB();
                float kurs = 0;
                if (valuta.getName().toLowerCase().equals("byr")) {
                    kurs = pdb.getValutaKurs(valuta.getId());
                    //System.out.println("бел руб");
                } else {
                    //System.out.println(valuta.getName());
                    kurs = updateCurrencyRateValue(valuta.getName());
                }
                // System.out.println(DateUtils.getDateByStringValue(ftDate.getValue()));
                pdb.disConn();
                tfFixedRate.setText(String.valueOf(kurs));
            }
        });

        cbValuta.setBounds(x + 150, y + 30, 65, 20);
        mainPan3.add(cbValuta);

        lblSaleRate = new JLabel("Текущий курс ");
        lblSaleRate.setBounds(x + 220, y + 30, 130, 20);
        mainPan3.add(lblSaleRate);

        //tfFixedRate.setBounds(x + 335, y + 30, 65, 20);

        mainPan3.add(tfFixedRate);

        tfnds = new JTextField(param.get("nds").toString());
        tfnds.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfnds.getBackground();
                if (tfnds.isEditable()) {
                    tfnds.setBackground(lightGreen);
                } else {
                    tfnds.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfnds.setBackground(col);
            }
        });

        tfnds.setEnabled(false);
        tfnds.setBounds(x + 435, y + 30, 50, 20);
        mainPan3.add(tfnds);
        lnds = new JLabel("НДС");
        lnds.setBounds(x + 445, y, 65, 20);
        mainPan3.add(lnds);

        ButtonGroup bgr = new ButtonGroup();
        skidkaSum = new JRadioButton("Скидка на сумму документа");
        skidkaCen = new JRadioButton("Скидка на цену изделия");
        skidkaCen.setBounds(x + 675, y, 250, 20);
        skidkaSum.setBounds(x + 675, y + 30, 250, 20);

        cbNoSort.setBounds(x + 675, y + 60, 250, 20);
        cbNoSort.setForeground(Color.BLUE.darker());

        mainPan3.add(cbNoSort);


        mainPan3.add(skidkaCen);
        mainPan3.add(skidkaSum);
        skidkaCen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    radioButt[3] = 1;
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        skidkaSum.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    radioButt[3] = 2;
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        bgr.add(skidkaCen);
        bgr.add(skidkaSum);


        ButtonGroup bg3 = new ButtonGroup();
        ndsY = new JRadioButton("авто");
        ndsN = new JRadioButton("руч");
        ndsN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (ndsN.isSelected()) {
                        setPropElements();
                        tfnds.setEditable(true);
                        tfnds.setEnabled(true);
                        //radioButt[4]=1;
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        ndsY.setSelected(true);
        ndsY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (ndsY.isSelected()) {
                        setPropElements();
                        tfnds.setEditable(false);
                        tfnds.setEnabled(false);
                        //radioButt[4]=0;
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        bg3.add(ndsY);
        bg3.add(ndsN);
        ndsY.setBounds(x + 405, y + 60, 60, 20);
        mainPan3.add(ndsY);
        ndsN.setBounds(x + 465, y + 60, 65, 20);
        mainPan3.add(ndsN);


        ButtonGroup bg5 = new ButtonGroup();
        torgN = new JRadioButton("авто");
        torgY = new JRadioButton("изм");
        torgN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (torgN.isSelected()) {
                        tfTorgNad.setEditable(false);
                        tfTorgNad.setEnabled(true);
                        radioButt[5] = 0;
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        torgY.setSelected(true);
        torgY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (torgY.isSelected()) {
                        tfTorgNad.setEditable(true);
                        tfTorgNad.setEnabled(true);
                        radioButt[5] = 1;
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        bg5.add(torgY);
        bg5.add(torgN);
        torgN.setEnabled(false);
        torgY.setEnabled(false);
        tfTorgNad.setEditable(false);
        torgY.setBounds(x + 600, y + 60, 60, 20);
        mainPan3.add(torgY);
        torgN.setBounds(x + 535, y + 60, 65, 20);
        mainPan3.add(torgN);
        if (!param.get("torg_nadb").toString().trim().equals("")) {
            tfTorgNad = new JTextField("");
            tfTorgNad.setText(param.get("torg_nadb").toString().trim());
        } else {
            tfTorgNad = new JTextField("0");
        }
        tfTorgNad.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                col = tfTorgNad.getBackground();
                if (tfTorgNad.isEditable()) {
                    tfTorgNad.setBackground(lightGreen);
                } else {
                    tfTorgNad.setBackground(lightRed);
                }
            }

            public void focusLost(FocusEvent e) {
                tfTorgNad.setBackground(col);
            }
        });
        tfTorgNad.setBounds(x + 560, y + 30, 50, 20);
        mainPan3.add(tfTorgNad);
        lTorgNad = new JLabel("Торговая надбавка");
        lTorgNad.setBounds(x + 525, y, 150, 20);
        mainPan3.add(lTorgNad);
        y = y + 75;
        bPrint.setBounds(x + 255, y + 560, 200, 20);
        bPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    /*if (ErrorInput()){
                     SkladDB sdb = new SkladDB();
                     //checkDate();
                     setParamOfForm();
                     if(sdb.getStatusTTN(ttn)!=0){
                     if(!sdb.thereIsARecordInPutList(ttn)){
                     setPutList();
                     /*if(pricingY.isSelected()){
                     //conversion();
                     Thread.sleep(1000);
                     }*/
                    /*      }
                     }*/
                    if (ErrorInput()) {
                        setParamOfForm();
                        createPrilog();
                        //Thread.sleep(1000);
                        createPutevoi();
                        //Thread.sleep(1000);
                    }
                } catch (Exception ex) {
                    System.err.println("Ошибка в SkladDB формирование накладной." + ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(bPrint);

        bClose = new JButton("Отмена");
        bClose.setBounds(x + 810, y + 560, 120, 20);
        bClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mainPanel.add(bClose);
        bWriteButton = new JButton("Записать");
        bWriteButton.setBounds(x + 50, y + 560, 200, 20);
        mainPanel.add(bWriteButton);
        bWriteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SkladDB sdb = new SkladDB();
                    radioButt[6] = 4;
                    /* if(sdb.presenceSpecialPrice(ttn)){
                     SpecCena sc = new SpecCena(thisForm,ttn);
                     }*/

                    if (sdb.getStatusTTN(ttn) != 0) {
                        if (ErrorInput()) {
                            if (!sdb.thereIsARecordInPutList(ttn)) {
                                //нет запись
                                setParamOfForm();
                                setPutList();
                                if (pricingY.isSelected()) {
                                    conversion();
                                    Thread.sleep(1000);
                                    //JOptionPane.showMessageDialog(null, "Данные сохранены.", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                                }
                            } else {
                                setParamOfForm();
                                setPutList();
                                //int answer = JOptionPane.showConfirmDialog(null, "Внимание! Могут быть изменены сохраненные данные! Произвести запись?", "Внимание", javax.swing.JOptionPane.YES_NO_OPTION);
                                //if(answer==0){
                                conversion();
                                Thread.sleep(1000);
                                //JOptionPane.showMessageDialog(null, "Данные сохранены!");
                                //есть запись
                                //}
                            }
//                                if (cbPolotno.isSelected()){
//                                    setParamOfForm();
//                                    setPutList();
//                                    conversion();
//                                }
                            setRadioButton(radioButt);
                            sdb.setDogovorIDTTN(ttn, tfgOsnovanie.getId());
                            sdb.setPrePaymentTTN(ttn, cbPrePayment.isSelected());
                            sdb.setNoSortFlag(ttn, cbNoSort.isSelected());
                            sdb.disConn();
                            JOptionPane.showMessageDialog(null, "Данные сохранены.", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                        }

                    }
                } catch (Exception ex) {
                    System.err.println("Ошибка в SkladDB button записать " + ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        bCloseTTN = new JButton("Закрыть накладную");
        bCloseTTN.setBounds(x + 460, y + 560, 200, 20);
        bCloseTTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(null, "Вы уверены что хотите закрыть документ № " + ttn + "?", "Внимание", javax.swing.JOptionPane.YES_NO_OPTION);
                if (answer == 0) {
                    radioButt[6] = 9;
                    SkladDB sdb = new SkladDB();
                    setParamOfForm();
                    setPutList();
                    //if(editCloseDateChek.isSelected())
                    //                       sdb.setDateClosing(ttn, ftClose.getText());
                    //else
                    sdb.setDateClosing(ttn, ftDate.getText());
                    sdb.setStatusCloseTTN(ttn);
                    setRadioButton(radioButt);
                    closeTTN();
                }
                //setPutList();
            }
        });

        mainPanel.add(bCloseTTN);
        radioButt = sdb.getRadioButtonPosition(ttn);
        setRadioButton(radioButt);

        y = y - 75;
        mainPanel.add(mainPan3);
        mainPanel.add(mainPan2);
        mainPanel.add(mainPan1);
        if (tfgOtpravitel.getText().trim().equals("")) {
            tfgOtpravitel.setEditable(true);
        } else {
            tfgOtpravitel.setEditable(false);
        }
        tfgOsnovanie.setEditable(false);
        tfgOtpravitel.setEnabled(true);

        tfgOsnovanie.setEnabled(true);
        if (tfgPunktotgruz.getText().trim().equals("")) {
            tfgPunktotgruz.setEditable(true);
        } else {
            tfgPunktotgruz.setEditable(false);
        }
        if (tfgPunktpogruz.getText().trim().equals("")) {
            tfgPunktpogruz.setEditable(true);
        } else {
            tfgPunktpogruz.setEditable(false);
        }
        tfgPunktpogruz.setEnabled(true);
        tfgSposobPogr.setEditable(false);
        tfgSposobPogr.setEnabled(true);

        /*bWriteButton.setEnabled(false);
         bPrint.setEnabled(false);
         bCloseTTN.setEnabled(false);*/

        jtpMyPan.addTab("Шапка", mainPanel);
        jtpMyPan.addTab("Спецификация полотна", jpTablePolotno);
        jtpMyPan.setEnabledAt(1, false);

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<AWTKeyStroke>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
        if (sdb.getStatusTTN(ttn) == 0) {
            closeTTN();
        } else {
            cbNoSort.setEnabled(true);
            cbPrePayment.setEnabled(true);

            Date d = (Date) param.get("document_date");
            if (d != null) {
                System.out.println("Дата документа " + d);
                if (d.getTime() < DateUtils.getDateByStringValue(keyDate).getTime()) {
                    bWriteButton.setEnabled(true);
                    bCloseTTN.setEnabled(true);

                } else {
                    bWriteButton.setEnabled(false);
                    bCloseTTN.setEnabled(false);
                }
                ;
            }
        }

        cbPrePayment.setSelected((Boolean) param.get("prepayment"));
        //cbNoSort.setSelected((Boolean) param.get("nosort"));

        Valuta valuta = (Valuta) cbValuta.getSelectedItem();
        if (!valuta.getName().toLowerCase().equals("byr")) {
            updateCurrencyRateValue(valuta.getName());
        }
    }

    private void checkDate() throws Exception {
    }

    private void setSelectedCB(String name) {
        for (int i = 0; i < valuta.size(); i++) {
            if (valuta.get(i).getName().equals(name)) {
                cbValuta.setSelectedItem(valuta.get(i));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void setParamOfForm() {
        money.put("koef", tfcFactor.getText().trim().replace(',', '.'));
        money.put("skidka", tfcDiscount.getText().trim().replace(',', '.'));
        money.put("kurs_bel", tfSaleRate.getText().trim().replace(',', '.'));
        if ((ttn1.isSelected() && eks.isSelected() && pricingY.isSelected()) || (tn2.isSelected() && eks.isSelected() && pricingY.isSelected())) {
            money.put("kurs", tfFixedRate.getText().trim().replace(',', '.'));
            money.put("valuta", ((Valuta) cbValuta.getSelectedItem()).getName());
        } else {
            money.put("kurs", -1);
            money.put("valuta", "BYR");
        }
        param.put("razgruz", tfgPunktotgruzCB.getSelectedItem());

        param.put("adres_poluch", tfgPoluchatelCB.getSelectedItem());
        param.put("date", ftDate.getText());
        if (tfgDoverDate.getText().trim().equals("00.00.0000")) {
            param.put("doverDate", "");
        } else {
            param.put("doverDate", tfgDoverDate.getText());
        }
        param.put("nomer", tfNDoc.getText().trim());
        param.put("avto", tfaAvto.getText().trim());
        param.put("voditel", tfaVodatel.getText().trim());
        param.put("trailer", tfaPrits.getText().trim());
        param.put("vladelets_avto", tfaVladelets.getText().trim());
        if (tfaZakazchik.getText().trim().equals("")) {
            param.put("zakazchik_avto", "(ПЛАТЕЛЬЩИК)");
        } else {
            param.put("zakazchik_avto", tfaZakazchik.getText().trim());
        }
        param.put("primechanie", tfgPrimechanie.getText().trim());
        param.put("otpustil", tfgOtpustil.getText().trim());
        param.put("sdal", tfgSdal.getText().trim());
        param.put("nplombi", tfgPlomba.getText().trim());
        param.put("prinial", tfgPrinial.getText().trim());
        param.put("ispolnitel", tfgIspolPogr.getText().trim());
        param.put("pereadres", tfgPereadresovka.getText().trim());
        param.put("doc", tfgDoc.getText().trim());
        param.put("valuta_str", ((Valuta) cbValuta.getSelectedItem()).getAbout());
        MoneyToStr mts = new MoneyToStr(param.get("gruz_mest").toString(), param.get("valuta").toString());
        String str = mts.num2str(true).trim();
        str = str.substring(0, str.lastIndexOf(" "));
        param.put("gruz_mest_str", str);
        WeightToStr wts = new WeightToStr(param.get("massa").toString());
        param.put("massa_str", wts.num2str());
        mts = new MoneyToStr(param.get("summa_nds").toString(), param.get("valuta").toString());
        param.put("summa_nds_str", mts.num2str(true));
        mts = new MoneyToStr(param.get("summa_i_nds").toString(), param.get("valuta").toString());
        param.put("summa_i_nds_str", mts.num2str(true));
        if (ndsN.isSelected()) {
            param.put("nds", tfnds.getText().toString().trim());
        }
        param.put("dover", tfgDoverennost.getText().toString().trim());
        //if(!tfgDoverVidannoi.getText().toString().trim().equals(""))
        param.put("doverVidan", tfgDoverVidannoi.getText().toString().trim());
        param.put("doverPrinial", tfgDoverPrinial.getText().toString().trim());
        param.put("doverPlomba", tfgDoverNomerPlombi.getText().toString().trim());
        param.put("schot_platelschik", tfaPlatelschik.getText().toString().trim());
        if (tfaPlatelschik.getText().toString().trim().isEmpty()) {
            param.put("schot_platelschik", "0");
        } else {
            param.put("schot_platelschik", tfaPlatelschik.getText().toString().trim());
        }
        /*if(tfgDoverennost.getText().trim().equals("00.00.0000"))
         param.put("doverDate", "01.01.1900");
         else
         param.put("doverDate", tfgDoverDate.getText());*/
        param.put("ppogruzki", tfgPunktpogruz.getText().trim());
        param.put("sposob", tfgSposobPogr.getText().trim());
        // param.put("osnovanie", tfgOsnovanie.getItemAt(0).trim());

        if (eks.isSelected()) {
            param.put("cmr", "CMR № " + tfCMR.getText().trim() + " от " + tfCMRDate.getText().trim());
        } else {
            param.put("cmr", " ");
        }

        param.put("adres_otprav", tfgOtpravitel.getText().trim());
        param.put("adrPogr", tfgPunktpogruz.getText().trim());
        if (!tfTorgNad.getText().trim().equals("")) {
            param.put("torg_nadb", tfTorgNad.getText().trim());
        } else {
            param.put("torg_nadb", "0");
        }

        param.put("prepayment", cbPrePayment.isSelected());
        param.put("nosort", cbNoSort.isSelected());

        //param.put("", valuta)
    }

    private float updateCurrencyRateValue(String curr) {
        SkladPDB pdb = new SkladPDB();
        float kurs1;
        float kurs = pdb.getCurrencyRate(curr, DateUtils.getDateByStringValue(ftDate.getText()), false);
        lblFixedRate = new JLabel("Курс НБРБ : ");
        tfSaleRate.setEditable(true);

        if (cbPrePayment.isSelected()) {
            //  lblFixedRate.setText("Курс НБРБ (предопл.)");
            //  String d = (String)param.get("datedoc") ;
            //  kurs = pdb.getCurrencyRate(curr,DateUtils.getDateByStringValueNew(d),false);

            lblSaleRate.setText("Курс предоплаты");
            tfSaleRate.setEditable(true);
            //  kurs1 = pdb.getCurrencyRate(curr, DateUtils.getDateByStringValue(ftDate.getText()), true);
            //  tfSaleRate.setText(String.valueOf(kurs1));
            /*          try {
                kurs = Float.valueOf(tfFixedRate.getText());
            }catch (Exception e){
                e.printStackTrace();
                kurs = 0.f;
            }*/
        } else {
            //lblFixedRate.setText("Курс НБРБ (начало)");
            // Если возвратная накладная
            if (isRetund) {
                lblSaleRate.setText("Курс при отгрузке");
                tfSaleRate.setEditable(true);
            } else {
                tfSaleRate.setEditable(false);
                lblSaleRate.setText("Текущий курс");
                kurs1 = pdb.getCurrencyRate(curr, DateUtils.getDateByStringValue(ftDate.getText()), true);
                tfSaleRate.setText(String.valueOf(kurs1));
            }
        }

        tfFixedRate.setText(String.valueOf(kurs));
        tfFixedRate.setEditable(false);

        pdb.disConn();
        return kurs;
    }

    public void createPrilog() {
        pb = new ProgressBar(thisForm, false, "Форм-ние приложения");
        sw = new SwingWorker() {
            SkladOO soo2;

            @Override
            protected Object doInBackground() throws Exception {
                ArrayList al2 = new ArrayList();
                List<Map<String, Object>> a = new ArrayList();
                SkladDB sdb = new SkladDB();

                al2.add(param);
                float nds = -1;
                if (!sdb.getIzm().equals("")) {
                    param.put("izm", sdb.getIzm());
                } else {
                    param.put("izm", "шт.");
                }
                if (ttn1.isSelected()) {
                    if (eks.isSelected()) {
                        if (pricingY.isSelected()) {
                            a = sdb.getPrilojenieTTNRus(ttn, nds, money);
                        } else {
                            a = sdb.getPrilojenieTTN2(ttn, nds, -1);
                        }
                        soo2 = new SkladOO(a, al2);
                        soo2.createReport("ПриложениеТТНРос.ots");

                        soo2.getCertificateForTTN("Справки для россии.ots");
                    }
//else{
//                                    if(!skidkaSum.isSelected()&&!skidkaCen.isSelected()){
//                                    a = sdb.getPrilojenieTTN(ttn, nds);
//                                    soo2 = new SkladOO(a, al2);
//                                    soo2.createReport("ПриложениеТТН.ots");
//                                    }
//                                }
                    if (pricingY.isSelected() && skidkaSum.isSelected()) {
                        a = sdb.getPrilojenieTTNSkidkaBelSummaDoc(ttn, money);
                        soo2 = new SkladOO(a, al2);
                        soo2.createReport("ПриложениеТТН Скидка сумма док.ots");
                    } else {
                        if (pricingY.isSelected() && skidkaCen.isSelected() && !eks.isSelected()) {
                            a = sdb.getPrilojenieTTNSkidkaBelCenaIzd(ttn, money);
                            //****************************************************************************************
                            //****************************************************************************************
                            int selectComboboxIndex = cbProtocol.getSelectedIndex();

                            if (selectComboboxIndex > 0) {
                                switch (selectComboboxIndex) {
                                    case 1:// Протокол евроопта
                                        soo2 = new SkladOO(a, al2);
                                        soo2.createReport("протоколЕвроторг.ots");
                                        break;
                                    case 2:// Протокол БелВиллесден
                                        soo2 = new SkladOO(a, al2);
                                        soo2.createReport("протоколБелВиллесденНовый.ots");
                                        break;
                                    case 3:// Протокол Юнифуд
                                        soo2 = new SkladOO(a, al2);
                                        soo2.createReport("протоколЮнифуд.ots");
                                        break;
                                    case 4:// Стандартный протокол
                                        soo2 = new SkladOO(a, al2);
                                        soo2.createReport("протоколБелпочта.ots");
                                        break;
                                    case 5:// Стандартный протокол
                                        soo2 = new SkladOO(a, al2);
                                        soo2.createReport("протоколГринРозница.ots");
                                        break;

                                    case 6:// Стандартный протокол
                                        soo2 = new SkladOO(a, al2);
                                        soo2.createReport("протоколСтандартный.ots");
                                        break;
                                    case 7:// Простор протокол
                                        soo2 = new SkladOO(a, al2);
                                        soo2.createReport("протоколПростор.ots");
                                        break;
                                    case 8:// Табак инвест протокол
                                        soo2 = new SkladOO(a, al2);
                                        soo2.createReport("протоколТабакИнвест.ots");
                                        break;
                                    default:
                                        break;
                                }
                            }
                            //****************************************************************************************
                            //****************************************************************************************

                            soo2 = new SkladOO(a, al2);
                            soo2.createReport("ПриложениеТТН Скидка цена изд.ots");
                        } else {
                            if (!eks.isSelected() && !tn2.isSelected() && ttn1.isSelected() && !pricingY.isSelected() && !cbPolotno.isSelected()) {
                                a = sdb.getPrilojenieTTN(ttn, nds);
                                soo2 = new SkladOO(a, al2);
                                soo2.createReport("ПриложениеТТН.ots");
                            } else {
                                if (cbPolotno.isSelected() && !eks.isSelected()) {
                                    sdb.updateSummForPolotnoDefaultNDS(ttn, nds, Float.valueOf(tfFixedRate.getText()));
                                    a = sdb.getPrilojenieTTN(ttn, nds);
                                    soo2 = new SkladOO(a, al2);
                                    soo2.createReport("ПриложениеТТН.ots");
                                }
                            }
                        }
                    }
                } else {
                    if (tn2.isSelected()) {
                        if (eks.isSelected()) {
                            if (pricingY.isSelected()) {
                                a = sdb.getPrilojenieTTNRus(ttn, nds, money);
                            } else {
                                a = sdb.getPrilojenieTTN2(ttn, nds, -1);
                            }
                            soo2 = new SkladOO(a, al2);
                            soo2.createReport("ПриложениеТТНРос.ots");
                            soo2.getCertificateForTTN("Справки для россии.ots");
                        } else {
                            a = sdb.getPrilojenieTTN(ttn, nds);
                            soo2 = new SkladOO(a, al2);
                            soo2.createReport("ПриложениеТТН.ots");
                        }
                    } else {
                        if (torgY.isSelected()) {
                            torgNadbavka = Float.parseFloat(tfTorgNad.getText());
                            sdb.setOtgruzAddition(Integer.parseInt(cbRound.getItemAt(cbRound.getSelectedIndex())), ttn, (int) torgNadbavka);
                        } else {
                            torgNadbavka = -1;
                            sdb.setOtgruzAddition(Integer.parseInt(cbRound.getItemAt(cbRound.getSelectedIndex())), ttn);
                        }
                        if (!ndsY.isSelected()) {
                            nds = -1;
                        } else {
                            nds = Float.valueOf(param.get("nds").toString());
                        }
                        a = sdb.getPrilojenieTTN2(ttn, nds, torgNadbavka);
                        soo2 = new SkladOO(a, al2);
                        if (!cbSimpleAnnex.isSelected()) {
                            soo2.createReport(Integer.parseInt(cbRound.getItemAt(cbRound.getSelectedIndex())), "ПриложениеТТН2.ots");
                        } else {
                            soo2.createReport("ПриложениеТТН2Простой.ots");
                        }


//                                    soo2 = new SkladOO(a, al2);
//                                    soo2.createReport("ПриложениеТТН2.ots");
                        //a = sdb.getPrilojenieTTN2(ttn, -1);
                        //sdb.setOtgruzAddition(ttn);

                    }

                }
                if (!sdb.getIzm().equals("")) {
                    param.put("izm", sdb.getIzm());
                } else {
                    param.put("izm", "шт.");
                }
                if (!sdb.getTypeProd().equals("")) {
                    param.put("type_prod", sdb.getTypeProd());
                } else {
                    param.put("type_prod", "Трикотажные,чулочно-носочные изделия");
                }

                //sdb.disConn();
                return 0;
            }

            @Override
            protected void done() {
                try {
                    BigDecimal n = new BigDecimal(soo2.result.get("kol").toString());
                    n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
                    param.put("kol", n.floatValue());
                    param.put("summa", soo2.result.get("summa"));
                    param.put("summa_rus", soo2.result.get("summa_rus"));
                    param.put("summa_nds", soo2.result.get("summa_nds"));
                    param.put("summa_i_nds", soo2.result.get("summa_i_nds"));
                    param.put("summa_nds_rus", soo2.result.get("summa_nds_rus"));
                    param.put("summa_i_nds_rus", soo2.result.get("summa_i_nds_rus"));
                    param.put("gruz_mest", soo2.result.get("gruz_mest"));
                    param.put("massa", soo2.result.get("massa"));
                    param.put("page_count", soo2.result.get("page_count"));
                    DecimalFormat df = new DecimalFormat("###.##");
                    MoneyToStr mts = new MoneyToStr(param.get("gruz_mest").toString(), param.get("valuta").toString());
                    String str = mts.num2str(true).trim();
                    str = str.substring(0, str.lastIndexOf(" "));
                    param.put("gruz_mest_str", str);
                    WeightToStr wts = new WeightToStr(param.get("massa").toString());
                    param.put("massa_str", wts.num2str());
                    mts = new MoneyToStr(df.format(Double.parseDouble(param.get("summa_nds").toString().replace(',', '.'))), param.get("valuta").toString());
                    param.put("summa_nds_str", mts.num2str(true));
                    String dsa = param.get("summa_i_nds").toString();
                    mts = new MoneyToStr(df.format(Double.parseDouble(dsa)), param.get("valuta").toString());
                    param.put("summa_i_nds_str", mts.num2str(true));
                    pb.dispose();
                } catch (Exception e) {
                    System.out.println("Возникла ошибка при завершеннии потока CreatePrilog() ");
                }
            }
        };
        sw.execute();
        pb.setVisible(true);
        thisForm.toFront();
    }

    public void createPutevoi() {
        pb = new ProgressBar(thisForm, false, "Форм-ние путевого листа");
        sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                ArrayList al = new ArrayList();
                ArrayList al2 = new ArrayList();
                al2.add(param);
                //int nds = -1;
                if (param.get("klient_id").toString().trim().equals("799") || param.get("klient_id").toString().trim().equals("2022")) {
                    param.put("nds", 0);
                }
//                          if(eks.isSelected() && pricingY.isSelected()){
//                              sdb.changePrice(ttn, Float.parseFloat(money.get("koef").toString()), Float.parseFloat(money.get("skidka").toString()), Float.parseFloat(money.get("kurs_bel").toString()), nds);
//                          }
                if (pricingY.isSelected()) {
                    if (money.get("skidka").toString().equals("0") || money.get("skidka").toString().equals("0.0") || money.get("skidka").toString().equals("0,0")) {
                        param.put("skidka1", "  ");
                    } else {
                        if (!skidkaCen.isSelected()) {
                            param.put("skidka1", "Скидка " + money.get("skidka").toString() + "%");
                        } else {
                            param.put("skidka1", "  ");
                        }
                    }
                } else {
                    param.put("skidka1", "  ");
                }
                al.add(param);
                SkladOO soo = new SkladOO(al, al2);
                if (bel.isSelected() && !tn2.isSelected()) {
                    soo.createReport("ПутевойЛист.ott");
                }
                if (eks.isSelected()) {
                    MoneyToStr mts = new MoneyToStr(param.get("summa_nds_rus").toString(), param.get("valuta").toString());
                    param.put("summa_nds_str", mts.num2str());
                    mts = new MoneyToStr(param.get("summa_i_nds_rus").toString(), param.get("valuta").toString());
                    param.put("summa_i_nds_str", mts.num2str());
                    al.add(param);
                    soo = new SkladOO(al, al2);
                    if (tn2.isSelected()) {
                        //TTN - 2
                        System.out.println("Путевой ТТН 2");
                        soo.createReport("ПутевойЛистРус.ott");
                    } else {
                        //TTN - 1
                        System.out.println("Путевой ТТН 1");
                        soo.createReport("ПутевойЛистРусТТН_new.ott");
                    }
                }
                if (tn2.isSelected() && !eks.isSelected()) {
                    al.add(param);
                    soo = new SkladOO(al, al2);
                    soo.createReport("ПЛТН2.ott");
                }
                return 0;
            }

            @Override
            protected void done() {
                pb.dispose();
            }
        };
        sw.execute();
        pb.setVisible(true);
    }

    public void conversion() {
        pb = new ProgressBar(thisForm, false, "Перерасчёт цен");
        sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                SkladDB sdb = new SkladDB();
                float nds = -1;
                if (pricingY.isSelected() && ndsN.isSelected() && !cbPolotno.isSelected()) {
                    nds = Float.parseFloat(param.get("nds").toString());
                }

                if (tn2.isSelected() && cbSebestoim.isSelected() && bel.isSelected() && pricingN.isSelected()) {
                    System.out.println("Конверсия 1");
                    sdb.changePriceToSebestoim(ttn, 0);

                    //sdb.updateSumTTN(ttn);
                }

                if (pricingY.isSelected() && eks.isSelected() && !cbPolotno.isSelected()) {

                    /*SpecCenaDB scdb = new SpecCenaDB();
                     scdb.setSpecCena(ttn);*/
                   /* if (isRetund) {
                        System.out.println("Конверсия 3");
                        //sdb.changePrice(ttn, Float.parseFloat(money.get("koef").toString()), Float.parseFloat(money.get("skidka").toString()), Float.parseFloat(money.get("kurs").toString()), Float.parseFloat(money.get("kurs_bel").toString()), nds, money.get("valuta").toString());
                        sdb.changePrice(ttn, Float.parseFloat(money.get("koef").toString()), Float.parseFloat(money.get("skidka").toString()), -1, Float.parseFloat(money.get("kurs_bel").toString()), nds, money.get("valuta").toString());
                    }else{
                        */
                    System.out.println("Конверсия 4");
                    sdb.changePriceFork(ttn, Float.parseFloat(money.get("koef").toString()), Float.parseFloat(money.get("skidka").toString()), Float.parseFloat(money.get("kurs").toString()), Float.parseFloat(money.get("kurs_bel").toString()), nds, money.get("valuta").toString());


//                if(qwert==0)
//                    sdb.changePrice(ttn, Float.parseFloat(money.get("koef").toString()), Float.parseFloat(money.get("skidka").toString()), Float.parseFloat(money.get("kurs").toString()), Float.parseFloat(money.get("kurs_bel").toString()), nds,val[0]);
//                if(qwert==1)
//                    sdb.changePrice(ttn, Float.parseFloat(money.get("koef").toString()), Float.parseFloat(money.get("skidka").toString()), Float.parseFloat(money.get("kurs").toString()), Float.parseFloat(money.get("kurs_bel").toString()), nds,val[1]);
//                if(qwert==2)
//                    sdb.changePrice(ttn, Float.parseFloat(money.get("koef").toString()), Float.parseFloat(money.get("skidka").toString()), Float.parseFloat(money.get("kurs").toString()), Float.parseFloat(money.get("kurs_bel").toString()), nds,val[2]);
//                if(qwert==3)
//                    sdb.changePrice(ttn, Float.parseFloat(money.get("koef").toString()), Float.parseFloat(money.get("skidka").toString()),Float.parseFloat(money.get("kurs").toString()), Float.parseFloat(money.get("kurs_bel").toString()), nds,val[3]);
//                if(qwert==4)
//                    sdb.changePrice(ttn, Float.parseFloat(money.get("koef").toString()), Float.parseFloat(money.get("skidka").toString()), Float.parseFloat(money.get("kurs").toString()), Float.parseFloat(money.get("kurs_bel").toString()), nds,val[4]);
                }
                if (pricingY.isSelected() && bel.isSelected() && skidkaSum.isSelected() && !cbPolotno.isSelected() && ttn1.isSelected()) {
                    System.out.println("Конверсия 5");
                    sdb.changePriceToBelSumDoc(ttn, Float.parseFloat(money.get("skidka").toString()));
                    sdb.updateSumTTN(ttn);

                }

                if (pricingY.isSelected() && bel.isSelected() && skidkaCen.isSelected() && !tn2.isSelected() && !cbPolotno.isSelected()) {
                    System.out.println("Конверсия 6");
                    sdb.changePriceToBelCenaIzd(ttn, Float.parseFloat(money.get("skidka").toString()));
                    System.out.println("Конверсия 6-1");
                    sdb.updateSumTTN(ttn);
                }

                if (pricingY.isSelected() && bel.isSelected() && tn2.isSelected() && !cbPolotno.isSelected() && !ttn2.isSelected()) {
                    System.out.println("Конверсия 7");
                    sdb.changePriceToTN2NDS(ttn, nds);
                    sdb.updateSumTTN(ttn);
                }

                if (ttn2.isSelected() && !skidkaCen.isSelected() && !skidkaSum.isSelected() && ndsN.isSelected() && pricingY.isSelected()) {
                    System.out.println("Конверсия 8");
                    sdb.changePriceFirmMag(ttn, nds);
                    sdb.updateSumTTN(ttn);

                }

                if (cbPolotno.isSelected() && ndsY.isSelected()) {
                    System.out.println("Конверсия 9");
                    sdb.updateSummForPolotnoDefaultNDS(ttn, Float.valueOf(tfnds.getText()), Float.valueOf(tfFixedRate.getText()));
                }

                if (cbPolotno.isSelected() && ndsN.isSelected()) {
                    System.out.println("Конверсия 10");
                    sdb.updateSummForPolotnoDefaultNDS(ttn, Float.valueOf(tfnds.getText()), Float.valueOf(tfFixedRate.getText()));
                }
                sdb.disConn();
                return 0;
            }

            @Override
            protected void done() {
                pb.dispose();
            }
        };
        sw.execute();
        pb.setVisible(true);
    }

    public void setPutList() {
        SkladDB db = new SkladDB();
        setParamOfForm();
        db.writePutList(param, money);
        sdb.setRadioButtonPosition(ttn, radioButt);
        db.disConn();
    }

    public void statusForTN(boolean state) {
        tfgOtpravitel.setEditable(false);
        tfgOtpravitel.setEnabled(true);
        if (tn2.isSelected()) {
            tfgOsnovanie.setEnabled(true);
        } else {
            tfgOsnovanie.setEnabled(false);
        }
        tfgOsnovanie.setEnabled(true);
        tfgPunktpogruz.setEditable(false);
        tfgPunktpogruz.setEnabled(true);
        tfgSposobPogr.setEditable(false);
        tfgSposobPogr.setEnabled(true);
        tfgPunktotgruzCB.setEnabled(true);
        tfgPoluchatelCB.setEnabled(true);
        tfFixedRate.setEditable(false);
        tfaAvto.setEditable(state);
        tfaPlatelschik.setEditable(state);
        tfaPrits.setEditable(state);
        tfaVladelets.setEditable(state);
        tfaVodatel.setEditable(state);
        tfaZakazchik.setEditable(state);
        tfcDiscount.setEditable(state);
        tfcFactor.setEditable(state);
        tfSaleRate.setEditable(state);
        tfgDoc.setEditable(state);
        tfgDoverNomerPlombi.setEditable(state);
        tfgIspolPogr.setEditable(state);
        tfgPereadresovka.setEditable(state);
        tfgPlomba.setEditable(state);
        tfgPrimechanie.setEditable(state);
        tfnds.setEditable(state);
        //        dateChooser.setEnabled(state);
        skidkaSum.setEnabled(state);
        skidkaCen.setEnabled(state);
        ndsY.setEnabled(state);
        ndsN.setEnabled(state);
        torgN.setEnabled(state);
        torgY.setEnabled(state);
        tfTorgNad.setEditable(state);
    }

    public void setRadioButton(int[] a) {
        /*
         * radioButton  для выбора типа документов ТТН1 ТТН1(для фирм) ТН
         */
        if (a[0] == 0) {
            ttn1.setSelected(true);
            setPropElements();
        } else {
            if (a[0] == 1) {
                ttn2.setSelected(true);

                setPropElements();
            } else {
                tn2.setSelected(true);
                setPropElements();
            }
        }
        /*
         *  был ли выбран экспорт
         */
        if (a[1] == 0) {
            bel.setSelected(true);
            setPropElements();
        } else {
            eks.setSelected(true);
            setPropElements();
        }
        /*
         * Выбирался ли пересчет
         */
        if (a[2] == 0) {
            pricingN.setSelected(true);
            setPropElements();
        } else {
            if (bel.isSelected()) {
                pricingY.setSelected(true);
                setPropElements();
            } else {
                pricingY.setSelected(true);
                setPropElements();
            }
        }
        /*
         * Какой тип скидки был выбран
         */
        if (a[3] == 1) {
            skidkaCen.setSelected(true);
        } else {
            if (a[3] == 2) {
                skidkaSum.setSelected(true);
            } else {
                skidkaCen.setSelected(false);
                skidkaSum.setSelected(false);
            }
        }
        /*
         * НДС атоматическое значение или заданно руками
         */
        if (a[4] == 0) {
            ndsY.setSelected(true);
            tfnds.setEnabled(true);
        } else {
            ndsN.setSelected(true);
            tfnds.setEnabled(false);
        }
        if (a[5] == 0) {
            torgN.setSelected(true);
            tfTorgNad.setEditable(false);
        } else {
            torgY.setSelected(true);
            tfTorgNad.setEditable(true);
        }
        /*
         * Порядок нажжатых клавиш (записать, закрыть, сформировать)
         */
        /*      if (a[6]==1){
         bWriteButton.setEnabled(true);
         bCloseTTN.setEnabled(false);
         bPrint.setEnabled(false);
         }
         if (a[6]==0){
         bWriteButton.setEnabled(true);
         bCloseTTN.setEnabled(false);
         bPrint.setEnabled(false);
         }
         if (a[6]==4){
         bWriteButton.setEnabled(true);
         bCloseTTN.setEnabled(true);
         bPrint.setEnabled(false);
         }
         if (a[6]==9){
         bWriteButton.setEnabled(false);
         bCloseTTN.setEnabled(false);
         bPrint.setEnabled(true);
         }
         */
    }

    public boolean ErrorInput() {
        boolean er = true;
        if (tn2.isSelected()) {
            er = true;
        }
        if (pricingY.isSelected() && !eks.isSelected()) {
            if (!skidkaCen.isSelected() && !skidkaSum.isSelected() && !tn2.isSelected() && !ttn2.isSelected()) {
                er = false;
                JOptionPane.showMessageDialog(null, "При выборе скидки необходимо выбрать тип скидки \n (на цену изделия или на сумму документа)", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            }
        }

        Valuta valuta = (Valuta) cbValuta.getSelectedItem();
        if (pricingY.isSelected() && eks.isSelected() && !cbPolotno.isSelected() && !valuta.getName().toLowerCase().equals("byr")) {
            if (updateCurrencyRateValue(valuta.getName()) == 0) {
                String tDate = DateUtils.getNormalDateFormat(DateUtils.getFirstDay(DateUtils.getDateByStringValue(ftDate.getText())));
                JOptionPane.showMessageDialog(null, "В базе отсутствует курс Нац.банка для валюты " + valuta.getName() + "\nпо состоянию на " + tDate + "г.\nОбратитесь в плановый отдел", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                er = false;
            }

            String sSale = " на момент отгрузки ";
            String sPrepay = " на момент предоплаты ";
            String res;
            if (cbPrePayment.isSelected()) {
                res = sPrepay;
            } else {
                res = sSale;
            }

            try {
                float temp = Float.parseFloat(tfSaleRate.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Введите корректное значение курса валюты " + res, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                er = false;
            }

            if (tfSaleRate.getText().equals("0.0")) {
                String tDate = DateUtils.getNormalDateFormat(DateUtils.getDateByStringValue(ftDate.getText()));
                JOptionPane.showMessageDialog(null, "В базе отсутствует курс Нац.банка для валюты " + valuta.getName() + sSale, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                er = false;
            }
        }

        if (tfNDoc.getText().trim().equals("0") || tfNDoc.getText().trim().equals("")) {
            er = false;
            JOptionPane.showMessageDialog(null, "Обязательно нужно ввести номер путевого листа!", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }

        //        if (param.get("razgruz").toString().trim().equals("")||param.get("razgruz").toString().trim().equals(null)){
        //            er=false;
        //            JOptionPane.showMessageDialog(null, "Не указан пункт разгрузки!!");
        //        }

        /*
        if (tfgOsnovanie.getText().trim().equals("")||tfgOsnovanie.getText().trim().equals(null))
        {
        JOptionPane.showMessageDialog(null,"Отсутствует основание отпуска", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        er=false;
        }
        */
        return er;
    }

    public void setPropElements() {
        pRound.setVisible(false);
        if (ttn1.isSelected()) {
            cbSimpleAnnex.setVisible(false);
            if (bel.isSelected()) {
                if (pricingY.isSelected()) {
                    pricingN.setEnabled(true);
                    pricingY.setEnabled(true);
                    statusForTN(true);
                    ttn2.setEnabled(true);
                    tn2.setEnabled(true);
                    cbValuta.setEnabled(false);
                    torgN.setEnabled(false);
                    torgY.setEnabled(false);
                    ndsN.setEnabled(false);
                    ndsY.setEnabled(false);
                    if (ndsN.isSelected()) {
                        tfnds.setEditable(true);
                    } else {
                        tfnds.setEditable(false);
                    }
                    //tfnds.setEditable(false);
                    tfcDiscount.setEditable(true);
                    tfFixedRate.setEditable(false);
                    tfSaleRate.setEditable(false);
                    tfTorgNad.setEditable(false);
                    tfcFactor.setEditable(false);
                } else {
                    pricingN.setEnabled(true);
                    pricingY.setEnabled(true);
                    statusForTN(true);
                    ttn2.setEnabled(true);
                    tn2.setEnabled(true);
                    cbValuta.setEnabled(false);
                    ndsN.setEnabled(false);
                    ndsY.setEnabled(false);
                    torgN.setEnabled(false);
                    torgY.setEnabled(false);
                    skidkaCen.setEnabled(false);
                    skidkaSum.setEnabled(false);
                    tfcDiscount.setEditable(false);
                    tfFixedRate.setEditable(false);
                    tfSaleRate.setEditable(false);
                    tfTorgNad.setEditable(false);
                    tfcFactor.setEditable(false);
                    if (ndsN.isSelected()) {
                        tfnds.setEditable(true);
                    } else {
                        tfnds.setEditable(false);
                    }
                }
            } else {
                if (pricingY.isSelected()) {
                    pricingN.setEnabled(true);
                    pricingY.setEnabled(true);
                    statusForTN(true);
                    ttn2.setEnabled(false);
                    tn2.setEnabled(true);
                    torgN.setEnabled(false);
                    torgY.setEnabled(false);
                    cbValuta.setEnabled(true);
                    ndsN.setEnabled(true);
                    lCMR.setVisible(true);
                    lCMRDate.setVisible(true);
                    tfCMR.setVisible(true);
                    tfCMRDate.setVisible(true);
                    ndsY.setEnabled(true);
                    skidkaCen.setEnabled(true);
                    skidkaSum.setEnabled(true);
                    tfcDiscount.setEditable(true);
                    tfTorgNad.setEditable(false);
                    tfSaleRate.setEditable(true);
                    tfcFactor.setEditable(true);
                    tfFixedRate.setEditable(false);
                    if (ndsN.isSelected()) {
                        tfnds.setEditable(true);
                    } else {
                        tfnds.setEditable(false);
                    }
                } else {
                    pricingN.setEnabled(true);
                    pricingY.setEnabled(true);
                    statusForTN(true);
                    ttn2.setEnabled(false);
                    tn2.setEnabled(true);
                    cbValuta.setEnabled(false);
                    ndsN.setEnabled(false);
                    ndsY.setEnabled(false);
                    lCMR.setVisible(true);
                    lCMRDate.setVisible(true);
                    tfCMR.setVisible(true);
                    tfCMRDate.setVisible(true);
                    torgN.setEnabled(true);
                    torgY.setEnabled(true);
                    skidkaCen.setEnabled(false);
                    skidkaSum.setEnabled(false);
                    if (ndsN.isSelected()) {
                        tfnds.setEditable(true);
                    } else {
                        tfnds.setEditable(false);
                    }
                    //tfnds.setEditable(false);
                    tfcDiscount.setEditable(false);
                    tfFixedRate.setEditable(false);
                    tfSaleRate.setEditable(false);
                    tfTorgNad.setEditable(false);
                    tfcFactor.setEditable(false);
                }
            }
            cbSimpleAnnex.setVisible(false);
        } else {
            if (ttn2.isSelected()) {
                pRound.setVisible(true);
                cbSimpleAnnex.setVisible(true);
                if (pricingY.isSelected()) {
                    pricingN.setEnabled(true);
                    pricingY.setEnabled(true);
                    statusForTN(true);
                    skidkaCen.setEnabled(true);
                    skidkaSum.setEnabled(true);
                    tfcDiscount.setEditable(true);
                    tfcDiscount.setEnabled(true);
                    ttn2.setEnabled(true);
                    tn2.setEnabled(true);
                    cbValuta.setEnabled(false);
                    torgN.setEnabled(true);
                    torgY.setEnabled(true);
                    ndsN.setEnabled(true);
                    ndsY.setEnabled(true);

                    if (!ndsN.isSelected()) {
                        tfnds.setEditable(true);
                    } else {
                        tfnds.setEditable(false);
                    }
                    tfcDiscount.setEditable(true);
                    tfFixedRate.setEditable(false);
                    tfSaleRate.setEditable(false);
                    tfcFactor.setEditable(false);
                    //tfnds.setEditable(false);
                    if (torgN.isSelected()) {
                        tfTorgNad.setEditable(false);
                    } else {
                        tfTorgNad.setEditable(true);
                    }
                } else {
                    pricingN.setEnabled(true);
                    pricingY.setEnabled(true);
                    statusForTN(true);
                    ttn2.setEnabled(true);
                    tn2.setEnabled(true);
                    cbValuta.setEnabled(false);
                    torgN.setEnabled(false);
                    torgY.setEnabled(false);
                    ndsN.setEnabled(false);
                    ndsY.setEnabled(false);
                    if (ndsN.isSelected()) {
                        tfnds.setEditable(true);
                    } else {
                        tfnds.setEditable(true);
                    }
                    skidkaCen.setEnabled(false);
                    skidkaSum.setEnabled(false);
                    tfTorgNad.setEditable(false);
                    tfcDiscount.setEditable(false);
                    tfFixedRate.setEditable(false);
                    tfSaleRate.setEditable(false);
                    tfcFactor.setEditable(false);
                }
            } else {
                cbSimpleAnnex.setVisible(false);
                pRound.setVisible(false);
                if (bel.isSelected()) {
                    if (pricingY.isSelected()) {
//                    if(eks.isSelected()){
//                        tfCMR.setEnabled(true);
//                        tfCMRDate.setEnabled(true);
//                    }else{
//                        tfCMR.setEnabled(true);
//                        tfCMRDate.setEnabled(true);
//                    }
                        //Поля не нужные в ТН-2
                        statusForTN(false);
                        //Вкаладка пересчет
                        ttn2.setEnabled(true);
                        tn2.setEnabled(true);
                        tfcDiscount.setEnabled(false);
                        cbValuta.setEnabled(true);
                        torgN.setEnabled(false);
                        torgY.setEnabled(false);
                        ndsN.setEnabled(true);

                        ndsY.setEnabled(true);
                        tfnds.setEnabled(true);
                        skidkaCen.setEnabled(false);
                        skidkaSum.setEnabled(false);
                        tfSaleRate.setEditable(false);
                        tfTorgNad.setEditable(false);
                        tfcFactor.setEditable(false);
                        tfnds.setEditable(false);
                        tfcDiscount.setEditable(true);
                        tfFixedRate.setEditable(false);
                        if (ndsN.isSelected()) {
                            tfnds.setEditable(true);
                        } else {
                            tfnds.setEditable(false);
                        }
                    } else {
//                    if(eks.isSelected()){
//                        tfCMR.setEnabled(true);
//                        tfCMRDate.setEnabled(true);
//                    }else{
//                        tfCMR.setEnabled(true);
//                        tfCMRDate.setEnabled(true);
//                    }
                        //Поля не нужные в ТН-2
                        statusForTN(false);
                        //Вкаладка пересчет                    
                        ttn2.setEnabled(true);
                        tn2.setEnabled(true);
                        cbValuta.setEnabled(false);
                        ndsN.setEnabled(false);
                        ndsY.setEnabled(false);
                        torgN.setEnabled(false);
                        torgY.setEnabled(false);
                        skidkaCen.setEnabled(false);
                        skidkaSum.setEnabled(false);
                        tfcDiscount.setEnabled(true);
                        tfSaleRate.setEditable(false);
                        tfFixedRate.setEditable(false);
                        tfnds.setEditable(true);
                        tfTorgNad.setEditable(false);
                        tfcFactor.setEditable(false);
                        tfcDiscount.setEditable(false);
                    }
                } else {
//                    if(param.get("primechanie").toString().equals("")){
//                            tfgPrimechanie = new JTextField("Условия поставки ФСА-Гомель");
//                        }else 
//                            tfgPrimechanie = new JTextField(param.get("primechanie").toString());
//                        
//                        if(param.get("otpustil").toString().equals("")){
//                            tfgOtpustil = new JTextField("Cпец. ОВЭС ");
//                        }else 
//                        tfgOtpustil = new JTextField(param.get("otpustil").toString());
//                        if(param.get("sdal").toString().equals("")){
//                            tfgSdal = new JTextField("Зав. скл. гот. прод. ");
//                        }else 
//                            tfgSdal = new JTextField(param.get("sdal").toString());
                    lCMR.setVisible(true);
                    lCMRDate.setVisible(true);
                    tfCMR.setVisible(true);
                    tfCMRDate.setVisible(true);
                    if (pricingY.isSelected()) {
//                      //Поля не нужные в ТН-2
                        statusForTN(true);
                        //Вкаладка пересчет
                        ttn2.setEnabled(false);
                        tn2.setEnabled(true);
                        tfcDiscount.setEnabled(false);
                        cbValuta.setEnabled(true);
                        torgN.setEnabled(false);
                        torgY.setEnabled(false);
                        ndsN.setEnabled(true);

                        ndsY.setEnabled(true);
                        tfnds.setEnabled(false);
                        skidkaCen.setEnabled(false);
                        skidkaSum.setEnabled(false);
                        tfSaleRate.setEditable(true);
                        tfTorgNad.setEditable(false);
                        tfcFactor.setEditable(true);
                        tfnds.setEditable(false);
                        tfcDiscount.setEditable(true);
                        tfFixedRate.setEditable(false);
                        //tfFixedRate.setEnabled(true);
                        if (ndsN.isSelected()) {
                            tfnds.setEditable(true);
                        } else {
                            tfnds.setEditable(false);
                        }
                    } else {
                        //Поля не нужные в ТН-2
                        statusForTN(false);
                        //Вкаладка пересчет                    
                        ttn2.setEnabled(false);
                        tn2.setEnabled(true);
                        cbValuta.setEnabled(false);
                        ndsN.setEnabled(false);
                        ndsY.setEnabled(false);
                        torgN.setEnabled(false);
                        torgY.setEnabled(false);
                        skidkaCen.setEnabled(false);
                        skidkaSum.setEnabled(false);
                        tfcDiscount.setEnabled(true);
                        tfSaleRate.setEditable(false);
                        tfFixedRate.setEditable(false);
                        tfnds.setEditable(false);
                        tfTorgNad.setEditable(false);
                        tfcFactor.setEditable(false);
                        tfcDiscount.setEditable(false);
                    }
                }
            }
        }
    }

    public void closeTTN() {
        cbPrePayment.setEnabled(false);
        cbNoSort.setEnabled(false);
        bCloseTTN.setEnabled(false);
        bWriteButton.setEnabled(false);
        ttn1.setEnabled(false);
        ttn2.setEnabled(false);
        tn2.setEnabled(false);
        bel.setEnabled(false);
        eks.setEnabled(false);
        pricingN.setEnabled(false);
        pricingY.setEnabled(false);
        tfNDoc.setEditable(false);
        tfgOtpustil.setEditable(false);
        tfgSdal.setEditable(false);
        tfgPrinial.setEditable(false);
        tfgDoverDate.setEditable(false);
        tfgDoverVidannoi.setEditable(false);
        tfFixedRate.setEditable(false);
        tfaAvto.setEditable(false);
        tfaPlatelschik.setEditable(false);
        tfaPrits.setEditable(false);
        tfaVladelets.setEditable(false);
        tfaVodatel.setEditable(false);
        tfaZakazchik.setEditable(false);
        tfcDiscount.setEditable(false);
        tfcFactor.setEditable(false);
        tfSaleRate.setEditable(false);
        tfgDoc.setEditable(false);
        tfgDoverNomerPlombi.setEditable(false);
        tfgIspolPogr.setEditable(false);
        tfgPereadresovka.setEditable(false);
        tfgPlomba.setEditable(false);
        tfgPoluchatelCB.setEnabled(false);
        tfgPrimechanie.setEditable(false);
        tfgPunktotgruzCB.setEnabled(false);
        tfgPunktpogruz.setEditable(false);
        tfgSposobPogr.setEditable(false);
        tfnds.setEditable(false);
        cbValuta.setEnabled(false);
        tfgDoverDate.setEditable(false);
        tfgDoverennost.setEditable(false);
        tfgPrinial.setEditable(false);
        ftDate.setEditable(false);
        tfgOsnovanie.setEditable(false);
        //tfgOsnovanie.setEnabled(false);
        cbPolotno.setEnabled(false);
        cbSebestoim.setEnabled(false);
        tfCMR.setEnabled(true);
        tfCMRDate.setEnabled(true);
        //dateChooser.setEnabled(false);
        //dateChooser2.setEnabled(false);
        tfgPrinial.setEditable(false);
        ndsN.setEnabled(false);
        ndsY.setEnabled(false);
        skidkaCen.setEnabled(false);
        skidkaSum.setEnabled(false);
        tfgDoverPrinial.setEditable(false);
        tfTorgNad.setEditable(false);
        torgN.setEnabled(false);
        torgY.setEnabled(false);
        bWriteButton.setEnabled(false);
        bCloseTTN.setEnabled(false);
        bPrint.setText("Распечатать");
        tfgPoluchatelCB.setEnabled(true);
        tfgPunktotgruzCB.setEnabled(true);
        tfgPoluchatelCB.setEditable(false);
        tfgPunktotgruzCB.setEditable(false);
    }

    public String convetDate(String da) {
        String dfstr, buf;
        Calendar cc;
        int i;
        dfstr = da;
        if (da.equals("")) {
            cc = Calendar.getInstance();
            dfstr = "";
            i = cc.get(Calendar.DAY_OF_MONTH);
            if (i < 10) {
                dfstr += "0" + Integer.toString(i) + ".";
            } else {
                dfstr += "" + Integer.toString(i) + ".";
            }
            i = cc.get(Calendar.MONTH) + 1;
            if (i < 10) {
                dfstr += "0" + Integer.toString(i) + ".";
            } else {
                dfstr += "" + Integer.toString(i) + ".";
            }
            dfstr += "" + cc.get(Calendar.YEAR);
        } else {
            buf = "";
            i = Integer.parseInt(dfstr.substring(8, 10));
            if (i < 10) {
                buf += "0" + Integer.toString(i) + ".";
            } else {
                buf += "" + Integer.toString(i) + ".";
            }
            i = Integer.parseInt(dfstr.substring(5, 7));
            if (i < 10) {
                buf += "0" + Integer.toString(i) + ".";
            } else {
                buf += "" + Integer.toString(i) + ".";
            }
            i = Integer.parseInt(dfstr.substring(0, 4));
            buf += "" + Integer.toString(i);
            dfstr = "";
            dfstr = buf.trim();
        }
        return dfstr;
    }

    public Object[][] getValuesFromTable() {
        Object[][] data;
        Object[][] dat = new Object[1][1];

        try {
            if (jtTablePolotno.getColumnCount() == 7) {
                data = new Object[jtTablePolotno.getRowCount()][8];
                for (int i = 0; i < jtTablePolotno.getRowCount(); i++) {
                    data[i][0] = jtTablePolotno.getValueAt(i, 0);
                    data[i][1] = jtTablePolotno.getValueAt(i, 1);
                    data[i][2] = jtTablePolotno.getValueAt(i, 2);
                    data[i][3] = jtTablePolotno.getValueAt(i, 3);
                    data[i][4] = jtTablePolotno.getValueAt(i, 4);

                    if (jtTablePolotno.getValueAt(i, 5) != null) {
                        data[i][5] = (jtTablePolotno.getValueAt(i, 5));
                    } else {
                        data[i][5] = ("0");
                    }

                    if (jtTablePolotno.getValueAt(i, 6) != null) {
                        data[i][6] = (jtTablePolotno.getValueAt(i, 6));
                    } else {
                        data[i][6] = ("0");
                    }

//                    if (jtTablePolotno.getValueAt(i, 7)!=null)
//                     data1.add(jtTablePolotno.getValueAt(i, 7));
//                     else
//                     data1.add("");
//
//                    if (cbUnits.isSelected())
//                        data[i][7]=("м.");
//                    else
//                        data[i][7]=("кг.");       
                }
                dat = data;
            }
            if (jtTablePolotno.getColumnCount() == 8) {
                data = new Object[jtTablePolotno.getRowCount()][9];
                for (int i = 0; i < jtTablePolotno.getRowCount(); i++) {
                    data[i][0] = jtTablePolotno.getValueAt(i, 0);
                    data[i][1] = jtTablePolotno.getValueAt(i, 1);
                    data[i][2] = jtTablePolotno.getValueAt(i, 2);
                    data[i][3] = jtTablePolotno.getValueAt(i, 3);
                    data[i][4] = jtTablePolotno.getValueAt(i, 4);

                    if (jtTablePolotno.getValueAt(i, 5) != null) {
                        data[i][5] = (jtTablePolotno.getValueAt(i, 5));
                    } else {
                        data[i][5] = ("0");
                    }

                    if (jtTablePolotno.getValueAt(i, 6) != null) {
                        data[i][6] = (jtTablePolotno.getValueAt(i, 6));
                    } else {
                        data[i][6] = ("0");
                    }

                    if (jtTablePolotno.getValueAt(i, 7) != null) {
                        data[i][7] = (jtTablePolotno.getValueAt(i, 7));
                    } else {
                        data[i][7] = ("0");
                    }

//                if (cbUnits.isSelected())
//                    data[i][8]=("м.");
//                else
//                    data[i][8]=("кг.");       
                }
                dat = data;
            }
        } catch (Exception e) {
            System.err.println("Error - - " + e);
        }
        return dat;
    }
}
