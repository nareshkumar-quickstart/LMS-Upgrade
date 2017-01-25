
package com.softech.vu360.lms.webservice.message.storefront;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="transactionResult" use="required">
 *         &lt;simpleType>
 *           &lt;list itemType="{http://www.360training.com/vu360/schemas/lms/sfMessages}TransactionResultType" />
 *         &lt;/simpleType>
 *       &lt;/attribute>
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
@XmlRootElement(name = "UpdateProfileResponse")
public class UpdateProfileResponse {

    @XmlAttribute(required = true)
    protected List<TransactionResultType> transactionResult;
    @XmlAttribute
    protected String transactionResultMessage;

    /**
     * Gets the value of the transactionResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transactionResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransactionResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransactionResultType }
     * 
     * 
     */
    public List<TransactionResultType> getTransactionResult() {
        if (transactionResult == null) {
            transactionResult = new ArrayList<TransactionResultType>();
        }
        return this.transactionResult;
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
