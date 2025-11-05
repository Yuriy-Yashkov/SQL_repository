package by.march8.entities.documents;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andy 04.12.14.
 */
@Entity
@Table(name = "DOCUMENT_TYPE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class DocumentTypeEntity extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME")
    @TableHeader(name = "ТИП ДОКУМЕНТА", width = 100, sequence = 0)
    private String name;

    @Column(name = "NOTE")
    @TableHeader(name = "ПРИМЕЧАНИЕ", width = 100, sequence = 1)
    private String note;

    @OneToMany(targetEntity = DocumentEntity.class, mappedBy = "documentType",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentEntity> documents = new HashSet<>();



    public Set<DocumentEntity> getDocuments() {
        return this.documents;
    }
    public void setDocuments(final Set<DocumentEntity> documents) {
        this.documents = documents;
    }
    @Override
    public int getId() {
        return id;
    }
    public String getName() {
        if ((name == null) || name.isEmpty()) {
            return "";
        } else {
            return name;
        }
    }
    public String getNote() {
        return note;
    }
    @Override
    public void setId(final int id) {
        this.id = id;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    public String getOrderByField(){
        return getName();
    }
}
