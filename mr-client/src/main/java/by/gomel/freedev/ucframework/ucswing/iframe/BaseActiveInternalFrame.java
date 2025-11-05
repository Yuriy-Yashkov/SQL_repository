package by.gomel.freedev.ucframework.ucswing.iframe;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameControl;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameRegion;
import by.march8.ecs.MainController;

import java.awt.*;

/**
 * Базовый класс внутреннего окна содержащий интерфейсы доступа к регионам
 * и элементам управления формы. Основной класс для построения режимов приложения
 * Created by Andy on 21.12.14.
 */
public class BaseActiveInternalFrame extends BaseInternalFrame implements IActiveFrameRegion, IActiveFrameControl {

    public BaseActiveInternalFrame(MainController controller) {
        super(controller);
    }

    @Override
    public String getTitleFrame() {
        return getTitle();
    }

    @Override
    public void setTitleFrame(String title) {
        setTitle(title);
    }

    @Override
    public void showFrame() {
        controller.openInternalFrame(this);
    }

    @Override
    public boolean showModalFrame() {
        showFrame();
        return true;
    }

    @Override
    public void closeFrame() {
        controller.closeInternalFrame(this);
    }

    @Override
    public void setFrameSize(Dimension dimension) {

    }


}
