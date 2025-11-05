package dept.marketing.report;

import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.ProgressBar;
import common.UtilFunctions;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

public class OtgruzForm extends javax.swing.JDialog {
    ProgressBar pb;
    OpenOffice oo;
    Vector rezaltData = new Vector();
    HashMap<String, String> status = new HashMap<>();
    HashMap<String, String> map = new LinkedHashMap<>();
    String vid[][] = {
            {"отгрузка", ">"},
            {"возвраты", "<"},
            {"отгрузка и возвраты", "<>"}};
    private JButton buttAdd;
    private JButton buttMake;
    private JButton buttClose;
    private JComboBox comboBox;
    private JDateChooser sStDate;
    private JDateChooser eStDate;
    private JLabel jTitle;
    private JLabel jInfoText;
    private JLabel jClient;
    private JPanel osnova;
    private JPanel buttPanel;
    private JPanel upPanel;
    private JPanel searchPanel;
    private JPanel searchButtPanel;
    private JTable table;
    private JPanel datePanel;
    private JButton buttClean;
    private DefaultTableModel tModel;
    private Object[] col;
    private Vector tmpClient;
    private ButtonGroup buttonGroup;
    private int idClient;
    private String nameClient;
    private ReportPDB pdb;
    private ReportDB db;

    public OtgruzForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        init();
        setTitle("Отгрузка покупателю");

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        sStDate.setDate(c.getTime());

        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        eStDate.setDate(c.getTime());

        try {
            pdb = new ReportPDB();
            String[] period = pdb.getPeriodOtgruz();
            jInfoText.setText("Доспупный период c " + period[0] + " по " + period[1]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }

        for (int k = 0; k < vid.length; k++) {
            status.put(vid[k][0], vid[k][1]);
            comboBox.addItem(vid[k][0]);
        }

        createTable(new Object[][]{});

        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(400, 400));
        setPreferredSize(new Dimension(550, 500));

        col = new Object[2];
        col[0] = "Код";
        col[1] = "Название";

        sStDate = new JDateChooser();
        sStDate.setPreferredSize(new Dimension(200, 20));

        eStDate = new JDateChooser();
        eStDate.setPreferredSize(new Dimension(200, 20));

        jTitle = new JLabel();
        jTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jTitle.setFont(new java.awt.Font("Dialog", 1, 13));
        jTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitle.setText("Задайте параметры для формирования отчёта");

        jClient = new JLabel();
        jClient.setFont(new java.awt.Font("Dialog", 0, 13));
        jClient.setText("");

        buttAdd = new JButton();
        buttAdd.setText("Выбрать");
        buttAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buttAdd.addActionListener(evt -> jButton1ActionPerformed());

        buttClean = new JButton();
        buttClean.setText("Очистить");
        buttClean.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buttClean.addActionListener(evt -> jButton4ActionPerformed());

        comboBox = new JComboBox();
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        comboBox.setFont(new java.awt.Font("Dialog", 0, 13));
        comboBox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        comboBox.setPreferredSize(new Dimension(200, 30));

        JRadioButton jRadioButton1 = new JRadioButton();
        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setText("По моделям;");
        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton1.setActionCommand("1");
        jRadioButton1.setSelected(true);

