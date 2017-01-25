
package com.softech.vu360.lms.webservice.message.integration;

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
 *       &lt;attribute name="transactionGUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="customerGUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="userName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="fileLocation" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountLocked" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="visibleOnReport" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="notifyLearnerOnRegistration" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="actionOnDuplicateRecords" use="required">
 *         &lt;simpleType>
 *           &lt;list itemType="{http://www.360training.com/vu360/schemas/lms/integrationInterface}ActionOnDuplicateRecords" />
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "BatchImportLearnerRequest")
public class BatchImportLearnerRequest {

    @XmlAttribute(required = true)
    protected String transactionGUID;
    @XmlAttribute(required = true)
    protected String customerGUID;
    @XmlAttribute(required = true)
    protected String userName;
    @XmlAttribute(required = true)
    protected String fileLocation;
    @XmlAttribute(required = true)
    protected boolean accountLocked;
    @XmlAttribute(required = true)
    protected boolean visibleOnReport;
    @XmlAttribute(required = true)
    protected boolean notifyLearnerOnRegistration;
    @XmlAttribute(required = true)
    protected List<ActionOnDuplicateRecords> actionOnDuplicateRecords;

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
     * Gets the value of the customerGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerGUID() {
        return customerGUID;
    }

    /**
     * Sets the value of the customerGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerGUID(String value) {
        this.customerGUID = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the fileLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * Sets the value of the fileLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileLocation(String value) {
        this.fileLocation = value;
    }

    /**
     * Gets the value of the accountLocked property.
     * 
     */
    public boolean isAccountLocked() {
        return accountLocked;
    }

    /**
     * Sets the value of the accountLocked property.
     * 
     */
    public void setAccountLocked(boolean value) {
        this.accountLocked = value;
    }

    /**
     * Gets the value of the visibleOnReport property.
     * 
     */
    public boolean isVisibleOnReport() {
        return visibleOnReport;
    }

    /**
     * Sets the value of the visibleOnReport property.
     * 
     */
    public void setVisibleOnReport(boolean value) {
        this.visibleOnReport = value;
    }

    /**
     * Gets the value of the notifyLearnerOnRegistration property.
     * 
     */
    public boolean isNotifyLearnerOnRegistration() {
        return notifyLearnerOnRegistration;
    }

    /**
     * Sets the value of the notifyLearnerOnRegistration property.
     * 
     */
    public void setNotifyLearnerOnRegistration(boolean value) {
        this.notifyLearnerOnRegistration = value;
    }

    /**
     * Gets the value of the actionOnDuplicateRecords property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actionOnDuplicateRecords property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActionOnDuplicateRecords().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActionOnDuplicateRecords }
     * 
     * 
     */
    public List<ActionOnDuplicateRecords> getActionOnDuplicateRecords() {
        if (actionOnDuplicateRecords == null) {
            actionOnDuplicateRecords = new ArrayList<ActionOnDuplicateRecords>();
        }
        return this.actionOnDuplicateRecords;
    }

}
