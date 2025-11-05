package by.march8.entities.documents;


import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by suvarov on 04.12.14.
 */
@Entity
@Table(name = "DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class DocumentEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME")
    @TableHeader(name = "НАИМЕНОВАНИЕ ДОКУМЕНТА", width = 200, sequence = 0)
    private String name;

    @Column(name = "NOTE")
    @TableHeader(name = "ПРИМЕЧАНИЕ", width = 200, sequence = 2)
    private String note;
    @Column(name = "ATTACHMENT") //!!!!!!!!!!!!
    @TableHeader(name = "ВЛОЖЕНИЕ", width = 200, sequence = 2)
    private byte[] attachment;

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    @ManyToOne
    @JoinColumn(name = "DOCUMENT_TYPE_ID")
    private DocumentTypeEntity documentType;

    public DocumentTypeEntity getDocumentType() {
        return documentType;
    }

    public void setDocumentType(final DocumentTypeEntity documentType) {
        this.documentType = documentType;
    }

    @TableHeader(name = "ТИП ДОКУМЕНТА", width = 200, sequence = 1)
    public String getDocumentTypeName() {
        try {
            return documentType.getName();
        } catch (RuntimeException e) {
            return "";
        }
    }

    //-----------------
    //DOCUMENT_ID
    @OneToMany(targetEntity = DocumentRelation.class, mappedBy = "document",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentRelation> documentRelations = new HashSet<DocumentRelation>();

    public Set<DocumentRelation> getDocumentRelations() {
        return this.documentRelations;
    }

    public void setDocumentRelations(final Set<DocumentRelation> documentRelations) {
        this.documentRelations = documentRelations;
    }

    //------------------
    //RELATED_DOCUMENT_ID
    @OneToMany(targetEntity = DocumentRelation.class, mappedBy = "relatedDocument",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentRelation> relatedDocumentRelations = new HashSet<DocumentRelation>();

    public Set<DocumentRelation> getRelatedDocumentRelations() {
        return this.relatedDocumentRelations;
    }

    public void setRelatedDocumentRelations(final Set<DocumentRelation> relatedDocumentRelations) {
        this.relatedDocumentRelations = relatedDocumentRelations;
    }

    //------------------
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        if ((name != null) && !name.isEmpty()) {
            return name;
        } else {
            return "";
        }
    }

    public String getNote() {
        return note;
    }


    public void setName(final String name) {
        this.name = name;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return getDocumentTypeName() + " " + this.getName();
    }

    public String getOrderByField() {
        return getName();
    }

}
