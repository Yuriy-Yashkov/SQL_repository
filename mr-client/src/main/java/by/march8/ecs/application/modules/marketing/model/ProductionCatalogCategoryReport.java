package by.march8.ecs.application.modules.marketing.model;

import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductionCatalogCategoryReport implements Serializable {
    private String categoryId;
    private String categoryName;
    private String categorySequence;
    private List<ProductCatalogArticleReport> data;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryAsString() {
        String[] categories = categoryId.split("_");
        return ClassifierTree.getNameAssortmentByArticleSegment(Integer.valueOf(categories[0])) + "_"
                + ClassifierTree.getNameAssortmentByArticleSegment(Integer.valueOf(categories[1]));
    }

    public List<ProductCatalogArticleReport> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }

    public void setData(List<ProductCatalogArticleReport> data) {
        this.data = data;
    }

    public String getCategorySequence() {
        return categorySequence;
    }

    public void setCategorySequence(String categorySequence) {
        this.categorySequence = categorySequence;
    }
}
