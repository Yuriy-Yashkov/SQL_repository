package by.march8.tasks.selections.logic;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.CertificateService;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.services.eancode.EanCodeControlService;
import by.march8.entities.warehouse.Certificate;
import by.march8.entities.warehouse.PriceListValue;
import by.march8.entities.warehouse.RetailValue;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.tasks.selections.model.RefundDBFItem;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import dept.MyReportsModule;
import org.apache.commons.io.FileUtils;
import workDB.DBF;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Andy 12.04.2018 - 9:39.
 */
@SuppressWarnings("all")
public class SelectionsCreator {
    private SaleDocumentDataProvider provider;
    private String exportPath;
    private String exportPathCurrency;
    private ArrayList<String> logList = new ArrayList<String>();

    /**
     * Конструктор
     * <p>
     * Аргументом передается каталог для сохранения документа/ов
     *
     * @param exportPath каталог для сохранения документа/ов
     */
    public SelectionsCreator(final String exportPath) {
        this.exportPath = exportPath;
        MyReportsModule.confPath = Settings.HOME_DIR;
    }


    public void processing___() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        logList.clear();

        logList.add("Дата сканирования: " + timestamp.toString());

        // Читаем возвратный документ
        //List<RefundDBFItem> refundList = readRefundDBF(exportPath + "/import.dbf", "cp866");
        //List<RefundDBFItem> refundList = readRefundDBFSelf(exportPath + "/input_3783.dbf");
        List<RefundDBFItem> refundList = readRefundDBFSelf(exportPath + "/kpk.dbf");


        Collections.sort(refundList, new Comparator<RefundDBFItem>() {
            @Override
            public int compare(RefundDBFItem item2, RefundDBFItem item1) {
                return item2.getEanCode().compareTo(item1.getEanCode());
            }
        });

        SaleDocumentManager manager = new SaleDocumentManager();
        SaleDocumentReport report = new SaleDocumentReport();
        SaleDocumentBase document = new SaleDocumentBase();
        report.setDocument(document);
        List<SaleDocumentDetailItemReport> detailList = new ArrayList<>();
        report.setDetailList(detailList);
        CertificateService certificateService = CertificateService.getInstance();
        System.out.println("Обработка ассортимента..." + refundList.size());
        int step = 0;

        for (RefundDBFItem item : refundList) {
            // Бежим пол всем позициям и формируем накладную
            // Получаем из классификатора изделие нужной структуры по его EAN CODE
            SaleDocumentDetailItemReport newItem = manager.getReportItemByEanCode(item.getEanCode());
            if (newItem != null) {
                step++;
                newItem.setItemColor("ПРОЗРАЧНЫЙ");
                newItem.setValuePrice(item.getCost());
                newItem.setValueSumCost(item.getSumCost());
                newItem.setValueVAT((float) item.getVat());
                newItem.setValueSumVat(item.getSumVat());
                newItem.setValueSumCostAndVat(item.getSumCostAndVat());
                newItem.setAmountPrint(item.getAmount());

                // попытка получить сертификат для изделия
                final Certificate certificate = certificateService.
                        getCertificateByArticleRecursively(newItem.getArticleCode(), 1);
                // Устанавливаем сертификат для изделия и его детали
                if (certificate != null) {
                    newItem.setCertificateType(certificate.getTypeName().trim());
                    newItem.setCertificateName(certificate.getValue().trim());
                }

                // попытка получить удостоверение гос регистрации для изделия
                final Certificate license = certificateService.
                        getLicenseByArticleRecursively(newItem.getArticleCode(), 1, newItem.getItemAgeGroup());
                // Устанавливаем сертификат для изделия и его детали
                if (license != null) {
                    newItem.setLicenseType("Свидетельство ГР");
                    newItem.setLicenseName(license.getValue().trim());
                }
                detailList.add(newItem);
                System.out.println("Выполнено [" + step + "] из [" + refundList.size() + "]");
            }
        }
        generateRetailDBF(report);

