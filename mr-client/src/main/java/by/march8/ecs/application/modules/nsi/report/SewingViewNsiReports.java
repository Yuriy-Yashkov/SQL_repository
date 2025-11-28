package by.march8.ecs.application.modules.nsi.report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Objects;

public class SewingViewNsiReports {

    public Parent createContent() {
        try {
            return FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/by/march8/ecs/application/modules/nsi/reports/views/SewingNsiReportsView.fxml"))
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
