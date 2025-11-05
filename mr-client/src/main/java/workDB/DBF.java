package workDB;

import by.march8.ecs.framework.common.LogCrutch;
import com.svcon.jdbf.JDBFException;
import dept.MyReportsModule;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

//import com.svcon.jdbf.DBFReader;


@SuppressWarnings("all")
public class DBF {

    // private static final Logger log = new Log().getLoger(DBF.class);
    private static final LogCrutch log = new LogCrutch();
    JDBField field[] = null;
    Object record[] = null;
    DBFWriter dbfw = null;
    DBFReader dbfr = null;
    String path = null;
    String nNakl = null;

    public DBF() {

    }

    public DBF(String path, JDBField field[]) {
        try {
            this.path = new String(path);
            if (field != null) {
                this.field = field.clone();
                this.dbfw = new DBFWriter(path, field, new String("cp866"));
            }
            this.dbfr = new DBFReader(path);
        } catch (Exception e) {
            System.err.println("Ошибка при создании обекта DBF: " + e.getMessage());
        }
    }

    /**
     *
     *
     */

    public DBF(int v, String nn, String pathSave) {
        nNakl = new String(nn);
        try {
            if (pathSave.equals("".toString()) || pathSave == null) {
                //path = new String((new File("")).getCanonicalPath().replace('\\', '/'));
                path = new String("");
                path += MyReportsModule.confPath + "/Enakl";
            } else path = new String(pathSave);

            File fC = new File(path);
            fC = new java.io.File(path);
            if (!fC.exists()) {
                new File(path).mkdir();
            }
        } catch (Exception e) {
            log.error("Ошибка при создании папки Enakl: ", e);
            System.err.println("Ошибка при создании папки Enakl: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при создании папки Enakl: " + e, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (v == 1) { //вид полной наклодной
                field = new JDBField[24];
                record = new Object[23];
                field[0] = new JDBField("FIRMA", 'C', 12, 0);
                field[1] = new JDBField("TTN_NOMER", 'C', 10, 0);
                field[2] = new JDBField("MODEL", 'F', 6, 0);
                field[3] = new JDBField("ART", 'C', 15, 0);
                field[4] = new JDBField("NAIM", 'C', 45, 0);
                field[5] = new JDBField("RAZMER", 'C', 20, 0);
                field[6] = new JDBField("SORT", 'C', 8, 0);
                field[7] = new JDBField("COLOR", 'C', 25, 0);
                field[8] = new JDBField("KOL", 'F', 16, 0);
                field[9] = new JDBField("CENA", 'F', 20, 2);
                field[10] = new JDBField("SUMMA", 'F', 20, 2);
                field[11] = new JDBField("NDS", 'F', 7, 2);
                field[12] = new JDBField("SUMMA_NDS", 'F', 20, 2);
                field[13] = new JDBField("ITOGO", 'F', 20, 2);
                field[14] = new JDBField("EANCODE", 'C', 13, 0);
                field[15] = new JDBField("COUNTRY", 'C', 2, 0);
                field[16] = new JDBField("SERTIFIKAT", 'C', 105, 0);
                field[17] = new JDBField("GGR", 'C', 80, 0);
                field[18] = new JDBField("TNVD", 'F', 12, 0);
                field[19] = new JDBField("MASSA_ED", 'C', 20, 0);
                field[20] = new JDBField("MASSA", 'C', 18, 0);
                field[21] = new JDBField("DOGOVOR", 'C', 50, 0);
                field[22] = new JDBField("PREISCUR", 'C', 50, 0);
                field[23] = new JDBField("CENA_UCH", 'F', 20, 2);
            }
            if (v == 2) { // вид ЦУМ накладной
                field = new JDBField[21];
                record = new Object[21];
                field[0] = new JDBField("ND", 'C', 20, 0);
                field[1] = new JDBField("DAT_D", 'D', 8, 0);
                field[2] = new JDBField("PNT", 'F', 13, 0);
                field[3] = new JDBField("NT", 'C', 30, 0);
                field[4] = new JDBField("A", 'C', 30, 0);
                field[5] = new JDBField("M", 'C', 15, 0);
                field[6] = new JDBField("RR", 'C', 15, 0);
                field[7] = new JDBField("RIS", 'C', 15, 0);
                field[8] = new JDBField("PK", 'C', 3, 0);
                field[9] = new JDBField("EI", 'C', 5, 0);
                field[10] = new JDBField("Q", 'F', 9, 2);
                field[11] = new JDBField("COT", 'F', 11, 3);
                field[12] = new JDBField("COT_1GR", 'F', 11, 3);
                field[13] = new JDBField("PCE", 'F', 7, 3);
                field[14] = new JDBField("SKIDKA", 'F', 7, 3);
                field[15] = new JDBField("PNDS", 'F', 7, 3);
                field[16] = new JDBField("NPR", 'C', 15, 0);
                field[17] = new JDBField("DPR", 'C', 8, 0);
                field[18] = new JDBField("SERTIF", 'C', 253, 0);
                field[19] = new JDBField("AKCIZ", 'C', 253, 0);
                field[20] = new JDBField("VST", 'C', 253, 0);
            }
            if (v == 3) { //вид накладной для филиалов
                field = new JDBField[29];
                record = new Object[29];
                field[0] = new JDBField("FIRMA", 'C', 12, 0);
                field[1] = new JDBField("TTN_NOMER", 'C', 10, 0);
                field[2] = new JDBField("MODEL", 'F', 6, 0);
                field[3] = new JDBField("ART", 'C', 15, 0);
                field[4] = new JDBField("NAIM", 'C', 45, 0);
                field[5] = new JDBField("RAZMER", 'C', 20, 0);
                field[6] = new JDBField("SORT", 'C', 8, 0);
                field[7] = new JDBField("COLOR", 'C', 25, 0);
                field[8] = new JDBField("KOL", 'F', 16, 0);
                field[9] = new JDBField("CENA", 'F', 20, 2);
                field[10] = new JDBField("SUMMA", 'F', 20, 2);
                field[11] = new JDBField("NDS", 'F', 7, 2);
                field[12] = new JDBField("SUMMA_NDS", 'F', 20, 2);
                field[13] = new JDBField("ITOGO", 'F', 20, 2);
                field[14] = new JDBField("EANCODE", 'C', 13, 0);
                field[15] = new JDBField("COUNTRY", 'C', 2, 0);
                field[16] = new JDBField("SERTIFIKAT", 'C', 80, 0);
                field[17] = new JDBField("GGR", 'C', 80, 0);
                field[18] = new JDBField("TNVD", 'F', 12, 0);
                field[19] = new JDBField("MASSA_ED", 'C', 20, 0);
                field[20] = new JDBField("MASSA", 'C', 18, 0);
                field[21] = new JDBField("DOGOVOR", 'C', 50, 0);
                field[22] = new JDBField("PREISCUR", 'C', 50, 0);
                field[23] = new JDBField("SAR", 'C', 15, 0);
                field[24] = new JDBField("TORG_NADB", 'C', 15, 0);
                field[25] = new JDBField("ROZN_CENA", 'C', 15, 0);
                field[26] = new JDBField("KPK", 'C', 15, 0);
                field[27] = new JDBField("CENA_UCH", 'F', 20, 2);
                field[28] = new JDBField("NDS_VALUE", 'F', 7, 2);
            }
            if (v == 4) { //отгрузка для старой линии
                field = new JDBField[22];
                record = new Object[22];
                field[0] = new JDBField("DAO", 'N', 4, 0);
                field[1] = new JDBField("KPL", 'N', 4, 0);
                field[2] = new JDBField("SK", 'N', 4, 0);
                field[3] = new JDBField("TTN", 'N', 9, 0);
                field[4] = new JDBField("NAK", 'N', 10, 0);
                field[5] = new JDBField("SAR", 'N', 8, 0);
                field[6] = new JDBField("SOT", 'N', 3, 0);
                field[7] = new JDBField("FAS", 'N', 6, 0);
                field[8] = new JDBField("RST", 'N', 3, 0);
                field[9] = new JDBField("RZM", 'N', 3, 0);
                field[10] = new JDBField("SRT", 'N', 1, 0);
                field[11] = new JDBField("PACH", 'N', 4, 0);
                field[12] = new JDBField("KOL", 'N', 8, 1);
                field[13] = new JDBField("CNO", 'N', 9, 2);
                field[14] = new JDBField("CNR", 'N', 8, 0);
                field[15] = new JDBField("KCV", 'N', 3, 0);
                field[16] = new JDBField("NPT", 'N', 5, 0);
                field[17] = new JDBField("VCN", 'C', 2, 0);
                field[18] = new JDBField("CRR", 'N', 10, 4);
                field[19] = new JDBField("NDB", 'N', 9, 2);
                field[20] = new JDBField("NDR", 'N', 8, 2);
                field[21] = new JDBField("PSK", 'N', 4, 1);
            }
            if (v == 5) { //Инвентаризация для старой линии
                field = new JDBField[19];
                record = new Object[19];
                field[0] = new JDBField("DATA", 'C', 4, 0);
                field[1] = new JDBField("POLN", 'N', 4, 0);
                field[2] = new JDBField("NOMN", 'N', 7, 0);
                field[3] = new JDBField("OPER", 'N', 2, 0);
                field[4] = new JDBField("SAR", 'N', 8, 0);
                field[5] = new JDBField("FAS", 'N', 6, 0);
                field[6] = new JDBField("KCV", 'N', 3, 0);
                field[7] = new JDBField("SRT", 'N', 1, 0);
                field[8] = new JDBField("RZM", 'N', 3, 0);
                field[9] = new JDBField("RST", 'N', 3, 0);
                field[10] = new JDBField("KLFS", 'N', 10, 1);
                field[11] = new JDBField("CEN", 'N', 9, 2);
                field[12] = new JDBField("CENR", 'N', 9, 2);
                field[13] = new JDBField("CENO", 'N', 9, 2);
                field[14] = new JDBField("OB", 'C', 1, 0);
                field[15] = new JDBField("VCN", 'C', 2, 0);
                field[16] = new JDBField("SK", 'N', 4, 0);
                field[17] = new JDBField("NAR", 'C', 30, 0);
                field[18] = new JDBField("NIZ", 'C', 15, 0);
            }
            if (v == 6) {
                // Электронная накладная формата БТП
                field = new JDBField[25];
                record = new Object[25];

                field[0] = new JDBField("nd", 'C', 20, 0);
                field[1] = new JDBField("dat_d", 'D', 8, 0);
                field[2] = new JDBField("pnt", 'N', 13, 0);
                field[3] = new JDBField("nt", 'C', 30, 0);
                field[4] = new JDBField("a", 'C', 30, 0);
                field[5] = new JDBField("m", 'C', 15, 0);
                field[6] = new JDBField("rr", 'C', 15, 0);
                field[7] = new JDBField("ris", 'C', 15, 0);
                field[8] = new JDBField("pk", 'C', 3, 0);
                field[9] = new JDBField("ei", 'C', 5, 0);
                field[10] = new JDBField("q", 'N', 9, 2);
                field[11] = new JDBField("cot", 'N', 11, 3);
                field[12] = new JDBField("cot_1gr", 'N', 11, 3);
                field[13] = new JDBField("pce", 'N', 7, 3);
                field[14] = new JDBField("skidka", 'N', 7, 3);
                field[15] = new JDBField("pnds", 'N', 7, 3);
                field[16] = new JDBField("npr", 'C', 15, 0);
                field[17] = new JDBField("dpr", 'D', 8, 0);
                field[18] = new JDBField("sertif", 'C', 253, 0);
                field[19] = new JDBField("akciz", 'C', 253, 0);
                field[20] = new JDBField("vst", 'C', 253, 0);
                field[21] = new JDBField("UNPPost", 'C', 20, 0);
                field[22] = new JDBField("UNPPol", 'C', 20, 0);
                field[23] = new JDBField("Seria", 'C', 10, 0);
                field[24] = new JDBField("Fasov", 'N', 6, 0);

            }

            if (v == 7) {
                // Электронная накладная формата Старого универмага
                field = new JDBField[16];
                record = new Object[16];

                field[0] = new JDBField("KODDOK", 'C', 20, 0);
                field[1] = new JDBField("BARCODE", 'C', 13, 0);
                field[2] = new JDBField("NAZV", 'C', 100, 0);
                field[3] = new JDBField("ED_IZM", 'C', 5, 0);
                field[4] = new JDBField("ART", 'C', 20, 0);
                field[5] = new JDBField("KOL", 'N', 10, 2);
                field[6] = new JDBField("CENA", 'N', 10, 2);
                field[7] = new JDBField("PNDS", 'N', 5, 2);
                field[8] = new JDBField("PNADB", 'N', 5, 2);
                field[9] = new JDBField("SNDS", 'N', 12, 2);
                field[10] = new JDBField("SVSEGO", 'N', 14, 2);
                field[11] = new JDBField("CENA_IZG", 'N', 10, 2);
                field[12] = new JDBField("FIKS", 'N', 1, 0);
                field[13] = new JDBField("VES", 'N', 1, 0);
                field[14] = new JDBField("SERT", 'C', 100, 0);
                field[15] = new JDBField("DOP_INFO", 'C', 100, 0);
            }

            if (v == 10) { //вид накладной для филиалов
                field = new JDBField[26];
                record = new Object[26];
                field[0] = new JDBField("ART", 'C', 10, 0);
                field[1] = new JDBField("MOD", 'C', 10, 0);
                field[2] = new JDBField("NAI", 'C', 30, 0);
                field[3] = new JDBField("SORT", 'N', 2, 0);
                field[4] = new JDBField("ROST", 'C', 3, 0);
                field[5] = new JDBField("NRA", 'C', 3, 0);
                field[6] = new JDBField("SIZEROST", 'C', 20, 0);
                field[7] = new JDBField("EAN_CODE", 'N', 12, 0);
                field[8] = new JDBField("SOSTAV", 'C', 64, 0);
                field[9] = new JDBField("SEX", 'C', 3, 0);
                field[10] = new JDBField("AGE", 'C', 20, 0);
                field[11] = new JDBField("COLLECTION", 'C', 40, 0);
                field[12] = new JDBField("SERT", 'C', 12, 0);
                field[13] = new JDBField("DSERT", 'D', 8, 0);
                field[14] = new JDBField("CENA", 'N', 11, 2);
                field[15] = new JDBField("PSKID", 'N', 7, 3);
                field[16] = new JDBField("NDSR", 'N', 5, 2);
                field[17] = new JDBField("NDSP", 'N', 5, 2);
                field[18] = new JDBField("TNV", 'N', 2, 0);
                field[19] = new JDBField("KOL", 'N', 9, 0);
                field[20] = new JDBField("CONT", 'C', 15, 0);
                field[21] = new JDBField("DGODN", 'D', 8, 0);
                field[22] = new JDBField("OPT", 'N', 2, 0);
                field[23] = new JDBField("NSP", 'N', 2, 0);
                field[24] = new JDBField("TOVGR", 'C', 45, 0);
                field[25] = new JDBField("EDIZM", 'C', 10, 0);
            }

            if (v == 11) { //накладной для ТриЦены
                field = new JDBField[14];
                record = new Object[14];
                field[0] = new JDBField("N", 'N', 4, 0);
                field[1] = new JDBField("Код_1С", 'C', 10, 0);
                field[2] = new JDBField("Атрикул", 'C', 15, 0);
                field[3] = new JDBField("Наименован", 'C', 45, 0);
                field[4] = new JDBField("Штрихкод", 'C', 13, 0);
                field[5] = new JDBField("Штрихкод2", 'C', 13, 0);
                field[6] = new JDBField("Количество", 'F', 16, 0);
                field[7] = new JDBField("Цена_б_НДС", 'F', 20, 2);
                field[8] = new JDBField("Стоимость", 'F', 20, 2);
                field[9] = new JDBField("Ставка_НДС", 'F', 7, 2);
                field[10] = new JDBField("Сумма_НДС", 'F', 20, 2);
                field[11] = new JDBField("Сумма", 'F', 20, 2);
                field[12] = new JDBField("Страна", 'C', 2, 0);
                field[13] = new JDBField("Код_ТНВЭД", 'F', 12, 0);
            }

            if (v == 99) { //Инвентаризация для старой линии
                field = new JDBField[13];
                record = new Object[13];
                field[0] = new JDBField("DOC_NUMBER", 'C', 20, 0);
                field[1] = new JDBField("DOC_DATE", 'D', 8, 0);
                field[2] = new JDBField("DOC_VAT", 'N', 18, 0);
                field[3] = new JDBField("DOC_TYPE", 'C', 20, 0);
                field[4] = new JDBField("ADDITION", 'C', 20, 0);
                field[5] = new JDBField("AMOUNT", 'N', 18, 0);
                field[6] = new JDBField("SUM_WSALE", 'N', 18, 2);
                field[7] = new JDBField("VAT", 'N', 18, 2);
                field[8] = new JDBField("SUM_ALLOW", 'N', 18, 2);
                field[9] = new JDBField("VAT_ALLOW", 'N', 18, 2);
                field[10] = new JDBField("ALL_VAT", 'N', 18, 2);
                field[11] = new JDBField("RET_CHILD", 'N', 18, 2);
                field[12] = new JDBField("RET_ADULT", 'N', 18, 2);

            }
        } catch (Exception e) {
            log.error("Ошибка при создании списка полей дбф: ", e);
            e.printStackTrace();
            //System.err.println("Ошибка при создании списка полей дбф: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при создании списка полей дбф: " + e, "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }


    }

    public void conn() {
        try {
            dbfw = new DBFWriter(path + "/" + nNakl + ".dbf", field, new String("cp866"));

        } catch (Exception e) {
            log.error("Ошибка при подключении к дбф: ", e);
            System.err.println("Ошибка при подключении к дбф: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при подключении к дбф:" + e, "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
    }


    public void disconn() {
        try {
            dbfw.close();
        } catch (Exception e) {
            log.error("Ошибка при закрытии дбф: ", e);
            System.err.println("Ошибка при закрытии дбф: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка при закрытии дбф:" + e, "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
    }


    public void write(Object[] r) throws Exception {
        try {
            dbfw.addRecord(r);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Ошибка формирования дбф накладной: ", e);
            System.err.println("Ошибка формирования дбф накладной: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка формирования дбф накладной: " + e, "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    public Vector readDBFPlan(String path) throws Exception {
        Vector o = new Vector();
        try {
            dbfr = new DBFReader(path);
            Object obj[] = new Object[dbfr.getFieldCount()];
            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                if (Long.parseLong(obj[4].toString().trim()) != 0 && Long.parseLong(obj[1].toString().trim()) == 737) {
                    o.add(obj[3]);
                    o.add(obj[4]);
                    if (obj[5] != null && !obj[5].toString().trim().equals("".toString())) o.add(obj[5]);
                    else o.add(0);
                }
            }
            dbfr.close();
        } catch (Exception e) {
            log.error("Ошибка чтения дбф плана: ", e);
            System.err.println("Ошибка чтения дбф плана: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e, "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            throw e;
        }
        return o;
    }

    public ArrayList readDBFSstoimost(String path) {
        ArrayList o = new ArrayList();
        try {
            dbfr = new DBFReader(path);
            Object obj[] = new Object[dbfr.getFieldCount()];
            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                o.add(obj);
            }
            dbfr.close();
        } catch (Exception e) {
            log.error("Ошибка чтения дбф плановой себестоимости: ", e);
            System.err.println("Ошибка чтения дбф плановой себестоимости: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e, "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return o;
    }

    public ArrayList readDBFClient(String path) {
        ArrayList<Object[]> o = new ArrayList();
        try {
            dbfr = new DBFReader(path);
            Object obj[] = new Object[dbfr.getFieldCount()];
            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                o.add(obj);
            }
            dbfr.close();
        } catch (Exception e) {
            log.error("Ошибка чтения дбф клиентов (nsi_pd): ", e);
            System.err.println("Ошибка чтения дбф клиентов (nsi_pd): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e, "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return o;
    }

    public Vector readDBFOtgruz(String path, int client) {
        Vector otgruz = new Vector();
        try {
            dbfr = new DBFReader(path);
            Object obj[] = new Object[dbfr.getFieldCount()];
            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                if (Integer.parseInt(obj[1].toString().trim()) == client) {
                    Vector tmp = new Vector();
                    tmp.add(obj[0].toString().trim());
                    tmp.add(obj[5].toString().trim());
                    tmp.add(obj[7].toString().trim());
                    tmp.add(obj[12].toString().trim());
                    tmp.add(obj[13].toString().trim());
                    tmp.add(obj[14].toString().trim());
                    otgruz.add(tmp);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка чтения дбф отгрузка: ", e);
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф отгрузка: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                dbfr.close();
            } catch (JDBFException ex) {
                log.error("Ошибка закрытия дбф отгрузка: ", ex);
                JOptionPane.showMessageDialog(null, "Ошибка закрытия дбф отгрузка: " + ex.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        return otgruz;
    }

    public ArrayList readDBF(String path) {
        ArrayList list = new ArrayList();
        try {
            dbfr = new DBFReader(path);
            Object obj[] = new Object[dbfr.getFieldCount()];
            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                list.add(obj);
            }
        } catch (Exception e) {
            log.error("Ошибка чтения дбф: ", e);
            JOptionPane.showMessageDialog(null, "Ошибка чтения дбф: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                dbfr.close();
            } catch (JDBFException ex) {
                log.error("Ошибка закрытия дбф: ", ex);
                JOptionPane.showMessageDialog(null, "Ошибка закрытия дбф: " + ex.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        return list;
    }

    /**
     * Возвращает список из элементов описывающих трудозатраты для продукта
     * (изделие описывается 3-мя подряд идущими эл-ми
     * 1-й артикул
     * 2-й модель
     * 3-й трудозатраты)
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList readTrudoZat() throws Exception {
        ArrayList ar = new ArrayList();
        try {
            Object obj[] = new Object[dbfr.getFieldCount()];
            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                ar.add(Integer.parseInt(obj[0].toString().trim()));
                ar.add(Integer.parseInt(obj[1].toString().trim()));
                ar.add(Float.parseFloat(obj[2].toString().trim()));
            }
            dbfr.close();
        } catch (Exception e) {
            log.error("Ошибка чтения дбф трудозатрат: ", e);
            System.err.println("Ошибка чтения дбф трудозатрат: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ошибка чтения трудозатрат: " + e, "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            throw e;
        }
        return ar;
    }
}
