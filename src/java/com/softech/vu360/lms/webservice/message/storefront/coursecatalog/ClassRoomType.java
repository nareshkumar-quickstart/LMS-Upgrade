
package com.softech.vu360.lms.webservice.message.storefront.coursecatalog;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ClassRoomType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClassRoomType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CourseStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="EnrollmentCloseDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="TIMEZONE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CourseStartTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="CourseEndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="AdditionalInformation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ClassSize" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="hasSchedules" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SchedulesSession" type="{http://www.threesixtytraining.com/CourseCatalog.xsd}SchedulesSessionType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassRoomType", propOrder = {
    "courseStartDate",
    "enrollmentCloseDate",
    "timezone",
    "courseStartTime",
    "courseEndTime",
    "additionalInformation",
    "classSize",
    "hasSchedules",
    "schedulesSession"
})
public class ClassRoomType {

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
    @XmlElement(name = "AdditionalInformation", required = true)
    protected String additionalInformation;
    @XmlElement(name = "ClassSize")
    protected double classSize;
    protected boolean hasSchedules;
    @XmlElement(name = "SchedulesSession", required = true)
    protected List<SchedulesSessionType> schedulesSession;

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
     * Gets the value of the hasSchedules property.
     * 
     */
    public boolean isHasSchedules() {
        return hasSchedules;
    }

    /**
     * Sets the value of the hasSchedules property.
     * 
     */
    public void setHasSchedules(boolean value) {
        this.hasSchedules = value;
    }

    /**
     * Gets the value of the schedulesSession property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the schedulesSession property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSchedulesSession().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SchedulesSessionType }
     * 
     * 
     */
    public List<SchedulesSessionType> getSchedulesSession() {
        if (schedulesSession == null) {
            schedulesSession = new ArrayList<SchedulesSessionType>();
        }
        return this.schedulesSession;
    }

}
