package by.march8.ecs.application.modules.nsi.report;

import javafx.scene.Parent;

import javax.swing.*;

public class SewingDialogNsiReports extends BaseFxDialog {

    public SewingDialogNsiReports(JFrame owner) {
        super(owner, "Отчёт: Пошив");
    }

    @Override
    protected Parent createContent() {
        return new SewingViewNsiReports().createContent();
    }

    @Override
    protected void onFxReady(Parent root) {
        double w = root.prefWidth(-1);
        double h = root.prefHeight(-1);
        System.out.println("FX preferred size: " + w + " x " + h);
    }
}
