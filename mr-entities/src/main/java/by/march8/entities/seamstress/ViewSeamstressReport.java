package by.march8.entities.seamstress;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author tmp on 27.12.2021 12:03
 */

@XmlRootElement(name = "ReportItems")
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewSeamstressReport {
    @XmlElement(name = "ReportItem")
    private List<ViewSeamstressItem> items = null;

    public List<ViewSeamstressItem> getItems() {
        return items;
    }

    public void setEmployees(List<ViewSeamstressItem> items) {
        this.items = items;
    }
}