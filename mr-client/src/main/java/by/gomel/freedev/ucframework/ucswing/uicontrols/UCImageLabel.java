package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Компонент для отображения кртинок
 * ОООООООчень сырой, пилить и пилить, но пока и так сойдет
 *
 * @author Andy 19.12.2017.
 */
public class UCImageLabel extends JLabel {
    private String imageFileName = "";
    private String imageFile = "";
    private int imageWidth;
    private int imageHeight;
    private ImageItem image;
    private ColorTextItem colorTextItem;

    private boolean sourceExist = true;
    private int[] x_ = new int[3];
    private int[] y_ = new int[3];

    public UCImageLabel() {
        super();
    }

    public UCImageLabel(ImageItem image) {
        super();
        this.image = image;
        colorTextItem = ColorPresetHelper.getColorByName(image.getColor());

        if (image.getBuffer() != null) {
            sourceExist = true;
        } else {
            sourceExist = image.sourceExist();
        }
        x_[0] = -30;
        x_[1] = 0;
        x_[2] = 0;
        y_[0] = 0;
        y_[1] = -30;
        y_[2] = 0;
    }

    public static void drawCircle(Graphics g, int x, int y, int radius) {
        int diameter = radius * 2;
        g.fillOval(x - radius, y - radius, diameter, diameter);
    }

    public boolean setImage() {
        if (imageFileName.equals("")) {
            if (imageFile == null) {
                imageFileName = "IMAGE_NOT_FOUND";
                return false;
            }

            File f = new File(imageFile);
            if (f.exists()) {
                setIcon(new ImageIcon(imageFile));
            }

            imageFileName = imageFile;
            return true;
        } else if (imageFileName.equals("IMAGE_NOT_FOUND")) {
            return false;
        } else {
            return true;
        }
    }

    public void setImage(String path) {
        if (imageFileName.equals("")) {

            ImageIcon imageIcon = new ImageIcon(path); // load the image to a imageIcon
            Image img = imageIcon.getImage(); // transform it

            imageWidth = imageIcon.getIconWidth();
            imageHeight = imageIcon.getIconHeight();
            int size = 120;
            double width = size;
            int height = size;

            // Получаем соотношение сторон
            double rate = imageWidth / imageHeight;
            if (rate < 1) {
                width = size * rate;
                height = size;
            } else {
                width = size * rate;
                height = (size);
            }

            // Пляски с масштабированием
            double widthScale = width / (double) imageWidth;
            double heightScale = height / (double) imageHeight;
            if (widthScale > heightScale) {
                width = imageWidth * widthScale;
            } else {
                width = imageWidth * heightScale;
            }

            Image newImage = img.getScaledInstance((int) width, height,
                    Image.SCALE_AREA_AVERAGING);
            setIcon(new ImageIcon(newImage));

            imageWidth = (int) width;
            imageHeight = (int) height;

            x_[0] = x_[0] + imageWidth;
            x_[1] = x_[1] + imageWidth;
            x_[2] = x_[2] + imageWidth;

            y_[0] = y_[0] + imageHeight + 2;
            y_[1] = y_[1] + imageHeight + 2;
            y_[2] = y_[2] + imageHeight + 2;
        }
    }

    public void setImageFile(final String imageFile) {
        if (imageFile != null) {
            this.imageFile = imageFile;
        } else {
            this.imageFile = null;
        }
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (colorTextItem != null) {
            g.setColor(colorTextItem.getColor());
            Polygon p = new Polygon(x_, y_, 3);
            g.fillPolygon(p);
        }

        if (image != null && !sourceExist) {
            g.setColor(java.awt.Color.RED);
            drawCircle(g, 7, 7, 5);
        }
    }
}
