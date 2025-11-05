package by.march8.entities.unknowns;

/**
 * @author Andy 30.10.2015.
 */

import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;


@Entity
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@Table(name = "user_scan")
public class ScanCodeEntity {

    @Id
    @Column(name = "scan")
    protected long scanCode;

    /**
     * Количество единиц изделия по коду (для кодов с упаковки всегда больше 1)
     */
    @Column(name = "kol")
    protected int amount;

    /**
     * Тип изделия: 1 - россыпь, 2 - упаковка
     */
    @Column(name = "tip")
    protected int type;


    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "time_ins")
    private Date recordDate ;

    @Column(name = "pc_ins")
    private String computerName ;

    @Column(name="doc_id")
    private int documentId ;

    @Column(name="kod_str")
    private int codeLabel ;

    @Column(name="rzm_marh")
    private int itemSize ;

    @Column(name="rst_marh")
    private int itemGrowth ;

    @Column(name="alias")
    private String aliasName ;

    @Column(name = "kol_in_upack")
    private int amountInPack;

    @Column(name="kod_izd")
    private int codeItem ;

    @Column(name = "srt")
    private int itemGrade ;

    @Column(name="ncw")
    private String itemColor ;

    @Column(name = "kod_kld")
    private int codeNSI ;

    @Column(name="kod_marh")
    private int codeTrace ;

    @Column(name = "uservrkv")
    private String userName ;


    public ScanCodeEntity(final long scanCode, final int amount, final int type) {
        this.scanCode = scanCode;
        this.amount = amount ;
        this.type = type ;

        itemColor="NO COLOR";
        recordDate = new Date();
    }

    public ScanCodeEntity() {

    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(final Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(final String computerName) {
        this.computerName = computerName;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final int documentId) {
        this.documentId = documentId;
    }

    public int getCodeLabel() {
        return codeLabel;
    }

    public void setCodeLabel(final int codeLabel) {
        this.codeLabel = codeLabel;
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(final int itemSize) {
        this.itemSize = itemSize;
    }

    public int getItemGrowth() {
        return itemGrowth;
    }

    public void setItemGrowth(final int itemGrowth) {
        this.itemGrowth = itemGrowth;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(final String aliasName) {
        this.aliasName = aliasName;
    }

    public int getAmountInPack() {
        return amountInPack;
    }

    public void setAmountInPack(final int amountInPack) {
        this.amountInPack = amountInPack;
    }

    public int getCodeItem() {
        return codeItem;
    }

    public void setCodeItem(final int codeItem) {
        this.codeItem = codeItem;
    }

    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(final int itemGrade) {
        this.itemGrade = itemGrade;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(final String itemColor) {
        this.itemColor = itemColor;
    }

    public int getCodeNSI() {
        return codeNSI;
    }

    public void setCodeNSI(final int codeNSI) {
        this.codeNSI = codeNSI;
    }

    public int getCodeTrace() {
        return codeTrace;
    }

    public void setCodeTrace(final int codeTrace) {
        this.codeTrace = codeTrace;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
        this.computerName = userName ;
    }

    public long getScanCode() {
        return scanCode;
    }

    public void setScanCode(final long scanCode) {
        this.scanCode = scanCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }
}
