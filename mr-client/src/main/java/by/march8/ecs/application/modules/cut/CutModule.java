/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.march8.ecs.application.modules.cut;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.cut.view.RouteListDialogCut;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;

/**
 *
 * @author pro03
 */
/**
 * Модуль подсистемы "Крой" (CUT), отвечающий за регистрацию пунктов меню
 * и обработку событий, связанных с формированием отчетов в desktop-приложении.
 *
 * <p>
 * Модуль реализует интерфейс {@link Module} и интегрируется в общий контроллер
 * приложения через метод {@link #registerModule(MainController)}.
 * Он добавляет элементы меню, а также регистрирует действия для их обработки.
 * </p>
 *
 * <p>
 * На данный момент модуль поддерживает:
 * <ul>
 *     <li>открытие диалога "Маршрутные листы" ({@link RouteListDialogCut});</li>
 *     <li>зарезервированную точку для будущего отчета "Накопительная ведомость".</li>
 * </ul>
 * </p>
 */
public class CutModule implements Module {

    /**
     * Главный контроллер приложения, через который модуль взаимодействует с UI.
     */
    private MainController controller;

    /**
     * Главное Swing-окно приложения, используемое как родитель для модальных диалогов.
     */
    private JFrame ownerFrame;

    /** Пункт меню для вызова отчёта "Маршрутные листы". */
    private final JMenuItem menuCutRouteList = new JMenuItem("Маршрутные листы");

    /** Пункт меню для вызова отчёта "Накопительная ведомость". */
    private final JMenuItem menuStatement = new JMenuItem("Накопительная ведомость");

    /**
     * Инициализация модуля: загрузка контроллера, установка родительского окна,
     * регистрация пунктов меню и обработчиков событий.
     *
     * @param mainController главный контроллер приложения
     */
    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = controller.getMainForm();
        registerMenu();
        registerMenuEvents();
    }

    /**
     * Регистрирует пункты меню модуля в общем навигационном меню системы.
     * <p>
     * На данный момент добавляется один пункт: "Маршрутные листы".
     * </p>
     */
    @Override
    public void registerMenu() {
        controller.addModuleMenu(new SectionMenu(MarchSection.CUT_ROUTELIST, menuCutRouteList));
        // Возможность добавления других пунктов меню в будущем.
    }

    /**
     * Регистрирует обработчики действий для элементов меню.
     * <p>
     * При выборе пункта "Маршрутные листы" открывается модальный диалог
     * {@link RouteListDialogCut}. Второй пункт оставлен как точка расширения.
     * </p>
     */
    @Override
    public void registerMenuEvents() {
        menuCutRouteList.addActionListener(e -> {
            RouteListDialogCut dialog = new RouteListDialogCut(ownerFrame);
            dialog.setModal(true);
            dialog.setVisible(true);
        });

        menuStatement.addActionListener(e -> {
            // Точка расширения для будущего отчета.
        });
    }
}