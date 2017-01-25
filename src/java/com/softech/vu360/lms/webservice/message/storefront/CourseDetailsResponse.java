
package com.softech.vu360.lms.webservice.message.storefront;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="coursePublishInfo" type="{http://www.360training.com/vu360/schemas/lms/sfMessages}CourseDetails" maxOccurs="unbounded" minOccurs="0"/>
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
    "coursePublishInfo"
})
@XmlRootElement(name = "CourseDetailsResponse")
public class CourseDetailsResponse {

    protected List<CourseDetails> coursePublishInfo;

    /**
     * Gets the value of the coursePublishInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coursePublishInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoursePublishInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourseDetails }
     * 
     * 
     */
    public List<CourseDetails> getCoursePublishInfo() {
        if (coursePublishInfo == null) {
            coursePublishInfo = new ArrayList<CourseDetails>();
        }
        return this.coursePublishInfo;
    }

}
