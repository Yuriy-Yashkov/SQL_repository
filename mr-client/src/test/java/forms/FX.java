package forms;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.io.IOException;

/**
 * @author Andy 02.03.2018.
 */
public class FX {

    public void initAndShowGUI() {
        // This method is invoked on the EDT thread
        JFrame frame = new JFrame("Swing and JavaFX");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(300, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

    private void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();

        fxPanel.setScene(scene);
    }

    private Scene createScene() {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("datamatrix.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (new Scene(root, 300, 275));
    }
}
