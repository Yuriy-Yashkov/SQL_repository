package dept.otk;

import common.PanelWihtFone;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.DateFormat;
import java.util.Calendar;

/**
 *
 * @author vova
 */
public class DataForm extends JDialog {
    static String sDate;
    static String eDate;
    MaskFormatter formatter;
    MaskFormatter formatter2;
    JDialog th;
    private javax.swing.JButton jButton1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private PanelWihtFone mainPanel;

    public DataForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        th = this;
        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');
        try {
            formatter2 = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter2.setPlaceholderCharacter('0');
        initComponents();
        this.setTitle("Период просмотра");

        add(mainPanel);
        setSize(360, 210);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField(formatter);
        jFormattedTextField2 = new javax.swing.JFormattedTextField(formatter2);
        jButton1 = new javax.swing.JButton();

        Calendar c = Calendar.getInstance();
        DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
        eDate = new String(df.format(c.getTime()));
        int i = c.get(Calendar.MONTH) + 1;
        String month = new String();
        if (i < 10) {
            month = "0" + i;
        } else month = Integer.toString(i);
        sDate = new String("01." + month + "." + c.get(Calendar.YEAR));
        jFormattedTextField1.setText(sDate);
        jFormattedTextField1.setBounds(140, 50, 80, 20);
        mainPanel.add(jFormattedTextField1);
        jFormattedTextField2.setText(eDate);
        jFormattedTextField2.setBounds(140, 80, 80, 20);
        mainPanel.add(jFormattedTextField2);


        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Данные на участке упаковки за период:");
        jLabel1.setBounds(30, 20, 300, 20);
        mainPanel.add(jLabel1);

        jLabel2.setText("с");
        jLabel2.setBounds(120, 50, 20, 20);
        mainPanel.add(jLabel2);

        jLabel3.setText("по");
        jLabel3.setBounds(120, 80, 20, 20);
        mainPanel.add(jLabel3);

        jButton1.setText("Показать");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (checkDate(jFormattedTextField1.getText()) && checkDate(jFormattedTextField2.getText())) {
                    new ResultForm(th, true, jFormattedTextField1.getText(), jFormattedTextField2.getText());
                }
            }
        });
        jButton1.setBounds(130, 120, 120, 20);
        mainPanel.add(jButton1);

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


