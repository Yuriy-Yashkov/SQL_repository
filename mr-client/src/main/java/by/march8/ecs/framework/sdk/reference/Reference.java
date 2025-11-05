package by.march8.ecs.framework.sdk.reference;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RecordStatusType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditableModule;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReferenceRegion;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IButtonControl;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IMultiSelector;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.focus.FocusProcessing;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.BaseEntity;
import by.march8.api.DialogFrameSize;
import by.march8.api.EntityControl;
import by.march8.api.ISimpleReference;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Основной класс справочника, генерирует форму, модель таблицы для отображения
 * данных из сущности, обрабатывает основные операции для работы с записью (
 * создание, редактирование, удаление)
 * <p>
 * Экземпляр данного класса создается как независимый объект. Поведение и состав
 * зависит от аргументов в конструкторе данного класса
 */
public class Reference implements IReference {
    /**
     * Ссылка на основной контроллер приложения
     */
    private MainController controller = null;
    /**
     * Коллекция объектов загружаемых из базы данных
     */
    private ArrayList<Object> data;
    /**
     * Тип справочника для конкретного экземпляра данного класса
     */
    private MarchReferencesType references = null;
    /**
     * Панель редактирования для спрапвочника
     */
    private EditingPane editPane = null;
    private ControlPane controlPane = null;
    private RightEnum right;
    private GridViewPort gridViewPort;
    private FrameViewPort viewPort;
    //public static HashMap parameters = new HashMap<>();
    private IReference linkReference;
    private boolean canceled = false;
    private Class<?> viewEntity = null;
    private String customQuery = null;
    private int entityParentId = -1;

