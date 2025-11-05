package by.gomel.freedev.ucframework.ucswing.focus.events;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Класс-событе обработки фокуса компонентом для окрашивания бордюра компонента
 */
public class ColorBorderFocusListener implements FocusListener {
    /**
     * Бордюр для редактируемого активного компонента
     */
    private Border bActiveEditable = new LineBorder(Color.green.darker());
    /**
     * Бордюр для активного но не редактируемого компонента
     */
    private Border bActiveNoEditable = new LineBorder(Color.red.brighter());
    /**
     * Бордюр не активного компонента
     */
    private Border bInActive = new LineBorder(Color.gray);

    /**
     * Компонент получил фокус
     * @param e событие
     */
    public void focusGained(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        if (component instanceof JTextComponent) {
            if (((JTextComponent) component).isEditable()) {
                component.setBorder(bActiveEditable);
            } else {
                component.setBorder(bActiveNoEditable);
            }
        }
    }

    /**
     * Компонент потерял фокус
     * @param e событие
     */
    public void focusLost(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        component.setBorder(bInActive);
    }
}
