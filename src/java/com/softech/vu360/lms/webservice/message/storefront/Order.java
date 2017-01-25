
package com.softech.vu360.lms.webservice.message.storefront;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Order complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Order">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customer" type="{http://www.360training.com/vu360/schemas/lms/sfMessages}Customer"/>
 *         &lt;element name="lineItem" type="{http://www.360training.com/vu360/schemas/lms/sfMessages}OrderLineItem" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="orderId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="orderDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="distributorId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="system" type="{http://www.w3.org/2001/XMLSchema}string" default="VU360-SF" />
 *       &lt;attribute name="issubscription" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Order", propOrder = {
    "customer",
    "lineItem"
})
public class Order {

  

	@XmlElement(required = true)
    protected Customer customer;
    @XmlElement(required = true)
    protected List<OrderLineItem> lineItem;
    @XmlAttribute(required = true)
    protected String orderId;
    @XmlAttribute(required = true)
    protected XMLGregorianCalendar orderDate;
    @XmlAttribute(required = true)
    protected String distributorId;
    @XmlAttribute
    protected String system;
    @XmlAttribute
    protected boolean issubscription;
    
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
     * Gets the value of the lineItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lineItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLineItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrderLineItem }
     * 
     * 
     */
    public List<OrderLineItem> getLineItem() {
        if (lineItem == null) {
            lineItem = new ArrayList<OrderLineItem>();
        }
        return this.lineItem;
    }

    public void setLineItem(List<OrderLineItem> lineItem) {
		this.lineItem = lineItem;
	}

	/**
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the orderDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the value of the orderDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOrderDate(XMLGregorianCalendar value) {
        this.orderDate = value;
    }

    /**
     * Gets the value of the distributorId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistributorId() {
        return distributorId;
    }

    /**
     * Sets the value of the distributorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistributorId(String value) {
        this.distributorId = value;
    }

    /**
     * Gets the value of the system property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystem() {
        if (system == null) {
            return "VU360-SF";
        } else {
            return system;
        }
    }

    /**
     * Sets the value of the system property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystem(String value) {
        this.system = value;
    }

    public boolean isIssubscription() {
  		return issubscription;
  	}

  	public void setIssubscription(boolean issubscription) {
  		this.issubscription = issubscription;
  	}
    
}
