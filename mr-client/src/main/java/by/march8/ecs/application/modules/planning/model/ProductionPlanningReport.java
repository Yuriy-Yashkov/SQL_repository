package by.march8.ecs.application.modules.planning.model;

import by.march8.entities.planning.ProductionPlanningDocument;

import java.util.List;

/**
 * @author Andy 07.12.2018 - 9:58.
 */
public class ProductionPlanningReport {

    private ProductionPlanningDocument document;
    private List<List<ProductionPlanningBucket>> productionData;

    public ProductionPlanningDocument getDocument() {
        return document;
    }

    public void setDocument(ProductionPlanningDocument document) {
        this.document = document;
    }

    public List<List<ProductionPlanningBucket>> getProductionData() {
        return productionData;
    }

    public void setProductionData(List<List<ProductionPlanningBucket>> productionData) {
        this.productionData = productionData;
    }
}
