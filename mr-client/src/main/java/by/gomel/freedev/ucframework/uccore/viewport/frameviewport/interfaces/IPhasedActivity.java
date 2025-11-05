package by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces;

/**
 * Фазирование компонентов. Распределение и перевод инициализации на фазы необходим в случаях, когда
 * пользователь за весь сеанс работы может не воспользоваться редактированием справочника, однако,
 * данные по этому справочнику (вспомогательные OneToMany справочники)  будут загружаться в момент инициализации
 * (конструктор) панели редактирования
 * Created by Andy on 12.08.2015.
 */
public interface IPhasedActivity {
    /**
     * Фаза наступает при вызове метода setEditorPane(EditingPane pane)
     * у компонента BaseEditorDialog.
     */
    void phaseBeforeShowing();
}
