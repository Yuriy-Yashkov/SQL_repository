package by.march8.tasks.factoring.model.blrwbl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.math.BigInteger;
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
 *         &lt;element name="DeliveryNoteType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *               &lt;enumeration value="700"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DocumentID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CreationDateTime">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FunctionCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DeliveryNoteID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DeliveryNoteDate">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ContractName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ContractID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ContractDate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="WaybillID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Document" maxOccurs="100" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DocumentID" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="70"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DocumentDate" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="35"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DocumentName">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="512"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{}Shipper"/>
 *         &lt;element ref="{}Receiver"/>
 *         &lt;element ref="{}FreightPayer" minOccurs="0"/>
 *         &lt;element ref="{}ShipFrom"/>
 *         &lt;element ref="{}ShipTo"/>
 *         &lt;element ref="{}Carrier"/>
 *         &lt;element name="QuantityTrip" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TransportOwnerName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="175"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TransportID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TrailerID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SealID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OrderID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Currency">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ExtraField" maxOccurs="500" minOccurs="0">
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
 *         &lt;element ref="{}DespatchAdviceLogisticUnitLineItem"/>
 *         &lt;element name="Total">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="TotalAmountWithoutCharges">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="35"/>
 *                         &lt;fractionDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TotalAmountCharges" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="35"/>
 *                         &lt;fractionDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TotalAmount">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="35"/>
 *                         &lt;fractionDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TotalLineItem">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;totalDigits value="18"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TotalLineItemQuantity">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="18"/>
 *                         &lt;fractionDigits value="5"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TotalGrossWeight">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="18"/>
 *                         &lt;fractionDigits value="6"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TotalDespatchUnitQuantity" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="18"/>
 *                         &lt;fractionDigits value="5"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TotalAmountExcise" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="35"/>
 *                         &lt;fractionDigits value="2"/>
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
        "deliveryNoteType",
        "documentID",
        "creationDateTime",
        "functionCode",
        "deliveryNoteID",
        "deliveryNoteDate",
        "contractName",
        "contractID",
        "contractDate",
        "waybillID",
        "document",
        "shipper",
        "receiver",
        "freightPayer",
        "shipFrom",
        "shipTo",
        "carrier",
        "quantityTrip",
        "transportOwnerName",
        "transportID",
        "trailerID",
        "sealID",
        "orderID",
        "currency",
        "extraField",
        "despatchAdviceLogisticUnitLineItem",
        "total"
})
@XmlRootElement(name = "DeliveryNote")
public class DeliveryNote {

    @XmlElement(name = "DeliveryNoteType", required = true)
    protected String deliveryNoteType;
    @XmlElement(name = "DocumentID", required = true)
    protected String documentID;
    @XmlElement(name = "CreationDateTime", required = true)
    protected String creationDateTime;
    @XmlElement(name = "FunctionCode", required = true)
    protected String functionCode;
    @XmlElement(name = "DeliveryNoteID", required = true)
    protected String deliveryNoteID;
    @XmlElement(name = "DeliveryNoteDate", required = true)
    protected String deliveryNoteDate;
    @XmlElement(name = "ContractName")
    protected String contractName;
    @XmlElement(name = "ContractID", required = true)
    protected String contractID;
    @XmlElement(name = "ContractDate")
    protected String contractDate;
    @XmlElement(name = "WaybillID", required = true)
    protected String waybillID;
    @XmlElement(name = "Document")
    protected List<DeliveryNote.Document> document;
    @XmlElement(name = "Shipper", required = true)
    protected Shipper shipper;
    @XmlElement(name = "Receiver", required = true)
    protected Receiver receiver;
    @XmlElement(name = "FreightPayer")
    protected FreightPayer freightPayer;
    @XmlElement(name = "ShipFrom", required = true)
    protected ShipFrom shipFrom;
    @XmlElement(name = "ShipTo", required = true)
    protected ShipTo shipTo;
    @XmlElement(name = "Carrier", required = true)
    protected Carrier carrier;
    @XmlElement(name = "QuantityTrip")
    protected String quantityTrip;
    @XmlElement(name = "TransportOwnerName")
    protected String transportOwnerName;
    @XmlElement(name = "TransportID", required = true)
    protected String transportID;
    @XmlElement(name = "TrailerID")
    protected String trailerID;
    @XmlElement(name = "SealID")
    protected String sealID;
    @XmlElement(name = "OrderID")
    protected String orderID;
    @XmlElement(name = "Currency", required = true)
    protected String currency;
    @XmlElement(name = "ExtraField")
    protected List<DeliveryNote.ExtraField> extraField;
    @XmlElement(name = "DespatchAdviceLogisticUnitLineItem", required = true)
    protected DespatchAdviceLogisticUnitLineItem despatchAdviceLogisticUnitLineItem;
    @XmlElement(name = "Total", required = true)
    protected DeliveryNote.Total total;

