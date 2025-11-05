package dept.sklad;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;
import common.CustomColumnCellRenderer;
import common.ProgressBar;
import dept.sklad.model.ResultTTN;
import dept.sklad.model.TemplateDocListForm;
import workDB.DB;
import workDB.PDB;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * Форма показывает содержимое накладной, цену товара как в рублях так и в
 * валюте с возможностью редактирования цены за единицу продукции
 */
@SuppressWarnings("all")
public class VozvratValuta extends TemplateDocListForm {

    private static final String solve = "Solve";
    /** Массив колонок в таблице формы */
    @SuppressWarnings("rawtypes")
    protected Vector columnsStory = new Vector();
    /** Коллекция строк таблицы формы */
    @SuppressWarnings("rawtypes")
    protected Vector rowsStory = new Vector();
    JButton bRecalculate;
    JButton bProtocol;
    JLabel lOldSum;
    JLabel lNewSum;
    @SuppressWarnings("rawtypes")
    Vector temp = new Vector();
    String ndoc;
    String client;
    int clientID;
    ResultTTN result;
    ProgressBar pb;
    private TableColumn tcol;
    private JTextField tf;
    private DB db;
    private DB storyDB;
    // *********************************************************************
    // Панель истории товара
    // *********************************************************************
    private MaskFormatter formatter;
    private JPanel panelStoryHeader;
    private JPanel panelStoryButton;
    private JButton bSetCost;
    private JTable tableStory;
    private DefaultTableModel tModelStory;
    private JScrollPane sbTableStory;
    // *********************************************************************
    // Поля информации о накладной
    // *********************************************************************
    private JLabel txtValuta;

    private JLabel txtBelStoimost;
    private JLabel txtBelSummaNDS;
    private JLabel txtBelItogo;

    private JLabel txtValutaStoimost;
    private JLabel txtValutaSummaNDS;
    private JLabel txtValutaItogo;

    private JRadioButton modeManual;
    private JRadioButton modeStory;

    private JDialog myself;

    private JSplitPane splitPane;

    private JPanel panelInfo;

    private JLabel lblDocNumber;
    private JLabel lblClient;
    private boolean tableWasChanged = false;

    private JCheckBox cbCarret = new JCheckBox("Переход по [Enter]             ");

    @SuppressWarnings({"rawtypes", "unchecked"})
    public VozvratValuta(MainController mainController, String count, String ndoc, String c) {
        super(mainController, FRAME_DEFAULT);
        setTitle("Накладная возврата в валюте № " + ndoc);
        client = c;
        this.ndoc = ndoc;

        db = new DB();
        result = db.getTTNInfo(ndoc);
        temp = db.getTTNAllDescr(ndoc);
        clientID = db.getClientID(ndoc);

        PDB pdb = new PDB();
        Iterator it = temp.iterator();
        while (it.hasNext()) {
            Vector v = (Vector) it.next();
            Vector vNew = new Vector();
            vNew.add(v.get(0));
            vNew.add(v.get(1));
            vNew.add(v.get(2));
            vNew.add(v.get(3));
            vNew.add(v.get(4));
            vNew.add(v.get(5));
            vNew.add(v.get(6));
            vNew.add(v.get(7));
            vNew.add(v.get(8));
            vNew.add(v.get(9));
            vNew.add(v.get(11));
            vNew.add(v.get(10));
            vNew.add(v.get(12));
            rows.add(vNew);
        }
        pdb.disConn();

        try {
            initComponents();
            lFoot.setText(count);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка инициализации компонентов", "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        try {
            initTableModel();
            initTableStoryModel();
            table.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка инициализации модели таблицы", "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setTTNInfo(result);

/*		MyReportsModule.desktop.add(this);
		MyReportsModule.desktop.getDesktopManager().activateFrame(this);
		//parent.setTitle(getTitle()+" : "+ MyReportsModule.titleText);*/
        setVisible(true);
    }

    private void setTTNInfo(ResultTTN result) {
        NumberFormat formatRub = NumberFormat.getCurrencyInstance();
        txtBelStoimost.setText(formatRub.format(result.getSummaRub()));
        txtBelSummaNDS.setText(formatRub.format(result.getSummaRubNDS()));
        txtBelItogo.setText(formatRub.format(result.getSummaRubItogo()));
        txtValutaStoimost.setText(formatRub.format(result.getSummaValuta()));
        txtValutaSummaNDS.setText(formatRub.format(result.getSummaValutaNDS()));
        txtValutaItogo.setText(formatRub.format(result.getSummaValutaItogo()));
        txtValuta.setText(result.getValutaName() + "(" + result.getValutaID()
                + ")");
    }

    /** Инициализация модели таблицы */
    @SuppressWarnings("unchecked")
    private void initTableStoryModel() {

        columnsStory.add("№ документа");
        columnsStory.add("Дата создания");
        columnsStory.add("Дата отгрузки");
        columnsStory.add("Покупатель");
        columnsStory.add("Цена бел. руб.");
        columnsStory.add("Цена росс. руб.");

        // создаём таблицу
        tModelStory = new DefaultTableModel(rowsStory, columnsStory) {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("rawtypes")
            @Override
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    if (column == 5) {
                        returnValue = Double.class;
                    } else {
                        returnValue = getValueAt(0, column).getClass();
                    }

                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == -1);
            }
        };

        tableStory.setModel(tModelStory);

        if (result.getValutaID() == 1) {
            tableStory.getColumnModel().getColumn(5).setMaxWidth(0);
            tableStory.getColumnModel().getColumn(5).setMinWidth(0);
            tableStory.getColumnModel().getColumn(5).setPreferredWidth(0);
        }
    }

