package dept.sbit.client.form;

import dept.sbit.client.dao.ClientBD;
import dept.sbit.client.dao.dto.ClientDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class ClientForm extends JDialog {

    private JButton addButton;
    private JButton changeButton;

    private JFrame jframe;

    private JTable clientTable;
    private DefaultTableModel tableModel;
    private ClientBD clientBD;

    private List<ClientDTO> clients;

    public ClientForm(JFrame frame, boolean b) {
        super(frame, b);
        this.jframe = frame;
        init();
        clientBD = new ClientBD();
        setData();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(800, 600));
        setTitle("Управление клиентами");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel input = new JPanel();
        input.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButton = new JButton("Добавить");
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> jButtonAddActionPerformed());

        changeButton = new JButton("Изменить");
        changeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        changeButton.addActionListener(e -> jButtonChangeActionPerformed());

        input.add(addButton, BorderLayout.EAST);
        input.add(changeButton, BorderLayout.WEST);

        String[] columnNames = {"Код", "Наименование", "Адрес", "Расчетный счет", "Телефон", "УНН"};
        tableModel = new DefaultTableModel(columnNames, 0);
        clientTable = new JTable(tableModel);
        clientTable.setAutoCreateRowSorter(true);
        resizeColumns();
        JScrollPane tableScrollPane = new JScrollPane(clientTable);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.add(input, BorderLayout.SOUTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        pack();
    }

    private void jButtonAddActionPerformed(){
        new AddClientForm(jframe, true);
    }

    private void jButtonChangeActionPerformed(){
        if (clientTable.getSelectedRowCount() > 0) {
            new AddClientForm(jframe, true,
                    clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString());

        } else
            JOptionPane.showMessageDialog(null,
                    "Вы не выбрали ни одного сотрудника!",
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

    }

    private void setData(){
        clients = clientBD.getClients();
        clients.forEach(c -> {
            Object[] rowData = {
                    c.getCode(),
                    c.getName(),
                    c.getAddress(),
                    c.getAccountNumber(),
                    c.getPhone(),
                    c.getUNN()
            };
            tableModel.addRow(rowData);
        });
    }

    private void resizeColumns(){
        TableColumn column;
        clientTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        column = clientTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column.setMaxWidth(50);

        column = clientTable.getColumnModel().getColumn(1);
        column.setResizable(true);
        column.setPreferredWidth(250);

        column = clientTable.getColumnModel().getColumn(2);
        column.setPreferredWidth(130);

        column = clientTable.getColumnModel().getColumn(3);
        column.setPreferredWidth(120);

        column = clientTable.getColumnModel().getColumn(4);
        column.setPreferredWidth(120);

        column = clientTable.getColumnModel().getColumn(5);
        column.setPreferredWidth(100);
    }
}
