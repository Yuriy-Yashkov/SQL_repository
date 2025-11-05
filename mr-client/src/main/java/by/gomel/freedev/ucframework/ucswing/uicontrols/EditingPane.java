package by.gomel.freedev.ucframework.ucswing.uicontrols;


import by.gomel.freedev.ucframework.uccore.enums.RecordStatusType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditingPane;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IPhasedActivity;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.march8.ecs.MainController;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Абстрактный класс панели редактирования, в общем смысле представляе собой контейнер,
 * с расположенными на нем компонентами редактирования бина
 * : текстовые поля, лэйблы, комбобоксы и т.д. <p/>Через интерфейс IEditingPane реализуется основной
 * функционал: заполнение контролов данными из бина, валидатор, сохранение изм енений в бине,
 * возвращение измененного бина в контроллер
 */
public abstract class EditingPane<V, E> extends JPanel implements IEditingPane, IPhasedActivity {

    private static final long serialVersionUID = -2799999429843944418L;
    /**
     * Статус записи
     */
    protected RecordStatusType recordStatus = RecordStatusType.NOT_SAVED;
    /**
     * Права на справочник
     */
    protected RightEnum right;
    /**
     * Ссылка на интерфейс справочника
     */
    protected IReference reference;
    /**
     * Класс источник даных
     */
    protected E sourceEntityClass;
    /**
     * ССылка на главный контроллер приложения
     */
    protected MainController controller;
    protected int entityParentId;
    private Object option;
    private BaseEditorDialog frameViewPort;

    /**
     * Конструктор с сылкой на интерфейс справочника
     *
     * @param reference ссылка на справочник
     */
    public EditingPane(IReference reference) {
        super(null);
        this.reference = reference;
    }

    /**
     * Конструктор по-умолчанию
     */
    public EditingPane() {
        super(null);
        this.reference = null;
    }

    /**
     * Метод возвращает текущее состояние записи, по умолчанию - NOT_SAVED
     * Необходим(?) для сущностей связанных OneToMany типом
     */
    public RecordStatusType getRecordStatus() {
        return recordStatus;
    }

    /**
     * Метод заполнения компонента данными
     */
    @Override
    public void defaultFillingData() {

    }

    /**
     * Метод управляет доступностью компонентов панели в зависимости от уровня доступа пользователя
     *
     * @param component  компонент панели редактирования
     * @param isReadOnly флаг доступа
     */
    public void setEnablePanel(Component component, boolean isReadOnly) {

        if (component instanceof JTextComponent) {
            ((JTextComponent) component).setEditable(isReadOnly);
        }

        if (component instanceof JComboBox) {
            ((JComboBox) component).setEditable(isReadOnly);
        }

        if (component instanceof AbstractButton) {
            component.setEnabled(isReadOnly);
        }

        if (component instanceof ComboBoxPanel) {
            ComboBoxPanel cbp = (ComboBoxPanel) component;
            cbp.getComboBox().setEnabled(false);
            cbp.getComboBox().setEditable(false);
            cbp.getSelectButton().setVisible(isReadOnly);
            cbp.getClearButton().setVisible(isReadOnly);
            Component[] components = cbp.getComboBox().getComponents();
            for (Component comp : components) {
                if (comp instanceof AbstractButton) {
                    cbp.getComboBox().remove(comp);
                }
            }
        } else if (component instanceof JToolBar) {

        } else if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setEnablePanel(child, isReadOnly);
            }
        }
    }

    /**
     * Метод возвращает уровень доступа для панели
     *
     * @return уровень доступа
     */
    public RightEnum getRight() {
        return right;
    }

    /**
     * Устанавливает уровень доступа для панели
     *
     * @param right уровень доступа
     */
    public void setRight(final RightEnum right) {
        this.right = right;
    }

    /**
     * Метод дополнительного обновления данных в зависимости от уровня доступа к панели
     *
     * @param right уровень доступа
     */
    public void updateRights(RightEnum right) {

    }

    /**
     * Возвращает имя класса источника данных
     *
     * @return имя класса источника данных
     */
    public E getSourceEntityClass() {
        return sourceEntityClass;
    }

    /**
     * Устанавливает для панели имя класса - источника данных
     *
     * @param sourceEntityClass имя класса источника данных
     */
    public void setSourceEntityClass(final E sourceEntityClass) {
        this.sourceEntityClass = sourceEntityClass;
    }

    @Override
    public UCController getUCController() {
        return null;
    }

    /**
     * Предварительно метод имеет пустую реализацию, все отрабатывает "по-старинке"
     */
    @Override
    public void phaseBeforeShowing() {

    }

    public void setEntityParentId(final int entityParentId) {
        this.entityParentId = entityParentId;
    }

    protected BaseEditorDialog getFrameViewPort() {
        return frameViewPort;
    }

    // TODO Переписать под FrameViewPort обязательно !!!!!!!
    public void setFrameViewPort(final BaseEditorDialog frameViewPort) {
        this.frameViewPort = frameViewPort;
    }

    /**
     * Метод позволяет передать в панельредактирования дополнительную информацию
     * (надобность сомнительна, но пока никак)
     *
     * @param object объект, необходимый редактору
     */
    public void beforeEditing(Object object) {

    }

    public Object getOption() {
        return option;
    }

    public void setOption(final Object option) {
        this.option = option;
    }

    public void updateEditorContent(V item) {

    }
}
