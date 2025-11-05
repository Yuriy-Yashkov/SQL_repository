package by.march8.tasks.factoring.model.blrwbl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="SecurityID" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
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
        "securityID"
})
@XmlRootElement(name = "Security")
public class Security {

    @XmlElement(name = "SecurityID", required = true)
    protected Object securityID;

    /**
     * Gets the value of the securityID property.
     *
     * @return
     *     possible object is
     *     {@link Object }
     *
     */
    public Object getSecurityID() {
        return securityID;
    }

    /**
     * Sets the value of the securityID property.
     *
     * @param value
     *     allowed object is
     *     {@link Object }
     *
     */
    public void setSecurityID(Object value) {
        this.securityID = value;
    }

}
