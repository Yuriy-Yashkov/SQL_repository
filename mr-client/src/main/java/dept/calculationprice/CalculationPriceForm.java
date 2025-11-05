/*
 * Форма калькуляции
 */
package dept.calculationprice;

import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.WrappedTargetException;
import common.User;
import dept.MyReportsModule;
import dept.calculationprice.Dao.DbMssql;
import dept.calculationprice.editor.PriceListNameEditor;
import dept.calculationprice.forms.ProtocolTypeDialog;
import dept.calculationprice.model.CalculationBeanTableModel;
import dept.calculationprice.model.MyTableModelPriceCen;
import dept.calculationprice.model.ProtocolPreset;
import lombok.Getter;
import lombok.Setter;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author platon
 */
public final class CalculationPriceForm extends JFrame {

    private static final long serialVersionUID = 1L;
    //private static final org.apache.log4j.Logger log = new Log().getLoger(PDB_new.class);
    private static final LogCrutch log = new LogCrutch();
    public JButton btnSearchModel;
    JPopupMenu menu;
    JMenuItem rename, delete, copy, deletedCopy;
    int column;
    int row;

    @Setter
    @Getter
    private int id; // id текущего расчета

    @Setter
    @Getter
    private CalculationPriceForm self; //ссылка на текущую форму

    @Setter
    @Getter
    private TableModel model; // модель для отображения таблицы калькуляции

    @Setter
    @Getter
    private TableModel modelFactor;//модель для таблицы
    private PricePDB ppdb;// ПОдключение к базе

    @Setter
    @Getter
    private boolean enabledTypeFrom;// Активность комбика ТИП ИЗ

    @Setter
    @Getter
    private boolean enabledTypeIn;// Активность комбика ТИП В

    @Setter
    @Getter
    private boolean enabledAssortFrom;// Активность комбика Асспортимент ИЗ

    @Setter
    @Getter
    private ArrayList<MyBean> beans; // бин для таблицы

    @Setter
    @Getter
    private ArrayList<Factor> beansFactor;// бин длядля кф

    @Setter
    @Getter
    private String ptk; // Строка,  для связи группы и вида продукции

    @Setter
    @Getter
    private int group;//Группа продукции

    @Getter
    @Setter
    private int tip; // Тип продукции

    @Setter
    @Getter
    private boolean boolNew; // Новый расчет

    @Setter
    @Getter
    private int idPrice;// id  прайса

    @Getter
    @Setter
    private String namePrice; // Имя прейскуранте

    @Setter
    @Getter
    private int typeCalculation;//ТИП РАСЧЕТА 1 - цены 2 - услуги
    private String sURL; // путь к отчету
    /**
     * Переменная хранит последнюю таблицу 1-услуги и цены . 2- Поиск по модели
     * . 3-Поиск по полотну . 4 -Прайс цены . 5 -Прайс услуги
     */
    private int endTable = 1;
    private boolean search;
    //Текст для поиска
    private String searchtext, searchtextArt;
    private MainController controller;
    private CalculationBeanTableModel myTableModelSelectionArticle = null;
    private int currentRowLisCalculation;
    private int currentRowListUslugi;
    private ArrayList<MyBean> dataTableModel;
    private ArrayList<MyBean> dataTablePolotno;
    private ArrayList<MyBean> dataTableCalculations;
    private ArrayList<MyBean> dataTablePriceCen;
    private CalculationBeanTableModel modelTableModel;
    private CalculationBeanTableModel modelTablePolotno;
    private CalculationBeanTableModel modelTableCalculations;
    private MyTableModelPriceCen modelTablePriceCen;
    private JTable tableSearchModel;
    private JTable tableSearchPolotno;
    private JTable tableCalculation;
    private JTable tablePriceCen;
    private JTable tableFactor;
    private JTable tableName;
    private JTable tablePriceUslugi;
    private JTable tableUslugi;
    private TableFilterHeader filterTableListCalculation;
    private TableFilterHeader filterTablePriceCen;
    private TableFilterHeader filterTableUslugi;
    private int currentRowPriceCen;
    private JTextField tfDirectorPost;
    private JTextField tfZamPost;
    private JTextField tfDeptPost;
    private JTextField tfOvesPost;
    private JTextField tfDirectorEmployee;
    private JTextField tfZamEmployee;
    private JTextField tfDeptEmployee;
    private JTextField tfOvesEmployee;
    private UCTextField tfCurrUSD = new UCTextField();
    private UCTextField tfCurrEUR = new UCTextField();
    private double[] currency;
    private JButton btnDocPrint;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnAddAssortment;
    private JButton btnAddGroup;
    private JButton btnAddPriceCen;
    private JButton btnAddPriceUslugi;
    private JButton btnAddProd;
    private JButton btnAddType;
    private JButton btnMSSQL;
    private JButton btnNewCalcUslugi;
    private JButton btnNewCalculation;
    private JButton btnOpenCalcCen;
    private JButton btnOpenCalcUslugi;
    private JButton btnOpenSearchModel;
    private JButton btnOpenSearchPol;
    private JButton btnPrint;
    private JButton btnPrintListSogl;
    private JButton btnPrintPriceCen;
    private JButton btnPrintPricePr;
    private JButton btnPrintPriceUslugi;

    //Открытие калькуляции .   2 клика открыть. 1 клик + кнопка открыть
    private JButton btnPrintUslugi;
    private JButton btnSaveFactor;
    private JButton btnSaveName;
    private JButton btnSavePrim;
    private JButton btnSaveToFile;
    private JButton btnSearchPolorno;
    private JButton btnUpdateAssortment;
    private JButton btnUpdateKurs;
    private JTextField etAddProd;
    private JFormattedTextField etDatePriceCen;
    private JFormattedTextField etDatePriceUslugi;
    private UCTextField tfCurrRUB;
    private JTextField etModelSearch;
    private JTextField etNameGroup;
    private JTextField etNamePriceAddCen;
    private JTextField etNamePriceAddUslugi;
    private JTextField etNameType;
    private JTextField etPolSearch;
    private JComboBox<String> etSearch;
    private JTextField etSearchArt;
    private JComboBox jComboBoxAge;
    private JComboBox jComboBoxAssortmentFrom;
    private JComboBox jComboBoxGroup;
    private JComboBox jComboBoxGroupAssort;
    private JComboBox jComboBoxGroupFrom;
    private JComboBox jComboBoxGroupIn;
    private JComboBox jComboBoxNameAssort;
    private JComboBox jComboBoxTypeAssort;
    private JComboBox jComboBoxTypeFrom;
    private JComboBox jComboBoxTypeIn;
    private JComboBox jComboBoxTypeProduction;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel18;

    // egavrilovich
    private JLabel jLabel19;
    private JLabel jLabel2;
    private JLabel jLabel20;
    private JLabel jLabel21;
    private JLabel jLabel22;
    private JLabel jLabel23;
    private JLabel jLabel24;
    private JLabel jLabel25;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel10;
    private JPanel jPanel11;
    private JPanel jPanel12;
    private JPanel jPanel13;
    private JPanel jPanel14;
    private JPanel jPanel15;
    private JPanel jPanel16;
    private JPanel jPanel17;
    private JPanel jPanel18;
    private JPanel jPanel19;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JPanel jPanel9;
    private JPanel jPanel20;
    private JPanel jPanelCen;
    private JRadioButton jRadioButtonCen;
    private JRadioButton jRadioButtonUslugi;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane10;
    private JScrollPane jScrollPane11;
    private JScrollPane jScrollPane12;
    private JScrollPane jScrollPane13;
    private JScrollPane jScrollPane14;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;

    /**
     * @return the type
     */
    private JScrollPane jScrollPane4;
    private JScrollPane jScrollPane5;
    private JScrollPane jScrollPane6;
    private JScrollPane jScrollPane7;
    private JScrollPane jScrollPane8;
    private JScrollPane jScrollPane9;
    private JTabbedPane jTabbedPanelAddCen;
    private JTabbedPane jTabbedPanelCen;
    private JTabbedPane jTabbedPanelEditor;
    private JTextArea jTextArea1;
    private JTree jTreeCen;
    private JTree jTreeCenEditor;

