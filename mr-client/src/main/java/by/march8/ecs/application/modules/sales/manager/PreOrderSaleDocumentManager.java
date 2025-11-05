package by.march8.ecs.application.modules.sales.manager;

import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.general.forms.OrderRequestSelectorDialog;
import by.march8.ecs.application.modules.general.forms.SaleDocumentSelectorDialog;
import by.march8.ecs.application.modules.general.forms.SendEMailDialog;
import by.march8.ecs.application.modules.general.forms.SizeSelectorDialog;
import by.march8.ecs.application.modules.marketing.model.ProductionOrder;
import by.march8.ecs.application.modules.references.classifier.dao.ClassifierDAO;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.ecs.application.modules.sales.editor.PreOrderProductEditor;
import by.march8.ecs.application.modules.sales.mode.PrepareProductsDialog;
import by.march8.ecs.application.modules.sales.model.PreOrderImportType;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.classifier.ClassifierModelView;
import by.march8.entities.product.OrderItem;
import by.march8.entities.sales.PreOrderSaleDocument;
import by.march8.entities.sales.PreOrderSaleDocumentBase;
import by.march8.entities.sales.PreOrderSaleDocumentItem;
import by.march8.entities.sales.PreOrderSaleDocumentItemBase;
import by.march8.entities.sales.PreOrderSaleDocumentItemView;
import by.march8.entities.sales.PreOrderSaleDocumentView;
import by.march8.entities.warehouse.SaleDocumentItemExportView;
import by.march8.entities.warehouse.SaleDocumentView;
import org.codehaus.jackson.map.ObjectMapper;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.ROUND_XXX_XX;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.round;

public class PreOrderSaleDocumentManager {
    private static PreOrderSaleDocumentManager instance;
    private MainController controller;
    private ClassifierPickMode classifier;
    private EditingPane productEditor;
    private SizeSelectorDialog sizeSelectDialog;


    private PreOrderSaleDocumentManager(MainController controller) {
        this.controller = controller;
        classifier = new ClassifierPickMode(controller, ClassifierType.CUSTOM);
        productEditor = new PreOrderProductEditor();
    }

    public static PreOrderSaleDocumentBase getDocumentById(int documentId) {
        DaoFactory<PreOrderSaleDocumentBase> factory = DaoFactory.getInstance();
        IGenericDaoGUI<PreOrderSaleDocumentBase> dao = factory.getGenericDao();
        try {
            return dao.getEntityByIdGUI(PreOrderSaleDocumentBase.class, documentId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения документа по его ID [" + documentId + "]");
        }
        return null;
    }

    public static PreOrderSaleDocumentManager getInstance(MainController controller) {
        if (instance == null) {
            instance = new PreOrderSaleDocumentManager(controller);
        }
        return instance;
    }


    public boolean importPreOrder(PreOrderSaleDocumentView document, PreOrderImportType importType) {
        switch (importType) {
            case ORDER:
                //
                String file = openFileDialog(MainController.getCurrentDialogPath());
                if (file != null) {
                    ProductionOrder order = getProductionOrderFromFile(file);
                    if (order != null) {
                        OrderRequestSelectorDialog dialog = new OrderRequestSelectorDialog(controller);
                        Set<OrderItem> set_ = dialog.selectOrderItems(order);
                        if (set_ != null) {
                            List<PreOrderSaleDocumentItem> list = new ArrayList<>();
                            for (OrderItem item : set_) {
                                list.add(new PreOrderSaleDocumentItem(item));
                            }
                            return addItemsToDocument(document.getId(), list);
                        }
                    }
                }

                break;

            case CLASSIFIER_ITEM:
                ClassifierItem item = classifier.selectProduct(null);
                if (item != null) {
                    PreOrderSaleDocumentItem item_ = new PreOrderSaleDocumentItem();
                    item_.setProductId(item.getId());
                    item_.setAccountingPrice(item.getPriceWholesale());
                    item_.setVat(item.getValueVat());
                    item_.setAmount(1);
                    item_.setItemColor("СИНИЙ");

                    BaseEditorDialog editor = new BaseEditorDialog(controller,
                            RecordOperationType.EDIT);
                    editor.setParentTitle("Новое изделие в заказ");
                    editor.setEditorPane(productEditor);

                    productEditor.updateEditorContent(item);
                    productEditor.setSourceEntity(item_);
                    if (editor.showModal()) {
                        return addItemToDocument(document.getId(), (PreOrderSaleDocumentItem) productEditor.getSourceEntity());
                    }
                }
                break;

            case CLASSIFIER_ARTICLE:
                ClassifierModelView article = classifier.selectArticle(null);
                if (article != null) {
                    //Артикул выбран, запрос на отображение доступных размеров
                    List<PreOrderSaleDocumentItem> list = getProductItemsFromArticle(article);
                    // Передаем список в форму ввода параметров цвета/количества
                    PrepareProductsDialog dialog = new PrepareProductsDialog(controller);
                    if (list != null) {
                        list = dialog.prepareParams(list);
                        return addItemsToDocument(document.getId(), list);
                    }

                }
                break;

            case SALE_DOCUMENT:
                // Получаем код контрагента
                SaleDocumentSelectorDialog dialog = new SaleDocumentSelectorDialog(controller);
                // Показываем форму документов для контрагента
                SaleDocumentView selectedItem = dialog.selectSaleDocument(document);
                if (selectedItem != null) {
                    // Пользователь выбрал необходимый документ
                    // Экспортируем документ предварительные заказы
                    List<PreOrderSaleDocumentItem> list = prepareOrderListFromSaleDocument(selectedItem);
                    if (list != null) {
                        return addItemsToDocument(document.getId(), list);
                    }
                }
                break;

            default:
                break;

        }
        return false;
    }

    public List<PreOrderSaleDocumentItem> getProductItemsFromArticle(ClassifierModelView item) {
        if (sizeSelectDialog == null) {
            sizeSelectDialog = new SizeSelectorDialog(controller);
        }

        List<PreOrderSaleDocumentItem> result = new ArrayList<>();

        ClassifierModelParams classifierItem = ClassifierDAO.getClassifierItem(item.getId());
        if (classifierItem != null) {

            Map<Integer, String> map = sizeSelectDialog.selectProductSizes(classifierItem, null);
            Map<Integer, ClassifierItem> assortment = classifierItem.getAssortmentAsMap();
            if (map != null) {
                // Бежим по мапе и забираем данные по изделию из артикула
                // Пробегаемся по мапе и заполняем новый список из мапы
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    Integer itemId = (Integer) pair.getKey();

                    // ПОлучаем данные по росторазмеру
                    ClassifierItem getItem = assortment.get(itemId);
                    if (getItem != null) {
                        PreOrderSaleDocumentItem newItem = new PreOrderSaleDocumentItem();
                        newItem.setProductId(itemId);
                        newItem.setAmount(1);
                        newItem.setItemColor("СИНИЙ");
                        newItem.setNote(getItem.getSizePrint());
                        newItem.setAccountingPrice(getItem.getPriceWholesale());
                        result.add(newItem);
                    } else {
                        System.out.println("ПУСТОЙ");
                    }

                    it.remove();
                }
                if (result.size() > 0) {
                    return result;
                }
            }
            // Юзер отказался выбрать размеры
            return null;
        }
        //Изделие не найдено в классификаторе
        return null;
    }

