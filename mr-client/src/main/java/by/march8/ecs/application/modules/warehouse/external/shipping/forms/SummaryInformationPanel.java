package by.march8.ecs.application.modules.warehouse.external.shipping.forms;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 01.03.2016.
 */
public class SummaryInformationPanel extends JPanel {
    private JLabel lblCurrencyName = new JLabel("Валюта");
    private JLabel lblSumCost = new JLabel("Стоимость");
    private JLabel lblSumVat = new JLabel("Сумма НДС");
    private JLabel lblSumCostAndVat = new JLabel("Стоимость с НДС");

    private JLabel lblCurrencyNameValue = new JLabel("0");
    private JLabel lblSumCostValue = new JLabel("0");
    private JLabel lblSumVatValue = new JLabel("0");
    private JLabel lblSumCostAndVatValue = new JLabel("0");


    public SummaryInformationPanel() {
        super(new MigLayout());
        add(lblCurrencyName, "width 140:20:140");
        add(lblCurrencyNameValue, "width 140:20:140, wrap");

        add(new JPanel(), "height 10:10,  wrap");

        add(lblSumCost, "width 140:20:140");
        add(lblSumCostValue, "width 140:20:140, wrap");

        add(lblSumVat, "width 140:20:140");
        add(lblSumVatValue, "width 140:20:140, wrap");

        add(lblSumCostAndVat, "width 140:20:140");
        add(lblSumCostAndVatValue, "width 140:20:140, wrap");
    }

    public void setCurrencyName(String s) {
        lblCurrencyNameValue.setForeground(Color.black);
        lblCurrencyNameValue.setText(s);
    }

    public void setSumCostValue(String s) {
        lblSumCostValue.setText(s);
    }

    public void setSumVatValue(String s) {
        lblSumVatValue.setText(s);
    }

    public void setSumCostAndVatValue(String s) {
        lblSumCostAndVatValue.setText(s);
    }

    public JLabel getLblCurrencyName() {
        return lblCurrencyName;
    }

    public void wrongCurrencyRate() {
        lblCurrencyNameValue.setForeground(Color.RED);
    }
}
