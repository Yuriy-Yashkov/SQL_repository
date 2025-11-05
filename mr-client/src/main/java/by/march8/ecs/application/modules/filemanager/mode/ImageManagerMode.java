package by.march8.ecs.application.modules.filemanager.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.UCFileListComponent;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.UCImageListComponent;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.FileListMode;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.dao.ImageManagerJDBC;
import by.march8.ecs.application.modules.filemanager.model.FileListEventAdaptor;
import by.march8.ecs.application.modules.filemanager.model.LabelItem;
import by.march8.ecs.application.modules.filemanager.model.ModelImagePanel;
import by.march8.ecs.application.modules.references.classifier.model.ClassifierNodeType;
import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.services.images.ImageServiceControl;
import by.march8.ecs.services.images.ModelImageService;
import by.march8.ecs.services.images.ModelImageServiceControlDB;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * @author Andy 18.12.2018 - 11:59.
 */
public class ImageManagerMode extends AbstractFunctionalMode {
    private ModelImagePanel imagePanel;
    private JButton btnUpdate;
    private ClassifierTree tree;
    private UCFileListComponent fileList;
    private JButton btnUpdateLastProduction;
    private JList<LabelItem> lLastProduction;
    private UCToolBar tbMain;
    private UCImageListComponent pNewImageTest;


    public ImageManagerMode(MainController mainController) {
        controller = mainController;
        modeName = "База изображений продукции ОАО \"8 Марта\"";
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        init();
        initEvents();
        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {

        tbMain = frameViewPort.getFrameRegion().getToolBar();
        tbMain.getBtnNewItem().setVisible(false);
        tbMain.getBtnEditItem().setVisible(false);
        tbMain.getBtnDeleteItem().setVisible(false);

        imagePanel = new ModelImagePanel(controller);
        JTabbedPane tabPanel = new JTabbedPane();
        final JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabPanel, imagePanel);
        splitPanel.setResizeWeight(0.2);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);

        JPanel pLastProduction = new JPanel(new BorderLayout());
        lLastProduction = new JList<>();
        UCToolBar tbLastProduction = new UCToolBar();
        tbLastProduction.getBtnNewItem().setVisible(false);
        tbLastProduction.getBtnEditItem().setVisible(false);
        tbLastProduction.getBtnDeleteItem().setVisible(false);
        tbLastProduction.getBtnViewItem().setVisible(false);
        btnUpdateLastProduction = new JButton();
        btnUpdateLastProduction.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnUpdateLastProduction.setToolTipText("Обновить данные");
        tbLastProduction.add(btnUpdateLastProduction);
        pLastProduction.add(tbLastProduction, BorderLayout.NORTH);
        pLastProduction.add(new JScrollPane(lLastProduction), BorderLayout.CENTER);


        JPanel pCatalog = new JPanel(new BorderLayout());

        JPanel pNewImages = new JPanel(new BorderLayout());

        fileList = new UCFileListComponent(new File(""), FileListMode.ImagePreview);
        UCToolBar tbNewImages = new UCToolBar();
        tbNewImages.getBtnNewItem().setVisible(false);
        tbNewImages.getBtnEditItem().setVisible(false);
        tbNewImages.getBtnDeleteItem().setVisible(false);
        tbNewImages.getBtnViewItem().setVisible(false);
        btnUpdate = new JButton();
        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnUpdate.setToolTipText("Обновить данные");
        tbNewImages.add(btnUpdate);
        pNewImages.add(tbNewImages, BorderLayout.NORTH);
        pNewImages.add(fileList, BorderLayout.CENTER);

        tree = new ClassifierTree();
        pCatalog.add(tree, BorderLayout.CENTER);

        pNewImageTest = new UCImageListComponent();


        tabPanel.addTab("Каталог", pCatalog);
        tabPanel.addTab("Новые", pNewImages);
        tabPanel.addTab("Маркировка", pLastProduction);
        //tabPanel.addTab("TEST", pNewImageTest);

        frameViewPort.getFrameRegion().getCenterContentPanel().add(splitPanel);
        //frameViewPort.getFrameRegion().getCenterContentPanel().add(new JPanel());


    }

    private void initEvents() {

        tree.addTreeListener(a -> {
            if (a != null) {
                if (a.getType() == ClassifierNodeType.MODEL) {
                    imagePanel.updateContext(String.valueOf(a.getName()));
                }
            }
        });


        btnUpdate.addActionListener(a -> {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        ImageServiceControl service = ModelImageServiceControlDB.getInstance();
                        for (String s : service.createImageFileList()) {
                            if (!service.sendImageToColorGroup(new File(s), "РАЗНОЦВЕТ")) {
                                service.ignoreImageFile(s);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }

            Task task = new Task("Формирование списка изображений...");
            task.executeTask();
        });

        fileList.addMouseClickListener(new FileListEventAdaptor() {
            @Override
            public void onClick(File file) {
                String modelNumber = ModelImageService.parseModelNumber(file.getName());
                if (modelNumber != null) {
                    imagePanel.updateContext(String.valueOf(modelNumber));
                }
            }

            @Override
            public void onDoubleClick(File file) {
                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        String modelNumber = ModelImageService.parseModelNumber(file.getName());
                        if (modelNumber != null) {
                            if (imagePanel.sendImageToModel(file)) {
                                fileList.deleteFileItem(file.getAbsolutePath());
                            }
                        }
                        return true;
                    }
                }

                Task task = new Task("Обработка изображения...");
                task.executeTask();
            }
        });

        btnUpdateLastProduction.addActionListener(a -> {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        ImageServiceControl service = ModelImageServiceControlDB.getInstance();

                        ImageManagerJDBC db = new ImageManagerJDBC();
                        List<LabelItem> list = db.getLastLabels();
                        if (list != null) {
                            for (LabelItem item : list) {
                                item.setExist(service.haveImage(item.getModel()));
                            }

                            DefaultListModel<LabelItem> listModel = new DefaultListModel<>();
                            for (LabelItem item : list) {
                                listModel.addElement(item);
                            }

                            lLastProduction.setModel(listModel);
                            lLastProduction.setSelectedIndex(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }

            Task task = new Task("Получение списка маркировок...");
            task.executeTask();
        });

        lLastProduction.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                LabelItem item = (LabelItem) value;
                if (item != null) {
                    setText(item.getModel());
                    if (item.isExist()) {
                        setForeground(Color.BLACK);
                    } else {
                        setForeground(Color.GRAY);
                    }
                }
                if (isSelected) {
                    setBackground(getBackground().darker());
                } else {
                    setBackground(getBackground());
                }

                return c;
            }

        });

        lLastProduction.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent value) {
                if (!value.getValueIsAdjusting()) {
                    LabelItem item = lLastProduction.getSelectedValue();
                    if (item != null) {
                        imagePanel.updateContext(String.valueOf(item.getModel()));
                    }
                }
            }
        });

    }

    @Override
    public void updateContent() {

    }

    @Override
    public void addRecord() {

    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {

    }
}
