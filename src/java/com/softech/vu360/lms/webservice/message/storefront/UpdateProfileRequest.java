
package com.softech.vu360.lms.webservice.message.storefront;

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
 *         &lt;element name="customer" type="{http://www.360training.com/vu360/schemas/lms/sfMessages}Customer"/>
 *         &lt;element name="authenticationCredential" type="{http://www.360training.com/vu360/schemas/lms/sfMessages}AuthenticationCredential"/>
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
    "customer",
    "authenticationCredential"
})
@XmlRootElement(name = "UpdateProfileRequest")
public class UpdateProfileRequest {

    @XmlElement(required = true)
    protected Customer customer;
    @XmlElement(required = true)
    protected AuthenticationCredential authenticationCredential;

    /**
     * Gets the value of the customer property.
     * 
     * @return
     *     possible object is
     *     {@link Customer }
     *     
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the value of the customer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Customer }
     *     
     */
    public void setCustomer(Customer value) {
        this.customer = value;
    }

    /**
     * Gets the value of the authenticationCredential property.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticationCredential }
     *     
     */
    public AuthenticationCredential getAuthenticationCredential() {
        return authenticationCredential;
    }

    /**
     * Sets the value of the authenticationCredential property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticationCredential }
     *     
     */
    public void setAuthenticationCredential(AuthenticationCredential value) {
        this.authenticationCredential = value;
    }

}
