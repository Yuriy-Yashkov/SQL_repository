package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "SaleDocumentItemExportView.findByDocumentId",
                query = "SELECT document FROM SaleDocumentItemExportView document WHERE document.documentId = :documentId ")
})

@Entity
@Table(name="VIEW_SALE_DOCUMENT_EXPORT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentItemExportView extends BaseEntity {
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "DOCUMENT_ID")
    private int documentId ;

    @Column(name = "PRODUCT_ID")
    private int productId ;

    @Column(name="COLOR")
    private String color ;

    @Column(name = "AMOUNT")
    private int amount ;

    @Column(name="ACCOUNTING_PRICE")
    private double accountingPrice;

    @Column(name = "VAT")
    private double vat ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getAccountingPrice() {
        return accountingPrice;
    }

    public void setAccountingPrice(double accountimgPrice) {
        this.accountingPrice = accountimgPrice;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }
}
