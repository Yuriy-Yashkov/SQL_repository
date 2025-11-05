package by.march8.ecs.services.images;

import by.gomel.freedev.ucframework.uccore.utils.PictureUtils;
import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.entities.images.DBImage;
import by.march8.entities.images.DBModelImage;
import org.apache.commons.io.FileUtils;
import xsd.image.ImageInformation;
import xsd.image.ImageListItem;
import xsd.image.SizeListItem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractImageService {

    public static final String DEFAULT = "IMAGES";
    public static final String THUMBNAILS = ".thumbs";
    public static final int SIZE_COMMON = 1200;
    public static final int SIZE_THUMBNAILS = 120;
    public static String CATALOG_DIR;
    public static String SOURCE_DIR;
    protected boolean isDebug = false;
    protected boolean isWindows = false;

    public AbstractImageService() {
        isWindows = SystemUtils.isWindows();
    }

    protected static String getLargeSourceFileName(String filename) {
        if (filename != null) {
            filename = filename.substring(1, filename.length());
            return SOURCE_DIR + filename;
        }
        return null;
    }

    protected static String getOriginalNameFromThumbnailsName(String name) {
        if (name != null) {
            return name.replace(THUMBNAILS + "/", "");
        }
        return null;
    }

    public static boolean validFileName(File file) {
        if (file.getName().length() >= 7) {
            String file_ = file.getName().substring(0, 3);
            for (int i = 0; i < file_.length(); i++) {
                if (Character.isLetter(file_.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected void prepareEnvironment() {
        if (isDebug) {
            if (isWindows) {
                CATALOG_DIR = "D:/!!catalog/";
                SOURCE_DIR = "D:/!!source/";
            } else {
                CATALOG_DIR = "//nfs/Programs/MyReports/catalog/";
                SOURCE_DIR = "//nfs/Public/Все модели";
            }
        } else {
            if (isWindows) {
                CATALOG_DIR = "//file-server4/Programs/catalog/";
                SOURCE_DIR = "//file-server3/share/Все модели/";

                //CATALOG_DIR = "D:/catalog/";
                //SOURCE_DIR = "//file-server3/share/Все модели/";

            } else {
                CATALOG_DIR = "//nfs/Programs/catalog/";
                SOURCE_DIR = "//nfs/Public/Все модели/";
            }
        }
    }

    protected ImageItem getImageAsImageItem(String model, ImageListItem item, String color) {
        ImageItem result = new ImageItem();
        result.setModel(model);
        result.setImage(item);
        result.setColor(color);
        result.setId(-1);
        return result;
    }

    protected ImageItem getImageAsImageItem(String model, DBImage item, String color) {
        ImageItem result = new ImageItem();
        ImageListItem item_ = new ImageListItem();
        item_.setImageFile(item.getImageFileName());
        item_.setSourceFile(item.getSourceFileName());
        item_.setImageWidth(item.getWidth());
        item_.setImageHeight(item.getHeight());
        result.setModel(model);
        result.setImage(item_);
        if (color == null) {
            ColorTextItem colorItem = ColorPresetHelper.getColorById(item.getColorId());
            if (colorItem != null) {
                String colorKey = colorItem.getName();
                result.setColor(colorKey);
            }
        } else {
            result.setColor(color);
        }

        result.setId(item.getId());
        return result;
    }

    protected ImageItem getImageAsImageItem(String model, SizeListItem item, String color) {
        ImageItem result = new ImageItem();
        ImageListItem item_ = new ImageListItem();
        item_.setImageFile(item.getImageFile());
        item_.setSourceFile(item.getSourceFile());
        item_.setImageWidth(item.getImageWidth());
        item_.setImageHeight(item.getImageHeight());
        result.setModel(model);
        result.setImage(item_);
        result.setColor(color);
        result.setId(-1);
        return result;
    }

    protected String getThumbnailsImage(String fileName) {
        if (fileName != null) {
            return fileName.replace("IMAGES", "IMAGES/.thumbs");
        } else {
            return null;
        }
    }

    protected String getLargeFileName(String filename) {
        if (filename != null) {
            filename = filename.substring(1, filename.length());
            return CATALOG_DIR + filename;
        }
        return null;
    }

    private Map<String, ImageInformation> prepareImageHashMap(DBModelImage model) {

        if (model != null) {
            Map<String, ImageInformation> result = new HashMap<>();
            for (DBImage image : model.getImageList()) {
                ImageInformation image_ = new ImageInformation();
                image_.setFile(image.getImageFileName());
                image_.setSource(image.getSourceFileName());
                image_.setWidth(image.getWidth());
                image_.setHeight(image.getHeight());
                result.put(image.getSourceFileName(), image_);
            }
            return result;
        }
        return null;
    }

    protected ImageInformation prepareImage(BufferedImage image, File path_, String fileName, int size) {
        String prefix = "";
        boolean dirCreated = true;
        ImageInformation result = null;
        File path = new File(path_ + File.separator + "IMAGES");

        try {
            if (!path.exists()) {
                // Восстанавливаем иерархию каталогов корня
                File dir = new File(path.getAbsoluteFile() + File.separator + THUMBNAILS);
                dirCreated = dir.mkdirs();
            }
            // Если [size] отличается от SIZE_COMMON заводим новый размер для изображения
            if (size != SIZE_COMMON) {
                prefix = size + File.separator;
                // Создаем каталог размера в указанном каталоге
                File dir = new File(path.getAbsoluteFile() + File.separator + prefix);
                dirCreated = dir.mkdirs();
            }
            // Жмем буффер до размеров 1200px
            BufferedImage buffer = PictureUtils.pictureResize(image, size);
            // Сохраняем в каталог
            result = PictureUtils.pictureSave(buffer, new File(path.getAbsoluteFile() + File.separator + prefix + fileName));


            if (result != null) {
                if (result.getFile() != null) {
                    fileName = result.getFile().getName();
                }
            }


            File thumbnails = new File(path.getAbsoluteFile() + File.separator + THUMBNAILS + File.separator + fileName);
            // Жмем буффер для эскиза 120px
            buffer = PictureUtils.pictureResize(image, SIZE_THUMBNAILS);
            // Сохраняем в каталог
            PictureUtils.pictureSave(buffer, thumbnails);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    protected String getShortPath(File file) {
        if (file != null) {
            return "/" + file.getAbsolutePath().replace("\\", "/").replace(CATALOG_DIR, "").replace(SOURCE_DIR, "");
        }
        return null;
    }

    protected String getShortPath(String file) {
        if (file != null) {
            return "/" + file.replace("\\", "/").replace(CATALOG_DIR, "").replace(SOURCE_DIR, "");
        }
        return null;
    }

    protected String getShortFullSizePath(String file) {
        if (file != null) {
            return "/" + file.replace("\\", "/").replace(CATALOG_DIR, "").replace(SOURCE_DIR, "").replace("/.thumbs", "");
        }
        return null;
    }

    /**
     * Метод формирует список необработанных файлов
     */
    protected List<String> createProcessingImageFileList(Set<String> source) {
        // Шаримся по каталогу маркетологов, вытаскиваем ссылки на картинки, фильтруем JPG, обрабатываем
        System.out.print("Сканирование каталога [" + SOURCE_DIR + "]... ");
        List<String> catalogList = new ArrayList<>();
        try {
            Files.walk(Paths.get(SOURCE_DIR))
                    .filter(Files::isRegularFile)
                    .forEach(a -> {
                        String path = a.toString();
                        if (path.toLowerCase().endsWith(".jpg")) {
                            catalogList.add(getShortPath(path));
                        }
                    });
            //FileUtils.writeLines(new File(CATALOG_DIR + "source_list.mirror"), catalogList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ЗАВЕРШЕНО (" + catalogList.size() + ")");

        // Вытаскиваем файлы исходники уже обработанные
        System.out.print("Загрузка обработанных исходников... ");

        if (source == null) {
            source = new HashSet<>();
        }

        System.out.println("ЗАВЕРШЕНО (" + source.size() + ")");

        // Загружаем файл черного списка изображений, которые нужно игнорировать при обработке
        // black_list.mirror

        System.out.print("Загрузка файла черного списка [" + CATALOG_DIR + "black_list.mirror" + "]... ");
        List<String> blackList = null;
        File collectionFile = new File(CATALOG_DIR + "black_list.mirror");
        if (collectionFile.exists()) {
            try {
                blackList = FileUtils.readLines(collectionFile, "utf-8");
                for (String s : blackList) {
                    source.add(s.toLowerCase());
                }
                System.out.println("ЗАВЕРШЕНО (" + blackList.size() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.print("Формирование списка изображений для обработки... ");
        List<String> list = new ArrayList<>();

        for (String s : catalogList) {
            if (!source.contains(s.toLowerCase())) {
                list.add(getLargeSourceFileName(s));
            }
        }

        System.out.println("ЗАВЕРШЕНО (" + list.size() + ")");

        try {
            FileUtils.writeLines(new File(CATALOG_DIR + "images_new_images.mirror"), list);
        } catch (Exception e) {
            System.out.println("ОШИБКА");
            e.printStackTrace();
        }


        return list;
    }

    protected void addFileToBlackList(String file) {
        if (file != null) {
            System.out.print("Добавление файла [" + file + "] в черный список... ");
            List<String> blackList = null;
            File collectionFile = new File(CATALOG_DIR + "black_list.mirror");
            if (collectionFile.exists()) {
                try {
                    blackList = FileUtils.readLines(collectionFile, "utf-8");
                    blackList.add(getShortPath(file));
                    FileUtils.writeLines(new File(CATALOG_DIR + "black_list.mirror"), blackList);
                    System.out.println("ЗАВЕРШЕНО (" + blackList.size() + ")");
                } catch (Exception e) {
                    System.out.println("ОШИБКА");
                    e.printStackTrace();
                }
            }
        }
    }

}
