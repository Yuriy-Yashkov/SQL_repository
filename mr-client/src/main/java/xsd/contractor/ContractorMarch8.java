package xsd.contractor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ContractorMarch8 complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ContractorMarch8">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Code">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FullName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Resident">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}boolean">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Region">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UNN">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="15"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DiscountValue">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="18"/>
 *               &lt;fractionDigits value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ContractList" type="{}ContractMarch8" maxOccurs="5000"/>
 *         &lt;element name="LegalAddress" type="{}AddressMarch8"/>
 *         &lt;element name="PostAddress" type="{}AddressMarch8"/>
 *         &lt;element name="AddressList" type="{}AddressMarch8" maxOccurs="5000"/>
 *         &lt;element name="EmailList" type="{}EmailMarch8" maxOccurs="5000"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContractorMarch8", propOrder = {
        "id",
        "code",
        "name",
        "fullName",
        "resident",
        "type",
        "region",
        "unn",
        "discountValue",
        "contractList",
        "legalAddress",
        "postAddress",
        "addressList",
        "emailList"
})
public class ContractorMarch8 {

    @XmlElement(name = "ID", required = true)
    protected BigInteger id;
    @XmlElement(name = "Code", required = true)
    protected BigInteger code;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "FullName", required = true)
    protected String fullName;
    @XmlElement(name = "Resident")
    protected boolean resident;
    @XmlElement(name = "Type", required = true)
    protected String type;
    @XmlElement(name = "Region", required = true)
    protected BigInteger region;
    @XmlElement(name = "UNN", required = true)
    protected BigInteger unn;
    @XmlElement(name = "DiscountValue", required = true)
    protected BigDecimal discountValue;
    @XmlElement(name = "ContractList", required = true)
    protected List<ContractMarch8> contractList;
    @XmlElement(name = "LegalAddress", required = true)
    protected AddressMarch8 legalAddress;
    @XmlElement(name = "PostAddress", required = true)
    protected AddressMarch8 postAddress;
    @XmlElement(name = "AddressList", required = true)
    protected List<AddressMarch8> addressList;
    @XmlElement(name = "EmailList", required = true)
    protected List<EmailMarch8> emailList;

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setID(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the code property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setCode(BigInteger value) {
        this.code = value;
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
     * Gets the value of the fullName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the value of the fullName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFullName(String value) {
        this.fullName = value;
    }

    /**
     * Gets the value of the resident property.
     *
     */
    public boolean isResident() {
        return resident;
    }

    /**
     * Sets the value of the resident property.
     *
     */
    public void setResident(boolean value) {
        this.resident = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the region property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getRegion() {
        return region;
    }

    /**
     * Sets the value of the region property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setRegion(BigInteger value) {
        this.region = value;
    }

    /**
     * Gets the value of the unn property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getUNN() {
        return unn;
    }

    /**
     * Sets the value of the unn property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setUNN(BigInteger value) {
        this.unn = value;
    }

    /**
     * Gets the value of the discountValue property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * Sets the value of the discountValue property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setDiscountValue(BigDecimal value) {
        this.discountValue = value;
    }

    /**
     * Gets the value of the contractList property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contractList property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContractList().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContractMarch8 }
     *
     *
     */
    public List<ContractMarch8> getContractList() {
        if (contractList == null) {
            contractList = new ArrayList<ContractMarch8>();
        }
        return this.contractList;
    }

    /**
     * Gets the value of the legalAddress property.
     *
     * @return
     *     possible object is
     *     {@link AddressMarch8 }
     *
     */
    public AddressMarch8 getLegalAddress() {
        return legalAddress;
    }

    /**
     * Sets the value of the legalAddress property.
     *
     * @param value
     *     allowed object is
     *     {@link AddressMarch8 }
     *
     */
    public void setLegalAddress(AddressMarch8 value) {
        this.legalAddress = value;
    }

    /**
     * Gets the value of the postAddress property.
     *
     * @return
     *     possible object is
     *     {@link AddressMarch8 }
     *
     */
    public AddressMarch8 getPostAddress() {
        return postAddress;
    }

    /**
     * Sets the value of the postAddress property.
     *
     * @param value
     *     allowed object is
     *     {@link AddressMarch8 }
     *
     */
    public void setPostAddress(AddressMarch8 value) {
        this.postAddress = value;
    }

    /**
     * Gets the value of the addressList property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addressList property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddressList().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AddressMarch8 }
     *
     *
     */
    public List<AddressMarch8> getAddressList() {
        if (addressList == null) {
            addressList = new ArrayList<AddressMarch8>();
        }
        return this.addressList;
    }

    /**
     * Gets the value of the emailList property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the emailList property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEmailList().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EmailMarch8 }
     *
     *
     */
    public List<EmailMarch8> getEmailList() {
        if (emailList == null) {
            emailList = new ArrayList<EmailMarch8>();
        }
        return this.emailList;
    }

}
