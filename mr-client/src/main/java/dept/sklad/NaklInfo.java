/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 *
 * @author DBozhkou
 */
public class NaklInfo extends JDialog {

    JLabel lbNomberDoc;
    JLabel lbUser;
    JLabel lbSumma;
    JLabel lbSummaNds;
    JLabel lbSummaAll;
    JLabel lbDateClosing;

    JLabel tfUser;
    JLabel tfSumma;
    JLabel tfSummaNds;
    JLabel tfSummaAll;
    JLabel tfDateClosing;

    JTextField tfNomberDoc;

    JButton btnFound;
    JButton btnClose;

    JPanel jpInfo;
    JPanel jpButton;
    JPanel jpAllComtent;

    public NaklInfo(JFrame parent) {
        super(parent);
        this.InitializationComp();
        this.setSize(850, 115);
        this.setTitle("Информация по накладной");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public void InitializationComp() {

        this.jpInfo = new JPanel(new GridLayout(2, 12));
        this.jpButton = new JPanel(new FlowLayout());
        this.jpAllComtent = new JPanel();

        this.lbNomberDoc = new JLabel("Номер документа:");
        this.tfNomberDoc = new JTextField();

        this.lbSumma = new JLabel("Сумма:");
        this.tfSumma = new JLabel();

        this.lbSummaNds = new JLabel("Сумма НДС:");
        this.tfSummaNds = new JLabel();

        this.lbSummaAll = new JLabel("Сумма с НДС:");
        this.tfSummaAll = new JLabel();

        this.lbDateClosing = new JLabel("Дата закрытия:");
        this.tfDateClosing = new JLabel();

        this.lbUser = new JLabel("Пользователь:");
        this.tfUser = new JLabel();

        this.btnFound = new JButton("Найти");
        this.btnFound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SkladDB sdb = new SkladDB();
                HashMap hm = sdb.getInfoNakl(tfNomberDoc.getText());
                if (!hm.get("summa").toString().equals("null")) {
                    tfSumma.setText(hm.get("summa").toString());
                    tfSummaNds.setText(hm.get("summaNds").toString());
                    tfSummaAll.setText(hm.get("summaAll").toString());
                    tfDateClosing.setText(hm.get("DateClosing").toString().substring(0, 10));
                    tfUser.setText(hm.get("User").toString());
                } else {
                    JOptionPane.showMessageDialog(null, "Проверьте номер накладной");
                }
            }
        });

        this.btnClose = new JButton("Выйти");
        this.btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.jpInfo.add(this.lbNomberDoc);
        this.jpInfo.add(this.tfNomberDoc);
        this.jpInfo.add(this.lbSumma);
        this.jpInfo.add(this.tfSumma);
        this.jpInfo.add(this.lbSummaNds);
        this.jpInfo.add(this.tfSummaNds);
        this.jpInfo.add(this.lbSummaAll);
        this.jpInfo.add(this.tfSummaAll);
        this.jpInfo.add(this.lbDateClosing);
        this.jpInfo.add(this.tfDateClosing);
        this.jpInfo.add(this.lbUser);
        this.jpInfo.add(this.tfUser);
        this.jpButton.add(this.btnFound);
        this.jpButton.add(this.btnClose);
        this.jpAllComtent.add(this.jpInfo);
        this.jpAllComtent.add(this.jpButton);

        this.add(this.jpAllComtent);

        pack();
    }
}
