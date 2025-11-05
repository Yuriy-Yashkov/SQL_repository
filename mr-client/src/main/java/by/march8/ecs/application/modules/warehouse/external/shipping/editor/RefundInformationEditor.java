package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.march8.ecs.MainController;
import by.march8.entities.warehouse.SaleDocumentRefundInformation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static by.march8.ecs.framework.common.Settings.COLOR_DISABLED;
import static by.march8.ecs.framework.common.Settings.COLOR_ENABLED;

/**
 * @author Andy 13.10.2017.
 */
public class RefundInformationEditor extends EditingPane {

    private JLabel lblSourceDocNumber = new JLabel("Исходный документ");
    private JLabel lblSourceDocDate = new JLabel("от");
    private JLabel lblRefundDocNumber = new JLabel("Возвратный документ");
    private JLabel lblRefundDocDate = new JLabel("от");

    private JTextField tfSourceDocNumber = new JTextField();
    private JTextField tfRefundDocNumber = new JTextField();

    private UCDatePicker dpSourceDocDate = new UCDatePicker(new Date());
    private UCDatePicker dpRefundDocDate = new UCDatePicker(new Date());

    private SaleDocumentRefundInformation source = null;

    public RefundInformationEditor(MainController mainController) {
        controller = mainController;
        setPreferredSize(new Dimension(400, 150));
        this.setLayout(new MigLayout());
        init();

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }

    private void init() {
        add(lblSourceDocNumber, "");
        add(tfSourceDocNumber, "width 100:20:100");

        add(lblSourceDocDate, "");
        add(dpSourceDocDate, "width 110:20:110, height 20:20, wrap");

        add(new JPanel(), "height 10:10,  wrap");

        add(lblRefundDocNumber, "");
        add(tfRefundDocNumber, "width 100:20:100");

        add(lblRefundDocDate, "");
        add(dpRefundDocDate, "width 110:20:110, height 20:20, wrap");

        NewFocusListener listener = new NewFocusListener();

        tfSourceDocNumber.addFocusListener(listener);
        tfRefundDocNumber.addFocusListener(listener);
    }

    @Override
    public Object getSourceEntity() {

        source.setSourceDocNumber(tfSourceDocNumber.getText().trim());
        source.setSourceDocDate(dpSourceDocDate.getDate());
        source.setRefundDocNumber(tfRefundDocNumber.getText().trim());
        source.setRefundDocDate(dpRefundDocDate.getDate());

        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new SaleDocumentRefundInformation();

        } else {
            source = (SaleDocumentRefundInformation) object;
        }

        if (source.getSourceDocNumber() == null) {
            source.setSourceDocNumber("");
        }

        if (source.getRefundDocNumber() == null) {
            source.setRefundDocNumber("");
        }

        if (source.getSourceDocDate() == null) {
            source.setSourceDocDate(new Date());
        }

        if (source.getRefundDocDate() == null) {
            source.setRefundDocDate(new Date());
        }

        tfSourceDocNumber.setText(source.getSourceDocNumber().trim());
        dpSourceDocDate.setDate(source.getSourceDocDate());

        tfRefundDocNumber.setText(source.getRefundDocNumber().trim());
        dpRefundDocDate.setDate(source.getRefundDocDate());
    }

    @Override
    public boolean verificationData() {
        return true;
    }

    private class NewFocusListener implements FocusListener {

        Color normalColor;

        @Override
        public void focusGained(final FocusEvent e) {
            Component component = e.getComponent();
            normalColor = component.getBackground();
            if (component instanceof JTextComponent) {
                if (((JTextComponent) component).isEditable()) {
                    component.setBackground(COLOR_ENABLED);
                    ((JTextComponent) component).selectAll();
                } else {
                    component.setBackground(COLOR_DISABLED);
                }
            }
        }

        @Override
        public void focusLost(final FocusEvent e) {
            Component component = e.getComponent();
            if (component.isEnabled()) {
                component.setBackground(Color.white);
            } else {
                component.setBackground(Color.LIGHT_GRAY);
            }
        }
    }
}
