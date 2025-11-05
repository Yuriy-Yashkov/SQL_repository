package by.march8.ecs.application.modules.references.materals.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.materials.CanvasModifier;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Панель редактирования модификатора полотна
 *
 * @author andy-linux
 * @see by.march8.entities.materials.CanvasModifier
 * @see by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane
 */

public class CanvasModifierEditor extends EditingPane {

    private static final long serialVersionUID = 5275094235059636482L;

    private CanvasModifier source;

    private JTextField tfAbbrev = new JTextField();
    private JTextField tfName = new JTextField();


    public CanvasModifierEditor(final IReference reference) {
        super(reference);
        this.setLayout(new MigLayout());
        setPreferredSize(new Dimension(320, 150));

        JLabel lAbbrev = new JLabel("Аттрибут *");
        add(lAbbrev, "wrap");
        add(tfAbbrev, "width 50:20:150, wrap");
        JLabel lName = new JLabel("Наименование отделки полотна *");
        add(lName, "wrap");
        add(tfName, "width 300:20:300, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setAbbreviation(tfAbbrev.getText().trim());
        source.setName(tfName.getText().trim());
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new CanvasModifier();
            tfAbbrev.setText("XXXXX");
            tfName.setText("Новый вид отделки");
        } else {
            source = (CanvasModifier) object;
            tfAbbrev.setText(source.getAbbreviation());
            tfName.setText(source.getName());
        }
    }

    @Override
    public boolean verificationData() {
        if (tfAbbrev.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Аттрибут\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfAbbrev.requestFocusInWindow();
            return false;
        }

        if (tfName.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Наименование отделки\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }
        return true;
    }

}
