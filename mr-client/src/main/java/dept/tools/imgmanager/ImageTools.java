package dept.tools.imgmanager;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Вспомогательный класс для работы с изображениями, собраны методы для
 * масштабирования, сохранения, переименования и т.д. Необходимо некоторые
 * методы упаковать в try-catch
 *
 * @author Andy
 * @version 0.1
 */

public class ImageTools {

    /**
     * Метод загружает изображение [path] с предварительной проверкой на
     * доступность и возвращает загруженное изображение типа BufferedImage
     *
     * @param path полный путь к изображению
     * @return BufferedImage изображение
     */
    public BufferedImage imageOpen(String path) {
        BufferedImage img = null;
        if ((new File(path)).exists()) {
            try {
                img = ImageIO.read(new File(path));
                return img;
            } catch (IOException e) {
                System.err.println("Ошибка метода imageOpen(" + path + ")" + e);
                e.getStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Метод конвертирует аргумент типа ImageIcon в BufferedImage
     */
    public BufferedImage convertImageIconToBufferedImage(ImageIcon imageIcon) {
        if (imageIcon == null) {
            System.out.println("Объект imageIcon NULL");
            return null;
        }
        BufferedImage bufferedImage = new BufferedImage(
                imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        imageIcon.paintIcon(null, g, 0, 0);
        g.dispose();
        return bufferedImage;
    }

    /**
     * Метод проверяет расширение файла и фозвращает true если расширение jpg
     */
    public boolean isJPGExtension(String fileName) {
        if (new File(fileName).isDirectory()) {
            return false;
        }
        String file = new File(fileName).getAbsoluteFile().getName();
        int index = file.indexOf(".");
        String extension = file.substring(index + 1);
        return extension.equals("jpg");
    }

    /**
     * Метод реализует операцию [Изменить размер] с сохранением соотношения
     * сторон
     *
     * @param sourceImage изображение-источник
     * @param sizeImage   размер изображение с фиксированием по высоте
     * @return BufferedImage измененное изображение
     */
    public BufferedImage imageResize(BufferedImage sourceImage, int sizeImage) {
        if (sourceImage == null) {
            return null;
        }
        int imageWidth = sourceImage.getWidth();
        int imageHeight = sourceImage.getHeight();
        int size = sizeImage;
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
        Image image = sourceImage.getScaledInstance((int) width, height,
                Image.SCALE_AREA_AVERAGING);
        BufferedImage changedImage = new BufferedImage((int) width,
                height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = changedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return changedImage;
    }

    /**
     * Метод реализует операцию [Открыть, Изменить размер]
     *
     * @param pathSource изображение источник
     * @param sizeImage  размер изображения с фикасированием по высоте
     */

    public BufferedImage imageOpenResize(String pathSource, int sizeImage) {
        if (!new File(pathSource).isDirectory()) {
            BufferedImage sourceImage = imageOpen(pathSource);
            if (sourceImage == null) {
                return null;
            }
            return imageResize(sourceImage, sizeImage);
        } else {
            return null;
        }
    }

    /**
     * Метод реализует операцию [Открыть , Изменить размер ,Сохранить как...]
     * для изображения. Необходимо передать путь к изображению-источнику
     * pathSource, каталог назначения pathDest, имя файла nameDest, и
     * предварительный размер изображения sizeImage (фиксирован по высоте)
     *
     * @param pathSource изображение источник
     * @param pathDest   каталог для сохранения
     * @param sizeImage  размер изображения
     * @return boolean возвращает false в случае неудачи
     */
    public boolean imageOpenResizeSaveAs(String pathSource, String pathDest,
                                         String destName, int sizeImage) {

        if (!new File(pathSource).isDirectory()) {
            BufferedImage sourceImage = imageOpen(pathSource);
            if (sourceImage == null)
                return false;

            try {
                sourceImage = ImageIO.read(new File(pathSource));
            } catch (IOException e1) {
                System.err.println("Ошибка загрузки изображения-источника ["
                        + pathSource + "]");
                e1.printStackTrace();
                return false;
            }
            BufferedImage destImage = imageResize(sourceImage, sizeImage);

            String fileDest = pathDest + "/" + destName;
            if (new File(pathDest).exists()) {
                try {
                    ImageIO.write(destImage, "jpg", new File(fileDest));
                    return true;
                } catch (IOException e1) {
                    System.err.println("Ошибка сохранения изображения ["
                            + fileDest + "]");
                    e1.printStackTrace();
                    return false;
                }
            } else {
                System.err.println("Ошибка сохранения изображения: путь ["
                        + fileDest + "] задан неверно");
                return false;
            }
        } else {
            System.err.println("Ошибка загрузки изображения источника: ["
                    + pathSource + "]");
            return false;
        }
    }


    public boolean openResizeSaveAs(String pathSource, String destFile, int sizeImage) {

        if (new File(destFile).exists()) {
            return false;
        }

        if (new File(pathSource).length() > 20000000) {
            System.out.println("Very big file, sorry. [" + new File(pathSource).length() / 1024 / 1024 + "]");
            return false;
        }

        BufferedImage sourceImage = null;

        try {
            sourceImage = ImageIO.read(new File(pathSource));
        } catch (Exception e1) {
            System.err.println("Ошибка загрузки изображения-источника ["
                    + pathSource + "]");
            //e1.printStackTrace();
            return false;
        }

        // Ресайз для 1200 точек
        BufferedImage destImage = imageResize(sourceImage, sizeImage);

        if (!new File(destFile).exists()) {
            try {
                File saveable = new File(destFile);

                JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
                jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                jpegParams.setCompressionQuality(0.3f);

                final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                writer.setOutput(new FileImageOutputStream(saveable));

                writer.write(null, new IIOImage(destImage, null, null), jpegParams);

                //return true;

            } catch (Exception e1) {
                System.err.println("Ошибка сохранения изображения ["
                        + destFile + "]");
                // e1.printStackTrace();
                return false;
            }
        }

        // Ресайз для 120 точек
        destImage = imageResize(sourceImage, 120);

        if (!new File(destFile.replace("resize", "resize_120")).exists()) {
            try {
                File saveable = new File(destFile.replace("resize", "resize_120"));

                JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
                jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                jpegParams.setCompressionQuality(0.9f);

                final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                writer.setOutput(new FileImageOutputStream(saveable));

                writer.write(null, new IIOImage(destImage, null, null), jpegParams);

                return true;

            } catch (Exception e1) {
                System.err.println("Ошибка сохранения изображения ["
                        + destFile.replace("resize", "resize_120") + "]");
                // e1.printStackTrace();
                return false;
            }
        }

        return false;
    }

    /**
     * Метод реализует операцию [Изменить размер, Сохранить как]
     */
    public boolean imageResizeSaveAs(BufferedImage image, String pathDest,
                                     String destName, int sizeImage) {

        BufferedImage destImage = imageResize(image, sizeImage);

        String fileDest = pathDest + "/" + destName;
        if (new File(pathDest).exists()) {
            try {
                ImageIO.write(destImage, "jpg", new File(fileDest));
                return true;
            } catch (IOException e1) {
                System.err.println("Ошибка сохранения изображения [" + fileDest
                        + "]");
                e1.printStackTrace();
                return false;
            }
        } else {
            System.err.println("Ошибка сохранения изображения: путь ["
                    + fileDest + "] задан неверно");
            return false;
        }

    }

    public BufferedImage placeTextOnImage(BufferedImage image, String text) {
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.drawImage(image, 0, 0, null);

        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2d.setComposite(alpha);

        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

        g2d.rotate(Math.toRadians(45), (image.getWidth() - (int) rect.getHeight()) / 2,
                (image.getHeight() - (int) rect.getHeight()) / 2);

        g2d.drawString(text, (image.getWidth() - (int) rect.getWidth()) / 2,
                (image.getHeight() - (int) rect.getHeight()) / 2);
        g2d.dispose();

        return image;
    }

    public void imageSaveAs(BufferedImage image, String pathDest, String nameDest) {
        String fileDest = pathDest + "/" + nameDest;
        if (new File(pathDest).exists()) {
            try {
                ImageIO.write(image, "jpg", new File(fileDest));
            } catch (IOException e1) {
                System.err.println("Ошибка сохранения изображения [" + fileDest
                        + "]");
                e1.printStackTrace();
            }
        } else {
            System.err.println("Ошибка сохранения изображения: путь ["
                    + fileDest + "] задан неверно");
        }
    }

}
