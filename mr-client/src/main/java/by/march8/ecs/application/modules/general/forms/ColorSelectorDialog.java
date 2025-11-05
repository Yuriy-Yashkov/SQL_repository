package by.march8.ecs.application.modules.general.forms;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.marketing.model.ColorSelectorCellRender;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Andy 22.01.2019 - 13:31.
 */
public class ColorSelectorDialog extends BasePickDialog {
    private GridViewPort<ColorTextItem> gvItem;
    private List<ColorTextItem> data;
    private HashMap<Integer, String> map;
    private JButton btnSelectAll;
    private JButton btnClearAll;


    public ColorSelectorDialog(MainController controller) {
        super(controller);
        setTitle("Укажите необходимые цвета изделий");

        init();
        initEvents();
    }

    private void init() {
        setFrameSize(new Dimension(400, 450));
        gvItem = new GridViewPort<>(ColorTextItem.class, true);
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

        gvItem.setCustomCellRender(new ColorSelectorCellRender());

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

    public Set<ColorTextItem> selectColor(List<ColorTextItem> presetList) {
        data.clear();
        data.addAll(ColorPresetHelper.getColorList());
        gvItem.selectAll(true);
        if (presetList != null) {
            for (ColorTextItem product : presetList) {
                for (ColorTextItem item : data) {
                    if (item.getId() == product.getId()) {
                        gvItem.setCheck(item.getId(), true);
                    }
                }
            }
        }

        gvItem.updateViewPort();

        if (showModalFrame()) {
            return gvItem.getSelectedItems();
        }
        return null;
    }

    public Set<ColorTextItem> selectColor(List<ColorTextItem> source, List<ColorTextItem> presetList) {
        data.clear();
        data.addAll(source);
        gvItem.selectAll(true);
        if (presetList != null) {
            for (ColorTextItem product : presetList) {
                for (ColorTextItem item : data) {
                    if (item.getId() == product.getId()) {
                        gvItem.setCheck(item.getId(), true);
                    }
                }
            }
        }

        gvItem.updateViewPort();

        if (showModalFrame()) {
            return gvItem.getSelectedItems();
        }
        return null;
    }
}
