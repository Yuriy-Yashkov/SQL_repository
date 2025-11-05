package by.march8.ecs.application.modules.general.forms;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.dao.ClassifierDAO;
import by.march8.entities.classifier.ClassifierArticleComposition;

import java.awt.*;
import java.util.List;

public class ClassifierCompositionSelectorDialog extends BasePickDialog {
    private GridViewPort<ClassifierArticleComposition> gvItem;
    private List<ClassifierArticleComposition> data;

    public ClassifierCompositionSelectorDialog(MainController controller) {
        super(controller);
        setTitle("Выберите из списка сырьевой состав изделия");

        init();
        initEvents();
    }

    private void initEvents() {
        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                return gvItem.getSelectedItem() != null;
            }

            @Override
            public boolean canCancel() {
                return true;
            }
        });
    }

    private void init() {
        setFrameSize(new Dimension(400, 450));
        gvItem = new GridViewPort<>(ClassifierArticleComposition.class, false);
        data = gvItem.getDataModel();

        getToolBar().setVisible(false);
        getCenterContentPanel().add(gvItem);
    }

    public ClassifierArticleComposition selectComposition(int model) {
        data.clear();
        List<ClassifierArticleComposition> list = ClassifierDAO.getClassifierArticleCompositionItem(model);
        if (list != null) {
            data.addAll(list);
        }
        gvItem.updateViewPort();

        if (showModalFrame()) {
            return gvItem.getSelectedItem();
        }
        return null;
    }
}