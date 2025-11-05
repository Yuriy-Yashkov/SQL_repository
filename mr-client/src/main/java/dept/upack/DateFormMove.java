/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.upack;

import com.toedter.calendar.JDateChooser;
import common.PanelWihtFone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author user
 */
public class DateFormMove extends JDialog {
    final DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
    private JPanel mainPanel;
    private String articleProduction;
    private String beginDate;
    private String endDate;
    private JLabel beginDateJL;
    private JLabel endDateJL;
    private JLabel articleProductionJL;
    private JLabel nameArtCB;
    private JCheckBox articleProductionCB;
    private JTextField articleProductionTF;
    private JDateChooser beginDateDC;
    private JDateChooser endDateDC;
    private JButton okButton;
    private JButton closeButon;
    private JPanel basePanel;

    public DateFormMove(JFrame parent, boolean modal) {
        Calendar c = Calendar.getInstance();
        beginDateDC = new JDateChooser(c.getTime());
        endDateDC = new JDateChooser(c.getTime());

        initComponents();

        this.setTitle("Отчет по перемещению");
        this.setLocation(160, 120);
        this.setSize(250, 180);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        int x = 10;
        int y = 10;
        mainPanel = new PanelWihtFone();

        articleProductionJL = new JLabel("Артикул:");
        articleProductionTF = new JTextField();
        articleProductionTF.setFocusable(false);

        articleProductionTF.setBackground(mainPanel.getBackground());
        articleProductionJL.setBounds(x + 10, y, 65, 20);
        articleProductionTF.setBounds(x + 80, y, 150, 20);
        mainPanel.add(articleProductionTF);
        mainPanel.add(articleProductionJL);
        articleProductionCB = new JCheckBox();
        nameArtCB = new JLabel("по артикулу");
        articleProductionCB.setBounds(x + 75, y + 25, 20, 20);
        nameArtCB.setBounds(x + 95, y + 25, 105, 20);
        articleProductionCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (articleProductionCB.isSelected()) {
                    articleProductionTF.setFocusable(true);
                    articleProductionTF.setBackground(Color.WHITE);
                } else {
                    articleProductionTF.setFocusable(false);
                    articleProductionTF.setText("");
                    articleProductionTF.setBackground(mainPanel.getBackground());
                }
            }
        });
        mainPanel.add(articleProductionCB);
        mainPanel.add(nameArtCB);
        beginDateJL = new JLabel("c");
        beginDateJL.setBounds(x + 50, y + 50, 30, 20);
        beginDateDC.setBounds(x + 80, y + 50, 120, 20);
        endDateJL = new JLabel("по:");
        endDateJL.setBounds(x + 50, y + 75, 30, 20);
        endDateDC.setBounds(x + 80, y + 75, 120, 20);
        mainPanel.add(beginDateDC);
        mainPanel.add(endDateDC);
        mainPanel.add(beginDateJL);
        mainPanel.add(endDateJL);
        okButton = new JButton("Ок");
        okButton.setBounds(x + 40, y + 105, 90, 20);
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        closeButon = new JButton("Отмена");
        closeButon.setBounds(x + 140, y + 105, 90, 20);
        closeButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        mainPanel.add(okButton);
        mainPanel.add(closeButon);

        this.add(mainPanel);
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Vector v = new Vector();
        boolean flagError = true;
        if (articleProductionCB.isSelected()) {
            if (articleProductionTF.getText().toString().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Не задан артикул", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                flagError = false;
            }
            if (checkDate(df.format(beginDateDC.getDate())) && checkDate(df.format(endDateDC.getDate()))) {
                beginDate = (df.format(beginDateDC.getDate()));
                endDate = (df.format(endDateDC.getDate()));
            } else {
                JOptionPane.showMessageDialog(null, "Не правильно задана дата", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                flagError = false;
            }
            if (flagError) {
                new TableFormMove(this, true, articleProductionTF.getText().toString().trim(), beginDate, endDate);
            }
        } else {
            if (checkDate(df.format(beginDateDC.getDate())) && checkDate(df.format(endDateDC.getDate()))) {
                beginDate = (df.format(beginDateDC.getDate()));
                endDate = (df.format(endDateDC.getDate()));
            } else {
                JOptionPane.showMessageDialog(null, "Не правильно задана дата", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                flagError = false;
            }
            if (flagError) {
                new TableFormMove(this, true, beginDate, endDate);
            }
        }
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
            return false;
        }
        return true;
    }

}