    /**
     * Конструктор класса
     *
     * @param mainController ссылка на главный контроллер приложения
     * @param referencesType тип справочника, перечислитель
     * @param winType        тип формы, в котором справочник будет показан
     *                       (Диалог/Внутренняя форма)
     */
    public Reference(final MainController mainController,
                     final MarchReferencesType referencesType, final MarchWindowType winType, String query) {
        customQuery = query;
        linkReference = this;

        class Task extends BackgroundTask {

            public Task(final String messageText) {
                super(messageText, true);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                controller = mainController;
                references = referencesType;
                // Получаем права на этот справочник
                //right = controller.getRight(getReferences().getShortName());
                right = RightEnum.WRITE;

                // По интерфесу получаем необходимые области справочника
                ReferenceFactory refFactory = new ReferenceFactory(linkReference);

                IReferenceRegion referenceRegion = refFactory.getReferenceRegion();
                // Область редактирования, если есть
                editPane = referenceRegion.getEditingPane();
                // Область контроля, если есть
                controlPane = referenceRegion.getControlPane();

                // Если для справочника определена панель редактирования, и она сформирована
                if (editPane != null) {
                    editPane.setRight(right);
                    FocusProcessing focusProcessing = new FocusProcessing();
                    focusProcessing.setBorderColor(editPane);
                }

                // Попытка получить панель управления бином через рефлексию
                if (controlPane == null) {
                    Class<?> entityClass = referencesType.getClassifierClass();
                    if (entityClass.isAnnotationPresent(EntityControl.class)) {
                        try {
                            EntityControl eControl = entityClass.getAnnotation(EntityControl.class);
                            Class<?> cpClass = eControl.control();
                            if (eControl.control() != Class.class) {
                                Constructor<?> cpConstructor;
                                cpConstructor = cpClass.getConstructor();
                                controlPane = (ControlPane) cpConstructor.newInstance();
                                referenceRegion.setControlPane(controlPane);
                            }
                        } catch (Exception e) {
                            MainController.exception(e, "Ошибка создания панели управления для класса " + entityClass);
                        }
                    }
                }

                // Экспериментальный этап получения класса представления для сущности
                if (referencesType.getClassifierClass().isAnnotationPresent(EntityControl.class)) {
                    // Если класс аннотирован, получаем имя класса представления, если он отличный от Class.class
                    EntityControl eControl = referencesType.getClassifierClass().getAnnotation(EntityControl.class);
                    if (eControl.viewEntity() == Class.class) {
                        // резерв
                        System.out.println("Сущность просмотра определена по-умолчанию");
                    } else {
                        viewEntity = eControl.viewEntity();
                    }
                }

                // Уличная магия выводит работу со справочником на новый уровень
                // Формируем абстрактную область просмотра ОКНО
                if (controlPane == null) {
                    viewPort = new FrameViewPort(controller, winType);
                } else {
                    viewPort = new FrameViewPort(controller, winType, linkReference);
                }

                // Устанавливаем титульник в заголовок
                viewPort.getFrameControl().setTitleFrame(references.getReferenceName());
                // Устанавливаем тулбар и определяем права на действия в нем
                UCToolBar toolBar = viewPort.getFrameRegion().getToolBar();
                toolBar.setRight(right);
                toolBar.registerEvents(linkReference);

                // Формируем табличную область просмотра в ОКНЕ
                // Если предполагается множественный выбор из справочника
                if (winType == MarchWindowType.PICKCHECK) {
                    viewPort.setGridViewPort(new GridViewPort(getViewEntity(), true, true));
                } else if (winType == MarchWindowType.PICKFRAME) {
                    viewPort.setGridViewPort(new GridViewPort(getViewEntity(), false, true));
                } else {
                    // Если это обычная работа со справочником
                    viewPort.setGridViewPort(new GridViewPort(getViewEntity(), false));
                }

                // Изменение размеров формы
                if (winType != MarchWindowType.INTERNALFRAME) {
                    if (referencesType.getClassifierClass().isAnnotationPresent(DialogFrameSize.class)) {
                        DialogFrameSize dialogSize = getViewEntity().getAnnotation(DialogFrameSize.class);
                        if ((dialogSize.width() > 0) && (dialogSize.height() > 0)) {
                            Dimension frameSize = new Dimension(dialogSize.width(), dialogSize.height());
                            viewPort.getFrameControl().setFrameSize(frameSize);
                        }
                    }
                }

                if (controlPane != null) {
                    controlPane.beforeEmbedding(viewPort);
                }

                gridViewPort = viewPort.getGridViewPort();
                gridViewPort.initialFooter();

                // Коллекцию берем из вьюпорта
                data = viewPort.getGridViewPort().getDataModel();
                // Первичная загрузка из БД
                updateContent();

                // Устанавливаем курсор в нулевую позицию грида
                gridViewPort.primaryInitialization();
                // Регистрируем событие мыши на двойной клик
                if ((winType == MarchWindowType.INTERNALFRAME) || (winType == MarchWindowType.DIALOG)) {
                    gridViewPort.setTableEventHandler(new TableEventAdapter() {
                        @Override
                        public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                            editRecord();
                        }
                    });
                    viewPort.getFrameControl().showFrame();
                } else {
                    final IButtonControl buttonControl = viewPort.getButtonControl();
                    if (buttonControl != null) {
                        gridViewPort.setTableEventHandler(new TableEventAdapter() {
                            @Override
                            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                                buttonControl.getOkButton().doClick();
                            }
                        });
                    }
                }
                if (controlPane != null) {
                    controlPane.afterEmbedding();
                }
                return true;
            }
        }

        Task task = new Task("Инициализация справочника...");
        try {
            task.executeTask();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка инициализации справочника " + references.getShortName());
        }
    }

    public Reference(final MainController mainController,
                     final MarchReferencesType referencesType, final MarchWindowType winType) {
        this(mainController, referencesType, winType, null);
    }

    /**
     * Событие, при нажатии на кнопку НОВАЯ ЗАПИСЬ в справочнике. В данном
     * методе, кроме прямого назначения, происходит получение интерфейса
     * ISimpleReference у класса для того, что-бы иметь возможность
     * редактировать запись для справочников имеющих одинаковую структуру
     * (id/name/note)
     */
    @Override
    public void addRecord() {
        // СОздаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle(getReferences().getShortName());
        // Получаем класс сущности справочника
        final Class sourceClass = references.getClassifierClass();
        Object source;
        try {
            // пытаемся созать объект данного класса
            source = sourceClass.newInstance();
            // ПРоверка , на реализацию интерфейса ISimpleReference
            if (source instanceof ISimpleReference) {
                // Интерфейс классом реализован
                editPane.setSourceEntity(source);
            } else {
                // Очередной костыль если владелец записи OneToMany не замаплен а указывается явно
                if (entityParentId != -1) {
                    editPane.setEntityParentId(entityParentId);
                }
                // Интерфейс классом не реализуется
                editPane.setSourceEntity(null);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            // Очередной костыль если владелец записи OneToMany не замаплен а указывается явно
            if (entityParentId != -1) {
                editPane.setEntityParentId(entityParentId);
            }
            editPane.setSourceEntity(null);
        }
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            // Если запись не сохранялась (для справочников простой структуры)
            if (editor.getRecordStatus() == RecordStatusType.NOT_SAVED) {
                try {
                    // Сохраняем сущность в БД
                    Object o = dao.saveEntity(editPane.getSourceEntity());
                    gridViewPort.setUpdatedObject(o);
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    // Обновляем сущность в БД
                    dao.updateEntity(editPane.getSourceEntity());
                    gridViewPort.setUpdatedObject(editPane.getSourceEntity());
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
            // ПОсле сохранения или обновления данных обновляем содержимое грида
            updateContent();
            canceled = false;
        } else {
            canceled = true;
        }
        // диспозим окно диалога
        editor.dispose();
        editor.removeEditPane();
    }

    /**
     * Событие, при нажатии на кнопку ИЗМЕНИТЬ ЗАПИСЬ в справочнике.
     */
    @Override
    public void editRecord(final int selectedRow) {
        editRecord();
    }

    @Override
    public void editRecord() {
        if (right == RightEnum.WRITE) {
            if (editPane == null) {
                return;
            }
            // Создаем пустую форму диалога
            BaseEditorDialog editor = new BaseEditorDialog(controller,
                    RecordOperationType.EDIT);
            editor.setParentTitle(getReferences().getShortName());
            if (viewEntity != null) {
                BaseEntity baseObject = (BaseEntity) viewPort.getGridViewPort().getSelectedItem();
                Object entity;
                try {
                    final DaoFactory factory = DaoFactory.getInstance();
                    // ПОлучаем интерфейс для работы с БД
                    final ICommonDao dao = factory.getCommonDao();
                    entity = dao.getEntityById(references.getClassifierClass(), baseObject.getId());
                    editPane.setSourceEntity(entity);
                    // dao.commitTransaction();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    final DaoFactory factory = DaoFactory.getInstance();
                    // ПОлучаем интерфейс для работы с БД
                    final ICommonDao dao = factory.getCommonDao();
                    Object entity = dao.getEntityById(references.getClassifierClass(), ((BaseEntity) gridViewPort.getSelectedItem()).getId());
                    editPane.setSourceEntity(entity);
                    // dao.commitTransaction();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Передаем на панель редактирования объект редактирования
                // editPane.setSourceEntity(gridViewPort.getSelectedItem());
            }

            // Для созданного пустого диалога устанавливаем панель редактирования
            editor.setEditorPane(editPane);
            // Модально показываем форму и ожидаем закрытия
            if (editor.showModal()) {
                // Форма закрыта со значением true
                // Получаем DAO слой
                final DaoFactory factory = DaoFactory.getInstance();
                // ПОлучаем интерфейс для работы с БД
                final ICommonDao dao = factory.getCommonDao();
                try {
                    gridViewPort.setUpdatedObject(gridViewPort.getSelectedItem());
                    // Обновляем запись в БД
                    dao.updateEntity(editPane.getSourceEntity());
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
                // ПОсле сохранения данных обновляем содержимое грида
                updateContent();
            }
            // диспозим окно диалога
            editor.dispose();
            editor.removeEditPane();
        } else {
            viewRecord();
        }
        canceled = false;
    }

    /**
     * Событие, при нажатии на кнопку УДАЛИТЬ ЗАПИСЬ в справочнике.
     */
    @Override
    public void deleteRecord(final int selectedRow) {
        deleteRecord();
    }

    @Override
    public void deleteRecord() {
        // Запрос для юзера на удаление записи
        final int answer = Dialogs.showDeleteDialog(references, gridViewPort.getSelectedItem());
        if (answer == 0) {
            // Юзер нажал ДА
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // Получаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            // Удаляем запись из БД
            try {
                if (viewEntity != null) {
                    final BaseEntity baseEntity = (BaseEntity) viewPort.getGridViewPort().getSelectedItem();
                    // Пометка на удаление в гриде
                    viewPort.getGridViewPort().setDeletedObject(viewPort.getGridViewPort().getSelectedItem());
                    // Запрос к DAO на удаление объекта
                    dao.deleteEntity(references.getClassifierClass(),
                            baseEntity.getId());
                } else {
                    // Получаем ID записи, и так как мы не знаем с какой именно
                    // сущностью мы работам
                    // Узнаем ID приведя объект к базовой сущности
                    final BaseEntity baseEntity = (BaseEntity) gridViewPort.getSelectedItem();
                    // Пометка на удаление в гриде
                    gridViewPort.setDeletedObject(gridViewPort.getSelectedItem());
                    // Запрос к DAO на удаление объекта
                    dao.deleteEntity(references.getClassifierClass(),
                            baseEntity.getId());
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            // Обновляем содержимое грида
            updateContent();
        }
        canceled = false;
    }

    /**
     * Событие, при нажатии на кнопку ПРОСМОТР ЗАПИСИ в справочнике. Метод пока
     * не реализован в полной мере, надо он вообще. Позволяет просматривать
     * данные по записи в ReadOnly
     */
    @Override
    public void viewRecord(final int selectedRow) {
        viewRecord();
    }

    @Override
    public void viewRecord() {
        if (editPane == null) {
            return;
        }
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.VIEW);
        // Передаем на панель редактирования объект редактирования
        editPane.setSourceEntity(gridViewPort.getSelectedItem());
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editPane);
        editPane.setEnablePanel(editPane, false);
        editor.showModal();
        editor.dispose();
        editor.removeEditPane();
    }

    /**
     * Метод обновляет коллекцию объектов справочника и содержимое грида
     */
    @Override
    public void updateContent() {
        final DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();

        try {
            data.clear();
            //data.addAll(dao.getAllEntityThread(references.getClassifierClass()));
            if (customQuery == null) {
                data.addAll(dao.getAllEntityThread(getViewEntity()));
            } else {
                data.addAll(dao.getAllEntityByStringQueryThread(getViewEntity(), customQuery));
            }
            //dao.closeEntityManager(references.getClassifierClass());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        gridViewPort.setFooterValue("Всего записей: " + gridViewPort.getDataModel().size());
        viewPort.updateContent();
    }

    @Override
    public void referenceToReport() {

    }

    @Override
    public ControlPane getControlPane() {
        return controlPane;
    }

    /**
     * Метод возвращает коллекцию объектов справочника
     */
    @Override
    public ArrayList<Object> getDataCollection() {
        return data;
    }

    @Override
    public IEditableModule getEditableModule() {
        return this;
    }

    /**
     * Метод возвращает панель редактирования для текущего справочника
     */
    @Override
    public EditingPane getEditingPane() {
        return editPane;
    }

    @Override
    public MainController getMainController() {
        return controller;
    }

    /**
     * Метод возвращает тип справочника с которым работает текущий экземпляр
     * Reference
     */
    @Override
    public MarchReferencesType getReferences() {
        return references;
    }

    /**
     * Метод для работы со справочником, как с формой выбора из списка.
     * Возвращает выбранную в таблице запись как Object
     */
    public Object showPickFrame() {
        if (viewPort.getFrameControl().showModalFrame()) {
            return gridViewPort.getSelectedItem();
        }
        return null;
    }

    /**
     * Возвращает коллекцию выбранных записей
     *
     * @return коллекция
     */
    public Set<Object> selectFromReference() {
        if (viewPort.getFrameControl().showModalFrame()) {
            IMultiSelector multiSelector = gridViewPort.getMultiSelector();
            return multiSelector.getSelectedItems();
        }
        return null;
    }

    /**
     * Метод предустанавливает курсор грида на объект переданный аргументом
     */
    public Object selectFromReference(Object presetObject) {
        viewPort.getGridViewPort().preset(presetObject);
        if (viewPort.getFrameControl().showModalFrame()) {
            return gridViewPort.getSelectedItem();
        }
        return null;
    }

    /**
     * Метод предустанавливает чекбоксы для грида справочника согласно коллекции аргумента
     */
    public Set<Object> selectFromReference(final ArrayList<Object> collection) {
        viewPort.getGridViewPort().preset(collection);
        if (viewPort.getFrameControl().showModalFrame()) {
            IMultiSelector multiSelector = gridViewPort.getMultiSelector();
            return multiSelector.getSelectedItems();
        }
        return null;
    }

    @Override
    public RightEnum getRight() {
        return right;
    }

    /**
     * Возвращает активную (текущую) запись
     *
     * @return текущая запись
     */
    public Object getActiveRecord() {
        return viewPort.getGridViewPort().getSelectedItem();
    }

    /**
     * Метод скрытого редактирования объекта в справочнике без отображения самого справочника на экране
     * СОМНИТЕЛЬНА НАДОБНОСТЬ
     *
     * @param object редактируемый объект
     * @return объект
     */
    public Object doEditActiveRecord(Object object) {
        viewPort.getGridViewPort().preset(object);
        editRecord();
        return getActiveRecord();
    }

    public boolean isCanceled() {
        return canceled;
    }


    /**
     * Возвращает имя класса View для справочника
     *
     * @return имя класса
     */
    public Class<?> getViewEntity() {
        if (viewEntity != null) {
            return viewEntity;
        } else {
            return references.getClassifierClass();
        }
    }

    public void setCustomQuery(String query, int entityParentId) {
        if (query != null) {
            if (!query.equals("")) {
                this.entityParentId = entityParentId;
                customQuery = query;
                updateContent();
            }
        }

    }
}
