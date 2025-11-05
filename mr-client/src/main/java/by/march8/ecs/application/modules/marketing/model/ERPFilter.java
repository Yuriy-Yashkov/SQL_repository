package by.march8.ecs.application.modules.marketing.model;

import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;

import java.util.List;

public class ERPFilter {
    private ClassifierNode node;
    private boolean gradeOnly;
    private String articles;
    private List<ModelSelectionItem> models;

    public ClassifierNode getNode() {
        return node;
    }

    public void setNode(ClassifierNode node) {
        this.node = node;
    }

    public String getArticles() {
        return articles;
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }

    public List<ModelSelectionItem> getModels() {
        return models;
    }

    public void setModels(List<ModelSelectionItem> models) {
        this.models = models;
    }

    public boolean isGradeOnly() {
        return gradeOnly;
    }

    public void setGradeOnly(boolean gradeOnly) {
        this.gradeOnly = gradeOnly;
    }
}
