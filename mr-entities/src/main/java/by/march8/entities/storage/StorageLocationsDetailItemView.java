package by.march8.entities.storage;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(name = "StorageLocationsDetailItemView.findByDocumentId",
                query = "SELECT detail FROM StorageLocationsDetailItemView detail WHERE detail.documentId = :document ")
})

/**
 * @author Andy 03.11.2018 - 12:22.
 */
@Entity
@Table(name="V_STORAGE_LOCATIONS_DETAIL_ITEM")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class StorageLocationsDetailItemView extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="NAME")

    private String itemName ;

    @Column(name="MODEL")
    private String itemModel ;

    @Column(name="ARTICLE")
    private String itemArticle;

    @Column(name="SIZE")
    private String itemPrintSize ;

    @Column(name="GRADE")
    private int itemGrade ;

    @Column(name = "COLOR")
    private String itemColor ;

    @Column(name="AMOUNT")
    private int amount ;

    @Column(name="AMOUNT_IN_PACK")
    private int amountInPack ;

    @Column(name="BARCODE")
    private long barCode ;

    @Column(name="LABEL_TYPE")
    private int labelType;

    @Column(name = "BATCH")
    private int batchNumber;

    @Column(name="DOCUMENT_ID")
    private int documentId ;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Готовая продукция_Наименование", sequence = 30)
    public String getItemName() {
        if(itemName!=null) {
            return itemName.trim();
        }else{
            return "" ;
        }
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    @TableHeader(name = "Готовая продукция_Модель", width = -80, sequence = 10)
    public Integer getItemModel() {
        if(itemModel!=null) {
            return Integer.valueOf(itemModel.trim());
        }else{
            return 0 ;
        }
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    @TableHeader(name = "Готовая продукция_Артикул", width = -80, sequence = 20)
    public String getItemArticle() {
        if(itemArticle !=null) {
            return itemArticle.trim();
        }else{
            return "" ;
        }
    }

    public void setItemArticle(String itemArcticle) {
        this.itemArticle = itemArcticle;
    }

    @TableHeader(name = "Готовая продукция_Размер", sequence = 40)
    public String getItemPrintSize() {
        if(itemPrintSize!=null) {
            return itemPrintSize.trim();
        }else{
            return "" ;
        }
    }

    public void setItemPrintSize(String itemPrintSize) {
        this.itemPrintSize = itemPrintSize;
    }

    @TableHeader(name = "Готовая продукция_Сорт", width = -50, sequence = 100)
    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(int itemGrade) {
        this.itemGrade = itemGrade;
    }

    @TableHeader(name = "Готовая продукция_Цвет", sequence = 110)
    public String getItemColor() {
        if(itemColor!=null) {
            return itemColor.trim();
        }else{
            return "" ;
        }
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    @TableHeader(name = "Количество_Упак.", width = -50, sequence = 120)
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    @TableHeader(name = "Количество_В упак.", width = -50, sequence = 125)
    public Integer getAmountInPack() {
        return amountInPack;
    }

    public void setAmountInPack(int amountInPack) {
        this.amountInPack = amountInPack;
    }

    @TableHeader(name = "Штрихкод", width = -100, sequence = 130)
    public Long getBarCode() {
        return barCode;
    }

    public void setBarCode(long barCode) {
        this.barCode = barCode;
    }

    public int getLabelType() {
        return labelType;
    }

    public void setLabelType(int type) {
        this.labelType = type;
    }

    @TableHeader(name = "Партия", width = -50, sequence = 140)
    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int patchNumber) {
        this.batchNumber = patchNumber;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }
}
