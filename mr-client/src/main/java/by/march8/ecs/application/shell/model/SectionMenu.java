package by.march8.ecs.application.shell.model;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import lombok.Getter;

import javax.swing.*;

/**
 * Модель добавляет соответствие константному перечислителю всех доступных форм их пунктам меню
 * Created by Andy on 30.09.2014.
 *
 * @see by.gomel.freedev.ucframework.uccore.enums.MarchSection
 */
@Getter
public class SectionMenu {

    private MarchSection section;
    private JMenuItem menu;
    private int branchCount;

    public SectionMenu(MarchSection marchSection, JMenuItem menuObject) {
        section = marchSection;
        menu = menuObject;
        String[] branch = marchSection.name().split("_");
        branchCount = branch.length;
    }

}
