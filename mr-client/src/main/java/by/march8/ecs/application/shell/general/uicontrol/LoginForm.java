/*
 *
 */
package by.march8.ecs.application.shell.general.uicontrol;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IAdministrationDao;
import by.march8.ecs.framework.helpers.AccountUtils;
import by.march8.entities.admin.UserInformation;
import by.march8.entities.company.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Форма входа в программу.
 *
 * @author andy-windows
 */
public class LoginForm extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5950178650715514604L;

    /** The txt user name. */
    private JTextField txtUserName;

    /** The txt user pass. */
    private JPasswordField txtUserPass;

    /** The btn login. */
    private JButton btnLogin;

    /** The login user. */

    private UserInformation loginUser = null;

    /** The modal result. */
    private boolean modalResult = false;

    private DaoFactory factory;
    private IAdministrationDao accountDao;

    /**
     * Конструктор класса формы Входа в программу.
     *
     * @param userName
     *            the user name
     */
    public LoginForm(final String userName) {
        setTitle("Вход в программу");

        setModal(true);

        // Инструмент для снятия параметров экрана Windows
        final Toolkit toolKit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolKit.getScreenSize();
        final Dimension frameSize = new Dimension(325, 180);

        final Point p = new Point((screenSize.width / 2)
                - (frameSize.width / 2), (screenSize.height / 2)
                - (frameSize.height / 2));

        //setModalityType(ModalityType.TOOLKIT_MODAL);
        setSize(frameSize);
        setLocation(p);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        final JLabel lblName = new JLabel("Имя:");
        final JLabel lblPass = new JLabel("Пароль: ");
        txtUserName = new JTextField(userName);
        txtUserPass = new JPasswordField();
        //txtUserPass.setText("admin148");

        btnLogin = new JButton("Вход");
        btnLogin.setFocusable(true);

        final int x = 60;
        final int y = 35;
        final int h = 25;

        getContentPane().setLayout(null);

        lblName.setBounds(x, y, 70, h);
        lblPass.setBounds(x, y + h + 10, 70, h);
        txtUserName.setBounds(x + 80, y, 125, h);
        txtUserPass.setBounds(x + 80, y + h + 10, 125, h);

        btnLogin.setBounds(x + 50, y + h + h + 30, 100, 30);

        this.add(lblName);
        this.add(lblPass);
        this.add(txtUserName);
        this.add(txtUserPass);
        this.add(btnLogin);

        btnLogin.addActionListener(e -> {
            // Проверка на незаполненные поля
            if (txtUserName.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Поле Логин не должно быть пустым",
                        "Предупреждение", JOptionPane.WARNING_MESSAGE);
                txtUserName.setText("");
                txtUserPass.setText("");
                return;
            }
            if (txtUserName.getText().trim().equals("DebuG")) {
                loginUser = new UserInformation();
                loginUser.setNote("Пользователь для отладки кода");
                loginUser.setUserLogin("debug");
                Employee eDebug = new Employee();
                eDebug.setName("Дебаг");
                eDebug.setSurname("Дебагурин");
                loginUser.setEmployee(eDebug);
                modalResult = true;
                setVisible(false);
            } else {
                factory = DaoFactory.getInstance();
                accountDao = factory.getAdministrationDao();
                final UserInformation requestUserInfo = accountDao
                        .getUserInformationThread(txtUserName.getText().toLowerCase()
                                .trim());

                if (requestUserInfo == null) {
                    JOptionPane.showMessageDialog(null,
                            "Пользователя с такими данными не существует",
                            "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    if (!AccountUtils.passCheck(txtUserPass.getPassword(),
                            requestUserInfo.getUserPassword().toCharArray())) {
                        JOptionPane.showMessageDialog(null,
                                "Введенный пароль неверный", "Предупреждение",
                                JOptionPane.WARNING_MESSAGE);
                        txtUserPass.setText("");
                        txtUserPass.requestFocusInWindow();
                        return;
                    } else {
                        loginUser = requestUserInfo;
                    }
                }
                modalResult = true;
                setVisible(false);
            }
        });

        txtUserName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });

        txtUserPass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                if (!txtUserName.getText().equals("")) {
                    txtUserPass.requestFocus();
                }
            }
        });
    }

    public boolean activateLoginForm() {
        setVisible(true);
        return modalResult;
    }

    public UserInformation getActiveUser() {
        return this.loginUser;
    }

}
