package by.gomel.freedev.ucframework.uccore.utils;

import xsd.image.ImageInformation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Andy 20.12.2018 - 7:52.
 */
public class PictureUtils {

    public static BufferedImage pictureOpen(File file) {
        BufferedImage img;
        if (file.exists()) {
            if (file.length() > 15000000) {
                return null;
            }
            try {
                img = ImageIO.read(file);
                return img;
            } catch (Exception e) {
                System.err.println("Ошибка метода PictureUtils.pictureOpen(" + file.getAbsolutePath() + ")");
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static BufferedImage pictureResize(BufferedImage sourceImage, int sizeImage) {
        if (sourceImage == null) {
            return null;
        }

        int imageWidth = sourceImage.getWidth();
        int imageHeight = sourceImage.getHeight();

        double width;
        int height;

        // Получаем соотношение сторон
        double rate = imageWidth / imageHeight;
        if (rate < 1) {
            width = sizeImage * rate;
            height = sizeImage;
        } else {
            width = sizeImage * rate;
            height = sizeImage;
        }
        // Пляски с масштабированием
        double widthScale = width / (double) imageWidth;
        double heightScale = height / (double) imageHeight;

        if (widthScale > heightScale) {
            width = imageWidth * widthScale;
        } else {
            width = imageWidth * heightScale;
        }

        Image image = sourceImage.getScaledInstance((int) width, height,
                Image.SCALE_AREA_AVERAGING);
        BufferedImage changedImage = new BufferedImage((int) width,
                height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = changedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return changedImage;
    }

    public static ImageInformation pictureSave(BufferedImage picture, File file) {
        try {
            ImageInformation result = new ImageInformation();
            if (file.exists()) {
                File f = new File(file.getAbsolutePath().replace(".jpg", "_.jpg"));
                ImageIO.write(picture, "jpg", f);
                result.setFileName(f);
            } else {
                ImageIO.write(picture, "jpg", file);
                result.setFileName(file);
            }

            result.setWidth(picture.getWidth());
            result.setHeight(picture.getHeight());
            return result;
        } catch (IOException e) {
            System.err.println("Ошибка сохранения изображения [" + file.getAbsolutePath()
                    + "]");
            e.printStackTrace();
        }
        return null;
    }
}
