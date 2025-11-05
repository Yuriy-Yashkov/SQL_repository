package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.readonly.AddressEntity;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import static by.march8.ecs.framework.common.Settings.COLOR_DISABLED;
import static by.march8.ecs.framework.common.Settings.COLOR_ENABLED;

/**
 * @author Andy 25.02.2016.
 */
public class AddressEditor extends EditingPane {

    private JLabel lblPostIndex = new JLabel("Почтовый индекс");
    private JLabel lblRegion = new JLabel("Область");
    private JLabel lblDistrict = new JLabel("Район");
    private JLabel lblCity = new JLabel("Населенный пункт");
    private JLabel lblStreet = new JLabel("Улица");
    private JLabel lblHouseNumber = new JLabel("Номер дома");

    private JTextField tfPostIndex = new JTextField();
    private JTextField tfRegion = new JTextField();
    private JTextField tfDistrict = new JTextField();
    private JTextField tfCity = new JTextField();
    private JTextField tfStreet = new JTextField();
    private JTextField tfHouseNumber = new JTextField();

    private AddressEntity source = null;

    public AddressEditor(MainController mainController) {
        controller = mainController;
        setPreferredSize(new Dimension(330, 200));
        this.setLayout(new MigLayout());
        init();

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }

    private void init() {
        add(lblPostIndex, "width 150:20:150");
        add(tfPostIndex, "width 150:20:150, wrap");

        add(lblRegion, "width 150:20:150");
        add(tfRegion, "width 150:20:150, wrap");

        add(lblDistrict, "width 150:20:150");
        add(tfDistrict, "width 150:20:150, wrap");

        add(lblCity, "width 150:20:150");
        add(tfCity, "width 150:20:150, wrap");

        add(lblStreet, "width 150:20:150");
        add(tfStreet, "width 150:20:150, wrap");

        add(lblHouseNumber, "width 150:20:150");
        add(tfHouseNumber, "width 150:20:150, wrap");

        NewFocusListener listener = new NewFocusListener();

        tfPostIndex.addFocusListener(listener);
        tfRegion.addFocusListener(listener);
        tfDistrict.addFocusListener(listener);
        tfCity.addFocusListener(listener);
        tfStreet.addFocusListener(listener);
        tfHouseNumber.addFocusListener(listener);
    }

    @Override
    public Object getSourceEntity() {

        source.setPostIndex(tfPostIndex.getText().trim());
        source.setRegion(tfRegion.getText().trim());
        source.setDistrict(tfDistrict.getText().trim());

        source.setCity(tfCity.getText().trim());
        source.setStreet(tfStreet.getText().trim());
        source.setHouseNumber(tfHouseNumber.getText().trim());

        String fullName = "";
        if (!source.getPostIndex().equals("")) {
            fullName = source.getPostIndex() + ",";
        }

        if (!source.getRegion().equals("")) {
            fullName = fullName + source.getRegion() + ",";
        }

        if (!source.getDistrict().equals("")) {
            fullName = fullName + source.getDistrict() + ",";
        }

        if (!source.getCity().equals("")) {
            fullName = fullName + source.getCity() + ",";
        }

        if (!source.getStreet().equals("")) {
            fullName = fullName + source.getStreet() + ",";
        }

        if (!source.getHouseNumber().equals("")) {
            fullName = fullName + source.getHouseNumber();
        }

        source.setFullName(fullName);

        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new AddressEntity();
        } else {
            source = (AddressEntity) object;
            source.setId(-1);
        }

        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        if (source.getPostIndex() != null) {
            tfPostIndex.setText(source.getPostIndex().trim());
        } else {
            tfPostIndex.setText("");
        }

        if (source.getRegion() != null) {
            tfRegion.setText(source.getRegion().trim());
        } else {
            tfRegion.setText("");
        }

        if (source.getDistrict() != null) {
            tfDistrict.setText(source.getDistrict().trim());
        } else {
            tfDistrict.setText("");
        }

        if (source.getCity() != null) {
            tfCity.setText(source.getCity().trim());
        } else {
            tfCity.setText("");
        }

        if (source.getStreet() != null) {
            tfStreet.setText(source.getStreet().trim());
        } else {
            tfStreet.setText("");
        }

        if (source.getHouseNumber() != null) {
            tfHouseNumber.setText(source.getHouseNumber().trim());
        } else {
            tfHouseNumber.setText("");
        }
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
