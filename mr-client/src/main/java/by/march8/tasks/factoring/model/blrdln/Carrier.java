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
 *         &lt;element name="DeliveryContact">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="150"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ProxyID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ProxyDate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PartyIssuingProxyName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="175"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BaseShippingDocumentName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="175"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BaseShippingDocumentID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BaseShippingDocumentDate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
        "deliveryContact",
        "proxyID",
        "proxyDate",
        "partyIssuingProxyName",
        "baseShippingDocumentName",
        "baseShippingDocumentID",
        "baseShippingDocumentDate"
})
@XmlRootElement(name = "Carrier")
public class Carrier {

    @XmlElement(name = "DeliveryContact", required = true)
    protected String deliveryContact;
    @XmlElement(name = "ProxyID")
    protected String proxyID;
    @XmlElement(name = "ProxyDate")
    protected String proxyDate;
    @XmlElement(name = "PartyIssuingProxyName")
    protected String partyIssuingProxyName;
    @XmlElement(name = "BaseShippingDocumentName")
    protected String baseShippingDocumentName;
    @XmlElement(name = "BaseShippingDocumentID")
    protected String baseShippingDocumentID;
    @XmlElement(name = "BaseShippingDocumentDate")
    protected String baseShippingDocumentDate;

    /**
     * Gets the value of the deliveryContact property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDeliveryContact() {
        return deliveryContact;
    }

    /**
     * Sets the value of the deliveryContact property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDeliveryContact(String value) {
        this.deliveryContact = value;
    }

    /**
     * Gets the value of the proxyID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProxyID() {
        return proxyID;
    }

    /**
     * Sets the value of the proxyID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProxyID(String value) {
        this.proxyID = value;
    }

    /**
     * Gets the value of the proxyDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProxyDate() {
        return proxyDate;
    }

    /**
     * Sets the value of the proxyDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProxyDate(String value) {
        this.proxyDate = value;
    }

    /**
     * Gets the value of the partyIssuingProxyName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPartyIssuingProxyName() {
        return partyIssuingProxyName;
    }

    /**
     * Sets the value of the partyIssuingProxyName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPartyIssuingProxyName(String value) {
        this.partyIssuingProxyName = value;
    }

    /**
     * Gets the value of the baseShippingDocumentName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBaseShippingDocumentName() {
        return baseShippingDocumentName;
    }

    /**
     * Sets the value of the baseShippingDocumentName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBaseShippingDocumentName(String value) {
        this.baseShippingDocumentName = value;
    }

    /**
     * Gets the value of the baseShippingDocumentID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBaseShippingDocumentID() {
        return baseShippingDocumentID;
    }

    /**
     * Sets the value of the baseShippingDocumentID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBaseShippingDocumentID(String value) {
        this.baseShippingDocumentID = value;
    }

    /**
     * Gets the value of the baseShippingDocumentDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBaseShippingDocumentDate() {
        return baseShippingDocumentDate;
    }

    /**
     * Sets the value of the baseShippingDocumentDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBaseShippingDocumentDate(String value) {
        this.baseShippingDocumentDate = value;
    }

}
