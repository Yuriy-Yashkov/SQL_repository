package by.march8.ecs.services.images;

import by.gomel.freedev.ucframework.uccore.utils.PictureUtils;
import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.services.IService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andy 27.12.2018 - 11:27.
 */
@SuppressWarnings("all")
@Deprecated
public class ModelImageServiceXML extends AbstractImageService implements IService, ColorImageService, ImageServiceControl, ImageConfiguration {
    private static ModelImageServiceXML instance = null;
    public boolean isWindows = false;
    private boolean isDebug;
    private boolean dataChanged = false;


    private HashMap<String, ModelImageItem> imageMap = null;

    private Map<String, String> saveMap = new HashMap<>();


    private ModelImageServiceXML() {
        isWindows = SystemUtils.isWindows();
        prepareEnvironment();
        reload();
    }

    public static ColorImageService getInstance(boolean b) {
        if (instance == null) {
            instance = new ModelImageServiceXML();
        }
        return instance;
    }


    @Override
    public List<String> getImageListByModelNumber(String modelNumber, ModelImageSize imageSize) {
        ModelImageItem image = imageMap.get(modelNumber);
        List<String> list = new ArrayList<>();
        if (image != null) {
            if (imageSize == ModelImageSize.SMALL) {
                list.add(getLargeFileName(getThumbnailsImage(image.getImageFileDefault())));
            } else {
                list.add(getLargeFileName(image.getImageFileDefault()));
            }

            for (ColorListItem colorListItem : image.getColorList()) {
                if (colorListItem != null) {
                    for (ImageListItem listItem : colorListItem.getImageList()) {
                        if (listItem != null) {
                            if (!image.getImageFileDefault().equals(listItem.getImageFile())) {
                                if (imageSize == ModelImageSize.SMALL) {
                                    list.add(getLargeFileName(getThumbnailsImage(listItem.getImageFile())));
                                } else {
                                    list.add(getLargeFileName(listItem.getImageFile()));
                                }
                            }
                        }
                    }
                }
            }
        }

        return list;
    }

    @Override
    public String getDefaultImageFileByModelNumber(String modelNumber, ModelImageSize imageSize) {
        ModelImageItem image = imageMap.get(modelNumber);
        if (image != null) {
            if (imageSize == ModelImageSize.BIG) {
                return getLargeFileName(image.getImageFileDefault());
            } else {
                return getLargeFileName(getThumbnailsImage(image.getImageFileDefault()));
            }
        } else {
            return getNoImageFile();
        }
    }


    @Override
    public ImageItem getDefaultImageByModelNumber(String model) {
        if (model == null) {
            return null;
        }

        ModelImageItem imageItem = imageMap.get(model);
        if (imageItem != null) {
            for (ColorListItem color : imageItem.getColorList()) {
                ImageListItem image_ = new ImageListItem();
                image_.setImageFile(imageItem.getImageFileDefault());
                image_.setSourceFile(imageItem.getSourceFileDefault());
                image_.setImageHeight(imageItem.getImageHeight());
                image_.setImageWidth(imageItem.getImageWidth());
                ImageItem item = new ImageItem();
                item.setModel(model);
                item.setImage(image_);
                return item;
            }
        }

        return null;
    }

    //@Override
    @Deprecated
    public String getImageFileByModelNumber(String modelNumber, ModelImageSize imageSize) {
        return null;
    }

    @Override
    public String getNoImageFile() {
        return getLargeFileName("/noimage.jpg");
    }


