package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentItemBase;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andy 19.01.2016.
 */
public class ManualInputItemEditor extends EditingPane {

    private static final Color COLOR_DISABLED = new Color(255, 200, 200);
    private static final Color COLOR_ENABLED = new Color(200, 255, 200);
    private UCTextFieldPanel<String> tfpMaterial;
    private JLabel lblMaterialInformation = new JLabel("Наименование: 154Т-TEX-EXAMPLE Артикул: 154Т");
    private JTextField tfColor;
    private UCTextField tfGrade;
    private UCTextField tfPrice;
    private UCTextField tfWeight;
    private MainController controller;
    private ClassifierItem classifierItem;
    private ClassifierModelParams model;
    private SaleDocumentItemBase source;
    private NewFocusListener listener = new NewFocusListener();

    private SaleDocumentBase document = null;

    public ManualInputItemEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(490, 250));
        controller = frameViewPort.getController();
        this.setLayout(new MigLayout());
        init();
        initEvents();
    }


    private void init() {

        lblMaterialInformation.setForeground(Color.BLUE);

        add(new JLabel("Шифр артикула"));

        tfpMaterial = new UCTextFieldPanel<>();
        tfpMaterial.getEditor().setEditable(false);

        add(tfpMaterial, "height 20:20, width 360:20:360,wrap");
        add(new JLabel());
        add(lblMaterialInformation, "wrap");

        add(new JPanel(), "height 10:10,  wrap");

        JLabel lblPrice = new JLabel("Цена единицы");
        add(lblPrice);
        tfPrice = new UCTextField();
        tfPrice.setComponentParams(lblPrice, Float.class, 2);
        add(tfPrice, "height 20:20, width 80:20:80,wrap");

        JLabel lblWeight = new JLabel("Вес");
        add(lblWeight);
        tfWeight = new UCTextField();
        tfWeight.setComponentParams(lblWeight, Float.class, 2);
        add(tfWeight, "height 20:20, width 80:20:80,wrap");

        add(new JPanel(), "height 10:10,  wrap");

        add(new JLabel("Цвет"));
        tfColor = new JTextField();

        //tfColor.setEditable(false);
        add(tfColor, "height 20:20, width 150:20:150,wrap");

        JLabel lblGrade = new JLabel("Сорт");
        add(lblGrade);
        tfGrade = new UCTextField();
        //tfGrade.setEditable(false);
        tfGrade.setComponentParams(lblGrade, Integer.class, -1);
        add(tfGrade, "height 20:20, width 50:20:50,wrap");


        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }


    private void initEvents() {
        tfpMaterial.addButtonSelectActionListener(a -> {
            ClassifierPickMode pickMode = new ClassifierPickMode(controller, ClassifierType.MATERIAL);
            Object selectedItem = pickMode.showSelectModal(null);

            if (selectedItem != null) {
                classifierItem = (ClassifierItem) selectedItem;
                if (classifierItem.getModel() != null) {
                    model = classifierItem.getModel();
                    prepareMaterialInfo(model);
                    tfpMaterial.preset(String.valueOf(model.getArticleCode()));

                    //tfPrice.setText(String.valueOf(classifierItem.getPriceExport()));
                    //source.setValuePriceForAccounting(classifierItem.getPriceWholesale());

                    tfPrice.selectAll();
                    tfPrice.requestFocusInWindow();
                }
            }
        });

        setFocusEvents(this);
    }

    private void setFocusEvents(Component component) {

        if (component instanceof JTextComponent) {
            component.addFocusListener(listener);
        }

        if (component instanceof JComboBox) {

            component.addFocusListener(listener);
        }

        if (component instanceof JXDatePicker) {
            ((JXDatePicker) component).getEditor().addFocusListener(listener);
        }

        if (component instanceof JPanel) {
            for (Component child : ((Container) component).getComponents()) {
                setFocusEvents(child);
            }
        }
    }

    @Override
    public void defaultFillingData() {
        tfGrade.setText("1");
        tfColor.setText(source.getItemColor());
        prepareMaterialInfo(model);

        if (document.getDocumentExport() == 1) {
            // Если документ экспортный - правим стоимость в рублях
            tfPrice.setText(String.valueOf(source.getValuePriceCurrency()));
        } else {
            // Документ отгружается по стране - правим стоимость в бел. рублях
            tfPrice.setText(String.valueOf(source.getValuePriceForAccountingAbs()));
        }

        double weight = (double) source.getAmountInPack() / 100;
        tfWeight.setText(String.valueOf(weight));
    }

    @Override
    public Object getSourceEntity() {
        double weightFloat = Math.round(Double.valueOf(tfWeight.getText()) * 100);

        int weightInteger = (int) weightFloat;

        source.setAmountInPack(weightInteger);

        String s = tfColor.getText().trim();
        if (s.length() > 15) {
            s = s.substring(0, 15);
        }

        source.setItemColor(s);
        source.setValueVAT(classifierItem.getValueVat());

        if (document.getDocumentExport() == 1) {
            // Если документ экспортный - правим стоимость в рублях
            source.setValuePriceCurrency(Double.valueOf(tfPrice.getText()));
        } else {
            // Документ отгружается по стране - правим стоимость в бел. рублях
            source.setValuePriceForAccounting(Double.valueOf(tfPrice.getText()));
            source.setValuePriceCurrency(0.0);
        }

        source.setSystemUser("SKLAD6 # proiz");
        source.setSystemDate(new Date());

        source.setItemType(3);
        source.setItemScanCode(0L);
        source.setPartNumber(1);
        source.setItemEanCode("0");
        source.setItemPriceList("ПР01");
        source.setAmount(1);
        source.setItemGrade(1);
        source.setItemGrowz(0);
        source.setItemSize(0);

        source.setItem(classifierItem);
        classifierItem.setModel(model);

        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {

        if (object != null) {
            source = (SaleDocumentItemBase) object;
            classifierItem = source.getItem();
            model = classifierItem.getModel();
            //source.setItemColor((SaleDocumentItemBase) object.);
        } else {
            source = new SaleDocumentItemBase();
            classifierItem = new ClassifierItem();
            model = new ClassifierModelParams();
            classifierItem.setModel(model);
        }

        defaultFillingData();
    }

    @Override
    public boolean verificationData() {
        // ПОлучатель не указан
        if (tfpMaterial.getEditor().getText().trim().equals("НЕТ ДАННЫХ")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо выбрать полотно", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfpMaterial.setFocus();
            return false;
        }

        if (Float.valueOf(tfPrice.getText()) == 0) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать цену отгружаемого материала", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfPrice.requestFocusInWindow();
            return false;
        }

        if (Float.valueOf(tfWeight.getText()) == 0) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать вес отгружаемого материала", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfWeight.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private void prepareMaterialInfo(ClassifierModelParams modelParams) {
        if (model != null) {
            if (model.getName() != null && model.getArticleName() != null) {
                if (model.getArticleName().trim().equals("") || model.getName().trim().equals("")) {
                    lblMaterialInformation.setText("Полотно не выбрано");
                    tfpMaterial.getEditor().setText("НЕТ ДАННЫХ");
                } else {
                    tfpMaterial.getEditor().setText(String.valueOf(model.getArticleCode()));
                    lblMaterialInformation.setText(
                            String.format("Наименование: %s , Артикул: %s", modelParams.getName().trim(), modelParams.getArticleName().trim()));
                }

            } else {
                tfpMaterial.getEditor().setText("НЕТ ДАННЫХ");
                lblMaterialInformation.setText("Полотно не выбрано");
            }
        }
    }

    @Override
    public void beforeEditing(final Object object) {
        // получаем объект документа, для того что бы разделить вводимую оператором стоимость изделия
        // для экспортных документов и документов по стране

        // Без проверок на приведение, т.к. гарантировано в данный редактор будет передаваться только
        // объект данного типа
        document = (SaleDocumentBase) object;
        System.out.println("Обновление документа");
    }

    private class NewFocusListener implements FocusListener {

        Color normalColor;

        @Override
        public void focusGained(final FocusEvent e) {
            Component component = e.getComponent();
            normalColor = component.getBackground();
            if (component instanceof JTextComponent) {
                if (((JTextComponent) component).isEditable()) {
                    component.setBackground(COLOR_ENABLED);
                    ((JTextComponent) component).selectAll();
                } else {
                    component.setBackground(COLOR_DISABLED);
                }
            } else {
                if (component instanceof JComboBox) {
                    if (((JComboBox) component).isEditable()) {
                        component.setBackground(COLOR_ENABLED);
                    } else {
                        component.setBackground(COLOR_DISABLED);
                    }
                } else if (((UCDatePicker) component).isEditable()) {
                    component.setBackground(COLOR_ENABLED);
                } else {
                    component.setBackground(COLOR_DISABLED);
                }
            }
        }

        @Override
        public void focusLost(final FocusEvent e) {
            Component component = e.getComponent();
            if (component.isEnabled()) {
                component.setBackground(Color.white);
            } else {
                component.setBackground(Color.LIGHT_GRAY);
            }
        }
    }
}
