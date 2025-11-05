package by.march8.ecs.services.images;

import by.march8.ecs.application.modules.filemanager.model.ImageItem;

import java.util.List;

/**
 * @author Andy 27.12.2018 - 14:28.
 */
public interface ColorImageService extends ImageService {
    List<String> getColorListForModel(String model);

    List<ImageItem> getColorImageListByModelAndColor(String model, String color);

    List<ImageItem> getColorImageListByModel(String model);

    ImageItem getImageByModelNumberAndImageName(String model, String imageFile);

    ImageItem getDefaultImageByModelNumber(String model);

    List<ImageItem> getColorImageListByModelAndColor(String model, String color, int size);

    List<ImageItem> getColorImageListByModel(String model, int size);

    String getImageLargeName(String name);
}
