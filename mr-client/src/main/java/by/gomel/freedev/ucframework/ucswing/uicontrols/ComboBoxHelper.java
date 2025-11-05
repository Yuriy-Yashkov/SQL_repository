package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.march8.api.BaseEntity;

import javax.swing.*;

public class ComboBoxHelper {

    public static void preset(JComboBox comboBox, String preset) {
        if (comboBox.getItemCount() < 1) {
            return;
        }
        if (preset == null) {
            comboBox.setSelectedIndex(-1);
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i) != null) {
                String comboItem = comboBox.getItemAt(i).toString();
                if (comboItem.trim().toLowerCase().equals(preset.trim().toLowerCase())) {
                    comboBox.setSelectedIndex(i);
                    return;
                }
            }
        }
        comboBox.setSelectedIndex(-1);
    }

    public static void presetById(JComboBox comboBox, int id) {
        if (comboBox.getItemCount() < 1) {
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i) != null) {
                BaseEntity comboItem = (BaseEntity) comboBox.getItemAt(i);
                if (comboItem.getId() == id) {
                    comboBox.setSelectedIndex(i);
                    return;
                }
            }
        }
        comboBox.setSelectedIndex(-1);
    }
}


