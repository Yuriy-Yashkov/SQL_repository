package by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Andy 20.12.2017.
 */
public class ImagePanelMouseListener extends MouseAdapter {

    private UCImagePanel panel;
    private ImagePanelCallBack callBack;
    private Color hoverColor = new Color(245, 114, 34);

    public ImagePanelMouseListener(UCImagePanel panel, ImagePanelCallBack callBack) {
        this.callBack = callBack;
        this.panel = panel;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        callBack.onEvent(panel);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        if (!panel.isSelected()) {
            panel.setBorder(BorderFactory.createMatteBorder(
                    1, 1, 5, 1, hoverColor));
        }
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        if (!panel.isSelected()) {
            panel.setBorder(BorderFactory.createMatteBorder(
                    1, 1, 5, 1, Color.ORANGE));
        }
    }
}
