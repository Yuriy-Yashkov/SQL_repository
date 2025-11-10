package by.march8.ecs.application.modules.nsi;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.nsi.ChekcNDS;

import javax.swing.*;

/**
 * Модуль NSI (нормативно-справочной информации) для приложения ECS.
 *
 *  Отвечает за регистрацию пункта меню {@code "Проверка ставок НДС"} в главной форме приложения
 *  и обработку события выбора этого пункта пользователем.
 *  При активации соответствующего элемента меню открывается окно проверки ставок НДС.
 *
 *  Класс реализует интерфейс {@link Module}, обеспечивая подключение модуля к {@link MainController}.
 */
public class NsiModule implements Module {

    /** Пункт меню "Проверка ставок НДС". */
    private final JMenuItem miCheckNDS = new JMenuItem("Проверка ставок НДС");
    private final JMenuItem miCheckNDS = new JMenuItem("Проверка ставок НДС");

    /** Основной контроллер приложения, через который выполняется регистрация меню и взаимодействие с интерфейсом. */
    private MainController controller;

    /** Главное окно приложения, используется как владелец для дочерних окон и диалогов. */
    private JFrame ownerFrame;

    /**
     * Регистрирует модуль в основном контроллере приложения.
     * Сохраняет ссылку на контроллер и главное окно, затем вызывает регистрацию пунктов меню.
     *
     * @param mainController основной контроллер приложения, в который добавляется модуль
     */
    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = controller.getMainForm();
        registerMenu();
    }

    /**
     * Регистрирует пункты меню, относящиеся к модулю "НСИ".
     * Добавляет пункт меню "Проверка ставок НДС" в раздел NSI_CHECKNDS и вызывает регистрацию событий.
     */
    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_CHECKNDS, miCheckNDS));

        registerMenuEvents();
    }

    /**
     * Регистрирует обработчики событий для пунктов меню.
     * При выборе пункта "Проверка ставок НДС" открывает окно {@link ChekcNDS}.
     */
    @Override
    public void registerMenuEvents() {
        miCheckNDS.addActionListener(e -> new ChekcNDS(ownerFrame));
    }
}
