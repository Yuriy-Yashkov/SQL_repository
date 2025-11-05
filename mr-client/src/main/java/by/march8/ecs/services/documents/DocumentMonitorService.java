package by.march8.ecs.services.documents;

import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.march8.ecs.services.IService;
import by.march8.ecs.services.documents.model.DocumentItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentMonitorService implements IService {
    private static DocumentMonitorService instance;
    private List<DocumentItem> documentList;


    public DocumentMonitorService() {
        documentList = new ArrayList<>();
    }

    public static DocumentMonitorService getInstance() {
        if (instance == null) {
            instance = new DocumentMonitorService();
        }
        return instance;
    }

    public List<DocumentItem> getDocuments() {
        return documentList;
    }

    public DocumentItem addDocument(File file) {
        DocumentItem item = new DocumentItem();
        item.setFile(file);
        String[] file_ = file.getName().split(".");
        if (file_.length > 1) {
            String ext = file_[file_.length - 1];
            if (ext.toLowerCase().equals("dbf")) {
                item.setType(DocumentType.DOCUMENT_DBF);
            }
            if (ext.toLowerCase().equals("ods")) {
                item.setType(DocumentType.DOCUMENT_ODS);
            }
        }
        documentList.add(item);

        return item;
    }

    public DocumentItem addDocument(String file) {
        return addDocument(new File(file));
    }

    public DocumentItem getLastDocument() {
        if (documentList.isEmpty()) {
            return null;
        }
        return documentList.get(documentList.size() - 1);
    }


}
