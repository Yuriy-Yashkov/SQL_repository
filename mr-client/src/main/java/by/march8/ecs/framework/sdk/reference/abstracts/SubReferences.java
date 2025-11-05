package by.march8.ecs.framework.sdk.reference.abstracts;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IExternalEditEvents;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.ISubReferences;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Абстрактный класс компонента содержащего таблицу и панель инструментов
 * для управления данными поля-коллекции главного бина.
 * <p>Класс <code>FOO.class</code> имеет поле коллекцию типа: <code>public List\Example\ example;</code></p>
 * <p>Тогда данный компонент будет управлять содержимым этого поля, т.е. содержать коллекцию
 * объектов класса <code>Example</code></p> и управлять добавлением, редактированием и удалением в коллекции
 */
public abstract class SubReferences implements ISubReferences {
    /**
     * Область просмотра ТАБЛИЦА
     */
    protected GridViewPort gridViewPort;
    /**
     * Панель инструментов
     */
    protected UCToolBar toolBar = new UCToolBar();
    /**
     * Ссылка на главный контроллер приложения
     */
    protected MainController controller;
    /**
     * Панель редактирования записи
     */
    protected EditingPane editPane;
    /**
     * Ссылка на главный справочник
     */
    protected IReference reference;
    /**
     * Если компонент использует выбор из справочника
     */
    protected boolean isSelectFromReference = false;
    /**
     * Имя класса объекты которого будет обрабатывать данный компонент
     */
    protected Class<?> entity;
    /**
     * Интерфейс событий для операций над данными
     */
    private IExternalEditEvents editEvents = null;

    /**
     * Конструктор
     * @param reference ссылка на главный справочник
     * @param container панель-контейнер куда будет встроен данный компонент
     * @param c Имя класса объекты которого будет обрабатывать данный компонент
     */
    public SubReferences(final IReference reference, final Container container, Class<?> c) {
        this.reference = reference;
        controller = reference.getMainController();
        editPane = reference.getEditingPane();
        gridViewPort = new GridViewPort(c, false);
        entity = c;
        toolBar.setRight(reference.getRight());
        container.setLayout(new BorderLayout());
        container.add(gridViewPort, BorderLayout.CENTER);
        container.add(toolBar, BorderLayout.NORTH);
        toolBar.setVisibleSearchControls(false);
        toolBar.getBtnReport().setVisible(false);
        initEvents();
        gridViewPort.primaryInitialization();
    }

    /**
     * Инициализация событий компонента
     */
    private void initEvents() {
        toolBar.getBtnNewItem().addActionListener(e -> addRecord());
        toolBar.getBtnEditItem().addActionListener(e -> editRecord());
        toolBar.getBtnDeleteItem().addActionListener(e -> deleteRecord());

        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                editRecord();
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Object> getData() {
        return new HashSet<>(gridViewPort.getDataModel());
    }

    @Override
    public void setData(Collection<?> data) {
        gridViewPort.getDataModel().clear();
        if (data != null) {
            gridViewPort.getDataModel().addAll(data);
        }
        updateContent();
    }

    @Override
    public Collection<Object> getDataForListType() {
        return new ArrayList<>(gridViewPort.getDataModel());
    }

    /**
     * Событие на добавление ногвой записи
     */
    private void addRecord() {
        if (editEvents == null) {
            final BaseEditorDialog editor = new BaseEditorDialog(controller,
                    RecordOperationType.NEW);

            MarchReferencesType tempReferenceType = MarchReferencesType.getTypeByClassName(entity);
            if (tempReferenceType != null) {
                editor.setParentTitle(tempReferenceType.getShortName());
            }

            editPane.setSourceEntity(null);
            editor.setEditorPane(editPane);

            if (editor.showModal()) {
                gridViewPort.getDataModel().add(editPane.getSourceEntity());
                updateEntity(editPane.getSourceEntity());
                gridViewPort.setUpdatedObject(editPane.getSourceEntity());
                updateContent();
            }
            editor.dispose();
        }
    }

    /**
     * Событие на редактирование записи
     */
    private void editRecord() {
        if (editEvents == null) {
            final BaseEditorDialog editor = new BaseEditorDialog(controller,
                    RecordOperationType.EDIT);

            MarchReferencesType tempReferenceType = MarchReferencesType.getTypeByClassName(entity);
            if (tempReferenceType != null) {
                editor.setParentTitle(tempReferenceType.getShortName());
            }

            editPane.setSourceEntity(gridViewPort.getSelectedItem());
            editor.setEditorPane(editPane);
            if (editor.showModal()) {
                gridViewPort.getDataModel().set(gridViewPort.getTable().getSelectedRowIndex(), editPane.getSourceEntity());
                gridViewPort.setUpdatedObject(editPane.getSourceEntity());
                updateContent();
            }
            editor.dispose();
        }
    }

    /**
     * Событие на удаление записи
     */
    private void deleteRecord() {
        final int answer = JOptionPane.showOptionDialog(
                null,
                "Удалить запись?\n"
                        + gridViewPort.getSelectedItem().toString(),
                "Удаление записи",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Да", "Нет"},
                "Нет");

        if (answer == 0) {
            gridViewPort.setDeletedObject(gridViewPort.getSelectedItem());
            gridViewPort.getDataModel().remove(gridViewPort.getTable().getSelectedRowIndex());
            updateContent();
        }

    }

    /**
     * Событие на обновление данных компонента
     */
    protected void updateContent() {
        gridViewPort.updateViewPort();
        if (gridViewPort != null) {
            System.out.println("Права на справочник " + reference.getRight() + " Права на редактор " + editPane.getRight());
            toolBar.updateButton(gridViewPort.getDataModel().size());
        }
    }

    /**
     * Метод активации внешнего обработчика событий в компоненте
     * @param editEvents ссылка на интерфейс внешних событий
     */
    protected void activateExternalEditProcessing(final IExternalEditEvents editEvents) {
        this.editEvents = editEvents;
    }

    @Override
    public GridViewPort getGridViewPort() {
        return gridViewPort;
    }

    @Override
    public ArrayList<Object> getDataArray() {
        return gridViewPort.getDataModel();
    }
}
