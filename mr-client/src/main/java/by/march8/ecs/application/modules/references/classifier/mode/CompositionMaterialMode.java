package by.march8.ecs.application.modules.references.classifier.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.editor.CompositionMaterialEditor;
import by.march8.ecs.application.modules.references.classifier.ui.ProductionParameterMonitor;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.classifier.CompositionMaterial;
import by.march8.entities.classifier.CompositionMaterialView;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 19.11.2018 - 11:03.
 */
public class CompositionMaterialMode extends AbstractFunctionalMode<CompositionMaterialView> {

    private ClassifierModelParams activeItem;
    private ArrayList<CompositionMaterialView> data;
    private ProductionParameterMonitor pRates;

    private EditingPane editingPane;
    private UCToolBar toolBar;
    private JButton btnApplyAll;

    public CompositionMaterialMode(MainController mainController, ClassifierModelParams item) {
        controller = mainController;
        activeItem = item;
        modeName = "Сырьевой состав для изделия : " +
                activeItem.getName() + "(арт.№" +
                activeItem.getArticleName() + ", модель " +
                activeItem.getModelNumber() + ")";

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);
        toolBar.getBtnReport().setVisible(false);
        toolBar.getBtnNewItem().setEnabled(true);
        toolBar.getBtnEditItem().setEnabled(true);

        toolBar.registerEvents(this);

        gridViewPort = new GridViewPort<>(CompositionMaterialView.class, false);
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        frameViewPort.getButtonControl().getCancelButton().setText("Закрыть");
        frameViewPort.getButtonControl().getOkButton().setVisible(false);

        btnApplyAll = new JButton("");
        btnApplyAll.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/selectall_20.png", "Применить всем"));
        btnApplyAll.setToolTipText("Заполнить для всех размеров");

        toolBar.addSeparator();
        toolBar.add(btnApplyAll);

        pRates = new ProductionParameterMonitor(controller, item);
        pRates.setPreferredSize(new Dimension(0, 300));

        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().removeNotify();
        frameViewPort.getFrameRegion().getBottomContentPanel().add(pRates, BorderLayout.SOUTH);

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                editingPane = new CompositionMaterialEditor(frameViewPort);
                editingPane.setRight(RightEnum.WRITE);
                editingPane.setSourceEntityClass(CompositionMaterialView.class);
                return true;
            }
        }

        Task task = new Task("Инициализация редактора компонентов ...");
        task.executeTask();

        initEvents();
        updateContent();

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().setFrameSize(new Dimension(800, 600));
        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {
        btnApplyAll.addActionListener(a -> {
            final int answer = Dialogs.showQuestionDialog("Заполнить данный состав для всего ассортимента?", "Заполнение сырьевого состава");
            if (answer == 0) {
                pRates.applyComponentsForAll();
            }
        });


    }

    @Override
    public void updateContent() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {

                DaoFactory<CompositionMaterialView> factory = DaoFactory.getInstance();
                IGenericDaoGUI<CompositionMaterialView> dao = factory.getGenericDao();
                java.util.List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("productId", activeItem.getId()));
                List<CompositionMaterialView> list;
                data.clear();
                try {
                    list = dao.getEntityListByNamedQueryGUI(CompositionMaterialView.class, "CompositionMaterialView.findByProductId", criteria);
                    data.addAll(list);
                    System.out.println(data.size());
                    pRates.updateMaterialList(list);
                    toolBar.updateButton(data.size());
                    btnApplyAll.setEnabled(data.size() > 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                frameViewPort.updateContent();
                return true;
            }
        }

        Task task = new Task("Получение сырьевого состава...");
        task.executeTask();
    }

    @Override
    public void addRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle("Компонент в составе изделия");

        // СОздаем пустой объект вью-структуры для передачи кода изделия
        CompositionMaterialView item = new CompositionMaterialView();

        item.setProductId(activeItem.getId());

        editingPane.setSourceEntity(item);
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
        editor.setParentTitle("Компонент в составе изделия");

        editingPane.setSourceEntity(gridViewPort.getSelectedItem());
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
        // Запрос для юзера на удаление записи
        final int answer = Dialogs.showDeleteDialog(gridViewPort.getSelectedItem());
        if (answer == 0) {
            // Юзер нажал ДА
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // Получаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            // Удаляем запись из БД
            try {
                // Удаляем этот компонент для всех размеров изделия
                pRates.deleteComponents(gridViewPort.getSelectedItem());
                // Пометка на удаление в гриде
                gridViewPort.setDeletedObject(gridViewPort.getSelectedItem());
                // Запрос к DAO на удаление объекта
                dao.deleteEntity(CompositionMaterial.class,
                        gridViewPort.getSelectedItem().getId());
            } catch (final Exception e) {
                e.printStackTrace();
            }
            updateContent();
        }
    }
}
