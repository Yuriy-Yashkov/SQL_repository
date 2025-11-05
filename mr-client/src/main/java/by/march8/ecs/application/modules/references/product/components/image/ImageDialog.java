package by.march8.ecs.application.modules.references.product.components.image;

import by.gomel.freedev.ucframework.ucswing.dialog.BaseDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.product.utils.UtilImage;
import by.march8.entities.product.ModelImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by lidashka.
 */
public class ImageDialog extends BaseDialog {

    private JPanel imageBigPanel;

    private ModelImage modelImage;

    public ImageDialog(MainController controller, ModelImage modelImage) throws Exception {
        super(controller, new Dimension(800, 650));

        setTitle("Изображение");
        this.modelImage = modelImage;

        init();
        initEvents();

        showBigImage(modelImage.getImageFull());

        setVisible(true);
    }

    private void init() {
        btnSave.setText("Cохранить как...");
        btnSave.setPreferredSize(new Dimension(180, 28));

        btnCancel.setPreferredSize(new Dimension(180, 28));

        imageBigPanel = new JPanel();
        imageBigPanel.setLayout(new BorderLayout());

        add(imageBigPanel, BorderLayout.CENTER);
    }

    private void initEvents() {
        btnCancel.addActionListener(e -> {
            setVisible(false);
            dispose();
        });

        btnSave.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            int returnVal = fileChooser.showDialog(ImageDialog.this, "Сохранить как...");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {

                    BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(modelImage.getImageFull()));

                    BufferedImage saveImage = new BufferedImage(
                            originalImage.getWidth(),
                            originalImage.getHeight(),
                            BufferedImage.TYPE_3BYTE_BGR);

                    for (int x = 0; x < originalImage.getWidth(); x++) {
                        for (int y = 0; y < originalImage.getHeight(); y++) {
                            saveImage.setRGB(x, y, originalImage.getRGB(x, y));
                        }
                    }

                    System.out.println(fileChooser.getSelectedFile().getPath() + ".jpg");

                    ImageIO.write(
                            saveImage,
                            "jpg",
                            new File(fileChooser.getSelectedFile().getPath() + modelImage.getName()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            fileChooser.setSelectedFile(null);

            setVisible(false);
            dispose();
        });
    }


    private void showBigImage(byte[] bytes) throws Exception {
        JLabel bigImage = new JLabel();
        bigImage.setHorizontalAlignment(JLabel.CENTER);
        bigImage.setVerticalAlignment(JLabel.CENTER);
        bigImage.setSize(this.getWidth(), this.getHeight());
        bigImage.setIcon(UtilImage
                .resizeImage(ImageIO.read(new ByteArrayInputStream(bytes)), bigImage.getWidth(), bigImage.getHeight()));
        imageBigPanel.removeAll();
        imageBigPanel.add(bigImage);
        imageBigPanel.repaint();
    }
}
