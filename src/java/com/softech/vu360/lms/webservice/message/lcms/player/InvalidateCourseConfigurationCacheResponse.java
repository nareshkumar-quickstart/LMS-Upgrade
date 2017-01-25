
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
 *         &lt;element name="InvalidateCourseConfigurationCacheResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "invalidateCourseConfigurationCacheResult"
})
@XmlRootElement(name = "InvalidateCourseConfigurationCacheResponse")
public class InvalidateCourseConfigurationCacheResponse {

    @XmlElement(name = "InvalidateCourseConfigurationCacheResult")
    protected boolean invalidateCourseConfigurationCacheResult;

    /**
     * Gets the value of the invalidateCourseConfigurationCacheResult property.
     * 
     */
    public boolean isInvalidateCourseConfigurationCacheResult() {
        return invalidateCourseConfigurationCacheResult;
    }

    /**
     * Sets the value of the invalidateCourseConfigurationCacheResult property.
     * 
     */
    public void setInvalidateCourseConfigurationCacheResult(boolean value) {
        this.invalidateCourseConfigurationCacheResult = value;
    }

}
