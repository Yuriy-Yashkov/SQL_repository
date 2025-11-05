package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImageLabel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.marketing.dao.ERPRemainsTreeJDBC;
import by.march8.ecs.application.modules.marketing.dao.ProductionCatalogTreeJDBC;
import by.march8.ecs.application.modules.marketing.editor.ProductionCatalogArticleEditor;
import by.march8.ecs.application.modules.marketing.manager.ProductionCatalogManager;
import by.march8.ecs.application.modules.marketing.model.PostgresPlanningDocument;
import by.march8.ecs.application.modules.marketing.model.ProductionCatalogArticleCellRenderer;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNodeType;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTreeContentLoader;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTreeDataSource;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.services.images.ColorImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import by.march8.entities.classifier.ClassifierModelView;
import by.march8.entities.product.ProductionCatalog;
import by.march8.entities.product.ProductionCatalogArticle;
import by.march8.entities.product.ProductionCatalogArticleEntity;
import by.march8.entities.product.ProductionCatalogArticleView;
import by.march8.entities.warehouse.ERPRemainsEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 21.01.2019 - 9:06.
 */
public class ProductionCatalogArticleMode extends AbstractFunctionalMode implements ClassifierTreeContentLoader {

    private FrameViewPort vpParent;

    private ProductionCatalog document;
    private GridViewPort<ProductionCatalogArticleView> gvArticle;
    private List<ProductionCatalogArticleView> dataList;
    private UCToolBar toolBar;
    private ClassifierPickMode classifier;
    private ClassifierTree tree;


    private ProductionCatalogManager manager;
    private ClassifierNode selectedProductNode;

    private EditingPane<ProductionCatalogArticleView, ProductionCatalogArticleEntity> editingPane;
    private ColorImageService imageService;
    private JButton btnExportDocument;
    private JPanel pCatalog;
    private JSplitPane spVertical;
    private JToggleButton btnShowTree;
    private JButton btnImport;
    private JPopupMenu popupMenuTools;
    private JMenuItem miRemainsFile;
    private ClassifierTreeDataSource db;
    private String[] articleWhiteList = new String[]{"8С", "9С"};
    private JMenuItem miProductionPlanningFile;

    public ProductionCatalogArticleMode(FrameViewPort parentViewPort, ProductionCatalog document) {
        vpParent = parentViewPort;
        this.document = document;
        controller = vpParent.getController();
        modeName = "Каталог продукции ОАО \"8 Марта\" № " + document.getDocumentNumber() + " [" + document.getNote() + "]";
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        init();
        initEvents();
        updateContent();

        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {
        db = new ProductionCatalogTreeJDBC(document);

        gvArticle = new GridViewPort<ProductionCatalogArticleView>(ProductionCatalogArticleView.class);
        dataList = gvArticle.getDataModel();

        gvArticle.setCustomCellRender(new ProductionCatalogArticleCellRenderer());

        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);

        toolBar.registerEvents(this);

        toolBar.getBtnViewItem().setVisible(true);

        tree = new ClassifierTree(this);
        tree.setExpandLimit(ClassifierNodeType.MODEL);

        pCatalog = new JPanel(new BorderLayout());
        pCatalog.add(tree, BorderLayout.CENTER);

        spVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pCatalog, gvArticle);
        spVertical.setResizeWeight(0.2);
        spVertical.setOneTouchExpandable(true);
        spVertical.setContinuousLayout(true);

        btnImport = new JButton();
        btnImport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/import_24.png", "Импортировать"));
        btnImport.setToolTipText("Ипортированть данные в каталог");

        btnExportDocument = new JButton();
        btnExportDocument.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/save_24.png", "Обновить данные"));
        btnExportDocument.setToolTipText("Выгрузить каталог");

        btnShowTree = new JToggleButton();
        btnShowTree.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/tree_24.png", ""));
        btnShowTree.setToolTipText("Дерево продукции");
        toolBar.addSeparator();
        toolBar.add(btnImport);
        toolBar.addSeparator();
        toolBar.add(btnShowTree);
        toolBar.addSeparator();
        toolBar.add(btnExportDocument);

        frameViewPort.getFrameRegion().getCenterContentPanel().add(spVertical);

        classifier = new ClassifierPickMode(controller, ClassifierType.CUSTOM);
        manager = new ProductionCatalogManager(controller);
        editingPane = new ProductionCatalogArticleEditor(frameViewPort, manager);
        imageService = ModelImageServiceDB.getInstance();
        pCatalog.setVisible(false);

        popupMenuTools = new JPopupMenu();

