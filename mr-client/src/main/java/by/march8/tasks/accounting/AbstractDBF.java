package by.march8.tasks.accounting;

import com.svcon.jdbf.JDBFException;
import workDB.DBFReader;
import workDB.DBFWriter;
import workDB.JDBField;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Andy
 */
public abstract class AbstractDBF {

    protected DBFWriter dbfWriter = null;
    protected DBFReader dbfReader = null;

    protected JDBField fields[] = null;

    protected String savePath;
    protected String fileName;
    protected String savePathFull;
    protected String codePage = "cp866";


    /**
     * Конструктор создания DBF файла
     *
     * @param savePath путь к файлу, без завершающего слэша
     * @param fileName имя файла, без расширения
     */
    public AbstractDBF(String savePath, String fileName) {
        try {
            this.savePath = savePath;
            this.fileName = fileName;

            File fC = new java.io.File(savePath);
            if (!fC.exists()) {
                new File(savePath).mkdir();
            }

            savePathFull = this.savePath + "/" + this.fileName + ".dbf";
        } catch (Exception e) {
            System.err.println("Ошибка при создании каталога [" + savePath + "]");
            e.printStackTrace();
        }
    }


    /**
     * Добавляет строку в файл
     *
     * @param item новая строка в файл
     */
    public void writeItem(Object[] item) {
        try {
            dbfWriter.addRecord(item);
        } catch (Exception e) {
            System.err.println("Ошибка формирования дбф накладной");
            e.printStackTrace();
        }
    }

    /**
     * Подключение к DBF файлу, по заранее созданной структуре. Создает файл с перезаписью существующего
     */
    public void connect() {
        try {
            dbfWriter = new DBFWriter(savePathFull, fields, codePage);
        } catch (Exception e) {
            System.err.println("Ошибка при подключении к дбф: " + savePathFull);
            e.printStackTrace();
        }

    }

    /**
     * Отключение от экземпляра DBF файла
     */
    public void disconnect() {
        try {
            dbfWriter.close();
        } catch (Exception e) {
            System.err.println("Ошибка при закрытии дбф: " + savePathFull);
            e.printStackTrace();
        }
    }

    /**
     * Чтение DBF и возвращает коллекцию строк
     *
     * @param path полный путь к DBF файлу с расширением (path_for_file/fileName.dbf)
     * @return коллекция строк
     */
    protected ArrayList readDBF(String path) {
        ArrayList list = new ArrayList();
        try {
            dbfReader = new DBFReader(path);
            Object obj[] = new Object[dbfReader.getFieldCount()];
            while (dbfReader.hasNextRecord()) {
                obj = dbfReader.nextRecord();
                list.add(obj);
            }
        } catch (Exception e) {
            System.err.println("Ошибка чтения дбф");
            e.printStackTrace();
        } finally {
            try {
                dbfReader.close();
            } catch (JDBFException ex) {
                System.err.println("Ошибка закрытия дбф: ");
                ex.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Устанавливает кодовую страницу файлу
     *
     * @param codePage кодовая страница
     */
    public void setCodePage(String codePage) {
        this.codePage = codePage;
    }

    public String getFilePath() {
        return savePathFull;
    }
}
