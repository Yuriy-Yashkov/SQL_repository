package by.march8.ecs.application.modules.nsi;

import by.gomel.freedev.ucframework.uccore.interfaces.ModuleSet;
import by.march8.ecs.MainController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NsiModuleSet implements ModuleSet {

    /**
     * Этот метод отвечает за регистрацию конкретных модулей (например, NsiModule, ReferencesModule) в контроллере.
     * @param controller главный контроллер приложения.
     */
    @Override
    public void initialModuleSet(MainController controller) {
        log.info("Работает класс! Регистрация конкретных модулей НСИ");
        NsiModule nsiModule = new NsiModule(); // Вкладка "Проверка ставок НДС" в меню -> "НСИ"
        nsiModule.registerModule(controller);

        ReferencesModule referencesModule = new ReferencesModule(); // "Справочники" в меню -> "НСИ"
        referencesModule.registerModule(controller);
    }
}
