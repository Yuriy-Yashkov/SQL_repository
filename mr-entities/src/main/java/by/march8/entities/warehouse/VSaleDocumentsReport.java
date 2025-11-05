package by.march8.entities.warehouse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author tmp on 03.12.2021 11:47
 */
@XmlRootElement(name = "ReportItems")
@XmlAccessorType(XmlAccessType.FIELD)
public class VSaleDocumentsReport {
    @XmlElement(name = "ReportItem")
    private List<VSaleDocumentReport> items = null;

    public List<VSaleDocumentReport> getItems() {
        return items;
    }

    public void setEmployees(List<VSaleDocumentReport> items) {
        this.items = items;
    }
}
