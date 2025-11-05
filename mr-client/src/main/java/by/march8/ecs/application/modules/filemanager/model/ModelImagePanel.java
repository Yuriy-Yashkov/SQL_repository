package by.march8.ecs.application.modules.filemanager.model;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImageLabel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.ImageViewDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.dao.ImageManagerJDBC;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.services.images.ImageConfiguration;
import by.march8.ecs.services.images.ImageServiceControl;
import by.march8.ecs.services.images.ModelImageService;
import by.march8.ecs.services.images.ModelImageServiceControlDB;
import net.miginfocom.swing.MigLayout;
import xsd.image.ModelImageItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * @author Andy 28.12.2018 - 7:16.
 */
@SuppressWarnings("all")
public class ModelImagePanel extends JPanel {
    private static int IMAGE_PANEL_HEIGHT = 195;
    private JPopupMenu menu;
    private JMenuItem miImageName;
    private JMenuItem miSetDefault;
    private JMenuItem miShowInFolder;
    private JPanel pFooterPanel;
    private JPanel pCenterPanel;
    private JPanel pDefaultInfo;
    private JPanel pDefaultImage;
    private UCImageLabel lblDefaultImage;
    private JPanel pModelInformation;
    private JPanel pImageColorList;
    private JPanel pImageList;
    //private ColorImageService imageService;
    private ImageServiceControl imageControl;

    private String activeModel = null;
    private ImageItem activeImageItem = null;
    private ContextMenuListener lContextMenuListener;
    private JList<ColorTextItem> colorSelector;
    private MainController controller;

    private JLabel lblItemName;
    private JLabel lblModelNumber;
    private ImageManagerJDBC db;
    private ImageServiceListener eventListener;
    private JScrollPane spSelector;

    public ModelImagePanel(MainController controller) {
        super(new BorderLayout());
        this.controller = controller;
        init();
    }

    private void init() {
        pFooterPanel = new JPanel(new BorderLayout());
        pImageList = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pImageList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pImageList.setBorder(BorderFactory.createTitledBorder("Изображения без цветовой группы"));
        JScrollPane sbImageList = new JScrollPane(pImageList);
        sbImageList.setPreferredSize(new Dimension(0, IMAGE_PANEL_HEIGHT));
        pFooterPanel.add(sbImageList, BorderLayout.CENTER);
        this.add(pFooterPanel, BorderLayout.SOUTH);


        pCenterPanel = new JPanel(new BorderLayout());
        pImageColorList = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pImageColorList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pImageColorList.setBorder(BorderFactory.createTitledBorder("Изображения в цветовых группах"));

        JScrollPane sbImageColorList = new JScrollPane(pImageColorList);
        sbImageColorList.setPreferredSize(new Dimension(0, IMAGE_PANEL_HEIGHT));
        pCenterPanel.add(sbImageColorList, BorderLayout.SOUTH);

        pDefaultImage = new JPanel(new BorderLayout());
        pDefaultImage.setPreferredSize(new Dimension(250, 0));
        pDefaultImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pDefaultImage.setBorder(BorderFactory.createTitledBorder("Основное изображение"));

        lblDefaultImage = new UCImageLabel();
        pDefaultImage.add(lblDefaultImage, BorderLayout.CENTER);
        pModelInformation = new JPanel(new MigLayout());


        pDefaultInfo = new JPanel(new BorderLayout());
        pCenterPanel.add(pDefaultInfo, BorderLayout.CENTER);

        lblItemName = new JLabel("");
        lblModelNumber = new JLabel("");

        pModelInformation.add(new JLabel("Наименование :"));
        pModelInformation.add(lblItemName, "height 80:80, width 300:20:300, wrap");
        pModelInformation.add(new JLabel("Модель :"));
        pModelInformation.add(lblModelNumber, "wrap");

        pDefaultInfo.add(pDefaultImage, BorderLayout.WEST);
        pDefaultInfo.add(pModelInformation, BorderLayout.CENTER);

        this.add(pCenterPanel, BorderLayout.CENTER);

        menu = new JPopupMenu();

        miImageName = new JMenuItem("Изменить цветовую группу");
        miSetDefault = new JMenuItem("Назначить основной");
        miShowInFolder = new JMenuItem("Показать оригинал в папке");
        menu.add(miSetDefault);
        menu.add(miImageName);
        menu.add(miShowInFolder);

        //imageService = ModelImageServiceDB.getInstance();
        imageControl = ModelImageServiceControlDB.getInstance();

        colorSelector = new JList<ColorTextItem>();
        ColorPresetHelper.fillingColorList(colorSelector);


        spSelector = new JScrollPane(colorSelector);
        spSelector.setPreferredSize(new Dimension(250, 300));

        initEvents();
    }

    private void initEvents() {

        colorSelector.setCellRenderer(new ColorListRenderer());

        lContextMenuListener = new ContextMenuListener();
        miImageName.addActionListener(a -> {
            int result = JOptionPane.showConfirmDialog(null, spSelector, "Укажите цветовую группу для изображения", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                ColorTextItem selected = colorSelector.getSelectedValue();
                if (selected != null) {
                    colorSelector.setSelectedIndex(0);
                    sendToColor(activeImageItem, selected.getName());
                }
            }
        });

        miSetDefault.addActionListener(a -> {
            setDefaultImage(activeImageItem);
        });

        miShowInFolder.addActionListener(a -> {
            showInFolder(activeImageItem);
        });
    }

