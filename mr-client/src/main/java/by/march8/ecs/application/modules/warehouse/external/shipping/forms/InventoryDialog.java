package by.march8.ecs.application.modules.warehouse.external.shipping.forms;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocument1C7DBF;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocument1C8DBF;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.DocumentUploadPreset;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.RemainsReductionService;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.RetailValue;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.entities.warehouse.SaleDocumentInventoryItem;
import by.march8.tasks.accounting.DBFExporter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Andy 11.01.2019 - 7:23.
 */
public class InventoryDialog extends BasePickDialog {

    private final int FORMAT_1C7 = 1;
    private final int FORMAT_1C8 = 2;
    private UCDatePeriodPicker datePeriodPicker;
    private UCTextField tfContractorCode;
    private JRadioButton rbFormat1C7;
    private JRadioButton rbFormat1C8;
    private JCheckBox chbAddress;
    private JComboBox<String> cbAddress;
    private JCheckBox cbFixedTradeAllowance;
    private JPanel pFixedAllowance;
    private UCTextField tfChildAllowance;
    private UCTextField tfAdultAllowance;
    private BackgroundTask task;

    public InventoryDialog(MainController controller) {
        super(controller);
        initComponents();
        initEvents();
    }

    public void initComponents() {
        setFrameSize(new Dimension(500, 300));
        getToolBar().setVisible(false);
        setTitle("Выгрузка документов по контрагенту");

        Container panel = getCenterContentPanel();
        panel.setLayout(new MigLayout());

        datePeriodPicker = new UCDatePeriodPicker();
        JLabel lblPeriod = new JLabel("Период отбора:");

        panel.add(lblPeriod);
        panel.add(datePeriodPicker, "wrap");

        panel.add(new JPanel(), "height 10:10,  wrap");

        JLabel lblContractorCode = new JLabel("Код контрагента: ");

        tfContractorCode = new UCTextField();
        tfContractorCode.setComponentParams(lblContractorCode, Integer.class, 4);

        rbFormat1C7 = new JRadioButton("Формат 1С v7");
        rbFormat1C8 = new JRadioButton("Формат 1С v8");
        panel.add(lblContractorCode);

        panel.add(tfContractorCode, "height 20:20, width 150:20:150, wrap");

        chbAddress = new JCheckBox("");
        cbAddress = new JComboBox<>();

        panel.add(chbAddress);
        panel.add(cbAddress, "height 20:20, width 350:20:350, wrap");

        panel.add(rbFormat1C7, "");
        panel.add(rbFormat1C8, "wrap");

        cbFixedTradeAllowance = new JCheckBox("Переоценка продукции");

        panel.add(cbFixedTradeAllowance, "height 200:20:200, height 20:20, span 2, wrap");

        pFixedAllowance = new JPanel(new MigLayout());
        tfChildAllowance = new UCTextField();
        tfChildAllowance.setComponentParams(null, Integer.class, 2);

        tfAdultAllowance = new UCTextField();
        tfAdultAllowance.setComponentParams(null, Integer.class, 2);


        pFixedAllowance.add(new JLabel("Детский"), "width 120:20:120, height 20:20");
        pFixedAllowance.add(tfChildAllowance, "width 50:20:50, height 20:20, wrap");
        pFixedAllowance.add(new JLabel("Взрослый"), "width 120:20:120, height 20:20");
        pFixedAllowance.add(tfAdultAllowance, "width 50:20:50, height 20:20, wrap");

        tfChildAllowance.setText("55");
        tfAdultAllowance.setText("65");

        panel.add(pFixedAllowance, "width 200:20:200, height 50:50, span 2, wrap");
        pFixedAllowance.setVisible(false);

        datePeriodPicker.setDatePickerBegin(new Date());
        datePeriodPicker.setDatePickerEnd(new Date());

        ButtonGroup buttonGroup = new ButtonGroup();
        rbFormat1C7.setSelected(true);
        buttonGroup.add(rbFormat1C7);
        buttonGroup.add(rbFormat1C8);
    }

    private void initEvents() {
        cbFixedTradeAllowance.addActionListener(a -> {
            if (cbFixedTradeAllowance.isSelected()) {
                pFixedAllowance.setVisible(true);
            } else {
                pFixedAllowance.setVisible(false);
            }
        });

        chbAddress.addActionListener(a -> {
            if (chbAddress.isSelected()) {
                if (!tfContractorCode.getText().trim().isEmpty()) {
                    getUploadAddressByContractorCode(Integer.valueOf(tfContractorCode.getText().trim()));
                }
            } else {
                cbAddress.setModel(null);
            }
        });
    }