        saveProcessingLog(logList, exportPath);
    }


    /**
     * Метод сохранения Log-файла
     *
     * @param list лист
     * @param path путь сохранения
     */
    private void saveProcessingLog(List<String> list, String path) {
        try {
            FileUtils.writeLines(new File(path + "\\processing-" + DateUtils.getNormalDateTimeFormatPlus(new Date()) + ".log"), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processing() {
        getUniqueEanByPart();
    }

    private void getUnicInvoices() {
        SaleDocumentJDBC db = new SaleDocumentJDBC();
        List<String> list = db.selectAllItems();

        HashMap<String, TestItem> map = new HashMap<>();
        for (String item : list) {
            TestItem get_ = map.get(item.trim());
            if (get_ != null) {
                get_.setIdent(get_.getIdent() + " " + item);
                System.out.println("Collision " + item);
            } else {
                map.put(item, new TestItem(Integer.valueOf(item.trim()), item.trim()));
            }
            //System.out.println(item.getEanCode());
        }
    }


    public void processing_() {
        //getInvoiceDifference();
    }

    /**
     * Отборка уникальных EAN кодов по первым символам
     */
    private void getUniqueEanByPart() {
        long start = System.currentTimeMillis();
        EanCodeControlService service = EanCodeControlService.getInstance();
        HashMap<String, Integer> eanMap = service.getUniqueEanMap();
        HashSet<String> eanSet = new HashSet<>();
        List<String> reportList = new ArrayList<>();
        int uniqueLength = 7;
        reportList.add("*************************************************************************");
        reportList.add("Запуск метода getUniqueEanByPart(): " + DateUtils.getNormalDateTimeFormatPlain(new Date()));
        reportList.add("*************************************************************************");
        reportList.add("Отбор кода производителя (" + uniqueLength + " знаков) из " + eanMap.size() + " уникальных EAN13 кодов");
        reportList.add("*************************************************************************");

        Iterator it = eanMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String ean = (String) pair.getKey();
            eanSet.add(ean.substring(0, uniqueLength));
        }

        List<String> eanList = new ArrayList<>(eanSet);
        eanList.sort((a1, a2) -> {
            return a1.compareTo(a2);
        });

        int count = 0;
        for (String s : eanList) {
            System.out.println(s);
            reportList.add(s);
            count++;
        }


        reportList.add("*************************************************************************");
        reportList.add("Обнаружено " + count + " уникальных EAN13 кодов");
        reportList.add("*************************************************************************");
        long end = System.currentTimeMillis();

        NumberFormat formatter = new DecimalFormat("#0.00000");
        reportList.add("Выполнено за " + formatter.format((end - start) / 1000d) + " сек.");
        reportList.add("*************************************************************************");

        try {
            FileUtils.writeLines(new File(exportPath + "\\" + "ean13_unique.txt"), reportList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getInvoiceDifference() {


        // Читаем исходник в мапу
        String sourceFile = "ПровDBF.DBF";
        int i = 0;
        List<SaleDocumentDetailItemReport> sourceList = readInvoiceDbf(sourceFile);
        HashMap<String, SaleDocumentDetailItemReport> map = new HashMap<>();
        for (SaleDocumentDetailItemReport item : sourceList) {
            SaleDocumentDetailItemReport get_ = map.get(item.getEanCode());
            if (get_ != null) {
                get_.setAmountAll(get_.getAmountAll() + item.getAmountAll());
                if (get_.getRetailValue().getValueCostRetail() != item.getRetailValue().getValueCostRetail()) {
                    System.out.println(get_.getRetailValue().getValueCostRetail() + " - " + item.getRetailValue().getValueCostRetail());
                    i++;
                }
            } else {
                map.put(item.getEanCode(), item);
            }
            //System.out.println(item.getEanCode());
        }

        System.out.println("В мапе кодов: " + map.size() + " - " + i);
        int amount = 0;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            SaleDocumentDetailItemReport item = (SaleDocumentDetailItemReport) pair.getValue();
            amount += item.getAmountAll();
            //result.add(item);
            //it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("Количество :" + amount);
        // поочередно добавляем в лист накладные 10/ 20/ Бюстье 10

        List<SaleDocumentDetailItemReport> list = new ArrayList<>();
        list.addAll(readInvoiceDbf("юэ1246245.dbf"));
        list.addAll(readInvoiceDbf("юэ1246597.dbf"));
        list.addAll(readInvoiceDbf("юэ1246598.dbf"));

        amount = 0;
        i = 0;
        for (SaleDocumentDetailItemReport item : list) {
            amount += item.getAmountAll();
            SaleDocumentDetailItemReport get_ = map.get(item.getEanCode());
            if (get_ != null) {

                if ((int) get_.getRetailValue().getValueTradeMarkup() != (int) item.getRetailValue().getValueTradeMarkup()) {
                    System.out.println(get_.getArticleNumber()
                            + " : [" + item.getEanCode()
                            + "] : Торговая надбавка : [" + get_.getRetailValue().getValueTradeMarkup()
                            + "] -> [" + item.getRetailValue().getValueTradeMarkup() + "]");
                    i++;
                }
            } else {
                System.out.println(item.getEanCode() + " : Данного EAN-кода нет в накладных");
            }
        }

        System.out.println("Обнаружено [" + i + "] несоответствий в торговых надбавках");

        for (SaleDocumentDetailItemReport item : list) {
            amount += item.getAmountAll();
            SaleDocumentDetailItemReport get_ = map.get(item.getEanCode());
            if (get_ != null) {
                if (get_.getRetailValue().getValueCostRetail() != item.getRetailValue().getValueCostRetail()) {
                    System.out.println(get_.getArticleNumber()
                            + " : [" + item.getEanCode()
                            + "] : Розничная цена : [" + get_.getRetailValue().getValueCostRetail()
                            + "] -> [" + item.getRetailValue().getValueCostRetail() + "] кол-во [" + item.getAmountAll() + "]");
                    i++;
                }

            } else {
                System.out.println(item.getEanCode() + " : Данного EAN-кода нет в накладных");
            }
        }

        System.out.println("Обнаружено [" + i + "] несоответствий в торговых надбавках");
        // Перебор для листа  и вывод в лог отличий по розничной цене единицы
    }


    private List<SaleDocumentDetailItemReport> readInvoiceDbf(String fileName) {
        String path = "c:\\selections\\123\\";
        List<SaleDocumentDetailItemReport> result = new ArrayList<>();

        try {
            InputStream inputStream = new FileInputStream(path + fileName);
            DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");
            System.out.println("Количество строк в документе [" + fileName + "]:" + reader.getRecordCount());
            Object obj[];
            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();

                SaleDocumentDetailItemReport item = new SaleDocumentDetailItemReport();
                item.setArticleNumber(obj[3].toString());
                item.setItemColor(obj[7].toString());
                item.setAmountAll(Integer.valueOf(obj[8].toString().replace(".0", "")));
                item.setValuePrice(Double.valueOf(obj[9].toString()));
                item.setValueSumCost(Double.valueOf(obj[10].toString()));
                item.setValueVAT(Float.valueOf(obj[11].toString()));
                item.setValueSumVat(Double.valueOf(obj[12].toString()));
                item.setValueSumCostAndVat(Double.valueOf(obj[13].toString()));
                item.setEanCode(obj[14].toString());
                item.setItemPriceList(obj[22].toString());


                RetailValue retail = new RetailValue();
                retail.setValueTradeMarkup(Integer.parseInt(obj[24].toString().trim().replace(",00", "").replace(".0", "")));
                retail.setValueCostRetail(Double.parseDouble(obj[25].toString().trim().replace(",", ".")));
                //item.setValueTradeMarkup();
                item.setRetailValue(retail);

                result.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void generateRetailDBF(SaleDocumentReport report) {
        String param;
        String path = "c:\\selections\\";
        //String nn = "output_3783" ;
        String nn = "output_KPK";

        DBF dbf = new DBF(3, nn, path);

        dbf.conn();
        try {
            List<SaleDocumentDetailItemReport> detailList = report.getDetailList();
            for (SaleDocumentDetailItemReport item : detailList) {

                Object[] v = new Object[29];
                v[0] = "ОАО \"8МАРТА\"";
                v[1] = item.getDocument();
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

                v[7] = "РАЗНОЦВЕТ";
                v[8] = item.getAmountPrint();
                v[9] = item.getValuePrice();
                v[10] = item.getValueSumCost();//rs.getObject("summa");
                v[11] = item.getValueVAT(); //rs.getObject("nds");
                v[12] = item.getValueSumVat();//rs.getObject("summa_nds");
                v[13] = item.getValueSumCostAndVat(); //rs.getObject("itogo");
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
                v[17] = param;

                v[18] = Long.valueOf(item.getTnvedCode());//rs.getObject("narp");
                v[19] = String.format("%.4f", item.getWeight() * item.getAmountPrint());//String.valueOf(rs.getObject("massa_ed"));
                v[20] = String.format("%.4f", item.getWeight());//String.valueOf(rs.getObject("massa"));
                v[21] = "";//dogovor;

                param = item.getItemPriceList();
                if (param.length() > 50) {
                    param = param.substring(0, 50);
                }
                v[22] = param;
                v[23] = item.getArticleCode();//rs.getString("sar").trim();
                v[24] = "0";//String.format("%.2f", documentBase.getTradeMarkValue());

                //RetailValue retail = item.getRetailValue();

                v[25] = "0";//String.format("%.2f", retail.getValueCostRetail());
                v[26] = String.valueOf(item.getPtkCode()); //"0";//rs.getObject("ptk") != null ? rs.getString("ptk").trim() : "0";
                v[27] = item.getAccountingPrice(); //rs.getObject("cena_uch") != null ? rs.getFloat("cena_uch") : 0;
                v[28] = item.getAccountingVat();

                dbf.write(v);
            }

        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        if (dbf != null) {
            dbf.disconn();
        }
    }

    private List<RefundDBFItem> readRefundDBFSelf(String path) {
        Object[][] colname = {{"COLOR", ""}, {"KOL", ""}, {"CENA", ""}, {"SUMMA", ""}, {"NDS", ""},
                {"SUMMA_NDS", ""}, {"ITOGO", ""}, {"EANCODE", ""}, {"PREISCUR", ""}};

        List<RefundDBFItem> result = new ArrayList<>();
        SaleDocumentJDBC db = new SaleDocumentJDBC();

        DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");
            System.out.println("Количество строк в документе :" + reader.getRecordCount());

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

            Object obj[] = new Object[reader.getFieldCount()];
            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();

                RefundDBFItem item = new RefundDBFItem();
                item.setColor(obj[0].toString().trim());
                item.setAmount(Double.valueOf(obj[1].toString().trim()));
                item.setCost(Double.valueOf(obj[2].toString().trim()));
                item.setSumCost(Double.valueOf(obj[3].toString().trim()));
                item.setVat(Double.valueOf(obj[4].toString().trim()));
                item.setSumVat(Double.valueOf(obj[5].toString().trim()));
                item.setSumCostAndVat(Double.valueOf(obj[6].toString().trim()));
                item.setEanCode(obj[7].toString().trim());
                item.setPriceList(obj[8].toString().trim());

                Integer count = service.getAmountEanCode(item.getEanCode().trim());
                if (count == 0) {
                    System.out.println("EAN13 код [" + item.getEanCode() + "] не найден в базе ");
                    logList.add(item.getEanCode() + ": Не найден");
                } else if (count > 1) {
                    System.out.println("EAN13 код [" + item.getEanCode() + "] дублирован в базе");
                    logList.add(item.getEanCode() + ": Дубликат");
                } else {
                    result.add(item);
                }
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void generateRetailDBF(String nn, String pathSave, boolean valuta, boolean diskount) {
        System.out.println("Скидка " + diskount);
        String query = "";

        String dogovor = new String("");
        String param;

        DBF dbf = null;
        if (pathSave.equals("".toString()) || pathSave == null) {
            dbf = new DBF(11, nn, "");
        } else {
            dbf = new DBF(11, nn, pathSave);
        }

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
                v[17] = param;

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
                v[24] = String.format("%.2f", documentBase.getTradeMarkValue());

                RetailValue retail = item.getRetailValue();

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

    private List<RefundDBFItem> readRefundDBFExcel(String path) {
        Object[][] colname = {{"EAN", ""}, {"COST", ""}, {"DOC_DATE", ""}};

        List<RefundDBFItem> result = new ArrayList<>();
        //DB db = new DB();
        DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");
            System.out.println("Количество строк в документе :" + reader.getRecordCount());

            for (int i = 0; i < colname.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colname[i][0]))
                        colname[i][1] = j;
                }
                if (colname[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            Object obj[] = new Object[reader.getFieldCount()];
            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();

                RefundDBFItem item = new RefundDBFItem();
                String date = parseDateDoc(obj[2].toString().trim());
                if (date != null) {
                    item.setEanCode(obj[0].toString().trim());
                    item.setAmount(Double.valueOf(obj[1].toString().trim()));
                    item.setPriceList(date);
                    item.setDate(DateUtils.getDateByStringValue(date));
                    // Поиск в базе EAN кода
                    String ean = item.getEanCode();
                    if (!ean.equals("")) {
                        //boolean res = db.eanCodeIsExist(ean);
                        result.add(item);
                    }
                }
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<RefundDBFItem> readRefundDBFVital(String path) {

        //DATATTN,C,20	NOMERTTN,C,12	REGISTR,C,70

        Object[][] colname = {{"COLOR", ""}, {"KOL", ""}, {"CENA", ""}, {"SUMMA", ""}, {"NDS", ""},
                {"SUMMA_NDS", ""}, {"ITOGO", ""}, {"EANCODE", ""}, {"PREISCUR", ""}, {"DATATTN", ""}, {"NOMERTTN", ""}, {"REGISTR", ""}};

        List<RefundDBFItem> result = new ArrayList<>();
        //DB db = new DB();
        DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");
            System.out.println("Количество строк в документе :" + reader.getRecordCount());

            for (int i = 0; i < colname.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colname[i][0]))
                        colname[i][1] = j;
                }
                if (colname[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            Object obj[] = new Object[reader.getFieldCount()];
            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();

                RefundDBFItem item = new RefundDBFItem();
                item.setColor(obj[0].toString().trim());
                item.setAmount(Double.valueOf(obj[1].toString().trim()));
                item.setCost(Double.valueOf(obj[2].toString().trim()));
                item.setSumCost(Double.valueOf(obj[3].toString().trim()));
                item.setVat(Double.valueOf(obj[4].toString().trim()));
                item.setSumVat(Double.valueOf(obj[5].toString().trim()));
                item.setSumCostAndVat(Double.valueOf(obj[6].toString().trim()));
                item.setEanCode(obj[7].toString().trim());
                item.setPriceList(obj[8].toString().trim());


                item.setDate(getDateByDateTimeString(obj[9].toString().trim()));
                item.setNumber(getParsedNumber(obj[10].toString().trim()));

                // Поиск в базе EAN кода
                String ean = item.getEanCode();
                if (!ean.equals("")) {
                    //boolean res = db.eanCodeIsExist(ean);
                    result.add(item);
                }
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Date getDateByDateTimeString(String datetimeValue) {
        if (!datetimeValue.equals("")) {
            return DateUtils.getDateByStringValue(datetimeValue.substring(0, 10));
        }
        return null;
    }

    private String getParsedNumber(String number) {
        if (!number.equals("")) {
            if (number.trim().length() > 7) {
                return number.trim().substring(2, number.length());
            } else {
                return number.trim();
            }
        }
        return null;
    }

    private List<SaleDocumentDetailItemReport> getEanCodeEntrance(String eanCode, List<SaleDocumentReport> list) {
        List<SaleDocumentDetailItemReport> result = new ArrayList<>();
        for (SaleDocumentReport report : list) {
            List<SaleDocumentDetailItemReport> invoice = report.getDetailList();
            for (SaleDocumentDetailItemReport item : invoice) {
                if (eanCode.equals(item.getEanCode().trim())) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    private Map<String, SaleDocumentReport> prepareInvoicesMap(List<SaleDocumentEntity> list) {
        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        Map<String, SaleDocumentReport> result = new HashMap<>();
        for (SaleDocumentEntity entity : list) {
            try {
                final SaleDocumentReport report = provider.prepareDocument(entity.getId(), true);
                if (report != null) {
                    SaleDocumentBase documentBase = report.getDocument();
                    result.put(documentBase.getDocumentNumber(), report);
                }
            } catch (Exception e) {
                System.out.println("error processing for document [" + entity.getDocumentNumber() + "]");
            }
        }
        return result;
    }

    private List<SaleDocumentReport> prepareInvoicesList(List<SaleDocumentEntity> list) {
        SaleDocumentDataProvider provider = new SaleDocumentDataProvider();
        List<SaleDocumentReport> result = new ArrayList<>();
        for (SaleDocumentEntity entity : list) {
            try {
                final SaleDocumentReport report = provider.prepareDocument(entity.getId(), true);
                if (report != null) {
                    result.add(report);
                }
            } catch (Exception e) {
                System.out.println("error processing for document [" + entity.getDocumentNumber() + "]");
            }
        }
        return result;
    }

    private String parseDateDoc(String dateDoc) {
        if (dateDoc == null) {
            return null;
        }

        int indexOT = dateDoc.indexOf("от");
        if (indexOT > -1) {
            return dateDoc.substring(indexOT + 3, indexOT + 13);
        }
        return null;
    }

    public class TestItem {
        private int id;
        private String ident;

        public TestItem(final int id, final String ident) {
            this.id = id;
            this.ident = ident;
        }

        public int getId() {
            return id;
        }

        public void setId(final int id) {
            this.id = id;
        }

        public String getIdent() {
            return ident;
        }

        public void setIdent(final String ident) {
            this.ident = ident;
        }
    }

}
