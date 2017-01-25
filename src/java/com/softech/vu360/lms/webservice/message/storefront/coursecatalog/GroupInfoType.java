
package com.softech.vu360.lms.webservice.message.storefront.coursecatalog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GroupInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GroupInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GroupID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GroupName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ParentGroupID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ContainsCourse" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroupInfoType", propOrder = {
    "groupID",
    "groupName",
    "parentGroupID",
    "containsCourse"
})
public class GroupInfoType {

    @XmlElement(name = "GroupID", required = true)
    protected String groupID;
    @XmlElement(name = "GroupName", required = true)
    protected String groupName;
    @XmlElement(name = "ParentGroupID", required = true)
    protected String parentGroupID;
    @XmlElement(name = "ContainsCourse")
    protected boolean containsCourse;

    /**
     * Gets the value of the groupID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupID() {
        return groupID;
    }

    /**
     * Sets the value of the groupID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupID(String value) {
        this.groupID = value;
    }

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the parentGroupID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentGroupID() {
        return parentGroupID;
    }

    /**
     * Sets the value of the parentGroupID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentGroupID(String value) {
        this.parentGroupID = value;
    }

    /**
     * Gets the value of the containsCourse property.
     * 
     */
    public boolean isContainsCourse() {
        return containsCourse;
    }

    /**
     * Sets the value of the containsCourse property.
     * 
     */
    public void setContainsCourse(boolean value) {
        this.containsCourse = value;
    }

}