        miRemainsFile = new JMenuItem("Из остатков 1C");
        miProductionPlanningFile = new JMenuItem("Из плана производства");
        popupMenuTools.add(miProductionPlanningFile);
        popupMenuTools.addSeparator();
        popupMenuTools.add(miRemainsFile);

    }

    private void initEvents() {
        gvArticle.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {

            }
        });

        tree.addTreeListener(a -> {
            if (a != null) {
                updateForNode(a);
            }
        });

        btnExportDocument.addActionListener(a -> {
            if (document != null) {
                manager.prepareReportDialog(document.getId());
            }
        });

        btnShowTree.addActionListener(a -> {
            if (btnShowTree.isSelected()) {
                pCatalog.setVisible(true);
                spVertical.resetToPreferredSizes();
            } else {
                pCatalog.setVisible(false);
            }
        });

        btnImport.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenuTools.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });

        miProductionPlanningFile.addActionListener(a -> {
            PlanningDocumentSelectorDialog dialog = new PlanningDocumentSelectorDialog(controller);
            PostgresPlanningDocument item = dialog.selectPlanningDocument();
            if (item != null) {
                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        manager.importToCatalogFromPlan(document, item);
                        updateContent();
                        return true;
                    }
                }

                Task task = new Task("Импортирование из плана ...");
                task.executeTask();
            }
        });

        miRemainsFile.addActionListener(a -> {
            ERPRemainsFilterDialog filterDialog = new ERPRemainsFilterDialog(controller, new ERPRemainsTreeJDBC());
            List<ERPRemainsEntity> remains = filterDialog.selectRemains();
            //System.out.println("REMAINS - -------------- "+remains.size());
            if (remains != null) {
                int limit = filterDialog.getAmountLimit();
                //manager.importToCatalogFromERP(document, remains);
                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        manager.importToCatalogFromERP(document, remains, limit);
                        tree.prepareDefaultTree();
                        updateContent();
                        return true;
                    }
                }

                Task task = new Task("Импортирование остатков 1С ERP...");
                task.executeTask();
            }
        });
    }

    private void viewDetail(ProductionCatalogArticleView item) {
        ProductionCatalogProductMode productMode = new ProductionCatalogProductMode(frameViewPort, item);
        if (productMode.showMode()) {
            updateContent();
        }
    }

    @Override
    public void updateContent() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<ProductionCatalogArticleView> factory = DaoFactory.getInstance();
                IGenericDao<ProductionCatalogArticleView> dao = factory.getGenericDao();
                List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("document", document.getId()));
                List<ProductionCatalogArticleView> list = null;
                dataList.clear();
                try {
                    list = dao.getEntityListByNamedQuery(ProductionCatalogArticleView.class, "ProductionCatalogArticleView.findByDocumentId", criteria);
                    dataList.addAll(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                prepareView(dataList);

                gvArticle.updateViewPort();

                resizeGridRows();
                return true;
            }
        }

        Task task = new Task("Получение каталога продукции...");
        task.executeTask();
    }

    private void prepareView(List<ProductionCatalogArticleView> dataList) {
        for (ProductionCatalogArticleView item : dataList) {
            if (item != null) {
                prepareColors(item);
                item.setItemName(ItemNameReplacer.transform(item.getItemName()));
                item.prepareComposition();
                item.preparePrices();
                item.prepareSizes();
                UCImageLabel label = (UCImageLabel) item.getImage();
                if (label == null) {
                    item.setImage(new UCImageLabel());
                    label = (UCImageLabel) item.getImage();
                }
                try {
                    ImageItem image = imageService.getImageByModelNumberAndImageName(String.valueOf(item.getModelNumber()), item.getDefaultImage());
                    if (image != null) {
                        label.setImageFile(image.getPreviewImageFile());
                        if (item.getLineHeight() < 130) {
                            item.setLineHeight(130);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void addRecord() {
        //Белый список для артикулов
        classifier.setArticleWhiteList(document.getArticles().split(";"));
        // ПРедустановка ноды в дереве
        classifier.presetTreeNode(selectedProductNode);
        // Скрываем панель росторазмеров
        classifier.setProductTableVisible(false);
        ClassifierModelView item = classifier.selectArticle(null);
        if (item != null) {
            ProductionCatalogArticle addedItem = manager.addArticleManual(document, item);
            if (addedItem != null) {
                // Если размеры выбраны, переходим к заполнению других параметров модели
                BaseEditorDialog editor = new BaseEditorDialog(controller,
                        RecordOperationType.EDIT);
                ProductionCatalogArticleView selectedItem = manager.getProductionCatalogArticleViewById(addedItem.getId());
                if (selectedItem != null) {
                    editingPane.updateEditorContent(selectedItem);
                    editor.setParentTitle("Редактирование данных изделия");
                    ProductionCatalogArticleEntity editableItem = manager.getProductionCatalogArticleEntityById(addedItem.getId());
                    if (editableItem != null) {
                        editingPane.setSourceEntity(editableItem);
                        editor.setEditorPane(editingPane);
                        if (editor.showModal()) {
                            try {
                                final DaoFactory factory = DaoFactory.getInstance();
                                final ICommonDao dao = factory.getCommonDao();
                                Object o = editingPane.getSourceEntity();
                                dao.updateEntity(o);
                                gvArticle.setUpdatedObject(o);
                            } catch (final SQLException e) {
                                e.printStackTrace();
                            }
                            updateContent();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void editRecord() {
        ProductionCatalogArticleView selectedItem = gvArticle.getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        editingPane.updateEditorContent(gvArticle.getSelectedItem());
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Редактирование данных изделия");

        ProductionCatalogArticleEntity entity = manager.getProductionCatalogArticleEntityById(selectedItem.getId());
        if (entity != null) {
            editingPane.setSourceEntity(entity);
            editor.setEditorPane(editingPane);
            if (editor.showModal()) {
                try {
                    final DaoFactory factory = DaoFactory.getInstance();
                    final ICommonDao dao = factory.getCommonDao();
                    Object o = editingPane.getSourceEntity();
                    dao.updateEntity(o);
                    gvArticle.setUpdatedObject(o);
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
                updateContent();
            }
        }
    }

    @Override
    public void deleteRecord() {
        ProductionCatalogArticleView selectedItem = gvArticle.getSelectedItem();
        if (selectedItem != null) {
            final int answer = Dialogs.showDeleteDialog(selectedItem.getArticleInformation());
            if (answer == 0) {
                try {
                    DaoFactory factory = DaoFactory.getInstance();
                    ICommonDao dao = factory.getCommonDao();
                    dao.deleteEntity(ProductionCatalogArticle.class, selectedItem.getId());
                    updateContent();
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка удаления записи");
                }
            }
        }
    }

    @Override
    public void viewRecord() {
        viewDetail(gvArticle.getSelectedItem());
    }

    @Override
    public ClassifierTreeDataSource getContentDataSource() {
        return db;
    }

    @Override
    public int[] getCodeIgnoreList() {
        return new int[]{45, 47, 48};
    }

    @Override
    public void updateForNode(ClassifierNode node) {
        DaoFactory<ProductionCatalogArticleView> factory = DaoFactory.getInstance();
        IGenericDao<ProductionCatalogArticleView> dao = factory.getGenericDao();
        try {
            dataList.clear();
            List<QueryProperty> criteria = new ArrayList<>();
            switch (node.getType()) {
                case ROOT:
                    criteria.add(new QueryProperty("document", document.getId()));
                    dataList.addAll(dao.getEntityListByNamedQuery(ProductionCatalogArticleView.class, "ProductionCatalogArticleView.findAllByDocumentId", criteria));
                    break;
                case GROUP:
                    criteria.add(new QueryProperty("category", node.getCode() + "%"));
                    criteria.add(new QueryProperty("document", document.getId()));
                    dataList.addAll(dao.getEntityListByNamedQuery(ProductionCatalogArticleView.class, "ProductionCatalogArticleView.findByDocumentIdAndGroup", criteria));
                    break;
                case CATEGORY:
                    criteria.add(new QueryProperty("category", node.getCode() + "%"));
                    criteria.add(new QueryProperty("document", document.getId()));
                    dataList.addAll(dao.getEntityListByNamedQuery(ProductionCatalogArticleView.class, "ProductionCatalogArticleView.findByDocumentIdAndCategory", criteria));
                    break;
                case ASSORTMENT:
                    criteria.add(new QueryProperty("category", node.getCode() + "%"));
                    criteria.add(new QueryProperty("name", node.getName()));
                    criteria.add(new QueryProperty("document", document.getId()));
                    dataList.addAll(dao.getEntityListByNamedQuery(ProductionCatalogArticleView.class, "ProductionCatalogArticleView.findByCategoryAndAssortment", criteria));
                    break;
                case MODEL:
                    criteria.add(new QueryProperty("document", document.getId()));
                    criteria.add(new QueryProperty("model", Integer.valueOf(node.getName())));
                    dataList.addAll(dao.getEntityListByNamedQuery(ProductionCatalogArticleView.class, "ProductionCatalogArticleView.findByModel", criteria));
                    break;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        prepareView(dataList);
        selectedProductNode = node;
        gvArticle.updateViewPort();
        resizeGridRows();
    }

    private void resizeGridRows() {
        for (ProductionCatalogArticleView item : dataList) {
            if (item != null) {
                gvArticle.setRowHeight(item, item.getLineHeight());
            }
        }
    }

    private void prepareColors(ProductionCatalogArticleView item_) {
        if (item_.getColors() != null) {
            String[] colors_ = item_.getColors().split(",");
            item_.setLineHeight(colors_.length * 16);
            StringBuilder c_ = new StringBuilder();
            for (String c : colors_) {
                ColorTextItem item = ColorPresetHelper.getColorByName(c);
                c_.append("<p><font color=\"").append(ColorPresetHelper.getHTMLColorString(item.getColor())).append("\">").append(c).append("</font>");
            }
            item_.setColorToHTML("<html>" + c_ + "</html>");
        } else {
            item_.setColorToHTML("");
        }
    }
}
