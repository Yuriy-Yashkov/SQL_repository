package by.gomel.freedev.ucframework.ucswing.dialog;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.march8.ecs.MainController;

import javax.swing.*;
import java.io.File;

/**
 * Created by suvarov on 25.11.14.
 * Класс диалогов и сообщений
 */
public class Dialogs {
    /**
     * Надписи на кнопках
     */
    protected static Object[] options = {"Удалить",
            "Отмена"};

    protected static Object[] optionsQuestions = {"Получить курс валюты",
            "Нет"};
    protected static Object[] optionsQuestionsTerminal = {"Продолжить",
            "Отменить"};
    protected static Object[] optionsSaveQuestionsTerminal = {"Сохранить",
            "Не сохранять"};

    /**
     * Вспомогательные надписи на кнопках
     */
    protected static Object[] changeOptions = {"Перезаписать", "Отмена"};

    /**
     * Метод отображения диалога удаления элемента а также возвращения соотв. int значения
     *
     * @param references тип справочника
     * @param element    объект данных
     * @return модальный результат
     */
    public static int showDeleteDialog(MarchReferencesType references, Object element) {

        return JOptionPane.showOptionDialog(null,
                "<html><div style=\"text-align: center;\"> Удалить из справочника " + "<font color=\"green\">" +
                        references.getShortName() + "</font>" + " запись:" + "<br>" + "<font color=\"blue\">" + element + "</font>"
                        + " ?</html>", "Удаление записи",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }


    /**
     * Метод отображения диалога удаления элемента а также возвращения соотв. int значения
     *
     * @param element объект данных
     * @return модальный результат
     */
    public static int showDeleteDialog(Object element) {

        return JOptionPane.showOptionDialog(null,
                "<html><div style=\"text-align: center;\"> Удалить запись:" + "<br>" + "<font color=\"blue\">" + element + "</font>"
                        + " ?</html>", "Удаление записи",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    public static int showDeleteDialog() {

        return JOptionPane.showOptionDialog(null,
                "<html><div style=\"text-align: center;\"> Сейчас будут удалены записи, продолжить ?"
                        + " </html>", "Удаление записи",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsQuestionsTerminal, optionsQuestionsTerminal[0]);
    }

    /**
     * Метод отображения диалога перезаписи файла
     *
     * @param fileName имя фпйла
     * @return модальный результат
     */
    public static int showRewriteDialog(String fileName) {

        return JOptionPane.showOptionDialog(null,
                "<html><div style=\"text-align: center;\"> Вы действительно хотите перезаписать файл: " + "<font color=\"blue\">" +
                        fileName + "</font></html>", "Перезапись файла",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, changeOptions, changeOptions[0]);
    }

    public static int showSaveRecordDialog() {
        Object[] labels = {"Да", "Нет"};
        return JOptionPane.showOptionDialog(null,
                "<html><div style=\"text-align: center;\"> Запись была изменена, сохранить изменения ?" + "</html>", "Сохрание записи",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, labels, labels[0]);
    }

    public static int showDuplicateDialog(Object element) {
        JOptionPane.showMessageDialog(null,
                "<html><div style=\"text-align: center;\"> Прейскурант с именем " + "<font color=\"green\">" +
                        element + "</font>" + " уже существует в базе"
                        + "</html>", "Дублирование данных", JOptionPane.INFORMATION_MESSAGE);
        return 0;
    }

    public static int showQuestionCurrencyDialog(final String text) {
        return JOptionPane.showOptionDialog(null,
                text, "Вопрос",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsQuestions, optionsQuestions[0]);
    }

    /**
     * Диалог пользователю о произвольном вопросе
     *
     * @param messageText текст в заголовке диалога
     * @param titleText   текст сообщения
     * @return результат действий пользователя
     */
    public static int showQuestionDialog(final String messageText, final String titleText) {
        return JOptionPane.showOptionDialog(null,
                messageText, titleText,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsQuestionsTerminal, optionsQuestionsTerminal[0]);
    }

    public static int showSaveQuestionDialog(final String messageText, final String titleText) {
        return JOptionPane.showOptionDialog(null,
                messageText, titleText,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsQuestionsTerminal, optionsSaveQuestionsTerminal[0]);
    }

    public static int showQuestionDialog(final String messageText, final String titleText, Object[] params) {
        return JOptionPane.showOptionDialog(null,
                messageText, titleText,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, params, params);
    }

    public static void showInformationDialog(final String s) {
        JOptionPane.showMessageDialog(null, s);
    }

    public static String getDirectory(MainController controller) {

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выбор директории");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(controller.getMainForm());
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getPath() + File.separator;
        }

        return null;
    }
}
