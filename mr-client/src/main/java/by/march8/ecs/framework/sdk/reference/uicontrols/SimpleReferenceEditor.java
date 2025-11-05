package by.march8.ecs.framework.sdk.reference.uicontrols;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.api.ISimpleReference;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Общая панель редактирования для бинов имеющих одинаковую структуру:
 * <p/><code>id, name, note</code>
 *
 * @see by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane
 * @see ISimpleReference
 * Created by Andy on 29.09.2014.
 */
public class SimpleReferenceEditor extends EditingPane {

    private JTextField tfName = new JTextField();
    private JTextField tfNote = new JTextField();

    private ISimpleReference source;

    public SimpleReferenceEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(320, 150));
        setLayout(new MigLayout());
        final JLabel lName = new JLabel("Наименование *");
        add(lName, "wrap");
        add(tfName, "width 300:24:300, wrap");
        final JLabel lNote = new JLabel("Примечание");
        add(lNote, "wrap");
        add(tfNote, "width 300:24:300, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setName(tfName.getText());
        source.setNote(tfNote.getText());
        return source;
    }

    @Override
    @SuppressWarnings("all")
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = (ISimpleReference) object;
            source.setId(0);
        } else {
            source = (ISimpleReference) object;

            tfName.setText(source.getName());
            tfNote.setText(source.getNote());
        }
    }

    @Override
    public boolean verificationData() {
        if (tfName.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Наименование \" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }
        return true;
    }
}
