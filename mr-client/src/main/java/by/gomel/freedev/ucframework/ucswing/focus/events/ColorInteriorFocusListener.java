package by.gomel.freedev.ucframework.ucswing.focus.events;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by Andy on 14.11.14.
 * Класс-событе обработки фокуса компонентом для окрашивания фона компонента
 */
public class ColorInteriorFocusListener implements FocusListener {
    /**
     * Свет фона активного компонента
     */
    private Color clrNormal;

    /**
     * Компонент получил фокус
     *
     * @param e компонент
     */
    public void focusGained(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        clrNormal = component.getBackground();
        if (component instanceof JTextComponent) {
            if (((JTextComponent) component).isEditable()) {
                component.setBackground(Color.red.brighter());
            } else {
                component.setBackground(Color.red.brighter());
            }
        }
    }

    /**
     * Компонент потерял фокус
     *
     * @param e компонент
     */
    public void focusLost(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        component.setBackground(clrNormal);
    }
}
