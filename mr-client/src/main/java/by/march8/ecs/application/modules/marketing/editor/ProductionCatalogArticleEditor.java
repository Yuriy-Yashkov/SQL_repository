package by.march8.ecs.application.modules.marketing.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxHelper;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.model.ColorListRenderer;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.filemanager.model.ModelImageComponent;
import by.march8.ecs.application.modules.general.forms.ClassifierCompositionSelectorDialog;
import by.march8.ecs.application.modules.general.forms.ColorSelectorDialog;
import by.march8.ecs.application.modules.general.forms.ImageSelectorDialog;
import by.march8.ecs.application.modules.marketing.manager.ProductionCatalogManager;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.services.images.ColorImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import by.march8.entities.classifier.ClassifierArticleComposition;
import by.march8.entities.classifier.ClassifierBrands;
import by.march8.entities.product.ProductionCatalogArticleEntity;
import by.march8.entities.product.ProductionCatalogArticleView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;


/**
 * @author Andy 23.01.2019 - 8:55.
 */
public class ProductionCatalogArticleEditor extends EditingPane<ProductionCatalogArticleView, ProductionCatalogArticleEntity> {

    private ProductInformationPane pInformation;
    private JPanel pContent;
    private JPanel pParams;
    private JPanel pParamsCenter;
    private JPanel pDefaultImage;
    private JList<ColorTextItem> lColors;
    private ModelImageComponent pModelImage;
    private JButton btnSelectImage;
    private JButton btnSelectColor;

    private ProductionCatalogArticleView article_;
    private ProductionCatalogArticleEntity source;

    private ColorImageService imageService;
    private ColorSelectorDialog colorSelector;
    private ClassifierCompositionSelectorDialog compositionSelector;
    private UCTextFieldPanel<String> tfComposition;
    private JComboBox<ClassifierBrands> cbBrands;
    private ProductionCatalogManager manager;


    public ProductionCatalogArticleEditor(final FrameViewPort frameViewPort, ProductionCatalogManager manager) {
        setPreferredSize(new Dimension(500, 440));
        controller = frameViewPort.getController();
        setLayout(new BorderLayout());

        init();
        initEvents();
        prepareBrands(manager.getBrandsList());
    }

    private void init() {
        pInformation = new ProductInformationPane();
        pInformation.setPanelFont(new Font(this.getFont().getName(), Font.BOLD, 12));

        pContent = new JPanel(new BorderLayout());

        pDefaultImage = new JPanel(new BorderLayout());
        pDefaultImage.setPreferredSize(new Dimension(250, 0));
        pDefaultImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pDefaultImage.setBorder(BorderFactory.createTitledBorder("Изображение"));

        pModelImage = new ModelImageComponent();
        pDefaultImage.add(pModelImage, BorderLayout.CENTER);

        lColors = new JList<>();
        lColors.setCellRenderer(new ColorListRenderer());
        JScrollPane spColors = new JScrollPane(lColors);
        spColors.setPreferredSize(new Dimension(150, 100));
        JPanel pContentLeft = new JPanel(new BorderLayout());

        btnSelectColor = new JButton();
        btnSelectColor.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/edit24.png", "Edit"));
        btnSelectColor.setPreferredSize(new Dimension(24, 24));

        JPanel pControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pControl.add(btnSelectColor);

        btnSelectImage = new JButton();
        btnSelectImage.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/edit24.png", "Edit"));
        btnSelectImage.setPreferredSize(new Dimension(24, 24));

        JPanel pImageControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pImageControl.add(btnSelectImage);
        pDefaultImage.add(pImageControl, BorderLayout.NORTH);

        pContentLeft.add(pControl, BorderLayout.NORTH);
        pContentLeft.add(spColors, BorderLayout.CENTER);

        pContentLeft.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pContentLeft.setBorder(BorderFactory.createTitledBorder("Цвета для изделия"));


        pParams = new JPanel(new BorderLayout());
        pParams.add(pContentLeft, BorderLayout.CENTER);
        //pParams.add(pParamsCenter, BorderLayout.CENTER);

        pContent.add(pDefaultImage, BorderLayout.WEST);
        pContent.add(pParams, BorderLayout.CENTER);
        add(pInformation, BorderLayout.NORTH);
        add(pContent, BorderLayout.CENTER);

        JPanel pFooter = new JPanel(new MigLayout());
        pFooter.add(new JLabel("Сырьевой состав изделия"), "wrap");
        tfComposition = new UCTextFieldPanel<>();
        tfComposition.getEditor().setEditable(false);
        pFooter.add(tfComposition, "width 370:20:370, height 20:20, wrap");
        pFooter.add(new JLabel("Брэнд изделия"), "wrap");
        cbBrands = new JComboBox<>();
        pFooter.add(cbBrands, "width 370:20:370, height 20:20");

        add(pFooter, BorderLayout.SOUTH);
        imageService = ModelImageServiceDB.getInstance();
    }