    /**
     * Запрос к базе данных для поиска конкретного товара в накладных по
     * выбранному покупателю
     */
    private void getStoryInfo() {
        pb = new ProgressBar(myself, false, "Поиск в накладных...");
        class SWorker extends SwingWorker<Object, Object> {

            public SWorker() {

            }

            @SuppressWarnings("rawtypes")
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    Date dateBegin = null;
                    Date dateEnd = null;
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd.MM.yyyy");

                    try {
                        //dateBegin = dateFormat.parse(ftDateBegin.getText());
                        //dateEnd = dateFormat.parse(ftDateEnd.getText());

                    } catch (Exception e) {
                        System.out.println("Ошибка преобразования даты");
                    }

                    try {
                        final int row = table.getSelectedRow();
                        String ean = table.getValueAt(row, 10).toString();
                        rowsStory = storyDB.getDocListByItem(ean, clientID,
                                periodPicker.getDatePickerBegin(), periodPicker.getDatePickerEnd());

                    } catch (Exception ex) {
                        System.out.println("Ошибка при запросе к базе");
                    }

                    tableStory.setVisible(false);
                    for (int i = tModelStory.getRowCount() - 1; i > -1; i--) {
                        tModelStory.removeRow(i);
                    }
                    for (int i = 0; i < rowsStory.size(); i++)
                        tModelStory.addRow((Vector) rowsStory.get(i));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                            "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {

                }
                return 0;
            }

