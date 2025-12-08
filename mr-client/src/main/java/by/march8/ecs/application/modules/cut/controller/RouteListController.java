package by.march8.ecs.application.modules.cut.controller;

import by.march8.ecs.application.modules.cut.model.CodeListRow;
import by.march8.ecs.application.modules.cut.model.CuttingRouteRow;
import by.march8.ecs.application.modules.cut.model.SewingRouteRow;
import by.march8.ecs.application.modules.cut.report.OdtTableBuilder;
import by.march8.ecs.application.modules.cut.report.OdtTemplateEngine;
import by.march8.ecs.application.modules.cut.report.ReportGenerator;
import by.march8.ecs.application.modules.cut.service.RouteListService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RouteListController {

    @FXML
    private Button searchButton;

    @FXML
    private DatePicker dateFromPicker;

    @FXML
    private DatePicker dateToPicker;

    @FXML
    private TextField numField;

    // Таблица
    @FXML
    private TableView<CodeListRow> codeListTable;

    // Колонки
    @FXML
    private TableColumn<CodeListRow, LocalDate> colDate;

    @FXML
    private TableColumn<CodeListRow, String> colNumber;

    // Таблица
    @FXML
    private TableView<CuttingRouteRow> cuttingRouteRowTable;

    // Колонки
    @FXML
    private TableColumn<CuttingRouteRow, String> colArticleCanvas;

    @FXML
    private TableColumn<CuttingRouteRow, String> colName;

    @FXML
    private TableColumn<CuttingRouteRow, String> colArticle1;

    @FXML
    private TableColumn<CuttingRouteRow, String> colModelNumber;

    @FXML
    private TableColumn<CuttingRouteRow, String> colColor;

    @FXML
    private TableColumn<CuttingRouteRow, String> colSize;

    @FXML
    private TableColumn<CuttingRouteRow, String> colHeight;

    @FXML
    private TableColumn<CuttingRouteRow, String> colQuantity;

    @FXML
    private TableColumn<CuttingRouteRow, String> colBrigade;

    @FXML
    private TableColumn<CuttingRouteRow, String> colForeman;

    @FXML
    private TableColumn<CuttingRouteRow, String> colCodeList;

    @FXML
    private TableColumn<CuttingRouteRow, String> colSewDate;

    // таблица
    @FXML
    private TableView<SewingRouteRow> sewingRouteRowTable;

    // колонки
    @FXML
    private TableColumn<SewingRouteRow, String> colArticle2;

    @FXML
    private TableColumn<SewingRouteRow, String> colName2;

    @FXML
    private TableColumn<SewingRouteRow, String> colArticle21;

    @FXML
    private TableColumn<SewingRouteRow, String> colModelNumber2;

    @FXML
    private TableColumn<SewingRouteRow, String> colColor2;

    @FXML
    private TableColumn<SewingRouteRow, String> colGrade2;

    @FXML
    private TableColumn<SewingRouteRow, String> colSize2;

    @FXML
    private TableColumn<SewingRouteRow, String> colHeight2;

    @FXML
    private TableColumn<SewingRouteRow, String> colQuantity2;

    @FXML
    private TableColumn<SewingRouteRow, String> colBrigade2;

    @FXML
    private TableColumn<SewingRouteRow, String> colForeman2;

    @FXML
    private TableColumn<SewingRouteRow, String> colCodeList2;

    @FXML
    private TableColumn<SewingRouteRow, String> colSewDate2;

    RouteListService routeListService = new RouteListService();

    @FXML
    public void initialize() {
        // --- настройка resize policy ---
        cuttingRouteRowTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        sewingRouteRowTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // --- настройка value factories ---

        // Привязка колонок к модели CodeListRow
        colDate.setCellValueFactory(cell -> cell.getValue().dateProperty());
        // отображаем LocalDate как dd.MM.yyyy
        colDate.setCellFactory(column -> new TableCell<CodeListRow, LocalDate>() {

            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.format(fmt));
            }
        });
        colNumber.setCellValueFactory(cell -> cell.getValue().codeListProperty());

        // Привязка колонок к модели CuttingRouteRow
        colArticleCanvas.setCellValueFactory(cell -> cell.getValue().articleCanvasProperty());
        colName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        colArticle1.setCellValueFactory(cell -> cell.getValue().articleProperty());
        colModelNumber.setCellValueFactory(cell -> cell.getValue().modelNumberProperty());
        colColor.setCellValueFactory(cell -> cell.getValue().colorProperty());
        colSize.setCellValueFactory(cell -> cell.getValue().sizeProperty());
        colHeight.setCellValueFactory(cell -> cell.getValue().heightProperty());
        colQuantity.setCellValueFactory(cell -> cell.getValue().quantityProperty());
        colBrigade.setCellValueFactory(cell -> cell.getValue().brigadeProperty());
        colForeman.setCellValueFactory(c -> c.getValue().foremanProperty());
        colCodeList.setCellValueFactory(cell -> cell.getValue().codeListProperty());
        colSewDate.setCellValueFactory(cell -> cell.getValue().sewDateProperty());

        // Привязка колонок к модели SewingRouteRow
        colArticle2.setCellValueFactory(c -> c.getValue().articleProperty());
        colName2.setCellValueFactory(c -> c.getValue().nameProperty());
        colArticle21.setCellValueFactory(c -> c.getValue().article2Property());
        colModelNumber2.setCellValueFactory(c -> c.getValue().modelNumberProperty());
        colColor2.setCellValueFactory(c -> c.getValue().colorProperty());
        colGrade2.setCellValueFactory(c -> c.getValue().gradeProperty());
        colSize2.setCellValueFactory(c -> c.getValue().sizeProperty());
        colHeight2.setCellValueFactory(c -> c.getValue().heightProperty());
        colQuantity2.setCellValueFactory(c -> c.getValue().quantityProperty());
        colBrigade2.setCellValueFactory(c -> c.getValue().brigadeProperty());
        colForeman2.setCellValueFactory(c -> c.getValue().foremanProperty());
        colCodeList2.setCellValueFactory(c -> c.getValue().codeListProperty());
        colSewDate2.setCellValueFactory(c -> c.getValue().sewDateProperty());

