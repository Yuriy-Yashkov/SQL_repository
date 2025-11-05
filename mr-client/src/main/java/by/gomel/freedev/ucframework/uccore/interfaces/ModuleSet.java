package by.gomel.freedev.ucframework.uccore.interfaces;

import by.march8.ecs.MainController;


/**
 * Интерфейс описывает методы инициализации набора модулей.
 *
 * @author andy-linux
 */
public interface ModuleSet {

    /**
     * Метод регистрирует набор модулей в контроллере модулей
     */
    void initialModuleSet(MainController controller);
}
