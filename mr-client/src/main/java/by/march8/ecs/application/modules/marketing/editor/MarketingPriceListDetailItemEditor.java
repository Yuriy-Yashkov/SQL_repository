package by.march8.ecs.application.modules.marketing.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.dao.MarketingPriceListMarchJDBC;
import by.march8.ecs.application.modules.marketing.model.ScaleItem;
import by.march8.ecs.application.modules.marketing.model.ScaleItemPreliminary;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.ecs.framework.common.money.RoundUtils;
import by.march8.entities.classifier.ClassifierModelView;
import dept.marketing.cena.CenaPDB;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andy 26.10.2017.
 */
public class MarketingPriceListDetailItemEditor extends EditingPane {

    private UCTextFieldPanel<String> tfpItem;
    private JLabel lblItemInformation = new JLabel("");

    private ClassifierModelView classifierItem;
    private GridViewPort gridViewPort;
    private ArrayList<Object> detailList;
    private List<ScaleItem> scaleList;

    private MainController controller;
    private ClassifierPickMode pickMode;

    public MarketingPriceListDetailItemEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(450, 250));
        controller = frameViewPort.getController();

        pickMode = new ClassifierPickMode(controller, ClassifierType.PRODUCT);

        this.setLayout(new MigLayout());
        init();
        initEvents();
    }


    private void initEvents() {
        tfpItem.addButtonSelectActionListener(a -> {

            Object selectedItem = pickMode.showSelectModal(null);

            if (selectedItem != null) {
                classifierItem = (ClassifierModelView) selectedItem;
                if (classifierItem.getArticleCode() != 0) {
                    loadProcessing(classifierItem);
                    prepareItemInformation(classifierItem);
                }
            }
        });
    }

    private void loadProcessing(ClassifierModelView classifierItem) {
        if (classifierItem == null) {
            return;
        }

        MarketingPriceListMarchJDBC db = new MarketingPriceListMarchJDBC();
        // Получаем размерная шкала изделия
        scaleList = createScales(db.getGetScaleItemListByItemId(classifierItem.getId(), 1));

        // Дополним размерную шкалу значения мебестоимости и рентабельности из базы
        CenaPDB pdb = new CenaPDB();

        for (ScaleItem item : scaleList) {
            try {
                float[] values = pdb.getSstoimostRentabel(String.valueOf(classifierItem.getArticleCode()), item.getMaxSize());

                double primeCost = values[0];
                double profitability = values[1];

                //rent = new BigDecimal(((rs_.getDouble("cno")-ss_sprav)/ss_sprav)*100).setScale(2, RoundingMode.HALF_UP).doubleValue();

                if (profitability < 1) {
                    profitability = (((item.getPrice() - primeCost) / primeCost) * 100);
                }

                double primeCostCalculated = (item.getPrice() * 100) / (profitability + 100);
                item.setPrimeCost((float) RoundUtils.round(primeCostCalculated, 5));
                item.setProfitability((float) RoundUtils.round(profitability, 4));

                item.setId(classifierItem.getId());
                //item.setPrimeCostCalc(values[0]);
                //item.setProfitabilityCalc(values[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pdb.disConn();

        detailList.clear();
        detailList.addAll(scaleList);
        gridViewPort.updateViewPort();
    }

    private void prepareItemInformation(ClassifierModelView modelParams) {
        if (modelParams != null) {
            if (modelParams.getName() != null && modelParams.getArticleName() != null) {
                if (modelParams.getArticleName().trim().equals("") || modelParams.getName().trim().equals("")) {
                    lblItemInformation.setText("Изделие не выбрано");
                    tfpItem.getEditor().setText("НЕТ ДАННЫХ");
                } else {
                    tfpItem.getEditor().setText(String.valueOf(modelParams.getArticleCode()));
                    lblItemInformation.setText(
                            String.format("Наименование: %s , Артикул: %s", modelParams.getName().trim(), modelParams.getArticleName().trim()));
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

    @Override
    public Object getSourceEntity() {
        return scaleList;
    }


    @Override
    public void setSourceEntity(final Object object) {
        prepareItemInformation(null);
        detailList.clear();
        gridViewPort.updateViewPort();
    }

    @Override
    public boolean verificationData() {
        if (scaleList == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо выбрать изделие для добавления в прейскурант", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (scaleList.size() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо выбрать изделие для добавления в прейскурант", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    @Override
    public void phaseBeforeShowing() {
        defaultFillingData();
    }


    private List<ScaleItem> createScales(List<ScaleItemPreliminary> list) {
        double currentSize = 0;
        double lastSize = 0;
        List<ScaleItem> scaleList = new ArrayList<>();
        for (ScaleItemPreliminary aList : list) {
            lastSize = aList.getPrice();
            if (currentSize != lastSize) {
                scaleList.add(new ScaleItem(aList.getSize(), aList.getSize(), (float) aList.getPrice()));
                currentSize = lastSize;
            } else {
                if (scaleList.size() > 0) {
                    scaleList.get(scaleList.size() - 1).setMaxSize(aList.getSize());
                }
            }
        }
        return scaleList;
    }

    private void init() {

        lblItemInformation.setForeground(Color.BLUE);

        add(new JLabel("Изделие"));

        tfpItem = new UCTextFieldPanel<>();
        tfpItem.getEditor().setEditable(false);

        add(tfpItem, "height 20:20, width 360:20:360,wrap");
        add(new JLabel("Выбрано:"));
        add(lblItemInformation, "wrap");

        add(new JPanel(), "height 10:10,  wrap");

        gridViewPort = new GridViewPort(ScaleItem.class, false);
        detailList = gridViewPort.getDataModel();

        add(gridViewPort, "height 120:120, span 2, wrap");

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }
}
