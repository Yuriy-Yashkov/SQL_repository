package by.gomel.freedev.ucframework.uccore.report.model;


import by.gomel.freedev.ucframework.uccore.report.enums.ReportEnum;

/**
 * Параметры отчета. Описаны настройки и параметры действий перед и после
 * формирования документа.
 * <p>
 * Created by Andy on 15.10.2014.
 */
public class ReportProperties {
    /**
     * Старый перечислитель, так и не реализованный в полной мере
     */
    private ReportEnum name;
    /**
     * Шаблон отчета
     */
    private ReportTemplate template;
    /**
     * Префикс имени файла
     */
    private String blankName;
    /**
     * Для отчета показывать диалог на печать
     */
    private boolean printable;
    /**
     * Для отчета показывать диалог сохранения
     */
    private boolean storable;

    /**
     * Отрывать отчет после сохранения
     */
    private boolean openable;

    /**
     * Примечание и заметки к отчету
     */
    private String note;
    private String savePath;

    public ReportEnum getName() {
        return name;
    }

    public void setName(final ReportEnum name) {
        this.name = name;
    }

    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(final boolean printable) {
        this.printable = printable;
    }

    public boolean isStorable() {
        return storable;
    }

    public void setStorable(final boolean storable) {
        this.storable = storable;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public ReportTemplate getTemplate() {
        return template;
    }

    public void setTemplate(final ReportTemplate template) {
        this.template = template;
    }

    public String getBlankName() {
        return blankName;
    }

    public void setBlankName(final String blankName) {
        this.blankName = blankName;
    }

    public boolean isOpenable() {
        return openable;
    }

    public void setOpenable(final boolean openable) {
        this.openable = openable;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}
