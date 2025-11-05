package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.dao.ERPRemainsTreeJDBC;
import by.march8.ecs.application.modules.marketing.model.ERPRemainsCellRenderer;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNodeType;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTreeContentLoader;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTreeDataSource;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.ERPRemainsEntity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ERPRemainsMode extends AbstractFunctionalMode implements ClassifierTreeContentLoader {

    private MainController controller;
    private ERPRemainsTreeJDBC db;

    private GridViewPort<ERPRemainsEntity> gvRemains;
    private List<ERPRemainsEntity> dataList;
    private UCToolBar toolBar;
    private ClassifierTree tree;

    private JSplitPane spVertical;
    private JToggleButton btnShowTree;
    private JPanel pRemains;
    private JButton btnImport;

    private ClassifierNode lastNode;
    private JButton btnFilter;

    public ERPRemainsMode(MainController mainController) {
        controller = mainController;

        modeName = "Остатки продукции ОАО \"8 Марта\" по данным 1C ERP по состоянию на " + DateUtils.getNormalDateFormat(new Date());
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        init();
        initEvents();
        // updateContent();

        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {
        db = new ERPRemainsTreeJDBC();

        gvRemains = new GridViewPort<>(ERPRemainsEntity.class);
        dataList = gvRemains.getDataModel();

        gvRemains.setCustomCellRender(new ERPRemainsCellRenderer());

        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);

        toolBar.registerEvents(this);

        toolBar.getBtnViewItem().setVisible(true);

        tree = new ClassifierTree(this);
        tree.setExpandLimit(ClassifierNodeType.MODEL);

        pRemains = new JPanel(new BorderLayout());
        pRemains.add(tree, BorderLayout.CENTER);

        spVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pRemains, gvRemains);
        spVertical.setResizeWeight(0.2);
        spVertical.setOneTouchExpandable(true);
        spVertical.setContinuousLayout(true);

        btnImport = new JButton();
        btnImport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/import_24.png", "Импортировать"));
        btnImport.setToolTipText("Ипортированть данные в каталог");

        btnShowTree = new JToggleButton();
        btnShowTree.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/tree_24.png", ""));
        btnShowTree.setToolTipText("Дерево продукции");

        btnFilter = new JButton();
        btnFilter.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/filter.png", ""));
        btnFilter.setToolTipText("Фильтр");

        toolBar.addSeparator();
        toolBar.add(btnImport);
        toolBar.addSeparator();
        toolBar.add(btnShowTree);
        toolBar.add(btnFilter);

        frameViewPort.getFrameRegion().getCenterContentPanel().add(spVertical);
        //pRemains.setVisible(false);
    }

    private void initEvents() {
        tree.addTreeListener(a -> {
            if (a != null) {
                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        updateForNode(a);
                        return true;
                    }
                }
                Task task = new Task("Получение остатков продукции ...");
                task.executeTask();
            }
        });

        btnShowTree.addActionListener(a -> {
            if (btnShowTree.isSelected()) {
                pRemains.setVisible(true);
                spVertical.resetToPreferredSizes();
            } else {
                pRemains.setVisible(false);
            }
        });

        btnFilter.addActionListener(a -> {
            ERPRemainsFilterDialog filterDialog = new ERPRemainsFilterDialog(controller, db);
            filterDialog.selectRemains();
        });
    }

    @Override
    public ClassifierTreeDataSource getContentDataSource() {
        return db;
    }

    @Override
    public int[] getCodeIgnoreList() {
        return new int[0];
    }

    @Override
    public void updateForNode(ClassifierNode node) {


        if (lastNode != null) {
            if (lastNode.getCode() == node.getCode()) {
                return;
            } else {
                lastNode = node;
            }
        }

        DaoFactory<ERPRemainsEntity> factory = DaoFactory.getInstance();
        IGenericDao<ERPRemainsEntity> dao = factory.getGenericDao();
        try {
            dataList.clear();
            List<QueryProperty> criteria = new ArrayList<>();
            switch (node.getType()) {
                case ROOT:
                    dataList.addAll(dao.getEntityListByNamedQuery(ERPRemainsEntity.class, "ERPRemainsEntity.findAll", null));
                    break;
                case GROUP:
                    criteria.add(new QueryProperty("category", node.getCode() + "%"));
                    dataList.addAll(dao.getEntityListByNamedQuery(ERPRemainsEntity.class, "ERPRemainsEntity.findByGroup", criteria));
                    break;
                case CATEGORY:
                    criteria.add(new QueryProperty("category", node.getCode() + "%"));
                    dataList.addAll(dao.getEntityListByNamedQuery(ERPRemainsEntity.class, "ERPRemainsEntity.findByCategory", criteria));
                    break;
                case ASSORTMENT:
                    criteria.add(new QueryProperty("category", node.getCode() + "%"));
                    criteria.add(new QueryProperty("name", node.getName()));
                    dataList.addAll(dao.getEntityListByNamedQuery(ERPRemainsEntity.class, "ERPRemainsEntity.findByCategoryAndAssortment", criteria));
                    break;
                case MODEL:
                    criteria.add(new QueryProperty("model", Integer.valueOf(node.getName())));
                    dataList.addAll(dao.getEntityListByNamedQuery(ERPRemainsEntity.class, "ERPRemainsEntity.findByModel", criteria));
                    break;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        gvRemains.updateViewPort();
    }

    @Override
    public void updateContent() {

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
}
