package by.march8.ecs.framework.common;

import by.gomel.freedev.ucframework.ucswing.uicontrols.ProgressBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.mode.BreakableThread;
import by.march8.ecs.framework.common.model.Breaker;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Andy on 21.10.2014.
 */
public abstract class BackgroundTask<T> extends SwingWorker<Object, Object> {
    protected ArrayList<T> result;
    protected Object resultObject;
    private ProgressBar pb = null;
    private BreakableThread event = null;

    public BackgroundTask(final String messageText) {
        if (!Settings.isNoGUI) {
            pb = new ProgressBar(MainController.parentFrame, messageText);
        }
    }

    public BackgroundTask(final String messageText, boolean isBreakable) {
        if (!Settings.isNoGUI) {
            pb = new ProgressBar(MainController.parentFrame, messageText);

            if (isBreakable) {
                pb.getBtnCancel().setEnabled(true);
                pb.getBtnCancel().addActionListener(e -> cancelProcess());
            }
        }
    }

    public BackgroundTask(final String messageText, BreakableThread event) {
        this(messageText, true);
        this.event = event;
    }

    public BackgroundTask(final String messageText, final Breaker breaker) {
        if (!Settings.isNoGUI) {
            pb = new ProgressBar(MainController.parentFrame, messageText, true);
            pb.getBtnCancel().addActionListener(e -> breaker.setBreak(true));
        }
    }

    public void cancelProcess() {

        if (event != null) {
            event.doBreak();
        }

        cancel(true);
    }

    @Override
    protected void done() {
        if (!Settings.isNoGUI) {
            pb.dispose();
        }

    }

    public ArrayList<T> getResult() {
        return result;
    }

    public void runTask() {
        super.run();
        if (!Settings.isNoGUI) {
            //pb.setVisible(true);
        }
    }

    public void executeTask() {
        super.execute();
        if (!Settings.isNoGUI) {
            pb.setVisible(true);
        }
    }

    public void setText(String text) {
        getProgressLabel().setText(text);
    }

    public Object getResultObject() {
        return resultObject;
    }

    public JLabel getProgressLabel() {
        return pb.getMessageLabel();
    }

    public void hideProgressBar() {
        pb.dispose();
    }

    public ProgressBar getProgressBar() {
        return pb;
    }
}
