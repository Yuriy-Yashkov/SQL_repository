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
 *         &lt;element ref="{}GLN"/>
 *         &lt;element ref="{}Address"/>
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
        "gln",
        "address"
})
@XmlRootElement(name = "ShipTo")
public class ShipTo {

    @XmlElement(name = "GLN", required = true)
    protected String gln;
    @XmlElement(name = "Address", required = true)
    protected String address;

    /**
     * Gets the value of the gln property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getGLN() {
        return gln;
    }

    /**
     * Sets the value of the gln property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setGLN(String value) {
        this.gln = value;
    }

    /**
     * Gets the value of the address property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAddress(String value) {
        this.address = value;
    }

}
