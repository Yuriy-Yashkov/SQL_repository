package by.march8.ecs.application.modules.cut;

import by.march8.ecs.application.modules.cut.view.BaseFxDialog;
import javafx.scene.Parent;

import javax.swing.*;

public class StatementDialogNsiReport extends BaseFxDialog {

    public StatementDialogNsiReport(JFrame owner) {
        super(owner, "Накопительная ведомость");
    }

    @Override
    protected Parent createContent() {
        return new StatementViewReport().createContent();
    }

    @Override
    protected void onFxReady(Parent root) {

    }
}