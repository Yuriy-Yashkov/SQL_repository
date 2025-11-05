package by.march8.ecs.application.modules.references.product.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.product.MeasurementType;
import by.march8.entities.product.ModelSizeValue;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lidashka.
 */
public class ModelSubSizeValueEditor extends EditingPane {

    private static final long serialVersionUID = -3954031689774875650L;
    MainController controller;
    private JTextField tfNumber;
    private JTextField tfValue;
    private JTextField tfNote;
    private ComboBoxPanel<MeasurementType> cbpMeasurementType;
    private MeasurementType measurementType = new MeasurementType();
    private ModelSizeValue source = null;

    public ModelSubSizeValueEditor(final IReference reference) {
        super(reference);
        this.controller = reference.getMainController();

        setPreferredSize(new Dimension(280, 230));

        init();
        initEvents();

        setVisible(true);
    }

    private void init() {
        setLayout(new MigLayout());

        cbpMeasurementType = new ComboBoxPanel<>(true, controller, MarchReferencesType.MEASUREMENT_TYPE);

        tfNumber = new JTextField();
        tfValue = new JTextField();
        tfNote = new JTextField();

        add(new JLabel("Тип обмера *:"), " wrap");
        add(cbpMeasurementType, "width 250:20:250,wrap");

        add(new JLabel("Порядковый номер *: "), " wrap");
        add(tfNumber, "width 250:24:250, wrap");

        add(new JLabel("Значение *: "), " wrap");
        add(tfValue, "width 250:24:250, wrap");

        add(new JLabel("Примечание: "), " wrap");
        add(tfNote, "width 250:24:250, wrap");
    }

    private void initEvents() {
        cbpMeasurementType.addButtonActionListener(e -> measurementType = cbpMeasurementType.selectFromReference(false));

        cbpMeasurementType.addComboBoxActionListener(e -> measurementType = cbpMeasurementType.getSelectedItem());
    }

    @Override
    public Object getSourceEntity() {
        if (cbpMeasurementType.getSelectedItem() != null) {
            source.setMeasumentType(measurementType);
        }

        try {
            int value = Integer.valueOf(tfNumber.getText().trim());

            source.setNumber(value);
        } catch (Exception e) {

        }

        source.setValue(tfValue.getText().trim());
        source.setNote(tfNote.getText().trim());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new ModelSizeValue();

            measurementType = new MeasurementType();

        } else {
            this.source = (ModelSizeValue) object;

            measurementType = this.source.getMeasumentType();

        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        cbpMeasurementType.preset(measurementType);

        tfNumber.setText(String.valueOf(this.source.getNumber()));
        tfValue.setText(this.source.getValue() != null ? String.valueOf(this.source.getValue()) : "");
        tfNote.setText(this.source.getNote());
    }


    @Override
    public boolean verificationData() {
        if (cbpMeasurementType.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать тип обмера", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpMeasurementType.requestFocusInWindow();
            return false;
        }

        try {
            float value = Float.valueOf(tfNumber.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Некорректно задано значение поля \"Порядковый номер\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfNumber.requestFocusInWindow();
            return false;
        }

        if (tfValue.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Не задано значение поля \"Значение\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfValue.requestFocusInWindow();
            return false;
        }

        return true;
    }
}
