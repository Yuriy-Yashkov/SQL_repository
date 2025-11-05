package by.march8.ecs.application.modules.references.classifier.ui;

import by.march8.ecs.application.modules.references.classifier.dao.ClassifierTreeJDBC;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNodeType;
import by.march8.ecs.framework.common.BackgroundTask;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Andy 08.01.2019 - 9:40.
 */
@SuppressWarnings("all")
public class ClassifierTree extends JPanel implements TreeWillExpandListener {


    public static final String GENDER_MAN = "Мужской";
    public static final String GENDER_WOMAN = "Женский";
    public static final String GENDER_BOY = "Для мальчика";
    public static final String GENDER_GIRL = "Для девочки";
    public static final String GENDER_UNKNOW = "Детский";
    protected JTree tree;
    private ClassifierTreeContentLoader contentLoader = null;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private ClassifierTreeDataSource db;
    private JComboBox<String> cbSearch;
    private JRadioButton rbSearchModel;
    private boolean expandForSearch = false;
    private ClassifierTreeListener listener;
    private ClassifierNodeType limitNode;
    private int[] blackListCode;
    private JPanel pSearchAction;
    private JPanel pSearch;

    public ClassifierTree(int[] bl) {
        super(new BorderLayout());
        blackListCode = bl;
        init();

        db = new ClassifierTreeJDBC();
        prepareDefaultTree();

        //tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setExpandsSelectedPaths(true);
        tree.setScrollsOnExpand(true);
        tree.setShowsRootHandles(true);

        tree.addTreeWillExpandListener(this);
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane, BorderLayout.CENTER);
        //initEvents();
    }

    public ClassifierTree(ClassifierTreeContentLoader loader) {
        super(new BorderLayout());
        contentLoader = loader;
        init();
        blackListCode = loader.getCodeIgnoreList();
        db = loader.getContentDataSource();
        prepareDefaultTree();

        tree.setExpandsSelectedPaths(true);
        tree.setScrollsOnExpand(true);
        tree.setShowsRootHandles(true);

        tree.addTreeWillExpandListener(this);
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane, BorderLayout.CENTER);
    }


    public ClassifierTree() {
        this(new int[]{});
    }

    public static String getNameAssortmentByArticleSegment(int code) {
        switch (code) {
            case 41:
                return "Белье";
            case 42:
                return "Верхний трикотаж";
            case 43:
                return "Чулки";
            case 45:
                return "Прочие";
            case 47:
                return "Полотно";
            case 48:
                return "Пряжа";

            case 411:
                return "Изделия мужские";
            case 412:
                return "Изделия женские";
            case 413:
                return "Изделия детские";
            case 415:
                return "Спортивные взрослые";
            case 416:
                return "Спортивные детские";

            case 421:
                return "Изделия мужские";
            case 422:
                return "Изделия женские";
            case 423:
                return "Изделия детские";
            case 425:
                return "Спортивные взрослые";
            case 426:
                return "Спортивные детские";

            case 431:
                return "Изделия мужские";
            case 432:
                return "Изделия женские";
            case 433:
                return "Изделия детские";
            case 435:
                return "Спортивные взрослые";


            default:
                return String.valueOf(code);
        }
    }

    public static String getGenderByArticleSegment(int code) {
        switch (code) {

            case 411:
                return GENDER_MAN;
            case 412:
                return GENDER_WOMAN;
            case 413:
                return null;
            case 415:
                return null;
            case 416:
                return null;

            case 421:
                return GENDER_MAN;
            case 422:
                return GENDER_WOMAN;
            case 423:
                return null;
            case 425:
                return null;
            case 426:
                return null;

            case 431:
                return GENDER_MAN;
            case 432:
                return GENDER_WOMAN;
            case 433:
                return null;
            case 435:
                return null;

            default:
                return null;
        }
    }

    private void init() {
        pSearch = new JPanel();
        pSearch.setLayout(new BorderLayout(1, 1));
        pSearch.setBorder(BorderFactory.createTitledBorder(null, "Поиск по актуальным моделям", TitledBorder.RIGHT, TitledBorder.DEFAULT_POSITION, new Font("Dialog", 2, 12)));

        pSearchAction = new JPanel();
        pSearchAction.setLayout(new GridLayout(1, 0, 0, 0));
        pSearch.add(pSearchAction, BorderLayout.NORTH);

        cbSearch = new JComboBox<>();
        cbSearch.setEditable(true);

        pSearchAction.add(cbSearch);

        JLabel imgPoiskLabel = new JLabel(UtilFunctions.createIcon("/Img/xmag.png"));
        imgPoiskLabel.setHorizontalAlignment(JLabel.CENTER);
        imgPoiskLabel.setVerticalAlignment(JLabel.CENTER);
        pSearch.add(imgPoiskLabel, BorderLayout.EAST);

        imgPoiskLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //cbSearch.postActionEvent();
            }
        });

        JPanel pSearchProperties = new JPanel();
        pSearchProperties.setLayout(new GridLayout(2, 0, 0, 0));
        pSearch.add(pSearchProperties);

        JLabel v1 = new JLabel("Поиск:");
        pSearchProperties.add(v1);
        pSearchProperties.add(new JLabel());

        rbSearchModel = new JRadioButton("По модели");
        pSearchProperties.add(rbSearchModel);
        JRadioButton rbSearchArticle = new JRadioButton("По артикулу;");
        pSearchProperties.add(rbSearchArticle);
        rbSearchArticle.setEnabled(false);

        ButtonGroup group1 = new ButtonGroup();
        group1.add(rbSearchArticle);
        group1.add(rbSearchModel);
        rbSearchModel.setSelected(true);

        add(pSearch, BorderLayout.NORTH);

        JTextField editor = (JTextField) cbSearch.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    doSearch(((JTextField) cbSearch.getEditor().getEditorComponent()).getText(), rbSearchModel.isSelected());
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cbSearch.setSelectedIndex(-1);
                    collapseAll();
                }
            }
        });
    }

    public void collapseAll() {
        int row = tree.getRowCount() - 1;
        while (row > 0) {
            tree.collapseRow(row);
            row--;
        }
    }

    private void searchNodeInTree(ClassifierNode selectedNode, List<ClassifierNode> list) {
        if (selectedNode != null) {
            DefaultMutableTreeNode node = selectedNode.getNode();
            if (node != null) {
                if (list != null) {
                    for (ClassifierNode item : list) {
                        if (item != null) {
                            if (blackListCode != null) {
                                if (!nodeInBlackList(item.getCode())) {
                                    if (!nodeExist(selectedNode, item)) {
                                        ClassifierNode cNode = new ClassifierNode();
                                        cNode.setName(item.getName());
                                        cNode.setType(item.getType());
                                        cNode.setParentId(selectedNode.getId());
                                        cNode.setCode(item.getCode());

                                        DefaultMutableTreeNode nNode = new DefaultMutableTreeNode(cNode);
                                        if (item.getType() == ClassifierNodeType.ARTICLE) {
                                            nNode.setAllowsChildren(false);
                                        }

                                        node.add(nNode);
                                        cNode.setId(node.getIndex(nNode));
                                        cNode.setNode(nNode);
                                    }
                                }
                            } else {
                                if (!nodeExist(selectedNode, item)) {
                                    ClassifierNode cNode = new ClassifierNode();
                                    cNode.setName(item.getName());
                                    cNode.setType(item.getType());
                                    cNode.setParentId(selectedNode.getId());
                                    cNode.setCode(item.getCode());

                                    DefaultMutableTreeNode nNode = new DefaultMutableTreeNode(cNode);
                                    if (item.getType() == ClassifierNodeType.ARTICLE) {
                                        nNode.setAllowsChildren(false);
                                    }

                                    node.add(nNode);
                                    cNode.setId(node.getIndex(nNode));
                                    cNode.setNode(nNode);
                                }
                            }
                        }
                    }
                }
                treeModel.reload(node);
            }
        }
    }

    private boolean nodeInBlackList(int code) {
        String code_ = String.valueOf(code);
        for (int i : blackListCode) {
            String bl_ = String.valueOf(i);
            if (code_.startsWith(bl_)) {
                return true;
            }

        }
        return false;
    }

    private ClassifierNode searchNodeInTree(ClassifierNode selectedNode) {
        if (selectedNode != null) {
            DefaultMutableTreeNode node;
            Enumeration e = rootNode.breadthFirstEnumeration();
            while (e.hasMoreElements()) {
                node = (DefaultMutableTreeNode) e.nextElement();
                ClassifierNode node_ = (ClassifierNode) node.getUserObject();
                if (selectedNode.getType() == node_.getType() && selectedNode.getName().equals(node_.getName())) {
                    if (node_.getType() == ClassifierNodeType.MODEL) {
                        if (selectedNode.getAssortmentName().equals(node_.getAssortmentName())) {
                            return node_;
                        }
                    } else if (node_.getType() == ClassifierNodeType.CATEGORY) {
                        if (selectedNode.getCode() == (node_.getCode())) {
                            return node_;
                        }
                    } else {
                        return node_;
                    }
                }
            }
        }
        return null;
    }

    private boolean nodeExist(ClassifierNode selectedNode, ClassifierNode addedNode) {
        if (selectedNode != null) {
            DefaultMutableTreeNode node = selectedNode.getNode();
            if (node != null) {
                Enumeration e = selectedNode.getNode().breadthFirstEnumeration();
                while (e.hasMoreElements()) {
                    node = (DefaultMutableTreeNode) e.nextElement();
                    ClassifierNode node_ = (ClassifierNode) node.getUserObject();
                    if (node_.getName().equals(addedNode.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void prepareDefaultTree() {
        ClassifierNode classifierNode = new ClassifierNode();
        classifierNode.setType(ClassifierNodeType.ROOT);
        classifierNode.setName("Продукция предприятия");

        rootNode = new DefaultMutableTreeNode(classifierNode);
        classifierNode.setId(0);
        classifierNode.setParentId(0);
        classifierNode.setNode(rootNode);

        treeModel = new DefaultTreeModel(rootNode, true);
        tree = new JTree(treeModel);

        List<ClassifierNode> list = db.getGroupNodes();
        searchNodeInTree((ClassifierNode) rootNode.getUserObject(), list);
        treeModel.setRoot(rootNode);
        treeModel.nodeStructureChanged(rootNode);
        System.out.println("ТУТ");
    }

    private void expandClassifierNode(ClassifierNode node) {
        if (node != null) {
            List<ClassifierNode> list;
            switch (node.getType()) {
                case GROUP:
                    if (limitNode != ClassifierNodeType.GROUP) {
                        list = db.getCategoryNodesByGroupId(node);
                        searchNodeInTree(node, list);
                    }
                    break;

                case CATEGORY:
                    if (limitNode != ClassifierNodeType.CATEGORY) {
                        list = db.getAssortmentNodesByCategoryId(node);
                        searchNodeInTree(node, list);
                    }
                    break;

                case ASSORTMENT:
                    if (limitNode != ClassifierNodeType.ASSORTMENT) {
                        list = db.getModelNodesByAssortmentName(node);
                        searchNodeInTree(node, list);
                    }
                    break;

                case MODEL:
                    if (limitNode != ClassifierNodeType.MODEL) {
                        list = db.getArticleNodesByProductNumber(node);
                        searchNodeInTree(node, list);
                    }
                    break;
            }
        }

        //treeModel.setRoot(rootNode);
        //treeModel.nodeStructureChanged(rootNode);
    }

    private void doSearch(String searchText, boolean searchTargetModel) {
        if (searchText.length() >= 3) {
            expandForSearch = true;

            expandForSearch = true;
            List<ClassifierNode> searchList = null;
            if (searchTargetModel) {
                searchList = db.searchModel(searchText);
            } else {
                searchList = db.searchArticle(searchText);
            }

            if (searchList.size() < 1) {
                cbSearch.addItem(searchText + " Не найдено");
            } else {
                cbSearch.addItem(searchText);
                restoreNodeList(searchList);
            }

            cbSearch.setSelectedIndex(cbSearch.getItemCount() - 1);
            expandForSearch = false;
        }
    }

    private ClassifierNodeType getParentNodeType(ClassifierNode node) {
        switch (node.getType()) {
            case GROUP:
                return ClassifierNodeType.ROOT;
            case CATEGORY:
                return ClassifierNodeType.GROUP;
            case ASSORTMENT:
                return ClassifierNodeType.CATEGORY;
            case MODEL:
                return ClassifierNodeType.ASSORTMENT;
            case ARTICLE:
                return ClassifierNodeType.MODEL;
        }
        return ClassifierNodeType.ROOT;
    }

    private ClassifierNode getParentNode(ClassifierNode selectedNode) {

        ClassifierNodeType parentType = getParentNodeType(selectedNode);
        DefaultMutableTreeNode node;
        Enumeration e = rootNode.breadthFirstEnumeration();
        int nodeCode = selectedNode.getCode();
        if (parentType == ClassifierNodeType.GROUP) {
            nodeCode = Integer.valueOf(String.valueOf(selectedNode.getCode()).substring(0, 2));
        }

        while (e.hasMoreElements()) {
            node = (DefaultMutableTreeNode) e.nextElement();
            ClassifierNode node_ = (ClassifierNode) node.getUserObject();
            if (parentType == ClassifierNodeType.ASSORTMENT) {
                if (selectedNode.getAssortmentName().equals(node_.getName())) {
                    return node_;
                }
            } else if (parentType == ClassifierNodeType.CATEGORY) {
                if (node_.getType() == parentType) {
                    if (node_.getCode() == selectedNode.getCode()) {
                        return node_;
                    }
                }
            } else {
                if (node_.getType() == parentType && node_.getCode() == nodeCode) {
                    return node_;
                }
            }
        }
        return null;
    }

    private ClassifierNode addNodeToTree(ClassifierNode selectedNode) {
        ClassifierNode node_ = searchNodeInTree(selectedNode);
        if (node_ != null) {
            if (node_.getNode() == null) {
                ClassifierNode parentNode = getParentNode(selectedNode);
                if (parentNode != null) {
                    if (parentNode.getNode() != null) {
                        selectedNode.setParentId(parentNode.getId());
                        DefaultMutableTreeNode nNode = new DefaultMutableTreeNode(selectedNode);
                        parentNode.getNode().add(nNode);
                        selectedNode.setId(parentNode.getNode().getIndex(nNode));
                        selectedNode.setNode(nNode);
                        return selectedNode;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return node_;
            }
        } else {
            ClassifierNode parentNode = getParentNode(selectedNode);
            if (parentNode != null) {
                if (parentNode.getNode() != null) {
                    selectedNode.setParentId(parentNode.getId());
                    DefaultMutableTreeNode nNode = new DefaultMutableTreeNode(selectedNode);
                    parentNode.getNode().add(nNode);
                    selectedNode.setId(parentNode.getNode().getIndex(nNode));
                    selectedNode.setNode(nNode);
                    return selectedNode;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private void restoreNodeList(List<ClassifierNode> nodeList) {
        // Поиск ноды для каждой найденой записи с корня
        List<TreePath> paths = null;
        collapseAll();
        for (ClassifierNode node : nodeList) {
            if (node != null) {
                addNodeToTree(ClassifierNode.getCustomNode(node, ClassifierNodeType.GROUP));
                addNodeToTree(ClassifierNode.getCustomNode(node, ClassifierNodeType.CATEGORY));
                addNodeToTree(ClassifierNode.getCustomNode(node, ClassifierNodeType.ASSORTMENT));
                ClassifierNode node_ = addNodeToTree(ClassifierNode.getCustomNode(node, ClassifierNodeType.MODEL));
                if (node_ != null) {
                    node_.getNode().setAllowsChildren(true);
                    if (paths == null) {
                        paths = new ArrayList<>();
                    }
                    paths.add(new TreePath(node_.getNode().getPath()));
                }
            }
        }

        if (paths != null) {
            expandForSearch = true;
            treeModel.reload(rootNode);
            for (TreePath path : paths) {
                tree.expandPath(path);
                tree.scrollPathToVisible(path);
                tree.setSelectionPath(path);
            }
            expandForSearch = false;
        }
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (event.getPath().getLastPathComponent());
        if (node != null) {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    if (!expandForSearch) {
                        expandClassifierNode((ClassifierNode) node.getUserObject());
                    }
                    return true;
                }
            }

            Task task = new Task("Обработка запроса...");
            task.executeTask();
        }
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

    }

    public void addTreeListener(ClassifierTreeListener listener) {
        this.listener = listener;
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();
            if (listener != null) {
                if (node != null) {
                    ClassifierNode sel = (ClassifierNode) node.getUserObject();
                    listener.onClick(sel);
                }
            }
        });
    }

    public void setExpandLimit(ClassifierNodeType type) {
        limitNode = type;
    }

    public void presetTreeNode(ClassifierNode node) {
        restoreNode(node);
        ClassifierNode node_ = searchNodeInTree(node);
        if (node_ != null) {
            TreePath path = new TreePath(node_.getNode().getPath());
            tree.expandPath(path);
            tree.scrollPathToVisible(path);
            tree.setSelectionPath(path);
        }
    }

    private void restoreNode(ClassifierNode node) {
        collapseAll();
        switch (node.getType()) {
            case CATEGORY:
                addNodeToTree(ClassifierNode.getCustomNode(node, ClassifierNodeType.CATEGORY));
                break;
            case ASSORTMENT:
                addNodeToTree(ClassifierNode.getCustomNode(node, ClassifierNodeType.ASSORTMENT));
                addNodeToTree(ClassifierNode.getCustomNode(node, ClassifierNodeType.CATEGORY));
                break;
        }
    }

    public boolean updateClassifierTree() {
        return true;
    }

    public String getSearchText() {
        return ((JTextField) cbSearch.getEditor().getEditorComponent()).getText();
    }

    public void setVisibleSearchPanel(boolean b) {
        pSearch.setVisible(b);
    }
}
