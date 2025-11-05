package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.march8.ecs.MainController;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Splash форма прогресса выполнения
 *
 * @author vova
 */
public class ProgressBar extends JDialog {
    /**
     * Кнопка ОТМЕНА
     */
    private JButton btnCancel;
    private JLabel messageText;


    public ProgressBar(JDialog parent, String message) {
        super(parent, true);

        initFrame(message);
    }

    public ProgressBar(JDialog parent, String message, boolean isCanceled) {
        super(parent, true);
        initFrame(message);
        btnCancel.setEnabled(isCanceled);
    }

    public ProgressBar(JFrame parent, String message) {
        super(parent, true);

        initFrame(message);
    }

    public ProgressBar(JFrame parent, String message, boolean isCanceled) {
        super(parent, true);
        initFrame(message);
        btnCancel.setEnabled(isCanceled);
    }

    private void initFrame(String message) {

        btnCancel = new JButton("Отмена");
        messageText = new JLabel(message);

        if (MainController.isLoad()) {

            setTitle("Ожидайте ...");
            setResizable(false);
            // setUndecorated(true);

            JPanel contentPane = new JPanel(new MigLayout());


            contentPane.add(messageText, "wrap");

            final JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            contentPane.add(progressBar, "width 280:30:280, height 24:24, wrap");

            //btnCancel = new JButton("Отмена");
            btnCancel.setEnabled(false);

            CC componentConstraints = new CC();
            componentConstraints.alignX("center").spanX();
            contentPane.add(btnCancel, componentConstraints);
            setContentPane(contentPane);

            final Toolkit toolKit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = toolKit.getScreenSize();
            final Dimension frameSize = new Dimension(300, 120);

            final Point p = new Point((screenSize.width / 2)
                    - (frameSize.width / 2), (screenSize.height / 2)
                    - (frameSize.height / 2));
            setSize(frameSize);
            setLocation(p);

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        } else {
            setTitle("Ожидайте ...");
            setResizable(false);

            JPanel content = (JPanel) getContentPane();
            content.setBackground(Color.black);

            int width = 475;
            int height = 270;
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screen.width - width) / 2;
            int y = (screen.height - height) / 2;
            //setBounds(x,y,width,height);

            Toolkit tool = Toolkit.getDefaultToolkit();
            JLabel label = new JLabel(new ImageIcon(MainController.getRunPath() + "/Img/MR.gif"));
            content.add(label, BorderLayout.CENTER);
            Color oraRed = new Color(0, 0, 0, 0);
            content.setBorder(BorderFactory.createLineBorder(oraRed, 1));
            setUndecorated(true);

            final Dimension frameSize = new Dimension(475, 270);

            setSize(frameSize);
            setLocation(new Point(x, y));

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    /**
     * Возвращает ссылку на кнопку ОТМЕНА
     *
     * @return кнопка Отмена
     */
    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JLabel getMessageLabel() {
        return messageText;
    }

    public void setMessageText(String text) {
        messageText.setText(text);
        messageText.updateUI();
    }
}