package dept.sbit.client.form;

import dept.sbit.client.dao.ClientBD;
import dept.sbit.client.dao.dto.AddressDto;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class AddAddressForm extends JDialog {
    private final String code;
    private JTextField zipcode;
    private JTextField street;
    private JTextField city;
    private JTextField state;
    private JTextField raion;
    private JTextField house;
    private static final String[] labels = {"Индекс", "Область", "Район", "Населенный пункт", "Улица", "Дом", "Адрес"};
    private final ClientBD clientBD;
    private DefaultListModel<String> addressListModel;
    private JLabel fullAddress;

    public AddAddressForm(JFrame parent, String id) {
        super(parent, true);
        clientBD = new ClientBD();
        addInit();
        code = id;
        loadAddress();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void addInit() {
        JList<String> addressList;
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        addressListModel = new DefaultListModel<>();
        addressList = new JList<>(addressListModel);
        JScrollPane listScrollPane = new JScrollPane(addressList);
        listScrollPane.setPreferredSize(new Dimension(750, 250));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2, 10, 10)); // 7 строк, 2 колонки

        zipcode = new JTextField();
        street = new JTextField();
        city = new JTextField();
        state = new JTextField();
        house = new JTextField();
        raion = new JTextField();
        fullAddress = new JLabel();

        inputPanel.add(new JLabel(labels[0]));
        inputPanel.add(zipcode);
        inputPanel.add(new JLabel(labels[1]));
        inputPanel.add(state);
        inputPanel.add(new JLabel(labels[2]));
        inputPanel.add(raion);
        inputPanel.add(new JLabel(labels[3]));
        inputPanel.add(city);
        inputPanel.add(new JLabel(labels[4]));
        inputPanel.add(street);
        inputPanel.add(new JLabel(labels[5]));
        inputPanel.add(house);
        inputPanel.add(new JLabel(labels[6]));
        inputPanel.add(fullAddress);

        JButton addButton = new JButton("Добавить адрес");
        addButton.addActionListener(e -> addAddress());

        Box mainBox = Box.createVerticalBox();
        mainBox.add(listScrollPane);
        mainBox.add(Box.createRigidArea(new Dimension(0, 20)));
        mainBox.add(inputPanel);
        mainBox.add(Box.createRigidArea(new Dimension(0, 20)));
        mainBox.add(addButton);

        add(mainBox);
        pack();

        zipcode.getDocument().addDocumentListener(new AddressChangeListener());
        street.getDocument().addDocumentListener(new AddressChangeListener());
        city.getDocument().addDocumentListener(new AddressChangeListener());
        state.getDocument().addDocumentListener(new AddressChangeListener());
        house.getDocument().addDocumentListener(new AddressChangeListener());
        raion.getDocument().addDocumentListener(new AddressChangeListener());
    }

    private class AddressChangeListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateFullAddress();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateFullAddress();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateFullAddress();
        }

        private void updateFullAddress() {
            String zip = zipcode.getText();
            String str = street.getText();
            String ct = city.getText();
            String st = state.getText();
            String hs = house.getText();
            String ra = raion.getText();

            StringBuilder fullAddrBuilder = new StringBuilder();

            if (!zip.isEmpty()) fullAddrBuilder.append(zip).append(", ");

            if (!st.isEmpty()) fullAddrBuilder.append(st).append("обл., ");

            if (!ra.isEmpty()) fullAddrBuilder.append(ra).append("рн., ");

            if (!ct.isEmpty()) fullAddrBuilder.append("г.").append(ct).append(", ");

            if (!str.isEmpty()) fullAddrBuilder.append("ул.").append(str).append(", ");

            if (!hs.isEmpty()) fullAddrBuilder.append("д.").append(hs);

            String fullAddressText = fullAddrBuilder.toString();
            if (!fullAddressText.isEmpty()) {
                fullAddress.setText(fullAddressText);
            }
        }
    }

    private void addAddress() {

        String zip = zipcode.getText();
        String st = state.getText();
        String ct = city.getText();
        String str = street.getText();
        String hs = house.getText();
        String ra = raion.getText();
        String fullAddressText = fullAddress.getText();

        if (zip.isEmpty() || st.isEmpty() || ct.isEmpty() || str.isEmpty() || hs.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Все поля обязательны для заполнения",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        AddressDto address = AddressDto.builder()
                .zipcode(zip)
                .street(st)
                .city(ct)
                .state(str)
                .raion(ra)
                .fullAddress(fullAddressText)
                .number(hs)
                .clientId(Integer.parseInt(code))
                .build();
        if (clientBD.addAddress(address)) {
            loadAddress();

            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Адрес успешно добавлен",
                    "Информация",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Ошибка при сохранении адреса",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadAddress() {
        String[] addresses = clientBD.getAddressByClientCode(code);
        addressListModel.clear();
        for (String address : addresses) {
            addressListModel.addElement(address);
        }
    }

    private void clearFields() {
        zipcode.setText("");
        street.setText("");
        raion.setText("");
        city.setText("");
        state.setText("");
        house.setText("");
        fullAddress.setText("");
    }
}
