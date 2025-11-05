package by.march8.ecs.application.modules.warehouse.external.shipping.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.BaseEntity;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.editor.ResponsiblePersonsEditor;
import by.march8.entities.warehouse.ResponsiblePersons;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Andy 13.12.2016.
 */
public class ResponsiblePersonsMode extends AbstractFunctionalMode {

    private ResponsiblePersonsEditor editingPane;
    private ArrayList<Object> data;
    private JComboBox cbPersonType = new JComboBox();
    private boolean export;


    public ResponsiblePersonsMode(MainController mainController, boolean isExport, int personType) {
        controller = mainController;
        export = isExport;
        modeName = "Справочник ответственных лиц";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);
        toolBar.registerEvents(this);
        toolBar.getBtnReport().setVisible(false);

        JPanel pFilterPanel = new JPanel(null);
        cbPersonType.setBounds(5, 7, 200, 20);
        pFilterPanel.add(cbPersonType);

        frameViewPort.getButtonControl().getOkButton().setVisible(false);
        frameViewPort.getButtonControl().getCancelButton().setText("Закрыть");

        pFilterPanel.setPreferredSize(new Dimension(240, 28));
        pFilterPanel.setOpaque(false);
        toolBar.add(pFilterPanel);

        toolBar.add(new Box(BoxLayout.X_AXIS));

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        gridViewPort = new GridViewPort(ResponsiblePersons.class, false);
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        comboBoxFiller(cbPersonType);
        cbPersonType.setSelectedIndex(personType);

        editingPane = new ResponsiblePersonsEditor(controller);
        editingPane.setRight(RightEnum.WRITE);
        //editingPane.setSourceEntityClass(ResponsiblePersons.class);

        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                ResponsiblePersons selectedItem = (ResponsiblePersons) object;
                if (selectedItem == null) {
                    toolBar.getBtnDeleteItem().setEnabled(false);
                    toolBar.getBtnEditItem().setEnabled(false);
                } else {
                    toolBar.getBtnDeleteItem().setEnabled(true);
                    toolBar.getBtnEditItem().setEnabled(true);
                }
            }
        });

        initEvent();

        updateContent();

        gridViewPort.primaryInitialization();

        frameViewPort.getFrameControl().showFrame();

    }

    public static void comboBoxFiller(final JComboBox comboBox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("Отпуск разрешил");
        array.add("Сдал грузоотправитель");
        array.add("К перевозке принял");
        array.add("Водитель");
        array.add("Транспортное средство");
        array.add("Доверенность выдана");
        array.add("Примечание");
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(0);
    }

    public void initEvent() {
        cbPersonType.addActionListener(a -> {
            updateContent();
        });
    }

    @Override
    public void addRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Новая запись");
        editingPane.setPersonType(cbPersonType.getSelectedIndex());
        editingPane.setExportJob(export);
        editingPane.setSourceEntity(null);

        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                // Сохраняем сущность в БД
                Object o = dao.saveEntity(editingPane.getSourceEntity());
                gridViewPort.setUpdatedObject(o);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    @Override
    public void editRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Редактирование записи");
        // Для созданного пустого диалога устанавливаем панель редактирования
        editingPane.setSourceEntity(gridViewPort.getSelectedItem());

        editor.setEditorPane(editingPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                // Обновляем сущность в БД
                Object o = editingPane.getSourceEntity();
                dao.updateEntity(o);
                gridViewPort.setUpdatedObject(o);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    @Override
    public void deleteRecord() {
        final int answer = Dialogs.showDeleteDialog();
        if (answer == 0) {
            // Юзер нажал ДА
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // Получаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            // Удаляем запись из БД
            try {
                // Получаем ID записи, и так как мы не знаем с какой именно
                // сущностью мы работам
                // Узнаем ID приведя объект к базовой сущности
                final BaseEntity baseEntity = (BaseEntity) gridViewPort.getSelectedItem();
                // Пометка на удаление в гриде
                gridViewPort.setDeletedObject(gridViewPort.getSelectedItem());
                // Запрос к DAO на удаление объекта
                dao.deleteEntity(ResponsiblePersons.class, baseEntity.getId());

            } catch (final Exception e) {
                e.printStackTrace();
            }
            // Обновляем содержимое грида
            updateContent();
        }
    }

    @Override
    public void updateContent() {
        DaoFactory<ResponsiblePersons> factory = DaoFactory.getInstance();
        IGenericDao<ResponsiblePersons> dao = factory.getGenericDao();

        try {
            java.util.List<QueryProperty> criteria = new ArrayList<>();
            criteria.add(new QueryProperty("type", cbPersonType.getSelectedIndex()));
            data.clear();
            data.addAll(dao.getEntityListByNamedQuery(ResponsiblePersons.class, "ResponsiblePersons.findByType", criteria));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // обновим грид
        int index = cbPersonType.getSelectedIndex();
        final TableColumnModel columnModel = gridViewPort.getTable().getColumnModel();
        int width;

        if (index > 3) {
            width = 0;
        } else {
            width = 250;
        }

        columnModel.getColumn(0).setMaxWidth(width);
        columnModel.getColumn(0).setMinWidth(width);
        columnModel.getColumn(0).setPreferredWidth(width);

        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
    }

}
