package by.march8.ecs.application.modules.sales.mode;

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
import by.march8.api.utils.DatePeriod;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.sales.editor.PreOrderSaleDocumentEditor;
import by.march8.ecs.application.modules.sales.manager.PreOrderSaleDocumentManager;
import by.march8.ecs.application.modules.sales.model.PreOrderControlType;
import by.march8.entities.sales.PreOrderSaleDocument;
import by.march8.entities.sales.PreOrderSaleDocumentBase;
import by.march8.entities.sales.PreOrderSaleDocumentView;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 14.01.2019 - 7:11.
 */
public class PreOrderSaleDocumentMode extends AbstractFunctionalMode<PreOrderSaleDocumentView> {
    private GridViewPort<PreOrderSaleDocumentView> gvPreOrderDocument;
    private List<PreOrderSaleDocumentView> dataList;
    private UCDatePeriodPicker periodPicker;
    private UCToolBar toolBar;

    private EditingPane editingPane;
    private PreOrderSaleDocumentManager manager;
    private PreOrderControlType type;
    private JButton btnUpdate;


    public PreOrderSaleDocumentMode(MainController mainController, PreOrderControlType type) {
        controller = mainController;
        this.type = type;
        modeName = "Предварительные заказы контрагентов ОАО 8 Марта";
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        init();
        initEvents();
        updateContent();

        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {
        gvPreOrderDocument = new GridViewPort<>(PreOrderSaleDocumentView.class);
        dataList = gvPreOrderDocument.getDataModel();
        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);
        toolBar.registerEvents(this);
        toolBar.getBtnViewItem().setVisible(true);
        frameViewPort.setGridViewPort(gvPreOrderDocument);

        periodPicker = new UCDatePeriodPicker();
        periodPicker.setName("periodPicker");
        toolBar.add(periodPicker);

        btnUpdate = new JButton();
        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnUpdate.setToolTipText("Обновить данные");
        toolBar.add(btnUpdate);
        Box box = new Box(BoxLayout.X_AXIS);
        box.setPreferredSize(new Dimension(24, 24));

        toolBar.add(box);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        Calendar c = Calendar.getInstance();
        Date endDate = new Date();
        c.setTime(endDate);
        int daysToDecrement = -35;
        c.add(Calendar.DATE, daysToDecrement);
        periodPicker.setDatePickerBegin(c.getTime());

        controller.getPersonalization().getPersonalSettings(this, periodPicker);
        periodPicker.setDatePickerEnd(new Date());
        editingPane = new PreOrderSaleDocumentEditor(frameViewPort);
        manager = PreOrderSaleDocumentManager.getInstance(controller);
    }

    private void initEvents() {
        gvPreOrderDocument.setTableEventHandler(new TableEventAdapter<PreOrderSaleDocumentView>() {
            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final PreOrderSaleDocumentView object) {
                viewDetail(object);
            }
        });

        periodPicker.addOnChangeAction(a -> {
            updateContent();
            controller.getPersonalization().setPersonalSettings(this, periodPicker);
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                updateContent();
            }
        });
    }

    @Override
    public void updateContent() {
        DaoFactory<PreOrderSaleDocumentView> factory = DaoFactory.getInstance();
        IGenericDao<PreOrderSaleDocumentView> dao = factory.getGenericDao();
        DatePeriod datePeriod = periodPicker.getTimeLimitPeriod();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("periodBegin", datePeriod.getBegin()));
        criteria.add(new QueryProperty("periodEnd", datePeriod.getEnd()));
        String sql = "PreOrderSaleDocumentView.findByPeriod";
        if (type == PreOrderControlType.EXPORT) {
            sql = "PreOrderSaleDocumentView.findByPeriodExport";
            criteria.add(new QueryProperty("currency", 2));
        } else if (type == PreOrderControlType.INTERNAL) {
            sql = "PreOrderSaleDocumentView.findByPeriodInternal";
            criteria.add(new QueryProperty("currency", 1));
        }
        dataList.clear();
        try {
            dataList.addAll(dao.getEntityListByNamedQuery(PreOrderSaleDocumentView.class, sql, criteria));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gvPreOrderDocument.updateViewPort();
        toolBar.updateButton(dataList.size());
        updateView();
    }

    private void updateView() {
        final TableColumnModel columnModel = gvPreOrderDocument.getTable().getColumnModel();
        if (type == PreOrderControlType.INTERNAL) {
            for (int c = 9; c <= 11; c++) {
                columnModel.getColumn(c).setMaxWidth(0);
                columnModel.getColumn(c).setMinWidth(0);
                columnModel.getColumn(c).setPreferredWidth(0);
            }
        }
    }

    @Override
    public void addRecord() {
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle("Документ предварительного заказа");
        editingPane.setSourceEntity(null);
        editor.setEditorPane(editingPane);
        if (editor.showModal()) {
            final DaoFactory factory = DaoFactory.getInstance();
            final ICommonDao dao = factory.getCommonDao();
            try {
                Object o = dao.saveEntity(editingPane.getSourceEntity());
                gvPreOrderDocument.setUpdatedObject(o);
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
        editor.setParentTitle("Документ предварительного заказа");
        PreOrderSaleDocumentView selectedItem = gvPreOrderDocument.getSelectedItem();
        if (selectedItem != null) {
            PreOrderSaleDocumentBase editableItem = PreOrderSaleDocumentManager.getDocumentById(selectedItem.getId());
            if (editableItem != null) {
                editingPane.setSourceEntity(editableItem);
                editor.setEditorPane(editingPane);
                if (editor.showModal()) {
                    try {
                        final DaoFactory factory = DaoFactory.getInstance();
                        final ICommonDao dao = factory.getCommonDao();
                        Object o = editingPane.getSourceEntity();
                        dao.updateEntity(o);
                        gvPreOrderDocument.setUpdatedObject(o);
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
        PreOrderSaleDocumentView selectedItem = gvPreOrderDocument.getSelectedItem();
        if (selectedItem != null) {
            final int answer = Dialogs.showDeleteDialog(selectedItem.getDocumentInformation());
            if (answer == 0) {
                try {
                    DaoFactory factory = DaoFactory.getInstance();
                    ICommonDao dao = factory.getCommonDao();
                    dao.deleteEntity(PreOrderSaleDocument.class, selectedItem.getId());
                    updateContent();
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка удаления записи");
                }
            }
        }
    }

    @Override
    public void viewRecord() {
        viewDetail(gvPreOrderDocument.getSelectedItem());
    }

    private void viewDetail(PreOrderSaleDocumentView document) {

        PreOrderSaleDocumentDetailMode detailMode = new PreOrderSaleDocumentDetailMode(frameViewPort, document, type);
        if (detailMode.showMode()) {
            updateContent();
        }
    }
}
