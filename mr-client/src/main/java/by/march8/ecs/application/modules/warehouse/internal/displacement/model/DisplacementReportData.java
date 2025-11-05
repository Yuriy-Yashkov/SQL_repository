package by.march8.ecs.application.modules.warehouse.internal.displacement.model;

import by.march8.entities.warehouse.DisplacementInvoiceView;

import java.util.List;

/**
 * @author Andy 10.04.2018 - 9:27.
 */
public class DisplacementReportData {
    private DisplacementInvoiceView document;
    private List<Object> data;

    public DisplacementInvoiceView getDocument() {
        return document;
    }

    public void setDocument(final DisplacementInvoiceView document) {
        this.document = document;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(final List<Object> data) {
        this.data = data;
    }
}
