package by.march8.ecs.services.images;


import by.march8.ecs.application.modules.filemanager.model.ImageItem;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;

import java.io.File;
import java.util.List;

/**
 * @author Andy 28.12.2018 - 7:57.
 */
public interface ImageServiceControl {

    boolean haveImage(String model);

    String getDefaultImageFileByModelNumber(String modelNumber, ModelImageSize imageSize);

    List<ImageItem> getColorImageListByModel(String model);

    List<ImageItem> getColorImageListByModelAndColor(String model, String color);

    boolean sendImageToColorGroup(ImageItem image, String color);

    boolean sendImageToColorGroup(File image, String color);

    boolean setImageAsDefault(ImageItem image);


    ImageItem createImageInNewSize(ImageItem image, int size);

    List<String> createImageFileList();

    void ignoreImageFile(String file);

    boolean isDataChanged();

    ImageConfiguration getImageConfiguration();
}
