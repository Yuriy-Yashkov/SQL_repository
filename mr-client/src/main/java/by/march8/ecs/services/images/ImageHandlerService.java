package by.march8.ecs.services.images;

import by.gomel.freedev.ucframework.uccore.utils.PictureUtils;
import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import org.apache.commons.io.FileUtils;
import xsd.image.ColorListItem;
import xsd.image.ImageInformation;
import xsd.image.ImageListItem;
import xsd.image.ModelImageItem;
import xsd.image.ModelImageList;
import xsd.image.SizeListItem;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 21.12.2018 - 8:43.
 */
@SuppressWarnings("all")
public class ImageHandlerService extends AbstractImageService {

    private static final String DEFAULT = "IMAGES";
    private static final String THUMBNAILS = ".thumbs";
    private static final int SIZE_COMMON = 1200;
    private static final int SIZE_THUMBNAILS = 120;
    private static String CATALOG_DIR;
    private static String SOURCE_DIR;
    private BufferedImage currentBuffer;
    private boolean isWindows;
    private boolean debug;


    public ImageHandlerService(boolean debug) {
        isWindows = SystemUtils.isWindows();
        this.debug = debug;
        prepareEnvironment();
    }

    public ImageHandlerService() {
        this(false);
    }


    public void modelProcessing(String modelNumber) {
        if (modelNumber == null) {
            return;
        }
/*
        List<String> list = new ArrayList<>();
        try {
            Files.walk(Paths.get(SOURCE_DIR))
                    .filter(Files::isRegularFile)
                    .forEach(a -> {
                        String path = a.toString();
                        if (path.toLowerCase().endsWith(".jpg")) {
                            list.add(path);
                        }
                    });
            FileUtils.writeLines(new File(CATALOG_DIR + "file_list_demo.mirror"), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        List<String> sourceList = null;
        File collectionFile = new File(CATALOG_DIR + "file_list_demo.mirror");
        if (collectionFile.exists()) {
            try {
                sourceList = FileUtils.readLines(collectionFile, "utf-8");
                for (String s : sourceList) {
                    File file = new File(s);
                    if (file.getName().contains(modelNumber)) {
                        if (file.length() < 20000000) {
                            processing_(file, file);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void prepareUsingImageFilesColorGroups() {
        // Шаримся по каталогу маркетологу, вытаскиваем ссылки на картинки, фильтруем JPG, обрабатываем
        List<String> list = new ArrayList<>();
        try {
            System.err.print("Сканирование рабочего каталога...");
            Files.walk(Paths.get(CATALOG_DIR))
                    .filter(Files::isRegularFile)
                    .forEach(a -> {
                        String path = a.toString();
                        if (path.toLowerCase().endsWith(".xml")) {
                            if (!path.toLowerCase().endsWith("models.xml")) {
                                list.add(path);
                            }
                        }
                    });
            FileUtils.writeLines(new File(CATALOG_DIR + "model_list.mirror"), list);
            System.err.println("ЗАВЕРШЕНО");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> sourceList = null;
        File collectionFile = new File(CATALOG_DIR + "model_list.mirror");
        if (collectionFile.exists()) {
            try {
                System.err.print("Загрузка конфигураций модели...");
                sourceList = FileUtils.readLines(collectionFile, "utf-8");
                ModelImageList modelImageList = new ModelImageList();
                for (String s : sourceList) {
                    File file = new File(s);

                    ModelImageItem item = openImageConfigFile(file);
                    if (item != null) {
                        modelImageList.getModelImageItems().add(item);
                    }
                }
                System.err.println("ЗАВЕРШЕНО");
                System.err.print("Сохранение файла конфигруации...");
                saveModelImageListConfig(modelImageList);
                System.err.println("ЗАВЕРШЕНО");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Метод сканирует каталог назначения, парсит информационные xml файлы
     * и отбирает изображения исходники для последующей обработки
     */
    public void prepareUsingImageFiles() {
        // Шаримся по каталогу маркетологу, вытаскиваем ссылки на картинки, фильтруем JPG, обрабатываем
        List<String> list = new ArrayList<>();
        try {
            System.err.print("Сканирование рабочего каталога...");
            Files.walk(Paths.get(CATALOG_DIR))
                    .filter(Files::isRegularFile)
                    .forEach(a -> {
                        String path = a.toString();
                        if (path.toLowerCase().endsWith(".xml")) {
                            if (!path.toLowerCase().endsWith("models.xml")) {
                                list.add(path);
                            }
                        }
                    });
            FileUtils.writeLines(new File(CATALOG_DIR + "model_list.mirror"), list);
            System.err.println("ЗАВЕРШЕНО");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> sourceList = null;
        File collectionFile = new File(CATALOG_DIR + "model_list.mirror");
        if (collectionFile.exists()) {
            try {
                System.err.print("Загрузка конфигураций модели...");
                sourceList = FileUtils.readLines(collectionFile, "utf-8");
                ModelImageList modelImageList = new ModelImageList();
                for (String s : sourceList) {
                    File file = new File(s);

                    ModelImageItem item = openImageConfigFile(file);
                    if (item != null) {
                        modelImageList.getModelImageItems().add(item);
                    }
                }
                System.err.println("ЗАВЕРШЕНО");
                System.err.print("Сохранение файла конфигруации...");
                saveModelImageListConfig(modelImageList);
                System.err.println("ЗАВЕРШЕНО");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void loadImageConfiguration() {
        System.err.print("Загрузка полной конфигураций...");
        ModelImageList modelList = loadModelImageListConfig(new File(CATALOG_DIR + "models.xml"));
        System.err.println("ЗАВЕРШЕНО");
        if (modelList != null) {
            System.err.println("Количество моделей в базе: " + modelList.getModelImageItems().size());
        }
    }

    private ModelImageItem openImageConfigFile(File file) {
        if (file != null) {
            if (file.exists()) {
                return loadModelImageConfig(file);
            }
        }
        return null;
    }

    public ImageInformation prepareImage(BufferedImage image, File path, String fileName, int size) {
        String prefix = "";
        boolean dirCreated = true;
        ImageInformation result = null;
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
            if (!thumbnails.exists()) {
                // Жмем буффер для эскиза 120px
                buffer = PictureUtils.pictureResize(image, SIZE_THUMBNAILS);
                // Сохраняем в каталог
                PictureUtils.pictureSave(buffer, thumbnails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Метод создает список изображений из исходного каталога,
     * и формируем в каталоге назначения рабочее изображение и изображение эскиза
     */
    public void batchImageProcessing() {
        // Шаримся по каталогу маркетологу, вытаскиваем ссылки на картинки, фильтруем JPG, обрабатываем
        List<String> list = new ArrayList<>();
        try {
            Files.walk(Paths.get(SOURCE_DIR))
                    .filter(Files::isRegularFile)
                    .forEach(a -> {
                        String path = a.toString();
                        if (path.toLowerCase().endsWith(".jpg")) {
                            list.add(path);
                        }
                    });
            FileUtils.writeLines(new File(CATALOG_DIR + "file_list.mirror"), list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> sourceList = null;
        File collectionFile = new File(CATALOG_DIR + "file_list.mirror");
        if (collectionFile.exists()) {
            try {
                sourceList = FileUtils.readLines(collectionFile, "utf-8");
                for (String s : sourceList) {
                    File file = new File(s);
                    if (file.length() < 20000000) {
                        processing_(file, file);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void processing_(File file, File originalFile) {
        // Получаем номер модели из имени файла изображения
        String modelNumber = ModelImageService.parseModelNumber(file.getName());
        if (modelNumber != null) {
            if (modelNumber.length() < 3) {
                return;
            }
        } else {
            return;
        }

        // Корневой каталог
        String rootDir = CATALOG_DIR + File.separator + modelNumber + File.separator;
        // Есть ли папка модели
        File destinationDir = new File(rootDir);
        boolean dirComplete = true;

        ModelImageItem model = null;

        if (!destinationDir.exists()) {
            try {
                //Files.move(Paths.get(file.getAbsolutePath()), Paths.get(originalFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                dirComplete = destinationDir.mkdirs();
                if (dirComplete) {
                    model = new ModelImageItem();
                    model.setModelNumber(modelNumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File modelFile = new File(rootDir + modelNumber + ".xml");
            if (modelFile.exists()) {
                model = loadModelImageConfig(modelFile);
                if (model == null) {
                    model = new ModelImageItem();
                    model.setModelNumber(modelNumber);
                }
            } else {
                model = new ModelImageItem();
                model.setModelNumber(modelNumber);
            }
        }

        currentBuffer = PictureUtils.pictureOpen(file);
        if (currentBuffer != null) {
            if (model != null) {
                System.out.println(model.getModelNumber());
                Map<String, ImageInformation> imageMap = prepareImageHashMap(model);
                // Получаем ссылки на все исходники для данной модели
                if (model.getImageFileDefault() == null) {
                    addPictureAsDefaultImage(imageMap, model, currentBuffer, file);
                }
                addPictureToModelColor(imageMap, model, currentBuffer, file, "РАЗНОЦВЕТ");
            }
        }
        saveModelImageConfig(model);
    }

    private void addPictureAsDefaultImage(Map<String, ImageInformation> imageMap, ModelImageItem imageItem, BufferedImage buffer, File file) {
        // Предполагаем, что дирректории уже простроены и XML загружен

        File pathProcessing = new File(CATALOG_DIR + File.separator + imageItem.getModelNumber() + File.separator + DEFAULT);
        // [МОДЕЛЬ][DEFAULT][THUMBNAIL] Из буфера Создаем эскиз ее [SIZE_THUMBNAIL]px
        // [МОДЕЛЬ][DEFAULT] Из буфера Создаем рабочую картинку [SIZE_COMMON] px
        ImageInformation info = imageMap.get(getShortPath(file));
        if (info == null) {
            info = prepareImage(buffer, pathProcessing, file.getName(), SIZE_COMMON);
        }
        if (info != null) {
            File file_ = info.getFile();
            imageItem.setSourceFileDefault(getShortPath(file));
            imageItem.setImageFileDefault(getShortPath(file_));
            imageItem.setImageWidth(info.getWidth());
            imageItem.setImageHeight(info.getHeight());

            info.setSource(getShortPath(file));
            info.setFile(file_.getAbsolutePath());
            imageMap.put(getShortPath(file), info);
        }
    }

    private ImageListItem addPictureToModelColor(Map<String, ImageInformation> imageMap, ModelImageItem imageItem, BufferedImage buffer, File file, String color) {
        // Предполагаем, что дирректории уже простроены и XML загружен
        File pathProcessing = new File(CATALOG_DIR + File.separator + imageItem.getModelNumber() + File.separator + DEFAULT);
        // [МОДЕЛЬ][ЦВЕТ][THUMBNAIL] Из буфера Создаем эскиз ее [SIZE_THUMBNAIL]px
        // [МОДЕЛЬ][ЦВЕТ] Из буфера Создаем рабочую картинку [SIZE_COMMON]px

        ImageInformation info = imageMap.get(getShortPath(file));
        if (info == null) {
            info = prepareImage(buffer, pathProcessing, file.getName(), SIZE_COMMON);
        }
        ColorListItem colorList_ = null;
        ImageListItem result = null;
        if (info != null) {
            File file_ = info.getFile();
            // Шерстим имеющиеся цвета у модели и вытаскиваем список картинок нужного цвета
            for (ColorListItem colorList : imageItem.getColorList()) {
                if (colorList != null) {
                    if (colorList.getColor().toUpperCase().equals(color)) {
                        colorList_ = colorList;
                        break;
                    }
                }
            }

            // Списка для указанного цвета не найдено у модели, создаем новый
            if (colorList_ == null) {
                colorList_ = new ColorListItem();
                colorList_.setColor(color);
                imageItem.getColorList().add(colorList_);
            } else {
                for (ImageListItem imageListItem : colorList_.getImageList()) {
                    if (imageListItem != null) {
                        if (imageListItem.getSourceFile().toLowerCase().equals(getShortPath(file).toLowerCase())) {
                            return imageListItem;
                        }
                    }
                }
            }

            ImageListItem imageListItem = new ImageListItem();
            imageListItem.setSourceFile(getShortPath(file));
            imageListItem.setImageFile(getShortPath(file_));
            imageListItem.setImageWidth(info.getWidth());
            imageListItem.setImageHeight(info.getHeight());
            colorList_.getImageList().add(imageListItem);
            result = imageListItem;

            // Вновь добавленное
            info.setSource(getShortPath(file));
            info.setFile(file_.getAbsolutePath());
            imageMap.put(getShortPath(file), info);
        }
        return result;
    }

    private void addPictureAsDefaultSizeImage(Map<String, ImageInformation> imageMap, ModelImageItem imageItem, BufferedImage buffer, File file, int size) {
        addPictureAsDefaultImage(imageMap, imageItem, buffer, file);
        // Предполагаем, что дирректории уже простроены и XML загружен
        File pathProcessing = new File(CATALOG_DIR + File.separator + imageItem.getModelNumber() + File.separator + DEFAULT);
        // [МОДЕЛЬ][DEFAULT][РАЗМЕР][THUMBNAIL] Из буфера Создаем эскиз ее [SIZE_THUMBNAIL]px
        // [МОДЕЛЬ][DEFAULT][РАЗМЕР] Из буфера Создаем рабочую картинку [SIZE]px
        ImageInformation info = imageMap.get(getShortPath(file) + "(" + size + ")");
        if (info == null) {
            info = prepareImage(buffer, pathProcessing, file.getName(), size);
        }
        if (info != null) {
            for (SizeListItem sizeListItem : imageItem.getDefaultImageSizeList()) {
                if (sizeListItem != null) {
                    if ((size == sizeListItem.getSize()) && (sizeListItem.getSourceFile().toLowerCase().equals(file.getAbsolutePath().toLowerCase()))) {
                        return;
                    }
                }
            }

            SizeListItem sizeListItem = new SizeListItem();
            File file_ = info.getFile();
            sizeListItem.setSize(size);
            sizeListItem.setImageFile(getShortPath(file_));
            sizeListItem.setSourceFile(getShortPath(file));
            sizeListItem.setImageWidth(info.getWidth());
            sizeListItem.setImageHeight(info.getHeight());
            imageItem.getDefaultImageSizeList().add(sizeListItem);

            // Вновь добавленное
            info.setSource(getShortPath(file));
            info.setFile(file_.getAbsolutePath());
            imageMap.put(getShortPath(file) + "(" + size + ")", info);
        }
    }

    private void addPictureToModelColorSize(Map<String, ImageInformation> imageMap, ModelImageItem imageItem, BufferedImage buffer, File file, String color, int size) {
        // Создание группы цвета
        addPictureToModelColor(imageMap, imageItem, buffer, file, color);
        // Предполагаем, что дирректории уже простроены и XML загружен
        File pathProcessing = new File(CATALOG_DIR + File.separator + imageItem.getModelNumber() + File.separator + DEFAULT);
        // [МОДЕЛЬ][DEFAULT][THUMBNAIL] Из буфера Создаем эскиз ее [SIZE_THUMBNAIL]px
        // [МОДЕЛЬ][DEFAULT] Из буфера Создаем рабочую картинку [SIZE_COMMON] px

        ImageInformation info = imageMap.get(getShortPath(file) + "(" + size + ")");
        if (info == null) {
            info = prepareImage(buffer, pathProcessing, file.getName(), size);
        }

        if (info != null) {
            File file_ = info.getFile();
            // Шерстим имеющиеся цвета у модели и вытаскиваем список картинок нужного цвета
            ColorListItem colorList_ = null;
            ImageListItem imageListItem_ = null;
            SizeListItem sizeListItem_ = null;
            for (ColorListItem colorList : imageItem.getColorList()) {
                if (colorList != null) {
                    if (colorList.getColor().toUpperCase().equals(color.toUpperCase())) {
                        colorList_ = colorList;
                        break;
                    }
                }
            }

            // ГРуппа не добавлена
            if (colorList_ != null) {

                for (ImageListItem imageListItem : colorList_.getImageList()) {
                    if (imageListItem != null) {
                        if (imageListItem.getSourceFile().toLowerCase().equals(getShortPath(file).toLowerCase())) {
                            imageListItem_ = imageListItem;
                            break;
                        }
                    }
                }

                if (imageListItem_ != null) {

                    for (SizeListItem sizeListItem : imageListItem_.getSizeList()) {
                        if (sizeListItem != null) {
                            if ((size == sizeListItem.getSize()) && (sizeListItem.getSourceFile().toLowerCase().equals(getShortPath(file).toLowerCase()))) {
                                return;
                            }
                        }
                    }

                    sizeListItem_ = new SizeListItem();
                    sizeListItem_.setSize(size);
                    sizeListItem_.setImageFile(getShortPath(file_));
                    sizeListItem_.setSourceFile(getShortPath(file));
                    sizeListItem_.setImageWidth(info.getWidth());
                    sizeListItem_.setImageHeight(info.getHeight());
                    imageListItem_.getSizeList().add(sizeListItem_);

                    // Вновь добавленное
                    info.setSource(getShortPath(file));
                    info.setFile(file_.getAbsolutePath());
                    imageMap.put(getShortPath(file) + "(" + size + ")", info);
                }
            }
        }
    }

    private Map<String, ImageInformation> prepareImageHashMap(ModelImageItem model) {

        if (model != null) {
            Map<String, ImageInformation> result = new HashMap<>();
            if (model.getSourceFileDefault() != null) {
                ImageInformation image = new ImageInformation();
                image.setFile(model.getImageFileDefault());
                image.setSource(model.getSourceFileDefault());
                image.setWidth(model.getImageWidth());
                image.setHeight(model.getImageHeight());

                result.put(model.getSourceFileDefault(), image);

                for (SizeListItem sizeListItem : model.getDefaultImageSizeList()) {
                    image = new ImageInformation();
                    image.setFile(sizeListItem.getImageFile());
                    image.setSource(sizeListItem.getSourceFile());
                    image.setWidth(sizeListItem.getImageWidth());
                    image.setHeight(sizeListItem.getImageHeight());
                    result.put(sizeListItem.getSourceFile() + "(" + sizeListItem.getSize() + ")", image);
                }
            }

            for (ColorListItem item : model.getColorList()) {
                if (item != null) {
                    for (ImageListItem imageListItem : item.getImageList()) {
                        if (imageListItem != null) {

                            for (SizeListItem sizeListItem : imageListItem.getSizeList()) {
                                ImageInformation image = new ImageInformation();
                                image.setFile(sizeListItem.getImageFile());
                                image.setSource(sizeListItem.getSourceFile());
                                image.setWidth(sizeListItem.getImageWidth());
                                image.setHeight(sizeListItem.getImageHeight());

                                result.put(sizeListItem.getSourceFile() + "(" + sizeListItem.getSize() + ")", image);
                            }

                            ImageInformation image = new ImageInformation();
                            image.setFile(imageListItem.getImageFile());
                            image.setSource(imageListItem.getSourceFile());
                            image.setWidth(imageListItem.getImageWidth());
                            image.setHeight(imageListItem.getImageHeight());
                            result.put(imageListItem.getSourceFile(), image);
                        }
                    }
                }
            }

            return result;
        }
        return null;
    }

    public void batchCatalogProcessing() {
        // Шаримся по каталогу маркетологов, вытаскиваем ссылки на картинки, фильтруем JPG, обрабатываем
        String marketingCatalog = "//file-server/public$/Все модели/";
        String catalogListPath = "\\\\file-server\\catalog\\catalog\\";
        List<String> list = new ArrayList<>();
        try {
            Files.walk(Paths.get(marketingCatalog))
                    .filter(Files::isRegularFile)
                    .forEach(a -> {
                        String path = a.toString();
                        if (path.toLowerCase().endsWith(".jpg")) {
                            list.add(path);
                        }
                    });
            FileUtils.writeLines(new File(catalogListPath + "file_list.mirror"), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveModelImageConfig(ModelImageItem model) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelImageItem.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(model, new File(CATALOG_DIR + File.separator + model.getModelNumber() + File.separator + model.getModelNumber() + ".xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ModelImageItem loadModelImageConfig(File modelFile) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelImageItem.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (ModelImageItem) jaxbUnmarshaller.unmarshal(modelFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveModelImageListConfig(ModelImageList modelList) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelImageList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(modelList, new File(CATALOG_DIR + "models.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ModelImageList loadModelImageListConfig(File modelFile) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelImageList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (ModelImageList) jaxbUnmarshaller.unmarshal(modelFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
