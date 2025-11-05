package by.march8.ecs.application.shell.administration.uicontrol;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.admin.FunctionMode;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andy on 18.09.2014.
 * Панель редактирования Функционального режима
 * @see by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane
 */
public class FunctionModeEditor extends EditingPane {

    private final JTextField tfNameEng = new JTextField();
    private final JTextField tfNameRus = new JTextField();
    private final JTextField tfNote = new JTextField();
    private FunctionMode source;

    public FunctionModeEditor(IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(520, 200));
        this.setLayout(new MigLayout());
        JLabel clNameEng = new JLabel("Name eng");
        add(clNameEng, "wrap");
        add(tfNameEng, "width 500:20:500, wrap");
        JLabel clNameRu = new JLabel("Name ru");
        add(clNameRu, "wrap");
        add(tfNameRus, "width 500:20:500, wrap");
        JLabel clNote = new JLabel("Примечание");
        add(clNote, "wrap");
        add(tfNote, "width 500:20:500, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setNameEng(tfNameEng.getText());
        source.setNameRus(tfNameRus.getText());
        source.setNote(tfNote.getText());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new FunctionMode();
            tfNameEng.setText("");
            tfNameRus.setText("");
            tfNote.setText("");
        } else {
            source = (FunctionMode) object;
            tfNameEng.setText(source.getNameEng());
            tfNameRus.setText(source.getNameRus());
            tfNote.setText(source.getNote());
        }
    }

    @Override
    public boolean verificationData() {
        if (tfNameEng.getText().equals("")) {
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Необходимо указать наименование функционального режима [ENG]",
                            "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfNameEng.requestFocusInWindow();
            return false;
        } else {
            tfNameEng.setText(tfNameEng.getText().toUpperCase());
        }

        if (tfNameRus.getText().equals("")) {
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Необходимо указать наименование функционального режима [RU]",
                            "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfNameRus.requestFocusInWindow();
            return false;
        } else {
            tfNameRus.setText(tfNameRus.getText().toUpperCase());
        }

        return true;
    }
}
