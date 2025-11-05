/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad.addNakladnaya;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 *
 * @author user
 */
public class addPryazhaNakl extends JDialog {

    JTextField tfNomDoc;
    JTextField tfShifrArt;

    JTextField tfMassaKondition;
    JTextField tfMassaSurovaya;
    JLabel lbShifrArt;
    JLabel lbMassaKondition;
    JLabel lbMassaSurovaya;
    JLabel lbNomDoc;
    JLabel lbHeader;
    JButton btnDocSel;
    JButton btnShifrArt;
    JButton btnClose;
    JButton btnSelectDoc;
    JButton btnAdd;

    JPanel jpContent;
    JPanel jpButton;
    JPanel jpHeader;
    addPryazhaNakl paren;
    JPanel jpForm;
    HashMap hmm;

    public addPryazhaNakl(JFrame parent) {
        super(parent);
        paren = this;
        initComp();
    }

    public void initComp() {
        jpForm = new JPanel(new BorderLayout());
        jpButton = new JPanel(new FlowLayout());
        jpContent = new JPanel(new GridLayout(4, 5));
        jpHeader = new JPanel(new FlowLayout());
        tfMassaKondition = new JTextField();
        tfMassaSurovaya = new JTextField();
        tfShifrArt = new JTextField();
        tfNomDoc = new JTextField();
        lbMassaKondition = new JLabel("Масса до крашения");
        lbMassaSurovaya = new JLabel("Масса после крашения");
        lbHeader = new JLabel("Добавить пряжу в спецификацию");
        lbShifrArt = new JLabel("Шифр арт.");
        lbNomDoc = new JLabel("Нормер накладной");
        btnDocSel = new JButton("Выбрать из списка");
        btnSelectDoc = new JButton("Добавить");
        btnShifrArt = new JButton("Выбрать из справ.");
        btnShifrArt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new browseDescrIzd(paren, "1");
            }
        });
        btnDocSel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new browseDescrIzd(paren, "2");
            }
        });

        btnClose = new JButton("Закрыть");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        jpContent.add(lbNomDoc);
        jpContent.add(tfNomDoc);
        jpContent.add(btnDocSel);
        jpContent.add(lbShifrArt);
        jpContent.add(tfShifrArt);
        jpContent.add(btnShifrArt);
        jpContent.add(lbMassaKondition);
        jpContent.add(tfMassaKondition);
        jpContent.add(lbMassaSurovaya);
        jpContent.add(tfMassaSurovaya);
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpContent.add(new JLabel());
        jpButton.add(btnSelectDoc);
        jpButton.add(btnClose);
        jpHeader.add(lbHeader);
        jpForm.add(jpHeader, BorderLayout.PAGE_START);
        jpForm.add(jpContent, BorderLayout.CENTER);
        jpForm.add(jpButton, BorderLayout.PAGE_END);
        this.add(jpForm);
        pack();
        this.setSize(1000, 180);
        this.setTitle("Редактор спецификации для пряжи");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

}
