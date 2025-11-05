package dept.upack.ns;

import com.toedter.calendar.JDateChooser;
import common.PanelWihtFone;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.DateFormat;
import java.util.Calendar;

/**
 *
 * @author vova
 * @date 15.11.2011
 */
public class DataForm extends JDialog {
    MaskFormatter formatter;
    MaskFormatter formatter2;
    JDialog th;
    //    static String sDate;
//    static String eDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    //    private javax.swing.JFormattedTextField jFormattedTextField1;
//    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private PanelWihtFone mainPanel;

    public DataForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        th = this;
       /* try{
            formatter = new MaskFormatter("##.##.####");
        }catch(Exception e){
            System.err.println("Ошибка: " +e);
        }
        formatter.setPlaceholderCharacter('0');
        try{
            formatter2 = new MaskFormatter("##.##.####");
        }catch(Exception e){
            System.err.println("Ошибка: " +e);
        }
        formatter2.setPlaceholderCharacter('0');*/
        initComponents();
        this.setTitle("Период просмотра");

        add(mainPanel);
        setSize(320, 190);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
//        jFormattedTextField1 = new javax.swing.JFormattedTextField(formatter);
//        jFormattedTextField2 = new javax.swing.JFormattedTextField(formatter2);
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        Calendar c = Calendar.getInstance();
        final DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
/*        eDate = df.format(c.getTime());
        int i = c.get(Calendar.MONTH)+1;
        String month = new String();
        if(i < 10){
            month = "0" + i;
        }else month = Integer.toString(i);
        sDate = "01." + month +"."+ c.get(Calendar.YEAR);
        jFormattedTextField1.setText(sDate);
        jFormattedTextField1.setBounds(140, 50, 80, 20);
        mainPanel.add(jFormattedTextField1);
        jFormattedTextField2.setText(eDate);
        jFormattedTextField2.setBounds(140, 80, 80, 20);
        mainPanel.add(jFormattedTextField2);*/

        java.util.Date d = c.getTime();
        d.setDate(1);
        final JDateChooser dateChooser = new JDateChooser(d);
        dateChooser.setBounds(120, 50, 100, 20);
        mainPanel.add(dateChooser);

        final JDateChooser dateChooser2 = new JDateChooser(c.getTime());
        dateChooser2.setBounds(120, 80, 100, 20);
        mainPanel.add(dateChooser2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Данные по н/с за период:");
        jLabel1.setBounds(60, 20, 300, 20);
        mainPanel.add(jLabel1);

        jLabel2.setText("с");
        jLabel2.setBounds(100, 50, 20, 20);
        mainPanel.add(jLabel2);

        jLabel3.setText("по");
        jLabel3.setBounds(100, 80, 20, 20);
        mainPanel.add(jLabel3);

        jButton1.setText("Маршруты");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    /*if(UtilFunctions.checkDate(jFormattedTextField1.getText()) && UtilFunctions.checkDate(jFormattedTextField2.getText())){
                        new MarhListForm(th, true, jFormattedTextField1.getText(), jFormattedTextField2.getText());
                    }*/
                    if (UtilFunctions.checkDate(df.format(dateChooser.getDate())) && UtilFunctions.checkDate(df.format(dateChooser2.getDate()))) {
                        new MarhListForm(th, true, df.format(dateChooser.getDate()), df.format(dateChooser2.getDate()));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка преобразования даты", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jButton1.setBounds(40, 120, 120, 20);
        mainPanel.add(jButton1);

        jButton2.setText("Изделия");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (UtilFunctions.checkDate(df.format(dateChooser.getDate())) && UtilFunctions.checkDate(df.format(dateChooser2.getDate()))) {
                        new ItemListForm(th, true, df.format(dateChooser.getDate()), df.format(dateChooser2.getDate()));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка преобразования даты", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jButton2.setBounds(170, 120, 120, 20);
        mainPanel.add(jButton2);

    }
} 