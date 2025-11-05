package by.march8.entities.documents;


import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by suvarov on 04.12.14.
 */
@Entity
@Table(name = "DOCUMENT_RELATION")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class DocumentRelation extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "DOCUMENT_ID")
    private DocumentEntity document;
    @ManyToOne
    @JoinColumn(name = "RELATED_DOCUMENT_ID")
    private DocumentEntity relatedDocument;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public DocumentEntity getDocument() {
        return document;
    }

    public void setDocument(final DocumentEntity document) {
        this.document = document;
    }

    public DocumentEntity getRelatedDocument() {
        return relatedDocument;
    }

    public void setRelatedDocument(final DocumentEntity relatedDocument) {
        this.relatedDocument = relatedDocument;
    }

    @TableHeader(name = "ОСНОВНОЙ ДОКУМЕНТ", width = 200, sequence = 0)
    public String getDocumentName() {
        return document.getName();
    }

    @TableHeader(name = "ПОДЧИНЁННЫЙ ДОКУМЕНТ", width = 200, sequence = 1)
    public String getRelatedDocumentName() {
        try {
            return relatedDocument.getName();
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        if (getRelatedDocumentName() != null)
            return "Основной: " + getDocumentName() + "; Подчинённый: " + getRelatedDocumentName();
        else
            return "Основной: " + getDocumentName();
    }

    public String getOrderByField() {
        return getDocumentName();
    }
}
