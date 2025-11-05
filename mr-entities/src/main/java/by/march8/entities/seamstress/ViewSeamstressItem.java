package by.march8.entities.seamstress;

import by.march8.api.TableHeader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author tmp on 27.12.2021 11:01
 */
@XmlRootElement(name = "ReportItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewSeamstressItem {
    private int id;
    @TableHeader(name = "Документ_№", sequence = 10)
    private int documentId ;
    @TableHeader(name = "Документ_Дата", sequence = 15)
    private Date documentDate ;

    @TableHeader(name = "ФИО швеи",width = 150, sequence = 20)
    private String workerName ;
    @TableHeader(name = "Таб.№", sequence = 30)
    private int workerNumber ;
    @TableHeader(name = "Код бригады", sequence = 40)
    private int brigadeCode ;
    @TableHeader(name = "Модель", sequence = 50)
    private int modelNumber ;
    @TableHeader(name = "Операция_Код", sequence = 60)
    private int operationType ;
    @TableHeader(name = "Операция_Наименование",width = 150, sequence = 70)
    private String operationName ;
    @TableHeader(name = "Оборудование",width = 150, sequence = 80)
    private String machineName;
    @TableHeader(name = "Кол-во", sequence = 90)
    private int amount ;
    @TableHeader(name = "Норма_Ед. изд", sequence = 100)
    private double normRate;
    @TableHeader(name = "Норма_Кол-во", sequence = 110)
    private double normRateAmount;
    @TableHeader(name = "Бригадир",width = 150, sequence = 120)
    private String brigadierName ;

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }

    public void setWorkerNumber(int workerNumber) {
        this.workerNumber = workerNumber;
    }

    public int getBrigadeCode() {
        return brigadeCode;
    }

    public void setBrigadeCode(int brigadeCode) {
        this.brigadeCode = brigadeCode;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getNormRate() {
        return normRate;
    }

    public void setNormRate(double normRate) {
        this.normRate = normRate;
    }

    public double getNormRateAmount() {
        return normRateAmount;
    }

    public void setNormRateAmount(double normRateAmount) {
        this.normRateAmount = normRateAmount;
    }

    public String getBrigadierName() {
        return brigadierName;
    }

    public void setBrigadierName(String brigadierName) {
        this.brigadierName = brigadierName;
    }
}
