package by.gomel.freedev.ucframework.ucswing.dialog;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameControl;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameRegion;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IButtonAction;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IButtonControl;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andy on 21.12.14.
 * Класс-шаблон Диалоговой формы с необходимыми интерфейсами для доступа к
 * регионам и элементам управления формы
 */
public class BaseActiveDialog extends BaseDialog implements IActiveFrameRegion, IActiveFrameControl, IButtonControl {
    protected IButtonAction buttonAction = null;
    /**
     * Панель инструментов
     */
    private UCToolBar toolBar = new UCToolBar();

    /**
     * Конструктор класса инициализируется главным контроллером
     *
     * @param controller ссылка на главный контроллер приложения
     */
    public BaseActiveDialog(MainController controller) {
        super(controller, new Dimension(900, 500));
        initFrame();
    }

    /**
     * Метод инициализации компонентов формы
     */
    private void initFrame() {
        toolBar.registerHint(controller);
        panelTop.add(toolBar, BorderLayout.NORTH);
        btnCancel.addActionListener(e -> {
            if (buttonAction != null) {
                if (buttonAction.canCancel()) {
                    modalResult = false;
                    setVisible(false);
                }
            } else {
                modalResult = false;
                setVisible(false);
            }
        });
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
        setVisible(true);
    }

    @Override
    public boolean showModalFrame() {
        return false;
    }

    @Override
    public void closeFrame() {
        setVisible(false);
    }

    @Override
    public void setFrameSize(Dimension dimension) {
        resizeFrame(dimension);
    }

    @Override
    public JPanel getTopContentPanel() {
        return panelTop;
    }

    @Override
    public JPanel getCenterContentPanel() {
        return panelCenter;
    }

    @Override
    public JPanel getBottomContentPanel() {
        return panelBottom;
    }

    @Override
    public JPanel getButtonPanel() {
        return panelButton;
    }

    @Override
    public UCToolBar getToolBar() {
        return toolBar;
    }

    @Override
    public JButton getOkButton() {
        return btnSave;
    }

    @Override
    public JButton getCancelButton() {
        return btnCancel;
    }

    public void setButtonAction(final IButtonAction buttonAction) {
        this.buttonAction = buttonAction;
    }
}
