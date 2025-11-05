package by.gomel.freedev.ucframework.uccore.modes.interfaces;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;

/**
 * Интерфейс позволяет получить доступ к контролльным блокам справочника
 * Created by Andy on 21.12.14.
 */
public interface IReferenceRegion {
    /**
     * Возвращает панель редактирования справочника
     *
     * @return панель редактирования
     */
    EditingPane getEditingPane();

    /**
     * Возвращает панель управления справочника
     *
     * @return панель управления
     */
    ControlPane getControlPane();

    /**
     * Метод устанавливает панель управления справочнику
     */
    void setControlPane(ControlPane controlPane);
}
