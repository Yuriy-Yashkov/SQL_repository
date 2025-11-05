package by.march8.ecs.application.modules.filemanager.model;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImagePanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.ImagePanelCallBack;

/**
 * @author Andy 28.12.2018 - 10:22.
 */
public class ModelImageComponent extends UCImagePanel {
    private ImageItem imageItem;

    public ModelImageComponent(ImageItem item, ImagePanelCallBack callBack) {
        super(item, callBack);
        imageItem = item;
    }

    public ModelImageComponent() {
        super();
    }

    public ImageItem getImageItem() {
        return imageItem;
    }

    public void setImageItem(ImageItem imageItem) {
        this.imageItem = imageItem;
    }

    public void updateImage(ImageItem image_) {
        fileName = image_.getPreviewImageFile();
        loadContent();
    }

    public void clearImage() {
        imageLabel.setIcon(null);
    }
}
