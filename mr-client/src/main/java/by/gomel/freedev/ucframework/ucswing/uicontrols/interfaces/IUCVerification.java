package by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces;

import javax.swing.*;

/**
 * Интерфейс для методов проверки введенных данных
 * Created by andy-linux on 3/21/15.
 */
public interface IUCVerification {

    /**
     * Устанавливает необходимые параметры для компонента.
     * Пока не вызван данный метод поле закрепленное
     * за данным компонентом считается не обязательным для заполнения
     *
     * @param label  текстовая метка компонента
     * @param type   тип содержимого у компонента
     * @param length длина поля
     */
    void setComponentParams(JLabel label, Class<?> type, int length);

    /**
     * Метод возвращает true если этот компонент необходимо проверять
     * Пока не вызван метод setComponentParams поле не считается
     * обязательным для заполнения
     *
     * @return true - поле обязательное для заполнения
     */
    boolean isRequired();

    /**
     * Метод производит проверку введенных данных
     *
     * @return флаг успешной проверки данных
     */
    boolean isVerificationComplete();


    /**
     * Метод возвращает содержимое в виде объекта
     *
     * @return содержимое компонента
     */
    Object getValue();

    /**
     * Метод возвращает заявленную условием длину записи
     *
     * @return количество введенных символов
     */
    int getPresetValueLength();

    /**
     * Метод возвращает дип данных компонента
     *
     * @return тип данных
     */
    Class<?> getValueType();

    /**
     * ВОзвращает ссылку на лэйбл для компонента, если закреплен
     *
     * @return ссылка на текстовую метку компонента
     */
    JLabel getComponentLabel();


}
