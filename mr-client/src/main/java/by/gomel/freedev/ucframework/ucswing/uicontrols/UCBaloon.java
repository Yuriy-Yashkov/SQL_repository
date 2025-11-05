package by.gomel.freedev.ucframework.ucswing.uicontrols;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by andy-linux on 3/21/15.
 * Компонент выводит всплывающую форму с текстом ниже компонета
 * переданного в метод showBaloon()
 */
public class UCBaloon extends JWindow {

    private JLabel lblMessage = new JLabel();

    /**
     * Конструктор компонента формы сообщения
     *
     * @param component родительский компонент контейнер на верхнем уровне которого
     *                  и будет располагаться форма сообщения
     */
    public UCBaloon(Component component) {
        super(SwingUtilities.getWindowAncestor(component));
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.add(lblMessage);
        contentPane.setBackground(Color.white);
        contentPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setOpacity(0.8f);
        initEvents();
    }

    private void initEvents() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hideBaloon();
            }
        });
    }

    /**
     * Показать форму сообщения под компонентом component
     *
     * @param component компонент-цель
     * @param text      текст сообщения (поддерживает HTML)
     */
    public void showBaloon(JComponent component, String text) {
        lblMessage.setText("<html><p style=\"background-color:#FFE5F2\">" + text + "</p></html>");
        Point targetLocation = component.getLocationOnScreen();
        setLocation(targetLocation.x, targetLocation.y + 20);
        pack();
        setVisible(true);
    }

    /**
     * Метод скрывает форму сообщения
     */
    public void hideBaloon() {
        this.setVisible(false);
    }
}
