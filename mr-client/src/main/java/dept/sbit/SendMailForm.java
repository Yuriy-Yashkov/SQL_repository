package dept.sbit;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.CriteriaItem;
import by.gomel.freedev.ucframework.ucdao.model.QueryBuilder;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.email.BelpostEMailReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.email.TorgodejdaEMailReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.RemainsReductionService;
import by.march8.entities.contractor.ClientEmailItem;
import by.march8.entities.warehouse.PriceListValue;
import by.march8.entities.warehouse.RetailValue;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentEntity;
import dept.MyReportsModule;
import dept.sklad.SkladDB;
import lombok.SneakyThrows;
import workDB.DB;
import workDB.DBF;
import workDB.PDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

@SuppressWarnings("all")
public class SendMailForm extends javax.swing.JDialog {
    int clientCode = -10;
    DB db = new DB();
    SkladDB sdb = new SkladDB();
    PDB pdb = new PDB();
    DefaultTableModel tm = null;
    DefaultTableModel tm2 = null;
    Vector col = new Vector();
    private MaskFormatter formatter;
    private JFormattedTextField ftDate;

    private JLabel lblElNakl;
    private JRadioButton rbCommonFormat;
    private JRadioButton rbBTPFormat;

    private ComboBoxPanel<ClientEmailItem> cbpClientEmail;
    private QueryBuilder qbClientEmail = new QueryBuilder(ClientEmailItem.class);
    private CriteriaItem criteriaItem = new CriteriaItem(clientCode, "clientCode", "=");
    private List<String> attachList = new ArrayList<>();

    private JCheckBox cbCreateApp;
    private MainController controller;

    // ToDo: Тут надо вставить новое поле через которое будем передавать коментарий к емейлу.
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton delButton;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // лейбл и поле для комментария
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextArea jTextArea;
    private javax.swing.JScrollPane jScrollPane;
    //
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // private javax.swing.JTextField jTextField1;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JButton sendButton;
    private javax.swing.JButton showButton;


    public SendMailForm(MainController mainController, java.awt.Frame parent, boolean modal) {
        super(mainController.getMainForm());
        controller = mainController;
        initComponents();
        initPanel();
        setLocationRelativeTo(mainController.getMainForm());
        setVisible(true);
    }


    public SendMailForm(MainController mainController, java.awt.Frame parent, boolean modal, boolean send) {
        super(mainController.getMainForm());
        controller = mainController;
        initComponents();
        initPanel();
        if (!send) {
            sendButton.setVisible(false);
            jPanel1.revalidate();
            jLabel2.setVisible(false);
        }

        setLocationRelativeTo(mainController.getMainForm());
        setVisible(true);
    }

    public SendMailForm(MainController mainController, java.awt.Frame parent, boolean modal, boolean send, SaleDocumentView document) {
        super(mainController.getMainForm());
        controller = mainController;
        initComponents();
        initPanel();
/*        if (!send) {
            sendButton.setVisible(false);
            jPanel1.revalidate();
            jLabel2.setVisible(false);
        }*/

        preset(document);

        setLocationRelativeTo(mainController.getMainForm());
        setVisible(true);
    }

    @SneakyThrows
    private void preset(SaleDocumentView document) {

        setIdKlient(document.getContractorCode());
        setNameKlient(document.getContractorName());

        Date date = DateUtils.getFirstDay(document.getDate());

        ftDate.setText(DateUtils.getNormalDateFormat(date));

        //System.out.println(getIdClient()+" "+getNameKlient() + " "+DateUtils.getNormalDateFormat(date));

        // Проставим контрагента
        tm = new DefaultTableModel(db.getNakl(ftDate.getText().trim(), clientCode), col) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTable1.setModel(tm);
        tm2 = new DefaultTableModel(null, col) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable2.setModel(tm2);

        prepareGrids();

        if (clientCode != -10) {
            qbClientEmail.updateCriteria("clientCode", clientCode);
            cbpClientEmail.setEntityParentId(clientCode);
            cbpClientEmail.loadEntityListByQuery(qbClientEmail.getQuery());
        }

        // ПОиск накладной в найденых за  период
        for (int i = 0; i < tm.getRowCount(); i++) {
            String v = (String) tm.getValueAt(i, 1);
            if (v.trim().equals(document.getDocumentNumber().trim())) {
                Vector temp = new Vector();
                temp.add(jTable1.getValueAt(i, 0));
                temp.add(jTable1.getValueAt(i, 1));
                temp.add(jTable1.getValueAt(i, 2));
                temp.add(jTable1.getValueAt(i, 3));
                tm2.addRow(temp);
                jTable2.setModel(tm2);
                tm.removeRow(i);
                jTable1.setModel(tm);

                break;
            }
        }


    }

    @SneakyThrows
    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        new AdresatForm(this, true);
        tm = new DefaultTableModel(db.getNakl(ftDate.getValue().toString().trim(), clientCode), col) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTable1.setModel(tm);
        tm2 = new DefaultTableModel(null, col) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable2.setModel(tm2);

        prepareGrids();

