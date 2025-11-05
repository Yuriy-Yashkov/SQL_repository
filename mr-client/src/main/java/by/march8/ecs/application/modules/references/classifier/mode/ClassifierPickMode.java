package by.march8.ecs.application.modules.references.classifier.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractSelectableFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.classifier.ClassifierModelView;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andy 20.01.2016.
 */
public class ClassifierPickMode extends AbstractSelectableFunctionalMode {

    private static final Color COLOR_ENABLED = new Color(200, 255, 200);
    private RightEnum right;
    private ArrayList<ClassifierModelView> dataList = null;
    private ArrayList<ClassifierItem> detailList = null;
    private DaoFactory factory = DaoFactory.getInstance();
    private GridViewPort<ClassifierModelView> gvArticle;
    private GridViewPort<ClassifierItem> gvProduct;
    private ClassifierItem selectedProduct;
    private ClassifierModelView selectedArticle;
    private ClassifierType classifierType;
    private UCTextField tfSearch;
    private JButton btnSearch;
    private JButton btnComposition;
    private JPanel pArticle;
    private JPanel pProduct;
    private TableRowSorter<TableModel> sorter;
    private ClassifierTree tree;

    private String searchString;
    private String[] articleWhiteList;

    private SelectorType selectorType = SelectorType.PRODUCT;


    @SuppressWarnings("all")
    public ClassifierPickMode(final MainController mainController, final ClassifierType type) {
        controller = mainController;
        classifierType = type;
        if (classifierType == ClassifierType.MATERIAL) {
            selectorType = SelectorType.PRODUCT;
        }
        modeName = "Классификатор продукции: Поиск полотна";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.PICKFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        frameViewPort.getFrameControl().setFrameSize(new Dimension(1000, 600));

        //right = controller.getRight(modeName);
        right = RightEnum.READ;

        init();
        initEvents();

        gvArticle.primaryInitialization();
        gvProduct.primaryInitialization();
        initialGridSorter();

        updateContent();
        tfSearch.requestFocusInWindow();
    }

    private void init() {
        //frameViewPort.getFrameRegion().getToolBar().setVisible(false);
        UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);
        toolBar.getBtnViewItem().setVisible(true);
        toolBar.getBtnViewItem().setEnabled(false);
        toolBar.setVisibleSearchControls(true);
        tfSearch = toolBar.getSearchField();
        tfSearch.setBackground(COLOR_ENABLED);
        btnSearch = toolBar.getBtnSearchItem();
        btnSearch.setVisible(false);


        btnComposition = new JButton("");
        btnComposition.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/pie_20.png", "Сырьевой состав продукции"));
        btnComposition.setToolTipText("Сырьевой состав продукции");
        toolBar.add(btnComposition);

        // Инициализация гридов
        gvArticle = new GridViewPort<>(ClassifierModelView.class, false);
        gvProduct = new GridViewPort<>(ClassifierItem.class, false);
        //

        pArticle = new JPanel(new BorderLayout());
        pArticle.add(gvArticle, BorderLayout.CENTER);
        pArticle.setBorder(BorderFactory.createLineBorder(Color.RED));

        pProduct = new JPanel(new BorderLayout());
        pProduct.add(gvProduct, BorderLayout.CENTER);
        pProduct.setBorder(BorderFactory.createLineBorder(Color.RED));

        // Получаем ссылку на модель данных грида
        dataList = gvArticle.getDataModel();
        detailList = gvProduct.getDataModel();

