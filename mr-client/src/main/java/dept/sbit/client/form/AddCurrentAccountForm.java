package dept.sbit.client.form;

import dept.sbit.client.dao.ClientBD;
import dept.sbit.client.dao.dto.CurrentAccountDTO;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AddCurrentAccountForm extends JDialog {
    private JTextField accountNameField;
    private JTextField numberField;
    private JComboBox<String> currencyTypeField;
    private JComboBox<String> bankField;
    private static final String[] currencyTypes = {"Беларусский рубль", "Российский рубль",
            "Доллар США", "Евро","Гривна"};
    private final ClientBD clientBD;
    private final String clientId;

    public AddCurrentAccountForm(JFrame owner, String code) {
        super(owner, true);
        clientBD = new ClientBD();
        clientId = code;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(owner);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        inputPanel.add(new JLabel("Название счета:"));
        accountNameField = new JTextField();
        inputPanel.add(accountNameField);

        inputPanel.add(new JLabel("Номер счета:"));
        numberField = new JTextField();
        inputPanel.add(numberField);

        inputPanel.add(new JLabel("Валюта:"));
        currencyTypeField = new JComboBox<>(currencyTypes);
        inputPanel.add(currencyTypeField);

        inputPanel.add(new JLabel("Банк:"));
        bankField = new JComboBox<>(
                clientBD.getBanks().stream()
                        .map(e -> e.getBankName().trim() + " "
                                + e.getAddress().trim() + " " + e.getMFO().trim()
                                + " id:" + e.getId().trim())
                        .toArray(String[]::new));
        inputPanel.add(bankField);

        JPanel buttonPanel = getButtonPanel();

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(inputPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Добавить новый счет");
        setVisible(true);
    }

    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Добавить счет");
        JButton addBankButton = new JButton("Добавить банк");
        JButton cancelButton = new JButton("Отмена");

        addButton.addActionListener(e -> addButtonActionListener());

        addBankButton.addActionListener(e -> {
            new AddBankForm(null, "Управление банками", true);
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(addBankButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void addButtonActionListener() {
        String accountName = accountNameField.getText().trim();
        String accountNumber = numberField.getText().trim();
        Integer currencyType = currencyTypeField.getSelectedIndex();
        String bank = Objects.requireNonNull(bankField.getSelectedItem()).toString();
        String bankId = bank.substring(bank.lastIndexOf(":") + 1);

        if (accountName.isEmpty() || accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(
                    AddCurrentAccountForm.this,
                    "Пожалуйста, заполните все обязательные поля",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        clientBD.addCurrentAccount(
                CurrentAccountDTO.builder()
                        .accountName(accountName)
                        .accountNumber(accountNumber)
                        .currencyType(currencyType)
                        .bankId(Integer.parseInt(bankId))
                        .clientId(Integer.parseInt(clientId))
                        .build());
    }
}
