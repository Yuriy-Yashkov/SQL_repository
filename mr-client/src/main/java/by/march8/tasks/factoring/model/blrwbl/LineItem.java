package by.march8.tasks.factoring.model.blrwbl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
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
 *         &lt;element name="LineItemNumber">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="2"/>
 *               &lt;maxLength value="14"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemBuyerID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemSupplierID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="512"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GrossWeightValue">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="18"/>
 *               &lt;fractionDigits value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="QuantityDespatched">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="35"/>
 *               &lt;fractionDigits value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemQuantityUOM">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DespatchUnitQuantityDespatched" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="35"/>
 *               &lt;fractionDigits value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CountryOfOrigin" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BestBeforeDate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TaxRate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="4"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AdditionalInformation" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="512"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemAmountWithoutCharges">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="35"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemAmountCharges" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="35"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemAmount">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="35"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemPrice">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="35"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemManufacturerPrice" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="35"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BulkDiscountRate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="4"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DiscountRate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="4"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LineItemAmountExcise" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="35"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SGBY01" maxOccurs="50" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SertificateType">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="70"/>
 *                         &lt;enumeration value="SBY"/>
 *                         &lt;enumeration value="DBY"/>
 *                         &lt;enumeration value="SCU"/>
 *                         &lt;enumeration value="DCU"/>
 *                         &lt;enumeration value="SRCU"/>
 *                         &lt;enumeration value="DRCU"/>
 *                         &lt;enumeration value="HGRB"/>
 *                         &lt;enumeration value="SGR"/>
 *                         &lt;enumeration value="SMEDBY"/>
 *                         &lt;enumeration value="SMEDEEU"/>
 *                         &lt;enumeration value="PSALEBY"/>
 *                         &lt;enumeration value="RTESTBY"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="SertificateID">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="70"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="BeginSertifDate" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="35"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="EndSertifDate" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="35"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Name" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="175"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="LineItemExtraField" maxOccurs="500" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="FieldName">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="FieldCode" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="6"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="FieldValue">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="2560"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
        "lineItemNumber",
        "lineItemID",
        "lineItemBuyerID",
        "lineItemSupplierID",
        "lineItemName",
        "grossWeightValue",
        "quantityDespatched",
        "lineItemQuantityUOM",
        "despatchUnitQuantityDespatched",
        "countryOfOrigin",
        "bestBeforeDate",
        "taxRate",
        "additionalInformation",
        "lineItemAmountWithoutCharges",
        "lineItemAmountCharges",
        "lineItemAmount",
        "lineItemPrice",
        "lineItemManufacturerPrice",
        "bulkDiscountRate",
        "discountRate",
        "lineItemAmountExcise",
        "sgby01",
        "lineItemExtraField"
})
@XmlRootElement(name = "LineItem")
public class LineItem {

    @XmlElement(name = "LineItemNumber", required = true)
    protected String lineItemNumber;
    @XmlElement(name = "LineItemID")
    protected String lineItemID;
    @XmlElement(name = "LineItemBuyerID")
    protected String lineItemBuyerID;
    @XmlElement(name = "LineItemSupplierID")
    protected String lineItemSupplierID;
    @XmlElement(name = "LineItemName", required = true)
    protected String lineItemName;
    @XmlElement(name = "GrossWeightValue", required = true)
    protected BigDecimal grossWeightValue;
    @XmlElement(name = "QuantityDespatched", required = true)
    protected BigDecimal quantityDespatched;
    @XmlElement(name = "LineItemQuantityUOM", required = true)
    protected String lineItemQuantityUOM;
    @XmlElement(name = "DespatchUnitQuantityDespatched")
    protected BigDecimal despatchUnitQuantityDespatched;
    @XmlElement(name = "CountryOfOrigin")
    protected String countryOfOrigin;
    @XmlElement(name = "BestBeforeDate")
    protected String bestBeforeDate;
    @XmlElement(name = "TaxRate")
    protected BigDecimal taxRate;
    @XmlElement(name = "AdditionalInformation")
    protected String additionalInformation;
    @XmlElement(name = "LineItemAmountWithoutCharges", required = true)
    protected BigDecimal lineItemAmountWithoutCharges;
    @XmlElement(name = "LineItemAmountCharges")
    protected BigDecimal lineItemAmountCharges;
    @XmlElement(name = "LineItemAmount", required = true)
    protected BigDecimal lineItemAmount;
    @XmlElement(name = "LineItemPrice", required = true)
    protected BigDecimal lineItemPrice;
    @XmlElement(name = "LineItemManufacturerPrice")
    protected BigDecimal lineItemManufacturerPrice;
    @XmlElement(name = "BulkDiscountRate")
    protected BigDecimal bulkDiscountRate;
    @XmlElement(name = "DiscountRate")
    protected BigDecimal discountRate;
    @XmlElement(name = "LineItemAmountExcise")
    protected BigDecimal lineItemAmountExcise;
    @XmlElement(name = "SGBY01")
    protected List<LineItem.SGBY01> sgby01;
    @XmlElement(name = "LineItemExtraField")
    protected List<LineItem.LineItemExtraField> lineItemExtraField;

