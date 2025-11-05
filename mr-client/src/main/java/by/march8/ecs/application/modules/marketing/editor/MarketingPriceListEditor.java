package by.march8.ecs.application.modules.marketing.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.entities.marketing.MarketingPriceListItem;
import by.march8.entities.marketing.PriceListType;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Andy 25.10.2017.
 */
public class MarketingPriceListEditor extends EditingPane {

    private MarketingPriceListItem source;

    private JLabel lblDocumentDate;
    private UCDatePicker dpDocumentDate;

    private JLabel lblDocumentNumber;
    private UCTextField tfDocumentNumber;

    private JLabel lblOperationType;
    private ComboBoxPanel<PriceListType> cbpOperationType;

    private JLabel lblAllowance;
    private UCTextField tfMarkdownValue;

    private UCTextField tfOrderNumber;
    private UCDatePicker dpOrderDate;
    private UCTextField tfAllowanceValue;

    private JCheckBox cbToPrimeCost;

    public MarketingPriceListEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(430, 300));
        controller = frameViewPort.getController();

        this.setLayout(new MigLayout());
        init();
        initEvents();
    }

    private void init() {
        // Номер документа
        lblDocumentNumber = new JLabel("Номер документа");
        lblDocumentNumber.setHorizontalAlignment(SwingConstants.LEFT);
        tfDocumentNumber = new UCTextField();
        add(lblDocumentNumber);
        add(tfDocumentNumber, "height 20:20, width 260:20:260, span 2, wrap");

        // дата документа
        lblDocumentDate = new JLabel("Дата документа");

        dpDocumentDate = new UCDatePicker(new Date());
        dpDocumentDate.setEnabled(true);
        dpDocumentDate.getEditor().setEditable(false);

        add(lblDocumentDate);
        add(dpDocumentDate, "height 20:20,width 105:20:105, wrap");


        add(new JPanel(), "height 5:5,  wrap");
        // Вид операции
        lblOperationType = new JLabel("Вид операции");
        cbpOperationType = new ComboBoxPanel<>(controller, MarchReferencesType.PRICE_LIST_TYPE, false);
        cbpOperationType.setEasy(true);
        add(lblOperationType);
        add(cbpOperationType, "height 20:20, width 260:40:260,span 2, wrap");


        lblAllowance = new JLabel("Ставка");
        tfMarkdownValue = new UCTextField();
        tfMarkdownValue.setComponentParams(lblAllowance, Float.class, 2);

        //tfMarkdownValue.setEnabled(false);
        add(lblAllowance);
        add(tfMarkdownValue, "height 20:20,width 100:20:100, wrap");

        add(new JLabel(""));
        cbToPrimeCost = new JCheckBox("До себестоимости");
        add(cbToPrimeCost, "height 20:20,width 250:20:250,span 2, wrap");

        JLabel lblOrder = new JLabel("Приказ");
        lblOrder.setForeground(Color.BLUE);
        add(new JPanel(), "height 10:10,  wrap");
        add(lblOrder);
        add(new JLabel("№"), "height 20:20, width 80:40:80");
        add(new JLabel("Дата"), "height 20:20, width 80:40:80, wrap");
        add(new JLabel(""));

        tfAllowanceValue = new UCTextField();
        tfAllowanceValue.setComponentParams(lblAllowance, Float.class, 2);
        tfOrderNumber = new UCTextField();
        dpOrderDate = new UCDatePicker(new Date());

        JLabel lblAllowanceValue = new JLabel("Торговая надбавка по приказу");
        lblAllowanceValue.setForeground(Color.BLUE);

        add(tfOrderNumber, "height 20:20,width 100:20:100");
        add(dpOrderDate, "height 20:20,width 100:20:100,wrap");
        add(lblAllowanceValue, "span 2");
        add(tfAllowanceValue, "height 20:20,width 100:20:100,wrap");

        //cbpOperationType.getComboBox().setEnabled(false);
    }

    private void initEvents() {
        cbToPrimeCost.addActionListener(a -> {
            if (cbToPrimeCost.isSelected()) {
                tfMarkdownValue.setEnabled(false);
                // tfMarkdownValue.setText("0");
            } else {
                tfMarkdownValue.setEnabled(true);
                // tfMarkdownValue.setText(String.valueOf(source.getDocumentValue()));
            }
        });

        cbpOperationType.addComboBoxActionListener(a -> {
            if (cbpOperationType.getComboBox().getSelectedIndex() == 0) {
                cbToPrimeCost.setEnabled(false);
                cbToPrimeCost.setSelected(false);
                tfMarkdownValue.setEnabled(true);
            } else {
                cbToPrimeCost.setEnabled(true);
                tfMarkdownValue.setEnabled(true);
            }
        });
    }

    @Override
    public void phaseBeforeShowing() {
        // Загрузка справочников типов документа
        cbpOperationType.updateValues();
        defaultFillingData();
    }

    @Override
    public Object getSourceEntity() {
        source.setDocumentNumber(tfDocumentNumber.getText().trim());
        source.setDocumentDate(dpDocumentDate.getDate());
        source.setDocumentType(cbpOperationType.getSelectedItem().getId());
        source.setDocumentValue(Float.valueOf((String) tfMarkdownValue.getValue()));

        source.setOrderNumber(tfOrderNumber.getText().trim());
        source.setOrderDate(dpOrderDate.getDate());
        source.setAllowanceValue(Double.valueOf((String) tfAllowanceValue.getValue()));

        source.setStatus(2);

        source.setToPrimeCost(cbToPrimeCost.isSelected());
        if (source.isToPrimeCost()) {
            source.setDocumentValue(-1);
        }

        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object != null) {
            source = (MarketingPriceListItem) object;
        } else {
            source = new MarketingPriceListItem();
            source.setDocumentNumber(prepareDocumentNumber());
            source.setDocumentType(1);

            source.setStatus(2);
        }
    }

    @Override
    public void defaultFillingData() {

        if (source.getDocumentNumber() != null) {
            tfDocumentNumber.setText(source.getDocumentNumber());
        } else {
            tfDocumentNumber.setText("");
        }

        if (source.getDocumentDate() != null) {
            dpDocumentDate.setDate(source.getDocumentDate());
        } else {
            dpDocumentDate.setDate(new Date());
        }

        cbpOperationType.presetSimple(source.getDocumentType());

        cbToPrimeCost.setSelected(source.isToPrimeCost());

        if (source.isToPrimeCost()) {
            tfMarkdownValue.setEnabled(false);
            tfMarkdownValue.setText("0");
        } else {
            tfMarkdownValue.setEnabled(true);
            tfMarkdownValue.setText(String.valueOf(source.getDocumentValue()));
        }

        if (source.getOrderNumber() != null) {
            tfOrderNumber.setText(source.getOrderNumber());
        } else {
            tfOrderNumber.setText("");
        }

        if (source.getOrderDate() != null) {
            dpOrderDate.setDate(source.getOrderDate());
        } else {
            dpOrderDate.setDate(new Date());
        }

        tfAllowanceValue.setText(String.valueOf(source.getAllowanceValue()));
    }

    @Override
    public boolean verificationData() {

        if (cbpOperationType.getComboBox().getSelectedIndex() == 0) {
            if (Float.valueOf((String) tfMarkdownValue.getValue()) < 1) {
                JOptionPane.showMessageDialog(null,
                        "Необходимо указать размер торговой надбавки", "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
                tfMarkdownValue.requestFocusInWindow();
                return false;
            } else {
                tfAllowanceValue.setText((String) tfMarkdownValue.getValue());
            }

        } else {
            if (Float.valueOf((String) tfMarkdownValue.getValue()) < 1) {
                if (!cbToPrimeCost.isSelected()) {
                    JOptionPane.showMessageDialog(null,
                            "Необходимо указать размер уценки по документу", "Ошибка!",
                            JOptionPane.ERROR_MESSAGE);
                    tfMarkdownValue.requestFocusInWindow();
                    return false;
                }
            }
        }

        if (!tfOrderNumber.getText().trim().equals("")) {
            if (Float.valueOf((String) tfAllowanceValue.getValue()) < 1) {
                JOptionPane.showMessageDialog(null,
                        "Необходимо указать размер торговой надбавки по документу приказа", "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
                tfAllowanceValue.requestFocusInWindow();
            }
        }
        // Если установлен номер приказа, необходимо дополнить дату приказа и велисину ТН

        return true;
    }

    private String prepareDocumentNumber() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
    }
}
