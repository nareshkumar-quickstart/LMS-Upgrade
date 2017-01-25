
package com.softech.vu360.lms.webservice.message.lcms.client;

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
 *         &lt;element name="GetAssetFileInfoResult" type="{http://360training.com/}ArrayOfString" minOccurs="0"/>
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
    "getAssetFileInfoResult"
})
@XmlRootElement(name = "GetAssetFileInfoResponse")
public class GetAssetFileInfoResponse {

    @XmlElement(name = "GetAssetFileInfoResult")
    protected ArrayOfString getAssetFileInfoResult;

    /**
     * Gets the value of the getAssetFileInfoResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getGetAssetFileInfoResult() {
        return getAssetFileInfoResult;
    }

    /**
     * Sets the value of the getAssetFileInfoResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setGetAssetFileInfoResult(ArrayOfString value) {
        this.getAssetFileInfoResult = value;
    }

}
