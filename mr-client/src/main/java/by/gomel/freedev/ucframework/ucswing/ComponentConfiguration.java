package by.gomel.freedev.ucframework.ucswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Фабричный класс настроек компонентов
 *
 * @author suvarov
 *
 */
@SuppressWarnings("all")
public abstract class ComponentConfiguration {

    public final static Font font = new Font("Times New Roman", Font.BOLD, 14);
    public final static Font fontForComboBoxMemo = new Font("Courier New",
            Font.BOLD, 12);

    /**
     * Метод установки фонта для компонентов с переменным количеством аргументов
     *
     * @param c компонент
     */
    public static void fontPatch(Component... c) {
        for (final Component component : c) {
            component.setFont(font);
        }
    }

    /**
     * Метод установки табуляции по Enter
     * @param c компонент
     */
    public static void focusPatch(final Component... c) {
        for (final Component component : c) {
            Set keys = component.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
            Set newKeys = new HashSet(keys);
            newKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
            component.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newKeys);
        }
    }

    /**
     * Метод отключения фокуса для textArea и scrollPane, а также устанавливает
     * необходимые настройки(политика scrollBar-ов, перенос текста/запрет на
     * редактирование/прозрачный фон/шрифт в textArea и размер scrollPane).
     *
     * @param textArea
     * @param scrollPane
     */
    public static void textAreaComboBoxPatch(final Component textArea,
                                             final JScrollPane scrollPane) {

        /*
         * Set<KeyStroke> strokes = new
         * HashSet<KeyStroke>(Arrays.asList(KeyStroke
         * .getKeyStroke("pressed TAB"))); textArea.setFocusTraversalKeys(
         * KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes); strokes = new
         * HashSet<KeyStroke>(Arrays.asList(KeyStroke
         * .getKeyStroke("shift pressed TAB"))); textArea.setFocusTraversalKeys(
         * KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
         */

        ((JTextArea) textArea).setLineWrap(true);
        ((JTextArea) textArea).setEditable(false);
        ((JTextArea) textArea).setOpaque(false);
        ((JTextArea) textArea).setFont(fontForComboBoxMemo);
        ((JTextArea) textArea).setWrapStyleWord(true);
        ((JTextArea) textArea).setForeground(Color.GRAY);
        scrollPane.setPreferredSize(new Dimension(350, 45));
        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // textArea.setFocusable(false);
        textArea.setFocusable(false);
        scrollPane.setFocusable(false);
        scrollPane.grabFocus();

    }

    /**
     * Метод переопределяющий поведение клавиш tab и tab+Shift для textArea в
     * scrollPane, а также устанавливает необходимые настройки(политика
     * scrollBar-ов, перенос текста в textArea и размер scrollPane)
     *
     * @param textArea
     * @param scrollPane
     */
    public static void textAreaPatch(final Component textArea,
                                     final JScrollPane scrollPane) {
        Set<KeyStroke> strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke
                .getKeyStroke("pressed TAB")));
        textArea.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke
                .getKeyStroke("shift pressed TAB")));
        textArea.setFocusTraversalKeys(
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
        ((JTextArea) textArea).setLineWrap(true);
        scrollPane.setPreferredSize(new Dimension(350, 35));
        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    }

    /**
     * Метод проверяет тип операционной системы, если windows, возвращает true
     * @return
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        //windows
        return (os.indexOf("win") >= 0);
    }
}