package dept.marketing.cena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class PrintForm extends JDialog {
    CenaForm cenaform;
    ChangeForm changeform;
    String _flagTable;
    DefaultTableModel _tmodel;
    private JCheckBox modelCheckBox;
    private JCheckBox sarCheckBox;
    private JCheckBox artikulCheckBox;
    private JCheckBox izdCheckBox;
    private JCheckBox sortCheckBox;
    private JCheckBox rstCheckBox;
    private JCheckBox sostavCheckBox;
    private JCheckBox rzmCheckBox;
    private JCheckBox cenaPlCheckBox;
    private JCheckBox ndsCheckBox;
    private JCheckBox cenaRTCheckBox;
    private JCheckBox tnCheckBox;
    private JCheckBox cenaRozCheckBox;
    private JCheckBox sebestCheckBox;
    private JCheckBox rentCheckBox;
    private JCheckBox sebest_CheckBox;
    private JCheckBox cenaVRUBCheckBox;
    private JCheckBox cenaVEURCheckBox;
    private JCheckBox cenaVUSDCheckBox;
    private JCheckBox cenaVBCheckBox;
    private JCheckBox sebVCheckBox;
    private JCheckBox rent_CheckBox;
    private JCheckBox rCenaVsebVCheckBox;
    private JCheckBox rCenaVBsebCheckBox;
    private JCheckBox newCenaV1CCheckBox;

    public PrintForm(Dialog cform, Dialog chform, boolean modal, DefaultTableModel tmodel, HashMap<String, String> map, String flagTable) {//(CenaForm parent, boolean modal, DefaultTableModel tmodel, HashMap<String, String> map, boolean flagTableArt) {
        super(cform, modal);

        cenaform = (CenaForm) cform;
        changeform = (ChangeForm) chform;

        _flagTable = flagTable;
        _tmodel = tmodel;

        init();

        modelCheckBox.setSelected(Boolean.valueOf(map.get("Модель")));
        sarCheckBox.setSelected(Boolean.valueOf(map.get("Шифр. артикул")));
        artikulCheckBox.setSelected(Boolean.valueOf(map.get("Артикул")));
        izdCheckBox.setSelected(Boolean.valueOf(map.get("Изделие")));
        sortCheckBox.setSelected(Boolean.valueOf(map.get("Сорт")));
        rstCheckBox.setSelected(Boolean.valueOf(map.get("Рост")));
        rzmCheckBox.setSelected(Boolean.valueOf(map.get("Размер")));
        sostavCheckBox.setSelected(Boolean.valueOf(map.get("Состав сырья")));
        cenaPlCheckBox.setSelected(Boolean.valueOf(map.get("Цена плановая")));
        ndsCheckBox.setSelected(Boolean.valueOf(map.get("НДС(%)")));
        cenaRTCheckBox.setSelected(Boolean.valueOf(map.get("Цена реализации")));
        tnCheckBox.setSelected(Boolean.valueOf(map.get("ТН(%)")));
        cenaRozCheckBox.setSelected(Boolean.valueOf(map.get("Цена розничная")));
        sebestCheckBox.setSelected(Boolean.valueOf(map.get("Себестоимость")));
        rentCheckBox.setSelected(Boolean.valueOf(map.get("Рентабельность")));
        cenaVRUBCheckBox.setSelected(Boolean.valueOf(map.get("Цена в RUB")));
        cenaVUSDCheckBox.setSelected(Boolean.valueOf(map.get("ВЦ в USD")));
        cenaVEURCheckBox.setSelected(Boolean.valueOf(map.get("ВЦ в EUR")));
        sebest_CheckBox.setSelected(Boolean.valueOf(map.get("Себ-сть")));
        rent_CheckBox.setSelected(Boolean.valueOf(map.get("Рен-сть")));
        sebVCheckBox.setSelected(Boolean.valueOf(map.get("Себ. в RUB")));
        cenaVBCheckBox.setSelected(Boolean.valueOf(map.get("ВЦ в бел.руб")));
        rCenaVsebVCheckBox.setSelected(Boolean.valueOf(map.get("ВЦ - ВС")));
        rCenaVBsebCheckBox.setSelected(Boolean.valueOf(map.get("ВЦБ - С")));
        newCenaV1CCheckBox.setSelected(Boolean.valueOf(map.get("Новая цена в RUB")));

        setResizable(false);
        setLocationRelativeTo(cform);
        setVisible(true);
    }

    private void init() {
        this.setTitle("Печать");
        setPreferredSize(new Dimension(500, 260));

        JPanel osnovaPanel = new JPanel();
        JPanel panel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel panelSide = new JPanel();
        JButton buttonCancel = new JButton("Отмена");
        JButton buttonOk = new JButton("Ok");

        modelCheckBox = new JCheckBox();
        sarCheckBox = new JCheckBox();
        artikulCheckBox = new JCheckBox();
        izdCheckBox = new JCheckBox();
        sortCheckBox = new JCheckBox();
        rstCheckBox = new JCheckBox();
        rzmCheckBox = new JCheckBox();
        sostavCheckBox = new JCheckBox();
        cenaPlCheckBox = new JCheckBox();
        ndsCheckBox = new JCheckBox();
        cenaRTCheckBox = new JCheckBox();
        tnCheckBox = new JCheckBox();
        cenaRozCheckBox = new JCheckBox();
        sebestCheckBox = new JCheckBox();
        rentCheckBox = new JCheckBox();
        cenaVRUBCheckBox = new JCheckBox();
        cenaVUSDCheckBox = new JCheckBox();
        cenaVEURCheckBox = new JCheckBox();
        sebest_CheckBox = new JCheckBox();
        rent_CheckBox = new JCheckBox();
        sebVCheckBox = new JCheckBox();
        cenaVBCheckBox = new JCheckBox();
        rCenaVsebVCheckBox = new JCheckBox();
        rCenaVBsebCheckBox = new JCheckBox();
        newCenaV1CCheckBox = new JCheckBox();

        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.setLayout(new GridLayout(10, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panelSide.setLayout(new GridLayout(9, 1, 5, 5));
        panelSide.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttonPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        panel.add(new JLabel(cenaform.modelText.getText()));
        panel.add(modelCheckBox);
        panel.add(new JLabel(cenaform.ndsText.getText()));
        panel.add(ndsCheckBox);
        panel.add(new JLabel(cenaform.sarText.getText()));
        panel.add(sarCheckBox);
        panel.add(new JLabel(cenaform.cenaRText.getText()));
        panel.add(cenaRTCheckBox);
        panel.add(new JLabel(cenaform.artikulText.getText()));
        panel.add(artikulCheckBox);
        panel.add(new JLabel(cenaform.tnText.getText()));
        panel.add(tnCheckBox);
        panel.add(new JLabel(cenaform.izdText.getText()));
        panel.add(izdCheckBox);
        panel.add(new JLabel(cenaform.cenaRozText.getText()));
        panel.add(cenaRozCheckBox);
        panel.add(new JLabel(cenaform.sortText.getText()));
        panel.add(sortCheckBox);
        panel.add(new JLabel(cenaform.sebestText.getText()));
        panel.add(sebestCheckBox);
        panel.add(new JLabel(cenaform.rstText.getText()));
        panel.add(rstCheckBox);
        panel.add(new JLabel(cenaform.rentText.getText()));
        panel.add(rentCheckBox);
        panel.add(new JLabel(cenaform.rzmText.getText()));
        panel.add(rzmCheckBox);
        panel.add(new JLabel(cenaform.cenaVRUBText.getText()));
        panel.add(cenaVRUBCheckBox);
        panel.add(new JLabel(cenaform.sostavText.getText()));
        panel.add(sostavCheckBox);
        panel.add(new JLabel(cenaform.cenaVUSDText.getText()));
        panel.add(cenaVUSDCheckBox);
        panel.add(new JLabel(cenaform.cenaPlText.getText()));
        panel.add(cenaPlCheckBox);
        panel.add(new JLabel(cenaform.cenaVEURText.getText()));
        panel.add(cenaVEURCheckBox);

        panelSide.add(new JLabel(cenaform.sebest_Text.getText()));
        panelSide.add(sebest_CheckBox);
        panelSide.add(new JLabel(cenaform.rent_Text.getText()));
        panelSide.add(rent_CheckBox);

        buttonPanel.add(buttonCancel);
        buttonPanel.add(buttonOk);

        osnovaPanel.add(panel, BorderLayout.CENTER);
        osnovaPanel.add(buttonPanel, BorderLayout.SOUTH);

        if (_flagTable.equals(UtilCena.TABLE_ANALYSIS)) {
            panelSide.add(new JLabel(cenaform.sebVText.getText()));
            panelSide.add(sebVCheckBox);
            panelSide.add(new JLabel(cenaform.cenaVBText.getText()));
            panelSide.add(cenaVBCheckBox);
            panelSide.add(new JLabel(cenaform.rCenaVsebVText.getText()));
            panelSide.add(rCenaVsebVCheckBox);
            panelSide.add(new JLabel(cenaform.rCenaVBsebText.getText()));
            panelSide.add(rCenaVBsebCheckBox);

            osnovaPanel.add(panelSide, BorderLayout.EAST);
            panelSide.setPreferredSize(new Dimension(200, 100));
            setPreferredSize(new Dimension(700, 260));
        }

        if (_flagTable.equals(UtilCena.TABLE_CHANGE)) {
            panelSide.add(new JLabel("Новая цена в RUB:"));
            panelSide.add(newCenaV1CCheckBox);

            osnovaPanel.add(panelSide, BorderLayout.EAST);
            panelSide.setPreferredSize(new Dimension(200, 100));
            setPreferredSize(new Dimension(700, 260));
        }

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttonOkActionPerformed(ActionEvent evt) {
        Vector columSelect = new Vector();
        String ooText;

        if (modelCheckBox.isSelected()) columSelect.add(1);
        if (sarCheckBox.isSelected()) columSelect.add(2);
        if (artikulCheckBox.isSelected()) columSelect.add(3);
        if (izdCheckBox.isSelected()) columSelect.add(4);
        if (sortCheckBox.isSelected()) columSelect.add(5);
        if (rstCheckBox.isSelected()) columSelect.add(8);
        if (rzmCheckBox.isSelected()) columSelect.add(11);
        if (sostavCheckBox.isSelected()) columSelect.add(12);
        if (cenaPlCheckBox.isSelected()) columSelect.add(13);
        if (ndsCheckBox.isSelected()) columSelect.add(14);
        if (cenaRTCheckBox.isSelected()) columSelect.add(15);
        if (tnCheckBox.isSelected()) columSelect.add(16);
        if (cenaRozCheckBox.isSelected()) columSelect.add(17);
        if (sebestCheckBox.isSelected()) columSelect.add(18);
        if (rentCheckBox.isSelected()) columSelect.add(19);
        if (cenaVRUBCheckBox.isSelected()) columSelect.add(20);
        if (cenaVUSDCheckBox.isSelected()) columSelect.add(21);
        if (cenaVEURCheckBox.isSelected()) columSelect.add(22);
        if (_flagTable.equals(UtilCena.TABLE_ANALYSIS)) {
            if (sebest_CheckBox.isSelected()) columSelect.add(23);
            if (rent_CheckBox.isSelected()) columSelect.add(24);
            if (sebVCheckBox.isSelected()) columSelect.add(25);
            if (cenaVBCheckBox.isSelected()) columSelect.add(26);
            if (rCenaVsebVCheckBox.isSelected()) columSelect.add(27);
            if (rCenaVBsebCheckBox.isSelected()) columSelect.add(28);
        } else if (_flagTable.equals(UtilCena.TABLE_CHANGE)) {
            if (sebest_CheckBox.isSelected()) columSelect.add(23);
            if (rent_CheckBox.isSelected()) columSelect.add(24);
            if (newCenaV1CCheckBox.isSelected()) columSelect.add(25);
        }

        ooText = "Цены на " + new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()) + "г.";
        if (columSelect.contains(20)) ooText += ", курс RUB/BYR - " + UtilCena.KURS_RUB;
        if (columSelect.contains(21)) ooText += ", курс USD/RUB - " + UtilCena.KURS_USD;
        if (columSelect.contains(22)) ooText += ", курс EUR/RUB - " + UtilCena.KURS_EUR;

        try {
            CenaOO oo = new CenaOO(ooText, _tmodel, columSelect);
            oo.createReport("PriceTable.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }
}