    private void reload() {
        File collectionFile = new File(CATALOG_DIR + "models.xml");
        if (collectionFile.exists()) {
            try {
                ModelImageList modelList = loadModelImageListConfig();
                if (modelList != null) {
                    System.out.println("*******************************************************************************");
                    System.out.println("Служба изображений запущена, загружено [" + modelList.getModelImageItems().size() + "] записей");
                    System.out.println("Файл коллекции [" + collectionFile.getAbsolutePath() + "]");
                    System.out.println("*******************************************************************************");
                    imageMap = prepareImageMap(modelList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("*******************************************************************************");
            System.out.println("Нет доступа к файлу [" + collectionFile.getAbsolutePath() + "]");
            System.out.println("*******************************************************************************");
        }
    }

    private HashMap<String, ModelImageItem> prepareImageMap(ModelImageList modelList) {
        HashMap<String, ModelImageItem> result = new HashMap<>();
        for (ModelImageItem item : modelList.getModelImageItems()) {
            if (item != null) {
                String key = item.getModelNumber();
                result.put(key, item);
            }
        }
        return result;
    }

    @Override
    public List<String> getColorListForModel(String model) {
        ModelImageItem image = imageMap.get(model);
        List<String> list = new ArrayList<>();
        if (image != null) {
            for (ColorListItem colorListItem : image.getColorList()) {
                if (colorListItem != null) {
                    list.add(colorListItem.getColor());
                }
            }
            Collections.sort(list, String::compareTo);
        }
        return list;
    }

    @Override
    public List<ImageItem> getColorImageListByModelAndColor(String model, String color) {
        ModelImageItem image = imageMap.get(model);
        List<ImageItem> list = new ArrayList<>();
        if (image != null) {
            for (ColorListItem colorListItem : image.getColorList()) {
                if (colorListItem != null) {
                    if (colorListItem.getColor().toUpperCase().equals(color.toUpperCase().trim())) {
                        for (ImageListItem listItem : colorListItem.getImageList()) {
                            if (listItem != null) {
                                list.add(getImageAsImageItem(model, listItem, color));
                            }
                        }
                        break;
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<ImageItem> getColorImageListByModelAndColor(String model, String color, int size) {
        ModelImageItem image = imageMap.get(model);
        List<ImageItem> list = new ArrayList<>();
        if (image != null) {
            for (ColorListItem colorListItem : image.getColorList()) {
                if (colorListItem != null) {
                    if (colorListItem.getColor().toUpperCase().equals(color.toUpperCase().trim())) {
                        for (ImageListItem listItem : colorListItem.getImageList()) {
                            if (size == SIZE_COMMON) {
                                ImageItem item = new ImageItem();
                                item.setModel(model);
                                item.setImage(listItem);
                                item.setColor(colorListItem.getColor());
                                list.add(item);
                            } else {
                                for (SizeListItem sizeListItem : listItem.getSizeList()) {
                                    if (sizeListItem != null) {
                                        if (sizeListItem.getSize() == size) {
                                            list.add(getImageAsImageItem(model, sizeListItem, colorListItem.getColor()));
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return list;
    }


    @Override
    public List<ImageItem> getColorImageListByModel(String model) {
        ModelImageItem image = imageMap.get(model);
        List<ImageItem> list = new ArrayList<>();
        if (image != null) {
            for (ColorListItem colorListItem : image.getColorList()) {
                if (colorListItem != null) {
                    if (!colorListItem.getColor().toUpperCase().equals("РАЗНОЦВЕТ")) {
                        for (ImageListItem listItem : colorListItem.getImageList()) {
                            if (listItem != null) {
                                list.add(getImageAsImageItem(model, listItem, colorListItem.getColor()));
                            }
                        }
                    }
                }
            }
            Collections.sort(list, (a, b) -> {
                if ((a != null) && (b != null)) {
                    return a.getColor().compareTo(b.getColor());
                }
                return 0;
            });
        }
        return list;
    }

    @Override
    public List<ImageItem> getColorImageListByModel(String model, int size) {
        ModelImageItem image = imageMap.get(model);
        List<ImageItem> list = new ArrayList<>();
        if (image != null) {
            for (ColorListItem colorListItem : image.getColorList()) {
                if (colorListItem != null) {
                    if (!colorListItem.getColor().toUpperCase().equals("РАЗНОЦВЕТ")) {
                        for (ImageListItem listItem : colorListItem.getImageList()) {
                            if (listItem != null) {
                                if (size == SIZE_COMMON) {
                                    list.add(getImageAsImageItem(model, listItem, colorListItem.getColor()));
                                } else {
                                    for (SizeListItem sizeListItem : listItem.getSizeList()) {
                                        if (sizeListItem != null) {
                                            if (sizeListItem.getSize() == size) {
                                                list.add(getImageAsImageItem(model, sizeListItem, colorListItem.getColor()));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Collections.sort(list, (a, b) -> {
                if ((a != null) && (b != null)) {
                    return a.getColor().compareTo(b.getColor());
                }
                return 0;
            });
        }

        return list;
    }

    @Override
    public String getImageLargeName(String name) {
        return getLargeFileName(name);
    }

    @Override
    public ImageItem getImageByModelNumberAndImageName(String model, String image) {
        if (image == null || model == null) {
            return null;
        }

        ModelImageItem imageItem = imageMap.get(model);
        if (imageItem != null) {
            for (ColorListItem color : imageItem.getColorList()) {
                for (ImageListItem image_ : color.getImageList()) {
                    if (image.toLowerCase().equals(image_.getImageFile().toLowerCase())) {
                        ImageItem item = new ImageItem();
                        item.setModel(model);
                        item.setImage(image_);
                        item.setColor(color.getColor());
                        return item;
                    }
                }
            }
        }
        return null;
    }


    @Override
    public void saveModelImageToMap(ModelImageItem model) {
        dataChanged = true;
        imageMap.put(model.getModelNumber(), model);
        saveMap.put(model.getModelNumber(), model.getModelNumber());
    }

    public ModelImageItem loadModelImage(String modelNumber) {
        return imageMap.get(modelNumber);
    }

    @Override
    public boolean sendImageToColorGroup(ImageItem image, String color) {

        ModelImageItem destImageItem = imageMap.get(image.getModel());
        if (destImageItem == null) {
            return false;
        }

        ColorListItem colorList_ = null;

        // Ищем группу нужного цвета куда будем добавлять картинку
        for (ColorListItem colorList : destImageItem.getColorList()) {
            if (colorList != null) {
                if (colorList.getColor().toUpperCase().equals(color)) {
                    colorList_ = colorList;
                    break;
                }
            }
        }

        // Если нету, создаем новую группу цвета
        if (colorList_ == null) {
            colorList_ = new ColorListItem();
            colorList_.setColor(color);
            destImageItem.getColorList().add(colorList_);
        }

        // Находим в группе-исходнике данную картинку, и переносим ее в группу назначения
        int index = -1;
        for (ColorListItem colorList : destImageItem.getColorList()) {
            if (colorList != null) {
                if (colorList.getColor().toUpperCase().equals(image.getColor())) {
                    for (ImageListItem imageListItem : colorList.getImageList()) {
                        index++;
                        if (imageListItem != null) {
                            if (imageListItem.getImageFile().equals(image.getImage().getImageFile()) &&
                                    imageListItem.getSourceFile().equals(image.getImage().getSourceFile())) {
                                image.setImage(imageListItem);
                                break;
                            }
                        }
                    }
                    // Удаляем из старой группы, и переносим в новую группу
                    colorList.getImageList().remove(index);
                    break;
                }
            }
        }

        // ПОищем, есть ли группе назначения такой исходник
        for (ImageListItem imageListItem : colorList_.getImageList()) {
            if (imageListItem != null) {
                if (imageListItem.getImageFile().equals(image.getImage().getImageFile()) &&
                        imageListItem.getSourceFile().equals(image.getImage().getSourceFile())) {
                    saveModelImageToMap(destImageItem);
                    return true;
                }
            }
        }


        colorList_.getImageList().add(image.getImage());
        saveModelImageToMap(destImageItem);
        return true;
    }


    @Override
    public ImageItem createImageInNewSize(ImageItem image, int size) {
        if (image == null) {
            return null;
        }

        // Найдем в куче модельку
        ModelImageItem destImageItem = imageMap.get(image.getModel());
        if (destImageItem == null) {
            return null;
        }

        // найдем картинку в одной из цветовых групп
        ImageListItem imageItem_ = null;
        for (ColorListItem colorList : destImageItem.getColorList()) {
            if (colorList != null) {
                for (ImageListItem imageListItem : colorList.getImageList()) {
                    if (imageListItem != null) {
                        if (imageListItem.getImageFile().equals(image.getImage().getImageFile()) &&
                                imageListItem.getSourceFile().equals(image.getImage().getSourceFile())) {
                            imageItem_ = imageListItem;
                            // Если нашли картинку, поищем, может есть такой размер уже
                            for (SizeListItem sizeListItem : imageItem_.getSizeList()) {
                                if (sizeListItem != null) {
                                    if (sizeListItem.getSize() == size) {
                                        return getImageAsImageItem(image.getModel(), sizeListItem, colorList.getColor());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Данной картинки нет у модели
        if (imageItem_ == null) {
            return null;
        }

        //ПОищем,
        SizeListItem sizeItem_ = null;


        if (sizeItem_ == null) {
            sizeItem_ = new SizeListItem();
            sizeItem_.setSize(size);
            imageItem_.getSizeList().add(sizeItem_);
        }

        ///if()

        return null;
    }


    @Override
    public void saveModelImageConfiguration() {
        ModelImageList list = new ModelImageList();
        // Удаление через мапу
        Iterator it = imageMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ModelImageItem item = (ModelImageItem) pair.getValue();
            list.getModelImageItems().add(item);
        }

        it = saveMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String item = (String) pair.getValue();
            if (item != null) {
                ModelImageItem savedItem = imageMap.get(item);
                if (savedItem != null) {
                    saveModelImageToFile(savedItem);
                }
            }
        }

        saveMap.clear();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelImageList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(list, new File(CATALOG_DIR + "models.xml"));
            dataChanged = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean setImageAsDefault(ImageItem image) {
        ModelImageItem imageItem = imageMap.get(image.getModel());
        if (imageItem == null) {
            return false;
        }

        for (ColorListItem colorList : imageItem.getColorList()) {
            if (colorList != null) {
                for (ImageListItem imageListItem : colorList.getImageList()) {
                    if (imageListItem != null) {
                        if (imageListItem.getImageFile().equals(image.getImage().getImageFile()) &&
                                imageListItem.getSourceFile().equals(image.getImage().getSourceFile())) {

                            imageItem.setImageFileDefault(imageListItem.getImageFile());
                            imageItem.setSourceFileDefault(imageListItem.getSourceFile());
                            imageItem.setImageHeight(imageListItem.getImageHeight());
                            imageItem.setImageWidth(imageListItem.getImageWidth());
                            saveModelImageToMap(imageItem);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void saveModelImageToFile(ModelImageItem model) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelImageItem.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(model, new File(CATALOG_DIR + model.getModelNumber() + File.separator + model.getModelNumber() + ".xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private ModelImageList loadModelImageListConfig() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelImageList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (ModelImageList) jaxbUnmarshaller.unmarshal(new File(CATALOG_DIR + "models.xml"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isDataChanged() {
        return dataChanged;
    }


    /**
     * Метод формирует список необработанных файлов
     */
    public List<String> createImageFileList() {
        Set<String> set_ = getSourceFileList();
        return createProcessingImageFileList(set_);
    }

    private String getOriginalFile(ModelImageItem item_, String file) {
        for (ColorListItem item : item_.getColorList()) {
            if (item != null) {
                for (ImageListItem imageListItem : item.getImageList()) {
                    if (imageListItem != null) {
                        if (imageListItem.getImageFile().toLowerCase().equals(getShortFullSizePath(file.toLowerCase()))) {
                            return imageListItem.getSourceFile();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void ignoreImageFile(String file) {
        addFileToBlackList(file);
    }

    @Override
    public boolean haveImage(String model) {
        return imageMap.get(model) != null;
    }

    public Set<String> getSourceFileList() {
        Set<String> result = new HashSet<>();
        for (Object o : imageMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            ModelImageItem item = (ModelImageItem) pair.getValue();
            if (item != null) {
                for (ColorListItem colorListItem : item.getColorList()) {
                    if (colorListItem != null) {
                        for (ImageListItem listItem : colorListItem.getImageList()) {
                            if (listItem != null) {
                                result.add(listItem.getSourceFile().toLowerCase());
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public boolean sendImageToColorGroup(File file, String color) {
        // Получаем номер модели из имени файла изображения
        String modelNumber = ModelImageService.parseModelNumber(file.getName());
        if (modelNumber != null) {
            if (modelNumber.length() < 3) {
                return false;
            }
        } else {
            return false;
        }

        // Корневой каталог
        String rootDir = CATALOG_DIR + File.separator + modelNumber + File.separator;
        // Есть ли папка модели
        File destinationDir = new File(rootDir);
        boolean dirComplete = true;

        ModelImageItem model = null;

        if (!destinationDir.exists()) {
            try {
                dirComplete = destinationDir.mkdirs();
                if (dirComplete) {
                    model = new ModelImageItem();
                    model.setModelNumber(modelNumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        model = imageMap.get(modelNumber);
        if (model == null) {
            model = new ModelImageItem();
            model.setModelNumber(modelNumber);
        }


        BufferedImage currentBuffer = PictureUtils.pictureOpen(file);
        if (currentBuffer != null) {
            Map<String, ImageInformation> imageMap = prepareImageHashMap(model);
            if (model.getImageFileDefault() == null) {
                addPictureAsDefaultImage(imageMap, model, currentBuffer, file);
            }
            addPictureToModelColor(imageMap, model, currentBuffer, file, color);
        }

        saveModelImageToMap(model);
        return true;
    }

    private ImageListItem addPictureToModelColor(Map<String, ImageInformation> imageMap, ModelImageItem
            imageItem, BufferedImage buffer, File file, String color) {
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

    private void addPictureAsDefaultImage(Map<String, ImageInformation> imageMap, ModelImageItem
            imageItem, BufferedImage buffer, File file) {
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


    @Override
    public ImageConfiguration getImageConfiguration() {
        return this;
    }
}