    public DocumentUploadPreset showDialog() {

        if (showModalFrame()) {
            DocumentUploadPreset result = new DocumentUploadPreset();
            result.setPeriodBegin(datePeriodPicker.getDatePickerBegin());
            result.setPeriodEnd(datePeriodPicker.getDatePickerEnd());

            result.setContractorCode(Integer.valueOf(tfContractorCode.getText().trim()));
            result.setSaveAs(false);

            if (cbFixedTradeAllowance.isSelected()) {
                result.setChildAllowance(Integer.valueOf(tfChildAllowance.getText()));
                result.setAdultAllowance(Integer.valueOf(tfAdultAllowance.getText()));
            } else {
                result.setChildAllowance(0);
                result.setAdultAllowance(0);
            }

            result.setFixedTradeAllowance(cbFixedTradeAllowance.isSelected());

            int format = 2;
            if (rbFormat1C7.isSelected()) {
                format = 1;
            }
            result.setFormatType(format);

            if (chbAddress.isSelected()) {
                String combo = (String) cbAddress.getSelectedItem();
                if (combo != null) {
                    String[] text = combo.split("_");
                    result.setAddressId(Integer.valueOf(text[1]));
                } else {
                    result.setAddressId(-1);
                }
            } else {
                result.setAddressId(-1);
            }

            return result;
        } else {
            return null;
        }
    }

