package dept.sbit.client.form;

import dept.sbit.client.dao.ClientBD;
import dept.sbit.client.dao.dto.ChangeClientDto;
import dept.sbit.client.dao.dto.NewClientDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class AddClientForm extends JDialog {

    private final String[] clientTypes = {"", "Организация", "Инд. предприниматель", "Частное лицо", "Сотрудник", "Розничный магазин"};

    private JFrame frame;
    private JTextField codeField;
    private JTextField nameField;
    private JTextField fullnameField;
    private JTextField phonesField;
    private JTextField unnField;
    private JTextField okpofield;
    private JTextField licenceField;
    private JTextField discountField;
    private JTextField codeCountryField;
    private JTextField typeOrganizationField;
    private JTextField adultField;
    private JTextField childrenField;
    private JTextField directorField;
    private JTextField chiefAccountantField;
    private JTextField purchasingManagerField;

    private JLabel shadowLabel = new JLabel("Торговая наценка(%): ");
    private JLabel adultLabel = new JLabel("Взрослый ассортимент: ");
    private JLabel childrenLabel = new JLabel("Детский ассортимент: ");

    private JCheckBox isClientCheckbox;
    private JCheckBox isResidentCheckbox;
    private JComboBox<String> clientTypeComboBox;
    private JComboBox<String> mainContractComboBox;
    private JComboBox<String> postAddressComboBox;
    private JComboBox<String> urAddressComboBox;
    private JComboBox<String> currentAccountComboBox;

    private final ClientBD clientBD;

    public AddClientForm(JFrame parent, boolean modal) {
        super(parent, modal);
        frame = parent;
        clientBD = new ClientBD();
        addInit();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public AddClientForm(JFrame parent, boolean modal, String code) {
        super(parent, modal);
        clientBD = new ClientBD();
        changeInit(code);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void addInit() {
        int yAxis = 800;
        setPreferredSize(new Dimension(yAxis, 600));
        setTitle("Добавление клиента");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel input = new JPanel();
        addForm(input, yAxis);

        getContentPane().add(input);
        pack();
    }

    private void changeInit(String code) {
        int yAxis = 800;
        setMinimumSize(new Dimension(yAxis, 600));
        setTitle("Изменение клиента");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        ChangeClientDto dto = clientBD.getClientByKOD(Integer.parseInt(code));

        JPanel input = new JPanel();
        changeForm(input, yAxis, dto);

        getContentPane().add(input);
        pack();
    }

    private void changeForm(JPanel input, int yAxis, ChangeClientDto client) {
        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
        int xAxis = 20;

        name(input, xAxis, yAxis - 50, client.getCode(),
                client.getName(), client.getFullname());

        mainInformation(input, xAxis, yAxis - 50,
                client.getPhone(), client.getUNN(), client.getOKPO(), client.getLicence(), client.getDiscount(),
                client.getCodeCountry(), client.getTypeOfOrganization(), client.getAdult(),
                client.getChildren(), client.isClient(), client.isRezident(),
                clientTypes[Integer.parseInt(client.getClientType())]);

        contract(input, xAxis, yAxis - 50, client.getMainContract(),
                client.getDirector(), client.getChiefAccountant(), client.getPurchasingManager());

        address(input, xAxis, yAxis - 50, client.getAddressPost(), client.getAddressUr(), client.getCode());

        currentAccount(input, xAxis, yAxis - 50, client.getCurrentAccountSelected());

        buttons(input, xAxis, yAxis - 50);
    }

    private void addForm(JPanel input, int yAxis) {
        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
        int xAxis = 20;

        name(input, xAxis, yAxis - 50, "", "", "");

        mainInformation(input, xAxis, yAxis - 50,
                "", "", "", "", "",
                "", "", "",
                "", false, false, "");

        contract(input, xAxis, yAxis - 50, "", "", "", "");

        address(input, xAxis, yAxis - 50, "", "", "");

        currentAccount(input, xAxis, yAxis - 50, "");

        buttons(input, xAxis, yAxis - 50);
    }

    private void name(JPanel input, int xAxis, int yAxis,
                      String codeClient, String nameString,
                      String fullnameString) {
        JLabel codeLabel = new JLabel("Код контрагента: ");
        JLabel nameLabel = new JLabel("Наименование: ");
        JLabel fullnameLabel = new JLabel("Полное наименование: ");

        codeField = new JTextField(codeClient);
        codeField.setPreferredSize(new Dimension(70, 0));
        nameField = new JTextField(nameString);
        fullnameField = new JTextField(fullnameString);

        JPanel apanel = getPanel(xAxis, yAxis);
        JPanel bpanel = getPanel(xAxis, yAxis);

        apanel.add(codeLabel);
        apanel.add(codeField);
        apanel.add(nameLabel);
        apanel.add(nameField);

        bpanel.add(fullnameLabel);
        bpanel.add(fullnameField);

        input.add(apanel);
        input.add(bpanel);
    }

    private void mainInformation(JPanel input, int x, int y,
                                 String phones, String unn, String okpo,
                                 String licence, String discount, String codeCountry,
                                 String typeOrganization, String adult,
                                 String children, Boolean isClient, Boolean isResident,
                                 String clientType) {

        phonesField = new JTextField(phones);
        unnField = new JTextField(unn);
        okpofield = new JTextField(okpo);
        licenceField = new JTextField(licence);
        discountField = new JTextField(discount);
        codeCountryField = new JTextField(codeCountry);
        typeOrganizationField = new JTextField(typeOrganization);
        adultField = new JTextField(adult);
        childrenField = new JTextField(children);

        isClientCheckbox = new JCheckBox();
        isClientCheckbox.setSelected(isClient);
        isResidentCheckbox = new JCheckBox();
        isResidentCheckbox.setSelected(isResident);
        clientTypeComboBox = new JComboBox<>(clientTypes);
        clientTypeComboBox.setSelectedItem(clientType);
        clientTypeComboBox.addActionListener(e -> jButtonChangeTypeOfClientActionPerformed());

        JPanel cpanel = getPanel(x, y);
        JPanel dpanel = getPanel(x, y);
        JPanel epanel = getPanel(x, y);
        JPanel fpanel = getPanel(x, y);
        JPanel gpanel = getPanel(x, y);
        JPanel hpanel = getPanel(x, y);
        JPanel ipanel = getPanel(x, y);

        JLabel isClientLabel = new JLabel("является покупателем");
        JLabel clientTypeLabel = new JLabel("Вид контрагента: ");
        JLabel phonesLabel = new JLabel("Телефоны: ");
        JLabel unnLabel = new JLabel("УНН: ");
        JLabel okpoLabel = new JLabel("     ОКПО: ");
        JLabel licenceLabel = new JLabel("Лицензия: ");
        JLabel residentLabel = new JLabel("резидент РБ      ");
        JLabel discountLabel = new JLabel("     Постоянная скидка, %");
        JLabel codeCountryLabel = new JLabel("Код территории: ");
        JLabel typeOrganisationLabel = new JLabel("       Вид организации: ");

        cpanel.add(clientTypeLabel);
        cpanel.add(clientTypeComboBox);
        cpanel.add(isClientCheckbox);
        cpanel.add(isClientLabel);

        dpanel.add(phonesLabel);
        dpanel.add(phonesField);

        epanel.add(unnLabel);
        epanel.add(unnField);
        epanel.add(okpoLabel);
        epanel.add(okpofield);

        fpanel.add(licenceLabel);
        fpanel.add(licenceField);

        gpanel.add(isResidentCheckbox);
        gpanel.add(residentLabel);
        gpanel.add(discountLabel);
        gpanel.add(discountField);

        hpanel.add(codeCountryLabel);
        hpanel.add(codeCountryField);
        hpanel.add(typeOrganisationLabel);
        hpanel.add(typeOrganizationField);

        JPanel shadowPanel = new JPanel();
        JPanel shadowInputPanel = getPanel(x, y);

        shadowInputPanel.add(adultLabel);
        shadowInputPanel.add(adultField);
        shadowInputPanel.add(childrenLabel);
        shadowInputPanel.add(childrenField);

        shadowPanel.setLayout(new BoxLayout(shadowPanel, BoxLayout.Y_AXIS));
        shadowPanel.setMaximumSize(new Dimension(y, x * 2));
        shadowLabel.setHorizontalAlignment(SwingConstants.LEFT);
        shadowPanel.add(shadowLabel);
        shadowPanel.add(shadowInputPanel);

        String selectedType = Objects.requireNonNull(clientTypeComboBox.getSelectedItem()).toString();
        isVisible(selectedType);

        input.add(cpanel);
        input.add(dpanel);
        input.add(epanel);
        input.add(fpanel);
        input.add(gpanel);
        input.add(hpanel);
        input.add(ipanel);
        input.add(shadowPanel);
    }

    private void contract(JPanel input, int x, int y, String dogovorName,
                          String directorName, String chiefAccountantName,
                          String purchasingManagerName) {
        String[] mainContract;

        mainContract = clientBD.getMainContract(codeField.getText());

        directorField = new JTextField(directorName);
        chiefAccountantField = new JTextField(chiefAccountantName);
        purchasingManagerField = new JTextField(purchasingManagerName);

        JPanel jpanel = getPanel(x, y);
        JPanel kpanel = getPanel(x, y);
        JPanel lpanel = getPanel(x, y);
        JPanel mpanel = getPanel(x, y);

        JLabel mainContractLabel = new JLabel("Основной договор: ");
        JLabel directorLabel = new JLabel("Директор: ");
        JLabel chiefAccountantLabel = new JLabel("Главный бухгалтер: ");
        JLabel purchasingManagerLabel = new JLabel("Менеджер по закупкам: ");

        mainContractComboBox = new JComboBox<>(mainContract);
        mainContractComboBox.setSelectedItem(dogovorName);

        jpanel.add(mainContractLabel);
        jpanel.add(mainContractComboBox);

        kpanel.add(directorLabel);
        kpanel.add(directorField);

        lpanel.add(chiefAccountantLabel);
        lpanel.add(chiefAccountantField);

        mpanel.add(purchasingManagerLabel);
        mpanel.add(purchasingManagerField);

        input.add(jpanel);
        input.add(kpanel);
        input.add(lpanel);
        input.add(mpanel);
    }

    private void address(JPanel input, int x, int y,
                         String addressPost, String addressUr, String code) {
        String[] address;
        address = clientBD.getAddressByClientCode(code);

        JLabel postAddressLabel = new JLabel("Почтовый адресс: ");
        postAddressComboBox = new JComboBox<>(address);
        postAddressComboBox.setPreferredSize(new Dimension(100, 100));
        postAddressComboBox.setSelectedItem(addressPost);

        JLabel urAddressLabel = new JLabel("Юридический адрес: ");
        urAddressComboBox = new JComboBox<>(address);
        postAddressComboBox.setPreferredSize(new Dimension(60, 60));
        urAddressComboBox.setSelectedItem(addressUr);

        JPanel apanel = getPanel(x, y);
        JPanel bpanel = getPanel(x, y);

        apanel.add(postAddressLabel);
        apanel.add(postAddressComboBox);

        bpanel.add(urAddressLabel);
        bpanel.add(urAddressComboBox);

        input.add(apanel);
        input.add(bpanel);
    }

    private void currentAccount(JPanel input, int x, int y,
                                String currentAccountSelected) {
        String[] currentAccount;

        currentAccount = clientBD.getCurrentAccountByClientCode(codeField.getText());

        JLabel currentAccountLabel = new JLabel("Расчетный счет: ");
        currentAccountComboBox = new JComboBox<>(currentAccount);
        currentAccountComboBox.setSelectedItem(currentAccountSelected);

        JPanel apanel = new JPanel();
        apanel.setLayout(new BoxLayout(apanel, BoxLayout.X_AXIS));
        apanel.setMaximumSize(new Dimension(y, x));

        apanel.add(currentAccountLabel);
        apanel.add(currentAccountComboBox);

        input.add(apanel);
    }

    private void buttons(JPanel input, int x, int y) {

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setMaximumSize(new Dimension(y - 100, x));

        JButton saveButton = new JButton("Сохранить клиента");
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> jButtonChangeActionPerformed());

        JButton addressButton = new JButton("Добавить адрес");
        addressButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addressButton.addActionListener(e -> jButtonAddressActionPerformed());

        JButton contractButton = new JButton("Добавить договор");
        contractButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contractButton.addActionListener(e -> jButtonContractActionPerformed());

        JButton currentAccountButton = new JButton("Добавить расчетный счет");
        currentAccountButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        currentAccountButton.addActionListener(e -> jButtonCurrentAccountActionPerformed());

        buttonPanel.add(addressButton);
        buttonPanel.add(contractButton);
        buttonPanel.add(currentAccountButton);
        buttonPanel.add(saveButton);

        input.add(buttonPanel);
    }

    private void refreshAddressComboBoxes() {
        String[] addresses = clientBD.getAddressByClientCode(codeField.getText());
        postAddressComboBox.setModel(new DefaultComboBoxModel<>(addresses));
        urAddressComboBox.setModel(new DefaultComboBoxModel<>(addresses));
    }

    private void refreshCurrentAccountComboBoxes() {
        String[] contracts = clientBD.getMainContract(codeField.getText());
        mainContractComboBox.setModel(new DefaultComboBoxModel<>(contracts));
    }

    private void refreshContractComboBoxes() {
        String[] accounts = clientBD.getCurrentAccountByClientCode(codeField.getText());
        currentAccountComboBox.setModel(new DefaultComboBoxModel<>(accounts));
    }

    private void jButtonCurrentAccountActionPerformed() {
        jButtonAddActionPerformed();
        AddCurrentAccountForm form = new AddCurrentAccountForm(frame, codeField.getText());
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshCurrentAccountComboBoxes();
            }
        });
    }

    private void jButtonContractActionPerformed() {
        jButtonAddActionPerformed();
        AddContractForm form = new AddContractForm(frame, codeField.getText());
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshContractComboBoxes();
            }
        });

    }

    private void jButtonAddressActionPerformed() {
        jButtonAddActionPerformed();
        AddAddressForm form = new AddAddressForm(frame, codeField.getText());
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshAddressComboBoxes();
            }
        });

    }

    private void jButtonChangeActionPerformed() {
        if (fieldsIsFull()) {
            String numberContract = mainContractComboBox.getSelectedItem() != null ?
                    mainContractComboBox.getSelectedItem().toString()
                            .substring(mainContractComboBox.getSelectedItem().toString().lastIndexOf("№") + 1,
                                    mainContractComboBox.getSelectedItem().toString().lastIndexOf("от") - 1)
                    : "";

            String currentAccount = currentAccountComboBox.getSelectedItem() != null ?
                    currentAccountComboBox.getSelectedItem().toString()
                            .substring(currentAccountComboBox.getSelectedItem().toString().lastIndexOf("Р/с:") + 5)
                    : "";

            NewClientDTO clientDTO = getNewClientDTO();
            clientDTO.setMainContract(numberContract);
            clientDTO.setCurrentAccount(currentAccount);
            clientDTO.setPostAddress(Objects.requireNonNull(postAddressComboBox.getSelectedItem()).toString());
            clientDTO.setUrAddress(Objects.requireNonNull(urAddressComboBox.getSelectedItem()).toString());
            clientDTO.setDirector(directorField.getText());
            clientDTO.setChiefAccount(chiefAccountantField.getText());
            clientDTO.setPurchasingManager(purchasingManagerField.getText());
            clientBD.changeClient(clientDTO);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Заполните поля!",
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonAddActionPerformed() {
        if (fieldsIsFull()) {
            clientBD.addClient(getNewClientDTO());
        } else {
            JOptionPane.showMessageDialog(null,
                    " Заполните поля!",
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean fieldsIsFull() {
        return !codeField.getText().trim().isEmpty() &&
                !nameField.getText().trim().isEmpty() &&
                !fullnameField.getText().trim().isEmpty() &&
                !phonesField.getText().trim().isEmpty() &&
                !unnField.getText().trim().isEmpty() &&
                !discountField.getText().trim().isEmpty() &&
                !codeCountryField.getText().trim().isEmpty() &&
                !typeOrganizationField.getText().trim().isEmpty();
    }

    private NewClientDTO getNewClientDTO() {

        return NewClientDTO.builder()
                .isResident(isResidentCheckbox.isSelected())
                .isClient(isClientCheckbox.isSelected())
                .code(Integer.parseInt(codeField.getText()))
                .name(nameField.getText())
                .fullName(fullnameField.getText())
                .phoneNumber(phonesField.getText())
                .unn(unnField.getText())
                .licence(licenceField.getText())
                .okpo(okpofield.getText())
                .codeCountry(codeCountryField.getText())
                .discount(discountField.getText())
                .typeOrganisation(typeOrganizationField.getText())
                .clientType(clientTypeComboBox.getSelectedIndex())
                .build();
    }

    private void jButtonChangeTypeOfClientActionPerformed() {
        String selectedType = Objects.requireNonNull(clientTypeComboBox.getSelectedItem()).toString();
        isVisible(selectedType);
        revalidate();
        repaint();
    }

    private void isVisible(String selectedType) {
        if (selectedType.equals("Розничный магазин")) {
            shadowLabel.setVisible(true);
            adultLabel.setVisible(true);
            adultField.setVisible(true);
            childrenLabel.setVisible(true);
            childrenField.setVisible(true);
        } else {
            shadowLabel.setVisible(false);
            adultLabel.setVisible(false);
            adultField.setVisible(false);
            childrenLabel.setVisible(false);
            childrenField.setVisible(false);
        }
    }

    private JPanel getPanel(int x, int y) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setMaximumSize(new Dimension(y, x));
        return panel;
    }
}
