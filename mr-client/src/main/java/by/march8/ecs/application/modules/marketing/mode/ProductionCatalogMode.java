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
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.editor.ProductionCatalogEditor;
import by.march8.ecs.application.modules.marketing.manager.ProductionCatalogManager;
import by.march8.entities.product.ProductionCatalog;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author Andy 18.01.2019 - 9:22.
 */
public class ProductionCatalogMode extends AbstractFunctionalMode {

    private GridViewPort<ProductionCatalog> gvProductionCatalog;
    private List<ProductionCatalog> dataList;
    private UCDatePeriodPicker periodPicker;
    private UCToolBar toolBar;

    private EditingPane editingPane;
    private JButton btnExportDocument;


    public ProductionCatalogMode(MainController mainController) {
        controller = mainController;
        modeName = "Каталог продукции ОАО \"8 Марта\"";
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        init();
        initEvents();
        updateContent();

        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {
        gvProductionCatalog = new GridViewPort<>(ProductionCatalog.class);
        dataList = gvProductionCatalog.getDataModel();
        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);
        toolBar.registerEvents(this);
        toolBar.getBtnViewItem().setVisible(true);
        frameViewPort.setGridViewPort(gvProductionCatalog);
        btnExportDocument = new JButton();
        btnExportDocument.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/save_24.png", "Обновить данные"));
        btnExportDocument.setToolTipText("Выгрузить каталог");
        toolBar.addSeparator();
        toolBar.add(btnExportDocument);

        periodPicker = new UCDatePeriodPicker();
        periodPicker.setName("periodPicker");
        toolBar.add(periodPicker);

        Calendar c = Calendar.getInstance();
        Date endDate = new Date();
        c.setTime(endDate);
        int daysToDecrement = -35;
        c.add(Calendar.DATE, daysToDecrement);
        periodPicker.setDatePickerBegin(c.getTime());

        controller.getPersonalization().getPersonalSettings(this, periodPicker);
        periodPicker.setDatePickerEnd(new Date());
        editingPane = new ProductionCatalogEditor(frameViewPort);
    }

    private void initEvents() {
        gvProductionCatalog.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {
                viewDetail((ProductionCatalog) object);
            }
        });

        periodPicker.addOnChangeAction(a -> {
            updateContent();
            controller.getPersonalization().setPersonalSettings(this, periodPicker);
        });

        btnExportDocument.addActionListener(a -> {
            ProductionCatalog item = gvProductionCatalog.getSelectedItem();
            if (item != null) {
                exportDocument(item.getId());
            }
        });
    }

    @Override
    public void updateContent() {
        DaoFactory<ProductionCatalog> factory = DaoFactory.getInstance();
        IGenericDao<ProductionCatalog> dao = factory.getGenericDao();

        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("periodBegin", periodPicker.getDatePickerBegin()));
        criteria.add(new QueryProperty("periodEnd", periodPicker.getDatePickerEnd()));
        dataList.clear();
        try {
            dataList.addAll(dao.getEntityListByNamedQuery(ProductionCatalog.class, "ProductionCatalog.findByPeriod", criteria));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gvProductionCatalog.updateViewPort();
        toolBar.updateButton(dataList.size());
    }

    @Override
    public void addRecord() {
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle("Каталог продукции");
        editingPane.setSourceEntity(null);
        editor.setEditorPane(editingPane);
        if (editor.showModal()) {
            final DaoFactory factory = DaoFactory.getInstance();
            final ICommonDao dao = factory.getCommonDao();
            try {
                Object o = dao.saveEntity(editingPane.getSourceEntity());
                gvProductionCatalog.setUpdatedObject(o);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            updateContent();
        }
    }


    @Override
    public void editRecord() {
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Каталог продукции");
        editingPane.setSourceEntity(gvProductionCatalog.getSelectedItem());
        editor.setEditorPane(editingPane);
        if (editor.showModal()) {
            try {
                final DaoFactory factory = DaoFactory.getInstance();
                final ICommonDao dao = factory.getCommonDao();
                Object o = editingPane.getSourceEntity();
                dao.updateEntity(o);
                gvProductionCatalog.setUpdatedObject(o);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            updateContent();
        }
    }

    @Override
    public void deleteRecord() {
        ProductionCatalog selectedItem = gvProductionCatalog.getSelectedItem();
        if (selectedItem != null) {
            final int answer = Dialogs.showDeleteDialog(selectedItem.getDocumentInformation());
            if (answer == 0) {
                try {
                    DaoFactory factory = DaoFactory.getInstance();
                    ICommonDao dao = factory.getCommonDao();
                    dao.deleteEntity(ProductionCatalog.class, selectedItem.getId());
                    updateContent();
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка удаления записи");
                }
            }
        }
    }

    @Override
    public void viewRecord() {
        viewDetail(gvProductionCatalog.getSelectedItem());
    }

    private void viewDetail(ProductionCatalog document) {
        new ProductionCatalogArticleMode(frameViewPort, document);
    }

    private void exportDocument(int documentId) {
        ProductionCatalogManager manager = new ProductionCatalogManager(controller);
        manager.prepareReportDialog(documentId);
    }
}
