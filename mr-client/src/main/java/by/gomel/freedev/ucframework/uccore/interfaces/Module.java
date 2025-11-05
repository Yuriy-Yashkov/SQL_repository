package by.gomel.freedev.ucframework.uccore.interfaces;

import by.march8.ecs.MainController;

import javax.swing.*;

/**
 * Интерфейс подключаемого к программе функционального модуля.
 *
 * @author andy-linux
 * @see ModuleSet
 */
public interface Module {
    /**
     * Регистрация модуля.
     *
     * @param mainController ссылка на контроллер приложения
     */
    void registerModule(MainController mainController);

    /**
     * Метод регистрации модуля в пунктах главного меню программы.
     */
    void registerMenu();

    /**
     * Метод регистрации событий пунктов меню модуля.
     */
    void registerMenuEvents();
}
