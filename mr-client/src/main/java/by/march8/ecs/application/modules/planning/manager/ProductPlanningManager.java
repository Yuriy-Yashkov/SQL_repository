package by.march8.ecs.application.modules.planning.manager;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.planning.model.ProductionEquipment;
import by.march8.ecs.application.modules.references.classifier.dao.ClassifierJDBC;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.classifier.ClassifierModelView;
import by.march8.entities.classifier.CompositionMaterialView;
import by.march8.entities.classifier.EquipmentItem;
import by.march8.entities.classifier.NSIMaterial;
import by.march8.entities.planning.PlanningItemComponent;
import by.march8.entities.planning.ProductionPlanningArticleView;
import by.march8.entities.planning.ProductionPlanningComposition;
import by.march8.entities.planning.ProductionPlanningDetailComponentView;
import by.march8.entities.planning.ProductionPlanningDocument;
import by.march8.entities.planning.ProductionPlanningItem;
import by.march8.entities.planning.ProductionPlanningView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Andy 28.11.2018 - 12:44.
 */
@SuppressWarnings("all")
public class ProductPlanningManager {

    public ProductPlanningManager() {

    }


    public boolean addNewArticle(ProductionPlanningView activeDocument, ClassifierModelView item) {
        ClassifierModelParams classifierItem = getClassifierItem(item.getId());

        // Получаем все его компоненты
        List<CompositionMaterialView> nsiCompositionList = getMaterialByArticleId(item);

        // Получаем документ плана
        ProductionPlanningDocument document = getProductionPlanningDocumentById(activeDocument.getId());

        // Сравниваем компоненты документа и классификатора для изделия
        compositionCompare(document, nsiCompositionList);

        // Проверяем, не добавлялся ли данный артикул в документ и добавляем росто-размеры артикула
        specificationCompare(document, classifierItem.getAssortmentList());

        // Сохраняем документ
        updateProductionPlanningDocument(document);

        return true;
    }

    private void specificationCompare(ProductionPlanningDocument document, Set<ClassifierItem> list_) {
        Set<ProductionPlanningItem> set_ = document.getProductionList();
        Set<ProductionPlanningComposition> compositionSet = document.getCompositionList();

        ClassifierJDBC db = new ClassifierJDBC();

        for (ClassifierItem item : list_) {
            if (item.getItemGrade() == 1) {
                if (!isProductExist(set_, item)) {
                    ProductionPlanningItem newItem = new ProductionPlanningItem();
                    ProductionEquipment equipment = db.getEquipmentBySDCode(item.getId());
                    newItem.setDocument(document);
                    newItem.setProductId(item.getId());
                    newItem.setArticleId(item.getModel().getId());
                    newItem.setSizePrint(item.getSizePrint());
                    newItem.setAmount(0);

                    if (equipment != null) {
                        newItem.setEquipmentId(equipment.getEquipmentId());
                        newItem.setPerformance(equipment.getPerformance());
                    }

                    set_.add(newItem);

                    // Добавляем компоненты для росто-размеров
                    materialCreator(compositionSet, newItem);
                }
            }
        }
    }

    private void materialCreator(Set<ProductionPlanningComposition> set_, ProductionPlanningItem item) {
        List<NSIMaterial> nsiMaterialList = getMaterialByProductId(item);
        Set<PlanningItemComponent> componentList = item.getComponentList();
        if (nsiMaterialList != null) {
            for (NSIMaterial material : nsiMaterialList) {
                ProductionPlanningComposition component = getMaterialByNSIMaterial(set_, material);
                if (component != null) {
                    PlanningItemComponent newItem = new PlanningItemComponent();
                    newItem.setProduct(item);
                    newItem.setComponent(component);
                    newItem.setRate(material.getRate());
                    newItem.setUseValue(0);
                    componentList.add(newItem);
                }
            }
        }
    }

    private ProductionPlanningComposition getMaterialByNSIMaterial(Set<ProductionPlanningComposition> set_, NSIMaterial nsiMaterial) {
        for (ProductionPlanningComposition item : set_) {
            if (item.getMaterialId() == nsiMaterial.getMaterialId()) {
                return item;
            }
        }
        return null;
    }