    /**
     * @return the type
     */
    private JTree jTreeUslugi;
    private JList listPriceCen;
    private JList listPriceUslugi;
    private JTabbedPane tabSearch;
    private JTabbedPane tabbedPanelSetting;
    private JLabel tvErrodAddGroup;
    private JLabel tvErrorAddProduction;
    private JLabel tvErrorAddType;
    /**
     * Creates new form CalculationPriceForm В конструкторе подготавливается
     * работа с калькуляциями(установка необходимых значени й и создание
     * объектов)
     *
     * @param parent ссылка
     * @param modal
     */
    public CalculationPriceForm(MainController mainController, boolean modal) {
        super();
        controller = mainController;

        User user = User.getInstance();
        MyReportsModule.UserName = user.getName();

        setTypeCalculation(1);
        getInputContext().selectInputMethod(new Locale("ru", "RU"));

        setEnabledAssortFrom(true);
        setSelf(this);
        setEnabledTypeFrom(true);
        setEnabledTypeIn(true);
        this.setVisible(true);
        try {
            ppdb = new PricePDB();
            ppdb.conn();

        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setDao(ppdb);
        setBeans(new ArrayList<MyBean>());
        setBeansFactor(new ArrayList<Factor>());
        Locale loc = new Locale("ru", "RU");
        this.setLocale(loc);
        this.getInputContext().selectInputMethod(loc);
        initComponents();

        try {
            jTextArea1.setText(ppdb.getPrim());
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(jRadioButtonCen);
        btnGroup.add(jRadioButtonUslugi);
        jRadioButtonCen.setSelected(true);

        tableName.setValueAt(ppdb.getLastName()[0], 0, 0);
        tableName.setValueAt(ppdb.getLastName()[1], 0, 1);
        tableName.setValueAt(ppdb.getLastName()[2], 0, 2);
        tableName.setValueAt(ppdb.getLastName()[3], 0, 3);
        tableName.setValueAt(ppdb.getLastName()[4], 0, 4);
        tableName.setValueAt(ppdb.getLastName()[5], 0, 5);
        tableName.setValueAt(ppdb.getLastName()[6], 0, 6);
        tableName.setValueAt(ppdb.getLastName()[7], 0, 7);
        tableName.setValueAt(ppdb.getLastName()[8], 0, 8);
        tableName.setValueAt(ppdb.getLastName()[9], 0, 9);

        tfZamEmployee.setText(ppdb.getLastName()[3]);
        tfDeptEmployee.setText(ppdb.getLastName()[4]);
        tfDirectorEmployee.setText(ppdb.getLastName()[5]);

        tfZamPost.setText(ppdb.getLastName()[0]);
        tfDeptPost.setText(ppdb.getLastName()[1]);
        tfDirectorPost.setText(ppdb.getLastName()[2]);

        tfOvesPost.setText(ppdb.getLastName()[6]);
        tfOvesEmployee.setText(ppdb.getLastName()[7]);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        etDatePriceUslugi.setValue(dateFormat.format(new Date()));
        MaskFormatter formatter;
        try {
            formatter = new MaskFormatter("##.##.##");
            formatter.setValidCharacters("0123456789");
            etDatePriceUslugi.setFormatterFactory(new DefaultFormatterFactory(formatter));
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(PrintForm.class.getName()).log(Level.SEVERE, null, ex);

        }

        dateFormat = new SimpleDateFormat("dd.MM.yy");
        etDatePriceCen.setValue(dateFormat.format(new Date()));
        try {
            formatter = new MaskFormatter("##.##.##");
            formatter.setValidCharacters("0123456789");
            etDatePriceCen.setFormatterFactory(new DefaultFormatterFactory(formatter));
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(PrintForm.class.getName()).log(Level.SEVERE, null, ex);

        }

        jTreeCenEditor.addMouseListener(new MouseEventTree());
        tableCalculation.addMouseListener(new MouseEventTableCen(tableCalculation));
        tableSearchModel.addMouseListener(new MouseEventTableCen(tableSearchModel));
        tableSearchPolotno.addMouseListener(new MouseEventTableCen(tableSearchPolotno));
        tableUslugi.addMouseListener(new MouseEventTableUslugi(tableUslugi));

        currency = ppdb.getKurs();

        tfCurrRUB.setText(String.valueOf(currency[0]));
        tfCurrUSD.setText(String.valueOf(currency[1]));
        tfCurrEUR.setText(String.valueOf(currency[2]));

        tfCurrRUB.getInputContext().selectInputMethod(new Locale("ru", "RU"));

        initTree(jTreeCen);

    }

    /**
     * Установаить ширину ячеек
     *
     * @param table - исходная таблица
     */
    public static void setColumnsWidth(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader th = table.getTableHeader();
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int prefWidth
                    = Math.round(
                    (float) th.getFontMetrics(
                            th.getFont()).getStringBounds(th.getTable().getColumnName(i),
                            th.getGraphics()).getWidth());
            column.setPreferredWidth(prefWidth + 10);
        }
    }

    private static void fillCombobox(JComboBox<String> combobox) {
        ArrayList<String> array = new ArrayList<>();
        array.add("Без отбора");
        array.add("3С");
        array.add("4С");
        array.add("5С");
        array.add("6С");
        array.add("7С");

        combobox.setModel(new DefaultComboBoxModel(array.toArray()));
        combobox.setSelectedIndex(0);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String sYear = String.valueOf(year);
        // TODO: Экономисты, как Я понял хотят убрать эту сортировку. Тут находится логика, выбирающая пункт, соответствующий нынешнему году.
//        for (int i = 0; i < combobox.getItemCount(); i++) {
//            if (sYear.substring(3).equals(combobox.getItemAt(i).substring(0, 1))) {
//                combobox.setSelectedIndex(i);
//            }
//        }
    }

    private void presetCursorPosition(final int id_new) {
        int id = modelTableCalculations.getIndexById(id_new);
        if (id > -1) {
            int row_ = tableCalculation.convertRowIndexToView(id);
            tableCalculation.setRowSelectionInterval(row_, row_);
        }
        //   tableCalculation.set
    }

    private boolean isMultiDelete() {
        // ПРОверка на мультивыбор удаления или удаляется одна запись
        int rowCount = tableCalculation.getRowCount();
        int multiCount = 0;
        for (int i = 0; i < rowCount - 1; i++) {
            boolean item = (boolean) tableCalculation.getValueAt(i, 0);
            if (item) {
                return true;
            }
        }
        return false;
    }

    /**
     * Обновление дерева для цен
     */
    public void updateJtreeCenEditor() {
        initTree(jTreeCenEditor);
        initTree(jTreeCen);
    }

    /**
     * @param jTree
     */
    public void initTree(JTree jTree) {

        try {

            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            ArrayList<Group> listGroup = getDao().getGroup(getTypeCalculation());
            NodeGroup nodeGroup = null;
            NodeType nodeType = null;
            NodeAssortment nodeAssortment = null;

            for (Group group : listGroup) {
                nodeGroup = new NodeGroup(group);

                for (dept.calculationprice.Type type : getDao().getType(nodeGroup.getId())) {
                    nodeType = new NodeType(type);

                    for (Assortment assortment : getDao().getAssortment(nodeType.getIdGroup(), nodeType.getId())) {
                        nodeAssortment = new NodeAssortment(assortment);
                        if (getTypeCalculation() == 2) {
                            nodeAssortment.setUserObject(nodeAssortment.getName());
                        }
                        nodeType.add(nodeAssortment);
                    }
                    nodeGroup.add(nodeType);
                }
                root.add(nodeGroup);
            }

            jTree.setModel(new DefaultTreeModel(root));
            jTree.setRootVisible(false);

        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPanelCen = new JTabbedPane();
        jPanelCen = new JPanel();
        jScrollPane2 = new JScrollPane();
        jTreeCen = new JTree();
        jScrollPane3 = new JScrollPane();

        btnNewCalculation = new JButton();
        btnPrint = new JButton();
        btnOpenCalcCen = new JButton();
        btnPrintListSogl = new JButton();
        etSearch = new JComboBox<String>();
        etSearchArt = new JTextField();
        jLabel24 = new JLabel();
        jLabel25 = new JLabel();
        jPanel2 = new JPanel();
        jScrollPane6 = new JScrollPane();
        jTreeUslugi = new JTree();
        jScrollPane7 = new JScrollPane();
        tableUslugi = new JTable();
        btnNewCalcUslugi = new JButton();
        btnPrintUslugi = new JButton();
        btnOpenCalcUslugi = new JButton();
        jPanel3 = new JPanel();
        tabbedPanelSetting = new JTabbedPane();
        jPanel6 = new JPanel();
        jScrollPane8 = new JScrollPane();
        tableFactor = new JTable();
        btnSaveFactor = new JButton();
        jPanel15 = new JPanel();
        jScrollPane13 = new JScrollPane();
        jTextArea1 = new JTextArea();
        btnSavePrim = new JButton();
        jPanel16 = new JPanel();
        jScrollPane14 = new JScrollPane();
        tableName = new JTable();
        btnSaveName = new JButton();
        jPanel4 = new JPanel();
        jTabbedPanelEditor = new JTabbedPane();
        jPanel5 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTreeCenEditor = new JTree();
        jTabbedPanelAddCen = new JTabbedPane();
        jPanel7 = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jComboBoxTypeProduction = new JComboBox();
        etNameGroup = new JTextField();
        btnAddGroup = new JButton();
        tvErrodAddGroup = new JLabel();
        jPanel8 = new JPanel();
        jLabel3 = new JLabel();
        jComboBoxGroup = new JComboBox();
        jLabel4 = new JLabel();
        jComboBoxAge = new JComboBox();
        jLabel5 = new JLabel();
        etNameType = new JTextField();
        btnAddType = new JButton();
        tvErrorAddType = new JLabel();
        jPanel9 = new JPanel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jComboBoxGroupAssort = new JComboBox();
        jComboBoxTypeAssort = new JComboBox();
        jComboBoxNameAssort = new JComboBox();
        btnAddAssortment = new JButton();
        jLabel14 = new JLabel();
        etAddProd = new JTextField();
        btnAddProd = new JButton();
        tvErrorAddProduction = new JLabel();
        jPanel10 = new JPanel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        jComboBoxGroupFrom = new JComboBox();
        jComboBoxTypeFrom = new JComboBox();
        jComboBoxAssortmentFrom = new JComboBox();
        jComboBoxGroupIn = new JComboBox();
        jComboBoxTypeIn = new JComboBox();
        jLabel11 = new JLabel();
        btnUpdateAssortment = new JButton();
        jLabel12 = new JLabel();
        jLabel13 = new JLabel();
        jRadioButtonCen = new JRadioButton();
        jRadioButtonUslugi = new JRadioButton();
        jPanel1 = new JPanel();
        tabSearch = new JTabbedPane();
        jPanel11 = new JPanel();
        jLabel15 = new JLabel();
        etModelSearch = new JTextField();
        btnSearchModel = new JButton();
        jScrollPane4 = new JScrollPane();

        btnOpenSearchModel = new JButton();
        jPanel12 = new JPanel();
        jLabel16 = new JLabel();
        etPolSearch = new JTextField();
        btnSearchPolorno = new JButton();
        jScrollPane5 = new JScrollPane();
        btnOpenSearchPol = new JButton();
        jPanel13 = new JPanel();
        jLabel17 = new JLabel();
        jScrollPane9 = new JScrollPane();
        listPriceCen = new JList();
        jScrollPane10 = new JScrollPane();

        btnAddPriceCen = new JButton();
        etNamePriceAddCen = new JTextField();
        btnPrintPriceCen = new JButton();
        jLabel19 = new JLabel();
        btnSaveToFile = new JButton();
        etDatePriceCen = new JFormattedTextField();
        jLabel22 = new JLabel();
        btnPrintPricePr = new JButton();
        jPanel14 = new JPanel();
        jLabel18 = new JLabel();
        jScrollPane11 = new JScrollPane();
        listPriceUslugi = new JList();
        jScrollPane12 = new JScrollPane();
        tablePriceUslugi = new JTable();
        etNamePriceAddUslugi = new JTextField();
        btnAddPriceUslugi = new JButton();
        btnPrintPriceUslugi = new JButton();
        jLabel20 = new JLabel();
        etDatePriceUslugi = new JFormattedTextField();
        jLabel21 = new JLabel();
        jPanel17 = new JPanel();
        jLabel23 = new JLabel();
        tfCurrRUB = new UCTextField();
        btnUpdateKurs = new JButton();
        jPanel18 = new JPanel();
        jPanel19 = new JPanel();
        btnMSSQL = new JButton();
        jPanel20 = new JPanel();

        dataTableModel = new ArrayList<>();
        dataTablePolotno = new ArrayList<>();
        dataTableCalculations = new ArrayList<>();
        dataTablePriceCen = new ArrayList<>();

        modelTableModel = new CalculationBeanTableModel(dataTableModel);
        modelTablePolotno = new CalculationBeanTableModel(dataTablePolotno);
        modelTableCalculations = new CalculationBeanTableModel(dataTableCalculations);
        modelTablePriceCen = new MyTableModelPriceCen(dataTablePriceCen);

        tableSearchModel = new JTable(modelTableModel);
        tableSearchPolotno = new JTable(modelTablePolotno);
        tableCalculation = new JTable(modelTableCalculations);
        tablePriceCen = new JTable(modelTablePriceCen);

        filterTableListCalculation = new TableFilterHeader(tableCalculation, AutoChoices.ENABLED);
        filterTablePriceCen = new TableFilterHeader(tablePriceCen, AutoChoices.ENABLED);
        filterTableUslugi = new TableFilterHeader(tableUslugi, AutoChoices.ENABLED);

        tableCalculation.getColumnModel().getColumn(11).setPreferredWidth(0);
        tableCalculation.getColumnModel().getColumn(11).setMinWidth(0);
        tableCalculation.getColumnModel().getColumn(11).setMaxWidth(0);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jTabbedPanelCen.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jTabbedPanelCenStateChanged(evt);
            }
        });

        jPanelCen.setPreferredSize(new java.awt.Dimension(1600, 1603));

        DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("root");
        jTreeCen.setModel(new DefaultTreeModel(treeNode1));
        jTreeCen.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                jTreeCenValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jTreeCen);


        tableCalculation.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(final ListSelectionEvent event) {
                        final int viewRow = tableCalculation.getSelectedRow();
                        if (viewRow >= 0) {
                            currentRowLisCalculation = tableCalculation.convertRowIndexToModel(viewRow);
                        }
                    }
                });

        tableUslugi.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(final ListSelectionEvent event) {
                        final int viewRow = tableUslugi.getSelectedRow();
                        if (viewRow >= 0) {
                            currentRowListUslugi = tableUslugi.convertRowIndexToModel(viewRow);
                        }
                    }
                });

        tablePriceCen.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(final ListSelectionEvent event) {
                        final int viewRow = tablePriceCen.getSelectedRow();
                        if (viewRow >= 0) {
                            currentRowPriceCen = tablePriceCen.convertRowIndexToModel(viewRow);
                        }
                    }
                });

        tableCalculation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableListCalculationMouseClicked(evt);
            }
        });


        //jScrollPane3.setViewportView(tableCalculation);
        jScrollPane3.getViewport().add(tableCalculation);
        //  jScrollPane3.add(tableCalculation);

        btnNewCalculation.setText("Новый расчет");
        btnNewCalculation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCalculationActionPerformed(evt);
            }
        });

        btnPrint.setText("Печать");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        btnOpenCalcCen.setText("Открыть");
        btnOpenCalcCen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenCalcCenActionPerformed(evt);
            }
        });

        btnPrintListSogl.setText("Печать лист согласования");
        btnPrintListSogl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintListSoglActionPerformed(evt);
            }
        });

        etSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etSearchActionPerformed(evt);
            }
        });
        etSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSearchKeyPressed(evt);
            }
        });

        etSearchArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etSearchArtActionPerformed(evt);
            }
        });
        etSearchArt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSearchArtKeyPressed(evt);
            }
        });

        jLabel24.setText("Отбор по артикулу");
        jLabel24.setForeground(Color.blue);

        //jLabel25.setText("Арт");

        GroupLayout jPanelCenLayout = new GroupLayout(jPanelCen);
        jPanelCen.setLayout(jPanelCenLayout);
        jPanelCenLayout.setHorizontalGroup(
                jPanelCenLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelCenLayout.createSequentialGroup()
                                .addGroup(jPanelCenLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnPrintListSogl, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnOpenCalcCen, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPrint, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnNewCalculation, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                                        .addComponent(jScrollPane2, GroupLayout.Alignment.LEADING))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelCenLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 1105, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanelCenLayout.createSequentialGroup()
                                                .addComponent(jLabel24)
                                                .addGap(10, 10, 10)
                                                .addComponent(etSearch, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                .addGap(25, 25, 25)
                                                .addComponent(jLabel25)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(etSearchArt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 211, Short.MAX_VALUE))
        );

        jLabel24.setVisible(true);
        jLabel25.setVisible(false);
        etSearch.setVisible(true);
        etSearch.setForeground(Color.blue);
        etSearchArt.setVisible(false);

        fillCombobox(etSearch);


        jPanelCenLayout.setVerticalGroup(
                jPanelCenLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelCenLayout.createSequentialGroup()
                                .addGroup(jPanelCenLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanelCenLayout.createSequentialGroup()
                                                .addGroup(jPanelCenLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(etSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(etSearchArt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel24)
                                                        .addComponent(jLabel25))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 492, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanelCenLayout.createSequentialGroup()
                                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 351, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnNewCalculation)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnPrint)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnOpenCalcCen)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnPrintListSogl)))
                                .addGap(0, 100, Short.MAX_VALUE))
        );

        jTabbedPanelCen.addTab("Цены", jPanelCen);

        treeNode1 = new DefaultMutableTreeNode("root");
        jTreeUslugi.setModel(new DefaultTreeModel(treeNode1));
        jTreeUslugi.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                jTreeUslugiValueChanged(evt);
            }
        });
        jScrollPane6.setViewportView(jTreeUslugi);

        tableUslugi.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{

                }
        ));
        tableUslugi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableUslugiMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tableUslugi);

        btnNewCalcUslugi.setText("Новый расчет");
        btnNewCalcUslugi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCalcUslugiActionPerformed(evt);
            }
        });

        btnDocPrint = new JButton("Печать лист согласования");
        btnDocPrint.addActionListener(a -> {
            servicePrintDoc();
        });

        btnPrintUslugi.setText("Печать");
        btnPrintUslugi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintUslugiActionPerformed(evt);
            }
        });

        btnOpenCalcUslugi.setText("Открыть");
        btnOpenCalcUslugi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenCalcUslugiActionPerformed(evt);
            }
        });

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnOpenCalcUslugi, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPrintUslugi, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnNewCalcUslugi, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                                        .addComponent(btnDocPrint, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                                        .addComponent(jScrollPane6, GroupLayout.Alignment.LEADING))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane7, GroupLayout.PREFERRED_SIZE, 861, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(461, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane6, GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                                        .addComponent(jScrollPane7, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnNewCalcUslugi)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPrintUslugi)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOpenCalcUslugi)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDocPrint)
                                .addContainerGap(141, Short.MAX_VALUE))
        );

        jTabbedPanelCen.addTab("Услуги", jPanel2);

        tabbedPanelSetting.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                tabbedPanelSettingStateChanged(evt);
            }
        });

        tableFactor.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{

                }
        ));
        jScrollPane8.setViewportView(tableFactor);

        btnSaveFactor.setText("Сохранить изменения");
        btnSaveFactor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveFactorActionPerformed(evt);
            }
        });

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(364, 364, 364)
                                .addComponent(btnSaveFactor, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jScrollPane8, GroupLayout.PREFERRED_SIZE, 904, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 438, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jScrollPane8, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(btnSaveFactor)
                                .addGap(0, 209, Short.MAX_VALUE))
        );

        tabbedPanelSetting.addTab("Коэффициенты", jPanel6);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane13.setViewportView(jTextArea1);

        btnSavePrim.setText("Сохранить");
        btnSavePrim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePrimActionPerformed(evt);
            }
        });

        GroupLayout jPanel15Layout = new GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jScrollPane13, GroupLayout.PREFERRED_SIZE, 492, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 850, Short.MAX_VALUE))
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(btnSavePrim, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jScrollPane13, GroupLayout.PREFERRED_SIZE, 308, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSavePrim)
                                .addContainerGap(222, Short.MAX_VALUE))
        );

        tabbedPanelSetting.addTab("Печать прейскуранта", jPanel15);
        /*Вкладка с фамилиями в настройках-------------------------------------------*/

        tableName.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Зам. директора по экономике", "Начальник ПЭО", "Директор ", "Должность 1", "Должность 2", "Должность 3",
                        "Начальник ОВЭД", "Должность", "Должность ком.вопр.", "ФИО зам по ком."
                }
        ) {
            public Class getColumnClass(int columnIndex) {
                return String.class;
            }

        });
        jScrollPane14.setViewportView(tableName);

        final TableColumnModel columnModel = tableName.getColumnModel();
        for (int c = 0; c < columnModel.getColumnCount(); c++) {
            //columnModel.getColumn(c).setMaxWidth(120);
            columnModel.getColumn(c).setMinWidth(120);
            columnModel.getColumn(c).setPreferredWidth(120);
        }

        btnSaveName.setText("Сохранить");
        btnSaveName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveNameActionPerformed(evt);
            }
        });

        GroupLayout jPanel16Layout = new GroupLayout(jPanel16);
        jPanel16Layout.setAutoCreateGaps(true);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGap(41, 41, 41)
                                                .addComponent(jScrollPane14, GroupLayout.PREFERRED_SIZE, 1300, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGap(180, 180, 180)
                                                .addComponent(btnSaveName, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(849, Short.MAX_VALUE))
        );

        jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane14, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSaveName)
                                .addContainerGap(470, Short.MAX_VALUE))
        );
        tabbedPanelSetting.addTab("Фамилии", jPanel16);

