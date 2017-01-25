
package com.softech.vu360.lms.webservice.message.lcms.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.softech.vu360.lms.webservice.message.lcms.ArrayOfLong;


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
 *         &lt;element name="assetIds" type="{http://360training.com/}ArrayOfLong" minOccurs="0"/>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="loggedInUserID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "assetIds",
    "active",
    "loggedInUserID"
})
@XmlRootElement(name = "UpdateAssetStatus")
public class UpdateAssetStatus {

    protected ArrayOfLong assetIds;
    protected boolean active;
    protected int loggedInUserID;

    /**
     * Gets the value of the assetIds property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfLong }
     *     
     */
    public ArrayOfLong getAssetIds() {
        return assetIds;
    }

    /**
     * Sets the value of the assetIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfLong }
     *     
     */
    public void setAssetIds(ArrayOfLong value) {
        this.assetIds = value;
    }

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the loggedInUserID property.
     * 
     */
    public int getLoggedInUserID() {
        return loggedInUserID;
    }

    /**
     * Sets the value of the loggedInUserID property.
     * 
     */
    public void setLoggedInUserID(int value) {
        this.loggedInUserID = value;
    }

}
