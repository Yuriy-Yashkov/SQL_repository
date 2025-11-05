package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.IUCComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Контроллер UC компонентов содержит необходимые методы и свойства
 * для групповой обработки и управления данными UC компонентов
 * Created by andy on 3/21/15.
 */
public class UCController {
    private Container container;
    private UCBaloon baloon;
    private ArrayList<IUCComponent> components;

    public UCController(Container container) {
        this.container = container;
        baloon = new UCBaloon(container);
        components = new ArrayList<>();
    }

    public void updateComponentList(Component component) {
        components.clear();
        getComponents(component);
        //Collections.sort(components, new SortByTabOrder());
    }

    /**
     * Метод рекурсивно получает коллекцию интерфейсов UC компонентов в контейнере
     *
     * @param component контейнер
     */
    public void getComponents(Component component) {
        if (component instanceof IUCComponent) {
            components.add((IUCComponent) component);
            ((IUCComponent) component).setUCController(this);
        } else if (component instanceof JPanel) {
            for (Component child : ((Container) component).getComponents()) {
                getComponents(child);
            }
        }
    }

    /**
     * Метод проводит проверку введенных данных у компонентов
     *
     * @return флаг проведенной проверки
     */
    public boolean verificationDataIsCorrect() {
        for (IUCComponent item : components) {
            if (item.isRequired()) {
                if (!item.isVerificationComplete()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Метод проводит поиск изменений в введенных данных
     *
     * @return true ели данные компонентов изменены
     */
    public boolean verificationDataHasChanged() {
        for (IUCComponent item : components) {
            if (item.isModified()) {
                return true;
            }
        }
        return false;
    }

    public UCBaloon getBaloon() {
        return baloon;
    }
}