        final JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pArticle, pProduct);

        splitPanel.setResizeWeight(0.5);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);

        tree = new ClassifierTree();
        JPanel pCatalog = new JPanel(new BorderLayout());
        pCatalog.add(tree, BorderLayout.CENTER);

        final JSplitPane spVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pCatalog, splitPanel);
        spVertical.setResizeWeight(0.2);
        spVertical.setOneTouchExpandable(true);
        spVertical.setContinuousLayout(true);

        frameViewPort.getFrameRegion().getCenterContentPanel().add(spVertical);
    }

    private void initEvents() {
        gvArticle.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                ClassifierModelView selectedItem = (ClassifierModelView) object;
                if (selectedItem != null) {
                    selectedArticle = selectedItem;
                    selectedProduct = null;
                    if (selectorType == SelectorType.PRODUCT) {
                        updateDetail((selectedItem.getId()));
                    }
                }
            }
        });

        gvProduct.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                ClassifierItem selectedItem = (ClassifierItem) object;
                if (selectedItem != null) {
                    selectedProduct = selectedItem;
                }
            }
        });

        // Получаем ссылку на фрэйм и вешаем слушатель на кнопку ВЫБРАТЬ
        // Так, если из классификатора не будет выбрано ни одной записи - диалог не закроется
        BasePickDialog frame = (BasePickDialog) frameViewPort.getFrame();

        frame.setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                if (classifierType == ClassifierType.MATERIAL) {
                    return gvProduct.getSelectedItem() != null;
                }

                // Если пользователь ничего не выбрал
                if (selectorType == SelectorType.PRODUCT) {
                    return (selectedProduct != null);
                } else {
                    if (selectedArticle == null) {
                        return gvArticle.getSelectedItem() != null;
                    }
                    return (selectedArticle != null);
                }
            }
        });

        tfSearch.addKeyListener(new

                                        KeyAdapter() {
                                            @Override
                                            public void keyPressed(KeyEvent evt) {
                                                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                                                    sorter.setRowFilter(RowFilter.regexFilter(tfSearch.getText().trim(),
                                                            0));
                                                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                                                    sorter.setRowFilter(null);
                                                    if (classifierType == ClassifierType.MATERIAL) {
                                                        tfSearch.setText("47");
                                                    } else {
                                                        tfSearch.setText("42");
                                                    }
                                                }
                                            }
                                        });

        btnComposition.addActionListener(a ->

        {
            ClassifierModelView selectedItem = gvArticle.getSelectedItem();
            if (selectedItem != null) {
                new CompositionMaterialMode(controller, getDetails(selectedItem.getId()));
            }
        });

        // btnSearch.addActionListener(a -> updateContent());

        tree.addTreeListener(a ->

        {
            if (a != null) {
                searchString = String.valueOf(a.getCode());
                doCustomUpdate(a);
            }
        });

    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateContent() {
        DaoFactory<ClassifierModelView> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierModelView> dao = factory.getGenericDao();
        try {
            dataList.clear();
            detailList.clear();
            selectedProduct = null;

            if (classifierType == ClassifierType.MATERIAL) {
                dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findCanvas", null));
            } else if (classifierType == ClassifierType.CUSTOM) {
                List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("article", searchString + "%"));
                dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByGroup", criteria));
            } else {
                dataList.addAll(dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findItems", null));
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
        frameViewPort.updateContent();
        gvArticle.updateViewPort();
        gvProduct.updateViewPort();
    }

    @SuppressWarnings("unchecked")
    private void updateDetail(int id) {
        DaoFactory<ClassifierModelParams> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierModelParams> dao = factory.getGenericDao();
        try {
            ClassifierModelParams item = dao.getEntityById(ClassifierModelParams.class, id);
            if (item != null) {
                detailList.clear();
                detailList.addAll(item.getAssortmentList());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gvProduct.updateViewPort();
        selectedProduct = null;
    }

    @Override
    public Object showSelectModal(final Object presetObject) {
        gvArticle.resetFilter();

        if (presetObject != null) {
            if (presetObject instanceof Integer) {
                tfSearch.setText(String.valueOf(presetObject));
                sorter.setRowFilter(RowFilter.regexFilter(tfSearch.getText().trim(),
                        0));
            }
        }

        if (frameViewPort.getFrameControl().showModalFrame()) {

            if (classifierType == ClassifierType.MATERIAL) {
                return gvProduct.getSelectedItem();
            }
            if (classifierType == ClassifierType.ALL_PRODUCT) {
                return gvProduct.getSelectedItem();
            } else {
                return gvArticle.getSelectedItem();
            }
        }
        return null;
    }

    public ClassifierModelView selectArticle(final Object presetObject) {
        gvArticle.resetFilter();
        pArticle.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        pProduct.setVisible(false);
        frameViewPort.getFrameRegion().getToolBar().setVisible(false);
        selectorType = SelectorType.ARTICLE;
        if (frameViewPort.getFrameControl().showModalFrame()) {
            return gvArticle.getSelectedItem();
        }
        return null;
    }

    public ClassifierItem selectProduct(final Object presetObject) {
        gvProduct.resetFilter();
        pArticle.setBorder(null);
        pProduct.setVisible(true);
        pProduct.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        frameViewPort.getFrameRegion().getToolBar().setVisible(false);
        selectorType = SelectorType.PRODUCT;
        if (frameViewPort.getFrameControl().showModalFrame()) {
            return gvProduct.getSelectedItem();
        }
        return null;
    }

    private void initialGridSorter() {
        if (classifierType == ClassifierType.MATERIAL) {
            tfSearch.setText("47");
        } else {
            tfSearch.setText("42");
        }
        sorter = new TableRowSorter<>(gvArticle.getTableModel());
        sorter.setSortsOnUpdates(true);
        gvArticle.getTable().setRowSorter(sorter);
    }

    private ClassifierModelParams getDetails(int id) {
        DaoFactory<ClassifierModelParams> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierModelParams> dao = factory.getGenericDao();
        ClassifierModelParams result = null;
        try {
            result = dao.getEntityById(ClassifierModelParams.class, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void doCustomUpdate(ClassifierNode node) {
        DaoFactory<ClassifierModelView> factory = DaoFactory.getInstance();
        IGenericDao<ClassifierModelView> dao = factory.getGenericDao();
        List<ClassifierModelView> list_ = null;
        try {
            dataList.clear();
            List<QueryProperty> criteria = new ArrayList<>();
            switch (node.getType()) {
                case ROOT:
                    list_ = dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findAll", null);
                    break;
                case GROUP:
                    criteria.add(new QueryProperty("article", node.getCode() + "%"));
                    list_ = dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByGroup", criteria);
                    break;
                case CATEGORY:
                    criteria.add(new QueryProperty("article", node.getCode() + "%"));
                    list_ = dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByGroup", criteria);
                    break;
                case ASSORTMENT:
                    criteria.add(new QueryProperty("article", node.getCode() + "%"));
                    criteria.add(new QueryProperty("name", node.getName()));
                    list_ = dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByGroupAndAssortment", criteria);
                    break;
                case MODEL:
                    criteria.add(new QueryProperty("model", Integer.valueOf(node.getName())));
                    list_ = dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByGroupAndModel", criteria);
                    break;
                case ARTICLE:
                    criteria.add(new QueryProperty("article", node.getName()));
                    list_ = dao.getEntityListByNamedQuery(ClassifierModelView.class, "ClassifierModelView.findByArticleName", criteria);
                    break;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // ФИльтруем записи согласно белого списка
        if (list_ != null) {
            dataList.addAll(list_.stream()
                    .filter(p -> {
                        if (articleWhiteList == null) {
                            return true;
                        }

                        if (articleWhiteList.length < 1) {
                            return true;
                        }

                        for (String s : articleWhiteList) {
                            if (p.getArticleName().startsWith(s)) {
                                return true;
                            }
                        }
                        return false;
                    }).collect(Collectors.toList()));

        }
        detailList.clear();
        selectedProduct = null;
        gvProduct.updateViewPort();
        gvArticle.updateViewPort();
    }

    public String generateLikeExpression(String field, String[] list) {
        if (list == null) {
            return "";
        }

        if (list.length < 1) {
            return "";
        }
        StringBuilder result = new StringBuilder(" (");
        for (String s : list) {
            result.append(field).append(" LIKE '").append(s).append("%' OR ");
        }
        result = new StringBuilder(result.substring(0, result.length() - 4));
        result.append(") ");
        return result.toString();
    }

    public void presetTreeNode(ClassifierNode node) {
        if (node == null) {
            return;
        }
        tree.presetTreeNode(node);
        doCustomUpdate(node);
    }

    public void setProductTableVisible(boolean visible) {
        pProduct.setVisible(visible);
        frameViewPort.getFrameRegion().getToolBar().setVisible(visible);
    }

    public String[] getArticleWhiteList() {
        return articleWhiteList;
    }

    public void setArticleWhiteList(String[] articleWhiteList) {
        this.articleWhiteList = articleWhiteList;
    }

    private enum SelectorType {
        ARTICLE, PRODUCT
    }
}
