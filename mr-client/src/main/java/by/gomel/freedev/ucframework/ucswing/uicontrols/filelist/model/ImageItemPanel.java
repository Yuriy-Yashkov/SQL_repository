package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model;

import javax.swing.*;
import java.awt.*;

public class ImageItemPanel extends JPanel {
    private JLabel lblImage;
    private ImageItem image;

    public ImageItemPanel(ImageItem image) {
        super(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(250, 130));
        lblImage = new JLabel();
        add(lblImage);
        updateImage(image);
    }

    public void updateImage(ImageItem item) {
        lblImage.setIcon(item.getIcon());
        lblImage.setText("");
        lblImage.setToolTipText(item.getName());
    }
}
