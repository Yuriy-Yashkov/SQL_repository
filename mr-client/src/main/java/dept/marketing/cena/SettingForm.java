package dept.marketing.cena;

import common.UtilFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class SettingForm extends JDialog {
    CenaForm cenaform;
    ChangeForm changeform;
    String _flagTable;
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
    private HashMap<String, String> _map;

    public SettingForm(Dialog cform, Dialog chform, boolean modal, HashMap<String, String> map, String flagTable) {
        super(cform, modal);

        cenaform = (CenaForm) cform;
        changeform = (ChangeForm) chform;

        _map = map;
        _flagTable = flagTable;

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
        this.setTitle("Параметры");

        setPreferredSize(new Dimension(300, 430));

        JPanel osnovaPanel = new JPanel();
        JPanel panel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel panelSide = new JPanel();
        JLabel text = new JLabel("Отображать в таблице столбцы:");
        JButton buttonOk = new JButton("Сохранить");
        JButton buttonCancel = new JButton("Отмена");

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

        panel.setLayout(new GridLayout(18, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttonPanel.setLayout(new GridLayout(1, 1, 5, 5));

        artikulCheckBox.setEnabled(false);

        text.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setVerticalAlignment(JLabel.CENTER);

        panelSide.setLayout(new GridLayout(9, 1, 5, 5));
        panelSide.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

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
        panel.add(new JLabel(cenaform.sarText.getText()));
        panel.add(sarCheckBox);
        panel.add(new JLabel(cenaform.artikulText.getText()));
        panel.add(artikulCheckBox);
        panel.add(new JLabel(cenaform.izdText.getText()));
        panel.add(izdCheckBox);
        panel.add(new JLabel(cenaform.sortText.getText()));
        panel.add(sortCheckBox);
        panel.add(new JLabel(cenaform.rstText.getText()));
        panel.add(rstCheckBox);
        panel.add(new JLabel(cenaform.rzmText.getText()));
        panel.add(rzmCheckBox);
        panel.add(new JLabel(cenaform.sostavText.getText()));
        panel.add(sostavCheckBox);
        panel.add(new JLabel(cenaform.cenaPlText.getText()));
        panel.add(cenaPlCheckBox);
        panel.add(new JLabel(cenaform.ndsText.getText()));
        panel.add(ndsCheckBox);
        panel.add(new JLabel(cenaform.cenaRText.getText()));
        panel.add(cenaRTCheckBox);
        panel.add(new JLabel(cenaform.tnText.getText()));
        panel.add(tnCheckBox);
        panel.add(new JLabel(cenaform.cenaRozText.getText()));
        panel.add(cenaRozCheckBox);
        panel.add(new JLabel(cenaform.sebestText.getText()));
        panel.add(sebestCheckBox);
        panel.add(new JLabel(cenaform.rentText.getText()));
        panel.add(rentCheckBox);
        panel.add(new JLabel(cenaform.cenaVRUBText.getText()));
        panel.add(cenaVRUBCheckBox);
        panel.add(new JLabel(cenaform.cenaVUSDText.getText()));
        panel.add(cenaVUSDCheckBox);
        panel.add(new JLabel(cenaform.cenaVEURText.getText()));
        panel.add(cenaVEURCheckBox);

        panelSide.add(new JLabel(cenaform.sebest_Text.getText()));
        panelSide.add(sebest_CheckBox);
        panelSide.add(new JLabel(cenaform.rent_Text.getText()));
        panelSide.add(rent_CheckBox);

        buttonPanel.add(buttonOk);
        buttonPanel.add(buttonCancel);

        osnovaPanel.add(panel, BorderLayout.CENTER);
        osnovaPanel.add(buttonPanel, BorderLayout.SOUTH);
        osnovaPanel.add(text, BorderLayout.NORTH);

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
            panelSide.setPreferredSize(new Dimension(280, 100));
            setPreferredSize(new Dimension(550, 430));
        }

        if (_flagTable.equals(UtilCena.TABLE_CHANGE)) {
            panelSide.add(new JLabel("Новая цена в RUB:"));
            panelSide.add(newCenaV1CCheckBox);
            osnovaPanel.add(panelSide, BorderLayout.EAST);
            panelSide.setPreferredSize(new Dimension(280, 100));
            setPreferredSize(new Dimension(550, 430));
        }

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttonOkActionPerformed(ActionEvent evt) {
        _map.put("Модель", String.valueOf(modelCheckBox.isSelected()));
        _map.put("Шифр. артикул", String.valueOf(sarCheckBox.isSelected()));
        _map.put("Артикул", String.valueOf(artikulCheckBox.isSelected()));
        _map.put("Изделие", String.valueOf(izdCheckBox.isSelected()));
        _map.put("Сорт", String.valueOf(sortCheckBox.isSelected()));
        _map.put("Рост", String.valueOf(rstCheckBox.isSelected()));
        _map.put("Размер", String.valueOf(rzmCheckBox.isSelected()));
        _map.put("Состав сырья", String.valueOf(sostavCheckBox.isSelected()));
        _map.put("Цена плановая", String.valueOf(cenaPlCheckBox.isSelected()));
        _map.put("НДС(%)", String.valueOf(ndsCheckBox.isSelected()));
        _map.put("Цена реализации", String.valueOf(cenaRTCheckBox.isSelected()));
        _map.put("ТН(%)", String.valueOf(tnCheckBox.isSelected()));
        _map.put("Цена розничная", String.valueOf(cenaRozCheckBox.isSelected()));
        _map.put("Себестоимость", String.valueOf(sebestCheckBox.isSelected()));
        _map.put("Рентабельность", String.valueOf(rentCheckBox.isSelected()));
        _map.put("Цена в RUB", String.valueOf(cenaVRUBCheckBox.isSelected()));
        _map.put("ВЦ в USD", String.valueOf(cenaVUSDCheckBox.isSelected()));
        _map.put("ВЦ в EUR", String.valueOf(cenaVEURCheckBox.isSelected()));

        if (_flagTable.equals(UtilCena.TABLE_ART))
            cenaform.createTableArt(cenaform.dataTableArt == null ? new Vector() : cenaform.dataTableArt);
        else if (_flagTable.equals(UtilCena.TABLE_ANALYSIS)) {
            _map.put("Себ-сть", String.valueOf(sebest_CheckBox.isSelected()));
            _map.put("Рен-сть", String.valueOf(rent_CheckBox.isSelected()));
            _map.put("Себ. в RUB", String.valueOf(sebVCheckBox.isSelected()));
            _map.put("ВЦ в бел.руб", String.valueOf(cenaVBCheckBox.isSelected()));
            _map.put("ВЦ - ВС", String.valueOf(rCenaVsebVCheckBox.isSelected()));
            _map.put("ВЦБ - С", String.valueOf(rCenaVBsebCheckBox.isSelected()));

            cenaform.createTableAnalysis(cenaform.dataTableArt == null ? new Vector() : cenaform.dataTableArt);

        } else if (_flagTable.equals(UtilCena.TABLE_CHANGE)) {
            _map.put("Себ-сть", String.valueOf(sebest_CheckBox.isSelected()));
            _map.put("Рен-сть", String.valueOf(rent_CheckBox.isSelected()));
            _map.put("Новая цена в RUB", String.valueOf(newCenaV1CCheckBox.isSelected()));

            changeform.createTableChange();
        }

        Object[] keys = _map.keySet().toArray();
        Object[] values = _map.values().toArray();
        String rezaltPrint = "";

        for (int row = 0; row < _map.size(); row++) {
            rezaltPrint += keys[row];
            rezaltPrint += ",";
            rezaltPrint += values[row];
            rezaltPrint += ";";
        }

        try {
            if (_flagTable.equals(UtilCena.TABLE_ART))
                UtilFunctions.setSettingPropFile(rezaltPrint, UtilCena.SETTING_ART);
            else if (_flagTable.equals(UtilCena.TABLE_ANALYSIS))
                UtilFunctions.setSettingPropFile(rezaltPrint, UtilCena.SETTING_ANALYSIS);
            else if (_flagTable.equals(UtilCena.TABLE_CHANGE))
                UtilFunctions.setSettingPropFile(rezaltPrint, UtilCena.SETTING_CHANGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        dispose();
    }

}
