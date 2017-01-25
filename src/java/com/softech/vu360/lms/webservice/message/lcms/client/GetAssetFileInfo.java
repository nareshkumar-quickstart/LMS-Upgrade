
package com.softech.vu360.lms.webservice.message.lcms.client;

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
 *         &lt;element name="assetId" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "assetId"
})
@XmlRootElement(name = "GetAssetFileInfo")
public class GetAssetFileInfo {

    protected long assetId;

    /**
     * Gets the value of the assetId property.
     * 
     */
    public long getAssetId() {
        return assetId;
    }

    /**
     * Sets the value of the assetId property.
     * 
     */
    public void setAssetId(long value) {
        this.assetId = value;
    }

}
