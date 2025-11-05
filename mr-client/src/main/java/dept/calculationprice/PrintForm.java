/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import by.gomel.freedev.ucframework.uccore.report.OfficeBootStrap;
import by.gomel.freedev.ucframework.uccore.report.connector.BootstrapSocketConnector;
import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XURLTransformer;
import dept.MyReportsModule;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * @author user
 */
@SuppressWarnings("all")
public final class PrintForm extends javax.swing.JFrame {

    // private static final Logger log = new Log().getLoger(PDB_new.class);
    private static final long serialVersionUID = 1L;
    private static XMultiComponentFactory xRemoteServiceManager = null;
    private static XURLTransformer xTransformer = null;
    private static XDesktop xDesktop = null;
    private static XComponentLoader xComponentLoader = null;
    private static PropertyValue[] loadProps = new PropertyValue[0];
    private PricePDB ppdb;
    private int id;
    private List<Integer> listId;
    private PricePDB dao;
    private PrintForm self;
    private int idTypeProd;
    private boolean bool;

    private JPopupMenu popupMenuTools = new JPopupMenu();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalculationCen;
    private javax.swing.JButton btnCalculationSebes;
    private javax.swing.JButton btnMatZat;
    private javax.swing.JFormattedTextField etDate;
    private javax.swing.JCheckBox chbAddCoefficients;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel tvError;

