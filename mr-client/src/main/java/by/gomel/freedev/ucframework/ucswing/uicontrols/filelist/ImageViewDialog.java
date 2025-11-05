package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist;

import by.gomel.freedev.ucframework.uccore.utils.PictureUtils;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseActiveDialog;
import by.march8.ecs.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Andy 10.01.2019 - 12:34.
 */
public class ImageViewDialog extends BaseActiveDialog {

    private JLabel lblImage;

    public ImageViewDialog(MainController controller, String file) {
        super(controller);
        init(file);
        setResizable(true);
        getToolBar().setVisible(false);
        getBtnSave().setVisible(false);
        getBtnCancel().setText("Закрыть");
        showFrame();
    }

    private void init(String file) {
        BufferedImage image = PictureUtils.pictureOpen(new File(file));


        lblImage = new JLabel();
        if (image != null) {
            setTitle("Просмотр изображения " + file);
            resizeFrame(new Dimension((int) (image.getWidth() / 1.8), (int) (image.getHeight() / 1.8) + 50));

            lblImage.setIcon(new ImageIcon(image));
        }

        JScrollPane sp = new JScrollPane(lblImage);
        sp.getHorizontalScrollBar().setMaximum(100);
        sp.getVerticalScrollBar().setMaximum(100);
        sp.getHorizontalScrollBar().setValue(50);
        sp.getVerticalScrollBar().setValue(50);
        panelCenter.add(sp, BorderLayout.CENTER);
    }
}