    private List<PreOrderSaleDocumentItem> prepareOrderListFromSaleDocument(SaleDocumentView selectedItem) {
        if (selectedItem != null) {
            DaoFactory<SaleDocumentItemExportView> factory = DaoFactory.getInstance();
            IGenericDaoGUI<SaleDocumentItemExportView> dao = factory.getGenericDao();
            List<QueryProperty> criteria = new ArrayList<>();
            criteria.add(new QueryProperty("documentId", selectedItem.getId()));
            List<PreOrderSaleDocumentItem> result = new ArrayList<>();
            List<SaleDocumentItemExportView> items = null;

            try {
                items = dao.getEntityListByNamedQueryGUI(SaleDocumentItemExportView.class, "SaleDocumentItemExportView.findByDocumentId", criteria);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (items != null) {
                for (SaleDocumentItemExportView item_ : items) {
                    PreOrderSaleDocumentItem item = new PreOrderSaleDocumentItem();
                    item.setProductId(item_.getProductId());
                    item.setAmount(item_.getAmount());
                    item.setAccountingPrice(item_.getAccountingPrice());
                    item.setItemColor(item_.getColor());
                    item.setVat(item_.getVat());
                    result.add(item);
                }
                return result;
            }

        }
        return null;
    }

    private boolean addItemsToDocument(int documentId, List<PreOrderSaleDocumentItem> list) {
        PreOrderSaleDocument document = getPreOrderSaleDocumentByDocumentId(documentId);
        HashMap<String, PreOrderSaleDocumentItem> map = new HashMap<>();

        if (document != null) {
            if (list != null) {
                for (PreOrderSaleDocumentItem item : document.getSpecification()) {
                    String key = item.getProductId() + "_" + item.getItemColor();
                    map.put(key, item);
                }
                for (PreOrderSaleDocumentItem item_ : list) {
                    String key = item_.getProductId() + "_" + item_.getItemColor();
                    PreOrderSaleDocumentItem itemGet = map.get(key);
                    if (itemGet != null) {
                        itemGet.setAmount(itemGet.getAmount() + item_.getAmount());
                    } else {
                        map.put(key, item_);
                        document.getSpecification().add(item_);
                        item_.setDocument(document);
                    }
                }
                return (saveArticle(document) != null);
            }
        }
        return false;
    }

    private boolean addItemToDocument(int documentId, PreOrderSaleDocumentItem item_) {
        PreOrderSaleDocument document = getPreOrderSaleDocumentByDocumentId(documentId);
        HashMap<String, PreOrderSaleDocumentItem> map = new HashMap<>();

        if (document != null) {
            for (PreOrderSaleDocumentItem item : document.getSpecification()) {
                String key = item.getProductId() + "_" + item.getItemColor();
                map.put(key, item);
            }
            String key = item_.getProductId() + "_" + item_.getItemColor();
            PreOrderSaleDocumentItem itemGet = map.get(key);
            if (itemGet != null) {
                itemGet.setAmount(itemGet.getAmount() + item_.getAmount());
            } else {
                map.put(key, item_);
                document.getSpecification().add(item_);
                item_.setDocument(document);
            }
            return (saveArticle(document) != null);
        }
        return false;
    }

    private PreOrderSaleDocument saveArticle(PreOrderSaleDocument document) {
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();
        try {
            if (document.getId() == 0) {
                return (PreOrderSaleDocument) dao.saveEntityThread(document);
            } else {
                dao.updateEntityThread(document);
                return document;
            }
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка сохранения документа по его ID [" + document.getId() + "]");
        }
        return null;
    }

    private PreOrderSaleDocumentItemBase saveProduct(PreOrderSaleDocumentItemBase product) {
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();
        try {
            if (product.getId() == 0) {
                return (PreOrderSaleDocumentItemBase) dao.saveEntityThread(product);
            } else {
                dao.updateEntityThread(product);
                return product;
            }
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка сохранения изделия по его ID [" + product.getId() + "]");
        }
        return null;
    }

    private String openFileDialog(String presetDir) {
        JFileChooser fc = new JFileChooser();
        if (presetDir != null) {
            fc.setCurrentDirectory(new File(presetDir));
        }
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Файл заказа продукции", "order"));
        fc.setAcceptAllFileFilterUsed(false);
        if (fc.showOpenDialog(controller.getMainForm()) == JFileChooser.OPEN_DIALOG) {
            MainController.setCurrentDialogPath(fc.getSelectedFile().getAbsolutePath());
            return fc.getSelectedFile().getPath();
        }

        return null;
    }

    private ProductionOrder getProductionOrderFromFile(String file) {
        ObjectMapper mapper = new ObjectMapper();
        ProductionOrder result = null;
        try {
            result = mapper.readValue(new FileInputStream(file),
                    ProductionOrder.class);
            int index = 1;
            if (result != null) {
                for (OrderItem item : result.getOrderList()) {
                    item.setId(index);
                    index++;
                }

                Comparator<OrderItem> comparator = Comparator.comparing(OrderItem::getModelNumber);
                comparator = comparator.thenComparing(Comparator.comparing(OrderItem::getArticleNumber));
                comparator = comparator.thenComparing(Comparator.comparing(OrderItem::getItemSize));
                comparator = comparator.thenComparing(Comparator.comparing(OrderItem::getItemName));

                Stream<OrderItem> order_ = result.getOrderList().stream().sorted(comparator);
                result.setOrderList(order_.collect(Collectors.toList()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public PreOrderSaleDocument getPreOrderSaleDocumentByDocumentId(int documentId) {
        DaoFactory<PreOrderSaleDocument> factory = DaoFactory.getInstance();
        IGenericDaoGUI<PreOrderSaleDocument> dao = factory.getGenericDao();
        try {
            return dao.getEntityByIdGUI(PreOrderSaleDocument.class, documentId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения документа по его ID [" + documentId + "]");
        }
        return null;
    }

    public PreOrderSaleDocumentItemBase getPreOrderSaleDocumentItemBaseById(int itemId) {
        DaoFactory<PreOrderSaleDocumentItemBase> factory = DaoFactory.getInstance();
        IGenericDaoGUI<PreOrderSaleDocumentItemBase> dao = factory.getGenericDao();
        try {
            return dao.getEntityByIdGUI(PreOrderSaleDocumentItemBase.class, itemId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения документа по его ID [" + itemId + "]");
        }
        return null;
    }

    public PreOrderSaleDocumentView getPreOrderSaleDocumentViewById(int itemId) {
        DaoFactory<PreOrderSaleDocumentView> factory = DaoFactory.getInstance();
        IGenericDaoGUI<PreOrderSaleDocumentView> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("documentId", itemId));
        try {
            return dao.getEntityByNamedQueryGUI(PreOrderSaleDocumentView.class, "PreOrderSaleDocumentView.findByDocumentId", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SaleDocumentReport prepareDataForReport(int id) {

        // Конвертим данный документ в документ отгрузки
        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        PreOrderSaleDocument document = getPreOrderSaleDocumentByDocumentId(id);
        if (document != null) {

            return provider.prepareDocumentForReport(document);
            /*SaleDocumentShippingReport report =
            if (report != null) {
                //new ProtocolBelPostReport(report);


                CommonFormatDBF dbf = new CommonFormatDBF("d:\\", "test_dbf");
                dbf.connect();
                dbf.addItem(report);
                dbf.disconnect();
            }*/
        }
        return null;
    }

    public void sendDocumentAsEMail(PreOrderSaleDocumentView document) {

        /*SendEMail sender = new SendEMail("Тестовое сообщение");
        sender.getAddressList().add("andyfreedev@gmail.com");
        sender.getAttachList().add(new File("c:/json - копия.obj"));
        sender.send();*/

        SendEMailDialog dialog = new SendEMailDialog(controller);
        dialog.showDialog(document);

    }

    public boolean editProduct(PreOrderSaleDocumentItemView selectedItem) {
        PreOrderSaleDocumentItem item_ = new PreOrderSaleDocumentItem();
        item_.setId(selectedItem.getId());
        item_.setAmount(selectedItem.getAmount());
        item_.setItemColor(selectedItem.getItemColor());

        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Изделие из заказа");
        editor.setEditorPane(productEditor);

        productEditor.updateEditorContent(selectedItem);

        productEditor.setSourceEntity(item_);
        if (editor.showModal()) {
            item_ = (PreOrderSaleDocumentItem) productEditor.getSourceEntity();

            PreOrderSaleDocumentItemBase item = getPreOrderSaleDocumentItemBaseById(item_.getId());
            if (item != null) {
                item.setItemColor(item_.getItemColor());
                item.setAmount(item_.getAmount());
                return saveProduct(item) != null;
            }
        }
        return false;
    }

    public TotalSummingUp doSummingUp(List<PreOrderSaleDocumentItemView> dataList) {
        TotalSummingUp total = new TotalSummingUp();

        // Обнуляем значения итогов по документу
        total.setValueSumCost(0.0);
        total.setValueSumVat(0.0);
        total.setValueSumCostAndVat(0.0);

        total.setValueSumCostCurrency(0.0);
        total.setValueSumVatCurrency(0.0);
        total.setValueSumCostAndVatCurrency(0.0);

        total.setValueSumAllowance(0.0);
        total.setValueSumVatRetail(0.0);
        total.setValueSumCostRetail(0.0);

        total.setAmount(0);

        //RemainsReductionService remainsService = RemainsReductionService.getInstance();
        for (PreOrderSaleDocumentItemView item : dataList) {

            // Суммируем стоимость
            total.setValueSumCost(SaleDocumentCalculator.round(total.getValueSumCost() + item.getSumCost(), ROUND_XXX_XX));
            // Суммируем сумму НДС
            total.setValueSumVat(round(total.getValueSumVat() + item.getSumVat(), ROUND_XXX_XX));
            // Суммируем итого (стоимость + сумма НДС)
            total.setValueSumCostAndVat(round(total.getValueSumCostAndVat() + item.getSumCostVat(), ROUND_XXX_XX));

            // Суммируем стоимость в валюте
            total.setValueSumCostCurrency(total.getValueSumCostCurrency() + item.getSumCostCurrency());
            // Суммируем сумму НДС в валюте
            total.setValueSumVatCurrency(total.getValueSumVatCurrency() + item.getSumVatCurrency());
            // Суммируем итого (стоимость + сумма НДС) в валюте
            total.setValueSumCostAndVatCurrency(round(total.getValueSumCostAndVatCurrency() + item.getSumCostVatCurrency(), ROUND_XXX_XX));

            total.setAmount(total.getAmount() + item.getAmount());

        }

        // Округление итоговых значений суммы
        total.setValueSumCost(round(total.getValueSumCost(), ROUND_XXX_XX));
        // Округление итоговых значений суммы НДС
        total.setValueSumVat(round(total.getValueSumVat(), ROUND_XXX_XX));
        // Округление итоговых значений Итого (сумма + НДС)
        total.setValueSumCostAndVat(round(total.getValueSumCostAndVat(), ROUND_XXX_XX));


        // Округление итоговых значений суммы
        total.setValueSumCostCurrency(round(total.getValueSumCostCurrency(), ROUND_XXX_XX));
        // Округление итоговых значений суммы НДС
        total.setValueSumVatCurrency(round(total.getValueSumVatCurrency(), ROUND_XXX_XX));
        // Округление итоговых значений Итого (сумма + НДС)
        total.setValueSumCostAndVatCurrency(round(total.getValueSumCostAndVatCurrency(), ROUND_XXX_XX));

        return total;

    }
}
