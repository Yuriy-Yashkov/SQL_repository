package dept.mylog;

import common.PanelWihtFone;
import workDB.PDB;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vova
 */
public class LoginDataForm extends JDialog {

    PanelWihtFone mainPanel;
    JLabel lName;
    JLabel lStart;
    JLabel lFinish;
    JButton bShow;
    JButton bDelete;
    JFormattedTextField lDateS;
    JFormattedTextField lDateE;
    MaskFormatter formatter;
    JComboBox listUsers;
    int x = 10;
    int y = 10;
    ArrayList users;
    JDialog thisWin;


    public LoginDataForm(JFrame frame, boolean f) {
        super(frame, f);
        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        PDB pdb = new PDB();
        try {
            users = new ArrayList(pdb.getUsers());
        } catch (Exception ex) {
            Logger.getLogger(LoginDataForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        pdb.disConn();
        users.add(0, "Все...");
        initComponent();
        setTitle("Ввод данных");
        add(mainPanel);
        setSize(300, 200);
        setResizable(false);
        thisWin = this;
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void initComponent() {
        mainPanel = new PanelWihtFone();

        lName = new JLabel("Пользователь: ");
        lName.setBounds(x, y, 120, 20);
        mainPanel.add(lName);
        listUsers = new JComboBox(users.toArray());
        listUsers.setBounds(x + 120, y, 150, 20);
        mainPanel.add(listUsers);

        lStart = new JLabel("Период с:");
        lStart.setBounds(x + 40, y + 40, 100, 20);
        mainPanel.add(lStart);

        lDateS = new javax.swing.JFormattedTextField(formatter);
        Calendar c = Calendar.getInstance();
        DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
        String eDate = df.format(c.getTime());
        lDateS.setText(eDate);
        lDateS.setBounds(x + 115, y + 40, 80, 20);
        mainPanel.add(lDateS);

        lFinish = new JLabel("Период по:");
        lFinish.setBounds(x + 30, y + 70, 100, 20);
        mainPanel.add(lFinish);

        lDateE = new javax.swing.JFormattedTextField(formatter);
        lDateE.setText(eDate);
        lDateE.setBounds(x + 115, y + 70, 80, 20);
        mainPanel.add(lDateE);

        bShow = new JButton("Показать");
        bShow.addActionListener(e -> {
            if (checkDate(lDateS.getText().trim()) && checkDate(lDateE.getText().trim())) {
                new LoginResultForm(thisWin, true, listUsers.getSelectedItem().toString(), lDateS.getText().trim(), lDateE.getText().trim());
            }
        });
        bShow.setBounds(x + 30, y + 115, 110, 20);
        mainPanel.add(bShow);

        bDelete = new JButton("Удалить");
        bDelete.addActionListener(e -> {
            if (checkDate(lDateS.getText().trim()) && checkDate(lDateE.getText().trim())) {
                PDB pdb = new PDB();
                pdb.delUserLogin(listUsers.getSelectedItem().toString(), lDateS.getText().trim(), lDateE.getText().trim());
                pdb.disConn();
            }
        });
        bDelete.setBounds(x + 150, y + 115, 110, 20);
        mainPanel.add(bDelete);
    }

    private boolean checkDate(String chDate) {
        int d, m, y;
        try {
            d = Integer.parseInt(chDate.substring(0, 2));
            if (d > 31 || d < 1) {
                throw new Exception("недопустимое значение дня");
            }
            m = Integer.parseInt(chDate.substring(3, 5));
            if ((m > 12) || (m < 1)) {
                throw new Exception("недопустимое значение месяца");
            }
            y = Integer.parseInt(chDate.substring(6, 10));
            if (y < 1) {
                throw new Exception("недопустимое значение года");
            }
        } catch (Exception e) {
            System.out.println("Ошибка преобразования даты:\n" + e);
            JOptionPane.showMessageDialog(null, "Ошибка преобразования даты: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
