package by.march8.ecs.application.modules.marketing.fx.mvc;

import by.march8.ecs.application.modules.marketing.fx.model.MovementProductionItem;
import by.march8.ecs.application.modules.marketing.fx.model.ProgressForm;
import by.march8.ecs.application.modules.marketing.model.ChartItem;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ProductionItem;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.util.Date;

public class TradingAnalysisController {
    @FXML
    private LineChart<Date, Number> chartMove;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    @FXML
    private Button btnProperties;

    @FXML
    private Label itemName;

    @FXML
    private Label articleNumber;

    @FXML
    private Label incommingAmount;

    @FXML
    private Label outcommingAmount;

    @FXML
    private LineChart<Date, Number> chartPlan;

    @FXML
    private CategoryAxis productionDateAxis;

    @FXML
    private NumberAxis productionAmountAxis;


    @FXML
    private Button btnUpdate;

    private XYChart.Series series1;
    private XYChart.Series series2;
    private XYChart.Series series3;

    private JDialog stage;

    public void initData(JDialog scene, MovementProductionItem product) {

        if (product == null) {
            return;
        }

        ProductionItem productionItem = product.getItem();
        itemName.setText(productionItem.getName());
        articleNumber.setText("Модель " + productionItem.getItemModel() + " артикул " + productionItem.getArticleName());

        this.stage = scene;
        int inAmount = 0;
        int outAmount = 0;
        int planAmount = 0;

        for (ChartItem item : product.getInList()) {
            series1.getData().add(new XYChart.Data(item.getDate(), item.getAmount()));
            inAmount += item.getAmount();
        }

        for (ChartItem item : product.getOutList()) {
            series2.getData().add(new XYChart.Data(item.getDate(), item.getAmount()));
            outAmount += item.getAmount();
        }

        for (ChartItem item : product.getPlanList()) {
            series3.getData().add(new XYChart.Data(item.getDate(), item.getAmount()));
            planAmount += item.getAmount();
        }
        chartMove.getData().addAll(series1, series2);
        chartPlan.getData().addAll(series3);

        chartMove.setLegendVisible(false);
        chartPlan.setLegendVisible(false);
        chartMove.setCreateSymbols(true);

        incommingAmount.setText("Передано на склад: " + inAmount + " - (По плану :" + planAmount + ")");
        outcommingAmount.setText("Реализовано :" + outAmount + " - (Остаток:" + productionItem.getStockBalance() + ")");
    }

    @FXML
    void onClick(MouseEvent event) {
        showProperties();
    }

    @FXML
    private void initialize() {
        // categoryAxis.setLabel("Дата");
        // numberAxis.setLabel("Количество");

        // productionDateAxis.setLabel("Дата");
        // productionAmountAxis.setLabel("Количество");
        productionDateAxis.setGapStartAndEnd(true);

        chartMove.setTitle("График перемещения изделия");
        chartPlan.setTitle("План производства изделия");

        series1 = new XYChart.Series();
        //series1.setName("Поступления");

        //series1.setNode(new );

        series2 = new XYChart.Series();
        //  series2.setName("Продажи");

        series3 = new XYChart.Series();
        // series3.setName("Производство");
    }

    private Stage showProperties() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "formTradingAnalysisParams.fxml"
                )
        );

        Stage stage = new Stage(StageStyle.UTILITY);
        try {
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        TradingAnalysisParamsController controller =
                loader.<TradingAnalysisParamsController>getController();
        controller.initData(this, stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return stage;
    }

    @FXML
    void onButtonUpdateClick(MouseEvent event) {
        ProgressForm pForm = new ProgressForm(stage);

        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                for (int i = 0; i < 10; i++) {
                    updateProgress(i, 10);
                    Thread.sleep(500);
                }
                updateProgress(10, 10);
                return null;
            }
        };

        pForm.activateProgressBar(task);

        task.setOnSucceeded(e -> {
            pForm.getProgressStage().close();
            btnUpdate.setDisable(false);
        });

        btnUpdate.setDisable(true);
        pForm.getProgressStage().show();

        Thread thread = new Thread(task);
        thread.start();
    }

    public String getValues() {
        return "Welcome";
    }


}