    /**
     * Creates new form PrintForm
     *
     * @param parent
     * @param modal
     * @param id
     */
    public PrintForm(java.awt.Frame parent, boolean modal, int id) {
        super();
        setId(id);
        try {
            ppdb = new PricePDB();
            ppdb.conn();

        } catch (Exception e) {
        }
        setDao(ppdb);
        setLocationRelativeTo(parent);
        setVisible(true);
        initComponents();
        chbAddCoefficients.setSelected(true);
        setBool(false);
        setSelf(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        MaskFormatter formatter;
        try {
            formatter = new MaskFormatter("##.##.####");
            formatter.setValidCharacters("0123456789");
            etDate.setFormatterFactory(new DefaultFormatterFactory(formatter));
            etDate.setValue(dateFormat.format(new Date()));
            //System.out.println(etDate.getValue());
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(PrintForm.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    /**
     * @param parent
     * @param modal
     * @param id
     */
    public PrintForm(java.awt.Frame parent, boolean modal, List<Integer> id) {
        super();
        setListId(id);
        try {
            ppdb = new PricePDB();
            ppdb.conn();

        } catch (Exception e) {
        }
        setDao(ppdb);
        setLocationRelativeTo(parent);
        setVisible(true);
        initComponents();
        chbAddCoefficients.setSelected(true);
        setBool(true);
        setSelf(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        etDate.setValue(dateFormat.format(new Date()));
        MaskFormatter formatter;
        try {
            formatter = new MaskFormatter("##.##.####");
            formatter.setValidCharacters("0123456789");
            etDate.setFormatterFactory(new DefaultFormatterFactory(formatter));
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(PrintForm.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    /**
     *
     */
    public static void connect() {
        try {
            // получим контекст удаленного компонента офиса

            String oooExeFolder;
/*
            java.io.File fC = new java.io.File("c:\\windows\\explorer.exe");
            if (!fC.exists()) {
                oooExeFolder = "/usr/bin/soffice";
            } else {
                oooExeFolder = "C:\\Program Files\\OpenOffice.org 3\\program";
                System.out.println(oooExeFolder);
            }
*/

            OfficeBootStrap office = OfficeBootStrap.getInstance();
            oooExeFolder = office.getBootPath();


            BootstrapSocketConnector boot = new BootstrapSocketConnector(oooExeFolder);
            XComponentContext xContext = boot.connect();

            xRemoteServiceManager = xContext.getServiceManager();
            // создадим сервис, который понадобится при печати
            Object transformer = xRemoteServiceManager.createInstanceWithContext("com.sun.star.util.URLTransformer", xContext);
            xTransformer = (XURLTransformer) UnoRuntime.queryInterface(XURLTransformer.class, transformer);
            //Object transformer = xRemoteServiceManager.createInstanceWithContext();

            // получим сервис Desktop
            Object desktop = (XInterface) xRemoteServiceManager.createInstanceWithContext(
                    "com.sun.star.frame.Desktop", xContext);
            xDesktop = (XDesktop) UnoRuntime.queryInterface(
                    XDesktop.class, desktop);

            // данный интерфейс позволяет загружать и сохранять документы
            xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(
                    XComponentLoader.class, desktop);
        } catch (BootstrapException ex) {
            java.util.logging.Logger.getLogger(PrintForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Ошибка соединения с OpenOffice!.", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (com.sun.star.uno.Exception ex) {
            java.util.logging.Logger.getLogger(PrintForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Ошибка соединения с OpenOffice!!.", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * @param sURL
     * @return
     */
    public static XComponent openDocument(String sURL) {

        String url = new String();

        try {

            java.io.File fC = new java.io.File("c:\\windows\\explorer.exe");
            if (!fC.exists()) {
                StringBuffer sTmp = new StringBuffer("file:///");
                sTmp.append(MyReportsModule.progPath);
                sTmp.append("/");
                url = sTmp.toString().replace('\\', '/') + sURL;
            } else {
                StringBuffer sTmp = new StringBuffer("file:///");
                //sTmp.append(MainForm.progPath);
                sTmp.append(MyReportsModule.progPath);
                sTmp.append("/");

                sURL = sTmp.toString().replace('\\', '/') + sURL;
                //System.out.println(sURL);
                //PropertyValue[] loadProps = new PropertyValue[0];
                return xComponentLoader.loadComponentFromURL(sURL, "_blank", 0, loadProps);
                // url = System.getProperty("user.dir") + "\\" + sURL.replace('/', '\\');
            }

            System.err.println(url);

            return xComponentLoader.loadComponentFromURL(url, "_blank", 0, loadProps);

        } catch (IOException e) {

            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка открытия шаблона " + url, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "!Ошибка открытия шаблона " + url, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.out.println(e.getMessage());
        }//log.warn("Открытие шаблона "+ sURL +" прошло нормально");
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JMenuItem miWithoutCredit = new JMenuItem("Без учета % по кредитам и займам");
        JMenuItem miWithCredit = new JMenuItem("С учетом % по кредитам и займам");

        popupMenuTools.add(miWithoutCredit);
        popupMenuTools.add(miWithCredit);

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        chbAddCoefficients = new javax.swing.JCheckBox();
        btnCalculationCen = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnMatZat = new javax.swing.JButton();
        btnCalculationSebes = new javax.swing.JButton();
        tvError = new javax.swing.JLabel();
        etDate = new javax.swing.JFormattedTextField();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Печать отчетов");

        chbAddCoefficients.setText("Коэффициенты");

        btnCalculationCen.setText("Плановая калькуляция по расчету отпускной цены");
        btnCalculationCen.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenuTools.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });

        jLabel2.setText("Дата");

        btnMatZat.setText("Расшифровка материальных затрат");
        btnMatZat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMatZatActionPerformed(evt);
            }
        });

        btnCalculationSebes.setText("Плановая калькуляция по расчету себестоимости");
        btnCalculationSebes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculationSebesActionPerformed(evt);
            }
        });

        etDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(btnCalculationCen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnMatZat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnCalculationSebes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(57, 57, 57)
                                                .addComponent(jLabel2)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(chbAddCoefficients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(tvError, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(etDate))))
                                .addContainerGap(18, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(109, 109, 109))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(24, 24, 24)
                                .addComponent(chbAddCoefficients)
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(etDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tvError)
                                .addGap(3, 3, 3)
                                .addComponent(btnCalculationCen)
                                .addGap(18, 18, 18)
                                .addComponent(btnMatZat)
                                .addGap(18, 18, 18)
                                .addComponent(btnCalculationSebes)
                                .addContainerGap(46, Short.MAX_VALUE))
        );

        miWithoutCredit.addActionListener(a -> {
            createCalculationWithoutCredit();
        });

        miWithCredit.addActionListener(a -> {
            createCalculationWithCredit();
        });


        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createCalculationWithoutCredit() {
        if ("".equals(etDate.getValue())) {
            tvError.setText("Введите дату");
            return;
        } else {
            connect();
            tvError.setText("");
            try {

                if (!chbAddCoefficients.isSelected()) {
                    XComponent currentDocument = openDocument("Templates/" + "CPcalcCenKf.ots");
                    if (isBool()) {
                        reportCen(currentDocument, getListId());
                    } else {
                        reportCen(currentDocument, getId());
                    }

                } else {
                    XComponent currentDocument = openDocument("Templates/" + "CPcalcCenKf.ots");
                    if (isBool()) {
                        reportCenKf(currentDocument, getListId());
                    } else {
                        reportCenKf(currentDocument, getId());
                    }
                }
            } catch (java.lang.Exception e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Ошибка при попытке создания отчёта: " + e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void createCalculationWithCredit() {
        if ("".equals(etDate.getValue())) {
            tvError.setText("Введите дату");
            return;
        } else {
            connect();
            tvError.setText("");
            try {

                boolean addCoefficients = chbAddCoefficients.isSelected();
                XComponent currentDocument = openDocument("Templates/" + "CPcalcCenKf_new.ots");
                List<Integer> list;
                if (isBool()) {
                    list = getListId();
                } else {
                    list = new ArrayList<>();
                    list.add(getId());
                }

                reportCenKfNew(currentDocument, list, addCoefficients);

            } catch (java.lang.Exception e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Ошибка при попытке создания отчёта: " + e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void btnMatZatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMatZatActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                connect();

                try {

                    if (isBool()) {
                        if (getListId().size() == 2) {
                            XComponent currentDocument = openDocument("Templates/" + "CPmatrZatr2.ots");
                            reportMatZatr(currentDocument, getListId());
                        } else {
                            XComponent currentDocument = openDocument("Templates/" + "CPmatrZatrList.ots");
                            reportMatZatr(currentDocument, getListId());
                        }

                    } else {
                        XComponent currentDocument = openDocument("Templates/" + "CPmatrZatr.ots");
                        reportMatZatr(currentDocument, getId());
                    }
                } catch (java.lang.Exception e) {
                    System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(null, "Ошибка при попытке создания отчёта: " + e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }//GEN-LAST:event_btnMatZatActionPerformed

    private void btnCalculationSebesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculationSebesActionPerformed

        if ("".equals(etDate.getText())) {
            tvError.setText("Введите дату");
            return;
        } else {
            connect();
            tvError.setText("");
            try {
                if (!chbAddCoefficients.isSelected()) {
                    XComponent currentDocument = openDocument("Templates/" + "CPcalcSebesKf.ots");

                    if (isBool()) {
                        reportSeb(currentDocument, getListId());

                    } else {
                        reportSeb(currentDocument, getId());
                    }
                } else {
                    XComponent currentDocument = openDocument("Templates/" + "CPcalcSebesKf.ots");

                    if (isBool()) {
                        reportSebKf(currentDocument, getListId());
                    } else {
                        reportSebKf(currentDocument, getId());
                    }
                }
            } catch (java.lang.Exception e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Ошибка при попытке создания отчёта: " + e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnCalculationSebesActionPerformed

    private void etDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etDateActionPerformed
    }//GEN-LAST:event_etDateActionPerformed

    /**
     * @param currentDocument
     * @param id
     */
    public void reportMatZatr(XComponent currentDocument, List<Integer> id) {
        ValueCalculation valueCalculation;
        String tempCsr, tempWsr, tempNsr, tempResult;
        int i = 6;
        try {
            for (int idCalc : id) {

                valueCalculation = dao.getDataCalculation(idCalc);

                XSpreadsheetDocument xSpreadsheetDocument;
                xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
                XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
                Object sheet = xSpreadsheets.getByName("Лист1");
                XSpreadsheet xSpreadsheet;
                xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
                XCell xCell;

                xCell = xSpreadsheet.getCellByPosition(0, i - 3);
                xCell.setFormula(valueCalculation.getNiz());
                xCell = xSpreadsheet.getCellByPosition(0, i - 2);
                xCell.setFormula("артикул: " + valueCalculation.getNar().trim());
                xCell = xSpreadsheet.getCellByPosition(1, i - 2);
                xCell.setFormula("модель: " + valueCalculation.getFas().trim());
                xCell = xSpreadsheet.getCellByPosition(3, i - 2);
                xCell.setFormula("размер: " + valueCalculation.getRzmn().trim() + "-" + valueCalculation.getRzmk().trim());

                double sumWsr = 0;
                double sum = 0;
                for (int j = 0; j < 9; j++) {

                    //if (valueCalculation.getNsr()[j] != null & valueCalculation.getWsr()[j] != 0 & valueCalculation.getCsr()[j] != 0) {
                    if (valueCalculation.getNsr()[j] != null) {

                        if (!valueCalculation.getNsr()[j].trim().equals("")) {
                            xCell = xSpreadsheet.getCellByPosition(0, i);
                            tempNsr = xCell.getFormula();
                            xCell = xSpreadsheet.getCellByPosition(1, i);
                            tempCsr = xCell.getFormula();
                            xCell = xSpreadsheet.getCellByPosition(2, i);
                            tempWsr = xCell.getFormula();
                            xCell = xSpreadsheet.getCellByPosition(3, i);
                            tempResult = xCell.getFormula();

                            xCell = xSpreadsheet.getCellByPosition(0, i);
                            xCell.setFormula(tempNsr + valueCalculation.getNsr()[j] + "\n");
                            xCell = xSpreadsheet.getCellByPosition(1, i);
                            xCell.setFormula(tempCsr + String.format("%.4f", valueCalculation.getCsr()[j]).replace(',', '.') + "\n");
                            xCell = xSpreadsheet.getCellByPosition(2, i);
                            xCell.setFormula(tempWsr + String.format("%.5f", valueCalculation.getWsr()[j]).replace(',', '.') + "\n");

                            xCell = xSpreadsheet.getCellByPosition(3, i);
                            xCell.setFormula(tempResult + String.format("%.4f", valueCalculation.getCsr()[j] * valueCalculation.getWsr()[j]).replace(',', '.') + "\n");

                            sumWsr += valueCalculation.getWsr()[j];
                            sum += (valueCalculation.getCsr()[j] * valueCalculation.getWsr()[j]);
                        }
                    }
                }

                xCell = xSpreadsheet.getCellByPosition(0, i + 1);
                xCell.setFormula("Осн. сырье  и материалы\n"
                        + "Топливо и энергия\n"
                        + "Вспомогат. материалы\n"
                        + "Всего мат. затрат");

                xCell = xSpreadsheet.getCellByPosition(2, i + 1);
                xCell.setFormula(String.format("%.5f", sumWsr).replace(',', '.') + "\n"
                        + String.format("%.4f", valueCalculation.getTen()).replace(',', '.')
                        + "*" + String.format("%.5f", sumWsr).replace(',', '.') + "* 1.000" + "\n"
                        + String.format("%.4f", valueCalculation.getVms()).replace(',', '.') + "* 1.000");

                xCell = xSpreadsheet.getCellByPosition(3, i + 1);
                xCell.setFormula(String.format("%.4f", valueCalculation.getSm()).replace(',', '.') + "\n"
                        + String.format("%.4f", valueCalculation.getTe()).replace(',', '.') + "\n"
                        + String.format("%.4f", valueCalculation.getVm()).replace(',', '.') + "\n"
                        + String.format("%.4f", valueCalculation.getSm() + valueCalculation.getTe() + valueCalculation.getVm()).replace(',', '.'));
                i += 5;
            }

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта  по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта  по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (WrappedTargetException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта  по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param currentDocument
     * @param id
     */
    public void reportMatZatr(XComponent currentDocument, int id) {
        ValueCalculation valueCalculation;
        String tempCsr, tempWsr, tempNsr, tempResult;

        try {
            valueCalculation = dao.getDataCalculation(id);

            XSpreadsheetDocument xSpreadsheetDocument;
            xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet;
            xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell;

            xCell = xSpreadsheet.getCellByPosition(0, 3);
            xCell.setFormula(valueCalculation.getNiz());
            xCell = xSpreadsheet.getCellByPosition(0, 4);
            xCell.setFormula("артикул: " + valueCalculation.getNar().trim());
            xCell = xSpreadsheet.getCellByPosition(1, 4);
            xCell.setFormula("модель: " + valueCalculation.getFas().trim());
            xCell = xSpreadsheet.getCellByPosition(3, 4);
            xCell.setFormula("размер: " + valueCalculation.getRzmn().trim() + "-" + valueCalculation.getRzmk().trim());

            int i = 6;
            double sumWsr = 0;
            double sum = 0;
            for (int j = 0; j < 9; j++) {

                //  if (valueCalculation.getNsr()[j] != null & valueCalculation.getWsr()[j] != 0 & valueCalculation.getCsr()[j] != 0) {
                if (valueCalculation.getNsr()[j] != null) {

                    if (!valueCalculation.getNsr()[j].trim().equals("")) {
                        xCell = xSpreadsheet.getCellByPosition(0, i);
                        tempNsr = xCell.getFormula();
                        xCell = xSpreadsheet.getCellByPosition(1, i);
                        tempCsr = xCell.getFormula();
                        xCell = xSpreadsheet.getCellByPosition(2, i);
                        tempWsr = xCell.getFormula();
                        xCell = xSpreadsheet.getCellByPosition(3, i);
                        tempResult = xCell.getFormula();

                        xCell = xSpreadsheet.getCellByPosition(0, i);
                        xCell.setFormula(tempNsr + valueCalculation.getNsr()[j] + "\n");
                        xCell = xSpreadsheet.getCellByPosition(1, i);
                        xCell.setFormula(tempCsr + String.format("%.4f", valueCalculation.getCsr()[j]).replace(',', '.') + "\n");
                        xCell = xSpreadsheet.getCellByPosition(2, i);
                        xCell.setFormula(tempWsr + String.format("%.5f", valueCalculation.getWsr()[j]).replace(',', '.') + "\n");

                        xCell = xSpreadsheet.getCellByPosition(3, i);
                        xCell.setFormula(tempResult + String.format("%.4f", valueCalculation.getCsr()[j] * valueCalculation.getWsr()[j]).replace(',', '.') + "\n");

                        sumWsr += valueCalculation.getWsr()[j];
                        sum += (valueCalculation.getCsr()[j] * valueCalculation.getWsr()[j]);
                    }
                }
            }

            xCell = xSpreadsheet.getCellByPosition(0, i + 1);
            xCell.setFormula("Осн. сырье  и материалы\n"
                    + "Топливо и энергия\n"
                    + "Вспомогат. материалы\n"
                    + "Всего мат. затрат");

            xCell = xSpreadsheet.getCellByPosition(2, i + 1);
            xCell.setFormula(String.format("%.5f", sumWsr).replace(',', '.') + "\n"
                    + String.format("%.4f", valueCalculation.getTen()).replace(',', '.')
                    + "*" + String.format("%.5f", sumWsr).replace(',', '.') + "* 1.000" + "\n"
                    + String.format("%.4f", valueCalculation.getVms()).replace(',', '.') + "* 1.000");

            xCell = xSpreadsheet.getCellByPosition(3, i + 1);
            xCell.setFormula(String.format("%.4f", valueCalculation.getSm()).replace(',', '.') + "\n"
                    + String.format("%.4f", valueCalculation.getTe()).replace(',', '.') + "\n"
                    + String.format("%.4f", valueCalculation.getVm()).replace(',', '.') + "\n"
                    + String.format("%.4f", valueCalculation.getSm() + valueCalculation.getTe() + valueCalculation.getVm()).replace(',', '.'));

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта  по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта  по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (WrappedTargetException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта  по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String parsePriceNumber(String number) {
        //System.out.println("Парсим прайс [" + number + "]");
        //System.out.println();
        return number.substring(5, 7);
    }

    /**
     * @param currentDocument
     * @param id
     */
    public void reportSeb(XComponent currentDocument, List<Integer> id) {
        ValueCalculation valueCalculation;
        int column = 1;
        String temp = "";

        try {

            for (int i : id) {

                valueCalculation = dao.getDataCalculation(i);

                idTypeProd = dao.getTypeProdByGroup(Integer.parseInt(valueCalculation.getGrup()));
                if (idTypeProd == 2 || idTypeProd == 5 || idTypeProd == 6 || idTypeProd == 7 || idTypeProd == 8) {
                    valueCalculation.setCno2(0);
                    valueCalculation.setCnv2(0);
                    valueCalculation.setCnr2(0);
                }

                XSpreadsheetDocument xSpreadsheetDocument;
                xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
                XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
                Object sheet = xSpreadsheets.getByName("Лист1");
                XSpreadsheet xSpreadsheet;
                xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
                XCell xCell;

//----------------Заполнение шапки документа
                String[] lastName = dao.getLastName();
                xCell = xSpreadsheet.getCellByPosition(1, 3);
                xCell.setFormula("Дата  " + etDate.getText().trim());
                // Должность зама
                xCell = xSpreadsheet.getCellByPosition(2, 2);
                xCell.setFormula(lastName[5].trim());
                //Зам
                xCell = xSpreadsheet.getCellByPosition(6, 2);
                xCell.setFormula("    " + lastName[2].trim());

                // ПЭО должность
                xCell = xSpreadsheet.getCellByPosition(1, 24);
                xCell.setFormula(lastName[4].trim());
                // ПЭО
                xCell = xSpreadsheet.getCellByPosition(3, 24);
                xCell.setFormula("    " + lastName[1].trim());

                if (valueCalculation.getNumberPrice() != 0) {
                    xCell = xSpreadsheet.getCellByPosition(2, 6);
                    xCell.setFormula(parsePriceNumber(dao.getPriceName(valueCalculation.getNumberPrice())) + "");
                }

                xCell = xSpreadsheet.getCellByPosition(column, 8);
                xCell.setFormula(valueCalculation.getNiz());
                xCell = xSpreadsheet.getCellByPosition(column, 9);
                xCell.setFormula(valueCalculation.getNiz1());
                xCell = xSpreadsheet.getCellByPosition(column, 10);
                xCell.setFormula(valueCalculation.getFas());
                xCell = xSpreadsheet.getCellByPosition(column, 11);
                xCell.setFormula(valueCalculation.getPol());
                xCell = xSpreadsheet.getCellByPosition(column, 12);
                xCell.setFormula(valueCalculation.getNar());
                xCell = xSpreadsheet.getCellByPosition(column, 13);
                xCell.setFormula("Размер " + valueCalculation.getRzmn() + "-" + valueCalculation.getRzmk());

                xCell = xSpreadsheet.getCellByPosition(column + 1, 14);
                xCell.setFormula((String.format("%.4f", valueCalculation.getSm()).replace(',', '.'))
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getTe())).replace(',', '.')
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getVm())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 15);
                xCell.setFormula((String.format("%.4f", valueCalculation.getUsto())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpd())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo() + valueCalculation.getZpd()).replace(',', '.')) + "\n"
                        + (String.format("%.4f", valueCalculation.getSn())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPrr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPss())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getHzr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getKmr())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 16);
                xCell.setFormula(String.format("%.4f", valueCalculation.getCc()).replace(',', '.') + "\n");

                column += 2;

            }

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (WrappedTargetException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param currentDocument
     * @param id
     */
    public void reportSeb(XComponent currentDocument, int id) {
        ValueCalculation valueCalculation;
        int column = 1;
        String temp = "";

        try {

            valueCalculation = dao.getDataCalculation(id);

            idTypeProd = dao.getTypeProdByGroup(Integer.parseInt(valueCalculation.getGrup()));
            if (idTypeProd == 2 || idTypeProd == 5 || idTypeProd == 6 || idTypeProd == 7 || idTypeProd == 8) {
                valueCalculation.setCno2(0);
                valueCalculation.setCnv2(0);
                valueCalculation.setCnr2(0);
            }

            XSpreadsheetDocument xSpreadsheetDocument;
            xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet;
            xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell;

//----------------Заполнение шапки документа
            String[] lastName = dao.getLastName();
            xCell = xSpreadsheet.getCellByPosition(1, 3);
            xCell.setFormula("Дата  " + etDate.getText().trim());
            // Должность зама
            xCell = xSpreadsheet.getCellByPosition(2, 2);
            xCell.setFormula(lastName[5].trim());
            //Зам
            xCell = xSpreadsheet.getCellByPosition(6, 2);
            xCell.setFormula("    " + lastName[2].trim());

            // ПЭО должность
            xCell = xSpreadsheet.getCellByPosition(1, 24);
            xCell.setFormula(lastName[4].trim());
            // ПЭО
            xCell = xSpreadsheet.getCellByPosition(3, 24);
            xCell.setFormula("    " + lastName[1].trim());

            if (valueCalculation.getNumberPrice() != 0) {
                xCell = xSpreadsheet.getCellByPosition(2, 6);
                xCell.setFormula(parsePriceNumber(dao.getPriceName(valueCalculation.getNumberPrice())) + "");
            }

            xCell = xSpreadsheet.getCellByPosition(column, 8);
            xCell.setFormula(valueCalculation.getNiz());
            xCell = xSpreadsheet.getCellByPosition(column, 9);
            xCell.setFormula(valueCalculation.getNiz1());
            xCell = xSpreadsheet.getCellByPosition(column, 10);
            xCell.setFormula(valueCalculation.getFas());
            xCell = xSpreadsheet.getCellByPosition(column, 11);
            xCell.setFormula(valueCalculation.getPol());
            xCell = xSpreadsheet.getCellByPosition(column, 12);
            xCell.setFormula(valueCalculation.getNar());
            xCell = xSpreadsheet.getCellByPosition(column, 13);
            xCell.setFormula("Размер " + valueCalculation.getRzmn() + "-" + valueCalculation.getRzmk());

            xCell = xSpreadsheet.getCellByPosition(column + 1, 14);
            xCell.setFormula((String.format("%.4f", valueCalculation.getSm()).replace(',', '.'))
                    + "\n"
                    + (String.format("%.4f", valueCalculation.getTe())).replace(',', '.')
                    + "\n"
                    + (String.format("%.4f", valueCalculation.getVm())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 15);
            xCell.setFormula((String.format("%.4f", valueCalculation.getUsto())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpo())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpd())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpo() + valueCalculation.getZpd()).replace(',', '.')) + "\n"
                    + (String.format("%.4f", valueCalculation.getSn())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getPrr())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getPss())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getHzr())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getKmr())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 16);
            xCell.setFormula(String.format("%.4f", valueCalculation.getCc()).replace(',', '.') + "\n");

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (WrappedTargetException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param currentDocument
     * @param id
     */
    public void reportSebKf(XComponent currentDocument, List<Integer> id) {
        ValueCalculation valueCalculation;
        int column = 1;
        String temp = "";

        try {

            for (int i : id) {

                valueCalculation = dao.getDataCalculation(i);

                idTypeProd = dao.getTypeProdByGroup(Integer.parseInt(valueCalculation.getGrup()));
                if (idTypeProd == 2 || idTypeProd == 5 || idTypeProd == 6 || idTypeProd == 7 || idTypeProd == 8) {
                    valueCalculation.setCno2(0);
                    valueCalculation.setCnv2(0);
                    valueCalculation.setCnr2(0);
                }

                XSpreadsheetDocument xSpreadsheetDocument;
                xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
                XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
                Object sheet = xSpreadsheets.getByName("Лист1");
                XSpreadsheet xSpreadsheet;
                xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
                XCell xCell;

//----------------Заполнение шапки документа
                String[] lastName = dao.getLastName();
                xCell = xSpreadsheet.getCellByPosition(1, 3);
                xCell.setFormula("Дата  " + etDate.getText().trim());
                // Должность зама
                xCell = xSpreadsheet.getCellByPosition(2, 2);
                xCell.setFormula(lastName[5].trim());
                //Зам
                xCell = xSpreadsheet.getCellByPosition(6, 2);
                xCell.setFormula("    " + lastName[2].trim());

                // ПЭО должность
                xCell = xSpreadsheet.getCellByPosition(1, 24);
                xCell.setFormula(lastName[4].trim());
                // ПЭО
                xCell = xSpreadsheet.getCellByPosition(3, 24);
                xCell.setFormula("    " + lastName[1].trim());

                if (valueCalculation.getNumberPrice() != 0) {
                    xCell = xSpreadsheet.getCellByPosition(2, 6);
                    xCell.setFormula(parsePriceNumber(dao.getPriceName(valueCalculation.getNumberPrice())) + "");
                }

                xCell = xSpreadsheet.getCellByPosition(column, 8);
                xCell.setFormula(valueCalculation.getNiz());
                xCell = xSpreadsheet.getCellByPosition(column, 9);
                xCell.setFormula(valueCalculation.getNiz1());
                xCell = xSpreadsheet.getCellByPosition(column, 10);
                xCell.setFormula(valueCalculation.getFas());
                xCell = xSpreadsheet.getCellByPosition(column, 11);
                xCell.setFormula(valueCalculation.getPol());
                xCell = xSpreadsheet.getCellByPosition(column, 12);
                xCell.setFormula(valueCalculation.getNar());
                xCell = xSpreadsheet.getCellByPosition(column, 13);
                xCell.setFormula("Размер " + valueCalculation.getRzmn() + "-" + valueCalculation.getRzmk());

                xCell = xSpreadsheet.getCellByPosition(column + 1, 14);
                xCell.setFormula((String.format("%.4f", valueCalculation.getSm()).replace(',', '.'))
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getTe())).replace(',', '.')
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getVm())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column, 15);
                xCell.setFormula("\n\n\n\n34.00%"
                        + "\n"
                        + (String.format("%.2f", valueCalculation.getPrrp())).replace(',', '.') + "%"
                        + "\n\n"
                        + (String.format("%.2f", valueCalculation.getHzrp())).replace(',', '.') + "%"
                        + "\n"
                        + (String.format("%.2f", valueCalculation.getKmrp())).replace(',', '.') + "%");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 15);
                xCell.setFormula((String.format("%.4f", valueCalculation.getUsto())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpd())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo() + valueCalculation.getZpd()).replace(',', '.')) + "\n"
                        + (String.format("%.4f", valueCalculation.getSn())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPrr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPss())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getHzr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getKmr())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 16);
                xCell.setFormula(String.format("%.4f", valueCalculation.getCc()).replace(',', '.') + "\n");

                column += 2;

            }

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (WrappedTargetException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param currentDocument
     * @param id
     */
    public void reportSebKf(XComponent currentDocument, int id) {
        ValueCalculation valueCalculation;
        int column = 1;
        String temp = "";

        try {

            valueCalculation = dao.getDataCalculation(id);

            idTypeProd = dao.getTypeProdByGroup(Integer.parseInt(valueCalculation.getGrup()));
            if (idTypeProd == 2 || idTypeProd == 5 || idTypeProd == 6 || idTypeProd == 7 || idTypeProd == 8) {
                valueCalculation.setCno2(0);
                valueCalculation.setCnv2(0);
                valueCalculation.setCnr2(0);
            }

            XSpreadsheetDocument xSpreadsheetDocument;
            xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet;
            xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell;

//----------------Заполнение шапки документа
            String[] lastName = dao.getLastName();
            xCell = xSpreadsheet.getCellByPosition(1, 3);
            xCell.setFormula("Дата  " + etDate.getText().trim());
            // Должность зама
            xCell = xSpreadsheet.getCellByPosition(2, 2);
            xCell.setFormula(lastName[5].trim());
            //Зам
            xCell = xSpreadsheet.getCellByPosition(6, 2);
            xCell.setFormula("    " + lastName[2].trim());

            // ПЭО должность
            xCell = xSpreadsheet.getCellByPosition(1, 24);
            xCell.setFormula(lastName[4].trim());
            // ПЭО
            xCell = xSpreadsheet.getCellByPosition(3, 24);
            xCell.setFormula("    " + lastName[1].trim());

            if (valueCalculation.getNumberPrice() != 0) {
                xCell = xSpreadsheet.getCellByPosition(2, 6);
                xCell.setFormula(parsePriceNumber(dao.getPriceName(valueCalculation.getNumberPrice())) + "");
            }

            xCell = xSpreadsheet.getCellByPosition(column, 8);
            xCell.setFormula(valueCalculation.getNiz());
            xCell = xSpreadsheet.getCellByPosition(column, 9);
            xCell.setFormula(valueCalculation.getNiz1());
            xCell = xSpreadsheet.getCellByPosition(column, 10);
            xCell.setFormula(valueCalculation.getFas());
            xCell = xSpreadsheet.getCellByPosition(column, 11);
            xCell.setFormula(valueCalculation.getPol());
            xCell = xSpreadsheet.getCellByPosition(column, 12);
            xCell.setFormula(valueCalculation.getNar());
            xCell = xSpreadsheet.getCellByPosition(column, 13);
            xCell.setFormula("Размер " + valueCalculation.getRzmn() + "-" + valueCalculation.getRzmk());

            xCell = xSpreadsheet.getCellByPosition(column + 1, 14);
            xCell.setFormula((String.format("%.4f", valueCalculation.getSm()).replace(',', '.'))
                    + "\n"
                    + (String.format("%.4f", valueCalculation.getTe())).replace(',', '.')
                    + "\n"
                    + (String.format("%.4f", valueCalculation.getVm())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column, 15);
            xCell.setFormula("\n\n\n\n34.00%"
                    + "\n"
                    + (String.format("%.2f", valueCalculation.getPrrp())).replace(',', '.') + "%"
                    + "\n\n"
                    + (String.format("%.2f", valueCalculation.getHzrp())).replace(',', '.') + "%"
                    + "\n"
                    + (String.format("%.2f", valueCalculation.getKmrp())).replace(',', '.') + "%");

            xCell = xSpreadsheet.getCellByPosition(column + 1, 15);
            xCell.setFormula((String.format("%.4f", valueCalculation.getUsto())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpo())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpd())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpo() + valueCalculation.getZpd()).replace(',', '.')) + "\n"
                    + (String.format("%.4f", valueCalculation.getSn())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getPrr())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getPss())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getHzr())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getKmr())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 16);
            xCell.setFormula(String.format("%.4f", valueCalculation.getCc()).replace(',', '.') + "\n");

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (WrappedTargetException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта по себестоимости", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param currentDocument
     * @param id
     */
    public void reportCenKf(XComponent currentDocument, List<Integer> id) {
        ValueCalculation valueCalculation;
        int column = 1;
        String temp = "";

        try {

            for (int i : id) {

                valueCalculation = dao.getDataCalculation(i);

                idTypeProd = dao.getTypeProdByGroup(Integer.parseInt(valueCalculation.getGrup()));
                if (idTypeProd == 2 || idTypeProd == 5 || idTypeProd == 6 || idTypeProd == 7 || idTypeProd == 8) {
                    valueCalculation.setCno2(0);
                    valueCalculation.setCnv2(0);
                    valueCalculation.setCnr2(0);
                }

                XSpreadsheetDocument xSpreadsheetDocument;
                xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
                XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
                Object sheet = xSpreadsheets.getByName("Лист1");
                XSpreadsheet xSpreadsheet;
                xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
                XCell xCell;

//----------------Заполнение шапки документа
                String[] lastName = dao.getLastName();
                xCell = xSpreadsheet.getCellByPosition(1, 3);
                xCell.setFormula("Дата  " + etDate.getText().trim());

                // Должность зама
                xCell = xSpreadsheet.getCellByPosition(2, 2);
                xCell.setFormula(lastName[5].trim());
                //Зам
                xCell = xSpreadsheet.getCellByPosition(6, 2);
                xCell.setFormula("    " + lastName[2].trim());

                // ПЭО должность
                xCell = xSpreadsheet.getCellByPosition(1, 24);
                xCell.setFormula(lastName[4].trim());
                // ПЭО
                xCell = xSpreadsheet.getCellByPosition(3, 24);
                xCell.setFormula("    " + lastName[1].trim());

                if (valueCalculation.getNumberPrice() != 0) {
                    xCell = xSpreadsheet.getCellByPosition(2, 6);
                    xCell.setFormula(parsePriceNumber(dao.getPriceName(valueCalculation.getNumberPrice())) + "");
                }

                xCell = xSpreadsheet.getCellByPosition(column, 8);
                xCell.setFormula(valueCalculation.getNiz());
                xCell = xSpreadsheet.getCellByPosition(column, 9);
                xCell.setFormula(valueCalculation.getNiz1());
                xCell = xSpreadsheet.getCellByPosition(column, 10);
                xCell.setFormula(valueCalculation.getFas());
                xCell = xSpreadsheet.getCellByPosition(column, 11);
                xCell.setFormula(valueCalculation.getPol());
                xCell = xSpreadsheet.getCellByPosition(column, 12);
                xCell.setFormula(valueCalculation.getNar());
                xCell = xSpreadsheet.getCellByPosition(column, 13);
                xCell.setFormula("Размер " + valueCalculation.getRzmn() + "-" + valueCalculation.getRzmk());

                xCell = xSpreadsheet.getCellByPosition(column + 1, 14);
                xCell.setFormula((String.format("%.4f", valueCalculation.getSm()).replace(',', '.'))
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getTe())).replace(',', '.')
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getVm())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column, 15);
                xCell.setFormula("\n\n\n\n34.00%"
                        + "\n"
                        + (String.format("%.2f", valueCalculation.getPrrp())).replace(',', '.') + "%"
                        + "\n\n"
                        + (String.format("%.2f", valueCalculation.getHzrp())).replace(',', '.') + "%"
                        + "\n"
                        + (String.format("%.2f", valueCalculation.getKmrp())).replace(',', '.') + "%");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 15);
                xCell.setFormula((String.format("%.4f", valueCalculation.getUsto())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpd())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo() + valueCalculation.getZpd()).replace(',', '.')) + "\n"
                        + (String.format("%.4f", valueCalculation.getSn())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPrr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPss())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getHzr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getKmr())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 16);
                xCell.setFormula(String.format("%.4f", valueCalculation.getCc()).replace(',', '.') + "\n");

                xCell = xSpreadsheet.getCellByPosition(column, 17);
                xCell.setFormula(String.format("%.2f", valueCalculation.getPrbp()).replace(',', '.') + "%" + "\n");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 17);
                xCell.setFormula(String.format("%.4f", valueCalculation.getPrb()).replace(',', '.') + "\n");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 18);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCno())).replace(',', '.') + "\n"
                        + (String.format("%.2f", valueCalculation.getCno2())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 19);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCnv())).replace(',', '.') + "\n"
                        + (String.format("%.2f", valueCalculation.getCnv2())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 20);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCnr())).replace(',', '.') + "\n"
                        + (String.format("%.2f", valueCalculation.getCnr2())).replace(',', '.'));

                column += 2;

            }

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (WrappedTargetException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
//----------------Заполнение тела документа

//----------------Заполнение тела документа
    }

    public void reportCenKfNew(XComponent currentDocument, List<Integer> id, boolean addCoefficients) {
        ValueCalculation valueCalculation;
        int column = 1;
        String temp = "";

        try {

            for (int i : id) {

                valueCalculation = dao.getDataCalculation(i);

                idTypeProd = dao.getTypeProdByGroup(Integer.parseInt(valueCalculation.getGrup()));
                if (idTypeProd == 2 || idTypeProd == 5 || idTypeProd == 6 || idTypeProd == 7 || idTypeProd == 8) {
                    valueCalculation.setCno2(0);
                    valueCalculation.setCnv2(0);
                    valueCalculation.setCnr2(0);
                }

                XSpreadsheetDocument xSpreadsheetDocument;
                xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
                XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
                Object sheet = xSpreadsheets.getByName("Лист1");
                XSpreadsheet xSpreadsheet;
                xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
                XCell xCell;

//----------------Заполнение шапки документа
                String[] lastName = dao.getLastName();
                xCell = xSpreadsheet.getCellByPosition(1, 3);
                xCell.setFormula("Дата  " + etDate.getText().trim());

                // Должность зама
                xCell = xSpreadsheet.getCellByPosition(2, 2);
                xCell.setFormula(lastName[5].trim());
                //Зам
                xCell = xSpreadsheet.getCellByPosition(6, 2);
                xCell.setFormula("    " + lastName[2].trim());

                // ПЭО должность
                xCell = xSpreadsheet.getCellByPosition(1, 28);
                xCell.setFormula(lastName[4].trim());
                // ПЭО
                xCell = xSpreadsheet.getCellByPosition(3, 28);
                xCell.setFormula("    " + lastName[1].trim());

                if (valueCalculation.getNumberPrice() != 0) {
                    xCell = xSpreadsheet.getCellByPosition(2, 6);
                    xCell.setFormula(parsePriceNumber(dao.getPriceName(valueCalculation.getNumberPrice())) + "");
                }

                xCell = xSpreadsheet.getCellByPosition(column, 8);
                xCell.setFormula(valueCalculation.getNiz());
                xCell = xSpreadsheet.getCellByPosition(column, 9);
                xCell.setFormula(valueCalculation.getNiz1());
                xCell = xSpreadsheet.getCellByPosition(column, 10);
                xCell.setFormula(valueCalculation.getFas());
                xCell = xSpreadsheet.getCellByPosition(column, 11);
                xCell.setFormula(valueCalculation.getPol());
                xCell = xSpreadsheet.getCellByPosition(column, 12);
                xCell.setFormula(valueCalculation.getNar());
                xCell = xSpreadsheet.getCellByPosition(column, 13);
                xCell.setFormula("Размер " + valueCalculation.getRzmn() + "-" + valueCalculation.getRzmk());

                xCell = xSpreadsheet.getCellByPosition(column + 1, 14);
                xCell.setFormula((String.format("%.4f", valueCalculation.getSm()).replace(',', '.'))
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getTe())).replace(',', '.')
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getVm())).replace(',', '.'));

                if (addCoefficients) {
                    xCell = xSpreadsheet.getCellByPosition(column, 15);
                    xCell.setFormula("\n\n\n\n34.00%"
                            + "\n"
                            + (String.format("%.2f", valueCalculation.getPrrp())).replace(',', '.') + "%"
                            + "\n\n"
                            + (String.format("%.2f", valueCalculation.getHzrp())).replace(',', '.') + "%"
                            + "\n"
                            + (String.format("%.2f", valueCalculation.getKmrp())).replace(',', '.') + "%");

                    xCell = xSpreadsheet.getCellByPosition(column, 21);
                    xCell.setFormula(String.format("%.2f", valueCalculation.getPercentCredit()).replace(',', '.') + "%" + "\n");

                    xCell = xSpreadsheet.getCellByPosition(column, 23);
                    xCell.setFormula(String.format("%.2f", (valueCalculation.getCno() - valueCalculation.getPrimeCostCredit())
                            / valueCalculation.getPrimeCostCredit()
                            * 100).replace(',', '.') + "%" + "\n");

                    xCell = xSpreadsheet.getCellByPosition(column, 17);
                    xCell.setFormula(String.format("%.2f", valueCalculation.getPrbp()).replace(',', '.') + "%" + "\n");
                }

                xCell = xSpreadsheet.getCellByPosition(column + 1, 15);
                xCell.setFormula((String.format("%.4f", valueCalculation.getUsto())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpd())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo() + valueCalculation.getZpd()).replace(',', '.')) + "\n"
                        + (String.format("%.4f", valueCalculation.getSn())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPrr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPss())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getHzr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getKmr())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 16);
                xCell.setFormula(String.format("%.4f", valueCalculation.getCc()).replace(',', '.') + "\n");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 17);
                xCell.setFormula(String.format("%.4f", valueCalculation.getPrb()).replace(',', '.') + "\n");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 18);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCno())).replace(',', '.')
                        .replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 19);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCnr())).replace(',', '.')
                        .replace(',', '.'));


                xCell = xSpreadsheet.getCellByPosition(column + 1, 21);
                xCell.setFormula(String.format("%.4f", valueCalculation.getValueCredit()).replace(',', '.') + "\n");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 22);
                xCell.setFormula(String.format("%.4f", valueCalculation.getPrimeCostCredit()).replace(',', '.') + "\n");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 23);
                xCell.setFormula(String.format("%.4f", valueCalculation.getProfitCredit()).replace(',', '.') + "\n");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 24);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCno())).replace(',', '.')
                        .replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 25);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCnr())).replace(',', '.')
                        .replace(',', '.'));

                column += 2;

            }

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (WrappedTargetException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
//----------------Заполнение тела документа

//----------------Заполнение тела документа
    }

    /**
     * @param currentDocument
     * @param id
     */
    public void reportCenKf(XComponent currentDocument, int id) {
        ValueCalculation valueCalculation;
        int column = 1;
        String temp = "";

        try {

            valueCalculation = dao.getDataCalculation(id);

            idTypeProd = dao.getTypeProdByGroup(Integer.parseInt(valueCalculation.getGrup()));
            if (idTypeProd == 2 || idTypeProd == 5 || idTypeProd == 6 || idTypeProd == 7 || idTypeProd == 8) {
                valueCalculation.setCno2(0);
                valueCalculation.setCnv2(0);
                valueCalculation.setCnr2(0);
            }

            XSpreadsheetDocument xSpreadsheetDocument;
            xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet;
            xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell;

//----------------Заполнение шапки документа
            String[] lastName = dao.getLastName();
            xCell = xSpreadsheet.getCellByPosition(1, 3);
            xCell.setFormula("Дата  " + etDate.getText().trim());
            // Должность зама
            xCell = xSpreadsheet.getCellByPosition(2, 2);
            xCell.setFormula(lastName[5].trim());
            //Зам
            xCell = xSpreadsheet.getCellByPosition(6, 2);
            xCell.setFormula("    " + lastName[2].trim());

            // ПЭО должность
            xCell = xSpreadsheet.getCellByPosition(1, 24);
            xCell.setFormula(lastName[4].trim());
            // ПЭО
            xCell = xSpreadsheet.getCellByPosition(3, 24);
            xCell.setFormula("    " + lastName[1].trim());

            if (valueCalculation.getNumberPrice() != 0) {
                xCell = xSpreadsheet.getCellByPosition(2, 6);
                xCell.setFormula(parsePriceNumber(dao.getPriceName(valueCalculation.getNumberPrice())) + "");
            }

            xCell = xSpreadsheet.getCellByPosition(column, 8);
            xCell.setFormula(valueCalculation.getNiz());
            xCell = xSpreadsheet.getCellByPosition(column, 9);
            xCell.setFormula(valueCalculation.getNiz1());
            xCell = xSpreadsheet.getCellByPosition(column, 10);
            xCell.setFormula(valueCalculation.getFas());
            xCell = xSpreadsheet.getCellByPosition(column, 11);
            xCell.setFormula(valueCalculation.getPol());
            xCell = xSpreadsheet.getCellByPosition(column, 12);
            xCell.setFormula(valueCalculation.getNar());
            xCell = xSpreadsheet.getCellByPosition(column, 13);
            xCell.setFormula("Размер " + valueCalculation.getRzmn() + "-" + valueCalculation.getRzmk());

            xCell = xSpreadsheet.getCellByPosition(column + 1, 14);
            xCell.setFormula((String.format("%.4f", valueCalculation.getSm()).replace(',', '.'))
                    + "\n"
                    + (String.format("%.4f", valueCalculation.getTe())).replace(',', '.')
                    + "\n"
                    + (String.format("%.4f", valueCalculation.getVm())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column, 15);
            xCell.setFormula("\n\n\n\n34.00%"
                    + "\n"
                    + (String.format("%.2f", valueCalculation.getPrrp())).replace(',', '.') + "%"
                    + "\n\n"
                    + (String.format("%.2f", valueCalculation.getHzrp())).replace(',', '.') + "%"
                    + "\n"
                    + (String.format("%.2f", valueCalculation.getKmrp())).replace(',', '.') + "%");

            xCell = xSpreadsheet.getCellByPosition(column + 1, 15);
            xCell.setFormula((String.format("%.4f", valueCalculation.getUsto())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpo())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpd())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpo() + valueCalculation.getZpd()).replace(',', '.')) + "\n"
                    + (String.format("%.4f", valueCalculation.getSn())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getPrr())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getPss())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getHzr())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getKmr())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 16);
            xCell.setFormula(String.format("%.4f", valueCalculation.getCc()).replace(',', '.') + "\n");

            xCell = xSpreadsheet.getCellByPosition(column, 17);
            xCell.setFormula(String.format("%.2f", valueCalculation.getPrbp()).replace(',', '.') + "%" + "\n");
            System.out.println(String.format("%.2f", valueCalculation.getPrbp()).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 17);
            xCell.setFormula(String.format("%.4f", valueCalculation.getPrb()).replace(',', '.') + "\n");

            xCell = xSpreadsheet.getCellByPosition(column + 1, 18);
            xCell.setFormula((String.format("%.2f", valueCalculation.getCno())).replace(',', '.') + "\n"
                    + (String.format("%.2f", valueCalculation.getCno2())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 19);
            xCell.setFormula((String.format("%.2f", valueCalculation.getCnv())).replace(',', '.') + "\n"
                    + (String.format("%.2f", valueCalculation.getCnv2())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 20);
            xCell.setFormula((String.format("%.2f", valueCalculation.getCnr())).replace(',', '.') + "\n"
                    + (String.format("%.2f", valueCalculation.getCnr2())).replace(',', '.'));

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (WrappedTargetException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
//----------------Заполнение тела документа

//----------------Заполнение тела документа
    }

    /**
     * @param currentDocument
     * @param id
     */
    public void reportCen(XComponent currentDocument, List<Integer> id) {

        ValueCalculation valueCalculation;
        int column = 1;
        String temp = "";

        try {

            for (int i : id) {

                valueCalculation = dao.getDataCalculation(i);

                idTypeProd = dao.getTypeProdByGroup(Integer.parseInt(valueCalculation.getGrup()));
                if (idTypeProd == 2 || idTypeProd == 5 || idTypeProd == 6 || idTypeProd == 7 || idTypeProd == 8) {
                    valueCalculation.setCno2(0);
                    valueCalculation.setCnv2(0);
                    valueCalculation.setCnr2(0);
                }

                XSpreadsheetDocument xSpreadsheetDocument;
                xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
                XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
                Object sheet = xSpreadsheets.getByName("Лист1");
                XSpreadsheet xSpreadsheet;
                xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
                XCell xCell;

//----------------Заполнение шапки документа
                String[] lastName = dao.getLastName();
                xCell = xSpreadsheet.getCellByPosition(1, 3);
                xCell.setFormula("Дата  " + etDate.getText().trim());
                // Должность зама
                xCell = xSpreadsheet.getCellByPosition(2, 2);
                xCell.setFormula(lastName[5].trim());
                //Зам
                xCell = xSpreadsheet.getCellByPosition(6, 2);
                xCell.setFormula("    " + lastName[2].trim());

                // ПЭО должность
                xCell = xSpreadsheet.getCellByPosition(1, 24);
                xCell.setFormula(lastName[4].trim());
                // ПЭО
                xCell = xSpreadsheet.getCellByPosition(3, 24);
                xCell.setFormula("    " + lastName[1].trim());

                if (valueCalculation.getNumberPrice() != 0) {
                    xCell = xSpreadsheet.getCellByPosition(2, 6);
                    xCell.setFormula(parsePriceNumber(dao.getPriceName(valueCalculation.getNumberPrice())) + "");
                }

                xCell = xSpreadsheet.getCellByPosition(column, 8);
                xCell.setFormula(valueCalculation.getNiz());
                xCell = xSpreadsheet.getCellByPosition(column, 9);
                xCell.setFormula(valueCalculation.getNiz1());
                xCell = xSpreadsheet.getCellByPosition(column, 10);
                xCell.setFormula(valueCalculation.getFas());
                xCell = xSpreadsheet.getCellByPosition(column, 11);
                xCell.setFormula(valueCalculation.getPol());
                xCell = xSpreadsheet.getCellByPosition(column, 12);
                xCell.setFormula(valueCalculation.getNar());
                xCell = xSpreadsheet.getCellByPosition(column, 13);
                xCell.setFormula("Размер " + valueCalculation.getRzmn() + "-" + valueCalculation.getRzmk());

                xCell = xSpreadsheet.getCellByPosition(column + 1, 14);
                xCell.setFormula((String.format("%.4f", valueCalculation.getSm()).replace(',', '.'))
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getTe())).replace(',', '.')
                        + "\n"
                        + (String.format("%.4f", valueCalculation.getVm())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 15);
                xCell.setFormula((String.format("%.4f", valueCalculation.getUsto())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpd())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getZpo() + valueCalculation.getZpd()).replace(',', '.')) + "\n"
                        + (String.format("%.4f", valueCalculation.getSn())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPrr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getPss())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getHzr())).replace(',', '.') + "\n"
                        + (String.format("%.4f", valueCalculation.getKmr())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 16);
                xCell.setFormula(String.format("%.4f", valueCalculation.getCc()).replace(',', '.') + "\n");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 17);
                xCell.setFormula(String.format("%.4f", valueCalculation.getPrb()).replace(',', '.') + "\n");

                xCell = xSpreadsheet.getCellByPosition(column + 1, 18);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCno())).replace(',', '.') + "\n"
                        + (String.format("%.2f", valueCalculation.getCno2())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 19);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCnv())).replace(',', '.') + "\n"
                        + (String.format("%.2f", valueCalculation.getCnv2())).replace(',', '.'));

                xCell = xSpreadsheet.getCellByPosition(column + 1, 20);
                xCell.setFormula((String.format("%.2f", valueCalculation.getCnr())).replace(',', '.') + "\n"
                        + (String.format("%.2f", valueCalculation.getCnr2())).replace(',', '.'));

                column += 2;

            }

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (WrappedTargetException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param currentDocument
     * @param id
     */
    public void reportCen(XComponent currentDocument, int id) {

        ValueCalculation valueCalculation;
        int column = 1;
        String temp = "";

        try {

            valueCalculation = dao.getDataCalculation(id);

            idTypeProd = dao.getTypeProdByGroup(Integer.parseInt(valueCalculation.getGrup()));
            if (idTypeProd == 2 || idTypeProd == 5 || idTypeProd == 6 || idTypeProd == 7 || idTypeProd == 8) {
                valueCalculation.setCno2(0);
                valueCalculation.setCnv2(0);
                valueCalculation.setCnr2(0);
            }

            XSpreadsheetDocument xSpreadsheetDocument;
            xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet;
            xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell;

//----------------Заполнение шапки документа
            String[] lastName = dao.getLastName();
            xCell = xSpreadsheet.getCellByPosition(1, 3);
            xCell.setFormula("Дата  " + etDate.getText().trim());
            // Должность зама
            xCell = xSpreadsheet.getCellByPosition(2, 2);
            xCell.setFormula(lastName[5].trim());
            //Зам
            xCell = xSpreadsheet.getCellByPosition(6, 2);
            xCell.setFormula("    " + lastName[2].trim());

            // ПЭО должность
            xCell = xSpreadsheet.getCellByPosition(1, 24);
            xCell.setFormula(lastName[4].trim());
            // ПЭО
            xCell = xSpreadsheet.getCellByPosition(3, 24);
            xCell.setFormula("    " + lastName[1].trim());

            if (valueCalculation.getNumberPrice() != 0) {
                xCell = xSpreadsheet.getCellByPosition(2, 6);
                xCell.setFormula(parsePriceNumber(dao.getPriceName(valueCalculation.getNumberPrice())) + "");
            }

            xCell = xSpreadsheet.getCellByPosition(column, 8);
            xCell.setFormula(valueCalculation.getNiz());
            xCell = xSpreadsheet.getCellByPosition(column, 9);
            xCell.setFormula(valueCalculation.getNiz1());
            xCell = xSpreadsheet.getCellByPosition(column, 10);
            xCell.setFormula(valueCalculation.getFas());
            xCell = xSpreadsheet.getCellByPosition(column, 11);
            xCell.setFormula(valueCalculation.getPol());
            xCell = xSpreadsheet.getCellByPosition(column, 12);
            xCell.setFormula(valueCalculation.getNar());
            xCell = xSpreadsheet.getCellByPosition(column, 13);
            xCell.setFormula("Размер " + valueCalculation.getRzmn() + "-" + valueCalculation.getRzmk());

            xCell = xSpreadsheet.getCellByPosition(column + 1, 14);
            xCell.setFormula((String.format("%.4f", valueCalculation.getSm()).replace(',', '.'))
                    + "\n"
                    + (String.format("%.4f", valueCalculation.getTe())).replace(',', '.')
                    + "\n"
                    + (String.format("%.4f", valueCalculation.getVm())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 15);
            xCell.setFormula((String.format("%.4f", valueCalculation.getUsto())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpo())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpd())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getZpo() + valueCalculation.getZpd()).replace(',', '.')) + "\n"
                    + (String.format("%.4f", valueCalculation.getSn())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getPrr())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getPss())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getHzr())).replace(',', '.') + "\n"
                    + (String.format("%.4f", valueCalculation.getKmr())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 16);
            xCell.setFormula(String.format("%.4f", valueCalculation.getCc()).replace(',', '.') + "\n");

            xCell = xSpreadsheet.getCellByPosition(column + 1, 17);
            xCell.setFormula(String.format("%.4f", valueCalculation.getPrb()).replace(',', '.') + "\n");

            xCell = xSpreadsheet.getCellByPosition(column + 1, 18);
            xCell.setFormula((String.format("%.2f", valueCalculation.getCno())).replace(',', '.') + "\n"
                    + (String.format("%.2f", valueCalculation.getCno2())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 19);
            xCell.setFormula((String.format("%.2f", valueCalculation.getCnv())).replace(',', '.') + "\n"
                    + (String.format("%.2f", valueCalculation.getCnv2())).replace(',', '.'));

            xCell = xSpreadsheet.getCellByPosition(column + 1, 20);
            xCell.setFormula((String.format("%.2f", valueCalculation.getCnr())).replace(',', '.') + "\n"
                    + (String.format("%.2f", valueCalculation.getCnr2())).replace(',', '.'));

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (WrappedTargetException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } //----------------Заполнение тела документа
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при заполнении отчёта Отпускные цены", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }

    }
    // End of variables declaration//GEN-END:variables

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the dao
     */
    public PricePDB getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(PricePDB dao) {
        this.dao = dao;
    }

    /**
     * @return the listId
     */
    public List<Integer> getListId() {
        return listId;
    }

    /**
     * @param listId the listId to set
     */
    public void setListId(List<Integer> listId) {
        this.listId = listId;
    }

    /**
     * @return the bool
     */
    public boolean isBool() {
        return bool;
    }

    /**
     * @param bool the bool to set
     */
    public void setBool(boolean bool) {
        this.bool = bool;
    }

    /**
     * @return the sels
     */
    public PrintForm getSelf() {
        return self;
    }

    /**
     * @param sels the sels to set
     */
    public void setSelf(PrintForm sels) {
        this.self = sels;
    }

    @Override
    public void dispose() {
        super.dispose();
        ppdb.disConn();
    }
}
