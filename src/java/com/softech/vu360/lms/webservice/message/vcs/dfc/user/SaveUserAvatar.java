
package com.softech.vu360.lms.webservice.message.vcs.dfc.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saveUserAvatarRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveUserAvatarRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="userGUID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avatar" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avatarWidth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="avatarHeight" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveUserAvatarRequestType", propOrder = {

})
@XmlRootElement(name = "saveUserAvatar")
public class SaveUserAvatar {

    @XmlElement(required = true)
    protected String userGUID;
    @XmlElement(required = true)
    protected String avatar;
    protected int avatarWidth;
    protected int avatarHeight;

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
     * Gets the value of the avatar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Sets the value of the avatar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvatar(String value) {
        this.avatar = value;
    }

    /**
     * Gets the value of the avatarWidth property.
     * 
     */
    public int getAvatarWidth() {
        return avatarWidth;
    }

    /**
     * Sets the value of the avatarWidth property.
     * 
     */
    public void setAvatarWidth(int value) {
        this.avatarWidth = value;
    }

    /**
     * Gets the value of the avatarHeight property.
     * 
     */
    public int getAvatarHeight() {
        return avatarHeight;
    }

    /**
     * Sets the value of the avatarHeight property.
     * 
     */
    public void setAvatarHeight(int value) {
        this.avatarHeight = value;
    }

}
