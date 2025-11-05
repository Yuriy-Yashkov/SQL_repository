package by.march8.ecs.application.modules.references.materals.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.materials.CanvasMarkingComposition;
import by.march8.entities.materials.YarnComponentType;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 06.04.2015.
 */
public class CanvasMarkingComponentEditor extends EditingPane {

    private final JTextField tfPercent = new JTextField();
    private ComboBoxPanel<YarnComponentType> cbpYarnType;
    private CanvasMarkingComposition source = null;
    private YarnComponentType componentType;

    public CanvasMarkingComponentEditor(final IReference reference) {
        super(reference);
        controller = reference.getMainController();
        initPanel();
        initEvents();
    }

    private void initPanel() {
        setPreferredSize(new Dimension(320, 150));
        this.setLayout(new MigLayout());
        JLabel lName = new JLabel("Наименование компонента пряжи *");
        add(lName, "wrap");
        cbpYarnType = new ComboBoxPanel<>(controller, MarchReferencesType.MATERIAL_YARN_TYPE);
        add(cbpYarnType, "width 300:20:300, height 20:20, wrap");
        JLabel lPercent = new JLabel("Содержание компонента, % *");
        add(lPercent, "wrap");
        add(tfPercent, "width 100:24:100, wrap");
    }

    private void initEvents() {
        cbpYarnType.addComboBoxActionListener(e -> componentType = cbpYarnType.getSelectedItem());

        cbpYarnType.addButtonActionListener(e -> componentType = cbpYarnType.selectFromReference(componentType));
    }

    @Override
    public Object getSourceEntity() {
        source.setComponent(componentType);
        source.setPercent(Float.valueOf(tfPercent.getText()));
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new CanvasMarkingComposition();
            // cbpYarnType.setSelectedIndex(0);
            cbpYarnType.preset(null);
            tfPercent.setText("0");
        } else {
            source = (CanvasMarkingComposition) object;
            tfPercent.setText(String.valueOf(source.getPercent()));
            componentType = source.getComponent();
            cbpYarnType.preset(componentType);
        }
    }

    @Override
    public boolean verificationData() {
        try {
            float result = Float.valueOf(tfPercent.getText().trim());
            if ((result <= 0) || (result > 100)) {
                JOptionPane.showMessageDialog(null,
                        "Процент содержания компонента в пряже должен быть в пределах 100%",
                        "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                tfPercent.requestFocusInWindow();
                return false;
            }
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"% содержания\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfPercent.requestFocusInWindow();
            return false;
        }
        return true;
    }
}
