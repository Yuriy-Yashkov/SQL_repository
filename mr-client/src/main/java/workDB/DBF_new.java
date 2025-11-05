package workDB;

import com.svcon.jdbf.DBFReader;
//import org.apache.log4j.Logger;

/**
 *
 * @author vova
 * @date 19.12.2011
 */
public class DBF_new {
    public DBFReader dbfr = null;
    // private static final Logger log = new Log().getLoger(DBF.class);
    JDBField field[] = null;
    Object record[] = null;
    DBFWriter dbfw = null;
    String path = null;
    String nNakl = null;

    public DBF_new() {

    }

    /**
     * Клнструктор для записи в ДБФ
     * @param path -- путь к ДБФ
     * @param field -- массив данных
     * @param encoding -- кодировка для записи
     * @date 19.12.2011
     */
    public DBF_new(String path, JDBField field[], String encoding) {
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

}
