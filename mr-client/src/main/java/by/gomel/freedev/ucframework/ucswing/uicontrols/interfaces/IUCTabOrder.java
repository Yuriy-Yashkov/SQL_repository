package by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces;

/**
 * Created by andy-linux on 3/21/15.
 * Интерфейс определяет для компонента индекс очередности
 * при переходе по клавише TAB.
 * Кроме того проверка используется при проверке валидности содержимого
 * в определенной последовательности
 */
public interface IUCTabOrder {
    /**
     * Возвращает индекс последовательности по TAB
     *
     * @return индекс
     */
    int getTabOrder();

    /**
     * Устанавливает индекс последовательности по TAB
     *
     * @param tabOrder индекс
     */
    void setTabOrder(int tabOrder);
}
