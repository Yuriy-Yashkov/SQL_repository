package dept.sbit.client.form;

import dept.sbit.client.dao.ClientBD;
import dept.sbit.client.dao.dto.BankDto;
import dept.sbit.client.dao.dto.ClientDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AddBankForm extends JDialog {
    private ClientBD clientBD;
    private JTextField bankNameField;
    private JTextField bankAddressField;
    private JCheckBox resident;
    private JTextField MFOField;
    private JTextField SWIFTField;
    private JTextField bankCorrespondentNameField;
    private JTextField bankCorrespondentNumberField;
    private JTextField bankCorrespondentUnnField;
    private final static String[] columns = {"id", "Название", "Адрес", "МФО"};

    private JTable banksTable;
    private DefaultTableModel banksTableModel;

    public AddBankForm(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        clientBD = new ClientBD();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(owner);

        banksTableModel = new DefaultTableModel(columns, 0);
        banksTable = new JTable(banksTableModel);
        banksTable.setAutoCreateRowSorter(true);

        JPanel inputPanel = createInputPanel();
        JScrollPane tableScrollPane = new JScrollPane(banksTable);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(tableScrollPane, BorderLayout.CENTER);

        setupEventHandlers();
        updateBanks();

        setTitle("Управление банками");
        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));

        // Создание полей ввода
        bankNameField = new JTextField();
        bankAddressField = new JTextField();
        resident = new JCheckBox();
        MFOField = new JTextField();
        SWIFTField = new JTextField();
        bankCorrespondentNameField = new JTextField();
        bankCorrespondentNumberField = new JTextField();
        bankCorrespondentUnnField = new JTextField();

        // Добавление меток и полей
        panel.add(new JLabel("Название банка:"));
        panel.add(bankNameField);
        panel.add(new JLabel("Адрес:"));
        panel.add(bankAddressField);
        panel.add(new JLabel("Резидент:"));
        panel.add(resident);
        panel.add(new JLabel("МФО:"));
        panel.add(MFOField);
        panel.add(new JLabel("SWIFT:"));
        panel.add(SWIFTField);
        panel.add(new JLabel("Корреспондент:"));
        panel.add(bankCorrespondentNameField);
        panel.add(new JLabel("Номер корреспондента:"));
        panel.add(bankCorrespondentNumberField);
        panel.add(new JLabel("УНН корреспондента:"));
        panel.add(bankCorrespondentUnnField);

        return panel;
    }

    private void setupEventHandlers() {
        JButton addButton = new JButton("Добавить банк");
        addButton.addActionListener(e -> addBank());

        JButton deleteButton = new JButton("Удалить банк");
        deleteButton.addActionListener(e -> deleteSelectedBank());

        JButton updateButton = new JButton("Обновить банк");
        updateButton.addActionListener(e -> updateSelectedBank());

        banksTable.getSelectionModel().addListSelectionListener(e -> {
            BankDto bankDto;
            String code = banksTable.getValueAt(banksTable.getSelectedRow(), 0).toString();
            if (!code.isEmpty()) {
                bankDto = clientBD.getBankById(Integer.parseInt(code));
                bankNameField.setText(bankDto.getBankName());
                bankAddressField.setText(bankDto.getAddress());
                MFOField.setText(bankDto.getMFO());
                SWIFTField.setText(bankDto.getSwift());
                bankCorrespondentNameField.setText(bankDto.getKornaim());
                bankCorrespondentNumberField.setText(bankDto.getKorschet());
                bankCorrespondentUnnField.setText(bankDto.getKorunn());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        Container contentPane = getContentPane();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addBank() {
        String[] rowData = {
                bankNameField.getText().trim(),
                bankAddressField.getText().trim(),
                resident.isSelected() ? "Да" : "Нет",
                MFOField.getText().trim(),
                SWIFTField.getText().trim(),
                bankCorrespondentNameField.getText().trim(),
                bankCorrespondentNumberField.getText().trim(),
                bankCorrespondentUnnField.getText().trim()
        };

        if (hasEmptyFields(rowData)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Пожалуйста, заполните все обязательные поля",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        banksTableModel.addRow(rowData);
        clearFields();
    }

    private void deleteSelectedBank() {
        int selectedRow = banksTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Вы уверены, что хотите удалить этот банк?",
                    "Подтверждение",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                banksTableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Выберите банк для удаления",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void updateSelectedBank() {
        int selectedRow = banksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Выберите банк для обновления",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String[] newData = {
                bankNameField.getText().trim(),
                bankAddressField.getText().trim(),
                resident.isSelected() ? "Да" : "Нет",
                MFOField.getText().trim(),
                SWIFTField.getText().trim(),
                bankCorrespondentNameField.getText().trim(),
                bankCorrespondentNumberField.getText().trim(),
                bankCorrespondentUnnField.getText().trim()
        };

        if (hasEmptyFields(newData)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Пожалуйста, заполните все обязательные поля",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        for (int i = 0; i < newData.length; i++) {
            banksTableModel.setValueAt(newData[i], selectedRow, i);
        }
    }

    private boolean hasEmptyFields(String[] fields) {
        return java.util.Arrays.stream(fields).anyMatch(String::isEmpty);
    }

    private void clearFields() {
        bankNameField.setText("");
        bankAddressField.setText("");
        resident.setSelected(false);
        MFOField.setText("");
        SWIFTField.setText("");
        bankCorrespondentNameField.setText("");
        bankCorrespondentNumberField.setText("");
        bankCorrespondentUnnField.setText("");
    }

    private void updateBanks() {
        clientBD.getBanks().forEach(e -> {
            Object[] row = {e.getId(), e.getBankName(), e.getAddress(), e.getMFO()};
            banksTableModel.addRow(row);
        });
    }
}
