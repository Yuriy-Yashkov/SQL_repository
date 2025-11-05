package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model;

import javax.swing.*;

/**
 * @author Andy 19.12.2018 - 7:44.
 */
public class ImageItem {
    private ImageIcon icon;
    private String name;
    private String path;

    public ImageItem(ImageIcon icon, String name, String path) {
        this.icon = icon;
        this.name = name;
        this.path = path;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
