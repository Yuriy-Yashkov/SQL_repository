package by.march8.ecs.application.modules.marketing.manager;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.general.forms.SizeSelectorDialog;
import by.march8.ecs.application.modules.marketing.model.PostgresPlanningDocument;
import by.march8.ecs.application.modules.marketing.model.PostgresPlanningDocumentItem;
import by.march8.ecs.application.modules.marketing.model.ProductCatalogArticleReport;
import by.march8.ecs.application.modules.marketing.model.ProductCatalogReport;
import by.march8.ecs.application.modules.marketing.model.ProductionCatalogCategoryReport;
import by.march8.ecs.application.modules.marketing.model.ProductionCatalogReportPreset;
import by.march8.ecs.application.modules.marketing.reports.ProductionCatalogHTMLGenerator;
import by.march8.ecs.application.modules.marketing.reports.ProductionCatalogPlainExcel;
import by.march8.ecs.application.modules.references.classifier.dao.ClassifierDAO;
import by.march8.ecs.application.modules.references.classifier.dao.ClassifierJDBC;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.services.images.ColorImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import by.march8.entities.classifier.ClassifierArticleComposition;
import by.march8.entities.classifier.ClassifierBrands;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.classifier.ClassifierModelView;
import by.march8.entities.product.ProductionCatalog;
import by.march8.entities.product.ProductionCatalogArticle;
import by.march8.entities.product.ProductionCatalogArticleEntity;
import by.march8.entities.product.ProductionCatalogArticleView;
import by.march8.entities.product.ProductionCatalogProduct;
import by.march8.entities.product.ProductionCatalogProductEntity;
import by.march8.entities.product.ProductionCatalogProductReport;
import by.march8.entities.warehouse.ERPRemainsEntity;
import dept.production.planning.PlanPDB;

import javax.swing.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Andy 21.01.2019 - 9:42.
 */
@SuppressWarnings("all")
public class ProductionCatalogManager {

    private final MainController controller;
    private SizeSelectorDialog sizeSelectDialog;
    private ColorImageService imageService;

    private List<ClassifierBrands> bransList = null;
    private ClassifierJDBC db;

    public ProductionCatalogManager(MainController controller) {
        this.controller = controller;
        imageService = ModelImageServiceDB.getInstance();
    }

    public void prepareReportDialog(int documentId) {
        PrepareReportDialog dialog = new PrepareReportDialog(controller);
        ProductionCatalogReportPreset preset = dialog.showDialog(null);
        if (preset != null) {
            createReport(controller, preset, documentId);
        }
    }

    public ProductionCatalogArticle addArticleManual(ProductionCatalog document, ClassifierModelView item) {
        if (sizeSelectDialog == null) {
            sizeSelectDialog = new SizeSelectorDialog(controller);
        }

        ClassifierModelParams classifierItem = ClassifierDAO.getClassifierItem(item.getId());
        if (classifierItem != null) {
            // Проверим, есть ли данная модель уже в каталоге
            ProductionCatalogArticle article = getArticleFromDocument(document.getId(), classifierItem.getId());
            List<ProductionCatalogProduct> productPreset = null;
            if (article != null) {
                productPreset = article.getProductList();
            }

            Map<Integer, String> map = sizeSelectDialog.selectProductSizes(classifierItem, productPreset);
            if (map != null) {
                if (article == null) {
                    article = new ProductionCatalogArticle();
                    article.setId(0);
                    article.setDocumentId(document.getId());
                    article.setArticleId(classifierItem.getId());
                    ImageItem image = imageService.getDefaultImageByModelNumber(String.valueOf(classifierItem.getModelNumber()));
                    article.setColors(getPresetColors(String.valueOf(classifierItem.getModelNumber())));
                    if (image != null) {
                        article.setDefaultImage(image.getImage().getImageFile());
                    }
                }

                for (ClassifierItem item_ : classifierItem.getAssortmentList()) {
                    if (item_ != null) {
                        if (item_.getItemGrade() == 1) {
                            if (map.get(item_.getId()) != null) {
                                ProductionCatalogProduct product = new ProductionCatalogProduct();
                                product.setId(0);
                                product.setArticle(article);
                                product.setProductId(item_.getId());

                                product.setColors(article.getColors());
                                product.setPriceValue(item_.getPriceWholesale());
                                product.setDiscountValue(0);
                                product.setPriceDiscountValue(0);

                                product.setSize(item_.getSize());
                                product.setGrowth(item_.getGrowth());


                                article.getProductList().add(product);
                            }
                        }
                    }
                }
                return saveArticle(article, true);
            }
            // Юзер отказался выбрать размеры
            return null;
        }
        //Изделие не найдено в классификаторе
        return null;
    }


