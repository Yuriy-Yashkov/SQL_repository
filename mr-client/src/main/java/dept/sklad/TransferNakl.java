/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad;

import common.ProgressBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author user
 */
public class TransferNakl extends JDialog {

    private JLabel lbNumberOldNakl;
    private JLabel lbNumberNewNakl;

    private JTextField tfNumberOldNakl;
    private JTextField tfNumberNewNakl;

    private JButton btnExit;
    private JButton btnPerenos;

    private JPanel jpButton;
    private JPanel jpContent;
    private JPanel jpAllForm;
    private ProgressBar pbVariable;
    private JFrame paren;
    private SkladDB sdb;

    public TransferNakl(JFrame parent) {
        super(parent);
        paren = parent;
        this.InitializationComp();
        this.setSize(350, 130);
        this.setTitle("Перенос спецификации");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public void InitializationComp() {

        jpButton = new JPanel(new FlowLayout());
        jpContent = new JPanel(new FlowLayout());
        jpAllForm = new JPanel(new GridLayout(2, 1));

        lbNumberOldNakl = new JLabel("Номер возвратной накладной");
        tfNumberOldNakl = new JTextField();
        tfNumberOldNakl.setColumns(10);

        lbNumberNewNakl = new JLabel("Номер новой накладной");
        tfNumberNewNakl = new JTextField();
        tfNumberNewNakl.setColumns(10);

        btnPerenos = new JButton("Перенести");
        btnPerenos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pbVariable = new ProgressBar(paren, false, "");
                SWorker sw = new SWorker();
                sw.execute();
                pbVariable.setVisible(true);
            }
        });

        btnExit = new JButton("Закрыть");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        jpContent.add(lbNumberOldNakl);
        jpContent.add(tfNumberOldNakl);
        jpContent.add(lbNumberNewNakl);
        jpContent.add(tfNumberNewNakl);

        jpButton.add(btnPerenos);
        jpButton.add(btnExit);

        jpAllForm.add(jpContent);
        jpAllForm.add(jpButton);

        this.add(jpAllForm);

        pack();
    }

    class SWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            sdb = new SkladDB();
            if (sdb.transfNakl(tfNumberOldNakl.getText(), tfNumberNewNakl.getText())) {
                JOptionPane.showMessageDialog(null, "Успешно!");
            } else {
                JOptionPane.showMessageDialog(null, "Ошибка выполнения.\nСвяжитесь с разработчиком!");
            }
            return null;
        }

        @Override
        protected void done() {
            try {
                pbVariable.dispose();
            } catch (Exception ex) {
                System.err.println("Ошибка при получении результатов из фонового потока " + ex);
            }
        }
    }
}
