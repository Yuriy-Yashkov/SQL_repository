package dept.tools.imgmanager;

import by.gomel.freedev.ucframework.common.DirectoryScanner;
import by.march8.ecs.framework.common.Settings;
import common.ProgressBar;
import common.UtilFunctions;
import org.apache.commons.io.FileUtils;
import workDB.DB;
import workDB.PDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

@SuppressWarnings("all")
public class SearchPanel extends JPanel {

    public JPanel panelConsole;
    public JButton btnSearch;
    public JButton btnClose;
    public JButton btnLoad;
    public JButton btnCopy;
    public JPanel panelSearch;
    public JTextField tfSearchString;
    public JTextField tfDestDir;
    public JPanel panelContent;
    public JPanel panelControl;
    public JPanel panelButton;
    public JList<String> listFile;
    public JList<String> listConsole;
    public JScrollPane spListFile;
    public JScrollPane spListConsole;
    public DefaultListModel<String> listFileModel;
    public DefaultListModel<String> listConsoleModel;
    public JComboBox<String> cbExportImageSize;
    public JComboBox<String> cbThumbImageSize;
    public JButton btnExportPreview;
    public JButton btnThumbPreview;
    private ProgressBar pb;
    private JFrame parent;
    private ArrayList<ItemModel> array = new ArrayList<ItemModel>();
    private PDB pdb = new PDB();

    private ImageTools imgTools = new ImageTools();

    public SearchPanel(JFrame parent) {
        super(null);
        this.parent = parent;
        initPanel();
    }

    public void imageProcessing() {
        pb = new ProgressBar(parent, false, "Обработка фотографий");

        class SWorker extends SwingWorker<Object, Object> {
            @Override
            protected Object doInBackground() throws Exception {
                String path = "";
                String name = "";
                ModelParser parser = new ModelParser();
                ImageTools imageTools = new ImageTools();
                listConsoleModel.clear();

                int count = listFileModel.getSize() - 1;
                ItemModel debugItem = null;
                for (int i = 0; i < listFileModel.getSize(); i++) {
                    try {
                        //count = listFileModel.getSize()-1;
                        path = listFileModel.getElementAt(i);
                        // Если выбрана не директория а файл имеющий расширение
                        // jpg
                        if (imageTools.isJPGExtension(path)) {
                            name = new File(path).getAbsoluteFile().getName();
                            // чистим массив
                            array.clear();
                            array = parser.getModelList(name);

                            // массив содержит пропарсеную модель
                            for (int j = 0; j < array.size(); j++) {
                                DB db = new DB();
                                ItemModel item = array.get(j);
                                debugItem = item;
                                String destPath = tfDestDir.getText();
                                int sizeExportImage = Integer
                                        .valueOf(cbExportImageSize
                                                .getSelectedItem().toString());
                                int sizeThumbImage = Integer
                                        .valueOf(cbThumbImageSize
                                                .getSelectedItem().toString());
                                // Проверяем, есть ли запись в PDB о этом номере
                                // модели если вернула -1 такая запись уже есть
                                // в базе
                                int result = pdb.isImageDetailModelExist(item);

                                if (result == 1) {
                                    // Выполняем, если запись в базе найдена
                                    pdb.addImageDetail(item, path);

                                    if (!new File(destPath + "/"
                                            + item.getFullName() + ".jpg")
                                            .exists()) {
                                        imageTools.imageOpenResizeSaveAs(path,
                                                destPath, item.getFullName()
                                                        + ".jpg",
                                                sizeExportImage);
                                    }
                                    listConsoleModel.addElement("["
                                            + item.getFullName()
                                            + "]: Добавлена в изображения");
                                } else if (result == 2) {
                                    // если Записи в базе не найдено
                                    BufferedImage image = imageTools
                                            .imageOpen(path);
                                    if (!new File(destPath + "/"
                                            + item.getFullName() + ".jpg")
                                            .exists()) {
                                        imageTools.imageResizeSaveAs(image,
                                                destPath, item.getFullName()
                                                        + ".jpg",
                                                sizeExportImage);
                                    }
                                    // фото быстрого просмотра (thumb) будем
                                    // формировать из экспортного фото, так
                                    // быстрее
                                    String imageSrc = destPath + "/"
                                            + item.getFullName() + ".jpg";
                                    // Прежде чем сохранить в базе, необходимо
                                    // сохранить в файл
                                    imageTools.imageOpenResizeSaveAs(imageSrc,
                                            destPath, "thumb.jpg",
                                            sizeThumbImage);
                                    // Пишем в базу картинку
                                    //int sar = db.getSarByModelNumber(item.getName());
                                    //pdb.addImageModel(item, sar, new File(destPath
                                    //        + "/thumb.jpg"));
                                    listConsoleModel.addElement("["
                                            + item.getFullName()
                                            + "]: Добавлена в модели");
                                    // Добавляем запись в базу
                                    //pdb.addImageDetail(item, path);
                                    listConsoleModel.addElement("["
                                            + item.getFullName()
                                            + "]: Добавлена в изображения");
                                } else if (result == 0) {
                                    listConsoleModel.addElement("["
                                            + item.getFullName()
                                            + "]: Запись существует");
                                    //pdb.updateSARIzdImage(item.getName(),db.getSarByModelNumber(item.getName()));
                                }

                            }
                        }
                        System.out.println("Обработано " + i + " элементов из " + count);

                    } catch (Exception ex) {
                        listConsoleModel.addElement("["
                                + debugItem.getFullName()
                                + "]: Добавлена в изображения");
                        /*JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "Ошибка метода imageProcessing()(doInBackground) ",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                        */
                    } finally {

                    }
                }

                return 0;
            }

            @Override
            protected void done() {
                try {
                    pb.dispose();
                    //db.disConn();
                    //pdb.disConn();
                } catch (Exception ex) {
                    System.err.println("Ошибка метода imageProcessing()(done) "
                            + ex);
                }

            }
        }

        SWorker sw = new SWorker();
        sw.execute();
        pb.setVisible(true);
    }

