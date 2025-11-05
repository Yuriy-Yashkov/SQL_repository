package by.march8.ecs.application.modules.sales.editor;

import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxHelper;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.marketing.editor.ProductInformationPane;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.sales.PreOrderSaleDocumentItem;
import by.march8.entities.sales.PreOrderSaleDocumentItemView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class PreOrderProductEditor extends EditingPane {

    private ProductInformationPane pInformation;
    private JComboBox<ColorTextItem> cbColors;
    private UCTextField tfAmount;
    private PreOrderSaleDocumentItem source;

    public PreOrderProductEditor() {
        setPreferredSize(new Dimension(400, 260));
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        pInformation = new ProductInformationPane();
        pInformation.activateAdditionInfo();
        pInformation.setPanelFont(new Font(this.getFont().getName(), Font.BOLD, 13));
        JPanel pContent = new JPanel(new MigLayout());

        cbColors = new JComboBox<>();
        ColorPresetHelper.fillingColorList(cbColors);

        tfAmount = new UCTextField();
        tfAmount.setComponentParams(null, Integer.class, 4);

        pContent.add(new JLabel("Цвет :"));
        pContent.add(cbColors, "width 200:20:200, height 20:20, wrap");
        pContent.add(new JLabel("Количество : "), "width 100:20:100");
        pContent.add(tfAmount, "width 100:20:100, height 20:20, wrap");

        add(pInformation, BorderLayout.NORTH);
        add(pContent, BorderLayout.CENTER);

    }

    @Override
    public Object getSourceEntity() {
        source.setItemColor(((ColorTextItem) cbColors.getSelectedItem()).getName());
        source.setAmount(Integer.valueOf(tfAmount.getText()));
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object != null) {
            source = (PreOrderSaleDocumentItem) object;
            ComboBoxHelper.preset(cbColors, source.getItemColor());
            System.out.println(source.getAmount());
            tfAmount.setText(String.valueOf((int) source.getAmount()));
        }
    }

    @Override
    public boolean verificationData() {
        if (cbColors.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать цвет изделия", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbColors.requestFocusInWindow();
            return false;
        }
        if (tfAmount.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать количество изделия", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfAmount.requestFocusInWindow();
            return false;
        }
        return true;
    }

    @Override
    public void updateEditorContent(Object item) {
        if (item != null) {
            if (item instanceof ClassifierItem) {
                pInformation.setItemName(((ClassifierItem) item).getModel().getName());
                pInformation.setModelNumber(String.valueOf(((ClassifierItem) item).getModel().getModelNumber()));
                pInformation.setArticleNumber(((ClassifierItem) item).getModel().getArticleName());
                pInformation.setGrade(String.valueOf(((ClassifierItem) item).getItemGrade()));
                pInformation.setSizeValue(((ClassifierItem) item).getSizePrint());
            }

            if (item instanceof PreOrderSaleDocumentItemView) {
                pInformation.setItemName(((PreOrderSaleDocumentItemView) item).getItemName());
                pInformation.setModelNumber(String.valueOf(((PreOrderSaleDocumentItemView) item).getModelNumber()));
                pInformation.setArticleNumber(((PreOrderSaleDocumentItemView) item).getArticleNumber());
                pInformation.setGrade(String.valueOf(((PreOrderSaleDocumentItemView) item).getItemGrade()));
                pInformation.setSizeValue(((PreOrderSaleDocumentItemView) item).getItemPrintSize());
            }
        }
    }
}
