package by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;

/**
 * Интерфейс дополнительногог встраивания событий и компонентов
 * Created by Andy on 05.11.2014.
 */
public interface IControlPane {
    /**
     * Событие возникает после создания формы, и перед отображением формы на экране
     *
     */
    void beforeEmbedding(FrameViewPort formViewPort);

    /**
     * Событие возникает после отправки области просмотра на экран (показать форму)
     */
    void afterEmbedding();
}
