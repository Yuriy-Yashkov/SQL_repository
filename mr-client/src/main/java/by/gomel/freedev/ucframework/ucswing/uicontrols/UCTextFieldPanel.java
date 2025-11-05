package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Компонент для выбора из справочника содержит ссылку на выбранную запись справочника,
 * и отображает ее в компоненте редактирования
 * Created by Andy on 23.11.14.
 */
public class UCTextFieldPanel<T> extends JPanel {

    private final JTextField textField = new JTextField();
    private final JButton btnSelect = new JButton();
    private final JButton btnClear = new JButton();
    private final JPanel pButton = new JPanel(new BorderLayout());
    private final MainController controller;
    private final MarchReferencesType reference;
    private T value;
    private String query;


    /**
     * Конструктор
     *
     * @param mainController ссылка на главный контроллер приложения
     * @param reference      тип справочника
     */
    public UCTextFieldPanel(MainController mainController, MarchReferencesType reference) {
        super(new BorderLayout());
        controller = mainController;
        this.reference = reference;
        initComponent(false);
    }

    public UCTextFieldPanel(MainController mainController, MarchReferencesType reference, String query) {
        super(new BorderLayout());
        controller = mainController;
        this.reference = reference;
        this.query = query;
        initComponent(false);
    }

    public UCTextFieldPanel(MainController mainController, MarchReferencesType reference, boolean isAuto) {
        super(new BorderLayout());
        controller = mainController;
        this.reference = reference;
        initComponent(isAuto);
    }

    /**
     * Конструктор по-умолчанию
     */
    public UCTextFieldPanel() {
        super(new BorderLayout());
        controller = null;
        this.reference = null;
        initComponent(false);
    }

    /**
     * Инициализация компонентов
     */
    private void initComponent(boolean isAuto) {
        setPreferredSize(new Dimension(120, 20));
        add(textField, BorderLayout.CENTER);
        add(pButton, BorderLayout.EAST);
        pButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        // pButton.setSize(new Dimension(20, 20));
        pButton.add(btnSelect, BorderLayout.WEST);
        pButton.add(btnClear, BorderLayout.EAST);
        btnSelect.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnClear.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/cancel.png", "Справочник"));
        btnSelect.setPreferredSize(new Dimension(20, 20));
        btnClear.setPreferredSize(new Dimension(20, 20));

        btnClear.setVisible(false);
        btnClear.setFocusable(false);
        btnSelect.setFocusable(false);

        btnClear.addActionListener(e -> clear());
        if (isAuto) {
            btnSelect.addActionListener(e -> selectFromReference());
        }
    }

    /**
     * Очистка содержимого компонента
     */
    public void clear() {
        value = null;
        textField.setText("");
    }

    /**
     * Предустановка компонента
     *
     * @param item объект
     */
    public void preset(T item) {
        if (item != null) {
            value = item;
            textField.setText(value.toString());
        }
    }

    /**
     * Активачия режима выбора из справочника
     *
     * @return выбранный из справочника объект
     */
    @SuppressWarnings("all")
    public T selectFromReference() {
        if ((reference == null) || (controller == null)) {
            return null;
        }
        final Reference ref = new Reference(controller, reference,
                MarchWindowType.PICKFRAME);

        final Object obj = ref.showPickFrame();
        if (obj != null) {
            textField.requestFocusInWindow();
            preset((T) obj);
            return (T) obj;
        } else {
            return null;
        }
    }

    /**
     * Активачия режима выбора из справочника
     *
     * @return выбранный из справочника объект
     */
    @SuppressWarnings("all")
    public T selectFromReference(Object object) {
        if ((reference == null) || (controller == null)) {
            return null;
        }

        System.out.println("SELECT FROM REFERENCE BY QUERY " + query);

        final Reference ref = new Reference(controller, reference,
                MarchWindowType.PICKFRAME, query);

        final Object obj = ref.selectFromReference(object);
        Object o = value;
        if (obj != null) {
            textField.requestFocusInWindow();
            preset((T) obj);
            return (T) obj;
        } else {
            preset((T) object);
            return null;
        }
    }

    /**
     * Возвращает объект содержащийся в компоненте
     *
     * @return объект содержащийся в компоненте
     */
    public T getValue() {
        return value;
    }

    /**
     * Добавляет событие на кнопку компонента
     *
     * @param listener ссылка на событие
     */
    public void addButtonSelectActionListener(final ActionListener listener) {
        btnSelect.addActionListener(listener);
    }

    public void setFocus() {
        textField.requestFocusInWindow();
    }

    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        textField.setEnabled(isEnabled);
        btnSelect.setEnabled(isEnabled);
    }

    public JTextField getEditor() {
        return textField;
    }

    public JButton getButtonSelect() {
        return btnSelect;
    }

    public String getText() {
        return getEditor().getText().trim();
    }

    public void setText(String text) {
        if (text == null) {
            text = "";
        }
        getEditor().setToolTipText(text);
        getEditor().setEditable(true);
        getEditor().setText(text);
    }

    public JButton getBtnClear() {
        return btnClear;
    }
}
