
package com.softech.vu360.lms.webservice.message.storefront;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InstructorInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InstructorInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InstructorID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstructorFirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstructorLastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstructorRole" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstructorInfo", propOrder = {
    "instructorID",
    "instructorFirstName",
    "instructorLastName",
    "instructorRole"
})
public class InstructorInfo {

    @XmlElement(name = "InstructorID", required = true)
    protected String instructorID;
    @XmlElement(name = "InstructorFirstName", required = true)
    protected String instructorFirstName;
    @XmlElement(name = "InstructorLastName", required = true)
    protected String instructorLastName;
    @XmlElement(name = "InstructorRole", required = true)
    protected String instructorRole;

    /**
     * Gets the value of the instructorID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstructorID() {
        return instructorID;
    }

    /**
     * Sets the value of the instructorID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstructorID(String value) {
        this.instructorID = value;
    }

    /**
     * Gets the value of the instructorFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstructorFirstName() {
        return instructorFirstName;
    }

    /**
     * Sets the value of the instructorFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstructorFirstName(String value) {
        this.instructorFirstName = value;
    }

    /**
     * Gets the value of the instructorLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstructorLastName() {
        return instructorLastName;
    }

    /**
     * Sets the value of the instructorLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstructorLastName(String value) {
        this.instructorLastName = value;
    }

    /**
     * Gets the value of the instructorRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstructorRole() {
        return instructorRole;
    }

    /**
     * Sets the value of the instructorRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstructorRole(String value) {
        this.instructorRole = value;
    }

}
