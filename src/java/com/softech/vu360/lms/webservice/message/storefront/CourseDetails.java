
package com.softech.vu360.lms.webservice.message.storefront;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CourseDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CourseDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="courseGUID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="synchronousClass" type="{http://www.360training.com/vu360/schemas/lms/sfMessages}SectionInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CourseDetails", propOrder = {
    "courseGUID",
    "synchronousClass"
})
public class CourseDetails {

    @XmlElement(required = true)
    protected String courseGUID;
    protected List<SectionInfo> synchronousClass;

    /**
     * Gets the value of the courseGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseGUID() {
        return courseGUID;
    }

    /**
     * Sets the value of the courseGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseGUID(String value) {
        this.courseGUID = value;
    }

    /**
     * Gets the value of the synchronousClass property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the synchronousClass property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSynchronousClass().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SectionInfo }
     * 
     * 
     */
    public List<SectionInfo> getSynchronousClass() {
        if (synchronousClass == null) {
            synchronousClass = new ArrayList<SectionInfo>();
        }
        return this.synchronousClass;
    }

}