    private void initEvents() {
        btnSelectImage.addActionListener(a -> {
            ImageSelectorDialog selector = new ImageSelectorDialog(controller, String.valueOf(article_.getModelNumber()));
            ImageItem item = selector.selectImage();
            if (item != null) {
                pModelImage.updateImage(item);
                source.setDefaultImage(item.getImage().getImageFile());
            }
        });

        btnSelectColor.addActionListener(a -> {
            if (colorSelector == null) {
                colorSelector = new ColorSelectorDialog(controller);
            }
            if (article_ != null) {
                Set<ColorTextItem> set_ = colorSelector.selectColor(ColorPresetHelper.getColorsAsList(source.getColors()));
                if (set_ != null) {
                    String colors = "";
                    for (ColorTextItem item_ : set_) {
                        colors += item_.getName() + ",";
                    }
                    colors = colors.substring(0, colors.length() - 1);
                    source.setColors(colors);
                    prepareColorList(colors);
                }
            }
        });

        tfComposition.addButtonSelectActionListener(a -> {
            if (compositionSelector == null) {
                compositionSelector = new ClassifierCompositionSelectorDialog(controller);
            }
            if (article_ != null) {
                ClassifierArticleComposition set_ = compositionSelector.selectComposition(article_.getModelNumber());
                if (set_ != null) {
                    String composition = "";
                    if (!set_.getComposition_1().isEmpty()) {
                        composition += set_.getComposition_1() + ",";
                    }

                    if (!set_.getComposition_2().isEmpty()) {
                        composition += set_.getComposition_2() + ",";
                    }

                    if (!set_.getComposition_3().isEmpty()) {
                        composition += set_.getComposition_3() + ",";
                    }

                    if (!set_.getComposition_4().isEmpty()) {
                        composition += set_.getComposition_4() + ",";
                    }

                    if (!composition.isEmpty() && composition.endsWith(",")) {
                        composition = composition.substring(0, composition.length() - 1);
                    }
                    source.setMaterialComposition(composition);
                    tfComposition.setText(composition);
                    tfComposition.getEditor().setEditable(false);
                }
            }
        });
    }

    private void prepareColorList(String colors) {
        if (colors != null) {
            DefaultListModel<ColorTextItem> listModel = new DefaultListModel<>();

            String[] aColor = colors.split(",");
            for (String color : aColor) {
                if (color != null) {
                    listModel.addElement(ColorPresetHelper.getColorByName(color));
                }
            }

            lColors.setModel(listModel);
            lColors.setSelectedIndex(0);
        } else {
            DefaultListModel<ColorTextItem> listModel = new DefaultListModel<>();
            lColors.setModel(listModel);
        }
    }

    private void prepareImage(ProductionCatalogArticleEntity source) {
        if (source.getDefaultImage() != null) {
            ImageItem image_ = imageService.getImageByModelNumberAndImageName(
                    String.valueOf(article_.getModelNumber()), source.getDefaultImage());
            if (image_ != null) {
                pModelImage.updateImage(image_);
            } else {
                pModelImage.clearImage();
            }
        } else {
            pModelImage.clearImage();
        }


    }

    @Override
    public void updateEditorContent(ProductionCatalogArticleView item) {
        if (item != null) {
            if (article_ == null) {
                article_ = item;
                updateContent(article_);
            } else {
                if (article_.getId() != item.getId()) {
                    article_ = item;
                    updateContent(article_);
                }
            }
        } else {
            pInformation.setItemName("");
            pInformation.setArticleNumber("");
            pInformation.setModelNumber("");
        }
    }


    @Override
    public Object getSourceEntity() {
        if (cbBrands.getSelectedItem() != null) {
            source.setBrand(cbBrands.getSelectedItem().toString());
        } else {
            source.setBrand(null);
        }
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object != null) {
            source = (ProductionCatalogArticleEntity) object;
            tfComposition.setText(source.getMaterialComposition());
            tfComposition.getEditor().setEditable(false);

            ComboBoxHelper.preset(cbBrands, source.getBrand());

            prepareColorList(source.getColors());
            prepareImage(source);
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

    private void prepareBrands(List<ClassifierBrands> brands) {
        cbBrands.setModel(new DefaultComboBoxModel(brands.toArray()));
        cbBrands.setSelectedIndex(-1);
    }
}