/*
        GroupLayout jPanel16Layout = new GroupLayout(jPanel16);
        jPanel16Layout.setAutoCreateGaps(true);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGap(41, 41, 41)
                                                .addComponent(jScrollPane14, GroupLayout.PREFERRED_SIZE, 1300, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGap(180, 180, 180)
                                                .addComponent(btnSaveName, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(849, Short.MAX_VALUE))
        );

        jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane14, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSaveName)
                                .addContainerGap(470, Short.MAX_VALUE))
        );
        tabbedPanelSetting.addTab("Фамилии", jPanel16);
*/

        //tabbedPanelSetting.addTab("Фамилии", jScrollPane14);
        /*------------------------------------------------------------------------------------*/
        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(tabbedPanelSetting, GroupLayout.PREFERRED_SIZE, 1354, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 258, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tabbedPanelSetting)
        );

        jTabbedPanelCen.addTab("Настройки", jPanel3);

        jTabbedPanelEditor.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jTabbedPanelEditorStateChanged(evt);
            }
        });

        treeNode1 = new DefaultMutableTreeNode("root");
        jTreeCenEditor.setModel(new DefaultTreeModel(treeNode1));
        jTreeCenEditor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTreeCenEditorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTreeCenEditor);

        jTabbedPanelAddCen.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jTabbedPanelAddCenStateChanged(evt);
            }
        });

        jLabel1.setText("Тип продукции");

        jLabel2.setText("Название");

        jComboBoxTypeProduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeProductionActionPerformed(evt);
            }
        });

        btnAddGroup.setText("Добавить группу");
        btnAddGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddGroupActionPerformed(evt);
            }
        });

        tvErrodAddGroup.setForeground(new java.awt.Color(241, 11, 90));

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                .addGap(39, 39, 39)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(tvErrodAddGroup, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                                        .addComponent(btnAddGroup, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                                        .addComponent(jComboBoxTypeProduction, 0, 242, Short.MAX_VALUE)
                                        .addComponent(etNameGroup))
                                .addContainerGap(125, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jComboBoxTypeProduction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(etNameGroup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnAddGroup)
                                .addGap(71, 71, 71)
                                .addComponent(tvErrodAddGroup, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(112, Short.MAX_VALUE))
        );

        jTabbedPanelAddCen.addTab("Добавить группу", jPanel7);

        jLabel3.setText("Группа");

        jComboBoxGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGroupActionPerformed(evt);
            }
        });

        jLabel4.setText("Возраст");

        jComboBoxAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAgeActionPerformed(evt);
            }
        });

        jLabel5.setText("Название");

        btnAddType.setText("Добавить вид");
        btnAddType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTypeActionPerformed(evt);
            }
        });

        tvErrorAddType.setForeground(new java.awt.Color(243, 6, 32));

        GroupLayout jPanel8Layout = new GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(tvErrorAddType, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                                        .addComponent(btnAddType, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                                        .addComponent(jComboBoxGroup, 0, 228, Short.MAX_VALUE)
                                        .addComponent(jComboBoxAge, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(etNameType))
                                .addContainerGap(161, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jComboBoxGroup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jComboBoxAge, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(etNameType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addComponent(btnAddType)
                                .addGap(28, 28, 28)
                                .addComponent(tvErrorAddType, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPanelAddCen.addTab("Добавить вид", jPanel8);

        jLabel6.setText("Группа");

        jLabel7.setText("Вид");

        jLabel8.setText("Название");

        jComboBoxGroupAssort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGroupAssortActionPerformed(evt);
            }
        });

        jComboBoxTypeAssort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeAssortActionPerformed(evt);
            }
        });

        jComboBoxNameAssort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNameAssortActionPerformed(evt);
            }
        });

        btnAddAssortment.setText("Добавить ассортимент");
        btnAddAssortment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAssortmentActionPerformed(evt);
            }
        });

        jLabel14.setText("Добавить продукцию");

        btnAddProd.setText("Добавить");
        btnAddProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProdActionPerformed(evt);
            }
        });

        GroupLayout jPanel9Layout = new GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel14))
                                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel9Layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(btnAddAssortment, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jComboBoxGroupAssort, 0, 298, Short.MAX_VALUE)
                                                        .addComponent(jComboBoxTypeAssort, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jComboBoxNameAssort, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(jPanel9Layout.createSequentialGroup()
                                                .addGap(41, 41, 41)
                                                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(tvErrorAddProduction, GroupLayout.PREFERRED_SIZE, 261, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(etAddProd, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(39, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAddProd, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
                                .addGap(127, 127, 127))
        );
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(jComboBoxGroupAssort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jComboBoxTypeAssort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(jComboBoxNameAssort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnAddAssortment)
                                .addGap(34, 34, 34)
                                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel14)
                                        .addComponent(etAddProd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddProd)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tvErrorAddProduction, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTabbedPanelAddCen.addTab("Добавить ассортимент", jPanel9);

        jLabel9.setText("Из");

        jLabel10.setText("В");

        jComboBoxGroupFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGroupFromActionPerformed(evt);
            }
        });

        jComboBoxTypeFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeFromActionPerformed(evt);
            }
        });

        jComboBoxGroupIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGroupInActionPerformed(evt);
            }
        });

        jLabel11.setText("Группа");

        btnUpdateAssortment.setText("Изменить");
        btnUpdateAssortment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateAssortmentActionPerformed(evt);
            }
        });

        jLabel12.setText("Вид");

        jLabel13.setText("Ассортимент");

        GroupLayout jPanel10Layout = new GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(187, 187, 187)
                                .addComponent(jLabel9)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10)
                                .addGap(128, 128, 128))
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel12)
                                        .addComponent(jLabel11))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jComboBoxGroupFrom, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBoxTypeFrom, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBoxAssortmentFrom, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31)
                                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jComboBoxGroupIn, 0, 183, Short.MAX_VALUE)
                                        .addComponent(jComboBoxTypeIn, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(22, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnUpdateAssortment, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                                .addGap(181, 181, 181))
        );
        jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBoxGroupFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBoxGroupIn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBoxTypeFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBoxTypeIn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBoxAssortmentFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13))
                                .addGap(39, 39, 39)
                                .addComponent(btnUpdateAssortment)
                                .addContainerGap(84, Short.MAX_VALUE))
        );

        jTabbedPanelAddCen.addTab("Перенести ассортимент", jPanel10);

        jRadioButtonCen.setText("Цены");
        jRadioButtonCen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonCenActionPerformed(evt);
            }
        });

        jRadioButtonUslugi.setText("Услуги");
        jRadioButtonUslugi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonUslugiActionPerformed(evt);
            }
        });

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 279, GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(jTabbedPanelAddCen, GroupLayout.PREFERRED_SIZE, 549, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 731, Short.MAX_VALUE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jRadioButtonCen)
                                        .addComponent(jRadioButtonUslugi))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jRadioButtonCen)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonUslugi)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 414, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTabbedPanelAddCen, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24))
        );

        jTabbedPanelEditor.addTab("Редактор", jPanel5);

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPanelEditor)
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jTabbedPanelEditor, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 64, Short.MAX_VALUE))
        );

        jTabbedPanelCen.addTab("Редактор ассортимента", jPanel4);

        jLabel15.setText("Модель");

        btnSearchModel.setText("Поиск");
        btnSearchModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchModelActionPerformed(evt);
            }
        });

        tableSearchModel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSearchModelMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tableSearchModel);

        btnOpenSearchModel.setText("Открыть");
        btnOpenSearchModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenSearchModelActionPerformed(evt);
            }
        });

        GroupLayout jPanel11Layout = new GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addComponent(jLabel15, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(etModelSearch, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnSearchModel, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnOpenSearchModel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 859, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(124, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel11Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(etModelSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearchModel)
                                        .addComponent(btnOpenSearchModel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 422, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(102, Short.MAX_VALUE))
        );

        tabSearch.addTab("По модели", jPanel11);

        jLabel16.setText("Полотно");

        etModelSearch.addActionListener(a -> {
            btnSearchModel.doClick();
        });

        etPolSearch.addActionListener(a -> {
            btnSearchPolorno.doClick();
        });

        btnSearchPolorno.setText("Поиск");
        btnSearchPolorno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchPolotnoActionPerformed(evt);
            }
        });

        tableSearchPolotno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSearchPolMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tableSearchPolotno);

        btnOpenSearchPol.setText("Открыть");
        btnOpenSearchPol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenSearchPolActionPerformed(evt);
            }
        });

        GroupLayout jPanel12Layout = new GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
                jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addComponent(jLabel16, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(etPolSearch, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnSearchPolorno, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnOpenSearchPol, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 860, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(123, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
                jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(etPolSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16)
                                        .addComponent(btnSearchPolorno)
                                        .addComponent(btnOpenSearchPol))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 422, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(102, Short.MAX_VALUE))
        );

        tabSearch.addTab("По полотну", jPanel12);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(tabSearch, GroupLayout.PREFERRED_SIZE, 995, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 617, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(tabSearch, GroupLayout.PREFERRED_SIZE, 614, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPanelCen.addTab("Поиск", jPanel1);

        jLabel17.setText("Список прейскурантов");

        //  listPriceCen.setMaximumSize(new java.awt.Dimension(190, 50));
        //  listPriceCen.setMinimumSize(new java.awt.Dimension(190, 50));
        //  listPriceCen.setPreferredSize(new java.awt.Dimension(190, 50));
        listPriceCen.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                listPriceCenValueChanged(evt);
            }
        });
        jScrollPane9.setViewportView(listPriceCen);
        listPriceCen.setVisibleRowCount(16);

        tablePriceCen.setPreferredSize(null);
        tablePriceCen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePriceCenMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tablePriceCen);

        btnAddPriceCen.setText("Добавить прейскурант");
        btnAddPriceCen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPriceCenActionPerformed(evt);
            }
        });

        etNamePriceAddCen.setToolTipText("");
        JPopupMenu popupAdditional = new JPopupMenu();

        btnPrintPriceCen.setText("Печать");
