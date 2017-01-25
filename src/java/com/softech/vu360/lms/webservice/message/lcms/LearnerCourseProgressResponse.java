package com.softech.vu360.lms.webservice.message.lcms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="transactionGUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="eventDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="transactionResult" use="required" type="{http://www.360training.com/vu360/schemas/lms/lcmsMessages}TransactionResultType" />
 *       &lt;attribute name="transactionResultMessage" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "LearnerCourseProgressResponse")
public class LearnerCourseProgressResponse {
	@XmlAttribute(required = true)
    protected String transactionGUID;
    @XmlAttribute(required = false)
    protected XMLGregorianCalendar eventDate;
    @XmlAttribute(required = true)
    protected TransactionResultType transactionResult;
    @XmlAttribute
    protected String transactionResultMessage;

    /**
     * Gets the value of the transactionGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionGUID() {
        return transactionGUID;
    }

    /**
     * Sets the value of the transactionGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionGUID(String value) {
        this.transactionGUID = value;
    }

    /**
     * Gets the value of the eventDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEventDate() {
        return eventDate;
    }

    /**
     * Sets the value of the eventDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEventDate(XMLGregorianCalendar value) {
        this.eventDate = value;
    }

    /**
     * Gets the value of the transactionResult property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionResultType }
     *     
     */
    public TransactionResultType getTransactionResult() {
        return transactionResult;
    }

    /**
     * Sets the value of the transactionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionResultType }
     *     
     */
    public void setTransactionResult(TransactionResultType value) {
        this.transactionResult = value;
    }

    /**
     * Gets the value of the transactionResultMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionResultMessage() {
        return transactionResultMessage;
    }

    /**
     * Sets the value of the transactionResultMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionResultMessage(String value) {
        this.transactionResultMessage = value;
    }

	



}
