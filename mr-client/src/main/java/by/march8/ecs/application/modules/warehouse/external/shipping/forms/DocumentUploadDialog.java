package by.march8.ecs.application.modules.warehouse.external.shipping.forms;

import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.DocumentUploadPreset;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * @author Andy 06.09.2018 - 7:23.
 */
public class DocumentUploadDialog extends BasePickDialog {

    private UCDatePeriodPicker datePeriodPicker;
    private UCTextField tfContractorCode;
    private JCheckBox chbSelectSaveDir;

    public DocumentUploadDialog(MainController controller) {
        super(controller);
        initComponents();
    }

    public void initComponents() {
        setFrameSize(new Dimension(450, 300));
        getToolBar().setVisible(false);
        setTitle("Выгрузка документов по контрагенту");

        Container panel = getCenterContentPanel();
        panel.setLayout(new MigLayout());

        datePeriodPicker = new UCDatePeriodPicker();
        JLabel lblPeriod = new JLabel("Период отбора:");

        panel.add(lblPeriod);
        panel.add(datePeriodPicker, "wrap");

        panel.add(new JPanel(), "height 10:10,  wrap");

        JLabel lblContractorCode = new JLabel("Код контрагента: ");

        tfContractorCode = new UCTextField();
        tfContractorCode.setComponentParams(lblContractorCode, Integer.class, 4);

        chbSelectSaveDir = new JCheckBox("Выбрать каталог для сохранения");
        panel.add(lblContractorCode);

        panel.add(tfContractorCode, "height 20:20, width 150:20:150, wrap");


        panel.add(new JPanel(), "height 10:10,  wrap");
        panel.add(chbSelectSaveDir, "span 2, wrap");

    }

    public DocumentUploadPreset showDialog(DocumentUploadPreset preset) {

        if (preset == null) {
            datePeriodPicker.setDatePickerBegin(new Date());
            datePeriodPicker.setDatePickerEnd(new Date());

            tfContractorCode.setText("0000");
        } else {
            datePeriodPicker.setDatePickerBegin(preset.getPeriodBegin());
            datePeriodPicker.setDatePickerEnd(preset.getPeriodEnd());

            tfContractorCode.setText(String.valueOf(preset.getContractorCode()));
        }

        if (showModalFrame()) {
            DocumentUploadPreset result = new DocumentUploadPreset();
            result.setPeriodBegin(datePeriodPicker.getDatePickerBegin());
            result.setPeriodEnd(datePeriodPicker.getDatePickerEnd());

            result.setContractorCode(Integer.valueOf(tfContractorCode.getText().trim()));
            result.setSaveAs(false);

            return result;
        } else {
            return null;
        }
    }
}
