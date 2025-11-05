package by.gomel.freedev.ucframework.ucswing.filechooserfilters;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by suvarov on 08.12.14.
 * Фильтр типов документов
 */
public class ExtFilter {
    /**
     * Статический метод создающий фильтр по типу одного или набора файлов
     * @param type Тип документа
     * @return расширенный фильт документа
     */
    public static FileNameExtensionFilter extFilterName(FilterType type) {
        FileNameExtensionFilter filter = null;
        switch (type) {
            case TXT:
                filter = new FileNameExtensionFilter("Текстовый документ (*.txt)", "txt");
                break;
            case PDF:
                filter = new FileNameExtensionFilter("Adobe Document (*.pdf)", "pdf");
                break;
            case OTS:
                filter = new FileNameExtensionFilter("Шаблон OpenOffice (*.ots)", "ots");
                break;
            case EXCEL:
                filter = new FileNameExtensionFilter("Документ Excel (*.xls)", "xls");
                break;
            case DOC:
                filter = new FileNameExtensionFilter("Документ Word 97-2003 (*.doc)", "doc");
                break;
            case DOCX:
                filter = new FileNameExtensionFilter("Документ Word (*.docx)", "docx");
                break;
            case JPG:
                filter = new FileNameExtensionFilter("JPG (*.jpg;*.jpeg;*.jpe;*.jfif)", "jpg", "jpeg", "jpe", "jfif");
                break;
            case PNG:
                filter = new FileNameExtensionFilter("PNG (*.png)", "png");
                break;
            case BMP:
                filter = new FileNameExtensionFilter("BMP (*.bmp)", "bmp");
                break;
            case GIF:
                filter = new FileNameExtensionFilter("GIF (*.gif)", "gif");
                break;
            case IMAGES:
                filter = new FileNameExtensionFilter("Картинки", "jpg", "png", "bmp", "tiff", "gif");
                break;
            case JRXML:
                filter = new FileNameExtensionFilter("Шаблон JasperReports (*.jrxml)", "jrxml");
                break;
        }
        return filter;
    }

    /**
     * Статический метод добавляющий к объекту типа JFileChooser набор фильтров по типу документа
     * @param fileChooser ссылка на диалог открытия документа
     * @param type тип документа
     */
    public static void addFiltersByDocumentType(JFileChooser fileChooser, FilterType type) {
        switch (type) {
            case TEMPLATES:
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.OTS));
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.JRXML));
                break;
            case IMAGES:
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.JPG));
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.BMP));
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.GIF));
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.PNG));
                break;
            case DOCUMENTS:
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.TXT));
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.DOC));
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.DOCX));
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.PDF));
                fileChooser.addChoosableFileFilter(extFilterName(FilterType.EXCEL));
                break;
        }
    }
}
