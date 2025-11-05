package dept.upack;

import com.toedter.calendar.JDateChooser;
import lombok.SneakyThrows;
import workDB.DB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Vector;

public class DataForm extends javax.swing.JDialog {
    final DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
    MaskFormatter formatter;
    private String sDate;
    private String eDate;
    private int type = 0;
    private JDateChooser dateChooser;
    private JDateChooser dateChooser2;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    public DataForm(JFrame parent, boolean modal, int type) {
        this.type = type;
       /* try{
            formatter = new MaskFormatter("##.##.####");
        }catch(Exception e){
            System.err.println("Ошибка: " +e);
        }
        formatter.setPlaceholderCharacter('0');*/
        Calendar c = Calendar.getInstance();
        dateChooser = new JDateChooser(c.getTime());
        dateChooser2 = new JDateChooser(c.getTime());
        initComponents();
        getContentPane().add(dateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 100, 20));
        getContentPane().add(dateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 100, 20));
        this.setTitle("Учёт на упаковке");
        this.setLocation(160, 120);
        this.setSize(340, 240);

        switch (type) {
            case 1:
                jLabel1.setText("Показать cдачу на склад за период:");
                jTextField1.setVisible(true);
                jLabel4.setVisible(true);
                break;
            case 2:
                jLabel1.setText("Показать приход на упаковку за период:");
                jTextField1.setVisible(false);
                jLabel4.setVisible(false);
                break;
            case 3:
                jLabel1.setText("Показать остатки за период:");
                jTextField1.setVisible(false);
                jLabel4.setVisible(false);
                break;
            case 4:
                jLabel1.setText("Сдача по моделям за период:");
                jTextField1.setVisible(true);
                jLabel4.setVisible(true);
                break;

        }
// ------------- Получаем текущую дату---------------------
      /*  Calendar c = Calendar.getInstance();
        DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
        eDate = new String(df.format(c.getTime()));
        int i = c.get(Calendar.MONTH)+1;
        String month = new String();
        if(i < 10){
            month = "0" + i;
        }else month = Integer.toString(i);
        sDate = new String("01." + month +"."+ c.get(Calendar.YEAR));
        jFormattedTextField1.setText(eDate);*/

//        jFormattedTextField2.setText(eDate);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField(formatter);
        jFormattedTextField2 = new javax.swing.JFormattedTextField(formatter);
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Показать cдaчу на склад за период:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel2.setText("с");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, -1, -1));

        jLabel3.setText("по");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, -1, -1));

        jLabel4.setText("номер ячейки");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, -1));

        jButton1.setText("Показать");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, -1, -1));
        getContentPane().add(jFormattedTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, -1, -1));
        getContentPane().add(jFormattedTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, -1));

        jTextField1.setColumns(4);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 120, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @SneakyThrows
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int f = 0;
        int n = 0;
        if (checkDate(df.format(dateChooser.getDate())) && checkDate(df.format(dateChooser2.getDate()))) {
            sDate = (df.format(dateChooser.getDate()));
            eDate = (df.format(dateChooser2.getDate()));
            if (jTextField1.getText().trim().length() == 0) {
                n = 0;
            } else if (jTextField1.getText().trim().equals("game".toString())) {
                jTextField1.setText("0");
                Runtime r = Runtime.getRuntime();
                Process p = null;
                try {
                    p = r.exec("C:\\WINDOWS\\system32\\sol.exe ");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Проблемка... " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } else
                try {
                    n = Integer.parseInt(jTextField1.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка при вводне номера ячейки", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            if (type == 4) {
                Vector v;
                DB db = new DB();
                v = db.sdachaNaSkladPoModel(sDate, eDate, n);
                OpenOffice oo = new OpenOffice("Сдача на склад по моделям c " + sDate + " по " + eDate, v);
                oo.createReport("UpackPoModel.ots");
            }
            if (type == 3) {
                new TableForm(this, true, sDate, eDate, type, n); // TableForm
            } else {
                new TableFormNew(this, true, sDate, eDate, type, n); // TableForm
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ошибка при вводе даты", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            return false;
        }
        return true;
    }
    // End of variables declaration//GEN-END:variables

}
