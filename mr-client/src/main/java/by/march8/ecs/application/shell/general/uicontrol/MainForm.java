/**
 *
 */
package by.march8.ecs.application.shell.general.uicontrol;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.gomel.freedev.ucframework.ucswing.iframe.BaseInternalFrame;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;

/**
 * Главаная форма программы.
 *
 * @author andy-windows
 * @since 1.0.1
 */
@SuppressWarnings("serial")
public class MainForm extends JFrame {

    /** The frame panel. */
    private JDesktopPane framePanel;
    /** The controller. */
    private MainController controller;


    /**
     * Instantiates a new main frame.
     *
     * @param marchController the march controller
     */
    public MainForm(MainController marchController) {
        super();
        this.controller = marchController;
        initFrame();
        initEventsFrame();
    }

    /**
     * Inits the frame.
     */
    private void initFrame() {

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        this.setContentPane(contentPane);
        final JPanel footherPanel = new JPanel(new BorderLayout());
        //footherPanel.setPreferredSize(new Dimension(0,80));
        footherPanel.add(controller.getIFrameListPane(), BorderLayout.NORTH);
        footherPanel.add(controller.getStatusBar(), BorderLayout.SOUTH);

        contentPane.add(footherPanel, BorderLayout.SOUTH);
        framePanel = new JDesktopPane();
        framePanel.setBackground(Color.gray);
        contentPane.add(framePanel, BorderLayout.CENTER);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(Settings.PROGRAM_NAME);
        this.getInputContext().selectInputMethod(new Locale("ru", "RU"));
        setVisible(true);
    }

    private void resizeForm() {
        // Инструмент для снятия параметров экрана Windows
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolKit.getScreenSize();
        Dimension frameSize = new Dimension(1024, 700);

        Point p = new Point(screenSize.width / 2 - frameSize.width / 2,
                screenSize.height / 2 - frameSize.height / 2);
        setSize(frameSize);
        setLocation(p);
    }

    /**
     * Inits the events frame.
     */
    private void initEventsFrame() {
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (!SystemUtils.applicationIsOutdated()) {
                    controller.applicationTerminate();
                } else {
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                JInternalFrame[] intF = framePanel.getAllFrames();
                for (JInternalFrame anIntF : intF) {
                    ((BaseInternalFrame) anIntF).resizeEvent(getFramePanelSize());
                }
            }
        });
    }

    /**
     * Preset title caption.
     *
     * @param title the title
     */
    private void presetTitleCaption(String title) {
        this.setTitle(title + " : " + Settings.PROGRAM_NAME);
    }

    /**
     * Adds the window.
     *
     * @param frame the frame
     */
    public void addWindow(JInternalFrame frame) {
        framePanel.add(frame);
        setActiveFrame(frame);
    }

    /**
     * Sets the active frame.
     *
     * @param frame the new active frame
     */
    public void setActiveFrame(JInternalFrame frame) {
        framePanel.getDesktopManager().activateFrame(frame);
        presetTitleCaption(frame.getTitle());
    }

    /**
     * Sets the general menu bar.
     *
     * @param menuBar the new general menu bar
     */
    public void setGeneralMenuBar(JMenuBar menuBar) {
        this.setJMenuBar(menuBar);
        resizeForm();
    }

    /**
     * Gets the frame panel size.
     *
     * @return the frame panel size
     */
    public Dimension getFramePanelSize() {
        validate();
        return framePanel.getSize();
    }

    public JDesktopPane getDesktopPane() {
        return framePanel;
    }

}
