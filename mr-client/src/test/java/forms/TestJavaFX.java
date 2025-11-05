package forms;

import javax.swing.*;

/**
 * @author Andy 02.03.2018.
 */
public class TestJavaFX {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FX fx = new FX();
                fx.initAndShowGUI();
            }
        });
    }
}
