package by.march8.ecs.application.shell.general.uicontrol;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;

import javax.swing.*;
import java.awt.*;

/**
 * Главное меню.
 *
 * @author andy-windows
 */
public class GeneralMenuBar extends JMenuBar {

    /**
     * Меню ФАЙЛ.
     */
    public static final JMenu MENU_FILE = new JMenu("Файл");

    /**
     * Меню СПРАВОЧНИКИ.
     */
    public static final JMenu MENU_REFERENCE = new JMenu("Справочники");

    /**
     * Меню Журналы.
     */
    public static final JMenu MENU_JOURNAL = new JMenu("Журналы");

    /**
     * Меню Отчеты.
     */
    public static final JMenu MENU_REPORTS = new JMenu("Отчеты");

    /**
     * Меню ПРОИЗВОДСТВО.
     */
    public static final JMenu MENU_PROTUCTION = new JMenu("Производство");

    /**
     * Меню Склад.
     */
    public static final JMenu MENU_STORAGE = new JMenu("Склад");

    /**
     * Меню Сбыт.
     */
    public static final JMenu MENU_SALES = new JMenu("Сбыт");

    /**
     * Меню Настройки.
     */
    public static final JMenu MENU_SETTINGS = new JMenu("Настройки");

    /**
     * Меню О программе.
     */
    public static final JMenu MENU_ABOUT = new JMenu("?");


    /**
     * Пункт меню для тестов
     */
    public static final JMenu MENU_TECH = new JMenu("Тестирование");

    /**
     * Пункт меню учёт
     */
    public static final JMenu MENU_ACCOUNTING = new JMenu("Учёт");

    /**
     * Пункт меню хэо
     */
    public static final JMenu MENU_ART = new JMenu("ХЭО");
    private final MainController controller;

    public Component menuGlue = Box.createHorizontalGlue();
    /**
     * The mi help.
     */
    JMenuItem miHelp = new JMenuItem("Помощь");

    /**
     * The mi about.
     */
    JMenuItem miAbout = new JMenuItem("О программе");
    JLabel lblMinimise = new JLabel();
    JLabel lblMaximise = new JLabel();
    JLabel lblClose = new JLabel();
    /**
     * The mi exit.
     */
    private final JMenuItem miExit = new JMenuItem("Выход");
    /**
     * The mi relogin.
     */
    private final JMenuItem miRelogin = new JMenuItem("Сменить пользователя");

    /**
     * Instantiates a new general menu bar.
     */
    public GeneralMenuBar(MainController controller) {

        this.controller = controller;
        this.add(MENU_FILE);
        this.add(MENU_REFERENCE);
        this.add(MENU_ACCOUNTING);
        this.add(MENU_ART);
        this.add(MENU_JOURNAL);
        this.add(MENU_REPORTS);
        this.add(MENU_PROTUCTION);
        this.add(MENU_STORAGE);
        this.add(MENU_SALES);
        this.add(MENU_SETTINGS);
        this.add(MENU_ABOUT);
        this.add(MENU_TECH);
        this.add(menuGlue);

//        lblMinimise.setIcon(new ImageIcon("./Img/mdi_icon.png", "New"));
//        lblMaximise.setIcon(new ImageIcon("./Img/mdi_restore.png", "New"));
//        lblClose.setIcon(new ImageIcon("./Img/mdi_close.png", "New"));

        MENU_FILE.setToolTipText("Главное меню.");
        MENU_REFERENCE.setToolTipText("Необходимые справочники");
        MENU_ART.setToolTipText("Художественно-экспериментальный отдел");
        MENU_JOURNAL.setToolTipText("Журналы сводной отчетности");
        MENU_REPORTS
                .setToolTipText("Операции формирования базовой отчетности ");
        MENU_PROTUCTION
                .setToolTipText("Операции и отчетность отдела производства");
        MENU_STORAGE.setToolTipText("Операции и отчетность отдела склада");
        MENU_SALES.setToolTipText("Операции и отчетность отдела продаж");
        MENU_SETTINGS
                .setToolTipText("Настройки программы и система администрирования");
        MENU_ABOUT.setToolTipText("Справочная информация о программме");
        MENU_TECH.setToolTipText("Содержит тестовые и отладочные приложения");

        MENU_FILE.add(miRelogin);
        MENU_FILE.add(miExit);

        MENU_ABOUT.add(miHelp);
        MENU_ABOUT.add(miAbout);

        initEvents();
        // Заполняем коллекцию главного контроллера корневыми пунктами меню
        controller.addModuleMenu(new SectionMenu(MarchSection.REFERENCE, MENU_REFERENCE));
        controller.addModuleMenu(new SectionMenu(MarchSection.ACCOUNTING, MENU_ACCOUNTING));
        controller.addModuleMenu(new SectionMenu(MarchSection.ART, MENU_ART));
        controller.addModuleMenu(new SectionMenu(MarchSection.JOURNAL, MENU_JOURNAL));
        controller.addModuleMenu(new SectionMenu(MarchSection.SALES, MENU_SALES));
        controller.addModuleMenu(new SectionMenu(MarchSection.PRODUCTION, MENU_PROTUCTION));
        controller.addModuleMenu(new SectionMenu(MarchSection.STORAGE, MENU_STORAGE));
        controller.addModuleMenu(new SectionMenu(MarchSection.REPORTS, MENU_REPORTS));
        controller.addModuleMenu(new SectionMenu(MarchSection.SETTINGS, MENU_SETTINGS));
        controller.addModuleMenu(new SectionMenu(MarchSection.TECH, MENU_TECH));
    }

    /**
     * Регистрация событий статичных элементов меню
     */
    private void initEvents() {
        miExit.addActionListener(e -> controller.applicationTerminate());
        miRelogin.addActionListener(e -> controller.changeUser());
    }

}

