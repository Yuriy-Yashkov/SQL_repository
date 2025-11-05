package by.march8.ecs.application.modules.references.materals.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.framework.helpers.FormatUtils;
import by.march8.entities.materials.CanvasWeave;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**Панель редактирования набивок полотна
 * @see by.march8.entities.materials.CanvasWeave
 * @see by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane
 * @author andy-linux
 *
 */
public class CanvasWeaveEditor extends EditingPane {

    private static final long serialVersionUID = 5275094235059636482L;
    private final JFormattedTextField tfCode = new JFormattedTextField(
            FormatUtils.getCustomFormat("###"));
    private final JTextField tfName = new JTextField();
    private CanvasWeave source = null;

    public CanvasWeaveEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(320, 140));
        setLayout(new MigLayout());

        JLabel lCode = new JLabel("Код");
        add(lCode, "wrap");
        add(tfCode, "width 50:20:150, wrap");
        JLabel lName = new JLabel("Наименование *");
        add(lName, "wrap");
        add(tfName, "width 300:20:300, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setCode((Integer) tfCode.getValue());
        source.setName(tfName.getText().trim());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            this.source = new CanvasWeave();
            tfCode.setValue(0);
            tfName.setText("Новый вид набивки");
        } else {
            this.source = (CanvasWeave) object;

            tfCode.setValue(this.source.getCode());
            tfName.setText(this.source.getName());
        }
    }

    @Override
    public boolean verificationData() {
        if (tfName.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Наименование набивки\" не должно быть пустым",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }
        return true;
    }

}
