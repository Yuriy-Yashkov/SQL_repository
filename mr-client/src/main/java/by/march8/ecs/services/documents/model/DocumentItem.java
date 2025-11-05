package by.march8.ecs.services.documents.model;

import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;

import java.io.File;

public class DocumentItem {
    private String fileName;
    private File file;
    private DocumentType type;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }
}
