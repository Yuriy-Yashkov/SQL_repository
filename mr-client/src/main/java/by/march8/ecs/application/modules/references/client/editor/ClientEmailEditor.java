package by.march8.ecs.application.modules.references.client.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.contractor.ClientEmailItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ClientEmailEditor extends EditingPane {
    private JTextField tfName = new JTextField();

    private ClientEmailItem source = null;

    public ClientEmailEditor(IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(320, 150));
        setLayout(new MigLayout());
        final JLabel lName = new JLabel("Наименование ");
        add(lName, "wrap");
        add(tfName, "width 300:24:300, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setEmail(tfName.getText());
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new ClientEmailItem();
            source.setClientCode(entityParentId);
        } else {
            source = (ClientEmailItem) object;
        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        tfName.setText(source.getEmail());
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
