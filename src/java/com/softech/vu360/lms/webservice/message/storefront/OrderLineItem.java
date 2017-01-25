
package com.softech.vu360.lms.webservice.message.storefront;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderLineItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderLineItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="groupguid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="guid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lineitemguid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="quantity" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="termOfService" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="subscriptioncode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="subscriptiontype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="subscriptionname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderLineItem")
public class OrderLineItem {

    @XmlAttribute
    protected String groupguid;
    @XmlAttribute(required = true)
    protected String guid;
    @XmlAttribute
    protected String lineitemguid;
    @XmlAttribute(required = true)
    protected BigInteger quantity;
    @XmlAttribute(required = true)
    protected BigInteger termOfService;
    @XmlAttribute
    protected String subscriptioncode;
    @XmlAttribute
    protected String subscriptiontype;
    @XmlAttribute
    protected String subscriptionname;
    
    
	/**
     * Gets the value of the groupguid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupguid() {
        return groupguid;
    }

    /**
     * Sets the value of the groupguid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupguid(String value) {
        this.groupguid = value;
    }

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
    }

    /**
     * Gets the value of the lineitemguid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineitemguid() {
        return lineitemguid;
    }

    /**
     * Sets the value of the lineitemguid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineitemguid(String value) {
        this.lineitemguid = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQuantity(BigInteger value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the termOfService property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTermOfService() {
        return termOfService;
    }

    /**
     * Sets the value of the termOfService property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTermOfService(BigInteger value) {
        this.termOfService = value;
    }
    
    
    public String getSubscriptioncode() {
		return subscriptioncode;
	}

	public void setSubscriptioncode(String subscriptioncode) {
		this.subscriptioncode = subscriptioncode;
	}

	public String getSubscriptiontype() {
		return subscriptiontype;
	}

	public void setSubscriptiontype(String subscriptiontype) {
		this.subscriptiontype = subscriptiontype;
	}

	public String getSubscriptionname() {
		return subscriptionname;
	}

	public void setSubscriptionname(String subscriptionname) {
		this.subscriptionname = subscriptionname;
	}


}
