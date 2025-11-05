package dept.sklad;

import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;
import common.CustomColumnCellRenderer;
import common.ProgressBar;
import dept.sklad.model.ResultTTN;
import dept.sklad.model.TemplateDocListForm;
import dept.tools.imgmanager.ImagePreviewForm;
import workDB.DB;
import workDB.PDB;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Vector;

/**
 * Форма показывает содержимое накладной, цену товара как в рублях так и в
 * валюте с возможностью редактирования цены за единицу продукции
 */
public class SpecificationDoc extends TemplateDocListForm {

    JButton bChange;
    JButton bProtocol;
    JButton bEdit;

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
    private DB db;
    private JPopupMenu popMenu;
    private JMenuItem previewMenu;

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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public SpecificationDoc(MainController mainController, String count, String ndoc,
                            String c) {
        super(mainController, FRAME_DEFAULT);
        setTitle("Спецификация накладной № " + ndoc);

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка инициализации модели таблицы", "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        setTTNInfo(result);

        //MyReportsModule.desktop.add(this);
        //MyReportsModule.desktop.getDesktopManager().activateFrame(this);
        //parent.setTitle(getTitle() + " : " + MyReportsModule.titleText);
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

        // создаём таблицу
        tModel = new DefaultTableModel(rows, columns) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

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

            @Override
            public void setValueAt(Object value, int row, int column) {

            }

        };
        tModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {

            }
        });

        table.setModel(tModel);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (int i = 0; i < table.getColumnCount(); i++) {
            tcol = table.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new CustomColumnCellRenderer());
        }

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

        if (result.getValutaID() == 1) {
            table.getColumnModel().getColumn(8).setMaxWidth(0);
            table.getColumnModel().getColumn(8).setMinWidth(0);
            table.getColumnModel().getColumn(8).setPreferredWidth(0);
        }

        table.getColumnModel().getColumn(11).setMaxWidth(0);
        table.getColumnModel().getColumn(11).setMinWidth(0);
        table.getColumnModel().getColumn(11).setPreferredWidth(0);
    }

    private void initComponents() {

        panelFoother.setPreferredSize(new Dimension(0, 200));
        panelHeader.setPreferredSize(new Dimension(0, 5));
        panelHeader.setLayout(new BorderLayout());

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

        // Создаём высплывающее меню
        popMenu = new JPopupMenu();
        previewMenu = new JMenuItem("Просмотр фото модели");
        popMenu.add(previewMenu);
        previewMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PDB pdb = new PDB();
                Long model = (Long) table
                        .getValueAt(table.getSelectedRow(), 0);
                new ImagePreviewForm(pdb.getThumbImageByModel(model.toString()));
                pdb.disConn();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    Point point = e.getPoint();
                    int column = table.columnAtPoint(point);
                    int row = table.rowAtPoint(point);

                    // выполняем проверку
                    if (column != -1 && row != -1) {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    popMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    Point point = e.getPoint();
                    int column = table.columnAtPoint(point);
                    int row = table.rowAtPoint(point);

                    // выполняем проверку
                    if (column != -1 && row != -1) {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    popMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    Point point = e.getPoint();
                    int column = table.columnAtPoint(point);
                    int row = table.rowAtPoint(point);

                    // выполняем проверку
                    if (column != -1 && row != -1) {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    popMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        bChange = new JButton("Изменить");
        bChange.setPreferredSize(Settings.BUTTON_NORMAL_SIZE);
        bChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DB db = new DB();
                try {
                    db.setPriceNakl(ndoc, rows);
                    JOptionPane.showMessageDialog(null, "Цены изменены ",
                            "Готово",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception er) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка при изменении цены: " + er.getMessage(),
                            "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    dispose();
                }
            }
        });

        panelButton.add(bClose);
        panelHeader.setVisible(true);

        bClose.addActionListener(e -> {
        });
    }

}
