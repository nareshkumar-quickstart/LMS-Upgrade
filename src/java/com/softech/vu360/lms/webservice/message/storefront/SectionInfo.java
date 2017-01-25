
package com.softech.vu360.lms.webservice.message.storefront;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SectionInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SectionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sectionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sectionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sectionStartDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sectionEndDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="availableSeats" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="sectionStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sectionEnrollmentCloseDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sectionMeetingType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sessions" type="{http://www.360training.com/vu360/schemas/lms/sfMessages}SessionInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="instructors" type="{http://www.360training.com/vu360/schemas/lms/sfMessages}InstructorInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SectionInfo", propOrder = {
    "sectionID",
    "sectionName",
    "sectionStartDate",
    "sectionEndDate",
    "availableSeats",
    "sectionStatus",
    "sectionEnrollmentCloseDate",
    "sectionMeetingType",
    "sessions",
    "instructors"
})
public class SectionInfo {

    @XmlElement(required = true)
    protected String sectionID;
    @XmlElement(required = true)
    protected String sectionName;
    @XmlElement(required = true)
    protected String sectionStartDate;
    @XmlElement(required = true)
    protected String sectionEndDate;
    @XmlElement(required = true)
    protected BigInteger availableSeats;
    @XmlElement(required = true)
    protected String sectionStatus;
    @XmlElement(required = true)
    protected String sectionEnrollmentCloseDate;
    @XmlElement(required = true)
    protected String sectionMeetingType;
    protected List<SessionInfo> sessions;
    protected List<InstructorInfo> instructors;

    /**
     * Gets the value of the sectionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectionID() {
        return sectionID;
    }

    /**
     * Sets the value of the sectionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectionID(String value) {
        this.sectionID = value;
    }

    /**
     * Gets the value of the sectionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the value of the sectionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectionName(String value) {
        this.sectionName = value;
    }

    /**
     * Gets the value of the sectionStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectionStartDate() {
        return sectionStartDate;
    }

    /**
     * Sets the value of the sectionStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectionStartDate(String value) {
        this.sectionStartDate = value;
    }

    /**
     * Gets the value of the sectionEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectionEndDate() {
        return sectionEndDate;
    }

    /**
     * Sets the value of the sectionEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectionEndDate(String value) {
        this.sectionEndDate = value;
    }

    /**
     * Gets the value of the availableSeats property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAvailableSeats() {
        return availableSeats;
    }

    /**
     * Sets the value of the availableSeats property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAvailableSeats(BigInteger value) {
        this.availableSeats = value;
    }

    /**
     * Gets the value of the sectionStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectionStatus() {
        return sectionStatus;
    }

    /**
     * Sets the value of the sectionStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectionStatus(String value) {
        this.sectionStatus = value;
    }

    /**
     * Gets the value of the sectionEnrollmentCloseDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectionEnrollmentCloseDate() {
        return sectionEnrollmentCloseDate;
    }

    /**
     * Sets the value of the sectionEnrollmentCloseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectionEnrollmentCloseDate(String value) {
        this.sectionEnrollmentCloseDate = value;
    }

    /**
     * Gets the value of the sectionMeetingType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectionMeetingType() {
        return sectionMeetingType;
    }

    /**
     * Sets the value of the sectionMeetingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectionMeetingType(String value) {
        this.sectionMeetingType = value;
    }

    /**
     * Gets the value of the sessions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sessions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSessions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SessionInfo }
     * 
     * 
     */
    public List<SessionInfo> getSessions() {
        if (sessions == null) {
            sessions = new ArrayList<SessionInfo>();
        }
        return this.sessions;
    }

    /**
     * Gets the value of the instructors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instructors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstructors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstructorInfo }
     * 
     * 
     */
    public List<InstructorInfo> getInstructors() {
        if (instructors == null) {
            instructors = new ArrayList<InstructorInfo>();
        }
        return this.instructors;
    }

}
