package by.gomel.freedev.ucframework.uccore.viewport.frameviewport;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameControl;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameRegion;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IButtonControl;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseActiveDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.iframe.BaseActiveInternalFrame;
import by.gomel.freedev.ucframework.ucswing.iframe.BaseInternalFrame;
import by.gomel.freedev.ucframework.ucswing.iframe.ICanCloseWindow;
import by.march8.ecs.MainController;

import java.awt.*;

/**
 * Модель области просмотра ФРЕЙМ
 * <br/> Инициализируется параметром типа MarchWindowType, определяющим стиль окна
 * <br/> Содержит интерфейсы для доступа к методам и компонентам создаваемой формы
 * <br/>Created by Andy on 21.12.14.
 */
public class FrameViewPort {
    private MainController controller;
    private Object frame = null;
    private GridViewPort gridViewPort;
    private IActiveFrameRegion frameRegion = null;
    private IActiveFrameControl frameControl = null;
    private IButtonControl buttonControl = null;
    private IReference reference = null;
    private MarchWindowType windowType;


    public FrameViewPort(MainController controller, MarchWindowType windowType) {
        init(controller, windowType);
    }

    public FrameViewPort(MainController controller, MarchWindowType windowType, IReference reference) {
        init(controller, windowType);
        this.reference = reference;
    }

    private void init(MainController controller, MarchWindowType windowType) {
        this.windowType = windowType;
        this.controller = controller;
        frame = getViewPort(windowType);
        // Получили интерфейс регионов фрейма
        frameRegion = (IActiveFrameRegion) frame;
        frameControl = (IActiveFrameControl) frame;
        if (frame instanceof IButtonControl) {
            buttonControl = (IButtonControl) frame;
        }
    }

    private Object getViewPort(MarchWindowType windowType) {
        switch (windowType) {
            case INTERNALFRAME:
                return new BaseActiveInternalFrame(controller);
            case DIALOG:
                return new BaseActiveDialog(controller);
            case DIALOGSIMPLE:
                return new BaseActiveDialog(controller);
            default:
                return new BasePickDialog(controller);
        }
    }

    /**
     * Возвращает форму как Object
     */
    public Object getFrame() {
        return frame;
    }

    /**
     * Возвращает интерфейс доступа к областям формы
     */
    public IActiveFrameRegion getFrameRegion() {
        return frameRegion;
    }

    /**
     * Возвращает интерфейс доступа к методам управления поведением формы
     */
    public IActiveFrameControl getFrameControl() {
        return frameControl;
    }

    /**
     * Возвращаает табличную область просмотра типа GridViewPort
     */
    public GridViewPort getGridViewPort() {
        return gridViewPort;
    }

    /**
     * Устанавливает Табличную область просмотра типа GridViewPort
     */
    public void setGridViewPort(GridViewPort viewPort) {
        gridViewPort = viewPort;
        frameRegion.getCenterContentPanel().add(viewPort, BorderLayout.CENTER);
    }

    /**
     * Метод для обновления области просмотра
     */
    public void updateContent() {
        if (gridViewPort != null) {
            gridViewPort.updateViewPort();
            frameRegion.getToolBar().updateButton(gridViewPort.getDataModel().size());
        }
    }

    /**
     * Метод возвращает интерфейс доступа к ключевым JButton управления формой
     */
    public IButtonControl getButtonControl() {
        return buttonControl;
    }

    /**
     * Метод возвращает интерфейс доступа к справочнику создавшему данный вьюпорт
     */
    public IReference getReference() {
        return reference;
    }

    /**
     * Возвращает ссылку на главный контроллер приложения
     */
    public MainController getController() {
        return controller;
    }

    public void addCloseEventListener(ICanCloseWindow event) {
        if (windowType == MarchWindowType.INTERNALFRAME) {
            ((BaseInternalFrame) frame).addCloseEvent(event);
        }
    }
}