            @Override
            protected void done() {
                try {
                    pb.dispose();
                    tableStory.setVisible(true);
                    tableStory.repaint();

                } catch (Exception ex) {
                    System.err
                            .println("Ошибка выполнения метода getStoryInfo() "
                                    + ex);
                }

            }
        }

        SWorker sw = new SWorker();
        sw.execute();
        pb.setVisible(true);
    }

    @SuppressWarnings({"unchecked"})
    private void initTableModel() {
        columns.add("Модель");
        columns.add("Артикул");
        columns.add("Наименование");
        columns.add("Размер");
        columns.add("Сорт");
        columns.add("Цвет");
        columns.add("Кол");
        columns.add("Цена бел.руб");
        columns.add("Цена, валюта");
        columns.add("");
        columns.add("EAN");
        columns.add("docid");
        columns.add("scan");

        tModel = new DefaultTableModel(rows, columns) {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("rawtypes")
            @Override
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    if (column == 8) {
                        returnValue = Double.class;
                    } else if (column == 12) {
                        returnValue = Long.class;
                    } else {
                        returnValue = getValueAt(0, column).getClass();
                    }
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == 7) || (column == 8);
            }

            @Override
            public void setValueAt(Object value, int row, int column) {
                if (column == 7) {
                    Long v = (Long) value;
                    if (0 <= v) {
                        super.setValueAt(value, row, column);
                        tableWasChanged = true;
                        bRecalculate.setVisible(true);
                    } else
                        JOptionPane.showMessageDialog(null,
                                "Цена должна быть больше 0", "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                if (column == 8) {
                    Double v = (Double) value;
                    if (0 <= v) {
                        super.setValueAt(value, row, column);
                        tableWasChanged = true;
                        bRecalculate.setVisible(true);
                    } else
                        JOptionPane.showMessageDialog(null,
                                "Цена должна быть больше 0", "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

        };

        table.setModel(tModel);

        //table.setColumnSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);

        for (int i = 0; i < table.getColumnCount(); i++) {
            tcol = table.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new CustomColumnCellRenderer());
        }

        tf = new JTextField();
        tf.setBorder(BorderFactory.createEmptyBorder());
        tf.setBackground(Color.RED);
        table.setDefaultEditor(Object.class, new DefaultCellEditor(tf));

        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,
                solve);
        table.getActionMap().put(solve, new EnterAction());
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
                tModel);
        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(25);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(20);

        table.getColumnModel().getColumn(9).setMaxWidth(0);
        table.getColumnModel().getColumn(9).setMinWidth(0);
        table.getColumnModel().getColumn(9).setPreferredWidth(0);

        table.getColumnModel().getColumn(10).setMaxWidth(0);
        table.getColumnModel().getColumn(10).setMinWidth(0);
        table.getColumnModel().getColumn(10).setPreferredWidth(0);

        if (result.getValutaID() == 1) {
            table.getColumnModel().getColumn(8).setMaxWidth(0);
            table.getColumnModel().getColumn(8).setMinWidth(0);
            table.getColumnModel().getColumn(8).setPreferredWidth(0);
        }

	/*	table.getColumnModel().getColumn(12).setMaxWidth(0);
		table.getColumnModel().getColumn(12).setMinWidth(0);
		table.getColumnModel().getColumn(12).setPreferredWidth(0);
	 */
        table.getColumnModel().getColumn(11).setMaxWidth(0);
        table.getColumnModel().getColumn(11).setMinWidth(0);
        table.getColumnModel().getColumn(11).setPreferredWidth(0);
    }

    /** Метод производит обновление документа новыми данными из таблицы */
    private void calculateTTN() {
        pb = new ProgressBar(controller.getMainForm(), false, "Обновление документа...");
        final SkladDB sdb = new SkladDB();
        db = new DB();

        class SWorker extends SwingWorker<Object, Object> {

            public SWorker() {

            }

            @Override
            protected Object doInBackground() throws Exception {

                try {
                    sdb.setNewPriceNakl(ndoc, rows);
                    boolean isValuta = false;
                    isValuta = result.getValutaID() > 1;
                    sdb.updateSumTTN(ndoc, isValuta);
                    setTTNInfo(db.getTTNInfo(ndoc));
                } catch (Exception er) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Ошибка при обновлении документа: "
                                    + er.getMessage(), "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    sdb.disConn();
                    tableWasChanged = false;
                    bRecalculate.setVisible(false);
                }
                return 0;
            }

            @Override
            protected void done() {
                try {
                    pb.dispose();
                } catch (Exception ex) {
                    System.err
                            .println("Ошибка при получении результатов calculateTTN() "
                                    + ex);
                }

            }
        }

        SWorker sw = new SWorker();

        sw.execute();
        pb.setVisible(true);
    }

    private void initComponents() {

        panelFoother.setPreferredSize(new Dimension(0, 200));
        panelHeader.setPreferredSize(new Dimension(0, 300));
        panelHeader.setLayout(new BorderLayout());
        // **************************************************************
        // Панель истории товара
        // ************************************************************
        tableStory = new JTable();
        sbTableStory = new JScrollPane(tableStory);
        panelMain.remove(panelHeader);
        panelMain.remove(scrollTable);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelHeader,
                scrollTable);
        Border empty = BorderFactory.createEmptyBorder();
        splitPane.setBorder(empty);

        panelInfo = new JPanel(null);
        panelInfo.setPreferredSize(new Dimension(0, 70));

        JLabel l = new JLabel("№ документа : ");
        l.setBounds(5, 5, 120, 23);
        panelInfo.add(l);

        JLabel l1 = new JLabel("Плательщик : ");
        l1.setBounds(5, 30, 120, 23);
        panelInfo.add(l1);

        lblDocNumber = new JLabel("");
        lblDocNumber.setBounds(130, 5, 150, 23);
        panelInfo.add(lblDocNumber);

        lblClient = new JLabel();
        lblClient.setBounds(130, 30, 600, 23);
        panelInfo.add(lblClient);

        lblDocNumber.setText(this.ndoc);
        lblClient.setText(client + " ( " + clientID + " )");

        panelMain.add(panelInfo, BorderLayout.NORTH);
        panelMain.add(splitPane, BorderLayout.CENTER);

        panelStoryHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelStoryHeader.setMinimumSize(new Dimension(0, 150));

        panelStoryButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelStoryButton.setPreferredSize(new Dimension(0, 35));

        try {
            formatter = new MaskFormatter("##.##.####");

        } catch (Exception e) {
            System.err.println("Ошибка задания формата даты : " + e);
        }

        formatter.setPlaceholderCharacter('0');
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.MONTH, Calendar.MONTH - 3);

        String date = sf.format(c.getTime());

