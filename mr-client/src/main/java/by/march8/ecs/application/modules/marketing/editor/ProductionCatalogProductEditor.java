package by.march8.ecs.application.modules.marketing.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.model.ColorListRenderer;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.general.forms.ColorSelectorDialog;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.entities.product.ProductionCatalogArticleEntity;
import by.march8.entities.product.ProductionCatalogArticleView;
import by.march8.entities.product.ProductionCatalogProductEntity;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Set;


/**
 * @author Andy 23.01.2019 - 8:55.
 */
public class ProductionCatalogProductEditor extends EditingPane<ProductionCatalogArticleView, ProductionCatalogArticleEntity> {

    private ProductInformationPane pInformation;
    private JPanel pContent;
    private JPanel pContentCenter;
    private JList<ColorTextItem> lColors;
    private JButton btnSelectColor;

    private ProductionCatalogArticleView article;
    private ProductionCatalogProductEntity source;

    private ColorSelectorDialog colorSelector;
    private UCTextField tfPrice;
    private UCTextField tfDiscount;
    private UCTextField tfPriceDiscont;


    public ProductionCatalogProductEditor(final FrameViewPort frameViewPort, ProductionCatalogArticleView article) {
        setPreferredSize(new Dimension(500, 400));
        controller = frameViewPort.getController();
        setLayout(new BorderLayout());
        this.article = article;

        init();
        initEvents();
    }

    private void init() {
        pInformation = new ProductInformationPane();
        pInformation.setPanelFont(new Font(this.getFont().getName(), Font.BOLD, 12));

        pContent = new JPanel(new BorderLayout());

        lColors = new JList<>();
        lColors.setCellRenderer(new ColorListRenderer());
        JScrollPane spColors = new JScrollPane(lColors);
        spColors.setPreferredSize(new Dimension(150, 100));
        JPanel pContentTop = new JPanel(new BorderLayout());

        btnSelectColor = new JButton();
        btnSelectColor.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/edit24.png", "Edit"));
        btnSelectColor.setPreferredSize(new Dimension(24, 24));

        JPanel pControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pControl.add(btnSelectColor);

        pContentTop.add(pControl, BorderLayout.NORTH);
        pContentTop.add(spColors, BorderLayout.CENTER);

        pContentTop.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pContentTop.setBorder(BorderFactory.createTitledBorder("Цвета для изделия"));


        tfPrice = new UCTextField();
        tfPrice.setComponentParams(null, Float.class, 2);

        tfDiscount = new UCTextField();
        tfDiscount.setComponentParams(null, Float.class, 2);

        tfPriceDiscont = new UCTextField();
        tfPriceDiscont.setComponentParams(null, Float.class, 2);

        pContentCenter = new JPanel(new MigLayout());
        pContentCenter.add(new JLabel("Учетная цена"));
        pContentCenter.add(tfPrice, "width 50:20:50, height 20:20 , wrap");

        pContentCenter.add(new JLabel("Скидка, %"));
        pContentCenter.add(tfDiscount, "width 50:20:50, height 20:20 , wrap");

        pContentCenter.add(new JLabel("Цена со скидкой"));
        pContentCenter.add(tfPriceDiscont, "width 50:20:50, height 20:20 , wrap");

        pContent.add(pContentTop, BorderLayout.NORTH);
        pContent.add(pContentCenter, BorderLayout.CENTER);

        add(pInformation, BorderLayout.NORTH);
        add(pContent, BorderLayout.CENTER);
    }

    private void initEvents() {

        btnSelectColor.addActionListener(a -> {
            if (colorSelector == null) {
                colorSelector = new ColorSelectorDialog(controller);
            }
            if (article != null) {
                String colors_ = source.getColors();
                if (colors_ == null) {
                    colors_ = article.getColors();
                }

                Set<ColorTextItem> set_ = colorSelector
                        .selectColor(ColorPresetHelper.getColorsAsList(article.getColors()),
                                ColorPresetHelper.getColorsAsList(colors_));

                if (set_ != null) {
                    StringBuilder colors = new StringBuilder();
                    for (ColorTextItem item_ : set_) {
                        colors.append(item_.getName()).append(",");
                    }
                    colors = new StringBuilder(colors.substring(0, colors.length() - 1));
                    source.setColors(colors.toString());
                    prepareColorList(colors.toString());
                }
            }
        });
    }

    private void prepareColorList(String colors) {

        if (colors == null) {
            colors = article.getColors();
        }
        DefaultListModel<ColorTextItem> listModel = new DefaultListModel<>();

        String[] aColor = colors.split(",");
        for (String color : aColor) {
            if (color != null) {
                listModel.addElement(ColorPresetHelper.getColorByName(color));
            }
        }

        lColors.setModel(listModel);
        lColors.setSelectedIndex(0);
    }

    @Override
    public Object getSourceEntity() {
        source.setPriceValue(Double.valueOf(tfPrice.getText()));
        source.setDiscountValue(Double.valueOf(tfDiscount.getText()));
        source.setPriceDiscountValue(Double.valueOf(tfPriceDiscont.getText()));
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        updateContent(article);
        if (object != null) {
            source = (ProductionCatalogProductEntity) object;
            prepareColorList(source.getColors());
            tfPrice.setValue(source.getPriceValue());
            tfDiscount.setValue(source.getDiscountValue());
            tfPriceDiscont.setValue(source.getPriceDiscountValue());
        }
    }

    private void updateContent(ProductionCatalogArticleView item) {
        pInformation.setItemName(ItemNameReplacer.transform(item.getItemName()));
        pInformation.setArticleNumber(item.getArticleNumber());
        pInformation.setModelNumber(String.valueOf(item.getModelNumber()));
    }

    @Override
    public boolean verificationData() {
        return true;
    }
}
