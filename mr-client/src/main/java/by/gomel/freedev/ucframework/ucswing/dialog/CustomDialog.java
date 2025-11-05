package by.gomel.freedev.ucframework.ucswing.dialog;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;

/**
 * @author Andy 28.12.2015.
 */
public class CustomDialog extends ExceptionDialog {

    public CustomDialog(final MainController mainController) {
        super(mainController);
        btnDetail.setPreferredSize(Settings.BUTTON_BIG_SIZE);
        btnDetail.setText("Список EAN кодов");
        btnCancel.setText("OK");
        setTitle("ЕАН - коды отсутствуют в базе");
    }

    public void setInformation(String text, String stringBuilder) {
        StringBuilder sBuilder = new StringBuilder();
        String result;
        int width = 325;

        text = "<font color=\"green\">Ошибки в файле возвратов</font>" +
                "<p align=\"center\"><font color=\"black\">При проверке файла возвратов </font>" +
                "<font color=\"blue\">" + text + "</font><font color=\"black\"> обнаружены неизвестные EAN коды </font>";

        sBuilder.append(String.format("<h3 align=\"center\">%s</h3>", text));
        sBuilder.append("<h6 align=\"center\">Для получение полной информации нажмите СПИСОК EAN КОДОВ</h6>");
        result = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", width, sBuilder.toString());
        message.setText(result);

        messageDetail.setText("");
        messageDetail.append(stringBuilder);
        messageDetail.setCaretPosition(0);
    }

    public void setInformation(String textTitle, String textNote, String stringBuilder) {
        StringBuilder sBuilder = new StringBuilder();
        setTitle(textTitle);
        String result;
        btnDetail.setText("Подробности");
        int width = 325;

        sBuilder.append(String.format("<h3 align=\"center\">%s</h3>", textNote));
        sBuilder.append("<h6 align=\"center\">Для получение полной информации нажмите ПОДРОБНОСТИ</h6>");
        result = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", width, sBuilder.toString());
        message.setText(result);

        messageDetail.setText("");
        messageDetail.append(stringBuilder);
        messageDetail.setCaretPosition(0);
    }

    public void setInformation(String[] list) {
        StringBuilder sBuilder = new StringBuilder();
        String result;
        int width = 325;
        setTitle("Чтение данных из терминала");
        message.setText("Принятые данные с терминала");

        messageDetail.setText("");
        for (String s : list) {
            messageDetail.append(s + "\n");
        }
        messageDetail.setCaretPosition(0);
    }

}
