package by.march8.ecs.application.modules.references.product.components.image;

import by.march8.entities.product.ModelImage;

import javax.swing.*;

/**
 *
 * Created by lidashka.
 */

public class ImageLabel extends JLabel {
    private int id;
    private ModelImage imageModel;
    private boolean select;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModelImage getImageModel() {
        return imageModel;
    }

    public void setImageModel(ModelImage imageModel) {
        this.imageModel = imageModel;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
