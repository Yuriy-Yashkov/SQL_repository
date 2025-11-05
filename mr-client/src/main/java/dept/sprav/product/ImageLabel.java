package dept.sprav.product;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author lidashka
 */

public class ImageLabel extends javax.swing.JLabel {

    private static final long serialVersionUID = 1L;
    public float sizeImg;
    JTextField text = new JTextField();
    private BufferedImage originalImage = null;
    private Image image = null;

    public ImageLabel() {

        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);

        this.setLayout(new BorderLayout(1, 1));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        text.setHorizontalAlignment(JLabel.CENTER);

        this.add(text, BorderLayout.SOUTH);

    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        int w = this.getWidth();
        int h = this.getHeight();

        if ((originalImage != null) && (w > 0) && (h > 0)) {
            image = originalImage.getScaledInstance(w, h, Image.SCALE_DEFAULT);
            this.repaint();
        }
    }

    private ImageIcon sizeImage(BufferedImage destImage, JComponent comp) {
        int width = 0, height = 0;
        float el_width = 0, el_height = 0;
        float multiple1, multiple2;
        float im_width = destImage.getWidth();
        float im_height = destImage.getHeight();

        el_width = comp.getWidth();
        el_height = comp.getHeight();

        if (im_width < el_width && im_height < el_height) {
            width = destImage.getWidth();
            height = destImage.getHeight();
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
        return new ImageIcon(destImage.getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }

    public void paint(Graphics g) {

        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }

        super.paintChildren(g);
        super.paintBorder(g);
    }


    public BufferedImage getImage() {
        return originalImage;
    }

    public void setImage(BufferedImage image) {
        this.originalImage = image;
        sizeImg = (float) (image.getWidth()) / (float) (image.getHeight());
        formComponentResized(null);
    }


    public void setImageFile(File file) {
        if (file == null)
            originalImage = null;
        else {
            try {
                BufferedImage bi;
                bi = ImageIO.read(file);
                originalImage = bi;
            } catch (IOException ex) {
                System.err.println("Couldn't load picture!");
                ex.printStackTrace();
            }
            formComponentResized(null);
            sizeImg = (float) (originalImage.getWidth()) / (float) (originalImage.getHeight());
            repaint();
        }
    }

    void setSize_(int el_width, int el_height) {
        int width = 0, height = 0;
        float multiple1, multiple2;
        float im_width = originalImage.getWidth();
        float im_height = originalImage.getHeight();


        if (im_width < el_width && im_height < el_height) {
            width = originalImage.getWidth();
            height = originalImage.getHeight();
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


        this.setSize(width, height);
    }

}
