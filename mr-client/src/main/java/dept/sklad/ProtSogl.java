package dept.sklad;

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
public class ProtSogl extends JDialog {

    JLabel lHead;
    JLabel lDateProt;
    JLabel lDateTTN;
    JLabel lKurs;
    JLabel lKursP;
    JLabel lSkidka;

    JTextField tfNProt;
    JFormattedTextField tfDateProt;
    JFormattedTextField tfDateTTN;
    JTextField tfKurs;
    JTextField tfKursP;
    JTextField tfSkidka;
    JButton bPrint;
    JPanel mainPanel;
    int x = 10;
    int y = 10;
    String ndoc;
    String client;
    private MaskFormatter formatter;
    private MaskFormatter formatter2;

    public ProtSogl(JDialog parent, boolean f, String ndoc, String c) {
        super(parent, f);
        this.ndoc = ndoc;
        client = c;

        try {
            formatter = new MaskFormatter("##.##.####");
            formatter2 = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');
        formatter2.setPlaceholderCharacter('0');

        initComponents();

        add(mainPanel);

        setSize(400, 205);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Протокол согласования");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        lHead = new JLabel("Протокол согласования № ");
        lHead.setBounds(x + 10, y, 250, 20);
        mainPanel.add(lHead);

        tfNProt = new JTextField('0');
        tfNProt.setBounds(x + 205, y, 60, 20);
        mainPanel.add(tfNProt);

        lDateProt = new JLabel("от");
        lDateProt.setBounds(x + 270, y, 50, 20);
        mainPanel.add(lDateProt);

        tfDateProt = new javax.swing.JFormattedTextField();
        Calendar c = Calendar.getInstance();
        DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
        String date = new String(df.format(c.getTime()));
        formatter.install(tfDateProt);
        tfDateProt.setText(date);
        tfDateProt.setBounds(x + 290, y, 80, 20);
        mainPanel.add(tfDateProt);

        lDateTTN = new JLabel("ТТН № " + ndoc + " от ");
        lDateTTN.setBounds(x + 10, y + 25, 250, 20);
        mainPanel.add(lDateTTN);

        tfDateTTN = new javax.swing.JFormattedTextField(formatter2);
        tfDateTTN.setText(date);
        tfDateTTN.setBounds(x + 150, y + 25, 80, 20);
        mainPanel.add(tfDateTTN);

        lKurs = new JLabel("Курс на момент отгрузки");
        lKurs.setBounds(x + 10, y + 50, 250, 20);
        mainPanel.add(lKurs);

        tfKurs = new JTextField("1");
        tfKurs.setBounds(x + 200, y + 50, 60, 20);
        mainPanel.add(tfKurs);

       /* lKursP = new JLabel("Курс по прейскуранту");
        lKursP.setBounds(x + 10, y + 75, 250, 20);
        mainPanel.add(lKursP);

        tfKursP = new JTextField("1");
        tfKursP.setBounds(x + 200, y + 75, 60, 20);
        mainPanel.add(tfKursP);

        lSkidka = new JLabel("Скидка по договору");
        lSkidka.setBounds(x + 10, y + 100, 250, 20);
        mainPanel.add(lSkidka);

        tfSkidka = new JTextField("0");
        tfSkidka.setBounds(x + 170, y + 100, 40, 20);
        mainPanel.add(tfSkidka);*/

        bPrint = new JButton("Печать");
        bPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkDate(tfDateProt.getText().trim()) && checkDate(tfDateTTN.getText().trim())) {
                    Vector v = new Vector();
                    Vector param = new Vector();

                    DB db = new DB();
                    try {
                        v = db.getProtocolSoglas(ndoc.toString());
                        param.add("ТТН №" + ndoc + " от " + tfDateTTN.getText().trim());
                        param.add(Float.parseFloat(tfKurs.getText().trim()));
                        param.add(client);
                        param.add(((Vector) v.get(1)).get(8));
                        param.add(((Vector) v.get(1)).get(7));

                    } catch (Exception er) {
                        JOptionPane.showMessageDialog(null, "Ошибка при nbkj: " + er.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        System.err.println(er);
                    } finally {
                        dispose();
                    }
                    if (tfNProt.getText().trim().equals(""))
                        tfNProt.setText("__________");
                    OpenOffice oo = new OpenOffice("Протокол согласования цен №" + tfNProt.getText() + " от " + tfDateProt.getText().trim() + " года", v, param);
                    oo.createReport("протоколСогласования.ots");
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
