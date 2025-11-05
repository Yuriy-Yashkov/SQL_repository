package by.gomel.freedev.ucframework.uccore.viewport.preview;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.march8.ecs.MainController;

/**
 * @author Andy 03.11.2015.
 */
public abstract class PreviewViewPort {
    /**
     * Ссылка на главный контроллер приложения
     */
    protected MainController controller;
    /**
     * Область просмотра ФОРМА
     */
    protected FrameViewPort frameViewPort;

    /**
     * Область просмотра ТАБЛИЦА
     */
    protected GridViewPort gridViewPort;


}
