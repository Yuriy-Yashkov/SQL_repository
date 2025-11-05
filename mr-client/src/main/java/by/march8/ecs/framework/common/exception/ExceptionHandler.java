package by.march8.ecs.framework.common.exception;

import by.gomel.freedev.ucframework.ucswing.dialog.ExceptionDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.WorkSession;
import common.DateUtils;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

/**
 * Класс- обработчик исключений. Используется для централизованного
 * вывода исчерпывающей информации о возникшем исключении пользователю.
 * В будущем планируется автоматическая отправка отчета на E-mail или
 * баг-треккер
 */
public class ExceptionHandler {
    // Флаг оповещения о устаревшей версии приложения
    public static boolean notificationOutOfDate = false;
    /**
     * Главный контроллер приложения
     */
    private MainController controller;

    /**
     * Конструктор обработчика исключений
     *
     * @param mainController главный контроллер приложения
     */
    public ExceptionHandler(MainController mainController) {
        controller = mainController;
    }

    /**
     * Вызов диалога исключения и вывод подробной информации
     *
     * @param e    ссылка на исключение
     * @param text содержание сообщения
     */
    public void showMessage(final Throwable e, String text) {
        e.printStackTrace();
        ExceptionDialog exceptionDialog = new ExceptionDialog(controller, e);
        exceptionDialog.setTitle(getTypeException(e));
        exceptionDialog.setExceptionInformation(generateExceptionInformation(text));
        exceptionDialog.setExceptionDetail(generateExceptionDetail(e));
        exceptionDialog.setVisible(true);
    }

    /**
     * Метод генерации сообщения
     *
     * @param text Входящее сообщение
     * @return HTML форматированная строка сообщения
     */
    private String generateExceptionInformation(String text) {
        StringBuilder message = new StringBuilder();
        String result;
        int width = 325;

        if (text.trim().equals("")) {
            text = "<font color=\"red\">Ааааааааааа, все пропало!!!</font>\" \n" +
                    "                \"<font color=\\\"green\\\"> Что-то тут явно пошло не так</font>\" +\n" +
                    "                \" и наконец действия оператора привели к ошибке в программе.";
        } else {
            text = "<h3 align=\"center\"><font color=\"red\">" + text + "</font>";
        }
        message.append(String.format("<h3 align=\"center\">%s</h3>", text));
        message.append("<h6 align=\"center\">Для получение полной информации об ошибке нажмите ДЕТАЛИ</h6>");
        result = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", width, message.toString());
        return result;
    }

    /**
     * Метод генерации детальной информации о возникшем исключении
     *
     * @param e ссылка на исключение
     * @return сформированный отчет о исключении
     */
    private StringBuilder generateExceptionDetail(final Throwable e) {
        StringBuilder result = new StringBuilder();
        // ***********************************************************
        // Заполнение отчета об ошибке
        // ***********************************************************
        Throwable rootCause = e;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause)
            rootCause = rootCause.getCause();
        rootCause.getMessage();
        result.append("******************* СЕССИЯ ******************\n");
        result.append(String.format("Дата : %s\n", DateUtils.getDateNow()));

        WorkSession session = controller.getWorkSession();
        if (session != null) {
            result.append(String.format("Пользователь : %s\n", session.getUser().getUserLogin()));
        }
        result.append("***************** ИСКЛЮЧЕНИЕ ****************\n");
        result.append(String.format("Класс-источник: %s\n", (rootCause.getStackTrace()[0].getClassName())));
        result.append(String.format("Метод-источник: %s\n", (rootCause.getStackTrace()[0].getMethodName())));
        result.append(String.format("Класс ошибки: %s\n", (rootCause.getClass())));
        result.append(String.format("Сообщение: %s\n", (rootCause.getMessage())));
        result.append("******************** ДАМП *******************\n");
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        result.append(errors.toString());
        result.append("*********************************************\n");
        return result;
    }

    /**
     * Проттотип классификатора исключений. Пока только базовые возможности
     *
     * @param e ссылка на исключение
     * @return строка типа исключения
     */
    private String getTypeException(final Throwable e) {
        if (e instanceof SQLException) {
            return "Ошибка базы данных";
        }
        return "Внутренняя ошибка программы";
    }

    /**
     * Временно выводи стандартный диалог для сообщения
     */
    public void showApplicationOutOfDate() {
        notificationOutOfDate = true;
        JOptionPane.showMessageDialog(null, "Обнаружена новая версия программы MyReports. Стабильность работы данного экземпляра не гарантируется.\nПерезагрузите программу");
    }
}
