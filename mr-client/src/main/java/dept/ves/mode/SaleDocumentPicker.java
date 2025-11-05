package dept.ves.mode;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.march8.api.utils.DatePeriod;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.SaleDocumentEntity;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SaleDocumentPicker extends BasePickDialog {

    private GridViewPort<SaleDocumentEntity> gvItem;
    private List<SaleDocumentEntity> data;

    private UCDatePeriodPicker datePeriodPicker;
    private SaleDocumentJDBC db;

    private DatePeriod datePeriod;
    private int contractorCode;
    private boolean openOnly = false;
    private boolean singleSelect;

    public SaleDocumentPicker(MainController controller, boolean singleSelect) {
        super(controller);
        this.singleSelect = singleSelect;
        init();
        initEvents();
    }

    private void initEvents() {
        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                if (singleSelect) {
                    return gvItem.getSelectedItem() != null;
                } else {
                    return gvItem.getSelectedItems().size() > 0;
                }
            }

            @Override
            public boolean canCancel() {
                return true;
            }
        });

        datePeriodPicker.addOnChangeAction(a -> {
            updateContent(openOnly);
        });
    }

    private void updateContent(final boolean openOnly) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                data.clear();
                datePeriod.setBegin(datePeriodPicker.getDatePickerBegin());
                datePeriod.setEnd(datePeriodPicker.getDatePickerEnd());

                List<SaleDocumentEntity> list = db.getSaleDocumentsForContractorsAndPeriod(datePeriod, contractorCode, openOnly);
                if (list != null) {
                    data.addAll(list);
                }

                gvItem.updateViewPort();
                return true;
            }
        }

        Task task = new Task("Получение списка документов...");
        task.executeTask();
    }

    private void updateClosedOnly() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                data.clear();
                datePeriod.setBegin(datePeriodPicker.getDatePickerBegin());
                datePeriod.setEnd(datePeriodPicker.getDatePickerEnd());
                List<SaleDocumentEntity> list = db.getClosedSaleDocumentsForContractorsAndPeriod(datePeriod, contractorCode);
                if (list != null) {
                    data.addAll(list);
                }

                gvItem.updateViewPort();
                return true;
            }
        }

        Task task = new Task("Получение списка документов...");
        task.executeTask();
    }

    private void updateRefundContent() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                data.clear();
                datePeriod.setBegin(datePeriodPicker.getDatePickerBegin());
                datePeriod.setEnd(datePeriodPicker.getDatePickerEnd());
                List<SaleDocumentEntity> list = db.getRefundDocumentsForContractorsAndPeriod(datePeriod, contractorCode);
                if (list != null) {
                    data.addAll(list);
                }

                gvItem.updateViewPort();
                return true;
            }
        }

        Task task = new Task("Получение списка документов...");
        task.executeTask();
    }

    private void init() {
        db = new SaleDocumentJDBC();

        setFrameSize(new Dimension(450, 450));
        if (singleSelect) {
            gvItem = new GridViewPort<>(SaleDocumentEntity.class);
            setTitle("Выберите из списка необходимый документ");
        } else {
            gvItem = new GridViewPort<>(SaleDocumentEntity.class, true);
            setTitle("Выберите из списка необходимые документы");
        }

        gvItem.setCustomCellRender(new SaleDocumentPickerCellRenderer(singleSelect));
        data = gvItem.getDataModel();

        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerBegin(new Date());
        datePeriodPicker.setDatePickerEnd(new Date());
        //datePeriodPicker.setEditable(true);

        datePeriodPicker.setEditorEditable(false);

        datePeriod = new DatePeriod();

        getToolBar().getBtnNewItem().setVisible(false);
        getToolBar().getBtnEditItem().setVisible(false);
        getToolBar().getBtnDeleteItem().setVisible(false);
        getToolBar().addSeparator();
        getToolBar().add(new JLabel("Период отбора"));
        getToolBar().addSeparator();
        getToolBar().add(datePeriodPicker);
        getCenterContentPanel().add(gvItem);
    }

    public Set<SaleDocumentEntity> selectSaleDocuments(int contractorCode, Date startPeriod, boolean openOnly) {
        this.openOnly = openOnly;
        this.contractorCode = contractorCode;
        datePeriodPicker.setDatePickerBegin(startPeriod);
        updateContent(openOnly);

        if (showModalFrame()) {
            return gvItem.getSelectedItems();
        }

        return null;
    }

    public Set<SaleDocumentEntity> selectRefundDocuments(int contractorCode, Date startPeriod, boolean openOnly) {
        this.openOnly = openOnly;
        this.contractorCode = contractorCode;

        datePeriodPicker.setDatePickerBegin(startPeriod);
        updateRefundContent();

        if (showModalFrame()) {
            return gvItem.getSelectedItems();
        }

        return null;
    }

    public SaleDocumentEntity selectSaleDocument(int contractorCode, Date startPeriod, boolean openOnly) {
        this.openOnly = openOnly;
        this.singleSelect = true;
        this.contractorCode = contractorCode;
        datePeriodPicker.setDatePickerBegin(startPeriod);
        updateContent(openOnly);

        if (showModalFrame()) {
            return gvItem.getSelectedItem();
        }

        return null;
    }

    public SaleDocumentEntity selectClosedSaleDocument(int contractorCode, Date startPeriod) {
        this.openOnly = false;
        this.singleSelect = true;
        this.contractorCode = contractorCode;
        datePeriodPicker.setDatePickerBegin(startPeriod);

        updateClosedOnly();

        if (showModalFrame()) {
            return gvItem.getSelectedItem();
        }

        return null;
    }
}
