package by.march8.tasks.factoring.model.blrdln;

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
 *         &lt;element ref="{}GLN" minOccurs="0"/>
 *         &lt;element ref="{}Name"/>
 *         &lt;element ref="{}Address"/>
 *         &lt;element ref="{}VATRegistrationNumber"/>
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
        "name",
        "address",
        "vatRegistrationNumber"
})
@XmlRootElement(name = "FreightPayer")
public class FreightPayer {

    @XmlElement(name = "GLN")
    protected String gln;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Address", required = true)
    protected String address;
    @XmlElement(name = "VATRegistrationNumber", required = true)
    protected String vatRegistrationNumber;

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
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
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

    /**
     * Gets the value of the vatRegistrationNumber property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVATRegistrationNumber() {
        return vatRegistrationNumber;
    }

    /**
     * Sets the value of the vatRegistrationNumber property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVATRegistrationNumber(String value) {
        this.vatRegistrationNumber = value;
    }

}
