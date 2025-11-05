package by.gomel.freedev.ucframework.uccore.report.api.openoffice;


import by.gomel.freedev.ucframework.uccore.report.model.DocumentProperty;
import com.sun.star.beans.PropertyValue;

/** Тип документа
 * Created by Andy on 17.10.2014.
 */
public enum DocumentType {
    /**
     * Тип файла - документ OpenOffice (*.ods)
     */
    DOCUMENT_ODS("OpenOffice Calc documents", "ods", new DocumentProperty[]{new DocumentProperty("", "")}),

    DOCUMENT_ODT("OpenOffice Write documents", "odt", new DocumentProperty[]{new DocumentProperty("", "")}),
    /**
     * Тип файла - документ Microsoft Excel 97 (*.xls)
     */
    DOCUMENT_XLS("Microsoft Excel documents", "xls", new DocumentProperty[]{new DocumentProperty("FilterName", "MS Excel 97")}),

    DOCUMENT_XLSX("Microsoft Excel documents with macros", "xlsx", new DocumentProperty[]{new DocumentProperty("FilterName", "Calc MS Excel 2007 XML")}),
    DOCUMENT_XLSM("Microsoft Excel documents XLSX", "xlsm", new DocumentProperty[]{new DocumentProperty("FilterName", "Calc8")}),
    /**
     * Тип файла - таблица dBase (*.dbf)
     */
    DOCUMENT_DBF("dBase files", "dbf", new DocumentProperty[]{new DocumentProperty("FilterName", "dBase"), new DocumentProperty("FilterOptions", "30")});


    private final String description;
    private final String extension;
    private final DocumentProperty[] properties;


    DocumentType(final String description, final String extension, final DocumentProperty[] properties) {
        this.description = description;
        this.extension = extension;
        this.properties = properties;
    }

    /**
     * Метод возвращает Свойства документа для определенного формата файла
     */
    public PropertyValue[] getProperties() {
        PropertyValue[] result = new PropertyValue[properties.length];
        for (int i = 0; i < properties.length; i++) {

            PropertyValue newItem = new PropertyValue();

            newItem.Name = properties[i].getKey();
            newItem.Value = properties[i].getValue();

            result[i] = newItem;
        }
        return result;
    }

    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }
}
