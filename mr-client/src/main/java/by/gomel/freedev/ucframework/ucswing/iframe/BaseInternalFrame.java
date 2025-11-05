package by.gomel.freedev.ucframework.ucswing.iframe;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IFrameRegion;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Базовый класс дочерней формы {@link JInternalFrame}.
 * Устаревший.
 *
 * @author andy-windows
 * @since 1.0.1
 */
@SuppressWarnings("all")
public class BaseInternalFrame extends JInternalFrame implements IFrameRegion {

    /**
     * The controller.
     */
    protected MainController controller = null;

    /**
     * The panel content.
     */
    protected JPanel panelContent = null;

    /**
     * The panel top.
     */
    protected JPanel panelTop = null;

    /**
     * The panel bottom.
     */
    protected JPanel panelBottom = null;

    /**
     * The panel center.
     */
    protected JPanel panelCenter = null;

    /**
     * The tool bar.
     */
    protected UCToolBar toolBar = new UCToolBar();
    ;

    /**
     * The panel button.
     */
    protected JPanel panelButton = null;

    /**
     * The btn close.
     */
    protected JButton btnClose = null;

    protected ICanCloseWindow canCloseWindow;


    /**
     * Instantiates a new base internal frame.
     *
     * @param controller the controller
     */
    public BaseInternalFrame(MainController controller) {
        this.controller = controller;
        this.setResizable(true);
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        panelContent = new JPanel(new BorderLayout());
        this.setContentPane(panelContent);
        initFrame();
        initEvents();
        setVisible(true);
    }

    /**
     * Метод изменяет размер данной формы относительно главной.
     *
     * @param size the size
     */
    public void resizeEvent(Dimension size) {
        setSize(new Dimension(size.width, size.height));
        setLocation(0, 0);
    }

    /**
     * Инициализация компонентов формы
     */
    private void initFrame() {
        panelTop = new JPanel(new BorderLayout());
        toolBar.registerHint(controller);
        panelTop.add(toolBar);

        panelTop.add(toolBar, BorderLayout.NORTH);
        panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());
        panelCenter.setBackground(Color.DARK_GRAY);

        panelBottom = new JPanel(new BorderLayout());
        panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBottom.add(panelButton, BorderLayout.SOUTH);

        panelContent.add(panelTop, BorderLayout.NORTH);
        panelContent.add(panelCenter, BorderLayout.CENTER);
        panelContent.add(panelBottom, BorderLayout.SOUTH);

        btnClose = new JButton("Закрыть");
        btnClose.setPreferredSize(Settings.BUTTON_NORMAL_SIZE);
    }

    /**
     * Инициализация событий компонентов формы
     */
    private void initEvents() {
        btnClose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (canCloseWindow != null) {
                    if (canCloseWindow.canClose(true)) {
                        dispose();
                        controller.closeInternalFrame(getFrame());
                    }
                } else {
                    dispose();
                    controller.closeInternalFrame(getFrame());
                }
            }
        });
    }


    private JInternalFrame getFrame() {
        return this;
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
    public UCToolBar getToolBar() {
        return toolBar;
    }

    public void addCloseEvent(ICanCloseWindow event) {
        canCloseWindow = event;
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                // do something
                if (canCloseWindow.canClose(true)) {
                    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                } else {
                    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }
}
