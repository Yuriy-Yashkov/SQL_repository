package dept;

import common.PanelWihtFone;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author vova
 */
public class AboutForm extends javax.swing.JDialog {

    int x = 10;
    int y = 10;

    String version = new String(" 13.10.29.01");
    JLabel imgLabel;
    JLabel aboutLabel;
    JLabel aboutL1;
    JLabel aboutL2;
    JLabel aboutL3;
    JLabel nameProg;
    JLabel autorLabel;
    JLabel autorL1;
    JLabel helpLabel;
    JLabel helpL1;
    JButton ok;
    PanelWihtFone fp;

    public AboutForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        //initComponents();
        initComp();
        this.setTitle("О программе MyReports");
        setContentPane(fp);
        setSize(480, 300);
        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public void initComp() {
        fp = new PanelWihtFone();
        imgLabel = new JLabel();
        ImageIcon icon1 = new ImageIcon(MyReportsModule.progPath + "/Img/kot.jpeg");
        imgLabel.setText("");
        imgLabel.setIcon(icon1);
        imgLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        imgLabel.setBounds(x, y, 147, 145);
        fp.add(imgLabel);

        aboutLabel = new JLabel();
        aboutLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        aboutLabel.setText("О программе:");
        aboutLabel.setBounds(x + 165, y, 120, 15);
        fp.add(aboutLabel);

        nameProg = new JLabel();
        nameProg.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        nameProg.setText("MyReports" + version);
        nameProg.setBounds(x + 195, y + 25, 170, 15);
        fp.add(nameProg);

        aboutL1 = new JLabel();
        aboutL1.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        aboutL1.setText("Программа предназначена для");
        aboutL1.setBounds(x + 195, y + 40, 250, 15);
        fp.add(aboutL1);

        aboutL2 = new JLabel();
        aboutL2.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        aboutL2.setText("автоматизации и упрощения");
        aboutL2.setBounds(x + 195, y + 55, 250, 15);
        fp.add(aboutL2);

        aboutL3 = new JLabel();
        aboutL3.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        aboutL3.setText("работы сотрудников предприятия");
        aboutL3.setBounds(x + 195, y + 70, 250, 15);
        fp.add(aboutL3);

        autorLabel = new JLabel();
        autorLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        autorLabel.setText("Для вас старались:");
        autorLabel.setBounds(x + 165, y + 95, 150, 15);
        fp.add(autorLabel);

        autorL1 = new JLabel();
        autorL1.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        autorL1.setText("Шляга Владимир;");
        autorL1.setBounds(x + 195, y + 115, 250, 15);
        fp.add(autorL1);

        autorL1 = new JLabel();
        autorL1.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        autorL1.setText("Пархоменко Лидия;");
        autorL1.setBounds(x + 195, y + 130, 250, 15);
        fp.add(autorL1);

        autorL1 = new JLabel();
        autorL1.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        autorL1.setText("Божков Дмитрий;");
        autorL1.setBounds(x + 195, y + 145, 250, 15);
        fp.add(autorL1);

        helpLabel = new JLabel();
        helpLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        helpLabel.setText("Поддержка:");
        helpLabel.setBounds(x + 165, y + 170, 150, 15);
        fp.add(helpLabel);

        helpL1 = new JLabel();
        helpL1.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        helpL1.setText("Разработка и поддержка 2009-" + new SimpleDateFormat("yyyy").format((Calendar.getInstance()).getTime()));
        helpL1.setBounds(x + 195, y + 190, 270, 15);
        fp.add(helpL1);

        ok = new JButton();
        ok.setText("Ok");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
        ok.setBounds(x + 185, y + 220, 80, 30);
        fp.add(ok);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