        JRadioButton jRadioButton2 = new JRadioButton();
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setText("По  группам изделий;");
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setActionCommand("2");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);

        jInfoText = new JLabel();
        jInfoText.setFont(new java.awt.Font("Dialog", 2, 12));
        jInfoText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        upPanel = new JPanel();
        upPanel.setLayout(new BorderLayout(1, 1));
        upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.add(jTitle, BorderLayout.NORTH);
        upPanel.add(new JLabel("Вид:   "), BorderLayout.WEST);
        upPanel.add(comboBox, BorderLayout.CENTER);
        upPanel.add(jInfoText, BorderLayout.SOUTH);

        datePanel = new JPanel();
        datePanel.setLayout(new ParagraphLayout());
        datePanel.add(new JLabel("с:"), ParagraphLayout.NEW_PARAGRAPH);
        datePanel.add(sStDate);
        datePanel.add(new JLabel("по:"), ParagraphLayout.NEW_PARAGRAPH);
        datePanel.add(eStDate);
        datePanel.add(jRadioButton1, ParagraphLayout.NEW_PARAGRAPH);
        datePanel.add(jRadioButton2);

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createTitledBorder("Контрагенты:"));

        searchButtPanel = new JPanel();
        searchButtPanel.setLayout(new GridLayout(6, 0, 5, 5));
        searchButtPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchButtPanel.add(buttAdd);
        searchButtPanel.add(buttClean);

        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout(1, 1));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.add(datePanel, BorderLayout.NORTH);
        searchPanel.add(pane, BorderLayout.CENTER);
        searchPanel.add(searchButtPanel, BorderLayout.EAST);

        buttMake = new JButton();
        buttMake.setFont(new java.awt.Font("Dialog", 0, 13));
        buttMake.setText("Сформировать");
        buttMake.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buttMake.addActionListener(evt -> jButton2ActionPerformed());

        buttClose = new JButton();
        buttClose.setFont(new java.awt.Font("Dialog", 0, 13));
        buttClose.setText("Отмена");
        buttClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buttClose.addActionListener(evt -> jButton3ActionPerformed());

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttMake);

        osnova = new JPanel();
        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(searchPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(osnova);
        pack();
    }

    private void jButton4ActionPerformed() {
        tModel.getDataVector().removeAllElements();
        tModel.fireTableDataChanged();
    }

    private void jButton3ActionPerformed() {
        dispose();
    }

    private void jButton1ActionPerformed() {
        new ClientForm(this, true, true);

        if (getIdClient() != 10) {
            tmpClient = new Vector();
            tmpClient.add(getIdClient());
            tmpClient.add(getNameClient());

            if (tModel.getDataVector().contains(tmpClient)) {
                return;
            }

            tModel.addRow(new Object[]{getIdClient(), getNameClient()});
            tModel.fireTableDataChanged();
        }
    }

    private void jButton2ActionPerformed() {
        if (tModel.getRowCount() < 1)
            JOptionPane.showMessageDialog(null, "Вы не выбрали покупателя!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        else {
            try {
                if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()))
                        && UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()))) {

                    pb = new ProgressBar(this, false, "Формирование отчёта...");
                    SwingWorker sw = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            ArrayList<Integer> idClients = new ArrayList<>();

                            for (int i = 0; i < tModel.getRowCount(); i++)
                                idClients.add(Integer.valueOf(tModel.getValueAt(i, 0).toString()));

                            try {
                                pdb = new ReportPDB();
                                rezaltData = pdb.getOtgruz(
                                        idClients,
                                        new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()),
                                        new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()),
                                        status.get(comboBox.getSelectedItem().toString()));
                            } catch (Exception e) {
                                rezaltData = new Vector();
                            } finally {
                                pdb.disConn();
                            }

                            if (buttonGroup.getSelection().getActionCommand().equals("2")) {
                                pb.setMessage("Группировка...");

                                try {
                                    db = new ReportDB();
                                    rezaltData = db.getGroupOtgruzClient(rezaltData);
                                } catch (Exception e) {
                                    rezaltData = new Vector();
                                }
                            }

                            String[] namesClient = new String[tModel.getRowCount()];

                            for (int i = 0; i < tModel.getRowCount(); i++)
                                namesClient[i] = tModel.getValueAt(i, 1).toString();

                            ReportOO oo = new ReportOO(
                                    "Отгрузка " + " c " + new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()) +
                                            " по " + new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate()),
                                    namesClient,
                                    rezaltData);

                            oo.createReport("AnalizOtgruzClient.ots");

                            return 0;
                        }

                        @Override
                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    pb.setVisible(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Неверно задан период!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setClient(int id, String name) {
        idClient = id;
        nameClient = name;

    }

    private int getIdClient() {
        return idClient;
    }

    private String getNameClient() {
        return nameClient;
    }


    private void createTable(final Object[][] row) {
        tModel = new DefaultTableModel(row, col);

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(95);

    }
}