    private String getPresetColors(String model) {
        List<String> colorList = imageService.getColorListForModel(model);
        StringBuilder result = new StringBuilder();

        if (!colorList.isEmpty()) {
            for (String s : colorList) {
                result.append(s).append(",");
            }

            return result.substring(0, result.length() - 1);
        }
        return null;
    }

    public boolean addProductManual(ProductionCatalogArticleView article_) {
        if (sizeSelectDialog == null) {
            sizeSelectDialog = new SizeSelectorDialog(controller);
        }

        ClassifierModelParams classifierItem = ClassifierDAO.getClassifierItem(article_.getArticleId());
        if (classifierItem != null) {
            // Проверим, есть ли данная модель уже в каталоге
            ProductionCatalogArticle article = getArticleFromDocument(article_.getDocumentId(), classifierItem.getId());
            List<ProductionCatalogProduct> productPreset;
            if (article != null) {
                productPreset = article.getProductList();
                Map<Integer, String> map = sizeSelectDialog.selectProductSizes(classifierItem, productPreset);
                if (map != null) {
                    for (ClassifierItem item_ : classifierItem.getAssortmentList()) {
                        if (item_ != null) {
                            if (item_.getItemGrade() == 1 && item_.getPriceWholesale() > 0) {
                                if (map.get(item_.getId()) != null) {
                                    ProductionCatalogProduct product = new ProductionCatalogProduct();
                                    product.setId(0);
                                    product.setArticle(article);
                                    product.setProductId(item_.getId());

                                    product.setColors(article_.getColors());
                                    product.setPriceValue(item_.getPriceWholesale());
                                    product.setDiscountValue(0);
                                    product.setPriceDiscountValue(0);

                                    product.setSize(item_.getSize());
                                    product.setGrowth(item_.getGrowth());

                                    article.getProductList().add(product);
                                }
                            }
                        }
                    }
                    return (saveArticle(article, true) != null);
                }
            }
            // Юзер отказался выбрать размеры
            return false;
        }
        //Изделие не найдено в классификаторе
        return false;
    }