    /**
     * Gets the value of the deliveryNoteType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDeliveryNoteType() {
        return deliveryNoteType;
    }

    /**
     * Sets the value of the deliveryNoteType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDeliveryNoteType(String value) {
        this.deliveryNoteType = value;
    }

    /**
     * Gets the value of the documentID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDocumentID() {
        return documentID;
    }

    /**
     * Sets the value of the documentID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDocumentID(String value) {
        this.documentID = value;
    }

    /**
     * Gets the value of the creationDateTime property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCreationDateTime() {
        return creationDateTime;
    }

    /**
     * Sets the value of the creationDateTime property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCreationDateTime(String value) {
        this.creationDateTime = value;
    }

    /**
     * Gets the value of the functionCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFunctionCode() {
        return functionCode;
    }

    /**
     * Sets the value of the functionCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFunctionCode(String value) {
        this.functionCode = value;
    }

    /**
     * Gets the value of the deliveryNoteID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDeliveryNoteID() {
        return deliveryNoteID;
    }

    /**
     * Sets the value of the deliveryNoteID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDeliveryNoteID(String value) {
        this.deliveryNoteID = value;
    }

    /**
     * Gets the value of the deliveryNoteDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDeliveryNoteDate() {
        return deliveryNoteDate;
    }

    /**
     * Sets the value of the deliveryNoteDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDeliveryNoteDate(String value) {
        this.deliveryNoteDate = value;
    }

    /**
     * Gets the value of the contractName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContractName() {
        return contractName;
    }

    /**
     * Sets the value of the contractName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContractName(String value) {
        this.contractName = value;
    }

    /**
     * Gets the value of the contractID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContractID() {
        return contractID;
    }

    /**
     * Sets the value of the contractID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContractID(String value) {
        this.contractID = value;
    }

    /**
     * Gets the value of the contractDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContractDate() {
        return contractDate;
    }

    /**
     * Sets the value of the contractDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContractDate(String value) {
        this.contractDate = value;
    }

    /**
     * Gets the value of the waybillID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getWaybillID() {
        return waybillID;
    }

    /**
     * Sets the value of the waybillID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setWaybillID(String value) {
        this.waybillID = value;
    }

    /**
     * Gets the value of the document property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the document property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocument().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeliveryNote.Document }
     *
     *
     */
    public List<DeliveryNote.Document> getDocument() {
        if (document == null) {
            document = new ArrayList<DeliveryNote.Document>();
        }
        return this.document;
    }

    /**
     * ����������������
     *
     * @return
     *     possible object is
     *     {@link Shipper }
     *
     */
    public Shipper getShipper() {
        return shipper;
    }

    /**
     * Sets the value of the shipper property.
     *
     * @param value
     *     allowed object is
     *     {@link Shipper }
     *
     */
    public void setShipper(Shipper value) {
        this.shipper = value;
    }

    /**
     * ���������������
     *
     * @return
     *     possible object is
     *     {@link Receiver }
     *
     */
    public Receiver getReceiver() {
        return receiver;
    }

    /**
     * Sets the value of the receiver property.
     *
     * @param value
     *     allowed object is
     *     {@link Receiver }
     *
     */
    public void setReceiver(Receiver value) {
        this.receiver = value;
    }

    /**
     * �������� ������������� ��������� (����������)
     *
     * @return
     *     possible object is
     *     {@link FreightPayer }
     *
     */
    public FreightPayer getFreightPayer() {
        return freightPayer;
    }

    /**
     * Sets the value of the freightPayer property.
     *
     * @param value
     *     allowed object is
     *     {@link FreightPayer }
     *
     */
    public void setFreightPayer(FreightPayer value) {
        this.freightPayer = value;
    }

    /**
     * Gets the value of the shipFrom property.
     *
     * @return
     *     possible object is
     *     {@link ShipFrom }
     *
     */
    public ShipFrom getShipFrom() {
        return shipFrom;
    }

    /**
     * Sets the value of the shipFrom property.
     *
     * @param value
     *     allowed object is
     *     {@link ShipFrom }
     *
     */
    public void setShipFrom(ShipFrom value) {
        this.shipFrom = value;
    }

    /**
     * ����� ���������
     *
     * @return
     *     possible object is
     *     {@link ShipTo }
     *
     */
    public ShipTo getShipTo() {
        return shipTo;
    }

