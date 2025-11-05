package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist;


import by.gomel.freedev.ucframework.uccore.utils.PictureUtils;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.CommonFileRenderer;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.FileListMode;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.ImageFileFilter;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.ImageItem;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.ImageListRenderer;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.ListTransferHandler;
import by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model.TextFileFilter;
import by.march8.ecs.services.images.AbstractImageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Andy 18.12.2018 - 12:12.
 */
public class UCFileListComponent extends JPanel {

    private File currentDir;

    private JList listComponent;
    private DefaultListModel listModel;

    private UCTextFieldPanel lblHeader = new UCTextFieldPanel();
    private JLabel lblFooter = new JLabel("*.*");
    private FileListMode mode;
    private FileFilter currentFilter;

    private Map<String, ImageItem> map = new HashMap<>();
    private File selectedFile;


    public UCFileListComponent(File startDir, FileListMode flMode) {
        super(new BorderLayout());
        this.currentDir = startDir;
        listComponent = new JList();
        if (flMode == null) {
            mode = FileListMode.FileManager;
        }
        mode = flMode;

        add(new JScrollPane(listComponent), BorderLayout.CENTER);
        add(lblHeader, BorderLayout.NORTH);
        add(lblFooter, BorderLayout.SOUTH);

        switch (mode) {
            case FileManager:
                listComponent.setCellRenderer(new CommonFileRenderer());
                currentFilter = new TextFileFilter();
                initFileList(currentDir);
                initFileManagerEvents();
                listComponent.setTransferHandler(new ListTransferHandler());
                break;

            case ImageViewer:
                listComponent.setCellRenderer(new ImageListRenderer(map));
                lblHeader.getButtonSelect().setVisible(false);
                currentFilter = new ImageFileFilter();

                initImageList(currentDir);
                listComponent.setDragEnabled(true);
                listComponent.setTransferHandler(new ListTransferHandler());
                break;

            case ImagePreview:
                listComponent.setCellRenderer(new ImageListRenderer(map));
                lblHeader.getButtonSelect().setVisible(false);
                currentFilter = new ImageFileFilter();

                //initImageList(currentDir);
                //listComponent.setDragEnabled(true);
                //listComponent.setTransferHandler(new ListTransferHandler());
                break;
        }

        listComponent.setVisibleRowCount(9);
    }

