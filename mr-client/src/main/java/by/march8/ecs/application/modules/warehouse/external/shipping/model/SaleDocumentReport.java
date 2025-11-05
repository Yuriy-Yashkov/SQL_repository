package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;

import java.util.HashMap;
import java.util.List;

/**
 * Структура документа используемого в отчетах и выгрузках
 *
 * @author Andy 18.05.2016.
 */
public class SaleDocumentReport {
    private SaleDocumentBase document;
    private List<SaleDocumentDetailItemReport> detailList;
    private HashMap<String, Object> detailMap;
    private TotalSummingUp summingUp;

    public static String getContractDescription(SaleDocumentReport report) {
        String contractName;
        String contractorCode;
        String result;
        try {
            contractName = (String) report.getDetailMap().get("CONTRACT_NAME");
            contractorCode = String.valueOf(report.getDocument().getRecipientCode());
            result = contractName + " код клиента " + contractorCode;
        } catch (Exception e) {
            return "Нет данных";
        }
        return result;
    }

    public SaleDocumentBase getDocument() {
        return document;
    }

    public void setDocument(final SaleDocumentBase document) {
        this.document = document;
    }

    public List<SaleDocumentDetailItemReport> getDetailList() {
        return detailList;
    }

    public void setDetailList(final List<SaleDocumentDetailItemReport> detailList) {
        this.detailList = detailList;
    }

    public HashMap<String, Object> getDetailMap() {
        return detailMap;
    }

    public void setDetailMap(final HashMap<String, Object> detailMap) {
        this.detailMap = detailMap;
    }

    public TotalSummingUp getSummingUp() {
        return summingUp;
    }

    public void setSummingUp(final TotalSummingUp summingUp) {
        this.summingUp = summingUp;
    }


}
