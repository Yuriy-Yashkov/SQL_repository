package by.march8.ecs.application.modules.warehouse.internal.storage.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.label.LabelPackItem;
import by.march8.entities.label.LabelSingleItem;
import by.march8.entities.storage.StorageLocationsDetailItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andy 05.11.2018 - 9:42.
 */
public class StorageLocationsDetailItemEditor extends EditingPane {

    private MainController controller;

    private UCTextFieldPanel<String> tfpItem;

    private JLabel lblItemInformation = new JLabel("");
    private ClassifierPickMode pickMode;
    private ClassifierItem classifierItem;

    private GridViewPort gridLabelSingle;
    private GridViewPort gridLabelPack;

    private List<Object> listLabelSingle;
    private List<Object> listLabelPack;

    private StorageLocationsDetailItem source;
    private LabelSingleItem currentSingleBarCode;


    public StorageLocationsDetailItemEditor(FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(1024, 700));
        controller = frameViewPort.getController();

        pickMode = new ClassifierPickMode(controller, ClassifierType.ALL_PRODUCT);

        this.setLayout(new MigLayout());
        init();
        initEvents();
    }

    private void initEvents() {
        tfpItem.addButtonSelectActionListener(a -> {

            Object selectedItem = pickMode.showSelectModal(null);

            if (selectedItem != null) {
                classifierItem = (ClassifierItem) selectedItem;
                prepareItemInformation(classifierItem.getModel());
                updateSingleContent(classifierItem);
            }
        });

        gridLabelSingle.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                currentSingleBarCode = (LabelSingleItem) gridLabelSingle.getSelectedItem();
                if (currentSingleBarCode != null) {
                    updatePackContent(currentSingleBarCode);
                }
            }
        });
    }

    @Override
    public Object getSourceEntity() {
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {

    }

    @Override
    public boolean verificationData() {
        return false;
    }


    private void updateSingleContent(ClassifierItem item) {
        DaoFactory<LabelSingleItem> factory = DaoFactory.getInstance();
        IGenericDaoGUI<LabelSingleItem> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();

        System.out.println(item.getModel().getId());

        criteria.add(new QueryProperty("itemCode", item.getModel().getId()));
        criteria.add(new QueryProperty("itemGrade", item.getItemGrade()));
        criteria.add(new QueryProperty("itemSize", item.getSize()));
        criteria.add(new QueryProperty("itemGrowth", item.getGrowth()));

        //1898410

        try {
            listLabelSingle.clear();
            listLabelSingle.addAll(dao.getEntityListByNamedQueryGUI(LabelSingleItem.class, "LabelSingleItem.findByItemCode", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        gridLabelSingle.updateViewPort();
    }

    private void updatePackContent(LabelSingleItem item) {
        DaoFactory<LabelPackItem> factory = DaoFactory.getInstance();
        IGenericDaoGUI<LabelPackItem> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();

        criteria.add(new QueryProperty("barCodeSingle", item.getBarCode()));

        //1898410

        try {
            listLabelPack.clear();
            listLabelPack.addAll(dao.getEntityListByNamedQueryGUI(LabelPackItem.class, "LabelPackItem.findBySingleLabelBarcode", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        gridLabelPack.updateViewPort();
    }

    private void init() {

        lblItemInformation.setForeground(Color.BLUE);

        add(new JLabel("Изделие"));

        tfpItem = new UCTextFieldPanel<>();
        tfpItem.getEditor().setEditable(false);

        add(tfpItem, "height 20:20, width 460:20:460,wrap");
        add(new JLabel("Выбрано:"));
        add(lblItemInformation, "wrap");

        add(new JPanel(), "height 10:10,  wrap");

        JPanel pLabelSingle = new JPanel(new BorderLayout());
        UCToolBar tbLabelSingle = new UCToolBar();

        gridLabelSingle = new GridViewPort(LabelSingleItem.class, false);
        listLabelSingle = gridLabelSingle.getDataModel();


        pLabelSingle.add(tbLabelSingle, BorderLayout.NORTH);
        pLabelSingle.add(gridLabelSingle, BorderLayout.CENTER);

        JPanel pLabelPack = new JPanel(new BorderLayout());
        UCToolBar tbLabelPack = new UCToolBar();

        gridLabelPack = new GridViewPort(LabelPackItem.class, false);
        listLabelPack = gridLabelPack.getDataModel();

        pLabelPack.add(tbLabelPack, BorderLayout.NORTH);
        pLabelPack.add(gridLabelPack, BorderLayout.CENTER);

        add(new JLabel("Штучные этикетки на изделие"), "height 20:20, width 360:20:360, span 2, wrap");
        add(pLabelSingle, "width 980:980, height 250:250, span 2, wrap");

        add(new JLabel("Упаковочные этикетки на изделие"), "height 20:20, width 360:20:360, span 2, wrap");
        add(pLabelPack, "width 980:980, height 250:250,span 2, wrap");

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }

    private void prepareItemInformation(ClassifierModelParams modelParams) {

        if (modelParams != null) {
            if (modelParams.getName() != null && modelParams.getArticleName() != null) {
                if (modelParams.getArticleName().trim().equals("") || modelParams.getName().trim().equals("")) {
                    lblItemInformation.setText("Изделие не выбрано");
                    tfpItem.getEditor().setText("НЕТ ДАННЫХ");
                } else {
                    tfpItem.getEditor().setText(String.valueOf(modelParams.getArticleCode()));
                    lblItemInformation.setText(
                            String.format("Наименование: %s, Модель: %s , Артикул: %s", modelParams.getName().trim(), modelParams.getModelNumber(), modelParams.getArticleName().trim()));
                }

            } else {
                tfpItem.getEditor().setText("НЕТ ДАННЫХ");
                lblItemInformation.setText("Изделие не выбрано");
            }
        } else {
            tfpItem.getEditor().setText("НЕТ ДАННЫХ");
            lblItemInformation.setText("Изделие не выбрано");
        }
    }
}
