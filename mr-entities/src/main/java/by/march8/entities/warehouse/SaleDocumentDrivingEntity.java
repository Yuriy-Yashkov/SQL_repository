package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Класс описывающий путевой лист и реквизиты накладной.
 *
 * @author Andy 11.11.2015.
 */
@Entity
@Table(name = "DRIVING_DIRECTION_DOCUMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentDrivingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "SALE_DOCUMENT_ID", nullable = true, insertable = true, updatable = true, precision = 0)
    private int saleDocumentId;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getSaleDocumentId() {
        return saleDocumentId;
    }

    public void setSaleDocumentId(final int saleDocumentId) {
        this.saleDocumentId = saleDocumentId;
    }
}
