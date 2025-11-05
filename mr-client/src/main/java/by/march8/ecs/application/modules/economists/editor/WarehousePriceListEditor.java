package by.march8.ecs.application.modules.economists.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.MainController;
import by.march8.entities.classifier.RemainPriceListItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Developer on 05.03.2020 7:14
 */
public class WarehousePriceListEditor extends EditingPane {

    private JLabel lblDocumentDate = new JLabel("Дата документа");
    private UCDatePicker dpDocumentDate = new UCDatePicker(new Date());

    private JLabel lblDocumentNumber = new JLabel("Номер документа");
    private JTextField tfDocumentNumber = new JTextField();

    private JLabel lblTradeAllowance = new JLabel("Надбавка");
    private UCTextField tfTradeAllowance = new UCTextField();

    private RemainPriceListItem source = null;

    private MainController controller;


    public WarehousePriceListEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(450, 150));
        controller = frameViewPort.getController();
        setLayout(new MigLayout());

        init();
        initEvents();
    }

    private void init() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, +1);

        tfTradeAllowance.setComponentParams(lblTradeAllowance, Double.class, 3);

        add(lblDocumentDate);
        add(dpDocumentDate, "width 200:20:200, height 20:20, wrap");
        add(lblDocumentNumber);
        add(tfDocumentNumber, "width 200:20:200, height 20:20, wrap");
        add(new JPanel(), "height 10:10,  wrap");
        add(lblTradeAllowance);
        add(tfTradeAllowance, "width 50:20:50, height 20:20, wrap");
        tfTradeAllowance.setText("0");
    }

    private void initEvents() {

    }


    @Override
    public Object getSourceEntity() {
        source.setDocumentDate(dpDocumentDate.getDate());
        source.setDocumentNumber(tfDocumentNumber.getText().trim());
        source.setTradeAllowanceValue(Double.valueOf(tfTradeAllowance.getText()));
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object != null) {
            source = (RemainPriceListItem) object;
        } else {
            source = new RemainPriceListItem();
            source.setDocumentDate(new Date());
            source.setDocumentNumber("");
            source.setTradeAllowanceValue(0.0);
        }

        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        tfDocumentNumber.setText(source.getDocumentNumber());
        dpDocumentDate.setDate(source.getDocumentDate());
        tfTradeAllowance.setValue(source.getTradeAllowanceValue());
    }

    @Override
    public boolean verificationData() {
        if (tfDocumentNumber.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Укажите номер документа", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfDocumentNumber.requestFocusInWindow();
            return false;
        }
        return true;
    }
}
