package dept.marketing.cena;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.LogCrutch;
import common.CheckBoxHeader;
import common.FormMenu;
import common.ProgressBar;
import common.UtilFunctions;
import dept.marketing.DynamicTree;
import dept.sprav.valuta.ValutaPDB;
import workDB.DB;
import workDB.PDB;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

@SuppressWarnings("all")
public class CenaForm extends javax.swing.JDialog implements FormMenu {

    //private static final Logger log = new Log().getLoger(CenaForm.class);
    private static final LogCrutch log = new LogCrutch();
    private static TreeMap menu = new TreeMap();
    public DB mydb = new DB();
    JButton buttonShow;
    JButton buttonPrint;
    JCheckBox modelCheckBox;
    JCheckBox cenaPlCheckBox;
    JCheckBox sarCheckBox;
    JCheckBox ndsCheckBox;
    JCheckBox artikulCheckBox;
    JCheckBox cenaRTCheckBox;
    JCheckBox izdCheckBox;
    JCheckBox tnCheckBox;
    JCheckBox sostavCheckBox;
    JCheckBox sortCheckBox;
    JCheckBox cenaRozCheckBox;
    JCheckBox rstCheckBox;
    JCheckBox sebestCheckBox;
    JCheckBox rzmCheckBox;
    JCheckBox rentCheckBox;
    JCheckBox cenaVRUBCheckBox;
    JCheckBox sebVCheckBox;
    JCheckBox cenaVBCheckBox;
    JCheckBox rCenaVsebVCheckBox;
    JCheckBox rCenaVBsebCheckBox;
    JCheckBox sebest_CheckBox;
    JCheckBox rent_CheckBox;
    JCheckBox cenaVUSDCheckBox;
    JCheckBox cenaVEURCheckBox;
    JLabel imgPoiskLabel;
    JLabel modelLabel;
    JLabel cenaPlLabel;
    JLabel ndsLabel;
    JLabel izdLabel;
    JLabel tnLabel;
    JLabel cenaRozLabel;
    JLabel sarLabel;
    JLabel rstLabel;
    JLabel rzmLabel;
    JLabel narLabel;
    JLabel cenaRLabel;
    JLabel sortLabel;
    JLabel sebestLabel;
    JLabel sostavLabel;
    JLabel rentLabel;
    JLabel cenaVRUBLabel;
    JLabel cenaVUSDLabel;
    JLabel cenaVEURLabel;
    JLabel modelText;
    JLabel sarText;
    JLabel ndsText;
    JLabel sortText;
    JLabel izdText;
    JLabel cenaPlText;
    JLabel artikulText;
    JLabel cenaRText;
    JLabel tnText;
    JLabel cenaRozText;
    JLabel rstText;
    JLabel sebestText;
    JLabel sostavText;
    JLabel rentText;
    JLabel rzmText;
    JLabel cenaVRUBText;
    JLabel sebVText;
    JLabel cenaVBText;
    JLabel rCenaVsebVText;
    JLabel rCenaVBsebText;
    JLabel sebest_Text;
    JLabel rent_Text;
    JLabel cenaVUSDText;
    JLabel cenaVEURText;
    JRadioButton r1;
    JRadioButton r2;
    JRadioButton r3;
    JRadioButton r4;
    JPanel artPanel;
    JPanel analysisPanel;
    JPanel detalPanel;
    JPanel buttPanel;
    JPanel osnovaPanelAnalysis;
    DefaultTableCellRenderer rendererFormat;
    JTable tableArt;
    JTable tableAnalysis;
    JTextField textPoisk;
    JTextField textKursRUB;
    JTextField textKursUSD;
    JTextField textKursEUR;
    DefaultTableModel tm;
    DefaultTableModel tmAnalysis;
    Vector dataTableArt;
    Vector dataTableAnalysis;
    String sDate;
    String eDate;
    JComboBox namePoisk;
    DynamicTree treeFasonyPanel;
    ProgressBar pb;
    ValutaPDB npdb;
    CenaDB db;
    CenaPDB cpdb;
    PDB pdb;
    HashMap<String, String> mapSetting = new HashMap<String, String>();
    HashMap<String, String> mapSettingAnalysis = new HashMap<String, String>();
    HashMap<String, String> mapSettingChange = new HashMap<String, String>();
    Boolean advanSearch = false;
    CenaForm thisCenaDetalForm = this;
    int minSelectedRow = -1;
    int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;

    int minSelectedRowAnalis = -1;
    int maxSelectedRowAnalis = -1;
    boolean tableModelListenerIsChangingAnalis = false;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem miListBaseCost;

    public CenaForm() {
        initMenu();
    }

    public CenaForm(MainController mainController, boolean modal) {
        //super(parent, modal);
        super(mainController.getMainForm(), false);//dplu
        controller = mainController;
        initMenu();
        controller.menuFormInitialisation(getClass().getName(), menu);
        initComponent();

        UtilCena.TEK_DATE = new String(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()));

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        sDate = new String(new SimpleDateFormat("dd.MM.yyyy").format(c.getTime()));
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        eDate = new String(new SimpleDateFormat("dd.MM.yyyy").format(c.getTime()));

        npdb = new ValutaPDB();
        Vector kurs = npdb.getKursList(0);
        npdb.disConn();

        for (Iterator it = kurs.iterator(); it.hasNext(); ) {
            Vector object = (Vector) it.next();
            if (object.get(0).toString().equals("RUB") && object.get(1).toString().equals("BYR"))
                UtilCena.setKURS_RUB(object.get(2).toString());
            if (object.get(0).toString().equals("USD") && object.get(1).toString().equals("RUB"))
                UtilCena.setKURS_USD(object.get(2).toString());
            if (object.get(0).toString().equals("EUR") && object.get(1).toString().equals("RUB"))
                UtilCena.setKURS_EUR(object.get(2).toString());
        }

        mapSetting.put("", "true");
        mapSetting.put("Артикул", "true");
        mapSetting.put("Рост", "true");
        mapSetting.put("Размер", "true");
        mapSetting.put("Цена плановая", "true");

        mapSettingAnalysis.put("", "true");
        mapSettingAnalysis.put("Артикул", "true");
        mapSettingAnalysis.put("Себ-сть", "true");
        mapSettingAnalysis.put("Цена в RUB", "true");
        mapSettingAnalysis.put("Себ. в RUB", "true");
        mapSettingAnalysis.put("ВЦ в бел.руб", "true");
        mapSettingAnalysis.put("ВЦ - ВС", "true");
        mapSettingAnalysis.put("ВЦБ - С", "true");

        mapSettingChange.put("", "true");
        mapSettingChange.put("Артикул", "true");
        mapSettingChange.put("Цена в RUB", "true");
        mapSettingChange.put("Новая цена в RUB", "true");

        try {
            UtilCena.setLoadDataPropFile(mapSetting, UtilFunctions.readPropFileStringsArray(UtilCena.SETTING_ART));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilCena.setLoadDataPropFile(mapSettingAnalysis, UtilFunctions.readPropFileStringsArray(UtilCena.SETTING_ANALYSIS));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilCena.setLoadDataPropFile(mapSettingChange, UtilFunctions.readPropFileStringsArray(UtilCena.SETTING_CHANGE));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        createTableArt(dataTableArt);

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        miListBaseCost = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        jMenu1.setText("Просмотр");

        jMenuItem1.setText("Расширенный поиск");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Сервис");

        jMenuItem2.setText("Курсы валют");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Параметры");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Документы");

