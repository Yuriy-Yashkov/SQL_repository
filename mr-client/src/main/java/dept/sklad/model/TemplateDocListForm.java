package dept.sklad.model;

import by.gomel.freedev.ucframework.ucswing.iframe.BaseInternalFrame;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;
import common.CustomTableCellRenderer;
import common.ProgressBar;
import workDB.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * Базовый класс для создаваемых диалоговых форм со списком документов. Имеет
 * необходимую структуру для разделения отображаемой информации поблочно
 *
 * @author Andy
 * @version 0.1.2
 */
public class TemplateDocListForm extends BaseInternalFrame {

    /** InternalFrame без фильтра в HeaderPanel */
    protected static final int FRAME_DEFAULT = 0;
    /** InternalFrame c фильтром поиска в HeaderPanel */
    protected static final int FRAME_FILTER = 1;
    /**
     *
     */
    private static final long serialVersionUID = 1234L;
    protected JPanel panelMain;
    /** Панель верхней части формы */
    protected JPanel panelHeader;
    /** Панель нижней части формы */
    protected JPanel panelFoother;
    /** Панель для расположения кнопок в нижней части формы */
    protected JPanel panelButton;
    /** Панель с текстовой информацией в нижней части формы */
    protected JPanel panelTextFoother;
    /** Выбор экспортных документов */
    protected JCheckBox cbExport;
    /** Список допустимых признаков документа */
    protected JComboBox<String> typeNaklList;
    /** Кнопка активации фильтра документов */
    protected JButton bShow;
    /** Кнопка закрытия формы */
    protected JButton bClose;
    /** Поле ввода даты начала периода для отбора документов */
    //protected JFormattedTextField ftDateBegin;
    /** Поле ввода даты конца периода для отбора документов */
    //protected JFormattedTextField ftDateEnd;
    /** Панель прокрутки для таблицы */
    protected JScrollPane scrollTable;
    /** Таблица формы */
    protected JTable table;
    /** Поле для ввода номера документа */
    protected JTextField tfTTN;
    /** Текстовая метка с информацией о количестве товара в накладной */
    protected JLabel lFoot;
    protected ResultTTN result;
    protected UCDatePeriodPicker periodPicker;
    /** Массив колонок в таблице формы */
    @SuppressWarnings("rawtypes")
    protected Vector columns = new Vector();
    /** Коллекция строк таблицы формы */
    @SuppressWarnings("rawtypes")
    protected Vector rows = new Vector();
    /** Модель таблицы формы */
    protected DefaultTableModel tModel;
    protected JFormattedTextField ftDateBegin;
    protected JFormattedTextField ftDateEnd;
    private int x = 0;
    private MaskFormatter formatter;
    private TableRowSorter<TableModel> sorter;
    private ProgressBar pb;

    /**
     * Конструктор формы, в зависимости от входного параметра frameStyle,
     * инициализирует необходимые компоненты формы
     *
     * @param frameStyle
     *            тип окна:
     *            <p>
     *            FRAME_DEFAULT - окно без инициализации элементов управления
     *            фильтром
     *            <p>
     *            FRAME_FILTER - окно c инициализацией элементов управления
     *            фильтром, таблицей, и событиями связанными с компонентов
     */

    public TemplateDocListForm(MainController mainController, int frameStyle) {
        super(mainController);
        this.setLayout(new BorderLayout(5, 5));
		
/*		 ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI())
		  .setNorthPane(null);*/

        initFrameStructure();
        switch (frameStyle) {
            case FRAME_DEFAULT: {
                break;
            }
            case FRAME_FILTER: {
                initFilter();
                initTableModel();
                initEvents();
                //getData();
                break;
            }
        }
        add(panelMain);
        //resizeFrame();

    }

    /** Метод инициализирует события компонентов по умолчанию */
    private void initEvents() {
        tfTTN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sorter.setRowFilter(RowFilter.regexFilter(tfTTN.getText().trim(),
                            1));

                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (rows.size() > 0) {
                        sorter.setRowFilter(null);
                    }
                    tfTTN.setText("");
                }
            }
        });