    private List<CompositionMaterialView> getMaterialByArticleId(ClassifierModelView item) {
        List<CompositionMaterialView> result = null;
        DaoFactory<CompositionMaterialView> factory = DaoFactory.getInstance();
        IGenericDaoGUI<CompositionMaterialView> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("productId", item.getId()));
        try {
            result = dao.getEntityListByNamedQueryGUI(CompositionMaterialView.class, "CompositionMaterialView.findByProductId", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<NSIMaterial> getMaterialByProductId(ProductionPlanningItem item) {
        List<NSIMaterial> result = null;

        DaoFactory<NSIMaterial> factory = DaoFactory.getInstance();
        IGenericDao<NSIMaterial> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("productId", item.getProductId()));

        try {
            result = dao.getEntityListByNamedQuery(NSIMaterial.class, "NSIMaterial.findByProductId", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isProductExist(Set<ProductionPlanningItem> set_, ClassifierItem product) {
        for (ProductionPlanningItem item : set_) {
            if (item.getProductId() == product.getId()) {
                return true;
            }
        }
        return false;
    }

    private void compositionCompare(ProductionPlanningDocument document, List<CompositionMaterialView> list_) {
        Set<ProductionPlanningComposition> set_ = document.getCompositionList();
        for (CompositionMaterialView item : list_) {
            if (!isMaterialExist(set_, item)) {
                ProductionPlanningComposition newItem = new ProductionPlanningComposition();
                newItem.setDocument(document);
                newItem.setMaterialId(item.getMaterialId());
                newItem.setMaterialType(item.getMaterialTypeId());
                newItem.setMaterialName(item.getMaterialName());
                set_.add(newItem);
            }
        }
    }

    private boolean isMaterialExist(Set<ProductionPlanningComposition> set_, CompositionMaterialView material) {
        for (ProductionPlanningComposition item : set_) {
            if (item.getMaterialId() == material.getMaterialId()) {
                return true;
            }
        }
        return false;
    }

    public void updateProductionPlanningDocument(ProductionPlanningDocument document) {
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDao dao = factory.getCommonDao();
        try {
            dao.updateEntity(document);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка сохранения документа по его ID [" + document.getId() + "]");
        }
    }

    public void updateProductionPlanningDocumentGUI(ProductionPlanningDocument document) {
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();
        // Обновляем документ в базе
        try {
            dao.updateEntityThread(document);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ProductionPlanningDocument saveProductionPlanningDocument(ProductionPlanningDocument document) {
        DaoFactory<ProductionPlanningDocument> factory = DaoFactory.getInstance();
        IGenericDao<ProductionPlanningDocument> dao = factory.getGenericDao();
        ProductionPlanningDocument result = null;
        try {
            result = dao.saveEntity(document);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка сохранения документа по его ID [" + document.getId() + "]");
        }
        return result;
    }

    public ProductionPlanningDocument getProductionPlanningDocumentById(int documentId) {
        DaoFactory<ProductionPlanningDocument> factory = DaoFactory.getInstance();
        IGenericDaoGUI<ProductionPlanningDocument> dao = factory.getGenericDao();
        ProductionPlanningDocument document = null;
        try {
            document = dao.getEntityByIdGUI(ProductionPlanningDocument.class, documentId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения документа по его ID [" + documentId + "]");
        }
        return document;
    }

    private ClassifierModelParams getClassifierItem(int id) {
        DaoFactory<ClassifierModelParams> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierModelParams> dao = factory.getGenericDao();
        ClassifierModelParams result = null;
        try {
            result = dao.getEntityById(ClassifierModelParams.class, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ProductionPlanningItem getProductionPlanningItemById(int itemId) {
        DaoFactory<ProductionPlanningItem> factory = DaoFactory.getInstance();
        IGenericDao<ProductionPlanningItem> dao = factory.getGenericDao();
        ProductionPlanningItem item = null;
        try {
            item = dao.getEntityById(ProductionPlanningItem.class, itemId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения изделия по его ID [" + itemId + "]");
        }
        return item;
    }

    public ProductionPlanningItem prepareProductionPlanningItem(ProductionPlanningItem item, ProductionPlanningArticleView view) {
        if (item == null && view == null) {
            return null;
        }
        if (item != null) {

            item.setAmount(view.getAmount());
            item.setPercent(view.getPercent());
            item.setPerformance(view.getPerformance());
            item.setEquipmentId(view.getEquipmentId());
            item.setEquipmentCount(view.getEquipmentCount());

            for (PlanningItemComponent component : item.getComponentList()) {
                if (component != null) {
                    if (prepareComponent(view.getComponentList(), component) == null) {
                        System.out.println("Ошибка обработки компонента с ID [" + component.getId() + "]");
                    }
                } else {
                    System.out.println("Ошибка обработки артикула с ID [" + item.getId() + "]");
                }
            }

            return item;
        }

        return null;
    }

    private PlanningItemComponent prepareComponent(List<ProductionPlanningDetailComponentView> componentList, PlanningItemComponent item) {
        for (ProductionPlanningDetailComponentView component : componentList) {
            if (component.getId() == item.getId()) {

                item.setRate(component.getRate());
                item.setUseValue(component.getUseValue());

                return item;
            }
        }

        return null;
    }

    public boolean saveProductionPlanningItem(ProductionPlanningItem product) {
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDao dao = factory.getCommonDao();
        try {
            dao.updateEntity(product);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка сохранения артикула с ID [" + product.getId() + "]");
            return false;
        }

        return true;
    }

    @SuppressWarnings("all")
    public void loadEquipments(JComboBox<EquipmentItem> comboBox) {
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

        comboBox.setModel(new DefaultComboBoxModel(list.toArray()));
        comboBox.setSelectedIndex(0);
    }

    public void calculateDocument(ProductionPlanningView activeDocument) {
        ProductionPlanningDocument document = getProductionPlanningDocumentById(activeDocument.getId());
        System.out.println(document.getId() + " - " + document.getCompositionList().size());
        // Очищаем расход компонента
        for (ProductionPlanningComposition component : document.getCompositionList()) {
            component.setUseValue(0);
        }

        // подготовка мапы компонентов
        for (ProductionPlanningItem item : document.getProductionList()) {
            if (item != null) {
                for (PlanningItemComponent component : item.getComponentList()) {
                    ProductionPlanningComposition component_ = component.getComponent();
                    component_.setUseValue(component_.getUseValue() + component.getUseValue());
                }
            }
        }

        updateProductionPlanningDocumentGUI(document);
    }
}
