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
 *         &lt;element name="MessageID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="14"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MsgDateTime">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MessageType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="6"/>
 *               &lt;enumeration value="BLRWBL"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MsgSenderID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="13"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MsgReceiverID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="13"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TestIndicator" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="1"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UserID" minOccurs="0">
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
        "messageID",
        "msgDateTime",
        "messageType",
        "msgSenderID",
        "msgReceiverID",
        "testIndicator",
        "userID"
})
@XmlRootElement(name = "MessageHeader")
public class MessageHeader {

    @XmlElement(name = "MessageID", required = true)
    protected String messageID;
    @XmlElement(name = "MsgDateTime", required = true)
    protected String msgDateTime;
    @XmlElement(name = "MessageType", required = true)
    protected String messageType;
    @XmlElement(name = "MsgSenderID", required = true)
    protected String msgSenderID;
    @XmlElement(name = "MsgReceiverID", required = true)
    protected String msgReceiverID;
    @XmlElement(name = "TestIndicator")
    protected String testIndicator;
    @XmlElement(name = "UserID")
    protected String userID;

    /**
     * Gets the value of the messageID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessageID(String value) {
        this.messageID = value;
    }

    /**
     * Gets the value of the msgDateTime property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMsgDateTime() {
        return msgDateTime;
    }

    /**
     * Sets the value of the msgDateTime property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMsgDateTime(String value) {
        this.msgDateTime = value;
    }

    /**
     * Gets the value of the messageType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }

    /**
     * Gets the value of the msgSenderID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMsgSenderID() {
        return msgSenderID;
    }

    /**
     * Sets the value of the msgSenderID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMsgSenderID(String value) {
        this.msgSenderID = value;
    }

    /**
     * Gets the value of the msgReceiverID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMsgReceiverID() {
        return msgReceiverID;
    }

    /**
     * Sets the value of the msgReceiverID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMsgReceiverID(String value) {
        this.msgReceiverID = value;
    }

    /**
     * Gets the value of the testIndicator property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTestIndicator() {
        return testIndicator;
    }

    /**
     * Sets the value of the testIndicator property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTestIndicator(String value) {
        this.testIndicator = value;
    }

    /**
     * Gets the value of the userID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUserID(String value) {
        this.userID = value;
    }

}