    /**
     * Sets the value of the shipTo property.
     *
     * @param value
     *     allowed object is
     *     {@link ShipTo }
     *
     */
    public void setShipTo(ShipTo value) {
        this.shipTo = value;
    }

    /**
     * Gets the value of the carrier property.
     *
     * @return
     *     possible object is
     *     {@link Carrier }
     *
     */
    public Carrier getCarrier() {
        return carrier;
    }

    /**
     * Sets the value of the carrier property.
     *
     * @param value
     *     allowed object is
     *     {@link Carrier }
     *
     */
    public void setCarrier(Carrier value) {
        this.carrier = value;
    }

    /**
     * Gets the value of the quantityTrip property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getQuantityTrip() {
        return quantityTrip;
    }

    /**
     * Sets the value of the quantityTrip property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setQuantityTrip(String value) {
        this.quantityTrip = value;
    }

    /**
     * Gets the value of the transportOwnerName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTransportOwnerName() {
        return transportOwnerName;
    }

    /**
     * Sets the value of the transportOwnerName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTransportOwnerName(String value) {
        this.transportOwnerName = value;
    }

    /**
     * Gets the value of the transportID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTransportID() {
        return transportID;
    }

    /**
     * Sets the value of the transportID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTransportID(String value) {
        this.transportID = value;
    }

    /**
     * Gets the value of the trailerID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTrailerID() {
        return trailerID;
    }

    /**
     * Sets the value of the trailerID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTrailerID(String value) {
        this.trailerID = value;
    }

    /**
     * Gets the value of the sealID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSealID() {
        return sealID;
    }

    /**
     * Sets the value of the sealID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSealID(String value) {
        this.sealID = value;
    }

    /**
     * Gets the value of the orderID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrderID() {
        return orderID;
    }

    /**
     * Sets the value of the orderID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOrderID(String value) {
        this.orderID = value;
    }

    /**
     * Gets the value of the currency property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the extraField property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extraField property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtraField().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeliveryNote.ExtraField }
     *
     *
     */
    public List<DeliveryNote.ExtraField> getExtraField() {
        if (extraField == null) {
            extraField = new ArrayList<DeliveryNote.ExtraField>();
        }
        return this.extraField;
    }

    /**
     * Gets the value of the despatchAdviceLogisticUnitLineItem property.
     *
     * @return
     *     possible object is
     *     {@link DespatchAdviceLogisticUnitLineItem }
     *
     */
    public DespatchAdviceLogisticUnitLineItem getDespatchAdviceLogisticUnitLineItem() {
        return despatchAdviceLogisticUnitLineItem;
    }

    /**
     * Sets the value of the despatchAdviceLogisticUnitLineItem property.
     *
     * @param value
     *     allowed object is
     *     {@link DespatchAdviceLogisticUnitLineItem }
     *
     */
    public void setDespatchAdviceLogisticUnitLineItem(DespatchAdviceLogisticUnitLineItem value) {
        this.despatchAdviceLogisticUnitLineItem = value;
    }

