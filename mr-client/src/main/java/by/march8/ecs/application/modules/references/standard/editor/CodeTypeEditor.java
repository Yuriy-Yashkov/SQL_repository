package by.march8.ecs.application.modules.references.standard.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.standard.StandardType;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Панель редактирования типов классификационных стандартов
 *
 * Created by Andy on 21.11.14.
 */
public class CodeTypeEditor extends EditingPane {

    private JTextField tfName = new JTextField();
    private JTextField tfFullName = new JTextField();
    private JTextField tfNote = new JTextField();

    private StandardType source = null;

    public CodeTypeEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(320, 180));
        setLayout(new MigLayout());
        add(new JLabel("Наименование *"), "wrap");
        add(tfName, "width 300:24:300, wrap");
        add(new JLabel("Полное наименование"), "wrap");
        add(tfFullName, "width 300:24:300, wrap");
        add(new JLabel("Примечание"), "wrap");
        add(tfNote, "width 300:24:300, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setShortName(tfName.getText());
        source.setFullName(tfFullName.getText());
        source.setNote(tfNote.getText());
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new StandardType();
        } else {
            source = (StandardType) object;
        }
        defaultFillingData();
    }

    @Override
    public boolean verificationData() {
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
        tfName.setText(source.getShortName());
        tfFullName.setText(source.getFullName());
        tfNote.setText(source.getNote());
    }
}
