/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.nsi;

import common.PanelWihtFone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author user
 */
public class EditDirectoryCodeForeignEconomicRelations extends JDialog {
    JLabel lKod;
    JLabel lSostav;
    JLabel lNaim;
    long kolOld;
    String sostavOld;
    String naimOld;
    JTextField tfKod;
    JTextField tfSostav;
    JTextField tfNaim;
    JButton bOk, bClose;
    JPanel mainPanel;
    DirectoryCodeForeignEconomicRelations myForm;

    public EditDirectoryCodeForeignEconomicRelations(DirectoryCodeForeignEconomicRelations parentF, boolean f) {
        super(parentF, f);
        myForm = parentF;
        initComponents();
        bOk.addActionListener(new EditDirectoryCodeForeignEconomicRelations.ActionOk());

        add(mainPanel);
        setTitle("Добавить");

        this.setSize(400, 200);
        this.setModal(true);
        this.setMaximumSize(new Dimension(400, 200));
        this.setMinimumSize(new Dimension(400, 200));
        setLocationRelativeTo(parentF);
        setVisible(true);

    }

    public EditDirectoryCodeForeignEconomicRelations(DirectoryCodeForeignEconomicRelations parentF, boolean f, Long kol, String sostav, String naim) {
        super(parentF, f);
        myForm = parentF;
        initComponents();
        tfKod.setText(kol.toString());
        tfSostav.setText(sostav);
        tfNaim.setText(naim);
        kolOld = kol;
        sostavOld = sostav;
        naimOld = naim;
        bOk.addActionListener(new EditDirectoryCodeForeignEconomicRelations.ActionUpdate());


        add(mainPanel);
        setTitle("Редактировать");

        this.setSize(400, 200);
        this.setModal(true);
        this.setMaximumSize(new Dimension(400, 200));
        this.setMinimumSize(new Dimension(400, 200));
        setLocationRelativeTo(parentF);
        setVisible(true);

    }


    public void initComponents() {
        viewForm();
    }

    public void viewForm() {
        mainPanel = new PanelWihtFone();

        lKod = new JLabel("Код:");
        lSostav = new JLabel("Состав:");
        lNaim = new JLabel("Название:");
        tfKod = new JTextField(25);
        tfSostav = new JTextField(25);
        tfNaim = new JTextField(25);
        bOk = new JButton("Ок");
        bClose = new JButton("Отмена");
        bClose.addActionListener(new EditDirectoryCodeForeignEconomicRelations.ActionClose());


        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));


        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel panel5 = new JPanel(new GridLayout(1, 2, 5, 5));


        panel1.add(lKod);
        panel1.add(Box.createHorizontalStrut(32));
        panel1.add(tfKod);
        panel.add(panel1);
        panel2.add(lSostav);
        panel2.add(Box.createHorizontalStrut(14));
        panel2.add(tfSostav);
        panel.add(panel2);
        panel3.add(lNaim);
        panel3.add(tfNaim);
        panel.add(panel3);
        panel5.add(bOk);
        panel5.add(bClose);
        panel4.add(panel5);


        panel.add(panel4, BorderLayout.SOUTH);


        mainPanel.add(panel);

    }

    private void ActionOkButton(java.awt.event.ActionEvent evt) {
        boolean flagError = true;
        NsiDB db = new NsiDB();
        if (db.getEqualsKod(tfKod.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Запись с таким кодом уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            flagError = false;
        }
        if (tfKod.getText().length() > 10) {
            JOptionPane.showMessageDialog(null, "Код имеет длину более 10 символов", "Ошибка", JOptionPane.ERROR_MESSAGE);
            flagError = false;
        }

        if (flagError) {
            db.addNsiWkd(tfKod.getText().trim(), tfSostav.getText().trim(), tfNaim.getText().trim());
            //JOptionPane.showMessageDialog(null, "Запись успешно добавлена", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            myForm.updateForm();
            dispose();
        }
    }

    private void ActionUpdateButton(java.awt.event.ActionEvent evt) {
        boolean flagError = true;
        NsiDB db = new NsiDB();
        if ((db.getEqualsKod(tfKod.getText().trim())) &&
                (kolOld != Long.parseLong(tfKod.getText().trim()))) {
            JOptionPane.showMessageDialog(null, "Запись с таким кодом уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            flagError = false;
        }
        if (tfKod.getText().length() > 10) {
            JOptionPane.showMessageDialog(null, "Код имеет длину более 10 символов", "Ошибка", JOptionPane.ERROR_MESSAGE);
            flagError = false;
        }
        if (flagError) {
            String str = tfSostav.getText().trim();
            db.updateNsiWkd(Long.parseLong(tfKod.getText().trim()), tfSostav.getText().trim(), tfNaim.getText().trim(), kolOld, sostavOld, naimOld);
            //JOptionPane.showMessageDialog(null, "Запись успешно изменина", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            myForm.updateForm();
            dispose();

        }
    }

    private void ActionCloseButton(java.awt.event.ActionEvent evt) {
        dispose();
    }

    class ActionOk implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ActionOkButton(e);
        }
    }

    class ActionUpdate implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ActionUpdateButton(e);
        }
    }

    class ActionClose implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ActionCloseButton(e);
        }
    }
}