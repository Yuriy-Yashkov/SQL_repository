package by.march8.ecs.framework.helpers;

import java.util.Arrays;

/**
 * Вспомогательный класс для работы с учетными записями.
 *
 * Created by Andy on 12.09.2014.
 */
public class AccountUtils {

    /**
     * Метод проверяет корректность введенного пароля при входе в программу.
     * В случае совпадения паролей возвращает true.
     *
     * @param input    введенный пароль
     * @param original оригинальный пароль, хранящийся в базе
     * @return true если пароли совпадают
     */
    public static boolean passCheck(char[] input, char[] original) {
        return input.length == original.length && Arrays.equals(input, original);
    }
}
