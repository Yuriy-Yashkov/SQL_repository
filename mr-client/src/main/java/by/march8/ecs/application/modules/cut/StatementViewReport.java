package by.march8.ecs.application.modules.cut;

import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class StatementViewReport {

    public Parent createContent() {

        TableView<String> table = new TableView<>();
        TableColumn<String, String> col = new TableColumn<>("Столбец");
        col.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));

        table.getColumns().add(col);
        table.setItems(FXCollections.observableArrayList("Строка 1", "Строка 2"));

        VBox root = new VBox(10, table);
        root.setStyle("-fx-padding: 20");

        return root;
    }
}
