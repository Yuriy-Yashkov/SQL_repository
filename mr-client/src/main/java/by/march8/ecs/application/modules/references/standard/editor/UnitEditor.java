package by.march8.ecs.application.modules.references.standard.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.standard.Unit;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Панель редактирования единиц измерений
 *
 * Created by lidashka.
 */
public class UnitEditor extends EditingPane {

    private JTextField tfName = new JTextField();
    private JTextField tfAbbreviation = new JTextField();

    private Unit source = null;

    public UnitEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(380, 140));
        setLayout(new MigLayout());
        add(new JLabel("Аббревиатура *"), "wrap");
        add(tfAbbreviation, "width 350:24:350, wrap");
        add(new JLabel("Наименование *"), "wrap");
        add(tfName, "width 350:24:350, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setAbbreviation(tfAbbreviation.getText());
        source.setName(tfName.getText());
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new Unit();
        } else {
            source = (Unit) object;
        }
        defaultFillingData();
    }

    @Override
    public boolean verificationData() {
        if (tfAbbreviation.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Аббревиатура\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfAbbreviation.requestFocusInWindow();
            return false;
        }
        if (tfName.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Наименование\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }
        return true;
    }

    @Override
    public void defaultFillingData() {
        tfName.setText(source.getName());
        tfAbbreviation.setText(source.getAbbreviation());
    }
}
