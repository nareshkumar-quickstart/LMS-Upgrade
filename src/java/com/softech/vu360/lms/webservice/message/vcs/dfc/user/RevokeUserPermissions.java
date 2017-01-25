
package com.softech.vu360.lms.webservice.message.vcs.dfc.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for revokeUserPermissionsRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="revokeUserPermissionsRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="userGUID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="forumGUID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "revokeUserPermissionsRequestType", propOrder = {

})
@XmlRootElement(name = "revokeUserPermissions")
public class RevokeUserPermissions {

    @XmlElement(required = true)
    protected String userGUID;
    @XmlElement(required = true)
    protected String userType;
    @XmlElement(required = true)
    protected String forumGUID;

    /**
     * Gets the value of the userGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserGUID() {
        return userGUID;
    }

    /**
     * Sets the value of the userGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserGUID(String value) {
        this.userGUID = value;
    }

    /**
     * Gets the value of the userType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Sets the value of the userType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserType(String value) {
        this.userType = value;
    }

    /**
     * Gets the value of the forumGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForumGUID() {
        return forumGUID;
    }

    /**
     * Sets the value of the forumGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForumGUID(String value) {
        this.forumGUID = value;
    }

}
