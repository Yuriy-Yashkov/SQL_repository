package dept.sklad.Import;

import by.gomel.freedev.ucframework.ucswing.dialog.CustomDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.enums.RefundType;
import by.march8.ecs.services.eancode.EanCodeControlService;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import common.ProgressBar;
import dept.marketing.CartForm;
import workDB.DB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * @author lidashka
 */
@SuppressWarnings("all")
public class ImportNakladnie extends javax.swing.JDialog {
    final JFileChooser fc = new JFileChooser();
    ArrayList list = new ArrayList();
    Object[] object;
    ImportNakladnie thisForm;
    DB db;
    double summa = 0;
    double kol = 0;
    double summa_nds = 0;
    double itogo = 0;
    String primerText = "";
    int idClient;
    String nameClient;
    boolean rezaltImport;
    ProgressBar pb;
    boolean dbfError = false;
    ArrayList<String> eanCodeList = new ArrayList<String>();
    MainController controller;
    JButton btnError = new JButton("Ошибки DBF");

    CustomDialog dialog = null;

    private RefundType REFUNT_TYPE = RefundType.COMMON;
    private List<SaleDocumentDetailItemReport> listRefundItems = null;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JTextField jTextField1;
    public ImportNakladnie(MainController mainController, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        controller = mainController;
        dialog = new CustomDialog(controller);
        initComponents();
        setTitle("Возвраты");
        thisForm = this;

        Calendar c = Calendar.getInstance();
        jLabel30.setText(new SimpleDateFormat("dd.MM.yyyy").format(c.getTime()));

        jComboBox1.addItem("Возврат от покупателя");
        jComboBox1.addItem("Возврат из розницы");

        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);
        buttonGroup1.add(jRadioButton3);

        jRadioButton1.setActionCommand("cp866");
        jRadioButton2.setActionCommand("cp1251");
        jRadioButton3.setActionCommand("UTF8");

