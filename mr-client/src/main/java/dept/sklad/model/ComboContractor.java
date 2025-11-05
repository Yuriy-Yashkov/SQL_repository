package dept.sklad.model;

import javax.swing.*;
import java.util.ArrayList;

public class ComboContractor {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void filling(JComboBox<String> combobox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("Общий вид");
        array.add("Евроторг");
        array.add("БелВиллесден");
        array.add("Юнифуд");
        array.add("Белпочта");
        array.add("ГринРозница");
        array.add("Стандартный");
        array.add("Простор");
        array.add("Табак инвест");
        combobox.setModel(new DefaultComboBoxModel(array.toArray()));
        combobox.setSelectedIndex(0);
    }

}
