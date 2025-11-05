package by.march8.ecs.application.modules.references.classifier.services;

import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.editor.ChangeColorEditor;
import by.march8.ecs.application.modules.references.classifier.mode.ColorSearchMode;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.services.IService;
import by.march8.entities.classifier.ColorSearchItem;
import by.march8.entities.classifier.NSIColorItem;
import by.march8.entities.classifier.ReferenceColorItem;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Andy 10.09.2018 - 8:37.
 */
public class ColorControlService implements IService {

    private static ColorControlService instance = null;
    private JTree generalTreeView;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTextField tfColorSearch;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private UCToolBar toolBar;
    private ArrayList<ReferenceColorItem> colorList = new ArrayList<>();
    private ArrayList<NSIColorItem> colorListNSI = new ArrayList<>();
    private ReferenceColorItem currentTreeColor;
    private MainController controller;
    private boolean readOnly;
    private JLabel lblGroupName;

    private ColorControlService() {
        System.out.println("Инициализация службы контроля цветов ...");
        updateColorTree();
    }

    public static ColorControlService getInstance() {
        if (instance == null) {
            instance = new ColorControlService();
        }
        return instance;
    }


    public JPanel getTreeComponent() {
        return mainPanel;
    }

    private void updateColorTree() {
        updateColorsFromDB();
        System.out.println("Формирование иерархии цветов, обновление компонента...");
        if (generalTreeView == null) {
            mainPanel = new JPanel(new BorderLayout());


            treeModel = new DefaultTreeModel(rootNode);
            //Setting the tree model to the JTree
            generalTreeView = new JTree(treeModel);

            toolBar = new UCToolBar();
            toolBar.getBtnDeleteItem().setVisible(false);

            // Комбик выбора типа документа
            JPanel pSearchPanel = new JPanel(null);
            JLabel lblColor = new JLabel("Цвет :");
            tfColorSearch = new JTextField();
            lblColor.setBounds(10, 10, 50, 20);
            tfColorSearch.setBounds(60, 10, 120, 20);

            pSearchPanel.add(lblColor);
            pSearchPanel.add(tfColorSearch);

            pSearchPanel.setPreferredSize(new Dimension(200, 28));
            pSearchPanel.setOpaque(false);
            toolBar.add(pSearchPanel);


            scrollPane = new JScrollPane(generalTreeView);

            mainPanel.add(toolBar, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            JPanel pFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pFooter.setBounds(0, 0, 100, 30);
            lblGroupName = new JLabel("");
            pFooter.add(lblGroupName);
            mainPanel.add(pFooter, BorderLayout.SOUTH);

            initEvents();
        }

        for (ReferenceColorItem item : colorList) {

            if (item.getId() == 1) {
                rootNode = new DefaultMutableTreeNode(item.getName());
            } else {
                //    int nodeId = getNode(rootNode, getNodeNameById(colorList, item.getParentId()));
                ((DefaultMutableTreeNode) rootNode.getRoot())
                        .add(new DefaultMutableTreeNode(item.getName()));
            }
        }

        treeModel.setRoot(rootNode);
        treeModel.nodeStructureChanged(rootNode);

        // После построения главной структуры, дополняем дерево цветами NSI_CD

        for (ReferenceColorItem item : colorList) {
            int nodeId = getNode(rootNode, getNodeNameById(colorList, item.getId()));
            addToChild(((DefaultMutableTreeNode) rootNode.getChildAt(nodeId)), item);
        }

        treeModel.nodeStructureChanged(rootNode);

        colorList.sort((a, b) -> a.getName().compareTo(b.getName()));
    }

    private void initEvents() {
        generalTreeView.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode =
                    (DefaultMutableTreeNode) generalTreeView.getLastSelectedPathComponent();
            if (selectedNode != null) {
                currentTreeColor = getColorItemByNameAndLevel(selectedNode.toString(), selectedNode.getLevel());
                lblGroupName.setText(currentTreeColor.getName());
            }
        });

        generalTreeView.addMouseListener(new MouseEventTree());

        toolBar.getBtnEditItem().addActionListener(a -> {
            // Создаем пустую форму диалога
            BaseEditorDialog editor = new BaseEditorDialog(controller, RecordOperationType.EDIT);
            EditingPane editPane = new ChangeColorEditor(controller, colorList);
            // Для созданного пустого диалога устанавливаем панель редактирования
            editor.setEditorPane(editPane);

            editPane.setSourceEntity(currentTreeColor);
            // Модально показываем форму и ожидаем закрытия
            if (editor.showModal()) {
                // Форма закрыта со значением true
                // Получаем DAO слой
                DaoFactory factoryMarch = DaoFactory.getInstance();
                // ПОлучаем интерфейс для работы с БД
                ICommonDao dao = factoryMarch.getCommonDao();
                try {
                    // Обновляем сущность в БД
                    Object o = editPane.getSourceEntity();
                    dao.updateEntity(o);
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                }
                // После сохранения данных обновляем дерево
                updateColorTree();
            }
        });

