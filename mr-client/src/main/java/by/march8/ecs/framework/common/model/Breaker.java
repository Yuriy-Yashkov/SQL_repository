package by.march8.ecs.framework.common.model;

/**
 * Класс-хелпер для внедрения в ответвленные потоки для последующего управления ходом выполнения ))
 *
 * Created by Andy on 11.03.2015.
 */
public class Breaker {
    private boolean isBreak = false;

    public boolean isBreak() {
        return isBreak;
    }

    public void setBreak(final boolean isBreak) {
        this.isBreak = isBreak;
    }
}
