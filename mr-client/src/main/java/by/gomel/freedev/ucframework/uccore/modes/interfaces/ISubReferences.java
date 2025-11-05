package by.gomel.freedev.ucframework.uccore.modes.interfaces;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Интерфейс подсправочника, позволяет унифицировать получения доступа к наследникам {@link by.march8.ecs.framework.sdk.reference.abstracts.SubReferences}
 */
public interface ISubReferences {
    /**
     * Возвращает коллекцию данных как HashSet
     *
     * @return коллекция данных
     */
    Collection<?> getData();

    /**
     * Устанавливает ссылку компоненту на внешнюю коллекцтю данных
     *
     * @param data ссылка на коллекцию
     */
    void setData(Collection<?> data);

    /**
     * Возвращает коллекцию данных как ArrayList
     *
     * @return коллекция данных
     */
    Collection<?> getDataForListType();

    /**
     * Устанавливает объект источник для компонента
     *
     * @param object объект
     */
    void setSourceEntity(Object object);

    /**
     * Метод вызывается при обновлении объекта в коллекции.
     * Обновлением в этом случае считается как добавление нового объекта, так и редактирование существующего.
     * Реализация конкретного случая зависит от типа связи между Entity в модели БД:
     * <code>@OneToOne; @OneToMany; @ManyToMany; @ManyToOne</code>
     *
     * @param object объект
     */
    void updateEntity(Object object);

    /**
     * Возвращает ссылку на Область просмотра ТАБЛИЦА
     *
     * @return область просмотра
     */
    GridViewPort getGridViewPort();

    /**
     * Возвращает коллекцию данных типа ArrayList без
     * предварительных преобразований
     *
     * @return коллекция типа ArrayList
     */
    ArrayList<Object> getDataArray();
}