    private void initImageList(File dir) {
        File[] fileArray = getDirectoryObjectList(dir, currentFilter);
        if (fileArray == null) {
            return;
        }

        List<File> list = new ArrayList<>();

        // Добавим все дирректории и файлы в отдельные списки для сортировки как в тотале,
        List<File> fileList = new ArrayList<>();
        for (File file : fileArray) {
            if (!file.isDirectory()) {
                fileList.add(file);
            }
        }

        // СОтрировка по имени для файлов
        Collections.sort(fileList, (f1, f2) -> (f1).getName().toLowerCase().compareTo((f2).getName().toLowerCase()));
        // В результирующий список добавим файлы
        list.addAll(fileList.stream().collect(Collectors.toList()));

        // Формируем мапу для картинок
        for (File file : list) {
            map.put(file.getAbsolutePath(),
                    new ImageItem(new ImageIcon(file.getAbsolutePath()), file.getName(), file.getAbsolutePath()));
        }

        listModel = new DefaultListModel();
        for (File file : list) {
            listModel.addElement(file.getAbsolutePath());
        }

        listComponent.setModel(listModel);
        listComponent.setSelectedIndex(0);
        currentDir = dir;

        try {
            lblHeader.setText(currentDir.getCanonicalPath());
            lblFooter.setText("В каталоге " + listModel.getSize() + " файлов изображений");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> initPreviewImageList(File dir, List<String> list) {
        List<String> finalList = new ArrayList<>();
        List<String> badFiles = new ArrayList<>();
        for (String file : list) {
            try {
                File file_ = new File(file);
                if (AbstractImageService.validFileName(file_)) {
                    BufferedImage currentBuffer = PictureUtils.pictureOpen(file_);
                    if (currentBuffer != null) {
                        BufferedImage resizeBuffer = PictureUtils.pictureResize(currentBuffer, 120);
                        if (resizeBuffer != null) {
                            map.put(file,
                                    new ImageItem(new ImageIcon(resizeBuffer), file_.getName(), file_.getAbsolutePath()));
                            finalList.add(file);
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

        listModel = new DefaultListModel();
        for (String file : finalList) {
            listModel.addElement(file);
        }

        listComponent.setModel(listModel);
        listComponent.setSelectedIndex(0);
        currentDir = dir;

        try {
            lblHeader.setText(currentDir.getCanonicalPath());
            lblFooter.setText("К обработке принято " + listModel.getSize() + " файлов изображений");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return badFiles;
    }

    private void initFileList(File dir) {
        File[] fileArray = getDirectoryObjectList(dir, currentFilter);
        if (fileArray == null) {
            return;
        }

        List<File> list = new ArrayList<>();
        list.add(new File("..."));

        // Добавим все дирректории и файлы в отдельные списки для сортировки как в тотале,
        List<File> dirList = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        for (File file : fileArray) {
            if (file.isDirectory()) {
                dirList.add(file);
            } else {
                fileList.add(file);
            }
        }

        // Сортировка по имени для каталогов
        Collections.sort(dirList, (f1, f2) -> (f1).getName().toLowerCase().compareTo((f2).getName().toLowerCase()));
        // СОтрировка по имени для файлов
        Collections.sort(fileList, (f1, f2) -> (f1).getName().toLowerCase().compareTo((f2).getName().toLowerCase()));
        // В результирующий список добавим каталоги
        list.addAll(dirList.stream().collect(Collectors.toList()));
        // В результирующий список добавим файлы
        list.addAll(fileList.stream().collect(Collectors.toList()));
        listModel = new DefaultListModel();
        for (File file : list) {
            listModel.addElement(file);
        }

        listComponent.setModel(listModel);
        listComponent.setSelectedIndex(0);
        currentDir = dir;

        try {
            lblHeader.setText(currentDir.getCanonicalPath());
            lblFooter.setText("В каталоге: " + dirList.size() + " папок, " + fileList.size() + " файлов");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File[] getDirectoryObjectList(File dir, FileFilter filter) {
        if (dir.exists()) {
            return dir.listFiles(filter);
        } else {
            return null;
        }
    }

    private void initFileManagerEvents() {
        listComponent.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                File selected = (File) listComponent.getSelectedValue();
                if (evt.getClickCount() == 1) {
                    selectedFile = selected;
                }
                if (evt.getClickCount() == 2) {
                    if (selected.isDirectory()) {
                        if (selected.getName().equals("...")) {
                            try {
                                initFileList(new File(currentDir.getCanonicalFile() + File.separator + ".."));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            initFileList(selected);
                        }
                    }
                }
            }
        });

        lblHeader.getEditor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    File f = new File(lblHeader.getText());
                    if (f.exists()) {
                        if (f.isDirectory()) {
                            initFileList(f);
                        }
                    }
                }
            }
        });
    }

    public File getCurrentDirectory() {
        return currentDir;
    }

    public void setCurrentDirectory(File currentDir) {
        if (currentDir.exists()) {
            switch (mode) {
                case ImageViewer:
                    initImageList(currentDir);
                    break;
                default:
                    initFileList(currentDir);
                    break;
            }
        }
    }

    public List<String> createPreviewImages(File tempPath, List<String> list) {
        if (tempPath.exists()) {
            switch (mode) {
                case ImagePreview:
                    return initPreviewImageList(tempPath, list);
                default:
                    break;
            }
        }

        return null;
    }

    public File getSelectedFile() {
        switch (mode) {
            case ImageViewer:
                selectedFile = new File((String) listComponent.getSelectedValue());
                break;
            default:
                selectedFile = (File) listComponent.getSelectedValue();
                break;
        }
        return selectedFile;
    }

    public void addMouseClickListener(UCFileListListener listener) {
        listComponent.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (listener != null) {
                    String selected_ = (String) listComponent.getSelectedValue();
                    if (selected_ != null) {

                        lblHeader.setText(selected_);
                        File selected = new File(selected_);

                        if (evt.getClickCount() == 1) {
                            selectedFile = selected;
                            listener.onClick(selected);
                        }

                        if (evt.getClickCount() == 2) {
                            listener.onDoubleClick(selected);
                        }
                    }
                }
            }
        });
    }

    public void deleteFileItem(String file) {
        DefaultListModel model = (DefaultListModel) listComponent.getModel();
        int selectedIndex = listComponent.getSelectedIndex();
        if (selectedIndex != -1) {
            lblHeader.setText("");
            model.remove(selectedIndex);
        }
    }
}
