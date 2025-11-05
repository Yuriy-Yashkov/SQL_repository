package dept.upack.ns;

import common.PanelWihtFone;
import workDB.DB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author vova
 */
public class PereuchZSH extends JDialog {
    JLabel nIach;
    JLabel lDate;
    JTextField tfnIach;
    JButton bPrint;
    JFormattedTextField tfDate;
    JPanel mainPanel;
    int x = 10;
    int y = 10;
    int number = -1;
    private MaskFormatter formatter;

    public PereuchZSH(JFrame parent, boolean f) {
        super(parent, f);

        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');

        initComponents();

        add(mainPanel);
        setSize(400, 205);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Переучёт ЗШ цеха");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        nIach = new JLabel("Введите номер ячейки:");
        nIach.setBounds(x + 10, y, 250, 20);
        mainPanel.add(nIach);

        tfnIach = new JTextField();
        tfnIach.setBounds(x + 180, y, 60, 20);
        mainPanel.add(tfnIach);


        lDate = new JLabel("Введите дату переучёта:");
        lDate.setBounds(x + 10, y + 25, 250, 20);
        mainPanel.add(lDate);

        tfDate = new javax.swing.JFormattedTextField();
        Calendar c = Calendar.getInstance();
        DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
        String date = new String(df.format(c.getTime()));
        formatter.install(tfDate);
        tfDate.setText(date);
        tfDate.setBounds(x + 195, y + 25, 80, 20);
        mainPanel.add(tfDate);

        bPrint = new JButton("Печать");
        bPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    number = Integer.parseInt(tfnIach.getText().trim());
                } catch (Exception er) {
                    System.err.println("Ошибка преобразования номера ячейки " + er);
                    JOptionPane.showMessageDialog(null, "Неправильно введено значение ячейки: " + er.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (checkDate(tfDate.getText().trim())) {
                    Vector v = new Vector();
                    DB db = new DB();
                    try {
                        v = db.getPereuchZSH(number);
                    } catch (Exception er) {
                        JOptionPane.showMessageDialog(null, "Ошибка при nbkj: " + er.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        dispose();
                    }
                    OpenOffice oo = new OpenOffice("Переучёт закройно-швейного цеха н/с продукции на " + tfDate.getText().trim(), v);
                    oo.createReport("ПереучётЗШ.ots");
                }
            }
        });
        bPrint.setBounds(x + 140, y + 125, 120, 20);
        mainPanel.add(bPrint);
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
