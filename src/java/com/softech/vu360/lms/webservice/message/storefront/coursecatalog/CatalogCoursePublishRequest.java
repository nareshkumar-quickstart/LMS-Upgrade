
package com.softech.vu360.lms.webservice.message.storefront.coursecatalog;

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
 *         &lt;element name="GUID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="STOREID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Course" type="{http://www.threesixtytraining.com/CourseCatalog.xsd}CourseType"/>
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
    "guid",
    "storeid",
    "course"
})
@XmlRootElement(name = "CatalogCoursePublishRequest")
public class CatalogCoursePublishRequest {

    @XmlElement(name = "GUID", required = true)
    protected String guid;
    @XmlElement(name = "STOREID", required = true)
    protected String storeid;
    @XmlElement(name = "Course", required = true)
    protected CourseType course;

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUID() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUID(String value) {
        this.guid = value;
    }

    /**
     * Gets the value of the storeid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTOREID() {
        return storeid;
    }

    /**
     * Sets the value of the storeid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTOREID(String value) {
        this.storeid = value;
    }

    /**
     * Gets the value of the course property.
     * 
     * @return
     *     possible object is
     *     {@link CourseType }
     *     
     */
    public CourseType getCourse() {
        return course;
    }

    /**
     * Sets the value of the course property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourseType }
     *     
     */
    public void setCourse(CourseType value) {
        this.course = value;
    }

}
