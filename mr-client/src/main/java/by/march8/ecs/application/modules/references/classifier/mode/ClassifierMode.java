package by.march8.ecs.application.modules.references.classifier.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.classifier.ClassifierModelView;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Режим работы - классификатор
 * Created by Andy on 16.10.2015.
 */
public class ClassifierMode extends AbstractFunctionalMode {

    private RightEnum right;
    private ArrayList<ClassifierModelView> dataList;
    private ArrayList<ClassifierItem> detailList;


    private GridViewPort<ClassifierModelView> dataGridViewPort;
    private GridViewPort<ClassifierItem> detailGridViewPort;

    private JButton btnComposition;
    private EditingPane editingPane;

    private ClassifierTree tree;

    private String searchString;

    public ClassifierMode(final MainController mainController) {
        controller = mainController;
        modeName = "Классификатор продукции";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        //right = controller.getRight(modeName);
        right = RightEnum.WRITE;

        init();
        initEvents();

        dataGridViewPort.primaryInitialization();
        detailGridViewPort.primaryInitialization();

        updateContent();
        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {
        //frameViewPort.getFrameRegion().getToolBar().setVisible(true);
        UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();


        btnComposition = new JButton("");
        btnComposition.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/pie_20.png", "Сырьевой состав продукции"));
        btnComposition.setToolTipText("Сырьевой состав продукции");

        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);

        toolBar.add(btnComposition);

        // Инициализация гридов
        dataGridViewPort = new GridViewPort<>(ClassifierModelView.class, false);
        detailGridViewPort = new GridViewPort<>(ClassifierItem.class, false);

        // Получаем ссылку на модель данных грида
        dataList = dataGridViewPort.getDataModel();
        detailList = detailGridViewPort.getDataModel();

        final JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, dataGridViewPort, detailGridViewPort);
        splitPanel.setResizeWeight(0.5);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);


        tree = new ClassifierTree();
        JPanel pCatalog = new JPanel(new BorderLayout());
        pCatalog.add(tree, BorderLayout.CENTER);

        final JSplitPane spVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pCatalog, splitPanel);
        spVertical.setResizeWeight(0.2);
        spVertical.setOneTouchExpandable(true);
        spVertical.setContinuousLayout(true);

        frameViewPort.getFrameRegion().getCenterContentPanel().add(spVertical);
    }

    private void initEvents() {

        // двойной клик на строке
        dataGridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                if (right == RightEnum.WRITE) {
                    editRecord();
                }
            }

            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                ClassifierModelView selectedItem = (ClassifierModelView) object;
                if (selectedItem != null) {
                    updateDetail((selectedItem.getId()));
                }
            }
        });

        btnComposition.addActionListener(a -> {
            ClassifierModelView selectedItem = (ClassifierModelView) dataGridViewPort.getSelectedItem();
            if (selectedItem != null) {
                new CompositionMaterialMode(controller, getDetails(selectedItem.getId()));
            }
        });

        tree.addTreeListener(a -> {
            if (a != null) {
                searchString = String.valueOf(a.getCode());
                getCustomUpdate(a);
            }
        });
    }

    @Override
    public void addRecord() {

    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateContent() {
        DaoFactory<ClassifierModelView> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierModelView> dao = factory.getGenericDao();
        try {
            dataList.clear();
            dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findAll", null));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        frameViewPort.updateContent();
        dataGridViewPort.updateViewPort();
    }

    @SuppressWarnings("unchecked")
    private void updateDetail(int id) {
        ClassifierModelParams item = getDetails(id);
        if (item != null) {
            detailList.clear();
            detailList.addAll(item.getAssortmentList());
        }
        detailGridViewPort.updateViewPort();
    }

    private ClassifierModelParams getDetails(int id) {
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

    private void getCustomUpdate(ClassifierNode node) {
        DaoFactory<ClassifierModelView> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierModelView> dao = factory.getGenericDao();
        try {
            dataList.clear();
            List<QueryProperty> criteria = new ArrayList<>();
            switch (node.getType()) {
                case ROOT:
                    dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findAll", null));
                    break;
                case GROUP:
                    criteria.add(new QueryProperty("article", node.getCode() + "%"));
                    dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByGroup", criteria));
                    break;
                case CATEGORY:
                    criteria.add(new QueryProperty("article", node.getCode() + "%"));
                    dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByGroup", criteria));
                    break;
                case ASSORTMENT:
                    criteria.add(new QueryProperty("article", node.getCode() + "%"));
                    criteria.add(new QueryProperty("name", node.getName()));
                    dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByGroupAndAssortment", criteria));
                    break;
                case MODEL:
                    criteria.add(new QueryProperty("model", Integer.valueOf(node.getName())));
                    dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByGroupAndModel", criteria));
                    break;
                case ARTICLE:
                    criteria.add(new QueryProperty("article", node.getName()));
                    dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByArticleName", criteria));
                    break;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        dataGridViewPort.updateViewPort();
    }

}
