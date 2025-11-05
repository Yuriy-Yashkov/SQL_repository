package by.march8.ecs.services.images;

import by.gomel.freedev.ucframework.uccore.utils.PictureUtils;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.filemanager.dao.ImageManagerJDBC;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.services.IService;
import by.march8.entities.images.DBImage;
import by.march8.entities.images.DBModelImage;
import xsd.image.ImageInformation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModelImageServiceControlDB extends AbstractImageService implements IService, ImageServiceControl {

    public static ModelImageServiceControlDB instance;
    private Set<Integer> modelSet;
    private DBModelImage lastRequestItem;

    private DaoFactory<DBModelImage> factory;
    private IGenericDao<DBModelImage> dao;

    private ModelImageServiceControlDB() {
        if (modelSet == null) {
            modelSet = updateModelSet();
        }
        prepareEnvironment();

        factory = DaoFactory.getInstance();
        dao = factory.getGenericDao();
    }

    public static ImageServiceControl getInstance() {
        if (instance == null) {
            instance = new ModelImageServiceControlDB();
        }
        return instance;
    }

    @Override
    public String getDefaultImageFileByModelNumber(String modelNumber, ModelImageSize imageSize) {
        DBModelImage image = requestImageFromDB(Integer.valueOf(modelNumber));
        if (image != null) {
            DBImage defaultImage = image.getDefaultImage();
            if (defaultImage != null) {
                if (imageSize == ModelImageSize.BIG) {
                    return getLargeFileName(defaultImage.getImageFileName());
                } else {
                    return getLargeFileName(getThumbnailsImage(defaultImage.getImageFileName()));
                }
            }
        }
        return null;
    }

    @Override
    public List<ImageItem> getColorImageListByModelAndColor(String model, String color) {
        DBModelImage image = requestImageFromDB(Integer.valueOf(model));
        List<ImageItem> list = new ArrayList<>();
        if (image != null) {
            for (DBImage item : image.getImageList()) {
                ColorTextItem colorItem = ColorPresetHelper.getColorById(item.getColorId());
                if (colorItem != null) {
                    String colorKey = colorItem.getName();
                    if (colorKey.toUpperCase().equals(color.toUpperCase().trim())) {
                        list.add(getImageAsImageItem(model, item, color));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<ImageItem> getColorImageListByModel(String model) {
        DBModelImage image = requestImageFromDB(Integer.valueOf(model));
        List<ImageItem> list = new ArrayList<>();
        if (image != null) {
            for (DBImage item : image.getImageList()) {
                ColorTextItem colorItem = ColorPresetHelper.getColorById(item.getColorId());
                if (colorItem != null) {
                    String colorKey = colorItem.getName();
                    if (!colorKey.toUpperCase().equals("РАЗНОЦВЕТ")) {
                        list.add(getImageAsImageItem(model, item, colorKey));
                    }
                }
            }
            list.sort((a, b) -> {
                if ((a != null) && (b != null)) {
                    return a.getColor().compareTo(b.getColor());
                }
                return 0;
            });
        }

        return list;
    }

    @Override
    public boolean sendImageToColorGroup(ImageItem image_, String color) {
        DBModelImage image = requestImageFromDB(Integer.valueOf(image_.getModel()));
        if (image != null) {
            for (DBImage item : image.getImageList()) {
                if (item.getId() == image_.getId()) {
                    ColorTextItem colorItem = ColorPresetHelper.getColorByName(color);
                    if (colorItem != null) {
                        item.setColorId(colorItem.getId());
                    }
                    try {
                        dao.updateEntity(image);
                        lastRequestItem = null;
                        updateModelSet();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
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

        if (!destinationDir.exists()) {
            try {
                dirComplete = destinationDir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DBModelImage image = requestImageFromDB(Integer.valueOf(modelNumber));
        if (image == null) {
            image = new DBModelImage();
            image.setModelNumber(Integer.valueOf(modelNumber));
        }

        if (!sourceInUse(image, file.getAbsolutePath())) {

            BufferedImage currentBuffer = PictureUtils.pictureOpen(file);
            if (currentBuffer != null) {
                ImageInformation imageInformation = prepareImage(currentBuffer, destinationDir, file.getName(), SIZE_COMMON);
                if (imageInformation != null) {

                    DBImage item = new DBImage();
                    item.setModel(image);
                    ColorTextItem colorItem = ColorPresetHelper.getColorByName(color);
                    if (colorItem != null) {
                        item.setColorId(colorItem.getId());
                    }

                    if (image.getDefaultImage() == null) {
                        image.setDefaultImage(item);
                    }

                    item.setImageFileName(getShortPath(imageInformation.getFile()));
                    item.setSourceFileName(getShortPath(file.getAbsolutePath()));
                    item.setWidth(imageInformation.getWidth());
                    item.setHeight(imageInformation.getHeight());
                    item.setSize(SIZE_COMMON);

                    image.getImageList().add(item);
                    try {
                        if (image.getId() > 0) {
                            dao.updateEntity(image);
                        } else {
                            dao.saveEntity(image);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Назначение изображению [" + file.getAbsolutePath() + "] модели [" + modelNumber + "] цветовой группы [" + color + "]");
                    updateModelSet();
                    lastRequestItem = null;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean setImageAsDefault(ImageItem image_) {
        DBModelImage image = requestImageFromDB(Integer.valueOf(image_.getModel()));
        if (image != null) {
            for (DBImage item : image.getImageList()) {
                if (item.getId() == image_.getId()) {
                    image.setDefaultImage(item);
                    try {
                        dao.updateEntity(image);
                        lastRequestItem = null;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    private boolean sourceInUse(DBModelImage model, String source) {
        for (DBImage image : model.getImageList()) {
            if (image.getSourceFileName().toLowerCase().equals(getShortPath(source).toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ImageItem createImageInNewSize(ImageItem image, int size) {
        return null;
    }

    @Override
    public List<String> createImageFileList() {
        ImageManagerJDBC db = new ImageManagerJDBC();
        Set<String> set_ = db.getImagesSourceFileSet();
        return createProcessingImageFileList(set_);
    }

    @Override
    public void ignoreImageFile(String file) {
        addFileToBlackList(file);
    }

    @Override
    public boolean isDataChanged() {
        return true;
    }

    private DBModelImage requestImageFromDB(int modelNumber) {
        if (lastRequestItem != null) {
            if (lastRequestItem.getModelNumber() == modelNumber) {
                return lastRequestItem;
            }
        }
        String sql = "SELECT item FROM DBModelImage item WHERE item.modelNumber = " + modelNumber;
        try {
            DBModelImage image = dao.getEntityByQuery(DBModelImage.class, sql);
            if (image != null) {
                lastRequestItem = image;
                return lastRequestItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Set<Integer> updateModelSet() {
        try {
            ImageManagerJDBC db = new ImageManagerJDBC();
            return db.getModelNumbersHavingImages();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void saveToDatabase(DBModelImage image) {
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDao dao = factory.getCommonDao();
        try {
            dao.saveEntity(image);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка сохранения документа по его ID [" + image.getId() + "]");
        }
    }

    @Override
    public boolean haveImage(String model) {
        return (modelSet.contains(Integer.valueOf(model)));
    }

    @Override
    public ImageConfiguration getImageConfiguration() {
        return null;
    }
}
