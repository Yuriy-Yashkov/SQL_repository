package by.march8.ecs.application.modules.nsi.report;

import javafx.scene.Parent;

import javax.swing.*;

/**
 * Диалоговое окно для операции «Закройный цех» в модуле NSI Reports.
 * <p>
 * Класс является конкретной реализацией {@link BaseFxDialog} и
 * предоставляет JavaFX-контент, созданный через {@link RouteSheetsShopViewNsiReports}.
 * <p>
 * Диалог автоматически:
 * <ul>
 *     <li>создаёт JavaFX UI через {@link #createContent()};</li>
 *     <li>ожидает завершения JavaFX layout-процесса;</li>
 *     <li>получает фактические размеры корневой ноды во время {@link #onFxReady(Parent)};</li>
 *     <li>подгоняет размеры окна посредством {@code pack()} (выполняется в базовом классе).</li>
 * </ul>
 */
public class RouteSheetsDialogNsiReports extends BaseFxDialog {

    public RouteSheetsDialogNsiReports(JFrame owner) {
        super(owner, "Маршрутные листы");
    }

    @Override
    protected Parent createContent() {
        return new RouteSheetsShopViewNsiReports().createContent();
    }

    @Override
    protected void onFxReady(Parent root) {
        double w = root.prefWidth(-1);
        double h = root.prefHeight(-1);
    }
}

