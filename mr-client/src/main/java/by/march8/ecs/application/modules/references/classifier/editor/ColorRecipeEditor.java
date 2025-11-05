package by.march8.ecs.application.modules.references.classifier.editor;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.classifier.RecipesColorItem;
import by.march8.entities.classifier.ReferenceColorItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Andy 03.10.2018 - 8:57.
 */
public class ColorRecipeEditor extends EditingPane {

    private JLabel lblColorGroup = new JLabel("Цветовая группа:");
    private JComboBox<ReferenceColorItem> cbColorGroup = new JComboBox<>();

    private JLabel lblRecipeCode = new JLabel("Код рецептуры:");
    private JTextField tfRecipeCode = new JTextField();

    private JLabel lblRecipeName = new JLabel("Наименование:");
    private JTextField tfRecipeName = new JTextField();

    private RecipesColorItem source;
    private ReferenceColorItem preset;


    public ColorRecipeEditor(MainController mainController, List<ReferenceColorItem> colorList, ReferenceColorItem presetColor) {
        controller = mainController;
        setPreferredSize(new Dimension(480, 200));
        this.setLayout(new MigLayout());
        cbColorGroup.setModel(new DefaultComboBoxModel(colorList.toArray()));
        preset = presetColor;
        init();
    }

    private void init() {
        add(lblColorGroup, "");
        add(cbColorGroup, "width 300:20:300, wrap");

        add(new JPanel(), "height 10:10,  wrap");

        add(lblRecipeCode, "");
        add(tfRecipeCode, "width 300:20:300, wrap");

        add(lblRecipeName, "");
        add(tfRecipeName, "width 300:20:300, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setColorId(cbColorGroup.getItemAt(cbColorGroup.getSelectedIndex()).getId());
        source.setName(tfRecipeName.getText().trim().toUpperCase());
        source.setCode(tfRecipeCode.getText().trim().toUpperCase());
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object != null) {
            source = (RecipesColorItem) object;
            tfRecipeCode.setText(source.getCode().toUpperCase());
            tfRecipeName.setText(source.getName().toUpperCase());
            presetParentColor(source.getColorId());
        } else {
            source = new RecipesColorItem();

            if (preset != null) {
                if (preset.isNsiColor()) {
                    presetParentColor(preset.getParentId());
                } else {
                    presetParentColor(preset.getId());
                }
            }
            tfRecipeCode.setText("А-");
            tfRecipeName.setText("");
        }
    }

    @Override
    public boolean verificationData() {

        if (tfRecipeCode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Код рецептуры\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

/*
        if (tfRecipeName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Наименование\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
*/

        return true;
    }

    private void presetParentColor(int colorId) {
        for (int i = 0; i < cbColorGroup.getItemCount(); i++) {
            final ReferenceColorItem itemAt = cbColorGroup.getItemAt(i);

            if (itemAt.getId() == colorId) {
                cbColorGroup.setSelectedIndex(i);
                return;
            }
        }
        cbColorGroup.setSelectedIndex(-1);
    }
}
