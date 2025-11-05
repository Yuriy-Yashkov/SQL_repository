package by.march8.ecs.services.images;

import xsd.image.ModelImageItem;

public interface ImageConfiguration {

    void saveModelImageConfiguration();

    void saveModelImageToMap(ModelImageItem model);

    void saveModelImageToFile(ModelImageItem model);

    ModelImageItem loadModelImage(String modelNumber);
}
