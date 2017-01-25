
package com.softech.vu360.lms.webservice.message.lcms.player;

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
 *         &lt;element name="courseApprovalID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="courseID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "courseApprovalID",
    "courseID"
})
@XmlRootElement(name = "InvalidateCourseApprovalCache")
public class InvalidateCourseApprovalCache {

    protected int courseApprovalID;
    protected int courseID;

    /**
     * Gets the value of the courseApprovalID property.
     * 
     */
    public int getCourseApprovalID() {
        return courseApprovalID;
    }

    /**
     * Sets the value of the courseApprovalID property.
     * 
     */
    public void setCourseApprovalID(int value) {
        this.courseApprovalID = value;
    }

    /**
     * Gets the value of the courseID property.
     * 
     */
    public int getCourseID() {
        return courseID;
    }

    /**
     * Sets the value of the courseID property.
     * 
     */
    public void setCourseID(int value) {
        this.courseID = value;
    }

}
