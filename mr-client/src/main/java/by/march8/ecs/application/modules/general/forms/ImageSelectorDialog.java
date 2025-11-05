package by.march8.ecs.application.modules.general.forms;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImagePanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.ImagePanelCallBack;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.filemanager.model.ModelImageComponent;
import by.march8.ecs.services.images.ColorImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Andy 23.01.2019 - 14:20.
 */
public class ImageSelectorDialog extends BasePickDialog implements ImagePanelCallBack {

    private JPanel content;
    private UCImagePanel selectedPanel = null;
    private String modelNumber;


    public ImageSelectorDialog(final MainController controller, String model) {
        super(controller);
        modelNumber = model;
        String titleText = "Доступные изображения для модели " + model;
        setTitle(titleText);
        init();
    }

    private void init() {
        setFrameSize(new Dimension(640, 260));
        getToolBar().setVisible(false);

        content = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //list.setPreferredSize(new Dimension(600, 200));

        JScrollPane scrollBar = new JScrollPane(content);
        scrollBar.setPreferredSize(new Dimension(600, 135));

        getCenterContentPanel().add(scrollBar);
        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                return selectedPanel != null;
            }

            @Override
            public boolean canCancel() {
                return true;
            }
        });
    }

    private boolean prepareImages() {
        ColorImageService imageService = ModelImageServiceDB.getInstance();
        content.removeAll();

        List<ImageItem> list = imageService.getColorImageListByModel(modelNumber);
        for (ImageItem image : list) {
            if (image != null) {
                ModelImageComponent panel = new ModelImageComponent(image, this);
                content.add(panel);
            }
        }

        content.revalidate();
        content.repaint();

        return true;
    }

    public ImageItem selectImage() {
        prepareImages();

        if (showModalFrame()) {
            return selectedPanel.getImageItem();
        } else {
            return null;
        }
    }

    @Override
    public boolean onEvent(final UCImagePanel panel) {

        Component[] comps = content.getComponents();
        for (Component comp : comps) {
            if (comp instanceof UCImagePanel) {
                //compList.addAll(getAllComponents((Container) comp));
                ((UCImagePanel) comp).setSelected(false);
            }
        }

        // Снимаем выбор со всех панелей, и выбираем активным на котором кликнули мышой
        panel.setSelected(true);
        selectedPanel = panel;

        return false;
    }
}
