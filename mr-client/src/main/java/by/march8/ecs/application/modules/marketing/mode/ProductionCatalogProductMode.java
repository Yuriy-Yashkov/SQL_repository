package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.marketing.editor.ProductInformationPane;
import by.march8.ecs.application.modules.marketing.editor.ProductionCatalogProductEditor;
import by.march8.ecs.application.modules.marketing.manager.ProductionCatalogManager;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.product.ProductionCatalogArticleView;
import by.march8.entities.product.ProductionCatalogProductEntity;
import by.march8.entities.product.ProductionCatalogProductView;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 21.01.2019 - 13:21.
 */
public class ProductionCatalogProductMode extends AbstractFunctionalMode {


    private FrameViewPort vpParent;

    private ProductionCatalogArticleView article;
    private GridViewPort<ProductionCatalogProductView> gvProduct;
    private List<ProductionCatalogProductView> dataList;
    private UCToolBar toolBar;
    private ClassifierPickMode classifier;

    private ProductionCatalogManager manager;

    private EditingPane editingPane;
    private boolean needToUpdate = false;

    public ProductionCatalogProductMode(FrameViewPort parentViewPort, ProductionCatalogArticleView article) {
        vpParent = parentViewPort;
        this.article = article;
        controller = vpParent.getController();
        modeName = "Росто-размеры для изделия  " + article.getItemName() + " [" + article.getArticleNumber() + "]";
        frameViewPort = new FrameViewPort(controller, MarchWindowType.DIALOG);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        frameViewPort.getFrameControl().setFrameSize(new Dimension(580, 600));

        init();
        initEvents();
        updateContent();

        //frameViewPort.getFrameControl().showFrame();
    }

    private void init() {
        gvProduct = new GridViewPort<>(ProductionCatalogProductView.class);
        dataList = gvProduct.getDataModel();
        frameViewPort.setGridViewPort(gvProduct);

        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);

        toolBar.registerEvents(this);

        ProductInformationPane pInformation = new ProductInformationPane();
        pInformation.setPanelFont(new Font(toolBar.getFont().getName(), Font.BOLD, 14));
        pInformation.setItemName(ItemNameReplacer.transform(article.getItemName()));
        pInformation.setArticleNumber(article.getArticleNumber());
        pInformation.setModelNumber(String.valueOf(article.getModelNumber()));

        frameViewPort.getFrameRegion().getTopContentPanel().add(pInformation);
        classifier = new ClassifierPickMode(controller, ClassifierType.CUSTOM);
        manager = new ProductionCatalogManager(controller);
        editingPane = new ProductionCatalogProductEditor(frameViewPort, article);

        frameViewPort.getButtonControl().getButtonPanel().setLayout(new FlowLayout(FlowLayout.RIGHT));
        frameViewPort.getButtonControl().getOkButton().setVisible(false);
        frameViewPort.getButtonControl().getCancelButton().setText("Закрыть");
    }

    private void initEvents() {

    }

    @Override
    public void updateContent() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<ProductionCatalogProductView> factory = DaoFactory.getInstance();
                IGenericDao<ProductionCatalogProductView> dao = factory.getGenericDao();
                List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("reference", article.getId()));
                dataList.clear();
                try {
                    dataList.addAll(dao.getEntityListByNamedQuery(ProductionCatalogProductView.class, "ProductionCatalogProductView.findByArticleReference", criteria));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                toolBar.updateButton(dataList.size());
                prepareView(dataList);
                gvProduct.updateViewPort();
                resizeGridRows();
                return true;
            }
        }

        Task task = new Task("Получение каталога продукции...");
        task.executeTask();
    }

    @Override
    public void addRecord() {
        if (article != null) {
            if (manager.addProductManual(article)) {
                updateContent();
            }
        }
    }

    @Override
    public void editRecord() {
        ProductionCatalogProductView selectedItem = gvProduct.getSelectedItem();
        if (selectedItem != null) {

            BaseEditorDialog editor = new BaseEditorDialog(controller,
                    RecordOperationType.EDIT);
            editor.setParentTitle("Редактирование данных изделия");

            ProductionCatalogProductEntity editableItem = manager.getProductionCatalogProductEntityById(selectedItem.getId());
            if (editableItem != null) {
                editingPane.setSourceEntity(editableItem);
                editor.setEditorPane(editingPane);
                if (editor.showModal()) {
                    try {
                        final DaoFactory factory = DaoFactory.getInstance();
                        final ICommonDao dao = factory.getCommonDao();
                        Object o = editingPane.getSourceEntity();
                        dao.updateEntity(o);
                        gvProduct.setUpdatedObject(o);
                    } catch (final SQLException e) {
                        e.printStackTrace();
                    }
                    updateContent();
                }
            }
        }
    }

    @Override
    public void deleteRecord() {
        ProductionCatalogProductView selectedItem = gvProduct.getSelectedItem();
        if (selectedItem != null) {
            final int answer = Dialogs.showDeleteDialog(selectedItem.getSizePrint() + " для " + article.getArticleInformation());
            if (answer == 0) {
                try {
                    DaoFactory factory = DaoFactory.getInstance();
                    ICommonDao dao = factory.getCommonDao();
                    dao.deleteEntity(ProductionCatalogProductEntity.class, selectedItem.getId());
                    updateContent();
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка удаления записи");
                }
            }
        }
    }

    public boolean showMode() {
        frameViewPort.getFrameControl().showFrame();
        return needToUpdate;
    }

    private void resizeGridRows() {
        for (ProductionCatalogProductView item : dataList) {
            if (item != null) {
                gvProduct.setRowHeight(item, item.getLineHeight());
            }
        }
    }

    private void prepareView(List<ProductionCatalogProductView> dataList) {

        for (ProductionCatalogProductView item : dataList) {
            if (item != null) {
                if (item.getColors() == null) {
                    item.setColors(article.getColors());
                }
                prepareColors(item);

            }
        }
    }

    private void prepareColors(ProductionCatalogProductView item_) {
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
