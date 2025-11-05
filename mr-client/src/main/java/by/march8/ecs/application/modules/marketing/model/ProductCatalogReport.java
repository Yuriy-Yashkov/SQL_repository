package by.march8.ecs.application.modules.marketing.model;

import by.march8.entities.product.ProductionCatalog;
import by.march8.entities.product.ProductionCatalogProductReport;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

public class ProductCatalogReport {
    private ProductionCatalog document;
    private List<ProductionCatalogCategoryReport> categoryList;
    private ProductionCatalogReportPreset preset;

    @JsonIgnore
    private List<ProductionCatalogProductReport> rawReportData;

    public ProductionCatalog getDocument() {
        return document;
    }

    public void setDocument(ProductionCatalog document) {
        this.document = document;
    }

    public List<ProductionCatalogCategoryReport> getCategoryList() {
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        return categoryList;
    }

    public void setCategoryList(List<ProductionCatalogCategoryReport> categoryList) {
        this.categoryList = categoryList;
    }

    @Transient
    public List<ProductionCatalogProductReport> getRawReportData() {
        if (rawReportData == null) {
            rawReportData = new ArrayList<>();
        }
        return rawReportData;
    }

    @Transient
    public void setRawReportData(List<ProductionCatalogProductReport> rawReportData) {
        this.rawReportData = rawReportData;
    }

    public ProductionCatalogReportPreset getPreset() {
        return preset;
    }

    public void setPreset(ProductionCatalogReportPreset preset) {
        this.preset = preset;
    }
}
