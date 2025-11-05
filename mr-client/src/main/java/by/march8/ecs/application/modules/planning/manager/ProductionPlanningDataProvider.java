package by.march8.ecs.application.modules.planning.manager;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.application.modules.planning.model.ProductionPlanningBucket;
import by.march8.ecs.application.modules.planning.model.ProductionPlanningReport;
import by.march8.entities.classifier.EquipmentItem;
import by.march8.entities.planning.ProductionPlanningDetailView;
import by.march8.entities.planning.ProductionPlanningDocument;
import by.march8.entities.planning.ProductionPlanningItem;
import by.march8.entities.planning.ProductionPlanningView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Andy 07.12.2018 - 10:27.
 */
public class ProductionPlanningDataProvider {

    public static ProductionPlanningDataProvider instance = null;

    private ProductionPlanningDataProvider() {

    }

    public static ProductionPlanningDataProvider getInstance() {
        if (instance == null) {
            instance = new ProductionPlanningDataProvider();
        }

        return instance;
    }

    /**
     * Подготовка данных для отчет. Группировки по компонентам, сортировка по артикулам
     *
     * @param document документ планирования
     * @return документ планирования подготовленный для создания отчета
     */
    private ProductionPlanningReport prepareReport(ProductionPlanningDocument document) {
        ProductionPlanningReport result = new ProductionPlanningReport();
        result.setDocument(document);
        Set<ProductionPlanningItem> productionList = document.getProductionList();
        // Создаем отсортированный список артикулов для вывода на печать
        List<Integer> articleList = getArticleList(productionList);
        // Создадим мапу артикулов с пачками изделий
        HashMap<Integer, List<ProductionPlanningItem>> map = getArticleMap(productionList);

        List<List<ProductionPlanningBucket>> data = new ArrayList<>();

        if (articleList != null) {
            // Бежим по списку артикулов, вытаскиваем из мапы списки изделий
            // и формируем по ним пачки расхода сырья
            for (Integer key : articleList) {
                List<ProductionPlanningItem> list_ = map.get(key);
                if (list_ != null) {
                    List<ProductionPlanningBucket> bucketList = prepareData(list_);
                    if (bucketList != null) {
                        data.add(bucketList);
                    }
                }
            }

            result.setProductionData(data);
            return result;
        } else {
            return null;
        }
    }

    private HashMap<Integer, List<ProductionPlanningItem>> getArticleMap(Set<ProductionPlanningItem> list) {
        HashMap<Integer, List<ProductionPlanningItem>> result = new HashMap<>();
        for (ProductionPlanningItem item : list) {
            List<ProductionPlanningItem> list_ = result.get(item.getArticleId());
            if (list_ == null) {
                list_ = new ArrayList<>();
                result.put(item.getArticleId(), list_);
            }
            list_.add(item);
        }
        return result;
    }

    private List<Integer> getArticleList(Set<ProductionPlanningItem> list) {
        List<Integer> result = new ArrayList<>();
        int key = 0;
        for (ProductionPlanningItem item : list) {
            if (key != item.getArticleId()) {
                result.add(item.getArticleId());
                key = item.getArticleId();
            }
        }
        return result;
    }

    private List<ProductionPlanningBucket> prepareData(List<ProductionPlanningItem> list) {

        List<ProductionPlanningBucket> buckets = new ArrayList<>();

        for (ProductionPlanningItem item : list) {
            // Если айтем еще не используется в бакетах, создаем новый бакет
            if (!item.isUsing()) {
                // Передаем бакету состав Айтема
                ProductionPlanningBucket bucket = new ProductionPlanningBucket(item);
                // Поиск подходящих по составу изделий
                searchSimilar(bucket, list);
                buckets.add(bucket);
            }
        }

        return buckets;
    }

    private void searchSimilar(ProductionPlanningBucket bucket, List<ProductionPlanningItem> list) {
        for (ProductionPlanningItem item : list) {
            // Если айтем еще не используется в бакетах, создаем новый бакет
            if (!item.isUsing()) {
                if (bucket.addToBucket(item)) {
                    // System.out.println("Добавлен");
                }
            }
        }
    }

    public ProductionPlanningReport getReportData(ProductPlanningManager manager, ProductionPlanningView activeDocument, List<ProductionPlanningDetailView> data) {
        ProductionPlanningDocument document = manager.getProductionPlanningDocumentById(activeDocument.getId());
        if (document != null) {
            // ПОдготовим наименования изделий для отчета
            // Создадим мапу наименований

            HashMap<Integer, ProductionPlanningDetailView> map = getArticleNameMap(data);
            for (ProductionPlanningItem item : document.getProductionList()) {
                // ПО коду артикула получаем наименование изделия и заполняем
                ProductionPlanningDetailView view = map.get(item.getArticleId());
                if (view != null) {
                    item.setItemName(view.getItemName());
                    item.setArticleNumber(view.getArticleNumber());
                    item.setModelNumber(view.getModelNumber());
                }
            }

            HashMap<Integer, EquipmentItem> mapEquip = getEquipmentMap();
            for (ProductionPlanningItem item : document.getProductionList()) {
                // ПО коду артикула получаем наименование изделия и заполняем
                EquipmentItem equipmentItem = mapEquip.get(item.getEquipmentId());
                if (equipmentItem != null) {
                    item.setEquipmentName(equipmentItem.getName());
                }
            }

            return prepareReport(document);
        }
        System.out.println("Документ не найден");
        return null;
    }

    private HashMap<Integer, ProductionPlanningDetailView> getArticleNameMap(List<ProductionPlanningDetailView> data) {
        HashMap<Integer, ProductionPlanningDetailView> result = new HashMap<>();
        for (ProductionPlanningDetailView item : data) {
            result.put(item.getArticleId(), item);
        }
        return result;
    }

    private HashMap<Integer, EquipmentItem> getEquipmentMap() {
        HashMap<Integer, EquipmentItem> result = new HashMap<>();
        List<EquipmentItem> list = null;
        DaoFactory<EquipmentItem> factory = DaoFactory.getInstance();
        IGenericDao<EquipmentItem> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("type", 1));
        try {
            list = dao.getEntityListByNamedQuery(EquipmentItem.class, "EquipmentItem.findAllSockType", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (EquipmentItem item : list) {
            result.put(item.getId(), item);
        }

        return result;
    }

}
