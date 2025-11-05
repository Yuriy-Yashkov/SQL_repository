package by.march8.ecs.application.modules.references.materals.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.materials.YarnComponentType;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author andy-linux
 */
@SuppressWarnings("ALL")
public class YarnTypeEditor extends EditingPane {

    private static final long serialVersionUID = 5275094235059636482L;

    private YarnComponentType source = null;


    private JTextField tfAbbrev = new JTextField();
    private JTextField tfName = new JTextField();


    private JLabel lAbbrev = new JLabel("Шифр *");
    private JLabel lName = new JLabel("Наименование пряжи *");


    public YarnTypeEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(320, 140));
        setLayout(new MigLayout());
        add(lAbbrev, "width 150:20:150, wrap");
        add(tfAbbrev, "width 150:20:150, wrap");
        add(lName, "width 150:20:150, wrap");
        add(tfName, "width 300:20:300, wrap");
    }

    @Override
    public void defaultFillingData() {
        tfAbbrev.setText(source.getAbbreviation());
        tfName.setText(source.getName());
    }

    @Override
    public Object getSourceEntity() {
        source.setAbbreviation(tfAbbrev.getText());
        source.setName(tfName.getText());
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new YarnComponentType();
        } else {
            source = (YarnComponentType) object;
        }
        defaultFillingData();
    }

    @Override
    public boolean verificationData() {

        if (tfAbbrev.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Шифр\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfAbbrev.requestFocusInWindow();
            return false;
        }

        if (tfName.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Наименование набивки\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }
        return true;
    }
}