    private ProductionCatalogArticle getArticleFromDocument(int documentId, int articleId) {
        DaoFactory<ProductionCatalogArticle> factory = DaoFactory.getInstance();
        IGenericDao<ProductionCatalogArticle> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("document", documentId));
        criteria.add(new QueryProperty("article", articleId));
        List<ProductionCatalogArticle> list = null;
        try {
            list = dao.getEntityListByNamedQuery(ProductionCatalogArticle.class, "ProductionCatalogArticle.findByDocumentAndArticleId", criteria);
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ProductionCatalogArticle saveArticle(ProductionCatalogArticle article, boolean ui) {
        // Расчет диапазона размеров и цен
        calculateRanges(article);


        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();
        try {
            if (article.getId() == 0) {
                if (ui) {
                    return (ProductionCatalogArticle) dao.saveEntityThread(article);
                } else {
                    return (ProductionCatalogArticle) dao.saveEntity(article);
                }
            } else {
                if (ui) {
                    dao.updateEntityThread(article);
                } else {
                    dao.updateEntity(article);
                }
                return article;
            }
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка сохранения документа по его ID [" + article.getArticleId() + "]");
        }
        return null;
    }

    private void calculateRanges(ProductionCatalogArticle article) {
        // ПОлучаем всевозможные учетные цены, так как они - ключ к группировке по диапазону
        Set<String> setPrice = new TreeSet<>();
        for (ProductionCatalogProduct product : article.getProductList()) {
            setPrice.add(String.format("%.2f", product.getPriceValue()));
        }

        List<List<Integer>> listSizes = new ArrayList<>();

        // Итерируемся по диапазону цен, и отбираем размеры для данной группы
        String prices = "";
        for (String price : setPrice) {
            List<Integer> list = new ArrayList<>();
            listSizes.add(list);
            for (ProductionCatalogProduct product : article.getProductList()) {
                if (price.equals(String.format("%.2f", (product.getPriceValue())))) {
                    list.add(product.getSize());
                }
            }
            prices += price + "_";
        }
        String sizes = "";
        for (List<Integer> list_ : listSizes) {
            Collections.sort(list_);
            sizes += list_.get(0) + " - " + list_.get(list_.size() - 1) + "_";
        }

        article.setSizeRange(sizes);
        article.setPriceRange(prices);
    }

    public ProductionCatalogArticleEntity getProductionCatalogArticleEntityById(int id) {
        DaoFactory<ProductionCatalogArticleEntity> factory = DaoFactory.getInstance();
        IGenericDaoGUI<ProductionCatalogArticleEntity> dao = factory.getGenericDao();
        try {
            return dao.getEntityByIdGUI(ProductionCatalogArticleEntity.class, id);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения артикула по его ID [" + id + "]");
        }
        return null;
    }

    public ProductionCatalogProductEntity getProductionCatalogProductEntityById(int id) {
        DaoFactory<ProductionCatalogProductEntity> factory = DaoFactory.getInstance();
        IGenericDaoGUI<ProductionCatalogProductEntity> dao = factory.getGenericDao();
        try {
            return dao.getEntityByIdGUI(ProductionCatalogProductEntity.class, id);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения изделия по его ID [" + id + "]");
        }
        return null;
    }

    public ProductionCatalogArticleView getProductionCatalogArticleViewById(int id) {
        DaoFactory<ProductionCatalogArticleView> factory = DaoFactory.getInstance();
        IGenericDao<ProductionCatalogArticleView> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("id", id));
        List<ProductionCatalogArticleView> list = null;
        try {
            list = dao.getEntityListByNamedQuery(ProductionCatalogArticleView.class, "ProductionCatalogArticleView.findById", criteria);
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ClassifierBrands> getBrandsList() {
        if (bransList == null) {
            DaoFactory<ClassifierBrands> factory = DaoFactory.getInstance();
            IGenericDao<ClassifierBrands> dao = factory.getGenericDao();

            try {
                bransList = dao.getAllEntity(ClassifierBrands.class);
                bransList.add(0, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bransList;
    }

    public ProductionCatalog getProductCatalogDocumentByDocumentId(int documentId) {
        DaoFactory<ProductionCatalog> factory = DaoFactory.getInstance();
        IGenericDaoGUI<ProductionCatalog> dao = factory.getGenericDao();
        try {
            return dao.getEntityByIdGUI(ProductionCatalog.class, documentId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения изделия по его ID [" + documentId + "]");
        }
        return null;
    }

    public List<ProductionCatalogProductReport> getProductionCatalogReportItemByDocumentId(int id) {
        DaoFactory<ProductionCatalogProductReport> factory = DaoFactory.getInstance();
        IGenericDao<ProductionCatalogProductReport> dao = factory.getGenericDao();

        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("document", id));

        try {
            return dao.getEntityListByNamedQuery(ProductionCatalogProductReport.class, "ProductionCatalogProductReport.findByDocumentId", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ProductCatalogReport prepareDocumentReportData(int documentId, List<String> order, ProductionCatalogReportPreset preset) {
        ProductCatalogReport reportData = new ProductCatalogReport();
        // ПОлучаем документ
        ProductionCatalog document = getProductCatalogDocumentByDocumentId(documentId);
        if (document != null) {
            reportData.setDocument(document);
            reportData.setPreset(preset);
            // Если порядок не установлен то берем все подряд
            List<ProductionCatalogProductReport> rawReportList = getProductionCatalogReportItemByDocumentId(documentId);
            if (rawReportList != null) {
                // Установка необработанных данных
                reportData.setRawReportData(rawReportList);

                prepareReportData(reportData, order);
            }

            return reportData;
        }

        return null;
    }

    private ProductCatalogReport prepareReportData(ProductCatalogReport data, List<String> order) {
        List<String> orderList = order;
        if (orderList == null) {
            orderList = getDefaultOrder();
        }
        HashMap<String, List<ProductionCatalogProductReport>> map = new HashMap<>();
        // Разбрасываем данные документа по группам
        String key;
        for (ProductionCatalogProductReport item : data.getRawReportData()) {
            key = item.getCategory().substring(0, 2) + "_" + item.getCategory();
            List<ProductionCatalogProductReport> list_ = map.get(key);
            if (list_ == null) {
                list_ = new ArrayList<>();
                map.put(key, list_);
            }
            list_.add(item);
        }

        List<ProductionCatalogCategoryReport> categoryList = new ArrayList<>();
        for (String order_ : orderList) {
            List<ProductionCatalogProductReport> list_ = map.get(order_);
            if (list_ != null) {
                ProductionCatalogCategoryReport category = prepareCategory(list_);
                category.setCategoryId(order_);
                categoryList.add(category);
            }
        }

        data.setCategoryList(categoryList);

        // Расчет цен
        for (ProductionCatalogCategoryReport category : data.getCategoryList()) {
            for (ProductCatalogArticleReport article : category.getData()) {
                int amount = 0;
                for (ProductionCatalogProductReport product : article.getData()) {
                    try {
                        double price = product.getPriceVale() / data.getPreset().getCurrencyRate();
                        double discount_ = (price * product.getDiscountValue() / 100);
                        double priceAll = price + discount_;
                        product.setPriceDiscountValue(priceAll);
                        amount += product.getAmount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                article.setAmount(amount);
            }
        }

        // ПОдготовка диапазонов
        for (ProductionCatalogCategoryReport category : data.getCategoryList()) {
            for (ProductCatalogArticleReport article : category.getData()) {
                prepareRanges(article);
            }
        }


        return data;
    }

    private void prepareRanges(ProductCatalogArticleReport article) {
        // ПОлучаем всевозможные учетные цены, так как они - ключ к группировке по диапазону
        Set<String> setPrice = new TreeSet<>();
        for (ProductionCatalogProductReport product : article.getData()) {
            setPrice.add(String.format("%.2f", product.getPriceDiscountValue()));
        }

        List<List<Integer>> listSizes = new ArrayList<>();

        // Итерируемся по диапазону цен, и отбираем размеры для данной группы
        String prices = "";
        for (String price : setPrice) {
            List<Integer> list = new ArrayList<>();
            listSizes.add(list);
            for (ProductionCatalogProductReport product : article.getData()) {
                if (price.equals(String.format("%.2f", (product.getPriceDiscountValue())))) {
                    list.add(product.getSize());
                }
            }
            prices += price + "_";
        }
        String sizes = "";
        for (List<Integer> list_ : listSizes) {
            Collections.sort(list_);
            sizes += list_.get(0) + " - " + list_.get(list_.size() - 1) + "_";
        }

        article.setSizeRange(sizes);
        article.setPriceRange(prices);
    }

    private ProductionCatalogCategoryReport prepareCategory(List<ProductionCatalogProductReport> list) {
        ProductionCatalogCategoryReport category = null;
        int article_ = 0;
        ProductCatalogArticleReport article = null;
        for (ProductionCatalogProductReport item : list) {
            if (category == null) {
                category = new ProductionCatalogCategoryReport();
            }
            if (item != null) {
                if (category.getCategoryId() == null) {
                    category.setCategoryId(item.getCategory().substring(0, 2) + "_" + item.getCategory());
                }

                if (article_ != item.getArticleId()) {
                    article = ProductCatalogArticleReport.getProductCatalogArticleReport(item);
                    article.setModelNumber(item.getModelNumber());
                    article.getData().add(item);
                    category.getData().add(article);
                } else {
                    article.getData().add(item);
                }
                article_ = item.getArticleId();
            }
        }
        return category;
    }

    private List<String> getDefaultOrder() {
        List<String> result = new ArrayList<>();
        result.add("42_421");
        result.add("42_422");
        result.add("42_423");
        result.add("42_425");
        result.add("42_426");

        result.add("41_411");
        result.add("41_412");
        result.add("41_413");
        result.add("41_415");
        result.add("41_416");

        result.add("43_431");
        result.add("43_432");
        result.add("43_433");
        result.add("43_435");
        result.add("43_436");
        return result;
    }

    private void createReport(MainController controller, ProductionCatalogReportPreset preset, int id) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showSaveDialog(controller.getMainForm()) == JFileChooser.APPROVE_OPTION) {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    ProductionCatalogManager manager = new ProductionCatalogManager(controller);
                    ProductCatalogReport report = manager.prepareDocumentReportData(id, null, preset);
                    if (report != null) {
                        switch (preset.getReportTYpe().getType()) {
                            case HTML_CATALOG:
                                ProductionCatalogHTMLGenerator generator = new ProductionCatalogHTMLGenerator(report);
                                generator.createPage(fc.getSelectedFile().getPath() + File.separator);
                                break;

                            case PLAIN_EXCEL:
                                new ProductionCatalogPlainExcel(report, fc.getSelectedFile().getPath() + File.separator);
                                break;
                        }
                    } else {
                        System.out.println("Документ КАТАЛОГ не найден в базе по ID:" + id);
                    }
                    return true;
                }
            }

            Task task = new Task("Получение каталога продукции...");
            task.executeTask();
        }
    }

    public void importToCatalogFromPlan(ProductionCatalog document, PostgresPlanningDocument plan) {
        PlanPDB pdb = new PlanPDB();
        // Получаем список планов
        // Выбыраем нужный ID
        int plan_id = plan.getId(); //Заглушка
        // Получаем категории продукции в плане

        // Пoлучаем список артикулов в плане
        Set<Integer> planArticles = pdb.getArticlesByPlanningDocument(plan_id);
        // ПОлучаем ассортимент плана и формируем мапу
        List<PostgresPlanningDocumentItem> planAssortment = pdb.getPlanningDocumentItemsByDocument(plan_id);
        HashMap<String, List<PostgresPlanningDocumentItem>> planAssortmentMap = new HashMap<>();
        for (PostgresPlanningDocumentItem item : planAssortment) {
            String key = String.valueOf(item.getArticleCode());
            List<PostgresPlanningDocumentItem> list_ = planAssortmentMap.get(key);
            if (list_ == null) {
                list_ = new ArrayList<>();
                planAssortmentMap.put(key, list_);
            }
            list_.add(item);
        }

        // Обходим ассортимент плана и из НСИ вытаскиваем данные в мапу
        for (Integer articleCode : planArticles) {
            planAssortment = planAssortmentMap.get(String.valueOf(articleCode));
            // Получим данные из классификатора для шифра артикула
            ClassifierModelParams article = ClassifierDAO.getClassifierItemByArticleCode(articleCode);
            if (article != null) {
                String key = String.valueOf(article.getModelNumber());
                // Получаем данные из каталога для данного артикула
                ProductionCatalogArticle catalogArticle = getArticleFromDocument(document.getId(), article.getId());
                if (catalogArticle == null) {
                    // Новый артикул
                    catalogArticle = new ProductionCatalogArticle();
                    catalogArticle.setDocumentId(document.getId());
                    catalogArticle.setArticleId(article.getId());

                    ImageItem image = imageService.getDefaultImageByModelNumber(key);
                    if (image != null) {
                        catalogArticle.setDefaultImage(image.getImage().getImageFile());
                    }

                    catalogArticle.setColors(getPresetColors(key));

                    catalogArticle.setMaterialComposition(getMaterialCompositionByArticleId(article.getId()).getAllCompositions().replace("_", ", "));
                }    // Синхронизируем ассортимент
                catalogArticle = synchroniseArticle(catalogArticle, article, planAssortment);
                saveArticle(catalogArticle, false);
            }
        }

    }

    public void importToCatalogFromERP(ProductionCatalog document, List<ERPRemainsEntity> remains, int limit) {
        // ФОрмируем мапу артикулов для каталога
        Set<String> articles = new HashSet<>();
        HashMap<String, ProductionCatalogArticle> map = new HashMap<>();
        for (ERPRemainsEntity entity : remains) {

            /// Временно к импорту принимаем только сортовые изделия
            if (!validItemGrade(entity)) {
                continue;
            }

            String key = entity.getArticleNumber();
            ProductionCatalogArticle article = map.get(key);
            if (article == null) {
                articles.add(key);
                article = new ProductionCatalogArticle();
                article.setArticleId(entity.getArticleId());
                article.setModelNumber(entity.getModelNumber());
                map.put(key, article);
            }

            if (!productExist(article.getProductList(), entity.getProductId())) {
                ProductionCatalogProduct item_ = new ProductionCatalogProduct();
                item_.setArticle(article);
                item_.setPriceValue(entity.getPrice());
                item_.setProductId(entity.getProductId());
                item_.setSize(entity.getSize());
                item_.setGrowth(entity.getGrowth());
                item_.setColors(entity.getNsiColorName());
                item_.setArticle(article);
                item_.setAmount(entity.getAmount());
                article.getProductList().add(item_);
            } else {
                checkColor(article.getProductList(), entity);
            }

        }

        // Обходим ассортимент плана и из НСИ вытаскиваем данные в мапу
        for (String articleCode : articles) {
            if (articleCode != null) {
                ProductionCatalogArticle article_ = map.get(articleCode);
                if (article_ != null) {
                    String key = String.valueOf(article_);
                    // Получаем данные из каталога для данного артикула
                    ProductionCatalogArticle catalogArticle = getArticleFromDocument(document.getId(), article_.getArticleId());
                    if (catalogArticle == null) {
                        // Новый артикул
                        catalogArticle = article_;
                        catalogArticle.setDocumentId(document.getId());

                        ImageItem image = imageService.getDefaultImageByModelNumber(String.valueOf(article_.getModelNumber()));
                        if (image != null) {
                            catalogArticle.setDefaultImage(image.getImage().getImageFile());
                        }
                        //catalogArticle.setColors(getPresetColors(String.valueOf(article_.getModelNumber())));
                        catalogArticle.setMaterialComposition(getMaterialCompositionByArticleId(article_.getArticleId()).getAllCompositions().replace("_", ", "));
                    } else {    // Синхронизируем ассортимент
                        for (ProductionCatalogProduct product : article_.getProductList()) {
                            if (!productExist(catalogArticle.getProductList(), product.getProductId())) {
                                catalogArticle.getProductList().add(product);
                                product.setArticle(catalogArticle);
                            }
                        }
                    }

                    //Синхронизация цветов
                    synchroniseColor(catalogArticle);
                    if (catalogArticle.getAmount() >= limit) {
                        saveArticle(catalogArticle, false);
                    }
                }
            }
        }

    }

    private void checkColor(List<ProductionCatalogProduct> productList, ERPRemainsEntity entity) {
        for (ProductionCatalogProduct product : productList) {
            if (product.getProductId() == entity.getProductId()) {
                Set<String> colorSet = new HashSet<>();
                if (product.getColors() != null) {
                    colorSet.add(entity.getNsiColorName());
                    String[] colors = product.getColors().split(",");
                    for (String s : colors) {
                        colorSet.add(s);
                    }

                    String colors_ = "";
                    for (String s : colorSet) {
                        colors_ += s + ",";
                    }

                    if (colors_.length() > 2) {
                        product.setColors(colors_.substring(0, colors_.length() - 1));
                    }

                    product.setAmount(product.getAmount() + entity.getAmount());

                    return;
                }
            }
        }
    }

    private void synchroniseColor(ProductionCatalogArticle catalogArticle) {
        Set<String> colorSet = new HashSet<>();
        int amount = 0;
        for (ProductionCatalogProduct product : catalogArticle.getProductList()) {
            if (product.getColors() != null) {
                String[] colors = product.getColors().split(",");
                for (String s : colors) {
                    colorSet.add(s);
                }
            }

            amount += product.getAmount();
        }
        catalogArticle.setAmount(amount);

        String colors_ = "";
        for (String s : colorSet) {
            colors_ += s + ",";
        }

        if (colors_.length() > 2) {
            catalogArticle.setColors(colors_.substring(0, colors_.length() - 1));
        }
    }

    private ProductionCatalogArticle synchroniseArticle(ProductionCatalogArticle catalogArticle, ClassifierModelParams classifierArticle, List<PostgresPlanningDocumentItem> planItems) {
        List<ProductionCatalogProduct> productList = catalogArticle.getProductList();
        for (PostgresPlanningDocumentItem item : planItems) {
            ClassifierItem product = getProductIdByPlaningItem(classifierArticle, item);
            if (product != null) {
                int productId = product.getId();
                if (!productExist(productList, productId)) {
                    ProductionCatalogProduct item_ = new ProductionCatalogProduct();
                    item_.setArticle(catalogArticle);
                    item_.setPriceValue(product.getPriceWholesale());
                    item_.setProductId(productId);
                    item_.setSize(product.getSize());
                    item_.setGrowth(product.getGrowth());
                    item_.setColors(catalogArticle.getColors());
                    productList.add(item_);
                }
            }
        }
        return catalogArticle;
    }

    private boolean productExist(List<ProductionCatalogProduct> assortment, int productId) {
        for (ProductionCatalogProduct product : assortment) {
            if (product.getProductId() == productId) {
                return true;
            }
        }
        return false;
    }

    private ClassifierItem getProductIdByPlaningItem(ClassifierModelParams classifierArticle, PostgresPlanningDocumentItem item) {
        Set<ClassifierItem> assortmentList = classifierArticle.getAssortmentList();
        for (ClassifierItem item_ : assortmentList) {
            if (item_.getItemGrade() == 1) {
                if (item_.getSize() == item.getSize() && item_.getGrowth() == item.getGrowth()) {
                    return item_;
                }
            }
        }
        System.out.println("Для артикула [" + classifierArticle.getArticleName() + "] Росторазмер [" + item.getSize() + "] [" + item.getGrowth() + "] не найден");
        return null;
    }

    private ClassifierArticleComposition getMaterialCompositionByArticleId(int articleId) {
        if (db == null) {
            db = new ClassifierJDBC();
        }
        return db.getMaterialCompositionByArticleId(articleId);
    }


    public boolean validItemGrade(ERPRemainsEntity item) {
        // System.err.println("артикул "+articleCode);
        String unit = item.getCategory().substring(0, 2);

        switch (unit) {
            case "43":
                if (item.getGrade() > 1) {
                    return false;
                } else {
                    return true;
                }
            default:
                if (item.getGrade() > 2) {
                    return false;
                } else {
                    return true;
                }
        }
    }
}