/*		ftDateBegin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    getData();

                }
            }
        });

		// ftDateEnd.setText(date);
		ftDateEnd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    getData();
                }
            }
        });*/

        periodPicker.addOnChangeAction(a -> {
            getData();
        });

        bShow.addActionListener(e -> getData());
    }

    /** Метод запрашивает список накладных по условию и заполняет таблицу */
    public void getData() {
        pb = new ProgressBar(controller.getMainForm(), false, "Формируется список накладных...");

        class SWorker extends SwingWorker<Object, Object> {
            @SuppressWarnings("rawtypes")
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    DB db = new DB();

                    Date dateBegin = null;
                    Date dateEnd = null;
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd.MM.yyyy");

/*					try {
						//dateBegin = dateFormat.parse(ftDateBegin.getText());
						//dateEnd = dateFormat.parse(ftDateEnd.getText());

					} catch (Exception e) {
						System.out.println("Ошибка преобразования даты");
					}*/

                    table.setRowSorter(null);
                    rows = db.getNakl(periodPicker.getDatePickerBegin(), periodPicker.getDatePickerEnd(),
                            typeNaklList.getSelectedIndex(),
                            cbExport.isSelected());

                    table.setVisible(false);

                    for (int i = tModel.getRowCount() - 1; i > -1; i--) {
                        tModel.removeRow(i);
                    }
                    for (Object row : rows) {
                        tModel.addRow((Vector) row);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                            "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                return 0;
            }

            @Override
            protected void done() {
                try {
                    pb.dispose();
                    // table.setRowSorter(sorter);

                    if (rows.size() > 0) {
                        if (tfTTN.getText().trim().equals("")) {
                            sorter.setRowFilter(RowFilter.regexFilter(
                                    tfTTN.getText(), 1));
                        }
                        table.setRowSorter(sorter);
                    }

                    table.setVisible(true);
                } catch (Exception ex) {
                    table.setVisible(true);
                    System.err
                            .println("Ошибка при получении результатов из фонового потока "
                                    + ex);
                }

            }
        }

        SWorker sw = new SWorker();

        sw.execute();
        pb.setVisible(true);
    }

    /** Метод инициализации модели таблицы */
    @SuppressWarnings({"unchecked", "serial"})
    private void initTableModel() {

        columns.add("Дата");
        columns.add("Номер");
        columns.add("Операция");
        columns.add("Код пол.");
        columns.add("Получатель");
        columns.add("Сумма без НДС");
        columns.add("НДС");
        columns.add("Сумма с НДС");
        columns.add("Статус");
        columns.add("№ Заявки");

        // создаём таблицу
        tModel = new DefaultTableModel(rows, columns) {
            @SuppressWarnings("rawtypes")
            @Override
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
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
        tModel.addTableModelListener(e -> {

        });
        table.setModel(tModel);

        sorter = new TableRowSorter<>(tModel);
        sorter.setSortsOnUpdates(true);

        table.setRowSorter(sorter);
        for (int i = 0; i < table.getColumnCount(); i++) {
            /* Колонки таблицы */
            final TableColumn tcol = table.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new CustomTableCellRenderer());
        }

        table.getSelectionModel().addListSelectionListener(
                e -> {
                    if (table.getSelectedRow() != -1) {
                        DB db = new DB();
                        Integer count[] = db.getNaklDescr((String) table
                                .getValueAt(table.getSelectedRow(), 1));
                        lFoot.setText("Всего едениц: " + count[0]
                                + "   В упаковках: "
                                + (count[0] - count[2]) + "   Россыпью: "
                                + count[2] + "   Упаковок:" + count[1]);

                        // result = db.getTTNInfo((String) table
                        // .getValueAt(table.getSelectedRow(), 1));

                    }
                });

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
    }

    /**
     * Метод инициализирует структуру InternalFrame, формирует внешний вид окна,
     * добавляет основные блок-панели Header, Foother, ButtonPanel, и панели
     * отступов лево-право, содержимое данных панелей инициализируется другими
     * методами
     */
    private void initFrameStructure() {

        panelMain = new JPanel(new BorderLayout(5, 5));
        panelHeader = new JPanel(null);
        panelHeader.setPreferredSize(new Dimension(0, 83));

        panelFoother = new JPanel(new BorderLayout(5, 5));
        panelFoother.setPreferredSize(new Dimension(0, 60));

        JPanel panelLeft = new JPanel(null);
        JPanel panelRight = new JPanel(null);

        panelLeft.setPreferredSize(Settings.SIDE_BORDER);
        panelRight.setPreferredSize(Settings.SIDE_BORDER);
        this.add(panelLeft, BorderLayout.WEST);
        this.add(panelRight, BorderLayout.EAST);

        panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFoother.add(panelButton, BorderLayout.SOUTH);

        table = new JTable();
        scrollTable = new JScrollPane(table);

        panelTextFoother = new JPanel(null);
        panelTextFoother.setPreferredSize(new Dimension(0, 100));

        lFoot = new JLabel();
        lFoot.setBounds(x, 0, 600, 23);
        panelTextFoother.add(lFoot);

        panelFoother.add(panelTextFoother, BorderLayout.NORTH);

        bClose = new JButton("Закрыть");
        bClose.setPreferredSize(Settings.BUTTON_NORMAL_SIZE);
        bClose.addActionListener(e -> closeFrame());

        panelMain.add(panelHeader, BorderLayout.NORTH);
        panelMain.add(scrollTable, BorderLayout.CENTER);
        panelMain.add(panelFoother, BorderLayout.SOUTH);
    }

    private void closeFrame() {
        controller.closeInternalFrame(this);
    }

    /** Инициализация элементов управления фильтром документов */
    private void initFilter() {
        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');

        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yy");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        final String date = sf.format(c.getTime());

        JLabel lHead = new JLabel("Накладные : ");
        final int y = 17;
        lHead.setBounds(x, y, 100, 20);
        JLabel var = new JLabel("c");
        var.setBounds(x + 350, y, 10, 23);

        JLabel po = new JLabel("по");
        po.setBounds(x + 473, y, 20, 23);

        panelHeader.add(lHead);

        //panelHeader.add(var);
        ////panelHeader.add(po);

		/*ftDateBegin = new javax.swing.JFormattedTextField(formatter);
		ftDateBegin.setValue(date);

		ftDateEnd = new javax.swing.JFormattedTextField(formatter);
		c = Calendar.getInstance();
		ftDateEnd.setValue(sf.format(c.getTime()));

		ftDateBegin.setBounds(x + 370, y, 100, 23);
		ftDateEnd.setBounds(x + 500, y, 100, 23);*/
        //panelHeader.add(ftDateBegin);
        //panelHeader.add(ftDateEnd);

        periodPicker = new UCDatePeriodPicker();
        periodPicker.setBounds(x + 350, 10, 300, 30);
        panelHeader.add(periodPicker);


        String[] typeNakl = {"Показать все", "Отгрузка покупателю",
                "Возврат от покупателя", "Перемещение в розницу",
                "Возврат из розницы"};

        typeNaklList = new JComboBox<>(typeNakl);
        typeNaklList.setSelectedIndex(0);
        typeNaklList.setBounds(x + 120, y, 220, 23);

        typeNaklList.addActionListener(e -> getData());

        panelHeader.add(typeNaklList);

        cbExport = new JCheckBox("Экспортные ТТН");
        cbExport.setBounds(x + 370, y + 30, 200, 23);
        cbExport.setVisible(false);
        panelHeader.add(cbExport);

        bShow = new JButton("Показать");
        bShow.setBounds(x + 620, y,
                (int) Settings.BUTTON_NORMAL_SIZE.getWidth(),
                (int) Settings.BUTTON_NORMAL_SIZE.getHeight());

        panelHeader.add(bShow);

        JLabel lTTN = new JLabel("ТТН:");
        lTTN.setBounds(x, y + 35, 50, 23);

        tfTTN = new JTextField(9);
        tfTTN.setBounds(x + 120, y + 35, 150, 23);

        panelHeader.add(lTTN);
        panelHeader.add(tfTTN);

        lFoot.setText("Всего единиц: ");
    }

}
