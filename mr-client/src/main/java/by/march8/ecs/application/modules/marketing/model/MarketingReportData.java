package by.march8.ecs.application.modules.marketing.model;

import by.march8.entities.marketing.MarketingPriceListItem;

import java.util.List;

/**
 * @author Andy 14.11.2017.
 */
public class MarketingReportData {
    private MarketingPriceListItem document;
    private List<Object> data;

    public MarketingPriceListItem getDocument() {
        return document;
    }

    public void setDocument(final MarketingPriceListItem document) {
        this.document = document;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(final List<Object> data) {
        this.data = data;
    }
}
