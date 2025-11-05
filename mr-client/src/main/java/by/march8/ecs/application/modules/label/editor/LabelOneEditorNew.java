package by.march8.ecs.application.modules.label.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.ucdao.implementations.ReferencesDao;
import by.gomel.freedev.ucframework.ucswing.BoundsPopupMenuListener;
import by.gomel.freedev.ucframework.ucswing.ComponentConfiguration;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.label.dao.MarkingJDBC;
import by.march8.entities.documents.DocumentEntity;
import by.march8.entities.documents.DocumentTypeEntity;
import by.march8.entities.label.LabelOne;
import by.march8.entities.label.VModelTestP;
import by.march8.entities.label.VNSICD;
import by.march8.entities.label.VNSIGOST;
import by.march8.entities.label.VNSIPolotno;
import by.march8.entities.label.VNSITextil;
import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by suvarov on 19.12.14.
 */
@SuppressWarnings("all")
public class LabelOneEditorNew extends EditingPane {
    private static final long serialVersionUID = 1L;
    final JComboBox cbpParametersForProduct = new JComboBox();          //Комбобокс для переключения между
    //Цвета
    private final ComboBoxPanel<VNSICD> cbpVNSICD;
    //Специальные символы стирки
    private final ComboBoxPanel<VNSITextil> cbpVNSITEXTIL;
    // private final ComboBoxPanel<VNSITextilTest> cbpVNSITEXTIL;
    //Состав изделия
    private final ComboBoxPanel<VNSIPolotno> cbpVNSIPOLOTNO;
    //Список ГОСТов
    private final ComboBoxPanel<VNSIGOST> cbpVNSIGOST;
    //Список ГОСТов, для случаев с 2 гостами (пока не используется)
    private final ComboBoxPanel<VNSIGOST> cbpGostOne;
    //Состав изделия (второй)
    private final ComboBoxPanel<VNSIPolotno> cbpSostavTwo;
    //Изделие
    private final ComboBoxPanel<VModelTestP> cbpProduct;
    //Шаблон этикетки
    private final ComboBoxPanel<DocumentEntity> cbpDocument;
    private final JLabel lProduct = new JLabel("Изделие");
    private final JLabel lArticle = new JLabel("Артикул");
    private final JLabel lModel = new JLabel("Модель");
    private final JLabel lSize = new JLabel("Размер");
    private final JLabel lAnotherGrowth = new JLabel("Рост");
    private final JLabel lSort = new JLabel("Сорт");
    private final JLabel lProductForPrint = new JLabel("Наименование для печати");
    private final JLabel lBrend = new JLabel("Брэнд,колл.");
    private final JLabel lSostavKomplekt = new JLabel("Состав комплекта");
    private final JLabel lSizeForPrint = new JLabel("Размер для печати");
    private final JLabel lBreastGirth = new JLabel("Обхват груди");
    private final JLabel lWaistGirth = new JLabel("Обхват талии");
    private final JLabel lColor = new JLabel("Цвет");
    private final JLabel lEANCode = new JLabel("КОД EAH");
    private final JLabel lGost = new JLabel("ГОСТ");
    private final JLabel lGostOne = new JLabel("ГОСТ1");
    private final JLabel lSostavOne = new JLabel("Состав");
    private final JLabel lSostavTwo = new JLabel("Состав2");
    private final JLabel lTextil = new JLabel("Выбор символов по уходу");
    private final JLabel lChoosenTextil = new JLabel("Символы по уходу");
    private final JLabel lForPrintOnProduct = new JLabel("К печати на изделие");
    private final JLabel lPackage = new JLabel("упаковочных по");
    private final JLabel lCountInPackage = new JLabel("ед в упаковке");
    private final JLabel lPrinted = new JLabel("Отпечатано");
    private final JLabel lGrowth = new JLabel("Рост");
    private final JLabel lDate = new JLabel("Дата выпуска");
    private final JLabel emptyLabel = new JLabel("");
    private final JTextField tfArticle = new JTextField();              //Артикул
    private final JTextField tfModel = new JTextField();                //Модель(фасон)
    private final JTextField tfGrowth = new JTextField();               //Рост
    private final JTextField tfProductForPrint = new JTextField();      //Наименование изделия для печати
    private final JTextField tfBrend = new JTextField();                //Бренд
    private final JTextField tfSostavKomplekt = new JTextField();       //Состав комплекта(комплект)
    private final JTextField tfSizeForPrint = new JTextField();         //Размер для печати
    private final JTextField tfBreastGirth = new JTextField();          //Обхват груди
    private final JTextField tfWaistGirth = new JTextField();           //Обхват талии
    private final JTextField tfColor = new JTextField();                //Цвета
    private final JTextField tfEANCode = new JTextField();              //EAN код
    private final JTextField tfForPrintOnProduct = new JTextField();    //"К печати на изделие" как в march8
    private final JTextField tfPackage = new JTextField();              //"упаковочных по" как в march8
    private final JTextField tfCountInPackage = new JTextField();       //"ед. в упаковке" как в march8
    private final JTextField tfPrinted = new JTextField();              //Отпечатано(пока не используется)
    private final JTextField tfProduct = new JTextField();              //Изделие
    private final JTextField tfSize = new JTextField();                 //Размер
    private final JTextField tfSort = new JTextField();                 //Сорт
    private final JTextField tfAnotherGrowth = new JTextField();        //Составной рост
    private final JTextField tfGost = new JTextField();                 //ГОСТ
    private final JTextField tfGostOne = new JTextField();              //ГОСТ 2(для случаев с 2 ГОСТами, пока не исп.)
    private final JTextField tfSostavOne = new JTextField();            //Состав изделия 1
    private final JTextField tfSostavTwo = new JTextField();            //Состав изделия 2
    private final JTextField tfDate = new JTextField();                //Дата создания этикетки
    private final JTextField tfTextil = new JTextField();               //Символы стирки
    private final JTextField tfParametersForProduct = new JTextField(); //Артикул\Шифр\Модель для поиска изделия
    private final JButton btnPrint = new JButton("Печать этикеток");    //Кнопка печати этикетки
    private final JButton btnProduct = new JButton("Выбрать изделие");  //Кнопка выбора изделия по заданным параметрам
    private final JCheckBox embroidery = new JCheckBox("Вышивка-ПП");   //Чекбокс печати "Вышивка-ПП" на этикетку
    private final JCheckBox prewash = new
            JCheckBox("Предварительная стирка");                        //Чекбокс печати "Предварительная стирка"
    protected Date today = new Date(System.currentTimeMillis());
    //private VNSITextilTest textil;
    DefaultTableModel tableModel;
    String procedureName = "modelByNar";
    HashMap parameters = new HashMap<>();
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");       //Маска для дат dd.MM.yyyy
    SimpleDateFormat formatForPrint = new SimpleDateFormat("MM.yyyy");  //Маска для дат MM.yyyy
    SimpleDateFormat formatYearForPrint = new SimpleDateFormat("yyyy"); //Маска для дат yyyy
    //шифром\артикулом\моделью для выбора изделия
    private LabelOne source = new LabelOne();
    private VNSIGOST standart;                                          //View ГОСТов
    private VNSICD color;                                               //View Цветов
    private VNSIPolotno sostav;                                         //View состава изделия
    private VNSITextil textil;                                          //View спец. символов стирки
    private VModelTestP product;                                        //Процедура для поиска изделия по параметрам
    //шифр\артикул\модель изделия
    private VNSIGOST standartOne;                                       //View ГОСТов (второй ГОСТ)
    private DocumentEntity template;                                          //Шаблон этикетки
    private int docTypeId;                                              //id типа документа шаблонов этикеток
    private ArrayList<DocumentTypeEntity> objectsTypeList = null;             //Коллекция объектов типов документа
    private Integer kodizd;                                             //Код изделия
    private Integer kodmarh;                                            //Код маршрутного листа
    private Integer poshiv;                                             //Пошив
    private double nomermarh;                                           //Номер маршрутного листа
    private Date datevrkv;
    private MarkingJDBC jdbc;

