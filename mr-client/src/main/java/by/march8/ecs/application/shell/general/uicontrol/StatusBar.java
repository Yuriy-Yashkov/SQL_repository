package by.march8.ecs.application.shell.general.uicontrol;

import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.WorkSession;
import by.march8.entities.unknowns.UserCompatibility;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Элемент пользовательского интерфейса - панель статуса. Содержит подпанели:
 * подсказки, выпадающий список открытых окон, шкалы прогресса(не реализовано),
 * информации о текущей сессии
 *
 * @author andy-linux
 */
public class StatusBar extends JPanel {

    /**
     * The lbl session.
     */
    private JLabel lblSession = null;
    private JPanel panelSession = new JPanel(null);
    private JLabel hintLabel;

    /**
     * Instantiates a new status bar.
     */
    public StatusBar(MainController mainController) {
        super(null);
        BorderLayout layout = new BorderLayout();
        layout.setHgap(10);
        layout.setVgap(10);
        this.setLayout(layout);
        this.setPreferredSize(new Dimension(0, 20));
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));

        hintLabel = new JLabel("");
        hintLabel.setOpaque(true);
        hintLabel.setFont(new Font("monospace", Font.PLAIN, 12));

        this.add(hintLabel, BorderLayout.WEST);

        JPanel panelControl = new JPanel(null);
        panelControl.setPreferredSize(new Dimension(404, 20));
        panelControl.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelControl.setOpaque(true);
        panelControl.add(prepareExceptionPanel());
        panelControl.add(prepareSessionPanel());

        this.add(panelControl, BorderLayout.EAST);
    }

    private JPanel prepareSessionPanel() {
        lblSession = new JLabel("");
        lblSession.setHorizontalAlignment(SwingConstants.LEFT);
        lblSession.setFont(this.getFont().deriveFont(10.0f));
        lblSession.setBounds(0, 0, 200, 20);

        panelSession.setOpaque(true);
        panelSession.setPreferredSize(new Dimension(200, 20));

        panelSession.add(lblSession);
        panelSession.setBounds(202, 0, 200, 20);
        return panelSession;
    }

    private JPanel prepareExceptionPanel() {
        // Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        final JPanel panelException = new JPanel(null);
        final JLabel lblException = new JLabel(new ImageIcon(MainController.getRunPath() + "/Img/check_.png"));
        lblException.setFont(this.getFont().deriveFont(10.0f));
        lblException.setPreferredSize(new Dimension(16, 16));
        lblException.setVisible(false);

        panelException.setOpaque(true);
        panelException.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelException.setPreferredSize(new Dimension(100, 20));
        panelException.add(lblException);
        lblException.setBounds(150, 0, 16, 16);
        panelException.setBounds(0, 0, 200, 20);

        lblException.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {

            }

            @Override
            public void mousePressed(final MouseEvent e) {
            }

            @Override
            public void mouseReleased(final MouseEvent e) {

            }

            @Override
            public void mouseEntered(final MouseEvent e) {

            }

            @Override
            public void mouseExited(final MouseEvent e) {

            }
        });

        return panelException;
    }

    /**
     * Sets the session.
     *
     * @param session the new session
     */
    public void setSession(final WorkSession session) {
        if (session != null) {
            lblSession.setText("Пользователь : " + session.getUser().getUserLogin());
            UserCompatibility user = session.getUserCompatibility();
            lblSession.setToolTipText("<html>" + user.getUserLogin() + "[" + user.getUserId() + "]<p>"
                    + user.getEmployeeName() + "[" + user.getEmployeeId() + "]<p>"
                    + user.getDepartmentName() + "[" + user.getDepartmentId() + "]</html>");
        } else {
            lblSession.setText("Вход не выполнен");
            lblSession.setToolTipText(null);
        }
    }

    public void setHint(String hintText) {
        hintLabel.setText(hintText);
    }

    public JLabel getLableSession() {
        return lblSession;
    }
}
