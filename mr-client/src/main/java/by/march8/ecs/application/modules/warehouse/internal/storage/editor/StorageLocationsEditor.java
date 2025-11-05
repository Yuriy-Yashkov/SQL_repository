package by.march8.ecs.application.modules.warehouse.internal.storage.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.services.eancode.UniqueIdentifierService;
import by.march8.entities.storage.StorageLocationsItem;
import by.march8.entities.storage.UniqueIdentifiers;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * @author Andy 03.11.2018 - 7:47.
 */
public class StorageLocationsEditor extends EditingPane {
    private JLabel lblDocumentDate = new JLabel("Дата документа");
    private JLabel lblDocumentNumber = new JLabel("Номер документа");
    private JLabel lblDepartment = new JLabel("Подразделение");
    private JLabel lblNote = new JLabel("Описание места хранения");

    private UCDatePicker dpDocumentDate = new UCDatePicker(new Date());
    private UCTextField tfDocumentNumber = new UCTextField();
    private UCTextFieldPanel<String> tfpDepartment = new UCTextFieldPanel<>();
    private JTextField tfNote = new JTextField();

    private StorageLocationsItem source = null;

    private MainController controller;

    private boolean isNew = false;

    private long nextDocumentNumber = 0;

    public StorageLocationsEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(400, 250));
        controller = frameViewPort.getController();
        setLayout(new MigLayout());

        tfDocumentNumber.setComponentParams(null, Integer.class, 4);

        add(lblDocumentDate);
        add(dpDocumentDate, "width 200:20:200, height 20:20, wrap");
        add(lblDocumentNumber);
        add(tfDocumentNumber, "width 200:20:200, height 20:20, wrap");
        add(new JPanel(), "height 10:10,  wrap");
        add(lblDepartment);
        add(tfpDepartment, "width 200:20:200, height 20:20, wrap");
        add(new JPanel(), "height 10:10,  wrap");
        add(lblNote);
        add(tfNote, "width 200:20:200, height 20:20, wrap");

        tfDocumentNumber.setEditable(false);
        tfpDepartment.setEnabled(false);
    }

    @Override
    public Object getSourceEntity() {
        source.setDocumentDate(dpDocumentDate.getDate());
        source.setDocumentNumber(Integer.valueOf(tfDocumentNumber.getText().trim()));
        source.setNote(tfNote.getText().trim());
        return source;
    }


    @Override
    public void setSourceEntity(final Object object) {
        String user = controller.getWorkSession().getUser().getUserLogin();
        if (object != null) {
            isNew = false;
            source = (StorageLocationsItem) object;
            source.setDatekrkv(new Date());
            source.setUserkrkv(user);
            source.setKpodkrkv("737");
            source.setDepartmentCode(737);

        } else {
            isNew = true;
            source = new StorageLocationsItem();
            source.setDatevrkv(new Date());
            source.setDatekrkv(new Date());
            source.setUservrkv(user);
            source.setUserkrkv(user);
            source.setKpodvrkv("737");
            source.setKpodkrkv("737");
            source.setDepartmentCode(737);

            long nextNumber = getNextDocumentNumber();
            source.setDocumentNumber(Integer.valueOf(String.valueOf(nextNumber)));
        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        if (isNew) {
            tfDocumentNumber.setText("");
            dpDocumentDate.setDate(new Date());
            tfDocumentNumber.setText(String.valueOf(source.getDocumentNumber()));
            tfNote.setText("");
        } else {
            tfDocumentNumber.setText(String.valueOf(source.getDocumentNumber()));
            dpDocumentDate.setDate(source.getDocumentDate());
            tfNote.setText(source.getNote());
        }

        tfpDepartment.getEditor().setText("737");
    }

    @Override
    public boolean verificationData() {

        if (tfNote.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Укажите место хранения", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfNote.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private long getNextDocumentNumber() {
        UniqueIdentifierService service = UniqueIdentifierService.getInstance();
        return service.getNextUniqueIdentifierAndSave(UniqueIdentifiers.STORAGE_LOCATIONS);
    }
}
