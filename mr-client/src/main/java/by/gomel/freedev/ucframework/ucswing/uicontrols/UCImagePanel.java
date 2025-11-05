package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.ImagePanelCallBack;
import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.ImagePanelMouseListener;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author Andy 20.12.2017.
 */
public class UCImagePanel extends JPanel {
    protected UCImageLabel imageLabel;
    protected String fileName;
    private JLabel label;
    private ImagePanelCallBack callBack;
    private boolean selected = false;
    private boolean typed = false;
    private ImageItem imageItem;


    public UCImagePanel() {
        super();
        init();
        addMouseListener(new ImagePanelMouseListener(this, a -> {
            return true;
        }));
    }

    public UCImagePanel(String fileName, ImagePanelCallBack callBack) {
        super();
        this.fileName = fileName;
        this.callBack = callBack;
        init();
        loadContent();
        addMouseListener(new ImagePanelMouseListener(this, callBack));
    }

    public UCImagePanel(ImageItem item, ImagePanelCallBack callBack) {
        super();
        imageItem = item;
        this.callBack = callBack;
        if (item.getBuffer() != null) {
            fileName = null;

        } else {
            fileName = item.getPreviewImageFile();
        }

        init();
        loadContent();

        addMouseListener(new ImagePanelMouseListener(this, callBack));
    }

    protected void loadContent() {
        if (fileName == null) {
            imageLabel.setIcon(imageItem.getBuffer());
            imageLabel.setPreferredSize(new Dimension(imageItem.getBuffer().getIconWidth(), 120));
            setPreferredSize(new Dimension(imageItem.getBuffer().getIconWidth(), 145));
            label.setText("");
            label.setToolTipText(imageItem.getImage().getSourceFile());
        } else {
            File file = new File(fileName);
            if (file.exists() && file.isFile()) {
                imageLabel.setImage(fileName);
                imageLabel.setPreferredSize(new Dimension(imageLabel.getImageWidth(), 120));
                label.setText(file.getName());
                label.setToolTipText(file.getName());
                //System.out.println("Размер картинки "+imageLabel.getImageWidth());
                setPreferredSize(new Dimension(imageLabel.getImageWidth(), 145));
            }
        }
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(
                1, 1, 5, 1, Color.ORANGE));
        //setBackground(Color.blue);
        label = new JLabel("filename");
        label.setPreferredSize(new Dimension(0, 15));
        if (imageItem != null) {
            imageLabel = new UCImageLabel(imageItem);
        } else {
            imageLabel = new UCImageLabel();
        }

        add(label, BorderLayout.SOUTH);
        add(imageLabel, BorderLayout.CENTER);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            setBorder(BorderFactory.createMatteBorder(
                    1, 1, 5, 1, Color.RED));
        } else {
            setBorder(BorderFactory.createMatteBorder(
                    1, 1, 5, 1, Color.ORANGE));
        }
    }

    public boolean isTyped() {
        return typed;
    }

    public void setTyped(boolean typed) {
        this.typed = typed;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ImageItem getImageItem() {
        return imageItem;
    }

    public void setImageItem(ImageItem imageItem) {
        this.imageItem = imageItem;
    }
}
