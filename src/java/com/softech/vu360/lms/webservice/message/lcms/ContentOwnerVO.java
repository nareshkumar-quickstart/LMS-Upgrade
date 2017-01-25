
package com.softech.vu360.lms.webservice.message.lcms;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContentOwnerVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContentOwnerVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="courseList" type="{http://www.360training.com/vu360/schemas/lms/lcmsMessages}CourseVO" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="contentOwnerGUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContentOwnerVO", propOrder = {
    "courseList"
})
public class ContentOwnerVO {

    @XmlElement(required = true)
    protected List<CourseVO> courseList;
    @XmlAttribute(required = true)
    protected String contentOwnerGUID;

    /**
     * Gets the value of the courseList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courseList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourseList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourseVO }
     * 
     * 
     */
    public List<CourseVO> getCourseList() {
        if (courseList == null) {
            courseList = new ArrayList<CourseVO>();
        }
        return this.courseList;
    }

    /**
     * Gets the value of the contentOwnerGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentOwnerGUID() {
        return contentOwnerGUID;
    }

    /**
     * Sets the value of the contentOwnerGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentOwnerGUID(String value) {
        this.contentOwnerGUID = value;
    }

}