    public LabelOneEditorNew(final MainController controller) {
        //super(reference);
        setLayout(new MigLayout());
        //Подгон размера окна в зависимости от ОС
        if (ComponentConfiguration.isWindows())
            setPreferredSize(new Dimension(700, 630));
        else
            setPreferredSize(new Dimension(700, 690));
        //Изменение шрифта отдельных компонент
        ComponentConfiguration.fontPatch(lArticle, lBreastGirth, lBrend, lColor, lCountInPackage, lCountInPackage,
                lEANCode, lForPrintOnProduct, lGost, lGostOne, lGrowth, lGrowth, lModel, lPackage,
                lCountInPackage, lPrinted, lProduct, lSize, lSizeForPrint, lSort, lSostavKomplekt,
                lProductForPrint, lSostavOne, lSostavTwo, lTextil, lChoosenTextil);
        cbpVNSICD = new ComboBoxPanel<>(true, controller,                //Комбобокс выбора цветов
                MarchReferencesType.ACCOUNTING_VNSICD);
        cbpVNSIGOST = new ComboBoxPanel<>(true, controller,                //Комбобокс выбора ГОСТов
                MarchReferencesType.ACCOUNTING_VNSISTANDART);
        cbpVNSITEXTIL = new ComboBoxPanel<>(false, controller,           //Комбобокс выбора спец. символов стирки
                MarchReferencesType.ACCOUNTING_VNSITEXTIL);
        cbpVNSIPOLOTNO = new ComboBoxPanel<>(true, controller,           //Комбобокс выбора состава изделия
                MarchReferencesType.ACCOUNTING_VNSPOLOTNO);
        cbpSostavTwo = new ComboBoxPanel<>(true, controller,             //Комбобокс выбора второго состава изделия
                MarchReferencesType.ACCOUNTING_VNSPOLOTNO);
        cbpGostOne = new ComboBoxPanel<>(true, controller,               //Комбобокс выбора второго ГОСТа
                MarchReferencesType.ACCOUNTING_VNSISTANDART);
        cbpProduct = new ComboBoxPanel<>(controller
                , MarchReferencesType.ACCOUNTING_VMODEL, false);
        cbpDocument = new ComboBoxPanel<>(MarchReferencesType.          //Комбобокс выбора шаблона этикетки
                SETTINGS_DOCUMENT);
        //Нахождение Id типа документа где встречается "этикет" (отображаются только шаблоны этикеток)

        ReferencesDao referencesDao = new ReferencesDao();

        try {
            objectsTypeList = (ArrayList<DocumentTypeEntity>) referencesDao.loadDataFromBase(DocumentTypeEntity.class, "Select c from DocumentTypeEntity c");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (DocumentTypeEntity doc : objectsTypeList) {
            if (doc.getName().toLowerCase().contains("этикет")) docTypeId = doc.getId();
        }
        cbpDocument.loadingByQuery("SELECT c FROM DocumentEntity c where c.documentType = " + docTypeId);
        // cbpProduct.loadFromProcedure(MarchReferencesType.ACCOUNTING_VMODEL,procedureName,parameters);
        cbpParametersForProduct.setModel(new DefaultComboBoxModel(new String[]{
                "Артикул", "Модель", "Шифр"}));
        //Отключения возможности редактирования некоторых полей
        tfBrend.setEditable(false);
        tfBreastGirth.setEditable(false);
        tfAnotherGrowth.setEditable(false);
        tfSostavKomplekt.setEditable(true);
        tfArticle.setEditable(false);
        tfSort.setEditable(false);
        tfColor.setEditable(false);
        tfEANCode.setEditable(false);
        tfGost.setEditable(false);
        tfGrowth.setEditable(false);
        tfModel.setEditable(false);
        tfPrinted.setEditable(false);
        tfProduct.setEditable(true);
        tfSize.setEditable(false);
        tfWaistGirth.setEditable(false);
        tfSostavOne.setEditable(false);
        tfSizeForPrint.setEditable(false);
        tfDate.setEditable(false);

        //add(cbpProduct,"w 400::270,span,grow");
        add(cbpParametersForProduct, "w 200::250");
        add(tfParametersForProduct, "w 200::250");
        add(btnProduct, "w 400::270,span,grow");
        add(lProduct);
        add(tfProduct, "w 200::250,wrap");
        add(lArticle);
        add(lModel, "wrap");
        add(tfArticle, "w 200::250");
        add(tfModel, "w 200::250,wrap");
        add(lSize);
        add(lGrowth);
        add(lSort, "wrap");
        add(tfSize, "w 200::250");
        add(tfGrowth, "w 200::250");
        add(tfSort, "w 200::270,span,grow");
        add(lProductForPrint);
        add(embroidery);
        add(lEANCode, "wrap");
        add(tfProductForPrint, "w 200::250");
        add(tfEANCode, "w 200::270,wrap");

        add(lBrend);
        add(lSostavKomplekt, "wrap");
        add(tfBrend, "w 200::250");
        add(tfSostavKomplekt, "w 200::250,wrap");
        add(lSizeForPrint);
        add(tfSizeForPrint, "w 200::250,wrap");
        add(lAnotherGrowth);
        add(lBreastGirth);
        add(lWaistGirth, "wrap");
        add(tfAnotherGrowth, "w 200::250");
        add(tfBreastGirth, "w 200::250");
        add(tfWaistGirth, "w 200::270,span,grow");
        add(lColor);
        add(tfColor, "w 200::250");
        add(cbpVNSICD, "w 400::270,span,grow");
        add(lGost);
        add(tfGost, "w 200::250");
        add(cbpVNSIGOST, "w 400::270,span,grow");
       /* ГОСТ 1
        add(lGostOne);
        add(tfGostOne,"w 200::250");
        add(cbpGostOne,"w 400::270,span,grow");
        */
        add(lSostavOne);
        add(tfSostavOne, "w 200::250");
        add(cbpVNSIPOLOTNO, "w 400::270,span,grow");
        /* Состав 2
        add(lSostavTwo);
        add(tfSostavTwo,"w 200::250");
        add(cbpSostavTwo,"w 400::270,span,grow");
        */
        add(lTextil);
        //add(cbpVNSITEXTIL,"w 400::570,span,grow");
        add(cbpVNSITEXTIL, "w 400::570,span,grow");
        //add(emptyLabel);
        // add(embroidery,"w 200::250");
        add(lChoosenTextil);
        add(tfTextil, "w 400::570,span,grow");
        add(lForPrintOnProduct);
        add(lPackage);
        add(lCountInPackage, "wrap");
        add(tfForPrintOnProduct, "w 200::250");
        add(tfPackage, "w 200::250");
        add(tfCountInPackage, "w 200::270, span,grow");
        //Отпечатано
        //add(lPrinted);
        //add(tfPrinted,"w 200::250");
        add(embroidery, "w 200::250");
        add(prewash, "w 200::250");
        add(cbpDocument, "w 200::270,span,grow");
        add(lDate);
        add(tfDate, "w 200::250");
        add(btnPrint, "w 200::270, span,grow");
        //Font fontTextile = new Font("Tk Textile",Font.TRUETYPE_FONT,30);
        //tfTextil.setFont(fontTextile);
        //cbpVNSITEXTIL.getComboBox().setFont(fontTextile);
        //Временное отключение выбора изделия
        btnProduct.setEnabled(false);
        initEvents();

        jdbc = new MarkingJDBC();
    }

    /**
     * Получение размера
     *
     * @param source
     * @return
     */
    public static String takeGrowth(String source) {
        System.out.println(source + " = " + source.length());
        if ((source.indexOf(",")) != -1) {
            System.out.println(source.indexOf(","));
            return source.substring(0, source.trim().indexOf(","));
        } else
            return source.substring(0, source.trim().length() - 1);
    }

    /**
     * Вырезание роста
     *
     * @param source
     * @return
     */
    public static String takeRost(String source) {
        if (source.indexOf("-") != -1)
            return source.substring(0, source.indexOf("-") - 1);
        else return source.trim();
    }

    /**
     * Вырезание обхвата груди
     *
     * @param source
     * @return
     */
    public static String takeBreastGirth(String source) {
        if (source.indexOf("-") == source.lastIndexOf("-"))
            return source.substring(source.indexOf("-") + 1, source.length());
        else
            return source.substring(source.indexOf("-") + 1, source.lastIndexOf("-"));
    }

    /**
     * Вырезание обхвата талии
     *
     * @param source
     * @return
     */
    public static String takeWaistGirth(String source) {
        if (source.indexOf("-") != source.lastIndexOf("-"))
            return source.substring(source.lastIndexOf("-") + 1, source.length());
        else return "";
    }

    /**
     * Получение составного размера для печати
     *
     * @param growth
     * @param breastGirth
     * @param waistGirth
     * @return
     */
    public static String takeSizeForPrint(String growth, String breastGirth, String waistGirth) {
        String result = null;
        if (growth != null && !growth.isEmpty()) {
            if (breastGirth.equals(null) || breastGirth.trim().isEmpty())
                result = growth;
            else if (waistGirth.equals(null) || waistGirth.trim().isEmpty())
                result = growth.trim() + "-" + breastGirth.trim();
            else
                result = growth.trim() + "-" + breastGirth.trim() + "-" + waistGirth.trim();
        }
        return result;
    }

    @Override
    public Object getSourceEntity() {
        source.setName(tfProduct.getText());
        source.setNar(tfArticle.getText());
        try {
            source.setModel(Integer.parseInt(tfModel.getText()));
        } catch (RuntimeException e) {
            source.setModel(null);
        }
        try {
            source.setRstMarh(Integer.parseInt(tfGrowth.getText()));
        } catch (RuntimeException e) {
            source.setRstMarh(null);
        }
        try {
            source.setRzmMarh(Integer.parseInt(tfSize.getText().trim()));
        } catch (RuntimeException e) {
            source.setRzmMarh(null);
        }
        source.setBrend(tfBrend.getText());
        source.setKomplekt(tfSostavKomplekt.getText());
        source.setRazmer(tfSizeForPrint.getText());
        source.setNcw(tfColor.getText());
        source.setEancode(tfEANCode.getText());
        source.setStandart(tfGost.getText());
        if (sostav != null) {
            source.setSostavOne(sostav.getFirstName());
            source.setSostavTwo(sostav.getSecondName());
        }
        /*if (textil!=null){
            source.setTextil(textil.getName());
        }*/
        //source.setTextil();
        if (!(tfPrinted.getText().trim().isEmpty()) && (!tfForPrintOnProduct.getText().trim().isEmpty()))
            source.setPrinted(Integer.parseInt(tfPrinted.getText()) + Integer.parseInt(tfForPrintOnProduct.getText()));
        else if (!tfForPrintOnProduct.getText().trim().isEmpty())
            source.setPrinted(Integer.parseInt(tfForPrintOnProduct.getText()));
        else
            source.setPrinted(0);
        try {
            source.setSort(Integer.parseInt(tfSort.getText()));
        } catch (RuntimeException e) {
            source.setSort(null);
        }
        try {
            source.setKolEd(Integer.parseInt(tfCountInPackage.getText()));
        } catch (RuntimeException e) {
            source.setKolEd(null);
        }
        try {
            source.setKolDop(Integer.parseInt(tfPackage.getText()));
        } catch (RuntimeException e) {
            source.setKolDop(null);
        }
        try {
            source.setData(format.parse(tfDate.getText()));
        } catch (ParseException e) {
            e.printStackTrace();

        }
        if (tfTextil.getText() != null && !tfTextil.getText().isEmpty())
            source.setTextil(tfTextil.getText());
        source.setKodIzd(kodizd);
        source.setKodMarh(kodmarh);
        source.setPoshiv(poshiv);
        source.setNomerMarh(nomermarh);
        source.setDatevrkv(datevrkv);
        //source.setKodIzd(0);
        // source.setKodMarh(0);
        embroidery.setSelected(false);
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            this.source = new LabelOne();
            tfArticle.setText("");
            tfModel.setText("");
            tfGrowth.setText("");
            tfAnotherGrowth.setText("");
            tfProductForPrint.setText("");
            tfBrend.setText("");
            tfSostavKomplekt.setText("");
            tfSizeForPrint.setText("");
            tfBreastGirth.setText("");
            tfWaistGirth.setText("");
            tfColor.setText("");
            tfEANCode.setText("");
            tfForPrintOnProduct.setText("");
            tfPackage.setText("");
            tfCountInPackage.setText("");
            tfPrinted.setText("");
            tfProduct.setText("");
            tfGost.setText("");
            tfGostOne.setText("");
            tfSostavOne.setText("");
            tfSostavTwo.setText("");
            tfSort.setText("");
            tfTextil.setText("");
            tfDate.setText(format.format(today));
            btnPrint.setEnabled(false);
        } else {
            this.source = (LabelOne) object;
            tfProduct.setText(source.getName());
            tfArticle.setText(source.getNar().trim());
            if (source.getModel() != null)
                tfModel.setText("" + source.getModel());
            else tfModel.setText("");
            if (source.getRzmMarh() != null)
                tfSize.setText("" + source.getRzmMarh());
            else tfSize.setText("");
            if (source.getRstMarh() != null)
                tfGrowth.setText("" + source.getRstMarh());
            else tfGrowth.setText("");
            tfBrend.setText(source.getBrend());
            tfSostavKomplekt.setText(source.getKomplekt());
            tfSizeForPrint.setText(source.getRazmer().trim());
            //Получение составного роста
            tfAnotherGrowth.setText(takeRost(source.getRazmer()).trim());
            //Получение обхвата груди
            tfBreastGirth.setText(takeBreastGirth(source.getRazmer()).trim());
            //Получение обхвата талии
            tfWaistGirth.setText(takeWaistGirth(source.getRazmer()).trim());
            tfColor.setText(source.getNcw());
            tfGost.setText(source.getStandart());
            tfGostOne.setText(source.getStandartOne());
            //СИМВОЛЫ ПО УХОДУ
            if (source.getTextil() != null)
                tfTextil.setText(source.getTextil());
            if (source.getSort() != null)
                tfSort.setText("" + source.getSort());
            else tfSort.setText("");
            if (source.getSostavTwo() == null || source.getSostavTwo().trim().isEmpty())
                tfSostavOne.setText(source.getSostavOne().trim());
            else
                tfSostavOne.setText(source.getSostavOne().trim() + " " + source.getSostavTwo());
            //Состав 2
            // tfSostavTwo.setText(source.getSostavTwo());
            if (source.getPoshiv() != null)
                tfForPrintOnProduct.setText("" + source.getPoshiv());
            else
                tfForPrintOnProduct.setText("");
            if (source.getKolDop() != null)
                tfPackage.setText("" + source.getKolDop());
            else tfPackage.setText("");
            if (source.getKolEd() != null)
                tfCountInPackage.setText("" + source.getKolEd());
            else
                tfCountInPackage.setText("");
            tfPrinted.setText("" + source.getPrinted());
            tfEANCode.setText(source.getEancode());
            tfProductForPrint.setText(source.getName());
            tfDate.setText(format.format(source.getData()));
            kodizd = source.getKodIzd();
            kodmarh = source.getKodMarh();
            poshiv = source.getPoshiv();
            nomermarh = source.getNomerMarh();
            datevrkv = source.getDatevrkv();
            btnPrint.setEnabled(true);
            // standart=this.source.getVnsigost();
            //cbpVNSIGOST.preset(standart);
            //color = this.source.getNcw();
            // tfName.setText(this.source.getName());

        }
        cbpVNSIGOST.setSelectedIndex(-1);
        cbpVNSIGOST.getItem(-1);
        cbpGostOne.setSelectedIndex(-1);
        cbpGostOne.getItem(-1);
        cbpProduct.setSelectedIndex(-1);
        cbpProduct.getItem(-1);
        cbpSostavTwo.setSelectedIndex(-1);
        cbpSostavTwo.getItem(-1);
        cbpVNSICD.setSelectedIndex(-1);
        cbpVNSICD.getItem(-1);
        cbpVNSIPOLOTNO.setSelectedIndex(-1);
        cbpVNSIPOLOTNO.getItem(-1);
        cbpVNSITEXTIL.setSelectedIndex(-1);
        cbpVNSITEXTIL.getItem(-1);
    }

