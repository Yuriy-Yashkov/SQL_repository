package by.march8.ecs.application.modules.warehouse.internal.displacement.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.MainController;
import by.march8.entities.warehouse.DisplacementCellEntity;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;

/**
 * ФОрма редактирования накладной
 * Created by Andy on 28.04.2015.
 */
public class DisplacementInvoiceEditor extends EditingPane {

    private JLabel lblDocumentDate = new JLabel("Дата документа");
    private JLabel lblDocumentNumber = new JLabel("Номер документа");
    private JLabel lblSender = new JLabel("Подразделение-отправитель");
    private JLabel lblRecipient = new JLabel("Подразделение-получатель");
    private JLabel lblOperation = new JLabel("Вид операции");

    private JXDatePicker dpDocumentDate = new JXDatePicker();
    private JTextField tfDocumentNumber = new JTextField();
    private ComboBoxPanel<String> cbpSender = new ComboBoxPanel<>();
    private ComboBoxPanel<String> cbpRecipient = new ComboBoxPanel<>();
    private ComboBoxPanel<String> cbpOperation = new ComboBoxPanel<>();
    //private final UCTextField tfStandardDen = new UCTextField();
    //private UCController ucController ;
    private UCTextField tfReduce = new UCTextField();
    private DisplacementCellEntity source = null;

    private MainController controller;

    public DisplacementInvoiceEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(375, 80));
        controller = frameViewPort.getController();
        setLayout(new MigLayout());

/*        add(lblDocumentDate);
        add(dpDocumentDate, "width 200:20:200, height 20:20, wrap");
        add(lblDocumentNumber);
        add(tfDocumentNumber, "width 200:20:200, height 20:20, wrap");

        add(lblSender);
        add(cbpSender, "width 200:20:200, height 20:20, wrap");
        add(lblRecipient);
        add(cbpRecipient, "width 200:20:200, height 20:20, wrap");

        add(lblOperation);
        add(cbpOperation, "width 200:20:200, height 20:20, wrap");*/

        tfReduce.setComponentParams(null, Float.class, 2);

        add(new JLabel("Уценка несортных:"), "width 150:20:150");
        add(tfReduce, "width 100:20:100, wrap");
    }

    @Override
    public Object getSourceEntity() {
        source.setReduce3Grade(Double.valueOf(tfReduce.getText()));
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object != null) {
            source = (DisplacementCellEntity) object;
            tfReduce.setValue(source.getReduce3Grade());
        } else {
            source = new DisplacementCellEntity();
        }
    }

    @Override
    public boolean verificationData() {

        if (tfReduce.getText().trim().equals("")) {
            tfReduce.setText("0.0");
        }

        try {
            if (Float.valueOf(tfReduce.getText()) == 0) {
                JOptionPane.showMessageDialog(null,
                        "Необходимо указать корректный размер уценки", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                tfReduce.requestFocusInWindow();
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать корректный размер уценкиа", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfReduce.requestFocusInWindow();
            e.printStackTrace();
            tfReduce.requestFocusInWindow();
            return false;
        }

        return true;
    }
}
