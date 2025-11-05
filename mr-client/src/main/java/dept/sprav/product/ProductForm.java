package dept.sprav.product;

import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.ProgressBar;
import common.User;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class ProductForm extends JFrame {
    private static TreeMap menu;
    private User user;

    private JPanel osnovaPanel;
    private JPanel searchPanel;
    private JPanel searchButtPanel;
    private JPanel buttPanel;
    private JPanel headPanel;
    private JDateChooser sStDate;
    private JDateChooser eStDate;
    private JDateChooser sInsDate;
    private JDateChooser eInsDate;
    private JButton buttSearch;
    private JButton buttAdd;
    private JButton buttCopy;
    private JButton buttOpen;
    private JButton buttEdit;
    private JCheckBox checkboxSt;
    private JCheckBox checkboxIns;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;

    private Vector col;
    private Vector row;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter sorter;
    private TableFilterHeader filterHeader;
    private DefaultTableCellRenderer renderer;

    private int minSelectedRow;
    private int maxSelectedRow;
    private boolean tableModelListenerIsChanging;

    private ProgressBar pb;
    private boolean result;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;

    /**
     * Конструктор инициализирует только меню формы
     */
    public ProductForm() {
        initMenu();
    }
    /**
     * Конструктор
     *
     * @param parent
     * @param modal
     */
    public ProductForm(java.awt.Frame parent, boolean modal) {
        super("Продукция ОАО 8 Марта");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        user = User.getInstance();

        if (user.getFio() != null) {
            initMenu();
            initPropSetting();
            init();
            initData();

            buttSearch.doClick();

            this.setLocationRelativeTo(null);
            this.setVisible(true);
        } else
            JOptionPane.showMessageDialog(null,
                    "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору! ",
                    "Вход",
                    javax.swing.JOptionPane.WARNING_MESSAGE);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // End of variables declaration//GEN-END:variables

    /**
     * Инициализирует меню формы
     */
    private void initMenu() {

    }

    /**
     * Читает настройки из config-файла
     */
    private void initPropSetting() {


    }


    /**
     * Первоначальные параметры
     */
    private void initData() {

    }

    /**
     * Инициализирует параметры и компоненты формы
     */
    private void init() {
        this.setMinimumSize(new Dimension(500, 500));
        this.setPreferredSize(new Dimension(800, 500));

        // title = new JLabel("Заявки на получение EAN-кодов");
        // title.setHorizontalAlignment(JLabel.CENTER);
        //  title.setFont(new Font("serif", Font.PLAIN, 24));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        headPanel = new JPanel();
        headPanel.setLayout(new BorderLayout(1, 1));
        headPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchPanel = new JPanel();
        searchPanel.setLayout(new ParagraphLayout());

        searchButtPanel = new JPanel();
        searchButtPanel.setLayout(new GridLayout(0, 3, 5, 5));
        searchButtPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        sStDate = new JDateChooser();
        sStDate.setPreferredSize(new Dimension(200, 20));
        sStDate.setEnabled(false);

        eStDate = new JDateChooser();
        eStDate.setPreferredSize(new Dimension(200, 20));
        eStDate.setEnabled(false);

        sInsDate = new JDateChooser();
        sInsDate.setPreferredSize(new Dimension(200, 20));

        eInsDate = new JDateChooser();
        eInsDate.setPreferredSize(new Dimension(200, 20));

        buttSearch = new JButton("Поиск");
        buttSearch.setPreferredSize(new Dimension(50, 20));
        buttSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttSearchActionPerformed(evt);
            }
        });

        buttAdd = new JButton("Добавить");
        buttAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAddActionPerformed(evt);
            }
        });

        buttCopy = new JButton("Копировать");
        buttCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttCopyActionPerformed(evt);
            }
        });

        buttOpen = new JButton("Открыть");
        buttOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttOpenActionPerformed(evt);
            }
        });

        buttEdit = new JButton("Редактировать");
        buttEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //buttEditActionPerformed(evt);
            }
        });

        checkboxSt = new JCheckBox("Дата");
        checkboxSt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //checkboxStActionPerformed(evt);
            }
        });

        checkboxIns = new JCheckBox("Корректировка");
        checkboxIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //checkboxInsActionPerformed(evt);
            }
        });
        checkboxIns.setSelected(true);

        jRadioButton1 = new JRadioButton();
        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton1.setText("Все;");
        jRadioButton1.setActionCommand("");
        jRadioButton1.setSelected(true);

        jRadioButton2 = new JRadioButton();
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setText("Действующая;");
        jRadioButton2.setActionCommand("0");

        jRadioButton3 = new JRadioButton();
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setText("Формируется;");
        jRadioButton3.setActionCommand("1");

        jRadioButton4 = new JRadioButton();
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setText("Снято с произв.;");
        jRadioButton4.setActionCommand("2");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton4);

        minSelectedRow = -1;
        maxSelectedRow = -1;
        tableModelListenerIsChanging = false;

        row = new Vector();

        col = new Vector();
        col.add("");
        col.add("Код ");
        col.add("Заявка №");
        col.add("Название");
        col.add("Дата");
        col.add("Автор");
        col.add("Дата коррект.");
        col.add("Статус");
        col.add("idСтатус");

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        tModel = new DefaultTableModel();

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    switch (Integer.valueOf(table.getValueAt(row, table.getColumnCount() - 1).toString())) {
                        case -1:
                            cell.setBackground(Color.PINK);
                            break;
                        case 1:
                            cell.setBackground(Color.YELLOW);
                            break;
                        default:
                            cell.setBackground(table.getBackground());
                            break;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }
        };

        searchPanel.add(new JLabel("Статус:"));
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(jRadioButton3);
        searchPanel.add(jRadioButton4);
        searchPanel.add(checkboxSt, ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel(" с "));
        searchPanel.add(sStDate);
        searchPanel.add(new JLabel(" по "));
        searchPanel.add(eStDate);
        searchPanel.add(checkboxIns, ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel(" с "));
        searchPanel.add(sInsDate);
        searchPanel.add(new JLabel(" по "));
        searchPanel.add(eInsDate);

        searchButtPanel.add(buttSearch);

        //    headPanel.add(title, BorderLayout.NORTH);
        headPanel.add(searchPanel, BorderLayout.CENTER);
        headPanel.add(searchButtPanel, BorderLayout.SOUTH);

        buttPanel.add(buttAdd);
        buttPanel.add(buttCopy);
        buttPanel.add(buttEdit);
        buttPanel.add(new JLabel());
        buttPanel.add(buttOpen);

        osnovaPanel.add(headPanel, BorderLayout.NORTH);
        osnovaPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        new ProductDetalForm(this, true);
    }

}
