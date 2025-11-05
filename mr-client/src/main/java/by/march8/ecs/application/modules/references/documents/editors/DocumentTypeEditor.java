package by.march8.ecs.application.modules.references.documents.editors;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.ComponentConfiguration;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.documents.DocumentTypeEntity;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by suvarov on 04.12.14.
 */
public class DocumentTypeEditor extends EditingPane {
    private static final long serialVersionUID = -8203716759026137796L;
    private final JTextField tfName = new JTextField();
    private final JTextArea tfNote = new JTextArea();
    private final JLabel lName = new JLabel("*Тип документа");
    private final JLabel lNote = new JLabel("Примечание");
    private DocumentTypeEntity source = new DocumentTypeEntity();

    public DocumentTypeEditor(final IReference reference) {
        super(reference);
        setLayout(new MigLayout());
        setPreferredSize(new Dimension(400, 150));
        ComponentConfiguration.fontPatch(lName, lNote);
        final JScrollPane saNote = new JScrollPane(tfNote);
        ComponentConfiguration.textAreaPatch(tfNote, saNote);
        add(lName, "wrap");
        add(tfName, "w 400::350, wrap");
        add(lNote, "wrap");
        add(saNote, "w 400::350, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setName(tfName.getText());
        source.setNote(tfNote.getText());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            this.source = new DocumentTypeEntity();
            tfName.setText("");
            tfNote.setText("");
        } else {
            this.source = (DocumentTypeEntity) object;
            tfName.setText(this.source.getName());
            tfNote.setText(this.source.getNote());
        }
    }

    @Override
    public boolean verificationData() {

        if (tfName.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Тип документа\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }
        return true;
    }
}
