package by.gomel.freedev.ucframework.ucswing.dialog;

import by.march8.ecs.MainController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Базовый диалог выбора из списка-таблицы
 *
 * @author andy-linux
 */
public class BasePickDialog extends BaseActiveDialog {

    public BasePickDialog(final MainController controller) {
        super(controller);
        setResizable(true);
        btnSave.setText("Выбрать");
        btnSave.setVisible(true);
        panelButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                {
                    if (buttonAction != null) {
                        if (buttonAction.canSave()) {
                            modalResult = true;
                            setVisible(false);
                        }
                    } else {
                        modalResult = true;
                        setVisible(false);
                    }
                }
            }
        });
    }

    public BasePickDialog(final MainController controller, boolean emptyListener) {
        super(controller);
        setResizable(true);
        btnSave.setText("Выбрать");
        btnSave.setVisible(true);
        panelButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        if (!emptyListener) {
            btnSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    {
                        if (buttonAction != null) {
                            if (buttonAction.canSave()) {
                                modalResult = true;
                                setVisible(false);
                            }
                        } else {
                            modalResult = true;
                            setVisible(false);
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean showModalFrame() {
        setVisible(true);
        return modalResult;
    }

    @Override
    public void setTitleFrame(String title) {
        setTitle("Выбрать из: " + title);
    }


}
