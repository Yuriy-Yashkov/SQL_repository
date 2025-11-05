package by.gomel.freedev.ucframework.ucswing.filechooserfilters;

import by.march8.entities.documents.Document;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Расширенный фильтр файлов
 * Created by suvarov on 08.12.14.
 */
public class ExtFileFilter extends FileFilter {

    String ext;
    String description;

    ExtFileFilter(String ext, String descr) {
        this.ext = ext;
        description = descr;
    }

    /**
     * Метод возвращающий по файлу его расширение
     *
     * @param f ссылка на файл
     * @return расширение файла
     */
    public static String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    /**
     * Метод возвращает имя файла с его расширением на диске при его наличии,
     * иначе сначала загружает файл из базы и сохраняет его на диск.
     *
     * @param templateName имя файла шаблона
     * @return путь к файлу на диске
     */
    @SuppressWarnings("unused")
    public static String getTemplatePathByName(String templateName) {
        //Путь к файлу
        String dirname = "D:/Users/User/Documents/";
        File file = new File(dirname + templateName);
        int fileExt = 0;
        byte[] attachment;
        FileOutputStream outputStream;
        final String TEMPLATE_EXTENSION = ".ots";
        String tempPath = null;
        Document document;
        //Если указан тип файла fileExt=1
        if (getExtension(file) != null) fileExt = 1;
        switch (fileExt) {
            case 0: {   //если файл уже есть, возвращаем его имя
                if (new File(file + TEMPLATE_EXTENSION).exists())
                    tempPath = file.getName() + TEMPLATE_EXTENSION;
                else {
                    try {
                        ArrayList<Object> objectsList = new ArrayList<>();
                        //Вытаскиваем объект с заданным полем name из базы
                        //ArrayList<Object> objectsList=ReferencesDao.loadDataForFile(Document.class,"Select c from Document c where c.name='" + templateName + "'");
                        document = (Document) objectsList.get(0);
                        outputStream = new FileOutputStream(file + TEMPLATE_EXTENSION);
                        // "вложение"
                        attachment = document.getAttachment();
                        outputStream.write(attachment);
                        outputStream.close();
                        tempPath = file.getName() + TEMPLATE_EXTENSION;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case 1: {
                try {
                    ArrayList<Object> objectsList = new ArrayList<>();
                    //ArrayList<Object> objectsList=ReferencesDao.loadDataForFile(Document.class,"Select c from Document c where c.name='" + templateName + "'");
                    document = (Document) objectsList.get(0);
                    outputStream = new FileOutputStream(file);
                    // "вложение"
                    attachment = document.getAttachment();
                    outputStream.write(attachment);
                    outputStream.close();
                    tempPath = file.getName() + TEMPLATE_EXTENSION;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return tempPath;
    }

    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension == null)
                return (ext.length() == 0);
            return ext.equals(extension);
        }
        return false;
    }

    public String getDescription() {
        return description;
    }
}