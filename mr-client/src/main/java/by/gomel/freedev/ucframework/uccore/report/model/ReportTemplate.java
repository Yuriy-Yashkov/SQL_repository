package by.gomel.freedev.ucframework.uccore.report.model;

import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;

/**
 * Created by Andy on 14.10.2014.
 */
public class ReportTemplate {

    private String templateName;
    private String saveName;
    private String savePath;
    private DocumentType documentType;
    private boolean visible;

    /**
     * Флаг пррямого пути к файлу/шаблону, установить в случае выбора файла через диалог
     */
    private boolean directPath;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(final String saveName) {
        this.saveName = saveName;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(final String savePath) {
        this.savePath = savePath;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(final DocumentType documentType) {
        this.documentType = documentType;
    }

    public boolean isDirectPath() {
        return directPath;
    }

    public void setDirectPath(final boolean directPath) {
        this.directPath = directPath;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
