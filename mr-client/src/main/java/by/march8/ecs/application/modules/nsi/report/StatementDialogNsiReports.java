package by.march8.ecs.application.modules.nsi.report;

import javafx.scene.Parent;

import javax.swing.*;

public class StatementDialogNsiReports extends BaseFxDialog {

    public StatementDialogNsiReports(JFrame owner) {
        super(owner, "Накопительная ведомость");
    }

    @Override
    protected Parent createContent() {
        return new StatementViewReports().createContent();
    }

    @Override
    protected void onFxReady(Parent root) {

    }
}