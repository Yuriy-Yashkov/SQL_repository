package by.march8.entities.storage;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 05.11.2018 - 9:22.
 */
@Entity
@Table(name="places2")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class StorageLocationsDetailItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private int id ;

    @Column(name="scan")
    private long barCode ;

    @Column(name = "part")
    private int batchNumber ;

    @Column(name="kol")
    private int amount ;

    @Column(name="kol_in_upack")
    private int amountInPack ;

    @Column(name="tip")
    private int labelType ;

    @Column(name = "doc_id")
    private int documentId ;

    @Column(name="kod_str")
    private long labelCode ;

    @Column(name="kod_izd")
    private int itemId ;

    @Column(name="ncw")
    private String itemColor ;

    @Column(name="srt")
    private int grade ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_ins")
    private Date insertDate ;

    @Column(name = "pc_ins")
    private String insertUser;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public long getBarCode() {
        return barCode;
    }

    public void setBarCode(long barCode) {
        this.barCode = barCode;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmountInPack() {
        return amountInPack;
    }

    public void setAmountInPack(int amountInPack) {
        this.amountInPack = amountInPack;
    }

    public int getLabelType() {
        return labelType;
    }

    public void setLabelType(int labelType) {
        this.labelType = labelType;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public long getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(long labelCode) {
        this.labelCode = labelCode;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemColor() {
        if(itemColor!=null) {
            return itemColor.trim();
        }else{
            return "";
        }
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public String getInsertUser() {
        if(insertUser!=null) {
            return insertUser.trim();
        }else{
            return "";
        }
    }

    public void setInsertUser(String insertUser) {
        this.insertUser = insertUser;
    }
}
