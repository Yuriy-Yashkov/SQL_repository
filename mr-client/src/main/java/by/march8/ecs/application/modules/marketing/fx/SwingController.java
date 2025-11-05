package by.march8.ecs.application.modules.marketing.fx;

import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.fx.model.MovementProductionItem;
import by.march8.ecs.application.modules.marketing.fx.mvc.TradingAnalysisController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.awt.*;


/**
 * @author Andy
 */
public class SwingController {

    private MainController controller;
    private MovementProductionItem item;

    public SwingController(MainController controller, MovementProductionItem item) {
        this.item = item;
        this.controller = controller;
        show();
    }

    private void show() {
        Dimension screenSize = controller.getMainForm().getSize();
        Dimension dimension = new Dimension(800, 700);
        Point p = new Point(screenSize.width / 2 - dimension.width / 2,
                screenSize.height / 2 - dimension.height / 2);
        JDialog frame = new JDialog(controller.getMainForm(), "Монитор оборота изделий", false);

        final JFXPanel panel = new JFXPanel();
        frame.getContentPane().add(panel);
        frame.setSize(dimension);
        frame.setLocation(p);
        frame.setLocationRelativeTo(controller.getMainForm());

        Platform.runLater(() -> {
            try {
                initFX(panel, frame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        frame.setVisible(true);
    }

    private void initFX(JFXPanel panel, JDialog frame) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mvc/formTradingAnalysis.fxml"));
        Parent root = loader.load();
        panel.setScene(new Scene(root, 800, 600));
        TradingAnalysisController controller = loader.getController();
        controller.initData(frame, item);
    }
}
