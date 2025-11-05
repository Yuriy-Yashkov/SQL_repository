package by.march8.ecs.application.modules.filemanager.model;

import by.march8.ecs.services.images.ModelImageServiceXML;
import xsd.image.ImageListItem;

import javax.swing.*;
import java.io.File;

/**
 * @author Andy 28.12.2018 - 10:23.
 */
public class ImageItem {
    private ImageIcon buffer = null;
    private String model;
    private String color;
    private ImageListItem image;
    private int id;

    public ImageItem() {
        buffer = null;
    }

    public ImageItem(ImageIcon imageIcon, File file_) {
        buffer = imageIcon;
        model = null;
        color = "РАЗНОЦВЕТ";

        image = new ImageListItem();
        image.setSourceFile(file_.toString());
        image.setImageFile(null);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ImageListItem getImage() {
        return image;
    }

    public void setImage(ImageListItem image) {
        this.image = image;
    }

    public String getColor() {
        if (color != null) {
            return color.trim();
        }
        return null;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFullOriginalImageFile() {
        if (image != null) {
            return ModelImageServiceXML.SOURCE_DIR + image.getSourceFile();
        }
        return null;
    }

    public String getPreviewImageFile() {
        if (image != null) {
            return "/" + ModelImageServiceXML.CATALOG_DIR + image.getImageFile().replace("IMAGES", "IMAGES/.thumbs")
                    .replace("//", "/");
        }
        return null;
    }

    public String getImageFile() {
        if (image != null) {
            return "/" + ModelImageServiceXML.CATALOG_DIR + image.getImageFile()
                    .replace("//", "/");
        }
        return null;
    }

    public String getSourceImageFile() {
        if (image != null) {
            return ModelImageServiceXML.SOURCE_DIR + image.getSourceFile();
        }
        return null;
    }

    public boolean sourceExist() {
        if (image != null) {
            File file = new File(getFullOriginalImageFile());
            return file.exists();
        }
        return false;
    }

    public ImageIcon getBuffer() {
        return buffer;
    }

    public void setBuffer(ImageIcon buffer) {
        this.buffer = buffer;
    }
}
