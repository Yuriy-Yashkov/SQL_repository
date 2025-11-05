package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 29.09.2015.
 */
@Entity
@Table(name = "otgruz1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentAmount extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Количество полное
     */
    @Column(name = "kola")
    private int amountAll;
    /**
     * Количество упаковок
     */
    @Column(name = "kolk")
    private int amountPack;
    /**
     * Количество россыпью
     */
    @Column(name = "kolr")
    private int amountNotPack;

    @Column(name = "operac")
    private String documentType ;


    public String getItemAmount(){
        if (documentType!=null){
            if(documentType.trim().equals(InvoiceType.DOCUMENT_SALE_MATERIAL)||documentType.trim().equals(InvoiceType.DOCUMENT_REFUND_MATERIAL)){
                double weight = (double)amountAll / 100 ;
                return "Вес материала, всего: "+weight+" кг." ;
            }else{
                return "Всего единиц: " + amountAll + " "
                        + "В упаковках: " + (amountAll - amountNotPack) + " "
                        + "Россыпью: " + amountNotPack + " " + "Упаковок: " + amountPack;
            }
        }
        return "Нет данных";
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getAmountAll() {
        return amountAll;
    }

    public void setAmountAll(final int amountAll) {
        this.amountAll = amountAll;
    }

    public int getAmountPack() {
        return amountPack;
    }

    public void setAmountPack(final int amountPack) {
        this.amountPack = amountPack;
    }

    public int getAmountNotPack() {
        return amountNotPack;
    }

    public void setAmountNotPack(final int amountNotPack) {
        this.amountNotPack = amountNotPack;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(final String documentType) {
        this.documentType = documentType;
    }
}
