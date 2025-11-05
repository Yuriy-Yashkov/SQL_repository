package dept.sbit.protocol;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.UtilFunctions;
import dept.production.planning.PlanProductioForm;
import dept.production.planning.UtilPlan;
import dept.sbit.protocol.forms.ArticlePicker;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author lidashka
 */
@SuppressWarnings("all")
public class ProtocolForm extends javax.swing.JDialog {
    private final String CONFIG_KEY = "protocol.article.mask";
    private JPanel osnovaPanel;
    private JPanel buttPanel;
    private JButton buttClose;
    private Vector col;
    private JTable table;
    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;
    private TableFilterHeader filterHeader;
    private JTextField prodNumPlan;
    private JTextField prodPlan;
    private JButton buttPlan;
    private JPanel planPanel;
    private JPanel centerPanel;
    private JPanel tablePanel;
    private JButton buttEditPlus;
    private JButton buttEditMinus;
    private JButton buttEditDownload;
    private JPanel buttEastlDetalPanel;
    private JTextPane clientText;
    private JPanel clientPanel;
    private JButton buttCreate;
    private DefaultTableModel tModel;
    private TableRowSorter<TableModel> sorter;
    private Vector list;
    private ProgressBar pb;
    private ProtocolPDB pdb;
    private ProtocolDB db;
    private Vector data;
    private JCheckBox checkEan;
    private JCheckBox checkAllPlan;
    private HashMap<Object, Vector> mapFas;
    private MainController controller;
    private String articleList = "7С";

