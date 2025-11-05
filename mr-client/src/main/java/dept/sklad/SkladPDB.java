package dept.sklad;

import by.march8.ecs.framework.common.LogCrutch;
import by.march8.ecs.framework.common.model.CurrencySet;
import common.DateUtils;
import common.Valuta;
import workDB.PDB_new;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/**
 * @author vova
 * @date 30.01.2012
 */
public class SkladPDB extends PDB_new {
    //private static final Logger log = new Log().getLoger(SkladPDB.class);
    private static final LogCrutch log = new LogCrutch();

    /**
     * Возвращает список объектов валют
     *
     * @return ArrayList(Valuta)
     * @date 30.01.2012
     * @author vova
     */
    public ArrayList getValutaList() {
        ArrayList<Valuta> items = new ArrayList();
        try {
            String query = "select id, name, full_name, symbol, about from valuta";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Valuta v = new Valuta(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                items.add(v);
            }
        } catch (Exception e) {
            log.error("Ошибка при выполнении функции getValutaList() ", e);
            System.err.println("Ошибка getValutaList() " + e);
            return (ArrayList) Collections.EMPTY_LIST;
        }
        return items;
    }

    /**
     * Возвращает курс валюту относительно российского рубля
     *
     * @param id -- код валюты
     * @return курс float
     * @date 30.01.2012
     * @author vova
     */
    public float getValutaKurs(int id) {
        float kurs = 1;
        try {
            String query = "select kurs from (select id_valuta1, id_valuta2, max(date_start) as ds from valuta_kurs where id_valuta1 = ? and id_valuta2 = 17 " +
                    " group by id_valuta1, id_valuta2 having max(date_start) <= now()) as t1 " +
                    " left join (select max(id)as id, date_start,id_valuta1, id_valuta2  from valuta_kurs group by id_valuta1, id_valuta2 ,date_start) as t2  " +
                    " on t2.date_start = t1.ds and t2.id_valuta1 = t1.id_valuta1 and t2.id_valuta2 = t1.id_valuta2  " +
                    " left join (select id, id_valuta1, id_valuta2, kurs, date_start from valuta_kurs) as t3 on t2.id = t3.id ";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                kurs = rs.getFloat(1);
            } else {
                query = "select kurs from (select id_valuta1, id_valuta2, max(date_start) as ds from valuta_kurs where id_valuta1 = 17 and id_valuta2 = ? " +
                        " group by id_valuta1, id_valuta2 having max(date_start) <= now()) as t1 " +
                        " left join (select max(id)as id, date_start,id_valuta1, id_valuta2  from valuta_kurs group by id_valuta1, id_valuta2 ,date_start) as t2  " +
                        " on t2.date_start = t1.ds and t2.id_valuta1 = t1.id_valuta1 and t2.id_valuta2 = t1.id_valuta2  " +
                        " left join (select id, id_valuta1, id_valuta2, kurs, date_start from valuta_kurs) as t3 on t2.id = t3.id ";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    kurs = 1 / rs.getFloat(1);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при выполнении функции getValutaList() ", e);
            System.err.println("Ошибка getValutaList() " + e);
        }
        return kurs;
    }

    /**
     *
     * @param currency Буквенный  код валюты
     * @param dateOperation дата курса
     * @param isNow признак первого дня месяца. Если true - дата в пределах месяца не важна, будет выбран курс на начало месяца
     * @return курс валюты
     */
    public float getCurrencyRate(String currency, Date dateOperation, boolean isNow) {
        String currencyTemp = currency.toLowerCase();
        int currencyId = 0;

        if (currencyTemp.equals("byr")) {
            currencyId = 16;
        } else if (currencyTemp.equals("rub")) {
            currencyId = 17;
        } else if (currencyTemp.equals("eur")) {
            currencyId = 19;
        } else if (currencyTemp.equals("uah")) {
            currencyId = 18;
        } else if (currencyTemp.equals("usd")) {
            currencyId = 15;
        }

        Date first;

        if (isNow) {
            first = dateOperation;
        } else {
            first = DateUtils.getFirstDay(dateOperation);
        }

        try {
            String query = "select * from valuta_kurs where date_start = ? and id_valuta1 = 16 and id_valuta2 = ? ORDER BY date_start DESC limit 1";
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(first.getTime()));
            ps.setInt(2, currencyId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat(4);
            } else {
                return 0;
            }

        } catch (Exception e) {
            System.out.println("Ошибка метода getCurrencyRate(String currency, Date dateOperation)");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Метод возвращает набор курсов на опредлеленную дату
     * @param dateOperation дата курса
     * @param isNow признак первого дня месяца. Если true - дата в пределах месяца не важна, будет выбран курс на начало месяца
     * @return массив курсов валюты
     */
    public CurrencySet[] getCurrencyRateAsArray(Date dateOperation, boolean isNow) {
        CurrencySet[] result = new CurrencySet[4];

        Date first;

        if (isNow) {
            first = dateOperation;
        } else {
            first = DateUtils.getFirstDay(dateOperation);
        }

        result[0] = new CurrencySet();
        result[0].setId(17);
        result[0].setName("rub");
        result[0].setDate(first);

        result[1] = new CurrencySet();
        result[1].setId(15);
        result[1].setName("usd");
        result[1].setDate(first);

        result[2] = new CurrencySet();
        result[2].setId(19);
        result[2].setName("eur");
        result[2].setDate(first);

        result[3] = new CurrencySet();
        result[3].setId(18);
        result[3].setName("uah");
        result[3].setDate(first);

        result[0].setRate(getCurrencyRate("rub", dateOperation, isNow));
        result[1].setRate(getCurrencyRate("usd", dateOperation, isNow));
        result[2].setRate(getCurrencyRate("eur", dateOperation, isNow));
        result[3].setRate(getCurrencyRate("uah", dateOperation, isNow));

        return result;
    }
}
