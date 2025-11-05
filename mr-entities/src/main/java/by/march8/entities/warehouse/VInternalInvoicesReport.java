package by.march8.entities.warehouse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Andy on 03.12.2021 13:39
 */
@XmlRootElement(name = "ReportItems")
@XmlAccessorType(XmlAccessType.FIELD)
public class VInternalInvoicesReport {
    @XmlElement(name = "ReportItem")
    private List<VInternalInvoiceReport> items = null;

    public List<VInternalInvoiceReport> getItems() {
        return items;
    }

    public void setItems(List<VInternalInvoiceReport> items) {
        this.items = items;
    }
}
