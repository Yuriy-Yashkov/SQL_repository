package by.gomel.freedev.ucframework.ucswing.uicontrols;

import java.awt.*;

/**
 * Константный класс содержит шаблоны сообщений выдаваемых пользователю
 * Created by Andy on 23.03.2015.
 */
@SuppressWarnings("all")
public class Template {
    /**
     * Некорректные данные
     */
    public static final String MESSAGE_BAD_VALUE = "Ошибка заполнения поля %s";
    /**
     * Длина символов больше чем в условии
     */
    public static final String MESSAGE_BAD_LENGTH_MAX = "Максимальная длина поля %s %d символов";

    /**
     * Длина символов меньше чем в условии
     */
    public static final String MESSAGE_BAD_LENGTH_MIN = "Минимальная длина поля %s %d символов";

    public static final String REGEX_INTEGER = "^[0-9]([0-9]*)$";
    public static final String REGEX_FLOAT = "^([+-]?\\d*(\\.?)\\d*)$";
    public static final String REGEX_EMPTY = "";


    public static Color requiredFieldColor = Color.decode("#F2FFF6");
}
