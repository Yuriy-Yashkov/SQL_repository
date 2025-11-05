package by.gomel.freedev.ucframework.ucswing.focus.events;

import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by Andy on 17.11.2014.
 * Класс обработки событий фокуса у компонента ComboboxPanel
 */
public class ComboBoxPanelFocusListener implements FocusListener {
    /**
     * Ссылка на компонент
     */
    private ComboBoxPanel cbpParent;
    /**
     * Бордюр активного редактируемого компонента
     */
    private Border bActiveEditable = new LineBorder(Color.GREEN.darker());
    /**
     * Бордюр активного не редактируемого компонента
     */
    @SuppressWarnings("unused")
    private Border bActiveNoEditable = new LineBorder(Color.red.brighter());
    /**
     * Бордюр не активного компонента
     */
    private Border bInActive = new LineBorder(Color.gray);

    /**
     * Конструктор обработчика
     * @param component ссылка на компонент ComboBoxPanel
     */
    public ComboBoxPanelFocusListener(final ComboBoxPanel component) {
        cbpParent = component;
    }


    public void focusGained(FocusEvent e) {
        cbpParent.setBorder(bActiveEditable);
    }

    public void focusLost(FocusEvent e) {
        cbpParent.setBorder(bInActive);
    }
}
