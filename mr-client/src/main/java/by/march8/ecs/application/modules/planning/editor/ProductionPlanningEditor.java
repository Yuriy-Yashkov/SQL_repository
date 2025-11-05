package by.march8.ecs.application.modules.planning.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.march8.ecs.MainController;
import by.march8.entities.planning.ProductionPlanningEntity;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * @author Andy 06.12.2018 - 10:05.
 */
public class ProductionPlanningEditor extends EditingPane {
    private JLabel lblDocumentDate = new JLabel("Дата документа");
    private JLabel lblDocumentNumber = new JLabel("Номер документа");
    private JLabel lblNote = new JLabel("Примечание");

    private UCDatePicker dpDocumentDate = new UCDatePicker(new Date());
    private JTextField tfDocumentNumber = new JTextField();
    private JTextField tfNote = new JTextField();

    private ProductionPlanningEntity source = null;

    private MainController controller;


    public ProductionPlanningEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(400, 150));
        controller = frameViewPort.getController();
        setLayout(new MigLayout());

        add(lblDocumentDate);
        add(dpDocumentDate, "width 200:20:200, height 20:20, wrap");
        add(lblDocumentNumber);
        add(tfDocumentNumber, "width 200:20:200, height 20:20, wrap");
        add(new JPanel(), "height 10:10,  wrap");
        add(lblNote);
        add(tfNote, "width 200:20:200, height 20:20, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setDocumentDate(dpDocumentDate.getDate());
        source.setDocumentNumber(tfDocumentNumber.getText().trim());
        source.setNote(tfNote.getText().trim());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object != null) {
            source = (ProductionPlanningEntity) object;
        } else {
            source = new ProductionPlanningEntity();
            source.setDocumentDate(new Date());
            source.setDocumentType(2);
            source.setStatus(3);
        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        tfDocumentNumber.setText(source.getDocumentNumber());
        dpDocumentDate.setDate(source.getDocumentDate());
        tfNote.setText(source.getNote());
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
