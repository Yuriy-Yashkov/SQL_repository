package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxHelper;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCCheckBoxList;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.dao.ERPRemainsTreeJDBC;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNode;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class ERPRemainsArticleSelector extends BasePickDialog {

    private String result;
    private JComboBox<String> cbModels;
    private UCCheckBoxList cblArticles;
    private JPanel pComboBox;
    private ERPRemainsTreeJDBC db;

    public ERPRemainsArticleSelector(MainController controller, ERPRemainsTreeJDBC db) {
        super(controller);
        this.db = db;
        init();
        initEvents();
    }

    private void init() {
        getToolBar().setVisible(false);
        setFrameSize(new Dimension(350, 400));
        cbModels = new JComboBox<>();
        pComboBox = new JPanel(new MigLayout());
        pComboBox.add(new JLabel("Модели"), "width 100:20:100");
        pComboBox.add(cbModels, "width 220:20:220, height 20:20, wrap");

        cblArticles = new UCCheckBoxList();
        JScrollPane sp = new JScrollPane(cblArticles);

        getCenterContentPanel().add(pComboBox, BorderLayout.NORTH);
        getCenterContentPanel().add(sp, BorderLayout.CENTER);
    }

    private void initEvents() {
        cbModels.addActionListener(a -> {
            String model = (String) cbModels.getSelectedItem();
            if (model != null && !model.isEmpty()) {
                result = null;
                updateArticleList(Integer.valueOf(model.trim()));
            }
        });

        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                result = prepareResultString();
                return result != null;
            }

            @Override
            public boolean canCancel() {
                result = null;
                return true;
            }
        });
    }

    public String selectArticles(ClassifierNode node, String preset) {
        updateModelList(node);
        if (preset != null) {
            String[] parser = preset.split(";");
            presetModel(Integer.valueOf(parser[0]));
            if (parser.length > 1) {
                presetArticles(parser[1]);
            }
        }
        showModalFrame();
        return result;
    }

    private void presetArticles(String articles) {
        String[] array = articles.split(",");
        for (int i = 0; i < cblArticles.getModel().getSize(); i++) {
            for (int z = 0; z < array.length; z++) {
                if (array[z].equals(cblArticles.getModel().getElementAt(i).getText())) {
                    cblArticles.getModel().getElementAt(i).setSelected(true);
                }
            }
        }
    }

    private void presetModel(int model) {
        ComboBoxHelper.preset(cbModels, String.valueOf(model));
        updateArticleList(model);
    }

    private void updateModelList(ClassifierNode node) {
        List<String> modelList = db.getModelListByNode(node);
        cbModels.setModel(new DefaultComboBoxModel(modelList.toArray()));
        cbModels.setSelectedIndex(-1);

        cblArticles.setModel(new DefaultComboBoxModel<>());
    }

    private void updateArticleList(int modelNumber) {
        List<String> articleList = db.getArticleListByNode(modelNumber);
        DefaultListModel<JCheckBox> model = new DefaultListModel<>();
        for (String article : articleList) {
            model.addElement(new JCheckBox(article));
        }
        cblArticles.setModel(model);
    }

    private String prepareResultString() {
        if (cbModels.getSelectedItem() == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < cblArticles.getModel().getSize(); i++) {
            if (cblArticles.getModel().getElementAt(i).isSelected()) {
                result.append(cblArticles.getModel().getElementAt(i).getText()).append(",");
            }
        }

        if (result.length() > 1) {
            result = new StringBuilder(result.substring(0, result.length() - 1));
            return cbModels.getSelectedItem() + ";" + result;
        } else {
            return cbModels.getSelectedItem() + ";";
        }
    }

}