    @Override
    public boolean verificationData() {
        JOptionPane.showMessageDialog(null,
                "Эта функция пока недоступна", "Ошибка!!!",
                JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    /**
     * Обработка баркода, преобразование к 9-12 символам, иначе новые сканеры их не распознают...
     *
     * @param barcode
     * @return
     */
    public String takeBarCode(int barcode) {
        String code = "" + barcode;
        final int NINE_NUMBERS = 9;
        final int TWELVE_NUMERS = 12;
        int length = code.length();
        StringBuffer str = new StringBuffer();
        if (length < NINE_NUMBERS) {
            for (int i = 0; i < NINE_NUMBERS - length; i++)
                str.append("0");
            str.append(code);
        } else if (length != NINE_NUMBERS && length < TWELVE_NUMERS) {
            for (int i = 0; i < TWELVE_NUMERS - length; i++)
                str.append("0");
            str.append(code);
        } else
            str.append(code);
        return str.toString();
    }

    //Регистрация событий
    private void initEvents() {

        cbpVNSIGOST.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                standart = cbpVNSIGOST.selectFromReference(false);
            }
        });

        cbpVNSIGOST.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                standart = cbpVNSIGOST.getSelectedItem();
                if (standart != null)
                    tfGost.setText(standart.getName());
            }
        });
        cbpVNSIGOST.addButtonDefaultActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                cbpVNSIGOST.setSelectedIndex(-1);
            }
        });
        cbpGostOne.addPopupMenuListener(new BoundsPopupMenuListener());

        cbpVNSIPOLOTNO.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                sostav = cbpVNSIPOLOTNO.selectFromReference(false);
            }
        });

        cbpVNSIPOLOTNO.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                sostav = cbpVNSIPOLOTNO.getSelectedItem();
                if (sostav != null)
                    tfSostavOne.setText(sostav.getFirstName().trim() + " " + sostav.getSecondName());
            }
        });
        cbpVNSIPOLOTNO.addButtonDefaultActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                cbpVNSIPOLOTNO.setSelectedIndex(-1);
            }
        });
        cbpVNSIPOLOTNO.addPopupMenuListener(new BoundsPopupMenuListener());

        cbpVNSICD.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                color = cbpVNSICD.selectFromReference(false);
            }
        });

        cbpVNSICD.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                color = cbpVNSICD.getSelectedItem();
                if (color != null)
                    tfColor.setText(color.getName());
            }
        });
        cbpVNSITEXTIL.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                textil = cbpVNSITEXTIL.getSelectedItem();
                if (textil != null)
                    tfTextil.setText(textil.getName());
            }
        });
        cbpVNSITEXTIL.addButtonDefaultActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                cbpVNSITEXTIL.setSelectedIndex(-1);
            }
        });
        cbpVNSICD.addButtonDefaultActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                cbpVNSICD.setSelectedIndex(-1);
            }
        });
        cbpVNSICD.addPopupMenuListener(new BoundsPopupMenuListener());
        cbpVNSITEXTIL.addPopupMenuListener(new BoundsPopupMenuListener());

        cbpVNSITEXTIL.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                textil = cbpVNSITEXTIL.selectFromReference(false);
                //System.out.println(textil.getId());
                cbpVNSITEXTIL.presetNew(textil);
            }
        });
        cbpDocument.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                cbpDocument.selectFromReference(false);
            }
        });
        cbpDocument.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                template = cbpDocument.getSelectedItem();
                //if (template!=null)
                //  tfTextil.setText(textil.getName());
            }
        });

        //Печать этикеток
        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labelPrint();
            }
        });
        cbpDocument.addPopupMenuListener(new BoundsPopupMenuListener());

    }

    /**
     * Передача заполненного массива параметров для печати в выбранный шаблон и отображение окна
     * предпросмотра печати
     */
    public void labelPrint() {
        JasperPrint jasperPrint = null;
        // JasperPrint jasperPrint;
        JasperReport jasperReport;

        try {
            if (cbpDocument.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null,
                        "Не выбран шаблон", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                cbpDocument.getComboBox().requestFocusInWindow();
            } else {
                //Наш ШАБЛОН
                ByteArrayInputStream bis = new ByteArrayInputStream(template.getAttachment());
                jasperReport = JasperCompileManager.compileReport(bis);
                JRField[] params = jasperReport.getFields();
                String[] fieldNames = new String[params.length];
                for (int i = 0; i < params.length; i++)
                    fieldNames[i] = params[i].getName();

                //params.length;
                TableModelData(fieldNames);
                jasperPrint = JasperFillManager.fillReport(
                        jasperReport, new HashMap(),
                        new JRTableModelDataSource(tableModel));
                final JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    String getSizeForPrint(String str) {
        return str.substring(0, str.indexOf("-"));
    }

    String getSizeForPrintAdv(String str) {
        String s = str.substring(0, str.indexOf("-"));
        try {
            int sInt = Integer.valueOf(s);
            switch (sInt) {
                case 92:
                    return s + "/M";
                case 100:
                    return s + "/L";
                case 104:
                    return s + "/XL";
                case 108:
                    return s + "/XXL";
                case 112:
                    return s + "/XXXL";
                case 116:
                    return s + "/4XL";
                case 120:
                    return s + "/5XL";
                default:
                    return s;
            }
        } catch (Exception e) {
            return s;
        }
    }

    String getSizeForPrintNotStandard(String str) {
        String s = str.substring(0, str.indexOf("-"));
        try {
            int sInt = Integer.valueOf(s);
            return s + " (" + (sInt / 2) + ")";
        } catch (Exception e) {
            return s;
        }
    }

    /**
     * Заполнение динамических полей в шаблоне выбранными данными
     *
     * @param columnNames
     */
    private void TableModelData(String[] columnNames) {
        String eanCode = null;
        if (source.getEancode() != null && !source.getEancode().trim().isEmpty()) {
            eanCode = source.getEancode().trim().substring(0, source.getEancode().trim().length() - 1);
        }
        String date = formatForPrint.format(new Date()) + " г.";
        String year = formatYearForPrint.format(source.getData()) + "г.";
        Object[] objects = new Object[columnNames.length];
        for (int i = 0; i < columnNames.length; i++)
            switch (columnNames[i]) {
                case "model":
                    objects[i] = source.getModel().toString();
                    break;
                case "product":
                    objects[i] = tfProductForPrint.getText();
                    break;
                case "article":
                    objects[i] = source.getNar();
                    break;
                case "size":
                    objects[i] = getSizeForPrint(source.getRazmer());
                    break;
                case "size_plus":
                    objects[i] = getSizeForPrintAdv(source.getRazmer());
                    break;

                case "size_short":
                    objects[i] = getSizeForPrintNotStandard(source.getRazmer());
                    break;

                //case "standart":objects[i]=source.getStandart();break;
                case "standart":
                    objects[i] = tfGost.getText();
                    break;
                case "sort":
                    if (source.getSort() < 3)
                        objects[i] = String.valueOf(source.getSort());
                    else
                        objects[i] = "";
                    break;
                case "breast":
                    objects[i] = tfBreastGirth.getText().trim();
                    break;
                case "waist":
                    objects[i] = tfWaistGirth.getText().trim();
                    break;
                case "composition":
                    objects[i] = tfSostavOne.getText().trim();
                    break;
                case "complect":
                    objects[i] = tfSostavKomplekt.getText().trim();
                    break;
                case "color":
                    objects[i] = tfColor.getText().trim();
                    break;
                case "count":
                    objects[i] = tfCountInPackage.getText().trim();
                    break;
                case "sorter":
                    objects[i] = "";
                    break;
                case "packager":
                    objects[i] = "";
                    break;
                case "date":
                    objects[i] = date;
                    break;
                case "year":
                    objects[i] = year;
                    break;
                case "BarCode128":
                    //objects[i]=""+source.getId();;break;
                    objects[i] = takeBarCode(source.getId());
                    break;
                case "BarCode128Long":
                    //objects[i]=""+source.getId();;break;
                    objects[i] = takeBarCodeLong(source.getModel().toString(), source.getRzmMarh());
                    break;

                case "BarCodeEAN13":
                    objects[i] = eanCode;
                    break;
                //case "sostavOne":objects[i]=source.getSostavOne(); break;
                case "sostavOne":
                    if (sostav != null) objects[i] = sostav.getFirstName();
                    else objects[i] = source.getSostavOne();
                    break;
                case "sostavTwo":
                    if (sostav != null) objects[i] = sostav.getSecondName();
                    else if (source.getSostavTwo() != null)
                        objects[i] = source.getSostavTwo();
                    else
                        objects[i] = "";
                    break;
                case "embroidery":
                    if (embroidery.isSelected()) objects[i] = "Вышивка-ПП";
                    else objects[i] = "";
                    break;
                case "prewash":
                    if (prewash.isSelected()) objects[i] = "Предварительная стирка обязательна";
                    else objects[i] = "";
                    break;
                case "image":
                    Image image = null;
                    String templateName = "Шапка этикетки на упаковку";
                    DocumentEntity document = null;
                    ArrayList<Object> objectsList = null;
                    ReferencesDao referencesDao = new ReferencesDao();
                    try {
                        objectsList = referencesDao.loadDataForFile(DocumentEntity.class, "Select c from DocumentEntity c where c.name='" + templateName + "'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (objectsList != null) {
                        document = (DocumentEntity) objectsList.get(0);
                        byte[] attachment;
                        // "вложение"

                        attachment = document.getAttachment();
                        InputStream in = new ByteArrayInputStream(attachment);
                        try {
                            image = ImageIO.read(in);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    objects[i] = image;
                    break;
                case "textil":
                    //Преобразовываем старый шрифт(не ясно как его преобразуют в march8) в нужную нам символику
                    // на основании шрифта Tk textile
                    switch (tfTextil.getText().trim()) {
                        case "N    9 g 0 )":
                            objects[i] = "M%xb-n";
                            break;
                        case "s  9o0":
                            objects[i] = "G%xe-";
                            break;
                        case "A  9o0":
                            objects[i] = "E%xe-";
                            break;
                        case "r  9o0":
                            objects[i] = "F%xe-";
                            break;
                    }
                    //Если выбраны новые символы
                    if (tfTextil != null && !tfTextil.getText().isEmpty() && objects[i] == null)
                        objects[i] = tfTextil.getText();
                    break;

            }
        final Object[][] data = {objects};
        tableModel = new DefaultTableModel(data, columnNames);
    }

    private String takeBarCodeLong(String model, int size) {
        String result = "0";
        try {
            int size_ = Integer.valueOf(size) / 2;
            result = jdbc.getBarCode128IfExist(Integer.valueOf(model), size_).replace(" ", "");
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении функции takeBarCodeLong");
            e.printStackTrace();
        }
        return result;
    }
}
