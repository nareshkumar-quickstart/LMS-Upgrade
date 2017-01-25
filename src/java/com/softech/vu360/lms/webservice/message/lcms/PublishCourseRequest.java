
package com.softech.vu360.lms.webservice.message.lcms;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="contentOwnerList" type="{http://www.360training.com/vu360/schemas/lms/lcmsMessages}ContentOwnerVO" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="transactionGUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="eventDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "contentOwnerList"
})
@XmlRootElement(name = "PublishCourseRequest")
public class PublishCourseRequest {

    @XmlElement(required = true)
    protected List<ContentOwnerVO> contentOwnerList;
    @XmlAttribute(required = true)
    protected String transactionGUID;
    @XmlAttribute(required = true)
    protected XMLGregorianCalendar eventDate;

    /**
     * Gets the value of the contentOwnerList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contentOwnerList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContentOwnerList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContentOwnerVO }
     * 
     * 
     */
    public List<ContentOwnerVO> getContentOwnerList() {
        if (contentOwnerList == null) {
            contentOwnerList = new ArrayList<ContentOwnerVO>();
        }
        return this.contentOwnerList;
    }

    /**
     * Gets the value of the transactionGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionGUID() {
        return transactionGUID;
    }

    /**
     * Sets the value of the transactionGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionGUID(String value) {
        this.transactionGUID = value;
    }

    /**
     * Gets the value of the eventDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEventDate() {
        return eventDate;
    }

    /**
     * Sets the value of the eventDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEventDate(XMLGregorianCalendar value) {
        this.eventDate = value;
    }

}
