package dept.sprav.product;

import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.User;
import dept.marketing.ImagePreview;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import workDB.PDB;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ProductDetalForm extends javax.swing.JDialog {

    private final String typeForm;
    private User user;
    private int idProduct;
    /**  шапка гл. формы  */
    private JPanel headPanel;
    /**  центр. часть гл. формы  */
    private JPanel osnovaPanel;
    /**  ниж. часть гл. формы  */
    private JPanel buttPanel;
    /**  центр. часть вкладки основной инф.  */
    private JPanel centerGeneralPanel;
    /**  бок. часть вкладки основной инф.  */
    private JPanel eastGeneralPanel;
    /**  ниж. часть вкладки основной инф.  */
    private JButton buttOperacGeneral;
    /**  осн. панель вкладки основной инф.  */
    private JPanel generalPanel;
    /**  панель системной инф.  */
    private JPanel systemPanel;
    /**  осн. панель вкладки спецификации  */
    private JPanel specPanel;
    /**  шапка вкладки спецификации  */
    private JPanel headSpecPanel;
    /**  ниж. часть вкладки спецификации  */
    private JPanel footerSpecPanel;
    /**  бок. часть вкладки спецификации  */
    private JPanel eastSpecPanel;
    /**  панель кнопок вкладки спецификации  */
    private JPanel buttSpecPanel;
    /**  панель описания изделия вкладки спецификации  */
    private JPanel descripSpecPanel;
    /**  панель обмеров вкладки спецификации  */
    private JPanel operacSpecPanel;
    /**  панель состава вкладки спецификации  */
    private JPanel sostavSpecPanel;
    /**  бок. панель состава вкладки спецификации  */
    private JPanel sostavEastPanel;
    /**  осн. панель вкладки классификации  */
    private JPanel classPanel;
    /**  центр. панель вкладки классификации  */
    private JPanel centerClassPanel;
    /**  бок. панель таблицы размеров  */
    private JPanel sizeEastPanel;
    /**  правая панель вкладки классификации  */
    private JPanel kodRightPanel;
    /**  левая панель вкладки классификации  */
    private JPanel kodLeftPanel;
    /**  осн. панель вкладки изображения  */
    private JPanel imagePanel;
    /**  центр. панель вкладки изображения  */
    private JPanel centerImagePanel;
    /**  панель схем вкладки изображения  */
    private JPanel diagramImagePanel;
    /**  панель фото вкладки изображения  */
    private JPanel fotoImagePanel;
    /**  бок. панель вкладки изображения  */
    private JPanel eastImagePanel;
    /**  осн. панель вкладки материалов  */
    private JPanel materialPanel;
    /**  бок. панель вкладки материалов  */
    private JPanel eastMaterialPanel;
    /**  кнопка сохранения изм. формы  */
    private JButton buttEdit;
    /**  кнопка сохранения формы  */
    private JButton buttSave;
    /**  кнопка печати  */
    private JButton buttPrint;
    /**  кнопка "закрыть"  */
    private JButton buttClose;
    /**  кнопка выбора gpc сегмента  */
    private JButton buttGpcSeg;
    /**  кнопка выбора gpc семейства  */
    private JButton buttGpcSem;
    /**  кнопка выбора gpc класса  */
    private JButton buttGpcKl;
    /**  кнопка выбора gpc брика  */
    private JButton buttGpcBr;
    /**  кнопка выбора кода ОКРБ  */
    private JButton buttOKRB;
    /**  кнопка выбора кода ТНВЭД  */
    private JButton buttTHB;
    /**  кнопка выбора ГОСТа  */
    private JButton buttGOST;
    /**  кнопка добавления строки состава  */
    private JButton buttSostavPlus;
    /**  кнопка удаления строки состава  */
    private JButton buttSostavMinus;
    /**  кнопка изменения строки размеров  */
    private JButton buttSizeEdit;
    /**  кнопка добавления строки размеров  */
    private JButton buttSizePlus;
    /**  кнопка удаления строки размеров  */
    private JButton buttSizeMinus;
    /**  кнопка выбора бренда  */
    private JButton buttBrand;
    /**  кнопка выбора типа  */
    private JButton buttTip;
    /**  кнопка выбора вида  */
    private JButton buttVid;
    /**  кнопка выбора ассорт.  */
    private JButton buttAssort;
    /**  кнопка выбора группы  */
    private JButton buttGroup;
    /**  кнопка выбора ед. изм.  */
    private JButton buttIzm;
    /**  кнопка выбора формата обмеров  */
    private JButton buttFormat;
    /**  кнопка выбора художника  */
    private JButton buttDesigner;
    /**  кнопка выбора конструктора  */
    private JButton buttMaster;
    /**  кнопка выбора наименования  */
    private JButton buttName;
    /**  кнопка добавления изображения  */
    private JButton buttImageAdd;
    /**  кнопка удаления изображения  */
    private JButton buttImageRemove;
    /**  кнопка "сохранить как..." изображение  */
    private JButton buttImageSave;
    /**  кнопка "открыть" изображение  */
    private JButton buttImageOpen;
    /**  кнопка формирования тех. описания  */
    private JButton buttCreateTO;
    /**  кнопка выбора обмера зав. от роста  */
    private JButton buttOperacOnRst;
    /**  кнопка выбора обмера не зав. от роста  */
    private JButton buttOperacOffRst;
    /**  кнопка выбора шаблона обмеров  */
    private JButton buttOperacRemove;
    /**  кнопка добавления строки материалов  */
    private JButton buttMaterialAdd;
    /**  кнопка удаления строки материалов  */
    private JButton buttMaterialRemove;
    /**  поле наименования изд.  */
    private JTextField name;
    /**  поле модели изд.  */
    private JTextField model;
    /**  поле кода модели изд.  */
    private JTextField idModel;
    /**  поле ID бренда изд.  */
    private JTextField brandID;
    /**  поле бренда изд.  */
    private JTextField brand;
    /**  поле ID типа изд.  */
    private JTextField tipID;
    /**  поле типа изд.  */
    private JTextField tip;
    /**  поле ID вида изд.  */
    private JTextField vidID;
    /**  поле вида изд.  */
    private JTextField vid;
    /**  поле ID ассорт. изд.  */
    private JTextField assortID;
    /**  поле ассорт. изд.  */
    private JTextField assort;
    /**  поле ID группы изд.  */
    private JTextField groupID;
    /**  поле группы изд.  */
    private JTextField group;
    /**  поле ID ед. изм. изд.  */
    private JTextField izmID;
    /**  поле ед. изм. изд.  */
    private JTextField izm;
    /**  поле ID формата обмеров изд.  */
    private JTextField formatID;
    /**  поле формата обмеров изд.  */
    private JTextField format;
    /**  поле размеров изд.  */
    private JTextField size;
    /**  поле ID художника  */
    private JTextField designerID;
    /**  поле художника  */
    private JTextField designer;
    /**  поле ID конструктора  */
    private JTextField masterID;
    /**  поле конструктора  */
    private JTextField master;
    /**  поле номера протокола автора */
    private JTextField designerDoc;
    /**  поле номера продолжения автора  */
    private JTextField designerText;
    /**  поле номера протокола */
    private JTextField procDoc;
    /**  поле номера изм. протокола */
    private JTextField procText;
    /**  поле ID gpс сегмента  */
    private JTextField kodGpcSegID;
    /**  поле gpс сегмента  */
    private JTextField kodGpcSeg;
    /**  поле ID gpс семейства  */
    private JTextField kodGpcSemID;
    /**  поле gpс семейства  */
    private JTextField kodGpcSem;
    /**  поле ID gpс класса  */
    private JTextField kodGpcKlID;
    /**  поле gpс класса  */
    private JTextField kodGpcKl;
    /**  поле ID gpс брика  */
    private JTextField kodGpcBrID;
    /**  поле gpс брика  */
    private JTextField kodGpcBr;
    /**  поле ID кода ОКРБ */
    private JTextField kodOKRBID;
    /**  поле кода ОКРБ */
    private JTextField kodOKRB;
    /**  поле ID кода ТНВЭД */
    private JTextField kodTHBID;
    /**  поле кода ТНВЭД */
    private JTextField kodTHB;
    /**  поле ID ГОСТа */
    private JTextField kodGOSTID;
    /**  поле ГОСТа */
    private JTextField kodGOST;
    /**  поле номера тех. описания */
    private JTextField toModelNum;
    /**  поле кода тех. описания */
    private JTextField toModelKod;
    /**  поле ID тех. описания */
    private JLabel toModelID;

    private JDateChooser date;
    /**  дата протокола автора */
    private JDateChooser designerDate;
    /**  дата протокола  */
    private JDateChooser procDate;
    /**  поле примечания изд.  */
    private JTextPane note;
    /**  поле основания изд.  */
    private JTextPane basis;
    /**  поле описания изд.  */
    private JTextPane descrip;

    private Vector colSostav;
    private Vector colSize;
    private Vector colTO;
    private Vector colOperacSpec;
    private Vector colMaterialGeneral;
    private Vector colMaterialOther;
    private JLabel vvodDate;
    private JLabel vvodAvtor;
    private JLabel insDate;
    private JLabel insAvtor;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private ButtonGroup buttonGroupStatus;
    private JTable tableSostav;
    private JTable tableSize;
    private JTable tableTO;
    private JTable tableOperacSpec;
    private JTable tableMaterialGeneral;
    private JTable tableMaterialOther;
    private JTabbedPane tableTabbedPane;
    private JTabbedPane specTabbedPane;
    private JTabbedPane materialTabbedPane;
    private JScrollPane scTableSostav;
    private JScrollPane scTableSize;
    private DefaultTableModel tModelSostav;
    private DefaultTableModel tModelSize;
    private DefaultTableModel tModelTO;
    private DefaultTableModel tModelOperacSpec;
    private DefaultTableModel tModelMaterialGeneral;
    private DefaultTableModel tModelMaterialOther;
    private TableFilterHeader filterHeaderSmall;
    private TableFilterHeader filterHeaderSize;
    private TableFilterHeader filterHeaderTO;
    private TableFilterHeader filterHeaderOperacSpec;
    private TableFilterHeader filterHeaderMaterialGeneral;
    private TableFilterHeader filterHeaderMaterialOther;
    private DefaultTableCellRenderer renderer;

    private int minSelectedRowSostav;
    private int maxSelectedRowSostav;
    private boolean tableSostavListenerIsChanging;

    private int minSelectedRowSize;
    private int maxSelectedRowSize;
    private boolean tableSizeListenerIsChanging;

    private int minSelectedRowTO;
    private int maxSelectedRowTO;
    private boolean tableTOListenerIsChanging;

    private int minSelectedRowOperacSpec;
    private int maxSelectedRowOperacSpec;
    private boolean tableOperacSpecListenerIsChanging;

    private int minSelectedRowMaterialOther;
    private int maxSelectedRowMaterialOther;
    private boolean tableMaterialOtherListenerIsChanging;

    private int minSelectedRowMaterialGeneral;
    private int maxSelectedRowMaterialGeneral;
    private boolean tableMaterialGeneralListenerIsChanging;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    /**
     * Конструктор в режиме ADD
     *
     * @param parent
     * @param modal
     */
    public ProductDetalForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        typeForm = UtilProduct.ADD;

        initMenu();
        initPropSetting();
        init();
        initData(typeForm, null);

        this.setTitle("Новая запись в справочнике");
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // End of variables declaration//GEN-END:variables

    private void initMenu() {

    }

    private void initPropSetting() {

    }

    private void initData(String typeForm, Object object) {
        user = User.getInstance();

        idProduct = -1;

        designerDate.setDate((Calendar.getInstance()).getTime());
        procDate.setDate((Calendar.getInstance()).getTime());

        vvodDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));
        insDate.setText(new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime()));
        vvodAvtor.setText(user.getFio());
        insAvtor.setText(user.getFio());

        createSostavTable(new Vector());
        createSizeTable(new Vector());
        createOperacSpecTable(new Vector());
        createTOTable(new Vector());
        createMaterialGeneralTable(new Vector());
        createMaterialOtherTable(new Vector());

    }

    private void init() {
        this.setMinimumSize(new Dimension(850, 450));
        this.setPreferredSize(new Dimension(
                Toolkit.getDefaultToolkit().getScreenSize().width - 200,
                Toolkit.getDefaultToolkit().getScreenSize().height - 100));

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                buttClose.doClick();
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

        buttEdit = new JButton("Редактировать");
        buttEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditTableActionPerformed(evt);
            }
        });

        buttSave = new JButton("Сохранить");
        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttSaveActionPerformed(evt);
            }
        });

        buttPrint = new JButton("Печать");
        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttPrintActionPerformed(evt);
            }
        });

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSostavPlus = new JButton("+");
        buttSostavPlus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttSostavPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSostavPlusActionPerformed(evt);
            }
        });

        buttSostavMinus = new JButton("-");
        buttSostavMinus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttSostavMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSostavMinusActionPerformed(evt);
            }
        });


        buttSizePlus = new JButton("+");
        buttSizePlus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttSizePlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditIzmActionPerformed(evt);
            }
        });


        buttSizeMinus = new JButton("-");
        buttSizeMinus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttSizeMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditCopyActionPerformed(evt);
            }
        });

        buttSizeEdit = new JButton("шк");
        buttSizeEdit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttSizeEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditShkalaActionPerformed(evt);
            }
        });

        buttImageAdd = new JButton("Добавить");
        buttImageAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttImageAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttImageAddActionPerformed(evt);
            }
        });


        buttImageRemove = new JButton("Удалить");
        buttImageRemove.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttImageRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditCopyActionPerformed(evt);
            }
        });

        buttImageSave = new JButton("Сохранить");
        buttImageSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttImageSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditShkalaActionPerformed(evt);
            }
        });

        buttImageOpen = new JButton("Открыть");
        buttImageOpen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttImageOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditShkalaActionPerformed(evt);
            }
        });

        buttCreateTO = new JButton("Сформировать ТО");
        buttCreateTO.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        buttCreateTO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditShkalaActionPerformed(evt);
            }
        });

        buttOperacGeneral = new JButton("Основные");
        buttOperacGeneral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOperacGeneralActionPerformed(evt);
            }
        });

        buttOperacOnRst = new JButton("Зав. от рост");
        buttOperacOnRst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOperacOnRstActionPerformed(evt);
            }
        });

        buttOperacOffRst = new JButton("Не зав. от рост");
        buttOperacOffRst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOperacOffRstActionPerformed(evt);
            }
        });

        buttOperacRemove = new JButton("Удалить");
        buttOperacRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOperacRemoveActionPerformed(evt);
            }
        });

        buttMaterialAdd = new JButton("Добавить");
        buttMaterialAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditShkalaActionPerformed(evt);
            }
        });

        buttMaterialRemove = new JButton("Удалить");
        buttMaterialRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditShkalaActionPerformed(evt);
            }
        });

        buttName = new JButton("↺");
        buttName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttNameActionPerformed(evt);
            }
        });

        buttBrand = new JButton("↺");
        buttBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttBrandActionPerformed(evt);
            }
        });

        buttTip = new JButton("↺");
        buttTip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTipActionPerformed(evt);
            }
        });

        buttVid = new JButton("↺");
        buttVid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttVidActionPerformed(evt);
            }
        });

        buttAssort = new JButton("↺");
        buttAssort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAssortActionPerformed(evt);
            }
        });

        buttGroup = new JButton("↺");
        buttGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttGroupActionPerformed(evt);
            }
        });

        buttIzm = new JButton("↺");
        buttIzm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttIzmActionPerformed(evt);
            }
        });

        buttFormat = new JButton("↺");
        buttFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditShkalaActionPerformed(evt);
            }
        });

        buttDesigner = new JButton("↺");
        buttDesigner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttDesignerActionPerformed(evt);
            }
        });

        buttMaster = new JButton("↺");
        buttMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttMasterActionPerformed(evt);
            }
        });

        buttGpcSeg = new JButton("↺");
        buttGpcSeg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttGpcSegActionPerformed(evt);
            }
        });

        buttGpcSem = new JButton("↺");
        buttGpcSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttGpcSemActionPerformed(evt);
            }
        });

        buttGpcKl = new JButton("↺");
        buttGpcKl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttGpcKlActionPerformed(evt);
            }
        });

        buttGpcBr = new JButton("↺");
        buttGpcBr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttGpcBrActionPerformed(evt);
            }
        });

        buttOKRB = new JButton("↺");
        buttOKRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttOkrbActionPerformed(evt);
            }
        });

        buttTHB = new JButton("↺");
        buttTHB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTnvActionPerformed(evt);
            }
        });

        buttGOST = new JButton("↺");
        buttGOST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttGostActionPerformed(evt);
            }
        });

        idModel = new JTextField();
        idModel.setPreferredSize(new Dimension(100, 20));

        model = new JTextField();
        model.setPreferredSize(new Dimension(150, 20));

        name = new JTextField();
        name.setPreferredSize(new Dimension(650, 20));

        brandID = new JTextField();
        brandID.setPreferredSize(new Dimension(10, 20));
        brandID.setEnabled(false);

        brand = new JTextField();
        brand.setPreferredSize(new Dimension(400, 20));
        brand.setEnabled(false);

        tipID = new JTextField();
        tipID.setPreferredSize(new Dimension(10, 20));
        tipID.setEnabled(false);

        tip = new JTextField();
        tip.setPreferredSize(new Dimension(200, 20));
        tip.setEnabled(false);

        vidID = new JTextField();
        vidID.setPreferredSize(new Dimension(10, 20));
        vidID.setEnabled(false);

        vid = new JTextField();
        vid.setPreferredSize(new Dimension(200, 20));
        vid.setEnabled(false);

        assortID = new JTextField();
        assortID.setPreferredSize(new Dimension(10, 20));
        assortID.setEnabled(false);

        assort = new JTextField();
        assort.setPreferredSize(new Dimension(300, 20));
        assort.setEnabled(false);

        groupID = new JTextField();
        groupID.setPreferredSize(new Dimension(10, 20));
        groupID.setEnabled(false);

        group = new JTextField();
        group.setPreferredSize(new Dimension(300, 20));
        group.setEnabled(false);

        izmID = new JTextField();
        izmID.setPreferredSize(new Dimension(10, 20));
        izmID.setEnabled(false);

        izm = new JTextField();
        izm.setPreferredSize(new Dimension(100, 20));
        izm.setEnabled(false);

        size = new JTextField();
        size.setPreferredSize(new Dimension(200, 20));
        size.setEnabled(false);

        formatID = new JTextField();
        formatID.setPreferredSize(new Dimension(10, 20));

        format = new JTextField();
        format.setPreferredSize(new Dimension(200, 20));

        designerID = new JTextField();
        designerID.setPreferredSize(new Dimension(10, 20));
        designerID.setEnabled(false);

        designer = new JTextField();
        designer.setPreferredSize(new Dimension(200, 20));
        designer.setEnabled(false);

        masterID = new JTextField();
        masterID.setPreferredSize(new Dimension(10, 20));
        masterID.setEnabled(false);

        master = new JTextField();
        master.setPreferredSize(new Dimension(200, 20));
        master.setEnabled(false);

        designerDoc = new JTextField();
        designerDoc.setPreferredSize(new Dimension(150, 20));

        designerText = new JTextField();
        designerText.setPreferredSize(new Dimension(150, 20));

        designerDate = new JDateChooser();
        designerDate.setPreferredSize(new Dimension(150, 20));

        procDoc = new JTextField();
        procDoc.setPreferredSize(new Dimension(150, 20));

        procText = new JTextField();
        procText.setPreferredSize(new Dimension(150, 20));

        procDate = new JDateChooser();
        procDate.setPreferredSize(new Dimension(150, 20));

        basis = new JTextPane();
        basis.setPreferredSize(new Dimension(700, 50));

        note = new JTextPane();
        note.setPreferredSize(new Dimension(700, 80));

        kodGpcSeg = new JTextField();
        kodGpcSeg.setPreferredSize(new Dimension(250, 20));
        kodGpcSeg.setEnabled(false);

        kodGpcSegID = new JTextField();
        kodGpcSegID.setPreferredSize(new Dimension(10, 20));
        kodGpcSegID.setEnabled(false);

        kodGpcSem = new JTextField();
        kodGpcSem.setPreferredSize(new Dimension(250, 20));
        kodGpcSem.setEnabled(false);

        kodGpcSemID = new JTextField();
        kodGpcSemID.setPreferredSize(new Dimension(10, 20));
        kodGpcSemID.setEnabled(false);

        kodGpcKl = new JTextField();
        kodGpcKl.setPreferredSize(new Dimension(250, 20));
        kodGpcKl.setEnabled(false);

        kodGpcKlID = new JTextField();
        kodGpcKlID.setPreferredSize(new Dimension(10, 20));
        kodGpcKlID.setEnabled(false);

        kodGpcBr = new JTextField();
        kodGpcBr.setPreferredSize(new Dimension(250, 20));
        kodGpcBr.setEnabled(false);

        kodGpcBrID = new JTextField();
        kodGpcBrID.setPreferredSize(new Dimension(10, 20));
        kodGpcBrID.setEnabled(false);

        kodOKRB = new JTextField();
        kodOKRB.setPreferredSize(new Dimension(250, 20));
        kodOKRB.setEnabled(false);

        kodOKRBID = new JTextField();
        kodOKRBID.setPreferredSize(new Dimension(10, 20));
        kodOKRBID.setEnabled(false);

        kodTHB = new JTextField();
        kodTHB.setPreferredSize(new Dimension(250, 20));
        kodTHB.setEnabled(false);

        kodTHBID = new JTextField();
        kodTHBID.setPreferredSize(new Dimension(10, 20));
        kodTHBID.setEnabled(false);

        kodGOST = new JTextField();
        kodGOST.setPreferredSize(new Dimension(250, 20));
        kodGOST.setEnabled(false);

        kodGOSTID = new JTextField();
        kodGOSTID.setPreferredSize(new Dimension(10, 20));
        kodGOSTID.setEnabled(false);

        toModelID = new JLabel();
        toModelID.setPreferredSize(new Dimension(250, 20));

        toModelNum = new JTextField();
        toModelNum.setPreferredSize(new Dimension(120, 20));

        toModelKod = new JTextField();
        toModelKod.setPreferredSize(new Dimension(120, 20));

        descrip = new JTextPane();
        descrip.setPreferredSize(new Dimension(700, 80));

        vvodDate = new JLabel();
        vvodDate.setPreferredSize(new Dimension(150, 20));
        vvodDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        vvodAvtor = new JLabel();
        vvodAvtor.setPreferredSize(new Dimension(400, 20));
        vvodAvtor.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        insDate = new JLabel();
        insDate.setPreferredSize(new Dimension(150, 20));
        insDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        insAvtor = new JLabel();
        insAvtor.setPreferredSize(new Dimension(400, 20));
        insAvtor.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jRadioButton1 = new JRadioButton();
        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton1.setText("Внедрено;");
        jRadioButton1.setActionCommand("0");

        jRadioButton2 = new JRadioButton();
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setText("Формируется;");
        jRadioButton2.setActionCommand("1");
        jRadioButton2.setSelected(true);

        jRadioButton3 = new JRadioButton();
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setText("Снято с производ.;");
        jRadioButton3.setActionCommand("-1");

        buttonGroupStatus = new ButtonGroup();
        buttonGroupStatus.add(jRadioButton1);
        buttonGroupStatus.add(jRadioButton2);
        buttonGroupStatus.add(jRadioButton3);

        minSelectedRowSostav = -1;
        maxSelectedRowSostav = -1;
        tableSostavListenerIsChanging = false;

        minSelectedRowSize = -1;
        maxSelectedRowSize = -1;
        tableSizeListenerIsChanging = false;

        minSelectedRowTO = -1;
        maxSelectedRowTO = -1;
        tableTOListenerIsChanging = false;

        minSelectedRowOperacSpec = -1;
        maxSelectedRowOperacSpec = -1;
        tableOperacSpecListenerIsChanging = false;

        minSelectedRowMaterialGeneral = -1;
        maxSelectedRowMaterialGeneral = -1;
        tableMaterialGeneralListenerIsChanging = false;

        minSelectedRowMaterialOther = -1;
        maxSelectedRowMaterialOther = -1;
        tableMaterialOtherListenerIsChanging = false;

        colSostav = new Vector();
        colSostav.add("");
        colSostav.add("flag");
        colSostav.add("idAssort");
        colSostav.add("Название");

        colSize = new Vector();
        colSize.add("");
        colSize.add("рост");
        colSize.add("груди обх.");
        colSize.add("талии(под гр.) обх.");
        colSize.add("бедер обх.");
        colSize.add("код размерный");

        colOperacSpec = new Vector();
        colOperacSpec.add("");
        colOperacSpec.add("flag");
        colOperacSpec.add("idOperac");
        colOperacSpec.add("idTip");
        colOperacSpec.add("Тип");
        colOperacSpec.add("Код");
        colOperacSpec.add("Наименование");
        colOperacSpec.add("Допуск 1");
        colOperacSpec.add("Допуск 2");

        colTO = new Vector();
        colTO.add("");
        colTO.add("flag");
        colTO.add("idTO");
        colTO.add("Ассортимент");
        colTO.add("idOperac");
        colTO.add("idTip");
        colTO.add("Код");
        colTO.add("Наименование");
        colTO.add("Рост");
        colTO.add("+ допуск");
        colTO.add("- допуск");
        colTO.add("");

        colMaterialGeneral = new Vector();
        colMaterialGeneral.add("");
        colMaterialGeneral.add("flag");
        colMaterialGeneral.add("idTO");
        colMaterialGeneral.add("Ассортимент");

        colMaterialOther = new Vector();
        colMaterialOther.add("");
        colMaterialOther.add("flag");
        colMaterialOther.add("idTO");
        colMaterialOther.add("Ассортимент");

        tableSostav = new JTable();
        tableSostav.setAutoCreateColumnsFromModel(true);
        tableSostav.getTableHeader().setReorderingAllowed(false);
        tableSostav.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowSostav = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowSostav = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tModelSostav = new DefaultTableModel();

        scTableSostav = new JScrollPane(tableSostav);
        scTableSostav.setPreferredSize(new Dimension(400, 150));
        scTableSostav.setBorder(javax.swing.BorderFactory.createTitledBorder("Состав изделия:"));

        tableSize = new JTable();
        tableSize.setAutoCreateColumnsFromModel(true);
        tableSize.getTableHeader().setReorderingAllowed(false);
        tableSize.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowSize = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowSize = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });
        filterHeaderSize = new TableFilterHeader(tableSize, AutoChoices.ENABLED);

        tModelSize = new DefaultTableModel();

        scTableSize = new JScrollPane(tableSize);
        scTableSize.setPreferredSize(new Dimension(400, 100));
        scTableSize.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Шкала размеров:",
                javax.swing.border.TitledBorder.RIGHT,
                javax.swing.border.TitledBorder.DEFAULT_POSITION));

        tableOperacSpec = new JTable();
        tableOperacSpec.setAutoCreateColumnsFromModel(true);
        tableOperacSpec.getTableHeader().setReorderingAllowed(false);
        tableOperacSpec.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowOperacSpec = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowOperacSpec = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });
        filterHeaderOperacSpec = new TableFilterHeader(tableOperacSpec, AutoChoices.ENABLED);

        tModelOperacSpec = new DefaultTableModel();

        tableTO = new JTable();
        tableTO.setAutoCreateColumnsFromModel(true);
        tableTO.getTableHeader().setReorderingAllowed(false);
        tableTO.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowTO = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowTO = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });
        filterHeaderTO = new TableFilterHeader(tableTO, AutoChoices.ENABLED);

        tModelTO = new DefaultTableModel();

        tableMaterialGeneral = new JTable();
        tableMaterialGeneral.setAutoCreateColumnsFromModel(true);
        tableMaterialGeneral.getTableHeader().setReorderingAllowed(false);
        tableMaterialGeneral.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowMaterialGeneral = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowMaterialGeneral = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });
        filterHeaderMaterialGeneral = new TableFilterHeader(tableMaterialGeneral, AutoChoices.ENABLED);

        tModelMaterialGeneral = new DefaultTableModel();

        tableMaterialOther = new JTable();
        tableMaterialOther.setAutoCreateColumnsFromModel(true);
        tableMaterialOther.getTableHeader().setReorderingAllowed(false);
        tableMaterialOther.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowMaterialOther = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowMaterialOther = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });
        filterHeaderMaterialOther = new TableFilterHeader(tableMaterialOther, AutoChoices.ENABLED);

        tModelMaterialOther = new DefaultTableModel();

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    if (table.getValueAt(row, 1).toString().equals("-1"))
                        cell.setBackground(Color.PINK);
                    else
                        cell.setBackground(table.getBackground());

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }
        };

        //----------Вкладка основное описание

        centerGeneralPanel = new JPanel();
        centerGeneralPanel.setLayout(new ParagraphLayout());
        centerGeneralPanel.add(new JLabel("Наименование: "), ParagraphLayout.NEW_PARAGRAPH);
        centerGeneralPanel.add(name, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(buttName);
        centerGeneralPanel.add(new JLabel("Коллекция: "), ParagraphLayout.NEW_PARAGRAPH);
        //centerGeneralPanel.add(brandID);
        centerGeneralPanel.add(brand, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(buttBrand);
        centerGeneralPanel.add(new JLabel(" Ед.изм.: "));
        //centerGeneralPanel.add(izmID);
        centerGeneralPanel.add(izm);
        centerGeneralPanel.add(buttIzm);
        centerGeneralPanel.add(new JLabel("Тип: "), ParagraphLayout.NEW_PARAGRAPH);
        //centerGeneralPanel.add(tipID);
        centerGeneralPanel.add(tip);
        centerGeneralPanel.add(buttTip);
        centerGeneralPanel.add(new JLabel(" Вид: "));
        //centerGeneralPanel.add(vidID);
        centerGeneralPanel.add(vid, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(buttVid);
        centerGeneralPanel.add(new JLabel("Группа: "), ParagraphLayout.NEW_PARAGRAPH);
        //centerGeneralPanel.add(groupID);
        centerGeneralPanel.add(group, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(buttGroup);
        centerGeneralPanel.add(new JLabel("Ассортимент: "), ParagraphLayout.NEW_PARAGRAPH);
        //centerGeneralPanel.add(assortID);
        centerGeneralPanel.add(assort, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(buttAssort);
        centerGeneralPanel.add(new JLabel("Художник: "), ParagraphLayout.NEW_PARAGRAPH);
        //centerGeneralPanel.add(designerID);
        centerGeneralPanel.add(designer, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(buttDesigner);
        centerGeneralPanel.add(new JLabel("Конструктор: "), ParagraphLayout.NEW_PARAGRAPH);
        //centerGeneralPanel.add(masterID);    
        centerGeneralPanel.add(master, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(buttMaster);
        centerGeneralPanel.add(new JLabel("Протокол: "), ParagraphLayout.NEW_PARAGRAPH);
        centerGeneralPanel.add(procDoc);
        centerGeneralPanel.add(new JLabel(" Дата: "));
        centerGeneralPanel.add(procDate);
        centerGeneralPanel.add(new JLabel(" №  изменения: "));
        centerGeneralPanel.add(procText, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(new JLabel("Пр.автора: "), ParagraphLayout.NEW_PARAGRAPH);
        centerGeneralPanel.add(designerDoc);
        centerGeneralPanel.add(new JLabel(" Дата: "));
        centerGeneralPanel.add(designerDate);
        centerGeneralPanel.add(new JLabel(" Продолжение: "));
        centerGeneralPanel.add(designerText, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(new JLabel("Основание: "), ParagraphLayout.NEW_PARAGRAPH);
        centerGeneralPanel.add(new JScrollPane(basis), ParagraphLayout.NEW_LINE_STRETCH_H);
        centerGeneralPanel.add(new JLabel("Примечание: "), ParagraphLayout.NEW_PARAGRAPH);
        centerGeneralPanel.add(new JScrollPane(note), ParagraphLayout.NEW_LINE_STRETCH_HV);

        sizeEastPanel = new JPanel();
        sizeEastPanel.setLayout(new BoxLayout(sizeEastPanel, BoxLayout.Y_AXIS));
        sizeEastPanel.add(buttSizePlus);
        sizeEastPanel.add(buttSizeMinus);
        sizeEastPanel.add(buttSizeEdit);

        eastGeneralPanel = new JPanel();
        eastGeneralPanel.setLayout(new ParagraphLayout());
        eastGeneralPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        eastGeneralPanel.add(new JLabel("       Размеры:"));
        eastGeneralPanel.add(size);
        eastGeneralPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        eastGeneralPanel.add(new JLabel("   Параметры:"));
        eastGeneralPanel.add(formatID);
        eastGeneralPanel.add(format, ParagraphLayout.NEW_LINE_STRETCH_H);
        eastGeneralPanel.add(buttFormat);
        eastGeneralPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        eastGeneralPanel.add(scTableSize, ParagraphLayout.NEW_LINE_STRETCH_HV);
        eastGeneralPanel.add(sizeEastPanel, ParagraphLayout.NEW_LINE_STRETCH_V);

        generalPanel = new JPanel();
        generalPanel.setLayout(new BorderLayout(1, 1));
        generalPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        generalPanel.add(centerGeneralPanel, BorderLayout.CENTER);
        generalPanel.add(eastGeneralPanel, BorderLayout.EAST);

        //----------Вкладка тех.описание

        sostavEastPanel = new JPanel();
        sostavEastPanel.setLayout(new BoxLayout(sostavEastPanel, BoxLayout.Y_AXIS));
        sostavEastPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        sostavEastPanel.add(buttSostavPlus);
        sostavEastPanel.add(buttSostavMinus);

        sostavSpecPanel = new JPanel();
        sostavSpecPanel.setLayout(new ParagraphLayout());
        sostavSpecPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        sostavSpecPanel.add(scTableSostav, ParagraphLayout.NEW_LINE_STRETCH_H);
        sostavSpecPanel.add(sostavEastPanel);

        descripSpecPanel = new JPanel();
        descripSpecPanel.setLayout(new BorderLayout(1, 1));
        descripSpecPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        descripSpecPanel.add(new JLabel("Описание: "), BorderLayout.NORTH);
        descripSpecPanel.add(new JScrollPane(descrip), BorderLayout.CENTER);

        footerSpecPanel = new JPanel();
        footerSpecPanel.setLayout(new GridLayout(1, 2, 5, 5));
        footerSpecPanel.add(sostavSpecPanel);
        footerSpecPanel.add(descripSpecPanel);

        headSpecPanel = new JPanel();
        headSpecPanel.setLayout(new ParagraphLayout());
        headSpecPanel.add(new JLabel("ТО"), ParagraphLayout.NEW_PARAGRAPH);
        headSpecPanel.add(new JLabel(" код "));
        headSpecPanel.add(toModelKod);
        headSpecPanel.add(new JLabel(" № "));
        headSpecPanel.add(toModelNum);
        headSpecPanel.add(new JLabel("      в БД фабрики ID "));
        headSpecPanel.add(toModelID);

        eastSpecPanel = new JPanel();
        eastSpecPanel.setLayout(new GridLayout(4, 0, 5, 5));
        eastSpecPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        eastSpecPanel.add(buttOperacGeneral);
        eastSpecPanel.add(buttOperacOnRst);
        eastSpecPanel.add(buttOperacOffRst);
        eastSpecPanel.add(buttOperacRemove);

        buttSpecPanel = new JPanel();
        buttSpecPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttSpecPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttSpecPanel.add(new JLabel());
        buttSpecPanel.add(new JLabel());
        buttSpecPanel.add(buttCreateTO);

        operacSpecPanel = new JPanel();
        operacSpecPanel.setLayout(new BorderLayout(1, 1));
        operacSpecPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        operacSpecPanel.add(new JScrollPane(tableOperacSpec), BorderLayout.CENTER);
        operacSpecPanel.add(buttSpecPanel, BorderLayout.SOUTH);
        operacSpecPanel.add(eastSpecPanel, BorderLayout.EAST);

        specTabbedPane = new javax.swing.JTabbedPane();
        specTabbedPane.addTab("Тех.описание изделия", new JScrollPane(tableTO));
        specTabbedPane.addTab("Обмеры", operacSpecPanel);

        specPanel = new JPanel();
        specPanel.setLayout(new BorderLayout(1, 1));
        specPanel.add(headSpecPanel, BorderLayout.NORTH);
        specPanel.add(specTabbedPane, BorderLayout.CENTER);
        specPanel.add(footerSpecPanel, BorderLayout.SOUTH);

        //----------Вкладка изображения

        fotoImagePanel = new JPanel();
        fotoImagePanel.setLayout(new GridLayout(0, 4, 0, 2));
        fotoImagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Фото:"));

        diagramImagePanel = new JPanel();
        diagramImagePanel.setLayout(new GridLayout(0, 4, 0, 2));
        diagramImagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Схемы:"));

        eastImagePanel = new JPanel();
        eastImagePanel.setLayout(new GridLayout(11, 1, 5, 5));
        eastImagePanel.add(buttImageAdd);
        eastImagePanel.add(buttImageRemove);
        eastImagePanel.add(buttImageSave);
        eastImagePanel.add(buttImageOpen);

        centerImagePanel = new JPanel();
        centerImagePanel.setLayout(new GridLayout(2, 1, 0, 2));
        centerImagePanel.add(new JScrollPane(fotoImagePanel));
        centerImagePanel.add(new JScrollPane(diagramImagePanel));

        imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout(1, 1));
        imagePanel.add(centerImagePanel, BorderLayout.CENTER);
        imagePanel.add(eastImagePanel, BorderLayout.EAST);

        //----------Вкладка материалы

        eastMaterialPanel = new JPanel();
        eastMaterialPanel.setLayout(new GridLayout(9, 0, 5, 5));
        eastMaterialPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        eastMaterialPanel.add(buttMaterialAdd);
        eastMaterialPanel.add(buttMaterialRemove);

        materialTabbedPane = new javax.swing.JTabbedPane();
        materialTabbedPane.addTab("Полотно", new JScrollPane(tableMaterialGeneral));
        materialTabbedPane.addTab("Прикладные", new JScrollPane(tableMaterialOther));

        materialPanel = new JPanel();
        materialPanel.setLayout(new BorderLayout(1, 1));
        materialPanel.add(materialTabbedPane, BorderLayout.CENTER);
        materialPanel.add(eastMaterialPanel, BorderLayout.EAST);

        //----------Вкладка классификация

        kodRightPanel = new JPanel();
        kodRightPanel.setLayout(new ParagraphLayout());
        kodRightPanel.add(new JLabel("ГОСТ:"), ParagraphLayout.NEW_PARAGRAPH);
        //kodRightPanel.add(kodGOSTID);
        kodRightPanel.add(kodGOST);
        kodRightPanel.add(buttGOST);
        kodRightPanel.add(new JLabel("ТНВЭД:"), ParagraphLayout.NEW_PARAGRAPH);
        //kodRightPanel.add(kodTHBID);
        kodRightPanel.add(kodTHB);
        kodRightPanel.add(buttTHB);
        kodRightPanel.add(new JLabel("  ОКРБ:"), ParagraphLayout.NEW_PARAGRAPH);
        //kodRightPanel.add(kodOKRBID);
        kodRightPanel.add(kodOKRB);
        kodRightPanel.add(buttOKRB);
        kodRightPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        kodRightPanel.add(new JLabel("Коды GPC:"), ParagraphLayout.NEW_PARAGRAPH);
        kodRightPanel.add(new JLabel("сегмент:"), ParagraphLayout.NEW_PARAGRAPH);
        //kodRightPanel.add(kodGpcSegID);
        kodRightPanel.add(kodGpcSeg);
        kodRightPanel.add(buttGpcSeg);
        kodRightPanel.add(new JLabel("семейсто:"), ParagraphLayout.NEW_PARAGRAPH);
        //kodRightPanel.add(kodGpcSemID);
        kodRightPanel.add(kodGpcSem);
        kodRightPanel.add(buttGpcSem);
        kodRightPanel.add(new JLabel("класс:"), ParagraphLayout.NEW_PARAGRAPH);
        //kodRightPanel.add(kodGpcKlID);
        kodRightPanel.add(kodGpcKl);
        kodRightPanel.add(buttGpcKl);
        kodRightPanel.add(new JLabel("брик:"), ParagraphLayout.NEW_PARAGRAPH);
        //kodRightPanel.add(kodGpcBrID);     
        kodRightPanel.add(kodGpcBr);
        kodRightPanel.add(buttGpcBr);

        kodLeftPanel = new JPanel();
        kodLeftPanel.setLayout(new ParagraphLayout());
        kodLeftPanel.add(new JLabel("ТО:"), ParagraphLayout.NEW_PARAGRAPH);
        kodLeftPanel.add(new JLabel("Маркировка:"), ParagraphLayout.NEW_PARAGRAPH);

        centerClassPanel = new JPanel();
        centerClassPanel.setLayout(new GridLayout(1, 2, 5, 5));
        centerClassPanel.add(kodRightPanel);
        centerClassPanel.add(kodLeftPanel);

        systemPanel = new JPanel();
        systemPanel.setLayout(new ParagraphLayout());
        systemPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Системная информация:"));
        systemPanel.add(new JLabel("Дата ввода: "), ParagraphLayout.NEW_PARAGRAPH);
        systemPanel.add(vvodDate);
        systemPanel.add(new JLabel(" Автор ввода:"));
        systemPanel.add(vvodAvtor);
        systemPanel.add(new JLabel("Дата корр.: "), ParagraphLayout.NEW_PARAGRAPH);
        systemPanel.add(insDate);
        systemPanel.add(new JLabel(" Автор корр. :"));
        systemPanel.add(insAvtor);

        classPanel = new JPanel();
        classPanel.setLayout(new BorderLayout(1, 1));
        classPanel.add(centerClassPanel, BorderLayout.CENTER);
        classPanel.add(systemPanel, BorderLayout.SOUTH);

        //----------Гл. форма

        headPanel = new JPanel();
        headPanel.setLayout(new ParagraphLayout());
        headPanel.add(new JLabel("ID"), ParagraphLayout.NEW_PARAGRAPH);
        headPanel.add(idModel);
        headPanel.add(new JLabel("      Модель"));
        headPanel.add(model);
        headPanel.add(new JLabel("         Статус:"));
        headPanel.add(jRadioButton2);
        headPanel.add(jRadioButton1);
        headPanel.add(jRadioButton3);

        tableTabbedPane = new javax.swing.JTabbedPane();
        tableTabbedPane.addTab("Общее описание", generalPanel);
        tableTabbedPane.addTab("Спецификация", specPanel);
        tableTabbedPane.addTab("Изображения", imagePanel);
        tableTabbedPane.addTab("Материалы", materialPanel);
        tableTabbedPane.addTab("Классификация", classPanel);

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttPrint);
        buttPanel.add(buttSave);

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnovaPanel.add(headPanel, BorderLayout.NORTH);
        osnovaPanel.add(tableTabbedPane, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();

    }

    private void createSostavTable(final Vector row) {
        tModelSostav = new DefaultTableModel(row, colSostav) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelSostav.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableSostavListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowSostav == -1 || minSelectedRowSostav == -1) {
                    return;
                }
                tableSostavListenerIsChanging = true;
                boolean value = ((Boolean) tModelSostav.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowSostav; i <= maxSelectedRowSostav; i++) {
                    tModelSostav.setValueAt(Boolean.valueOf(value), tableSostav.convertRowIndexToModel(i), column);
                }

                minSelectedRowSostav = -1;
                maxSelectedRowSostav = -1;

                tableSostavListenerIsChanging = false;
            }
        });

        tableSostav.setModel(tModelSostav);
        tableSostav.setAutoCreateColumnsFromModel(true);
        tableSostav.getColumnModel().getColumn(0).setPreferredWidth(1);

        tableSostav.getColumnModel().getColumn(1).setMinWidth(0);
        tableSostav.getColumnModel().getColumn(1).setMaxWidth(0);
        tableSostav.getColumnModel().getColumn(2).setMinWidth(0);
        tableSostav.getColumnModel().getColumn(2).setMaxWidth(0);

        tableSostav.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableSostav.getTableHeader(), 0, ""));

        for (int i = 1; i < tableSostav.getColumnModel().getColumnCount(); i++) {
            tableSostav.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void createSizeTable(final Vector row) {
        tModelSize = new DefaultTableModel(row, colSize) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelSize.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableSizeListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowSize == -1 || minSelectedRowSize == -1) {
                    return;
                }
                tableSizeListenerIsChanging = true;
                boolean value = ((Boolean) tModelSize.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowSize; i <= maxSelectedRowSize; i++) {
                    tModelSize.setValueAt(Boolean.valueOf(value), tableSize.convertRowIndexToModel(i), column);
                }

                minSelectedRowSize = -1;
                maxSelectedRowSize = -1;

                tableSizeListenerIsChanging = false;
            }
        });

        tableSize.setModel(tModelSize);
        tableSize.setAutoCreateColumnsFromModel(true);
        tableSize.getColumnModel().getColumn(0).setPreferredWidth(1);


        tableSize.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableSize.getTableHeader(), 0, ""));
    }

    private void createOperacSpecTable(final Vector row) {
        tModelOperacSpec = new DefaultTableModel(row, colOperacSpec) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else if (col == 0)
                    return getValueAt(0, col).getClass();
                else
                    return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelOperacSpec.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableOperacSpecListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowOperacSpec == -1 || minSelectedRowOperacSpec == -1) {
                    return;
                }
                tableOperacSpecListenerIsChanging = true;
                boolean value = ((Boolean) tModelOperacSpec.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowOperacSpec; i <= maxSelectedRowOperacSpec; i++) {
                    tModelOperacSpec.setValueAt(Boolean.valueOf(value), tableOperacSpec.convertRowIndexToModel(i), column);
                }

                minSelectedRowOperacSpec = -1;
                maxSelectedRowOperacSpec = -1;

                tableOperacSpecListenerIsChanging = false;
            }
        });

        tableOperacSpec.setModel(tModelOperacSpec);
        tableOperacSpec.setAutoCreateColumnsFromModel(true);
        tableOperacSpec.getColumnModel().getColumn(0).setPreferredWidth(1);
        tableOperacSpec.getColumnModel().getColumn(1).setMinWidth(0);
        tableOperacSpec.getColumnModel().getColumn(1).setMaxWidth(0);
        tableOperacSpec.getColumnModel().getColumn(2).setMinWidth(0);
        tableOperacSpec.getColumnModel().getColumn(2).setMaxWidth(0);
        tableOperacSpec.getColumnModel().getColumn(3).setMinWidth(0);
        tableOperacSpec.getColumnModel().getColumn(3).setMaxWidth(0);

        for (int i = 1; i < tableOperacSpec.getColumnModel().getColumnCount(); i++) {
            tableOperacSpec.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tableOperacSpec.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableOperacSpec.getTableHeader(), 0, ""));
    }

    private void createTOTable(final Vector row) {
        tModelTO = new DefaultTableModel(row, colTO) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else if (col == 0)
                    return getValueAt(0, col).getClass();
                else
                    return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelTO.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableTOListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowTO == -1 || minSelectedRowTO == -1) {
                    return;
                }
                tableTOListenerIsChanging = true;
                boolean value = ((Boolean) tModelTO.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowTO; i <= maxSelectedRowTO; i++) {
                    tModelTO.setValueAt(Boolean.valueOf(value), tableTO.convertRowIndexToModel(i), column);
                }

                minSelectedRowTO = -1;
                maxSelectedRowTO = -1;

                tableTOListenerIsChanging = false;
            }
        });

        tableTO.setModel(tModelTO);
        tableTO.setAutoCreateColumnsFromModel(true);
        tableTO.getColumnModel().getColumn(0).setPreferredWidth(1);

        tableTO.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableTO.getTableHeader(), 0, ""));
    }

    private void createMaterialGeneralTable(final Vector row) {
        tModelMaterialGeneral = new DefaultTableModel(row, colMaterialGeneral) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else if (col == 0)
                    return getValueAt(0, col).getClass();
                else
                    return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelMaterialGeneral.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableMaterialGeneralListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowMaterialGeneral == -1 || minSelectedRowMaterialGeneral == -1) {
                    return;
                }
                tableMaterialGeneralListenerIsChanging = true;
                boolean value = ((Boolean) tModelMaterialGeneral.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowMaterialGeneral; i <= maxSelectedRowMaterialGeneral; i++) {
                    tModelMaterialGeneral.setValueAt(Boolean.valueOf(value), tableMaterialGeneral.convertRowIndexToModel(i), column);
                }

                minSelectedRowMaterialGeneral = -1;
                maxSelectedRowMaterialGeneral = -1;

                tableMaterialGeneralListenerIsChanging = false;
            }
        });

        tableMaterialGeneral.setModel(tModelMaterialGeneral);
        tableMaterialGeneral.setAutoCreateColumnsFromModel(true);
        tableMaterialGeneral.getColumnModel().getColumn(0).setPreferredWidth(1);

        tableMaterialGeneral.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableMaterialGeneral.getTableHeader(), 0, ""));
    }

    private void createMaterialOtherTable(final Vector row) {
        tModelMaterialOther = new DefaultTableModel(row, colMaterialOther) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else if (col == 0)
                    return getValueAt(0, col).getClass();
                else
                    return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelMaterialOther.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableMaterialOtherListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowMaterialOther == -1 || minSelectedRowMaterialOther == -1) {
                    return;
                }
                tableMaterialOtherListenerIsChanging = true;
                boolean value = ((Boolean) tModelMaterialOther.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowMaterialOther; i <= maxSelectedRowMaterialOther; i++) {
                    tModelMaterialOther.setValueAt(Boolean.valueOf(value), tableMaterialOther.convertRowIndexToModel(i), column);
                }

                minSelectedRowMaterialOther = -1;
                maxSelectedRowMaterialOther = -1;

                tableMaterialOtherListenerIsChanging = false;
            }
        });

        tableMaterialOther.setModel(tModelMaterialOther);
        tableMaterialOther.setAutoCreateColumnsFromModel(true);
        tableMaterialOther.getColumnModel().getColumn(0).setPreferredWidth(1);

        tableMaterialOther.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableMaterialOther.getTableHeader(), 0, ""));
    }


    private void buttCloseActionPerformed(ActionEvent evt) {
        try {
            if (typeForm.equals(UtilProduct.OPEN)) {
                dispose();
            } else {
                if (JOptionPane.showOptionDialog(null,
                        "Сохранить изменения?",
                        "Сохранение...",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Да", "Нет"},
                        "Нет") == JOptionPane.YES_OPTION)

                    buttSave.doClick();

                else {
                    dispose();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttNameActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_NAME);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                name.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_NAME);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttBrandActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_BRAND);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                brandID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                brand.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_BRAND);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttIzmActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_IZM);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                izmID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                izm.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_IZM);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTipActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_TIP);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                tipID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                tip.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_TIP);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttVidActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_VID);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                vidID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                vid.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_VID);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttAssortActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_ASSORT);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                assortID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                assort.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_ASSORT);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSostavPlusActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_ASSORT);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(0);
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH.getName());

                tModelSostav.insertRow(tModelSostav.getRowCount(), tmp);
            }


        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_ASSORT);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSostavMinusActionPerformed(ActionEvent evt) {
        try {
            boolean select = false;

            for (Object row : tModelSostav.getDataVector()) {
                if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                    select = true;
                    break;
                }
            }

            if (select) {
                for (int i = 0; i < tableSostav.getRowCount(); i++) {
                    if (Boolean.valueOf(tableSostav.getValueAt(i, 0).toString())) {
                        tModelSostav.setValueAt(
                                -1,
                                tableSostav.convertRowIndexToModel(i),
                                1);
                    }
                }

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

            tableSostav.revalidate();
            tableSostav.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttGroupActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_GROUP);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                groupID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                group.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_GROUP);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttDesignerActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_EMPL_DESIGNER);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                designerID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                designer.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_EMPL_DESIGNER);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttMasterActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_EMPL_MASTER);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                masterID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                master.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_EMPL_MASTER);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttGpcSegActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_KOD_SEG);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                kodGpcSegID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                kodGpcSeg.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_KOD_SEG);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttGpcSemActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_KOD_SEM);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                kodGpcSemID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                kodGpcSem.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_KOD_SEM);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttGpcKlActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_KOD_KL);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                kodGpcKlID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                kodGpcKl.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_KOD_KL);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttGpcBrActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_KOD_BR);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                kodGpcBrID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                kodGpcBr.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_KOD_BR);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttGostActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_KOD_GOST);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                kodGOSTID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                kodGOST.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_KOD_GOST);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTnvActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_KOD_TNV);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                kodTHBID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                kodTHB.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_KOD_TNV);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOkrbActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_IZD_KOD_OKRB);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                kodOKRBID.setText(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH.getId()));
                kodOKRB.setText(UtilProduct.ITEM_SELECT_SEARCH.getName());
            }
        } catch (Exception e) {
            clear(UtilProduct.TYPE_IZD_KOD_OKRB);
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOperacGeneralActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_TO_GENERAL, UtilProduct.SELECT_TO);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(0);
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getId()));
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getTip()));
                tmp.add(UtilProduct.getNameTipTO(Integer.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getTip())));
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getNum()));
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH_TO.getName());
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH_TO.getVal1());
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH_TO.getVal2());

                tModelOperacSpec.insertRow(tModelOperacSpec.getRowCount(), tmp);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOperacOnRstActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_TO_RST_ON, UtilProduct.SELECT_TO);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(0);
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getId()));
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getTip()));
                tmp.add(UtilProduct.getNameTipTO(Integer.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getTip())));
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getNum()));
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH_TO.getName());
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH_TO.getVal1());
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH_TO.getVal2());

                tModelOperacSpec.insertRow(tModelOperacSpec.getRowCount(), tmp);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOperacOffRstActionPerformed(ActionEvent evt) {
        try {
            new SelectForm(this, true, UtilProduct.TYPE_TO_RST_OFF, UtilProduct.SELECT_TO);

            if (UtilProduct.ACTION_BUTT_SELECT_SEARCH) {
                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(0);
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getId()));
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getTip()));
                tmp.add(UtilProduct.getNameTipTO(Integer.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getTip())));
                tmp.add(String.valueOf(UtilProduct.ITEM_SELECT_SEARCH_TO.getNum()));
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH_TO.getName());
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH_TO.getVal1());
                tmp.add(UtilProduct.ITEM_SELECT_SEARCH_TO.getVal2());

                tModelOperacSpec.insertRow(tModelOperacSpec.getRowCount(), tmp);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttImageAddActionPerformed(ActionEvent evt) {
        try {

            JFileChooser fileChs = new JFileChooser();
            fileChs.setDialogTitle("Choose file for loading");
            fileChs.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChs.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {

                    if (f != null)
                        if (f.isDirectory()) return true;

                    return f.getName().toLowerCase().endsWith("jpeg") || f.getName().toLowerCase().endsWith("jpg");

                }

                public String getDescription() {
                    return "*.jpeg; *.jpg;";
                }

            });
            fileChs.setAcceptAllFileFilterUsed(false);

            // Добавление панели предварительного просмотра.
            fileChs.setAccessory(new ImagePreview(fileChs));

            int res = fileChs.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                ImageLabel imgLabel = new ImageLabel();
                imgLabel.setImageFile(fileChs.getSelectedFile());
                BufferedImage im = imgLabel.getImage();
                imgLabel.setSize(500, 500);
                fotoImagePanel.add(imgLabel);
                fotoImagePanel.revalidate();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttOperacRemoveActionPerformed(ActionEvent evt) {
        try {
            boolean select = false;

            for (Object row : tModelOperacSpec.getDataVector()) {
                if (Boolean.valueOf(((Vector) row).get(0).toString())) {
                    select = true;
                    break;
                }
            }

            if (select) {
                for (int i = 0; i < tableOperacSpec.getRowCount(); i++) {
                    if (Boolean.valueOf(tableOperacSpec.getValueAt(i, 0).toString())) {
                        tModelOperacSpec.setValueAt(
                                -1,
                                tableOperacSpec.convertRowIndexToModel(i),
                                1);
                    }
                }

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

            tableOperacSpec.revalidate();
            tableOperacSpec.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear(String type) {
        try {
            if (type.equals(UtilProduct.TYPE_IZD_NAME)) {
                name.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_BRAND)) {
                brandID.setText("");
                brand.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_IZM)) {
                izmID.setText("");
                izm.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_TIP)) {
                tipID.setText("");
                tip.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_VID)) {
                vidID.setText("");
                vid.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_GROUP)) {
                groupID.setText("");
                group.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_ASSORT)) {
                assortID.setText("");
                assort.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_SEG)) {
                kodGpcSegID.setText("");
                kodGpcSeg.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_SEM)) {
                kodGpcSemID.setText("");
                kodGpcSem.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_BR)) {
                kodGpcBrID.setText("");
                kodGpcBr.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_KL)) {
                kodGpcKlID.setText("");
                kodGpcKl.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_GOST)) {
                kodGOSTID.setText("");
                kodGOST.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_TNV)) {
                kodTHBID.setText("");
                kodTHB.setText("");

            } else if (type.equals(UtilProduct.TYPE_IZD_KOD_OKRB)) {
                kodOKRBID.setText("");
                kodOKRB.setText("");

            } else if (type.equals(UtilProduct.TYPE_EMPL_DESIGNER)) {
                designerID.setText("");
                designer.setText("");

            } else if (type.equals(UtilProduct.TYPE_EMPL_MASTER)) {
                masterID.setText("");
                master.setText("");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBigImage(int id) throws Exception {
        PDB pdb = new PDB();
        JLabel bigImage = new JLabel();
        bigImage.setHorizontalAlignment(JLabel.CENTER);
        bigImage.setVerticalAlignment(JLabel.CENTER);
        bigImage.setSize(fotoImagePanel.getWidth(), fotoImagePanel.getHeight());
        bigImage.setIcon(sizeImage(ImageIO.read(new ByteArrayInputStream(pdb.getBigImages(id))), 2));
        fotoImagePanel.removeAll();
        fotoImagePanel.add(bigImage);
        fotoImagePanel.repaint();
        fotoImagePanel.validate();
        pdb.disConn();
    }

    private ImageIcon sizeImage(BufferedImage destImage, int i) {
        int width = 0, height = 0;
        float el_width = 0, el_height = 0;
        float multiple1, multiple2;
        float im_width = destImage.getWidth();
        float im_height = destImage.getHeight();

        switch (i) {
            case 1:
                //  el_width = jScrollPane3.getWidth(); el_height = jScrollPane3.getHeight();
                break;
            case 2:
                el_width = fotoImagePanel.getWidth();
                el_height = fotoImagePanel.getHeight();
                break;
            case 3:
                //   el_width = jScrollPane3.getWidth()+50; el_height = jScrollPane3.getHeight()+50;
                break;
            default:
                break;
        }

        if (im_width < el_width && im_height < el_height) {
            width = destImage.getWidth();
            height = destImage.getHeight();
        } else {
            multiple1 = im_width / (el_width - 50);
            multiple2 = im_height / (el_height - 50);
            if (multiple1 >= multiple2) {
                width = (int) (im_width / multiple1);
                height = (int) (im_height / multiple1);
            } else if (multiple1 < multiple2) {
                width = (int) (im_width / multiple2);
                height = (int) (im_height / multiple2);
            }
        }
        return new ImageIcon(destImage.getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }

}
