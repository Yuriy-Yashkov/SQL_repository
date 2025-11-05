package by.march8.ecs.application.modules.marketing.model;

import by.march8.ecs.application.modules.marketing.manager.ProductionCatalogReportTypeItem;
import by.march8.entities.readonly.CurrencyEntityMarch8;

import java.util.Date;

public class ProductionCatalogReportPreset {
    private CurrencyEntityMarch8 currencyType;
    private Date currencyDate;
    private double currencyRate;

    private ProductionCatalogReportTypeItem reportTYpe;

    public CurrencyEntityMarch8 getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyEntityMarch8 currencyType) {
        this.currencyType = currencyType;
    }

    public Date getCurrencyDate() {
        return currencyDate;
    }

    public void setCurrencyDate(Date currencyDate) {
        this.currencyDate = currencyDate;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public ProductionCatalogReportTypeItem getReportTYpe() {
        return reportTYpe;
    }

    public void setReportType(ProductionCatalogReportTypeItem reportTYpe) {
        this.reportTYpe = reportTYpe;
    }
}
