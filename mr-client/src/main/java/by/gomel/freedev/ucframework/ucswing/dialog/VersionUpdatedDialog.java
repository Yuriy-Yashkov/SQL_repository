package by.gomel.freedev.ucframework.ucswing.dialog;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;

/**
 * @ author Andy 21.10.2015.
 */
public class VersionUpdatedDialog extends ExceptionDialog {

    public VersionUpdatedDialog(final MainController mainController) {
        super(mainController);
        btnDetail.setPreferredSize(Settings.BUTTON_BIG_SIZE);
        btnDetail.setText("Что нового в обновлении ?");
        btnCancel.setText("OK");
        setTitle("Новая версия");
    }

    public void setInformation(String text, String stringBuilder) {
        StringBuilder sBuilder = new StringBuilder();
        String result;
        int width = 325;

        text = "<font color=\"green\">Добро пожаловать в новую версию программы</font>" +
                "<p align=\"center\"><font color=\"black\">Версия сборки от </font>" +
                "<font color=\"blue\">" + text + "</font><p>";

        sBuilder.append(String.format("<h3 align=\"center\">%s</h3>", text));
        sBuilder.append("<h6 align=\"center\">Для получение полной информации об изменениях нажмите ЧТО НОВОГО В ОБНОВЛЕНИИ</h6>");
        result = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", width, sBuilder.toString());
        message.setText(result);

        messageDetail.setText("");
        messageDetail.append(stringBuilder);
        messageDetail.setCaretPosition(0);
    }

}
