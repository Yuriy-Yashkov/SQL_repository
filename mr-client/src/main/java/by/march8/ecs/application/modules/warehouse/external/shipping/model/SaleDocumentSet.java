package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.utils.DatePeriod;
import by.march8.entities.warehouse.SaleDocumentBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andy 22.01.2019 - 7:41.
 */
public class SaleDocumentSet {
    private int year;
    private DatePeriod period;
    private List<SaleDocumentBase> documents;

    public DatePeriod getPeriod() {
        return period;
    }

    public void setPeriod(DatePeriod period) {
        this.period = period;
    }

    public List<SaleDocumentBase> getDocuments() {
        return documents;
    }

    public void setDocuments(List<SaleDocumentBase> documents) {
        this.documents = documents;
    }

    public String printInformation() {
        return "За период [" + period.printPeriod() + "] найдено " + documents.size() + " документов [" + getDiscountList() + "] " + getSalesValue();
    }

    public String getDiscountList() {
        Set<Double> set_ = new HashSet<>();
        for (SaleDocumentBase doc : documents) {
            set_.add((double) doc.getDiscountValue());
        }

        String result = "";

        for (Double d : set_) {
            result += String.valueOf(d.intValue()) + "-";
        }
        if (result.length() > 1) {
            return result.substring(0, result.length() - 1);
        } else {
            return result.substring(0, result.length());
        }
    }

    public Double getSalesValue() {

        double value = 0;
        for (SaleDocumentBase doc : documents) {
            value += doc.getValueSumCostAndVat();
        }


        return value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
