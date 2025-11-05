package by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces;

import javax.swing.*;

/**
 * Интерфейс доступа к ключевым JButton-s управления формой
 * Created by Andy on 21.12.14.
 */
public interface IButtonControl {
    /**
     * Возвращает кнопку OK
     */
    JButton getOkButton();

    /**
     * Возвращает кнопку CANCEL
     */
    @SuppressWarnings("unused")
    JButton getCancelButton();


    JPanel getButtonPanel();
}
