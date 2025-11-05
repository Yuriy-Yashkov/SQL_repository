package by.march8.ecs.application.modules.references.company.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.company.CompanyDepartment;
import by.march8.entities.company.CompanyPosition;
import by.march8.entities.company.Employee;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Панель редактирования сотрудника
 *
 * @see by.march8.entities.company.Employee
 * @see by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane
 * Created by Andy on 17.09.2014.
 */
public class EmployeeEditor extends EditingPane {

    private final JTextField tfSurname = new JTextField();
    private final JTextField tfName = new JTextField();
    private final JTextField tfPatronymic = new JTextField();
    private final JTextField tfCardNumber = new JTextField();


    private final ComboBoxPanel<CompanyDepartment> cbpDepartment;
    private final ComboBoxPanel<CompanyPosition> cbpPosition;


    private final JTextField tfHomePhone = new JTextField();
    private final JTextField tfWorkPhone = new JTextField();
    private final JTextField tfMobilePhone = new JTextField();
    private final JTextField tfEmail = new JTextField();
    private final JTextField tfNote = new JTextField();
    private Employee source = new Employee();
    private CompanyDepartment department = new CompanyDepartment();
    private CompanyPosition position = new CompanyPosition();


    public EmployeeEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(478, 360));
        this.setLayout(new MigLayout());

        JLabel clSurname = new JLabel("Фамилия");
        add(clSurname);
        JLabel clName = new JLabel("Имя");
        add(clName);
        JLabel clPatronymic = new JLabel("Отчество");
        add(clPatronymic, "wrap");
        add(tfSurname, "width 150:24:150");
        add(tfName, "width 150:24:150");
        add(tfPatronymic, "width 150:24:150, wrap");

        JLabel clCardNumber = new JLabel("Табельный номер");
        add(clCardNumber, "wrap");
        add(tfCardNumber, "width 150:24:150, wrap");

        JLabel clDepartment = new JLabel("Подразделение");
        add(clDepartment, "wrap");
        cbpDepartment = new ComboBoxPanel<>(reference.getMainController(), MarchReferencesType.COMPANY_DEPARTMENTS);
        add(cbpDepartment, "width 300:20:300,height 20:20,span 2, wrap");


        JLabel clPosition = new JLabel("Должность");
        add(clPosition, "wrap");
        cbpPosition = new ComboBoxPanel<>(reference.getMainController(), MarchReferencesType.COMPANY_POSITION);
        add(cbpPosition, "width 300:20:300, height 20:20,span 2,wrap");


        JLabel clHomePhone = new JLabel("Домашний телефон");
        add(clHomePhone);
        JLabel clWorkPhone = new JLabel("Рабочий телефон");
        add(clWorkPhone);
        JLabel clMobilePhone = new JLabel("Мобильный телефон");
        add(clMobilePhone, "wrap");
        add(tfHomePhone, "width 150:24:150");
        add(tfWorkPhone, "width 150:24:150");
        add(tfMobilePhone, "width 150:24:150, wrap");

        JLabel clEmail = new JLabel("E-mail");
        add(clEmail, "wrap");
        add(tfEmail, "width 250:24:250,span 2,wrap");

        JLabel clNote = new JLabel("Примечание");
        add(clNote, "wrap");
        add(tfNote, "width 457:24:457,span,wrap");

        initEvents();
    }

    @Override
    public Object getSourceEntity() {

        System.out.println(department.toString());
        System.out.println(position.toString());

        source.setDepartment(department);
        source.setPosition(position);

        source.setSurname(tfSurname.getText());
        source.setName(tfName.getText());
        source.setPatronymic(tfPatronymic.getText());

        source.setCardNumber(Integer.valueOf(tfCardNumber.getText()));

        source.setHomePhone(tfHomePhone.getText());
        source.setWorkPhone(tfWorkPhone.getText());
        source.setMobilePhone(tfMobilePhone.getText());

        source.setEmail(tfEmail.getText());
        source.setNote(tfNote.getText());

        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {

        if (object == null) {
            department = cbpDepartment.getItem(0);
            position = cbpPosition.getItem(0);

            tfSurname.setText("");
            tfName.setText("");
            tfPatronymic.setText("");

            tfCardNumber.setText("0");

            tfHomePhone.setText("");
            tfWorkPhone.setText("");
            tfMobilePhone.setText("");
            tfEmail.setText("");
            tfNote.setText("");
        } else {
            this.source = (Employee) object;
            department = this.source.getDepartment();
            position = this.source.getPosition();

            tfSurname.setText(source.getSurname());
            tfName.setText(source.getName());
            tfPatronymic.setText(source.getPatronymic());

            if (String.valueOf(source.getCardNumber()).equals("null")) {
                tfCardNumber.setText("0");
            } else {
                tfCardNumber.setText(String.valueOf(source.getCardNumber()));
            }

            tfHomePhone.setText(source.getHomePhone());
            tfWorkPhone.setText(source.getWorkPhone());
            tfMobilePhone.setText(source.getMobilePhone());

            tfEmail.setText(source.getEmail());
            tfNote.setText(source.getNote());

            cbpDepartment.preset(department);
            cbpPosition.preset(position);
        }
    }

    @Override
    public boolean verificationData() {
        if (tfSurname.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Для сотрудника необходимо указать фамилию", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfSurname.requestFocusInWindow();
            return false;
        }
        if (tfName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Для сотрудника необходимо указать имя", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }
        try {
            Integer.valueOf(tfCardNumber.getText());
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Табельный номер всегда цифровой", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfCardNumber.requestFocusInWindow();
            return false;
        }

        if (cbpDepartment.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Укажите подразделение предприятия", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpDepartment.setFocus();
            return false;
        }

        if (cbpPosition.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Укажите должность сотрудника", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpPosition.setFocus();
            return false;
        }

        return true;
    }

    /**
     * Инициализация событий на панели
     */
    private void initEvents() {

        cbpDepartment.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                department = cbpDepartment.selectFromReference(false);
            }
        });

        cbpPosition.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                position = cbpPosition.selectFromReference(false);
            }
        });

        cbpDepartment.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                department = cbpDepartment.getSelectedItem();
            }
        });

        cbpPosition.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                position = cbpPosition.getSelectedItem();
            }
        });
    }
}