    public void createReport(BackgroundTask task, DocumentUploadPreset preset) {
        this.task = task;
        HashMap<String, SaleDocumentInventoryItem> map = createExitTradeSaleDocument(preset);
        if (!map.isEmpty()) {
            JFileChooser fc = new JFileChooser();

            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String savePath = fc.getSelectedFile().getPath();
                exportDocumentToDBF(preset, savePath, map);
            }
        }
    }

    private void exportDocumentToDBF(DocumentUploadPreset preset, String savePath, HashMap<String, SaleDocumentInventoryItem> map) {

        DBFExporter<SaleDocumentInventoryItem> dbf = null;
        int format = preset.getFormatType();
        if (format == FORMAT_1C7) {
            dbf = new SaleDocument1C7DBF(savePath, "INVENTORY_" + preset.getContractorCode() + "_" + DateUtils.getNormalDateTimeFormatPlus(new Date()) + "_V7");
        } else if (format == FORMAT_1C8) {
            dbf = new SaleDocument1C8DBF(savePath, "INVENTORY_" + preset.getContractorCode() + "_" + DateUtils.getNormalDateTimeFormatPlus(new Date()) + "_V8");
        }
        if (dbf != null) {
            try {
                dbf.connect();
                Iterator it = map.entrySet().iterator();
                int id = 1;

                RemainsReductionService remainsService = RemainsReductionService.getInstance();

                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    SaleDocumentInventoryItem item = (SaleDocumentInventoryItem) pair.getValue();
                    if (item.getVat() > 0) {
                        item.setVatWholesale(item.getVat());
                    }
                    // Если нужно пересчитать розничную цену
                    if (preset.isFixedTradeAllowance()) {
                        if (item.getTradeAllowance() > 34) {
                            if (item.getVat() > 10) {
                                // Расчет торговой надбавки для взрослого
                                RetailValue retail = SaleDocumentCalculator.getPriceAndTradeMarkupValueNew(
                                        (float) item.getPrice(),
                                        (float) preset.getAdultAllowance(),
                                        (float) item.getVat(), 1);
                                item.setTradeAllowance(preset.getAdultAllowance());
                                item.setRetailPrice(retail.getValueCostRetail());

                            } else {
                                // Расчет торговой надбавки для детского
                                RetailValue retail = SaleDocumentCalculator.getPriceAndTradeMarkupValueNew(
                                        (float) item.getPrice(),
                                        (float) preset.getChildAllowance(),
                                        (float) item.getVat(), 1);
                                item.setTradeAllowance(preset.getChildAllowance());
                                item.setRetailPrice(retail.getValueCostRetail());
                            }
                        } else {
                            int tm = preset.getAdultAllowance();
                            int vat = 20;
                            String TNVD = item.getCodeTNVED();
                            if (SaleDocumentDataProvider.isChildItem(item.getArticleCode())) {
                                tm = preset.getChildAllowance();
                                if ((TNVD.equals("6101209000") && item.getArticleCode().substring(0, 4).equals("4233"))
                                        || (TNVD.equals("6102209000") && item.getArticleCode().substring(0, 4).equals("4233"))
                                        || ((TNVD.substring(0, 4).equals("6103")) && (item.getArticleCode().substring(0, 4).equals("4231")
                                        || item.getArticleCode().substring(0, 4).equals("4233") || item.getArticleCode().substring(0, 4).equals("4238")
                                        || item.getArticleCode().substring(0, 4).equals("4261")))
                                        || ((TNVD.substring(0, 4).equals("6104")) && (item.getArticleCode().substring(0, 4).equals("4231")
                                        || item.getArticleCode().substring(0, 4).equals("4233") || item.getArticleCode().substring(0, 4).equals("4238")
                                        || item.getArticleCode().substring(0, 4).equals("4236") || item.getArticleCode().substring(0, 4).equals("4268")))
                                        || ((TNVD.substring(0, 4).equals("6112")) && (item.getArticleCode().substring(0, 4).equals("4166")
                                        || item.getArticleCode().substring(0, 5).equals("41678") || item.getArticleCode().substring(0, 5).equals("41674")
                                        || item.getArticleCode().substring(0, 5).equals("41679") || item.getArticleCode().substring(0, 4).equals("4265"))))
                                    vat = 10;
                            }

                            SaleDocumentDetailItemReport item_ = new SaleDocumentDetailItemReport();
                            item_.setArticleNumber(item.getArticleName());
                            item_.setArticleCode(item.getArticleCode());
                            int tradeMarkup = (int) remainsService.getTradeMarkupByArticleAndSize(item_, item.getItemSize(), tm);

                            RetailValue retail = SaleDocumentCalculator.getPriceAndTradeMarkupValueNew(
                                    (float) item.getPrice(),
                                    (float) tradeMarkup,
                                    (float) vat, 1);

                            item.setTradeAllowance(tm);
                            item.setRetailPrice(retail.getValueCostRetail());
                            item.setVatWholesale(vat);

                        }

                        System.out.println("ТУТ");
                    }

                    if (item != null) {
                        dbf.addItem(item);
                        task.setText("Экспорт в DBF ..." + item.getItemName() + " (" + id + " из " + map.size() + ")");
                    }
                    id++;
                }
                dbf.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private HashMap<String, SaleDocumentInventoryItem> createExitTradeSaleDocument(DocumentUploadPreset preset) {
        // Инициализация менеджера документов
        SaleDocumentManager manager = new SaleDocumentManager();

        // Загружаем кастомный документ
        List<SaleDocumentEntity> documentList;
        if (preset.getAddressId() > 0) {
            documentList = manager.getSaleDocumentsByPreset(preset);
        } else {
            documentList = manager.getRetailSaleDocumentsByPreset(preset);
        }

        HashMap<String, SaleDocumentInventoryItem> map = new HashMap<>();
        if (!documentList.isEmpty()) {
            // формируем выходную мапу со срезом последних с группировкой  (ITEM/COLOR/EAN13)
            String key = "";
            System.out.println("К учету принято " + documentList.size() + " за период " + DateUtils.getNormalDateFormat(preset.getPeriodBegin()) + " - " + DateUtils.getNormalDateFormat(preset.getPeriodEnd()));
            int id = 1;
            for (SaleDocumentEntity document : documentList) {
                // Получаем спеку на документ
                List<SaleDocumentInventoryItem> specification = getInventorySpecificationByDocumentId(document);


                if (specification != null) {
                    System.out.println(specification.size());
                    for (SaleDocumentInventoryItem item : specification) {
                        item.setDocumentDate(document.getDocumentDate());
                        item.setDocumentNumber(document.getDocumentNumber());
                        //ФОрмируем ключ для мапы (ITEM/COLOR/EAN13)
                        key = item.getEancode();
                        map.put(key, item);
                    }
                }
                task.setText("Обработка документа " + document.getDocumentNumber() + " (" + id + " из " + documentList.size() + ")...");
                id++;
            }
        }
        System.out.println("В мапе " + map.size() + " изделий");
        return map;
    }

    private List<SaleDocumentInventoryItem> getInventorySpecificationByDocumentId(SaleDocumentEntity document) {
        DaoFactory<SaleDocumentInventoryItem> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentInventoryItem> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("document", document.getId()));
        List<SaleDocumentInventoryItem> list = null;
        try {
            list = dao.getEntityListByNamedQuery(SaleDocumentInventoryItem.class, "SaleDocumentInventoryItem.findByDocumentId", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void getUploadAddressByContractorCode(int code) {
        SaleDocumentJDBC db = new SaleDocumentJDBC();
        List<String> list = db.getUploadAddressByContractorCode(code);
        cbAddress.setModel(new DefaultComboBoxModel(list.toArray()));
        cbAddress.setSelectedIndex(-1);
    }

}
