package by.march8.ecs.application.modules.references.company.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.company.CompanyDepartment;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 21.11.14.
 */
public class CompanyDepartmentEditor extends EditingPane {

    private JTextField tfName = new JTextField();
    private JTextField tfNote = new JTextField();

    private CompanyDepartment source = null;

    public CompanyDepartmentEditor(IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(320, 150));
        setLayout(new MigLayout());
        final JLabel lName = new JLabel("Наименование");
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
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new CompanyDepartment();
        } else {
            source = (CompanyDepartment) object;
        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        tfName.setText(source.getName());
        tfNote.setText(source.getNote());
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
}
