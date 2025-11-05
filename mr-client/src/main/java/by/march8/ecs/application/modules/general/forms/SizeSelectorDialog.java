package by.march8.ecs.application.modules.general.forms;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.model.CheckClassifierItem;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.product.ProductionCatalogProduct;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Andy 21.01.2019 - 11:34.
 */
public class SizeSelectorDialog extends BasePickDialog {

    private GridViewPort<CheckClassifierItem> gvItem;
    private List<CheckClassifierItem> data;
    private HashMap<Integer, String> map;
    private JButton btnSelectAll;
    private JButton btnClearAll;


    public SizeSelectorDialog(MainController controller) {
        super(controller);
        setTitle("Укажите необходимые размеры для отборки изделий");

        init();
        initEvents();
    }

    private void init() {
        setFrameSize(new Dimension(400, 450));
        gvItem = new GridViewPort<>(CheckClassifierItem.class, true);
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

                return gvItem.getSelectedItems() != null;
            }

            @Override
            public boolean canCancel() {
                return true;
            }
        });
    }

    public Map<Integer, String> selectProductSizes(ClassifierModelParams item, List<ProductionCatalogProduct> presetList) {
        data.clear();

        List<ClassifierItem> outList = item.getAssortmentList().stream()
                .sorted(Comparator.comparing(ClassifierItem::getSize)
                        .thenComparing(ClassifierItem::getGrowth))
                .parallel().collect(Collectors.toList());

        for (ClassifierItem assortment : outList) {
            if (assortment.getItemGrade() == 1 && assortment.getPriceWholesale() > 0) {
                if (presetList != null) {
                    boolean exist = false;
                    for (ProductionCatalogProduct product : presetList) {
                        if (product.getProductId() == assortment.getId()) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        data.add(new CheckClassifierItem(assortment.getId(),
                                assortment.getSizePrint(),
                                assortment.getSize(),
                                assortment.getGrowth()));
                    }
                } else {
                    data.add(new CheckClassifierItem(assortment.getId(),
                            assortment.getSizePrint(),
                            assortment.getSize(),
                            assortment.getGrowth()));
                }
            }
        }

        gvItem.updateViewPort();

        if (showModalFrame()) {
            for (CheckClassifierItem item_ : gvItem.getSelectedItems()) {
                if (item_ != null) {
                    if (map == null) {
                        map = new HashMap<>();
                    }
                    map.put(item_.getId(), item_.getName());
                }
            }
            return map;
        }
        return null;
    }
}
