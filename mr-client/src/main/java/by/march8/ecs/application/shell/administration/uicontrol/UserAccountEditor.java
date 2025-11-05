package by.march8.ecs.application.shell.administration.uicontrol;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.RecordEditorPane;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IAdministrationDao;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.sdk.reference.Reference;
import by.march8.entities.admin.UserInformation;
import by.march8.entities.company.Employee;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andy 17.09.2014.
 */
public class UserAccountEditor extends RecordEditorPane {

    private static final Map<String, String> letters = new HashMap<>();

    static {
        letters.put("А", "A");
        letters.put("Б", "B");
        letters.put("В", "V");
        letters.put("Г", "G");
        letters.put("Д", "D");
        letters.put("Е", "E");
        letters.put("Ё", "E");
        letters.put("Ж", "ZH");
        letters.put("З", "Z");
        letters.put("И", "I");
        letters.put("Й", "I");
        letters.put("К", "K");
        letters.put("Л", "L");
        letters.put("М", "M");
        letters.put("Н", "N");
        letters.put("О", "O");
        letters.put("П", "P");
        letters.put("Р", "R");
        letters.put("С", "S");
        letters.put("Т", "T");
        letters.put("У", "U");
        letters.put("Ф", "F");
        letters.put("Х", "H");
        letters.put("Ц", "C");
        letters.put("Ч", "CH");
        letters.put("Ш", "SH");
        letters.put("Щ", "SH");
        letters.put("Ъ", "'");
        letters.put("Ы", "Y");
        letters.put("Ъ", "'");
        letters.put("Э", "E");
        letters.put("Ю", "U");
        letters.put("Я", "YA");
        letters.put("а", "a");
        letters.put("б", "b");
        letters.put("в", "v");
        letters.put("г", "g");
        letters.put("д", "d");
        letters.put("е", "e");
        letters.put("ё", "e");
        letters.put("ж", "zh");
        letters.put("з", "z");
        letters.put("и", "i");
        letters.put("й", "i");
        letters.put("к", "k");
        letters.put("л", "l");
        letters.put("м", "m");
        letters.put("н", "n");
        letters.put("о", "o");
        letters.put("п", "p");
        letters.put("р", "r");
        letters.put("с", "s");
        letters.put("т", "t");
        letters.put("у", "u");
        letters.put("ф", "f");
        letters.put("х", "h");
        letters.put("ц", "c");
        letters.put("ч", "ch");
        letters.put("ш", "sh");
        letters.put("щ", "sh");
        letters.put("ъ", "'");
        letters.put("ы", "y");
        letters.put("ъ", "'");
        letters.put("э", "e");
        letters.put("ю", "u");
        letters.put("я", "ya");
    }

    private final JLabel lEmployee = new JLabel();
    private final JTextField tfLogin = new JTextField();
    private final JTextField tfPassword = new JTextField();
    private final JButton btnEmployeeSelect = new JButton("+");
    private UserInformation source;
    private Employee employee;

    public UserAccountEditor(final MainController controller) {
        super(controller);
        this.setLayout(new MigLayout());
        this.setPreferredSize(new Dimension(345, 190));
        final Font titleFont = getFont().deriveFont(Font.BOLD, 16);

        lEmployee.setFont(titleFont);
        lEmployee.setForeground(Color.BLUE.darker());

        btnEmployeeSelect.setPreferredSize(new Dimension(20, 20));

        JLabel clEmployee = new JLabel("Сотрудник");
        add(clEmployee, "wrap");
        add(lEmployee, "width 300:20:300");
        add(btnEmployeeSelect, "width 20:20:20,wrap");
        JLabel clLogin = new JLabel("Логин");
        add(clLogin, "wrap");
        add(tfLogin, "width 100:20:100, wrap");
        JLabel clPassword = new JLabel("Пароль");
        add(clPassword, "wrap");
        add(tfPassword, "width 100:20:100");

        // Для сотрудника, выбранного из справочника необходимо проверить, \
        // есть ли у его аккаунт
        btnEmployeeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final Reference ref = new Reference(controller,
                        MarchReferencesType.COMPANY_EMPLOYEES,
                        MarchWindowType.PICKFRAME);
                final Employee item = (Employee) ref.showPickFrame();
                if (item != null) {
                    final DaoFactory factory = DaoFactory
                            .getInstance();
                    final IAdministrationDao adminDao = factory
                            .getAdministrationDao();
                    final UserInformation user = adminDao.hasHaveAccount(item
                            .getId());
                    if (user == null) {
                        employee = item;
                        lEmployee.setText(employee.toString());
                        tfLogin.setText(generationAccountName(item));
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Для выбранного сотрудника уже существует учетная запись ["
                                        + user.getUserLogin() + "]",
                                "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
    }

    /**
     * Метод генерирует транслит строку по строке агрументом (RUS to EN)
     */
    public static String toTransliterate(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            String l = text.substring(i, i + 1);
            if (letters.containsKey(l)) {
                sb.append(letters.get(l));
            } else {
                sb.append(l);
            }
        }
        return sb.toString();
    }

    @Override
    public Object getSourceEntity() {
        source.setEmployee(employee);
        source.setUserLogin(tfLogin.getText().trim());
        source.setUserPassword(tfPassword.getText().trim());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new UserInformation();
            employee = new Employee();

            lEmployee.setText("Выбрать из справочника");
            tfLogin.setText("");
            tfPassword.setText("");
        } else {
            this.source = (UserInformation) object;
            employee = this.source.getEmployee();

            lEmployee.setText(employee.toString());
            tfLogin.setText(this.source.getUserLogin());
            tfPassword.setText(this.source.getUserPassword());
        }
    }

    @Override
    public boolean verificationData() {
        if (employee.getSurname().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо выбрать сотрудника из справочника",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            btnEmployeeSelect.requestFocusInWindow();
            return false;
        }

        if (tfLogin.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Логин\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfLogin.requestFocusInWindow();
            return false;
        }

        if (tfPassword.getText().trim().equals("")) {
            final int answer = JOptionPane.showConfirmDialog(null,
                    "Учетная запись не имеет пароля, продолжить?",
                    "Пароль не задан", JOptionPane.YES_NO_OPTION);
            if (answer == 0) {
                return true;
            } else {
                tfPassword.requestFocusInWindow();
                return false;
            }
        }
        return true;
    }

    /**
     * Метод возвращает логин в транслите согласно его фамилии
     */
    private String generationAccountName(final Employee item) {
        String name = item.getName().substring(0, 1);
        return toTransliterate(name.toLowerCase() + item.getSurname().toLowerCase());
    }
}
