package by.march8.ecs.application.modules.art.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.art.util.UtilArt;
import by.march8.entities.product.Protocol;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lidashka.
 */
public class ModelProtocolEditor extends EditingPane {

    private final JTextField tfCode = new JTextField();

    private final JDateChooser jcDate = new JDateChooser();

    private final JTextPane jtNote = new JTextPane();
    private final MainController controller;
    private Protocol source = null;

    public ModelProtocolEditor(IReference reference) {
        super(reference);
        this.controller = reference.getMainController();

        setPreferredSize(new Dimension(500, 300));

        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new MigLayout());

        infoPanel.add(new JLabel("Номер протокола ХТС *: "), "wrap");
        infoPanel.add(tfCode, "width 400:20:400, wrap");

        infoPanel.add(new JLabel("Дата *: "), "wrap");
        infoPanel.add(jcDate, "width 400:20:400, wrap");

        infoPanel.add(new JLabel("Примечание:"), "wrap");
        infoPanel.add(new JScrollPane(jtNote), "push, grow, height 50, wrap");

        add(infoPanel, BorderLayout.CENTER);
    }

    @Override
    public Object getSourceEntity() {
        source.setDate(jcDate.getDate());

        source.setCode(tfCode.getText().trim());

        source.setNote(jtNote.getText().trim());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new Protocol();

            jcDate.setDate(UtilArt.TODAY);

        } else {
            this.source = (Protocol) object;

            jcDate.setDate(source.getDate());

        }
        defaultFillingData();

    }

    public void defaultFillingData() {
        tfCode.setText(this.source.getCode());

        jtNote.setText(this.source.getNote());

    }

    @Override
    public boolean verificationData() {
        if (tfCode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать номер протокола ХТС", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tfCode.requestFocusInWindow();
            return false;
        }

        if ((jcDate.getDate() == null)
                || (jcDate.getDate().toString().trim().equals(""))) {
            JOptionPane.showMessageDialog(null,
                    "Дата задана некорректно", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            jcDate.requestFocusInWindow();
            return false;
        }

        return true;
    }
}
