package by.march8.ecs.services.images;

import by.march8.ecs.application.modules.marketing.model.ModelImageSize;

import java.util.List;

/**
 * @author Andy 27.12.2018 - 11:03.
 */
public interface ImageService {
    List<String> getImageListByModelNumber(String modelNumber, ModelImageSize imageSize);

    String getDefaultImageFileByModelNumber(String modelNumber, ModelImageSize imageSize);

    String getNoImageFile();

    boolean haveImage(String model);
}
