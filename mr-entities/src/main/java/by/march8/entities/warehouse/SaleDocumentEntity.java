package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "SaleDocumentEntity.findAllPerPeriod",
                query = "SELECT doc FROM SaleDocumentEntity doc WHERE doc.documentStatus <> 1 and doc.documentDate BETWEEN :beginDate AND :endDate " +
                        "order by " +
                        "doc.documentDate ASC "),

        @NamedQuery(name = "SaleDocumentEntity.findByNumber",
                query = "SELECT doc FROM SaleDocumentEntity doc WHERE doc.documentNumber = :number ")

})

/**
 * Описание основных параметров таблицы otgruz1 программы March8
 * Created by Andy on 18.08.2015.
 */
@Entity
@Table(name = "otgruz1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentEntity extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Дата создания документа
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    @TableHeader(name = "Дата", sequence = 10)
    private Date documentDate;
    /**
     * Номер документа
     */
    @Column(name = "ndoc")
    private String documentNumber;

    @Column(name = "status")
    @TableHeader(name = "Status", width = 0 , sequence = 200)
    protected int documentStatus;

    @Transient
    private boolean isCalculated ;

    @Column(name = "export")
    protected int isExport;

    @Column(name="adjustment_type")
    private int adjustmentType ;

    @Column(name="adjustment_ndoc")
    private int adjustmentDocumentId;

    @Column(name = "kurs_bank")
    private float cursInSaleDate;

    public float getCursInSaleDate() {
        return cursInSaleDate;
    }

    public void setCursInSaleDate(float cursInSaleDate) {
        this.cursInSaleDate = cursInSaleDate;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(final Date documentDate) {
        this.documentDate = documentDate;
    }

    @TableHeader(name = "Номер", sequence = 20)
    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
        } else {
            return "";
        }
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(final int documentStatus) {
        this.documentStatus = documentStatus;
    }

    public int getIsExport() {
        return isExport;
    }

    public void setIsExport(final int isExport) {
        this.isExport = isExport;
    }

    @Override
    public String toString() {
        return getDocumentNumber();
    }

    public int getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(int adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public int getAdjustmentDocumentId() {
        return adjustmentDocumentId;
    }

    public void setAdjustmentDocumentId(int adjustmentDocumentNumber) {
        this.adjustmentDocumentId = adjustmentDocumentNumber;
    }

    @TableHeader(name = "Calc", width = 0 ,sequence = 300)
    public boolean isCalculated() {
        return isCalculated;
    }

    public void setCalculated(boolean calculated) {
        isCalculated = calculated;
    }

}