    /**
     * Gets the value of the total property.
     *
     * @return
     *     possible object is
     *     {@link DeliveryNote.Total }
     *
     */
    public DeliveryNote.Total getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     *
     * @param value
     *     allowed object is
     *     {@link DeliveryNote.Total }
     *
     */
    public void setTotal(DeliveryNote.Total value) {
        this.total = value;
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
     *         &lt;element name="DocumentID" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="70"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DocumentDate" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="35"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DocumentName">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *               &lt;maxLength value="512"/>
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
            "documentID",
            "documentDate",
            "documentName"
    })
    public static class Document {

        @XmlElement(name = "DocumentID")
        protected String documentID;
        @XmlElement(name = "DocumentDate")
        protected String documentDate;
        @XmlElement(name = "DocumentName", required = true)
        protected String documentName;

        /**
         * Gets the value of the documentID property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getDocumentID() {
            return documentID;
        }

        /**
         * Sets the value of the documentID property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setDocumentID(String value) {
            this.documentID = value;
        }

        /**
         * Gets the value of the documentDate property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getDocumentDate() {
            return documentDate;
        }

        /**
         * Sets the value of the documentDate property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setDocumentDate(String value) {
            this.documentDate = value;
        }

        /**
         * Gets the value of the documentName property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getDocumentName() {
            return documentName;
        }

        /**
         * Sets the value of the documentName property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setDocumentName(String value) {
            this.documentName = value;
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
    public static class ExtraField {

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
     *         &lt;element name="TotalAmountWithoutCharges">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;totalDigits value="35"/>
     *               &lt;fractionDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TotalAmountCharges" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;totalDigits value="35"/>
     *               &lt;fractionDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TotalAmount">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;totalDigits value="35"/>
     *               &lt;fractionDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TotalLineItem">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;totalDigits value="18"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TotalLineItemQuantity">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;totalDigits value="18"/>
     *               &lt;fractionDigits value="5"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TotalGrossWeight">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;totalDigits value="18"/>
     *               &lt;fractionDigits value="6"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TotalDespatchUnitQuantity" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;totalDigits value="18"/>
     *               &lt;fractionDigits value="5"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TotalAmountExcise" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;totalDigits value="35"/>
     *               &lt;fractionDigits value="2"/>
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
            "totalAmountWithoutCharges",
            "totalAmountCharges",
            "totalAmount",
            "totalLineItem",
            "totalLineItemQuantity",
            "totalGrossWeight",
            "totalDespatchUnitQuantity",
            "totalAmountExcise"
    })
    public static class Total {

        @XmlElement(name = "TotalAmountWithoutCharges", required = true)
        protected BigDecimal totalAmountWithoutCharges;
        @XmlElement(name = "TotalAmountCharges")
        protected BigDecimal totalAmountCharges;
        @XmlElement(name = "TotalAmount", required = true)
        protected BigDecimal totalAmount;
        @XmlElement(name = "TotalLineItem", required = true)
        protected BigInteger totalLineItem;
        @XmlElement(name = "TotalLineItemQuantity", required = true)
        protected BigDecimal totalLineItemQuantity;
        @XmlElement(name = "TotalGrossWeight", required = true)
        protected BigDecimal totalGrossWeight;
        @XmlElement(name = "TotalDespatchUnitQuantity")
        protected BigDecimal totalDespatchUnitQuantity;
        @XmlElement(name = "TotalAmountExcise")
        protected BigDecimal totalAmountExcise;

        /**
         * Gets the value of the totalAmountWithoutCharges property.
         *
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *
         */
        public BigDecimal getTotalAmountWithoutCharges() {
            return totalAmountWithoutCharges;
        }

        /**
         * Sets the value of the totalAmountWithoutCharges property.
         *
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *
         */
        public void setTotalAmountWithoutCharges(BigDecimal value) {
            this.totalAmountWithoutCharges = value;
        }

        /**
         * Gets the value of the totalAmountCharges property.
         *
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *
         */
        public BigDecimal getTotalAmountCharges() {
            return totalAmountCharges;
        }

        /**
         * Sets the value of the totalAmountCharges property.
         *
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *
         */
        public void setTotalAmountCharges(BigDecimal value) {
            this.totalAmountCharges = value;
        }

        /**
         * Gets the value of the totalAmount property.
         *
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *
         */
        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        /**
         * Sets the value of the totalAmount property.
         *
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *
         */
        public void setTotalAmount(BigDecimal value) {
            this.totalAmount = value;
        }

        /**
         * Gets the value of the totalLineItem property.
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getTotalLineItem() {
            return totalLineItem;
        }

        /**
         * Sets the value of the totalLineItem property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setTotalLineItem(BigInteger value) {
            this.totalLineItem = value;
        }

        /**
         * Gets the value of the totalLineItemQuantity property.
         *
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *
         */
        public BigDecimal getTotalLineItemQuantity() {
            return totalLineItemQuantity;
        }

        /**
         * Sets the value of the totalLineItemQuantity property.
         *
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *
         */
        public void setTotalLineItemQuantity(BigDecimal value) {
            this.totalLineItemQuantity = value;
        }

        /**
         * Gets the value of the totalGrossWeight property.
         *
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *
         */
        public BigDecimal getTotalGrossWeight() {
            return totalGrossWeight;
        }

        /**
         * Sets the value of the totalGrossWeight property.
         *
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *
         */
        public void setTotalGrossWeight(BigDecimal value) {
            this.totalGrossWeight = value;
        }

        /**
         * Gets the value of the totalDespatchUnitQuantity property.
         *
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *
         */
        public BigDecimal getTotalDespatchUnitQuantity() {
            return totalDespatchUnitQuantity;
        }

        /**
         * Sets the value of the totalDespatchUnitQuantity property.
         *
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *
         */
        public void setTotalDespatchUnitQuantity(BigDecimal value) {
            this.totalDespatchUnitQuantity = value;
        }

        /**
         * Gets the value of the totalAmountExcise property.
         *
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *
         */
        public BigDecimal getTotalAmountExcise() {
            return totalAmountExcise;
        }

        /**
         * Sets the value of the totalAmountExcise property.
         *
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *
         */
        public void setTotalAmountExcise(BigDecimal value) {
            this.totalAmountExcise = value;
        }

    }

}
