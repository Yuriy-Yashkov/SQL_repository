package dept.sbit.client.form;

import dept.sbit.client.dao.ClientBD;
import dept.sbit.client.dao.dto.ContractDto;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddContractForm extends JDialog {
    private final String code;
    private JTextField nameField;
    private JTextField numberField;
    private JXDatePicker startDateField;
    private JXDatePicker endDateField;
    private static final String[] labels = {"Наименование", "Номер", "Дата начало", "Дата окончания"};
    private final ClientBD client;
    private DefaultListModel<String> accountListModel;

    public AddContractForm(JFrame parent, String code) {
        super(parent, true);
        this.code = code;
        client = new ClientBD();

        addInit();

        loadAddContract();

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void addInit() {
        JList<String> accountList;
        setPreferredSize(new Dimension(600, 500)); // Увеличенная высота для списка
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBorder(BorderFactory.createTitledBorder("Расчетные счета"));

        accountListModel = new DefaultListModel<>();
        accountList = new JList<>(accountListModel);
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(accountList);
        listScrollPane.setPreferredSize(new Dimension(300, 100));

        accountPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        nameField = new JTextField();
        numberField = new JTextField();
        startDateField = new JXDatePicker();
        startDateField.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
        startDateField.setDate(Calendar.getInstance().getTime());

        endDateField = new JXDatePicker();
        endDateField.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
        endDateField.setDate(Calendar.getInstance().getTime());

        inputPanel.add(new JLabel(labels[0]));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel(labels[1]));
        inputPanel.add(numberField);
        inputPanel.add(new JLabel(labels[2]));
        inputPanel.add(startDateField);
        inputPanel.add(new JLabel(labels[3]));
        inputPanel.add(endDateField);

        JButton addButton = new JButton("Добавить контракт");
        addButton.addActionListener(e -> addContract());

        Box mainBox = Box.createVerticalBox();
        mainBox.add(accountPanel);
        mainBox.add(Box.createRigidArea(new Dimension(0, 10)));
        mainBox.add(inputPanel);
        mainBox.add(Box.createRigidArea(new Dimension(0, 20)));
        mainBox.add(addButton);

        add(mainBox);
        pack();
    }

    private void addContract() {
        String name = nameField.getText();
        String number = numberField.getText();
        java.util.Date startDate = startDateField.getDate();
        java.util.Date endData = endDateField.getDate();

        if (name.isEmpty() || number.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Все поля обязательны для заполнения",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        ContractDto contract = ContractDto.builder()
                .nameContract(name)
                .numberContract(number)
                .beginDate(new Date(startDate.getTime()))
                .endDate(new Date(endData.getTime()))
                .clientId(Integer.parseInt(code))
                .build();

        if (client.addContract(contract)) {
            loadAddContract();
            clearFields();
            JOptionPane.showMessageDialog(
                    this,
                    "Контракт успешно добавлен",
                    "Информация",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Ошибка при сохранении контракта",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadAddContract() {
        String[] contracts = client.getMainContract(code);
        accountListModel.clear();
        for (String contract : contracts) {
            accountListModel.addElement(contract);
        }
    }

    private void clearFields() {
        nameField.setText("");
        numberField.setText("");
        startDateField.setDate(new Date(0));
        endDateField.setDate(new java.util.Date(0));
    }
}