        jMenuItem4.setText("Перечень базовых цен для РФ");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });

        miListBaseCost.setText("Перечень базовых цен для РФ (без коэффициента)");
        miListBaseCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new DocumentPBCRFSimple(thisCenaDetalForm, false, dataTableArt);
            }
        });

        jMenu3.add(miListBaseCost);
        jMenu3.add(jMenuItem4);


        jMenuBar1.add(jMenu3);

        jMenu4.setText("Дополнительно");

        jMenuItem5.setText("Анализ цен");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem6.setText("Корректировка цен");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        //  jMenu1.setVisible(false);
        //  jMenu2.setVisible(false);
        //  jMenu3.setVisible(false);
        jMenu4.setVisible(false);

        //  jMenuItem1.setVisible(false);
        //  jMenuItem2.setVisible(false);
        //  jMenuItem3.setVisible(false);
        //  jMenuItem4.setVisible(false);
        jMenuItem5.setVisible(false);
        jMenuItem6.setVisible(false);

        setJMenuBar(jMenuBar1);

        menu.put("1", jMenu1);              //Просмотр
        menu.put("1-1", jMenuItem1);            //Расширенный поиск
        menu.put("2", jMenu2);              //Сервис
        menu.put("2-1", jMenuItem2);            //Курсы валют
        menu.put("2-2", jMenuItem3);            //Параметры
        menu.put("3", jMenu3);              //Документы
        menu.put("3-1", jMenuItem4);            //Перечень базовых цен для РФ
        menu.put("4", jMenu4);              //Дополнительно
        menu.put("4-1", jMenuItem5);          //Анализ цен
        menu.put("4-2", jMenuItem6);          //Корректировка цен
    }

    private void initComponent() {
        setTitle("Цены");
        setMinimumSize(new Dimension(650, 350));
        setPreferredSize(new Dimension(950, 680));

        addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });

        JPanel osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel onePanel = new JPanel();
        onePanel.setLayout(new BorderLayout(1, 1));

        JPanel twoPanel = new JPanel();
        twoPanel.setLayout(new BorderLayout(1, 1));

        detalPanel = new JPanel();
        detalPanel.setLayout(new GridLayout(9, 2, 1, 1));
        detalPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        detalPanel.setBorder(BorderFactory.createTitledBorder(null, "Детали", TitledBorder.RIGHT, TitledBorder.DEFAULT_POSITION, new Font("Dialog", 2, 12)));

        JSplitPane rHSp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, twoPanel, detalPanel);
        rHSp.setDividerLocation(450);
        JPanel rigthPanel = new JPanel();
        rigthPanel.setLayout(new BorderLayout());
        rigthPanel.add(rHSp);

        JSplitPane mainSp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, onePanel, rigthPanel);
        osnovaPanel.add(mainSp, BoxLayout.X_AXIS);

        JPanel poiskPanel = new JPanel();
        poiskPanel.setLayout(new BorderLayout(1, 1));
        poiskPanel.setBorder(BorderFactory.createTitledBorder(null, "Поиск по актуальным моделям", TitledBorder.RIGHT, TitledBorder.DEFAULT_POSITION, new Font("Dialog", 2, 12)));
        onePanel.add(poiskPanel, BorderLayout.NORTH);

        JPanel selectPoiskPanel = new JPanel();
        selectPoiskPanel.setLayout(new GridLayout(2, 0, 0, 0));
        poiskPanel.add(selectPoiskPanel, BorderLayout.NORTH);

        namePoisk = new JComboBox();
        selectPoiskPanel.add(namePoisk);

        namePoisk.addItem("Поиск по модели...");
        namePoisk.addItem("Поиск по артикулу изделия...");

        namePoisk.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (namePoisk.getSelectedItem().equals("Поиск по артикулу изделия..."))
                    textPoisk.setToolTipText("Введите артикул...");
                else if (namePoisk.getSelectedItem().equals("Поиск по модели..."))
                    textPoisk.setToolTipText("Введите модель...");
            }
        });

        textPoisk = new JTextField();
        selectPoiskPanel.add(textPoisk);

        textPoisk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (textPoisk.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Поле для поиска не должно быть пустым!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                    } else {
                        DynamicTree.flagSearch = true;
                        String criteria = "";
                        String text = "";
                        String constisselected = "";

                        treeFasonyPanel.cleanSelectedPath();

                        if (r3.isSelected()) constisselected = "%";

                        if (r4.isSelected()) DynamicTree.flagSearch = false;

                        if (namePoisk.getSelectedItem().equals("Поиск по артикулу изделия...")) {
                            if (textPoisk.getText().trim().toUpperCase().equals("0"))
                                text = "like '" + constisselected + textPoisk.getText().trim().toUpperCase() + "С%'";
                            else
                                text = "like '" + constisselected + textPoisk.getText().trim().toUpperCase() + "%'";
                            criteria = "nar";
                        } else if (namePoisk.getSelectedItem().equals("Поиск по модели...")) {

                            if (r1.isSelected()) {
                                text = "='" + (Integer.parseInt(textPoisk.getText().trim().toUpperCase())) + "'";
                                System.out.println("Явный поиск");
                            } else {
                                text = "like '" + constisselected + (Integer.parseInt(textPoisk.getText().trim().toUpperCase())) + "%'";
                            }

                            criteria = "fas";
                        }

                        //System.out.println("Ищем "+criteria + " для ");

                        treeFasonyPanel.search(criteria, text);
                        if (DynamicTree.flagSearch) DynamicTree.flagSearch = false;
                    }
                } catch (Exception ex) {
                    if (DynamicTree.flagSearch) DynamicTree.flagSearch = false;
                    textPoisk.setText("");
                    treeFasonyPanel.updateTree();
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение!\n " + ex.getMessage() + "", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        imgPoiskLabel = new JLabel(UtilFunctions.createIcon("/Img/xmag.png"));
        imgPoiskLabel.setHorizontalAlignment(JLabel.CENTER);
        imgPoiskLabel.setVerticalAlignment(JLabel.CENTER);
        poiskPanel.add(imgPoiskLabel, BorderLayout.EAST);

        imgPoiskLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textPoisk.postActionEvent();
            }
        });

        JPanel vidPoiskPanel = new JPanel();
        vidPoiskPanel.setLayout(new GridLayout(4, 0, 0, 0));
        poiskPanel.add(vidPoiskPanel);

        JLabel v1 = new JLabel("Поиск:");
        vidPoiskPanel.add(v1);
        JLabel v2 = new JLabel("Вид каталога:");
        vidPoiskPanel.add(v2);

        r1 = new JRadioButton("начало строки;");
        vidPoiskPanel.add(r1);
        r2 = new JRadioButton("краткий;");
        vidPoiskPanel.add(r2);
        r3 = new JRadioButton("любая позиция;");
        vidPoiskPanel.add(r3);
        r4 = new JRadioButton("полный;");
        vidPoiskPanel.add(r4);

        ButtonGroup group1 = new ButtonGroup();
        group1.add(r1);
        group1.add(r3);
        r1.setSelected(true);

        ButtonGroup group2 = new ButtonGroup();
        group2.add(r2);
        group2.add(r4);
        r2.setSelected(true);

        JPanel treePanel = new JPanel();
        treePanel.setLayout(new BorderLayout(1, 1));
        treePanel.setBorder(BorderFactory.createTitledBorder(null, "Каталог", TitledBorder.RIGHT, TitledBorder.DEFAULT_POSITION, new Font("Dialog", 2, 12)));
        onePanel.add(treePanel, BorderLayout.CENTER);

        treeFasonyPanel = new DynamicTree(this, true, true);
        treeFasonyPanel.madeTree();
        treePanel.removeAll();
        treePanel.add(treeFasonyPanel);
        treePanel.repaint();

        artPanel = new JPanel();
        artPanel.setLayout(new BorderLayout(1, 1));
        artPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        artPanel.setBorder(BorderFactory.createTitledBorder(null, null, TitledBorder.RIGHT, TitledBorder.DEFAULT_POSITION, new Font("Dialog", 2, 12)));
        twoPanel.add(artPanel, BorderLayout.CENTER);

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 2, 5, 5));
        buttonShow = new JButton("Показать историю");
        buttPanel.add(buttonShow);
        buttonPrint = new JButton("Печать");
        buttPanel.add(buttonPrint);

        buttonShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                new PriceHistoryDialog(thisCenaDetalForm, true);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        buttonPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new PrintForm(thisCenaDetalForm, null, true, tm, mapSetting, UtilCena.TABLE_ART);
            }
        });

        tableArt = new JTable();
        artPanel.add(new JScrollPane(tableArt));

        dataTableArt = new Vector();

        rendererFormat = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                cell = super.getTableCellRendererComponent(table, new DecimalFormat("##0.0000").format(value), isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT);

                return cell;
            }

            ;
        };

        modelText = new JLabel("Модель:");
        detalPanel.add(modelText);

        modelLabel = new JLabel("");
        detalPanel.add(modelLabel);

        ndsText = new JLabel("НДС(%):");
        detalPanel.add(ndsText);

        ndsLabel = new JLabel();
        detalPanel.add(ndsLabel);

        sarText = new JLabel("Шифр артикула:");
        detalPanel.add(sarText);

        sarLabel = new JLabel();
        detalPanel.add(sarLabel);

        cenaRText = new JLabel("Цена реализ.:");
        detalPanel.add(cenaRText);

        cenaRLabel = new JLabel();
        detalPanel.add(cenaRLabel);

        artikulText = new JLabel("Артикул:");
        detalPanel.add(artikulText);

        narLabel = new JLabel();
        detalPanel.add(narLabel);

        tnText = new JLabel("ТН(%):");
        detalPanel.add(tnText);

        tnLabel = new JLabel();
        detalPanel.add(tnLabel);

        izdText = new JLabel("Наименование:");
        detalPanel.add(izdText);

        izdLabel = new JLabel();
        detalPanel.add(izdLabel);

        cenaRozText = new JLabel("Цена роз.:");
        detalPanel.add(cenaRozText);

        cenaRozLabel = new JLabel();
        detalPanel.add(cenaRozLabel);

        sortText = new JLabel("Сорт:");
        detalPanel.add(sortText);

        sortLabel = new JLabel();
        detalPanel.add(sortLabel);

        sebestText = new JLabel("Себестоимость:");
        detalPanel.add(sebestText);

        sebestLabel = new JLabel();
        detalPanel.add(sebestLabel);

        rstText = new JLabel("Рост:");
        detalPanel.add(rstText);

        rstLabel = new JLabel();
        detalPanel.add(rstLabel);

        rentText = new JLabel("Рентабельность:");
        detalPanel.add(rentText);

        rentLabel = new JLabel();
        detalPanel.add(rentLabel);

        rzmText = new JLabel("Размер:");
        detalPanel.add(rzmText);

        rzmLabel = new JLabel();
        detalPanel.add(rzmLabel);

        cenaVRUBText = new JLabel("Цена в RUB:");
        detalPanel.add(cenaVRUBText);

        cenaVRUBLabel = new JLabel();
        detalPanel.add(cenaVRUBLabel);

        sostavText = new JLabel("Состав сырья:");
        detalPanel.add(sostavText);

        sostavLabel = new JLabel();
        detalPanel.add(sostavLabel);

        cenaVUSDText = new JLabel("ВЦ в USD:");
        detalPanel.add(cenaVUSDText);

        cenaVUSDLabel = new JLabel();
        detalPanel.add(cenaVUSDLabel);

        cenaPlText = new JLabel("Цена пл.:");
        detalPanel.add(cenaPlText);

        cenaPlLabel = new JLabel();
        detalPanel.add(cenaPlLabel);

        cenaVEURText = new JLabel("ВЦ в EUR:");
        detalPanel.add(cenaVEURText);

        cenaVEURLabel = new JLabel();
        detalPanel.add(cenaVEURLabel);

        sebest_Text = new JLabel("Себ-сть:");
        rent_Text = new JLabel("Рен-сть:");
        sebVText = new JLabel("Себ. в RUB:");
        cenaVBText = new JLabel("ВЦ в бел.руб:");
        rCenaVsebVText = new JLabel("ВЦ - ВС:");
        rCenaVBsebText = new JLabel("ВЦБ - С:");

        getContentPane().add(osnovaPanel);
        pack();

        //-------------------------TableAnalysis---------------------------------

        dataTableAnalysis = new Vector();

        osnovaPanelAnalysis = new JPanel();
        osnovaPanelAnalysis.setLayout(new BorderLayout(1, 1));
        osnovaPanelAnalysis.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        analysisPanel = new JPanel();
        analysisPanel.setLayout(new BorderLayout(1, 1));
        analysisPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        analysisPanel.setBorder(BorderFactory.createTitledBorder(null, null, TitledBorder.RIGHT, TitledBorder.DEFAULT_POSITION, new Font("Dialog", 2, 12)));
        osnovaPanelAnalysis.add(analysisPanel, BorderLayout.CENTER);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Просмотр");

        jMenuItem1.setText("Расширенный поиск");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Сервис");

        jMenuItem2.setText("Курсы валют");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Параметры");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Документы");

        jMenuItem4.setText("Перечень базовых цен для РФ");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Дополнительно");

        jMenuItem5.setText("Анализ цен");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem6.setText("Корректировка цен");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 412, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 427, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new AnalysisDialog(thisCenaDetalForm, true, dataTableArt);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        new ChangeForm(thisCenaDetalForm, true, dataTableArt, mapSettingChange);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        new DocumentPBCRF(thisCenaDetalForm, false, dataTableArt);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new SettingForm(thisCenaDetalForm, null, true, mapSetting, UtilCena.TABLE_ART);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new ValutaKursDialog(thisCenaDetalForm, true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        AdvanSearchForm.getInstance(thisCenaDetalForm, false);
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void updateDetalPanel() {
        modelLabel.setText("");
        cenaPlLabel.setText("");
        ndsLabel.setText("");
        izdLabel.setText("");
        tnLabel.setText("");
        cenaRozLabel.setText("");
        sarLabel.setText("");
        rstLabel.setText("");
        rzmLabel.setText("");
        narLabel.setText("");
        cenaRLabel.setText("");
        sortLabel.setText("");
        sebestLabel.setText("");
        sostavLabel.setText("");
        rentLabel.setText("");
        cenaVRUBLabel.setText("");
        cenaVUSDLabel.setText("");
        cenaVEURLabel.setText("");
    }

    public void clearDataTable() {
        dataTableArt = new Vector();
        createTableArt(dataTableArt);
    }

    public void createDataTable(final Object fas, final Object sar, final Object nar, final String nptk,
                                final Object preiscurs, final boolean flagCenaV, final boolean flagХО, final boolean flagCenaVP, final String sDate, final String eDate) {
        try {
            pb = new ProgressBar(this, false, "Построение таблицы...");
            db = new CenaDB();
            cpdb = new CenaPDB();

            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    if (advanSearch) {
                        pb.setMessage("Поиск продукции...");
                        Vector temp = db.getFullTableIzd(sar, nar, fas, preiscurs, flagCenaV, flagХО, flagCenaVP, sDate, eDate);
                        //System.out.println("Ищем тут");
                        pb.setMessage("Поиск себестоимости...");
                        dataTableArt = cpdb.getFullTableAdvanSearch(temp);
                    } else {
                        if (!fas.toString().equals("") || !sar.toString().equals("") || !nar.toString().equals("") || !nptk.equals("")) {
                            //System.err.println("Тупой поиск");
                            dataTableArt = db.getFullTableIzd(Integer.parseInt(fas.toString()), sar, nar, nptk);
                        } else {
                            dataTableArt = new Vector();
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
            createTableArt(dataTableArt);
        } catch (Exception e) {
            log.error("Ошибка createDataTable(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка createDataTable(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        cpdb.disConn();
        advanSearch = false;
    }

    void createTableProgressBar(final int type) {
        try {
            pb = new ProgressBar(this, false, "Построение таблицы...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    boolean flag = (type == 0) ? createTableArt(dataTableArt) : createTableAnalysis(dataTableArt);
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
        } catch (Exception e) {
            log.error("Ошибка createTableProgressBar(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка createTableProgressBar(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean createTableArt(Vector rowD) {
        try {
            tableArt = new JTable();
            dataTableArt = new Vector();

            Vector col = new Vector();
            col.add("");
            col.add("Модель");
            col.add("Шифр. артикул");
            col.add("Артикул");
            col.add("Изделие");
            col.add("Сорт");
            col.add("min рост");
            col.add("max рост");
            col.add("Рост");
            col.add("min размер");
            col.add("max размер");
            col.add("Размер");
            col.add("Состав сырья");
            col.add("Цена плановая");
            col.add("НДС(%)");
            col.add("Цена реализации");
            col.add("ТН(%)");
            col.add("Цена розничная");
            col.add("Себестоимость");
            col.add("Рентабельность");
            col.add("Цена в RUB");
            col.add("ВЦ в USD");
            col.add("ВЦ в EUR");
            col.add("Себ-сть");
            col.add("Рен-сть");

            for (Iterator it = rowD.iterator(); it.hasNext(); ) {
                Vector vrow = (Vector) it.next();
                Vector tmp = new Vector();
                int i = 0;
                for (Iterator it1 = vrow.iterator(); it1.hasNext(); i++) {
                    Object velement = it1.next();
                    switch (i) {
                        case 11:
                            tmp.add(Integer.valueOf(vrow.get(10).toString()) % 2 == 1 &&
                                    Integer.valueOf(vrow.get(10).toString()) <= 33 &&
                                    vrow.get(2).toString().substring(0, 4).equals("4334")
                                    ? Integer.valueOf(vrow.get(9).toString()) + "--" + new Integer(Integer.valueOf(vrow.get(10).toString()) + 1)
                                    : velement);
                            break;
                        case 21:
                            tmp.add(new BigDecimal(Double.valueOf(vrow.get(20).toString()) / Double.parseDouble(UtilCena.KURS_USD)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                            break;
                        case 22:
                            tmp.add(new BigDecimal(Double.valueOf(vrow.get(20).toString()) / Double.parseDouble(UtilCena.KURS_EUR)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                            break;
                        default:
                            tmp.add(velement);
                            break;
                    }
                }
                dataTableArt.add(tmp);
            }

            tm = new DefaultTableModel(dataTableArt, col) {
                @Override
                public Class<?> getColumnClass(int col) {
                    return getValueAt(0, col).getClass();
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    if (col == 0) return true;
                    else return false;
                }
            };

            tm.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (tableModelListenerIsChanging) {
                        return;
                    }
                    int firstRow = e.getFirstRow();
                    int column = e.getColumn();

                    if (column != 0 || maxSelectedRow == -1 || minSelectedRow == -1) {
                        return;
                    }
                    tableModelListenerIsChanging = true;
                    boolean value = ((Boolean) tm.getValueAt(firstRow, column)).booleanValue();
                    for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                        tm.setValueAt(Boolean.valueOf(value), tableArt.convertRowIndexToModel(i), column);
                    }

                    minSelectedRow = -1;
                    maxSelectedRow = -1;

                    tableModelListenerIsChanging = false;
                }
            });

            tableArt.setModel(tm);
            tableArt.setAutoCreateColumnsFromModel(true);

            UtilCena.initColumTableMap(tableArt, mapSetting);

            tableArt.getColumnModel().getColumn(18).setCellRenderer(rendererFormat);

            tableArt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                            -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                    maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                            -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();

                    if (tableArt.getSelectedRow() != -1 && tableArt.getSelectedColumn() != -1) {
                        modelLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 1).toString());
                        sarLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 2).toString());
                        narLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 3).toString());
                        izdLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 4).toString());
                        sortLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 5).toString());
                        rstLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 8).toString());
                        rzmLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 11).toString());
                        sostavLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 12).toString());
                        cenaPlLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 13).toString());
                        ndsLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 14).toString());
                        cenaRLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 15).toString());
                        tnLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 16).toString());
                        cenaRozLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 17).toString());
                        sebestLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 18).toString());
                        rentLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 19).toString());
                        cenaVRUBLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 20).toString());
                        cenaVUSDLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 21).toString());
                        cenaVEURLabel.setText(tableArt.getValueAt(tableArt.getSelectedRow(), 22).toString());
                    }
                }
            });

            if (!dataTableArt.isEmpty()) {
                TableRowSorter sorter = new TableRowSorter<TableModel>(tm);
                tableArt.setRowSorter(sorter);

                tableArt.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableArt.getTableHeader(), 0, "Выбрать"));

                buttonShow.setEnabled(true);
                buttonPrint.setEnabled(true);
            } else {
                buttonShow.setEnabled(false);
                buttonPrint.setEnabled(false);
            }

            artPanel.removeAll();
            artPanel.add(new JScrollPane(tableArt), BorderLayout.CENTER);
            artPanel.add(buttPanel, BorderLayout.SOUTH);
            artPanel.revalidate();
            artPanel.repaint();

            updateDetalPanel();
        } catch (Exception e) {
            log.error("Ошибка createTableArt(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка createTableArt(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return true;
        }
    }

    private String testKurs(JTextField textKurs) {
        String kurs = String.valueOf(new Double(1));
        try {
            if (Double.parseDouble(textKurs.getText().trim().replace(",", ".")) > 0)
                kurs = String.valueOf(Double.parseDouble(textKurs.getText().trim().replace(",", ".")));
            else
                throw new Exception();
        } catch (Exception e) {
            kurs = String.valueOf(new Double(1));
            JOptionPane.showMessageDialog(null, "Некорректное значение! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            textKurs.setText(kurs);
            return kurs;
        }
    }

    public boolean createTableAnalysis(Vector row) {
        try {
            tableAnalysis = new JTable();
            dataTableAnalysis = new Vector();

            Vector col = new Vector();
            col.add("");
            col.add("Модель");
            col.add("Шифр. артикул");
            col.add("Артикул");
            col.add("Изделие");
            col.add("Сорт");
            col.add("min рост");
            col.add("max рост");
            col.add("Рост");
            col.add("min размер");
            col.add("max размер");
            col.add("Размер");
            col.add("Состав сырья");
            col.add("Цена плановая");
            col.add("НДС(%)");
            col.add("Цена реализации");
            col.add("ТН(%)");
            col.add("Цена розничная");
            col.add("Себестоимость");
            col.add("Рентабельность");
            col.add("Цена в RUB");
            col.add("ВЦ в USD");
            col.add("ВЦ в EUR");
            col.add("Себ-сть");
            col.add("Рен-сть");
            col.add("Себ. в RUB");
            col.add("ВЦ в бел.руб");
            col.add("ВЦ - ВС");
            col.add("ВЦБ - С");

            for (Iterator it = row.iterator(); it.hasNext(); ) {
                Vector vrow = (Vector) it.next();
                Vector tmp = new Vector();
                int i = 0;
                for (Iterator it1 = vrow.iterator(); it1.hasNext(); i++) {
                    Object velement = it1.next();
                    switch (i) {
                        case 11:
                            tmp.add(Integer.valueOf(vrow.get(10).toString()) % 2 == 1 &&
                                    Integer.valueOf(vrow.get(10).toString()) <= 33 &&
                                    vrow.get(2).toString().substring(0, 4).equals("4334")
                                    ? Integer.valueOf(vrow.get(9).toString()) + "--" + new Integer(Integer.valueOf(vrow.get(10).toString()) + 1)
                                    : velement);
                            break;
                        case 21:
                            tmp.add(new BigDecimal(Double.valueOf(vrow.get(20).toString()) / Double.parseDouble(UtilCena.KURS_USD)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                            break;
                        case 22:
                            tmp.add(new BigDecimal(Double.valueOf(vrow.get(20).toString()) / Double.parseDouble(UtilCena.KURS_EUR)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                            break;
                        default:
                            tmp.add(velement);
                            break;
                    }
                }
                tmp.add(new BigDecimal(Double.valueOf(vrow.get(23).toString()) / Double.parseDouble(UtilCena.KURS_RUB)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                tmp.add(new BigDecimal(Double.valueOf(vrow.get(20).toString()) * Double.parseDouble(UtilCena.KURS_RUB)).setScale(-1, RoundingMode.HALF_UP).doubleValue());
                if (Double.parseDouble(tmp.get(20).toString()) > 0) {
                    tmp.add(new BigDecimal(Double.valueOf(tmp.get(20).toString()) - Double.valueOf(tmp.get(25).toString())).doubleValue());
                    tmp.add(new BigDecimal(Double.valueOf(tmp.get(26).toString()) - Double.valueOf(tmp.get(23).toString())).doubleValue());
                } else {
                    tmp.add(new Double(0));
                    tmp.add(new Double(0));
                }

                if (Boolean.valueOf(tmp.get(0).toString()))
                    dataTableAnalysis.add(tmp);
            }

            tmAnalysis = new DefaultTableModel(dataTableAnalysis, col) {
                @Override
                public Class<?> getColumnClass(int col) {
                    return getValueAt(0, col).getClass();
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    if (col == 0) return true;
                    else return false;
                }
            };

            tmAnalysis.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (tableModelListenerIsChangingAnalis) {
                        return;
                    }
                    int firstRow = e.getFirstRow();
                    int column = e.getColumn();

                    if (column != 0 || maxSelectedRowAnalis == -1 || minSelectedRowAnalis == -1) {
                        return;
                    }
                    tableModelListenerIsChangingAnalis = true;
                    boolean value = ((Boolean) tmAnalysis.getValueAt(firstRow, column)).booleanValue();
                    for (int i = minSelectedRowAnalis; i <= maxSelectedRowAnalis; i++) {
                        tmAnalysis.setValueAt(Boolean.valueOf(value), tableAnalysis.convertRowIndexToModel(i), column);
                    }

                    minSelectedRowAnalis = -1;
                    maxSelectedRowAnalis = -1;

                    tableModelListenerIsChangingAnalis = false;
                }
            });

            tableAnalysis.setModel(tmAnalysis);
            tableAnalysis.setAutoCreateColumnsFromModel(true);

            UtilCena.initColumTableMap(tableAnalysis, mapSettingAnalysis);

            tableAnalysis.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    minSelectedRowAnalis = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                            -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                    maxSelectedRowAnalis = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                            -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
                }
            });

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (table.getColumnCount() > 23) {
                        if (Double.valueOf(table.getValueAt(row, 27).toString()) < 0 || Double.valueOf(table.getValueAt(row, 28).toString()) < 0)
                            cell.setBackground(Color.PINK);
                        else
                            cell.setBackground(table.getBackground());
                    }
                    JLabel cell_ = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    cell_.setHorizontalAlignment(SwingConstants.RIGHT);

                    cell = super.getTableCellRendererComponent(table, new BigDecimal(Double.valueOf(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue(), isSelected, hasFocus, row, column);
                    return cell;
                }

                ;
            };
            tableAnalysis.getColumnModel().getColumn(27).setCellRenderer(renderer);
            tableAnalysis.getColumnModel().getColumn(28).setCellRenderer(renderer);

            tableAnalysis.getColumnModel().getColumn(18).setCellRenderer(rendererFormat);
            tableAnalysis.getColumnModel().getColumn(23).setCellRenderer(rendererFormat);

            if (!dataTableAnalysis.isEmpty()) {
                TableRowSorter sorter = new TableRowSorter<TableModel>(tmAnalysis);
                tableAnalysis.setRowSorter(sorter);
                tableAnalysis.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableAnalysis.getTableHeader(), 0, "Выбрать"));
            }

            analysisPanel.removeAll();
            analysisPanel.add(new JScrollPane(tableAnalysis), BorderLayout.CENTER);
            analysisPanel.revalidate();
            analysisPanel.repaint();
        } catch (Exception e) {
            log.error("Ошибка createTableArt(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка createTableArt(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return true;
        }
    }

    public TreeMap getMenuMap() {
        return menu;
    }

    public void setFormVisible() {
        this.setVisible(false);
    }

    public void disposeForm() {
        dispose();
    }

    private class PriceHistoryDialog extends JDialog {
        public PriceHistoryDialog(CenaForm parent, boolean modal) {
            super(parent, modal);
            this.setTitle("История изменения цены");

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(1, 1));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            Vector temp_db = new Vector();
            Vector row = new Vector();
            Vector temp_row;
            Vector temp;

            String date = "";
            String startDate = "";
            boolean flag;

            String artHistory = "";
            String metka = "1";

            final JTable tableDetal = new JTable();
            Vector col = new Vector();
            col.add("Шифр. артикул");
            col.add("Модель");
            col.add("Артикул");
            col.add("Изделие");
            col.add("Сорт");
            col.add("min рост");
            col.add("max рост");
            col.add("Рост");
            col.add("min размер");
            col.add("max размер");
            col.add("Размер");
            col.add("Дата");
            col.add("Себестоимость");
            col.add("Рентабельность");
            col.add("Цена плановая");
            col.add("НДС(%)");
            col.add("Цена реализации");
            col.add("Цена розничная");
            col.add("Прейскурант");
            col.add("Цвет");

            final DefaultTableModel tmHistory = new DefaultTableModel(row, col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            tableDetal.setModel(tmHistory);
            tableDetal.setAutoCreateColumnsFromModel(true);

            TableColumn column = null;
            for (int i = 0; i < tableDetal.getColumnCount(); i++) {
                column = tableDetal.getColumnModel().getColumn(i);
                switch (i) {
                    case 0:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 19:
                        column.setMinWidth(0);
                        column.setMaxWidth(0);
                        break;
                    default:
                        break;
                }
            }

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    cell.setBackground(table.getValueAt(row, table.getColumnCount() - 1).toString().equals("1") ? Color.LIGHT_GRAY : table.getBackground());
                    cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    return cell;
                }

                ;
            };
            for (int i = 0; i < col.size(); i++)
                tableDetal.getColumnModel().getColumn(i).setCellRenderer(renderer);

            cpdb = new CenaPDB();
            db = new CenaDB();
            for (Object tmp : dataTableArt) {
                if (((Vector) tmp).get(0).toString().equals("true")) {
                    temp_row = new Vector();

                    try {
                        temp_db = db.getHistoryPrice(Integer.parseInt(((Vector) tmp).get(2).toString()),
                                ((Vector) tmp).get(3).toString(),
                                Integer.parseInt(((Vector) tmp).get(1).toString()),
                                Integer.parseInt(((Vector) tmp).get(5).toString()),
                                Integer.parseInt(((Vector) tmp).get(6).toString()),
                                Integer.parseInt(((Vector) tmp).get(7).toString()),
                                Integer.parseInt(((Vector) tmp).get(9).toString()),
                                Integer.parseInt(((Vector) tmp).get(10).toString()),
                                Double.parseDouble(((Vector) tmp).get(14).toString()),
                                (String.valueOf(Integer.parseInt(((Vector) tmp).get(2).toString())).substring(2, 3).equals("3") || String.valueOf(Integer.parseInt(((Vector) tmp).get(2).toString())).substring(2, 3).equals("6")) ? 20 : 30);

                    } catch (Exception e) {
                    }

                    try {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime((Date) new SimpleDateFormat("dd-MM-yyyy").parse(((Vector) (((Vector) temp_db).get(0))).elementAt(0).toString()));
                        cal.add(Calendar.MONTH, -1);
                        startDate = new String(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()));
                    } catch (Exception ee) {
                    }

                    for (Object object : temp_db) {
                        temp = new Vector();
                        temp.add(Integer.parseInt(((Vector) tmp).get(2).toString()));
                        temp.add(Integer.parseInt(((Vector) tmp).get(1).toString()));
                        temp.add(((Vector) tmp).get(3).toString());
                        temp.add(((Vector) tmp).get(4).toString());
                        temp.add(Integer.parseInt(((Vector) tmp).get(5).toString()));
                        temp.add(((Vector) tmp).get(6).toString());
                        temp.add(((Vector) tmp).get(7).toString());
                        temp.add(((Vector) tmp).get(8).toString());
                        temp.add(((Vector) tmp).get(9).toString());
                        temp.add(((Vector) tmp).get(10).toString());
                        temp.add(((Vector) tmp).get(11).toString());
                        temp.add(((Vector) object).get(0).toString());
                        temp.add("");
                        temp.add("");
                        temp.add(((Vector) object).get(1).toString());
                        temp.add(((Vector) object).get(2).toString());
                        temp.add(((Vector) object).get(3).toString());
                        temp.add(((Vector) object).get(5).toString());
                        temp.add(((Vector) object).get(6).toString());
                        temp_row.add(temp);
                    }
                    try {
                        temp_db = cpdb.getHistorySStoim(Integer.parseInt(((Vector) tmp).get(2).toString()),
                                Integer.parseInt(((Vector) tmp).get(10).toString()));
                    } catch (Exception ex) {
                        log.error("Ошибка PriceHistoryDialog(): ", ex);
                        JOptionPane.showMessageDialog(null, "Ошибка PriceHistoryDialog(): " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);

                    }

                    String historyart = "";
                    String historyrzm = "";
                    String historydate = "";
                    for (int i = 0; i < temp_db.size(); i++) {
                        if (historydate.equals(((Vector) temp_db.elementAt(i)).get(0).toString()) &&
                                historyart.equals(((Vector) temp_db.elementAt(i)).get(1).toString()) &&
                                historyrzm.equals(((Vector) temp_db.elementAt(i)).get(2).toString())) {
                            temp_db.remove(i - 1);
                            i--;
                        } else {
                            historydate = ((Vector) temp_db.elementAt(i)).get(0).toString();
                            historyart = ((Vector) temp_db.elementAt(i)).get(1).toString();
                            historyrzm = ((Vector) temp_db.elementAt(i)).get(2).toString();
                        }
                    }

                    for (Object object : temp_db) {
                        try {
                            flag = true;
                            date = ((Vector) object).elementAt(0).toString();

                            if (new SimpleDateFormat("MM-yyyy").parse(date.substring(3)).compareTo(
                                    new SimpleDateFormat("MM-yyyy").parse(startDate.substring(3))) >= 0) {
                                temp = new Vector();
                                temp.add(Integer.parseInt(((Vector) tmp).get(2).toString()));
                                temp.add(Integer.parseInt(((Vector) tmp).get(1).toString()));
                                temp.add(((Vector) tmp).get(3).toString());
                                temp.add(((Vector) tmp).get(4).toString());
                                temp.add(Integer.parseInt(((Vector) tmp).get(5).toString()));
                                temp.add(((Vector) tmp).get(6).toString());
                                temp.add(((Vector) tmp).get(7).toString());
                                temp.add(((Vector) tmp).get(8).toString());
                                temp.add(((Vector) tmp).get(9).toString());
                                temp.add(((Vector) tmp).get(10).toString());
                                temp.add(((Vector) tmp).get(11).toString());
                                temp.add(((Vector) object).get(0).toString());

                                float ss = new Float(((Vector) object).get(3).toString());
                                float rent = new Float(((Vector) object).get(4).toString());

                                if (rent > 0)
                                    ss = new BigDecimal((new Float(((Vector) tmp).get(13).toString()) * 100) / (rent + 100)).setScale(4, RoundingMode.HALF_UP).floatValue();

                                temp.add(ss);
                                temp.add(rent);
                                temp.add("");
                                temp.add("");
                                temp.add("");
                                temp.add("");
                                temp.add("");

                                for (int i = 0; i < temp_row.size(); i++) {
                                    if (new SimpleDateFormat("dd-MM-yyyy").parse(((Vector) object).get(0).toString()).compareTo(
                                            new SimpleDateFormat("dd-MM-yyyy").parse(((Vector) ((Vector) temp_row).get(i)).elementAt(11).toString())) < 0) {
                                        temp_row.insertElementAt(temp, i);
                                        flag = false;
                                        break;
                                    }
                                }

                                if (flag) temp_row.add(temp);
                            }
                        } catch (Exception e) {
                        }
                    }

                    for (Iterator it = temp_row.iterator(); it.hasNext(); ) {
                        Vector object = (Vector) it.next();
                        if (!object.elementAt(2).toString().equals(artHistory)) {
                            metka = metka.equals("1") ? "2" : "1";
                            artHistory = object.elementAt(2).toString();
                        }
                        object.add(metka);
                        tmHistory.insertRow(tmHistory.getRowCount(), object);
                    }
                }
            }
            cpdb.disConn();

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(0, 4, 5, 5));
            buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            JButton buttonDispose = new JButton("Закрыть");
            buttonPanel.add(buttonDispose);

            buttonDispose.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispose();
                }
            });

            JButton buttonPrint = new JButton("Печать");
            buttonPanel.add(buttonPrint);

            buttonPrint.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Vector columSelect = new Vector();
                    columSelect.add(10);
                    columSelect.add(11);
                    columSelect.add(12);
                    columSelect.add(13);
                    columSelect.add(14);
                    columSelect.add(15);
                    columSelect.add(16);
                    columSelect.add(17);
                    columSelect.add(18);

                    try {
                        CenaOO oo = new CenaOO("История изменения цены", tmHistory, columSelect);
                        oo.createReport("PriceHistory.ots");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton buttonSSDetal = new JButton("Себестоимость подробно");
            buttonSSDetal.setVisible(false);
            buttonPanel.add(buttonSSDetal);

            if (jMenuItem5.isVisible()) buttonSSDetal.setVisible(true);

            buttonSSDetal.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (tableDetal.getSelectedRowCount() > 0) {
                        new DetalSSDialog(thisCenaDetalForm, true,
                                Integer.parseInt(tableDetal.getValueAt(tableDetal.getSelectedRow(), 0).toString()),
                                Integer.parseInt(tableDetal.getValueAt(tableDetal.getSelectedRow(), 8).toString()),
                                Integer.parseInt(tableDetal.getValueAt(tableDetal.getSelectedRow(), 9).toString()),
                                tableDetal.getValueAt(tableDetal.getSelectedRow(), 10).toString());
                    }
                }
            });

            panel.add(new JScrollPane(tableDetal), BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            getContentPane().add(panel);

            setPreferredSize(new Dimension(900, 500));
            setMinimumSize(new Dimension(450, 150));
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }

    private class DetalSSDialog extends JDialog {
        public DetalSSDialog(CenaForm parent, boolean modal, int sar, int minrzm, int maxrzm, String rzms) {
            super(parent, modal);
            this.setTitle("Справочник затрат шифр артикула " + sar + ", размер " + rzms);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(1, 1));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            final JTable tableDetal = new JTable();

            Vector col = new Vector();
            Vector row = new Vector();
            col.add("Дата");
            col.add("Размер");
            col.add("Сырьё");
            col.add("Всп. материалы");
            col.add("Топливо");
            col.add("Зарплата вся");
            col.add("Соц. нужды");
            col.add("Произ. расходы");
            col.add("Хоз. расходы");
            col.add("Комм. расходы");
            col.add("Себестоимость");
            col.add("Рентабельность");
            col.add("Дата изменения");

            try {
                cpdb = new CenaPDB();
                row = cpdb.getHistoryZatraty(sar, maxrzm);
                cpdb.disConn();
            } catch (Exception e) {
                log.error("Ошибка DetalSSDialog(): ", e);
                JOptionPane.showMessageDialog(null, "Ошибка DetalSSDialog(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            DefaultTableModel tm = new DefaultTableModel(row, col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            tableDetal.setModel(tm);
            tableDetal.setAutoCreateColumnsFromModel(true);

            panel.add(new JScrollPane(tableDetal), BorderLayout.CENTER);
            getContentPane().add(panel);

            setPreferredSize(new Dimension(1000, 300));
            setMinimumSize(new Dimension(450, 150));
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }

    private class AnalysisDialog extends JDialog {
        public AnalysisDialog(CenaForm parent, boolean modal, Vector row) {
            super(parent, modal);
            this.setTitle("Анализ цен");

            osnovaPanelAnalysis.removeAll();
            osnovaPanelAnalysis.add(analysisPanel, BorderLayout.CENTER);

            JPanel DownPanel = new JPanel();
            DownPanel.setLayout(new GridLayout(0, 5, 5, 5));
            DownPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            osnovaPanelAnalysis.add(DownPanel, BorderLayout.SOUTH);

            JMenuBar menuBar = new JMenuBar();
            menuBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            osnovaPanelAnalysis.add(menuBar, BorderLayout.NORTH);

            JMenu menuF = new JMenu("Файл");
            menuBar.add(menuF);

            JMenuItem menuF_Item1 = new JMenuItem("Печать");
            menuF.add(menuF_Item1);

            JMenu menuFDoc = new JMenu("Печать документов");
            menuF.add(menuFDoc);

            JMenuItem menuFDoc_Item1 = new JMenuItem("Перечень продукции");
            menuFDoc.add(menuFDoc_Item1);

            JMenu menuS = new JMenu("Сервис");
            menuBar.add(menuS);

            JMenuItem menuS_Item1 = new JMenuItem("Параметры");
            menuS.add(menuS_Item1);

            menuS_Item1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    new SettingForm(thisCenaDetalForm, null, true, mapSettingAnalysis, UtilCena.TABLE_ANALYSIS);
                }
            });

            menuF_Item1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    new PrintForm(thisCenaDetalForm, null, true, tmAnalysis, mapSettingAnalysis, UtilCena.TABLE_ANALYSIS);
                }
            });

            menuFDoc_Item1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        JFormattedTextField format = new javax.swing.JFormattedTextField(UtilFunctions.maskFormatterDate());
                        format.setSelectionStart(0);
                        format.setText(UtilCena.TEK_DATE);

                        if (JOptionPane.showOptionDialog(null, format, "Документ от: ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, format) == JOptionPane.YES_OPTION) {
                            if (UtilFunctions.checkDate(format.getText().trim())) {
                                Vector columSelect = new Vector();
                                columSelect.add(1);
                                columSelect.add(3);
                                columSelect.add(4);
                                columSelect.add(8);
                                columSelect.add(11);
                                columSelect.add(12);
                                columSelect.add(20);
                                columSelect.add(22);
                                columSelect.add(21);
                                columSelect.add(23);
                                columSelect.add(25);
                                columSelect.add(26);
                                columSelect.add(27);
                                columSelect.add(28);

                                CenaOO oo = new CenaOO(new SimpleDateFormat("MM.dd.yyyy").format(new SimpleDateFormat("dd.MM.yyyy").parse(format.getText().trim())),
                                        tmAnalysis, columSelect, "Курсы: RUB/BYR -" + UtilCena.KURS_RUB + ", USD/RUB - " + UtilCena.KURS_USD + ", EUR/RUB - " + UtilCena.KURS_EUR);
                                oo.createReport("PriceTableDocument.ots");
                            }

                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton buttonCancel = new JButton("Закрыть");
            DownPanel.add(buttonCancel);

            JButton buttonKurs = new JButton("Пересчитать");
            DownPanel.add(buttonKurs);

            JPanel panelRUB = new JPanel();
            panelRUB.setLayout(new GridLayout(0, 2, 5, 5));
            DownPanel.add(panelRUB);

            JLabel labelKursRUB = new JLabel("RUB/BYR:");
            labelKursRUB.setHorizontalAlignment(JLabel.CENTER);
            labelKursRUB.setVerticalAlignment(JLabel.CENTER);
            panelRUB.add(labelKursRUB);

            textKursRUB = new JTextField(UtilCena.KURS_RUB);
            panelRUB.add(textKursRUB);

            JPanel panelEUR = new JPanel();
            panelEUR.setLayout(new GridLayout(0, 2, 5, 5));
            DownPanel.add(panelEUR);

            JLabel labelKursEUR = new JLabel("EUR/RUB:");
            labelKursEUR.setHorizontalAlignment(JLabel.CENTER);
            labelKursEUR.setVerticalAlignment(JLabel.CENTER);
            panelEUR.add(labelKursEUR);

            textKursEUR = new JTextField(UtilCena.KURS_EUR);
            panelEUR.add(textKursEUR);

            JPanel panelUSD = new JPanel();
            panelUSD.setLayout(new GridLayout(0, 2, 5, 5));
            DownPanel.add(panelUSD);

            JLabel labelKursUSD = new JLabel("USD/RUB:");
            labelKursUSD.setHorizontalAlignment(JLabel.CENTER);
            labelKursUSD.setVerticalAlignment(JLabel.CENTER);
            panelUSD.add(labelKursUSD);

            textKursUSD = new JTextField(UtilCena.KURS_USD);
            panelUSD.add(textKursUSD);

            buttonCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispose();
                }
            });

            buttonKurs.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    UtilCena.setKURS_RUB(textKursRUB.getText());
                    UtilCena.setKURS_USD(textKursUSD.getText());
                    UtilCena.setKURS_EUR(textKursEUR.getText());

                    createTableProgressBar(0);
                    createTableProgressBar(1);
                }
            });

            createTableAnalysis(dataTableArt);

            getContentPane().add(osnovaPanelAnalysis);
            osnovaPanelAnalysis.revalidate();
            osnovaPanelAnalysis.repaint();

            setMinimumSize(new Dimension(850, 500));
            setPreferredSize(new Dimension(950, 600));
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }

    private class ValutaKursDialog extends JDialog {
        private final JLabel label4;
        private final JLabel label6;
        private final JLabel label8;
        Vector model;
        JTextField text;
        DefaultTableModel tm_table;

        Vector row = new Vector();
        Vector col = new Vector();

        public ValutaKursDialog(CenaForm parent, boolean modal) {
            super(parent, modal);
            this.setTitle("Курсы валют");

            JPanel osnovaPanel = new JPanel();
            osnovaPanel.setLayout(new BorderLayout(1, 1));
            osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JPanel northPanel = new JPanel();
            northPanel.setLayout(new GridLayout(0, 3, 10, 10));
            northPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            osnovaPanel.add(northPanel, BorderLayout.NORTH);

            JPanel centrPanel = new JPanel();
            centrPanel.setLayout(new BorderLayout(1, 1));
            osnovaPanel.add(centrPanel, BorderLayout.CENTER);

            JPanel southPanel = new JPanel();
            southPanel.setLayout(new BorderLayout(1, 1));
            southPanel.setPreferredSize(new Dimension(130, 130));
            osnovaPanel.add(southPanel, BorderLayout.SOUTH);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2, 5, 5));
            southPanel.add(buttonPanel, BorderLayout.SOUTH);

            JPanel editPanel = new JPanel();
            editPanel.setLayout(new GridLayout(0, 2, 5, 5));
            editPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            centrPanel.add(editPanel, BorderLayout.SOUTH);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2, 5, 5));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            southPanel.add(panel, BorderLayout.CENTER);

            JButton sbn = new JButton("Сохранить");
            buttonPanel.add(sbn);
            sbn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    UtilCena.setKURS_RUB(label4.getText());
                    UtilCena.setKURS_USD(label6.getText());
                    UtilCena.setKURS_EUR(label8.getText());

                    dispose();

                    createTableProgressBar(0);
                }
            });

            JButton cbn = new JButton("Закрыть");
            buttonPanel.add(cbn);
            cbn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispose();
                }
            });

            JLabel label1 = new JLabel("Валюта:");
            label1.setHorizontalAlignment(JLabel.RIGHT);
            northPanel.add(label1);

            final JComboBox combo = new JComboBox();
            northPanel.add(combo);

            text = new JTextField();
            editPanel.add(text);

            JButton addButton = new JButton("Использовать");
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Item itv = (Item) model.get(combo.getSelectedIndex());
                    itv.getLabel().setText(testKurs(text));
                }
            });
            editPanel.add(addButton);

            JLabel label2 = new JLabel("Активные курсы валют:");
            label2.setHorizontalAlignment(JLabel.CENTER);
            southPanel.add(label2, BorderLayout.NORTH);

            JLabel label3 = new JLabel("RUB/BYR");
            panel.add(label3);

            label4 = new JLabel(UtilCena.KURS_RUB);
            label4.setHorizontalAlignment(JLabel.CENTER);
            panel.add(label4);

            JLabel label5 = new JLabel("USD/RUB");
            panel.add(label5);

            label6 = new JLabel(UtilCena.KURS_USD);
            label6.setHorizontalAlignment(JLabel.CENTER);
            panel.add(label6);

            JLabel label7 = new JLabel("EUR/RUB");
            panel.add(label7);

            label8 = new JLabel(UtilCena.KURS_EUR);
            label8.setHorizontalAlignment(JLabel.CENTER);
            panel.add(label8);

            model = new Vector();
            model.add(new Item(1, "RUB/BYR", "RUB", "BYR", label4));
            model.add(new Item(2, "USD/RUB", "USD", "RUB", label6));
            model.add(new Item(3, "EUR/RUB", "EUR", "RUB", label8));

            for (Iterator itr = model.iterator(); itr.hasNext(); ) {
                Item it = (Item) itr.next();
                combo.addItem(it.getDescription());
            }

            final JTable table = new JTable();
            row = new Vector();
            col = new Vector();
            col.add("Дата");
            col.add("Валюта осн.");
            col.add("Валюта доп.");
            col.add("Курс");
            col.add("Дата изм.");

            tm_table = new DefaultTableModel(row, col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            table.setModel(tm_table);
            table.setAutoCreateColumnsFromModel(true);

            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (table.getSelectedRow() != -1 && table.getSelectedColumn() != -1) {
                        text.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
                    }
                }
            });

            centrPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            getContentPane().add(osnovaPanel);

            combo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Item itv = (Item) model.get(combo.getSelectedIndex());

                    npdb = new ValutaPDB();
                    row = npdb.getKurs(itv.getValuta1(), itv.getValuta2());
                    npdb.disConn();

                    tm_table.setDataVector(row, col);
                    tm_table.fireTableDataChanged();

                    table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, table.getColumnCount() - 1, true));

                    text.setText("");
                }
            });
            combo.setSelectedIndex(0);

            setMinimumSize(new Dimension(380, 350));
            setPreferredSize(new Dimension(480, 450));
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }

        private class Item {
            private int id;
            private String description;
            private String valuta1;
            private String valuta2;
            private JLabel label;

            public Item(int id, String description, String valuta1, String valuta2, JLabel label) {
                this.id = id;
                this.description = description;
                this.valuta1 = valuta1;
                this.valuta2 = valuta2;
                this.label = label;
            }

            public int getId() {
                return id;
            }

            public String getDescription() {
                return description;
            }

            public JLabel getLabel() {
                return label;
            }

            public String getValuta1() {
                return valuta1;
            }

            public String getValuta2() {
                return valuta2;
            }
        }
    }
}