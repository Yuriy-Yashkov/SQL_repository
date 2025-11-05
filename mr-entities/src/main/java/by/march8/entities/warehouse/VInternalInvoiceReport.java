package by.march8.entities.warehouse;

import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @author Andy 17.12.2014.
 */
@NamedQueries({


        @NamedQuery(name = "VInternalInvoiceReport.findByPeriodAndDepartments",
                query = "SELECT detail FROM VInternalInvoiceReport detail " +
                        "WHERE deptSender = :deptSender and deptReceiver = :deptReceiver and " +
                        "documentDate between :period_begin AND :period_end ")
})

@Entity
@Table(name = "V_INTERNAL_INVOICE_REPORT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@SuppressWarnings("all")
@XmlRootElement(name = "ReportItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class VInternalInvoiceReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Документ_Дата", width = -100, sequence = 1)
    @Column(name="documentDate")
    private Date documentDate ;

    @TableHeader(name="Документ_№",sequence = 20, width = -80)
    private String documentNumber;

    @TableHeader(name="М.лист",sequence =25 , width = -80)
    private Double routingNumber = 0d ;

    @TableHeader(name="Модель",sequence = 30, width = -50)
    private String modelNumber;

    @TableHeader(name="Артикул",sequence = 40, width = -80)
    private String articleNumber ;

    @TableHeader(name="Наименование",sequence = 50)
    private String itemName;

    @TableHeader(name="Размер",sequence = 60, width = -100)
    private String itemSize ;

    @TableHeader(name="Сорт",sequence = 70, width = -30)
    private String itemGrade ;

    @TableHeader(name="Цвет",sequence = 80 , width = -100)
    private String itemColor ;

    @TableHeader(name="Цена",sequence = 90 , width = -50)
    private double cost ;

    @TableHeader(name="Кол-во",sequence = 100, width = -50)
    private int quantityPack ;

    @TableHeader(name="Сумма",sequence =110 , width = -80)
    private float costAll ;

    @TableHeader(name="Норма_на ед.",sequence =120 , width = -80)
    @Transient
    private double normByItem ;

    @Transient
    private double normByAmount ;


    public double getNormByItem() {
        return normByItem;
    }

    public void setNormByItem(double normByItem) {
        this.normByItem = new BigDecimal(normByItem).setScale(4, RoundingMode.HALF_UP).doubleValue() ;
        this.normByAmount = new BigDecimal(this.normByItem*this.quantityPack).setScale(4, RoundingMode.HALF_UP).doubleValue() ;
    }

    @TableHeader(name="Норма_на кол-во",sequence =130 , width = -80)
    public double getNormByAmount() {
        return normByAmount;
    }

    public void setNormByAmount(double normByAmount) {
        this.normByAmount = new BigDecimal(normByAmount).setScale(4, RoundingMode.HALF_UP).doubleValue() ;
    }

    private int deptSender ;
    private int deptReceiver ;

    public int getDeptSender() {
        return deptSender;
    }

    public void setDeptSender(int deptSender) {
        this.deptSender = deptSender;
    }

    public int getDeptReceiver() {
        return deptReceiver;
    }

    public void setDeptReceiver(int deptReceiver) {
        this.deptReceiver = deptReceiver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Double getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(Double routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(String itemGrade) {
        this.itemGrade = itemGrade;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantityPack() {
        return quantityPack;
    }

    public void setQuantityPack(int quantityPack) {
        this.quantityPack = quantityPack;
    }

    public float getCostAll() {
        return costAll;
    }

    public void setCostAll(float costAll) {
        this.costAll = costAll;
    }
}
