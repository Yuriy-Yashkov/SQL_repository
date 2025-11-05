package dept.tools.imgmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePreviewForm extends JFrame {
    private imgPanel lblImage;
    private Image img;

    public ImagePreviewForm(BufferedImage img) {
        // super();
        this.img = img;
        Container c = this.getContentPane();
        if (img == null) {
            System.out.println("Неверный jpg файл");
            this.dispose();
            return;
        }
        lblImage = new imgPanel();
        c.add(lblImage);

        Dimension size = new Dimension(img.getWidth(), img.getHeight());
        lblImage.setPreferredSize(size);
        this.setPreferredSize(new Dimension(img.getWidth() + 10,
                img.getHeight() + 50));
        this.setTitle(size.width + "x" + size.height);

        // Инструмент для снятия параметров экрана
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolKit.getScreenSize();
        Dimension frameSize = this.getPreferredSize();

        Point p = new Point(screenSize.width / 2 - frameSize.width / 2,
                screenSize.height / 2 - frameSize.height / 2);

        setSize(frameSize);
        setLocation(p);

        // this.setPreferredSize(new Dimension(200, 200));
        // this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private class imgPanel extends JPanel {

        @Override
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }
    }
}
