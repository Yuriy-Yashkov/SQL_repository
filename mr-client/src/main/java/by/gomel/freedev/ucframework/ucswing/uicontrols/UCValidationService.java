package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.IUCComponent;

import javax.swing.*;

/**
 * Сервис предлагает проверку компонентов на корректность заполнения
 * Created by Andy on 23.03.2015.
 */
public class UCValidationService {
    public static final int WRONG_VALUE = 1;
    public static final int WRONG_LENGTH = 2;

    /**
     * Метод проверяет корректность данных
     *
     * @param value данные
     * @param type  тип данных
     * @return флаг результата проверки
     */
    @SuppressWarnings("all")
    public static boolean isCorrect(Object value, Class<?> type, int length) {
        if (type == String.class) {
            return value != null && !((String) value).trim().equals("");
        } else if (type == Integer.class) {
            if (value == null) {
                return false;
            } else {
                try {
                    Integer result = Integer.parseInt(String.valueOf(value).trim());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } else if (type == Float.class) {
            if (value == null) {
                return false;
            } else {
                try {
                    Float result = Float.parseFloat(String.valueOf(value).trim());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } else if (type == Double.class) {
            if (value == null) {
                return false;
            } else {
                try {
                    Double result = Double.parseDouble(String.valueOf(value).trim());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Метод проверяет корректность длины символов
     *
     * @param value  значение
     * @param length длина символов
     * @return флаг результата
     */
    public static boolean isConditionMet(Object value, int length) {
        if (value != null) {
            int length_ = String.valueOf(value).trim().length();
            if (length < 0) {
                // Минимальная длина
                return length_ >= (length * (-1));

            } else if (length > 0) {
                // Максимальная длина
                return length_ <= length;
            }
        }
        return false;
    }

    /**
     * Метод проверяет значение компонента как на содержание так и на длину и выдает сообщение пользователю
     *
     * @param component ссылка на интрефейс компонента
     * @return флаг результата
     */
    public static boolean doDataCheck(IUCComponent component) {
        boolean result;
        // Проверка на корректность данных
        result = UCValidationService.isCorrect(component.getValue(), component.getValueType(), component.getPresetValueLength());
        if (!result) {
            // Формирование сообщения пользователю
            createMessage(component, WRONG_VALUE);
            component.setFocus();
            return false;
        }

        // Проверка на длину
        if (component.getPresetValueLength() != 0) {
            result = UCValidationService.isConditionMet(component.getValue(), component.getPresetValueLength());
            if (!result) {
                // формирование сообщения пользователю
                createMessage(component, WRONG_LENGTH);
                component.setFocus();
                return false;
            }
        }

        if (component.getUCController() != null) {
            component.getUCController().getBaloon().hideBaloon();
        }

        return true;
    }


    private static boolean createMessage(IUCComponent component, int errorId) {
        String messageTemplate;
        String resultString = "";
        String label = "";

        if (component.getComponentLabel() != null) {
            label = "\"" + component.getComponentLabel().getText() + "\"";
        }

        switch (errorId) {
            case 1:
                messageTemplate = Template.MESSAGE_BAD_VALUE;
                resultString = String.format(messageTemplate, label);
                break;
            case 2:
                int length = component.getPresetValueLength();
                if (length > 0) {
                    messageTemplate = Template.MESSAGE_BAD_LENGTH_MAX;
                } else {
                    messageTemplate = Template.MESSAGE_BAD_LENGTH_MIN;
                    length *= -1;
                }
                resultString = String.format(messageTemplate, label, length);
                break;
            default:
        }

        if (component.getUCController() != null) {
            UCBaloon baloon = component.getUCController().getBaloon();
            baloon.showBaloon(component.getComponent(), resultString);
        } else {
            JOptionPane.showMessageDialog(null,
                    resultString, "Ошибка ввода",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}