    public void imageProcessingCopy() {
        String outputDir = "\\\\file-server3\\datamatrix\\resize";
        //String outputDir = "d:\\Каталог\\resize_120";
        //tfSearchString.setText("\\\\file-server3\\datamatrix\\resize");

        String path = "";
        ImageTools imageTools = new ImageTools();
        listConsoleModel.clear();

        int count = listFileModel.getSize() - 1;
        File dir = null;
        File directory = null;

        String fileName = "";
        for (int i = 0; i < listFileModel.getSize(); i++) {
            try {
                path = listFileModel.getElementAt(i);
                directory = new File(path);
                fileName = path.replace(tfSearchString.getText().trim(), outputDir);
                //fileName = path.replace(tfSearchString.getText().trim(), "d:\\Каталог");
                //fileName = path.replace(tfSearchString.getText().trim(), "\\\\file-server\\catalog\\new");
                if (directory.isDirectory()) {
                    File newDir = new File(fileName);
                    if (!newDir.exists()) {
                        Thread.sleep(100);
                        System.out.println(fileName + " : " + newDir.mkdirs());
                    }

                    newDir = new File(fileName.replace("resize", "resize_120"));
                    if (!newDir.exists()) {
                        Thread.sleep(100);
                        System.out.println(newDir.getName() + " : " + newDir.mkdirs());
                    }

                } else {
                    if (imageTools.openResizeSaveAs(path, fileName, 1200)) {
                        System.out.println("Файл добавлен  : " + fileName);
                        listConsoleModel.addElement("[" + directory.getName()
                                + "]: Добавлен в каталог [" + i + "] элементов из [" + count + "]");
                    }
                }
                //System.out.println("Обработано " + i + " элементов из " + count+" : "+path);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Формируем файлы списков изображений для разных ОС
        DirectoryScanner scanner;
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> listTemp = new ArrayList<>();

        try {

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            list.add("Дата сканирования: " + timestamp.toString());
            scanner = new DirectoryScanner(outputDir, list);
            scanner.start();
            //scanner.saveToFile(scanDir + "\\"+treeDirFile);
            FileUtils.writeLines(new File(outputDir + "\\" + Settings.CATALOG_TREE_FILE), list);

            for (String s : list) {
                listTemp.add(s.replace(outputDir + "\\", Settings.WINDOWS_CATALOG_PATH));
            }
            FileUtils.writeLines(new File(outputDir + "\\" + Settings.CATALOG_TREE_FILE + ".windows"), listTemp);

            listTemp.clear();
            for (String s : list) {
                listTemp.add(s.replace(outputDir + "\\", Settings.UNIX_CATALOG_PATH + "/").replace("\\", "/"));
            }
            FileUtils.writeLines(new File(outputDir + "\\" + Settings.CATALOG_TREE_FILE + ".linux"), listTemp);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initPanel() {

        /**
         * (15:46:50) Шестаков Иван Георгиевич: //file-server/Programs/MyReports
         * (15:47:05) Шестаков Иван Георгиевич: /nfs/Programs/MyReports
         *
         * */

        // \\file-server.local\Public$\catalog\
        // \\file-server.local\Public$\Все модели\
        setPreferredSize(new Dimension(800, 500));
        BorderLayout layout = new BorderLayout();
        BorderLayout layout1 = new BorderLayout();
        BorderLayout layoutConsole = new BorderLayout();
        this.setLayout(layout);

        panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSearch.setPreferredSize(new Dimension(0, 30));
        panelContent = new JPanel(layout1);

        panelControl = new JPanel(null);
        panelControl.setPreferredSize(new Dimension(0, 74));
        panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setPreferredSize(new Dimension(0, 40));

        String[] sizeImage = {"150", "200", "250", "300", "350", "1200"};
        cbExportImageSize = new JComboBox<String>(sizeImage);
        cbExportImageSize.setBounds(280, 10, 50, 24);
        cbExportImageSize.setSelectedIndex(2);

        cbThumbImageSize = new JComboBox<String>(sizeImage);
        cbThumbImageSize.setBounds(280, 40, 50, 24);
        cbThumbImageSize.setSelectedIndex(0);
        JLabel lbl = new JLabel("Размер фотографии для выгрузки :");
        lbl.setBounds(10, 10, 300, 24);
        JLabel lbl1 = new JLabel("Размер фотографии для эскиза :");
        lbl1.setBounds(10, 40, 300, 24);

        btnExportPreview = new JButton("Предпросмотр");
        btnExportPreview.setBounds(340, 10, 140, 24);

        btnThumbPreview = new JButton("Предпросмотр");
        btnThumbPreview.setBounds(340, 40, 140, 24);

        panelControl.add(cbExportImageSize);
        panelControl.add(cbThumbImageSize);
        panelControl.add(lbl);
        panelControl.add(lbl1);
        panelControl.add(btnExportPreview);
        panelControl.add(btnThumbPreview);

        listFile = new JList<String>();
        spListFile = new JScrollPane(listFile);

        listConsole = new JList<String>();
        spListConsole = new JScrollPane(listConsole);

        listFileModel = new DefaultListModel<String>();
        listFile.setModel(listFileModel);

        listConsoleModel = new DefaultListModel<String>();
        listConsole.setModel(listConsoleModel);

        panelContent.add(panelControl, BorderLayout.NORTH);
        panelContent.add(spListFile, BorderLayout.CENTER);

        panelConsole = new JPanel(layoutConsole);
        panelConsole.setPreferredSize(new Dimension(0, 200));

        if (UtilFunctions.isWindows()) {
            tfSearchString = new JTextField("\\\\file-server.local\\Public$\\Все модели");
            //tfSearchString = new JTextField("\\\\file-server3\\datamatrix\\resize");

            tfDestDir = new JTextField("\\\\file-server\\catalog\\resize");
            //tfDestDir = new JTextField("//file-server.local/Public/catalog");
            //                          \\file-server\Programs\MyReports\catalog\
        } else {
            tfSearchString = new JTextField("/nfs/Public/Все модели");
            tfDestDir = new JTextField("/nfs/Programs/MyReports/catalog");
        }


        //tfDestDir.setEditable(false);
        tfDestDir.setForeground(Color.BLACK);
        tfDestDir.setBackground(Color.white);
        tfSearchString.setPreferredSize(new Dimension(300, 24));
        tfDestDir.setPreferredSize(new Dimension(290, 24));

        panelSearch.add(new JLabel("Источник :"));
        panelSearch.add(tfSearchString);
        panelSearch.add(new JLabel("Место назначения :"));
        panelSearch.add(tfDestDir);


        btnSearch = new JButton("Обновить источник");
        btnSearch.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);

        btnLoad = new JButton("Синхронизировать");
        btnLoad.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        btnLoad.setEnabled(false);

        btnCopy = new JButton("Копировать");
        btnCopy.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        btnCopy.setEnabled(false);

        panelConsole.add(spListConsole, BorderLayout.CENTER);
        panelConsole.add(panelButton, BorderLayout.NORTH);
        panelButton.add(btnCopy);
        panelButton.add(btnSearch);
        //panelButton.add(btnLoad);

        this.add(panelSearch, BorderLayout.NORTH);
        this.add(panelContent, BorderLayout.CENTER);
        this.add(panelConsole, BorderLayout.SOUTH);

        btnLoad.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                imageProcessing();
            }
        });

        btnCopy.addActionListener(a -> {
            imageProcessingCopy();
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listFileModel.clear();
                String str = tfSearchString.getText();
                FileDirScanner fileDirScaner;
                try {
                    fileDirScaner = new FileDirScanner(str, listFileModel);
                } catch (Exception ex) {

                    ex.printStackTrace();
                    return;
                }
                Thread t = new Thread(fileDirScaner);
                t.start();
                btnLoad.setEnabled(true);
                btnCopy.setEnabled(true);
            }
        });

        btnExportPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePreview(Integer.valueOf(cbExportImageSize
                        .getSelectedItem().toString()));
            }
        });

        btnThumbPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePreview(Integer.valueOf(cbThumbImageSize.getSelectedItem()
                        .toString()));
            }
        });
    }

    private void imagePreview(int imageSize) {
        int index = listFile.getSelectedIndex();
        if (index < 0) {
            return;
        }
        String src = listFileModel.getElementAt(index);
        new ImagePreviewForm(imgTools.imageOpenResize(src, imageSize));
    }

}
