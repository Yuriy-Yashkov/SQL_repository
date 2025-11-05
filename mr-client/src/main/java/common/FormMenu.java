package common;

import javax.swing.*;
import java.util.Map;

/**
 * Рефакторинг интерфеса, документирование методов
 *
 * @author Andy
 *
 * @author vova
 * @version 13.08.2012
 * Рефакторинг
 */
public interface FormMenu {
    /**
     * Возвращает мапу меню
     * @return мапа пунктов меню для формы
     */
    Map<String, JMenuItem> getMenuMap();

    /**
     * Скрывает форму
     */
    void setFormVisible();

    /**
     * Уничтожает форму
     */
    void disposeForm();
}
