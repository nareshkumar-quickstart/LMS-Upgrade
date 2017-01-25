
package com.softech.vu360.lms.webservice.message.storefront;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SessionInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SessionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SessionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SessionStartDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SessionEndDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SessionInfo", propOrder = {
    "sessionID",
    "sessionStartDateTime",
    "sessionEndDateTime"
})
public class SessionInfo {

    @XmlElement(name = "SessionID", required = true)
    protected String sessionID;
    @XmlElement(name = "SessionStartDateTime", required = true)
    protected String sessionStartDateTime;
    @XmlElement(name = "SessionEndDateTime", required = true)
    protected String sessionEndDateTime;

    /**
     * Gets the value of the sessionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Sets the value of the sessionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionID(String value) {
        this.sessionID = value;
    }

    /**
     * Gets the value of the sessionStartDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionStartDateTime() {
        return sessionStartDateTime;
    }

    /**
     * Sets the value of the sessionStartDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionStartDateTime(String value) {
        this.sessionStartDateTime = value;
    }

    /**
     * Gets the value of the sessionEndDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionEndDateTime() {
        return sessionEndDateTime;
    }

    /**
     * Sets the value of the sessionEndDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionEndDateTime(String value) {
        this.sessionEndDateTime = value;
    }

}
