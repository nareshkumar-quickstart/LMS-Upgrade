
package com.softech.vu360.lms.webservice.message.lcms.player;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="InvalidateBrandCacheResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "invalidateBrandCacheResult"
})
@XmlRootElement(name = "InvalidateBrandCacheResponse")
public class InvalidateBrandCacheResponse {

    @XmlElement(name = "InvalidateBrandCacheResult")
    protected boolean invalidateBrandCacheResult;

    /**
     * Gets the value of the invalidateBrandCacheResult property.
     * 
     */
    public boolean isInvalidateBrandCacheResult() {
        return invalidateBrandCacheResult;
    }

    /**
     * Sets the value of the invalidateBrandCacheResult property.
     * 
     */
    public void setInvalidateBrandCacheResult(boolean value) {
        this.invalidateBrandCacheResult = value;
    }

}
