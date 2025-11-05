package by.march8.ecs.application.shell.general.uicontrol;

import by.gomel.freedev.ucframework.ucswing.IFrameButton;
import by.march8.ecs.MainController;

import javax.swing.*;
import java.awt.*;

/** Панель кнопок открытых окон(нижняя часть главного окна)
 * Created by Andy on 03.11.2014.
 */
public class IFrameListPane extends JPanel {

    private MainController controller;

    public IFrameListPane(MainController mainController) {
        controller = mainController;
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(2);
        layout.setVgap(2);
        setLayout(layout);
        setPreferredSize(new Dimension(0, 0));
        JButton btnService = new JButton("");
        btnService.setPreferredSize(new Dimension(30, 30));
        btnService.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/cascade.png", "SRV"));
        add(btnService);
        btnService.addActionListener(e -> cascade(controller.getDesktop()));
    }

    private void cascade(JDesktopPane desktopPane) {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        if (frames.length == 0) return;

        cascade(frames, desktopPane.getBounds(), 24);
    }

    private void cascade(JInternalFrame[] frames, Rectangle dBounds, int separation) {
        int margin = frames.length * separation + separation;
        int width = dBounds.width - margin;
        int height = dBounds.height - margin;
        for (int i = 0; i < frames.length; i++) {
            frames[i].setBounds(separation + dBounds.x + i * separation,
                    separation + dBounds.y + i * separation,
                    width, height);
            controller.setActiveInternalFrame(frames[i]);
        }
    }

    public void addWindow(final IFrameButton iFrameButton) {
        add(iFrameButton);
        activateButton(iFrameButton.getFrame());
        resize();
    }

    public void resize() {
        Component[] components = this.getComponents();
        int maxButtonWidth = 200;
        int mainFrameWidth = this.getWidth();
        int outer = 50;
        int currentButtonWidth = 0;
        if (components.length == 1) {
            this.setPreferredSize(new Dimension(0, 0));
        } else {
            this.setPreferredSize(new Dimension(0, 34));
            currentButtonWidth = (mainFrameWidth - outer) / components.length;
            if (currentButtonWidth > maxButtonWidth) {
                currentButtonWidth = maxButtonWidth;
            }
        }

        for (final Component component : components) {
            if (component instanceof IFrameButton) {
                component.setPreferredSize(new Dimension(currentButtonWidth, 26));
            }
        }

    }

    public void activateButton(final JInternalFrame frame) {
        Component[] components = this.getComponents();
        for (final Component component : components) {
            if (component instanceof IFrameButton) {
                JInternalFrame iFrame = ((IFrameButton) component).getFrame();
                if (frame != iFrame) {
                    ((IFrameButton) component).setSelected(false);
                } else {
                    ((IFrameButton) component).setSelected(true);
                }
            }
        }
    }

    public void deleteAllButtons() {
        Component[] components = this.getComponents();
        for (Component component : components) {
            if (component instanceof IFrameButton) {
                ((IFrameButton) component).getFrame().dispose();
                ((IFrameButton) component).setFrame(null);
                this.remove(component);
                this.updateUI();
            }
        }
        resize();
    }

    public void deleteButton(final JInternalFrame internalFrame) {
        Component[] components = this.getComponents();
        for (Component component : components) {
            if (component instanceof IFrameButton) {
                JInternalFrame iFrame = ((IFrameButton) component).getFrame();
                if (internalFrame == iFrame) {
                    ((IFrameButton) component).setFrame(null);
                    iFrame.dispose();
                    this.remove(component);
                    this.updateUI();
                }
            }
        }
        resize();
    }
}
