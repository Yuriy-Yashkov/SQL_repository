package by.march8.ecs.application.modules.label.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.label.editor.LabelOneEditorNew;
import by.march8.ecs.application.modules.marketing.mode.ProductionCatalogArticleMode;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.label.LabelOne;
import by.march8.entities.product.ProductionCatalog;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Developer on 13.01.2020 9:26
 */
public class MarkingMode extends AbstractFunctionalMode {
    private GridViewPort<LabelOne> gvProductionCatalog;
    private List<LabelOne> dataList;
    private UCDatePeriodPicker periodPicker;
    private UCToolBar toolBar;

    private EditingPane editingPane;
    private JButton btnExportDocument;


    public MarkingMode(MainController mainController) {
        controller = mainController;
        modeName = "Маркировка готовой продукции";
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        init();
        initEvents();
        updateContent();

        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {
        gvProductionCatalog = new GridViewPort<>(LabelOne.class);
        dataList = gvProductionCatalog.getDataModel();
        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);
        toolBar.registerEvents(this);
        toolBar.getBtnViewItem().setVisible(true);
        frameViewPort.setGridViewPort(gvProductionCatalog);
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);
        toolBar.getBtnViewItem().setVisible(false);
        //btnExportDocument = new JButton();
        //btnExportDocument.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/save_24.png", "Обновить данные"));
        //btnExportDocument.setToolTipText("Выгрузить каталог");
        toolBar.addSeparator();
        //toolBar.add(btnExportDocument);

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
//
    }

    private void initEvents() {
        gvProductionCatalog.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {
                //viewDetail((ProductionCatalog) object);

                LabelOne selectedItem = gvProductionCatalog.getSelectedItem();

                BaseEditorDialog editor = new BaseEditorDialog(controller,
                        RecordOperationType.EDIT);
                editor.setParentTitle("Печать этикетки для " + selectedItem.getName().trim() + "(" + selectedItem.getModel() + ")");
                editingPane = new LabelOneEditorNew(controller);
                editingPane.setSourceEntity(selectedItem);
                editor.setEditorPane(editingPane);
                if (editor.showModal()) {

                }

            }
        });

        periodPicker.addOnChangeAction(a -> {
            updateContent();
            controller.getPersonalization().setPersonalSettings(this, periodPicker);
        });

    }

    @Override
    public void updateContent() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<LabelOne> factory = DaoFactory.getInstance();
                IGenericDao<LabelOne> dao = factory.getGenericDao();

                List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("periodBegin", periodPicker.getDatePickerBegin()));
                criteria.add(new QueryProperty("periodEnd", periodPicker.getDatePickerEnd()));
                dataList.clear();
                try {
                    dataList.addAll(dao.getEntityListByNamedQuery(LabelOne.class, "LabelOne.findByPeriod", criteria));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                gvProductionCatalog.updateViewPort();
                toolBar.updateButton(dataList.size());
                return true;
            }
        }

        Task task = new Task("Получение данных ...");
        task.executeTask();
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
    public void viewRecord() {

    }

    private void viewDetail(ProductionCatalog document) {
        new ProductionCatalogArticleMode(frameViewPort, document);
    }

    private void exportDocument(int documentId) {

    }

}