    /**
     * Gets the value of the lineItemNumber property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLineItemNumber() {
        return lineItemNumber;
    }

    /**
     * Sets the value of the lineItemNumber property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLineItemNumber(String value) {
        this.lineItemNumber = value;
    }

    /**
     * Gets the value of the lineItemID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLineItemID() {
        return lineItemID;
    }

    /**
     * Sets the value of the lineItemID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLineItemID(String value) {
        this.lineItemID = value;
    }

    /**
     * Gets the value of the lineItemBuyerID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLineItemBuyerID() {
        return lineItemBuyerID;
    }

    /**
     * Sets the value of the lineItemBuyerID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLineItemBuyerID(String value) {
        this.lineItemBuyerID = value;
    }

    /**
     * Gets the value of the lineItemSupplierID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLineItemSupplierID() {
        return lineItemSupplierID;
    }

    /**
     * Sets the value of the lineItemSupplierID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLineItemSupplierID(String value) {
        this.lineItemSupplierID = value;
    }

    /**
     * Gets the value of the lineItemName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLineItemName() {
        return lineItemName;
    }

    /**
     * Sets the value of the lineItemName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLineItemName(String value) {
        this.lineItemName = value;
    }

    /**
     * Gets the value of the grossWeightValue property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getGrossWeightValue() {
        return grossWeightValue;
    }

    /**
     * Sets the value of the grossWeightValue property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setGrossWeightValue(BigDecimal value) {
        this.grossWeightValue = value;
    }

    /**
     * Gets the value of the quantityDespatched property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getQuantityDespatched() {
        return quantityDespatched;
    }

    /**
     * Sets the value of the quantityDespatched property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setQuantityDespatched(BigDecimal value) {
        this.quantityDespatched = value;
    }

    /**
     * Gets the value of the lineItemQuantityUOM property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLineItemQuantityUOM() {
        return lineItemQuantityUOM;
    }

    /**
     * Sets the value of the lineItemQuantityUOM property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLineItemQuantityUOM(String value) {
        this.lineItemQuantityUOM = value;
    }

    /**
     * Gets the value of the despatchUnitQuantityDespatched property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getDespatchUnitQuantityDespatched() {
        return despatchUnitQuantityDespatched;
    }

    /**
     * Sets the value of the despatchUnitQuantityDespatched property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setDespatchUnitQuantityDespatched(BigDecimal value) {
        this.despatchUnitQuantityDespatched = value;
    }

    /**
     * Gets the value of the countryOfOrigin property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    /**
     * Sets the value of the countryOfOrigin property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCountryOfOrigin(String value) {
        this.countryOfOrigin = value;
    }

    /**
     * Gets the value of the bestBeforeDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBestBeforeDate() {
        return bestBeforeDate;
    }

    /**
     * Sets the value of the bestBeforeDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBestBeforeDate(String value) {
        this.bestBeforeDate = value;
    }

    /**
     * Gets the value of the taxRate property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * Sets the value of the taxRate property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setTaxRate(BigDecimal value) {
        this.taxRate = value;
    }

    /**
     * Gets the value of the additionalInformation property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Sets the value of the additionalInformation property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAdditionalInformation(String value) {
        this.additionalInformation = value;
    }

    /**
     * Gets the value of the lineItemAmountWithoutCharges property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getLineItemAmountWithoutCharges() {
        return lineItemAmountWithoutCharges;
    }

    /**
     * Sets the value of the lineItemAmountWithoutCharges property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setLineItemAmountWithoutCharges(BigDecimal value) {
        this.lineItemAmountWithoutCharges = value;
    }

    /**
     * Gets the value of the lineItemAmountCharges property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getLineItemAmountCharges() {
        return lineItemAmountCharges;
    }

    /**
     * Sets the value of the lineItemAmountCharges property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setLineItemAmountCharges(BigDecimal value) {
        this.lineItemAmountCharges = value;
    }

    /**
     * Gets the value of the lineItemAmount property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getLineItemAmount() {
        return lineItemAmount;
    }

    /**
     * Sets the value of the lineItemAmount property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setLineItemAmount(BigDecimal value) {
        this.lineItemAmount = value;
    }

    /**
     * Gets the value of the lineItemPrice property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getLineItemPrice() {
        return lineItemPrice;
    }

    /**
     * Sets the value of the lineItemPrice property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setLineItemPrice(BigDecimal value) {
        this.lineItemPrice = value;
    }

    /**
     * Gets the value of the lineItemManufacturerPrice property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getLineItemManufacturerPrice() {
        return lineItemManufacturerPrice;
    }

    /**
     * Sets the value of the lineItemManufacturerPrice property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setLineItemManufacturerPrice(BigDecimal value) {
        this.lineItemManufacturerPrice = value;
    }

    /**
     * Gets the value of the bulkDiscountRate property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getBulkDiscountRate() {
        return bulkDiscountRate;
    }

    /**
     * Sets the value of the bulkDiscountRate property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setBulkDiscountRate(BigDecimal value) {
        this.bulkDiscountRate = value;
    }

    /**
     * Gets the value of the discountRate property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    /**
     * Sets the value of the discountRate property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setDiscountRate(BigDecimal value) {
        this.discountRate = value;
    }

    /**
     * Gets the value of the lineItemAmountExcise property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getLineItemAmountExcise() {
        return lineItemAmountExcise;
    }

    /**
     * Sets the value of the lineItemAmountExcise property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setLineItemAmountExcise(BigDecimal value) {
        this.lineItemAmountExcise = value;
    }

    /**
     * Gets the value of the sgby01 property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sgby01 property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSGBY01().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LineItem.SGBY01 }
     *
     *
     */
    public List<LineItem.SGBY01> getSGBY01() {
        if (sgby01 == null) {
            sgby01 = new ArrayList<LineItem.SGBY01>();
        }
        return this.sgby01;
    }