        if (clientCode != -10) {
            qbClientEmail.updateCriteria("clientCode", clientCode);
            cbpClientEmail.setEntityParentId(clientCode);
            cbpClientEmail.loadEntityListByQuery(qbClientEmail.getQuery());
        }
    }//GEN-LAST:event_selectButtonActionPerformed

    private void initPanel() {
        col.add("Дата");
        col.add("Накладная");

        tm = new DefaultTableModel(null, col);

        col.add("Статус");
        col.add("Расчет");

        tm2 = new DefaultTableModel(null, col);

        jTable1.setModel(tm);
        jTable2.setModel(tm2);
        ftDate.setValue(MyReportsModule.sDate);
        ftDate.addActionListener(e -> showButton.doClick());


        setTitle("Отправка накладных");
        this.setResizable(false);
    }

    public void setIdKlient(int id) {
        clientCode = id;
    }

    public int getIdClient() {
        return clientCode;
    }

    public String getNameKlient() {
        return jLabel3.getText();
    }

    public void setNameKlient(String name) {
        jLabel3.setText(name);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPanel p = new JPanel(null);
        p.setPreferredSize(new Dimension(350, 70));
        cbpClientEmail = new ComboBoxPanel<>(controller, MarchReferencesType.CLIENT_EMAIL, false);
        qbClientEmail.addCriteria(criteriaItem);
        qbClientEmail.setOrderBy("email", "id");
        //System.out.println("Тут вызов справочника \n" + qbClientEmail.getQuery());

        cbpClientEmail.addButtonActionListener(e -> {

            // ClientEmail email_ = cbpClientEmail.selectFromReference(false) ;
            // System.out.println("Тут вызов справочника \n" + qbClientEmail.getQuery());
            if (clientCode != -10) {
                // Reference refClientEmail = new Reference(controller, MarchReferencesType.CLIENT_EMAIL, MarchWindowType.PICKFRAME, qbClientEmail.getQuery());
                cbpClientEmail.selectFromReference(cbpClientEmail.getSelectedItem());
            }
        });

        //refClientEmail.selectFromReference(new ClientEmail()) ;


        ButtonGroup bgDocFormat = new ButtonGroup();
        lblElNakl = new JLabel("Формат электронной накладной");
        lblElNakl.setBounds(10, 5, 200, 20);
        rbCommonFormat = new JRadioButton("Общий формат");
        rbCommonFormat.setSelected(true);
        rbCommonFormat.setBounds(20, 30, 200, 20);
        rbBTPFormat = new JRadioButton("Формат БТП");
        rbBTPFormat.setBounds(20, 50, 200, 20);
        rbBTPFormat.setForeground(Color.BLUE.brighter());
        bgDocFormat.add(rbCommonFormat);
        bgDocFormat.add(rbBTPFormat);
        p.add(lblElNakl);
        p.add(rbCommonFormat);
        p.add(rbBTPFormat);

        // cbpClientEmail = new ComboBoxPanel<>();


        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        sendButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        delButton = new javax.swing.JButton();
        selectButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        //jTextField1 = new javax.swing.JTextField();
        showButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jCheckBox1 = new javax.swing.JCheckBox();

        // Лейбл и поле для комментария
        jLabel4 = new javax.swing.JLabel();
        jTextArea = new javax.swing.JTextArea(2, 0);
        //


        cbCreateApp = new JCheckBox("Формировать приложеине");
        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка формирования маски календаря [##.##.####] SendMailForm " + e);
        }
        formatter.setPlaceholderCharacter('0');
        ftDate = new javax.swing.JFormattedTextField(formatter);
        //ftDate.setValue(date);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sendButton.setText("Отправить");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//              System.out.println(jTextArea.getText());
                sendButtonActionPerformed(evt);
            }
        });
        jPanel1.add(sendButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 570, -1, -1));

        saveButton.setText("Сохранить");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel1.add(saveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 570, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {1, 2}
                },
                new String[]{"Title 1", "Title 2"}
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 270, 190, 260));

        jLabel1.setText("Кому: ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel2.setText("email: ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 60, -1));

        // Лейбл и филда для комента.
        jLabel4.setText("Текст:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 60, -1));

        jTextArea.setText("");
        jTextArea.setLineWrap(true);
        jScrollPane = new JScrollPane(jTextArea);
        //jScrollPane.add(jTextArea);
        jPanel1.add(jScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 420, 40));
        //

        jLabel3.setText("Адресат не выбран");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 300, -1));

        addButton.setText("<--");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, 60, -1));

        delButton.setText("-->");
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delButtonActionPerformed(evt);
            }
        });
        jPanel1.add(delButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 380, 60, -1));

        selectButton.setText("Выбрать");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        jPanel1.add(selectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 110, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Тип накладной"));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Полная");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("ЦУМ");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Филиал");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setSelected(true);
        jRadioButton4.setText("Без скидки");
        jRadioButton4.setEnabled(false);

        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setText("Со скидкой");
        jRadioButton5.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jRadioButton1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jRadioButton2))
                                        .addComponent(jRadioButton3)
                                        .addComponent(jRadioButton4)
                                        .addComponent(jRadioButton5))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jRadioButton1)
                                        .addComponent(jRadioButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton5)
                                .addGap(238, 238, 238))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 180, 140));

        //ftDate.setColumns(10);
        //jTextField1.setText("дд.мм.гггг");
        jPanel1.add(ftDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 240, 82, 25));

        showButton.setText("Показать");
        showButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showButtonActionPerformed(evt);
            }
        });
        jPanel1.add(showButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 240, -1, -1));
        jPanel1.add(p, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 125, -1, -1));

        jLabel5.setText("Показать все накладные от:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, -1, -1));


        //jPanel1.add(cbEmailSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 61, 300, -1));
        jPanel1.add(cbpClientEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 420, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null}
                },
                new String[]{
                        "Title 1", "Title 2"
                }
        ));
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 180, 260));

        jCheckBox1.setText("цена в валюте");
        jPanel1.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 540, -1, -1));
        jPanel1.add(cbCreateApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 540, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );

        initEvents();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initEvents() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent me) {
                if (me.getClickCount() == 2) {
                    addButton.doClick();
                }
            }
        });

        jTable2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent me) {
                if (me.getClickCount() == 2) {
                    delButton.doClick();
                }
            }
        });
    }

    /**
     * Сохраняем на диск электронную накладную
     *
     * @param evt
     */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (jTable2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Нет накладных для сохранения", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        attachList.clear();
        JFileChooser fc = new JFileChooser();
        int f = 1; // тип накладной
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String pathSave = new String(fc.getSelectedFile().getPath());

            if (jRadioButton1.isSelected()) f = 1; // полная
            if (jRadioButton2.isSelected()) f = 2; // ЦУМ
            if (jRadioButton3.isSelected() && jRadioButton4.isEnabled() && jRadioButton4.isSelected()) f = 3; //склад
            if (jRadioButton3.isSelected() && jRadioButton5.isEnabled() && jRadioButton5.isSelected()) f = 4; //склад
            int r = jTable2.getRowCount();
            boolean valuta = false;
            boolean createApp = false;
            valuta = jCheckBox1.isSelected();
            createApp = cbCreateApp.isSelected();

            if (f == 1) for (int i = 1; i <= r; i++) {
                // Если выбран общий формат для DBF файла
                if (rbCommonFormat.isSelected()) {
                    System.out.println("Клиент " + clientCode);
                    if (clientCode == 2354) {
                        db.createOldEmporium(jTable2.getValueAt(i - 1, 1).toString(), pathSave);
                    } else if (clientCode == 8519) {// Условие для Столбцовского РАЙПО
                        createStolbcy(jTable2.getValueAt(i - 1, 1).toString(), pathSave, valuta, createApp);
                    } else if (clientCode == 9242 || clientCode == 94402) {// Условие для БЕЛПОЧТА
                        //createFullNaklNew(jTable2.getValueAt(i - 1, 1).toString(), pathSave, valuta, createApp);
                        createCustomAttachment(clientCode, jTable2.getValueAt(i - 1, 1).toString());
                        attachList.add(jTable2.getValueAt(i - 1, 1).toString());
                    } else if (clientCode == 5143) {// Условие для РЦ ФиксМаркет
                        createFixMarket(jTable2.getValueAt(i - 1, 1).toString(), pathSave, valuta, createApp);
                    } else {
                        createFullNaklNew(jTable2.getValueAt(i - 1, 1).toString(), pathSave, valuta, createApp);
                    }
                } else {
                    // Если выбран формат Бел. торгового портала для DBF файла
                    db.createENaklBTP(jTable2.getValueAt(i - 1, 1).toString(), pathSave);
                }
            }

            if (f == 2) for (int i = 1; i <= r; i++) {
                createCUMNaklNew(jTable2.getValueAt(i - 1, 1).toString(), pathSave);
                //db.createCUMNakl(jTable2.getValueAt(i - 1, 1).toString(), pathSave);
            }

            // Фирменный без скидки
            if (f == 3) for (int i = 1; i <= r; i++) {
                //sdb.createSkladNakl(jTable2.getValueAt(i - 1, 1).toString(), pathSave, valuta, true);
                generateRetailDBF(jTable2.getValueAt(i - 1, 1).toString(), pathSave, valuta, false);
            }

            //Фирменный со скидкой
            if (f == 4) for (int i = 1; i <= r; i++) {
                //sdb.createSkladNakl(jTable2.getValueAt(i - 1, 1).toString(), pathSave, valuta, false);
                generateRetailDBF(jTable2.getValueAt(i - 1, 1).toString(), pathSave, valuta, true);
            }

        }

        showButton.doClick();
        tm2 = new

                DefaultTableModel(null, col);

        jTable2.setModel(tm2);

        prepareGrids();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void createCUMNaklNew(String nn, String pathSave) {

        DaoFactory<SaleDocumentEntity> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentEntity> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("number", nn));

        SaleDocumentEntity entity = null;

        try {
            entity = dao.getEntityByNamedQuery(SaleDocumentEntity.class, "SaleDocumentEntity.findByNumber", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (entity == null) {
            return;
        }

        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        final SaleDocumentReport report = provider.prepareDocument(entity.getId(), false);

        String param;
        DBF dbf = null;
        if (pathSave.equals("".toString()) || pathSave == null) {
            dbf = new DBF(2, nn, "");
        } else dbf = new DBF(2, nn, pathSave);

        long n = -1;
        ResultSet rs;
        // Подготовим мапу
        HashMap<String, SaleDocumentDetailItemReport> map = new HashMap<>();
        List<String> set_ = new ArrayList<>();
        final java.util.List<SaleDocumentDetailItemReport> detailList = report.getDetailList();

        for (SaleDocumentDetailItemReport item : detailList) {
            SaleDocumentDetailItemReport get_ = map.get(item.getEanCode());
            if (get_ == null) {
                map.put(item.getEanCode(), item);
                set_.add(item.getEanCode());
            } else {
                get_.setAmountAll(get_.getAmountAll() + item.getAmountAll());
            }
        }

        try {
            dbf.conn();
            SaleDocumentBase documentBase = report.getDocument();
            String sDate = (String) report.getDetailMap().get("DOCUMENT_DATE");
            Date date = DateUtils.getDateByStringValue(sDate);


            for (String key : set_) {
                SaleDocumentDetailItemReport item = map.get(key);

                Object[] v = new Object[21];
                v[0] = nn;

                v[1] = date;
                v[2] = Long.parseLong(item.getEanCode());

                param = item.getItemName();
                if (param.length() > 30) param = param.substring(0, 30);
                v[3] = param;

                v[4] = item.getArticleName();
                v[5] = item.getModelNumber();
                v[6] = String.valueOf(item.getItemSize());

                v[7] = "РБ";
                v[8] = String.valueOf(item.getItemGrade());
                v[9] = "шт   ";
                v[10] = item.getAmountAll();

                v[11] = item.getValueCost();
                v[12] = Float.parseFloat("0");
                v[13] = Float.parseFloat("0");
                v[14] = documentBase.getDiscountValue();
                v[15] = item.getValueVAT();
                v[16] = item.getItemPriceList();
                v[17] = DateUtils.getShortNormalDateFormat(parsePriceList(item.getItemPriceList()));

                param = item.getCertificateName();
                if (param.length() > 253) param = param.substring(0, 253);
                v[18] = param;
                v[19] = "";
                v[20] = "";

                dbf.write(v);
            }
        } catch (Exception e) {
            System.out.println(e);
            return;
        } finally {
            if (dbf != null) dbf.disconn();
        }
    }

    private Date parsePriceList(String priceList) {
        String date = "";
        if (priceList.length() > 9) {
            if (priceList.endsWith(".")) {
                date = priceList.substring(priceList.length() - 9, priceList.length() - 1);
            } else {
                date = priceList.substring(priceList.length() - 8, priceList.length());
            }

            return DateUtils.getDateByStringValueSimple(date);
        }

        return DateUtils.getDateByStringValue("01.01.0001");
    }

    private void createCustomAttachment(int contractorCode, final String documentNumber) {
        DaoFactory<SaleDocumentEntity> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentEntity> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("number", documentNumber));

        SaleDocumentEntity entity = null;

        try {
            entity = dao.getEntityByNamedQuery(SaleDocumentEntity.class, "SaleDocumentEntity.findByNumber", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (entity == null) {
            return;
        }

        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        final SaleDocumentReport report = provider.prepareDocument(entity.getId(), false);
        if (contractorCode == 9242) {
            new BelpostEMailReport(report);
        } else if (contractorCode == 94402) {
            new TorgodejdaEMailReport(report);
        }
    }

    /**
     * Отправляем по электронной почте
     *
     * @param evt
     */
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        if (jTable2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Нет накладных для отправки", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbpClientEmail.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Укажите электронный адрес для клиента", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        char f = 0;
        Vector nakl = new Vector();
        attachList.clear();
        if (jRadioButton1.isSelected()) f = 1;
        if (jRadioButton2.isSelected()) f = 2;
        if (jRadioButton3.isSelected() && jRadioButton4.isEnabled() && jRadioButton4.isSelected())
            f = 3; //Фирменный без скидки
        if (jRadioButton3.isSelected() && jRadioButton5.isEnabled() && jRadioButton5.isSelected())
            f = 4; //Фирменный со скидкой
        int r = jTable2.getRowCount();

        boolean valuta = false;
        boolean createApp = false;

        valuta = jCheckBox1.isSelected();
        createApp = cbCreateApp.isSelected();
        boolean customAttach = false;

        if (f == 1) for (int i = 1; i <= r; i++) {
            // Если выбран общий формат для DBF файла
            if (rbCommonFormat.isSelected()) {
                // Для старого универмага определенный вид накладной
                if (clientCode == 2354) {
                    nakl.add(db.createOldEmporium(jTable2.getValueAt(i - 1, 1).toString(), ""));
                    nakl.add(db.createOldEmporium(jTable2.getValueAt(i - 1, 1).toString(), ""));
                } else if (clientCode == 8519) {// Условие для Столбцовского РАЙПО
                    createStolbcy(jTable2.getValueAt(i - 1, 1).toString(), "", valuta, createApp);
                    nakl.add(jTable2.getValueAt(i - 1, 1).toString());
                } else if (clientCode == 9242 || clientCode == 94402) {// НАЧАЛОСЬ
                    createCustomAttachment(clientCode, jTable2.getValueAt(i - 1, 1).toString());
                    nakl.add(jTable2.getValueAt(i - 1, 1).toString());
                    customAttach = true;
                } else {
                    //db.createFullNakl(jTable2.getValueAt(i - 1, 1).toString(), "", valuta, createApp);
                    createFullNaklNew(jTable2.getValueAt(i - 1, 1).toString(), "", valuta, createApp);
                    nakl.add(jTable2.getValueAt(i - 1, 1).toString());
                }
            } else {
                // Если выбран формат Бел. торгового портала для DBF файла
                nakl.add(db.createENaklBTP(jTable2.getValueAt(i - 1, 1).toString(), ""));
            }
        }

        if (f == 2) for (int i = 1; i <= r; i++) {
            //db.createCUMNakl(jTable2.getValueAt(i - 1, 1).toString(), "");
            createCUMNaklNew(jTable2.getValueAt(i - 1, 1).toString(), "");
            nakl.add(jTable2.getValueAt(i - 1, 1).toString());
        }

        // Накладная филиалам без скидки
        if (f == 3) for (int i = 1; i <= r; i++) {
            //sdb.createSkladNakl(jTable2.getValueAt(i - 1, 1).toString(), "", valuta, true);
            generateRetailDBF(jTable2.getValueAt(i - 1, 1).toString(), "", valuta, false);
            nakl.add(jTable2.getValueAt(i - 1, 1).toString());
        }

        // Накладная филиалам со скидкой
        if (f == 4) for (int i = 1; i <= r; i++) {
            //sdb.createSkladNakl(jTable2.getValueAt(i - 1, 1).toString(), "", valuta, false);
            generateRetailDBF(jTable2.getValueAt(i - 1, 1).toString(), "", valuta, true);
            nakl.add(jTable2.getValueAt(i - 1, 1).toString());
        }
        //TODO: Done. Добавить вытягивание комеентария к письму, и передавать в "emailMsgTxt"
        SendMail sm = new SendMail(nakl, jTextArea.getText(), ((ClientEmailItem) cbpClientEmail.getSelectedItem()).getEmail(), createApp, customAttach);
        sm.execute();

        showButton.doClick();


        tm2 = new DefaultTableModel(null, col);
        jTable2.setModel(tm2);

        prepareGrids();

//        dispose();
    }//GEN-LAST:event_sendButtonActionPerformed

    private void prepareGrids() {
        TableColumn tcol;

        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            tcol = jTable1.getColumnModel().getColumn(i);
            tcol.setCellRenderer(new SendMailTableCellRenderer());
        }

        jTable1.getColumnModel().getColumn(2).setPreferredWidth(0);
        jTable1.getColumnModel().getColumn(2).setMinWidth(0);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(0);

        jTable1.getColumnModel().getColumn(3).setPreferredWidth(0);
        jTable1.getColumnModel().getColumn(3).setMinWidth(0);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(0);

        jTable2.getColumnModel().getColumn(2).setPreferredWidth(0);
        jTable2.getColumnModel().getColumn(2).setMinWidth(0);
        jTable2.getColumnModel().getColumn(2).setMaxWidth(0);

        jTable2.getColumnModel().getColumn(3).setPreferredWidth(0);
        jTable2.getColumnModel().getColumn(3).setMinWidth(0);
        jTable2.getColumnModel().getColumn(3).setMaxWidth(0);


    }

    private boolean checkDate(String chDate) {
        int d = 0, m = 0, y = 0;
        try {
            d = Integer.parseInt(chDate.substring(0, 2));
            if (d > 31 || d < 1) {
                throw new Exception("недопустимое значение дня");
            }
            m = Integer.parseInt(chDate.substring(3, 5));
            if ((m > 12) || (m < 1)) {
                throw new Exception("недопустимое значение месяца");
            }
            y = Integer.parseInt(chDate.substring(6, 10));
            if (y < 1) {
                throw new Exception("недопустимое значение года");
            }
        } catch (Exception e) {
            System.out.println("Ошибка преобразования даты:\n" + e);
            JOptionPane.showMessageDialog(null, "Ошибка формата даты. Формат: дд.мм.гггг", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @SneakyThrows
    private void showButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showButtonActionPerformed
        String date = new String(ftDate.getText().trim());
        if (checkDate(date)) {
            tm = new DefaultTableModel(db.getNakl(date, clientCode), col);
            jTable1.setModel(tm);

            prepareGrids();
            if (jTable1.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Нет накладных за данный период", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_showButtonActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        pdb.disConn();
        sdb.disConn();
    }//GEN-LAST:event_formWindowClosed

    /**
     * Проверка, можно ли выгружать данную накладную
     *
     * @return
     */
    private boolean isCorrect() {
        int status = (int) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
        String saved = jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString();

        if (status == 0) {
            return true;
        } else {
            if (saved.equals("true")) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            if (isCorrect()) {
                Vector temp = new Vector();
                temp.add(jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                temp.add(jTable1.getValueAt(jTable1.getSelectedRow(), 1));
                temp.add(jTable1.getValueAt(jTable1.getSelectedRow(), 2));
                temp.add(jTable1.getValueAt(jTable1.getSelectedRow(), 3));
                tm2.addRow(temp);
                jTable2.setModel(tm2);
                tm.removeRow(jTable1.getSelectedRow());
                jTable1.setModel(tm);
            } else {
                JOptionPane.showMessageDialog(null, "Только для закрытых и рассчитанных документов", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали ни одной наклданой", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_addButtonActionPerformed

    private void delButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delButtonActionPerformed
        if (jTable2.getSelectedRow() != -1) {
            Vector temp = new Vector();
            temp.add(jTable2.getValueAt(jTable2.getSelectedRow(), 0));
            temp.add(jTable2.getValueAt(jTable2.getSelectedRow(), 1));
            temp.add(jTable2.getValueAt(jTable2.getSelectedRow(), 2));
            temp.add(jTable2.getValueAt(jTable2.getSelectedRow(), 3));
            tm.addRow(temp);
            jTable1.setModel(tm);
            tm2.removeRow(jTable2.getSelectedRow());
            jTable2.setModel(tm2);
        } else
            JOptionPane.showMessageDialog(null, "Вы не выбрали ни одной наклданой", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_delButtonActionPerformed

    /**
     * Создаем электронную накладную для контрагента
     *
     * @param nn        номер накладной
     * @param pathSave  путь для сохранения накладной
     * @param valuta    валюта отгрузки
     * @param createApp флаг если нужно формировать дополнительно приложение к электронной накладной
     */
    public void createFullNaklNew(String nn, String pathSave, boolean valuta, boolean createApp) {

        DaoFactory<SaleDocumentEntity> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentEntity> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("number", nn));

        SaleDocumentEntity entity = null;

        try {
            entity = dao.getEntityByNamedQuery(SaleDocumentEntity.class, "SaleDocumentEntity.findByNumber", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (entity == null) {
            return;
        }

        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        final SaleDocumentReport report = provider.prepareDocument(entity.getId(), false);

        String param;
        DBF dbf = null;
        if (pathSave.equals("".toString()) || pathSave == null) {
            dbf = new DBF(1, nn, "");
        } else dbf = new DBF(1, nn, pathSave);
        ArrayList<Object[]> data = new ArrayList<Object[]>();

        RemainsReductionService remainsService = RemainsReductionService.getInstance();

        long n = -1;
        ResultSet rs;
        try {
            dbf.conn();
            SaleDocumentBase documentBase = report.getDocument();
            final java.util.List<SaleDocumentDetailItemReport> detailList = report.getDetailList();

            for (SaleDocumentDetailItemReport item : detailList) {
                Object[] v = new Object[24];
                v[0] = "ОАО \"8МАРТА\"";
                v[1] = nn;
                v[2] = Integer.valueOf(item.getModelNumber());//rs.getObject("fas");
                v[3] = item.getArticleNumber();//rs.getString("nar").trim();
                param = item.getItemName();
                if (param.length() > 45) param = param.substring(0, 45);
                v[4] = param;

                v[5] = item.getItemSizePrint().replace("--", "-");
/*              String str = rs.getObject("srt").toString().trim();
                if (str.length() > 8) str = str.substring(0, 7);*/
                v[6] = item.getGradeAsStringPlus();

                param = item.getItemColor();
                if (param.length() > 25) param = param.substring(0, 25);
                v[7] = param;

                v[8] = item.getAmountPrint();

                if (documentBase.getDocumentExport() > 0) {
                    v[9] = item.getValuePriceCurrency();//rs.getFloat("cena");
                    v[10] = item.getValueSumCostCurrency();//rs.getFloat("summa");
                    v[11] = item.getValueVAT(); //rs.getObject("nds");
                    v[12] = item.getValueSumVatCurrency();//rs.getFloat("summa_nds");
                    v[13] = item.getValueSumCostAndVatCurrency(); //rs.getFloat("itogo");
                } else {
                    v[9] = item.getValuePrice();//rs.getFloat("cena");
                    v[10] = item.getValueSumCost();//rs.getFloat("summa");
                    v[11] = item.getValueVAT(); //rs.getObject("nds");
                    v[12] = item.getValueSumVat();//rs.getFloat("summa_nds");
                    v[13] = item.getValueSumCostAndVat(); //rs.getFloat("itogo");
                }

                v[14] = item.getEanCode();//rs.getObject("eancode");
                v[15] = "РБ";

                param = item.getCertificateName();
                if (param.length() > 100) param = param.substring(0, 105);

                v[16] = param;

                param = item.getLicenseName();
                if (param.length() > 80) param = param.substring(0, 80);

                double tm = 65;

                if (item.getArticleCode().substring(2, 3).equals("3") || item.getArticleCode().substring(2, 3).equals("6")) {
                    tm = 55;
                }

                int tradeMarkup = (int) remainsService.getTradeMarkupByArticleAndSize(item, item.getItemSize(), (int) tm);
                String trM = String.valueOf(tradeMarkup);
                if (trM.length() == 1) {
                    trM = "0" + trM;
                }
                v[17] = trM + "_" + (int) item.getAccountingVat();

                v[18] = Long.valueOf(item.getTnvedCode());//rs.getObject("narp");

                v[19] = String.format("%.6f", item.getWeight() / item.getAmountAll());//
                v[20] = String.format("%.6f", item.getWeight());//String.valueOf(rs.getObject("massa"));

                param = SaleDocumentReport.getContractDescription(report);
                if (param.length() > 50) param = param.substring(0, 49);
                v[21] = param;

                param = item.getItemPriceList();
                if (param.length() > 50) param = param.substring(0, 50);
                v[22] = param;
                v[23] = item.getAccountingPrice();//rs.getFloat("cena");

                data.add(v);
                dbf.write(v);
            }
            try {
                if (createApp) {
                    SaleOO saleOO = new SaleOO(data);
                    saleOO.createReport("ПриложениеЭлНакл.ots", pathSave);
                }
            } catch (Exception exportError) {
                System.out.println("Ошибка экспорта электронной накладной" + exportError);
                JOptionPane.showMessageDialog(null, exportError.getMessage(), "Ошибка createApp!!!", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка сохранения dbf!!!", JOptionPane.ERROR_MESSAGE);
            return;
        } finally {
            if (dbf != null) dbf.disconn();
        }
    }

    /**
     * Создаем электронную накладную для ФиксМаркета
     *
     * @param nn        номер накладной
     * @param pathSave  путь для сохранения накладной
     * @param valuta    валюта отгрузки
     * @param createApp флаг если нужно формировать дополнительно приложение к электронной накладной
     */
    public void createFixMarket(String nn, String pathSave, boolean valuta, boolean createApp) {

        DaoFactory<SaleDocumentEntity> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentEntity> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("number", nn));

        SaleDocumentEntity entity = null;

        try {
            entity = dao.getEntityByNamedQuery(SaleDocumentEntity.class, "SaleDocumentEntity.findByNumber", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (entity == null) {
            return;
        }

        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        final SaleDocumentReport report = provider.prepareDocument(entity.getId(), false);

        String param;
        DBF dbf = null;
        if (pathSave.equals("".toString()) || pathSave == null) {
            dbf = new DBF(11, nn, "");
        } else dbf = new DBF(11, nn, pathSave);
        ArrayList<Object[]> data = new ArrayList<Object[]>();

        RemainsReductionService remainsService = RemainsReductionService.getInstance();

        long n = -1;
        ResultSet rs;
        try {
            dbf.conn();
            SaleDocumentBase documentBase = report.getDocument();
            final java.util.List<SaleDocumentDetailItemReport> detailList = report.getDetailList();
            int npp = 1;
            for (SaleDocumentDetailItemReport item : detailList) {
                Object[] v = new Object[14];
                v[0] = npp;
                v[1] = "";
                v[2] = item.getArticleNumber();
                param = item.getItemName();
                if (param.length() > 45) param = param.substring(0, 45);
                v[3] = param;
                v[4] = item.getEanCode();//rs.getObject("eancode");
                v[5] = "";
                v[6] = item.getAmountPrint();
                v[7] = item.getValuePrice();//rs.getFloat("cena");
                v[8] = item.getValueSumCost();//rs.getFloat("summa");
                v[9] = item.getValueVAT(); //rs.getObject("nds");
                v[10] = item.getValueSumVat();//rs.getFloat("summa_nds");
                v[11] = item.getValueSumCostAndVat(); //rs.getFloat("itogo");
                v[12] = "РБ";
                v[13] = Long.valueOf(item.getTnvedCode());//rs.getObject("narp");
                data.add(v);
                dbf.write(v);
                npp++;
            }
            try {
                if (createApp) {
                    SaleOO saleOO = new SaleOO(data);
                    saleOO.createReport("ПриложениеЭлНакл.ots", pathSave);
                }
            } catch (Exception exportError) {
                System.out.println("Ошибка экспорта электронной накладной" + exportError);
            }

        } catch (Exception e) {
            System.out.println(e);
            return;
        } finally {
            if (dbf != null) dbf.disconn();
        }
    }

    /**
     * Создаем электронную накладную для контрагента Столбцовское РАЙПО
     *
     * @param nn        номер накладной
     * @param pathSave  путь для сохранения накладной
     * @param valuta    валюта отгрузки
     * @param createApp флаг если нужно формировать дополнительно приложение к электронной накладной
     */
    public void createStolbcy(String nn, String pathSave, boolean valuta, boolean createApp) {

        DaoFactory<SaleDocumentEntity> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentEntity> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("number", nn));

        SaleDocumentEntity entity = null;

        try {
            entity = dao.getEntityByNamedQuery(SaleDocumentEntity.class, "SaleDocumentEntity.findByNumber", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (entity == null) {
            return;
        }

        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        final SaleDocumentReport report = provider.prepareDocument(entity.getId(), false);

        String param;
        DBF dbf = null;
        if (pathSave.equals("".toString()) || pathSave == null) {
            dbf = new DBF(10, nn, "");
        } else dbf = new DBF(10, nn, pathSave);
        ArrayList<Object[]> data = new ArrayList<Object[]>();

        long n = -1;
        ResultSet rs;
        try {
            dbf.conn();
            SaleDocumentBase documentBase = report.getDocument();
            final java.util.List<SaleDocumentDetailItemReport> detailList = report.getDetailList();

            for (SaleDocumentDetailItemReport item : detailList) {
                Object[] v = new Object[26];

                v[0] = item.getArticleNumber();
                v[1] = item.getModelNumber();

                param = item.getItemName();
                if (param.length() > 30) param = param.substring(0, 30);
                v[2] = param;

                v[3] = item.getItemGrade();
                v[4] = String.valueOf(item.getItemGrowz());
                v[5] = String.valueOf(item.getItemSize());
                v[6] = item.getItemSizePrint().replace("--", "-");
                v[7] = Long.valueOf(item.getEanCode().substring(0, 12));

                v[8] = "";//SOSTAV
                v[9] = "нет";// SEX
                v[10] = "";// AGE
                v[11] = "";//COLLECTION
                v[12] = "";// SERT
                //v[13] = null;//DSERT
                v[14] = item.getValuePrice();//CENA
                v[15] = documentBase.getDiscountValue();
                v[16] = item.getValueVAT();
                v[17] = item.getValueVAT();
                v[18] = 0;
                v[19] = item.getAmountPrint();//TNV
                v[20] = "РБ";//CONT
                //v[21] = null ;//DGODN
                v[22] = 0;//OPT
                v[23] = 0;//NSP
                v[24] = String.valueOf(item.getPtkCode()) + " Трикотажные изделия";//TOVGR
                v[25] = item.getMeasureUnit();//EDIZM

 /*
                v[0] = "ОАО \"8МАРТА\"";
                v[1] = nn;
                v[2] = Integer.valueOf(item.getModelNumber());//rs.getObject("fas");
                v[3] = item.getArticleNumber();//rs.getString("nar").trim();
                param = item.getItemName();
                if (param.length() > 45) param = param.substring(0, 45);
                v[4] = param;

                v[5] = item.getItemSizePrint();
              String str = rs.getObject("srt").toString().trim();
                if (str.length() > 8) str = str.substring(0, 7);
                v[6] = item.getGradeAsStringPlus();

                param = item.getItemColor();
                if (param.length() > 25) param = param.substring(0, 25);
                v[7] = param;

                v[8] = item.getAmountPrint();

                if (documentBase.getDocumentExport() > 0) {
                    v[9] = item.getValuePriceCurrency();//rs.getFloat("cena");
                    v[10] = item.getValueSumCostCurrency();//rs.getFloat("summa");
                    v[11] = item.getValueVAT(); //rs.getObject("nds");
                    v[12] = item.getValueSumVatCurrency();//rs.getFloat("summa_nds");
                    v[13] = item.getValueSumCostAndVatCurrency(); //rs.getFloat("itogo");
                } else {
                    v[9] = item.getValuePrice();//rs.getFloat("cena");
                    v[10] = item.getValueSumCost();//rs.getFloat("summa");
                    v[11] = item.getValueVAT(); //rs.getObject("nds");
                    v[12] = item.getValueSumVat();//rs.getFloat("summa_nds");
                    v[13] = item.getValueSumCostAndVat(); //rs.getFloat("itogo");
                }

                v[14] = item.getEanCode();//rs.getObject("eancode");
                v[15] = "РБ";

                param = item.getCertificateName();
                if (param.length() > 100) param = param.substring(0, 105);

                v[16] = param;

                param = item.getLicenseName();
                if (param.length() > 80) param = param.substring(0, 80);
                v[17] = param;

                v[18] = Long.valueOf(item.getTnvedCode());//rs.getObject("narp");

                v[19] = String.format("%.6f", item.getWeight() / item.getAmountAll());//
                v[20] = String.format("%.6f", item.getWeight());//String.valueOf(rs.getObject("massa"));

                param = SaleDocumentShippingReport.getContractDescription(report);
                if (param.length() > 50) param = param.substring(0, 49);
                v[21] = param;

                param = item.getItemPriceList();
                if (param.length() > 50) param = param.substring(0, 50);
                v[22] = param;
                v[23] = item.getAccountingPrice();//rs.getFloat("cena");
*/

                data.add(v);
                dbf.write(v);
            }
            try {
                if (createApp) {
                    SaleOO saleOO = new SaleOO(data);
                    saleOO.createReport("ПриложениеЭлНакл.ots", pathSave);
                }
            } catch (Exception exportError) {
                System.out.println("Ошибка экспорта электронной накладной" + exportError);
            }

        } catch (Exception e) {
            System.out.println(e);
            return;
        } finally {
            if (dbf != null) dbf.disconn();
        }
    }

    /**
     * Формирование Электронной накладной для фирменных магазинов и филлиалов
     *
     * @param nn       номер накладной
     * @param pathSave путь для сохранения файла
     * @param valuta   валюта отгрузки
     * @param diskount процент скидки
     */
    private void generateRetailDBF(String nn, String pathSave, boolean valuta, boolean diskount) {
        System.out.println("Скидка " + diskount);
        String query = "";
        //setOtgruzAddition(nn);
        if (!valuta && !diskount) {
            query = " {? = call _nakladniev_firm_mag (?) }";
        }
//        } else {
//            query = " {? = call _nakladniev_firm_mag (?) }";
//        }
        // Скидка !!!!!!!!!!
        if (!valuta && diskount) {
            query = " {? = call _nakladniev_firm_mag_skid (?) }";
        }
//        else {
//            query = " {? = call _nakladniev_firm_mag_skid (?) }";
//        }
        String dogovor = new String("");
        String param;

        DBF dbf = null;
        if (pathSave.equals("".toString()) || pathSave == null) {
            dbf = new DBF(3, nn, "");
        } else {
            dbf = new DBF(3, nn, pathSave);
        }
        long n = -1;

        DaoFactory<SaleDocumentEntity> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentEntity> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("number", nn));

        SaleDocumentEntity entity = null;

        try {
            entity = dao.getEntityByNamedQuery(SaleDocumentEntity.class, "SaleDocumentEntity.findByNumber", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (entity == null) {
            return;
        }

        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        final SaleDocumentReport report = provider.prepareDocument(entity.getId(), false);

        try {

            dbf.conn();
            SaleDocumentBase documentBase = report.getDocument();
            final java.util.List<SaleDocumentDetailItemReport> detailList = report.getDetailList();

            RemainsReductionService remainsService = RemainsReductionService.getInstance();

            for (SaleDocumentDetailItemReport item : detailList) {

                Object[] v = new Object[29];
                v[0] = "ОАО \"8МАРТА\"";
                v[1] = nn;
                v[2] = Integer.valueOf(item.getModelNumber());
                v[3] = item.getArticleNumber();

                if (item.getItemName().length() > 45) {
                    v[4] = item.getItemName().substring(0, 45);
                } else {
                    v[4] = item.getItemName();
                }
                v[5] = item.getItemSizePrint().replace("--", "-");
                v[6] = item.getGradeAsStringPlus();

                param = item.getItemColor().trim();
                if (param.length() > 25) {
                    param = param.substring(0, 25);
                }

                v[7] = param;
                v[8] = item.getAmountPrint();

                PriceListValue priceList = item.getPriceListValue();

                // Если выбран тип со скидкой, пишем в DBF цифры с блока расчета цены со скидкой
                if (diskount) {
                    v[9] = item.getValuePrice();
                    v[10] = item.getValueSumCost();//rs.getObject("summa");
                    v[11] = item.getValueVAT(); //rs.getObject("nds");
                    v[12] = item.getValueSumVat();//rs.getObject("summa_nds");
                    v[13] = item.getValueSumCostAndVat(); //rs.getObject("itogo");
                } else {
                    // Иначе пишем с блока расчета цены по прейскуранту
                    v[9] = item.getAccountingPrice();
                    v[10] = priceList.getPriceListSumCost();//rs.getObject("summa");
                    v[11] = item.getValueVAT(); //rs.getObject("nds");
                    v[12] = priceList.getPriceListSumVat();//rs.getObject("summa_nds");
                    v[13] = priceList.getPriceListSumCostAndVat(); //rs.getObject("itogo");
                }

                v[14] = item.getEanCode();// rs.getObject("eancode");
                v[15] = "РБ";

                param = item.getCertificateName();
                if (param.length() > 80) {
                    param = param.substring(0, 80);
                }
                v[16] = param;

                param = item.getLicenseName();
                if (param.length() > 80) {
                    param = param.substring(0, 80);
                }

                double tm = 65;

                if (item.getArticleCode().substring(2, 3).equals("3") || item.getArticleCode().substring(2, 3).equals("6")) {
                    tm = 55;
                }

                int tradeMarkup = (int) remainsService.getTradeMarkupByArticleAndSize(item, item.getItemSize(), (int) tm);
                String trM = String.valueOf(tradeMarkup);
                if (trM.length() == 1) {
                    trM = "0" + trM;
                }
                v[17] = trM + "_" + (int) item.getAccountingVat();


                v[18] = Long.valueOf(item.getTnvedCode());//rs.getObject("narp");
                v[19] = String.format("%.4f", item.getWeight() / item.getAmountAll());//String.valueOf(rs.getObject("massa_ed"));
                v[20] = String.format("%.4f", item.getWeight());//String.valueOf(rs.getObject("massa"));
                v[21] = "";//dogovor;

                param = item.getItemPriceList();
                if (param.length() > 50) {
                    param = param.substring(0, 50);
                }
                v[22] = param;
                v[23] = item.getArticleCode();//rs.getString("sar").trim();

                RetailValue retail = item.getRetailValue();
                v[24] = String.format("%.2f", retail.getValueTradeMarkup());

                v[25] = String.format("%.2f", retail.getValueCostRetail());
                v[26] = String.valueOf(item.getPtkCode()); //"0";//rs.getObject("ptk") != null ? rs.getString("ptk").trim() : "0";
                v[27] = item.getAccountingPrice(); //rs.getObject("cena_uch") != null ? rs.getFloat("cena_uch") : 0;
                v[28] = item.getAccountingVat();

                dbf.write(v);
            }

        } catch (Exception e) {
            System.err.println(e);
            return;
        } finally {
            if (dbf != null) {
                dbf.disconn();
            }
        }

    }

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        jRadioButton4.setEnabled(true);
        jRadioButton5.setEnabled(true);
        rbBTPFormat.setEnabled(false);
        rbCommonFormat.setSelected(true);
        jRadioButton5.setSelected(true);

    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        jRadioButton4.setEnabled(false);
        jRadioButton5.setEnabled(false);
        rbBTPFormat.setEnabled(true);
        //rbCommonFormat.setSelected(true);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        jRadioButton4.setEnabled(false);
        jRadioButton5.setEnabled(false);
        rbBTPFormat.setEnabled(false);
        rbCommonFormat.setSelected(true);
    }//GEN-LAST:event_jRadioButton2ActionPerformed
    // End of variables declaration//GEN-END:variables


}