package dept.ves.model;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

/**
 * @author Developer on 02.03.2020 7:30
 */
public class VolumeOfPurchases extends BaseEntity {

    private int id;
    private String contractorName;
    private int contractorCode;
    private double sumPurchases;
    private int currencyId;
    private int discountValue;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Контрагент_Наименование", sequence = 20)
    public String getContractorName() {
        if (contractorName != null) {
            return contractorName.trim();
        }
        return "";
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    @TableHeader(name = "Контрагент_Код", width = -50, sequence = 10)
    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(int contractorCode) {
        this.contractorCode = contractorCode;
    }

    @TableHeader(name = "Объем заказа", width = -100, sequence = 30)
    public double getSumPurchases() {
        return sumPurchases;
    }

    public void setSumPurchases(double sumPurchases) {
        this.sumPurchases = sumPurchases;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    @TableHeader(name = "Скидка", width = -50, sequence = 100)
    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }
}
