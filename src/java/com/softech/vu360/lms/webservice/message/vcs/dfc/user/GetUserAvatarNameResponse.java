
package com.softech.vu360.lms.webservice.message.vcs.dfc.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getUserAvatarNameResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getUserAvatarNameResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="WSResponse" type="{http://www.threesixtytraining.com/phpbbIntUserWS}WSResponse"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUserAvatarNameResponseType", propOrder = {

})
@XmlRootElement(name = "getUserAvatarNameResponse")
public class GetUserAvatarNameResponse {

    @XmlElement(name = "WSResponse", required = true)
    protected WSResponse wsResponse;

    /**
     * Gets the value of the wsResponse property.
     * 
     * @return
     *     possible object is
     *     {@link WSResponse }
     *     
     */
    public WSResponse getWSResponse() {
        return wsResponse;
    }

    /**
     * Sets the value of the wsResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSResponse }
     *     
     */
    public void setWSResponse(WSResponse value) {
        this.wsResponse = value;
    }

}
