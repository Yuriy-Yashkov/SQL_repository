package by.march8.ecs.application.modules.references.classifier.ui;

import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;

import java.util.List;

public interface ClassifierTreeDataSource {
    List<ClassifierNode> getGroupNodes();

    List<ClassifierNode> getCategoryNodesByGroupId(ClassifierNode node);

    List<ClassifierNode> getAssortmentNodesByCategoryId(ClassifierNode node);

    List<ClassifierNode> getModelNodesByAssortmentName(ClassifierNode node);

    List<ClassifierNode> getArticleNodesByProductNumber(ClassifierNode node);

    List<ClassifierNode> searchModel(String text);

    List<ClassifierNode> searchArticle(String text);
}
