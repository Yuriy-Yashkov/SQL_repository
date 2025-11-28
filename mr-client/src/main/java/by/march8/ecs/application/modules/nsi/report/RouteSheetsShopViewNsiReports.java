package by.march8.ecs.application.modules.nsi.report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Objects;

public class RouteSheetsShopViewNsiReports {

    public Parent createContent() {
        try {
            return FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/by/march8/ecs/application/modules/nsi/reports/views/RouteSheetsNsiReportsView.fxml"))
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
