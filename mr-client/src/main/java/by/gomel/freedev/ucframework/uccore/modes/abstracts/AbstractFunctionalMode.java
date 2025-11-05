package by.gomel.freedev.ucframework.uccore.modes.abstracts;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditableFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.march8.ecs.MainController;

/**
 * Created by Andy on 13.03.2015.
 * Абстрактный Класс-контроллер функционального режима.
 * Представляет собой контроллер с базовыми полями для создания полноценного функционального режима
 * Содержит необходимые ссылки и методы для последующей реализации
 */
public abstract class AbstractFunctionalMode<T> implements IEditableFunctionalMode {
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
    protected GridViewPort<T> gridViewPort;
    /**
     * Имя функционального режима
     */
    protected String modeName;

    /**
     * Реализация в частном случае не требуется т.к. режимы будут доступны не только лишь всем))
     */
    @Override
    public void viewRecord() {

    }

    public String getModeName() {
        return modeName;
    }


}
