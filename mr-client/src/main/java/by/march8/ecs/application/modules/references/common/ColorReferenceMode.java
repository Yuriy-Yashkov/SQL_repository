package by.march8.ecs.application.modules.references.common;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameRegion;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.BaseEntity;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.editor.ColorRecipeEditor;
import by.march8.ecs.application.modules.references.classifier.mode.ColorRecipeSearchMode;
import by.march8.ecs.application.modules.references.classifier.services.ColorControlService;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.classifier.ColorRecipesSearchItem;
import by.march8.entities.classifier.RecipesColorItem;
import by.march8.entities.classifier.ReferenceColorItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Andy 21.05.2018 - 7:11.
 */
public class ColorReferenceMode extends AbstractFunctionalMode {

    private final ArrayList<Object> data;
    private RightEnum right;
    private ColorControlService colorControlService;
    private JTree tree;
    private JPanel treePanel;
    private JTextField tfRecipeSearch;
    private UCToolBar tbRecipes;
    private EditingPane editingPane;


    public ColorReferenceMode(MainController mainController) {
        controller = mainController;
        modeName = "Справочник цветов";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        right = controller.getRight(modeName);
        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnReport().setVisible(false);

        toolBar.getBtnEditItem().setVisible(true);
        toolBar.getBtnNewItem().setVisible(true);
        toolBar.getBtnDeleteItem().setVisible(false);


        //toolBar.add(pSearchPanel);
        //toolBar.add(btnSearch);
        JPanel pLeftFooter = new JPanel(new BorderLayout());

        JPanel pRightPanel = new JPanel(new BorderLayout());

        pLeftFooter.setPreferredSize(new Dimension(250, 30));
        pLeftFooter.add(new JLabel("TEST"), BorderLayout.CENTER);
        // lblOrderInformation.setForeground(Color.BLUE);

        toolBar.setVisible(false);

        //Инициализация службы контроля цветов
        colorControlService = ColorControlService.getInstance();
        colorControlService.setMainController(controller);
        colorControlService.setRights(right);
        tree = colorControlService.getColorTree();

        gridViewPort = new GridViewPort(RecipesColorItem.class, false);
        data = gridViewPort.getDataModel();

        tbRecipes = new UCToolBar();
        pRightPanel.add(tbRecipes, BorderLayout.NORTH);
        pRightPanel.add(gridViewPort, BorderLayout.CENTER);
        tbRecipes.registerEvents(this);
        tbRecipes.setRight(RightEnum.WRITE);

        // Комбик выбора типа документа
        JPanel pSearchPanel = new JPanel(null);
        JLabel lblRecipe = new JLabel("Рецептура :");
        tfRecipeSearch = new JTextField();
        lblRecipe.setBounds(10, 10, 125, 20);
        tfRecipeSearch.setBounds(130, 10, 120, 20);

        pSearchPanel.add(lblRecipe);
        pSearchPanel.add(tfRecipeSearch);

        pSearchPanel.setPreferredSize(new Dimension(300, 28));
        pSearchPanel.setOpaque(false);
        tbRecipes.add(pSearchPanel);


        final JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, colorControlService.getTreeComponent(), pRightPanel);
        splitPanel.setResizeWeight(0.1);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);

        IActiveFrameRegion frameRegion = frameViewPort.getFrameRegion();
        frameRegion.getCenterContentPanel().add(splitPanel, BorderLayout.CENTER);

        initEvents();

        updateContent();
        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }


    private void initEvents() {
        tree.addTreeSelectionListener(e -> {
            updateRecipes(colorControlService.getColorItemFromTree());
            tbRecipes.updateButton(data.size());
        });

        tfRecipeSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doSearch();
                }
            }
        });
    }


    @Override
    public void updateContent() {
        //frameViewPort.updateContent();
        //gridViewPort.updateViewPort();

        updateRecipes(colorControlService.getColorItemFromTree());
        tbRecipes.updateButton(data.size());
    }

    private void updateRecipes(ReferenceColorItem colorItem) {
        int colorId = -1;
        if (colorItem != null) {
            colorId = colorItem.getId();
        }

        DaoFactory<RecipesColorItem> factory = DaoFactory.getInstance();
        IGenericDao<RecipesColorItem> dao = factory.getGenericDao();

        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("colorId", colorId));
        try {
            data.clear();
            data.addAll(dao.getEntityListByNamedQuery(RecipesColorItem.class, "RecipesColorItem.findByColorId", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        gridViewPort.updateViewPort();
    }

    @Override
    public void addRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);

        editingPane = new ColorRecipeEditor(controller, colorControlService.getGeneralColorList(), colorControlService.getColorItemFromTree());

        editor.setParentTitle("Рецептура цвета");
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
        editingPane = new ColorRecipeEditor(controller, colorControlService.getGeneralColorList(), colorControlService.getColorItemFromTree());
        editor.setParentTitle("Рецептура цвета");
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
                // Сохраняем сущность в БД
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
        final int answer = Dialogs.showDeleteDialog(gridViewPort.getSelectedItem());
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
                dao.deleteEntity(RecipesColorItem.class, baseEntity.getId());
            } catch (final Exception e) {
                e.printStackTrace();
            }
            // Обновляем содержимое грида
            updateContent();
        }
    }

    private void doSearch() {
        if (tfRecipeSearch.getText().trim().equals("")) {
            return;
        }

        ArrayList<Object> resultList = new ArrayList<Object>();
        resultList.clear();

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<ColorRecipesSearchItem> factory = DaoFactory.getInstance();
                IGenericDao<ColorRecipesSearchItem> dao = factory.getGenericDao();

                java.util.List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("recipe", "%" + tfRecipeSearch.getText().trim() + "%"));
                try {
                    resultList.clear();
                    resultList.addAll(dao.getEntityListByNamedQuery(ColorRecipesSearchItem.class, "ColorRecipesSearchItem.findByCode", criteria));
                    System.out.println(resultList.size());
                    return true;
                } catch (final Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        Task task = new Task("Поиск рецептуры цвета...");
        task.executeTask();

        if (resultList.size() > 0) {
            ColorRecipeSearchMode frame = new ColorRecipeSearchMode(controller, resultList);
            ColorRecipesSearchItem item = frame.showModal();
            if (item != null) {
                colorControlService.expandByColorId(item.getColorId());
            }
        }
    }
}
