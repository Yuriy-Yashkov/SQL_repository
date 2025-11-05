package dept.sprav.valuta;

import common.PanelWihtFone;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 *
 * @author vova
 */
public class EditValutaForm extends JDialog {

    String name;
    String fullName;
    String symbol;
    String about;
    HashMap valuta;
    int x = 10;
    int y = 10;
    private PanelWihtFone mainPanel;
    private JLabel lName;
    private JLabel lFullName;
    private JLabel lSymbol;
    private JLabel lAbout;
    private JTextField fName;
    private JTextField fFullName;
    private JTextField fSymbol;
    private JTextField fAbout;
    private JButton bEdit;

    public EditValutaForm(JDialog parent, boolean f, int id) {
        super(parent, f);
        setTitle("Валюта");

        ValutaPDB db = new ValutaPDB();
        valuta = db.getValuta(id);
        db.disConn();

        initComponents();

        if (id == 0) {
            bEdit.setText("Добавить");
        } else {
            bEdit.setText("Изменить");
        }

        add(mainPanel);
        setSize(350, 200);
        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        lName = new JLabel("Краткое имя:");
        lName.setBounds(x, y, 150, 20);
        mainPanel.add(lName);

        lFullName = new JLabel("Полное имя:");
        lFullName.setBounds(x, y + 25, 150, 20);
        mainPanel.add(lFullName);

        lSymbol = new JLabel("Обозначение:");
        lSymbol.setBounds(x, y + 50, 150, 20);
        mainPanel.add(lSymbol);

        lAbout = new JLabel("Примечание:");
        lAbout.setBounds(x, y + 75, 150, 20);
        mainPanel.add(lAbout);

        fName = new JTextField(valuta.get("name").toString());
        fName.setBounds(x + 155, y, 150, 20);
        mainPanel.add(fName);

        fFullName = new JTextField(valuta.get("full_name").toString());
        fFullName.setBounds(x + 155, y + 25, 150, 20);
        mainPanel.add(fFullName);

        fSymbol = new JTextField(valuta.get("symbol").toString());
        fSymbol.setBounds(x + 155, y + 50, 150, 20);
        mainPanel.add(fSymbol);

        fAbout = new JTextField(valuta.get("about").toString());
        fAbout.setBounds(x + 155, y + 75, 150, 20);
        mainPanel.add(fAbout);


        bEdit = new JButton();
        bEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                valuta.put("name", fName.getText().trim());
                valuta.put("full_name", fFullName.getText().trim());
                valuta.put("symbol", fSymbol.getText().trim());
                valuta.put("about", fAbout.getText().trim());
                ValutaPDB db = new ValutaPDB();
                if (checkData(valuta)) db.setValuta(valuta);
                db.disConn();
                dispose();
            }
        });
        bEdit.setBounds(x + 90, y + 120, 120, 25);
        mainPanel.add(bEdit);
    }

    private boolean checkData(HashMap v) {
        boolean f = false;
        if (v.get("name").toString().length() == 0 || v.get("full_name").toString().length() == 0) {
            JOptionPane.showMessageDialog(null, "Имя не должно быть пустым", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } else f = true;
        return f;
    }
}
