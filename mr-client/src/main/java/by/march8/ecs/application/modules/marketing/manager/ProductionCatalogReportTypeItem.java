package by.march8.ecs.application.modules.marketing.manager;

import by.march8.ecs.application.modules.marketing.reports.ProductionCatalogReportType;

public class ProductionCatalogReportTypeItem {
    private int id;
    private String name;
    private ProductionCatalogReportType type;

    public ProductionCatalogReportTypeItem(int id, String name, ProductionCatalogReportType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductionCatalogReportType getType() {
        return type;
    }

    public void setType(ProductionCatalogReportType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getName();
    }
}
