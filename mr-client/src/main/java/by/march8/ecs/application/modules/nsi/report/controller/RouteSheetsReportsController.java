package by.march8.ecs.application.modules.nsi.report.controller;

import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class RouteSheetsReportsController {

    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private TextField numField;

    @FXML
    private TableView<MainRow> mainTable;
    @FXML
    private TableView<DetailRow1> detailTable1;
    @FXML
    private TableView<DetailRow2> detailTable2;

    @FXML
    public void initialize() {
        // настройка колонок, listeners и т.п.
        mainTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                loadDetails(newV.getNumber());
            }
        });
    }

    @FXML
    private void onSearch() {
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();
        loadMainTable(from, to);
    }

    private void loadMainTable(LocalDate from, LocalDate to) {
        // TODO: получить данные
        // mainTable.setItems(FXCollections.observableList(...));
    }

    private void loadDetails(String number) {
        // TODO: подгрузить detailTable1 и detailTable2
    }

    @FXML
    private void onPrint() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            job.printPage(mainTable); // или отдельный родитель
            job.endJob();
        }
    }

    @FXML
    private void onExport() {
        // экспорт в CSV
    }
}
