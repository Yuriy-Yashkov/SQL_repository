package by.gomel.freedev.ucframework.ucswing.focus;

import by.gomel.freedev.ucframework.ucswing.focus.events.ColorBorderFocusListener;
import by.gomel.freedev.ucframework.ucswing.focus.events.ColorInteriorFocusListener;
import by.gomel.freedev.ucframework.ucswing.focus.events.ComboBoxPanelFocusListener;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;


/**
 * Created by Andy on 14.11.14.
 * Класс обработчик событий компонента SWING при получении илипотере фокуса
 */
public class FocusProcessing {
    /**
     * Слушатель для окрашивания фона компонента
     */
    ColorInteriorFocusListener listenerInterior = new ColorInteriorFocusListener();
    /**
     * Слушатель для окрашивания бордюра компонента
     */
    ColorBorderFocusListener listenerBorder = new ColorBorderFocusListener();
    /**
     * Бордюр не активного компонента
     */
    private Border bInActive = new LineBorder(Color.gray);

    /**
     * Метод устанавливает слушателя контейнеру типа JPanel
     *
     * @param container панель JPanel
     */
    public void setBorderColor(JPanel container) {
        setFocusListener(container, true);
    }

    /**
     * Метод устанавливает слушателя контейнеру типа JPanel
     *
     * @param container панель JPanel
     */
    @SuppressWarnings("all")
    public void setInteriorColor(JPanel container) {
        setFocusListener(container, false);
    }

    /**
     * Метод непосредственно инициализирует слушателя у компонента
     *
     * @param component     ссылка на компонент SWING
     * @param isColorBorder флаг бордюра. Если true - используется окраска
     *                      бордюра, иначе раскраска фона компонента
     */
    public void setFocusListener(Component component, boolean isColorBorder) {
        if (component instanceof JTextComponent) {
            if (isColorBorder) {
                component.addFocusListener(listenerBorder);
                ((JTextComponent) component).setBorder(bInActive);
            } else {
                component.addFocusListener(listenerInterior);
            }
        }

        /*if (component instanceof JComboBox) {
            if (isColorBorder) {
               // ((JComboBox)component).setRenderer(new ComboBOxRenderer());
            } else {
                //component.addFocusListener(listenerInterior);
            }
        }*/

        if (component instanceof ComboBoxPanel) {
            if (isColorBorder) {
                ((ComboBoxPanel) component).getComboBox().addFocusListener(
                        new ComboBoxPanelFocusListener((ComboBoxPanel) component));
                ((ComboBoxPanel) component).setBorder(bInActive);
            }
        } else {
            if (component instanceof Container) {
                for (Component child : ((Container) component).getComponents()) {
                    setFocusListener(child, isColorBorder);
                }
            }
        }
    }

}
