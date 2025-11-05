package common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Класс позволяет перемещать фокус в произвольном направлении по компонентам
 * формы или диалога по клавише [TAB] или [ENTER]. Кроме того для компонента,
 * получившего фокус, происходит selectAll() для текста. После вызова
 * конструктора с аргументом JFrame или JDialod, нужную последовательность для
 * прохода по компонентам клавишей [TAB] или [ENTER] следует задавать методом
 * [addComponent(Component)]. Метод заполняет внутренний ArrayList<Component> по
 * порядку добавления
 *
 * @author Andy
 *
 * */
@SuppressWarnings("all")
public class TransitionController {
    private ArrayList<Component> componentList = new ArrayList<Component>();
    private int focussedComponentID = 0;
    private KeyStrokes keyStrokesAction = new KeyStrokes();
    private ComponentHaveFocus componentHaveFocusAction = new ComponentHaveFocus();

    /** Конструктор с аргументом JFrame */
    public TransitionController(JFrame parent) {
        componentList.clear();
        parent.setFocusTraversalPolicy(new FWTraversalPolicy());
        setFocusManager();
    }

    /** Конструктор с аргументом JDialog */
    public TransitionController(JDialog parent) {
        componentList.clear();
        parent.setFocusTraversalPolicy(new FWTraversalPolicy());
        setFocusManager();
    }

    /** Метод получает текущий диспетчер событий */
    private void setFocusManager() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent ke) {
                        if (ke.getID() == KeyEvent.KEY_PRESSED) {
                            if (ke.getKeyCode() == KeyEvent.VK_TAB) {
                                Component comp = KeyboardFocusManager
                                        .getCurrentKeyboardFocusManager()
                                        .getFocusOwner();
                                if (comp.isEnabled() == false) {
                                    if (ke.isShiftDown()) {
                                        KeyboardFocusManager
                                                .getCurrentKeyboardFocusManager()
                                                .focusPreviousComponent();
                                    } else {
                                        KeyboardFocusManager
                                                .getCurrentKeyboardFocusManager()
                                                .focusNextComponent();
                                    }
                                }
                            }
                        }
                        return false;
                    }
                });
    }

    /** Метод добавляет в private ArrayList ссылку на внешний компонент */
    public void addComponent(Component component) {
        componentList.add(component);
        component.addKeyListener(keyStrokesAction);
        component.addFocusListener(componentHaveFocusAction);
        if (component instanceof JFormattedTextField) {
            // Чего-то делаем для JFormattedTextField
        } else if (component instanceof JTextField) {
            // Чего-то делаем для JTextField
        }
    }

    /** Класс - правило для прохода по компоненту клавишей [TAB] */
    private class FWTraversalPolicy extends FocusTraversalPolicy {

        @Override
        public Component getComponentAfter(Container focusCycleRoot,
                                           Component aComponent) {
            focussedComponentID = (focussedComponentID + 1)
                    % componentList.size();
            return componentList.get(focussedComponentID);
        }

        @Override
        public Component getComponentBefore(Container focusCycleRoot,
                                            Component aComponent) {
            focussedComponentID = (componentList.size() + focussedComponentID - 1)
                    % componentList.size();
            return componentList.get(focussedComponentID);
        }

        @Override
        public Component getDefaultComponent(Container focusCycleRoot) {
            return componentList.get(0);
        }

        @Override
        public Component getLastComponent(Container focusCycleRoot) {
            return componentList.get(componentList.size() - 1);
        }

        @Override
        public Component getFirstComponent(Container focusCycleRoot) {
            return componentList.get(0);
        }
    }

    /** Слушатель при получении/потере компонентом фокуса */
    private class ComponentHaveFocus implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            final Component component = e.getComponent();
            if (component instanceof JTextField) {
                ((JTextField) component).selectAll();
            } else if (component instanceof JFormattedTextField) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        ((JFormattedTextField) component).selectAll();
                    }
                });
            }
        }

        @Override
        public void focusLost(FocusEvent e) {

        }

    }

    private class KeyStrokes implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager()
                        .focusNextComponent();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

    }

}
