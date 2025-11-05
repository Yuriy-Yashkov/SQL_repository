package by.march8.ecs.application.modules.references.product.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.product.ModelMaterial;
import by.march8.entities.standard.Unit;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 30.12.2014.
 */
public class ModelMaterialEditor extends EditingPane {

    private ComboBoxPanel<Unit> cbpUnit;
    private JTextField tfName = new JTextField();
    private JTextField tfNote = new JTextField();

    private ModelMaterial source = null;
    private Unit unit = null;

    public ModelMaterialEditor(final IReference reference) {
        super(reference);

        init();
        initEvent();
    }

    private void initEvent() {
        cbpUnit.addButtonActionListener(e -> unit = cbpUnit.selectFromReference(false));

        cbpUnit.addComboBoxActionListener(e -> unit = cbpUnit.getSelectedItem());
    }

    private void init() {
        setLayout(new MigLayout());
        setPreferredSize(new Dimension(430, 200));
        cbpUnit = new ComboBoxPanel<>(reference.getMainController(), MarchReferencesType.UNIT, true);
        cbpUnit.setSelectButtonVisible(true);

        add(new JLabel("Наименование *"), "wrap");
        add(tfName, "width 400:25:400, wrap");

        add(new JLabel("Ед. изм. *"), "width 100:20:100, wrap");
        add(cbpUnit, "width 200:20:200, height 25,  wrap");

        add(new JLabel("Примечание"), "wrap");
        add(tfNote, "width 400:25:400,  wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setName(tfName.getText());
        source.setNote(tfNote.getText());
        unit = cbpUnit.getSelectedItem();
        source.setUnit(unit);
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new ModelMaterial();
            unit = new Unit();
            cbpUnit.preset(null);
        } else {
            source = (ModelMaterial) object;
            unit = source.getUnit();
            cbpUnit.preset(unit);
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
        if (cbpUnit.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Укажите ед. измерения", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpUnit.setFocus();
            return false;
        }

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
