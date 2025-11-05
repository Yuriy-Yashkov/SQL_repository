package by.march8.entities.production;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.util.Date;


/**
 * @author Developer on 04.12.2019 7:30
 */


@NamedQueries({
        @NamedQuery(name = "CutCardDocumentView.findByPeriod",
                query = "SELECT items FROM CutCardDocumentView items WHERE items.documentDate between :date_start and :date_end " +
                        "order by items.documentNumber")
})

@Entity
@Table(name = "VIEW_CUTCARD")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CutCardDocumentView extends BaseEntity {

    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "DOCUMENT_NUMBER")
    private int documentNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    private Date documentDate;

    @Column(name = "AMOUNT")
    private int amount;

    @Column(name = "AMOUNT_ROUTE")
    private int amountRoute;

    @Column(name = "BRIGADE_CODE")
    private int brigadeCode;

    @Column(name = "BRIGADE_NAME")
    private String brigadeName;

    @Column(name = "PRINT_TYPE")
    private String printType;

    @Column(name = "ROUTE_TYPE")
    private int routeType;


    @Column(name = "DISPOSITION_NUMBER")
    private String dispositionNumber;

    @Column(name = "CUSTOMER_CODE")
    private int customerCode;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "INCOMING_DATE")
    @Temporal(TemporalType.DATE)
    private Date incomingDate;

    @TableHeader(name = "Маршрут", sequence = 80)
    public String getRouteTypeView() {
        if (routeType == 1) {
            return "План производства";
        }

        if (routeType == 2) {
            return "Распоряжение";
        }

        return "";
    }

    @Override
    @TableHeader(name = "ID", sequence = 999)
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Номер", sequence = 10)
    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    @TableHeader(name = "Дата", sequence = 20)
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    @TableHeader(name = "Кол-во", sequence = 30)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @TableHeader(name = "Выписано", sequence = 40)
    public int getAmountRoute() {
        return amountRoute;
    }

    public void setAmountRoute(int amountRoute) {
        this.amountRoute = amountRoute;
    }

    @TableHeader(name = "Бригада_Код", sequence = 50)
    public int getBrigadeCode() {
        return brigadeCode;
    }

    public void setBrigadeCode(int brigadeCode) {
        this.brigadeCode = brigadeCode;
    }

    @TableHeader(name = "Бригада_Наименование", sequence = 60)
    public String getBrigadeName() {
        if (brigadeName != null) {
            return brigadeName.trim();
        }
        return "";
    }

    public void setBrigadeName(String brigadeName) {
        this.brigadeName = brigadeName;
    }

    @TableHeader(name = "Вид печати", sequence = 70)
    public String getPrintType() {
        if(printType!=null) {
            return printType.trim();
        }

        return "";
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public int getRouteType() {
        return routeType;
    }

    public void setRouteType(int routeType) {
        this.routeType = routeType;
    }

    @TableHeader(name = "Распоряжение", sequence = 90)
    public String getDispositionNumber() {
        if(dispositionNumber!=null) {
            return dispositionNumber.trim();
        }
        return "";
    }

    public void setDispositionNumber(String dispositionNumber) {
        this.dispositionNumber = dispositionNumber;
    }

    @TableHeader(name = "Заказчик_Код", sequence = 100)
    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }

    @TableHeader(name = "Заказчик_Наименование", sequence = 110)
    public String getCustomerName() {
        if(customerName!=null) {
            return customerName.trim();
        }

        return "";
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @TableHeader(name = "Дата прихода", sequence = 120)
    public Date getIncomingDate() {
        return incomingDate;
    }

    public void setIncomingDate(Date incomingDate) {
        this.incomingDate = incomingDate;
    }
}
