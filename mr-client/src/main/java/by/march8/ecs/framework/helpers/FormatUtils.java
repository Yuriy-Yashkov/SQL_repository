package by.march8.ecs.framework.helpers;

import javax.swing.text.MaskFormatter;

/**
 * Вспомогательный класс для работами с числовыми паттернами
 *
 * @author andy-linux
 */
public class FormatUtils {

    /**
     * Возвращает паттерн для артикула пряжи (9-ти значное число)
     */
    public static MaskFormatter getSarYarnFormat() {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter("9########");
        } catch (Exception e) {
            System.err.println("Ошибка метода getSarYarnFormat()" + e);
        }
        if (formatter != null) {
            formatter.setPlaceholderCharacter('0');
        }
        return formatter;
    }

    public static MaskFormatter getDateFormat() {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка метода getDateFormat()" + e);
        }
        if (formatter != null) {
            formatter.setPlaceholderCharacter('0');
        }
        return formatter;
    }

    /**
     * Возвращает паттерн исходя из шаблона, переданного параметром
     */
    public static MaskFormatter getCustomFormat(String format) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(format);
        } catch (Exception e) {
            System.err.println("Ошибка метода getCustomFormat()" + e);
        }
        if (formatter != null) {
            formatter.setPlaceholderCharacter('0');
        }
        return formatter;
    }

}