    private void showInFolder(ImageItem activeImageItem) {
        try {
            File f = new File(activeImageItem.getFullOriginalImageFile());
            Desktop.getDesktop().open(new File(f.getParentFile().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultImage(ImageItem image) {
        imageControl.setImageAsDefault(image);
        updateContext(activeModel);
    }

    private void sendToColor(ImageItem image, String colorName) {
        imageControl.sendImageToColorGroup(image, colorName);
        updateContext(activeModel);
    }

    private boolean sendNewImageToColor(File file, String colorName) {
        if (imageControl.sendImageToColorGroup(file, colorName)) {
            updateContext(ModelImageService.parseModelNumber(file.getName()));
            return true;
        }
        return false;
    }

    public void updateContext(String item) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                if (item != null) {

                    ImageConfiguration configuration = imageControl.getImageConfiguration();
                    if (configuration != null) {
                        ModelImageItem modelItem = configuration.loadModelImage(item);
                        if (modelItem != null) {
                            activeModel = modelItem.getModelNumber();

                            loadDefaultInformation(modelItem.getModelNumber());
                            loadImageList(modelItem.getModelNumber());
                            loadImageColorList(modelItem.getModelNumber());
                        } else {
                            clearAll();
                        }

                        if (eventListener != null) {
                            eventListener.selectionChanged(modelItem, imageControl.isDataChanged());
                        }
                    } else {
                        activeModel = item;
                        if (imageControl.haveImage(activeModel)) {
                            loadDefaultInformation(activeModel);
                            loadImageList(activeModel);
                            loadImageColorList(activeModel);
                        } else {
                            clearAll();
                        }
                    }

                }
                return true;
            }
        }

        Task task = new Task("Обработка запроса...");
        task.executeTask();
    }


    private void loadDefaultInformation(String modelNumber) {

        if (modelNumber != null) {
            String img = imageControl.getDefaultImageFileByModelNumber(modelNumber, ModelImageSize.SMALL);
            lblDefaultImage.setImage(img);

            getArticleInformation(modelNumber);
        }
    }

    private void clearAll() {
        pImageList.removeAll();
        pImageList.revalidate();
        pImageList.repaint();

        pImageColorList.removeAll();
        pImageColorList.revalidate();
        pImageColorList.repaint();

        lblDefaultImage.setIcon(null);
        lblItemName.setText("");
        lblModelNumber.setText("");
    }

    private void loadImageList(String modelNumber) {
        pImageList.removeAll();
        List<ImageItem> list = imageControl.getColorImageListByModelAndColor(modelNumber, "РАЗНОЦВЕТ");

        for (ImageItem image : list) {
            if (image != null) {
                ModelImageComponent panel = new ModelImageComponent(image, a -> {
                    return false;
                });
                panel.addMouseListener(lContextMenuListener);
                pImageList.add(panel);
            }
        }
        pImageList.revalidate();
        pImageList.repaint();
    }

    private void loadImageColorList(String model) {
        pImageColorList.removeAll();

        List<ImageItem> list = imageControl.getColorImageListByModel(model);
        for (ImageItem image : list) {
            if (image != null) {
                ModelImageComponent panel = new ModelImageComponent(image, a -> {
                    return false;
                });
                panel.addMouseListener(lContextMenuListener);
                pImageColorList.add(panel);
            }
        }

        pImageColorList.revalidate();
        pImageColorList.repaint();
    }

    public boolean sendImageToModel(File file) {
        int result = JOptionPane.showConfirmDialog(null, spSelector, "Укажите цветовую группу для изображения", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            ColorTextItem selected = (ColorTextItem) colorSelector.getSelectedValue();
            if (selected != null) {
                colorSelector.setSelectedIndex(0);
                if (sendNewImageToColor(file, selected.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addDataChangeListener(ImageServiceListener listener) {
        eventListener = listener;
    }

    private void showViewImageDialog(String file) {
        try {
            Desktop.getDesktop().open(new File(file));
        } catch (IOException e) {
            new ImageViewDialog(controller, file);
            e.printStackTrace();
        }
    }

    private void getArticleInformation(String modelNumber) {
        if (db == null) {
            db = new ImageManagerJDBC();
        }
        lblItemName.setText("<html>");
        lblModelNumber.setText(modelNumber);
        List<String> list = db.getItemNamesByModelNumber(modelNumber);
        if (list != null) {
            for (String s : list) {
                lblItemName.setText(lblItemName.getText() + "<p>" + ItemNameReplacer.transform(s));
            }
            lblItemName.setText(lblItemName.getText() + "</html>");
        }
    }

    public boolean isDataChanged() {
        return true;// (imageControl.isDataChanged();
    }

    class ContextMenuListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                showContextMenu(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                showContextMenu(e);
        }

        private void showContextMenu(MouseEvent e) {
            ModelImageComponent label = ((ModelImageComponent) e.getComponent());
            if (label != null) {
                activeImageItem = label.getImageItem();
                if (activeImageItem.sourceExist()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        public void mouseClicked(MouseEvent e) {
            ModelImageComponent label = ((ModelImageComponent) e.getComponent());
            if (e.getClickCount() == 2) {
                if (label != null) {
                    showViewImageDialog(label.getFileName().replace(".thumbs/", ""));
                }
            }
        }
    }
}
