package by.march8.ecs.application.modules.warehouse.internal.displacement;

import by.march8.entities.warehouse.DisplacementInvoiceView;
import by.march8.entities.warehouse.VInternalInvoiceItem;

import java.util.List;

/**
 * @author Andy 19.03.2018.
 */
public class DisplacementInvoiceReport {
    private DisplacementInvoiceView document;
    private List<VInternalInvoiceItem> list;

    public DisplacementInvoiceView getDocument() {
        return document;
    }

    public void setDocument(final DisplacementInvoiceView document) {
        this.document = document;
    }

    public List<VInternalInvoiceItem> getList() {
        return list;
    }

    public void setList(final List<VInternalInvoiceItem> list) {
        this.list = list;
    }
}
