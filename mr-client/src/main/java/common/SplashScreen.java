package common;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.black);

        int width = 475;
        int height = 270;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        JLabel label = new JLabel(new ImageIcon("./Img/MR.gif"));
        content.add(label, BorderLayout.CENTER);
        Color oraRed = new Color(0, 0, 0, 0);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 1));
    }
}