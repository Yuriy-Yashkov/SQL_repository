package by.gomel.freedev.ucframework.ucswing.dialog;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Базовый класс формы-диалога.
 *
 * @author andy-windows
 * @since 1.0.1
 */
@SuppressWarnings("serial")
public class BaseDialog extends JDialog {

    /**
     * The controller.
     */
    protected MainController controller = null;

    /**
     * Панель контента формы
     */
    protected JPanel pContent = null;

    /**
     * Верхняя панель формы
     */
    protected JPanel panelTop = null;

    /**
     * Центральная панель формы (верхний слой)
     */
    protected JPanel panelCenter = null;

    /**
     * Нижняя панель формы
     */
    protected JPanel panelBottom = null;
    /**
     * Панель кнопок
     */
    protected JPanel panelButton = null;
    /**
     * Центральная панель формы (нижний слой) включающий панель <code>panelBottom</code>
     */
    protected JPanel pCenter;

    /**
     * Кнопка Сохранить
     */
    protected JButton btnSave = null;

    /**
     * Кнопка Отмена
     */
    protected JButton btnCancel = null;

    /**
     * Результат модального открытия формы
     */
    protected boolean modalResult = false;

    /**
     * Конструктор диалога
     *
     * @param controller Ссылка на главный контроллер приложения
     * @param dimension  Размер формы по-умолчанию
     */
    public BaseDialog(MainController controller, Dimension dimension) {
        super(controller.getMainForm());
        this.setModalityType(ModalityType.DOCUMENT_MODAL);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.controller = controller;

        Dimension screenSize = controller.getMainForm().getSize();


        Point p = new Point(screenSize.width / 2 - dimension.width / 2,
                screenSize.height / 2 - dimension.height / 2);
        setSize(dimension);
        setLocation(p);
        setLocationRelativeTo(controller.getMainForm());
        setResizable(false);

        initFrame();
    }

    public BaseDialog(MainController controller, JFrame frame, Dimension dimension) {
        super(frame);
        this.setModalityType(ModalityType.DOCUMENT_MODAL);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.controller = controller;

        Dimension screenSize = frame.getSize();


        Point p = new Point(screenSize.width / 2 - dimension.width / 2,
                screenSize.height / 2 - dimension.height / 2);
        setSize(dimension);
        setLocation(p);
        setLocationRelativeTo(frame);
        setResizable(false);

        initFrame();
    }

    /**
     * Метод изменения размера формы
     *
     * @param size новый размер формы
     */
    protected void resizeFrame(Dimension size) {
        Dimension screenSize = controller.getMainForm().getSize();

        Point p = new Point(screenSize.width / 2 - size.width / 2,
                screenSize.height / 2 - size.height / 2);
        setSize(size);
        setLocation(p);
        setLocationRelativeTo(controller.getMainForm());
    }

    /**
     * Инициализация компонентов формы
     */
    private void initFrame() {
        pContent = new JPanel(new BorderLayout());
        pCenter = new JPanel(new BorderLayout());

        this.setContentPane(pContent);

        panelTop = new JPanel(new BorderLayout());
        pCenter.add(panelTop, BorderLayout.NORTH);

        panelCenter = new JPanel(new BorderLayout());
        panelCenter.setPreferredSize(new Dimension(0, 30));
        pCenter.add(panelCenter, BorderLayout.CENTER);
        panelBottom = new JPanel();
        pCenter.add(panelBottom, BorderLayout.SOUTH);

        panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelButton.setPreferredSize(new Dimension(0, 40));
        pContent.add(pCenter, BorderLayout.CENTER);
        pContent.add(panelButton, BorderLayout.SOUTH);

        btnSave = new JButton("Сохранить");
        btnSave.setPreferredSize(Settings.BUTTON_NORMAL_SIZE);

        btnCancel = new JButton("Отмена");
        btnCancel.setPreferredSize(Settings.BUTTON_NORMAL_SIZE);

        panelButton.add(btnSave);
        panelButton.add(btnCancel);

        initEvents();
    }

    /**
     * Метод инициализации событий компонентов формы
     */
    private void initEvents() {
        //Событие по нажатию Enter на кнопке btnSave
        btnCancel.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnCancel.doClick();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        //Событие по нажатию Enter на кнопке btnCancel
        btnSave.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSave.doClick();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * Метод открывает форму модально и возвращает результат <code>modalResult</code>
     * в зависимости от нажатой на форме кнопки.
     * <p><code>true</code> - нажата кнопка СОХРАНИТЬ</p>
     * <p><code>false</code> - нажата кнопка ОТМЕНА</p>
     *
     * @return true флаг модального закрытия формы
     */
    public boolean showModal() {
        this.setVisible(true);
        return modalResult;
    }

    /**
     * Метод возвращает ссылку на компонент
     *
     * @return кнопка Сохранить
     */
    public JButton getBtnSave() {
        return btnSave;
    }

    /**
     * Метод возвращает ссылку на компонент
     *
     * @return кнопка Отмена
     */
    public JButton getBtnCancel() {
        return btnCancel;
    }

    protected void deleteListener(JButton currentButton) {
        for (ActionListener listener : currentButton.getActionListeners()) {
            currentButton.removeActionListener(listener);
        }
    }
}
