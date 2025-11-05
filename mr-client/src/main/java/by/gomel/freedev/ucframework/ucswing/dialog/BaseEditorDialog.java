package by.gomel.freedev.ucframework.ucswing.dialog;

import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RecordStatusType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.RecordEditorPane;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IButtonAction;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IButtonControl;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Базовый диалог для форм редактирования записи
 *
 * @author andy-linux
 */
@SuppressWarnings("serial")
public class BaseEditorDialog extends BaseDialog implements IButtonControl {

    /**
     * Вариант операции над записью
     */
    protected RecordOperationType recordOperation;

    /**
     * Панель контента
     */
    protected JPanel panelContent = null;
    protected IButtonAction buttonAction = null;
    /**
     * Ссылка на панель редактирования
     */
    private EditingPane editingPane = null;

    /**
     * Конструктор класса, фиксированнный размер формы
     *
     * @param controller      ссылка на главный контроллер приложения
     * @param recordOperation вариант операции над записью
     */
    public BaseEditorDialog(final MainController controller,
                            final RecordOperationType recordOperation) {
        super(controller, new Dimension(200, 200));
        this.recordOperation = recordOperation;
        setModeFrame(this.recordOperation);
        panelContent = new JPanel(new BorderLayout());
        panelCenter.add(panelContent, BorderLayout.CENTER);
        initEvents();
    }

    public BaseEditorDialog(final MainController controller, JFrame frame,
                            final RecordOperationType recordOperation) {
        super(controller, frame, new Dimension(200, 200));
        this.recordOperation = recordOperation;
        setModeFrame(this.recordOperation);
        panelContent = new JPanel(new BorderLayout());
        panelCenter.add(panelContent, BorderLayout.CENTER);
        initEvents();
    }

    /**
     * Конструктор класса, произвольный размер формы размер формы
     *
     * @param controller      ссылка на главный контроллер приложения
     * @param recordOperation вариант операции над записью
     * @param dimension       размер формы
     */
    public BaseEditorDialog(final MainController controller,
                            final RecordOperationType recordOperation, final Dimension dimension) {
        super(controller, dimension);
        this.recordOperation = recordOperation;
        setModeFrame(this.recordOperation);
        panelContent = new JPanel(null);
        panelCenter.add(panelContent, BorderLayout.CENTER);
        initEvents();
    }

    /**
     * Волзвращает вариант операции для текущей записи
     *
     * @return вариант операции над записью
     */
    public RecordStatusType getRecordStatus() {
        return editingPane.getRecordStatus();
    }

    /**
     * Устанавливает для формы панель редактирования
     *
     * @param pane ссылка на панель редактирования
     */
    public void setEditorPane(final EditingPane pane) {
        this.editingPane = pane;
        editingPane.setFrameViewPort(this);
        panelContent.add(pane, BorderLayout.CENTER);
        final Dimension size = pane.getPreferredSize();
        size.height += 35;
        this.setMinimumSize(size);
        this.pack();
        resizeFrame(size);
        // Инициализация методов перед отображением на экране

        pane.phaseBeforeShowing();
    }

    /**
     * Устанавливает для формы панель редактирования
     *
     * @param pane ссылка на панель редактирования
     */
    public void setEditorPane(final RecordEditorPane pane) {
        panelContent.add(pane, BorderLayout.CENTER);
        final Dimension size = pane.getPreferredSize();
        size.height += 35;
        this.setMinimumSize(size);
        this.pack();
        resizeFrame(size);
    }

    /**
     * Инициализация событий форм
     */
    private void initEvents() {

        getRootPane().registerKeyboardAction(e -> actionCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        btnCancel.addActionListener(e -> actionCancel());

        btnSave.addActionListener(e -> {
            if (editingPane != null) {
                if (!editingPane.verificationData()) {
                    return;
                }
            }

            if (buttonAction != null) {
                if (buttonAction.canSave()) {
                    modalResult = true;
                    setVisible(false);
                }
            } else {
                modalResult = true;
                setVisible(false);
            }
        });
    }

    private void actionCancel() {

        if (editingPane == null) {
            modalResult = false;
            setVisible(false);
            return;
        }

        if (editingPane.getUCController() != null) {
            if (editingPane.getUCController().verificationDataHasChanged()) {
                final int answer = Dialogs.showSaveRecordDialog();
                if (answer == 0) {
                    if (editingPane != null) {
                        if (!editingPane.verificationData()) {
                            return;
                        }
                    }
                    modalResult = true;
                    setVisible(false);
                } else {
                    modalResult = false;
                    setVisible(false);
                }
            } else {
                modalResult = false;
                setVisible(false);
            }
        } else {
            if (buttonAction != null) {
                if (buttonAction.canCancel()) {
                    modalResult = false;
                    setVisible(false);
                }
            } else {
                modalResult = false;
                setVisible(false);
            }
        }
    }

    /**
     * Метод устанавливает надписи для элементов управления в зависимости от
     * операции над текущей записью.(подписи к кнопкам, доступность кнопок, заголовок формы)
     *
     * @param recordOperation вариант операции над текущей записью
     */
    private void setModeFrame(final RecordOperationType recordOperation) {
        switch (recordOperation) {
            case VIEW: {
                setTitle("Просмотр записи");
                btnSave.setEnabled(false);
                btnCancel.setText("Закрыть");
                return;
            }
            case NEW: {
                setTitle("Добавить запись");
                btnSave.setText("Добавить");
                return;
            }
            case EDIT: {
                setTitle("Изменить запись");
                return;
            }

            case NEW_ID: {
                setTitle("Добавить запись");
                btnSave.setText("Добавить");
                return;
            }
            case EDIT_ID: {
                setTitle("Изменить запись");
                return;
            }
            case DELETE: {
                setTitle("Удалить запись");
                return;
            }
            case PICK: {
                setTitle("Выбрать запись");
                btnSave.setText("Выбрать");
                return;
            }
            case SELECT: {
                setTitle("Справочник");
                btnSave.setText("Сохранить");
                btnCancel.setText("Отмена");
            }

        }
    }

    /**
     * Устанавливает заголовок формы, отличный от метода
     * {@link by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog#setModeFrame(by.gomel.freedev.ucframework.uccore.enums.RecordOperationType)}
     *
     * @param title заголовок формы
     */
    public void setParentTitle(String title) {
        String tempTitle = getTitle();
        setTitle(tempTitle + ": " + title);
    }

    /**
     * Удаляет с формы панель редактирования
     */
    public void removeEditPane() {
        this.remove(editingPane);
        this.editingPane = null;
    }

    @Override
    public JButton getOkButton() {
        return btnSave;
    }

    @Override
    public JButton getCancelButton() {
        return btnCancel;
    }

    @Override
    public JPanel getButtonPanel() {
        return panelButton;
    }

    public void setButtonAction(final IButtonAction buttonAction) {
        this.buttonAction = buttonAction;
    }
}