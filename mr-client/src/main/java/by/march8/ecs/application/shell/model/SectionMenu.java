package by.march8.ecs.application.shell.model;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import lombok.Getter;

import javax.swing.*;

/**
 * Модель добавляет соответствие константному перечислителю всех доступных форм их пунктам меню
 * Created by Andy on 30.09.2014.
 *
 * Класс SectionMenu представляет элемент меню, связанный с определённым разделом приложения.
 *  Используется для группировки пунктов меню по логическим секциям системы (например, NSI, REPORTS и т.д.).
 *
 *  Каждый объект хранит информацию о разделе, элементе меню и количестве уровней в названии секции.
 *
 *
 * @see by.gomel.freedev.ucframework.uccore.enums.MarchSection
 */
@Getter
public class SectionMenu {

    /** Раздел приложения, к которому относится данный пункт меню. */
    private MarchSection section;
    /** Конкретный элемент меню, отображаемый в интерфейсе. */
    private JMenuItem menu;
    /** Количество уровней в названии раздела (определяется по символу "_"). */
    private int branchCount;

    public SectionMenu(MarchSection marchSection, JMenuItem menuObject) {
        section = marchSection;
        menu = menuObject;
        String[] branch = marchSection.name().split("_");
        branchCount = branch.length;
    }

}
