package by.march8.ecs.application.modules.sales.mode;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.sales.editor.ProductParamsTableEditor;
import by.march8.entities.sales.PreOrderSaleDocumentItem;

import java.awt.*;
import java.util.List;

public class PrepareProductsDialog extends BasePickDialog {
    private GridViewPort<PreOrderSaleDocumentItem> gvItem;
    private List<PreOrderSaleDocumentItem> data;

    private ProductParamsTableEditor cellEditor;

    public PrepareProductsDialog(MainController controller) {
        super(controller);
        setTitle("Заполните параметры отбора изделий");

        init();
        initEvents();
    }

    private void init() {
        setFrameSize(new Dimension(750, 450));
        gvItem = new GridViewPort<>(PreOrderSaleDocumentItem.class, false);
        data = gvItem.getDataModel();

        getToolBar().getBtnNewItem().setVisible(false);
        getToolBar().getBtnEditItem().setVisible(false);
        getToolBar().getBtnDeleteItem().setVisible(false);
        getToolBar().getBtnViewItem().setVisible(false);

        getCenterContentPanel().add(gvItem);

        //gvItem.setCustomCellRender(new SaleDocumentViewRenderer());

        cellEditor = new ProductParamsTableEditor(gvItem);
        gvItem.setCustomCellEditor(cellEditor);
        gvItem.setIgnoreNotEditableCells(true);
        gvItem.getTable().setRowSelectionAllowed(true);

        gvItem.primaryInitialization();
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

/*    private void prepareView(DatePeriod period) {
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
    }*/

    public List<PreOrderSaleDocumentItem> prepareParams(List<PreOrderSaleDocumentItem> list) {
        data.clear();
        data.addAll(list);

        int i = 1;
        for (PreOrderSaleDocumentItem item : data) {
            item.setId(i);
            i++;
        }

        gvItem.updateViewPort();
        if (showModalFrame()) {
            for (PreOrderSaleDocumentItem item : data) {
                item.setId(0);
            }
            return data;
        }
        return null;
    }
}
