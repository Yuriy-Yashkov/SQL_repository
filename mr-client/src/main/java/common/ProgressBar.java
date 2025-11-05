package common;

import javax.swing.*;

/**
 *
 * @author vova
 */
public class ProgressBar extends JDialog {
    private JProgressBar progress;
    private JLabel mess;
    private String message;

    public ProgressBar(JDialog parent, boolean proc, String message) {
        super(parent, true);
        setTitle("Ожидайте...");
        JPanel contentPane = new JPanel(null);
        progress = new JProgressBar();
        if (!proc) {
            progress.setIndeterminate(true);
        } else {
            progress.setStringPainted(true);
            progress.setMinimum(0);
            progress.setMaximum(100);
            progress.setValue(0);
        }
        progress.setBounds(10, 40, 270, 30);
        contentPane.add(progress);
        mess = new JLabel(message);
        mess.setBounds(5, 10, 250, 20);
        this.message = message;
        contentPane.add(mess);
        setContentPane(contentPane);
        setSize(300, 130);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
    }

    public ProgressBar(JFrame parent, boolean proc, String message) {
        super(parent, true);
        setTitle("Ожидайте...");

        this.message = message;
        JPanel contentPane = new JPanel(null);
        progress = new JProgressBar();
        if (!proc) {
            progress.setIndeterminate(true);
        } else {
            progress.setStringPainted(true);
            progress.setMinimum(0);
            progress.setMaximum(100);
            progress.setValue(0);
        }
        progress.setBounds(10, 40, 270, 30);
        contentPane.add(progress);
        mess = new JLabel(message);
        mess.setBounds(5, 10, 250, 20);
        contentPane.add(mess);
        setContentPane(contentPane);
        setSize(300, 130);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
    }

    public void setProgress(int pr) {
        progress.setValue(pr);
    }

    public void setMaxValue(int pr) {
        progress.setMaximum(pr);
    }

    public void setMessage(String mes) {
        mess.setText(mes);
    }

    public void setMessageValue(String mes) {
        message = mes;
    }

    public void updateValue(final int step) {
        mess.setText(String.format(message, step));
    }
}