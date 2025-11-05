package common;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author vova
 */
public class PanelWihtFone extends JPanel {
    private Image image;

    public PanelWihtFone() {
        super();
        setLayout(null);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
