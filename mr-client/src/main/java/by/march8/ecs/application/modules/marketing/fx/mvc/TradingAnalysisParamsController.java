package by.march8.ecs.application.modules.marketing.fx.mvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * @author Andy
 */
public class TradingAnalysisParamsController {
    private Stage stage;

    @FXML
    private DatePicker dateBegin;

    @FXML
    private DatePicker dateEnd;

    @FXML
    private Button btnAccept;

    @FXML
    private Button btnCancel;

    @FXML
    void onCalcelButtonClick(MouseEvent event) {
        stage.close();
    }

/*    public TradingAnalysisParamsController() {
       // dateBegin = new DatePicker();
       // dateEnd = new DatePicker();
       // btnAccept = new Button();


    }*/


    public void initData(TradingAnalysisController controller, Stage stage) {
        btnAccept.setText(controller.getValues());
        dateBegin.setValue(LocalDate.now());
        dateEnd.setValue(LocalDate.of(2014, 10, 8));

        this.stage = stage;
    }
}
