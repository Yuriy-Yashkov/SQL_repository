package by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces;

/**
 * @author Andy 20.01.2016.
 */

/**
 * Интерфейс позволяет перехватить нажатия по кнопкам диалоговых окон СОХРАНИТЬ/ВЫБРАТЬ/ОТМЕНА
 */
public interface IButtonAction {
    /**
     * Нажатие по кнопке СОХРАНИТЬ/ДОБАВИТЬ/ВЫБРАТЬ разрешено
     *
     * @return если вернул true - нажатие кнопки будет обработано
     */
    boolean canSave();

    /**
     * Нажатие по кнопке ОТМЕНА/ЗАКРЫТЬ разрешено
     *
     * @return если вернул true - нажатие кнопки будет обработано
     */
    boolean canCancel();
}
