package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.dao.ERPRemainsJDBC;
import by.march8.ecs.application.modules.marketing.dao.ERPRemainsTreeJDBC;
import by.march8.ecs.application.modules.marketing.model.ERPFilter;
import by.march8.ecs.application.modules.marketing.model.ERPFilterCategory;
import by.march8.ecs.application.modules.marketing.model.ModelSelectionItem;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNodeType;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTreeContentLoader;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTreeDataSource;
import by.march8.entities.warehouse.ERPRemainsEntity;
import dept.sbit.protocol.forms.ArticlePicker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ERPRemainsFilterDialog extends BasePickDialog implements ClassifierTreeContentLoader {
    private ClassifierTree tree;
    private JPanel content;
    private JPanel pLeft;

    private ERPRemainsTreeJDBC db;
    private JPanel pControls;
    private JPanel pTop;
    private JRadioButton chbArticle;
    private JRadioButton chbModel;
    private JPanel pTable;
    private GridViewPort<ModelSelectionItem> gvItem;
    private List<ModelSelectionItem> data;
    private ClassifierNode activeNode;

    private JPanel pCounter;
    private JCheckBox chbCounter;
    private UCTextField tfCounter;
    private JCheckBox chbFirstGradeOnly;

    private List<ERPRemainsEntity> result;


    private UCTextFieldPanel<String> tfArticles;
    private ERPRemainsArticleSelector selector;
    private JButton btnPreview;

    private JCheckBox chbCategory;
    private JComboBox<ERPFilterCategory> cbCategory;
    private JPanel pCategorySelector;

    public ERPRemainsFilterDialog(MainController controller, ERPRemainsTreeJDBC db) {
        super(controller);
        setTitle("Отбор данных из остатков ERP");
        this.db = db;
        init();
        initEvents();
    }

    private void init() {
        setFrameSize(new Dimension(800, 400));

        btnPreview = new JButton("Просмотреть");
        //getButtonPanel().add(btnPreview, 0);

        pCategorySelector = new JPanel(new MigLayout());
        chbCategory = new JCheckBox("Общий критерий отбора");

        cbCategory = new JComboBox<>();
        cbCategory.setVisible(false);
        createCategoryPreset();

        pCategorySelector.add(chbCategory, "height 20:20, width 200:20:200, wrap");
        pCategorySelector.add(cbCategory, "height 20:20, width 250:20:250, wrap");

        tree = new ClassifierTree(this);
        tree.setExpandLimit(ClassifierNodeType.CATEGORY);
        tree.setVisibleSearchPanel(false);
        tree.setPreferredSize(new Dimension(260, 100));

        pTop = new JPanel(new MigLayout());
        pControls = new JPanel(new BorderLayout());

        chbArticle = new JRadioButton("Артикула");
        chbModel = new JRadioButton("Модели");

        ButtonGroup group = new ButtonGroup();
        group.add(chbModel);
        group.add(chbArticle);

        chbArticle.setSelected(true);

        tfArticles = new UCTextFieldPanel<>();
        tfArticles.getEditor().setEditable(false);
        tfArticles.getBtnClear().setVisible(true);

        pTop.add(chbArticle, "width 100:20:100, height 20:20");
        pTop.add(tfArticles, "width 380:20:380, height 20:20, wrap");
        pTop.add(chbModel, "width 100:20:100, height 20:20, wrap");

        chbFirstGradeOnly = new JCheckBox("Только 1-й сорт");
        chbFirstGradeOnly.setSelected(true);

        pControls.add(pTop, BorderLayout.NORTH);

        gvItem = new GridViewPort<>(ModelSelectionItem.class);
        data = gvItem.getDataModel();

        pTable = new JPanel(new BorderLayout());
        pTable.add(getToolBar(), BorderLayout.NORTH);
        pTable.add(gvItem, BorderLayout.CENTER);
        pControls.add(pTable, BorderLayout.CENTER);

        content = new JPanel(new BorderLayout());

        pLeft = new JPanel(new BorderLayout());
        pLeft.add(pCategorySelector, BorderLayout.NORTH);
        pLeft.add(tree, BorderLayout.CENTER);

        pCounter = new JPanel(new MigLayout());
        tfCounter = new UCTextField();
        tfCounter.setComponentParams(null, Integer.class, 4);

        chbCounter = new JCheckBox("Количество на артикул");

        pCounter.add(chbCounter, "height 20:20");
        pCounter.add(tfCounter, "width 50:20:50, height 20:20, wrap");
        pCounter.add(chbFirstGradeOnly, "height 20:20");

        pLeft.add(pCounter, BorderLayout.SOUTH);
        content.add(pLeft, BorderLayout.WEST);
        content.add(pControls, BorderLayout.CENTER);
        getCenterContentPanel().add(content);

        selector = new ERPRemainsArticleSelector(controller, db);
    }

    private void initEvents() {
        tfArticles.addButtonSelectActionListener(a -> {
            ArticlePicker picker = new ArticlePicker(this, tfArticles.getText());
            String result = picker.selectArticles();
            if (result != null) {
                tfArticles.setText(result);
                tfArticles.getEditor().setEditable(false);
            }
        });

        chbArticle.addActionListener(a -> {
            tfArticles.setVisible(chbArticle.isSelected());
            setVisibleGridControls(!chbArticle.isSelected());
        });

        chbCounter.addActionListener(a -> {
            tfCounter.setVisible(chbCounter.isSelected());
            tfCounter.setText("1");
        });

        chbCategory.addActionListener(a -> {
            cbCategory.setVisible(chbCategory.isSelected());
        });

        chbModel.addActionListener(a -> {
            setVisibleGridControls(chbModel.isSelected());
            tfArticles.setVisible(!chbModel.isSelected());
        });

        getToolBar().getBtnNewItem().addActionListener(a -> {
            addCriteria();
        });

        getToolBar().getBtnEditItem().addActionListener(a -> {
            editCriteria();
        });

        getToolBar().getBtnDeleteItem().addActionListener(a -> {
            deleteCriteria();
        });

        tree.addTreeListener(a -> {
            activeNode = a;
        });

        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                result = prepareRemainsByCriteria();
                return result != null;
            }

            @Override
            public boolean canCancel() {
                result = null;
                return true;
            }
        });
    }

    private void createCategoryPreset() {
        DefaultComboBoxModel<ERPFilterCategory> model = new DefaultComboBoxModel<>();
        model.addElement(new ERPFilterCategory(1, "Взрослое", new int[]{411, 412, 415, 421, 422, 425, 431, 432, 435}));
        model.addElement(new ERPFilterCategory(2, "Детское", new int[]{413, 416, 423, 426, 433}));

        model.addElement(new ERPFilterCategory(3, "Взрослое без спортивного", new int[]{411, 412, 421, 422, 431, 432}));
        model.addElement(new ERPFilterCategory(4, "Детское без спортивного", new int[]{413, 423, 433}));

        model.addElement(new ERPFilterCategory(5, "Мужское", new int[]{411, 421, 431}));
        model.addElement(new ERPFilterCategory(6, "Женское", new int[]{412, 422, 432}));

        model.addElement(new ERPFilterCategory(7, "Спортивное", new int[]{415, 416, 425, 426, 435}));
        model.addElement(new ERPFilterCategory(8, "Спортивное взрослое", new int[]{415, 425, 435}));
        model.addElement(new ERPFilterCategory(9, "Спортивное детское", new int[]{416, 426}));

        cbCategory.setModel(model);
    }

    public List<ERPRemainsEntity> selectRemains() {
        chbArticle.setSelected(true);
        tfArticles.setVisible(true);
        setVisibleGridControls(false);
        tfCounter.setVisible(false);
        chbCategory.setSelected(false);
        cbCategory.setSelectedIndex(0);
        cbCategory.setVisible(false);

        updateModelContent();
        showModalFrame();

        return result;
    }

    public int getAmountLimit() {
        if (chbCounter.isSelected()) {
            if (tfCounter.getText().trim().equals("")) {
                return 1;
            }
            try {
                return Integer.valueOf(tfCounter.getText().trim());
            } catch (Exception e) {
                return 1;
            }

        } else {
            return 1;
        }
    }

    private void setVisibleGridControls(boolean b) {
        getToolBar().setVisible(b);
        gvItem.setVisible(b);
    }

    private void updateModelContent() {
        int id = 0;
        for (ModelSelectionItem item : data) {
            item.setId(id++);
        }

        gvItem.updateViewPort();
        for (ModelSelectionItem item : data) {
            if (item != null) {
                prepareArticles(item);
                gvItem.setRowHeight(item, item.getLineHeight());
            }
        }
    }

    @Override
    public ClassifierTreeDataSource getContentDataSource() {
        return db;
    }

    @Override
    public int[] getCodeIgnoreList() {
        return new int[0];
    }

    @Override
    public void updateForNode(ClassifierNode node) {
    }

    private void prepareArticles(ModelSelectionItem item) {
        item.setLineHeight(16);
        if (item.getArticles() != null) {
            String[] articles = item.getArticles().split(",");
            item.setLineHeight(articles.length * 16);
            StringBuilder c_ = new StringBuilder();
            for (String c : articles) {
                c_.append("<p>").append(c).append("</font>");
            }
            item.setArticlesToHTML("<html>" + c_ + "</html>");
        } else {
            item.setArticlesToHTML("");
        }
    }

    private void addCriteria() {
        if (activeNode == null) {
            return;
        }

        String result = selector.selectArticles(activeNode, null);
        if (result != null) {
            String[] parser = result.split(";");
            ModelSelectionItem item = new ModelSelectionItem();
            item.setModel(Integer.valueOf(parser[0]));
            if (parser.length > 1) {
                item.setArticles(parser[1]);
            }
            data.add(item);
        }
        updateModelContent();
    }

    private void editCriteria() {
        ModelSelectionItem selectedItem = gvItem.getSelectedItem();
        if (selectedItem != null) {
            String preset = selectedItem.getModel() + ";" + selectedItem.getArticles();

            String result = selector.selectArticles(activeNode, preset);
            if (result != null) {
                String[] parser = result.split(";");
                selectedItem.setModel(Integer.valueOf(parser[0]));
                if (parser.length > 1) {
                    selectedItem.setArticles(parser[1]);
                } else {
                    selectedItem.setArticles("");
                }
            }
            updateModelContent();
        }
    }

    private void deleteCriteria() {
        ModelSelectionItem selectedItem = gvItem.getSelectedItem();
        if (selectedItem != null) {
            final int answer = Dialogs.showDeleteDialog(selectedItem.getModel());
            if (answer == 0) {
                try {
                    data.remove(selectedItem);
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка удаления записи");
                }
                updateModelContent();
            }
        }
    }

    private List<ERPRemainsEntity> prepareRemainsByCriteria() {
        ERPRemainsJDBC db = new ERPRemainsJDBC();
        ERPFilter filter = new ERPFilter();
        filter.setNode(activeNode);
        if (chbArticle.isSelected()) {
            if (!tfArticles.getText().trim().equals("")) {
                filter.setArticles(tfArticles.getText());
            } else {
                filter.setArticles(null);
            }
        } else {
            filter.setArticles(null);
        }

        if (chbModel.isSelected()) {
            if (!data.isEmpty()) {
                filter.setModels(data);
            } else {
                filter.setModels(null);
            }
        } else {
            filter.setModels(null);
        }

        filter.setGradeOnly(chbFirstGradeOnly.isSelected());
        if (chbCategory.isSelected()) {
            ERPFilterCategory category = (ERPFilterCategory) cbCategory.getSelectedItem();
            if (category != null) {
                List<ERPRemainsEntity> resultList = new ArrayList<>();
                for (int categoryCode : category.getCategory()) {
                    System.out.println(category.getCategory());
                    ClassifierNode node = new ClassifierNode();
                    node.setCode(categoryCode);
                    filter.setNode(node);
                    List<ERPRemainsEntity> list_ = db.getRemainsByCriteria(filter);
                    if (list_ != null) {
                        resultList.addAll(list_);
                    }
                }

                return resultList;
            }
            return null;
        } else {
            return db.getRemainsByCriteria(filter);
        }
    }
}
