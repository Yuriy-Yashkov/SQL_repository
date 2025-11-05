package by.gomel.freedev.ucframework.ucswing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andy on 02.11.14.
 * Компонент-кнопка для управления запущенным функциональным режимом.
 * В планах добавить всплывающее меню для правой кнопки мыши
 */
public class IFrameButton extends JToggleButton {
    /**
     * Ссылка на форму функционального режима
     */
    private JInternalFrame frame;
    /**
     * Текст кнопки
     */
    private String text;

    /**
     * Конструктор компонента.
     * @param frame ссылка на форму режима
     */
    public IFrameButton(final JInternalFrame frame) {
        text = frame.getTitle();
        this.frame = frame;
        setToolTipText(text);
        setPreferredSize(new Dimension(200, 32));
        setText(text);
    }

    /**
     * Метод возвращает ссылку на форму связанную с этим компонентом
     * @return ссылка на форму режима
     */
    public JInternalFrame getFrame() {
        return frame;
    }

    /**
     * Устанавливает для компонента ссылку на форму режима
     * @param frame ссылка на форму режима
     */
    public void setFrame(JInternalFrame frame) {
        this.frame = frame;
    }

    /**
     * Метод возвращает текст компонента
     * @return текст компонента
     */
    public String getText() {
        return text;
    }

    /**
     * Метод устанавливает для компонента текст
     * @param text текст для компонента
     */
    public void setText(String text) {
        this.text = text;
    }
}
