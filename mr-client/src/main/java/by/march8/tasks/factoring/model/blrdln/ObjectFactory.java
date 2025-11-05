package by.march8.tasks.factoring.model.blrdln;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the by.march8.tasks.factoring.model.blrdln package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _VATRegistrationNumber_QNAME = new QName("", "VATRegistrationNumber");
    private final static QName _Address_QNAME = new QName("", "Address");
    private final static QName _GLN_QNAME = new QName("", "GLN");
    private final static QName _Name_QNAME = new QName("", "Name");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: by.march8.tasks.factoring.model.blrdln
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeliveryNote }
     *
     */
    public DeliveryNote createDeliveryNote() {
        return new DeliveryNote();
    }

    /**
     * Create an instance of {@link LineItem }
     *
     */
    public LineItem createLineItem() {
        return new LineItem();
    }

    /**
     * Create an instance of {@link ShipFrom }
     *
     */
    public ShipFrom createShipFrom() {
        return new ShipFrom();
    }

    /**
     * Create an instance of {@link DeliveryNote.Document }
     *
     */
    public DeliveryNote.Document createDeliveryNoteDocument() {
        return new DeliveryNote.Document();
    }

    /**
     * Create an instance of {@link Shipper }
     *
     */
    public Shipper createShipper() {
        return new Shipper();
    }

    /**
     * Create an instance of {@link Receiver }
     *
     */
    public Receiver createReceiver() {
        return new Receiver();
    }

    /**
     * Create an instance of {@link DeliveryNote.ShipTo }
     *
     */
    public DeliveryNote.ShipTo createDeliveryNoteShipTo() {
        return new DeliveryNote.ShipTo();
    }

    /**
     * Create an instance of {@link Carrier }
     *
     */
    public Carrier createCarrier() {
        return new Carrier();
    }

    /**
     * Create an instance of {@link DeliveryNote.ExtraField }
     *
     */
    public DeliveryNote.ExtraField createDeliveryNoteExtraField() {
        return new DeliveryNote.ExtraField();
    }

    /**
     * Create an instance of {@link DespatchAdviceLogisticUnitLineItem }
     *
     */
    public DespatchAdviceLogisticUnitLineItem createDespatchAdviceLogisticUnitLineItem() {
        return new DespatchAdviceLogisticUnitLineItem();
    }

    /**
     * Create an instance of {@link LineItem.SGBY01 }
     *
     */
    public LineItem.SGBY01 createLineItemSGBY01() {
        return new LineItem.SGBY01();
    }

    /**
     * Create an instance of {@link LineItem.LineItemExtraField }
     *
     */
    public LineItem.LineItemExtraField createLineItemLineItemExtraField() {
        return new LineItem.LineItemExtraField();
    }

    /**
     * Create an instance of {@link DeliveryNote.Total }
     *
     */
    public DeliveryNote.Total createDeliveryNoteTotal() {
        return new DeliveryNote.Total();
    }

    /**
     * Create an instance of {@link BLRDLN }
     *
     */
    public BLRDLN createBLRDLN() {
        return new BLRDLN();
    }

    /**
     * Create an instance of {@link MessageHeader }
     *
     */
    public MessageHeader createMessageHeader() {
        return new MessageHeader();
    }

    /**
     * Create an instance of {@link FreightPayer }
     *
     */
    public FreightPayer createFreightPayer() {
        return new FreightPayer();
    }

    /**
     * Create an instance of {@link Security }
     *
     */
    public Security createSecurity() {
        return new Security();
    }

    /**
     * Create an instance of {@link by.march8.tasks.factoring.model.blrdln.ShipTo }
     *
     */
    public by.march8.tasks.factoring.model.blrdln.ShipTo createShipTo() {
        return new by.march8.tasks.factoring.model.blrdln.ShipTo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "VATRegistrationNumber")
    public JAXBElement<String> createVATRegistrationNumber(String value) {
        return new JAXBElement<String>(_VATRegistrationNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "Address")
    public JAXBElement<String> createAddress(String value) {
        return new JAXBElement<String>(_Address_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "GLN")
    public JAXBElement<String> createGLN(String value) {
        return new JAXBElement<String>(_GLN_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "Name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

}