/*		ftDateBegin = new javax.swing.JFormattedTextField(formatter);
		ftDateBegin.setValue(date);

		ftDateEnd = new javax.swing.JFormattedTextField(formatter);
		c = Calendar.getInstance();
		ftDateEnd.setValue(sf.format(c.getTime()));

		panelStoryHeader.add(new JLabel("Накладные за период с :"));
		panelStoryHeader.add(ftDateBegin);
		panelStoryHeader.add(new JLabel("по :"));
		panelStoryHeader.add(ftDateEnd);*/

        bSetCost = new JButton("Принять цену");
        bSetCost.setPreferredSize(Settings.BUTTON_NORMAL_SIZE);
        //bSetCost.setIcon(new ImageIcon(MainForm.progPath
        //		+ "/Img/down_arrow.png"));
        bSetCost.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableStory.getSelectedRow() != -1) {
                    try {

                    } catch (Exception ex) {

                    }

                }
            }
        });

        panelHeader.add(panelStoryHeader, BorderLayout.NORTH);
        panelHeader.add(sbTableStory, BorderLayout.CENTER);
        panelHeader.add(panelStoryButton, BorderLayout.SOUTH);

        int x = 0;
        int y = 30;
        int height = 25;
        int width = 160;

        panelTextFoother.setPreferredSize(new Dimension(0, 170));
        JLabel lblBelStoimost = new JLabel("Стоимость : ");
        lblBelStoimost.setBounds(x, y + height, 120, height);
        panelTextFoother.add(lblBelStoimost);
        JLabel lblBelSummaNDS = new JLabel("Сумма НДС : ");
        lblBelSummaNDS.setBounds(x, y + height * 2, width, height);
        panelTextFoother.add(lblBelSummaNDS);
        JLabel lblBelItogo = new JLabel("Стоимость с НДС : ");
        lblBelItogo.setBounds(x, y + height * 3, width, height);
        panelTextFoother.add(lblBelItogo);
        JLabel lblValuta = new JLabel("Валюта :");
        lblValuta.setBounds(x + 300, y, 100, height);
        panelTextFoother.add(lblValuta);
        JLabel lblValutaStoimost = new JLabel("Стоимость : ");
        lblValutaStoimost.setBounds(x + 300, y + height, width, height);
        JLabel lblValutaSummaNDS = new JLabel("Сумма НДС : ");
        lblValutaSummaNDS.setBounds(x + 300, y + height * 2, width, height);
        JLabel lblValutaItogo = new JLabel("Стоимость с НДС : ");
        lblValutaItogo.setBounds(x + 300, y + height * 3, width, height);

        x = 150;
        width = 150;

        txtBelStoimost = new JLabel("00000000.0");
        txtBelStoimost.setBounds(x, y + height, 120, height);
        panelTextFoother.add(txtBelStoimost);
        txtBelSummaNDS = new JLabel("00000000.0");
        txtBelSummaNDS.setBounds(x, y + height * 2, width, height);
        panelTextFoother.add(txtBelSummaNDS);
        txtBelItogo = new JLabel("00000000.0");
        txtBelItogo.setBounds(x, y + height * 3, width, height);
        panelTextFoother.add(txtBelItogo);
        txtValuta = new JLabel("BYR");
        txtValuta.setBounds(x + 230, y, 200, height);
        panelTextFoother.add(txtValuta);
        txtValutaStoimost = new JLabel("00000000.00");
        txtValutaStoimost.setBounds(x + 300, y + height, width, height);
        txtValutaSummaNDS = new JLabel("000000000.00");
        txtValutaSummaNDS.setBounds(x + 300, y + height * 2, width, height);
        txtValutaItogo = new JLabel("000000000.00");
        txtValutaItogo.setBounds(x + 300, y + height * 3, width, height);

        if (result.getValutaID() > 1) {
            panelTextFoother.add(lblValutaItogo);
            panelTextFoother.add(lblValutaStoimost);
            panelTextFoother.add(lblValutaSummaNDS);
            panelTextFoother.add(txtValutaItogo);
            panelTextFoother.add(txtValutaSummaNDS);
            panelTextFoother.add(txtValutaStoimost);
        }

        bRecalculate = new JButton("Расчет накладной");
        bRecalculate.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        bRecalculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateTTN();
            }
        });

        bRecalculate.setVisible(false);
        // *******************************************************************
        // Радио баттоны выбора режима работы
        // *******************************************************************
        JPanel panelMode = new JPanel(null);
        panelMode.setPreferredSize(new Dimension(210, 40));
        panelButton.setPreferredSize(new Dimension(0, 50));
        ButtonGroup groupMode = new ButtonGroup();
        modeManual = new JRadioButton("Ручной ввод");
        modeManual.setBounds(0, -1, 200, 23);
        modeManual.setSelected(true);
        modeStory = new JRadioButton("История отгрузок");
        modeStory.setBounds(0, 19, 200, 23);
        groupMode.add(modeManual);
        groupMode.add(modeStory);
        panelMode.add(modeManual);
        panelMode.add(modeStory);
        panelButton.add(cbCarret);
        panelButton.add(panelMode);


        modeManual.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setMode(!modeManual.isSelected());
            }
        });

        modeStory.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setMode(modeStory.isSelected());
            }
        });

        bProtocol = new JButton("Протокол согласования");
        bProtocol.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        bProtocol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ProtSogl(myself, true, ndoc, client);
            }
        });

        JButton bGetDocList = new JButton("Поиск в базе");
        bGetDocList.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        bGetDocList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getStoryInfo();
            }
        });

        panelStoryButton.add(bGetDocList);
        panelButton.add(bRecalculate);
        panelButton.add(bRecalculate);
        panelButton.add(bClose);
        panelHeader.setVisible(false);

        bClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableWasChanged) {
                    if (JOptionPane.showConfirmDialog(null,
                            "Данные накладной изменены, сохранить изменения перед выходом ?", "Вопрос",
                            javax.swing.JOptionPane.OK_CANCEL_OPTION) == 0) {
                        calculateTTN();
                    }
                }
            }
        });
    }

    private void setMode(boolean b) {
        panelHeader.setVisible(b);
        panelHeader.repaint();
        if (b) {
            table.setColumnSelectionAllowed(false);
            panelHeader.setPreferredSize(new Dimension(0, 150));
            splitPane.setDividerLocation(250);
            storyDB = new DB();
        } else {
            table.setColumnSelectionAllowed(true);
        }
    }

    private class EnterAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            final int column = table.getSelectedColumn();
            final int row = table.getSelectedRow();
            if (column > 8 || column < 7) {
                return;
            }

            table.editCellAt(row, column);
            JTextField o = (JTextField) table.getEditorComponent();
            o.requestFocus();
            o.selectAll();
            o.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyReleased(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {

                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        if (result.getValutaID() == 1) {

                            if (row == table.getRowCount() - 1) {
                                table.setRowSelectionInterval(row, row);
                            } else {
                                table.setRowSelectionInterval(row + 1, row + 1);
                            }
                            table.setColumnSelectionInterval(7, 7);
                        } else if (column == 7) {
                            if (row == table.getRowCount() - 1) {
                                table.setRowSelectionInterval(row, row);
                            } else {
                                table.setRowSelectionInterval(row + 1, row + 1);
                            }
                            if (cbCarret.isSelected()) {
                                table.setRowSelectionInterval(row, row);
                                table.setColumnSelectionInterval(8, 8);
                            } else {

                            }
                        } else {
                            if (row == table.getRowCount() - 1) {
                                table.setRowSelectionInterval(row, row);
                            } else {
                                table.setRowSelectionInterval(row + 1, row + 1);

                            }
                            if (cbCarret.isSelected()) {
                                table.setColumnSelectionInterval(7, 7);
                            }
                        }
                        //table.changeSelection(row, 0, false, false);
                    }
                }
            });
        }
    }

}
