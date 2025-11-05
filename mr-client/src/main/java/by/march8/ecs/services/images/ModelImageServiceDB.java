package by.march8.ecs.services.images;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.filemanager.model.ColorTextItem;
import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.services.IService;
import by.march8.entities.images.DBImage;
import by.march8.entities.images.DBImageView;
import by.march8.entities.images.DBModelImage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelImageServiceDB extends AbstractImageService implements IService, ColorImageService {

    public static ModelImageServiceDB instance;

    private HashMap<Integer, DBModelImage> imageMap;

    private ModelImageServiceDB() {
        prepareEnvironment();

        try {
            imageMap = prepareImageMap();
            if (imageMap != null) {
                System.out.println("*******************************************************************************");
                System.out.println("Служба изображений запущена, загружено [" + imageMap.size() + "] ");
                System.out.println("*******************************************************************************");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ColorImageService getInstance() {
        if (instance == null) {
            instance = new ModelImageServiceDB();
        }
        return instance;
    }


    @Override
    public List<String> getImageListByModelNumber(String modelNumber, ModelImageSize imageSize) {
        DBModelImage image = requestImageFromSource(Integer.valueOf(modelNumber));
        List<String> list = new ArrayList<>();
        if (image != null) {
            DBImage defaultImage = image.getDefaultImage();
            if (defaultImage != null) {
                if (imageSize == ModelImageSize.SMALL) {
                    list.add(getLargeFileName(getThumbnailsImage(defaultImage.getImageFileName())));
                } else {
                    list.add(getLargeFileName(defaultImage.getImageFileName()));
                }
            }
            for (DBImage item : image.getImageList()) {
                if (item != null) {
                    if (imageSize == ModelImageSize.SMALL) {
                        list.add(getLargeFileName(getThumbnailsImage(item.getImageFileName())));
                    } else {
                        list.add(getLargeFileName(item.getImageFileName()));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public String getDefaultImageFileByModelNumber(String modelNumber, ModelImageSize imageSize) {
        DBModelImage image = requestImageFromSource(Integer.valueOf(modelNumber));
        if (image != null) {
            DBImage defaultImage = getDefaultImageById(image, image.getDefaultImage().getId());
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

    private DBImage getDefaultImageById(DBModelImage image, int id) {

        for (DBImage item_ : image.getImageList()) {
            if (item_.getId() == id) {
                return item_;
            }
        }
        return null;
    }

    @Override
    public String getNoImageFile() {
        return null;
    }

    @Override
    public boolean haveImage(String model) {
        return imageMap.containsKey(Integer.valueOf(model));
    }


    @Override
    public List<String> getColorListForModel(String model) {
        DBModelImage image = requestImageFromSource(Integer.valueOf(model));
        List<String> list = new ArrayList<>();
        Set<String> colorSet = new HashSet<>();
        if (image != null) {
            for (DBImage item : image.getImageList()) {
                if (item != null) {
                    if (item.getColorId() != 2) {
                        ColorTextItem colorItem = ColorPresetHelper.getColorById(item.getColorId());
                        colorSet.add(colorItem.getName());
                    }
                }
            }
            list.addAll(colorSet);
            list.sort(String::compareTo);
        }
        return list;
    }

    @Override
    public List<ImageItem> getColorImageListByModelAndColor(String model, String color) {
        DBModelImage image = requestImageFromSource(Integer.valueOf(model));
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
        DBModelImage image = requestImageFromSource(Integer.valueOf(model));
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
    public ImageItem getImageByModelNumberAndImageName(String model, String imageFile) {
        if (imageFile == null || model == null) {
            return null;
        }

        DBModelImage image = requestImageFromSource(Integer.valueOf(model));
        if (image != null) {
            for (DBImage item : image.getImageList()) {
                if (imageFile.toLowerCase().equals(item.getImageFileName().toLowerCase())) {
                    ColorTextItem colorItem = ColorPresetHelper.getColorById(item.getColorId());
                    if (colorItem != null) {
                        String colorKey = colorItem.getName();
                        return getImageAsImageItem(model, item, colorKey);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ImageItem getDefaultImageByModelNumber(String model) {
        DBModelImage image = requestImageFromSource(Integer.valueOf(model));
        if (image != null) {
            DBImage defaultImage = getDefaultImageById(image, image.getDefaultImage().getId());
            if (defaultImage != null) {
                return getImageAsImageItem(model, defaultImage, null);
            }
        }
        return null;
    }

    @Override
    public List<ImageItem> getColorImageListByModelAndColor(String model, String color, int size) {
        return getColorImageListByModelAndColor(model, color);
    }

    @Override
    public List<ImageItem> getColorImageListByModel(String model, int size) {
        return getColorImageListByModel(model);
    }


    private boolean sourceInUse(DBModelImage model, String source) {
        for (DBImage image : model.getImageList()) {
            if (image.getSourceFileName().toLowerCase().equals(getShortPath(source).toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private DBModelImage requestImageFromSource(int modelNumber) {
        if (imageMap == null) {
            return null;
        }
        return imageMap.get(modelNumber);
    }

    @Override
    public String getImageLargeName(String name) {
        return getLargeFileName(name);
    }

    private HashMap<Integer, DBModelImage> prepareImageMap() {
        List<DBImageView> listImages = null;

        DaoFactory<DBImageView> factoryImages = DaoFactory.getInstance();
        IGenericDao<DBImageView> daoImages = factoryImages.getGenericDao();

        try {
            listImages = daoImages.getAllEntity(DBImageView.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (listImages != null) {
            imageMap = new HashMap<>();
            for (DBImageView view : listImages) {
                DBModelImage model = imageMap.get(view.getModelNumber());
                if (model == null) {
                    model = new DBModelImage();
                    model.setModelNumber(view.getModelNumber());
                    DBImage default_ = new DBImage();
                    default_.setId(view.getDefaultImage());
                    model.setDefaultImage(default_);
                    imageMap.put(view.getModelNumber(), model);
                }

                DBImage image = new DBImage();
                image.setId(view.getId());
                image.setColorId(view.getColorId());
                image.setImageFileName(view.getImageFileName());
                image.setSourceFileName(view.getSourceFileName());
                image.setSize(view.getSize());
                image.setHeight(view.getHeight());
                image.setWidth(view.getWidth());
                image.setModel(model);

                model.getImageList().add(image);

            }
        }

        return imageMap;
    }
}
