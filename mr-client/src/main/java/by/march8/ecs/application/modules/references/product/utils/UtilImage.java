package by.march8.ecs.application.modules.references.product.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by lidashka.
 */
public class UtilImage {

    public static ImageIcon resizeImage(BufferedImage bufferedImage,
                                        int componentWidth, int componentHeight) throws Exception {
        int width = 0;
        int height = 0;
        float el_width = 0;
        float el_height = 0;
        float multiple1;
        float multiple2;
        float im_width = bufferedImage.getWidth();
        float im_height = bufferedImage.getHeight();

        el_width = componentWidth;
        el_height = componentHeight;

        if (im_width < el_width && im_height < el_height) {
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();

        } else {
            multiple1 = im_width / (el_width - 50);
            multiple2 = im_height / (el_height - 50);

            if (multiple1 >= multiple2) {
                width = (int) (im_width / multiple1);
                height = (int) (im_height / multiple1);

            } else if (multiple1 < multiple2) {
                width = (int) (im_width / multiple2);
                height = (int) (im_height / multiple2);
            }
        }
        return new ImageIcon(bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }

    public static ByteArrayOutputStream compressImage(ImageIcon scaledImage_) throws Exception {
        ByteArrayOutputStream out1 = null;

        try {
            Image scaledImage = scaledImage_.getImage();
            RenderedImage renderedImage;

            if (scaledImage instanceof RenderedImage) {
                renderedImage = (RenderedImage) scaledImage;

            } else {
                BufferedImage bufferedImage = new BufferedImage(
                        scaledImage_.getIconWidth(),
                        scaledImage_.getIconHeight(),
                        BufferedImage.TYPE_4BYTE_ABGR);

                bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);
                renderedImage = bufferedImage;
            }
            out1 = new ByteArrayOutputStream();
            ImageIO.write(renderedImage, "jpg", out1);

        } catch (Exception e) {
            return null;
        }

        return out1;
    }

    public static BufferedImage getBufferedImage(ImageIcon scaledImage_) throws Exception {
        BufferedImage bufferedImage_ = null;

        try {
            Image scaledImage = scaledImage_.getImage();

            BufferedImage bufferedImage = new BufferedImage(
                    scaledImage_.getIconWidth(),
                    scaledImage_.getIconHeight(),
                    BufferedImage.TYPE_3BYTE_BGR);

            bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);
            bufferedImage_ = bufferedImage;

        } catch (Exception e) {
            return null;
        }

        return bufferedImage_;
    }

    public static void imageSaveAs(BufferedImage image, String path, String name) {
        String file = path + "/" + name;
        if (new File(path).exists()) {
            try {
                ImageIO.write(image, "jpg", new File(file));
            } catch (IOException e1) {
                System.err.println("Ошибка сохранения изображения [" + file + "]");
                e1.printStackTrace();
            }
        } else {
            System.err.println("Ошибка сохранения изображения: путь [" + file + "] задан неверно");
        }
    }
}
