package xsd.contractor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ContractorList" type="{}ContractorMarch8" maxOccurs="5000"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "contractorList"
})
@XmlRootElement(name = "ContractorXML")
public class ContractorXML {

    @XmlElement(name = "ContractorList", required = true)
    protected List<ContractorMarch8> contractorList;

    /**
     * Gets the value of the contractorList property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contractorList property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContractorList().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContractorMarch8 }
     *
     *
     */
    public List<ContractorMarch8> getContractorList() {
        if (contractorList == null) {
            contractorList = new ArrayList<ContractorMarch8>();
        }
        return this.contractorList;
    }

}