    /**
     * Gets the value of the lineItemExtraField property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lineItemExtraField property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLineItemExtraField().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LineItem.LineItemExtraField }
     *
     *
     */
    public List<LineItem.LineItemExtraField> getLineItemExtraField() {
        if (lineItemExtraField == null) {
            lineItemExtraField = new ArrayList<LineItem.LineItemExtraField>();
        }
        return this.lineItemExtraField;
    }


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
     *         &lt;element name="FieldName">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="FieldCode" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="6"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="FieldValue">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="2560"/>
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
            "fieldName",
            "fieldCode",
            "fieldValue"
    })
    public static class LineItemExtraField {

        @XmlElement(name = "FieldName", required = true)
        protected String fieldName;
        @XmlElement(name = "FieldCode")
        protected String fieldCode;
        @XmlElement(name = "FieldValue", required = true)
        protected String fieldValue;

        /**
         * Gets the value of the fieldName property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getFieldName() {
            return fieldName;
        }

        /**
         * Sets the value of the fieldName property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setFieldName(String value) {
            this.fieldName = value;
        }

        /**
         * Gets the value of the fieldCode property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getFieldCode() {
            return fieldCode;
        }

        /**
         * Sets the value of the fieldCode property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setFieldCode(String value) {
            this.fieldCode = value;
        }

        /**
         * Gets the value of the fieldValue property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getFieldValue() {
            return fieldValue;
        }

        /**
         * Sets the value of the fieldValue property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setFieldValue(String value) {
            this.fieldValue = value;
        }

    }


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
     *         &lt;element name="SertificateType">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="70"/>
     *               &lt;enumeration value="SBY"/>
     *               &lt;enumeration value="DBY"/>
     *               &lt;enumeration value="SCU"/>
     *               &lt;enumeration value="DCU"/>
     *               &lt;enumeration value="SRCU"/>
     *               &lt;enumeration value="DRCU"/>
     *               &lt;enumeration value="HGRB"/>
     *               &lt;enumeration value="SGR"/>
     *               &lt;enumeration value="SMEDBY"/>
     *               &lt;enumeration value="SMEDEEU"/>
     *               &lt;enumeration value="PSALEBY"/>
     *               &lt;enumeration value="RTESTBY"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="SertificateID">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="70"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="BeginSertifDate" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="35"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="EndSertifDate" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="35"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Name" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="175"/>
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
            "sertificateType",
            "sertificateID",
            "beginSertifDate",
            "endSertifDate",
            "name"
    })
    public static class SGBY01 {

        @XmlElement(name = "SertificateType", required = true)
        protected String sertificateType;
        @XmlElement(name = "SertificateID", required = true)
        protected String sertificateID;
        @XmlElement(name = "BeginSertifDate")
        protected String beginSertifDate;
        @XmlElement(name = "EndSertifDate")
        protected String endSertifDate;
        @XmlElement(name = "Name")
        protected String name;

        /**
         * Gets the value of the sertificateType property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getSertificateType() {
            return sertificateType;
        }

        /**
         * Sets the value of the sertificateType property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setSertificateType(String value) {
            this.sertificateType = value;
        }

        /**
         * Gets the value of the sertificateID property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getSertificateID() {
            return sertificateID;
        }

        /**
         * Sets the value of the sertificateID property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setSertificateID(String value) {
            this.sertificateID = value;
        }

        /**
         * Gets the value of the beginSertifDate property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getBeginSertifDate() {
            return beginSertifDate;
        }

        /**
         * Sets the value of the beginSertifDate property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setBeginSertifDate(String value) {
            this.beginSertifDate = value;
        }

        /**
         * Gets the value of the endSertifDate property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getEndSertifDate() {
            return endSertifDate;
        }

        /**
         * Sets the value of the endSertifDate property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setEndSertifDate(String value) {
            this.endSertifDate = value;
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

    }

}
