package by.march8.ecs.application.modules.marketing.fx.model;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;

/**
 * @author Andy
 */
public class ProgressForm {
    private final Stage progressStage;
    private final JDialog parent;

    private final ProgressIndicator pin = new ProgressIndicator();

    public ProgressForm(JDialog parentStage) {
        this.parent = parentStage;
        progressStage = new Stage();
        progressStage.initStyle(StageStyle.TRANSPARENT);
        progressStage.setResizable(false);
        progressStage.initModality(Modality.WINDOW_MODAL);

        pin.setPrefHeight(64);
        pin.setPrefWidth(64);

        final HBox hb = new HBox();
        hb.setSpacing(0);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(pin);

        Scene scene = new Scene(hb);
        progressStage.setScene(scene);
    }

    public void activateProgressBar(final Task<?> task) {
        progressStage.show();
        progressStage.setX(parent.getX() + parent.getWidth() / 2 - progressStage.getWidth() / 2); //dialog.getWidth() = not NaN
        progressStage.setY(parent.getY() + parent.getHeight() / 2 - progressStage.getHeight() / 2); //dialog.getHeight() = not NaN
    }

    public Stage getProgressStage() {
        return progressStage;
    }
}
