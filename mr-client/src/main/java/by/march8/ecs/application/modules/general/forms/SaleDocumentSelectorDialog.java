package by.march8.ecs.application.modules.general.forms;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.general.forms.model.SaleDocumentViewRenderer;
import by.march8.entities.sales.PreOrderSaleDocumentView;
import by.march8.entities.warehouse.SaleDocumentView;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaleDocumentSelectorDialog extends BasePickDialog {
    private GridViewPort<SaleDocumentView> gvItem;
    private List<SaleDocumentView> data;
    private PreOrderSaleDocumentView document;
    private UCDatePeriodPicker datePeriodPicker;

    public SaleDocumentSelectorDialog(MainController controller) {
        super(controller);
        setTitle("Выберите необходимый документ для экспорта");

        init();
        initEvents();
    }

    private void init() {
        setFrameSize(new Dimension(750, 450));
        gvItem = new GridViewPort<>(SaleDocumentView.class);
        data = gvItem.getDataModel();

        getToolBar().getBtnNewItem().setVisible(false);
        getToolBar().getBtnEditItem().setVisible(false);
        getToolBar().getBtnDeleteItem().setVisible(false);
        getToolBar().getBtnViewItem().setVisible(false);

        // Селектор периода просмотра документов
        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerBegin(DateUtils.getFirstDay(datePeriodPicker.getDatePickerEnd()));
        datePeriodPicker.setEditable(true);
        getToolBar().add(datePeriodPicker);

        getCenterContentPanel().add(gvItem);

        gvItem.setCustomCellRender(new SaleDocumentViewRenderer());
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

        datePeriodPicker.addOnChangeAction(a -> {
            prepareView(datePeriodPicker.getTimeLimitPeriod());
        });
    }


    public SaleDocumentView selectSaleDocument(PreOrderSaleDocumentView document) {
        setTitle("Документы для экспорта : " + document.getContractorName());
        this.document = document;
        prepareView(datePeriodPicker.getTimeLimitPeriod());
        if (showModalFrame()) {
            return gvItem.getSelectedItem();
        }
        return null;
    }

    private void prepareView(DatePeriod period) {
        int export = document.getCurrencyId() > 1 ? 1 : 0;
        DaoFactory<SaleDocumentView> factory = DaoFactory.getInstance();
        IGenericDaoGUI<SaleDocumentView> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("periodBegin", period.getBegin()));
        criteria.add(new QueryProperty("periodEnd", period.getEnd()));
        criteria.add(new QueryProperty("contractor", document.getContractorId()));
        data.clear();
        try {
            data.addAll(dao.getEntityListByNamedQueryGUI(SaleDocumentView.class, "SaleDocumentView.findAllPerPeriodAndContractor", criteria));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gvItem.updateViewPort();
    }
}
