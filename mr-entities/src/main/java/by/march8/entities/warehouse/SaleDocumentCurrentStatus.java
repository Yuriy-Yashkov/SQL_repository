package by.march8.entities.warehouse;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Класс накладной для получения статуса документа
 *
 * @author Andy 22.02.2016.
 */
@Entity
@Table(name = "otgruz1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentCurrentStatus {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name = "status")
    protected int documentStatus;

    public SaleDocumentCurrentStatus() {

    }

    public SaleDocumentCurrentStatus(final SaleDocumentBase documentBase) {
        id = documentBase.getId();
    }

    public SaleDocumentCurrentStatus(final int documentBase) {
        id = documentBase;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(final int documentStatus) {
        this.documentStatus = documentStatus;
    }
}
