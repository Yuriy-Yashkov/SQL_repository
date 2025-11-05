package dept.sprav.valuta;

import common.PanelWihtFone;
import common.User;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author vova
 */
public class EditValutaKursForm extends JDialog {
    MaskFormatter formatter;
    JFormattedTextField ftDate;
    String date;
    int x = 10;
    int y = 10;
    Vector<Vector> valuta;
    String[] cbval;
    private PanelWihtFone mainPanel;
    private JComboBox cbValuta1;
    private JComboBox cbValuta2;
    private JTextField tfKurs;
    private JLabel lv1;
    private JLabel lv2;
    private JLabel lkurs;
    private JLabel ldate;
    private JButton bAdd;

    public EditValutaKursForm(JDialog parent, boolean f, int id) {
        super(parent, f);
        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.MONTH) + 1;
        String month = new String();
        if (i < 10) {
            month = "0" + i;
        } else month = Integer.toString(i);
        i = c.get(Calendar.DAY_OF_MONTH);
        String day = new String();
        if (i < 10) {
            day = "0" + i;
        } else day = Integer.toString(i);
        date = new String(day + "." + month + "." + c.get(Calendar.YEAR));

        setTitle("Добавить курс");

        ValutaPDB db = new ValutaPDB();
        valuta = db.getValutaList();
        db.disConn();
        int k = valuta.size();
        cbval = new String[k];
        int q = 0;
        for (Vector v : valuta) {
            cbval[q++] = v.get(1).toString();
        }

        initComponents();

        add(mainPanel);
        setSize(370, 220);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        lv1 = new JLabel("Валюта(осн)");
        lv1.setBounds(x, y, 100, 20);
        mainPanel.add(lv1);
        cbValuta1 = new JComboBox(cbval);
        cbValuta1.setSelectedIndex(0);
        cbValuta1.setBounds(x + 110, y, 80, 20);
        mainPanel.add(cbValuta1);

        lv2 = new JLabel("Валюта(доп)");
        lv2.setBounds(x, y + 25, 100, 20);
        mainPanel.add(lv2);
        cbValuta2 = new JComboBox(cbval);
        cbValuta2.setSelectedIndex(0);
        cbValuta2.setBounds(x + 110, y + 25, 80, 20);
        mainPanel.add(cbValuta2);

        lkurs = new JLabel("Курс");
        lkurs.setBounds(x, y + 50, 100, 20);
        mainPanel.add(lkurs);
        tfKurs = new JTextField("1");
        tfKurs.setBounds(x + 110, y + 50, 60, 20);
        mainPanel.add(tfKurs);

        ldate = new JLabel("Дата начала действия курса");
        ldate.setBounds(x, y + 75, 220, 20);
        mainPanel.add(ldate);
        ftDate = new javax.swing.JFormattedTextField(formatter);
        ftDate.setText(date);
        ftDate.setBounds(x, y + 100, 80, 20);
        mainPanel.add(ftDate);

        bAdd = new JButton("Добавить");
        bAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!cbValuta1.getSelectedItem().toString().equals(cbValuta2.getSelectedItem().toString())) {
                    if (checkDate(date)) {
                        ValutaPDB db = new ValutaPDB();
                        db.addKurs(Integer.parseInt(db.getValuta(cbValuta1.getSelectedItem().toString()).get("id").toString()), Integer.parseInt(db.getValuta(cbValuta2.getSelectedItem().toString()).get("id").toString()), Float.parseFloat(tfKurs.getText().trim()), User.getInstance().getName(), ftDate.getText().trim());
                        db.disConn();
                        JOptionPane.showMessageDialog(null, "Новый курс добавлен", "Успешно", JOptionPane.INFORMATION_MESSAGE);
                        dispose();

                    }
                } else
                    JOptionPane.showMessageDialog(null, "Валюты не должны совпадать", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            }
        });
        bAdd.setBounds(x + 210, y + 125, 120, 25);
        mainPanel.add(bAdd);

    }

    private boolean checkDate(String chDate) {
        int d = 0, m = 0, y = 0;
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
