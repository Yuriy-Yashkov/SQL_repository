/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sbit;

import dept.sbit.model.SubTypeProduct;
import dept.sklad.model.ClientCombo;
import dept.sklad.model.Dogovor;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author Andy
 */
@SuppressWarnings("serial")
public class EditTradeAllowance extends JDialog {

    private JPanel jpProduct, jpStore, jpButton;
    private JLabel jlKnitwearAdult, jlKnitwearBaby, jlStockingAdult,
            jlStockingBaby;
    private JFormattedTextField jtfKnitwearAdult, jtfKnitwearBaby,
            jtfStockingAdult, jtfStockingBaby;
    private JButton jbOk, jbCancel;
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private SubTypeProduct nowTradeAllowance;
    private LogicTradeAllowance lta;
    private MaskFormatter formatter;
    private ClientCombo clientList;

    public EditTradeAllowance(JFrame parent) {
        super(parent);
        initComponent();
        setPropertyComponent();
        setDataForComponent();
        setPropertyForm();
    }

    private void setDataForComponent() {
        lta = new LogicTradeAllowance();
        nowTradeAllowance = new SubTypeProduct(
                lta.getAllowsByClientId(clientList.getId()));
        jtfKnitwearAdult.setValue(Integer.toString(nowTradeAllowance
                .getAllowanceKnitwearAdult()));
        jtfKnitwearBaby.setValue(Integer.toString(nowTradeAllowance
                .getAllowanceKnitwearBaby()));
        jtfStockingAdult.setValue(Integer.toString(nowTradeAllowance
                .getAllowanceStockingAdult()));
        jtfStockingBaby.setValue(Integer.toString(nowTradeAllowance
                .getAllowanceStockingBaby()));
    }

    private void initComponent() {
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();

        try {
            formatter = new MaskFormatter("##");
        } catch (Exception e) {
            System.err
                    .println("Ошибка создания формата для компонентов торговой надбавки "
                            + e);
        }
        formatter.setPlaceholderCharacter('0');
        jpProduct = new JPanel(gbl);
        jpStore = new JPanel(null);
        jpButton = new JPanel(new FlowLayout());
        jlKnitwearAdult = new JLabel("Трикотаж (вз. 1,2 сорт),% :");
        jlKnitwearAdult.setPreferredSize(new Dimension(10, 50));
        jlStockingAdult = new JLabel("Чул.-Нос. (вз. сортные),% :");
        jlStockingAdult.setPreferredSize(new Dimension(10, 50));
        jlKnitwearBaby = new JLabel("Трикотаж (дет. 1,2 сорт),% :");
        jlKnitwearBaby.setPreferredSize(new Dimension(10, 50));
        jlStockingBaby = new JLabel("Чул.Нос. (дет. сортные),% :");
        jlStockingBaby.setPreferredSize(new Dimension(10, 50));
        jtfKnitwearAdult = new JFormattedTextField(formatter);

        jtfKnitwearAdult.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfStockingAdult = new JFormattedTextField(formatter);
        jtfStockingAdult.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfKnitwearBaby = new JFormattedTextField(formatter);
        jtfKnitwearBaby.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfStockingBaby = new JFormattedTextField(formatter);
        jtfStockingBaby.setHorizontalAlignment(SwingConstants.RIGHT);

        ArrayList<Dogovor> clients = new ArrayList<Dogovor>();
        clients.add(new Dogovor(804, "31 павильон"));
        clients.add(new Dogovor(781, "Валентина"));
        clients.add(new Dogovor(813, "Элегия"));
        clients.add(new Dogovor(909, "Светлогорск"));
        clients.add(new Dogovor(988, "Янина"));
        clients.add(new Dogovor(2938, "Филиал 6"));
        clients.add(new Dogovor(3194, "Филиал Мозырь"));

        clientList = new ClientCombo(clients);
        clientList.setBounds(5, 5, 355, 20);
        clientList.setSelectedIndex(0);

        jpStore.add(clientList);

        jbOk = new JButton("Изменить");
        jbOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int aKA, aSA, aKB, aSB;
                aSA = Integer.parseInt(jtfKnitwearAdult.getText());
                aKA = Integer.parseInt(jtfStockingAdult.getText());
                aKB = Integer.parseInt(jtfKnitwearBaby.getText());
                aSB = Integer.parseInt(jtfStockingBaby.getText());
                SubTypeProduct stp = new SubTypeProduct(aKA, aSA, aKB, aSB);
                lta.setAllowsByClientId(clientList.getId(), stp);
                JOptionPane.showMessageDialog(null, "Торговые надбавки изменены.", "Выполнено", JOptionPane.ERROR_MESSAGE);
            }
        });

        jbCancel = new JButton("Закрыть");
        jbCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        clientList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDataForComponent();
            }
        });
    }

    private void setPropertyComponent() {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 3, 3, 3);
        jpProduct.add(jlKnitwearAdult, gbc);
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        jpProduct.add(jtfKnitwearAdult, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpProduct.add(jlKnitwearBaby, gbc);
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        jpProduct.add(jtfKnitwearBaby, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpProduct.add(jlStockingAdult, gbc);
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        jpProduct.add(jtfStockingAdult, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpProduct.add(jlStockingBaby, gbc);
        gbc.gridx = 5;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        jpProduct.add(jtfStockingBaby, gbc);
        jpButton.add(jbOk);
        jpButton.add(jbCancel);
        setLayout(gbl);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        add(jpStore, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(jpProduct, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridheight = 1;
        add(jpButton, gbc);
    }

    private void setPropertyForm() {
        setTitle("Изменение торговой надбавки");
        setSize(new Dimension(370, 270));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }


    public JTextField getJtfKnitwearAdult() {
        return jtfKnitwearAdult;
    }

    public JTextField getJtfKnitwearBaby() {
        return jtfKnitwearBaby;
    }

    public JTextField getJtfStockingAdult() {
        return jtfStockingAdult;
    }

    public JTextField getJtfStockingBaby() {
        return jtfStockingBaby;
    }
}
