
package com.softech.vu360.lms.webservice.message.storefront.coursecatalog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ClassRoomWebinarType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClassRoomWebinarType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CourseStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="EnrollmentCloseDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="TIMEZONE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CourseStartTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="CourseEndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="WebinarServiceProvider" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MeetingURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DialNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AccessCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AdditionalInformation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Address" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Location" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ClassSize" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Country" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZipCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassRoomWebinarType", propOrder = {
    "courseStartDate",
    "enrollmentCloseDate",
    "timezone",
    "courseStartTime",
    "courseEndTime",
    "webinarServiceProvider",
    "meetingURL",
    "dialNumber",
    "accessCode",
    "additionalInformation",
    "address",
    "description",
    "location",
    "classSize",
    "city",
    "state",
    "country",
    "zipCode"
})
public class ClassRoomWebinarType {

    @XmlElement(name = "CourseStartDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar courseStartDate;
    @XmlElement(name = "EnrollmentCloseDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar enrollmentCloseDate;
    @XmlElement(name = "TIMEZONE", required = true)
    protected String timezone;
    @XmlElement(name = "CourseStartTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar courseStartTime;
    @XmlElement(name = "CourseEndTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar courseEndTime;
    @XmlElement(name = "WebinarServiceProvider", required = true)
    protected String webinarServiceProvider;
    @XmlElement(name = "MeetingURL", required = true)
    protected String meetingURL;
    @XmlElement(name = "DialNumber", required = true)
    protected String dialNumber;
    @XmlElement(name = "AccessCode", required = true)
    protected String accessCode;
    @XmlElement(name = "AdditionalInformation", required = true)
    protected String additionalInformation;
    @XmlElement(name = "Address", required = true)
    protected String address;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "Location", required = true)
    protected String location;
    @XmlElement(name = "ClassSize")
    protected double classSize;
    @XmlElement(name = "City", required = true)
    protected String city;
    @XmlElement(name = "State", required = true)
    protected String state;
    @XmlElement(name = "Country", required = true)
    protected String country;
    @XmlElement(name = "ZipCode", required = true)
    protected String zipCode;

    /**
     * Gets the value of the courseStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCourseStartDate() {
        return courseStartDate;
    }

    /**
     * Sets the value of the courseStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCourseStartDate(XMLGregorianCalendar value) {
        this.courseStartDate = value;
    }

    /**
     * Gets the value of the enrollmentCloseDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEnrollmentCloseDate() {
        return enrollmentCloseDate;
    }

    /**
     * Sets the value of the enrollmentCloseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEnrollmentCloseDate(XMLGregorianCalendar value) {
        this.enrollmentCloseDate = value;
    }

    /**
     * Gets the value of the timezone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIMEZONE() {
        return timezone;
    }

    /**
     * Sets the value of the timezone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIMEZONE(String value) {
        this.timezone = value;
    }

    /**
     * Gets the value of the courseStartTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCourseStartTime() {
        return courseStartTime;
    }

    /**
     * Sets the value of the courseStartTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCourseStartTime(XMLGregorianCalendar value) {
        this.courseStartTime = value;
    }

    /**
     * Gets the value of the courseEndTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCourseEndTime() {
        return courseEndTime;
    }

    /**
     * Sets the value of the courseEndTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCourseEndTime(XMLGregorianCalendar value) {
        this.courseEndTime = value;
    }

    /**
     * Gets the value of the webinarServiceProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebinarServiceProvider() {
        return webinarServiceProvider;
    }

    /**
     * Sets the value of the webinarServiceProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebinarServiceProvider(String value) {
        this.webinarServiceProvider = value;
    }

    /**
     * Gets the value of the meetingURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeetingURL() {
        return meetingURL;
    }

    /**
     * Sets the value of the meetingURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeetingURL(String value) {
        this.meetingURL = value;
    }

    /**
     * Gets the value of the dialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDialNumber() {
        return dialNumber;
    }

    /**
     * Sets the value of the dialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDialNumber(String value) {
        this.dialNumber = value;
    }

    /**
     * Gets the value of the accessCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessCode() {
        return accessCode;
    }

    /**
     * Sets the value of the accessCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessCode(String value) {
        this.accessCode = value;
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
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the classSize property.
     * 
     */
    public double getClassSize() {
        return classSize;
    }

    /**
     * Sets the value of the classSize property.
     * 
     */
    public void setClassSize(double value) {
        this.classSize = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the zipCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the value of the zipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZipCode(String value) {
        this.zipCode = value;
    }

}
