package dept.calculationprice.forms;

import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;
import dept.calculationprice.model.ProtocolPreset;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Andy 27.02.2017.
 */
public class ProtocolTypeDialog extends BasePickDialog {

    private JRadioButton rbUMIRAType = new JRadioButton("ОМ");
    private JRadioButton rbOVESType = new JRadioButton("ОВЭС");
    private JComboBox<String> cbCurrency = new JComboBox<>();

    private JRadioButton rbTenderType = new JRadioButton("Тендер");
    private JCheckBox chbCurrency = new JCheckBox("Валюта");

    private JCheckBox chbCredit = new JCheckBox("Учет кредитов и займов");
    private ProtocolPreset preset;

    public ProtocolTypeDialog(final MainController controller, JFrame calculationOfPriceForm) {
        super(controller);
        setLocationRelativeTo(calculationOfPriceForm);
        initComponents();
    }

    private void initComponents() {

        setFrameSize(new Dimension(320, 240));
        getToolBar().setVisible(false);

        Container panel = getCenterContentPanel();
        panel.setLayout(new MigLayout());
        ButtonGroup group = new ButtonGroup();
        group.add(rbUMIRAType);
        group.add(rbOVESType);
        group.add(rbTenderType);

        rbUMIRAType.setSelected(true);

        panel.add(rbUMIRAType, "height 20:20, width 150:20:150, span 2, wrap");
        panel.add(rbOVESType, "height 20:20, width 100:20:100");
        panel.add(cbCurrency, "height 20:20, width 150:20:150, wrap");
        panel.add(rbTenderType, "height 20:20, width 100:20:100");
        panel.add(chbCurrency, "height 20:20, width 100:20:100, wrap");
        chbCurrency.setVisible(false);

        chbCredit.setForeground(Color.BLUE);
        panel.add(new Label(), "height 20:20, span 2, wrap");

        panel.add(chbCredit, "height 20:20, width 250:20:250, span 2, wrap");

        getBtnSave().setText("Сформировать");
        getBtnSave().setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);

        cbCurrency.setEnabled(false);
        ArrayList<String> array = new ArrayList<String>();
        array.add("Росс. руб.");
        array.add("Доллар США");
        array.add("Евро");
        cbCurrency.setModel(new DefaultComboBoxModel(array.toArray()));
        cbCurrency.setSelectedIndex(0);

        initEvents();
    }

    /**
     * Метод отображает форму и согласно документа инициализирует  элементы управления
     *
     * @param documentPreset обрабатываемый документ
     * @return предустановка для обработки документа
     */
    public ProtocolPreset showDialog(ProtocolPreset documentPreset) {
        this.preset = documentPreset;
        setTitle("Выбор типа листа согласования");

        if (showModalFrame()) {
            int type = 0;
            if (rbUMIRAType.isSelected()) {
                type = 0;
            } else if (rbOVESType.isSelected()) {
                type = 1;
            } else {
                type = 2;
            }

            preset.setShowCurrency(chbCurrency.isSelected());

            preset.setProtocolType(type);
            preset.setCurrencyType(cbCurrency.getSelectedIndex());
            preset.setCurrencyRate(preset.getCurrencyRateSet()[preset.getCurrencyType()]);

            preset.setShowCredit(chbCredit.isSelected());

            return preset;
        } else {
            return null;
        }
    }

    private void initEvents() {
        rbOVESType.addActionListener(a -> {
            if (rbOVESType.isSelected()) {
                cbCurrency.setEnabled(true);
            } else {
                cbCurrency.setEnabled(false);
            }
            chbCurrency.setVisible(false);
            chbCurrency.setSelected(false);
        });

        rbTenderType.addActionListener(a -> {
            cbCurrency.setEnabled(false);
            chbCurrency.setVisible(true);
            chbCurrency.setSelected(true);
        });

        rbUMIRAType.addActionListener(a -> {
            cbCurrency.setEnabled(false);
            chbCurrency.setVisible(false);
            chbCurrency.setSelected(false);
        });
    }
}
