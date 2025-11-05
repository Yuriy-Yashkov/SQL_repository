package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist;

import by.gomel.freedev.ucframework.uccore.utils.PictureUtils;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImagePanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.services.images.AbstractImageService;
import by.march8.ecs.services.images.ImageServiceControl;
import by.march8.ecs.services.images.ModelImageServiceControlDB;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UCImageListComponent extends JPanel {

    private JButton btnUpdate;
    private JScrollPane spMain;
    private JPanel pContent;
    private Map<String, ImageItem> map = new HashMap<>();


    public UCImageListComponent() {
        super(new BorderLayout());
        init();
        initEvents();
    }


    private void init() {
        UCToolBar tbMain = new UCToolBar();
        tbMain.getBtnNewItem().setVisible(false);
        tbMain.getBtnEditItem().setVisible(false);
        tbMain.getBtnDeleteItem().setVisible(false);
        tbMain.getBtnViewItem().setVisible(false);
        btnUpdate = new JButton();
        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnUpdate.setToolTipText("Обновить данные");
        tbMain.add(btnUpdate);
        add(tbMain, BorderLayout.NORTH);
        pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));
        spMain = new JScrollPane(pContent);
        spMain.getVerticalScrollBar().setUnitIncrement(16);
        add(spMain, BorderLayout.CENTER);
    }

    private void initEvents() {
        btnUpdate.addActionListener(a -> {
            try {
                ImageServiceControl service = ModelImageServiceControlDB.getInstance();
                List<String> badList = initPreviewImageList(service.createImageFileList());
                if (badList != null) {
                    for (String s : badList) {
                        service.ignoreImageFile(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private List<String> initPreviewImageList(List<String> list) {
        List<String> badFiles = new ArrayList<>();
        for (String file : list) {
            try {
                File file_ = new File(file);
                if (AbstractImageService.validFileName(file_)) {
                    BufferedImage currentBuffer = PictureUtils.pictureOpen(file_);
                    if (currentBuffer != null) {
                        BufferedImage resizeBuffer = PictureUtils.pictureResize(currentBuffer, 120);
                        if (resizeBuffer != null) {
                            ImageItem image = new ImageItem(new ImageIcon(resizeBuffer), file_);


                            map.put(file, image);
                            pContent.add(new UCImagePanel(image, e -> {
                                return false;
                            }));
                        }
                    } else {
                        // Плохие файлы игнорим
                        badFiles.add(file);
                    }
                } else {
                    badFiles.add(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return badFiles;
    }
}
