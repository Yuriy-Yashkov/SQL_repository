package by.march8.ecs.application.shell.administration.uicontrol;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.admin.FunctionMode;
import by.march8.entities.admin.FunctionalRole;
import by.march8.entities.admin.UserRight;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Andy on 19.09.2014.
 * Панель редактирования функциональной роли
 *
 * @see by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane
 */
public class FunctionalRoleEditor extends EditingPane {

    private final ComboBoxPanel<FunctionMode> cbpFunctionMode;
    private final ComboBoxPanel<UserRight> cbpRight;


    private final JTextField tfNote = new JTextField();

    private FunctionalRole source;
    private FunctionMode functionalMode;
    private UserRight right;


    /**
     * инициализация контролов
     */
    public FunctionalRoleEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(495, 200));
        setLayout(new MigLayout());
        JLabel clFunction = new JLabel("Функция");
        add(clFunction, "wrap");
        cbpFunctionMode = new ComboBoxPanel<>(reference.getMainController(), MarchReferencesType.ADM_FUNCTION_MODE);
        add(cbpFunctionMode, "span 3, width 473:20:473, height 20:20, wrap");
        JLabel clRight = new JLabel("Права");
        add(clRight, "span 2, wrap");

        cbpRight = new ComboBoxPanel<>(reference.getMainController(), MarchReferencesType.ADM_RIGHT);
        add(cbpRight, "width 235:20:235, height 20:20, wrap");

        JLabel clNote = new JLabel("Примечание");
        add(clNote, "span 4, wrap");
        add(tfNote, "span 4, width 473:25:473, wrap");

        initEvents();
    }

    @Override
    public Object getSourceEntity() {
        source.setNote(tfNote.getText());
        source.setFunctionMode(functionalMode);
        source.setRight(right);
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {

        if (object == null) {
            source = new FunctionalRole();
            functionalMode = new FunctionMode();
            right = new UserRight();
        } else {
            source = (FunctionalRole) object;
            functionalMode = source.getFunctionMode();
            right = source.getRight();
            cbpFunctionMode.preset(functionalMode);
            cbpRight.preset(right);
        }

        cbpRight.preset(right);
        cbpFunctionMode.preset(functionalMode);
    }

    @Override
    public boolean verificationData() {
        if (cbpFunctionMode.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать функцию роли", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpFunctionMode.setFocus();
            return false;
        }

        if (cbpRight.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать права для данной функции", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpRight.setFocus();
            return false;
        }

        return true;
    }

    /**
     * События нажатий кнопок выбора из справочника
     */
    private void initEvents() {
        cbpFunctionMode.addComboBoxActionListener(e -> functionalMode = cbpFunctionMode.getSelectedItem());
        cbpRight.addComboBoxActionListener(e -> right = cbpRight.getSelectedItem());


        cbpFunctionMode.addButtonActionListener(e -> functionalMode = cbpFunctionMode.selectFromReference(true));

        cbpRight.addButtonActionListener(e -> right = cbpRight.selectFromReference(false));
    }

    public ArrayList<Object> getFunctionalModeData() {
        return cbpFunctionMode.getDataModel();
    }

}