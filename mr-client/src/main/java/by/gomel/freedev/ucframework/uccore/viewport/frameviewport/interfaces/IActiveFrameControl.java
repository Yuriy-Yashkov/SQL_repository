package by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces;

import java.awt.*;

/**
 * Интерфейс доступа к управлению формой
 * Created by Andy on 20.12.14.
 */
public interface IActiveFrameControl {
    /**
     * Возвращает текст заголовка формы
     */
    @SuppressWarnings("unused")
    String getTitleFrame();

    /**
     * Устанавливает текст в заголовке формой
     */
    void setTitleFrame(String title);

    /**
     * Показывает форму
     */
    void showFrame();

    /**
     * Показывает форму модально, если реализовано
     */
    boolean showModalFrame();

    /**
     * Закрывает форму
     */
    @SuppressWarnings("unused")
    void closeFrame();

    /**
     * Изменяет размер формы отображения (только JDialog и его наследники )
     * @param dimension
     */
    void setFrameSize(Dimension dimension);
}
