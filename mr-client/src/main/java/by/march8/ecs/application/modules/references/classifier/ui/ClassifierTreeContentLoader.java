package by.march8.ecs.application.modules.references.classifier.ui;

import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;

public interface ClassifierTreeContentLoader {
    ClassifierTreeDataSource getContentDataSource();

    int[] getCodeIgnoreList();

    void updateForNode(ClassifierNode node);

}
