package by.march8.ecs.application.modules.marketing.model;

import by.march8.entities.warehouse.WarehouseRemainsItem;

import java.util.List;

/**
 * @author Andy 15.05.2018 - 11:06.
 */
public class MarketingRemainsReportData {
    private MarketingRemainsItem document;
    private List<WarehouseRemainsItem> data;

    public MarketingRemainsItem getDocument() {
        return document;
    }

    public void setDocument(final MarketingRemainsItem document) {
        this.document = document;
    }

    public List<WarehouseRemainsItem> getData() {
        return data;
    }

    public void setData(final List<WarehouseRemainsItem> data) {
        this.data = data;
    }
}