/*        btnPrintPriceCen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {

            }
        });*/

        btnPrintPriceCen.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupAdditional.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });


        JMenuItem newPriceMenu = new JMenuItem("Новые цены                                    ");
        newPriceMenu.setSize(300, 25);
        JMenuItem oldPriceMenu = new JMenuItem("Старые цены                                   ");

        JMenuItem newFormat = new JMenuItem("Новый формат прейскуранта                     ");


        newPriceMenu.addActionListener(e -> {
            printPriceList(1);
        });

        oldPriceMenu.addActionListener(e -> {
            printPriceList(0);
        });

        newFormat.addActionListener(e -> {
            printPriceList(2);
        });

        popupAdditional.add(newPriceMenu);
        popupAdditional.add(oldPriceMenu);
        popupAdditional.add(newFormat);


        jLabel19.setText("Дата");

        btnSaveToFile.setText("Сохранить в файл");
        btnSaveToFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveToFileActionPerformed(evt);
            }
        });

        jLabel22.setText("Название");

        btnPrintPricePr.setText("Печать пряжа");
        btnPrintPricePr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintPricePrActionPerformed(evt);
            }
        });

        GroupLayout jPanel13Layout = new GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
                jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addComponent(jScrollPane9, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane10, GroupLayout.PREFERRED_SIZE, 883, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel17, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(btnSaveToFile, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnAddPriceCen, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                                                .addGroup(GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                                                        .addComponent(jLabel19)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(etDatePriceCen, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addComponent(jLabel22, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(etNamePriceAddCen, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(btnPrintPricePr, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnPrintPriceCen, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)))
                                .addContainerGap(511, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
                jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel17, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane10, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(jScrollPane9, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNamePriceAddCen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel22))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddPriceCen)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(etDatePriceCen, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel19))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSaveToFile)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPrintPriceCen)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPrintPricePr)
                                .addContainerGap(122, Short.MAX_VALUE))
        );

        // Скрою контролы фиг знает эту компановку НетБинса
        jLabel22.setVisible(false);
        etNamePriceAddCen.setVisible(false);

        jTabbedPanelCen.addTab("Прейскурант Цены", jPanel13);

        jLabel18.setText("Список прейскурантов");
        jLabel18.setMaximumSize(new java.awt.Dimension(155, 19));
        jLabel18.setMinimumSize(new java.awt.Dimension(155, 19));

        jScrollPane9.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane9.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listPriceUslugi.setMaximumSize(new java.awt.Dimension(190, 50));
        listPriceUslugi.setMinimumSize(new java.awt.Dimension(190, 50));
        listPriceUslugi.setPreferredSize(new java.awt.Dimension(190, 50));
        listPriceUslugi.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                listPriceUslugiValueChanged(evt);
            }
        });
        jScrollPane11.setViewportView(listPriceUslugi);

        tablePriceUslugi.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{

                }
        ));
        tablePriceUslugi.setPreferredSize(null);
        tablePriceUslugi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePriceUslugiMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tablePriceUslugi);

        btnAddPriceUslugi.setText("Добавить прейскурант");
        btnAddPriceUslugi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPriceUslugiActionPerformed(evt);
            }
        });

        btnPrintPriceUslugi.setText("Печать");
        btnPrintPriceUslugi.setPreferredSize(new java.awt.Dimension(10, 27));
        btnPrintPriceUslugi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintPriceUslugiActionPerformed(evt);
            }
        });

        jLabel20.setText("Дата");

        etDatePriceUslugi.setMaximumSize(new java.awt.Dimension(10, 27));
        etDatePriceUslugi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etDatePriceUslugiActionPerformed(evt);
            }
        });

        jLabel21.setText("Название");

        GroupLayout jPanel14Layout = new GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel14Layout.createSequentialGroup()
                                                .addGroup(jPanel14Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(btnAddPriceUslugi, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                                                        .addComponent(jScrollPane11, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                        .addGroup(jPanel14Layout.createSequentialGroup()
                                                                .addComponent(jLabel21, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(etNamePriceAddUslugi, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane12, GroupLayout.PREFERRED_SIZE, 879, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel18, GroupLayout.PREFERRED_SIZE, 245, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel14Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(btnPrintPriceUslugi, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                                                .addGroup(GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                                        .addComponent(jLabel20)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(etDatePriceUslugi, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(515, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel18, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel14Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane11, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane12, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel14Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNamePriceAddUslugi, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel21))
                                .addGap(4, 4, 4)
                                .addComponent(btnAddPriceUslugi)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel14Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(etDatePriceUslugi, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel20))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPrintPriceUslugi, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(160, Short.MAX_VALUE))
        );

        jTabbedPanelCen.addTab("Прейскурант Услуги", jPanel14);

        jLabel23.setText("Курс");
        jLabel23.setVisible(false);

        btnUpdateKurs.setText("Изменить");
        btnUpdateKurs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateKursActionPerformed(evt);
            }
        });

        GroupLayout jPanel20Layout = new GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
                jPanel20Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
                jPanel20Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 229, Short.MAX_VALUE)
        );

        JButton btnNull = new JButton();
        btnNull.setVisible(false);
        JButton btnNull1 = new JButton();
        btnNull1.setVisible(false);
        GroupLayout jPanel17Layout = new GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
                jPanel17Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel23)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnNull, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addGap(85, 85, 85)
                                                .addComponent(btnNull1)))
                                .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jPanel20, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(800, Short.MAX_VALUE))
        );


        jPanel17Layout.setVerticalGroup(
                jPanel17Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel17Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel23)
                                        .addComponent(btnNull, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNull1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel20, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(323, Short.MAX_VALUE))
        );

        //  jPanel20.setBackground(Color.BLACK);

        jTabbedPanelCen.addTab("Курс Рос рубль", jPanel17);
        jPanel17.add(new Button("НОвая кнопка"));

        btnMSSQL.setText("MsSqlConnect");
        btnMSSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMSSQLActionPerformed(evt);
            }
        });

        GroupLayout jPanel19Layout = new GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
                jPanel19Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGap(236, 236, 236)
                                .addComponent(btnMSSQL)
                                .addContainerGap(480, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
                jPanel19Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(btnMSSQL)
                                .addContainerGap(35, Short.MAX_VALUE))
        );

        GroupLayout jPanel18Layout = new GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
                jPanel18Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jPanel19, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(745, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
                jPanel18Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addComponent(jPanel19, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(417, Short.MAX_VALUE))
        );

        //jTabbedPanelCen.addTab("tab9", jPanel18);
        // jPanel18.setVisible(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPanelCen)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPanelCen, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 646, GroupLayout.PREFERRED_SIZE)
        );


        jPanel20.setLayout(null);
        jPanel20.setPreferredSize(new Dimension(450, 360));
        JLabel lblPost = new JLabel("Сотрудник");
        JLabel lblEmploeyee = new JLabel("Должность");

        JLabel lblDirector = new JLabel("Директор :");
        JLabel lblZam = new JLabel("Заместитель :");
        JLabel lblDept = new JLabel("Начальник :");
        JLabel lblOves = new JLabel("ОВЭД :");


        tfDirectorPost = new JTextField();
        tfZamPost = new JTextField();
        tfDeptPost = new JTextField();
        tfOvesPost = new JTextField();

        tfDirectorEmployee = new JTextField();
        tfZamEmployee = new JTextField();
        tfDeptEmployee = new JTextField();
        tfOvesEmployee = new JTextField();

        lblPost.setBounds(100, 10, 90, 20);
        lblEmploeyee.setBounds(200, 10, 90, 20);

        lblDirector.setBounds(10, 50, 100, 20);
        tfDirectorPost.setBounds(100, 50, 100, 20);
        tfDirectorEmployee.setBounds(205, 50, 200, 20);

        lblZam.setBounds(10, 80, 100, 20);
        tfZamPost.setBounds(100, 80, 100, 20);
        tfZamEmployee.setBounds(205, 80, 200, 20);

        lblDept.setBounds(10, 110, 100, 20);
        tfDeptPost.setBounds(100, 110, 100, 20);
        tfDeptEmployee.setBounds(205, 110, 200, 20);

        lblOves.setBounds(10, 140, 100, 20);
        tfOvesPost.setBounds(100, 140, 100, 20);
        tfOvesEmployee.setBounds(205, 140, 200, 20);

        jPanel20.add(lblPost);
        jPanel20.add(lblEmploeyee);

        jPanel20.add(lblDirector);
        jPanel20.add(lblZam);
        jPanel20.add(lblDept);

        jPanel20.add(lblOves);
        jPanel20.add(tfOvesPost);
        jPanel20.add(tfOvesEmployee);

        jPanel20.add(tfDirectorPost);
        jPanel20.add(tfDirectorEmployee);

        jPanel20.add(tfZamPost);
        jPanel20.add(tfZamEmployee);

        jPanel20.add(tfDeptPost);
        jPanel20.add(tfDeptEmployee);

        JLabel lblCurrLabel = new JLabel("Курсы валют");
        JLabel lblCurrRUB = new JLabel("Росс. рубль:");
        JLabel lblCurrUSD = new JLabel("Доллар США:");
        JLabel lblCurrEUR = new JLabel("Евро :");

        lblCurrLabel.setForeground(Color.BLUE);

        tfCurrRUB.setComponentParams(null, Float.class, 6);
        tfCurrUSD.setComponentParams(null, Float.class, 4);
        tfCurrEUR.setComponentParams(null, Float.class, 4);

        lblCurrLabel.setBounds(10, 200, 100, 20);
        lblCurrRUB.setBounds(10, 230, 100, 20);
        tfCurrRUB.setBounds(110, 230, 100, 20);
        lblCurrUSD.setBounds(10, 260, 100, 20);
        tfCurrUSD.setBounds(110, 260, 100, 20);
        lblCurrEUR.setBounds(10, 290, 100, 20);
        tfCurrEUR.setBounds(110, 290, 100, 20);

        jPanel20.add(lblCurrLabel);
        jPanel20.add(lblCurrRUB);
        jPanel20.add(tfCurrRUB);

        btnUpdateKurs.setBounds(110, 320, 100, 30);
        jPanel20.add(btnUpdateKurs);

        jPanel20.add(lblCurrUSD);
        jPanel20.add(tfCurrUSD);

        jPanel20.add(lblCurrEUR);
        jPanel20.add(tfCurrEUR);

        // tfOvesPost.setBounds(100, 140, 100, 20);


        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void servicePrintDoc() {
        /*        if (SaveForPrint.listSoglId.size() < 3) {
            SaveForPrint.listSoglId.add(getId());
        }*/


        //Отобразить форму выбора протокола согласования
        ProtocolTypeDialog dialog = new ProtocolTypeDialog(controller, this);
        ProtocolPreset preset = new ProtocolPreset();
        double[] currency = ppdb.getKurs();

        preset.setCurrencyRateSet(currency);
        ProtocolPreset resultPreset = dialog.showDialog(preset);

        if (resultPreset != null) {
            if (SaveForPrint.listSoglId.size() == 0) {
                SaveForPrint.listSoglId.add(getId());
            }
            //
            PrintListSogl printListSogl = new PrintListSogl(SaveForPrint.listSoglId);
            try {
                printListSogl.printListAdvanced(resultPreset);
            } catch (NoSuchElementException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (com.sun.star.lang.IndexOutOfBoundsException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        SaveForPrint.listSoglId = new LinkedList<Integer>();
    }

    /**
     * Добавление группы
     */
    public void addGroup() {
        String name;
        int idTypeProduction;
        tvErrodAddGroup.setText("");

        idTypeProduction = ((ComboBoxTypeProduction) jComboBoxTypeProduction.getModel()).getId();

        if (!etNameGroup.getText().equals("")) {
            name = etNameGroup.getText();
            try {
                getDao().addGroup(idTypeProduction, name, getTypeCalculation());
                initTree(jTreeCenEditor);
                initTree(jTreeCen);
            } catch (Exception e) {
                log.error("Ошибка при входе в систему: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            tvErrodAddGroup.setText("Ошибка!Введите название");
        }

    }

    /**
     * Добавление типа продукции
     */
    public void addType() {
        int idgroup, ageType;
        String name;
        tvErrorAddType.setText("");
        idgroup = ((ComboBoxGroup) jComboBoxGroup.getModel()).getId();
        ageType = jComboBoxAge.getSelectedIndex();
        if (!etNameType.getText().equals("")) {
            name = etNameType.getText();
            try {
                getDao().addType(idgroup, name, 1);
                initTree(jTreeCenEditor);
                initTree(jTreeCen);
            } catch (Exception e) {
                log.error("Ошибка при входе в систему: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            tvErrorAddType.setText("Ошибка!Введите название");
        }
    }

    /**
     * Добавление ассортимента
     */
    public void addAssortment() {
        int idGroup, idType;
        String ptk;
        idGroup = ((ComboBoxGroup) jComboBoxGroupAssort.getModel()).getId();
        idType = ((ComboBoxType) jComboBoxTypeAssort.getModel()).getId();
        ptk = ((ComboBoxAssortment) jComboBoxNameAssort.getModel()).getPtk();
        try {
            getDao().addAssortment(idGroup, idType, ptk);
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        initTree(jTreeCenEditor);
        initTree(jTreeCen);
    }

    /**
     * Обновление ассортимента в редакторе
     */
    public void updateAssortment() {

        int idGroupIn, idTypeIn;
        String ptkTemp;
        ptkTemp = ((ComboBoxAssortment) jComboBoxAssortmentFrom.getModel()).getPtk();
        idGroupIn = ((ComboBoxGroup) jComboBoxGroupIn.getModel()).getId();
        idTypeIn = ((ComboBoxType) jComboBoxTypeIn.getModel()).getId();

        try {
            getDao().updateAssortment(idGroupIn, idTypeIn, ptkTemp);
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        initTree(jTreeCenEditor);
        initTree(jTreeCen);

    }

    //Вкладки
    private void jTabbedPanelCenStateChanged(ChangeEvent evt) {//GEN-FIRST:event_jTabbedPanelCenStateChanged
        switch (jTabbedPanelCen.getSelectedIndex()) {
            //Цены
            case 0:
                setTypeCalculation(1);
                // initTree(jTreeCen);
                setTypeCalculation(1);
                break;
            //Услуги
            case 1:
                setTypeCalculation(2);
                initTree(jTreeUslugi);

                break;
            case 2:
                break;
            //Прейск ЦЕНЫ
            case 5:
                UpdateListCen();
                break;
            //Прейскурант услуги
            case 6:
                UpdateListUslugi();

                break;
        }
    }//GEN-LAST:event_jTabbedPanelCenStateChanged

    private void jTableListCalculationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableListCalculationMouseClicked

        if (evt.getClickCount() == 2) {
            Point point = evt.getPoint();
            column = tableCalculation.columnAtPoint(point);
            row = tableCalculation.rowAtPoint(point);
            //  tableCalculation.setColumnSelectionInterval(column, column);
            // tableCalculation.setRowSelectionInterval(row, row);
            setId(Integer.valueOf(tableCalculation.getModel().getValueAt(currentRowLisCalculation, 11).toString()));
            new CalculationOfPriceForm(controller, this, 1).loadFieldData(getId());
            System.out.println(getId());
        }
        if (evt.getClickCount() == 1) {

            Point point = evt.getPoint();
            column = tableCalculation.columnAtPoint(point);
            row = tableCalculation.rowAtPoint(point);
            //  tableCalculation.setColumnSelectionInterval(column, column);
            //  tableCalculation.setRowSelectionInterval(row, row);
            setId(Integer.valueOf(tableCalculation.getModel().getValueAt(currentRowLisCalculation, 11).toString()));
            System.out.println(getId());
        }
    }//GEN-LAST:event_jTableListCalculationMouseClicked

    //Выбор ассортимента в дереве Цены
    private void jTreeCenValueChanged(TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeCenValueChanged
        setBoolNew(false);
        DefaultMutableTreeNode selectedNode;
        NodeAssortment nodeAssortment = null;
        selectedNode = (DefaultMutableTreeNode) jTreeCen.getLastSelectedPathComponent();
        if (selectedNode instanceof NodeAssortment) {

            setPtk(((NodeAssortment) selectedNode).getPtk());

            NodeAssortment assortment = (NodeAssortment) selectedNode;
            //System.out.println(assortment.getId() + "/" + assortment.getIdGroup() + "/" + assortment.getIdType() + "/" + assortment.getName() + "/");

            //System.out.println("PTK=" + getPtk());
            refreshTable(getPtk());
            endTable = 1;
            setBoolNew(true);
        }
    }//GEN-LAST:event_jTreeCenValueChanged

    //Открытие новой калькуляции
    private void btnNewCalculationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCalculationActionPerformed
        if (boolNew) {
            new CalculationOfPriceForm(controller, getSelf(), endTable).newCalculation(getPtk());
        }
    }//GEN-LAST:event_btnNewCalculationActionPerformed

    /**
     * Открытие калькуляй во вкладке Поиск ( по модели)
     *
     * @param evt
     */
    private void tableSearchModelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSearchModelMouseClicked
        if (evt.getClickCount() == 2) {
            Point point = evt.getPoint();
            column = tableSearchModel.columnAtPoint(point);
            row = tableSearchModel.rowAtPoint(point);
            tableSearchModel.setColumnSelectionInterval(column, column);
            tableSearchModel.setRowSelectionInterval(row, row);
            setId(Integer.valueOf(tableSearchModel.getModel().getValueAt(row, 11).toString()));
            new CalculationOfPriceForm(controller, getSelf(), endTable).loadFieldData(getId());
        }

    }//GEN-LAST:event_tableSearchModelMouseClicked

    //Открытие калькуляции после поиска по модели
    private void btnSearchModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchModelActionPerformed
        if (!"".equals(etModelSearch.getText())) {
            updateArrayList(dataTableModel, getDao().getSearchModel(etModelSearch.getText().trim()));
            // modelTableModel.updateModel(getDao().getSearchModel(etModelSearch.getText().trim()));
            setColumnsWidth(tableSearchModel);
            tableSearchModel.getColumnModel().getColumn(0).setPreferredWidth(0);
            tableSearchModel.getColumnModel().getColumn(0).setMinWidth(0);
            tableSearchModel.getColumnModel().getColumn(0).setMaxWidth(0);
            tableSearchModel.getColumnModel().getColumn(11).setPreferredWidth(0);
            tableSearchModel.getColumnModel().getColumn(11).setMinWidth(0);
            tableSearchModel.getColumnModel().getColumn(11).setMaxWidth(0);
            endTable = 2;
            modelTableModel.fireTableDataChanged();
        }
    }//GEN-LAST:event_btnSearchModelActionPerformed

    //Открытие калькуляции двойным кликом и выделение во вкладке Поиск(полотно)
    private void tableSearchPolMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSearchPolMouseClicked
        if (evt.getClickCount() == 2) {
            Point point = evt.getPoint();
            column = tableSearchPolotno.columnAtPoint(point);
            row = tableSearchPolotno.rowAtPoint(point);
            tableSearchPolotno.setColumnSelectionInterval(column, column);
            tableSearchPolotno.setRowSelectionInterval(row, row);
            setId(Integer.valueOf(tableSearchPolotno.getModel().getValueAt(row, 11).toString()));
            new CalculationOfPriceForm(controller, getSelf(), endTable).loadFieldData(getId());
        }
    }

    //Открытие после выделения
    private void btnSearchPolotnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        if (!"".equals(etPolSearch.getText())) {

            updateArrayList(dataTablePolotno, getDao().getSearchPol(etPolSearch.getText().trim()));
            //modelTablePolotno.updateModel(getDao().getSearchPol(etPolSearch.getText().trim()));


            setColumnsWidth(tableSearchPolotno);
            tableSearchPolotno.getColumnModel().getColumn(0).setPreferredWidth(0);
            tableSearchPolotno.getColumnModel().getColumn(0).setMinWidth(0);
            tableSearchPolotno.getColumnModel().getColumn(0).setMaxWidth(0);

            tableSearchPolotno.getColumnModel().getColumn(11).setPreferredWidth(0);
            tableSearchPolotno.getColumnModel().getColumn(11).setMinWidth(0);
            tableSearchPolotno.getColumnModel().getColumn(11).setMaxWidth(0);
            modelTablePolotno.fireTableDataChanged();

            endTable = 3;
        }
    }

    // Обработка нажатий щелчков мыши для таблицы УСЛУГИ
    private void tableUslugiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableUslugiMouseClicked
        if (evt.getClickCount() == 2) {

            setId(Integer.valueOf(tableUslugi.getModel().getValueAt(currentRowListUslugi, 7).toString()));
            new CalculationOfPriceForm(controller, getSelf(), endTable).loadFieldData(getId());
        }
        if (evt.getClickCount() == 1) {

            setId(Integer.valueOf(tableUslugi.getModel().getValueAt(currentRowListUslugi, 7).toString()));

        }
    }//GEN-LAST:event_tableUslugiMouseClicked

    private void jTabbedPanelEditorStateChanged(ChangeEvent evt) {//GEN-FIRST:event_jTabbedPanelEditorStateChanged
        switch (jTabbedPanelEditor.getSelectedIndex()) {

            case 0:
                initTree(jTreeCenEditor);
                break;
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;

        }

    }//GEN-LAST:event_jTabbedPanelEditorStateChanged

    /**
     * Вкладка Настройки. Добавление . Перенос ассортимента и прочее.
     * Работает и лучше не трогать.
     *
     * @param evt
     */
    private void jTabbedPanelAddCenStateChanged(ChangeEvent evt) {//GEN-FIRST:event_jTabbedPanelAddCenStateChanged
        //System.out.println("jTabbedPane1.stateChanged, selected=" + jTabbedPanelAddCen.getSelectedIndex());
        try {

            if (getTypeCalculation() == 1) {
                switch (jTabbedPanelAddCen.getSelectedIndex()) {

                    case 0:
                        ArrayList<TypeProduction> typeProduction;
                        typeProduction = getDao().getTypeProduction();
                        if (!typeProduction.isEmpty()) {
                            jComboBoxTypeProduction.setModel(new ComboBoxTypeProduction(typeProduction));
                        }
                        break;
                    case 1:
                        jComboBoxGroup.setModel(new ComboBoxGroup(getDao().getGroup(1)));
                        jComboBoxAge.removeAllItems();
                        jComboBoxAge.addItem("Детский");
                        jComboBoxAge.addItem("Взрослый");
                        jComboBoxAge.setSelectedIndex(0);
                        break;
                    case 2:
                        int idGroup;
                        ArrayList<dept.calculationprice.Type> typeList;
                        jComboBoxGroupAssort.setModel(new ComboBoxGroup(getDao().getGroup(1)));
                        idGroup = ((ComboBoxGroup) jComboBoxGroupAssort.getModel()).getId();
                        typeList = getDao().getType(idGroup);
                        if (!typeList.isEmpty()) {
                            jComboBoxTypeAssort.setEnabled(true);
                            jComboBoxNameAssort.setEnabled(true);
                            btnAddAssortment.setEnabled(true);
                            jComboBoxTypeAssort.setModel(new ComboBoxType(typeList));
                            jComboBoxNameAssort.setModel(new ComboBoxAssortment(getDao().getNameFree()));
                        } else {
                            jComboBoxTypeAssort.setEnabled(false);
                            jComboBoxNameAssort.setEnabled(false);
                            btnAddAssortment.setEnabled(false);
                        }

                        break;
                    case 3:

                        int idGroupFrom,
                                idTypeFrom,
                                idGroupIn,
                                idTypeIn;
                        ArrayList<dept.calculationprice.Type> typeListFrom,
                                typeListIn;
                        ArrayList<Assortment> assortmentListFrom;
                        jComboBoxGroupFrom.setModel(new ComboBoxGroup(getDao().getGroup(1)));
                        idGroupFrom = ((ComboBoxGroup) jComboBoxGroupFrom.getModel()).getId();

                        typeListFrom = getDao().getType(idGroupFrom);
                        if (!typeListFrom.isEmpty()) {
                            setEnabledTypeFrom(true);
                            jComboBoxTypeFrom.setEnabled(isEnabledTypeFrom());
                            jComboBoxTypeFrom.setModel(new ComboBoxType(typeListFrom));
                            idTypeFrom = ((ComboBoxType) jComboBoxTypeFrom.getModel()).getId();
                            assortmentListFrom = getDao().getAssortment(idGroupFrom, idTypeFrom);
                            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                            if (!assortmentListFrom.isEmpty()) {
                                setEnabledAssortFrom(true);
                                btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                                jComboBoxAssortmentFrom.setEnabled(isEnabledAssortFrom());
                                jComboBoxAssortmentFrom.setModel(new ComboBoxAssortment(assortmentListFrom));
                            } else {
                                setEnabledAssortFrom(false);
                                btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                                jComboBoxAssortmentFrom.setEnabled(isEnabledAssortFrom());

                            }
                        } else {
                            setEnabledTypeFrom(false);
                            jComboBoxTypeFrom.setEnabled(isEnabledTypeFrom());
                            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                        }

                        jComboBoxGroupIn.setModel(new ComboBoxGroup(getDao().getGroup(1)));
                        idGroupIn = ((ComboBoxGroup) jComboBoxGroupIn.getModel()).getId();
                        typeListIn = getDao().getType(idGroupIn);

                        if (!typeListIn.isEmpty()) {
                            setEnabledTypeIn(true);
                            jComboBoxTypeIn.setEnabled(isEnabledTypeIn());
                            jComboBoxTypeIn.setModel(new ComboBoxType(getDao().getType(idGroupIn)));
                            idTypeIn = ((ComboBoxType) jComboBoxTypeIn.getModel()).getId();
                            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                        } else {
                            setEnabledTypeIn(false);
                            jComboBoxTypeIn.setEnabled(isEnabledTypeIn());
                            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                        }

                        break;

                }
            } else {

                switch (jTabbedPanelAddCen.getSelectedIndex()) {

                    case 0:
                        ArrayList<TypeProduction> typeProduction;
                        typeProduction = getDao().getTypeProduction();
                        if (!typeProduction.isEmpty()) {
                            jComboBoxTypeProduction.setModel(new ComboBoxTypeProduction(typeProduction));
                        }
                        break;
                    case 1:
                        jComboBoxGroup.setModel(new ComboBoxGroup(getDao().getGroup(2)));
                        jComboBoxAge.removeAllItems();
                        jComboBoxAge.addItem("Взрослый");
                        jComboBoxAge.setSelectedIndex(0);
                        break;
                    case 2:
                        int idGroup;
                        ArrayList<dept.calculationprice.Type> typeList;
                        jComboBoxGroupAssort.setModel(new ComboBoxGroup(getDao().getGroup(2)));
                        idGroup = ((ComboBoxGroup) jComboBoxGroupAssort.getModel()).getId();
                        typeList = getDao().getType(idGroup);
                        if (!typeList.isEmpty()) {
                            jComboBoxTypeAssort.setEnabled(true);
                            jComboBoxNameAssort.setEnabled(true);
                            btnAddAssortment.setEnabled(true);
                            jComboBoxTypeAssort.setModel(new ComboBoxType(typeList));
                            jComboBoxNameAssort.setModel(new ComboBoxAssortment(getDao().getNameFree()));
                        } else {
                            jComboBoxTypeAssort.setEnabled(false);
                            jComboBoxNameAssort.setEnabled(false);
                            btnAddAssortment.setEnabled(false);
                        }

                        break;
                    case 3:

                        int idGroupFrom,
                                idTypeFrom,
                                idGroupIn,
                                idTypeIn;
                        ArrayList<dept.calculationprice.Type> typeListFrom,
                                typeListIn;
                        ArrayList<Assortment> assortmentListFrom;
                        jComboBoxGroupFrom.setModel(new ComboBoxGroup(getDao().getGroup(2)));
                        idGroupFrom = ((ComboBoxGroup) jComboBoxGroupFrom.getModel()).getId();

                        typeListFrom = getDao().getType(idGroupFrom);
                        if (!typeListFrom.isEmpty()) {
                            setEnabledTypeFrom(true);
                            jComboBoxTypeFrom.setEnabled(isEnabledTypeFrom());
                            jComboBoxTypeFrom.setModel(new ComboBoxType(typeListFrom));
                            idTypeFrom = ((ComboBoxType) jComboBoxTypeFrom.getModel()).getId();
                            assortmentListFrom = getDao().getAssortment(idGroupFrom, idTypeFrom);
                            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                            if (!assortmentListFrom.isEmpty()) {
                                setEnabledAssortFrom(true);
                                btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                                jComboBoxAssortmentFrom.setEnabled(isEnabledAssortFrom());
                                jComboBoxAssortmentFrom.setModel(new ComboBoxAssortment(assortmentListFrom));
                            } else {
                                setEnabledAssortFrom(false);
                                btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                                jComboBoxAssortmentFrom.setEnabled(isEnabledAssortFrom());

                            }
                        } else {
                            setEnabledTypeFrom(false);
                            jComboBoxTypeFrom.setEnabled(isEnabledTypeFrom());
                            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                        }

                        jComboBoxGroupIn.setModel(new ComboBoxGroup(getDao().getGroup(2)));
                        idGroupIn = ((ComboBoxGroup) jComboBoxGroupIn.getModel()).getId();
                        typeListIn = getDao().getType(idGroupIn);

                        if (!typeListIn.isEmpty()) {
                            setEnabledTypeIn(true);
                            jComboBoxTypeIn.setEnabled(isEnabledTypeIn());
                            jComboBoxTypeIn.setModel(new ComboBoxType(getDao().getType(idGroupIn)));
                            idTypeIn = ((ComboBoxType) jComboBoxTypeIn.getModel()).getId();
                            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                        } else {
                            setEnabledTypeIn(false);
                            jComboBoxTypeIn.setEnabled(isEnabledTypeIn());
                            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                        }

                        break;

                }
            }
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_jTabbedPanelAddCenStateChanged

    private void btnUpdateAssortmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateAssortmentActionPerformed
        updateAssortment();
    }//GEN-LAST:event_btnUpdateAssortmentActionPerformed

    private void jComboBoxGroupInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGroupInActionPerformed
        int idGroupIn;
        ArrayList<dept.calculationprice.Type> typeList;
        idGroupIn = ((ComboBoxGroup) jComboBoxGroupIn.getModel()).getId();
        try {
            typeList = getDao().getType(idGroupIn);
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!typeList.isEmpty()) {
            setEnabledTypeIn(true);
            jComboBoxTypeIn.setEnabled(isEnabledTypeIn());
            jComboBoxTypeIn.setModel(new ComboBoxType(typeList));
            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
        } else {
            setEnabledTypeIn(false);
            jComboBoxTypeIn.setEnabled(isEnabledTypeIn());
            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
        }
    }//GEN-LAST:event_jComboBoxGroupInActionPerformed

    private void jComboBoxTypeFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeFromActionPerformed
        int idGroupFrom, idTypeFrom;
        ArrayList<Assortment> assortmentList;
        idGroupFrom = ((ComboBoxGroup) jComboBoxGroupFrom.getModel()).getId();
        idTypeFrom = ((ComboBoxType) jComboBoxTypeFrom.getModel()).getId();
        try {
            assortmentList = getDao().getAssortment(idGroupFrom, idTypeFrom);
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!assortmentList.isEmpty()) {
            jComboBoxAssortmentFrom.setModel(new ComboBoxAssortment(assortmentList));
            setEnabledAssortFrom(true);
            jComboBoxAssortmentFrom.setEnabled(isEnabledAssortFrom());
            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
        } else {
            setEnabledAssortFrom(false);
            jComboBoxAssortmentFrom.setEnabled(isEnabledAssortFrom());
            btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
        }
    }//GEN-LAST:event_jComboBoxTypeFromActionPerformed

    private void jComboBoxGroupFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGroupFromActionPerformed
        int idGroupFrom, idTypeFrom;
        ArrayList<dept.calculationprice.Type> typeListFrom;
        ArrayList<Assortment> assortmentListFrom;
        idGroupFrom = ((ComboBoxGroup) jComboBoxGroupFrom.getModel()).getId();
        try {
            typeListFrom = getDao().getType(idGroupFrom);
            if (!typeListFrom.isEmpty()) {
                setEnabledTypeFrom(true);
                jComboBoxTypeFrom.setEnabled(isEnabledTypeFrom());
                jComboBoxTypeFrom.setModel(new ComboBoxType(typeListFrom));
                btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                idTypeFrom = ((ComboBoxType) jComboBoxTypeFrom.getModel()).getId();
                assortmentListFrom = getDao().getAssortment(idGroupFrom, idTypeFrom);

                if (!assortmentListFrom.isEmpty()) {
                    setEnabledAssortFrom(true);
                    btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                    jComboBoxAssortmentFrom.setEnabled(isEnabledAssortFrom());
                    jComboBoxAssortmentFrom.setModel(new ComboBoxAssortment(assortmentListFrom));
                } else {
                    setEnabledAssortFrom(false);
                    btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
                    jComboBoxAssortmentFrom.setEnabled(isEnabledAssortFrom());

                }
            } else {
                setEnabledTypeFrom(false);
                jComboBoxTypeFrom.setEnabled(isEnabledTypeFrom());
                jComboBoxAssortmentFrom.setEnabled(isEnabledTypeFrom());
                btnUpdateAssortment.setEnabled(isEnabledTypeFrom() && isEnabledAssortFrom() && isEnabledTypeIn());
            }
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }//GEN-LAST:event_jComboBoxGroupFromActionPerformed

    //Добавить продукцию
    private void btnAddProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProdActionPerformed
        String prod;
        prod = etAddProd.getText();
        if (!"".equals(etAddProd.getText())) {
            ppdb.addProd(etAddProd.getText());
            tvErrorAddProduction.setText("");
            jComboBoxNameAssort.setModel(new ComboBoxAssortment(getDao().getNameFree()));
        } else {
            tvErrorAddProduction.setText("Введите название продукции");
        }
    }//GEN-LAST:event_btnAddProdActionPerformed

    private void btnAddAssortmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAssortmentActionPerformed
        addAssortment();
    }//GEN-LAST:event_btnAddAssortmentActionPerformed

    private void jComboBoxNameAssortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNameAssortActionPerformed
    }//GEN-LAST:event_jComboBoxNameAssortActionPerformed

    private void jComboBoxTypeAssortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeAssortActionPerformed
    }//GEN-LAST:event_jComboBoxTypeAssortActionPerformed

    private void jComboBoxGroupAssortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGroupAssortActionPerformed
        int idGroup;
        ArrayList<dept.calculationprice.Type> typeList;
        idGroup = ((ComboBoxGroup) jComboBoxGroupAssort.getModel()).getId();
        try {
            typeList = getDao().getType(idGroup);
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!typeList.isEmpty()) {
            jComboBoxTypeAssort.setEnabled(true);
            jComboBoxNameAssort.setEnabled(true);
            btnAddAssortment.setEnabled(true);
            jComboBoxTypeAssort.setModel(new ComboBoxType(typeList));
            jComboBoxNameAssort.setModel(new ComboBoxAssortment(getDao().getNameFree()));
        } else {
            jComboBoxTypeAssort.setEnabled(false);
            jComboBoxNameAssort.setEnabled(false);
            btnAddAssortment.setEnabled(false);
        }

    }//GEN-LAST:event_jComboBoxGroupAssortActionPerformed

    private void btnAddTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTypeActionPerformed
        addType();
    }//GEN-LAST:event_btnAddTypeActionPerformed

    private void jComboBoxAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAgeActionPerformed
    }//GEN-LAST:event_jComboBoxAgeActionPerformed

    private void jComboBoxGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGroupActionPerformed
    }//GEN-LAST:event_jComboBoxGroupActionPerformed

    private void btnAddGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddGroupActionPerformed
        addGroup();
    }//GEN-LAST:event_btnAddGroupActionPerformed

    private void jComboBoxTypeProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeProductionActionPerformed
    }//GEN-LAST:event_jComboBoxTypeProductionActionPerformed

    private void jRadioButtonUslugiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonUslugiActionPerformed
        setTypeCalculation(2);
        initTree(jTreeCenEditor);
    }//GEN-LAST:event_jRadioButtonUslugiActionPerformed

    private void jRadioButtonCenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonCenActionPerformed
        setTypeCalculation(1);
        initTree(jTreeCenEditor);
    }//GEN-LAST:event_jRadioButtonCenActionPerformed

    private void jTreeUslugiValueChanged(TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeUslugiValueChanged
        setBoolNew(false);
        DefaultMutableTreeNode selectedNode;
        NodeAssortment nodeAssortment = null;
        selectedNode = (DefaultMutableTreeNode) jTreeUslugi.getLastSelectedPathComponent();
        if (selectedNode instanceof NodeAssortment) {
            setPtk(((NodeAssortment) selectedNode).getPtk());
            refreshTableUslugi(getPtk());
            endTable = 1;
            setBoolNew(true);
        }//GEN-LAST:event_jTreeUslugiValueChanged
    }

    //Открыть новую калькуляцию услуги
    private void btnNewCalcUslugiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCalcUslugiActionPerformed
        if (boolNew) {
            new CalculationOfPriceForm(controller, getSelf(), endTable).newCalculation(getPtk());
        }
    }//GEN-LAST:event_btnNewCalcUslugiActionPerformed

    private void tabbedPanelSettingStateChanged(ChangeEvent evt) {//GEN-FIRST:event_tabbedPanelSettingStateChanged
        switch (tabbedPanelSetting.getSelectedIndex()) {

            case 0:
                refreshTableFactor();

                break;
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;

        }
    }//GEN-LAST:event_tabbedPanelSettingStateChanged

    /**
     * Сохранение измененных коэффициентов
     *
     * @param evt
     */
    private void btnSaveFactorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveFactorActionPerformed

        Factor factor;
        factor = new Factor();

        for (int i = 0; i < tableFactor.getModel().getRowCount(); i++) {

            for (int j = 0; j < tableFactor.getModel().getColumnCount(); j++) {

                switch (j) {
                    case 0:
                        factor.setSmvp(Double.valueOf(tableFactor.getValueAt(i, j).toString()));
                        //System.err.println(tableFactor.getValueAt(i, j).toString());
                        break;
                    case 1:
                        factor.setSmtp(Double.valueOf(tableFactor.getValueAt(i, j).toString()));
                        break;
                    case 2:
                        factor.setVmtp(Double.valueOf(tableFactor.getValueAt(i, j).toString()));
                        break;
                    case 3:
                        factor.setPrrp(Double.valueOf(tableFactor.getValueAt(i, j).toString()));
                        break;
                    case 4:
                        factor.setHzrp(Double.valueOf(tableFactor.getValueAt(i, j).toString()));
                        break;
                    case 5:
                        factor.setKmrp(Double.valueOf(tableFactor.getValueAt(i, j).toString()));
                        factor.setId(Integer.parseInt(tableFactor.getModel().getValueAt(i, 8).toString()));
                        break;

                }

            }
            try {
                ppdb.updateFactor(factor);
            } catch (Exception e) {
                log.error("Ошибка при входе в систему: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        tableFactor.revalidate();
        refreshTableFactor();

    }//GEN-LAST:event_btnSaveFactorActionPerformed

    //Добавить прейскурант ЦЕНЫ
    private void btnAddPriceCenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPriceCenActionPerformed
        BaseEditorDialog editor = new BaseEditorDialog(controller, this,
                RecordOperationType.NEW);
        EditingPane editingPane = new PriceListNameEditor();
        editor.setEditorPane(editingPane);
        editor.setParentTitle("Прейскурант цен");
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        String monthStr = "";
        if (month < 10) {
            monthStr = "0";
        }
        String result = c.get(Calendar.YEAR) + "-" + monthStr + String.valueOf(month);

        //System.out.println("Календарь "+);
        editingPane.setSourceEntity(result);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            if (isPriceListExist((String) editingPane.getSourceEntity())) {
                Dialogs.showDuplicateDialog((String) editingPane.getSourceEntity());
            } else {
                try {
                    ppdb.addPrice((String) editingPane.getSourceEntity(), 1);
                } catch (Exception e) {
                    log.error("Ошибка при входе в систему: " + e);
                    JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                UpdateListCen();
            }
        }
     /*   if (!etNamePriceAddCen.getText().equals("")) {
            try {
                ppdb.addPrice(etNamePriceAddCen.getText(), 1);
            } catch (Exception e) {
                log.error("Ошибка при входе в систему: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            UpdateListCen();



        }*/
    }

    private boolean isPriceListExist(String name) {
        ListModel model = listPriceCen.getModel();

        for (int i = 0; i < model.getSize(); i++) {
            String o = (String) model.getElementAt(i);
            if (o.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    //Добавить прейскурант УСЛУГИ
    private void btnAddPriceUslugiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPriceUslugiActionPerformed
        if (!etNamePriceAddUslugi.getText().equals("")) {
            ppdb.addPrice(etNamePriceAddUslugi.getText(), 2);
            UpdateListUslugi();
        }
    }//GEN-LAST:event_btnAddPriceUslugiActionPerformed

    //ВЫбрать прейскурант ЦЕНЫ
    private void listPriceCenValueChanged(ListSelectionEvent evt) {//GEN-FIRST:event_listPriceCenValueChanged
        Object element = listPriceCen.getSelectedValue();
        if (element != null) {
            setIdPrice(((ListModelPrice) listPriceCen.getModel()).getIdPrice());
            setNamePrice(((ListModelPrice) listPriceCen.getModel()).getName());
            refreshTablePriceCen(((ListModelPrice) listPriceCen.getModel()).getIdPrice());
            endTable = 4;
        }

    }//GEN-LAST:event_listPriceCenValueChanged

    //Открыть калькуляцию в прейскуранте услуг
    private void tablePriceUslugiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePriceUslugiMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {

            Point point = evt.getPoint();
            column = tablePriceUslugi.columnAtPoint(point);
            row = tablePriceUslugi.rowAtPoint(point);
            tablePriceUslugi.setColumnSelectionInterval(column, column);
            tablePriceUslugi.setRowSelectionInterval(row, row);
            setId(Integer.valueOf(tablePriceUslugi.getModel().getValueAt(currentRowListUslugi, 7).toString()));
            new CalculationOfPriceForm(controller, getSelf(), endTable).loadFieldData(getId());
        }
    }//GEN-LAST:event_tablePriceUslugiMouseClicked

    //Выбрать прейскурант УСЛУГИ
    private void listPriceUslugiValueChanged(ListSelectionEvent evt) {//GEN-FIRST:event_listPriceUslugiValueChanged
        Object element = listPriceUslugi.getSelectedValue();

        if (element != null) {
            setIdPrice(((ListModelPrice) listPriceUslugi.getModel()).getIdPrice());
            setNamePrice(((ListModelPrice) listPriceUslugi.getModel()).getName());
            refreshTablePriceUslugi(((ListModelPrice) listPriceUslugi.getModel()).getIdPrice());
            btnSearchModelActionPerformed(null);
            endTable = 5;
        }
    }//GEN-LAST:event_listPriceUslugiValueChanged

    //Открыть калькуляцию в прейскуранте ЦЕНЫ
    private void tablePriceCenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePriceCenMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            endTable = 4;
/*            Point point = evt.getPoint();
            column = tablePriceCen.columnAtPoint(point);
            row = tablePriceCen.rowAtPoint(point);
            tablePriceCen.setColumnSelectionInterval(column, column);
            tablePriceCen.setRowSelectionInterval(row, row);*/
            setId(Integer.valueOf(tablePriceCen.getModel().getValueAt(currentRowPriceCen, 9).toString()));
            new CalculationOfPriceForm(controller, getSelf(), endTable).loadFieldData(getId());
        }
    }//GEN-LAST:event_tablePriceCenMouseClicked

    private String parsePriceNumber(String number) {
        System.out.println("Парсим прайс [" + number + "]");
        System.out.println();
        String s = number.substring(5, 7);
        if (s.substring(0, 1).equals("0")) {
            return s.substring(1);
        } else {
            return s;
        }
    }

    //Печать прейскуранта ЦЕНЫ
    private void printPriceList(int type) {//GEN-FIRST:event_btnPrintPriceCenActionPerformed

        JasperDesign jasperDesign = new JasperDesign();
        if (getIdPrice() != 0 & !etDatePriceCen.getText().equals("")) {

            String prefix = "";
            if (type == 1) {
                prefix = "_";
            } else if (type == 2) {
                prefix = "_wide";
            }

            try {

                java.io.File fC = new java.io.File("c:\\windows\\explorer.exe");
                if (!fC.exists()) {
                    jasperDesign = JRXmlLoader.load(MyReportsModule.progPath + "/Templates/JasperReports/CPreportPriceCen" + prefix + ".jrxml");
                } else {
                    jasperDesign = JRXmlLoader.load(System.getProperty("user.dir") + "/Templates/JasperReports/CPreportPriceCen" + prefix + ".jrxml");
                }

                System.out.println("прейскурант ID =" + getIdPrice());

                JRDesignQuery designQuery = new JRDesignQuery();

                String oldPriceSQL = "SELECT \"NIZ\", \"FAS\", \"NAR\", \"RZMN\", \"RZMK\",\n" +
                        "round(CAST(\"CNO\" as numeric),2)as \"CNO\",\n" +
                        "round(CAST(\"CNO2\" as numeric),2)as \"CNO2\",\"CNVP\" \n" +
                        "FROM \"C1_Cen\" WHERE \"numberPrice\" = " + getIdPrice() + " and status !='del' ORDER BY \"FAS\",\"RZMN\",\"RZMK\" ASC";

                String oldWidePriceSQL = "SELECT \"NIZ\", \"FAS\", \"NAR\", \"RZMN\", \"RZMK\",\n" +
                        "round(CAST(\"CNO\" as numeric),2)as \"CNO\",\n" +
                        "\"CNO\" as \"CNO_NEW\",\n" +
                        "round(CAST(\"CNO2\" as numeric),2)as \"CNO2\"," +
                        "\"CNO2\" as \"CNO2_NEW\",\n" +
                        "\"CNVP\" \n" +
                        "FROM \"C1_Cen\" WHERE \"numberPrice\" = " + getIdPrice() + " and status !='del' ORDER BY \"FAS\",\"RZMN\",\"RZMK\" ASC";

                String newWidePriceSQL = "SELECT \"NIZ\", \"FAS\", \"NAR\", \"RZMN\", \"RZMK\",\n" +
                        "round(CAST(\"CNO\" as numeric)*10000,2)as \"CNO_NEW\",\n" +
                        "\"CNO\" as \"CNO\",\n" +
                        "round(CAST(\"CNO2\" as numeric)*10000,2)as \"CNO2_NEW\"," +
                        "\"CNO2\" as \"CNO2\",\n" +
                        "\"CNVP\" \n" +
                        "FROM \"C1_Cen\" WHERE \"numberPrice\" = " + getIdPrice() + " and status !='del' ORDER BY \"FAS\",\"RZMN\",\"RZMK\" ASC";


                String newPriceSQL = "SELECT * FROM \"C1_Cen\" WHERE \"numberPrice\" =  " + getIdPrice() + " and status !='del' ORDER BY \"FAS\",\"RZMN\",\"RZMK\" ASC";

                Map<String, Object> parameters = new HashMap<String, Object>();
                if (type == 0) {
                    designQuery.setText(newPriceSQL);
                    parameters.put("date", DateUtils.getNormalDateFormat(DateUtils.getDateByStringValueSimple(etDatePriceCen.getText())));
                } else if (type == 1) {
                    designQuery.setText(oldPriceSQL);
                    parameters.put("date", DateUtils.getNormalDateFormat(DateUtils.getDateByStringValueSimple(etDatePriceCen.getText())) + "г.");
                } else if (type == 2) {
                    if (getIdPrice() > 49) {
                        designQuery.setText(newWidePriceSQL);
                    } else {
                        designQuery.setText(oldWidePriceSQL);
                    }

                    parameters.put("date", DateUtils.getNormalDateFormat(DateUtils.getDateByStringValueSimple(etDatePriceCen.getText())) + "г.");
                }

                jasperDesign.setQuery(designQuery);
                JasperReport jasperReports = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint;

                parameters.put("zam", " ");
                parameters.put("zam_post", " ");
                parameters.put("director", "С.Ю. Комков");
                //   parameters.put("director_post", "Заместитель генерального директора по общим\n вопросам управляющей организации ОАО \"БПХО\"");
                parameters.put("director_post", "Заместитель директора по экономике");

                parameters.put("number", parsePriceNumber(getNamePrice()));

                parameters.put("prim", ppdb.getPrim());
                parameters.put("dolzh1", "Начальник ПЭО");
                parameters.put("dolzh2", "Ведущий экономист по ценам");
                parameters.put("fio1", "И.П.Власова");
                parameters.put("fio2", "А.С. Симоненко"); //"В.И.Дасько"

                jasperPrint = JasperFillManager.fillReport(jasperReports, parameters, ppdb.getConn());

                JasperViewer.viewReport(jasperPrint, false);

            } catch (JRException ex) {
                Logger.getLogger(CalculationPriceForm.class.getName()).log(Level.SEVERE, null, ex);
                log.error("Ошибка при входе в систему: " + ex);
                JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnPrintPriceCenActionPerformed

    //Сохранить примечание в настройках для прейскурантов
    private void btnSavePrimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePrimActionPerformed
        ppdb.setPrim(jTextArea1.getText());
    }//GEN-LAST:event_btnSavePrimActionPerformed

    //СОхранение фамилии нач по экономике
    private void btnSaveNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveNameActionPerformed
        ppdb.updateLastName(tableName.getValueAt(0, 0).toString(), tableName.getValueAt(0, 1).toString(), tableName.getValueAt(0, 2).toString(),
                tableName.getValueAt(0, 3).toString(), tableName.getValueAt(0, 4).toString(), tableName.getValueAt(0, 5).toString(),
                tableName.getValueAt(0, 6).toString(), tableName.getValueAt(0, 7).toString());
        //tableName.getValueAt(0, 8).toString(),
        //                tableName.getValueAt(0, 9).toString()

    }//GEN-LAST:event_btnSaveNameActionPerformed

    //Печать прейскуранта Услуги
    private void btnPrintPriceUslugiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintPriceUslugiActionPerformed
        if (getIdPrice() != 0 & !etDatePriceUslugi.getText().equals("")) {

            if (getIdPrice() != 0 & !etDatePriceCen.getText().equals("")) {

                try {

                    JasperDesign jasperDesign = new JasperDesign();

                    java.io.File fC = new java.io.File("c:\\windows\\explorer.exe");
                    if (!fC.exists()) {
                        jasperDesign = JRXmlLoader.load(MyReportsModule.progPath + "/Templates/JasperReports/CPreportPriceUslugi.jrxml");
                    } else {
                        jasperDesign = JRXmlLoader.load(System.getProperty("user.dir") + "/Templates/JasperReports/CPreportPriceUslugi.jrxml");
                    }

                    JRDesignQuery designQuery = new JRDesignQuery();
                    designQuery.setText("SELECT * FROM \"C1_Cen\" WHERE \"numberPrice\" =  " + getIdPrice());
                    jasperDesign.setQuery(designQuery);
                    JasperReport jasperReports = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint;
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    String zam = ppdb.getLastName()[0];
                    String zam_post = ppdb.getLastName()[3];
                    String director = ppdb.getLastName()[2];
                    String director_post = ppdb.getLastName()[5];
//                    parameters.put("zam", zam);
//                    parameters.put("zam_post", zam_post);
                    parameters.put("director", director);
                    parameters.put("director_post", director_post);
                    parameters.put("number", getNamePrice());
                    parameters.put("empty", " - ");
                    parameters.put("date", etDatePriceUslugi.getText());

                    jasperPrint = JasperFillManager.fillReport(jasperReports, parameters, ppdb.getConn());
                    JasperViewer.viewReport(jasperPrint, false);

                } catch (JRException ex) {
                    Logger.getLogger(CalculationPriceForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception e) {
                    log.error("Ошибка при входе в систему: " + e);
                    JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
    }//GEN-LAST:event_btnPrintPriceUslugiActionPerformed

    //Сохранение в файл  для отмеченных калькуляций
    private void btnSaveToFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveToFileActionPerformed

        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < tablePriceCen.getModel().getRowCount(); i++) {

            if (Boolean.valueOf(tablePriceCen.getModel().getValueAt(i, 8).toString())) {
                list.add(Integer.valueOf(tablePriceCen.getModel().getValueAt(i, 9).toString()));
            }

        }
        String s = "";
        int[] mas = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            mas[i] = list.get(i);
            if (i == list.size() - 1) {
                s += list.get(i);
            } else {
                s += list.get(i) + ",";
            }
        }

        System.out.println(s);
        try {

            JasperDesign jasperDesign = new JasperDesign();

            java.io.File fC = new java.io.File("c:\\windows\\explorer.exe");
            if (!fC.exists()) {
                jasperDesign = JRXmlLoader.load(MyReportsModule.progPath + "/Templates/JasperReports/CPCheckCalc.jrxml");
            } else {
                jasperDesign = JRXmlLoader.load(System.getProperty("user.dir") + "/Templates/JasperReports/CPCheckCalc.jrxml");
            }

            JRDesignQuery designQuery = new JRDesignQuery();
            //System.out.println( ppdb.conn.createArrayOf("int", list.toArray(new Integer[list.size()]))  );
            Array x = ppdb.conn.createArrayOf("int", list.toArray(new Integer[list.size()]));

            String query = "SELECT  "
                    + " * "
                    + "  FROM"
                    + " \"C1_Cen\""
                    + " WHERE "
                    + " \"id\" = ANY(array[ " + s + "])";

            designQuery.setText(query);
            jasperDesign.setQuery(designQuery);
            JasperReport jasperReports = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            Map<String, Object> parameters = new HashMap<String, Object>();
            jasperPrint = JasperFillManager.fillReport(jasperReports, parameters, ppdb.getConn());
            String pathSave = null;
            JFileChooser fc = new JFileChooser();
            fc.addChoosableFileFilter(new FileNameExtensionFilter("odt", "odt"));
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {

                    FileWriter fw = new FileWriter(fc.getSelectedFile() + ".odt");
                } catch (IOException e) {
                    System.out.println("Всё погибло!");
                }
                pathSave = fc.getSelectedFile().getPath() + ".odt";
                JROdtExporter exporterPdf = new JROdtExporter();
                exporterPdf.setParameter(JRDocxExporterParameter.JASPER_PRINT, jasperPrint);
                exporterPdf.setParameter(JRDocxExporterParameter.OUTPUT_FILE_NAME, pathSave);
                exporterPdf.exportReport();
            }

        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }//GEN-LAST:event_btnSaveToFileActionPerformed

    private void jTreeCenEditorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTreeCenEditorMouseClicked

        /*
         DefaultMutableTreeNode selectedNode;
         selectedNode = (DefaultMutableTreeNode) jTreeCenEditor.getLastSelectedPathComponent();

         if (selectedNode instanceof NodeGroup) {
         System.out.println( ((NodeGroup) selectedNode).getName()+"1");

         }
         if (selectedNode instanceof NodeType) {
         System.out.println( ((NodeType) selectedNode).getName()+"2");

         }
         if (selectedNode instanceof NodeAssortment) {
         System.out.println( ((NodeAssortment) selectedNode).getName()+"3");

         }

         }
         */
    }//GEN-LAST:event_jTreeCenEditorMouseClicked

    private void etDatePriceUslugiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etDatePriceUslugiActionPerformed
    }//GEN-LAST:event_etDatePriceUslugiActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        System.out.println("Печать");
        if (SaveForPrint.listId.size() == 2) {

            new PrintForm(self, false, SaveForPrint.listId);

            SaveForPrint.listId = new LinkedList<Integer>();

        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnPrintUslugiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintUslugiActionPerformed
        if (SaveForPrint.listId.size() == 2) {
            new PrintForm(self, false, SaveForPrint.listId);

            SaveForPrint.listId = new LinkedList<Integer>();

        }
    }//GEN-LAST:event_btnPrintUslugiActionPerformed

    //Открыть калькуляцию ЦЕНЫ
    private void btnOpenCalcCenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenCalcCenActionPerformed
        new CalculationOfPriceForm(controller, getSelf(), endTable).loadFieldData(getId());
    }//GEN-LAST:event_btnOpenCalcCenActionPerformed

    //Открыть калькуляцию УСЛУГИ
    private void btnOpenCalcUslugiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenCalcUslugiActionPerformed
        new CalculationOfPriceForm(controller, getSelf(), endTable).loadFieldData(getId());
    }//GEN-LAST:event_btnOpenCalcUslugiActionPerformed

    private void btnOpenSearchPolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenSearchPolActionPerformed
        new CalculationOfPriceForm(controller, getSelf(), endTable).loadFieldData(getId());
    }//GEN-LAST:event_btnOpenSearchPolActionPerformed

    //Печать листа согласования
    private void btnPrintListSoglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintListSoglActionPerformed

