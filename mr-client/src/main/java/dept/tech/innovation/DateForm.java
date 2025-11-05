package dept.tech.innovation;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.march8.api.utils.DatePeriod;
import com.sun.star.awt.FontWeight;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;
import common.PanelWihtFone;
import common.ProgressBar;
import common.UtilFunctions;
import dept.MyReportsModule;
import workOO.OO_new;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vova
 * @date 15.11.2011
 */
@SuppressWarnings("all")
public class DateForm extends JDialog {
    public static Vector forSort;
    final DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
    public boolean repFlag = false;
    MaskFormatter formatter;
    MaskFormatter formatter2;
    JDialog th;
    ResultSet itemsInfo = null;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private PanelWihtFone mainPanel;
    private UCDatePicker dateChooser;
    private UCDatePicker dateChooser2;
    private ProgressBar pb;

    public DateForm(JDialog parent, boolean modal, DatePeriod period) {
        super(parent, modal);
        th = this;
        initComponents();
        dateChooser.setDate(period.getBegin());
        dateChooser2.setDate(period.getEnd());
        this.setTitle("Период просмотра");

        add(mainPanel);
        setSize(320, 190);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public DateForm(JDialog parent, boolean modal, boolean trade) {
        super(parent, modal);
        th = this;
        repFlag = trade;
        initComponents();
        this.setTitle("Период просмотра");

        add(mainPanel);
        setSize(320, 190);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new PanelWihtFone();

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        Calendar c = Calendar.getInstance();
        java.util.Date d = c.getTime();
        d.setDate(1);

        dateChooser = new UCDatePicker(d);
        dateChooser.setBounds(120, 50, 100, 20);
        mainPanel.add(dateChooser);

        dateChooser2 = new UCDatePicker(c.getTime());
        dateChooser2.setBounds(120, 80, 100, 20);
        mainPanel.add(dateChooser2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Данные по инновационным моделям");
        jLabel1.setBounds(20, 20, 300, 20);
        mainPanel.add(jLabel1);

        jLabel2.setText("с");
        jLabel2.setBounds(100, 50, 20, 20);
        mainPanel.add(jLabel2);

        jLabel3.setText("по");
        jLabel3.setBounds(100, 80, 20, 20);
        mainPanel.add(jLabel3);

        jButton1.setText("Печать");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (UtilFunctions.checkDate(df.format(dateChooser.getDate())) && UtilFunctions.checkDate(df.format(dateChooser2.getDate()))) {

                        pb = new ProgressBar(th, false, "Сбор данных");
                        SwingWorker sw;
                        sw = new SwingWorker() {

                            @Override
                            protected Object doInBackground() throws Exception {

                                ProductionDBF dbf = new ProductionDBF();
                                ProductionDB db = new ProductionDB();

                                int smonth = dateChooser.getDate().getMonth() + 1;
                                int emonth = dateChooser2.getDate().getMonth() + 1;

                                int syear = dateChooser.getDate().getYear() + 1900;
                                int eyear = dateChooser2.getDate().getYear() + 1900;

                                String year_path = String.valueOf(syear).trim().substring(2, 4);

                                long sdate = dateChooser.getDate().getTime();
                                long edate = dateChooser2.getDate().getTime();

                                //String sdRep = dateChooser.getDate().getDate()+"."+(dateChooser.getDate().getMonth()+1)+"."+(dateChooser.getDate().getYear()+1900);
                                //String edRep = dateChooser2.getDate().getDate()+"."+(dateChooser2.getDate().getMonth()+1)+"."+(dateChooser2.getDate().getYear()+1900);

                                String sdRep = (String.valueOf(dateChooser.getDate().getDate()).trim().length() == 2 ? String.valueOf(dateChooser.getDate().getDate()).trim() : "0" + String.valueOf(dateChooser.getDate().getDate()).trim()) + "." +
                                        (String.valueOf(dateChooser.getDate().getMonth() + 1).trim().length() == 2 ? String.valueOf(dateChooser.getDate().getMonth() + 1).trim() : "0" + String.valueOf(dateChooser.getDate().getMonth() + 1).trim()) + "." +
                                        (String.valueOf(dateChooser.getDate().getYear() + 1900).trim());

                                String edRep = (String.valueOf(dateChooser2.getDate().getDate()).trim().length() == 2 ? String.valueOf(dateChooser2.getDate().getDate()).trim() : "0" + String.valueOf(dateChooser2.getDate().getDate()).trim()) + "." +
                                        (String.valueOf(dateChooser2.getDate().getMonth() + 1).trim().length() == 2 ? String.valueOf(dateChooser2.getDate().getMonth() + 1).trim() : "0" + String.valueOf(dateChooser2.getDate().getMonth() + 1).trim()) + "." +
                                        (String.valueOf(dateChooser2.getDate().getYear() + 1900).trim());


                                if (syear == eyear) {
                                    if (smonth <= emonth) {
                                        Vector res = new Vector();

                                        /*if(SystemUtils.isWindows()){
                                            MyReportsModule.dbfPlanPath =
                                            MyReportsModule.dbfDVIPath =
                                        }*/

                                        if (SystemUtils.isWindows()) {
                                            MyReportsModule.dbfPlanPath = "//ser01/D$/PLAN/";
                                            MyReportsModule.dbfDVIPath = "//ser01/D$/DVI/";
                                        }


                                        ArrayList plan = preparePlanData(year_path, smonth, emonth, MyReportsModule.dbfPlanPath, "plan", dbf);
                                        ArrayList produced = prepareProductionData(year_path, smonth, emonth, MyReportsModule.dbfDVIPath, "pr_", dbf);
                                        ArrayList export = prepareExportSaleData(year_path, smonth, emonth, MyReportsModule.dbfDVIPath, "ar_o", dbf);

                                        System.out.println(plan.size() + " - " + produced.size() + " - " + export.size());

                                        if (!repFlag) {
                                            // Импортозамещение
                                            res = db.getProductionList(plan, produced, export, sdate, edate);
                                        } else {
                                            // Торговые марки
                                            res = db.getTProductionList(plan, produced, export, sdate, edate);
                                        }

                                        createInnovRepot(sortVectorData(res), db, sdRep, edRep);

                                    } else {
                                        JOptionPane.showMessageDialog(null, "Период задан некорректно!");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Период задан некорректно!");
                                }
                                db.disConn();
                                return 0;
                            }

                            @Override
                            protected void done() {
                                try {
                                    pb.dispose();
                                } catch (Exception ex) {
                                    System.err.println("Ошибка при получении результатов из фонового потока " + ex);
                                }
                            }
                        };
                        sw.execute();
                        pb.setVisible(true);
                        dispose();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка " + e, "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jButton1.setBounds(100, 120, 120, 20);
        mainPanel.add(jButton1);
    }

    private ArrayList preparePlanData(String year, int smonth, int emonth, String dirPath, String dbfName, ProductionDBF dbf) {
        ArrayList list = new ArrayList();
        File file = new File(dirPath);
        File[] files = file.listFiles();
        try {
            for (int i = smonth; i <= emonth; i++) {
                String dbfN = new String();
                if (i >= 10) {
                    dbfN = dbfName + String.valueOf(i).trim();
                } else {
                    if (i < 10) {
                        dbfN = dbfName + "0" + String.valueOf(i).trim();
                    }
                }
                for (int j = 0; j < files.length; j++) {
                    if (files[j].getName().toString().trim().length() == 10) {
                        String fileName = files[j].getName().toString().trim().substring(0, 6).toLowerCase();
                        if (fileName.equals(dbfN)) {
                            String filePath = dirPath + files[j].getName();
                            System.out.println("Читаем плановую ДБФ " + filePath + " : " + filePath.length());
                            list = dbf.readUnite(filePath, list, 1);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(DateForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    private ArrayList prepareProductionData(String year, int smonth, int emonth, String dirPath, String dbfName, ProductionDBF dbf) {
        ArrayList list = new ArrayList();
        dirPath = dirPath + "god" + year + "/";
        File file = new File(dirPath);
        File[] files = file.listFiles();
        try {
            for (int i = smonth; i <= emonth; i++) {
                String dbfN = new String();
                if (i >= 10) {
                    dbfN = dbfName + String.valueOf(i).trim() + "d";
                } else {
                    if (i < 10) {
                        dbfN = dbfName + "0" + String.valueOf(i).trim() + "d";
                    }
                }

                for (int j = 0; j < files.length; j++) {
                    if (files[j].getName().toString().trim().length() == 10) {
                        String fileName = files[j].getName().toString().trim().substring(0, 6).toLowerCase();
                        if (fileName.equals(dbfN)) {
                            String filePath = dirPath + files[j].getName();
                            System.out.println("Читаем производственную ДБФ " + filePath + " : " + filePath.length());
                            list = dbf.readUnite(filePath, list, 2);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DateForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private ArrayList prepareExportSaleData(String year, int smonth, int emonth, String dirPath, String dbfName, ProductionDBF dbf) {
        ArrayList list = new ArrayList();

        dirPath = dirPath + "god" + year + "/";
        File file = new File(dirPath);
        File[] files = file.listFiles();
        try {
            for (int i = smonth; i <= emonth; i++) {
                String dbfN = new String();
                if (i >= 10) {
                    dbfN = dbfName + String.valueOf(i).trim() + "d";
                } else {
                    if (i < 10) {
                        dbfN = dbfName + "0" + String.valueOf(i).trim() + "d";
                    }
                }
                for (int j = 0; j < files.length; j++) {
                    if (files[j].getName().toString().trim().length() == 11) {
                        String fileName = files[j].getName().toString().trim().substring(0, 7).toLowerCase();
                        if (fileName.equals(dbfN)) {
                            String filePath = dirPath + files[j].getName();
                            System.out.println("Читаем экспортную ДБФ " + filePath + " : " + filePath.length());
                            list = dbf.readUnite(filePath, list, 3);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DateForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }


    public Vector sortVectorData(Vector v) {
        forSort = v;
        int startIndex = 0;
        int endIndex = v.size() - 1;
        doSort(startIndex, endIndex);
        v = forSort;
        return v;
    }

    public void doSort(int start, int end) {
        if (start >= end) {
            return;
        }
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            Integer.parseInt(((Vector) forSort.elementAt(i)).get(9).toString());
            while (i < cur && (Integer.parseInt(((Vector) forSort.elementAt(i)).get(9).toString()) <=
                    Integer.parseInt(((Vector) forSort.elementAt(cur)).get(9).toString()))) {
                i++;
            }
            while (j > cur && (Integer.parseInt(((Vector) forSort.elementAt(cur)).get(9).toString()) <=
                    Integer.parseInt(((Vector) forSort.elementAt(j)).get(9).toString()))) {
                j--;
            }
            if (i < j) {
                Vector temp = (Vector) forSort.elementAt(i);
                forSort.setElementAt(forSort.elementAt(j), i);
                forSort.setElementAt(temp, j);
                if (i == cur) {
                    cur = j;
                } else if (j == cur) {
                    cur = i;
                }
            }
        }
        doSort(start, cur);
        doSort(cur + 1, end);
    }

    public void createInnovRepot(Vector data, ProductionDB db, String sD, String eD) {
        try {
            OO_new.connect();
            XComponent document;
            if (!repFlag) {
                document = OO_new.openDocumentOld("Templates/ImportSubstitutingProduction.ots");
            } else {
                document = OO_new.openDocumentOld("Templates/ImportSubstitutingProduction1.ots");
            }
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, document);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            com.sun.star.beans.XPropertySet xPropSet = null;

            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = aLine.LineDistance = 0;
            aLine.OuterLineWidth = 1;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
            aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;
            XCell xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula(sD + " - " + eD);
            xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));

            int col_start_position = 1;
            int row_start_position = 4;
            int col_final_position = 9;

            int vector_size = data.size();

            float count_plan = 0;
            float sum_plan = 0;
            float count_sklad = 0;
            float sum_sklad = 0;
            float count_export = 0;
            float sum_export = 0;
            int x = 1;

            for (int k = 0; k < vector_size; k++) {


                if (k == 0) {
                    XCellRange xCellRange = xSpreadsheet.getCellRangeByPosition(col_start_position, row_start_position,
                            col_final_position, row_start_position);
                    com.sun.star.util.XMergeable xMerge = UnoRuntime.queryInterface(com.sun.star.util.XMergeable.class, xCellRange);
                    xMerge.merge(true);
                    int kod_naim = Integer.parseInt(((Vector) data.elementAt(k)).get(9).toString());
                    String projDescript = new String();
                    if (!repFlag) {
                        projDescript = db.getProjDescription(kod_naim);
                    } else {
                        projDescript = db.getTProjDescription(kod_naim);
                    }
                    xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);
                    xCell.setFormula(projDescript.trim());

                    row_start_position += 1;
                }

                if (((vector_size - 1) - k) != 0) {
                    if (Integer.parseInt(((Vector) data.elementAt(k)).get(9).toString()) ==
                            Integer.parseInt(((Vector) data.elementAt(k + 1)).get(9).toString())) {
                        count_plan += Float.parseFloat(((Vector) data.elementAt(k)).get(3).toString().trim());
                        sum_plan += Float.parseFloat(((Vector) data.elementAt(k)).get(4).toString().trim());
                        count_sklad += Float.parseFloat(((Vector) data.elementAt(k)).get(5).toString().trim());
                        sum_sklad += Float.parseFloat(((Vector) data.elementAt(k)).get(6).toString().trim());
                        count_export += Float.parseFloat(((Vector) data.elementAt(k)).get(7).toString().trim());
                        sum_export += Float.parseFloat(((Vector) data.elementAt(k)).get(8).toString().trim());
                        for (int z = 0; z < ((Vector) data.elementAt(k)).size(); z++) {
                            if (z != 9) {
                                if (z == 0) {
                                    xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                    xPropSet.setPropertyValue("TableBorder", aBorder);
                                    xCell.setFormula(String.valueOf(x));
                                    col_start_position++;
                                } else {
                                    xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                    xPropSet.setPropertyValue("TableBorder", aBorder);
                                    xCell.setFormula(((Vector) data.elementAt(k)).get(z).toString().trim());
                                    //String.valueOf(Long.parseLong(((Vector)data.elementAt(k)).get(z).toString().trim()));
                                    col_start_position++;
                                }

                            }

                        }
                        col_start_position = 1;
                        x++;
                    } else {

                        for (int z = 0; z < ((Vector) data.elementAt(k)).size(); z++) {
                            if (z != 9) {
                                if (z == 0) {
                                    xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                    xPropSet.setPropertyValue("TableBorder", aBorder);
                                    xCell.setFormula(String.valueOf(x));
                                    col_start_position++;
                                } else {
                                    xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                    xPropSet.setPropertyValue("TableBorder", aBorder);
                                    xCell.setFormula(((Vector) data.elementAt(k)).get(z).toString().trim());
                                    col_start_position++;
                                }
                            }
                        }
                        count_plan += Float.parseFloat(((Vector) data.elementAt(k)).get(3).toString().trim());
                        sum_plan += Float.parseFloat(((Vector) data.elementAt(k)).get(4).toString().trim());
                        count_sklad += Float.parseFloat(((Vector) data.elementAt(k)).get(5).toString().trim());
                        sum_sklad += Float.parseFloat(((Vector) data.elementAt(k)).get(6).toString().trim());
                        count_export += Float.parseFloat(((Vector) data.elementAt(k)).get(7).toString().trim());
                        sum_export += Float.parseFloat(((Vector) data.elementAt(k)).get(8).toString().trim());
                        col_start_position = 1;
                        row_start_position += 1;
                        x = 1;
                        XCellRange xCellRange_1 = xSpreadsheet.getCellRangeByPosition(col_start_position, row_start_position,
                                col_final_position - 6, row_start_position);
                        com.sun.star.util.XMergeable xMerge_1 = UnoRuntime.queryInterface(com.sun.star.util.XMergeable.class, xCellRange_1);
                        xMerge_1.merge(true);
                        xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.RIGHT);
                        xCell.setFormula("Итого:");

                        xCell = xSpreadsheet.getCellByPosition(col_start_position + 3, row_start_position);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell.setFormula(String.valueOf(new BigDecimal(count_plan).setScale(1, BigDecimal.ROUND_HALF_UP)));

                        xCell = xSpreadsheet.getCellByPosition(col_start_position + 4, row_start_position);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell.setFormula(String.valueOf(new BigDecimal(sum_plan).setScale(1, BigDecimal.ROUND_HALF_UP)));

                        xCell = xSpreadsheet.getCellByPosition(col_start_position + 5, row_start_position);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell.setFormula(String.valueOf(new BigDecimal(count_sklad).setScale(1, BigDecimal.ROUND_HALF_UP)));

                        xCell = xSpreadsheet.getCellByPosition(col_start_position + 6, row_start_position);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell.setFormula(String.valueOf(new BigDecimal(sum_sklad).setScale(1, BigDecimal.ROUND_HALF_UP)));

                        xCell = xSpreadsheet.getCellByPosition(col_start_position + 7, row_start_position);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell.setFormula(String.valueOf(new BigDecimal(count_export).setScale(1, BigDecimal.ROUND_HALF_UP)));

                        xCell = xSpreadsheet.getCellByPosition(col_start_position + 8, row_start_position);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xCell.setFormula(String.valueOf(new BigDecimal(sum_export).setScale(1, BigDecimal.ROUND_HALF_UP)));

                        count_plan = 0;
                        sum_plan = 0;
                        count_sklad = 0;
                        sum_sklad = 0;
                        count_export = 0;
                        sum_export = 0;

                        col_start_position = 1;
                        row_start_position += 1;
                        XCellRange xCellRange_2 = xSpreadsheet.getCellRangeByPosition(col_start_position, row_start_position,
                                col_final_position, row_start_position);
                        com.sun.star.util.XMergeable xMerge_2 = UnoRuntime.queryInterface(com.sun.star.util.XMergeable.class, xCellRange_2);
                        xMerge_2.merge(true);
                        //  row_start_position += 1;
                        int kod_naim = Integer.parseInt(((Vector) data.elementAt(k + 1)).get(9).toString());
                        String projDescript = new String();
                        if (!repFlag) {
                            projDescript = db.getProjDescription(kod_naim);
                        } else {
                            projDescript = db.getTProjDescription(kod_naim);
                        }
                        xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("TableBorder", aBorder);
                        xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                        xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);
                        xCell.setFormula(projDescript.trim());
                        //row_start_position += 1;
                    }
                } else {
                    x = 1;
                    for (int z = 0; z < ((Vector) data.elementAt(k)).size(); z++) {
                        if (z != 9) {
                            if (z == 0) {
                                xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                xPropSet.setPropertyValue("TableBorder", aBorder);
                                xCell.setFormula(String.valueOf(x));
                                col_start_position++;
                            } else {
                                xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                                xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                                xPropSet.setPropertyValue("TableBorder", aBorder);
                                xCell.setFormula(((Vector) data.elementAt(k)).get(z).toString().trim());
                                col_start_position++;
                            }


                        }
                    }
                    col_start_position = 1;
                    row_start_position += 1;
                    count_plan += Float.parseFloat(((Vector) data.elementAt(k)).get(3).toString().trim());
                    sum_plan += Float.parseFloat(((Vector) data.elementAt(k)).get(4).toString().trim());
                    count_sklad += Float.parseFloat(((Vector) data.elementAt(k)).get(5).toString().trim());
                    sum_sklad += Float.parseFloat(((Vector) data.elementAt(k)).get(6).toString().trim());
                    count_export += Float.parseFloat(((Vector) data.elementAt(k)).get(7).toString().trim());
                    sum_export += Float.parseFloat(((Vector) data.elementAt(k)).get(8).toString().trim());
                    XCellRange xCellRange = xSpreadsheet.getCellRangeByPosition(col_start_position, row_start_position,
                            col_final_position - 6, row_start_position);
                    com.sun.star.util.XMergeable xMerge = UnoRuntime.queryInterface(com.sun.star.util.XMergeable.class, xCellRange);
                    xMerge.merge(true);
                    xCell = xSpreadsheet.getCellByPosition(col_start_position, row_start_position);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.RIGHT);
                    xCell.setFormula("Итого:");

                    xCell = xSpreadsheet.getCellByPosition(col_start_position + 3, row_start_position);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(String.valueOf(new BigDecimal(count_plan).setScale(1, BigDecimal.ROUND_HALF_UP)));

                    xCell = xSpreadsheet.getCellByPosition(col_start_position + 4, row_start_position);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(String.valueOf(new BigDecimal(sum_plan).setScale(1, BigDecimal.ROUND_HALF_UP)));

                    xCell = xSpreadsheet.getCellByPosition(col_start_position + 5, row_start_position);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(String.valueOf(new BigDecimal(count_sklad).setScale(1, BigDecimal.ROUND_HALF_UP)));

                    xCell = xSpreadsheet.getCellByPosition(col_start_position + 6, row_start_position);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(String.valueOf(new BigDecimal(sum_sklad).setScale(1, BigDecimal.ROUND_HALF_UP)));

                    xCell = xSpreadsheet.getCellByPosition(col_start_position + 7, row_start_position);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(String.valueOf(new BigDecimal(count_export).setScale(1, BigDecimal.ROUND_HALF_UP)));

                    xCell = xSpreadsheet.getCellByPosition(col_start_position + 8, row_start_position);
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
                    xPropSet = UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
                    xPropSet.setPropertyValue("TableBorder", aBorder);
                    xCell.setFormula(String.valueOf(new BigDecimal(sum_export).setScale(1, BigDecimal.ROUND_HALF_UP)));
                }

                row_start_position++;
            }
        } catch (Exception e) {
            Logger.getLogger(DateForm.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Deprecated
    private ArrayList WarehouseList(String year, int smonth, int emonth, String dirPath, String dbfName, ProductionDBF dbf) {
        ArrayList list = new ArrayList();
        if (dirPath.equals(MyReportsModule.dbfPlanPath)) {
            File file = new File(dirPath);
            File[] files = file.listFiles();
            try {
                for (int i = smonth; i <= emonth; i++) {
                    String dbfN = new String();
                    if (i >= 10) {
                        dbfN = dbfName + String.valueOf(i).trim();
                    } else {
                        if (i < 10) {
                            dbfN = dbfName + "0" + String.valueOf(i).trim();
                        }
                    }
                    for (int j = 0; j < files.length; j++) {
                        if (files[j].getName().toString().trim().length() == 10) {
                            String fileName = files[j].getName().toString().trim().substring(0, 6).toLowerCase();
                            if (fileName.equals(dbfN)) {
                                String filePath = dirPath + files[j].getName();
                                list = dbf.readUnite(filePath, list, 1);
                                System.out.println("Читаем плановую ДБФ " + filePath + " : " + filePath.length());

                            }
                        }
                    }

//                if (i != emonth) {
//                    i = i + 1;
//                }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(DateForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (dbfName.equals("pr_")) {
                dirPath = dirPath + "god" + year + "/";
                File file = new File(dirPath);
                File[] files = file.listFiles();
                try {
                    for (int i = smonth; i <= emonth; i++) {
                        String dbfN = new String();
                        if (i >= 10) {
                            dbfN = dbfName + String.valueOf(i).trim() + "d";
                        } else {
                            if (i < 10) {
                                dbfN = dbfName + "0" + String.valueOf(i).trim() + "d";
                            }
                        }
                        for (int j = 0; j < files.length; j++) {
                            if (files[j].getName().toString().trim().length() == 10) {
                                String fileName = files[j].getName().toString().trim().substring(0, 6).toLowerCase();
                                if (fileName.equals(dbfN)) {
                                    String filePath = dirPath + files[j].getName();
                                    System.out.println("Читаем производственную ДБФ " + filePath + " : " + filePath.length());
                                    list = dbf.readUnite(filePath, list, 2);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(DateForm.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                dirPath = dirPath + "god" + year + "/";
                File file = new File(dirPath);
                File[] files = file.listFiles();
                try {
                    for (int i = smonth; i <= emonth; i++) {
                        String dbfN = new String();
                        if (i >= 10) {
                            dbfN = dbfName + String.valueOf(i).trim() + "d";
                        } else {
                            if (i < 10) {
                                dbfN = dbfName + "0" + String.valueOf(i).trim() + "d";
                            }
                        }
                        for (int j = 0; j < files.length; j++) {
                            if (files[j].getName().toString().trim().length() == 11) {
                                String fileName = files[j].getName().toString().trim().substring(0, 7).toLowerCase();
                                if (fileName.equals(dbfN)) {
                                    String filePath = dirPath + files[j].getName();
                                    System.out.println("Читаем экспортную ДБФ " + filePath + " : " + filePath.length());
                                    list = dbf.readUnite(filePath, list, 3);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(DateForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
} 
