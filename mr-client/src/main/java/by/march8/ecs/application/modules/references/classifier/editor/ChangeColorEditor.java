package by.march8.ecs.application.modules.references.classifier.editor;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.classifier.NSIColorItem;
import by.march8.entities.classifier.ReferenceColorItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Andy 10.09.2018 - 9:51.
 */
public class ChangeColorEditor extends EditingPane {

    private JLabel lblColorItem = new JLabel("Первичный цвет:");
    private JComboBox<ReferenceColorItem> cbColorItem = new JComboBox<>();

    private JLabel lblColorCode = new JLabel("Код цвета:");
    private JTextField tfColorCode = new JTextField();

    private JLabel lblColorName = new JLabel("Наименование цвета:");
    private JTextField tfColorName = new JTextField();

    private ReferenceColorItem source = null;

    public ChangeColorEditor(MainController mainController, List<ReferenceColorItem> colorList) {
        controller = mainController;
        setPreferredSize(new Dimension(480, 150));
        this.setLayout(new MigLayout());
        cbColorItem.setModel(new DefaultComboBoxModel(colorList.toArray()));

        init();
    }

    private void init() {

        add(lblColorCode, "");
        add(tfColorCode, "width 100:20:100, wrap");

        tfColorCode.setEnabled(false);

        add(lblColorName, "");
        add(tfColorName, "width 300:20:300, wrap");

        add(lblColorItem, "");
        add(cbColorItem, "width 300:20:300, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setParentId(cbColorItem.getItemAt(cbColorItem.getSelectedIndex()).getId());
        NSIColorItem nsiColor = new NSIColorItem();
        nsiColor.setName(tfColorName.getText().trim());

        if (source.isNewColor()) {
            nsiColor.setId(0);
            nsiColor.setParentId(source.getId());
        } else {
            nsiColor.setId(source.getId());
            nsiColor.setParentId(source.getParentId());
        }

        return nsiColor;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object != null) {
            source = (ReferenceColorItem) object;

            if (source.isNewColor()) {
                tfColorName.setText("");
                tfColorCode.setText("---");
                presetParentColor(source.getId());
            } else {
                presetParentColor(source.getParentId());
                tfColorName.setText(source.getName());
                tfColorCode.setText(String.valueOf(source.getId()));
            }

            defaultFillingData();
        }
    }

    private void presetParentColor(int colorId) {
        for (int i = 0; i < cbColorItem.getItemCount(); i++) {
            final ReferenceColorItem itemAt = cbColorItem.getItemAt(i);

            if (itemAt.getId() == colorId) {
                cbColorItem.setSelectedIndex(i);
                return;
            }
        }
        cbColorItem.setSelectedIndex(-1);
    }

    @Override
    public boolean verificationData() {
        if (cbColorItem.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Укажите цвет верхнего уровня для переноса", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (tfColorName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Наименование цвета\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
