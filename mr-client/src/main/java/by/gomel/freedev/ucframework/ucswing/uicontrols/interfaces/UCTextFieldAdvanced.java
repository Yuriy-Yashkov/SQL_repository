package by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 29.11.2018 - 9:06.
 */
public class UCTextFieldAdvanced extends JPanel {

    private UCTextField textField;
    private JButton btnCommon;

    public UCTextFieldAdvanced() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        textField = new UCTextField();
        btnCommon = new JButton();

        setPreferredSize(new Dimension(120, 20));
        add(textField, BorderLayout.CENTER);
        add(btnCommon, BorderLayout.EAST);
        btnCommon.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnCommon.setPreferredSize(new Dimension(18, 18));
    }

    public UCTextField getTextField() {
        return textField;
    }

    public JButton getButton() {
        return btnCommon;
    }

    public void setValue(String value) {
        textField.setText(value);
    }

    public void setValue(Double value) {
        textField.setValue(value);
    }

    public void setValue(Integer value) {
        textField.setText(String.valueOf(value));
    }
}