    public ProtocolForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), "Протокол по заявке покупателя из плана производства");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initPropSetting();
        init();
        initData();
        controller = mainController;
        this.setLocationRelativeTo(controller.getMainForm());
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void initPropSetting() {
        try {
            UtilProtocol.FOLDER_SELECT = UtilFunctions.readPropFileString(UtilProtocol.SETTING_FOLDER_SELECT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilProtocol.COLUMN_SELECT = UtilFunctions.readPropFileString(UtilProtocol.SETTING_COLUMN_SELECT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilProtocol.CLIENT_SELECT = UtilFunctions.readPropFileString(UtilProtocol.SETTING_CLIENT_SELECT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void init() {
        this.setMinimumSize(new Dimension(500, 500));
        this.setPreferredSize(new Dimension(600, 500));

        articleList = controller.getConfiguration().getProperty(CONFIG_KEY);
        if (articleList.equals("")) {
            articleList = "7С";
            controller.getConfiguration().setProperty(CONFIG_KEY, articleList);
        }

        buttEditPlus = new JButton("добавить");
        buttEditPlus.setPreferredSize(new Dimension(120, 25));
        buttEditPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEditPlusActionPerformed(evt);
            }
        });

        buttEditMinus = new JButton("удалить");
        buttEditMinus.setPreferredSize(new Dimension(120, 25));
        buttEditMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEditMinusActionPerformed(evt);
            }
        });

        buttEditDownload = new JButton("загрузить");
        buttEditDownload.setPreferredSize(new Dimension(120, 25));
        buttEditDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEditDownloadActionPerformed(evt);
            }
        });

        buttEastlDetalPanel = new JPanel();
        buttEastlDetalPanel.setLayout(new ParagraphLayout());
        buttEastlDetalPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttEastlDetalPanel.add(buttEditDownload, ParagraphLayout.NEW_PARAGRAPH);
        buttEastlDetalPanel.add(buttEditPlus, ParagraphLayout.NEW_PARAGRAPH);
        buttEastlDetalPanel.add(buttEditMinus, ParagraphLayout.NEW_PARAGRAPH);

        clientText = new JTextPane();
        clientText.setPreferredSize(new Dimension(450, 60));

        clientPanel = new JPanel();
        clientPanel.setLayout(new BorderLayout(1, 1));
        clientPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Заказчик(шапка документа)"));
        clientPanel.add(new JScrollPane(clientText));

        col = new Vector();
        col.add("");
        col.add("Модель");

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() ==
                        ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });
        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(1, 1));
        tablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Заявка"));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.add(buttEastlDetalPanel, BorderLayout.EAST);

        buttPlan = new JButton("Найти");
        buttPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttProdActionPerformed(evt);
            }
        });
        buttPlan.setEnabled(false);

        prodNumPlan = new JTextField();
        prodNumPlan.setPreferredSize(new Dimension(100, 20));
        prodNumPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttPlan.doClick();
            }
        });
        prodNumPlan.setEnabled(false);

        prodPlan = new JTextField();
        prodPlan.setPreferredSize(new Dimension(250, 20));
        prodPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttPlan.doClick();
            }
        });
        prodPlan.setEnabled(false);

        checkAllPlan = new JCheckBox("Все планы");
        checkAllPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAllPlanActionPerformed(evt);
            }
        });
        checkAllPlan.setSelected(true);

        planPanel = new JPanel();
        planPanel.setLayout(new ParagraphLayout());
        planPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("План производства"));
        planPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        planPanel.add(checkAllPlan);
        planPanel.add(prodNumPlan);
        planPanel.add(prodPlan);
        planPanel.add(buttPlan);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(clientPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(planPanel, BorderLayout.SOUTH);

        checkEan = new JCheckBox("только с ean13");
        checkEan.setSelected(true);

        buttCreate = new JButton("Сформировать");
        buttCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCreateActionPerformed(evt);
            }
        });

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        JPanel panelControl = new JPanel(null);
        checkEan.setBounds(0, 0, 150, 20);

        JButton btnDetail = new JButton();
        btnDetail.setBounds(150, 0, 24, 24);

        btnDetail.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/view_detail.png", "Спецификация"));

        btnDetail.addActionListener(a -> {
            ArticlePicker picker = new ArticlePicker(ProtocolForm.this, articleList);
            String result = picker.selectArticles();
            if (result != null) {
                articleList = result;
                // Сохраняем в конфиг
                controller.getConfiguration().setProperty(CONFIG_KEY, articleList);
            }
        });

        panelControl.add(checkEan);
        panelControl.add(btnDetail);

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttCreate);
        buttPanel.add(panelControl);

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnovaPanel.add(centerPanel, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void initData() {
        try {
            clientText.setText(UtilProtocol.CLIENT_SELECT);

            createTable(new Vector());
        } catch (Exception e) {
        }
    }

    private void buttEditPlusActionPerformed(ActionEvent evt) {
        try {
            JTextField text = new JTextField();

            if (JOptionPane.showOptionDialog(
                    null,
                    text,
                    "Модель",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Добавить", "Отмена"},
                    "Добавить") == JOptionPane.YES_OPTION) {

                if (!text.getText().trim().equals("")) {
                    Vector tmp = new Vector();
                    tmp.add(false);
                    tmp.add(text.getText().trim());
                    tModel.insertRow(0, tmp);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditMinusActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(null,
                    "Удалить отмеченные строки?",
                    "Удаление ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Удалить", "Отмена"},
                    "Удалить") == JOptionPane.YES_OPTION) {

                for (int i = 0; i < tModel.getDataVector().size(); i++) {
                    if (Boolean.valueOf(((Vector) tModel.getDataVector().get(i)).elementAt(0).toString())) {
                        tModel.getDataVector().remove(i);
                        i--;
                    }
                }
                createTable(tModel.getDataVector());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEditDownloadActionPerformed(ActionEvent evt) {
        try {
            list = new Vector();

            final JTextField nCol = new JTextField();
            nCol.setText(UtilProtocol.COLUMN_SELECT);

            if (JOptionPane.showOptionDialog(
                    null,
                    nCol,
                    "Колонка \"Модель\"",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"},
                    "Сохранить") == JOptionPane.YES_OPTION) {

                if (!nCol.getText().trim().equals("")) {

                    final JFileChooser fc = new JFileChooser(UtilProtocol.FOLDER_SELECT);
                    fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
                    fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            if (f != null) {
                                if (f.isDirectory()) {
                                    return true;
                                }
                            }
                            if (f.getName().toLowerCase().endsWith(".ods")) {
                                return true;
                            }
                            if (f.getName().toLowerCase().endsWith(".xlsx")) {
                                return true;
                            }
                            if (f.getName().toLowerCase().endsWith(".xls")) {
                                return true;
                            }
                            if (f.getName().toLowerCase().endsWith(".csv")) {
                                return true;
                            }
                            return false;
                        }

                        @Override
                        public String getDescription() {
                            return "";
                        }
                    });
                    fc.setAcceptAllFileFilterUsed(false);

                    if (fc.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {

                        if (fc.getSelectedFile().exists()) {
                            pb = new ProgressBar(this, false, "Проверка и загрузка файла ...");
                            SwingWorker sw = new SwingWorker() {
                                protected Object doInBackground() {
                                    try {
                                        UtilProtocol.FOLDER_SELECT = fc.getSelectedFile().getPath();
                                        UtilProtocol.COLUMN_SELECT = nCol.getText().trim();

                                        ProtocolOO oo = new ProtocolOO(
                                                fc.getSelectedFile().getPath(),
                                                Integer.valueOf(nCol.getText().trim()));

                                        list = oo.openReport(ProtocolForm.this);

                                    } catch (Exception ex) {
                                        list = new Vector();

                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Ошибка чтения заявки: " + ex.getMessage(),
                                                "Ошибка!",
                                                javax.swing.JOptionPane.ERROR_MESSAGE);

                                    }
                                    return null;
                                }

                                protected void done() {
                                    pb.dispose();
                                }

                            };
                            sw.execute();
                            pb.setVisible(true);

                            createTable(getData(list));
                            //getData(list);

                        } else {
                            createTable(new Vector());

                            JOptionPane.showMessageDialog(
                                    null,
                                    "Файл не существует!",
                                    "Ошибка!",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        }

                        ProtocolForm.this.toFront(); // на самый верх
                    }
                } else
                    JOptionPane.showMessageDialog(null,
                            "Колонка модели задана некорректно!",
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttProdActionPerformed(ActionEvent evt) {
        try {
            new PlanProductioForm(controller, true, UtilPlan.PLAN);

            if (UtilPlan.ACTION_BUTT_PLAN_SELECT) {
                prodNumPlan.setText(String.valueOf(UtilPlan.PLAN_SELECT_NUM));
                prodPlan.setText(String.valueOf(UtilPlan.PLAN_SELECT_NAME));
            }

        } catch (Exception e) {
            prodNumPlan.setText("");
            prodPlan.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCreateActionPerformed(ActionEvent evt) {
        try {
            boolean saveFlag = true;
            String str = "";

            if (!checkAllPlan.isSelected())
                if (prodNumPlan.getText().trim().equals("")) {
                    saveFlag = false;
                    str += "Вы не выбрали план производства!\n";
                }


            if (tModel.getDataVector().size() <= 0) {
                saveFlag = false;
                str += "Нет данных в таблице заявки!\n";
            }

            if (!saveFlag) {
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            if (saveFlag) {
                data = getVector(tModel.getDataVector());
                mapFas = new HashMap<Object, Vector>();

                pb = new ProgressBar(this, false, "Обработка данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            pdb = new ProtocolPDB();
                            db = new ProtocolDB();

                            for (Object fas : data) {
                                Vector tmp = new Vector();

                                // находим все шифр артикула из плана производства
                                Vector tmp1 = pdb.searchSarInPlanProduction(
                                        fas,
                                        checkAllPlan.isSelected() ?
                                                -1 :
                                                Integer.valueOf(prodNumPlan.getText().trim()),
                                        checkAllPlan.isSelected());

                                // по найденаму шифру находим артикула
                                for (Object sar : tmp1) {
                                    try {
                                        Vector rezalt = db.getNarModel(
                                                Integer.valueOf(sar.toString()),
                                                checkEan.isSelected(),
                                                articleList);
                                        if (!rezalt.isEmpty()) tmp.add(rezalt);
                                    } catch (Exception e) {
                                        tmp = new Vector();
                                    }
                                }

                                // помещаем в карту (модель, артикула)
                                mapFas.put(fas, tmp);
                            }
                        } catch (Exception e) {
                            mapFas = new HashMap<Object, Vector>();
                            JOptionPane.showMessageDialog(null,
                                    "Ошибка. " + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);

                        } finally {
                            pdb.disConn();
                            db.disConn();
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                // отображаем все что нашли/не нашли
                data = new Vector();
                try {
                    pb.setMessage("Формирование отчета...");

                    for (Map.Entry s : mapFas.entrySet()) {
                        Vector tmp = new Vector();
                        tmp.add(false);
                        tmp.add(s.getKey());
                        tmp.add(s.getValue().toString().replace("[", "").replace("]", ""));
                        data.add(tmp);
                    }

                } catch (Exception e) {
                    data = new Vector();
                    JOptionPane.showMessageDialog(null,
                            "Ошибка. " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);

                }
                SmallTableForm smallTableForm = new SmallTableForm(controller, ProtocolForm.this, true, data, checkEan.isSelected());

                // формируем протокол
                if (UtilProtocol.BUTT_ACTION_CREATE) {
                    ProtocolOO oo = new ProtocolOO(
                            clientText.getText().trim(),
                            smallTableForm.getDataReport());
                    oo.createReport(smallTableForm.getTypeDocument());
                }

                // пишем настройки в конфиг
                try {
                    UtilFunctions.setSettingPropFile(UtilProtocol.FOLDER_SELECT, UtilProtocol.SETTING_FOLDER_SELECT);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    UtilFunctions.setSettingPropFile(UtilProtocol.COLUMN_SELECT, UtilProtocol.SETTING_COLUMN_SELECT);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    UtilFunctions.setSettingPropFile(clientText.getText().trim(), UtilProtocol.SETTING_CLIENT_SELECT);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void checkAllPlanActionPerformed(ActionEvent evt) {
        if (checkAllPlan.isSelected()) {
            prodNumPlan.setEnabled(false);
            prodPlan.setEnabled(false);
            buttPlan.setEnabled(false);
        } else {
            prodNumPlan.setEnabled(true);
            prodPlan.setEnabled(true);
            buttPlan.setEnabled(true);
        }
    }

    private void createTable(final Vector row) {
        tModel = new DefaultTableModel(row, col) {

            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0) return true;
                else return false;
            }
        };

        tModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelListenerIsChanging) {
                    return;
                }

                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRow == -1 || minSelectedRow == -1) {
                    return;
                }
                tableModelListenerIsChanging = true;
                boolean value = ((Boolean) tModel.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                    tModel.setValueAt(Boolean.valueOf(value), table.convertRowIndexToModel(i), column);
                }

                minSelectedRow = -1;
                maxSelectedRow = -1;

                tableModelListenerIsChanging = false;
            }
        });


        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

    }

    private Vector getData(Vector list) {
        Vector rezalt = new Vector();
        Object o = "";
        try {
            for (Object list1 : list) {

                Vector tmp = new Vector();
                tmp.add(false);
                tmp.add(list1);
                o = list1;


                if (!rezalt.contains(tmp)) {
                    rezalt.add(tmp);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка для " + o.toString());
            rezalt = new Vector();
        }
        return rezalt;
    }

    private Vector getVector(Vector data) {
        Vector rezalt = new Vector();
        try {
            for (Object list1 : data) {
                Vector vec = (Vector) list1;

                if (!rezalt.contains(vec.get(1))) {
                    rezalt.add(vec.get(1));
                }
            }
        } catch (Exception e) {
            rezalt = new Vector();
        }
        return rezalt;
    }
}