//         настройка колонок, listeners и т.п.
        codeListTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {

                String code = newV.getCodeList();

                // Добавляем в поле
                numField.setText(code);

                // Загружаем детальную таблицу
                loadDetails(code);
            }
        });
    }

    @FXML
    private void onSearch() {
        LocalDate dateFrom = this.dateFromPicker.getValue();
        LocalDate dateTo = this.dateToPicker.getValue();

        // 1) Проверяем, что "Дата от" указана
        if (dateFrom == null) {
            // TODO: 03.12.2025  можно показать alert
            System.out.println("Выберите дату 'От'");
            return;
        }

        if (dateTo == null || !dateTo.isAfter(dateFrom)) {
            dateTo = dateFrom.plusDays(1L);
            dateToPicker.setValue(dateTo);
        }

        String code = numField.getText();

        if (code != null && !code.isEmpty()) {
            // Пользователь указал номер — показываем только его
            loadCodeListRowTable(dateFrom, dateTo, code);
            loadDetails(code);
        } else {
            // Номер не указан — показываем весь список
            loadCodeListRowTable(dateFrom, dateTo, "");
        }
    }

    private void loadCodeListRowTable(LocalDate from, LocalDate to, String codeList) {

        long number = 0;

        if (codeList != null && !codeList.isEmpty()) {
            number = Long.parseLong(codeList);
        }

        List<CodeListRow> rows = routeListService.findCodeList(from, to, number);
        codeListTable.setItems(FXCollections.observableList(rows));
    }

    private void loadDetails(String codeList) {
        if (codeList == null || codeList.isEmpty()) return;

        // конвертируем в long
        long number = Long.parseLong(codeList);

        LocalDate dateFrom = dateFromPicker.getValue();
        LocalDate dateTo = dateToPicker.getValue();

        List<CuttingRouteRow> rowsCutting = routeListService.findRouteList(dateFrom, dateTo, number);
        List<SewingRouteRow> rowSewing = routeListService.findSewingList(number);

        cuttingRouteRowTable.setItems(FXCollections.observableList(rowsCutting));
        sewingRouteRowTable.setItems(FXCollections.observableList(rowSewing));
    }

    @FXML
    private void onPrint() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
//            job.printPage(mainTable); // или отдельный родитель
            job.endJob();
        }
    }

    @FXML
    private void onExport() {
        try {
            // 1. Достаём данные из таблиц
            List<CuttingRouteRow> cuttingRows = new ArrayList<>(cuttingRouteRowTable.getItems());
            List<SewingRouteRow> sewingRows = new ArrayList<>(sewingRouteRowTable.getItems());

            // 2. Генерируем XML строки
            String cuttingXml = OdtTableBuilder.buildCuttingRouteRows(cuttingRows);
            String sewingXml = OdtTableBuilder.buildSewingRows(sewingRows);

            // 3. Формируем карту значений для шаблона
            Map<String, String> values = new HashMap<>();
            values.put("CUTTING_ROWS", cuttingXml);
            values.put("SEWING_ROWS", sewingXml);

            File templateFile = new File("d:\\Users\\OAO8Marta\\Documents\\Templates\\templateRouteSheet.odt");
            System.out.println("Абсолютный путь: " + templateFile.getAbsolutePath());
            System.out.println("Существует? " + templateFile.exists());


            // 4. Читаем и подставляем
            String contentXml = OdtTemplateEngine.loadAndFillTemplate(
                    new File("templateRouteSheet.odt"),
                    values
            );

            // 5. Создаём итоговый ODT
            File result = ReportGenerator.generateReport(
                    new File("templateRouteSheet.odt"),
                    "route_report.odt",
                    contentXml
            );

            System.out.println("Готово: " + result.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // showError реализация в этом же классе:
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
