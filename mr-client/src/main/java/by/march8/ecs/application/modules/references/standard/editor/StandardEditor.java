package by.march8.ecs.application.modules.references.standard.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.api.utils.DateUtils;
import by.march8.entities.standard.Standard;
import by.march8.entities.standard.StandardType;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Панель редактирования кодов классификационных стандартов
 *
 * Created by Andy on 21.11.14.
 */
public class StandardEditor extends EditingPane {
    private ComboBoxPanel<StandardType> cbpStandardType;
    private JTextField tfName = new JTextField();
    private JDateChooser dateChooser = new JDateChooser();
    private JTextField tfNote = new JTextField();

    private Standard source = null;
    private StandardType type = null;

    public StandardEditor(final IReference reference) {
        super(reference);
        setLayout(new MigLayout());
        setPreferredSize(new Dimension(450, 230));
        cbpStandardType = new ComboBoxPanel<>(reference.getMainController(), MarchReferencesType.CODETYPE, true);
        cbpStandardType.setSelectButtonVisible(true);

        add(new JLabel("Тип *"), "width 100:20:100, wrap");
        add(cbpStandardType, "width 430:20:430, height 20:20, wrap");

        add(new JLabel("Наименование *"), "wrap");
        add(tfName, "width 430:24:430, wrap");

        add(new JLabel("Дата вступления в силу"), "wrap");
        add(dateChooser, "width 200:24:200, wrap");

        add(new JLabel("Примечание"), "wrap");
        add(tfNote, "width 430:24:430, wrap");
        dateChooser.setDateFormatString("dd.MM.yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -30);

        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 60);
        Date endDate = calendar.getTime();

        dateChooser.setMinSelectableDate(startDate);
        dateChooser.setMaxSelectableDate(endDate);

        cbpStandardType.addButtonActionListener(e -> type = cbpStandardType.selectFromReference(false));

        cbpStandardType.addComboBoxActionListener(e -> type = cbpStandardType.getSelectedItem());
    }

    @Override
    public Object getSourceEntity() {
        source.setName(tfName.getText());
        source.setNote(tfNote.getText());
        type = cbpStandardType.getSelectedItem();
        source.setType(type);
        source.setDate(dateChooser.getDate());
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new Standard();
            type = new StandardType();
            dateChooser.setDate(DateUtils.getDateNow());
            cbpStandardType.preset(null);
        } else {
            source = (Standard) object;
            type = source.getType();
            cbpStandardType.preset(type);
            dateChooser.setDate(source.getDate());
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
        if (cbpStandardType.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Укажите тип стандарта", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpStandardType.setFocus();
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