/*        if (SaveForPrint.listSoglId.size() < 3) {
            SaveForPrint.listSoglId.add(getId());
        }*/


        //Отобразить форму выбора протокола согласования
        ProtocolTypeDialog dialog = new ProtocolTypeDialog(controller, this);
        ProtocolPreset preset = new ProtocolPreset();
        double[] currency = ppdb.getKurs();

        preset.setCurrencyRateSet(currency);
        ProtocolPreset resultPreset = dialog.showDialog(preset);

        if (resultPreset != null) {
            if (SaveForPrint.listSoglId.size() == 0) {
                SaveForPrint.listSoglId.add(getId());
            }
            //
            PrintListSogl printListSogl = new PrintListSogl(SaveForPrint.listSoglId);
            try {
                printListSogl.printListAdvanced(resultPreset);
            } catch (NoSuchElementException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (com.sun.star.lang.IndexOutOfBoundsException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        SaveForPrint.listSoglId = new LinkedList<Integer>();
//        }
    }//GEN-LAST:event_btnPrintListSoglActionPerformed

    //Обновление курса рубля
    private void btnUpdateKursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateKursActionPerformed
        ppdb.updateKurs(Double.valueOf(tfCurrRUB.getText().replace(",", ".").replaceAll(" ", "")),
                Double.valueOf(tfCurrUSD.getText()),
                Double.valueOf(tfCurrEUR.getText()));
    }//GEN-LAST:event_btnUpdateKursActionPerformed

    //Открыть калькуляцию ЦЕНЫ в поиске по модели
    private void btnOpenSearchModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenSearchModelActionPerformed
        new CalculationOfPriceForm(controller, getSelf(), endTable).loadFieldData(getId());
    }//GEN-LAST:event_btnOpenSearchModelActionPerformed

    //Поиск
    private void etSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSearchKeyPressed

    }//GEN-LAST:event_etSearchKeyPressed

    private void etSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etSearchActionPerformed

    }//GEN-LAST:event_etSearchActionPerformed

    private void btnMSSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMSSQLActionPerformed
        DbMssql xxx = new DbMssql();
        xxx.conn();
    }//GEN-LAST:event_btnMSSQLActionPerformed

    //Поиск по артикулу
    private void etSearchArtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSearchArtKeyPressed
    }//GEN-LAST:event_etSearchArtKeyPressed

    private void etSearchArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etSearchArtActionPerformed
    }//GEN-LAST:event_etSearchArtActionPerformed

    //Печать прейскуранта
    private void btnPrintPricePrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintPricePrActionPerformed
        JasperDesign jasperDesign = new JasperDesign();
        if (getIdPrice() != 0 & !etDatePriceCen.getText().equals("")) {

            try {

                java.io.File fC = new java.io.File("c:\\windows\\explorer.exe");
                if (!fC.exists()) {
                    jasperDesign = JRXmlLoader.load(MyReportsModule.progPath + "/Templates/JasperReports/CPreportPriceCenPr.jrxml");
                } else {
                    jasperDesign = JRXmlLoader.load(System.getProperty("user.dir") + "/Templates/JasperReports/CPreportPriceCenPr.jrxml");
                }

                JRDesignQuery designQuery = new JRDesignQuery();
                designQuery.setText("SELECT * FROM \"C1_Cen\" WHERE \"numberPrice\" =  " + getIdPrice());
                jasperDesign.setQuery(designQuery);
                JasperReport jasperReports = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint;
                Map<String, Object> parameters = new HashMap<String, Object>();
                String zam = ppdb.getLastName()[0];
                String director = ppdb.getLastName()[2];
                parameters.put("zam", zam);
                parameters.put("director", director);
                parameters.put("number", getNamePrice());
                parameters.put("date", etDatePriceCen.getText());
                // parameters.put("prim", ppdb.getPrim());

                jasperPrint = JasperFillManager.fillReport(jasperReports, parameters, ppdb.getConn());
                // File pdf = File.createTempFile("/home/user/123.", ".pdf");
                // JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));
                // JRDocxExporter

                //JasperFillManager.fill
                //JasperExportManager.exportReportToPdfFile(JasperFillManager.fillReportToFile(jasperReports, "/home/user/NetBeansProjects/MyReports/TemplatesCen", parameters ,ppdb.getConn() ),
                //  "/home/user/NetBeansProjects/123.docx");
                JasperViewer.viewReport(jasperPrint, false);

            } catch (JRException ex) {
                Logger.getLogger(CalculationPriceForm.class.getName()).log(Level.SEVERE, null, ex);
                log.error("Ошибка при входе в систему: " + ex);
                JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnPrintPricePrActionPerformed

    /**
     * Обновление списка прейкурантов цены
     */
    public void UpdateListCen() {

        ArrayList<Price> list;

        list = new ArrayList<Price>();
        try {
            list = ppdb.getPriceNoInclude(1);
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        listPriceCen.setModel(new ListModelPrice(list));
        listPriceCen.setVisible(true);

    }

    /**
     * Обновление списка прейкурантов УСЛУГИ
     */
    public void UpdateListUslugi() {

        ArrayList<Price> list;

        list = new ArrayList<Price>();
        try {
            list = ppdb.getPriceNoInclude(2);
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        listPriceUslugi.setModel(new ListModelPrice(list));
        listPriceUslugi.setVisible(true);
    }

    /**
     * Обновить таблицу
     *
     * @param ptk -  идентификатор ассортимента
     */
    public void refreshTable(String ptk) {
        if (etSearch.getSelectedIndex() > 0) {
            updateArrayList(dataTableCalculations, getDao().getListArticle(ptk, etSearch.getItemAt(etSearch.getSelectedIndex())));
        } else {
            updateArrayList(dataTableCalculations, getDao().getListArticle(ptk));
        }
        setColumnsWidth(tableCalculation);

        tableCalculation.getColumnModel().getColumn(11).setPreferredWidth(0);
        tableCalculation.getColumnModel().getColumn(11).setMinWidth(0);
        tableCalculation.getColumnModel().getColumn(11).setMaxWidth(0);

        modelTableCalculations.fireTableDataChanged();
    }

    /**
     * @param ptk -  идентификатор ассортимента
     */
    public void refreshTableUslugi(String ptk) {
        tableUslugi.removeAll();
        getBeans().clear();
        setBeans(getDao().getListArticle(ptk));
        setModel(new MyTableModelSelectionUslugi(getBeans()));
        tableUslugi.setModel(getModel());
        setColumnsWidth(tableUslugi);
    }

    /**
     * Обновить таблицу с калькуляциями ЦЕНЫ по идентификатору прейскуранта
     *
     * @param id
     */
    public void refreshTablePriceCen(int id) {
        // filterTablePriceCen.setTable(null);
        updateArrayList(dataTablePriceCen, getDao().getListPriceCen(id));
        //modelTablePriceCen.updateModel(getDao().getListPriceCen(id));
        setColumnsWidth(tablePriceCen);
        // filterTablePriceCen.setTable(tablePriceCen);
        modelTablePriceCen.fireTableDataChanged();
    }

    /**
     * Обновить таблицу с калькуляциями ЦЕНЫ по идентификатору прейскуранта
     *
     * @param id
     */
    public void refreshTablePriceUslugi(int id) {
        tablePriceUslugi.removeAll();
        getBeans().clear();
        setBeans(getDao().getListPriceCen(id));
        setModel(new MyTableModelSelectionUslugi(getBeans()));
        tablePriceUslugi.setModel(getModel());
        setColumnsWidth(tablePriceUslugi);
    }

    /**
     * Обновить таблицу с коэффициентами
     */
    public void refreshTableFactor() {
        tableFactor.removeAll();
        getBeansFactor().clear();
        setBeansFactor(getDao().getFactorAll());
        setModelFactor(new MyTableModelSelectionFactor(getBeansFactor()));
        tableFactor.setModel(getModelFactor());
        setColumnsWidth(tableFactor);
    }

    /**
     * @return the ppdb
     */
    public PricePDB getDao() {
        return ppdb;
    }

    /**
     * @param dao
     */
    public void setDao(PricePDB dao) {
        this.ppdb = dao;
    }

    @Override
    public void dispose() {
        super.dispose();
        ppdb.disConn();
    }

    /**
     * Обновление последней таблицы
     *
     * @param value - значение , которое означает номер последней открытой таблицы
     */
    public void updateTableEnd(int value) {
        switch (value) {
            case 1:
                if (getTypeCalculation() == 1) {

                }

                if (getTypeCalculation() == 2) {
                    refreshTableUslugi(getPtk());
                }
                break;
            case 2:
                btnSearchModelActionPerformed(null);
                break;
            case 3:
                btnSearchPolotnoActionPerformed(null);
                break;
            case 4:
                listPriceCenValueChanged(null);
                break;
            case 5:
                listPriceUslugiValueChanged(null);
                break;
        }
    }

    /****
     * ПРавки и дополнения кода
     ****/
    private void initPriceListNameFormatter() {
        MaskFormatter formatter;
        try {
            formatter = new MaskFormatter("20##-##");
            formatter.setPlaceholderCharacter('0');
            formatter.setValidCharacters("0123456789");
            etDatePriceCen.setFormatterFactory(new DefaultFormatterFactory(formatter));
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(PrintForm.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private void updateArrayList(ArrayList<MyBean> array, ArrayList<MyBean> source) {
        array.clear();
        array.addAll(source);
        System.out.println(array.size());
    }

    /**
     * Класс для редактирования название асспортимента ( ВКЛАДКА РЕДАКТОР
     * АССОРТИМЕНТА)
     */
    class MouseEventTree extends MouseAdapter {

        String activeType, name;
        int id;

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {

                DefaultMutableTreeNode selectedNode;
                selectedNode = (DefaultMutableTreeNode) jTreeCenEditor.getLastSelectedPathComponent();

                if (selectedNode instanceof NodeGroup) {
                    activeType = "группа";
                    id = (((NodeGroup) selectedNode).getId());
                    name = (((NodeGroup) selectedNode).getName());

                }
                if (selectedNode instanceof NodeType) {
                    activeType = "тип";
                    id = (((NodeType) selectedNode).getId());
                    name = (((NodeType) selectedNode).getName());

                }
                if (selectedNode instanceof NodeAssortment) {
                    activeType = "ассортимент";
                    id = Integer.valueOf((((NodeAssortment) selectedNode).getPtk()));
                    name = (((NodeAssortment) selectedNode).getName());
                }

                TreePath path = jTreeCenEditor.getPathForLocation(e.getX(), e.getY());
                Rectangle pathBounds = jTreeCenEditor.getUI().getPathBounds(jTreeCenEditor, path);
                if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
                    menu = new JPopupMenu();
                    rename = new JMenuItem("Изменить");
                    delete = new JMenuItem("Удалить");
                    menu.add(rename);

                    if (activeType.equals("ассортимент")) {
                        menu.add(delete);
                    }

                    menu.show(jTreeCenEditor, e.getX(), pathBounds.y + pathBounds.height);

                    rename.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new DialogRename(activeType, id, getSelf(), name);
                        }
                    });

                    delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String stringPtk = null;
                            DefaultMutableTreeNode selectedNode;
                            NodeAssortment nodeAssortment = null;
                            selectedNode = (DefaultMutableTreeNode) jTreeCenEditor.getLastSelectedPathComponent();
                            if (selectedNode instanceof NodeAssortment) {
                                stringPtk = ((NodeAssortment) selectedNode).getPtk();
                            }

                            final ArrayList<MyBean> listArticle = getDao().getListArticle(stringPtk);

                            if (listArticle.size() > 0) {
                                JOptionPane.showMessageDialog(null, "В указанном ассортименте существуют записи.\nУдаление возможно только для \"пустых\" видов ассортимента!");
                            } else {
                                new DialogDelete(activeType, id, getSelf(), name);
                            }
                        }
                    });

                }
            }
        }

    }

    /**
     * Класс для обработки нажатия правой кнопки мыши на строке в таблице на
     * вкладке ЦЕНЫ Удаление логическое + дублирование.
     */
    class MouseEventTableCen extends MouseAdapter {

        JTable jtable;

        public MouseEventTableCen(JTable jtable) {
            this.jtable = jtable;
            int id;
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {

            if (SwingUtilities.isRightMouseButton(e)) {

                Point p = e.getPoint();

                // get the row index that contains that coordinate
                int rowNumber = jtable.rowAtPoint(p);

                // Get the ListSelectionModel of the JTable
                ListSelectionModel model = jtable.getSelectionModel();

                // set the selected interval of rows. Using the "rowNumber"
                // variable for the beginning and end selects only that one row.
                model.setSelectionInterval(rowNumber, rowNumber);

                menu = new JPopupMenu();
                copy = new JMenuItem("Дублировать");
                deletedCopy = new JMenuItem("Удалить");
                menu.add(copy);
                menu.add(deletedCopy);

                menu.show(jtable, (int) p.getX(), (int) p.getY());

                column = jtable.columnAtPoint(p);
                row = jtable.rowAtPoint(p);
                jtable.setColumnSelectionInterval(column, column);
                jtable.setRowSelectionInterval(row, row);


                id = Integer.valueOf(jtable.getValueAt(tableCalculation.convertRowIndexToView(currentRowLisCalculation), 11).toString());

                //Дублирование
                copy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println(id);
                        //System.out.println("Currents PTK " +ptk);
                        int id_new = getDao().newCalcalculation(ptk);
                        //System.out.println("Current ID "+id_new);

                        ValueCalculation value = getDao().getDataCalculation(id);
                        value.setNumberPrice(0);

                        //value.setNiz(value.getNiz() + "_новый") ;
                        //System.out.println(value.toString()) ;

                        getDao().updateCalculation(id_new, value);
                        refreshTable(getPtk());
                        presetCursorPosition(id_new);

                        new CalculationOfPriceForm(controller, self, endTable, true).loadFieldData(id_new);

                    }
                });

                //Удаление
                deletedCopy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //presetCursorPosition(row);

                        int row_ = tableCalculation.convertRowIndexToModel(row);

                        String str = (String) tableCalculation.getModel().getValueAt(row_, 1) + " " +
                                (String) tableCalculation.getModel().getValueAt(row_, 2);

                        final int answer = Dialogs.showDeleteDialog(str);
                        if (answer == 0) {
                            final int answer2 = Dialogs.showDeleteDialog();
                            if (answer2 == 0) {
                                if (isMultiDelete()) {
                                    // ПРОверка на мультивыбор удаления или удаляется одна запись
                                    int rowCount = tableCalculation.getRowCount();
                                    int multiCount = 0;
                                    for (int i = 0; i < rowCount; i++) {
                                        boolean item = (boolean) tableCalculation.getValueAt(i, 0);
                                        id = Integer.valueOf(tableCalculation.getValueAt(i, 11).toString());
                                        if (item) {
                                            getDao().deleteCalc(id);
                                        }
                                    }

                                } else {
                                    getDao().deleteCalc(id);
                                }
                            }
                            refreshTable(getPtk());
                        }
                    }
                });
            }
        }
    }

    /**
     * Дублирование и удаление калькуляций для УСЛУГ
     */
    class MouseEventTableUslugi extends MouseAdapter {

        JTable jtable;

        public MouseEventTableUslugi(JTable jtable) {
            this.jtable = jtable;
            int id;
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {

            if (SwingUtilities.isRightMouseButton(e)) {

                Point p = e.getPoint();

                // get the row index that contains that coordinate
                int rowNumber = jtable.rowAtPoint(p);

                // Get the ListSelectionModel of the JTable
                ListSelectionModel model = jtable.getSelectionModel();

                // set the selected interval of rows. Using the "rowNumber"
                // variable for the beginning and end selects only that one row.
                model.setSelectionInterval(rowNumber, rowNumber);

                menu = new JPopupMenu();
                copy = new JMenuItem("Дублировать");
                deletedCopy = new JMenuItem("Удалить");
                menu.add(copy);
                menu.add(deletedCopy);
                menu.show(jtable, (int) p.getX(), (int) p.getY());

                column = jtable.columnAtPoint(p);
                row = jtable.rowAtPoint(p);
                log.error(jtable.getSelectedRow() + "");
                jtable.setColumnSelectionInterval(column, column);
                jtable.setRowSelectionInterval(row, row);
                System.out.println(currentRowListUslugi);
                id = Integer.valueOf(jtable.getModel().getValueAt(currentRowListUslugi, 7).toString());
                System.out.println(id);
                copy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int id_new = getDao().newCalcalculation(ptk);
                        ValueCalculation value = getDao().getDataCalculation(id);
                        getDao().updateCalculation(id_new, value);
                        refreshTableUslugi(getPtk());
                        new CalculationOfPriceForm(controller, self, endTable).loadFieldData(id);
                    }
                });

                deletedCopy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        getDao().deleteCalc(id);
                        refreshTableUslugi(getPtk());
                    }
                });

            }

        }

    }

    /**
     * Модель для отображения таблицы с кф
     */
    public class MyTableModelSelectionFactor implements TableModel {

        private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
        private ArrayList<Factor> beans;

        /**
         * @param beans
         */
        public MyTableModelSelectionFactor(ArrayList<Factor> beans) {
            this.beans = beans;
        }

        public void addTableModelListener(TableModelListener listener) {
            listeners.add(listener);
        }

        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public int getColumnCount() {
            return 8;
        }

        /**
         * @return
         */
        public ArrayList<Factor> getList() {
            return beans;
        }

        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "Возвратные ";
                case 1:
                    return "Трансп.-заготов";
                case 2:
                    return "Трансп";
                case 3:
                    return "Общепроизвод";
                case 4:
                    return "Общехоз ";
                case 5:
                    return "Коммерческие  ";
                case 6:
                    return "Тип                                                             ";
                case 7:
                    return "Статус";
            }

            return "";
        }

        public int getRowCount() {
            return beans.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Factor bean = beans.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return bean.getSmvp();
                case 1:
                    return bean.getSmtp();
                case 2:
                    return bean.getVmtp();
                case 3:
                    return bean.getPrrp();
                case 4:
                    return bean.getHzrp();
                case 5:
                    return bean.getKmrp();
                case 6:
                    return bean.getType();
                case 7:
                    return bean.getStatus();
                case 8:
                    return bean.getId();

            }
            return "";
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            // if(columnIndex==7 || columnIndex==6){
            //     return false;
            // }
            return true;
        }

        public void removeTableModelListener(TableModelListener listener) {
            listeners.remove(listener);
        }

        @SuppressWarnings("fallthrough")
        public void setValueAt(Object value, int rowIndex, int columnIndex) {

            System.out.println(value);
            Factor bean = beans.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    bean.setSmvp(Double.parseDouble(value.toString()));
                    break;
                case 1:
                    bean.setSmtp(Double.parseDouble(value.toString()));
                    break;
                case 2:
                    bean.setVmtp(Double.parseDouble(value.toString()));
                    break;
                case 3:
                    bean.setPrrp(Double.parseDouble(value.toString()));
                    break;
                case 4:
                    bean.setHzrp(Double.parseDouble(value.toString()));
                    break;
                case 5:
                    bean.setKmrp(Double.parseDouble(value.toString()));
                    break;

            }

        }
    }

    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object obj,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            if (column == 8) {

                if (isSelected) {
                } else {
                }

                return (JCheckBox) obj;
            }
            return (Component) obj;
        }
    }

    /**
     * Модель для  калькуляций УСЛУГИ
     */
    public class MyTableModelSelectionUslugi implements TableModel {

        private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
        private List<MyBean> beans;

        /**
         * @param beans
         */
        public MyTableModelSelectionUslugi(List<MyBean> beans) {
            this.beans = beans;
        }

        public void addTableModelListener(TableModelListener listener) {
            listeners.add(listener);
        }

        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public int getColumnCount() {
            return 7;
        }

        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "Наименование изделия";
                case 1:
                    return "Название  артикула";
                case 2:
                    return "Модель";
                case 3:
                    return "Цена без НДС";
                case 4:
                    return "Ставка НДС %";
                case 5:
                    return "Цена с НДС";
                case 6:
                    return "Примечание";
            }

            return "";
        }

        public int getRowCount() {
            return beans.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            MyBean bean = beans.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return bean.getName();
                case 1:
                    return bean.getArticle();
                case 2:
                    return bean.getModel();
                case 3:
                    return bean.getCostWithoutNds();
                case 4:
                    return bean.getBetNds();
                case 5:
                    return bean.getCostWithNds();
                case 6:
                    return bean.getPrim();
                case 7:
                    return bean.getId();

            }
            return "";
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public void removeTableModelListener(TableModelListener listener) {
            listeners.remove(listener);
        }

        public void setValueAt(Object value, int rowIndex, int columnIndex) {
        }
    }

    // End of variables declaration//GEN-END:variables
}
