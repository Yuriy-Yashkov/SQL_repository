package by.march8.ecs.application.modules.filemanager.model;

import xsd.image.ModelImageItem;

/**
 * @author Andy 18.01.2019 - 7:28.
 */
public interface ImageServiceListener {
    void selectionChanged(ModelImageItem activeItem, boolean dataHasChanged);
}
