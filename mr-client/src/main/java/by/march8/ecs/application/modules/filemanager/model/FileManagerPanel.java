package by.march8.ecs.application.modules.filemanager.model;

import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.UCFileListComponent;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.FileListMode;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.ListTransferHandler;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.services.images.ImageHandlerService;
import by.march8.ecs.services.images.ImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

/**
 * @author Andy 18.12.2018 - 12:09.
 */
public class FileManagerPanel extends JPanel {

    private static final String CATALOG_DIR = "//file-server/catalog/catalog/"; //"d:\\catalog";
    private static final String MANAGER_DIR = "d:\\catalog\\!!!MoveDir";
    private static final String REVIEW_DIR = "d:\\catalog\\!!!ReviewDir";
    private static String WORK_CATALOG_PATH = "";
    public DefaultListModel<String> listFileModel;
    private JPanel footerPanel;
    private JButton btnCreate;
    private JButton btnTest;
    private UCFileListComponent leftPanel;
    private UCFileListComponent centerPanel;

    public FileManagerPanel() {
        super(new BorderLayout());

        File startDir = new File("C:\\");
        //File startDir = new File(System.getProperty("user.home"));

        leftPanel = new UCFileListComponent(new File(REVIEW_DIR), FileListMode.ImageViewer);
        centerPanel = new UCFileListComponent(new File("\\\\file-server\\public$\\Все модели\\"), FileListMode.FileManager);

        leftPanel.setPreferredSize(new Dimension(250, 0));
        JPanel rightPanel = new JPanel();

        JLabel label = new JLabel("Какой то текст");
        label.setBounds(100, 100, 300, 200);
        label.setTransferHandler(new ListTransferHandler());
        rightPanel.add(label);
        //UCFileListComponent centerPanel = new UCFileListComponent(new File(System.getProperty("user.home")),FileListMode.FileManager);

        final JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rightPanel, centerPanel);
        splitPanel.setResizeWeight(0.5);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);

        add(leftPanel, BorderLayout.WEST);
        add(splitPanel, BorderLayout.CENTER);
        setBorder(new EmptyBorder(3, 3, 3, 3));

        footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        //footerPanel.setPreferredSize(new Dimension(0,40));
        btnCreate = new JButton("Обработка");
        btnCreate.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        footerPanel.add(btnCreate);


        btnTest = new JButton("Тест");
        btnTest.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);

        footerPanel.add(btnTest);

        add(footerPanel, BorderLayout.SOUTH);

        initEvents();
    }

    public void initEvents() {
        btnCreate.addActionListener(a -> {
            /*
            File file = leftPanel.getSelectedFile();
            if (file.exists()) {
                processing_(file, new File(MANAGER_DIR));
            }

            */

            //batchProcessing();
            //batchCatalogProcessing();
            ImageHandlerService service = new ImageHandlerService(false);
            //service.batchImageProcessing();
            service.prepareUsingImageFiles();
        });

        btnTest.addActionListener(a -> {
            String model = "7210";
            ImageService service = ModelImageServiceDB.getInstance();
            System.out.println(service.getDefaultImageFileByModelNumber(model, ModelImageSize.SMALL));
            System.out.println(service.getDefaultImageFileByModelNumber(model, ModelImageSize.BIG));

            System.out.println("**********************************************************************");
            java.util.List<String> list = service.getImageListByModelNumber(model, ModelImageSize.BIG);
            for (String s : list) {
                System.out.println(s);
            }

            System.out.println("**********************************************************************");
            list = service.getImageListByModelNumber(model, ModelImageSize.SMALL);
            for (String s : list) {
                System.out.println(s);
            }
        });
    }


}
