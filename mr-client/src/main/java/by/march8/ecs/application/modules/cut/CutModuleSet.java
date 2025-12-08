package by.march8.ecs.application.modules.cut;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;
import lombok.extern.slf4j.Slf4j;

/**
 * Этот метод отвечает за регистрацию конкретных модулей (например, CutModule, NsiModule, ReferencesModule) в контроллере.
 * @param controller главный контроллер приложения.
 */
@Slf4j
public class CutModuleSet implements ModuleSet {

    @Override
    public void initialModuleSet(MainController controller) {
        log.info("Регистрация конкретных модулей \"Крой\"");

        CutModule cutModule = new CutModule(); // Вкладка "Маршрутные листы" в меню -> "Крой"
        cutModule.registerModule(controller);
    }
}