        toolBar.getBtnNewItem().addActionListener(a -> {
            // Создаем пустую форму диалога
            BaseEditorDialog editor = new BaseEditorDialog(controller, RecordOperationType.NEW);
            EditingPane editPane = new ChangeColorEditor(controller, colorList);
            currentTreeColor.setNewColor(true);
            // Для созданного пустого диалога устанавливаем панель редактирования
            editor.setEditorPane(editPane);
            editPane.setSourceEntity(currentTreeColor);
            // Модально показываем форму и ожидаем закрытия
            if (editor.showModal()) {
                // Форма закрыта со значением true
                // Получаем DAO слой
                DaoFactory factoryMarch = DaoFactory.getInstance();
                // ПОлучаем интерфейс для работы с БД
                ICommonDao dao = factoryMarch.getCommonDao();
                try {
                    // Обновляем сущность в БД
                    Object o = editPane.getSourceEntity();
                    dao.saveEntity(o);
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                }
                // После сохранения данных обновляем дерево
                updateColorTree();
            }
            currentTreeColor.setNewColor(false);
        });

        tfColorSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    // searchAndExpandNode(tfColorSearch.getText());
                    doSearch();
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tfColorSearch.setText("");
                }
            }
        });
    }

    private ReferenceColorItem getColorItemByNameAndLevel(String colorName, int treeLevel) {
        ReferenceColorItem returnColor = null;
        //Клик на руте
        if (treeLevel == 0) {
            returnColor = new ReferenceColorItem();
            returnColor.setId(1);
            returnColor.setName(colorName);
            returnColor.setParentId(0);
            return returnColor;

        } else if (treeLevel == 1) {
            //Клик на первой ветке
            returnColor = getReferenceColorByName(colorName);
            if (returnColor == null) {
                returnColor = getNSIReferenceColorByName(colorName);
            }

            return returnColor;
        } else if (treeLevel > 1) {
            //Клик на последующих ветках-вложениях
            returnColor = getNSIReferenceColorByName(colorName);
        }

        return returnColor;
    }

    private void updateColorsFromDB() {
        System.out.println("Получение структуры первичных цветов...");
        DaoFactory<ReferenceColorItem> factory = DaoFactory.getInstance();
        IGenericDao<ReferenceColorItem> dao = factory.getGenericDao();
        try {
            colorList.clear();
            colorList.addAll(dao.getEntityListByNamedQuery(ReferenceColorItem.class, "ReferenceColorItem.findAll", null));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.out.println("Получение структуры цветов справочника НСИ...");
        DaoFactory<NSIColorItem> fNSIColor = DaoFactory.getInstance();
        IGenericDao<NSIColorItem> daoNSIColor = fNSIColor.getGenericDao();
        try {
            colorListNSI.clear();
            colorListNSI.addAll(daoNSIColor.getEntityListByNamedQuery(NSIColorItem.class, "NSIColorItem.findAll", null));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void addToChild(DefaultMutableTreeNode node, ReferenceColorItem colorItem) {
        for (NSIColorItem nsiColorItem : colorListNSI) {
            if (nsiColorItem.getParentId() == colorItem.getId()) {
                if (nsiColorItem.getParentId() == 1) {
                    ((DefaultMutableTreeNode) rootNode.getRoot())
                            .add(new DefaultMutableTreeNode(nsiColorItem.getName()));
                } else {
                    node.add(new DefaultMutableTreeNode(nsiColorItem.getName()));
                }
            }
        }
    }

    private String getNodeNameById(List<ReferenceColorItem> list, int parentId) {
        for (ReferenceColorItem item : list) {
            if (item.getId() == parentId) {
                return item.getName();
            }
        }
        return "";
    }


    private int getNode(TreeNode node, String name) {
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChildAt(i).toString().equals(name)) {
                return i;
            }
        }
        return 0;
    }


    private ReferenceColorItem getReferenceColorByName(String name) {
        for (ReferenceColorItem item : colorList) {
            if (item.getName().equals(name)) {

                return item;
            }
        }
        return null;
    }

    private ReferenceColorItem getNSIReferenceColorByName(String name) {
        for (NSIColorItem item : colorListNSI) {
            if (item.getName().equals(name)) {
                ReferenceColorItem returnColor = new ReferenceColorItem();
                returnColor.setId(item.getId());
                returnColor.setParentId(item.getParentId());
                returnColor.setName(item.getName());
                returnColor.setNsiColor(true);
                return returnColor;
            }
        }
        return null;
    }

    public ReferenceColorItem getColorItemFromTree() {
        DefaultMutableTreeNode selectedNode =
                (DefaultMutableTreeNode) generalTreeView.getLastSelectedPathComponent();
        if (selectedNode != null) {
            int treeLevel = selectedNode.getLevel();
            if (treeLevel == 0) {
                return null;
            } else if (treeLevel == 1) {
                return getReferenceColorByName(selectedNode.toString());
            } else if (treeLevel > 1) {
                return null;//updateRecipes(getReferenceColorByName(selectedNode.toString()));
            }
        }
        return null;
    }

    public void searchAndExpandNode(String text) {
        TreePath path = find((DefaultMutableTreeNode) treeModel.getRoot(), text);
        generalTreeView.setSelectionPath(path);
        generalTreeView.scrollPathToVisible(path);
        System.out.println(path);
    }

    private void doSearch() {
        if (tfColorSearch.getText().trim().equals("")) {
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
                DaoFactory<ColorSearchItem> factory = DaoFactory.getInstance();
                IGenericDao<ColorSearchItem> dao = factory.getGenericDao();

                java.util.List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("colorName", "%" + tfColorSearch.getText().trim() + "%"));
                try {
                    resultList.clear();
                    resultList.addAll(dao.getEntityListByNamedQuery(ColorSearchItem.class, "ColorSearchItem.findByName", criteria));
                    System.out.println(resultList.size());
                    return true;
                } catch (final Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        Task task = new Task("Поиск цвета...");
        task.executeTask();

        if (resultList.size() > 0) {
            ColorSearchMode frame = new ColorSearchMode(controller, resultList);
            ColorSearchItem item = frame.showModal();
            if (item != null) {
                expandByColorId(item.getParentId(), item.getColorName());
                //String path = ""+item.getColorRootName()+", "+item.getColorParentName()+", "+item.getColorName()+"";
                //generalTreeView.expandPath(new TreePath(path));
            }
        }

    }


    private TreePath find(DefaultMutableTreeNode root, String s) {
        Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = e.nextElement();
            if (node.toString().toLowerCase().contains(s.toLowerCase())) {
                return new TreePath(node.getPath());
            }
        }
        return null;
    }

    public ReferenceColorItem getTreeCurrentColor() {
        return currentTreeColor;
    }


    public void setMainController(MainController mainController) {
        controller = mainController;
    }

    public JTree getColorTree() {
        return generalTreeView;
    }

    public void expandByColorId(int colorId) {
        int nodeId = getNode(rootNode, getNodeNameById(colorList, colorId));
        TreePath path = new TreePath(((DefaultMutableTreeNode) rootNode.getChildAt(nodeId)).getPath());
        generalTreeView.setSelectionPath(path);
        generalTreeView.scrollPathToVisible(path);
    }

    public void expandByColorId(int colorId, String text) {
        int nodeId = getNode(rootNode, getNodeNameById(colorList, colorId));
        TreePath path = new TreePath(((DefaultMutableTreeNode) rootNode.getChildAt(nodeId)).getPath());

        //TreePath path1 = find( ,text);

        generalTreeView.expandPath(path);
        generalTreeView.setSelectionPath(path);
        generalTreeView.scrollPathToVisible(path);

        DefaultMutableTreeNode nodeTree = (DefaultMutableTreeNode) generalTreeView.getLastSelectedPathComponent();


        for (int i = 0; i < nodeTree.getChildCount(); i++) {
            if (nodeTree.getChildAt(i).toString().toLowerCase().contains(text.toLowerCase())) {
                TreePath treePath = new TreePath(treeModel.getPathToRoot(nodeTree.getChildAt(i)));
                generalTreeView.setSelectionPath(treePath);
                generalTreeView.scrollPathToVisible(treePath);
                break;
            }
        }
    }

    public void setRights(RightEnum rights) {
        toolBar.getBtnNewItem().setVisible(false);
        if (rights == RightEnum.READ) {
            readOnly = true;
            toolBar.setVisible(false);
        } else {
            readOnly = false;
            toolBar.setVisible(true);
        }

        toolBar.setRight(rights);
    }

    public List<ReferenceColorItem> getGeneralColorList() {
        return colorList;
    }

    class MouseEventTree extends MouseAdapter {

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (currentTreeColor != null) {
                    if (currentTreeColor.isNsiColor()) {
                        toolBar.getBtnEditItem().setEnabled(true);
                        toolBar.getBtnNewItem().setEnabled(false);
                    } else {
                        toolBar.getBtnEditItem().setEnabled(false);
                        toolBar.getBtnNewItem().setEnabled(true);
                    }
                }
            }
        }
    }
}
