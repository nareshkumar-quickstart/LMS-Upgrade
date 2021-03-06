//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 01:43:13 PM PKT 
//


package com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.DuplicatesEnrollment;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerCourses;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LearnerCourses" type="{http://enrollment.types.lmsapi.message.webservice.lms.vu360.softech.com}LearnerCourses" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="customerCode" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="notifyLearnersByEmail" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "learnerCourses"
})
@XmlRootElement(name = "LearnerCoursesEnrollRequest")
public class LearnerCoursesEnrollRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "LearnerCourses", required = true)
    protected List<LearnerCourses> learnerCourses;
    @XmlAttribute(name = "customerCode", required = true)
    protected String customerCode;
    @XmlAttribute(name = "key", required = true)
    protected String key;
    @XmlAttribute(name = "notifyLearnersByEmail")
    protected Boolean notifyLearnersByEmail;
    @XmlAttribute(name = "duplicatesEnrollment")
	protected DuplicatesEnrollment duplicatesEnrollment;

    /**
     * Gets the value of the learnerCourses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the learnerCourses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLearnerCourses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LearnerCourses }
     * 
     * 
     */
    public List<LearnerCourses> getLearnerCourses() {
        if (learnerCourses == null) {
            learnerCourses = new ArrayList<LearnerCourses>();
        }
        return this.learnerCourses;
    }

    /**
     * Gets the value of the customerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * Sets the value of the customerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerCode(String value) {
        this.customerCode = value;
    }

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the notifyLearnersByEmail property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isNotifyLearnersByEmail() {
        if (notifyLearnersByEmail == null) {
            return false;
        } else {
            return notifyLearnersByEmail;
        }
    }

    /**
     * Sets the value of the notifyLearnersByEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNotifyLearnersByEmail(Boolean value) {
        this.notifyLearnersByEmail = value;
    }
    
    /**
	 * Gets the value of the duplicatesEnrollment property.
	 * 
	 * @return possible object is {@link DuplicatesEnrollment }
	 * 
	 */
	public DuplicatesEnrollment getDuplicatesEnrollment() {
		if (duplicatesEnrollment == null) {
			return DuplicatesEnrollment.UPDATE;
		} else {
			return duplicatesEnrollment;
		}
	}
	
	/**
	 * Sets the value of the duplicatesEnrollment property.
	 * 
	 * @param value
	 *            allowed object is {@link DuplicatesEnrollment }
	 * 
	 */
	public void setDuplicatesEnrollment(DuplicatesEnrollment value) {
		this.duplicatesEnrollment = value;
	}

}
