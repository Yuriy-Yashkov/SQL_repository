package by.march8.ecs.application.modules.general.forms;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.model.Catalog;
import by.march8.ecs.application.modules.marketing.model.ProductionOrder;
import by.march8.entities.product.OrderItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OrderRequestSelectorDialog extends BasePickDialog {

    private GridViewPort<OrderItem> gvItem;
    private List<OrderItem> data;
    private HashMap<Integer, String> map;
    private JButton btnSelectAll;
    private JButton btnClearAll;

    private JPanel pOrderInformation;
    private JLabel lblOrderDate;
    private JLabel lblCatalogNumber;
    private JLabel lblCatalogDate;


    public OrderRequestSelectorDialog(MainController controller) {
        super(controller);
        setTitle("Укажите наименования изделий для импорта в документ");

        init();
        initEvents();
    }

    private void init() {
        setFrameSize(new Dimension(600, 450));
        gvItem = new GridViewPort<>(OrderItem.class, true);
        data = gvItem.getDataModel();

        getToolBar().getBtnNewItem().setVisible(false);
        getToolBar().getBtnEditItem().setVisible(false);
        getToolBar().getBtnDeleteItem().setVisible(false);
        getToolBar().getBtnViewItem().setVisible(false);

        btnSelectAll = new JButton("");
        btnSelectAll.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/select_all_24.png", "Выбрать/Инвертировать"));
        btnSelectAll.setToolTipText("Выбрать/Инвертировать");

        btnClearAll = new JButton("");
        btnClearAll.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/clear_all_24.png", "Очистить"));
        btnClearAll.setToolTipText("Очистить");

        getToolBar().add(btnSelectAll);
        getToolBar().add(btnClearAll);


        pOrderInformation = new JPanel(new MigLayout());
        lblOrderDate = new JLabel();
        lblCatalogNumber = new JLabel();
        lblCatalogDate = new JLabel();

        pOrderInformation.add(new JLabel("Дата заявки: "));
        pOrderInformation.add(lblOrderDate, "wrap");
        pOrderInformation.add(new JLabel("Номер каталога: "));
        pOrderInformation.add(lblCatalogNumber, "wrap");
        pOrderInformation.add(new JLabel("Дата каталога: "));
        pOrderInformation.add(lblCatalogDate, "wrap");

        getTopContentPanel().add(pOrderInformation);
        getCenterContentPanel().add(gvItem);
    }

    private void initEvents() {
        btnSelectAll.addActionListener(a -> {
            gvItem.selectAll(false);
        });
        btnClearAll.addActionListener(a -> {
            gvItem.selectAll(true);
        });

        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                return gvItem.getSelectedItems() != null && gvItem.getSelectedItems().size() > 0;
            }

            @Override
            public boolean canCancel() {
                return true;
            }
        });
    }

    private void prepareView(ProductionOrder order) {
        if (order == null) {
            return;
        }

        lblOrderDate.setText(DateUtils.getNormalDateFormat(order.getDate()));
        Catalog catalog = order.getCatalog();
        if (catalog != null) {
            lblCatalogDate.setText(DateUtils.getNormalDateFormat(catalog.getDate()));
            lblCatalogNumber.setText(catalog.getNumber());
        }

        data.clear();
        data.addAll(order.getOrderList());
        gvItem.updateViewPort();
    }

    public Set<OrderItem> selectOrderItems(ProductionOrder order) {
        prepareView(order);
        if (showModalFrame()) {
            return gvItem.getSelectedItems();
        }
        return null;
    }
}