        jRadioButton1.setSelected(true);
        btnError.setVisible(false);
        btnError.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                {
                    dialog.setVisible(true);
                }
            }
        });

        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }
    public ImportNakladnie(MainController mainController, SaleDocumentBase document) {
        super(mainController.getMainForm(), true);
        controller = mainController;
        dialog = new CustomDialog(controller);
        initComponents();
        setTitle("Возвраты");
        thisForm = this;

        Calendar c = Calendar.getInstance();
        jLabel30.setText(new SimpleDateFormat("dd.MM.yyyy").format(c.getTime()));

        jComboBox1.addItem("Возврат от покупателя");
        jComboBox1.addItem("Возврат из розницы");

        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);
        buttonGroup1.add(jRadioButton3);

        jRadioButton1.setActionCommand("cp866");
        jRadioButton2.setActionCommand("cp1251");
        jRadioButton3.setActionCommand("UTF8");

        jRadioButton1.setSelected(true);
        btnError.setVisible(false);
        btnError.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                {
                    dialog.setVisible(true);
                }
            }
        });

        setLocationRelativeTo(controller.getMainForm());
        setResizable(false);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel34 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Операция:");

        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Кол-во:");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Сумма:");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Сумма НДС:");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Итого:");

        jButton1.setText("Загрузить");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel6.setText("DBF:");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Покупатель:");

        jButton2.setText("Выбрать");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Номер ТТН:");

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Импорт возвратов от покупателей из DBF-файлов ");

        jButton3.setText("Добавить");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Отмена");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("-");
        jLabel10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel11.setText("не выбран");
        jLabel11.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("0");
        jLabel12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("0");
        jLabel13.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("0");
        jLabel14.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("0");
        jLabel15.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel16.setText("не выбран");

        jLabel17.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel17.setText("Дата документа:");

        jButton5.setText("...");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("737");
        jLabel18.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel19.setText("Склад6");
        jLabel19.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Скидка, % :");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("0");
        jLabel21.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel22.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Вид сертиф.:");

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("РБ");
        jLabel23.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel24.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Вид удост. ГГР:");

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("РБ");
        jLabel25.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel26.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Валюта:");

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Бел. руб.");
        jLabel27.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel28.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Уценка:");

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("0");
        jLabel29.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel30.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel30.setText(" . . ");

        jLabel31.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel31.setText("Кодировка:");

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setText("cp866");

        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setText("cp1251");

        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setText("UTF8");

        jLabel34.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                                .addGap(331, 331, 331))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGap(12, 12, 12)
                                                                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addComponent(jLabel22))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                                                        .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                                                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                                                .addGap(19, 19, 19)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel24))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                                                .addGap(57, 57, 57)
                                                .addComponent(jButton1)
                                                .addGap(10, 10, 10)
                                                .addComponent(btnError)
                                                .addGap(10, 10, 10))

                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel1)
                                                                .addGap(12, 12, 12)
                                                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(74, 74, 74)))))))
                                .addGap(18, 18, 18))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel31)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jRadioButton1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jRadioButton2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jRadioButton3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(25, 25, 25))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                                                .addGap(38, 38, 38)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(266, 266, 266)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17)
                                        .addComponent(jLabel8)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel30)
                                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel20)
                                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel26)
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel22)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel24)
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel28)
                                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(jLabel6)
                                        .addComponent(jButton1)
                                        .addComponent(btnError))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jRadioButton1)
                                                .addComponent(jLabel31)
                                                .addComponent(jRadioButton2)
                                                .addComponent(jRadioButton3))
                                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel3))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel13, 0, 0, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton4)
                                        .addComponent(jButton3))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        list = new ArrayList();
        kol = 0;
        summa = 0;
        summa_nds = 0;
        itogo = 0;
        primerText = "";
        jLabel16.setText("");

        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null)
                    if (f.isDirectory()) return true;

                return f.getName().toLowerCase().endsWith(".dbf");
            }

            @Override
            public String getDescription() {
                return "*.dbf";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showDialog(thisForm, null) == JFileChooser.APPROVE_OPTION) {
            jLabel16.setText(fc.getSelectedFile().getName());

            pb = new ProgressBar(thisForm, false, "Загрузка DBF-файла и проверка ...");
            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    eanCodeList.clear();
                    readDBF(fc.getSelectedFile().getPath(), buttonGroup1.getSelection().getActionCommand());

                    if (dbfError) {
                        jButton3.setEnabled(false);
                        String temp = "";
                        for (String st : eanCodeList) {
                            temp = temp + st + "\n";
                        }

                        btnError.setVisible(true);
                        dialog.setInformation(jLabel16.getText(), temp);
                        dialog.setVisible(true);

                        kol = 0;
                        summa = 0;
                        summa_nds = 0;
                        itogo = 0;
                        primerText = "";

                    } else {
                        if (REFUNT_TYPE == RefundType.COMMON) {
                            for (Iterator it = list.iterator(); it.hasNext(); ) {
                                object = (Object[]) it.next();

                                kol += Double.parseDouble(object[1].toString());
                                summa += Double.parseDouble(object[3].toString());
                                summa_nds += Double.parseDouble(object[5].toString());
                                itogo += Double.parseDouble(object[6].toString());
                                primerText = object[0].toString();
                            }
                        }

                        btnError.setVisible(false);
                        jButton3.setEnabled(true);
                    }

                    return null;
                }

                protected void done() {
                    pb.dispose();
                }
            };

            sw.execute();
            pb.setVisible(true);
        }
        setDataDBF();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new ClientForm(this, true, true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (!list.isEmpty()) {
            if (!jTextField1.getText().trim().equals("")) {
                if (!jLabel10.getText().equals("-") && !jLabel11.getText().equals("не выбран")) {
                    if (!jLabel16.getText().equals("не выбран")) {
                        try {
                            db = new DB();
                            if (!db.testNDoc(jTextField1.getText().trim())) {

                                pb = new ProgressBar(thisForm, false, "Импорт данных...");
                                SwingWorker sw = new SwingWorker() {
                                    protected Object doInBackground() {
                                        if (REFUNT_TYPE == RefundType.COMMON) {
                                            rezaltImport = db.insertOtgruz(jLabel30.getText(), jTextField1.getText().trim(), jComboBox1.getSelectedItem().toString(), getIdClient(), list, true);
                                        } else {
                                            rezaltImport = db.createRefundInvoice(jLabel30.getText(), jTextField1.getText().trim(), jComboBox1.getSelectedItem().toString(), getIdClient(), listRefundItems);
                                        }
                                        return null;
                                    }

                                    protected void done() {
                                        pb.dispose();
                                    }
                                };
                                sw.execute();
                                pb.setVisible(true);

                                if (rezaltImport)
                                    JOptionPane.showMessageDialog(null, "Импорт завершен успешно!", "Операция завершена", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                else
                                    JOptionPane.showMessageDialog(null, "Не удалось импортировать DBF-файл!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);

                            } else
                                JOptionPane.showMessageDialog(null, "ТТН с таким номером уже существует или задан некорректный формат! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } catch (Exception e) {
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "DBF-файл не выбран! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(null, "Покупатель не выбран! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Поле 'Номер ТТН' не заполнено! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } else
            JOptionPane.showMessageDialog(null, "Ошибка загрузки dbf-файла. Вектор импорта не сформирован! ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JFormattedTextField format = new javax.swing.JFormattedTextField(CartForm.maskFormatterDate());
        format.setText(jLabel30.getText());
        format.setSelectionStart(0);
        format.addKeyListener(null);
        if (JOptionPane.showOptionDialog(null, format, "Дата документа:", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Изменить", "Закрыть"}, format) == JOptionPane.YES_OPTION) {
            String tmpDate = format.getText().trim();
            if (CartForm.checkDate(tmpDate))
                jLabel30.setText(tmpDate);
        }
    }//GEN-LAST:event_jButton5ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void setDataDBF() {
        jLabel14.setText(new DecimalFormat("###,###.###").format(kol));
        jLabel15.setText(new DecimalFormat("###,###.###").format(summa));
        jLabel12.setText(new DecimalFormat("###,###.###").format(summa_nds));
        jLabel13.setText(new DecimalFormat("###,###.###").format(itogo));
        jLabel34.setText(primerText);
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int id) {
        if (id == -10) jLabel10.setText("-");
        else jLabel10.setText(String.valueOf(id));

        idClient = id;
    }

    public String getNameClient() {
        return jLabel11.getText();
    }

    public void setNameClient(String name) {
        jLabel11.setText(name);
        nameClient = name;
    }

    private boolean readDBF(String path, String codepage) {

        REFUNT_TYPE = RefundType.COMMON;
        //TODO Облагородить по-случаю, если входящих электронок будет больше
        boolean razalt = false;
        dbfError = false;
        Object[][] colname = {{"COLOR", ""}, {"KOL", ""}, {"CENA", ""}, {"SUMMA", ""}, {"NDS", ""},
                {"SUMMA_NDS", ""}, {"ITOGO", ""}, {"EANCODE", ""}, {"PREISCUR", ""}};
        DB dataBase = new DB();
        com.linuxense.javadbf.DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            com.linuxense.javadbf.DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName(codepage);
            System.out.println("Количество строк в документе :" + reader.getRecordCount());

            // Возвратная накладная трикотажторга
            int x = 0;
            if (reader.getFieldCount() == 25) {
                //System.err.println("Трикотаж");
                Object objitem[];
                Object obj[] = new Object[reader.getFieldCount()];
                boolean oldItem = false;
                for (int i = 0; i < reader.getRecordCount(); i++) {
                    oldItem = false;
                    obj = reader.nextRecord();
                    objitem = new Object[9];

                    objitem[0] = ".РАЗНОЦВЕТ";
                    objitem[1] = obj[15];

                    objitem[2] = obj[14];
                    objitem[3] = obj[18];
                    objitem[4] = obj[19];
                    objitem[5] = obj[20];
                    objitem[6] = obj[21];
                    objitem[8] = "НЕТ ДАННЫХ";

                    objitem[7] = obj[23];

                    String s = (String) objitem[7];

                    if (s != null && !s.trim().equals("")) {
                        // Поиск в базе EAN кода
                        if (!objitem[7].equals("")) {
                            boolean res = dataBase.eanCodeIsExist(objitem[7].toString());
                            // System.out.println("Результат "+res);
                            if (!res) {
                                dbfError = true;
                                eanCodeList.add(objitem[7].toString());
                                System.out.println(objitem[7].toString() + ": В базе отсутствует");
                            }
                        }

                        for (int j = 0; j < list.size(); j++) {
                            Object[] o = (Object[]) list.get(j);
                            // Если в коллекции найден такой же штрихкод
                            // суммируем количество
                            String eanSource = (String) o[7];
                            String eanDest = (String) objitem[7];
                            if (eanSource != null && eanDest != null) {
                                if (eanSource.trim().equals(eanDest.trim())) {
                                    double amountSource = (double) o[1];
                                    double amountDest = (double) objitem[1];
                                    o[1] = amountSource + amountDest;
                                    oldItem = true;
                                    break;
                                }
                            }
                        }

                        if (!objitem[7].equals("")) {
                            if (!oldItem) {
                                list.add(objitem);
                            }
                        }
                    } else {
                        System.out.println(objitem[7] + " - " + objitem[1] + " - " + objitem[2]);
                        JOptionPane.showMessageDialog(null, "В dbf-файле есть некорректные данные! Cтрока - " + (i + 2), "Внимание!!!", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                }
                double amount = 0.0;
                for (int i = 0; i < list.size(); i++) {
                    Object[] o = (Object[]) list.get(i);
                    System.out.println(o[1] + " - " + o[7]);
                    amount += (double) o[1];

                    //System.out.println("EAN 13 VALUE: " + o[7]);
                }

                System.out.println("Всего изделий после группировки: " + amount + " и строк в массиве: " + list.size() + " при исходном: " + reader.getRecordCount());

                // Электронные накладные формата КОМИНТЕРН
            }
            if (reader.getFieldCount() == 14) {
                //Коминтерн
                System.err.println("Коминтерн возврат");
                Object objitem[];
                Object obj[] = new Object[reader.getFieldCount()];
                boolean oldItem = false;
                for (int i = 0; i < reader.getRecordCount(); i++) {
                    oldItem = false;
                    obj = reader.nextRecord();
                    objitem = new Object[9];

                    objitem[0] = ".РАЗНОЦВЕТ";
                    objitem[1] = obj[5];// Кол-во

                    objitem[2] = obj[6];// Цена
                    objitem[3] = obj[8];//Сумма
                    objitem[4] = obj[7];
                    objitem[5] = obj[8];
                    objitem[6] = obj[8];
                    objitem[8] = "НЕТ ДАННЫХ";

                    objitem[7] = String.format("%.0f", obj[9]).replace(",", "");

                    String s = (String) objitem[7];

                    if (s != null && !s.trim().equals("")) {
                        // Поиск в базе EAN кода
                        if (!objitem[7].equals("")) {
                            boolean res = dataBase.eanCodeIsExist(objitem[7].toString());
                            // System.out.println("Результат "+res);
                            if (!res) {
                                dbfError = true;
                                eanCodeList.add(objitem[7].toString());
                                System.out.println(objitem[7].toString() + ": В базе отсутствует");
                            }
                        }

                        for (int j = 0; j < list.size(); j++) {
                            Object[] o = (Object[]) list.get(j);
                            // Если в коллекции найден такой же штрихкод
                            // суммируем количество
                            String eanSource = (String) o[7];
                            String eanDest = (String) objitem[7];
                            if (eanSource != null && eanDest != null) {
                                if (eanSource.trim().equals(eanDest.trim())) {
                                    double amountSource = (double) o[1];
                                    double amountDest = (double) objitem[1];
                                    o[1] = amountSource + amountDest;
                                    oldItem = true;
                                    break;
                                }
                            }
                        }

                        if (!objitem[7].equals("")) {
                            if (!oldItem) {
                                list.add(objitem);
                            }
                        }
                    } else {
                        System.out.println(objitem[7] + " - " + objitem[1] + " - " + objitem[2]);
                        JOptionPane.showMessageDialog(null, "В dbf-файле есть некорректные данные! Cтрока - " + (i + 2), "Внимание!!!", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                }
                double amount = 0.0;
                for (int i = 0; i < list.size(); i++) {
                    Object[] o = (Object[]) list.get(i);
                    System.out.println(o[1] + " - " + o[7]);
                    amount += (double) o[1];
                }
                System.out.println("Всего изделий после группировки: " + amount + " и строк в массиве: " + list.size() + " при исходном: " + reader.getRecordCount());
            } else if (reader.getFieldCount() == 29) {
                //*********************************************************************************
                // Файл возврата содержит ставку торговой надбавки
                //*********************************************************************************
                REFUNT_TYPE = RefundType.RETAIL;
                jComboBox1.setSelectedIndex(1);

                Object objitem[];
                Object obj[] = new Object[reader.getFieldCount()];
                boolean oldItem = false;
                listRefundItems = new ArrayList<>();
                EanCodeControlService service = EanCodeControlService.getInstance();

                for (int i = 0; i < reader.getRecordCount(); i++) {
                    oldItem = false;
                    obj = reader.nextRecord();

                    SaleDocumentDetailItemReport item = new SaleDocumentDetailItemReport();
                    item.setItemColor(obj[7].toString());
                    item.setAmountAll(Integer.valueOf(obj[8].toString().replace(".0", "")));

                    item.setValuePrice(Double.valueOf(obj[9].toString()));
                    item.setValueSumCost(Double.valueOf(obj[10].toString()));
                    item.setValueVAT(Float.valueOf(obj[11].toString()));
                    item.setValueSumVat(Double.valueOf(obj[12].toString()));
                    item.setValueSumCostAndVat(Double.valueOf(obj[13].toString()));

                    item.setEanCode(obj[14].toString().trim());
                    item.setItemPriceList(obj[22].toString().trim());


                    if (obj[24] == null)
                        item.setValueTradeMarkup(0);
                    else
                        item.setValueTradeMarkup(Integer.valueOf(obj[24].toString().trim().replace(".0", "")));

                    Integer count = service.getAmountEanCode(item.getEanCode().trim());
                    if (count == 0) {
                        System.out.println("EAN13 код [" + item.getEanCode() + "] не найден в базе ");
                        dbfError = true;
                        eanCodeList.add(item.getEanCode() + ": Не найден");
                    } else if (count > 1) {
                        System.out.println("EAN13 код [" + item.getEanCode() + "] дублирован в базе");
                        dbfError = true;
                        eanCodeList.add(item.getEanCode() + ": Дубликат");
                    } else {
                        listRefundItems.add(item);
                    }

                }
                list.add(1);
                for (SaleDocumentDetailItemReport i : listRefundItems) {
                    kol += i.getAmountAll();
                    summa += i.getValueSumCost();
                    summa_nds += i.getValueSumVat();
                    itogo += i.getValueSumCostAndVat();
                    //primerText = object[0].toString();
                }

            } else {

                for (int i = 0; i < colname.length; i++) {
                    for (int j = 0; j < reader.getFieldCount(); j++) {
                        field = reader.getField(j);
                        if (field.getName().trim().toUpperCase().equals(colname[i][0]))
                            colname[i][1] = j;
                    }
                    if (colname[i][1].equals(""))
                        JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                EanCodeControlService service = EanCodeControlService.getInstance();

                Object objitem[];
                Object obj[] = new Object[reader.getFieldCount()];
                boolean oldItem = false;
                for (int i = 0; i < reader.getRecordCount(); i++) {
                    oldItem = false;
                    obj = reader.nextRecord();
                    objitem = new Object[9];
                    for (int j = 0; j < colname.length; j++) {
                        objitem[j] = obj[Integer.parseInt(colname[j][1].toString())].toString().toUpperCase().trim();
                    }

                    if (objitem[0].equals("")) {
                        objitem[0] = ".РАЗНОЦВЕТ";
                    }

                    if (objitem[8].equals("")) {
                        objitem[8] = "НЕТ ДАННЫХ";
                    }
                    // Поиск в базе EAN кода
                    String ean = objitem[7].toString();
                    if (!ean.equals("")) {

                        Integer count = service.getAmountEanCode(ean.trim());
                        if (count == 0) {
                            System.out.println("EAN13 код [" + ean + "] не найден в базе ");
                            dbfError = true;
                            eanCodeList.add(ean + ": Не найден");
                        } else if (count > 1) {
                            System.out.println("EAN13 код [" + ean + "] дублирован в базе");
                            dbfError = true;
                            eanCodeList.add(ean + ": Дубликат");
                        } else {
                            list.add(objitem);
                        }
                    }
                }
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            list = new ArrayList();
            jLabel16.setText("не выбран");
            kol = 0;
            summa = 0;
            summa_nds = 0;
            itogo = 0;
            primerText = "";
            setDataDBF();
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return razalt;
    }
}
