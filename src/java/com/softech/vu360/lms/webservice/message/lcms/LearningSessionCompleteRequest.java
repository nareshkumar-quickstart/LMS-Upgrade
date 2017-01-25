
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
 *       &lt;attribute name="learningSessionId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="courseCompleted" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="certificateURL" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "LearningSessionCompleteRequest")
public class LearningSessionCompleteRequest {

    @XmlAttribute(required = true)
    protected String transactionGUID;
    @XmlAttribute
    protected XMLGregorianCalendar eventDate;
    @XmlAttribute(required = true)
    protected String learningSessionId;
    @XmlAttribute
    protected Boolean courseCompleted;
    @XmlAttribute
    protected String certificateURL;

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
     * Gets the value of the learningSessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLearningSessionId() {
        return learningSessionId;
    }

    /**
     * Sets the value of the learningSessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLearningSessionId(String value) {
        this.learningSessionId = value;
    }

    /**
     * Gets the value of the courseCompleted property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCourseCompleted() {
        return courseCompleted;
    }

    /**
     * Sets the value of the courseCompleted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCourseCompleted(Boolean value) {
        this.courseCompleted = value;
    }

    /**
     * Gets the value of the certificateURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateURL() {
        return certificateURL;
    }

    /**
     * Sets the value of the certificateURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateURL(String value) {
        this.certificateURL = value;
    }

}
